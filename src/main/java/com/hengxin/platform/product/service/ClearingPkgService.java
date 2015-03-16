/*
 * Project Name: kmfex-platform
 * File Name: ClearingPkgService.java
 * Class Name: ClearingPkgService
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
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.hengxin.platform.fund.dto.biz.req.atom.FreezeReq;
import com.hengxin.platform.fund.dto.biz.req.atom.TransferInfo;
import com.hengxin.platform.fund.dto.biz.req.atom.UnFreezeReq;
import com.hengxin.platform.fund.dto.biz.req.atom.UnReserveReq;
import com.hengxin.platform.fund.entity.PositionLotPo;
import com.hengxin.platform.fund.entity.PositionPo;
import com.hengxin.platform.fund.enums.EFundUseType;
import com.hengxin.platform.fund.service.FinancierBreachFundClearingService;
import com.hengxin.platform.fund.service.FreezeReserveDtlService;
import com.hengxin.platform.fund.service.FundAcctBalService;
import com.hengxin.platform.fund.service.FundAcctFreezeMgtService;
import com.hengxin.platform.fund.service.PositionLotService;
import com.hengxin.platform.fund.service.WarrantCmpnsAmtService;
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
import com.hengxin.platform.product.enums.EWarrantyType;
import com.hengxin.platform.product.repository.PaymentScheduleRepository;
import com.hengxin.platform.product.repository.ProductPackageRepository;
import com.hengxin.platform.product.repository.ProductRepository;
import com.hengxin.platform.product.service.MsgHolder.CleaningPkgMsgHolder;
import com.hengxin.platform.product.service.MsgHolder.PayScheMsgHolder;
import com.hengxin.platform.product.service.MsgHolder.PkgUtils;
import com.hengxin.platform.product.service.MsgHolder.req.ExchagePayeeListBuildReq;
import com.hengxin.platform.product.service.MsgHolder.req.InvestorPayeeListBuildReq;
import com.hengxin.platform.product.service.ClearingPkgService;
import com.hengxin.platform.product.service.ProdPkgMsgRemindService;

/**
 * 融资包清分服务 Class Name: ClearingPkgService
 * 
 * @author tingwang
 * 
 */
@Service
public class ClearingPkgService {

    private final static Logger LOG = LoggerFactory.getLogger(ClearingPkgService.class);

    @Autowired
    ProductPackageRepository productPackageRepository;
    @Autowired
    PositionService positionService;
    @Autowired
    PaymentScheduleRepository paymentScheduleRepository;
    @Autowired
    PositionLotService positionLotService;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    FinancierBreachFundClearingService financierBreachFundClearingService;
    @Autowired
    WarrantCmpnsAmtService warrantCmpnsAmtService;
    @Autowired
    FundAcctFreezeMgtService fundAcctFreezeMgtService;
    @Autowired
    ActionHistoryService actionHistoryService;
    @Autowired
    WarrantDepositService warrantDepositService;
    @Autowired
    FreezeReserveDtlService freezeReserveDtlService;
    @Autowired
    JobWorkService jobWorkService;
    @Autowired
    FundAcctBalService fundAcctBalService;
    @Autowired
    ProdPkgMsgRemindService prodPkgMsgRemindService;

    public static final int calcuScale = 20;
    public static final int saveScale = 2;
    
    protected boolean canDoClearing(CleaningPkgMsgHolder msgHolder){
    	boolean bool = msgHolder!=null;
    	bool = bool&&!msgHolder.getPayerList().isEmpty();
    	bool = bool&&!msgHolder.getPayeeList().isEmpty(); 
    	return bool;
    }

