package com.hengxin.platform.product.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.entity.id.IdUtil;
import com.hengxin.platform.common.enums.ActionResult;
import com.hengxin.platform.common.enums.ActionType;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.enums.EntityType;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.fund.dto.biz.req.atom.FreezeReq;
import com.hengxin.platform.fund.dto.biz.req.atom.TransferInfo;
import com.hengxin.platform.fund.dto.biz.req.atom.UnFreezeReq;
import com.hengxin.platform.fund.dto.biz.req.atom.UnReserveReq;
import com.hengxin.platform.fund.entity.PositionLotPo;
import com.hengxin.platform.fund.entity.PositionPo;
import com.hengxin.platform.fund.enums.EFundUseType;
import com.hengxin.platform.fund.util.AmtUtils;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.product.domain.PaymentSchedule;
import com.hengxin.platform.product.domain.Product;
import com.hengxin.platform.product.domain.ProductPackage;
import com.hengxin.platform.product.dto.PkgClearingPaymentAmtDto;
import com.hengxin.platform.product.dto.PkgPaymentClearDto;
import com.hengxin.platform.product.enums.EPackageStatus;
import com.hengxin.platform.product.enums.EPayStatus;
import com.hengxin.platform.product.enums.EPkgPaymentClearType;
import com.hengxin.platform.product.service.MsgHolder.PkgUtils;

@Service
public class ClearingPkgWithRulesService extends ClearingPkgService {

    private final static Logger LOG = LoggerFactory.getLogger(ClearingPkgWithRulesService.class);
    
    @Transactional
	public PkgClearingPaymentAmtDto getClearingPkgAmtInfo(PaymentSchedule schedule){
		BigDecimal prinAmt  = schedule.getPrincipalAmt().subtract(schedule.getPayPrincipalAmt());
		prinAmt = AmtUtils.processNegativeAmt(prinAmt, BigDecimal.ZERO);
		BigDecimal intrAmt = schedule.getInterestAmt().subtract(schedule.getPayInterestAmt());
		intrAmt = AmtUtils.processNegativeAmt(intrAmt, BigDecimal.ZERO);
		ProductPackage pkg = productPackageRepository.getProductPackageById(schedule.getPackageId());
		BigDecimal fnrCanPayAmt = financierBreachFundClearingService.getUserCurrentAvlAmtIgnoreFrzAmt(schedule.getFinancerId(), true);//true:加上小微宝余额
		//BigDecimal fnrCanPayAmt = financierBreachFundClearingService.getUserCurrentAvlAmtIgnoreFrzAmt(schedule.getFinancerId(), false);
		BigDecimal loanDepositAmt = freezeReserveDtlService.getUnFnrAbleAmt(pkg.getLoanFnrJnlNo());
		BigDecimal wrtrDepositAmt = BigDecimal.ZERO;//freezeReserveDtlService.getUnFnrAbleAmt(pkg.getWrtrFnrJnlNo());
		BigDecimal totalCanPaymentAmt = BigDecimal.ZERO;
		totalCanPaymentAmt = totalCanPaymentAmt.add(fnrCanPayAmt);
		if(PkgUtils.isLastSchedule(schedule)){
			totalCanPaymentAmt = totalCanPaymentAmt.add(loanDepositAmt);
		}
		PkgClearingPaymentAmtDto amtDto = new PkgClearingPaymentAmtDto();
		amtDto.setFncrCanPaymentAmt(fnrCanPayAmt);
		amtDto.setIntrAmt(intrAmt);
		amtDto.setLoanDepositAmt(loanDepositAmt);
		amtDto.setPeriod(schedule.getSequenceId());
		amtDto.setPkgId(schedule.getPackageId());
		amtDto.setPrinAmt(prinAmt);
		amtDto.setWrtrDepositAmt(wrtrDepositAmt);
		amtDto.setTotalCanPaymentAmt(totalCanPaymentAmt);
		amtDto.setLastFlag(schedule.getLastFlag());
		return amtDto;
	}
	
	/**
	 * 清分类型参数有误校验
	 * @param ct
	 */
	private void errorInClearingType(EPkgPaymentClearType ct){
        throw new BizServiceException(EErrorCode.TECH_PARAM_REQUIRED, "清分类型参数有误."+ct);
	}

