/*
 * Project Name: kmfex-platform
 * File Name: ManualRepayService.java
 * Class Name: ManualRepayService
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
import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.entity.id.IdUtil;
import com.hengxin.platform.common.enums.ActionResult;
import com.hengxin.platform.common.enums.ActionType;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.enums.EntityType;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.service.ActionHistoryService;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.fund.dto.biz.req.ManualPaymentReq;
import com.hengxin.platform.fund.dto.biz.req.atom.DedicatedTransferInfo;
import com.hengxin.platform.fund.dto.biz.req.atom.TransferInfo;
import com.hengxin.platform.fund.dto.biz.req.atom.UnReserveReq;
import com.hengxin.platform.fund.entity.AcctPo;
import com.hengxin.platform.fund.entity.PositionLotPo;
import com.hengxin.platform.fund.entity.PositionPo;
import com.hengxin.platform.fund.enums.EFlagType;
import com.hengxin.platform.fund.enums.EFundUseType;
import com.hengxin.platform.fund.exception.AvlBalNotEnoughException;
import com.hengxin.platform.fund.service.AcctService;
import com.hengxin.platform.fund.service.FreezeReserveDtlService;
import com.hengxin.platform.fund.service.FundAcctBalService;
import com.hengxin.platform.fund.service.FundPositionService;
import com.hengxin.platform.fund.service.PlatformRepayByHandService;
import com.hengxin.platform.fund.service.PositionLotService;
import com.hengxin.platform.fund.service.WarrantDepositService;
import com.hengxin.platform.fund.service.atom.PositionService;
import com.hengxin.platform.fund.util.AmtUtils;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.product.ProdPkgUtil;
import com.hengxin.platform.product.domain.PaymentSchedule;
import com.hengxin.platform.product.domain.Product;
import com.hengxin.platform.product.domain.ProductPackage;
import com.hengxin.platform.product.enums.EPackageStatus;
import com.hengxin.platform.product.enums.EPayStatus;
import com.hengxin.platform.product.repository.PaymentScheduleRepository;
import com.hengxin.platform.product.repository.ProductPackageRepository;
import com.hengxin.platform.product.repository.ProductRepository;
import com.hengxin.platform.product.service.MsgHolder.PayScheMsgHolder;
import com.hengxin.platform.product.service.MsgHolder.PkgUtils;
import com.hengxin.platform.product.service.MsgHolder.req.ExchagePayeeListBuildReq;
import com.hengxin.platform.product.service.MsgHolder.req.InvestorPayeeListBuildReq;

/**
 * 融资包手工还款服务 Class Name: ManualRepayService.
 * 
 * @author tingwang
 * 
 */
@Service
public class ManualRepayService {

    private static final int SAVE_SCALE = 2;
    
    @Autowired
    ProductPackageRepository productPackageRepository;
    @Autowired
    ActionHistoryService actionHistoryService;
    @Autowired
    PositionService positionService;
    @Autowired
    PaymentScheduleRepository paymentScheduleRepository;
    @Autowired
    PositionLotService positionLotService;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    PlatformRepayByHandService platformRepayByHandService;
    @Autowired
    FundAcctBalService fundAcctBalService;
    @Autowired
    FundPositionService fundPositionService;
    @Autowired
    FreezeReserveDtlService freezeReserveDtlService;
    @Autowired
    WarrantDepositService warrantDepositService;
    @Autowired
    JobWorkService jobWorkService;
    @Autowired
    AcctService acctService;

