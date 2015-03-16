/*
 * Project Name: kmfex-platform
 * File Name: SignPkgService.java
 * Class Name: SignPkgService
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.batch.enums.ETaskGroupId;
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
import com.hengxin.platform.fund.dto.biz.req.atom.ReserveReq;
import com.hengxin.platform.fund.dto.biz.req.atom.TransferInfo;
import com.hengxin.platform.fund.dto.biz.req.atom.UnReserveReq;
import com.hengxin.platform.fund.entity.AcctPo;
import com.hengxin.platform.fund.entity.PkgTradeJnlPo;
import com.hengxin.platform.fund.entity.PositionLotPo;
import com.hengxin.platform.fund.entity.PositionPo;
import com.hengxin.platform.fund.enums.EFlagType;
import com.hengxin.platform.fund.enums.EFundTrdType;
import com.hengxin.platform.fund.enums.EFundUseType;
import com.hengxin.platform.fund.repository.PositionRepository;
import com.hengxin.platform.fund.service.AcctService;
import com.hengxin.platform.fund.service.AddMaxCashableAmtService;
import com.hengxin.platform.fund.service.FinancierEntrustDepositService;
import com.hengxin.platform.fund.service.FinancierSigningService;
import com.hengxin.platform.fund.service.FundPositionService;
import com.hengxin.platform.fund.service.PkgTradeJnlService;
import com.hengxin.platform.fund.service.PositionLotService;
import com.hengxin.platform.fund.service.UserPayFeeService;
import com.hengxin.platform.fund.service.WarrantDepositService;
import com.hengxin.platform.fund.service.atom.PositionService;
import com.hengxin.platform.fund.util.AmtUtils;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.product.domain.ContractRate;
import com.hengxin.platform.product.domain.ContractRatePK;
import com.hengxin.platform.product.domain.LoanContract;
import com.hengxin.platform.product.domain.PackageContract;
import com.hengxin.platform.product.domain.PackageSubscribes;
import com.hengxin.platform.product.domain.PaymentSchedule;
import com.hengxin.platform.product.domain.Product;
import com.hengxin.platform.product.domain.ProductFee;
import com.hengxin.platform.product.domain.ProductPackage;
import com.hengxin.platform.product.enums.EFeePayMethodType;
import com.hengxin.platform.product.enums.EPackageStatus;
import com.hengxin.platform.product.enums.EPayMethodType;
import com.hengxin.platform.product.enums.EPayStatus;
import com.hengxin.platform.product.enums.ETermType;
import com.hengxin.platform.product.exception.PkgSubscribRecordsIsEmptyException;
import com.hengxin.platform.product.repository.LoanContractRepository;
import com.hengxin.platform.product.repository.PackageContractRepository;
import com.hengxin.platform.product.repository.PackageSubscribesRepository;
import com.hengxin.platform.product.repository.PaymentScheduleRepository;
import com.hengxin.platform.product.repository.ProductFeeRepository;
import com.hengxin.platform.product.repository.ProductPackageRepository;
import com.hengxin.platform.product.repository.ProductRepository;
import com.hengxin.platform.product.service.MsgHolder.PkgMinSubsDtMsgHolder;
import com.hengxin.platform.product.service.MsgHolder.PkgSubsUnitSumMsgHolder;
import com.hengxin.platform.product.service.MsgHolder.PkgUtils;

/**
 * 签约融资包服务 Class Name: SignPkgService
 * 
 * @author tingwang
 * 
 */
@Service
public class SignPkgService {

    @Autowired
    ProductPackageRepository productPackageRepository;
    @Autowired
    ActionHistoryService actionHistoryService;
    @Autowired
    PositionService positionService;
    @Autowired
    FinancierSigningService financierSigningService;
    @Autowired
    PaymentScheduleRepository paymentScheduleRepository;
    @Autowired
    PositionLotService positionLotService;
    @Autowired
    PackageSubscribesRepository packageSubscribesRepository;
    @Autowired
    WarrantDepositService warrantDepositService;
    @Autowired
    UserPayFeeService userPayFeeService;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductFeeRepository productFeeRepository;
    @Autowired
    AddMaxCashableAmtService addMaxCashableAmtService;
    @Autowired
    PkgTradeJnlService pkgTradeJnlService;
    @Autowired
    FinancierEntrustDepositService financierEntrustDepositService;
    @Autowired
    FundPositionService fundPositionService;
    @Autowired
    ProductContractTemplateService productContractTemplateService;
    @Autowired
    PositionRepository positionRepository;
    @Autowired
    PackageContractRepository packageContractRepository;
    @Autowired
    JobWorkService jobWorkService;
    @Autowired
    AcctService acctService;
    @Autowired
    LoanContractRepository loanContractRepository;
    @Autowired
    ContractRateService contractRateService;

    public static final int calcuScale = 20;
    public static final int saveScale = 2;

