/*
 * Project Name: kmfex-platform
 * File Name: CancelPkgService.java
 * Class Name: CancelPkgService
 *
 * Copyright 2014 Hengtian Software Inc
 *
 * Licensed under the Hengtiansoft
 *
 * http://www.hengtiansoft.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hengxin.platform.product.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.batch.service.JobWorkService;
import com.hengxin.platform.common.entity.id.IdUtil;
import com.hengxin.platform.common.enums.ActionResult;
import com.hengxin.platform.common.enums.ActionType;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.enums.EntityType;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.service.ActionHistoryService;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.fund.dto.biz.req.atom.DedicatedTransferInfo;
import com.hengxin.platform.fund.dto.biz.req.atom.TransferInfo;
import com.hengxin.platform.fund.dto.biz.req.atom.UnReserveReq;
import com.hengxin.platform.fund.entity.AcctPo;
import com.hengxin.platform.fund.enums.EFundUseType;
import com.hengxin.platform.fund.exception.AvlBalNotEnoughException;
import com.hengxin.platform.fund.service.AcctService;
import com.hengxin.platform.fund.service.FinancierRevokeFundService;
import com.hengxin.platform.fund.service.FinancierSigningService;
import com.hengxin.platform.fund.service.FreezeReserveDtlService;
import com.hengxin.platform.fund.service.FundAcctBalService;
import com.hengxin.platform.fund.service.WarrantDepositService;
import com.hengxin.platform.fund.util.AmtUtils;
import com.hengxin.platform.product.domain.PackageSubscribes;
import com.hengxin.platform.product.domain.Product;
import com.hengxin.platform.product.domain.ProductPackage;
import com.hengxin.platform.product.enums.EPackageStatus;
import com.hengxin.platform.product.exception.PkgTerminateOrCancelInMarketOpenException;
import com.hengxin.platform.product.repository.PackageSubscribesRepository;
import com.hengxin.platform.product.repository.ProductPackageRepository;
import com.hengxin.platform.product.repository.ProductRepository;
import com.hengxin.platform.product.service.MsgHolder.CanclePkgMsgHolder;
import com.hengxin.platform.product.service.MsgHolder.PkgUtils;

/**
 * 融资包撤单服务 Class Name: CancelPkgService
 * 
 * @author tingwang
 * 
 */
@Service
public class CancelPkgService {

    @Autowired
    ProductPackageRepository productPackageRepository;
    @Autowired
    ActionHistoryService actionHistoryService;
    @Autowired
    FinancierRevokeFundService financierRevokeFundService;
    @Autowired
    FreezeReserveDtlService freezeReserveDtlService;
    @Autowired
    PackageSubscribesRepository packageSubscribesRepository;
    @Autowired
    WarrantDepositService warrantDepositService;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    FundAcctBalService fundAcctBalService;
    @Autowired
    FinancierSigningService financierSigningService;
    @Autowired
    JobWorkService jobWorkService;
    @Autowired
    AcctService acctService;
    @Autowired
    ProdPkgMsgRemindService prodPkgMsgRemindService;

    public static final int calcuScale = 20;
    public static final int saveScale = 2;

