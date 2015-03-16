/*
 * Project Name: kmfex-platform
 * File Name: PrepayPkgService.java
 * Class Name: PrepayPkgService
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
import java.util.Date;
import java.util.List;

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
import com.hengxin.platform.fund.service.PositionLotService;
import com.hengxin.platform.fund.service.PrepaymentService;
import com.hengxin.platform.fund.service.WarrantDepositService;
import com.hengxin.platform.fund.service.atom.PositionService;
import com.hengxin.platform.fund.util.AmtUtils;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.product.ProdPkgUtil;
import com.hengxin.platform.product.domain.PackageContract;
import com.hengxin.platform.product.domain.PaymentSchedule;
import com.hengxin.platform.product.domain.Product;
import com.hengxin.platform.product.domain.ProductFee;
import com.hengxin.platform.product.domain.ProductPackage;
import com.hengxin.platform.product.enums.EFeePayMethodType;
import com.hengxin.platform.product.enums.EPackageStatus;
import com.hengxin.platform.product.enums.EPayStatus;
import com.hengxin.platform.product.enums.ETermType;
import com.hengxin.platform.product.repository.PackageContractRepository;
import com.hengxin.platform.product.repository.PaymentScheduleRepository;
import com.hengxin.platform.product.repository.ProductContractTemplateRepository;
import com.hengxin.platform.product.repository.ProductFeeRepository;
import com.hengxin.platform.product.repository.ProductPackageRepository;
import com.hengxin.platform.product.repository.ProductRepository;
import com.hengxin.platform.product.service.MsgHolder.PayScheMsgHolder;
import com.hengxin.platform.product.service.MsgHolder.PkgUtils;
import com.hengxin.platform.product.service.MsgHolder.PrePayPkgMsgHolder;
import com.hengxin.platform.product.service.MsgHolder.req.ExchagePayeeListBuildReq;
import com.hengxin.platform.product.service.MsgHolder.req.InvestorPayeeListBuildReq;

/**
 * 融资包提前还款服务 Class Name: PrepayPkgService
 * 
 * @author tingwang
 * 
 */
@Service
public class PrepayPkgService {

    @Autowired
    ActionHistoryService actionHistoryService;
    @Autowired
    PositionService positionService;
    @Autowired
    PaymentScheduleRepository paymentScheduleRepository;
    @Autowired
    PrepaymentService prepaymentService;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    FundPositionService fundPositionService;
    @Autowired
    ProductPackageRepository productPackageRepository;
    @Autowired
    ProductContractTemplateRepository productContractTemplateRepository;
    @Autowired
    FundAcctBalService fundAcctBalService;
    @Autowired
    FreezeReserveDtlService freezeReserveDtlService;
    @Autowired
    WarrantDepositService warrantDepositService;
    @Autowired
    JobWorkService jobWorkService;
    @Autowired
    AcctService acctService;
    @Autowired
    PackageContractRepository packageContractRepository;
    @Autowired
    PositionLotService positionLotService;
    @Autowired
    ProductFeeRepository productFeeRepository;

    public static final int calcuScale = 20;
    public static final int saveScale = 2;
    
    // 风险管理费
    public static final Integer RISK_MGT_FEE = Integer.valueOf(1);
    // 融资服务费
    public static final Integer LOAN_SERV_FEE = Integer.valueOf(2);
    // 担保费
    public static final Integer WRTR_FEE = Integer.valueOf(3);

