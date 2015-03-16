package com.hengxin.platform.product.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.batch.service.JobWorkService;
import com.hengxin.platform.common.entity.id.IdUtil;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.dto.biz.req.atom.TransferInfo;
import com.hengxin.platform.fund.dto.biz.req.atom.UnFreezeReq;
import com.hengxin.platform.fund.entity.AcctPo;
import com.hengxin.platform.fund.enums.EFundUseType;
import com.hengxin.platform.fund.service.AcctService;
import com.hengxin.platform.fund.service.FundAcctFreezeMgtService;
import com.hengxin.platform.fund.service.WarrantCmpnsAmtService;
import com.hengxin.platform.fund.util.AmtUtils;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.product.domain.PaymentSchedule;
import com.hengxin.platform.product.domain.ProductPackage;
import com.hengxin.platform.product.enums.EPackageStatus;
import com.hengxin.platform.product.enums.EPayStatus;
import com.hengxin.platform.product.repository.PaymentScheduleRepository;
import com.hengxin.platform.product.repository.ProductPackageRepository;
import com.hengxin.platform.product.service.MsgHolder.PkgUtils;

/**
 * 融资包代偿金额管理
 * 
 * @author jishen
 * 
 */
@Service
public class PkgWarrantCmpnsMgtService {

    @Autowired
    private WarrantCmpnsAmtService warrantCmpnsAmtService;
    @Autowired
    private FundAcctFreezeMgtService fundAcctFreezeMgtService;
    @Autowired
    private PaymentScheduleRepository paymentScheduleRepository;
    @Autowired
    private ProductPackageRepository productPackageRepository;
    @Autowired
    private JobWorkService jobWorkService;
    @Autowired
    private AcctService acctService;