    @Transactional
    public void manualRePayProductPackage(String packageId, int period, String operatorId) {
        Date workDate = CommonBusinessUtil.getCurrentWorkDate();
        String eventId = IdUtil.produce();
        String bizId = packageId + "_" + period;
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
        if (!StringUtils.equals(productPackage.getStatus().getCode(), EPackageStatus.PAYING.getCode())) {
            throw new BizServiceException(EErrorCode.PROD_PKG_STATUS_IS_NOT_PAYING);
        }
        PaymentSchedule schedule = paymentScheduleRepository.getByPackageIdAndSequenceId(packageId, period);
        if (schedule == null) {
            throw new BizServiceException(EErrorCode.TECH_DATA_NOT_EXIST);
        }
        if (EPayStatus.NORMAL != schedule.getStatus()) {
            throw new BizServiceException(EErrorCode.PROD_PKG_HAS_BEEN_DISPOSED);
        }
        Product product = productRepository.findByProductId(productPackage.getProductId());

        BigDecimal supplyAmount = AmtUtils.processNegativeAmt(productPackage.getSupplyAmount(), BigDecimal.ZERO);
        // 实际申购份数
        BigDecimal supplyUnit = supplyAmount.divide(productPackage.getUnitAmount());

        // 融资人
        String owner = product.getApplUserId();

        // 还款利息（包括罚息）平台扣除比例
        BigDecimal deductRate = AmtUtils.processNegativeAmt(CommonBusinessUtil.getPaymentInterestDeductRate(),
                BigDecimal.ZERO);

        PayScheMsgHolder scheMsgHolder = ProdPkgUtil.resolvePaymentSchedule(schedule, true, true, true, true, true,
                true);

        BigDecimal prinPay = scheMsgHolder.getPrinPay();
        BigDecimal prinFinePay = scheMsgHolder.getPrinFinePay();

        BigDecimal intrPay = scheMsgHolder.getIntrPay();
        BigDecimal intrFinePay = scheMsgHolder.getIntrFinePay();

        BigDecimal feePay = scheMsgHolder.getFeePay();
        BigDecimal feeFinePay = scheMsgHolder.getFeeFinePay();

        // 累计实际付给投资人的金额
        BigDecimal buyerRecvTotalAc = BigDecimal.ZERO;
        // 累计实际付给平台的金额
        BigDecimal platRecvTotalAc = BigDecimal.ZERO;
        // 累计实际投资人付给平台的金额
        BigDecimal buyer2PlatTotalAc = BigDecimal.ZERO;

        // 给投资人的应付利息
        BigDecimal intrBuyer = intrPay;
        // 给投资人的应付利息罚金
        BigDecimal intrForfBuyer = intrFinePay;
        // 给投资人的应付本金
        BigDecimal prinBuyer = prinPay;
        // 给投资人的应付本金罚金
        BigDecimal prinForfBuyer = prinFinePay;

        // 给平台的应付费用
        BigDecimal feePlat = feePay;
        // 给平台的应付费用罚金
        BigDecimal feeForfPlat = feeFinePay;

        BigDecimal fncrPayTotalAmt = intrBuyer.add(intrForfBuyer).add(prinBuyer).add(prinForfBuyer).add(feePlat)
                .add(feeForfPlat);

        // 融资人可用余额（加小微宝）
        BigDecimal acctAvlBal = fundAcctBalService.getUserCurrentAcctAvlBal(owner, true);
        if (StringUtils.equals(schedule.getLastFlag(), EFlagType.YES.getCode())) {
            BigDecimal loanMarginAmt = AmtUtils.processNegativeAmt(
                    freezeReserveDtlService.getByJnlNo(productPackage.getLoanFnrJnlNo()).getTrxAmt(), BigDecimal.ZERO);
            acctAvlBal = acctAvlBal.add(loanMarginAmt);
        }
        if (acctAvlBal.compareTo(fncrPayTotalAmt) < 0) {
            AcctPo acctPo = acctService.getAcctByUserId(owner);
            throw new AvlBalNotEnoughException(acctPo.getAcctNo(), "账户余额不足");
        }

        // 获取平台ID
        String exchangeUserId = CommonBusinessUtil.getExchangeUserId();

        List<TransferInfo> payeeList = new ArrayList<TransferInfo>();
        List<DedicatedTransferInfo> payerList = new ArrayList<DedicatedTransferInfo>();
        List<TransferInfo> profPayeeList = new ArrayList<TransferInfo>();
        List<TransferInfo> profPayerList = new ArrayList<TransferInfo>();

        List<PositionPo> positions = positionService.getByPkgId(packageId);
        for (PositionPo p : positions) {

            // 已转让，头寸份额为 0
            if (p.getUnit().longValue() <= 0) {
                continue;
            }

            BigDecimal intrBuyerAc = intrBuyer.multiply(BigDecimal.valueOf(p.getUnit().longValue())).divide(supplyUnit,
                    SAVE_SCALE, RoundingMode.DOWN);

            BigDecimal intrForfBuyerAc = intrForfBuyer.multiply(BigDecimal.valueOf(p.getUnit().longValue())).divide(
                    supplyUnit, SAVE_SCALE, RoundingMode.DOWN);

            BigDecimal prinBuyerAc = prinBuyer.multiply(BigDecimal.valueOf(p.getUnit().longValue())).divide(supplyUnit,
                    SAVE_SCALE, RoundingMode.DOWN);

            BigDecimal prinForfBuyerAc = prinForfBuyer.multiply(BigDecimal.valueOf(p.getUnit().longValue())).divide(
                    supplyUnit, SAVE_SCALE, RoundingMode.DOWN);

            BigDecimal intrBuyer2PlatAc = intrBuyerAc.add(intrForfBuyerAc).multiply(deductRate)
                    .setScale(SAVE_SCALE, RoundingMode.DOWN);

            // 实际支付给投资人的钱
            BigDecimal buyerRecvAc = intrBuyerAc.add(intrForfBuyerAc).add(prinBuyerAc).add(prinForfBuyerAc);

            // 累计金额
            buyerRecvTotalAc = buyerRecvTotalAc.add(buyerRecvAc);
            buyer2PlatTotalAc = buyer2PlatTotalAc.add(intrBuyer2PlatAc);

            InvestorPayeeListBuildReq investorReq = new InvestorPayeeListBuildReq();
            investorReq.setIntrBuyerAc(intrBuyerAc);
            investorReq.setIntrForfBuyerAc(intrForfBuyerAc);
            investorReq.setIntrBuyer2PlatAc(intrBuyer2PlatAc);
            investorReq.setMsg("手工还款");
            investorReq.setPkgId(packageId);
            investorReq.setPrinBuyerAc(prinBuyerAc);
            investorReq.setPrinForfBuyerAc(prinForfBuyerAc);
            investorReq.setUserId(p.getUserId());
            investorReq.setSequenceId(String.valueOf(period));
            investorReq.setIntrRate(deductRate);

            ProdPkgUtil.buildPayeeListOfInvestor(investorReq, payeeList, profPayerList);

            String positionId = p.getPositionId();

            // 过滤掉已转让的份额
            List<PositionLotPo> lots = positionService.getValidPostionLotsByPostionId(positionId);
            int size = lots.size();
            BigDecimal assignAmt = BigDecimal.ZERO;
            boolean isLast = false;
            if (StringUtils.equals(schedule.getLastFlag(), EFlagType.YES.getCode())) {
                isLast = true;
            }
            for (int i = 0; i < size; i++) {
                PositionLotPo lot = lots.get(i);
                ManualPaymentReq paymentReq = new ManualPaymentReq();
                paymentReq.setBuyerUserId(p.getUserId());
                paymentReq.setCurrOpId(operatorId);
                paymentReq.setLast(isLast);
                paymentReq.setLotId(lot.getLotId());
                paymentReq.setPkgId(productPackage.getId());
                paymentReq.setWorkDate(workDate);

                BigDecimal actualAmt = buyerRecvAc.multiply(BigDecimal.valueOf(lot.getUnit().longValue()));
                actualAmt = actualAmt.divide(BigDecimal.valueOf(p.getUnit().longValue()), SAVE_SCALE,
                      RoundingMode.DOWN);
                
                if ((size - 1) != i) {
                    paymentReq.setPaymentAmt(actualAmt);
                } else {
                	BigDecimal surplusAmt = AmtUtils.processNegativeAmt(buyerRecvAc.subtract(assignAmt),BigDecimal.ZERO);
                    paymentReq.setPaymentAmt(surplusAmt);
                }
                // 更新头寸
                fundPositionService.manualPayment(paymentReq);
                assignAmt = assignAmt.add(actualAmt);
            }
        }

        platRecvTotalAc = buyer2PlatTotalAc.add(feePlat).add(feeForfPlat);
        buyerRecvTotalAc = buyerRecvTotalAc.subtract(buyer2PlatTotalAc);

        // 误差金额 = 应付总额 - 实际付给投资人的还款- 实际付给平台的还款
        BigDecimal deviation = AmtUtils.processNegativeAmt(
                fncrPayTotalAmt.subtract(buyerRecvTotalAc).subtract(platRecvTotalAc), BigDecimal.ZERO);

        ExchagePayeeListBuildReq exchageReq = new ExchagePayeeListBuildReq();
        exchageReq.setDeviation(deviation);
        exchageReq.setExchangeUserId(exchangeUserId);
        exchageReq.setFeeForfPlat(feeForfPlat);
        exchageReq.setBuyer2PlatTotalAc(buyer2PlatTotalAc);
        exchageReq.setFeePlat(feePlat);
        exchageReq.setMsg("手工还款");
        exchageReq.setPkgId(packageId);
        exchageReq.setSequenceId(String.valueOf(period));
        exchageReq.setIntrRate(deductRate);

        ProdPkgUtil.buildPayeeListOfExchange(exchageReq, payeeList, profPayeeList);

        if (StringUtils.equals(schedule.getLastFlag(), EFlagType.NO.getCode())) {
            List<TransferInfo> payerList2 = new ArrayList<TransferInfo>();
            for (DedicatedTransferInfo info : payerList) {
                payerList2.add(ConverterService.convert(info, TransferInfo.class));
            }
            TransferInfo dInfo = new TransferInfo();
            dInfo.setUserId(owner);
            dInfo.setRelZQ(true);
            dInfo.setTrxAmt(fncrPayTotalAmt);
            dInfo.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "_" + period + "手工还款，融资会员划出" + fncrPayTotalAmt + "元");
            dInfo.setUseType(EFundUseType.FNCR_REPAYMENT);
            payerList2.add(dInfo);
            // 非最后一期按比例发放融资还款
            platformRepayByHandService.repayOnPayDate(eventId, payerList2, payeeList, bizId, 
            		packageId, String.valueOf(period), operatorId, workDate);
        } else {
            DedicatedTransferInfo dInfo = new DedicatedTransferInfo();
            dInfo.setUserId(owner);
            dInfo.setRelZQ(true);
            dInfo.setTrxAmt(fncrPayTotalAmt);
            dInfo.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "_" + period + "手工还款，融资会员划出" + fncrPayTotalAmt + "元");
            dInfo.setUseType(EFundUseType.FNCR_REPAYMENT);
            dInfo.setFnrJnlNo(productPackage.getLoanFnrJnlNo());
            payerList.add(dInfo);
            // 最后一期按比例发放融资还款
            platformRepayByHandService.repayOnMaturityDate(eventId, payerList, payeeList, bizId, 
            		packageId, String.valueOf(period), operatorId, workDate);
        }

        if (!profPayerList.isEmpty()) {
            platformRepayByHandService.payInvsIntrProfitToExchange(eventId, profPayerList, profPayeeList, bizId,
            		packageId, String.valueOf(period), operatorId, workDate);
        }

        // 更新还款计划表状态为已结束
        schedule.setStatus(EPayStatus.FINISH);
        schedule.setPayPrincipalAmt(schedule.getPrincipalAmt());
        schedule.setPayInterestAmt(schedule.getInterestAmt());
        schedule.setPayAmt(schedule.getFeeAmt());
        schedule.setPayPrincipalForfeit(schedule.getPrincipalForfeit());
        schedule.setPayInterestForfeit(schedule.getInterestForfeit());
        schedule.setPayFeeForfeit(schedule.getFeeForfeit());
        schedule.setLastMntOpid(operatorId);
        schedule.setLastMntTs(new Date());
        schedule.setLastPayTs(DateUtils.formatYYYYMMDDHHmmss(workDate));
        paymentScheduleRepository.save(schedule);
        paymentScheduleRepository.flush();

        if (StringUtils.equals(schedule.getLastFlag(), EFlagType.YES.getCode()) && productPackage.getWrtrFnrJnlNo() != null) {
            // 退担保保证金
            UnReserveReq uReq = new UnReserveReq();
            uReq.setCurrOpId(operatorId);
            uReq.setReserveJnlNo(productPackage.getWrtrFnrJnlNo());
            uReq.setTrxMemo(PkgUtils.getPkgIdNameStr(productPackage.getId()) + "_" + period + "最后一期手工还款，担保保证金退回");
            uReq.setUserId(product.getWarrantId());
            uReq.setWorkDate(workDate);
            warrantDepositService.refundWarrantDeposit(uReq);
        }

        // 如果还款计划都为结束的时候，包的状态更新为结束
        productPackageRepository.updatePkgStatusToEnd(packageId, EPackageStatus.PAYING.getCode(),
                EPackageStatus.END.getCode(), EPayStatus.FINISH.getCode(), operatorId, new Date());

        // 记录日志
        actionHistoryService.save(EntityType.PRODUCTPACKAGE, packageId, ActionType.PKGMANUALREPAY,
                ActionResult.PRODUCT_PACKAGE_MANUAL_REPAY);

    }

}