    /**
     * 还款中：管理员对融资包提前还款
     * 
     * @param packageId
     * @param operatorId
     * @param operateDate
     */
    @Transactional
    public void prepayProductPackageByManager(String packageId, String operatorId) {
        Date workDate = CommonBusinessUtil.getCurrentWorkDate();
        String eventId = IdUtil.produce();
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
        // 检索融资包违约还款的期次
        List<PaymentSchedule> dueSchedules = paymentScheduleRepository.getByPackageIdAndStatusIn(packageId,
                Arrays.asList(EPayStatus.OVERDUE, EPayStatus.COMPENSATING, EPayStatus.COMPENSATORY, EPayStatus.BADDEBT));

        if (!dueSchedules.isEmpty()) {
            throw new BizServiceException(EErrorCode.PROD_PKG_HAS_BREACH_TERM);
        }
        List<PaymentSchedule> schedules = paymentScheduleRepository.getByPackageIdAndStatusOrderBySequenceIdAsc(
                packageId, EPayStatus.NORMAL);
        if (schedules.isEmpty()) {
            throw new BizServiceException(EErrorCode.PROD_PKG_HAS_BEEN_DISPOSED);
        }
        // 还款计划表为最后一期
        if (StringUtils.equals(schedules.get(0).getLastFlag(), EFlagType.YES.getCode())) {
            throw new BizServiceException(EErrorCode.PROD_PKG_IS_LAST_TERM);
        }

        Product product = productRepository.findByProductId(productPackage.getProductId());

        // 融资人
        String owner = product.getApplUserId();

        // 固化模板
        PackageContract contract = packageContractRepository.findOne(productPackage.getId());

        UnReserveReq uReq = new UnReserveReq();
        uReq.setCurrOpId(operatorId);
        uReq.setReserveJnlNo(productPackage.getLoanFnrJnlNo());
        uReq.setTrxMemo(PkgUtils.getPkgIdNameStr(productPackage.getId()) + "提前还款，融资服务合同保证金解冻");
        uReq.setUserId(owner);
        uReq.setWorkDate(workDate);
        prepaymentService.unReserveMargin(uReq);

        BigDecimal financerTotalNeedPay = getFinancerTotalNeedPay(schedules, contract);

        // 融资人可用余额（加小微宝）
        BigDecimal acctAvlBal = fundAcctBalService.getUserCurrentAcctAvlBal(owner, true);

        if (acctAvlBal.compareTo(financerTotalNeedPay) < 0) {
            sendMessage(EErrorCode.ACCT_NOT_ENOUGH_AVL_BAL.getDisplayMsg(), owner, financerTotalNeedPay);
            AcctPo acctPo = acctService.getAcctByUserId(owner);
            throw new AvlBalNotEnoughException(acctPo.getAcctNo(), "账户可用余额不足");
        }
        
        int lastSeqIds = 0;

        for (int i = 0; i < schedules.size(); i++) {

            PaymentSchedule schedule = schedules.get(i);
            
            if(i>0){
            	lastSeqIds++;
            }
            
            String bizId = getBizId(schedule);

            PrePayPkgMsgHolder holder = getPrePayPkgMsgHolder(schedule, productPackage, product, owner, operatorId,
                    workDate, false, i, contract);

            if (!holder.getPayerList().isEmpty() && !holder.getPayeeList().isEmpty()) {
                // 按比例发放融资还款
                prepaymentService.prepayment(eventId, holder.getPayerList(), holder.getPayeeList(), bizId, 
                		schedule.getPackageId(), String.valueOf(schedule.getSequenceId()), operatorId, workDate);
            }
            if (!holder.getProfPayerList().isEmpty() && !holder.getProfPayeeList().isEmpty()) {
                prepaymentService.payInvsIntrProfitToExchange(eventId, 
                		holder.getProfPayerList(), holder.getProfPayeeList(), bizId, 
                		schedule.getPackageId(), String.valueOf(schedule.getSequenceId()), operatorId, workDate);
            }

            schedule.setStatus(EPayStatus.FINISH);
            schedule.setPayPrincipalAmt(schedule.getPrincipalAmt());
            schedule.setPayInterestAmt(BigDecimal.ZERO);
            if(i==0){
            	schedule.setPayAmt(schedule.getFeeAmt());
            }else{
	            // 除当期以外，剩余期数的费用免除， 应付设置为 已付
	            schedule.setFeeAmt(schedule.getPayAmt());
            }
            schedule.setPayPrincipalForfeit(BigDecimal.ZERO);
            schedule.setPayInterestForfeit(BigDecimal.ZERO);
            schedule.setPayFeeForfeit(BigDecimal.ZERO);
            schedule.setPrcdMsg("提前还款");
            schedule.setLastMntOpid(operatorId);
            schedule.setLastMntTs(new Date());
            schedule.setLastPayTs(DateUtils.formatYYYYMMDDHHmmss(workDate));
            if (i == 0 && StringUtils.equals(contract.getPrePayDeductIntrFlag(), EFlagType.YES.getCode())) {
                schedule.setPayInterestAmt(schedule.getInterestAmt());
            }else{
            	schedule.setInterestAmt(schedule.getPayInterestAmt());
            }
        }

        // 更新还款计划表状态为已结束
        paymentScheduleRepository.save(schedules);
        paymentScheduleRepository.flush();

        if (StringUtils.isNotBlank(productPackage.getWrtrFnrJnlNo())) {
            // 退担保保证金
            UnReserveReq uReq2 = new UnReserveReq();
            uReq2.setCurrOpId(operatorId);
            uReq2.setReserveJnlNo(productPackage.getWrtrFnrJnlNo());
            uReq2.setTrxMemo(PkgUtils.getPkgIdNameStr(productPackage.getId()) + "提前还款，担保保证金退回");
            uReq2.setUserId(product.getWarrantId());
            uReq2.setWorkDate(workDate);
            warrantDepositService.refundWarrantDeposit(uReq2);
        }
        
        if(lastSeqIds>0){
	        // 返还一次性付的费用
	        refundOncePaidFeeAfterPrePay(productPackage.getProductId(), packageId, lastSeqIds, operatorId);
        }
        // 如果还款计划都为结束的时候，包的状态更新为结束
        productPackageRepository.updatePkgStatusToEnd(packageId, EPackageStatus.PAYING.getCode(),
                EPackageStatus.END.getCode(), EPayStatus.FINISH.getCode(), operatorId, new Date());

        // 记录日志
        actionHistoryService.save(EntityType.PRODUCTPACKAGE, packageId, ActionType.PKGPREPAY,
                ActionResult.PRODUCT_PACKAGE_PREPAY);
    }