	/**
	 * 违约中清分
	 * @param clearDto
	 * @param operatorId
	 */
	@Transactional
	public void clearingProductPackageInOverdue(PkgPaymentClearDto clearDto, String operatorId) {
		EPkgPaymentClearType ct = clearDto.getClearType();
		switch(ct){
			case AUTO_SHARE:
				super.clearingProductPackageInOverdue(clearDto.getPackageId(), clearDto.getPeriod(), operatorId);
				break;
			case PRIN_CLEAR:
				this.clearingWithRules(clearDto, operatorId);
				break;
			case INTR_CLEAR:
				this.clearingWithRules(clearDto, operatorId);
				break;
			default:
				this.errorInClearingType(ct);
		}
	}

	/**
	 * 已代偿清分
	 * @param clearDto
	 * @param operatorId
	 */
	@Transactional
	public void clearingProductPackageInCompensatory(PkgPaymentClearDto clearDto, String operatorId) {
		EPkgPaymentClearType ct = clearDto.getClearType();
		switch(ct){
			case AUTO_SHARE:
				super.clearingProductPackageInCompensatory(clearDto.getPackageId(), clearDto.getPeriod(), operatorId);
				break;
			case PRIN_CLEAR:
				this.clearingWithRules(clearDto, operatorId);
				break;
			case INTR_CLEAR:
				this.clearingWithRules(clearDto, operatorId);
				break;
			default:
				this.errorInClearingType(ct);
		}
	}

	/**
	 * 代偿中清分
	 * @param clearDto
	 * @param operatorId
	 */
	@Transactional
	public void clearingProductPackageInCompensating(PkgPaymentClearDto clearDto, String operatorId) {
		EPkgPaymentClearType ct = clearDto.getClearType();
		switch(ct){
			case AUTO_SHARE:
				super.clearingProductPackageInCompensating(clearDto.getPackageId(), clearDto.getPeriod(), operatorId);
				break;
			case PRIN_CLEAR:
				this.clearingWithRules(clearDto, operatorId);
				break;
			case INTR_CLEAR:
				this.clearingWithRules(clearDto, operatorId);
				break;
			default:
				this.errorInClearingType(ct);
		}
	}
	
	/**
	 * 可支持的按特殊规则清分方式
	 * @return
	 */
	private List<EPkgPaymentClearType> getSupportableRulesClearingTypes(){
		return Arrays.asList(EPkgPaymentClearType.PRIN_CLEAR, EPkgPaymentClearType.INTR_CLEAR);
	}
	
	/**
	 * 是否本金清分
	 * @param clearType
	 * @return
	 */
	private boolean isPrinClearing(EPkgPaymentClearType clearType){
		return clearType.compareTo(EPkgPaymentClearType.PRIN_CLEAR)==0;
	}
	
	/**
	 * 是否利息清分
	 * @param clearType
	 * @return
	 */
	private boolean isIntrClearing(EPkgPaymentClearType clearType){
		return clearType.compareTo(EPkgPaymentClearType.INTR_CLEAR)==0;
	}
	
