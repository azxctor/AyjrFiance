/*
 * Project Name: kmfex-platform
 * File Name: LoanApprovalPkgService.java
 * Class Name: LoanApprovalPkgService
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
import com.hengxin.platform.fund.dto.biz.req.LoadnHonourAgtDepositPayReq;
import com.hengxin.platform.fund.dto.biz.req.RiskTransferFinancingAmtReq;
import com.hengxin.platform.fund.dto.biz.req.TradeFeePayReq;
import com.hengxin.platform.fund.dto.biz.req.UserPayFeeReq;
import com.hengxin.platform.fund.dto.biz.req.atom.UnReserveReq;
import com.hengxin.platform.fund.enums.EFeePeriodType;
import com.hengxin.platform.fund.enums.EFeeType;
import com.hengxin.platform.fund.service.AddMaxCashableAmtService;
import com.hengxin.platform.fund.service.FinancierReceiveFundSerive;
import com.hengxin.platform.fund.service.FinancierSigningService;
import com.hengxin.platform.fund.service.UserPayFeeService;
import com.hengxin.platform.fund.util.AmtUtils;
import com.hengxin.platform.product.domain.Product;
import com.hengxin.platform.product.domain.ProductFee;
import com.hengxin.platform.product.domain.ProductLib;
import com.hengxin.platform.product.domain.ProductPackage;
import com.hengxin.platform.product.enums.EFeePayMethodType;
import com.hengxin.platform.product.enums.EPackageStatus;
import com.hengxin.platform.product.enums.ETermType;
import com.hengxin.platform.product.repository.ProductFeeRepository;
import com.hengxin.platform.product.repository.ProductLibRepository;
import com.hengxin.platform.product.repository.ProductPackageRepository;
import com.hengxin.platform.product.repository.ProductRepository;
import com.hengxin.platform.product.service.MsgHolder.PkgUtils;

/**
 * 融资包放款审批服务 Class Name: LoanApprovalPkgService.
 * 
 * @author tingwang
 * 
 */
@Service
public class LoanApprovalPkgService {

    @Autowired
    ProductPackageRepository productPackageRepository;
    @Autowired
    ActionHistoryService actionHistoryService;
    @Autowired
    FinancierSigningService financierSigningService;
    @Autowired
    FinancierReceiveFundSerive financierReceiveFundSerive;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    AddMaxCashableAmtService addMaxCashableAmtService;
    @Autowired
    ProductContractTemplateService productContractTemplateService;
    @Autowired
    JobWorkService jobWorkService;
    @Autowired
    UserPayFeeService userPayFeeService;
    @Autowired
    ProductFeeRepository productFeeRepository;
    @Autowired
    ProductLibRepository productLibRepository;
    
    private static final int CALCU_SCALE = 20;
    private static final int SAVE_SCALE = 2;
    private static final long NUMBER_12 = 12L;
    private static final long NUMBER_30 = 30L;
    private static final String SERVFEERT = "融资服务费";
    private static final String RISKFEERT = "风险管理费";

    /**
     * 融资包放款审批.
     * 
     * @param packageId
     * @param operatorId
     * @param operateDate
     */
    @Transactional
    public void loanApprovalPassProductPackage(String packageId, String operatorId) {
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
        if (!StringUtils.equals(productPackage.getStatus().getCode(), EPackageStatus.WAIT_LOAD_APPROAL.getCode())) {
            throw new BizServiceException(EErrorCode.PROD_PKG_STATUS_IS_NOT_WAIT_LOAD_APPROAL);
        }

        // 更新融资包状态
        productPackage.setStatus(EPackageStatus.WAIT_LOAD_APPROAL_CONFIRM);
        productPackage.setLoanTime(new Date());
        productPackageRepository.save(productPackage);

        // 记录日志
        actionHistoryService.save(EntityType.PRODUCTPACKAGE, packageId, ActionType.PKGLOANAPPROVE,
                ActionResult.PRODUCT_PACKAGE_LOAN_APPROVAL);
    }
    /**
     * 融资包放款审批--批量 
     */
    @Transactional
    public boolean loanApprovalPassProductPackage(List<String> idsList, String operatorId) {
    	for (String packageId : idsList) {
			loanApprovalPassProductPackage(packageId, operatorId);
		}
 		return true;
    }

    /**
     * 融资包放款.
     * 
     * @param packageId
     * @param operatorId
     * @param operateDate
     */
    @Transactional
    public void loanApprovalProductPackage(String packageId, String operatorId) {
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
        if (!StringUtils.equals(productPackage.getStatus().getCode(),
                EPackageStatus.WAIT_LOAD_APPROAL_CONFIRM.getCode())) {
            throw new BizServiceException(EErrorCode.PROD_PKG_STATUS_IS_NOT_WAIT_LOAD_APPROAL_CONFIRM);
        }

        Product product = productRepository.findByProductId(productPackage.getProductId());

        // 融资人
        String owner = product.getApplUserId();

        // 发布保证金解冻
        UnReserveReq urReq = new UnReserveReq();
        urReq.setCurrOpId(operatorId);
        urReq.setReserveJnlNo(productPackage.getServFnrJnlNo());
        urReq.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "放款成功，融资服务合同保证金解冻");
        urReq.setUserId(owner);
        urReq.setWorkDate(workDate);
        financierSigningService.refundAllEntrustDeposit(urReq);