    /**
     * 调用信息接口发信息
     * 
     * @param errMsg
     * @param owner
     * @param totalTrxAmt
     */
    private void sendMessage(String errMsg, String owner, BigDecimal totalTrxAmt) {
        // TODO Auto-generated method stub

    }

    /**
     * 还款中：融资人对融资包提前还款
     * 
     * @param packageId
     * @param operatorId
     */
    @Transactional
    public void prepayProductPackageByFinancer(String packageId, String operatorId) {
        Date workDate = CommonBusinessUtil.getCurrentWorkDate();
        String eventId = IdUtil.produce();
        if (jobWorkService.isBatchBizTaskProcessing()) {
            throw new BizServiceException(EErrorCode.BATCH_EXEC_PROCESSING_WAITING);
        }
        if (StringUtils.isBlank(packageId)) {
            throw new BizServiceException(EErrorCode.TECH_PARAM_REQUIRED);
        }
        if (StringUtils.isBlank(operatorId)) {
            throw new BizServiceException(EErrorCode.TECH_PARAM_REQUIRED);
        }
        // 系统闭市 之后，批量任务执行成功之前，不允许会员做提前还款操作
        if (!CommonBusinessUtil.isMarketOpen() 
        		&& jobWorkService.existUnSucceedTasks(Arrays.asList(ETaskGroupId.REPAYMENT.getCode(),
        				ETaskGroupId.BIZTASK.getCode()))){
            throw new BizServiceException(EErrorCode.BATCH_EXEC_PROCESSING_WAITING);
        }
        ProductPackage productPackage = productPackageRepository.getProductPackageById(packageId);
        if (productPackage == null) {
            throw new BizServiceException(EErrorCode.TECH_DATA_NOT_EXIST);
        }
        if (!StringUtils.equals(productPackage.getStatus().getCode(), EPackageStatus.PAYING.getCode())) {
            throw new BizServiceException(EErrorCode.PROD_PKG_STATUS_IS_NOT_PAYING);
        }
        // 检索融资包违约还款的期次
        List<PaymentSchedule> dueSchedules = paymentScheduleRepository.getByPackageIdAndStatusIn(packageId,
                Arrays.asList(EPayStatus.OVERDUE, EPayStatus.COMPENSATING, EPayStatus.COMPENSATORY, EPayStatus.BADDEBT));

        if (!dueSchedules.isEmpty()) {
            throw new BizServiceException(EErrorCode.PROD_PKG_HAS_BREACH_TERM);
        }
        List<PaymentSchedule> schedules = paymentScheduleRepository.getByPackageIdAndStatusOrderBySequenceIdAsc(
                packageId, EPayStatus.NORMAL);
        if (schedules.isEmpty()) {
            throw new BizServiceException(EErrorCode.PROD_PKG_HAS_BEEN_DISPOSED);
        }
        // 还款计划表为最后一期
        if (StringUtils.equals(schedules.get(0).getLastFlag(), EFlagType.YES.getCode())) {
            throw new BizServiceException(EErrorCode.PROD_PKG_IS_LAST_TERM);
        }

        Product product = productRepository.findByProductId(productPackage.getProductId());

        // 融资人
        String owner = product.getApplUserId();

        // 固化模板
        PackageContract contract = packageContractRepository.findOne(productPackage.getId());

        UnReserveReq uReq = new UnReserveReq();
        uReq.setCurrOpId(operatorId);
        uReq.setReserveJnlNo(productPackage.getLoanFnrJnlNo());
        uReq.setTrxMemo(PkgUtils.getPkgIdNameStr(productPackage.getId()) + "提前还款，借款履约合同保证金解冻");
        uReq.setUserId(owner);
        uReq.setWorkDate(workDate);
        prepaymentService.unReserveMargin(uReq);

        BigDecimal financerTotalNeedPay = getFinancerTotalNeedPay(schedules, contract);

        // 融资人可用余额（加小微宝）
        BigDecimal acctAvlBal = fundAcctBalService.getUserCurrentAcctAvlBal(owner, true);

        if (acctAvlBal.compareTo(financerTotalNeedPay) < 0) {
            sendMessage(EErrorCode.ACCT_NOT_ENOUGH_AVL_BAL.getDisplayMsg(), owner, financerTotalNeedPay);
            AcctPo acctPo = acctService.getAcctByUserId(owner);
            throw new AvlBalNotEnoughException(acctPo.getAcctNo(), "账户可用余额不足");
        }
        
        int lastSeqIds = 0;

        for (int i = 0; i < schedules.size(); i++) {

            PaymentSchedule schedule = schedules.get(i);
            
            if(i>0){
            	lastSeqIds++;
            }
            
            String bizId = getBizId(schedule);

            PrePayPkgMsgHolder holder = getPrePayPkgMsgHolder(schedule, productPackage, product, owner, operatorId,
                    workDate, true, i, contract);

            if (!holder.getPayerList().isEmpty() && !holder.getPayeeList().isEmpty()) {
                // 按比例发放融资还款
                prepaymentService.prepayment(eventId, holder.getPayerList(), holder.getPayeeList(), bizId, 
                		schedule.getPackageId(), String.valueOf(schedule.getSequenceId()), operatorId, workDate);
            }
            if (!holder.getProfPayerList().isEmpty() && !holder.getProfPayeeList().isEmpty()) {
                prepaymentService.payInvsIntrProfitToExchange(eventId, 
                		holder.getProfPayerList(), holder.getProfPayeeList(), 
                		bizId, schedule.getPackageId(), String.valueOf(schedule.getSequenceId()), operatorId, workDate);
            }

            schedule.setStatus(EPayStatus.FINISH);
            schedule.setPayPrincipalAmt(schedule.getPrincipalAmt());
            schedule.setPayInterestAmt(BigDecimal.ZERO);
            if(i==0){
            	schedule.setPayAmt(schedule.getFeeAmt());
            }else{
	            // 除当期以外，剩余期数的费用免除， 应付设置为 已付
	            schedule.setFeeAmt(schedule.getPayAmt());
            }
            schedule.setPayPrincipalForfeit(BigDecimal.ZERO);
            schedule.setPayInterestForfeit(BigDecimal.ZERO);
            schedule.setPayFeeForfeit(BigDecimal.ZERO);
            schedule.setPrcdMsg("提前还款");
            schedule.setLastMntOpid(operatorId);
            schedule.setLastMntTs(new Date());
            schedule.setLastPayTs(DateUtils.formatYYYYMMDDHHmmss(workDate));
            if (i == 0 && StringUtils.equals(contract.getPrePayDeductIntrFlag(), EFlagType.YES.getCode())) {
                schedule.setPayInterestAmt(schedule.getInterestAmt());
            }else{
            	schedule.setInterestAmt(schedule.getPayInterestAmt());
            }
        }

        // 更新还款计划表状态为已结束
        paymentScheduleRepository.save(schedules);
        paymentScheduleRepository.flush();

        if (StringUtils.isNotBlank(productPackage.getWrtrFnrJnlNo())) {
            // 退担保保证金
            UnReserveReq uReq2 = new UnReserveReq();
            uReq2.setCurrOpId(operatorId);
            uReq2.setReserveJnlNo(productPackage.getWrtrFnrJnlNo());
            uReq2.setTrxMemo(PkgUtils.getPkgIdNameStr(productPackage.getId()) + "提前还款，担保保证金退回");
            uReq2.setUserId(product.getWarrantId());
            uReq2.setWorkDate(workDate);
            warrantDepositService.refundWarrantDeposit(uReq2);
        }
        
        if(lastSeqIds>0){
        	// 返还一次性付的费用
        	refundOncePaidFeeAfterPrePay(productPackage.getProductId(), packageId, lastSeqIds, operatorId);
        }
        
        // 如果还款计划都为结束的时候，包的状态更新为结束
        productPackageRepository.updatePkgStatusToEnd(packageId, EPackageStatus.PAYING.getCode(),
                EPackageStatus.END.getCode(), EPayStatus.FINISH.getCode(), operatorId, new Date());

        // 记录日志
        actionHistoryService.save(EntityType.PRODUCTPACKAGE, packageId, ActionType.PKGPREPAY,
                ActionResult.PRODUCT_PACKAGE_PREPAY);

    }
    