	/**
	 * 获取这一期总欠款额
	 * @param schedule
	 * @return
	 */
	private BigDecimal calculateScheduleTotalDebtAmt(PaymentSchedule schedule){
		BigDecimal totalDebtAmt = BigDecimal.ZERO;
		BigDecimal prinAmt = schedule.getPrincipalAmt().subtract(schedule.getPayPrincipalAmt());
		prinAmt = AmtUtils.max(prinAmt, BigDecimal.ZERO);
		BigDecimal prinFineAmt = schedule.getPrincipalForfeit().subtract(schedule.getPayPrincipalForfeit());
		prinFineAmt = AmtUtils.max(prinFineAmt, BigDecimal.ZERO);
		BigDecimal intrAmt = schedule.getInterestAmt().subtract(schedule.getPayInterestAmt());
		intrAmt = AmtUtils.max(intrAmt, BigDecimal.ZERO);
		BigDecimal intrFineAmt = schedule.getInterestForfeit().subtract(schedule.getPayInterestForfeit());
		intrFineAmt = AmtUtils.max(intrFineAmt, BigDecimal.ZERO);
		BigDecimal feeAmt = schedule.getFeeAmt().subtract(schedule.getPayAmt());
		feeAmt = AmtUtils.max(feeAmt, BigDecimal.ZERO);
		BigDecimal feeFineAmt = schedule.getFeeForfeit().subtract(schedule.getPayFeeForfeit());
		feeFineAmt = AmtUtils.max(feeFineAmt, BigDecimal.ZERO);
		BigDecimal wrtrPrinFineAmt = schedule.getWrtrPrinForfeit().subtract(schedule.getPayWrtrPrinForfeit());
		wrtrPrinFineAmt = AmtUtils.max(wrtrPrinFineAmt, BigDecimal.ZERO);
		BigDecimal wrtrIntrFineAmt = schedule.getWrtrInterestForfeit().subtract(schedule.getPayWrtrInterestForfeit());
		wrtrIntrFineAmt = AmtUtils.max(wrtrIntrFineAmt, BigDecimal.ZERO);
		totalDebtAmt = totalDebtAmt.add(prinAmt);
		totalDebtAmt = totalDebtAmt.add(prinFineAmt);
		totalDebtAmt = totalDebtAmt.add(intrAmt);
		totalDebtAmt = totalDebtAmt.add(intrFineAmt);
		totalDebtAmt = totalDebtAmt.add(feeAmt);
		totalDebtAmt = totalDebtAmt.add(feeFineAmt);
		totalDebtAmt = totalDebtAmt.add(wrtrPrinFineAmt);
		totalDebtAmt = totalDebtAmt.add(wrtrIntrFineAmt);
		return totalDebtAmt;
	}
	
	/**
	 * 本期是否可还清
	 * @param schedule
	 * @param paymentAmt
	 * @return
	 */
	private boolean canFinishPkgPayment(PaymentSchedule schedule, BigDecimal paymentAmt){
		BigDecimal totalDebtAmt = this.calculateScheduleTotalDebtAmt(schedule);
		return paymentAmt.compareTo(totalDebtAmt)>=0;
	}
	
	private void clearingWithRules(PkgPaymentClearDto clearDto, String operatorId){
 		String packageId = clearDto.getPackageId();
		Integer period = clearDto.getPeriod();
		EPkgPaymentClearType clearType = clearDto.getClearType();
		BigDecimal clearAmt = clearDto.getClearAmt();
		Date workDate = CommonBusinessUtil.getCurrentWorkDate();
		if(!getSupportableRulesClearingTypes().contains(clearType)){
            throw new BizServiceException(EErrorCode.TECH_DATA_INVALID);
		}
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
        PaymentSchedule schedule = paymentScheduleRepository.getByPackageIdAndSequenceId(packageId, period);
        List<EPayStatus> payStatus = Arrays.asList(EPayStatus.OVERDUE, EPayStatus.COMPENSATING, EPayStatus.COMPENSATORY);
        if (!payStatus.contains(schedule.getStatus())) {
            throw new BizServiceException(EErrorCode.PROD_PKG_STATUS_IS_ERROR);
        }
        
        // 本金欠款金额
        BigDecimal prinAmt = schedule.getPrincipalAmt().subtract(schedule.getPayPrincipalAmt());
        // 利息欠款金额
        BigDecimal intrAmt = schedule.getInterestAmt().subtract(schedule.getPayInterestAmt());
        
        // 融资人此次清分金额
        BigDecimal debtAmt = BigDecimal.ZERO;
        
        if(isPrinClearing(clearType)){
        	debtAmt = debtAmt.add(prinAmt);
        }
        else if(isIntrClearing(clearType)){
        	debtAmt = debtAmt.add(intrAmt);
        }
        
        if(debtAmt.compareTo(BigDecimal.ZERO)<=0){
        	throw new BizServiceException(EErrorCode.TECH_DATA_INVALID, "还款计划上欠款金额项应该大于0.00");
        }
        
        // 获取产品信息
        Product product = productRepository.findByProductId(productPackage.getProductId());

        // 融资人
        String owner = product.getApplUserId();
        
        //最后一期清分，解冻借款合同履约保证金
        if (PkgUtils.isLastSchedule(schedule)) {
            boolean fnrActive = freezeReserveDtlService.isfnrDtlActive(productPackage.getLoanFnrJnlNo());
            if (fnrActive) {
                UnReserveReq urReq = new UnReserveReq();
                urReq.setCurrOpId(operatorId);
                urReq.setReserveJnlNo(productPackage.getLoanFnrJnlNo());
                urReq.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "_" + period + "最后一期清分，解冻借款合同履约保证金");
                urReq.setUserId(owner);
                urReq.setWorkDate(workDate);
                // 解冻借款合同履约保证金
                financierBreachFundClearingService.refundLoadnHonourAgtDeposit(urReq);
            }
        }
        