    /**
     * 融资包签约
     * 
     * @param packageId
     * @param operatorId
     * @param workDate
     */
    @Transactional
    public void signProductPackageInWaitSign(String packageId, String operatorId) {
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
        // 系统闭市 之后，批量任务执行成功之前，不允许做签约操作
        if (!CommonBusinessUtil.isMarketOpen() 
        		&& jobWorkService.existUnSucceedTasks(Arrays.asList(ETaskGroupId.REPAYMENT.getCode(),
        				ETaskGroupId.BIZTASK.getCode()))){
            throw new BizServiceException(EErrorCode.BATCH_EXEC_PROCESSING_WAITING);
        }
        ProductPackage pkg = productPackageRepository.getProductPackageById(packageId);
        if (pkg == null) {
            throw new BizServiceException(EErrorCode.TECH_DATA_NOT_EXIST);
        }
        if (!StringUtils.equals(pkg.getStatus().getCode(), EPackageStatus.WAIT_SIGN.getCode())) {
            throw new BizServiceException(EErrorCode.PROD_PKG_STATUS_IS_NOT_WAIT_SIGN);
        }
        List<PackageSubscribes> subscribes = packageSubscribesRepository.getSubsByPkgId(packageId);
        if (subscribes == null || subscribes.isEmpty()) {
            throw new PkgSubscribRecordsIsEmptyException("您的申购额必须大于0才能签约！请撤单！");
        }
        Product product = pkg.getProduct();

        // 融资包金额
        BigDecimal packageQuota = AmtUtils.processNegativeAmt(pkg.getPackageQuota(), BigDecimal.ZERO);

        // 实际申购额
        BigDecimal supplyAmount = AmtUtils.processNegativeAmt(pkg.getSupplyAmount(), BigDecimal.ZERO);

        // 融资人
        String owner = product.getApplUserId();

        // 实际总申购金额
        BigDecimal totalActualSubsAmt = BigDecimal.ZERO;
        List<DedicatedTransferInfo> payerList = new ArrayList<DedicatedTransferInfo>();

        // 交自动申购费用
        BigDecimal totalSubsFee = BigDecimal.ZERO;
        List<DedicatedTransferInfo> feePayers = new ArrayList<DedicatedTransferInfo>();

        for (PackageSubscribes subs : subscribes) {
            // 份数
            long unit = AmtUtils.max(subs.getUnit().longValue(), 0L);
            // 每份金额
            BigDecimal unitAmt = AmtUtils.processNegativeAmt(subs.getUnitAmt(), BigDecimal.ZERO);

            // 申购金额 = 份数 * 每份金额
            BigDecimal subsAmt = unitAmt.multiply(BigDecimal.valueOf(unit));
            subsAmt = subsAmt.setScale(saveScale, RoundingMode.DOWN);
            if (BigDecimal.ZERO.compareTo(subsAmt) == 0) {
                continue;
            }

            DedicatedTransferInfo dInfo = new DedicatedTransferInfo();
            dInfo.setUserId(subs.getUserId());
            dInfo.setRelZQ(true);
            dInfo.setTrxAmt(subsAmt);
            dInfo.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "签约，投资会员划出" + subsAmt + "元");
            dInfo.setUseType(EFundUseType.SUBSCRIBE);
            // 投资人申购保证金流水号
            dInfo.setFnrJnlNo(subs.getReserveJnlNo());
            payerList.add(dInfo);
            // 累计金额
            totalActualSubsAmt = totalActualSubsAmt.add(subsAmt);

            if (subs.getAutoSubscribe()) {
            	BigDecimal autoSubsFeeRt = CommonBusinessUtil.getAutoSubscribeFeeRate();
            	autoSubsFeeRt = AmtUtils.processNullAmt(autoSubsFeeRt, BigDecimal.ZERO);
                BigDecimal deal = subs.getUnitAmt().multiply(BigDecimal.valueOf(subs.getUnit()));
                BigDecimal fee = deal.multiply(autoSubsFeeRt).setScale(2,
                        RoundingMode.DOWN);
                DedicatedTransferInfo feeTrans = new DedicatedTransferInfo();
                feeTrans.setFnrJnlNo(subs.getFeeReserveJnlNo());
                feeTrans.setRelZQ(false);
                feeTrans.setTrxAmt(fee);
                feeTrans.setUseType(EFundUseType.SUBSCRIBEFEE);
                feeTrans.setTrxMemo(PkgUtils.getPkgIdNameStr(subs.getPkgId()) + "签约，缴纳申购预支付费用");
                feeTrans.setUserId(subs.getUserId());
                feePayers.add(feeTrans);
                totalSubsFee = totalSubsFee.add(fee);
            }
        }