    /**
     * 返回比例
     * @return
     */
    private BigDecimal getValidRt(BigDecimal srcRt){
		srcRt = AmtUtils.max(srcRt, BigDecimal.ZERO);
		srcRt = AmtUtils.min(srcRt, BigDecimal.ONE);
		return srcRt;
    }
    
    /**
     * 提前还款，返还一定比例一次性付款所交的融资服务费
     * @param prodId
     * @param pkgId
     * @param lastSeqIds
     */
    private void refundOncePaidFeeAfterPrePay(String prodId, String pkgId, int lastSeqIds, String operatorId){
    	
    	List<ProductFee> prodFeeList = productFeeRepository.findByProductId(prodId);
		
    	Integer totalSeq = paymentScheduleRepository.countByPackageId(pkgId);
    	
    	ProductPackage pkg = productPackageRepository.findOne(pkgId);
    	
        String exchangeUserId = CommonBusinessUtil.getExchangeUserId();
        
		BigDecimal refundRt = CommonBusinessUtil.getPrePayRefundFeeRt();
		BigDecimal refRt = getValidRt(refundRt);
		
		BigDecimal servFeeAmt = BigDecimal.ZERO;
		
		for(ProductFee fee : prodFeeList){
			
			if(fee.getFeeId().compareTo(LOAN_SERV_FEE)==0 
					&& fee.getPayMethod()==EFeePayMethodType.ONCE){
				
		    	Product product = productRepository.findOne(prodId);
		    	
				BigDecimal size = BigDecimal.valueOf(product.getTermLength().longValue());
				
				BigDecimal sfRt = AmtUtils.processNegativeAmt(fee.getFeeRt(), BigDecimal.ZERO);
				
				sfRt = getValidRt(sfRt);
				
				servFeeAmt = sfRt.multiply(pkg.getSupplyAmount()).multiply(size);
		        if (product.getTermType() == ETermType.YEAR) {
		        	servFeeAmt = servFeeAmt.multiply(BigDecimal.valueOf(12L));
		        }
		        if (product.getTermType() == ETermType.DAY) {
		        	servFeeAmt = servFeeAmt.divide(BigDecimal.valueOf(30L), calcuScale, RoundingMode.DOWN);
		        }
		        
		        // 融资服务费扣除后几个月份的总金额 = 总融资服务费 * 后几期/总期数 
		        servFeeAmt = servFeeAmt.multiply(BigDecimal.valueOf(lastSeqIds)).divide(BigDecimal.valueOf(totalSeq.intValue()), calcuScale, RoundingMode.DOWN);
		        
		        // 融资服务费返还金额 = 后几期的总金额 * 返还比例 
		        servFeeAmt = servFeeAmt.multiply(refRt);
		        
		        servFeeAmt = servFeeAmt.setScale(saveScale, RoundingMode.DOWN);
		        
		        break;
			}
		}
        
		if(servFeeAmt.compareTo(BigDecimal.ZERO)>0){
			
    		StringBuffer memo = new StringBuffer("提前还款，");
    		memo.append("返还部分一次性支付的融资服务费，");
    		memo.append("返还比例为");
    		memo.append(refundRt.multiply(BigDecimal.valueOf(100)));
    		memo.append("%，");
			memo.append("返还金额为").append(servFeeAmt).append("元.");
			
			TransferInfo payeeInfo = new TransferInfo();
			payeeInfo.setBizId(pkgId);
			payeeInfo.setCashPool(null);
			payeeInfo.setRelZQ(false);
			payeeInfo.setTrxAmt(servFeeAmt);
			payeeInfo.setTrxMemo(memo.toString());
			payeeInfo.setUserId(pkg.getFinancierId());
			payeeInfo.setUseType(EFundUseType.TRADEEXPENSE);
			
			TransferInfo payerInfo = new TransferInfo();
			payerInfo.setBizId(pkgId);
			payerInfo.setCashPool(null);
			payerInfo.setRelZQ(false);
			payerInfo.setTrxAmt(servFeeAmt);
			payerInfo.setTrxMemo(memo.toString());
			payerInfo.setUserId(exchangeUserId);
			payerInfo.setUseType(EFundUseType.TRADEEXPENSE);
	
			Date workDate = CommonBusinessUtil.getCurrentWorkDate();
			prepaymentService.refundOncePaidFee(IdUtil.produce(), 
					Arrays.asList(payerInfo), Arrays.asList(payeeInfo), 
					false, pkgId, pkgId, null, operatorId, workDate);
		}
    }
    