        //清分，还款违约资金解冻
        if (freezeReserveDtlService.isfnrDtlActive(schedule.getPrinFrzId())) {
            UnFreezeReq ufReq = new UnFreezeReq();
            ufReq.setCurrOpId(operatorId);
            ufReq.setFreezeJnlNo(schedule.getPrinFrzId());
            ufReq.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "_" + period + "清分，还款违约资金解冻");
            ufReq.setUserId(owner);
            ufReq.setWorkDate(workDate);

            // 还款违约资金解冻
            financierBreachFundClearingService.unFreezeLateFee(ufReq);
        }

        // 获取会员可还款金额
        BigDecimal canPaymentAmt = AmtUtils.processNegativeAmt(
                financierBreachFundClearingService.getUserCurrentAvlAmtIgnoreFrzAmt(owner, true), BigDecimal.ZERO);
        
        // 此次应还款金额计算， min(min(清分金额，此期还款金额项的欠款金额),融资人账户的还款金额)
        BigDecimal currPaymentAmt = AmtUtils.min(clearAmt, debtAmt);
        currPaymentAmt = AmtUtils.min(currPaymentAmt, canPaymentAmt);
        if(currPaymentAmt.compareTo(BigDecimal.ZERO)<=0){
        	throw new BizServiceException(EErrorCode.TECH_DATA_INVALID, "此次计算的清分金额应该大于0.00");
        }
        boolean canFinish = this.canFinishPkgPayment(schedule, currPaymentAmt);
        boolean needToUpdatePositionToZero = super.needToUpdatePositionToZero(schedule);
        // 根据融资包编号查询投资人头寸
        List<PositionPo> positions = positionService.getByPkgId(packageId);
        
        // 定义付款人列表
        List<TransferInfo> payerList = new ArrayList<TransferInfo>();
        // 定义收款人列表
        List<TransferInfo> payeeList = new ArrayList<TransferInfo>();
        // 投资人付利息，利息罚金收益
        List<TransferInfo> profPayerList = new ArrayList<TransferInfo>();
        // 平台收取投资人付利息，利息罚金收益
        List<TransferInfo> profPayeeList = new ArrayList<TransferInfo>();

        // 清分时平台费用扣除比例
        BigDecimal deductRate = AmtUtils.processNegativeAmt(CommonBusinessUtil.getClearingInterestDeductRate(),
                BigDecimal.ZERO);

        // 实际申购份数
        BigDecimal supplyUnit = productPackage.getSupplyAmount().divide(productPackage.getUnitAmount(), calcuScale,
                RoundingMode.DOWN);
        
        BigDecimal prinBuyer = BigDecimal.ZERO;
        BigDecimal intrBuyer = BigDecimal.ZERO;
        boolean isPrinClear = isPrinClearing(clearType);
        boolean isIntrClear = isIntrClearing(clearType);
        if(isPrinClear){
        	prinBuyer = prinBuyer.add(currPaymentAmt);
        }
        if(isIntrClear){
        	intrBuyer = intrBuyer.add(currPaymentAmt);
        }
        
        String eventId = IdUtil.produce();
        String bizId = PkgUtils.formatBizId(packageId, period);
        
        // 投资会员实际收到的总金额
        BigDecimal buyerRealTotalRecvAmt = BigDecimal.ZERO;
        // 平台实际收到投资人利息、利息罚金收益比例金额
        BigDecimal platRealInvsTotalPrfAmt = BigDecimal.ZERO;
        // 平台实际收到的总金额
        BigDecimal platRealRecvTotalAmt = BigDecimal.ZERO;
        // 还款计算误差金额
        BigDecimal repayDeviationAmt = BigDecimal.ZERO;
        
        // 按头寸处理投资人收款
        for (PositionPo pos : positions) {
            BigDecimal prinBuyerAc = prinBuyer.multiply(BigDecimal.valueOf(pos.getUnit().longValue())).divide(supplyUnit,
                    saveScale, RoundingMode.DOWN);
            BigDecimal intrBuyerAc = intrBuyer.multiply(BigDecimal.valueOf(pos.getUnit().longValue())).divide(supplyUnit,
                    saveScale, RoundingMode.DOWN);
            // 此投资人实际收到金额
            BigDecimal buyerRealRecvAmt = prinBuyerAc.add(intrBuyerAc);
            // 此次累计实际将付给投资人的总金额
            buyerRealTotalRecvAmt = buyerRealTotalRecvAmt.add(buyerRealRecvAmt);
            // 此投资人利息收益部分的一定比例划转至平台的金额
            BigDecimal toPlatPrfAmt = intrBuyerAc.multiply(deductRate).setScale(saveScale, RoundingMode.DOWN);
            // 此次累计投资人实际将付给平台的总收益金额
            platRealInvsTotalPrfAmt = platRealInvsTotalPrfAmt.add(toPlatPrfAmt);

            if (prinBuyerAc.compareTo(BigDecimal.ZERO) > 0) {
                TransferInfo prinReq = new TransferInfo();
                prinReq.setUserId(pos.getUserId());
                prinReq.setRelZQ(true);
                prinReq.setTrxAmt(prinBuyerAc);
                prinReq.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "_" + period + "清分，收到本金" + prinBuyerAc + "元");
                prinReq.setUseType(EFundUseType.PRIN_REPAYMENT);
                payeeList.add(prinReq);
            }

            if (intrBuyerAc.compareTo(BigDecimal.ZERO) > 0) {
                TransferInfo intrReq = new TransferInfo();
                intrReq.setUserId(pos.getUserId());
                intrReq.setRelZQ(false);
                intrReq.setTrxAmt(intrBuyerAc);
                intrReq.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "_" + period + "清分，收到利息" + intrBuyerAc + "元");
                intrReq.setUseType(EFundUseType.INTR_REPAYMENT);
                payeeList.add(intrReq);
            }

            if (toPlatPrfAmt.compareTo(BigDecimal.ZERO) > 0) {
                TransferInfo prfReq = new TransferInfo();
                prfReq.setUserId(pos.getUserId());
                prfReq.setRelZQ(false);
                prfReq.setTrxAmt(toPlatPrfAmt);
                prfReq.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "_" + period + "清分，平台收取利息、利息罚金收益比例金额为" + toPlatPrfAmt + "元");
                prfReq.setUseType(EFundUseType.PROFITINVS2EXCH);
                profPayerList.add(prfReq);
            }

            // 头寸份额
            List<PositionLotPo> lots = pos.getPositionLotPos();
            // 份额累计分配金额
            BigDecimal accurAssignAmt = BigDecimal.ZERO;
            int size = lots.size();
            for (int i = 0; i < size; i++) {
                PositionLotPo lot = lots.get(i);
                if (lot.getUnit() <= 0) {
                    // 已转让的忽略
                    continue;
                }
                BigDecimal oldAccumCrAmt = AmtUtils.processNegativeAmt(lot.getAccumCrAmt(), BigDecimal.ZERO);
                BigDecimal realRecvAmt = buyerRealRecvAmt;
                BigDecimal lotAddAmt = realRecvAmt.multiply(BigDecimal.valueOf(lot.getUnit().longValue()))
                        .divide(BigDecimal.valueOf(pos.getUnit().longValue()), saveScale, RoundingMode.DOWN);
                accurAssignAmt = accurAssignAmt.add(lotAddAmt);
                if ((size - 1) != i) {
                    lot.setAccumCrAmt(oldAccumCrAmt.add(lotAddAmt));
                } 
                else {
                    // 份额分配金额误差金额 = 投资人实收金额 - 已分配金额, 误差金额加入最后一个份额
                    BigDecimal assignErrorAmt = AmtUtils.max(realRecvAmt.subtract(accurAssignAmt), BigDecimal.ZERO);
                    lot.setAccumCrAmt(oldAccumCrAmt.add(lotAddAmt).add(assignErrorAmt));
                }
                if (canFinish && needToUpdatePositionToZero) {
                    // 最后一期，份额的份数清零
                    lot.setUnit(BigDecimal.ZERO.intValue());
                }
                lot.setLastMntOpid(operatorId);
                lot.setLastMntTs(workDate);
            }
            // 更新头寸份额表
            positionLotService.savePositionLots(lots);

            // 如果是最后一期，则更新头寸份额头寸表
            if (canFinish && needToUpdatePositionToZero) {
                pos.setUnit(BigDecimal.ZERO.intValue());
                pos.setLastMntOpid(operatorId);
                pos.setLastMntTs(workDate);
                positionService.savePositions(Arrays.asList(pos));
            }
        }

        // 获取平台ID
        String platformUserId = CommonBusinessUtil.getExchangeUserId();
        // 投资人利息，利息罚金收益部分划转至平台金额
        BigDecimal intrProfitToPlat = platRealInvsTotalPrfAmt;
        if (intrProfitToPlat.compareTo(BigDecimal.ZERO) > 0) {
            TransferInfo prfReq = new TransferInfo();
            prfReq.setUserId(platformUserId);
            prfReq.setRelZQ(false);
            prfReq.setTrxAmt(intrProfitToPlat);
            prfReq.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "_" + period + "清分，平台收到投资人利息收益部分金额：" + intrProfitToPlat + "元");
            prfReq.setUseType(EFundUseType.PROFITINVS2EXCH);
            profPayeeList.add(prfReq);
        }
        // 还款误差金额 
        repayDeviationAmt = AmtUtils.processNegativeAmt(currPaymentAmt.subtract(buyerRealTotalRecvAmt), BigDecimal.ZERO);
        if (repayDeviationAmt.compareTo(BigDecimal.ZERO) > 0) {
            // 还款误差
            TransferInfo deviationReq = new TransferInfo();
            deviationReq.setUserId(CommonBusinessUtil.getExchangeUserId());
            deviationReq.setRelZQ(false);
            deviationReq.setTrxAmt(repayDeviationAmt);
            deviationReq.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "_" + period + "清分，收到还款计算误差金额" + repayDeviationAmt + "元");
            deviationReq.setUseType(EFundUseType.REPAYAMTOFERROR2EXCH);
            payeeList.add(deviationReq);
        }
        // 融资人此次的还款金额
        if (currPaymentAmt.compareTo(BigDecimal.ZERO) > 0) {
            TransferInfo dInfo = new TransferInfo();
            dInfo.setUserId(owner);
            dInfo.setRelZQ(true);
            dInfo.setTrxAmt(currPaymentAmt);
            dInfo.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "_" + period + "清分， 支付还款金额" + currPaymentAmt + "元");
            dInfo.setUseType(EFundUseType.FNCR_REPAYMENT);
            payerList.add(dInfo);
        }
        // 平台实际收到金额
        platRealRecvTotalAmt = platRealRecvTotalAmt.add(intrProfitToPlat).add(repayDeviationAmt);
        if (LOG.isDebugEnabled()) {
        	
            LOG.debug("平台收取总金额为" + platRealRecvTotalAmt + "元");

            BigDecimal payTotal = BigDecimal.ZERO;
            for (TransferInfo payer : payerList) {
                LOG.debug(payer.getUseType() + "--->" + payer.getTrxAmt());
                payTotal = payTotal.add(payer.getTrxAmt());
            }

            BigDecimal recvTotal = BigDecimal.ZERO;
            for (TransferInfo payee : payeeList) {
                LOG.debug(payee.getUseType() + "--->" + payee.getTrxAmt());
                recvTotal = recvTotal.add(payee.getTrxAmt());
            }
            LOG.debug("总付款金额为--->" + payTotal);
            LOG.debug("总收款金额为--->" + recvTotal);
        }
        
        if(canFinish){
        	schedule.setStatus(EPayStatus.FINISH);
        	if(isPrinClear){
        		schedule.setPayPrincipalAmt(schedule.getPrincipalAmt());
        	}
        	if(isIntrClear){
        		schedule.setPayInterestAmt(schedule.getInterestAmt());
        	}
        	if (PkgUtils.isLastSchedule(schedule)) {
	            // 退担保保证金
	        	boolean fnrActive = freezeReserveDtlService
	                    .isfnrDtlActive(productPackage.getWrtrFnrJnlNo());
	            if (fnrActive) {
	                UnReserveReq uReq = new UnReserveReq();
	                uReq.setCurrOpId(operatorId);
	                uReq.setReserveJnlNo(productPackage.getWrtrFnrJnlNo());
	                uReq.setTrxMemo(PkgUtils.getPkgIdNameStr(productPackage.getId()) + "_" + period + "期清分，担保保证金退回");
	                uReq.setUserId(product.getWarrantId());
	                uReq.setWorkDate(workDate);
	                warrantDepositService.refundWarrantDeposit(uReq);
	            }
        	}
        }
        else{
        	if(isPrinClear){
        		schedule.setPayPrincipalAmt(schedule.getPayPrincipalAmt().add(currPaymentAmt));
        	}
        	if(isIntrClear){
        		schedule.setPayInterestAmt(schedule.getPayInterestAmt().add(currPaymentAmt));
        	}
            // 计算此时还款之后，剩余欠款金额
            BigDecimal surplusDebtAmt = this.calculateScheduleTotalDebtAmt(schedule);
            if(surplusDebtAmt.compareTo(BigDecimal.ZERO)>0){
    	        // 重新冻结一笔新的欠款明细
    	        FreezeReq fReq = new FreezeReq();
    	        fReq.setBizId(bizId);
    	        fReq.setCurrOpId(operatorId);
    	        fReq.setTrxAmt(surplusDebtAmt);
    	        fReq.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "_" + period + "清分，部分还款，冻结新的欠款金额为" + surplusDebtAmt + "元");
    	        fReq.setUserId(owner);
    	        fReq.setUseType(EFundUseType.FNCR_REPAYMENT_PENALTY);
    	        fReq.setWorkDate(workDate);
    	        // 冻结欠款金额
    	        String prinFrzId = financierBreachFundClearingService.freezeArrearageAmt(fReq);
    	        schedule.setPrinFrzId(prinFrzId);
            }
        }
        schedule.setLastMntOpid(operatorId);
        schedule.setLastMntTs(new Date());
        schedule.setLastPayTs(DateUtils.formatYYYYMMDDHHmmss(workDate));

        // 资金太小，平摊金额计算结果为0.00，不需要执行清分操作
        boolean doClearing = false;
        if (!payerList.isEmpty() && !payeeList.isEmpty()) {
            doClearing = true;
        }

        if (doClearing) {
            // 代偿，资金清分
            financierBreachFundClearingService.breachRepaymentFundClearing(eventId, payerList, payeeList, true, bizId,
            		packageId, String.valueOf(period), operatorId, workDate);
            
            if (!profPayerList.isEmpty() && !profPayeeList.isEmpty()) {
                financierBreachFundClearingService.payInvsIntrProfitToExchange(eventId, profPayerList, profPayeeList,
                        bizId, packageId, String.valueOf(period), operatorId, workDate);
            }
            // 更新还款计划表
            paymentScheduleRepository.save(schedule);
            paymentScheduleRepository.flush();
            // 清分结束，如果还款计划都为结束的时候，包的状态更新为结束
            productPackageRepository.updatePkgStatusToEnd(packageId, EPackageStatus.PAYING.getCode(),
                    EPackageStatus.END.getCode(), EPayStatus.FINISH.getCode(), operatorId, new Date());
            // 记录日志
            actionHistoryService.save(EntityType.PRODUCTPACKAGE, packageId, ActionType.PKGCLEANING,
                    ActionResult.PRODUCT_PACKAGE_CLEANING);
        }
	}
	
}