        TransferInfo payee = new TransferInfo();
        payee.setUserId(owner);
        payee.setRelZQ(false);
        payee.setTrxAmt(totalActualSubsAmt);
        payee.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "签约，融资会员收到融资额" + totalActualSubsAmt + "元");
        payee.setUseType(EFundUseType.FINANCING);
        // 从投资人账户划转融资金额至融资人账户
        String loanFnrJnlNo = financierSigningService.signingTransferAmt(eventId, payerList, payee, bizId, packageId, operatorId,
                workDate);

        // 设置资金划转流水号
        pkg.setFnrJnlNo(loanFnrJnlNo);

        if (totalSubsFee.compareTo(BigDecimal.ZERO) > 0 && !feePayers.isEmpty()) {
            TransferInfo feePayee = new TransferInfo();
            feePayee.setUserId(CommonBusinessUtil.getExchangeUserId());
            feePayee.setRelZQ(false);
            feePayee.setTrxAmt(totalSubsFee);
            feePayee.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "签约，平台收到申购预支付费用" + totalSubsFee + "元");
            feePayee.setUseType(EFundUseType.SUBSCRIBEFEE);
            // 申购费用划转
            financierSigningService.subsFeeTransferAmt(eventId, feePayers, feePayee, bizId, packageId, operatorId, workDate);
        }

        /*
         * // 席位费校验和缴纳 boolean hasFeeOfSeat = userPayFeeService.hasPayFeeOfSeat(owner, workDate); if (!hasFeeOfSeat) {
         * UserPayFeeReq upReq = new UserPayFeeReq(); upReq.setCurrOpId(operatorId); upReq.setEventId(eventId);
         * upReq.setFeeType(EFeeType.SEAT); upReq.setPeriodNum(new Integer(1));
         * upReq.setPeriodType(EFeePeriodType.YEAR); upReq.setTrxMemo("融资会员：" + owner + "缴纳坐席服务费");
         * upReq.setUserId(owner); upReq.setWorkDate(workDate);
         * 
         * BigDecimal seatFeeThreshold = AmtUtils.processNegativeAmt(CommonBusinessUtil.getSeatFeeThreshold(),
         * BigDecimal.ZERO); BigDecimal seatFee = BigDecimal.ZERO; if (supplyAmount.compareTo(seatFeeThreshold) <= 0) {
         * seatFee = AmtUtils.processNegativeAmt(CommonBusinessUtil.getSeatFeeLowAmt(), BigDecimal.ZERO); } else {
         * seatFee = AmtUtils.processNegativeAmt(CommonBusinessUtil.getSeatFeeHighAmt(), BigDecimal.ZERO); }
         * 
         * upReq.setTrxAmt(seatFee.setScale(saveScale, RoundingMode.DOWN)); // 缴纳席位费 userPayFeeService.payFee(upReq); }
         * 
         * // 融资项目融资服务费实体 ProductFee serveFeeEntity =
         * productFeeRepository.getFeeByProductIdAndFeeName(product.getProductId(), SERVFEERT);
         * 
         * // 融资服务费率 BigDecimal serveFeeRate = AmtUtils.processNegativeAmt(serveFeeEntity.getFeeRt(), BigDecimal.ZERO);
         * 
         * // 融资项目风险管理费实体 ProductFee riskFeeEntity =
         * productFeeRepository.getFeeByProductIdAndFeeName(product.getProductId(), RISKFEERT);
         * 
         * // 风险管理费率 BigDecimal riskFeeRate = AmtUtils.processNegativeAmt(riskFeeEntity.getFeeRt(), BigDecimal.ZERO);
         * 
         * List<TradeFeePayReq> feeReqList = new ArrayList<TradeFeePayReq>();
         * 
         * if (BigDecimal.ZERO.compareTo(serveFeeRate) < 0) { if (EFeePayMethodType.ONCE ==
         * serveFeeEntity.getPayMethod()) { // 需还融资服务费 = 融资服务费率 * 融资包实际申购金额 * 月数 BigDecimal size =
         * BigDecimal.valueOf(product.getTermLength().longValue()); BigDecimal totalServeFee =
         * serveFeeRate.multiply(supplyAmount).multiply(size); if (product.getTermType() == ETermType.YEAR) {
         * totalServeFee = totalServeFee.multiply(BigDecimal.valueOf(12L)); } if (product.getTermType() ==
         * ETermType.DAY) { totalServeFee = totalServeFee.divide(BigDecimal.valueOf(30L), saveScale, RoundingMode.DOWN);
         * } if (totalServeFee.compareTo(BigDecimal.ZERO) > 0) { TradeFeePayReq serviceFeeReq = new TradeFeePayReq();
         * serviceFeeReq.setFeeType(EFeeType.LOANSERVE); serviceFeeReq.setTrxAmt(totalServeFee);
         * serviceFeeReq.setTrxMemo("融资包" + packageId + "签约成功，扣除融资服务费" + totalServeFee + "元");
         * feeReqList.add(serviceFeeReq); } } } if (BigDecimal.ZERO.compareTo(riskFeeRate) < 0) { if
         * (EFeePayMethodType.ONCE == riskFeeEntity.getPayMethod()) { BigDecimal size =
         * BigDecimal.valueOf(product.getTermLength().longValue()); // 需还风险管理费 = 风险管理费率 * 融资包金额* 月数 BigDecimal
         * totalRiskFee = riskFeeRate.multiply(supplyAmount).multiply(size); if (product.getTermType() ==
         * ETermType.YEAR) { size = size.multiply(BigDecimal.valueOf(12L)); totalRiskFee =
         * totalRiskFee.multiply(BigDecimal.valueOf(12L)); } if (product.getTermType() == ETermType.DAY) { totalRiskFee
         * = totalRiskFee.divide(BigDecimal.valueOf(30L), saveScale, RoundingMode.DOWN); } if
         * (totalRiskFee.compareTo(BigDecimal.ZERO) > 0) { TradeFeePayReq riskFeeReq = new TradeFeePayReq();
         * riskFeeReq.setFeeType(EFeeType.RISKMANAGE); riskFeeReq.setTrxAmt(totalRiskFee); riskFeeReq.setTrxMemo("融资包" +
         * packageId + "签约成功，扣除风险管理费" + totalRiskFee + "元"); feeReqList.add(riskFeeReq); } } }
         * 
         * // 融资会员交易签约，向交易所支付费用 if (!feeReqList.isEmpty()) { financierSigningService.payTradeFeeToExchange(eventId,
         * owner, feeReqList, bizId, operatorId, workDate); }
         */

        if (StringUtils.isNotBlank(pkg.getServFnrJnlNo())) {
            // 解冻发布保证金
            UnReserveReq unReq = new UnReserveReq();
            unReq.setCurrOpId(operatorId);
            unReq.setReserveJnlNo(pkg.getServFnrJnlNo());
            unReq.setTrxMemo(PkgUtils.getPkgIdNameStr(pkg.getId()) + "签约，解冻融资服务合同保证金");
            unReq.setUserId(owner);
            unReq.setWorkDate(workDate);
            BigDecimal entrustAmt = financierSigningService.refundAllEntrustDeposit(unReq);

            // 实际冻结金额 = 冻结金额*实际申购额/融资额
            BigDecimal actualFrezzAmt = entrustAmt.multiply(supplyAmount).divide(packageQuota, saveScale,
                    RoundingMode.DOWN);

            // 冻结发布保证金
            ReserveReq req = new ReserveReq();
            req.setAddXwb(true);
            req.setBizId(bizId);
            req.setCurrOpId(operatorId);
            req.setTrxAmt(actualFrezzAmt);
            req.setTrxMemo(PkgUtils.getPkgIdNameStr(pkg.getId()) + "签约，按实际申购额冻结融资服务合同保证金");
            req.setUserId(owner);
            req.setUseType(EFundUseType.POSTDEPOSIT);
            req.setWorkDate(workDate);
            String servFnrJnlNo = financierEntrustDepositService.payDepositAgain(req);
            pkg.setServFnrJnlNo(servFnrJnlNo);
        }

        if (StringUtils.isNotBlank(pkg.getWrtrFnrJnlNo())) {
            // 解冻担保保证金
            UnReserveReq unReq = new UnReserveReq();
            unReq.setCurrOpId(operatorId);
            unReq.setReserveJnlNo(pkg.getWrtrFnrJnlNo());
            unReq.setTrxMemo(PkgUtils.getPkgIdNameStr(pkg.getId()) + "签约，解冻担保保证金");
            unReq.setUserId(product.getWarrantId());
            unReq.setWorkDate(workDate);
            BigDecimal warrantAmt = warrantDepositService.refundWarrantDeposit(unReq);

            // 实际冻结金额 = 冻结金额*实际申购额/融资额
            BigDecimal actualFrezzAmt = warrantAmt.multiply(supplyAmount).divide(packageQuota, saveScale,
                    RoundingMode.DOWN);

            // 冻结担保保证金
            ReserveReq req = new ReserveReq();
            req.setAddXwb(true);
            req.setBizId(bizId);
            req.setCurrOpId(operatorId);
            req.setTrxAmt(actualFrezzAmt);
            req.setTrxMemo(PkgUtils.getPkgIdNameStr(pkg.getId()) + "签约，按实际申购额冻结担保保证金");
            req.setUserId(product.getWarrantId());
            req.setUseType(EFundUseType.WARRANTDEPOSIT);
            req.setWorkDate(workDate);
            String wrtrFnrJnlNo = warrantDepositService.payDepositAgain(req);
            pkg.setWrtrFnrJnlNo(wrtrFnrJnlNo);
        }

        // 更新融资包状态为待放款审批
        pkg.setStatus(EPackageStatus.WAIT_LOAD_APPROAL);
        pkg.setSignContractTime(new Date());
        pkg.setSigningDt(workDate);
        pkg.setLastOperatorId(operatorId);
        pkg.setLastTime(new Date());
        productPackageRepository.save(pkg);

        // 记录日志
        actionHistoryService.save(EntityType.PRODUCTPACKAGE, packageId, ActionType.PKGSIGN,
                ActionResult.PRODUCT_PACKAGE_SIGN);

        List<PaymentSchedule> schedules = this.addPaymentSchedules(product, pkg, workDate, operatorId);

        // 插入还款计划表
        paymentScheduleRepository.save(schedules);

        List<PkgSubsUnitSumMsgHolder> unitHolders = new ArrayList<PkgSubsUnitSumMsgHolder>();
        List<Object[]> unitResults = packageSubscribesRepository.getUnitSumByPkgIdGroupByUserId(pkg.getId());
        for (Object[] objs : unitResults) {
            if (objs != null && objs.length == 2) {
                PkgSubsUnitSumMsgHolder holder = new PkgSubsUnitSumMsgHolder();
                holder.setUnit(BigDecimal.valueOf(Long.valueOf(objs[0].toString())));
                holder.setUserId(objs[1].toString());
                holder.setPkgId(pkg.getId());
                unitHolders.add(holder);
            }
        }

        List<PositionPo> positions = new ArrayList<PositionPo>();
        for (PkgSubsUnitSumMsgHolder holder : unitHolders) {
            PositionPo position = new PositionPo();
            position.setCreateOpid(operatorId);
            position.setCreateTs(new Date());
            position.setLastMntOpid(operatorId);
            position.setLastMntTs(new Date());
            position.setPkgId(holder.getPkgId());
            position.setUnit(holder.getUnit().intValue());
            position.setUnitFaceValue(pkg.getUnitAmount());
            position.setUserId(holder.getUserId());
            positions.add(position);
        }
        // 新增投资会员产品包头寸
        positions = positionService.savePositions(positions);

        List<PkgMinSubsDtMsgHolder> dtHolders = new ArrayList<PkgMinSubsDtMsgHolder>();
        List<Object[]> dtResults = packageSubscribesRepository.getMinSubsDtByPkgIdGroupByUserId(pkg.getId());
        for (Object[] objs : dtResults) {
            if (objs != null && objs.length == 2) {
                PkgMinSubsDtMsgHolder holder = new PkgMinSubsDtMsgHolder();
                holder.setSubsDt(DateUtils.getDate(objs[0].toString(), "yyyy-MM-dd"));
                holder.setUserId(objs[1].toString());
                holder.setPkgId(pkg.getId());
                dtHolders.add(holder);
            }
        }

        Map<String, Date> dtMap = new HashMap<String, Date>();
        for (PkgMinSubsDtMsgHolder holder : dtHolders) {
            dtMap.put(holder.getUserId(), holder.getSubsDt());
        }

        List<PositionLotPo> lots = new ArrayList<PositionLotPo>();
        List<LoanContract> contracts = new ArrayList<LoanContract>();
        int seqIdx = 1;
        for (PositionPo po : positions) {
            PositionLotPo positionLot = new PositionLotPo();
            AcctPo acct = acctService.getAcctByUserId(po.getUserId());
            positionLot.setPositionId(po.getPositionId());
            positionLot.setCrId(generateCrId(pkg.getId(), seqIdx));
            positionLot.setUnit(po.getUnit());
            positionLot.setLotBuyPrice(BigDecimal.valueOf(po.getUnit()).multiply(po.getUnitFaceValue()));
            positionLot.setAccumCrAmt(BigDecimal.ZERO);
            positionLot.setSubsDt(dtMap.get(po.getUserId()));
            positionLot.setSettleDt(workDate);
            positionLot.setCreateOpid(operatorId);
            positionLot.setCreateTs(new Date());
            positionLot.setLastMntOpid(operatorId);
            positionLot.setLastMntTs(new Date());
            positionLot.setContractId(generateCntrctId(po.getPkgId(), acct.getAcctNo()));
            lots.add(positionLot);
            LoanContract contract = new LoanContract();
            contract.setContractId(positionLot.getContractId());
            contract.setUnit(positionLot.getUnit());
            contract.setCreateOpid(positionLot.getCreateOpid());
            contract.setCreateTs(positionLot.getCreateTs());
            contracts.add(contract);
            seqIdx++;
        }

        // 新增投资会员产品包头寸头寸份额表
        positionLotService.savePositionLots(lots);
        // 借款合同
        loanContractRepository.save(contracts);

        List<PositionLotPo> lotPos = positionLotService.getPositionLotList(pkg.getId());

        List<PkgTradeJnlPo> jnls = new ArrayList<PkgTradeJnlPo>();
        for (PositionLotPo lot : lotPos) {
            PositionPo position = positionRepository.findOne(lot.getPositionId());
            PkgTradeJnlPo pkgTradeJnl = new PkgTradeJnlPo();
            pkgTradeJnl.setBuyerUserId(position.getUserId());
            pkgTradeJnl.setCreateOpid(operatorId);
            pkgTradeJnl.setCreateTs(new Date());
            pkgTradeJnl.setTrdType(EFundTrdType.BONDSUBS);
            pkgTradeJnl.setTrdDt(workDate);
            pkgTradeJnl.setTrdMemo("融资人签约，债权交割");
            pkgTradeJnl.setLotBuyPrice(lot.getLotBuyPrice());
            pkgTradeJnl.setLotId(lot.getLotId());
            pkgTradeJnl.setPkgId(pkg.getId());
            pkgTradeJnl.setCrId(lot.getCrId());
            // 一级市场申购，卖方为null
            pkgTradeJnl.setSellerUserId(null);
            pkgTradeJnl.setUnit(lot.getUnit());
            jnls.add(pkgTradeJnl);
        }
        pkgTradeJnlService.savePkgTradeJnls(jnls);