    private String getBizId(PaymentSchedule schd){
    	return schd.getPackageId()+"_"+schd.getSequenceId();
    }

    /**
     * 获取融资人需要付的总金额
     * 
     * @param schedules
     * @return
     */
    private BigDecimal getFinancerTotalNeedPay(List<PaymentSchedule> schedules, PackageContract contract) {
        BigDecimal financerTotalNeedPay = BigDecimal.ZERO;
        for (int i = 0; i < schedules.size(); i++) {
            PaymentSchedule schedule = schedules.get(i);
            // 本金、罚金计算
            PayScheMsgHolder scheMsgHolder = null;
            if (i == 0) {
                if (StringUtils.equals(contract.getPrePayDeductIntrFlag(), EFlagType.NO.getCode())) {
                    scheMsgHolder = ProdPkgUtil
                            .resolvePaymentSchedule(schedule, true, false, true, false, false, false);
                } else {
                    scheMsgHolder = ProdPkgUtil.resolvePaymentSchedule(schedule, true, true, true, false, true, false);
                }
            } else {
                scheMsgHolder = ProdPkgUtil.resolvePaymentSchedule(schedule, true, false, true, true, false, true);
            }
            if (scheMsgHolder != null) {
            	financerTotalNeedPay = financerTotalNeedPay.add(scheMsgHolder.getFncrPayTotalAmt());
            }
        }
        
        if(financerTotalNeedPay.compareTo(BigDecimal.ZERO)>0){
        	//扣除不需要付的款,除当月的费用以外的剩余几个月的费用免交
        	List<PaymentSchedule> tmpList = new ArrayList<PaymentSchedule>();
        	tmpList.addAll(schedules);
        	if(!tmpList.isEmpty()){
        		// 当月费用不免
	        	tmpList.remove(0);
	        	financerTotalNeedPay = deductNextScheduleFees(tmpList, financerTotalNeedPay);
        	}
        }
        return financerTotalNeedPay;
    }
    