    /**
     * 申购中：撤销融资包 Description: TODO
     * 
     * @param packageId
     * @param operatorId
     * @param operateDate
     */
    @Transactional
    public void cancelProductPackageInApply(String packageId, String operatorId) {
        Date workDate = CommonBusinessUtil.getCurrentWorkDate();
        String eventId = IdUtil.produce();
        String bizId = packageId;
        if (jobWorkService.isBatchBizTaskProcessing()) {
            throw new BizServiceException(EErrorCode.BATCH_EXEC_PROCESSING_WAITING);
        }
        // 判断时间点是否为闭市后开市前
        if (CommonBusinessUtil.isMarketOpen()) {
            throw new PkgTerminateOrCancelInMarketOpenException("只能在闭市后开市前进行操作");
        }
        if (StringUtils.isBlank(packageId)) {
            throw new BizServiceException(EErrorCode.TECH_PARAM_REQUIRED);
        }
        if (StringUtils.isBlank(operatorId)) {
            throw new BizServiceException(EErrorCode.TECH_PARAM_REQUIRED);
        }
        ProductPackage productPackage = productPackageRepository.getProductPackageById(packageId);
        if (productPackage == null) {
            throw new BizServiceException(EErrorCode.TECH_DATA_NOT_EXIST);
        }
        if (!StringUtils.equals(productPackage.getStatus().getCode(), EPackageStatus.SUBSCRIBE.getCode())) {
            throw new BizServiceException(EErrorCode.PROD_PKG_STATUS_IS_NOT_SUBSCRIBE);
        }

        Product product = productRepository.findByProductId(productPackage.getProductId());

        List<PackageSubscribes> subscribes = packageSubscribesRepository.getSubsByPkgId(packageId);

        // 融资人
        String owner = product.getApplUserId();

        if (subscribes.isEmpty()) {
            // 解冻发布保证金
            UnReserveReq unReq = new UnReserveReq();
            unReq.setCurrOpId(operatorId);
            unReq.setReserveJnlNo(productPackage.getServFnrJnlNo());
            unReq.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "签约时0申购，解冻融资服务合同保证金");
            unReq.setUserId(owner);
            unReq.setWorkDate(workDate);
            financierSigningService.refundAllEntrustDeposit(unReq);
        } else {
            // 融资包金额
            BigDecimal packageQuota = AmtUtils.processNegativeAmt(productPackage.getPackageQuota(), BigDecimal.ZERO);

            // 合同利息年利率
            BigDecimal rate = AmtUtils.processNegativeAmt(product.getRate(), BigDecimal.ZERO);
            // 月利息单位值
            BigDecimal origServMrgnRt = AmtUtils.processNegativeAmt(product.getOrigServMrgnRt(), BigDecimal.ZERO);

            // 合同发布保证金 = 融资包金额 * 月利息单位值* 合同利息年利率 / 12
            BigDecimal contractServFee = packageQuota.multiply(rate).multiply(origServMrgnRt)
                    .divide(new BigDecimal(12L), saveScale, RoundingMode.DOWN);

            long pkgUnit = productPackage.getUnit();
            long subsUnit = this.calSubsTotalUnit(subscribes);

            // 实际冻结金额
            BigDecimal actualFrezzAmt = AmtUtils.processNegativeAmt(
                    freezeReserveDtlService.getByJnlNo(productPackage.getServFnrJnlNo()).getTrxAmt(), BigDecimal.ZERO);

            BigDecimal currentAcctAvlBal = AmtUtils.processNegativeAmt(
                    fundAcctBalService.getUserCurrentAcctAvlBal(owner), BigDecimal.ZERO);

            BigDecimal temp = AmtUtils.max(contractServFee, actualFrezzAmt);

            BigDecimal compensateAmt = temp.multiply(BigDecimal.valueOf(subsUnit)).divide(BigDecimal.valueOf(pkgUnit),
                    saveScale, RoundingMode.DOWN);

            BigDecimal totalAvlBal = currentAcctAvlBal.add(actualFrezzAmt).setScale(saveScale);

            if (compensateAmt.compareTo(totalAvlBal) > 0) {
                // 发送提醒
                prodPkgMsgRemindService.pkgCancleWhenCompnsAmtNotEnough(productPackage.getId(), owner);
                AcctPo acctPo = acctService.getAcctByUserId(owner);
                throw new AvlBalNotEnoughException(acctPo.getAcctNo(), "账户可用余额及融资服务合同保证金不足！");
            }

            // 撤单时发布保证金平台扣除比例
            BigDecimal deductRate = AmtUtils.processNegativeAmt(CommonBusinessUtil.getPublishMarginDeductRate(),
                    BigDecimal.ZERO);

            CanclePkgMsgHolder holder = this.getCanclePkgMsgHolder(compensateAmt, deductRate, subscribes,
                    BigDecimal.valueOf(subsUnit), productPackage, owner, false);

            // 按比例分发发布保证金
            financierRevokeFundService.compensateInvestorsAndExchangeForRevoke(eventId, holder.getPayerList(),
                    holder.getPayeeList(), bizId, packageId, null, operatorId, workDate);
            
            for (PackageSubscribes sub : subscribes) {
                // 申购份数
                long subUnit = sub.getUnit().longValue();

                if (subUnit == 0) {
                    continue;
                }

                UnReserveReq urReq = new UnReserveReq();
                urReq.setCurrOpId(operatorId);
                urReq.setReserveJnlNo(sub.getReserveJnlNo());
                urReq.setTrxMemo(PkgUtils.getPkgIdNameStr(sub.getPkgId()) + "撤单，申购预支付资金退还");
                urReq.setUserId(sub.getUserId());
                urReq.setWorkDate(workDate);

                financierRevokeFundService.refundSubsPrePayAmt(urReq);

                if (sub.getAutoSubscribe()) {
                    UnReserveReq feeRrReq = new UnReserveReq();
                    feeRrReq.setCurrOpId(operatorId);
                    feeRrReq.setReserveJnlNo(sub.getFeeReserveJnlNo());
                    feeRrReq.setTrxMemo(PkgUtils.getPkgIdNameStr(sub.getPkgId()) + "撤单，申购预支付费用退还");
                    feeRrReq.setUserId(sub.getUserId());
                    feeRrReq.setWorkDate(workDate);

                    financierRevokeFundService.refundSubsFeePrePayAmt(feeRrReq);
                }
            }
        }