        // 风控部对融资人放款
        RiskTransferFinancingAmtReq req = new RiskTransferFinancingAmtReq();
        req.setBizId(bizId);
        req.setCurrOpId(operatorId);
        req.setEventId(eventId);
        req.setFrzJnlNo(productPackage.getFnrJnlNo());
        req.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "放款");
        req.setUserId(owner);
        req.setWorkDate(workDate);
        BigDecimal riskTransferAmt = financierReceiveFundSerive.riskTransferAmt(req);

        // 扣除席位费、融资服务费、风险管理费
        payFees(owner, product, productPackage, operatorId, workDate, eventId, bizId);

        BigDecimal supplyAmount = AmtUtils.processNegativeAmt(productPackage.getSupplyAmount(), BigDecimal.ZERO);
        BigDecimal loanMarginRate = AmtUtils.processNegativeAmt(product.getLoanMrgnRt(), BigDecimal.ZERO);

        BigDecimal loanMarginAmt = supplyAmount.multiply(loanMarginRate);

        // 履约保证金冻结
        LoadnHonourAgtDepositPayReq lhadpReq = new LoadnHonourAgtDepositPayReq();
        lhadpReq.setBizId(bizId);
        lhadpReq.setCurrOpId(operatorId);
        lhadpReq.setTrxAmt(loanMarginAmt.setScale(SAVE_SCALE, RoundingMode.DOWN));
        lhadpReq.setTxMemo(PkgUtils.getPkgIdNameStr(packageId) + "放款成功，借款履约保证金" + loanMarginAmt.setScale(SAVE_SCALE, RoundingMode.DOWN)
                + "元冻结");
        lhadpReq.setUserId(owner);
        lhadpReq.setWorkDate(workDate);
        String loanFnrJnlNo = financierReceiveFundSerive.payLoadnHonourAgtDeposit(lhadpReq);
        productPackage.setLoanFnrJnlNo(loanFnrJnlNo);

        BigDecimal refreshAmt = riskTransferAmt.subtract(loanMarginAmt.setScale(SAVE_SCALE, RoundingMode.DOWN));
        
        // 高流动性包资金处理
        if (isRealTimeCashable(product)) {
            addMaxCashableAmtService.addMaxCashableAmt(owner, refreshAmt, operatorId);
        }

        // 更新融资包状态
        productPackage.setStatus(EPackageStatus.PAYING);
        productPackage.setLoanTs(new Date());
        productPackageRepository.save(productPackage);

        // 记录日志
        actionHistoryService.save(EntityType.PRODUCTPACKAGE, packageId, ActionType.PKGLOANAPPROVECONF,
                ActionResult.PRODUCT_PACKAGE_LOAN_APPROVAL_CONFIRM);

    }
    /**
     * 融资包放款--批量 
     */
    @Transactional
    public boolean loanApprovalProductPackageBatck(List<String> idsList, String operatorId) {
		for (String packageId : idsList) {
			loanApprovalProductPackage(packageId, operatorId);
		}
		return true;
    }
    
    /**
     * 高流动性包 或者 票据质押产品，实时可提现.
     * @param product
     * @return
     */
    private boolean isRealTimeCashable(Product product){
    	//return ProdPkgUtil.isHighFluidityProduct(product) || isBillPledgeProduct(product);
    	//安益金融禁用T+0实时提现
    	return false;
    }
    
    /**
     * 是否属于T+0提现产品.
     * @param product
     * @return
     */
    @SuppressWarnings("unused")
	private boolean isRealCashableProduct(Product product){
        boolean isBillPledge = false;
        Integer libId = product.getLibId();
        ProductLib prodLib = null;
        if(libId!=null){
        	prodLib = productLibRepository.findOne(libId);
        }
        if(prodLib!=null){
        	isBillPledge = StringUtils.equalsIgnoreCase("Y", prodLib.getRealCashableFlg());
        }
        return isBillPledge;
    }

    // 扣除席位费、融资服务费、风险管理费
    private void payFees(String owner, Product product, ProductPackage pkg, String operatorId, Date workDate,
            String eventId, String bizId) {

        BigDecimal supplyAmount = AmtUtils.processNegativeAmt(pkg.getSupplyAmount(), BigDecimal.ZERO);
        String packageId = pkg.getId();

        // 席位费校验和缴纳
        boolean hasFeeOfSeat = userPayFeeService.hasPayFeeOfSeat(owner, workDate);
        if (!hasFeeOfSeat) {
            UserPayFeeReq upReq = new UserPayFeeReq();
            upReq.setCurrOpId(operatorId);
            upReq.setEventId(eventId);
            upReq.setFeeType(EFeeType.SEAT);
            upReq.setPeriodNum(Integer.valueOf(1));
            upReq.setPeriodType(EFeePeriodType.YEAR);
            upReq.setTrxMemo("融资会员：" + owner + "缴纳坐席服务费");
            upReq.setUserId(owner);
            upReq.setWorkDate(workDate);
            upReq.setBizId(pkg.getId());
            
            BigDecimal seatFeeThreshold = AmtUtils.processNegativeAmt(CommonBusinessUtil.getSeatFeeThreshold(),
                    BigDecimal.ZERO);
            BigDecimal seatFee = BigDecimal.ZERO;
            if (supplyAmount.compareTo(seatFeeThreshold) <= 0) {
                seatFee = AmtUtils.processNegativeAmt(CommonBusinessUtil.getSeatFeeLowAmt(), BigDecimal.ZERO);
            } else {
                seatFee = AmtUtils.processNegativeAmt(CommonBusinessUtil.getSeatFeeHighAmt(), BigDecimal.ZERO);
            }

            upReq.setTrxAmt(seatFee.setScale(SAVE_SCALE, RoundingMode.DOWN));
            // 缴纳席位费
            userPayFeeService.payFee(upReq);
        }

        // 融资项目融资服务费实体
        ProductFee serveFeeEntity = productFeeRepository.getFeeByProductIdAndFeeName(product.getProductId(), SERVFEERT);

        // 融资服务费率
        BigDecimal serveFeeRate = AmtUtils.processNegativeAmt(serveFeeEntity.getFeeRt(), BigDecimal.ZERO);

        // 融资项目风险管理费实体
        ProductFee riskFeeEntity = productFeeRepository.getFeeByProductIdAndFeeName(product.getProductId(), RISKFEERT);

        // 风险管理费率
        BigDecimal riskFeeRate = AmtUtils.processNegativeAmt(riskFeeEntity.getFeeRt(), BigDecimal.ZERO);

        List<TradeFeePayReq> feeReqList = new ArrayList<TradeFeePayReq>();

        if (BigDecimal.ZERO.compareTo(serveFeeRate) < 0) {
            if (EFeePayMethodType.ONCE == serveFeeEntity.getPayMethod()) {
                // 需还融资服务费 = 融资服务费率 * 融资包实际申购金额 * 月数
                BigDecimal size = BigDecimal.valueOf(product.getTermLength().longValue());
                BigDecimal totalServeFee = serveFeeRate.multiply(supplyAmount).multiply(size);
                if (product.getTermType() == ETermType.YEAR) {
                    totalServeFee = totalServeFee.multiply(BigDecimal.valueOf(NUMBER_12));
                }
                if (product.getTermType() == ETermType.DAY) {
                    totalServeFee = totalServeFee.divide(BigDecimal.valueOf(NUMBER_30), CALCU_SCALE, RoundingMode.DOWN);
                }
                totalServeFee = totalServeFee.setScale(SAVE_SCALE, RoundingMode.DOWN);
                if (totalServeFee.compareTo(BigDecimal.ZERO) > 0) {
                    TradeFeePayReq serviceFeeReq = new TradeFeePayReq();
                    serviceFeeReq.setFeeType(EFeeType.LOANSERVE);
                    serviceFeeReq.setTrxAmt(totalServeFee);
                    serviceFeeReq.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "放款，收取融资服务费" + totalServeFee + "元");
                    feeReqList.add(serviceFeeReq);
                }
            }
        }
        if (BigDecimal.ZERO.compareTo(riskFeeRate) < 0) {
            if (EFeePayMethodType.ONCE == riskFeeEntity.getPayMethod()) {
                BigDecimal size = BigDecimal.valueOf(product.getTermLength().longValue());
                // 需还风险管理费 = 风险管理费率 * 融资包金额* 月数
                BigDecimal totalRiskFee = riskFeeRate.multiply(supplyAmount).multiply(size);
                if (product.getTermType() == ETermType.YEAR) {
                    totalRiskFee = totalRiskFee.multiply(BigDecimal.valueOf(NUMBER_12));
                }
                if (product.getTermType() == ETermType.DAY) {
                    totalRiskFee = totalRiskFee.divide(BigDecimal.valueOf(NUMBER_30), CALCU_SCALE, RoundingMode.DOWN);
                }
                totalRiskFee = totalRiskFee.setScale(SAVE_SCALE, RoundingMode.DOWN);
                if (totalRiskFee.compareTo(BigDecimal.ZERO) > 0) {
                    TradeFeePayReq riskFeeReq = new TradeFeePayReq();
                    riskFeeReq.setFeeType(EFeeType.RISKMANAGE);
                    riskFeeReq.setTrxAmt(totalRiskFee);
                    riskFeeReq.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "放款，收取风险管理费" + totalRiskFee + "元");
                    feeReqList.add(riskFeeReq);
                }
            }
        }

        // 融资会员交易签约，向交易所支付费用
        if (!feeReqList.isEmpty()) {
            financierSigningService.payTradeFeeToExchange(eventId, owner, feeReqList, bizId, packageId, operatorId, workDate);
        }

    }

}