    private BigDecimal deductNextScheduleFees(List<PaymentSchedule> schedules, BigDecimal financerTotalNeedPay){
    	
    	BigDecimal value = BigDecimal.ZERO;
    	BigDecimal addend = BigDecimal.ZERO;
		
		for(int idx = 0; idx < schedules.size(); idx++){
			
			PaymentSchedule sch = schedules.get(idx);
			// 应付费用
	        BigDecimal feeAmt = AmtUtils.processNegativeAmt(sch.getFeeAmt(), BigDecimal.ZERO);
	        // 实付费用
	        BigDecimal payAmt = AmtUtils.processNegativeAmt(sch.getPayAmt(), BigDecimal.ZERO);
	        // 应付费用罚金
	        BigDecimal feeForfeit = AmtUtils.processNegativeAmt(sch.getFeeForfeit(), BigDecimal.ZERO);
	        // 实付费用罚金
	        BigDecimal payFeeForfeit = AmtUtils.processNegativeAmt(sch.getPayFeeForfeit(), BigDecimal.ZERO);
	        
	        BigDecimal payFeeAmt = feeAmt.subtract(payAmt);
	        payFeeAmt = AmtUtils.processNegativeAmt(payFeeAmt, BigDecimal.ZERO);
	        
	        BigDecimal payFeeFineAmt = feeForfeit.subtract(payFeeForfeit);
	        payFeeFineAmt = AmtUtils.processNegativeAmt(payFeeFineAmt, BigDecimal.ZERO);
	        
	        addend = addend.add(payFeeAmt);
	        addend = addend.add(payFeeFineAmt);
		}
		
    	value = financerTotalNeedPay.subtract(addend);
    	value = AmtUtils.processNegativeAmt(value, BigDecimal.ZERO);
    	return value;
    }