    /**
     * 融资包清分 Description: TODO 违约中
     * 
     * @param packageId
     * @param period
     * @param operatorId
     * @param operateDate
     */
    @Transactional
    public void clearingProductPackageInOverdue(String packageId, int period, String operatorId) {
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
        List<EPayStatus> payStatus = Arrays.asList(EPayStatus.OVERDUE);
        if (!payStatus.contains(schedule.getStatus())) {
            throw new BizServiceException(EErrorCode.PROD_PKG_STATUS_IS_ERROR);
        }
        // 获取产品信息
        Product product = productRepository.findByProductId(productPackage.getProductId());

        // 融资人
        String owner = product.getApplUserId();
        Date workDate = CommonBusinessUtil.getCurrentWorkDate();

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

        // 获取活期账户可用余额+小微宝
        BigDecimal canPayableAmt = AmtUtils.processNegativeAmt(
                financierBreachFundClearingService.getUserCurrentAvlAmtIgnoreFrzAmt(owner, true), BigDecimal.ZERO);

        CleaningPkgMsgHolder msgHolder = null;
        if(canPayableAmt.compareTo(BigDecimal.ZERO)>0){
	        msgHolder = this.getCleaningPkgMsgHolder(canPayableAmt, schedule, productPackage, product,
	                owner, operatorId, workDate);
        }

        boolean doClearing = canDoClearing(msgHolder);
        
        if (doClearing) {
        	
        	String eventId = IdUtil.produce();
        	String bizId = PkgUtils.formatBizId(packageId, period);
        	
        	// 违约还款，资金清分
            financierBreachFundClearingService.breachRepaymentFundClearing(eventId, msgHolder.getPayerList(),
                    msgHolder.getPayeeList(), true, bizId, packageId, String.valueOf(period), operatorId, workDate);
            
            if (!msgHolder.getProfPayerList().isEmpty() && !msgHolder.getProfPayeeList().isEmpty()) {
                financierBreachFundClearingService.payInvsIntrProfitToExchange(eventId, msgHolder.getProfPayerList(),
                        msgHolder.getProfPayeeList(), bizId, packageId, String.valueOf(period), operatorId, workDate);
            }
        	
            // 处理宽限期问题
            Integer breachGraceDays = ProdPkgUtil.getBreachGraceDays(product);
            Date paymentDate = schedule.getPaymentDate();
            boolean needPayFine = ProdPkgUtil.needPayFineAmt(paymentDate, workDate,
                    Long.valueOf(breachGraceDays.intValue()));
            boolean needPayOverdueCmpnsFineAmt = ProdPkgUtil.needPayOverdueCmpnsFineAmt(schedule.getPaymentDate(),
                    workDate, Arrays.asList(product.getCmpnsGracePeriod(), product.getOverdue2CmpnsGracePeriod()));
            if (msgHolder.isCanFinish()) {
                // 违约宽限天数(宽限期以内，不收违约金)
                if (needPayFine) {
                    schedule.setPayPrincipalForfeit(schedule.getPrincipalForfeit());
                    schedule.setPayInterestForfeit(schedule.getInterestForfeit());
                    schedule.setPayFeeForfeit(schedule.getFeeForfeit());
                } else {
                    schedule.setPrincipalForfeit(schedule.getPayPrincipalForfeit());
                    schedule.setInterestForfeit(schedule.getPayInterestForfeit());
                    schedule.setFeeForfeit(schedule.getPayFeeForfeit());
                }
                schedule.setStatus(EPayStatus.FINISH);
                schedule.setPayPrincipalAmt(schedule.getPrincipalAmt());
                schedule.setPayInterestAmt(schedule.getInterestAmt());
                schedule.setPayAmt(schedule.getFeeAmt());

                if (needPayOverdueCmpnsFineAmt) {
                    schedule.setPayWrtrInterestForfeit(schedule.getWrtrInterestForfeit());
                    schedule.setPayWrtrPrinForfeit(schedule.getWrtrPrinForfeit());
                } else {
                    schedule.setWrtrInterestForfeit(schedule.getPayWrtrInterestForfeit());
                    schedule.setWrtrPrinForfeit(schedule.getPayWrtrPrinForfeit());
                }
                schedule.setLastMntOpid(operatorId);
                schedule.setLastMntTs(new Date());
                schedule.setLastPayTs(DateUtils.formatYYYYMMDDHHmmss(workDate));
                
                if(PkgUtils.isLastSchedule(schedule)){
                    boolean fnrActive = freezeReserveDtlService.isfnrDtlActive(productPackage.getWrtrFnrJnlNo());
                    if (fnrActive) {
                        // 退担保保证金
                        UnReserveReq uReq = new UnReserveReq();
                        uReq.setCurrOpId(operatorId);
                        uReq.setReserveJnlNo(productPackage.getWrtrFnrJnlNo());
                        uReq.setTrxMemo(PkgUtils.getPkgIdNameStr(productPackage.getId()) + "_" + period + "期完全清分，担保保证金退回");
                        uReq.setUserId(product.getWarrantId());
                        uReq.setWorkDate(workDate);
                        warrantDepositService.refundWarrantDeposit(uReq);
                    }
                }               
            } else {
                // 实付本金
                BigDecimal payPrincipalAmt = AmtUtils
                        .processNegativeAmt(schedule.getPayPrincipalAmt(), BigDecimal.ZERO);
                // 实付利息
                BigDecimal payInterestAmt = AmtUtils.processNegativeAmt(schedule.getPayInterestAmt(), BigDecimal.ZERO);
                // 实付费用
                BigDecimal payAmt = AmtUtils.processNegativeAmt(schedule.getPayAmt(), BigDecimal.ZERO);
                // 实付本金罚金
                BigDecimal payPrincipalForfeit = AmtUtils.processNegativeAmt(schedule.getPayPrincipalForfeit(),
                        BigDecimal.ZERO);
                // 实付利息罚金
                BigDecimal payInterestForfeit = AmtUtils.processNegativeAmt(schedule.getPayInterestForfeit(),
                        BigDecimal.ZERO);
                // 实付费用罚金
                BigDecimal payFeeForfeit = AmtUtils.processNegativeAmt(schedule.getPayFeeForfeit(), BigDecimal.ZERO);

                BigDecimal payWrtrInterestForfeit = AmtUtils.processNegativeAmt(schedule.getPayWrtrInterestForfeit(),
                        BigDecimal.ZERO);

                BigDecimal payWrtrPrinForfeit = AmtUtils.processNegativeAmt(schedule.getPayWrtrPrinForfeit(),
                        BigDecimal.ZERO);

                payPrincipalAmt = payPrincipalAmt.add(msgHolder.getPrinPay());
                payInterestAmt = payInterestAmt.add(msgHolder.getIntrPay());
                payAmt = payAmt.add(msgHolder.getFeePay());
                payPrincipalForfeit = payPrincipalForfeit.add(msgHolder.getPrinFinePay());
                payInterestForfeit = payInterestForfeit.add(msgHolder.getIntrFinePay());
                payFeeForfeit = payFeeForfeit.add(msgHolder.getFeeFinePay());
                payWrtrInterestForfeit = payWrtrInterestForfeit.add(msgHolder.getWrtrIntrForfPay());
                payWrtrPrinForfeit = payWrtrPrinForfeit.add(msgHolder.getWrtrPrinForfPay());

                schedule.setPayPrincipalAmt(payPrincipalAmt);
                schedule.setPayInterestAmt(payInterestAmt);
                schedule.setPayAmt(payAmt);
                schedule.setPayPrincipalForfeit(payPrincipalForfeit);
                schedule.setPayInterestForfeit(payInterestForfeit);
                schedule.setPayFeeForfeit(payFeeForfeit);
                schedule.setPayWrtrInterestForfeit(payWrtrInterestForfeit);
                schedule.setPayWrtrPrinForfeit(payWrtrPrinForfeit);
                schedule.setLastMntOpid(operatorId);
                schedule.setLastMntTs(new Date());
                schedule.setLastPayTs(DateUtils.formatYYYYMMDDHHmmss(workDate));

                // 计算剩余欠款金额
                BigDecimal newPyAmt = schedule.getPrincipalAmt().add(schedule.getPrincipalForfeit());
                newPyAmt = newPyAmt.add(schedule.getInterestAmt()).add(schedule.getInterestForfeit());
                newPyAmt = newPyAmt.add(schedule.getFeeAmt()).add(schedule.getFeeForfeit());
                newPyAmt = newPyAmt.add(schedule.getWrtrPrinForfeit()).add(schedule.getWrtrInterestForfeit());

                BigDecimal newPdAmt = schedule.getPayPrincipalAmt().add(schedule.getPayPrincipalForfeit());
                newPdAmt = newPdAmt.add(schedule.getPayInterestAmt()).add(schedule.getPayInterestForfeit());
                newPdAmt = newPdAmt.add(schedule.getPayAmt()).add(schedule.getPayFeeForfeit());
                newPdAmt = newPdAmt.add(schedule.getPayWrtrPrinForfeit()).add(schedule.getPayWrtrInterestForfeit());

                BigDecimal diffAmt = AmtUtils.processNegativeAmt(newPyAmt.subtract(newPdAmt), BigDecimal.ZERO);

                if (diffAmt.compareTo(BigDecimal.ZERO) > 0) {
                    FreezeReq fReq = new FreezeReq();
                    fReq.setBizId(bizId);
                    fReq.setCurrOpId(operatorId);
                    fReq.setTrxAmt(diffAmt);
                    fReq.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "_" + period + "清分部分还款，冻结新的欠款金额" + diffAmt);
                    fReq.setUserId(owner);
                    fReq.setUseType(EFundUseType.FNCR_REPAYMENT_PENALTY);
                    fReq.setWorkDate(workDate);
                    // 冻结欠款金额
                    String prinFrzId = financierBreachFundClearingService.freezeArrearageAmt(fReq);
                    schedule.setPrinFrzId(prinFrzId);
                }
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

    /**
     * 
     * Description: TODO 已代偿
     * 
     * @param packageId
     * @param period
     * @param operatorId
     * @param operateDate
     */
    @Transactional
    public void clearingProductPackageInCompensatory(String packageId, int period, String operatorId) {
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
        List<EPayStatus> payStatus = Arrays.asList(EPayStatus.COMPENSATORY);
        if (!payStatus.contains(schedule.getStatus())) {
            throw new BizServiceException(EErrorCode.PROD_PKG_STATUS_IS_ERROR);
        }

        Product product = productRepository.findByProductId(productPackage.getProductId());

        // 融资人
        String owner = product.getApplUserId();
        Date workDate = CommonBusinessUtil.getCurrentWorkDate();

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

        // 获取活期账户可用余额+小微宝
        BigDecimal canPayableAmt = AmtUtils.processNegativeAmt(
                financierBreachFundClearingService.getUserCurrentAvlAmtIgnoreFrzAmt(owner, true), BigDecimal.ZERO);

        CleaningPkgMsgHolder msgHolder = null;
        if(canPayableAmt.compareTo(BigDecimal.ZERO)>0){
	        msgHolder = this.getCleaningPkgMsgHolder(canPayableAmt, schedule, productPackage, product,
	                owner, operatorId, workDate);
        }

        boolean doClearing = canDoClearing(msgHolder);
        if (doClearing) {
	        String eventId = IdUtil.produce();
	        String bizId = PkgUtils.formatBizId(packageId, period);
            // 违约还款，资金清分
            financierBreachFundClearingService.breachRepaymentFundClearing(eventId, msgHolder.getPayerList(),
                    msgHolder.getPayeeList(), true, bizId, packageId, String.valueOf(period), operatorId, workDate);

            if (!msgHolder.getProfPayerList().isEmpty() && !msgHolder.getProfPayeeList().isEmpty()) {
                financierBreachFundClearingService.payInvsIntrProfitToExchange(eventId, msgHolder.getProfPayerList(),
                        msgHolder.getProfPayeeList(), bizId, packageId, String.valueOf(period), operatorId, workDate);
            }
            
            // 处理宽限期问题
            Integer breachGraceDays = ProdPkgUtil.getBreachGraceDays(product);
            Date paymentDate = schedule.getPaymentDate();
            boolean needPayFine = ProdPkgUtil.needPayFineAmt(paymentDate, workDate,
                    Long.valueOf(breachGraceDays.intValue()));
            boolean needPayOverdueCmpnsFineAmt = ProdPkgUtil.needPayOverdueCmpnsFineAmt(schedule.getPaymentDate(),
                    workDate, Arrays.asList(product.getCmpnsGracePeriod(), product.getOverdue2CmpnsGracePeriod()));
            if (msgHolder.isCanFinish()) {
                // 违约宽限天数(宽限期以内，不收违约金)
                if (needPayFine) {
                    schedule.setPayPrincipalForfeit(schedule.getPrincipalForfeit());
                    schedule.setPayInterestForfeit(schedule.getInterestForfeit());
                    schedule.setPayFeeForfeit(schedule.getFeeForfeit());
                } else {
                    schedule.setPrincipalForfeit(schedule.getPayPrincipalForfeit());
                    schedule.setInterestForfeit(schedule.getPayInterestForfeit());
                    schedule.setFeeForfeit(schedule.getPayFeeForfeit());
                }

                schedule.setStatus(EPayStatus.CLEARED);
                schedule.setPayPrincipalAmt(schedule.getPrincipalAmt());
                schedule.setPayInterestAmt(schedule.getInterestAmt());
                schedule.setPayAmt(schedule.getFeeAmt());

                if (needPayOverdueCmpnsFineAmt) {
                    schedule.setPayWrtrInterestForfeit(schedule.getWrtrInterestForfeit());
                    schedule.setPayWrtrPrinForfeit(schedule.getWrtrPrinForfeit());
                } else {
                    schedule.setWrtrInterestForfeit(schedule.getPayWrtrInterestForfeit());
                    schedule.setWrtrPrinForfeit(schedule.getPayWrtrPrinForfeit());
                }
                schedule.setLastMntOpid(operatorId);
                schedule.setLastMntTs(new Date());
                schedule.setLastPayTs(DateUtils.formatYYYYMMDDHHmmss(workDate));
            } else {
                // 实付本金
                BigDecimal payPrincipalAmt = AmtUtils
                        .processNegativeAmt(schedule.getPayPrincipalAmt(), BigDecimal.ZERO);
                // 实付利息
                BigDecimal payInterestAmt = AmtUtils.processNegativeAmt(schedule.getPayInterestAmt(), BigDecimal.ZERO);
                // 实付费用
                BigDecimal payAmt = AmtUtils.processNegativeAmt(schedule.getPayAmt(), BigDecimal.ZERO);
                // 实付本金罚金
                BigDecimal payPrincipalForfeit = AmtUtils.processNegativeAmt(schedule.getPayPrincipalForfeit(),
                        BigDecimal.ZERO);
                // 实付利息罚金
                BigDecimal payInterestForfeit = AmtUtils.processNegativeAmt(schedule.getPayInterestForfeit(),
                        BigDecimal.ZERO);
                // 实付费用罚金
                BigDecimal payFeeForfeit = AmtUtils.processNegativeAmt(schedule.getPayFeeForfeit(), BigDecimal.ZERO);

                BigDecimal payWrtrInterestForfeit = AmtUtils.processNegativeAmt(schedule.getPayWrtrInterestForfeit(),
                        BigDecimal.ZERO);

                BigDecimal payWrtrPrinForfeit = AmtUtils.processNegativeAmt(schedule.getPayWrtrPrinForfeit(),
                        BigDecimal.ZERO);

                payPrincipalAmt = payPrincipalAmt.add(msgHolder.getPrinPay());
                payInterestAmt = payInterestAmt.add(msgHolder.getIntrPay());
                payAmt = payAmt.add(msgHolder.getFeePay());
                payPrincipalForfeit = payPrincipalForfeit.add(msgHolder.getPrinFinePay());
                payInterestForfeit = payInterestForfeit.add(msgHolder.getIntrFinePay());
                payFeeForfeit = payFeeForfeit.add(msgHolder.getFeeFinePay());
                payWrtrInterestForfeit = payWrtrInterestForfeit.add(msgHolder.getWrtrIntrForfPay());
                payWrtrPrinForfeit = payWrtrPrinForfeit.add(msgHolder.getWrtrPrinForfPay());

                schedule.setPayPrincipalAmt(payPrincipalAmt);
                schedule.setPayInterestAmt(payInterestAmt);
                schedule.setPayAmt(payAmt);
                schedule.setPayPrincipalForfeit(payPrincipalForfeit);
                schedule.setPayInterestForfeit(payInterestForfeit);
                schedule.setPayFeeForfeit(payFeeForfeit);
                schedule.setPayWrtrInterestForfeit(payWrtrInterestForfeit);
                schedule.setPayWrtrPrinForfeit(payWrtrPrinForfeit);
                schedule.setLastMntOpid(operatorId);
                schedule.setLastMntTs(new Date());
                schedule.setLastPayTs(DateUtils.formatYYYYMMDDHHmmss(workDate));

                // 计算剩余欠款金额
                BigDecimal newPyAmt = schedule.getPrincipalAmt().add(schedule.getPrincipalForfeit());
                newPyAmt = newPyAmt.add(schedule.getInterestAmt()).add(schedule.getInterestForfeit());
                newPyAmt = newPyAmt.add(schedule.getFeeAmt()).add(schedule.getFeeForfeit());
                newPyAmt = newPyAmt.add(schedule.getWrtrPrinForfeit()).add(schedule.getWrtrInterestForfeit());

                BigDecimal newPdAmt = schedule.getPayPrincipalAmt().add(schedule.getPayPrincipalForfeit());
                newPdAmt = newPdAmt.add(schedule.getPayInterestAmt()).add(schedule.getPayInterestForfeit());
                newPdAmt = newPdAmt.add(schedule.getPayAmt()).add(schedule.getPayFeeForfeit());
                newPdAmt = newPdAmt.add(schedule.getPayWrtrPrinForfeit()).add(schedule.getPayWrtrInterestForfeit());

                BigDecimal diffAmt = AmtUtils.processNegativeAmt(newPyAmt.subtract(newPdAmt), BigDecimal.ZERO);

                if (diffAmt.compareTo(BigDecimal.ZERO) > 0) {
                    FreezeReq fReq = new FreezeReq();
                    fReq.setBizId(bizId);
                    fReq.setCurrOpId(operatorId);
                    fReq.setTrxAmt(diffAmt);
                    fReq.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "_" + period + "清分部分还款，冻结新的欠款金额" + diffAmt);
                    fReq.setUserId(owner);
                    fReq.setUseType(EFundUseType.FNCR_REPAYMENT_PENALTY);
                    fReq.setWorkDate(workDate);
                    // 冻结欠款金额
                    String prinFrzId = financierBreachFundClearingService.freezeArrearageAmt(fReq);
                    schedule.setPrinFrzId(prinFrzId);
                }
            }
            // 更新还款计划表
            paymentScheduleRepository.save(schedule);
            paymentScheduleRepository.flush();

            /*
             * // 清分结束，如果还款计划都为结束的时候，包的状态更新为结束 productPackageRepository.updatePkgStatusToEnd(packageId,
             * EPackageStatus.PAYING.getCode(), EPackageStatus.END.getCode(), EPayStatus.FINISH.getCode(), operatorId,
             * new Date());
             */

            // 记录日志
            actionHistoryService.save(EntityType.PRODUCTPACKAGE, packageId, ActionType.PKGCLEANING,
                    ActionResult.PRODUCT_PACKAGE_CLEANING);

            if (msgHolder.isCanFinish()) {
                prodPkgMsgRemindService.pkgScheduleClearToCleared(schedule, owner, product.getWarrantId());
            }
        }

    }

    public static void main(String[] args) {
        Date payDate = DateUtils.getDate("2014-07-05", "yyyy-MM-dd");
        payDate = DateUtils.add(payDate, Calendar.DATE, 1);
        System.out.println(DateUtils.formatDate(payDate, "yyyy-MM-dd"));
    }

    /**
     * 融资包清分-代偿中 Description:
     * 
     * @param packageId
     * @param period
     * @param operatorId
     */
    @Transactional
    public void clearingProductPackageInCompensating(String packageId, int period, String operatorId) {
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
        List<EPayStatus> payStatus = Arrays.asList(EPayStatus.COMPENSATING);
        if (!payStatus.contains(schedule.getStatus())) {
            throw new BizServiceException(EErrorCode.PROD_PKG_STATUS_IS_ERROR);
        }

        // 产品
        Product product = productRepository.findByProductId(productPackage.getProductId());
        
        Date workDate = CommonBusinessUtil.getCurrentWorkDate();

        // 免交逾期代偿罚金宽限期
        boolean wrtrNeedPayFineAmt = ProdPkgUtil.needPayOverdueCmpnsFineAmt(schedule.getPaymentDate(), workDate,
                Arrays.asList(product.getOverdue2CmpnsGracePeriod(), product.getCmpnsGracePeriod()));
        // 免交逾期违约罚金宽限期
        Integer brkGraceDays = ProdPkgUtil.getBreachGraceDays(product);
        boolean fncrNeedPayFineAmt = ProdPkgUtil.needPayFineAmt(schedule.getPaymentDate(), workDate,
                Long.valueOf(brkGraceDays.intValue()));

        // 清分时平台费用扣除比例
        BigDecimal deductRate = AmtUtils.processNegativeAmt(CommonBusinessUtil.getClearingInterestDeductRate(),
                BigDecimal.ZERO);

        // 实际申购份数
        BigDecimal supplyUnit = productPackage.getSupplyAmount().divide(productPackage.getUnitAmount(), calcuScale,
                RoundingMode.DOWN);

        // 融资人
        String owner = product.getApplUserId();

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

        // 应付本金
        BigDecimal principalAmt = AmtUtils.processNegativeAmt(schedule.getPrincipalAmt(), BigDecimal.ZERO);
        // 应付利息
        BigDecimal interestAmt = AmtUtils.processNegativeAmt(schedule.getInterestAmt(), BigDecimal.ZERO);
        // 应付费用
        BigDecimal feeAmt = AmtUtils.processNegativeAmt(schedule.getFeeAmt(), BigDecimal.ZERO);

        // 应付本金罚金
        BigDecimal principalForfeit = AmtUtils.processNegativeAmt(schedule.getPrincipalForfeit(), BigDecimal.ZERO);
        // 应付利息罚金
        BigDecimal interestForfeit = AmtUtils.processNegativeAmt(schedule.getInterestForfeit(), BigDecimal.ZERO);
        // 应付费用罚金
        BigDecimal feeForfeit = AmtUtils.processNegativeAmt(schedule.getFeeForfeit(), BigDecimal.ZERO);

        // 担保方应付本金代偿逾期罚金
        BigDecimal wrtrPrinForfeit = AmtUtils.processNegativeAmt(schedule.getWrtrPrinForfeit(), BigDecimal.ZERO);
        // 担保方应付利息代偿逾期罚金
        BigDecimal wrtrIntrForfeit = AmtUtils.processNegativeAmt(schedule.getWrtrInterestForfeit(), BigDecimal.ZERO);

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        // 实付本金
        BigDecimal payPrincipalAmt = AmtUtils.processNegativeAmt(schedule.getPayPrincipalAmt(), BigDecimal.ZERO);
        // 实付利息
        BigDecimal payInterestAmt = AmtUtils.processNegativeAmt(schedule.getPayInterestAmt(), BigDecimal.ZERO);
        // 实付费用
        BigDecimal payFeeAmt = AmtUtils.processNegativeAmt(schedule.getPayAmt(), BigDecimal.ZERO);
        // 实付本金罚金
        BigDecimal payPrincipalForfeit = AmtUtils
                .processNegativeAmt(schedule.getPayPrincipalForfeit(), BigDecimal.ZERO);
        // 实付利息罚金
        BigDecimal payInterestForfeit = AmtUtils.processNegativeAmt(schedule.getPayInterestForfeit(), BigDecimal.ZERO);
        // 实付费用罚金
        BigDecimal payFeeForfeit = AmtUtils.processNegativeAmt(schedule.getPayFeeForfeit(), BigDecimal.ZERO);

        // 担保方实付本金代偿逾期罚金
        BigDecimal payWrtrPrinForfeit = AmtUtils.processNegativeAmt(schedule.getPayWrtrPrinForfeit(), BigDecimal.ZERO);
        // 担保方实付利息代偿逾期罚金
        BigDecimal payWrtrIntrForfeit = AmtUtils.processNegativeAmt(schedule.getPayWrtrInterestForfeit(),
                BigDecimal.ZERO);

        // 本金需付
        BigDecimal prinPay = AmtUtils.processNegativeAmt(principalAmt.subtract(payPrincipalAmt), BigDecimal.ZERO);
        // 本金罚金需付
        BigDecimal prinFinePay = AmtUtils.processNegativeAmt(principalForfeit.subtract(payPrincipalForfeit),
                BigDecimal.ZERO);
        // 利息需付
        BigDecimal intrPay = AmtUtils.processNegativeAmt(interestAmt.subtract(payInterestAmt), BigDecimal.ZERO);
        // 利息罚金需付
        BigDecimal intrFinePay = AmtUtils.processNegativeAmt(interestForfeit.subtract(payInterestForfeit),
                BigDecimal.ZERO);
        // 费用需付
        BigDecimal feePay = AmtUtils.processNegativeAmt(feeAmt.subtract(payFeeAmt), BigDecimal.ZERO);
        // 费用罚金需付
        BigDecimal feeFinePay = AmtUtils.processNegativeAmt(feeForfeit.subtract(payFeeForfeit), BigDecimal.ZERO);
        // 本金代偿逾期罚金需付
        BigDecimal wrtrPrinFinePay = AmtUtils.processNegativeAmt(wrtrPrinForfeit.subtract(payWrtrPrinForfeit),
                BigDecimal.ZERO);
        // 本金代偿逾期罚金需付
        BigDecimal wrtrIntrFinePay = AmtUtils.processNegativeAmt(wrtrIntrForfeit.subtract(payWrtrIntrForfeit),
                BigDecimal.ZERO);

        // 给投资人的应付本金
        BigDecimal prinBuyer = prinPay;
        // 给投资人的应付利息
        BigDecimal intrBuyer = intrPay;
        // 给平台的应付费用
        BigDecimal feePlat = feePay;

        // 给投资人的应付利息罚金
        BigDecimal intrForfBuyer = BigDecimal.ZERO;
        // 给投资人的应付本金罚金
        BigDecimal prinForfBuyer = BigDecimal.ZERO;
        // 给平台的应付费用罚金
        BigDecimal feeForfPlat = BigDecimal.ZERO;

        if (fncrNeedPayFineAmt) {
            intrForfBuyer = intrFinePay;
            prinForfBuyer = prinFinePay;
            feeForfPlat = feeFinePay;
        }

        BigDecimal wrtrPrinForfBuyer = BigDecimal.ZERO;
        BigDecimal wrtrIntrForfBuyer = BigDecimal.ZERO;
        if (wrtrNeedPayFineAmt) {
            wrtrPrinForfBuyer = wrtrPrinFinePay;
            wrtrIntrForfBuyer = wrtrIntrFinePay;
        }

        // 投资人应收总额
        BigDecimal buyerRecvTotal = intrBuyer.add(intrForfBuyer).add(prinBuyer).add(prinForfBuyer);

        // 平台应收总额
        BigDecimal platRecvTotal = feePlat.add(feeForfPlat);

        // 担保方应付代偿逾期罚金
        BigDecimal wrtrFineAmt = wrtrPrinForfBuyer.add(wrtrIntrForfBuyer);

        // 总共应还总额
        BigDecimal totalNeedPayAmt = buyerRecvTotal.add(platRecvTotal);
        // 担保方逾期代偿违约金 暂由担保方支付 TODO
        totalNeedPayAmt = totalNeedPayAmt.add(wrtrFineAmt);

        // 融资人是否可自己完成还款
        boolean fncrCanFinish = false;
        // 融资人和担保方是否可一同完成还款
        boolean togetherCanFinish = false;
        // 是否可代偿
        boolean canCmpns = false;

        // 融资人钱不够，担保公司介入
        BigDecimal warrantyAmt = BigDecimal.ZERO;

        // 获取活期账户可用余额+小微宝
        BigDecimal oldAvalAmtUser = financierBreachFundClearingService.getUserCurrentAvlAmtIgnoreFrzAmt(owner, true);

        boolean needToUpdatePositionToZero = this.needToUpdatePositionToZero(schedule);

        if (oldAvalAmtUser.compareTo(totalNeedPayAmt) < 0) {

            EWarrantyType warrantyType = product.getWarrantyType();
            BigDecimal wrtrTempCmpnsAmt = BigDecimal.ZERO;
            if (warrantyType == EWarrantyType.PRINCIPAL) {// 本金担保
                wrtrTempCmpnsAmt = prinPay;
            } else if (warrantyType == EWarrantyType.PRINCIPALINTEREST) {// 本息担保
                wrtrTempCmpnsAmt = prinPay.add(intrPay);
            } else {
                throw new BizServiceException(EErrorCode.PROD_WARRANTYTYPE_ERROR, "风险保障类型错误，不是本金担保或本息担保");
            }

            // 除担保金额之外的，融资人应付金额
            BigDecimal tempAmt = AmtUtils.processNegativeAmt(totalNeedPayAmt.subtract(wrtrTempCmpnsAmt),
                    BigDecimal.ZERO);

            // 融资人还完除担保金额之外的剩余金额
            BigDecimal netAmt = oldAvalAmtUser.subtract(tempAmt);

            int cmp = netAmt.compareTo(BigDecimal.ZERO);

            if (cmp == 0) {
                // 融资人余额只够付除担保以外的应付款，担保方需全部支付担保的应付款项
                warrantyAmt = wrtrTempCmpnsAmt;
                // 融资人和担保方共同可完成还款
                togetherCanFinish = true;
                fncrCanFinish = false;
            } else if (cmp > 0) {
                // 融资人还完非担保金额有剩余金额，则 担保方可少付部分担保费
                if (wrtrTempCmpnsAmt.compareTo(netAmt) >= 0) {
                    warrantyAmt = wrtrTempCmpnsAmt.subtract(netAmt);
                    // 融资人和担保方共同可完成还款
                    togetherCanFinish = true;
                    fncrCanFinish = false;
                } else {
                    // 融资人够还所有钱
                    warrantyAmt = BigDecimal.ZERO;
                    togetherCanFinish = false;
                    fncrCanFinish = true;
                }
            } else {
                togetherCanFinish = false;
                fncrCanFinish = false;
                warrantyAmt = wrtrTempCmpnsAmt;
            }

            // 获取担保机构可用余额
            BigDecimal avalAmtWarrant = fundAcctBalService.getUserCurrentAcctAvlBal(product.getWarrantId());
            BigDecimal wrtrAmt = BigDecimal.ZERO;
            if (PkgUtils.isLastSchedule(schedule)) {
                if (StringUtils.isNotBlank(productPackage.getWrtrFnrJnlNo())) {
                    wrtrAmt = freezeReserveDtlService.getUnFnrAbleAmt(productPackage.getWrtrFnrJnlNo());
                }
            }

            // 担保方可担保的金额为 可用余额+担保保证金
            avalAmtWarrant = avalAmtWarrant.add(wrtrAmt);

            // 担保公司代偿金额足，则进行代偿
            if (avalAmtWarrant.compareTo(warrantyAmt) >= 0) {
                canCmpns = true;
            } else {
                togetherCanFinish = false;
            }

        } else {

            // 获取担保机构可用余额
            BigDecimal avalAmtWarrant = fundAcctBalService.getUserCurrentAcctAvlBal(product.getWarrantId());
            BigDecimal wrtrAmt = BigDecimal.ZERO;
            if (PkgUtils.isLastSchedule(schedule)) {
                if (StringUtils.isNotBlank(productPackage.getWrtrFnrJnlNo())) {
                    wrtrAmt = freezeReserveDtlService.getUnFnrAbleAmt(productPackage.getWrtrFnrJnlNo());
                }
            }

            // 担保方可担保的金额为 可用余额+担保保证金
            avalAmtWarrant = avalAmtWarrant.add(wrtrAmt);

            fncrCanFinish = true;
        }

        String eventId = IdUtil.produce();
        String bizId = PkgUtils.formatBizId(packageId, period);

        // 融资人不可资金全额完成还款
        if (!fncrCanFinish) {
            // 可代偿
            if (canCmpns) {

                if (PkgUtils.isLastSchedule(schedule)) {

                    if (StringUtils.isNotBlank(productPackage.getWrtrFnrJnlNo())) {
                        if (warrantyAmt.compareTo(BigDecimal.ZERO) > 0) {
                            // 担保方付代偿金额给融资人
                            DedicatedTransferInfo warrantPayer = new DedicatedTransferInfo();
                            warrantPayer.setFnrJnlNo(productPackage.getWrtrFnrJnlNo());
                            warrantPayer.setRelZQ(false);
                            warrantPayer.setTrxAmt(warrantyAmt);
                            warrantPayer.setTrxMemo(PkgUtils.getPkgIdNameStr(productPackage.getId()) + "_" + period + "清分,担保机构支付代偿金额"
                                    + warrantyAmt + "元");
                            warrantPayer.setUserId(product.getWarrantId());
                            warrantPayer.setUseType(EFundUseType.CMPNSAMT_PAY);

                            // 融资人收担保方支付的代偿金额
                            TransferInfo fncrPayee = new TransferInfo();
                            fncrPayee.setRelZQ(false);
                            fncrPayee.setTrxAmt(warrantyAmt);
                            fncrPayee.setTrxMemo(PkgUtils.getPkgIdNameStr(productPackage.getId()) + "_" + period + "清分,担保机构代偿"
                                    + warrantyAmt + "元");
                            fncrPayee.setUserId(owner);
                            fncrPayee.setUseType(EFundUseType.CMPNSAMT_PAY);

                            // 担保方代偿，支付代偿款
                            warrantCmpnsAmtService.payCmpnsAmtOnFinalPayment(eventId, warrantPayer, fncrPayee, bizId, 
                            		packageId, String.valueOf(period), operatorId, workDate);
                        } else {
                            boolean fnrActive = freezeReserveDtlService
                                    .isfnrDtlActive(productPackage.getWrtrFnrJnlNo());
                            if (fnrActive) {
                                // 退担保保证金
                                UnReserveReq uReq = new UnReserveReq();
                                uReq.setCurrOpId(operatorId);
                                uReq.setReserveJnlNo(productPackage.getWrtrFnrJnlNo());
                                uReq.setTrxMemo(PkgUtils.getPkgIdNameStr(productPackage.getId()) + "_" + period + "期清分，需代偿金额为"
                                        + warrantyAmt + "元，担保保证金退回");
                                uReq.setUserId(product.getWarrantId());
                                uReq.setWorkDate(workDate);
                                warrantDepositService.refundWarrantDeposit(uReq);
                            }
                        }

                    } else {

                        if (warrantyAmt.compareTo(BigDecimal.ZERO) > 0) {
                            // 担保方付代偿金额给融资人
                            TransferInfo warrantPayer = new TransferInfo();
                            warrantPayer.setRelZQ(false);
                            warrantPayer.setTrxAmt(warrantyAmt);
                            warrantPayer.setTrxMemo(PkgUtils.getPkgIdNameStr(productPackage.getId()) + "_" + period + "清分,担保机构支付代偿金额"
                                    + warrantyAmt + "元");
                            warrantPayer.setUserId(product.getWarrantId());
                            warrantPayer.setUseType(EFundUseType.CMPNSAMT_PAY);

                            // 融资人收担保方支付的代偿金额
                            TransferInfo fncrPayee = new TransferInfo();
                            fncrPayee.setRelZQ(false);
                            fncrPayee.setTrxAmt(warrantyAmt);
                            fncrPayee.setTrxMemo(PkgUtils.getPkgIdNameStr(productPackage.getId()) + "_" + period + "清分,担保机构代偿"
                                    + warrantyAmt + "元");
                            fncrPayee.setUserId(owner);
                            fncrPayee.setUseType(EFundUseType.CMPNSAMT_PAY);

                            // 担保方代偿，支付代偿款
                            warrantCmpnsAmtService.payCmpnsAmtOnPayment(eventId, warrantPayer, fncrPayee, bizId, 
                            		packageId, String.valueOf(period), operatorId, workDate);
                        }
                    }
                } else {

                    if (warrantyAmt.compareTo(BigDecimal.ZERO) > 0) {
                        // 担保方付代偿金额给融资人
                        TransferInfo warrantPayer = new TransferInfo();
                        warrantPayer.setRelZQ(false);
                        warrantPayer.setTrxAmt(warrantyAmt);
                        warrantPayer.setTrxMemo(PkgUtils.getPkgIdNameStr(productPackage.getId()) + "_" + period + "清分,担保机构支付代偿金额"
                                + warrantyAmt + "元");
                        warrantPayer.setUserId(product.getWarrantId());
                        warrantPayer.setUseType(EFundUseType.CMPNSAMT_PAY);

                        // 融资人收担保方支付的代偿金额
                        TransferInfo fncrPayee = new TransferInfo();
                        fncrPayee.setRelZQ(false);
                        fncrPayee.setTrxAmt(warrantyAmt);
                        fncrPayee.setTrxMemo(PkgUtils.getPkgIdNameStr(productPackage.getId()) + "_" + period + "清分,担保机构代偿" + warrantyAmt
                                + "元");
                        fncrPayee.setUserId(owner);
                        fncrPayee.setUseType(EFundUseType.CMPNSAMT_PAY);

                        // 担保方代偿，支付代偿款
                        warrantCmpnsAmtService.payCmpnsAmtOnPayment(eventId, warrantPayer, fncrPayee, bizId, 
                        		packageId, String.valueOf(period), operatorId, workDate);
                    }
                }

                // 融资人账户更新为只收不付
                FreezeReq frzAcct = new FreezeReq();
                frzAcct.setBizId(bizId);
                frzAcct.setCurrOpId(operatorId);
                frzAcct.setTrxAmt(BigDecimal.ZERO);
                frzAcct.setTrxMemo(PkgUtils.getPkgIdNameStr(productPackage.getId()) + "_" + period + "清分,担保公司代偿，会员资金账户冻结");
                frzAcct.setUserId(owner);
                frzAcct.setUseType(EFundUseType.BIZFREEZEFUNDACCT);
                frzAcct.setWorkDate(workDate);
                // 更新账户为只收不付
                String acctFrzJnlNo = fundAcctFreezeMgtService.freezeAcct(frzAcct, true);
                // 代偿融资人账户冻结编号
                schedule.setCmpnsFrzId(acctFrzJnlNo);
                // 设置还款计划上的代偿金额
                schedule.setCmpnsPyAmt(warrantyAmt);
                // 设置代偿时间
                schedule.setCmpnsTs(DateUtils.formatYYYYMMDDHHmmss(workDate));
            }
        } else {
            // 融资人可以自己完成还款

            if (PkgUtils.isLastSchedule(schedule)) {

                if (StringUtils.isNotBlank(productPackage.getWrtrFnrJnlNo())) {
                    boolean fnrActive = freezeReserveDtlService.isfnrDtlActive(productPackage.getWrtrFnrJnlNo());
                    if (fnrActive) {
                        // 退担保保证金
                        UnReserveReq uReq = new UnReserveReq();
                        uReq.setCurrOpId(operatorId);
                        uReq.setReserveJnlNo(productPackage.getWrtrFnrJnlNo());
                        uReq.setTrxMemo(PkgUtils.getPkgIdNameStr(productPackage.getId()) + "_" + period + "期完全清分，担保保证金退回");
                        uReq.setUserId(product.getWarrantId());
                        uReq.setWorkDate(workDate);
                        warrantDepositService.refundWarrantDeposit(uReq);
                    }
                }
            }
        }

        // 代偿划转后，融资人账户新的可还款金额
        BigDecimal newAvlAmt = financierBreachFundClearingService.getUserCurrentAvlAmtIgnoreFrzAmt(owner, true);

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

        // 融资人实际应付金额
        BigDecimal fncrRealPayTotalAmt = BigDecimal.ZERO;

        // 投资人实际收到融资人支付的总额
        BigDecimal buyerRealTotalRecvAmt = BigDecimal.ZERO;

        BigDecimal fncrPayWrtrPrinFineAmt = BigDecimal.ZERO;

        BigDecimal fncrPayWrtrIntrFineAmt = BigDecimal.ZERO;

        // 投资人实际收到担保方本金逾期代偿罚金总额
        BigDecimal buyerRealTotalRecvWrtrOverduePrinCmpnsFineAmt = BigDecimal.ZERO;

        // 投资人实际收到担保方利息逾期代偿罚金总额
        BigDecimal buyerRealTotalRecvWrtrOverdueIntrCmpnsFineAmt = BigDecimal.ZERO;

        // 平台实际收款金额
        BigDecimal platRealRecvTotalAmt = BigDecimal.ZERO;

        // 平台实际收到投资人利息、利息罚金收益比例金额
        BigDecimal platRealInvsTotalPrfAmt = BigDecimal.ZERO;

        // 还款计算误差金额
        BigDecimal repayDeviationAmt = BigDecimal.ZERO;

        // 可以全额还款完成
        if (fncrCanFinish || togetherCanFinish) {

            // 可还款结束，实际还款额=总共应还款额
            fncrRealPayTotalAmt = totalNeedPayAmt.subtract(wrtrFineAmt);

            fncrPayWrtrPrinFineAmt = wrtrPrinForfBuyer;

            fncrPayWrtrIntrFineAmt = wrtrIntrForfBuyer;

            // 按头寸处理投资人收款
            for (PositionPo pos : positions) {

                BigDecimal prinBuyerAc = BigDecimal.ZERO;

                BigDecimal prinForfBuyerAc = BigDecimal.ZERO;

                BigDecimal intrBuyerAc = BigDecimal.ZERO;

                BigDecimal intrForfBuyerAc = BigDecimal.ZERO;

                BigDecimal wrtrPrinForfBuyerAc = BigDecimal.ZERO;

                BigDecimal wrtrIntrForfBuyerAc = BigDecimal.ZERO;

                prinBuyerAc = prinBuyer.multiply(BigDecimal.valueOf(pos.getUnit().longValue())).divide(supplyUnit,
                        saveScale, RoundingMode.DOWN);

                prinForfBuyerAc = prinForfBuyer.multiply(BigDecimal.valueOf(pos.getUnit().longValue())).divide(
                        supplyUnit, saveScale, RoundingMode.DOWN);

                intrBuyerAc = intrBuyer.multiply(BigDecimal.valueOf(pos.getUnit().longValue())).divide(supplyUnit,
                        saveScale, RoundingMode.DOWN);

                intrForfBuyerAc = intrForfBuyer.multiply(BigDecimal.valueOf(pos.getUnit().longValue())).divide(
                        supplyUnit, saveScale, RoundingMode.DOWN);

                wrtrPrinForfBuyerAc = wrtrPrinForfBuyer.multiply(BigDecimal.valueOf(pos.getUnit().longValue())).divide(
                        supplyUnit, saveScale, RoundingMode.DOWN);

                wrtrIntrForfBuyerAc = wrtrIntrForfBuyer.multiply(BigDecimal.valueOf(pos.getUnit().longValue())).divide(
                        supplyUnit, saveScale, RoundingMode.DOWN);

                // 实际支付给投资人的钱
                BigDecimal buyerRealRecvAmt = intrBuyerAc.add(intrForfBuyerAc).add(prinBuyerAc).add(prinForfBuyerAc);

                // 担保方实际付给投资人的钱
                BigDecimal buyerRealRecvWrtrAmt = wrtrPrinForfBuyerAc.add(wrtrIntrForfBuyerAc);

                // 累计实际付给投资人总金额
                buyerRealTotalRecvAmt = buyerRealTotalRecvAmt.add(buyerRealRecvAmt);

                buyerRealTotalRecvWrtrOverduePrinCmpnsFineAmt = buyerRealTotalRecvWrtrOverduePrinCmpnsFineAmt
                        .add(wrtrPrinForfBuyerAc);

                buyerRealTotalRecvWrtrOverdueIntrCmpnsFineAmt = buyerRealTotalRecvWrtrOverdueIntrCmpnsFineAmt
                        .add(wrtrIntrForfBuyerAc);

                // 此投资人利息，利息罚金的收益部分的一定比例划转至平台的金额
                BigDecimal toPlatPrfAmt = intrBuyerAc.add(intrForfBuyerAc).add(wrtrIntrForfBuyerAc)
                        .multiply(deductRate).setScale(saveScale, RoundingMode.DOWN);

                // 平台收取投资人收益部分累计金额
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

                if (prinForfBuyerAc.compareTo(BigDecimal.ZERO) > 0) {
                    TransferInfo prinForfReq = new TransferInfo();
                    prinForfReq.setUserId(pos.getUserId());
                    prinForfReq.setRelZQ(true);
                    prinForfReq.setTrxAmt(prinForfBuyerAc);
                    prinForfReq.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "_" + period + "清分，收到本金罚金" + prinForfBuyerAc + "元");
                    prinForfReq.setUseType(EFundUseType.PRIN_FINE_REPAYMENT);
                    payeeList.add(prinForfReq);
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

                if (intrForfBuyerAc.compareTo(BigDecimal.ZERO) > 0) {
                    TransferInfo intrForfReq = new TransferInfo();
                    intrForfReq.setUserId(pos.getUserId());
                    intrForfReq.setRelZQ(false);
                    intrForfReq.setTrxAmt(intrForfBuyerAc);
                    intrForfReq.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "_" + period + "清分，收到利息罚金" + intrForfBuyerAc + "元");
                    intrForfReq.setUseType(EFundUseType.INTR_FINE_REPAYMENT);
                    payeeList.add(intrForfReq);
                }

                if (wrtrPrinForfBuyerAc.compareTo(BigDecimal.ZERO) > 0) {
                    TransferInfo intrForfReq = new TransferInfo();
                    intrForfReq.setUserId(pos.getUserId());
                    intrForfReq.setRelZQ(false);
                    intrForfReq.setTrxAmt(wrtrPrinForfBuyerAc);
                    intrForfReq.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "_" + period + "清分，收到担保方本金代偿逾期罚金" + wrtrPrinForfBuyerAc
                            + "元");
                    intrForfReq.setUseType(EFundUseType.PRIN_CMPNS_OVERDUE_FINE);
                    payeeList.add(intrForfReq);
                }

                if (wrtrIntrForfBuyerAc.compareTo(BigDecimal.ZERO) > 0) {
                    TransferInfo intrForfReq = new TransferInfo();
                    intrForfReq.setUserId(pos.getUserId());
                    intrForfReq.setRelZQ(false);
                    intrForfReq.setTrxAmt(wrtrIntrForfBuyerAc);
                    intrForfReq.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "_" + period + "清分，收到担保方利息代偿逾期罚金" + wrtrIntrForfBuyerAc
                            + "元");
                    intrForfReq.setUseType(EFundUseType.INTR_CMPNS_OVERDUE_FINE);
                    payeeList.add(intrForfReq);
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
                    BigDecimal realRecvAmt = buyerRealRecvAmt.add(buyerRealRecvWrtrAmt);
                    BigDecimal lotAddAmt = realRecvAmt.multiply(BigDecimal.valueOf(lot.getUnit().longValue()))
                            .divide(BigDecimal.valueOf(pos.getUnit().longValue()),saveScale, RoundingMode.DOWN);
                    accurAssignAmt = accurAssignAmt.add(lotAddAmt);
                    if ((size - 1) != i) {
                        lot.setAccumCrAmt(oldAccumCrAmt.add(lotAddAmt));
                    } else {
                        // 份额分配金额误差金额 = 投资人实收金额 - 已分配金额, 误差金额加入最后一个份额
                        BigDecimal assignErrorAmt = AmtUtils.max(realRecvAmt.subtract(accurAssignAmt), BigDecimal.ZERO);
                        lot.setAccumCrAmt(oldAccumCrAmt.add(lotAddAmt).add(assignErrorAmt));
                    }
                    if (needToUpdatePositionToZero) {
                        // 最后一期，份额的份数清零
                        lot.setUnit(BigDecimal.ZERO.intValue());
                    }
                    lot.setLastMntOpid(operatorId);
                    lot.setLastMntTs(workDate);
                }
                // 更新头寸份额表
                positionLotService.savePositionLots(lots);

                // 如果是最后一期，则更新头寸份额头寸表
                if (needToUpdatePositionToZero) {
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
            // 交易费用金额
            BigDecimal feePlatAc = feePlat;
            // 交易费用罚金金额
            BigDecimal feeForfPlatAc = feeForfPlat;

            if (intrProfitToPlat.compareTo(BigDecimal.ZERO) > 0) {
                TransferInfo prfReq = new TransferInfo();
                prfReq.setUserId(platformUserId);
                prfReq.setRelZQ(false);
                prfReq.setTrxAmt(intrProfitToPlat);
                prfReq.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "_" + period + "清分，平台收到投资人利息收益部分金额：" + intrProfitToPlat + "元");
                prfReq.setUseType(EFundUseType.PROFITINVS2EXCH);
                profPayeeList.add(prfReq);
            }

            if (feePlatAc.compareTo(BigDecimal.ZERO) > 0) {
                TransferInfo feeReq = new TransferInfo();
                feeReq.setUserId(platformUserId);
                feeReq.setRelZQ(false);
                feeReq.setTrxAmt(feePlatAc);
                feeReq.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "_" + period + "清分，平台收到费用：" + feePlatAc + "元");
                feeReq.setUseType(EFundUseType.TRADEEXPENSE);
                payeeList.add(feeReq);
            }