    /**
     * 风控部门解冻划转融资人资金偿还担保机构的代偿款及代偿利息
     * 
     * @param userId
     * @param pkgId
     * @param sequenceId
     * @param currOpId
     * @param workDate
     * @param isTransfer
     *            是否需要划转代偿欠款
     */
    @Transactional
    public void pkgWarrantCmpnsAmtRepay(String userId, String pkgId, int seqId, String currOpId, Date workDate,
            boolean isTransfer) {
        if (jobWorkService.isBatchBizTaskProcessing()) {
            throw new BizServiceException(EErrorCode.BATCH_EXEC_PROCESSING_WAITING);
        }
        // 还款计划
        PaymentSchedule paySchedule = paymentScheduleRepository.getByPackageIdAndSequenceId(pkgId, seqId);

        UnFreezeReq req = new UnFreezeReq();
        req.setUserId(userId);
        req.setFreezeJnlNo(paySchedule.getCmpnsFrzId());
        req.setTrxMemo("偿还代偿金额，资金账户解冻");
        req.setCurrOpId(currOpId);
        req.setWorkDate(workDate);
        // 解冻代偿欠款
        fundAcctFreezeMgtService.unFreezeAcct(req, true);

        // 获取代偿的对应融资包
        ProductPackage pkg = productPackageRepository.getProductPackageById(pkgId);

        if (isTransfer) {

            // 可用余额
            BigDecimal availbal = warrantCmpnsAmtService.getUserCurrentAcctAvlBal(userId, true);

            // 应付代偿金额罚金
            BigDecimal cmpnsFinePyAmt = AmtUtils.processNegativeAmt(paySchedule.getCmpnsFinePyAmt(), BigDecimal.ZERO);
            // 应付代偿金额
            BigDecimal cmpnsPyAmt = AmtUtils.processNegativeAmt(paySchedule.getCmpnsPyAmt(), BigDecimal.ZERO);
            // 实付代偿金额罚金
            BigDecimal cmpnsFinePdAmt = AmtUtils.processNegativeAmt(paySchedule.getCmpnsFinePdAmt(), BigDecimal.ZERO);
            // 实付代偿金额
            BigDecimal cmpnsPdAmt = AmtUtils.processNegativeAmt(paySchedule.getCmpnsPdAmt(), BigDecimal.ZERO);

            BigDecimal netCmpnsAmt = AmtUtils.processNegativeAmt(cmpnsPyAmt.subtract(cmpnsPdAmt), BigDecimal.ZERO);

            BigDecimal netCmpnsFineAmt = AmtUtils.processNegativeAmt(cmpnsFinePyAmt.subtract(cmpnsFinePdAmt),
                    BigDecimal.ZERO);

            // 获取划转金额
            BigDecimal rePayAmt = netCmpnsAmt.add(netCmpnsFineAmt);

            if (availbal.compareTo(rePayAmt) < 0) {
            	AcctPo acct = acctService.getAcctByUserId(userId);
                EErrorCode errorCode = EErrorCode.ACCT_NOT_ENOUGH_AVL_BAL;
                errorCode.setArgs(new Object[]{ acct.getAcctNo() });
                throw new BizServiceException(errorCode);
            }

            if (rePayAmt.compareTo(BigDecimal.ZERO) > 0) {

                String warrantId = paySchedule.getWarrantId();
                String bizId = pkgId + "_" + String.valueOf(seqId);
                String eventId = IdUtil.produce();

                if(netCmpnsAmt.compareTo(BigDecimal.ZERO)>0){
                	
	                StringBuffer memo = new StringBuffer();
	                memo.append("融资人编号为").append(userId).append(",");
	                memo.append(PkgUtils.getPkgIdNameStr(pkgId));
	                memo.append("，还款期数为第").append(seqId).append("期");
	                memo.append("，还款日期为 ").append(DateUtils.formatDate(paySchedule.getPaymentDate(), "yyyy-MM-dd"));
	                memo.append(" 的还款计划，偿还担保人(").append(warrantId);
	                memo.append(")代偿金额为").append(String.valueOf(netCmpnsAmt)).append("元");
	
	                TransferInfo fncrPayer = new TransferInfo();
	                fncrPayer.setUserId(userId);
	                fncrPayer.setTrxAmt(netCmpnsAmt);
	                fncrPayer.setUseType(EFundUseType.CMPNSAMT_REPAYMENT);
	                fncrPayer.setRelZQ(true);
	                fncrPayer.setTrxMemo(memo.toString());
	
	                TransferInfo warrantPayee = new TransferInfo();
	                warrantPayee.setUserId(warrantId);
	                warrantPayee.setTrxAmt(netCmpnsAmt);
	                warrantPayee.setUseType(EFundUseType.CMPNSAMT_REPAYMENT);
	                warrantPayee.setRelZQ(true);
	                warrantPayee.setTrxMemo(memo.toString());
	
	                // 融资人偿还担保方支付的代偿款
	                warrantCmpnsAmtService.repaymentCmpnsAmt(eventId, fncrPayer, warrantPayee, bizId, 
	                		pkgId, String.valueOf(seqId), currOpId, workDate);
                }
                // ---------------------------------------------------------------------
                if(netCmpnsFineAmt.compareTo(BigDecimal.ZERO)>0){
                	
	                StringBuffer memo2 = new StringBuffer();
	                memo2.append("融资人编号为").append(userId).append(",");
	                memo2.append(PkgUtils.getPkgIdNameStr(pkgId));
	                memo2.append("，还款期数为第").append(seqId).append("期");
	                memo2.append("，还款日期为 ").append(DateUtils.formatDate(paySchedule.getPaymentDate(), "yyyy-MM-dd"));
	                memo2.append(" 的还款计划，偿还担保人(").append(warrantId);
	                memo2.append(")代偿金额罚金总金额为").append(String.valueOf(netCmpnsFineAmt)).append("元");
	
	                TransferInfo fncrPayer2 = new TransferInfo();
	                fncrPayer2.setUserId(userId);
	                fncrPayer2.setTrxAmt(netCmpnsFineAmt);
	                fncrPayer2.setUseType(EFundUseType.CMPNSAMT_FINE_REPAYMENT);
	                fncrPayer2.setRelZQ(true);
	                fncrPayer2.setTrxMemo(memo2.toString());
	
	                TransferInfo warrantPayee2 = new TransferInfo();
	                warrantPayee2.setUserId(warrantId);
	                warrantPayee2.setTrxAmt(netCmpnsFineAmt);
	                warrantPayee2.setUseType(EFundUseType.CMPNSAMT_FINE_REPAYMENT);
	                warrantPayee2.setRelZQ(true);
	                warrantPayee2.setTrxMemo(memo2.toString());
	
	                // 融资人偿还担保方支付的代偿款
	                warrantCmpnsAmtService.repaymentCmpnsAmt(eventId, fncrPayer2, warrantPayee2, bizId, 
	                		pkgId, String.valueOf(seqId), currOpId, workDate);
                }
             }
        }

        // 更新当期还款计划状态为结束
        paySchedule.setStatus(EPayStatus.FINISH);
        paySchedule.setCmpnsFinePdAmt(AmtUtils.processNegativeAmt(paySchedule.getCmpnsFinePyAmt(), BigDecimal.ZERO));
        paySchedule.setCmpnsPdAmt(AmtUtils.processNegativeAmt(paySchedule.getCmpnsPyAmt(), BigDecimal.ZERO));
        paySchedule.setLastMntOpid(currOpId);
        paySchedule.setLastMntTs(new Date());
        paymentScheduleRepository.save(paySchedule);

        List<PaymentSchedule> list = paymentScheduleRepository.getByPackageIdAndStatusIn(pkgId, Arrays.asList(
                EPayStatus.NORMAL, EPayStatus.OVERDUE, EPayStatus.COMPENSATING, EPayStatus.COMPENSATORY,
                EPayStatus.CLEARED));

        // 已全部还款完毕 更新融资包状态为结束
        if (list.size() == 0) {
            pkg.setStatus(EPackageStatus.END);
            pkg.setLastOperatorId(currOpId);
            pkg.setLastTime(new Date());
            productPackageRepository.save(pkg);
        }
    }
}