/*
        ProductContractTemplate template = productContractTemplateService.getProductContractTemplate(product
                .getContractTemplateId().toString());
*/                
        ContractRatePK contractRatePk = new ContractRatePK();
        contractRatePk.setContractId(product.getContractTemplateId().toString());
        contractRatePk.setProductLevelId(product.getProductLevel().getCode());
        ContractRate contractRate = contractRateService.findOne(contractRatePk);
        
        PackageContract packageContract = new PackageContract();
        packageContract.setCreateOpid(operatorId);
        packageContract.setCreateTs(new Date());
        packageContract.setPkgId(pkg.getId());
        if (contractRate.getPlatformPrepaymentPenaltyRate().compareTo(BigDecimal.ZERO) < 0) {
        	// 一个月利息
        	packageContract.setPlatformPrepayPenaltyRt(product.getRate().divide(new BigDecimal(12), 8, RoundingMode.FLOOR));
        } else {
        	packageContract.setPlatformPrepayPenaltyRt(contractRate.getPlatformPrepaymentPenaltyRate());
        }
        if (contractRate.getFinancierPrepaymentPenaltyRate().compareTo(BigDecimal.ZERO) < 0) {
        	// 一个月利息
        	packageContract.setFncrPrepayPenaltyRt(product.getRate().divide(new BigDecimal(12), 8, RoundingMode.FLOOR));
        } else {
        	packageContract.setFncrPrepayPenaltyRt(contractRate.getFinancierPrepaymentPenaltyRate());
        }
        packageContract.setPrePayDeductIntrFlag(contractRate.getPrepayDeductIntrFlg());
        packageContract.setReppayPenaltyFineRt(contractRate.getPaymentPenaltyFineRate());
        packageContractRepository.save(packageContract);

    }

    private String generateCrId(String pkgId, int seq) {
        StringBuffer strs = new StringBuffer();
        strs.append(pkgId);
        strs.append(StringUtils.leftPad(String.valueOf(seq), 3, '0'));
        return strs.toString();
    }

    private String generateCntrctId(String pkgId, String acctNo) {
        StringBuffer strs = new StringBuffer();
        strs.append(pkgId);
        strs.append("-");
        strs.append(acctNo);
        return strs.toString();
    }

    private List<PaymentSchedule> addPaymentSchedules(Product product, ProductPackage pkg, Date workDate,
            String operatorId) {
        List<PaymentSchedule> schedules = new ArrayList<PaymentSchedule>();

        EPayMethodType payMethod = product.getPayMethod();
        BigDecimal termLength = BigDecimal.valueOf(product.getTermLength().longValue());
        ETermType termType = product.getTermType();
        BigDecimal supplyAmount = AmtUtils.processNegativeAmt(pkg.getSupplyAmount(), BigDecimal.ZERO);
        BigDecimal yearRate = AmtUtils.processNegativeAmt(product.getRate(), BigDecimal.ZERO);

        // 应付本金 = 融资包额 / 期数
        BigDecimal principalAmt = BigDecimal.ZERO;
        // 应付利息 = 应付本金 * 月利率
        BigDecimal interestAmt = BigDecimal.ZERO;
        // 利率
        BigDecimal rate = BigDecimal.ZERO;

        // 累计应付本金
        BigDecimal calPrinAmt = BigDecimal.ZERO;
        // 累计应付利息
        BigDecimal calInteAmt = BigDecimal.ZERO;

        switch (payMethod) {
        case MONTH_INTEREST:
            if (termType == ETermType.DAY) {
                // TODO
                throw new BizServiceException(EErrorCode.PROD_TERMTYPE_NOT_MATCH_REPAYMETHOD, "期限类型与还款方式不符");
            }
            BigDecimal size1 = termLength;
            if (termType == ETermType.YEAR) {
                size1 = size1.multiply(BigDecimal.valueOf(12L));
            }
            for (int i = 0; i < size1.intValue(); i++) {
                Calendar paymentDate = Calendar.getInstance();
                paymentDate.setTime(workDate);
                paymentDate.add(Calendar.MONTH, i + 1);
                principalAmt = supplyAmount.divide(size1, saveScale, RoundingMode.DOWN);
                boolean isLast = false;
                if (i == size1.intValue() - 1) {
                    isLast = true;
                    principalAmt = supplyAmount.subtract(calPrinAmt);
                }
                interestAmt = supplyAmount.multiply(yearRate).divide(BigDecimal.valueOf(12L), calcuScale,
                        RoundingMode.DOWN);

                String dateStr = DateUtils.formatDate(paymentDate.getTime(), "yyyy-MM-dd");

                Date payDate = DateUtils.getDate(dateStr, "yyyy-MM-dd");

                PaymentSchedule schedule = this.createPaymentSchedule(product, pkg, BigDecimal.ZERO, interestAmt,
                        supplyAmount, interestAmt, payDate, i + 1, payMethod, isLast, operatorId);
                schedules.add(schedule);

                calPrinAmt = calPrinAmt.add(principalAmt);
                calInteAmt = calInteAmt.add(interestAmt);
            }
            break;
        case MONTH_AVERAGE_INTEREST:
            if (termType == ETermType.DAY) {
                // TODO
                throw new BizServiceException(EErrorCode.PROD_TERMTYPE_NOT_MATCH_REPAYMETHOD, "期限类型与还款方式不符");
            }
            BigDecimal size2 = termLength;
            if (termType == ETermType.YEAR) {
                size2 = size2.multiply(BigDecimal.valueOf(12L));
            }
            rate = yearRate.divide(BigDecimal.valueOf(12L), calcuScale, RoundingMode.DOWN);
            // A*b%*(1+b%)^n
            BigDecimal molecular1 = supplyAmount.multiply(rate)
                    .multiply(BigDecimal.ONE.add(rate).pow(size2.intValue()));
            // (1+b%)^n-1
            BigDecimal denominator = BigDecimal.ONE.add(rate).pow(size2.intValue()).subtract(BigDecimal.ONE);

            BigDecimal perTermAmt = molecular1.divide(denominator, saveScale, RoundingMode.DOWN);
            for (int i = 0; i < size2.intValue(); i++) {
                Calendar paymentDate = Calendar.getInstance();
                paymentDate.setTime(workDate);
                paymentDate.add(Calendar.MONTH, i + 1);
                // A*b%*(1+b%)^(m-1)
                BigDecimal molecular2 = supplyAmount.multiply(rate).multiply(BigDecimal.ONE.add(rate).pow(i));
                principalAmt = molecular2.divide(denominator, saveScale, RoundingMode.DOWN);

                boolean isLast = false;
                if (i == size2.intValue() - 1) {
                    isLast = true;
                    principalAmt = supplyAmount.subtract(calPrinAmt);
                }
                interestAmt = perTermAmt.subtract(principalAmt);

                String dateStr = DateUtils.formatDate(paymentDate.getTime(), "yyyy-MM-dd");

                Date payDate = DateUtils.getDate(dateStr, "yyyy-MM-dd");

                PaymentSchedule schedule = this.createPaymentSchedule(product, pkg, principalAmt, interestAmt,
                        principalAmt, interestAmt, payDate, i + 1, payMethod, isLast, operatorId);
                schedules.add(schedule);

                calPrinAmt = calPrinAmt.add(principalAmt);
                calInteAmt = calInteAmt.add(interestAmt);
            }
            break;
        case MONTH_PRINCIPAL_INTEREST:
            if (termType == ETermType.DAY) {
                // TODO
                throw new BizServiceException(EErrorCode.PROD_TERMTYPE_NOT_MATCH_REPAYMETHOD, "期限类型与还款方式不符");
            }
            BigDecimal size3 = termLength;
            if (termType == ETermType.YEAR) {
                size3 = size3.multiply(BigDecimal.valueOf(12L));
            }
            for (int i = 0; i < size3.intValue(); i++) {
                Calendar paymentDate = Calendar.getInstance();
                paymentDate.setTime(workDate);
                paymentDate.add(Calendar.MONTH, i + 1);
                principalAmt = supplyAmount.divide(size3, saveScale, RoundingMode.DOWN);
                boolean isLast = false;
                if (i == size3.intValue() - 1) {
                    isLast = true;
                    principalAmt = supplyAmount.subtract(calPrinAmt);
                }
                interestAmt = supplyAmount.multiply(yearRate).divide(BigDecimal.valueOf(12L), calcuScale,
                        RoundingMode.DOWN);

                String dateStr = DateUtils.formatDate(paymentDate.getTime(), "yyyy-MM-dd");

                Date payDate = DateUtils.getDate(dateStr, "yyyy-MM-dd");

                PaymentSchedule schedule = this.createPaymentSchedule(product, pkg, principalAmt, interestAmt,
                        principalAmt, interestAmt, payDate, i + 1, payMethod, isLast, operatorId);
                schedules.add(schedule);

                calPrinAmt = calPrinAmt.add(principalAmt);
                calInteAmt = calInteAmt.add(interestAmt);
            }
            break;
        case ONCE_FOR_ALL:
            Calendar paymentDate = Calendar.getInstance();
            paymentDate.setTime(workDate);
            if (termType == ETermType.DAY) {
                paymentDate.add(Calendar.DAY_OF_YEAR, termLength.intValue());
                interestAmt = supplyAmount
                        .multiply(yearRate)
                        .multiply(termLength)
                        .divide(BigDecimal.valueOf(12L).multiply(BigDecimal.valueOf(30L)), calcuScale,
                                RoundingMode.DOWN);
            } else if (termType == ETermType.MONTH) {
                paymentDate.add(Calendar.MONTH, termLength.intValue());
                interestAmt = supplyAmount.multiply(yearRate).multiply(termLength)
                        .divide(BigDecimal.valueOf(12L), calcuScale, RoundingMode.DOWN);
            } else if (termType == ETermType.YEAR) {
                paymentDate.add(Calendar.YEAR, termLength.intValue());
                interestAmt = supplyAmount.multiply(yearRate).multiply(termLength);
            }
            principalAmt = supplyAmount;

            String dateStr = DateUtils.formatDate(paymentDate.getTime(), "yyyy-MM-dd");

            Date payDate = DateUtils.getDate(dateStr, "yyyy-MM-dd");

            PaymentSchedule schedule = this.createPaymentSchedule(product, pkg, BigDecimal.ZERO, BigDecimal.ZERO,
                    supplyAmount, interestAmt, payDate, 1, payMethod, true, operatorId);
            schedules.add(schedule);
            break;
        default:
            break;
        }

        return schedules;

    }

    private PaymentSchedule createPaymentSchedule(Product product, ProductPackage pkg, BigDecimal perPrincipalAmt,
            BigDecimal perInterestAmt, BigDecimal lastPrincipalAmt, BigDecimal lastInterestAmt, Date paymentDate,
            int sequenceId, EPayMethodType payMethod, boolean isLast, String operatorId) {

        BigDecimal subsAmt = AmtUtils.processNegativeAmt(pkg.getSupplyAmount(), BigDecimal.ZERO);

        PaymentSchedule schedule = new PaymentSchedule();
        if (isLast) {
            schedule.setPrincipalAmt(lastPrincipalAmt.setScale(saveScale, RoundingMode.DOWN));
            schedule.setInterestAmt(lastInterestAmt.setScale(saveScale, RoundingMode.DOWN));
            schedule.setLastFlag(EFlagType.YES.getCode());

        } else {
            schedule.setPrincipalAmt(perPrincipalAmt.setScale(saveScale, RoundingMode.DOWN));
            schedule.setInterestAmt(perInterestAmt.setScale(saveScale, RoundingMode.DOWN));
            schedule.setLastFlag(EFlagType.NO.getCode());
        }

        schedule.setPackageId(pkg.getId());

        schedule.setProductId(product.getProductId());

        // 序列号
        schedule.setSequenceId(sequenceId);

        schedule.setPaymentDate(paymentDate);

        // 应付本金罚金
        schedule.setPrincipalForfeit(BigDecimal.ZERO);
        // 应付利息罚金
        schedule.setInterestForfeit(BigDecimal.ZERO);
        // 应付费用罚金
        schedule.setFeeForfeit(BigDecimal.ZERO);
        // 实付本金
        schedule.setPayPrincipalAmt(BigDecimal.ZERO);
        // 实付利息
        schedule.setPayInterestAmt(BigDecimal.ZERO);
        // 实付费用
        schedule.setPayAmt(BigDecimal.ZERO);
        // 实付本金罚金
        schedule.setPayPrincipalForfeit(BigDecimal.ZERO);
        // 实付利息罚金
        schedule.setPayInterestForfeit(BigDecimal.ZERO);
        // 实付费用罚金
        schedule.setPayFeeForfeit(BigDecimal.ZERO);
        // 担保方
        schedule.setWarrantId(product.getWarrantId());
        // 代偿金额
        schedule.setCmpnsPyAmt(BigDecimal.ZERO);
        // 代偿利息
        schedule.setCmpnsFinePyAmt(BigDecimal.ZERO);
        // 实际代偿金额
        schedule.setCmpnsPdAmt(BigDecimal.ZERO);
        // 实际代偿利息
        schedule.setCmpnsFinePdAmt(BigDecimal.ZERO);
        // 应付利息担保逾期代偿罚金
        schedule.setWrtrInterestForfeit(BigDecimal.ZERO);
        // 应付本金担保逾期代偿罚金
        schedule.setWrtrPrinForfeit(BigDecimal.ZERO);
        // 实付利息担保逾期代偿罚金
        schedule.setPayWrtrInterestForfeit(BigDecimal.ZERO);
        // 实付本金担保逾期代偿罚金
        schedule.setPayWrtrPrinForfeit(BigDecimal.ZERO);
        // 本息费用冻结ID
        schedule.setPrinFrzId(null);
        // 代偿欠款冻结ID
        schedule.setCmpnsFrzId(null);
        // 处理时间
        schedule.setPrcdDt(null);
        // 处理信息
        schedule.setPrcdMsg(null);

        // 支付状态
        schedule.setStatus(EPayStatus.NORMAL);

        schedule.setCreateOpid(operatorId);

        schedule.setCreateTs(new Date());

        schedule.setLastMntOpid(operatorId);

        schedule.setLastMntTs(new Date());

        schedule.setFinancerId(product.getApplUserId());

        // 还款期限类型按日还是按年，若是一次性则置为0
        BigDecimal pkgFTotalee = BigDecimal.ZERO;
        List<ProductFee> fees = productFeeRepository.getFeeByProductId(product.getProductId());
        for (ProductFee fee : fees) {
            BigDecimal perFee = BigDecimal.ZERO;
            BigDecimal feeRate = AmtUtils.processNegativeAmt(fee.getFeeRt(), BigDecimal.ZERO);
            ETermType termType = product.getTermType();
            if (EFeePayMethodType.ONCE == fee.getPayMethod()) {
                perFee = BigDecimal.ZERO;
            } else {
                // 每期需还服务费 = 服务费率 * 融资包金额 * 月数
                perFee = feeRate.multiply(subsAmt);
                if (payMethod == EPayMethodType.ONCE_FOR_ALL) {
                    BigDecimal termLe = BigDecimal.valueOf(product.getTermLength());
                    if (termType == ETermType.YEAR) {
                        perFee = perFee.multiply(BigDecimal.valueOf(12L)).multiply(termLe);
                    } else if (termType == ETermType.DAY) {
                        perFee = perFee.multiply(termLe).divide(BigDecimal.valueOf(30L), calcuScale, RoundingMode.DOWN);
                    }else{
                        perFee = perFee.multiply(termLe); 
                    }
                }
            }
            pkgFTotalee = pkgFTotalee.add(perFee);
        }

        // 应付费用 = 每期需还融资服务费 + 每期需还风险管理费
        schedule.setFeeAmt(pkgFTotalee.setScale(saveScale, RoundingMode.DOWN));

        return schedule;

    }
}