        boolean fnrActive = freezeReserveDtlService.isfnrDtlActive(productPackage.getWrtrFnrJnlNo());
        if (fnrActive) {
            // 担保方保证金退回
            UnReserveReq urReq = new UnReserveReq();
            urReq.setCurrOpId(operatorId);
            urReq.setReserveJnlNo(productPackage.getWrtrFnrJnlNo());
            urReq.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "撤单，退还担保方保证金");
            urReq.setUserId(product.getWarrantId());
            urReq.setWorkDate(workDate);
            warrantDepositService.refundWarrantDeposit(urReq);
        }

        // 更新融资包状态为已废弃
        productPackage.setStatus(EPackageStatus.ABANDON);
        productPackage.setLastOperatorId(operatorId);
        productPackage.setLastTime(new Date());
        productPackageRepository.save(productPackage);

        // 记录日志
        actionHistoryService.save(EntityType.PRODUCTPACKAGE, packageId, ActionType.PKGCANCEL,
                ActionResult.PRODUCT_PACKAGE_CANCEL);

    }

    private long calSubsTotalUnit(List<PackageSubscribes> subscribes) {
        long unit = 0;
        if (subscribes != null && !subscribes.isEmpty()) {
            for (PackageSubscribes subs : subscribes) {
                unit += subs.getUnit().intValue();
            }
        }
        return unit;
    }

    /**
     * 待签约：撤销融资包 Description: TODO
     * 
     * @param packageId
     * @param operatorId
     * @param workDate
     */
    @Transactional
    public void cancelProductPackageInSign(String packageId, String operatorId) {
        Date workDate = CommonBusinessUtil.getCurrentWorkDate();
        String eventId = IdUtil.produce();
        String bizId = packageId;
        if (jobWorkService.isBatchBizTaskProcessing()) {
            throw new BizServiceException(EErrorCode.BATCH_EXEC_PROCESSING_WAITING);
        }
        if (StringUtils.isBlank(packageId)) {
            throw new BizServiceException(EErrorCode.TECH_PARAM_REQUIRED);
        }
        if (StringUtils.isBlank(operatorId)) {
            throw new BizServiceException(EErrorCode.TECH_PARAM_REQUIRED);
        }
        ProductPackage productPackage = productPackageRepository.getProductPackageById(packageId);
        if (productPackage == null) {
            throw new BizServiceException(EErrorCode.TECH_DATA_NOT_EXIST);
        }
        if (!StringUtils.equals(productPackage.getStatus().getCode(), EPackageStatus.WAIT_SIGN.getCode())) {
            throw new BizServiceException(EErrorCode.PROD_PKG_STATUS_IS_NOT_WAIT_SIGN);
        }

        Product product = productRepository.findByProductId(productPackage.getProductId());

        List<PackageSubscribes> subscribes = packageSubscribesRepository.getSubsByPkgId(packageId);

        // 融资人
        String owner = product.getApplUserId();

        if (subscribes.isEmpty()) {
            // 解冻发布保证金
            UnReserveReq unReq = new UnReserveReq();
            unReq.setCurrOpId(operatorId);
            unReq.setReserveJnlNo(productPackage.getServFnrJnlNo());
            unReq.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "签约时0申购，解冻融资服务合同保证金");
            unReq.setUserId(owner);
            unReq.setWorkDate(workDate);
            financierSigningService.refundAllEntrustDeposit(unReq);
        } else {
            // 融资包金额
            BigDecimal packageQuota = AmtUtils.processNegativeAmt(productPackage.getPackageQuota(), BigDecimal.ZERO);

            // 合同利息年利率
            BigDecimal rate = AmtUtils.processNegativeAmt(product.getRate(), BigDecimal.ZERO);
            // 月利息单位值
            BigDecimal publishMarginRate = AmtUtils.processNegativeAmt(CommonBusinessUtil.getPublishMarginRate(),
                    BigDecimal.ZERO);

            // 发布保证金 = 融资包金额 * 月利息单位值* 合同利息年利率 / 12
            BigDecimal contractServFee = packageQuota.multiply(rate).multiply(publishMarginRate)
                    .divide(new BigDecimal(12L), calcuScale, RoundingMode.DOWN);

            long pkgUnit = productPackage.getUnit();
            long subsUnit = this.calSubsTotalUnit(subscribes);

            // 实际冻结金额
            BigDecimal actualFrezzAmt = AmtUtils.processNegativeAmt(
                    freezeReserveDtlService.getByJnlNo(productPackage.getServFnrJnlNo()).getTrxAmt(), BigDecimal.ZERO);

            BigDecimal currentAcctAvlBal = AmtUtils.processNegativeAmt(
                    fundAcctBalService.getUserCurrentAcctAvlBal(owner), BigDecimal.ZERO);

            BigDecimal temp = AmtUtils.max(contractServFee, actualFrezzAmt);

            BigDecimal compensateAmt = temp.multiply(BigDecimal.valueOf(subsUnit)).divide(BigDecimal.valueOf(pkgUnit),
                    saveScale, RoundingMode.DOWN);

            BigDecimal totalAvlBal = currentAcctAvlBal.add(actualFrezzAmt).setScale(saveScale);

            if (compensateAmt.compareTo(totalAvlBal) > 0) {
                // 发送提醒
                prodPkgMsgRemindService.pkgCancleWhenCompnsAmtNotEnough(productPackage.getId(), owner);
                AcctPo acctPo = acctService.getAcctByUserId(owner);
                throw new AvlBalNotEnoughException(acctPo.getAcctNo(), "账户可用余额及融资服务合同保证金不足！");
            }

            // 撤单时发布保证金平台扣除比例
            BigDecimal deductRate = AmtUtils.processNegativeAmt(CommonBusinessUtil.getPublishMarginDeductRate(),
                    BigDecimal.ZERO);

            CanclePkgMsgHolder holder = this.getCanclePkgMsgHolder(compensateAmt, deductRate, subscribes,
                    BigDecimal.valueOf(subsUnit), productPackage, owner, false);

            // 按比例分发发布保证金
            financierRevokeFundService.compensateInvestorsAndExchangeForRevoke(eventId, holder.getPayerList(),
                    holder.getPayeeList(), bizId, packageId, null, operatorId, workDate);

            for (PackageSubscribes sub : subscribes) {
                // 申购份数
                long subUnit = sub.getUnit().longValue();

                if (subUnit == 0) {
                    continue;
                }

                UnReserveReq urReq = new UnReserveReq();
                urReq.setCurrOpId(operatorId);
                urReq.setReserveJnlNo(sub.getReserveJnlNo());
                urReq.setTrxMemo(PkgUtils.getPkgIdNameStr(sub.getPkgId()) + "撤单，申购预支付资金退还");
                urReq.setUserId(sub.getUserId());
                urReq.setWorkDate(workDate);
                financierRevokeFundService.refundSubsPrePayAmt(urReq);

                if (sub.getAutoSubscribe()) {
                    UnReserveReq feeRrReq = new UnReserveReq();
                    feeRrReq.setCurrOpId(operatorId);
                    feeRrReq.setReserveJnlNo(sub.getFeeReserveJnlNo());
                    feeRrReq.setTrxMemo(PkgUtils.getPkgIdNameStr(sub.getPkgId()) + "撤单，申购预支付费用退还");
                    feeRrReq.setUserId(sub.getUserId());
                    feeRrReq.setWorkDate(workDate);
                    financierRevokeFundService.refundSubsFeePrePayAmt(feeRrReq);
                }
            }
        }

        boolean fnrActive = freezeReserveDtlService.isfnrDtlActive(productPackage.getWrtrFnrJnlNo());
        if (fnrActive) {
            // 担保方保证金退回
            UnReserveReq urReq = new UnReserveReq();
            urReq.setCurrOpId(operatorId);
            urReq.setReserveJnlNo(productPackage.getWrtrFnrJnlNo());
            urReq.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "撤单，退还担保方保证金");
            urReq.setUserId(product.getWarrantId());
            urReq.setWorkDate(workDate);
            warrantDepositService.refundWarrantDeposit(urReq);
        }

        // 更新融资包状态为已废弃
        productPackage.setStatus(EPackageStatus.ABANDON);
        productPackage.setLastOperatorId(operatorId);
        productPackage.setLastTime(new Date());
        productPackageRepository.save(productPackage);

        // 记录日志
        actionHistoryService.save(EntityType.PRODUCTPACKAGE, packageId, ActionType.PKGCANCEL,
                ActionResult.PRODUCT_PACKAGE_CANCEL);

    }

    /**
     * 异常撤单 Description: TODO
     * 
     * @param packageId
     * @param operatorId
     * @param operateDate
     * @throws AmtParamInvalidException
     */
    @Transactional
    public void abnormalCancelProductPackage(String packageId, String operatorId) {
        Date workDate = CommonBusinessUtil.getCurrentWorkDate();
        String eventId = IdUtil.produce();
        String bizId = packageId;
        if (jobWorkService.isBatchBizTaskProcessing()) {
            throw new BizServiceException(EErrorCode.BATCH_EXEC_PROCESSING_WAITING);
        }
        if (StringUtils.isBlank(packageId)) {
            throw new BizServiceException(EErrorCode.TECH_PARAM_REQUIRED);
        }
        if (StringUtils.isBlank(operatorId)) {
            throw new BizServiceException(EErrorCode.TECH_PARAM_REQUIRED);
        }
        ProductPackage productPackage = productPackageRepository.getProductPackageById(packageId);
        if (productPackage == null) {
            throw new BizServiceException(EErrorCode.TECH_DATA_NOT_EXIST);
        }

        Product product = productRepository.findByProductId(productPackage.getProductId());
        // 融资人
        String owner = product.getApplUserId();

        List<PackageSubscribes> subscribes = packageSubscribesRepository.getSubsByPkgId(packageId);

        if (subscribes.isEmpty()) {
            // 解冻发布保证金
            UnReserveReq unReq = new UnReserveReq();
            unReq.setCurrOpId(operatorId);
            unReq.setReserveJnlNo(productPackage.getServFnrJnlNo());
            unReq.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "签约时0申购，解冻融资服务合同保证金");
            unReq.setUserId(owner);
            unReq.setWorkDate(workDate);
            financierSigningService.refundAllEntrustDeposit(unReq);
        } else {
            long subsUnit = this.calSubsTotalUnit(subscribes);

            // 融资包金额
            BigDecimal packageQuota = AmtUtils.processNegativeAmt(productPackage.getPackageQuota(), BigDecimal.ZERO);

            // 实际冻结金额
            BigDecimal actualFrezzAmt = AmtUtils.processNegativeAmt(
                    freezeReserveDtlService.getByJnlNo(productPackage.getServFnrJnlNo()).getTrxAmt(), BigDecimal.ZERO);

            // 合同利息年利率
            BigDecimal rate = AmtUtils.processNegativeAmt(product.getRate(), BigDecimal.ZERO);
            // 月利息单位值
            BigDecimal publishMarginRate = AmtUtils.processNegativeAmt(CommonBusinessUtil.getPublishMarginRate(),
                    BigDecimal.ZERO);

            // 合同发布保证金 = 融资包金额 * 月利息单位值* 合同利息年利率 / 12
            BigDecimal contractServFee = packageQuota.multiply(rate).multiply(publishMarginRate)
                    .divide(new BigDecimal(12L), saveScale, RoundingMode.DOWN);

            BigDecimal compensateAmt = contractServFee.multiply(BigDecimal.valueOf(subsUnit)).divide(
                    BigDecimal.valueOf(productPackage.getUnit()), calcuScale, RoundingMode.DOWN);

            BigDecimal trxAmt = AmtUtils.min(actualFrezzAmt, compensateAmt);

            // 撤单时发布保证金平台扣除比例
            BigDecimal deductRate = AmtUtils.processNegativeAmt(CommonBusinessUtil.getPublishMarginDeductRate(),
                    BigDecimal.ZERO);

            CanclePkgMsgHolder holder = this.getCanclePkgMsgHolder(trxAmt, deductRate, subscribes,
                    BigDecimal.valueOf(subsUnit), productPackage, owner, true);

            // 按比例分发发布保证金
            financierRevokeFundService.compensateInvestorsAndExchangeForRevoke(eventId, holder.getPayerList(),
                    holder.getPayeeList(), bizId, packageId, null, operatorId, workDate);

            for (PackageSubscribes sub : subscribes) {
                // 申购份数
                long subUnit = sub.getUnit().longValue();

                if (subUnit == 0) {
                    continue;
                }

                UnReserveReq urReq = new UnReserveReq();
                urReq.setCurrOpId(operatorId);
                urReq.setReserveJnlNo(sub.getReserveJnlNo());
                urReq.setTrxMemo(PkgUtils.getPkgIdNameStr(sub.getPkgId()) + "异常撤单，申购预支付资金退还");
                urReq.setUserId(sub.getUserId());
                urReq.setWorkDate(workDate);

                financierRevokeFundService.refundSubsPrePayAmt(urReq);

                if (sub.getAutoSubscribe()) {
                    UnReserveReq feeRrReq = new UnReserveReq();
                    feeRrReq.setCurrOpId(operatorId);
                    feeRrReq.setReserveJnlNo(sub.getFeeReserveJnlNo());
                    feeRrReq.setTrxMemo(PkgUtils.getPkgIdNameStr(sub.getPkgId()) + "撤单，申购预支付费用退还");
                    feeRrReq.setUserId(sub.getUserId());
                    feeRrReq.setWorkDate(workDate);
                    // 申购预支付费用退还
                    financierRevokeFundService.refundSubsFeePrePayAmt(feeRrReq);
                }
            }
        }

        boolean fnrActive = freezeReserveDtlService.isfnrDtlActive(productPackage.getWrtrFnrJnlNo());
        if (fnrActive) {
            // 担保方保证金退回
            UnReserveReq urReq = new UnReserveReq();
            urReq.setCurrOpId(operatorId);
            urReq.setReserveJnlNo(productPackage.getWrtrFnrJnlNo());
            urReq.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "异常撤单，退还担保方保证金");
            urReq.setUserId(product.getWarrantId());
            urReq.setWorkDate(workDate);
            warrantDepositService.refundWarrantDeposit(urReq);
        }

        // 更新融资包状态为已废弃
        productPackage.setStatus(EPackageStatus.ABANDON);
        productPackage.setLastOperatorId(operatorId);
        productPackage.setLastTime(new Date());
        productPackageRepository.save(productPackage);

        // 记录日志
        actionHistoryService.save(EntityType.PRODUCTPACKAGE, packageId, ActionType.PKGABNORMALCANCEL,
                ActionResult.PRODUCT_PACKAGE_ABNORMAL_CANCEL);

        // 发送提醒
        prodPkgMsgRemindService.pkgAbnormalCancleSuccess(packageId);

    }

    /**
     * 获取撤单时的支付信息 Description: TODO
     * 
     * @param trxAmt
     * @param rate
     * @param subscribes
     * @param productPackage
     * @param owner
     * @param isAbnormal
     * @return
     */
    private CanclePkgMsgHolder getCanclePkgMsgHolder(BigDecimal trxAmt, BigDecimal rate,
            List<PackageSubscribes> subscribes, BigDecimal subsUnit, ProductPackage productPackage, String owner,
            boolean isAbnormal) {

        String msg = isAbnormal ? "异常撤单" : "撤单";

        // 获取平台ID
        String exchUserId = CommonBusinessUtil.getExchangeUserId();

        // 分给投资会员的发布保证金 = 发布保证金金额 * (1-撤单时发布保证金平台扣除比例)
        BigDecimal payBuyerAmt = AmtUtils.max(trxAmt.multiply(BigDecimal.ONE.subtract(rate)), BigDecimal.ZERO);

        // 实际付给投资人的发布保证金
        BigDecimal actualpayBuyerAmt = BigDecimal.ZERO;

        BigDecimal actualpayPlatAmt = BigDecimal.ZERO;

        List<TransferInfo> payeeList = new ArrayList<TransferInfo>();

        List<DedicatedTransferInfo> payerList = new ArrayList<DedicatedTransferInfo>();

        if (subscribes != null && !subscribes.isEmpty()) {

            if (payBuyerAmt.compareTo(BigDecimal.ZERO) > 0) {

                for (PackageSubscribes sub : subscribes) {
                    // 申购份数
                    long subUnit = sub.getUnit().longValue();

                    if (subUnit == 0) {
                        continue;
                    }

                    // 支付金额 = 实际申购份数 / 总实际申购份数 * 分给投资会员的发布保证金
                    BigDecimal perPayAmt = BigDecimal.valueOf(subUnit).multiply(payBuyerAmt)
                            .divide(subsUnit, calcuScale, RoundingMode.DOWN);

                    BigDecimal actualPerPayAmt = perPayAmt.setScale(saveScale, RoundingMode.DOWN);

                    TransferInfo tInfo = new TransferInfo();
                    tInfo.setUserId(sub.getUserId());
                    tInfo.setRelZQ(true);
                    tInfo.setTrxAmt(actualPerPayAmt);
                    tInfo.setTrxMemo(PkgUtils.getPkgIdNameStr(sub.getPkgId()) + msg + "，收到融资服务合同保证金");
                    tInfo.setUseType(EFundUseType.REVOKE_ORDER_FINE);
                    payeeList.add(tInfo);
                    // 累加交易金额
                    actualpayBuyerAmt = actualpayBuyerAmt.add(actualPerPayAmt);
                }
            }

            // 实际支付给平台的发布保证金 = 冻结金额 - 实际支付给投资人的金额
            actualpayPlatAmt = AmtUtils.processNegativeAmt(
                    trxAmt.subtract(actualpayBuyerAmt).setScale(saveScale, RoundingMode.DOWN), BigDecimal.ZERO);
            // 分给平台保证金
            TransferInfo ptInfo = new TransferInfo();
            ptInfo.setUserId(exchUserId);
            ptInfo.setRelZQ(true);
            ptInfo.setTrxAmt(actualpayPlatAmt);
            ptInfo.setTrxMemo(PkgUtils.getPkgIdNameStr(productPackage.getId()) + msg + "，收到融资服务合同保证金");
            ptInfo.setUseType(EFundUseType.REVOKE_ORDER_FINE);
            payeeList.add(ptInfo);

            // 实际支付总金额
            BigDecimal actualTotalPayAmt = actualpayPlatAmt.add(actualpayBuyerAmt);

            DedicatedTransferInfo dInfo = new DedicatedTransferInfo();
            dInfo.setUserId(owner);
            dInfo.setRelZQ(true);
            dInfo.setTrxAmt(actualTotalPayAmt);
            dInfo.setTrxMemo(PkgUtils.getPkgIdNameStr(productPackage.getId()) + msg + "，划出融资服务合同保证金");
            dInfo.setUseType(EFundUseType.REVOKE_ORDER_FINE);
            dInfo.setFnrJnlNo(productPackage.getServFnrJnlNo());
            payerList.add(dInfo);

            // // 支付给平台的发布保证金 = 发布保证金金额 * (撤单时发布保证金平台扣除比例)
            // BigDecimal actualpayPlatAmt = AmtUtils.max(trxAmt.multiply(rate), BigDecimal.ZERO).setScale(saveScale,
            // RoundingMode.DOWN);
            //
            // // 分给平台保证金
            // TransferInfo ptInfox = new TransferInfo();
            // ptInfo.setUserId(exchUserId);
            // ptInfo.setRelZQ(true);
            // ptInfo.setTrxAmt(actualpayPlatAmt);
            // ptInfo.setTrxMemo("融资包" + productPackage.getId() + msg + "，收到融资服务合同保证金");
            // ptInfo.setUseType(EFundUseType.POSTDEPOSIT);
            // payeeList.add(ptInfox);
            //
            // BigDecimal deviation = trxAmt.subtract(actualpayPlatAmt).subtract(actualpayBuyerAmt);
            //
            // if(deviation.compareTo(BigDecimal.ZERO)>0){
            // // 给平台的误差
            // TransferInfo deviationInfox = new TransferInfo();
            // ptInfo.setUserId(exchUserId);
            // ptInfo.setRelZQ(true);
            // ptInfo.setTrxAmt(deviation);
            // ptInfo.setTrxMemo("融资包" + productPackage.getId() + msg + "，收到融资服务合同保证金误差");
            // ptInfo.setUseType(EFundUseType.);
            // payeeList.add(deviationInfox);
            // }
            //
            // DedicatedTransferInfo dInfox = new DedicatedTransferInfo();
            // dInfo.setUserId(owner);
            // dInfo.setRelZQ(true);
            // dInfo.setTrxAmt(trxAmt);
            // dInfo.setTrxMemo("融资包" + productPackage.getId() + msg + "，划出融资服务合同保证金");
            // dInfo.setUseType(EFundUseType.POSTDEPOSIT);
            // dInfo.setFnrJnlNo(productPackage.getServFnrJnlNo());
            // payerList.add(dInfox);

        }

        CanclePkgMsgHolder holder = new CanclePkgMsgHolder();
        holder.setActualpayBuyerAmt(actualpayBuyerAmt);
        holder.setActualpayPlatAmt(actualpayPlatAmt);
        holder.setPayeeList(payeeList);
        holder.setPayerList(payerList);
        return holder;
    }

}