    private PrePayPkgMsgHolder getPrePayPkgMsgHolder(PaymentSchedule schedule, ProductPackage pkg, Product product,
            String owner, String operatorId, Date workDate, boolean isFinancier, int squence, PackageContract contract) {

        BigDecimal supplyAmount = AmtUtils.processNegativeAmt(pkg.getSupplyAmount(), BigDecimal.ZERO);

        BigDecimal supplyUnit = supplyAmount.divide(pkg.getUnitAmount());

        String packageId = pkg.getId();

        // 还款利息（包括罚息）平台扣除比例
        BigDecimal deductRate = AmtUtils.processNegativeAmt(CommonBusinessUtil.getPaymentInterestDeductRate(),
                BigDecimal.ZERO);

        // 本金、罚金计算
        PayScheMsgHolder scheMsgHolder = null;
        if (squence == 0) {
            if (StringUtils.equals(contract.getPrePayDeductIntrFlag(), EFlagType.NO.getCode())) {
                scheMsgHolder = ProdPkgUtil.resolvePaymentSchedule(schedule, true, false, true, false, false, false);
            } else {
                scheMsgHolder = ProdPkgUtil.resolvePaymentSchedule(schedule, true, true, true, false, true, false);
            }
        } else {
            scheMsgHolder = ProdPkgUtil.resolvePaymentSchedule(schedule, true, false, true, true, false, true);
        }

        BigDecimal prinPay = scheMsgHolder.getPrinPay();
        BigDecimal prinFinePay = scheMsgHolder.getPrinFinePay();

        BigDecimal intrPay = scheMsgHolder.getIntrPay();
        BigDecimal intrFinePay = scheMsgHolder.getIntrFinePay();

        // 列表中第一期的费用需要交，其余免交
        BigDecimal feePay = squence == 0?scheMsgHolder.getFeePay():BigDecimal.ZERO;
        BigDecimal feeFinePay = squence == 0?scheMsgHolder.getFeeFinePay():BigDecimal.ZERO;

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

        // 融资人需支付的总金额
        BigDecimal fncrPayTotalAmt = intrBuyer.add(intrForfBuyer).add(prinBuyer).add(prinForfBuyer).add(feePlat)
                .add(feeForfPlat);

        // 违约金
        BigDecimal penaltyAmt = BigDecimal.ZERO;
        if (squence == 0) {
            // 提前还款违约金比例
            BigDecimal penaltyRate = BigDecimal.ZERO;
            if (isFinancier) {
                penaltyRate = contract.getFncrPrepayPenaltyRt();
            } else {
                penaltyRate = contract.getPlatformPrepayPenaltyRt();
            }
            // 违约金
            penaltyAmt = pkg.getSupplyAmount().multiply(penaltyRate);
            // 按两位小数截取
            penaltyAmt = penaltyAmt.setScale(saveScale, RoundingMode.DOWN);
        }

        // 融资人总的应付金额 = 本金+利息（当月整月利息）+费用+违约金
        fncrPayTotalAmt = fncrPayTotalAmt.add(penaltyAmt);

        // 累计实际付给投资人的金额
        BigDecimal buyerRecvTotalAc = BigDecimal.ZERO;
        // 累计投资人实际付给平台的金额
        BigDecimal buyer2PlatTotalAc = BigDecimal.ZERO;
        // 累计实际付给平台的金额
        BigDecimal platRecvTotalAc = BigDecimal.ZERO;

        // 获取平台ID
        String exchangeUserId = CommonBusinessUtil.getExchangeUserId();

        List<TransferInfo> payeeList = new ArrayList<TransferInfo>();
        List<TransferInfo> payerList = new ArrayList<TransferInfo>();
        List<TransferInfo> profPayeeList = new ArrayList<TransferInfo>();
        List<TransferInfo> profPayerList = new ArrayList<TransferInfo>();

        List<PositionPo> positions = positionService.getByPkgId(pkg.getId());
        for (PositionPo p : positions) {

            // 已转让，头寸份额为 0
            if (p.getUnit().longValue() <= 0) {
                continue;
            }

            BigDecimal intrBuyerAc = intrBuyer.multiply(BigDecimal.valueOf(p.getUnit().longValue())).divide(supplyUnit,
                    saveScale, RoundingMode.DOWN);

            BigDecimal intrForfBuyerAc = intrForfBuyer.multiply(BigDecimal.valueOf(p.getUnit().longValue())).divide(
                    supplyUnit, saveScale, RoundingMode.DOWN);

            BigDecimal prinBuyerAc = prinBuyer.multiply(BigDecimal.valueOf(p.getUnit().longValue())).divide(supplyUnit,
                    saveScale, RoundingMode.DOWN);

            BigDecimal prinForfBuyerAc = prinForfBuyer.multiply(BigDecimal.valueOf(p.getUnit().longValue())).divide(
                    supplyUnit, saveScale, RoundingMode.DOWN);

            BigDecimal penaltyBuyerAc = penaltyAmt.multiply(BigDecimal.valueOf(p.getUnit().longValue())).divide(
                    supplyUnit, saveScale, RoundingMode.DOWN);

            BigDecimal intrBuyer2PlatAc = intrBuyerAc.add(intrForfBuyerAc).multiply(deductRate)
                    .setScale(saveScale, RoundingMode.DOWN);

            // 实际支付给投资人的钱
            BigDecimal buyerRecvAc = intrBuyerAc.add(intrForfBuyerAc).add(prinBuyerAc).add(prinForfBuyerAc)
                    .add(penaltyBuyerAc);

            InvestorPayeeListBuildReq investorReq = new InvestorPayeeListBuildReq();
            investorReq.setIntrBuyerAc(intrBuyerAc);
            investorReq.setIntrForfBuyerAc(intrForfBuyerAc);
            investorReq.setIntrBuyer2PlatAc(intrBuyer2PlatAc);
            investorReq.setMsg("提前还款");
            investorReq.setPkgId(packageId);
            investorReq.setPrinBuyerAc(prinBuyerAc);
            investorReq.setPrinForfBuyerAc(prinForfBuyerAc);
            investorReq.setPenaltyBuyerAc(penaltyBuyerAc);
            investorReq.setUserId(p.getUserId());
            investorReq.setIntrRate(deductRate);
            investorReq.setSequenceId(String.valueOf(schedule.getSequenceId()));

            ProdPkgUtil.buildPayeeListOfInvestor(investorReq, payeeList, profPayerList);

            buyerRecvTotalAc = buyerRecvTotalAc.add(buyerRecvAc);
            buyer2PlatTotalAc = buyer2PlatTotalAc.add(intrBuyer2PlatAc);

            String positionId = p.getPositionId();

            // 过滤掉已转让的份额
            List<PositionLotPo> lots = positionService.getValidPostionLotsByPostionId(positionId);
            int size = lots.size();
            BigDecimal assignAmt = BigDecimal.ZERO.setScale(calcuScale);
            for (int i = 0; i < size; i++) {
                PositionLotPo lot = lots.get(i);
                BigDecimal accumCrAmt = AmtUtils.processNegativeAmt(lot.getAccumCrAmt(), BigDecimal.ZERO);
                BigDecimal actualAmt = buyerRecvAc.multiply(BigDecimal.valueOf(lot.getUnit().longValue())).divide(
                        BigDecimal.valueOf(p.getUnit()), saveScale, RoundingMode.DOWN);
                if ((size - 1) != i) {
                    lot.setAccumCrAmt(accumCrAmt.add(actualAmt));
                } else {
                    lot.setAccumCrAmt(accumCrAmt.add(buyerRecvAc).subtract(assignAmt));
                }
                if (StringUtils.equals(schedule.getLastFlag(), EFlagType.YES.getCode())) {
                    lot.setUnit(BigDecimal.ZERO.intValue());
                }
                lot.setLastMntOpid(operatorId);
                lot.setLastMntTs(new Date());
                assignAmt = assignAmt.add(actualAmt);
            }
            // 更新头寸份额表
            positionLotService.savePositionLots(lots);

            // 如果是最后一期，则更新头寸份额头寸表
            if (StringUtils.equals(schedule.getLastFlag(), EFlagType.YES.getCode())) {
                p.setUnit(BigDecimal.ZERO.intValue());
                p.setLastMntOpid(operatorId);
                p.setLastMntTs(new Date());
                positionService.savePositions(Arrays.asList(p));
            }
        }

        platRecvTotalAc = buyer2PlatTotalAc.add(feePlat).add(feeForfPlat);
        buyerRecvTotalAc = buyerRecvTotalAc.subtract(buyer2PlatTotalAc);

        // 误差金额 = 应付总额 - 实际付给投资人的还款- 实际付给平台的还款
        BigDecimal deviation = AmtUtils.processNegativeAmt(
                fncrPayTotalAmt.subtract(buyerRecvTotalAc).subtract(platRecvTotalAc), BigDecimal.ZERO);

        platRecvTotalAc = platRecvTotalAc.add(deviation);

        ExchagePayeeListBuildReq exchageReq = new ExchagePayeeListBuildReq();
        exchageReq.setDeviation(deviation);
        exchageReq.setExchangeUserId(exchangeUserId);
        exchageReq.setFeeForfPlat(feeForfPlat);
        exchageReq.setBuyer2PlatTotalAc(buyer2PlatTotalAc);
        exchageReq.setFeePlat(feePlat);
        exchageReq.setMsg("提前还款");
        exchageReq.setPkgId(packageId);
        exchageReq.setIntrRate(deductRate);
        exchageReq.setSequenceId(String.valueOf(schedule.getSequenceId()));

        ProdPkgUtil.buildPayeeListOfExchange(exchageReq, payeeList, profPayeeList);

        TransferInfo dInfo = new TransferInfo();
        dInfo.setUserId(owner);
        dInfo.setRelZQ(true);
        dInfo.setTrxAmt(fncrPayTotalAmt);
        StringBuffer sb = new StringBuffer();
        sb.append(PkgUtils.getPkgIdNameStr(pkg.getId()));
        sb.append("_");
        sb.append(schedule.getSequenceId());
        sb.append("提前还款，划出");
        sb.append(fncrPayTotalAmt);
        sb.append("元");
        if (penaltyAmt.compareTo(BigDecimal.ZERO) > 0) {
            sb.append("，其中" + penaltyAmt + "元为违约金");
        }
        dInfo.setTrxMemo(sb.toString());
        dInfo.setUseType(EFundUseType.FNCR_REPAYMENT);
        payerList.add(dInfo);

        PrePayPkgMsgHolder holder = new PrePayPkgMsgHolder();
        holder.setActualpayBuyerAmt(buyerRecvTotalAc);
        holder.setActualpayPlatAmt(platRecvTotalAc);
        holder.setTotalTrxAmt(fncrPayTotalAmt);
        holder.setPayeeList(payeeList);
        holder.setPayerList(payerList);
        holder.setProfPayeeList(profPayeeList);
        holder.setProfPayerList(profPayerList);

        return holder;
    }

}