            if (feeForfPlatAc.compareTo(BigDecimal.ZERO) > 0) {
                TransferInfo feeForfReq = new TransferInfo();
                feeForfReq.setUserId(platformUserId);
                feeForfReq.setRelZQ(false);
                feeForfReq.setTrxAmt(feeForfPlatAc);
                feeForfReq.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "_" + period + "清分，平台收到费用罚金" + feeForfPlatAc + "元");
                feeForfReq.setUseType(EFundUseType.TRADEEXPENSE_FINE);
                payeeList.add(feeForfReq);
            }

            // 平台实际收到金额
            platRealRecvTotalAmt = intrProfitToPlat.add(feePlatAc).add(feeForfPlatAc);

            // 融资人划转至平台的金额
            BigDecimal fncrToPlatTotalAmt = feePlatAc.add(feeForfPlatAc);

            // 还款误差金额 = 融资人实际应还金额 - 投资人实际收款金额 - 平台实际收款金额
            repayDeviationAmt = AmtUtils.processNegativeAmt(fncrRealPayTotalAmt.subtract(buyerRealTotalRecvAmt)
                    .subtract(fncrToPlatTotalAmt), BigDecimal.ZERO);

            // 担保公司逾期代偿罚金计算误差
            BigDecimal wrtrPayPrinFineDevationAmt = AmtUtils.processNegativeAmt(
                    fncrPayWrtrPrinFineAmt.subtract(buyerRealTotalRecvWrtrOverduePrinCmpnsFineAmt), BigDecimal.ZERO);

            BigDecimal wrtrPayIntrFineDevationAmt = AmtUtils.processNegativeAmt(
                    fncrPayWrtrIntrFineAmt.subtract(buyerRealTotalRecvWrtrOverdueIntrCmpnsFineAmt), BigDecimal.ZERO);

            repayDeviationAmt = repayDeviationAmt.add(wrtrPayPrinFineDevationAmt).add(wrtrPayIntrFineDevationAmt);

            // 可还清，更新还款计划状态
            if (canCmpns) {
                schedule.setStatus(EPayStatus.CLEARED);
            } else {
                schedule.setStatus(EPayStatus.FINISH);
            }
            schedule.setPayPrincipalAmt(schedule.getPrincipalAmt());
            schedule.setPayInterestAmt(schedule.getInterestAmt());
            schedule.setPayAmt(schedule.getFeeAmt());
            if (fncrNeedPayFineAmt) {
                schedule.setPayPrincipalForfeit(schedule.getPrincipalForfeit());
                schedule.setPayInterestForfeit(schedule.getInterestForfeit());
                schedule.setPayFeeForfeit(schedule.getFeeForfeit());
            } else {
                schedule.setPrincipalForfeit(schedule.getPayPrincipalForfeit());
                schedule.setInterestForfeit(schedule.getPayInterestForfeit());
                schedule.setFeeForfeit(schedule.getPayFeeForfeit());
            }

            if (wrtrNeedPayFineAmt) {
                schedule.setPayWrtrPrinForfeit(schedule.getWrtrPrinForfeit());
                schedule.setPayWrtrInterestForfeit(schedule.getWrtrInterestForfeit());
            } else {
                schedule.setWrtrPrinForfeit(schedule.getPayWrtrPrinForfeit());
                schedule.setWrtrInterestForfeit(schedule.getPayWrtrInterestForfeit());
            }

            schedule.setLastMntOpid(operatorId);
            schedule.setLastMntTs(new Date());
            schedule.setLastPayTs(DateUtils.formatYYYYMMDDHHmmss(workDate));

        } else {

            BigDecimal prinBuyerRtAmt = BigDecimal.ZERO;
            BigDecimal prinForfBuyerRtAmt = BigDecimal.ZERO;
            BigDecimal intrBuyerRtAmt = BigDecimal.ZERO;
            BigDecimal intrForfBuyerRtAmt = BigDecimal.ZERO;
            BigDecimal feePlatRtAmt = BigDecimal.ZERO;
            BigDecimal feeForfPlatRtAmt = BigDecimal.ZERO;
            BigDecimal wrtrPrinForfBuyerRtAmt = BigDecimal.ZERO;
            BigDecimal wrtrIntrForfBuyerRtAmt = BigDecimal.ZERO;

            BigDecimal pdPrinAmt = BigDecimal.ZERO;
            BigDecimal pdIntrAmt = BigDecimal.ZERO;
            BigDecimal pdPrinFineAmt = BigDecimal.ZERO;
            BigDecimal pdIntrFineAmt = BigDecimal.ZERO;
            BigDecimal pdFeeAmt = BigDecimal.ZERO;
            BigDecimal pdFeeFineAmt = BigDecimal.ZERO;
            BigDecimal pdWrtrPrinFineAmt = BigDecimal.ZERO;
            BigDecimal pdWrtrIntrFineAmt = BigDecimal.ZERO;

            // 可代偿
            if (canCmpns) {
                BigDecimal avlCalAmt = BigDecimal.ZERO;
                BigDecimal totalNeedPayExceptCmpnsAmt = BigDecimal.ZERO;
                EWarrantyType warrantyType = product.getWarrantyType();
                if (warrantyType == EWarrantyType.PRINCIPAL) {// 本金担保
                    pdPrinAmt = AmtUtils.processNegativeAmt(
                            schedule.getPrincipalAmt().subtract(schedule.getPayPrincipalAmt()), BigDecimal.ZERO);
                    prinBuyerRtAmt = pdPrinAmt;
                    totalNeedPayExceptCmpnsAmt = AmtUtils.processNegativeAmt(totalNeedPayAmt.subtract(prinBuyerRtAmt),
                            BigDecimal.ZERO);
                    avlCalAmt = AmtUtils.processNegativeAmt(newAvlAmt.subtract(pdPrinAmt), BigDecimal.ZERO);
                } else if (warrantyType == EWarrantyType.PRINCIPALINTEREST) {// 本息担保
                    pdPrinAmt = AmtUtils.processNegativeAmt(
                            schedule.getPrincipalAmt().subtract(schedule.getPayPrincipalAmt()), BigDecimal.ZERO);
                    pdIntrAmt = AmtUtils.processNegativeAmt(
                            schedule.getInterestAmt().subtract(schedule.getPayInterestAmt()), BigDecimal.ZERO);
                    prinBuyerRtAmt = pdPrinAmt;
                    intrBuyerRtAmt = pdIntrAmt;
                    totalNeedPayExceptCmpnsAmt = AmtUtils.processNegativeAmt(totalNeedPayAmt.subtract(prinBuyerRtAmt)
                            .subtract(intrBuyerRtAmt), BigDecimal.ZERO);
                    avlCalAmt = AmtUtils.processNegativeAmt(newAvlAmt.subtract(pdPrinAmt).subtract(pdIntrAmt),
                            BigDecimal.ZERO);
                }

                if (totalNeedPayExceptCmpnsAmt.compareTo(BigDecimal.ZERO) == 0) {
                    totalNeedPayExceptCmpnsAmt = BigDecimal.ONE;
                }

                prinForfBuyerRtAmt = prinForfBuyer.multiply(avlCalAmt).divide(totalNeedPayExceptCmpnsAmt, saveScale,
                        RoundingMode.DOWN);
                intrForfBuyerRtAmt = intrForfBuyer.multiply(avlCalAmt).divide(totalNeedPayExceptCmpnsAmt, saveScale,
                        RoundingMode.DOWN);
                feePlatRtAmt = feePlat.multiply(avlCalAmt).divide(totalNeedPayExceptCmpnsAmt, saveScale,
                        RoundingMode.DOWN);
                feeForfPlatRtAmt = feeForfPlat.multiply(avlCalAmt).divide(totalNeedPayExceptCmpnsAmt, saveScale,
                        RoundingMode.DOWN);
                wrtrPrinForfBuyerRtAmt = wrtrPrinForfBuyer.multiply(avlCalAmt).divide(totalNeedPayExceptCmpnsAmt,
                        saveScale, RoundingMode.DOWN);
                wrtrIntrForfBuyerRtAmt = wrtrIntrForfBuyer.multiply(avlCalAmt).divide(totalNeedPayExceptCmpnsAmt,
                        saveScale, RoundingMode.DOWN);

                pdPrinFineAmt = prinForfBuyerRtAmt;
                if (warrantyType == EWarrantyType.PRINCIPAL) {
                    intrBuyerRtAmt = intrBuyer.multiply(avlCalAmt).divide(totalNeedPayExceptCmpnsAmt, saveScale,
                            RoundingMode.DOWN);
                    pdIntrAmt = intrBuyerRtAmt;
                }
                pdIntrFineAmt = intrForfBuyerRtAmt;
                pdFeeAmt = feePlatRtAmt;
                pdFeeFineAmt = feeForfPlatRtAmt;
                pdWrtrPrinFineAmt = wrtrPrinForfBuyerRtAmt;
                pdWrtrIntrFineAmt = wrtrIntrForfBuyerRtAmt;

                // 设置为已代偿
                schedule.setStatus(EPayStatus.COMPENSATORY);
            } else {

                prinBuyerRtAmt = prinBuyer.multiply(newAvlAmt).divide(totalNeedPayAmt, saveScale, RoundingMode.DOWN);
                prinForfBuyerRtAmt = prinForfBuyer.multiply(newAvlAmt).divide(totalNeedPayAmt, saveScale,
                        RoundingMode.DOWN);
                intrBuyerRtAmt = intrBuyer.multiply(newAvlAmt).divide(totalNeedPayAmt, saveScale, RoundingMode.DOWN);
                intrForfBuyerRtAmt = intrForfBuyer.multiply(newAvlAmt).divide(totalNeedPayAmt, saveScale,
                        RoundingMode.DOWN);
                feePlatRtAmt = feePlat.multiply(newAvlAmt).divide(totalNeedPayAmt, saveScale, RoundingMode.DOWN);
                feeForfPlatRtAmt = feeForfPlat.multiply(newAvlAmt)
                        .divide(totalNeedPayAmt, saveScale, RoundingMode.DOWN);
                wrtrPrinForfBuyerRtAmt = wrtrPrinForfBuyer.multiply(newAvlAmt).divide(totalNeedPayAmt, saveScale,
                        RoundingMode.DOWN);
                wrtrIntrForfBuyerRtAmt = wrtrIntrForfBuyer.multiply(newAvlAmt).divide(totalNeedPayAmt, saveScale,
                        RoundingMode.DOWN);

                pdPrinAmt = prinBuyerRtAmt;
                pdIntrAmt = intrBuyerRtAmt;
                pdPrinFineAmt = prinForfBuyerRtAmt;
                pdIntrFineAmt = intrForfBuyerRtAmt;
                pdFeeAmt = feePlatRtAmt;
                pdFeeFineAmt = feeForfPlatRtAmt;
                pdWrtrPrinFineAmt = wrtrPrinForfBuyerRtAmt;
                pdWrtrIntrFineAmt = wrtrIntrForfBuyerRtAmt;

                schedule.setStatus(schedule.getStatus());
            }

            // 实际分配至还款计划各项金额之和
            BigDecimal assignAmt = BigDecimal.ZERO;
            assignAmt = assignAmt.add(pdPrinAmt);
            assignAmt = assignAmt.add(pdIntrAmt);
            assignAmt = assignAmt.add(pdPrinFineAmt);
            assignAmt = assignAmt.add(pdIntrFineAmt);
            assignAmt = assignAmt.add(pdFeeAmt);
            assignAmt = assignAmt.add(pdFeeFineAmt);

            BigDecimal wrtrPrinAssignAmt = BigDecimal.ZERO;
            wrtrPrinAssignAmt = wrtrPrinAssignAmt.add(pdWrtrPrinFineAmt);
            BigDecimal wrtrIntrAssignAmt = BigDecimal.ZERO;
            wrtrIntrAssignAmt = wrtrIntrAssignAmt.add(pdWrtrIntrFineAmt);

            schedule.setPayPrincipalAmt(schedule.getPayPrincipalAmt().add(pdPrinAmt));
            schedule.setPayInterestAmt(schedule.getPayInterestAmt().add(pdIntrAmt));
            schedule.setPayAmt(schedule.getPayAmt().add(pdFeeAmt));

            schedule.setPayPrincipalForfeit(schedule.getPayPrincipalForfeit().add(pdPrinFineAmt));
            schedule.setPayInterestForfeit(schedule.getPayInterestForfeit().add(pdIntrFineAmt));
            schedule.setPayFeeForfeit(schedule.getPayFeeForfeit().add(pdFeeFineAmt));

            schedule.setPayWrtrPrinForfeit(schedule.getPayWrtrPrinForfeit().add(pdWrtrPrinFineAmt));
            schedule.setPayWrtrInterestForfeit(schedule.getPayWrtrInterestForfeit().add(pdWrtrIntrFineAmt));

            schedule.setLastMntOpid(operatorId);
            schedule.setLastMntTs(new Date());
            schedule.setLastPayTs(DateUtils.formatYYYYMMDDHHmmss(workDate));

            BigDecimal totalAssignAmt = assignAmt.add(wrtrPrinAssignAmt).add(wrtrIntrAssignAmt);

            if (totalAssignAmt.compareTo(BigDecimal.ZERO) > 0) {
                // 按头寸处理投资人收款
                for (PositionPo pos : positions) {

                    BigDecimal prinBuyerAc = BigDecimal.ZERO;

                    BigDecimal prinForfBuyerAc = BigDecimal.ZERO;

                    BigDecimal intrBuyerAc = BigDecimal.ZERO;

                    BigDecimal intrForfBuyerAc = BigDecimal.ZERO;

                    BigDecimal wrtrPrinForfBuyerAc = BigDecimal.ZERO;

                    BigDecimal wrtrIntrForfBuyerAc = BigDecimal.ZERO;

                    prinBuyerAc = prinBuyerRtAmt.multiply(BigDecimal.valueOf(pos.getUnit().longValue())).divide(
                            supplyUnit, saveScale, RoundingMode.DOWN);

                    prinForfBuyerAc = prinForfBuyerRtAmt.multiply(BigDecimal.valueOf(pos.getUnit().longValue()))
                            .divide(supplyUnit, saveScale, RoundingMode.DOWN);

                    intrBuyerAc = intrBuyerRtAmt.multiply(BigDecimal.valueOf(pos.getUnit().longValue())).divide(
                            supplyUnit, saveScale, RoundingMode.DOWN);

                    intrForfBuyerAc = intrForfBuyerRtAmt.multiply(BigDecimal.valueOf(pos.getUnit().longValue()))
                            .divide(supplyUnit, saveScale, RoundingMode.DOWN);

                    wrtrPrinForfBuyerAc = wrtrPrinForfBuyerRtAmt
                            .multiply(BigDecimal.valueOf(pos.getUnit().longValue())).divide(supplyUnit, saveScale,
                                    RoundingMode.DOWN);

                    wrtrIntrForfBuyerAc = wrtrIntrForfBuyerRtAmt
                            .multiply(BigDecimal.valueOf(pos.getUnit().longValue())).divide(supplyUnit, saveScale,
                                    RoundingMode.DOWN);

                    // 实际支付给投资人的钱
                    BigDecimal buyerRealRecvAmt = intrBuyerAc.add(intrForfBuyerAc).add(prinBuyerAc)
                            .add(prinForfBuyerAc);

                    // 累计实际付给投资人总金额
                    buyerRealTotalRecvAmt = buyerRealTotalRecvAmt.add(buyerRealRecvAmt);

                    // 担保方实际付给投资人的钱
                    BigDecimal buyerRealRecvWrtrAmt = wrtrPrinForfBuyerAc.add(wrtrIntrForfBuyerAc);

                    buyerRealTotalRecvWrtrOverduePrinCmpnsFineAmt = buyerRealTotalRecvWrtrOverduePrinCmpnsFineAmt
                            .add(wrtrPrinForfBuyerAc);

                    buyerRealTotalRecvWrtrOverdueIntrCmpnsFineAmt = buyerRealTotalRecvWrtrOverdueIntrCmpnsFineAmt
                            .add(wrtrIntrForfBuyerAc);

                    // 此投资人利息，利息罚金的收益部分的一定比例划转至平台的金额
                    BigDecimal toPlatPrfAmt = intrBuyerAc.add(intrForfBuyerAc).add(wrtrIntrForfBuyerAc)
                            .multiply(deductRate).setScale(saveScale, RoundingMode.DOWN);

                    // 平台收取投资人收益部分累计金额
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

                    if (prinForfBuyerAc.compareTo(BigDecimal.ZERO) > 0) {
                        TransferInfo prinForfReq = new TransferInfo();
                        prinForfReq.setUserId(pos.getUserId());
                        prinForfReq.setRelZQ(true);
                        prinForfReq.setTrxAmt(prinForfBuyerAc);
                        prinForfReq.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "_" + period + "清分，收到本金罚金" + prinForfBuyerAc + "元");
                        prinForfReq.setUseType(EFundUseType.PRIN_FINE_REPAYMENT);
                        payeeList.add(prinForfReq);
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

                    if (intrForfBuyerAc.compareTo(BigDecimal.ZERO) > 0) {
                        TransferInfo intrForfReq = new TransferInfo();
                        intrForfReq.setUserId(pos.getUserId());
                        intrForfReq.setRelZQ(false);
                        intrForfReq.setTrxAmt(intrForfBuyerAc);
                        intrForfReq.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "_" + period + "清分，收到利息罚金" + intrForfBuyerAc + "元");
                        intrForfReq.setUseType(EFundUseType.INTR_FINE_REPAYMENT);
                        payeeList.add(intrForfReq);
                    }

                    if (wrtrPrinForfBuyerAc.compareTo(BigDecimal.ZERO) > 0) {
                        TransferInfo intrForfReq = new TransferInfo();
                        intrForfReq.setUserId(pos.getUserId());
                        intrForfReq.setRelZQ(false);
                        intrForfReq.setTrxAmt(wrtrPrinForfBuyerAc);
                        intrForfReq.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "_" + period + "清分，收到本金担保代偿逾期罚金"
                                + wrtrPrinForfBuyerAc + "元");
                        intrForfReq.setUseType(EFundUseType.PRIN_CMPNS_OVERDUE_FINE);
                        payeeList.add(intrForfReq);
                    }

                    if (wrtrIntrForfBuyerAc.compareTo(BigDecimal.ZERO) > 0) {
                        TransferInfo intrForfReq = new TransferInfo();
                        intrForfReq.setUserId(pos.getUserId());
                        intrForfReq.setRelZQ(false);
                        intrForfReq.setTrxAmt(wrtrIntrForfBuyerAc);
                        intrForfReq.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "_" + period + "清分，收到利息担保代偿逾期罚金"
                                + wrtrIntrForfBuyerAc + "元");
                        intrForfReq.setUseType(EFundUseType.INTR_CMPNS_OVERDUE_FINE);
                        payeeList.add(intrForfReq);
                    }

                    if (toPlatPrfAmt.compareTo(BigDecimal.ZERO) > 0) {
                        TransferInfo prfReq = new TransferInfo();
                        prfReq.setUserId(pos.getUserId());
                        prfReq.setRelZQ(false);
                        prfReq.setTrxAmt(toPlatPrfAmt);
                        prfReq.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "_" + period + "清分，平台收取利息、利息罚金收益比例金额为" + toPlatPrfAmt
                                + "元");
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
                        BigDecimal realRecvAmt = buyerRealRecvAmt.add(buyerRealRecvWrtrAmt);
                        BigDecimal lotAddAmt = realRecvAmt.multiply(BigDecimal.valueOf(lot.getUnit().longValue()))
                                .divide(BigDecimal.valueOf(pos.getUnit().longValue()),saveScale, RoundingMode.DOWN);
                        accurAssignAmt = accurAssignAmt.add(lotAddAmt);
                        if ((size - 1) != i) {
                            lot.setAccumCrAmt(oldAccumCrAmt.add(lotAddAmt));
                        } else {
                            // 份额分配金额误差金额 = 投资人实收金额 - 已分配金额, 误差金额加入最后一个份额
                            BigDecimal assignErrorAmt = AmtUtils.max(realRecvAmt.subtract(accurAssignAmt),
                                    BigDecimal.ZERO);
                            lot.setAccumCrAmt(oldAccumCrAmt.add(lotAddAmt).add(assignErrorAmt));
                        }
                        lot.setLastMntOpid(operatorId);
                        lot.setLastMntTs(workDate);
                    }
                    // 更新头寸份额表
                    positionLotService.savePositionLots(lots);
                }
            }

            // 获取平台ID
            String platformUserId = CommonBusinessUtil.getExchangeUserId();
            // 投资人利息，利息罚金收益部分划转至平台金额
            BigDecimal intrProfitToPlat = platRealInvsTotalPrfAmt;
            // 交易费用金额
            BigDecimal feePlatAc = feePlatRtAmt;
            // 交易费用罚金金额
            BigDecimal feeForfPlatAc = feeForfPlatRtAmt;

            if (intrProfitToPlat.compareTo(BigDecimal.ZERO) > 0) {
                TransferInfo intrReq = new TransferInfo();
                intrReq.setUserId(platformUserId);
                intrReq.setRelZQ(false);
                intrReq.setTrxAmt(intrProfitToPlat);
                intrReq.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "_" + period + "清分，平台收到投资人利息收益部分金额：" + intrProfitToPlat + "元");
                intrReq.setUseType(EFundUseType.PROFITINVS2EXCH);
                profPayeeList.add(intrReq);
            }

            if (feePlatAc.compareTo(BigDecimal.ZERO) > 0) {
                TransferInfo feeReq = new TransferInfo();
                feeReq.setUserId(platformUserId);
                feeReq.setRelZQ(false);
                feeReq.setTrxAmt(feePlatAc);
                feeReq.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "_" + period + "清分，平台收到费用：" + feePlatAc + "元");
                feeReq.setUseType(EFundUseType.TRADEEXPENSE);
                payeeList.add(feeReq);
            }

            if (feeForfPlatAc.compareTo(BigDecimal.ZERO) > 0) {
                TransferInfo feeForfReq = new TransferInfo();
                feeForfReq.setUserId(platformUserId);
                feeForfReq.setRelZQ(false);
                feeForfReq.setTrxAmt(feeForfPlatAc);
                feeForfReq.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "_" + period + "清分，平台收到费用罚金" + feeForfPlatAc + "元");
                feeForfReq.setUseType(EFundUseType.TRADEEXPENSE_FINE);
                payeeList.add(feeForfReq);
            }

            // 计算剩余欠款金额
            BigDecimal newPyAmt = schedule.getPrincipalAmt().add(schedule.getPrincipalForfeit());
            newPyAmt = newPyAmt.add(schedule.getInterestAmt()).add(schedule.getInterestForfeit());
            newPyAmt = newPyAmt.add(schedule.getFeeAmt()).add(schedule.getFeeForfeit());
            newPyAmt = newPyAmt.add(schedule.getWrtrPrinForfeit()).add(schedule.getWrtrInterestForfeit());

            BigDecimal newPdAmt = schedule.getPayPrincipalAmt().add(schedule.getPayPrincipalForfeit());
            newPdAmt = newPdAmt.add(schedule.getPayInterestAmt()).add(schedule.getPayInterestForfeit());
            newPdAmt = newPdAmt.add(schedule.getPayAmt()).add(schedule.getPayFeeForfeit());
            newPdAmt = newPdAmt.add(schedule.getPayWrtrPrinForfeit()).add(schedule.getPayWrtrInterestForfeit());

            BigDecimal diffAmt = AmtUtils.processNegativeAmt(newPyAmt.subtract(newPdAmt), BigDecimal.ZERO);
            if(diffAmt.compareTo(BigDecimal.ZERO)>0){
	            // 重新冻结一笔新的欠款明细
	            FreezeReq fReq = new FreezeReq();
	            fReq.setBizId(bizId);
	            fReq.setCurrOpId(operatorId);
	            fReq.setTrxAmt(diffAmt);
	            fReq.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "_" + period + "清分，部分还款，冻结新的欠款金额为" + diffAmt + "元");
	            fReq.setUserId(owner);
	            fReq.setUseType(EFundUseType.FNCR_REPAYMENT_PENALTY);
	            fReq.setWorkDate(workDate);
	            // 冻结欠款金额
	            String prinFrzId = financierBreachFundClearingService.freezeArrearageAmt(fReq);
	            schedule.setPrinFrzId(prinFrzId);
            }
            
            // 平台实际收到金额
            platRealRecvTotalAmt = intrProfitToPlat.add(feePlatAc).add(feeForfPlatAc);

            // 融资人划转至平台的金额
            BigDecimal fncrToPlatTotalAmt = feePlatAc.add(feeForfPlatAc);

            // 误差金额 = 实际分配至还款计划上的金额 - 投资人实收 - 交易所应收金额
            repayDeviationAmt = AmtUtils.processNegativeAmt(
                    assignAmt.subtract(buyerRealTotalRecvAmt).subtract(fncrToPlatTotalAmt), BigDecimal.ZERO);

            // 担保公司逾期代偿罚金计算误差
            BigDecimal wrtrPayPrinFineDevationAmt = AmtUtils.processNegativeAmt(
                    wrtrPrinAssignAmt.subtract(buyerRealTotalRecvWrtrOverduePrinCmpnsFineAmt), BigDecimal.ZERO);

            BigDecimal wrtrPayIntrFineDevationAmt = AmtUtils.processNegativeAmt(
                    wrtrIntrAssignAmt.subtract(buyerRealTotalRecvWrtrOverdueIntrCmpnsFineAmt), BigDecimal.ZERO);

            repayDeviationAmt = repayDeviationAmt.add(wrtrPayPrinFineDevationAmt).add(wrtrPayIntrFineDevationAmt);

            // 实际还款额 = 融资人实际收款金额 + 平台实际收款金额 + 计算误差金额 = 实际分配至还款计划上的金额
            fncrRealPayTotalAmt = assignAmt;

            fncrPayWrtrPrinFineAmt = wrtrPrinAssignAmt;

            fncrPayWrtrIntrFineAmt = wrtrIntrAssignAmt;

            BigDecimal netAmt = AmtUtils.processNegativeAmt(
                    newAvlAmt.subtract(fncrRealPayTotalAmt).subtract(fncrPayWrtrPrinFineAmt)
                            .subtract(fncrPayWrtrIntrFineAmt), BigDecimal.ZERO);

            if (LOG.isDebugEnabled()) {
                LOG.debug("--------- 融资人账户剩余金额为 --------" + netAmt + "元");
            }
        }

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

        if (fncrRealPayTotalAmt.compareTo(BigDecimal.ZERO) > 0) {
            TransferInfo dInfo = new TransferInfo();
            dInfo.setUserId(owner);
            dInfo.setRelZQ(true);
            dInfo.setTrxAmt(fncrRealPayTotalAmt);
            dInfo.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "_" + period + "清分， 支付还款金额" + fncrRealPayTotalAmt + "元");
            dInfo.setUseType(EFundUseType.FNCR_REPAYMENT);
            payerList.add(dInfo);
        }

        if (fncrPayWrtrPrinFineAmt.compareTo(BigDecimal.ZERO) > 0) {
            TransferInfo dInfo = new TransferInfo();
            dInfo.setUserId(owner);
            dInfo.setRelZQ(true);
            dInfo.setTrxAmt(fncrPayWrtrPrinFineAmt);
            dInfo.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "_" + period + "清分， 支付本金担保代偿逾期罚金" + fncrPayWrtrPrinFineAmt + "元");
            dInfo.setUseType(EFundUseType.PRIN_CMPNS_OVERDUE_FINE);
            payerList.add(dInfo);
        }

        if (fncrPayWrtrIntrFineAmt.compareTo(BigDecimal.ZERO) > 0) {
            TransferInfo dInfo = new TransferInfo();
            dInfo.setUserId(owner);
            dInfo.setRelZQ(true);
            dInfo.setTrxAmt(fncrPayWrtrIntrFineAmt);
            dInfo.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "_" + period + "清分， 支付利息担保代偿逾期罚金" + fncrPayWrtrIntrFineAmt + "元");
            dInfo.setUseType(EFundUseType.INTR_CMPNS_OVERDUE_FINE);
            payerList.add(dInfo);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("~~~~~~~~~~~~~~~~~平台收取总金额为" + platRealRecvTotalAmt.add(repayDeviationAmt) + "元");

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

            if (!fncrCanFinish && canCmpns) {
                if (togetherCanFinish) {
                    prodPkgMsgRemindService.pkgScheduleClearToCleared(schedule, owner, product.getWarrantId());
                } else {
                    prodPkgMsgRemindService.pkgScheduleClearToCompensatry(schedule, owner, product.getWarrantId());
                }
            }
        }
    }

    private CleaningPkgMsgHolder getCleaningPkgMsgHolder(BigDecimal avalAmt, PaymentSchedule schedule,
            ProductPackage pkg, Product product, String owner, String operatorId, Date workDate) {

        // 实际申购份数
        BigDecimal supplyUnit = pkg.getSupplyAmount().divide(pkg.getUnitAmount(), calcuScale, RoundingMode.DOWN);

        String packageId = pkg.getId();

        // 清分时平台费用扣除比例
        BigDecimal deductRate = AmtUtils.processNegativeAmt(CommonBusinessUtil.getClearingInterestDeductRate(),
                BigDecimal.ZERO);

        // 累计实际付给投资人的金额
        BigDecimal buyerRecvTotalAc = BigDecimal.ZERO;
        // 累计实际投资人付给平台的金额
        BigDecimal buyer2PlatTotalAc = BigDecimal.ZERO;
        // 累计实际付给平台的金额
        BigDecimal platRecvTotalAc = BigDecimal.ZERO;

        // ======================================================================

        PayScheMsgHolder scheMsgHolder = null;
        // 违约宽限天数(宽限期以内，不收违约金)
        Long breachGraceDays = Long.valueOf(ProdPkgUtil.getBreachGraceDays(product));
        Date paymentDate = schedule.getPaymentDate();
        boolean needPayFine = ProdPkgUtil.needPayFineAmt(paymentDate, workDate, breachGraceDays);
        switch (schedule.getStatus()) {
        case OVERDUE:
            if (needPayFine) {
                // 交罚金
                scheMsgHolder = ProdPkgUtil.resolvePaymentSchedule(schedule, true, true, true, true, true, true);
            } else {
                // 不用交罚金
                scheMsgHolder = ProdPkgUtil.resolvePaymentSchedule(schedule, true, true, true, false, false, false);
            }
            break;
        case COMPENSATORY:
            if (needPayFine) {
                // 交罚金
                scheMsgHolder = ProdPkgUtil.resolvePaymentSchedule(schedule, true, true, true, true, true, true);
            } else {
                // 不用交罚金
                scheMsgHolder = ProdPkgUtil.resolvePaymentSchedule(schedule, true, true, true, false, false, false);
            }
            break;
        default:
            scheMsgHolder = ProdPkgUtil.resolvePaymentSchedule(schedule, true, true, true, true, true, true);
        }

        // ===================================================================

        BigDecimal prinPay = scheMsgHolder.getPrinPay();
        BigDecimal prinFinePay = scheMsgHolder.getPrinFinePay();

        BigDecimal intrPay = scheMsgHolder.getIntrPay();
        BigDecimal intrFinePay = scheMsgHolder.getIntrFinePay();

        BigDecimal feePay = scheMsgHolder.getFeePay();
        BigDecimal feeFinePay = scheMsgHolder.getFeeFinePay();

        // 担保方应付本金罚金
        BigDecimal wrtrPrinForfeit = AmtUtils.processNegativeAmt(schedule.getWrtrPrinForfeit(), BigDecimal.ZERO);
        // 担保方实付本金罚金
        BigDecimal payWrtrPrinForfeit = AmtUtils.processNegativeAmt(schedule.getPayWrtrPrinForfeit(), BigDecimal.ZERO);
        // 担保方应付利息罚金
        BigDecimal wrtrInterestForfeit = AmtUtils
                .processNegativeAmt(schedule.getWrtrInterestForfeit(), BigDecimal.ZERO);
        // 担保方实付利息罚金
        BigDecimal payWrtrInterestForfeit = AmtUtils.processNegativeAmt(schedule.getPayWrtrInterestForfeit(),
                BigDecimal.ZERO);

        boolean needPayOverdueCmpnsFineAmt = ProdPkgUtil.needPayOverdueCmpnsFineAmt(schedule.getPaymentDate(),
                workDate, Arrays.asList(product.getCmpnsGracePeriod(), product.getOverdue2CmpnsGracePeriod()));

        // 担保本金罚金
        BigDecimal wrtrPrinForfPay = needPayOverdueCmpnsFineAmt ? AmtUtils.processNegativeAmt(
                wrtrPrinForfeit.subtract(payWrtrPrinForfeit), BigDecimal.ZERO) : BigDecimal.ZERO;

        // 担保利息罚金
        BigDecimal wrtrIntrForfPay = needPayOverdueCmpnsFineAmt ? AmtUtils.processNegativeAmt(
                wrtrInterestForfeit.subtract(payWrtrInterestForfeit), BigDecimal.ZERO) : BigDecimal.ZERO;

        BigDecimal record = prinPay.add(prinFinePay).add(intrPay).add(intrFinePay).add(feePay).add(feeFinePay);

        record = record.add(wrtrPrinForfPay).add(wrtrIntrForfPay);

        boolean canFinish = false;

        if (avalAmt.compareTo(record) >= 0) {
            canFinish = true;
        } else {
            prinPay = prinPay.multiply(avalAmt).divide(record, saveScale, RoundingMode.DOWN);
            prinFinePay = prinFinePay.multiply(avalAmt).divide(record, saveScale, RoundingMode.DOWN);
            intrPay = intrPay.multiply(avalAmt).divide(record, saveScale, RoundingMode.DOWN);
            intrFinePay = intrFinePay.multiply(avalAmt).divide(record, saveScale, RoundingMode.DOWN);
            feePay = feePay.multiply(avalAmt).divide(record, saveScale, RoundingMode.DOWN);
            feeFinePay = feeFinePay.multiply(avalAmt).divide(record, saveScale, RoundingMode.DOWN);
            wrtrPrinForfPay = wrtrPrinForfPay.multiply(avalAmt).divide(record, saveScale, RoundingMode.DOWN);
            wrtrIntrForfPay = wrtrIntrForfPay.multiply(avalAmt).divide(record, saveScale, RoundingMode.DOWN);
        }

        // 给投资人的应付利息
        BigDecimal intrBuyer = intrPay;
        // 给投资人的应付利息罚金
        BigDecimal intrForfBuyer = intrFinePay;
        // 给投资人的应付本金
        BigDecimal prinBuyer = prinPay;
        // 给投资人的应付本金罚金
        BigDecimal prinForfBuyer = prinFinePay;

        // TODO 给投资人的担保本金罚金
        BigDecimal wrtrPrinForfBuyer = wrtrPrinForfPay;
        // TODO 给投资人的担保利息罚金
        BigDecimal wrtrIntrForfBuyer = wrtrIntrForfPay;

        // 给平台的应付费用
        BigDecimal feePlat = feePay;
        // 给平台的应付费用罚金
        BigDecimal feeForfPlat = feeFinePay;

        BigDecimal fncrPayTotalAmt = intrBuyer.add(intrForfBuyer).add(prinBuyer).add(prinForfBuyer).add(feePlat)
                .add(feeForfPlat);

        fncrPayTotalAmt = fncrPayTotalAmt.add(wrtrPrinForfBuyer).add(wrtrIntrForfBuyer);

        // 获取平台ID
        String exchangeUserId = CommonBusinessUtil.getExchangeUserId();

        List<TransferInfo> payeeList = new ArrayList<TransferInfo>();
        List<TransferInfo> payerList = new ArrayList<TransferInfo>();
        List<TransferInfo> profPayeeList = new ArrayList<TransferInfo>();
        List<TransferInfo> profPayerList = new ArrayList<TransferInfo>();

        boolean needToUpdatePositionToZero = this.needToUpdatePositionToZero(schedule);

        List<PositionPo> positions = positionService.getByPkgId(pkg.getId());
        for (PositionPo p : positions) {

            // 已转让，头寸份额为 0
            if (p.getUnit().longValue() <= 0) {
                continue;
            }
            
            BigDecimal prinBuyerAc = prinBuyer.multiply(BigDecimal.valueOf(p.getUnit().longValue())).divide(supplyUnit,
                    saveScale, RoundingMode.DOWN);
            
            BigDecimal prinForfBuyerAc = prinForfBuyer.multiply(BigDecimal.valueOf(p.getUnit().longValue())).divide(
                    supplyUnit, saveScale, RoundingMode.DOWN);

            BigDecimal intrBuyerAc = intrBuyer.multiply(BigDecimal.valueOf(p.getUnit().longValue())).divide(supplyUnit,
                    saveScale, RoundingMode.DOWN);
            
            BigDecimal intrForfBuyerAc = intrForfBuyer.multiply(BigDecimal.valueOf(p.getUnit().longValue())).divide(
                    supplyUnit, saveScale, RoundingMode.DOWN);
            
            // 担保机构本金代偿逾期罚金
            BigDecimal wrtrPrinForfBuyerAc = wrtrPrinForfBuyer.multiply(BigDecimal.valueOf(p.getUnit().longValue()))
                    .divide(supplyUnit, saveScale, RoundingMode.DOWN);
            
            // 担保机构利息代偿逾期罚金
            BigDecimal wrtrIntrForfBuyerAc = wrtrIntrForfBuyer.multiply(BigDecimal.valueOf(p.getUnit().longValue()))
                    .divide(supplyUnit, saveScale, RoundingMode.DOWN);
            
            // 投资人收
            if (wrtrPrinForfBuyerAc.compareTo(BigDecimal.ZERO) > 0) {
                TransferInfo payeePrin = new TransferInfo();
                payeePrin.setUserId(p.getUserId());
                payeePrin.setRelZQ(false);
                payeePrin.setTrxAmt(wrtrPrinForfBuyerAc);
                payeePrin.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "_" + schedule.getSequenceId() + "清分，收到本金担保代偿逾期罚金"
                        + wrtrPrinForfBuyerAc + "元");
                payeePrin.setUseType(EFundUseType.PRIN_CMPNS_OVERDUE_FINE);
                payeeList.add(payeePrin);
            }
            // 投资人收担保机构利息代偿逾期罚金
            if (wrtrIntrForfBuyerAc.compareTo(BigDecimal.ZERO) > 0) {
                TransferInfo payeeIntr = new TransferInfo();
                payeeIntr.setUserId(p.getUserId());
                payeeIntr.setRelZQ(false);
                payeeIntr.setTrxAmt(wrtrIntrForfBuyerAc);
                payeeIntr.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "_" + schedule.getSequenceId() + "清分，收到利息担保代偿逾期罚金"
                        + wrtrIntrForfBuyerAc + "元");
                payeeIntr.setUseType(EFundUseType.INTR_CMPNS_OVERDUE_FINE);
                payeeList.add(payeeIntr);
            }
            
            // 投资人的收益的一定比例付给平台
            BigDecimal intrBuyer2PlatAc = intrBuyerAc.add(intrForfBuyerAc);
            // 投资人收到担保机构利息代偿逾期罚金收益分成给平台
            if(wrtrIntrForfBuyerAc.compareTo(BigDecimal.ZERO) > 0){
            	intrBuyer2PlatAc = intrBuyer2PlatAc.add(wrtrIntrForfBuyerAc);
            }
            // 投资人的收益部分 *比例
            intrBuyer2PlatAc = intrBuyer2PlatAc.multiply(deductRate).setScale(saveScale, RoundingMode.DOWN);

            // 实际支付给投资人的钱
            BigDecimal buyerRecvAc = intrBuyerAc.add(intrForfBuyerAc).add(prinBuyerAc).add(prinForfBuyerAc);
            buyerRecvAc = buyerRecvAc.add(wrtrPrinForfBuyerAc).add(wrtrIntrForfBuyerAc);

            // 累计金额
            buyerRecvTotalAc = buyerRecvTotalAc.add(buyerRecvAc);

            buyer2PlatTotalAc = buyer2PlatTotalAc.add(intrBuyer2PlatAc);

            InvestorPayeeListBuildReq investorReq = new InvestorPayeeListBuildReq();
            investorReq.setIntrBuyerAc(intrBuyerAc);
            investorReq.setIntrForfBuyerAc(intrForfBuyerAc);
            investorReq.setIntrBuyer2PlatAc(intrBuyer2PlatAc);
            investorReq.setMsg("清分");
            investorReq.setPkgId(packageId);
            investorReq.setPrinBuyerAc(prinBuyerAc);
            investorReq.setPrinForfBuyerAc(prinForfBuyerAc);
            investorReq.setUserId(p.getUserId());
            investorReq.setSequenceId(String.valueOf(schedule.getSequenceId()));
            investorReq.setIntrRate(deductRate);
            
            // 处理投资人收款， 投资人付收益提成比例的付款相关金额
            ProdPkgUtil.buildPayeeListOfInvestor(investorReq, payeeList, profPayerList);

            BigDecimal perUnit = buyerRecvAc.divide(BigDecimal.valueOf(p.getUnit().longValue()), calcuScale,
                    RoundingMode.DOWN);

            // 更新投资人融资包份额头寸头寸份额
            String positionId = p.getPositionId();

            // 过滤掉已转让的份额
            List<PositionLotPo> lots = positionService.getValidPostionLotsByPostionId(positionId);
            int size = lots.size();
            BigDecimal assignAmt = BigDecimal.ZERO.setScale(calcuScale);
            for (int i = 0; i < size; i++) {
                PositionLotPo lot = lots.get(i);
                BigDecimal accumCrAmt = AmtUtils.processNegativeAmt(lot.getAccumCrAmt(), BigDecimal.ZERO);
                BigDecimal actualAmt = perUnit.multiply(BigDecimal.valueOf(lot.getUnit().longValue())).setScale(
                        saveScale, RoundingMode.DOWN);
                if ((size - 1) != i) {
                    lot.setAccumCrAmt(accumCrAmt.add(actualAmt));
                } else {
                    lot.setAccumCrAmt(accumCrAmt.add(buyerRecvAc).subtract(assignAmt));
                }
                if (canFinish && needToUpdatePositionToZero) {
                    lot.setUnit(BigDecimal.ZERO.intValue());
                }
                lot.setLastMntOpid(operatorId);
                lot.setLastMntTs(workDate);
                assignAmt = assignAmt.add(actualAmt);
            }
            // 更新头寸份额表
            positionLotService.savePositionLots(lots);

            // 如果是最后一期，则更新头寸份额头寸表
            if (canFinish && needToUpdatePositionToZero) {
                p.setUnit(BigDecimal.ZERO.intValue());
                p.setLastMntOpid(operatorId);
                p.setLastMntTs(workDate);
                positionService.savePositions(Arrays.asList(p));
            }
        }

        // TODO 融资人付担保公司本金代偿逾期罚金
        if (wrtrPrinForfPay.compareTo(BigDecimal.ZERO) > 0) {
            TransferInfo payerPrin = new TransferInfo();
            payerPrin.setUserId(owner);
            payerPrin.setRelZQ(false);
            payerPrin.setTrxAmt(wrtrPrinForfPay);
            payerPrin.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "_" + schedule.getSequenceId() + "清分，支付本金担保代偿逾期罚金"
                    + wrtrPrinForfPay + "元");
            payerPrin.setUseType(EFundUseType.PRIN_CMPNS_OVERDUE_FINE);
            payerList.add(payerPrin);
        }

        // TODO 融资人付担保公司利息代偿逾期罚金
        if (wrtrIntrForfPay.compareTo(BigDecimal.ZERO) > 0) {
            TransferInfo payerIntr = new TransferInfo();
            payerIntr.setUserId(owner);
            payerIntr.setRelZQ(false);
            payerIntr.setTrxAmt(wrtrIntrForfPay);
            payerIntr.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "_" + schedule.getSequenceId() + "清分，支付利息担保代偿逾期罚金"
                    + wrtrIntrForfPay + "元");
            payerIntr.setUseType(EFundUseType.INTR_CMPNS_OVERDUE_FINE);
            payerList.add(payerIntr);
        }

        platRecvTotalAc = buyer2PlatTotalAc.add(feePlat).add(feeForfPlat);
        buyerRecvTotalAc = buyerRecvTotalAc.subtract(buyer2PlatTotalAc);

        // 误差金额 = 应付总额 - 实际付给投资人的还款- 实际付给平台的还款
        BigDecimal deviation = AmtUtils.processNegativeAmt(
                fncrPayTotalAmt.subtract(buyerRecvTotalAc).subtract(platRecvTotalAc), BigDecimal.ZERO);

        // TODO
        fncrPayTotalAmt = fncrPayTotalAmt.subtract(wrtrPrinForfBuyer).subtract(wrtrIntrForfBuyer);

        platRecvTotalAc = platRecvTotalAc.add(deviation);

        ExchagePayeeListBuildReq exchageReq = new ExchagePayeeListBuildReq();
        exchageReq.setDeviation(deviation);
        exchageReq.setExchangeUserId(exchangeUserId);
        exchageReq.setFeeForfPlat(feeForfPlat);
        exchageReq.setBuyer2PlatTotalAc(buyer2PlatTotalAc);
        exchageReq.setFeePlat(feePlat);
        exchageReq.setMsg("清分");
        exchageReq.setPkgId(packageId);
        exchageReq.setSequenceId(String.valueOf(schedule.getSequenceId()));
        exchageReq.setIntrRate(deductRate);

        ProdPkgUtil.buildPayeeListOfExchange(exchageReq, payeeList, profPayeeList);

        // 融资人付款
        TransferInfo dInfo = new TransferInfo();
        dInfo.setUserId(owner);
        dInfo.setRelZQ(true);
        dInfo.setTrxAmt(fncrPayTotalAmt);
        dInfo.setTrxMemo(PkgUtils.getPkgIdNameStr(pkg.getId()) + "_" + schedule.getSequenceId() + "清分，支付" + fncrPayTotalAmt + "元");
        dInfo.setUseType(EFundUseType.FNCR_REPAYMENT);
        payerList.add(dInfo);

        CleaningPkgMsgHolder msgHolder = new CleaningPkgMsgHolder();
        msgHolder.setActualpayBuyerAmt(buyerRecvTotalAc);
        msgHolder.setActualpayPlatAmt(platRecvTotalAc);
        msgHolder.setCanFinish(canFinish);
        msgHolder.setPayeeList(payeeList);
        msgHolder.setPayerList(payerList);
        msgHolder.setProfPayeeList(profPayeeList);
        msgHolder.setProfPayerList(profPayerList);
        msgHolder.setTotalTrxAmt(fncrPayTotalAmt.add(wrtrPrinForfPay).add(wrtrIntrForfPay));

        msgHolder.setPrinPay(prinPay);
        msgHolder.setPrinFinePay(prinFinePay);
        msgHolder.setIntrPay(intrPay);
        msgHolder.setIntrFinePay(intrFinePay);
        msgHolder.setFeePay(feePay);
        msgHolder.setFeeFinePay(feeFinePay);
        msgHolder.setWrtrIntrForfPay(wrtrIntrForfPay);
        msgHolder.setWrtrPrinForfPay(wrtrPrinForfPay);

        return msgHolder;

    }

    // /**
    // * 判断是否在违约期内
    // *
    // * @param graceDay
    // * @param paymentDate
    // * @return
    // */
    // private boolean isInBreachGraceDays(Integer graceDay, Date paymentDate, Integer offsite) {
    // Calendar nowDt = Calendar.getInstance();
    // Calendar payDt = Calendar.getInstance();
    // try {
    // payDt.setTime(DateUtils.getStartDate(paymentDate));
    // } catch (ParseException e) {
    // e.printStackTrace();
    // }
    // payDt.add(Calendar.DAY_OF_YEAR, offsite);
    // Calendar endDt = Calendar.getInstance();
    // try {
    // endDt.setTime(DateUtils.getEndDate(paymentDate));
    // } catch (ParseException e) {
    // e.printStackTrace();
    // }
    // endDt.add(Calendar.DAY_OF_YEAR, graceDay);
    // return nowDt.compareTo(payDt) > 0 && nowDt.compareTo(endDt) < 0;
    // }

    /**
     * 是否需要将头寸份数更新为0， 
     * 还款计划中，所有欠款已完成或者只剩下最后一期
     * @param paymentSchedule
     * @return
     */
    protected boolean needToUpdatePositionToZero(PaymentSchedule paymentSchedule) {
        boolean bool = false;
        if (paymentSchedule == null) {
            return bool;
        }
        List<PaymentSchedule> schedules = paymentScheduleRepository.getByPackageIdAndStatusNotIn(
                paymentSchedule.getPackageId(), Arrays.asList(EPayStatus.CLEARED, EPayStatus.FINISH));
        if (!schedules.isEmpty()) {
            if (schedules.size() == 1) {
                PaymentSchedule ps = schedules.get(0);
                if (ps.getSequenceId() == paymentSchedule.getSequenceId()) {
                    bool = true;
                }
            }
        } else {
            bool = true;
        }
        return bool;
    }

}
