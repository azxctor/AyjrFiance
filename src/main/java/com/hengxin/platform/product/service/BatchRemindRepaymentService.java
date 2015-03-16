package com.hengxin.platform.product.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.fund.util.AmtUtils;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.member.enums.EMessageType;
import com.hengxin.platform.member.service.MemberMessageService;
import com.hengxin.platform.product.domain.PaymentSchedule;
import com.hengxin.platform.product.domain.Product;
import com.hengxin.platform.product.domain.ProductPackage;
import com.hengxin.platform.product.enums.EPayStatus;
import com.hengxin.platform.product.enums.EWarrantyType;
import com.hengxin.platform.product.repository.PaymentScheduleRepository;
import com.hengxin.platform.product.repository.ProductPackageRepository;
import com.hengxin.platform.product.repository.ProductRepository;
import com.hengxin.platform.security.enums.EBizRole;

/**
 * 还款批量提醒服务
 * 
 * @author dcliu
 * 
 */
@Service
public class BatchRemindRepaymentService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(BatchRemindRepaymentService.class);

	private static final String fncrMessageKey = "repayment.fncr.remind.message";
	private static final String wrtrMessageKey = "repayment.wrtr.remind.message";
	private static final String wrtrCmpnsMessageKey = "repayment.wrtr.cmpns.remind.message";
	private static final String bizRoleMessageKey = "repayment.bizRole.remind.message";
	private static final String bizSettleMessageKey = "repayment.bizSettle.remind.message";

	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private MemberMessageService memberMessageService;
	@Autowired
	private ProductPackageRepository productPackageRepository;
	@Autowired
	private PaymentScheduleRepository paymentScheduleRepository;

	@Transactional(readOnly = true)
	public List<PaymentSchedule> getTodayRepaymentRemindList(Date workDate) {
		List<PaymentSchedule> list = new ArrayList<PaymentSchedule>();
		Date lessThanDate = DateUtils.add(workDate, Calendar.DATE, 1);
		List<EPayStatus> inStatus = Arrays.asList(EPayStatus.OVERDUE,
				EPayStatus.COMPENSATING, EPayStatus.COMPENSATORY);
		list = paymentScheduleRepository
				.findByPaymentDateLessThanAndStatusInOrderByFinancerIdAscPaymentDateAscSequenceIdAsc(
						lessThanDate, inStatus);
		return list;
	}

	@Transactional(readOnly = true)
	public List<PaymentSchedule> getPreRemindRepaymentList(Date workDate,
			int preDays) {
		Date curDate = CommonBusinessUtil.getCurrentWorkDate();
		Date today = DateUtils.add(curDate, Calendar.DATE, preDays);
		String dateStr = DateUtils.formatDate(today, "yyyy-MM-dd");
		today = DateUtils.getDate(dateStr, "yyyy-MM-dd");
		List<PaymentSchedule> schedules = paymentScheduleRepository
				.findByPaymentDateAndStatusInOrderByFinancerIdAscPaymentDateAscSequenceIdAsc(
						today, Arrays.asList(EPayStatus.NORMAL));
		return schedules;
	}

	private void remindFncr(EMessageType msgType, String userId, String pkgId,
			String seqStr, Date payDate, BigDecimal totalAmt,
			BigDecimal prinAmt, BigDecimal intrAmt, BigDecimal feeAmt,
			BigDecimal fineAmt) {
		if(StringUtils.isBlank(userId)){
			LOGGER.error("提醒融资人，融资人编号为空,融资包编号为"+pkgId);
			return;
		}
		DecimalFormat fmt = new DecimalFormat();
		fmt.applyPattern("##,##0.00");
		// "尊敬的客户，您好！您的{0}融资包{1}期，应还款日期为{2}，应还款总金额：{3}元。其中应还本金：{4}元，应还利息：{5}元，应还费用：{6}元，应还罚金：{7}元。请确保您的账户余额足。";
		memberMessageService.sendMessage(msgType, fncrMessageKey,
				userId, pkgId, seqStr,
				DateUtils.formatDate(payDate, "yyyy-MM-dd"),
				fmt.format(totalAmt.doubleValue()),
				fmt.format(prinAmt.doubleValue()),
				fmt.format(intrAmt.doubleValue()),
				fmt.format(feeAmt.doubleValue()),
				fmt.format(fineAmt.doubleValue()));
	}

	private void remindWrtr(EMessageType msgType, String userId, String pkgId,
			String seqStr, Date payDate, BigDecimal totalAmt,
			BigDecimal prinAmt, BigDecimal intrAmt, BigDecimal feeAmt,
			BigDecimal fineAmt) {
		if(StringUtils.isBlank(userId)){
			LOGGER.error("提醒担保机构，担保机构编号为空,融资包编号为"+pkgId);
			return;
		}
		DecimalFormat fmt = new DecimalFormat();
		fmt.applyPattern("##,##0.00");
		// String msg2 =
		// "尊敬的客户，您好！您担保的{0}融资包{1}期，应还款日期为{2}，应还款总金额：{3}元。其中应还本金：{4}元，应还利息：{5}元，应还费用：{6}元，应还罚金：{7}元。请通知融资人及时还款。";
		memberMessageService.sendMessage(msgType, wrtrMessageKey,
				userId, pkgId, seqStr,
				DateUtils.formatDate(payDate, "yyyy-MM-dd"),
				fmt.format(totalAmt.doubleValue()),
				fmt.format(prinAmt.doubleValue()),
				fmt.format(intrAmt.doubleValue()),
				fmt.format(feeAmt.doubleValue()),
				fmt.format(fineAmt.doubleValue()));
	}

	private void remindWrtrOfCmpns(EMessageType msgType, String userId,
			String pkgId, String seqStr, Date payDate, BigDecimal totalAmt,
			BigDecimal prinAmt, BigDecimal intrAmt, BigDecimal feeAmt,
			BigDecimal fineAmt, EWarrantyType wrtrType, BigDecimal wrtrAmt) {
		if(StringUtils.isBlank(userId)){
			LOGGER.error("提醒担保机构代偿，担保机构编号为空,融资包编号为"+pkgId);
			return;
		}
		DecimalFormat fmt = new DecimalFormat();
		fmt.applyPattern("##,##0.00");
		// "尊敬的客户，您好！您担保的{0}融资包{1}期，应还款日期为{2}，应还款总金额：{3}元。其中应还本金：{4}元，应还利息：{5}元，应还费用：{6}元，应还罚金：{7}元。担保类型：{8}，应付担保金额：{9}元。请确保您的账户余额足，请通过融资人及时还款。";
		memberMessageService.sendMessage(msgType, wrtrCmpnsMessageKey,
				userId, pkgId, seqStr,
				DateUtils.formatDate(payDate, "yyyy-MM-dd"),
				fmt.format(totalAmt.doubleValue()),
				fmt.format(prinAmt.doubleValue()),
				fmt.format(intrAmt.doubleValue()),
				fmt.format(feeAmt.doubleValue()),
				fmt.format(fineAmt.doubleValue()), wrtrType.getText(),
				fmt.format(wrtrAmt.doubleValue()));
	}

	
	private void sendMsgToBizRole(EMessageType msgType, List<EBizRole> roles,
			String pkgId, String seqStr) {
		if(roles==null||roles.isEmpty()){
			return;
		}
		// {0}融资包 {1}期，日终还款不足额已违约，并开始计算罚金
		memberMessageService.sendMessage(msgType, bizRoleMessageKey,
				roles, pkgId, seqStr);
	}
	
	private void sendMsgToSettle(EMessageType msgType, List<EBizRole> roles,
            String pkgId, String seqStr) {
		if(roles==null||roles.isEmpty()){
			return;
		}
        // {0}融资包 {1}期，已代偿
        memberMessageService.sendMessage(msgType, bizSettleMessageKey,
                roles, pkgId, seqStr);
    }

	@Transactional
	public void remindSchduleRepayment(PaymentSchedule sh, Integer totalSeq,
			Product product) {
		String fncrId = sh.getFinancerId();
		String wrtrId = sh.getWarrantId();
		String pkgId = sh.getPackageId();
		Date payDate = sh.getPaymentDate();
		EPayStatus payStatus = sh.getStatus();
		String seqStr = String.valueOf(sh.getSequenceId()) + "/"
				+ String.valueOf(totalSeq.intValue());
		BigDecimal prinAmt = AmtUtils.processNegativeAmt(sh.getPrincipalAmt()
				.subtract(sh.getPayPrincipalAmt()), BigDecimal.ZERO);
		BigDecimal intrAmt = AmtUtils.processNegativeAmt(sh.getInterestAmt()
				.subtract(sh.getPayInterestAmt()), BigDecimal.ZERO);
		BigDecimal feeAmt = AmtUtils.processNegativeAmt(sh.getFeeAmt()
				.subtract(sh.getPayAmt()), BigDecimal.ZERO);
		BigDecimal fineAmt = BigDecimal.ZERO;
		fineAmt = fineAmt.add(AmtUtils.processNegativeAmt(sh
				.getPrincipalForfeit().subtract(sh.getPayPrincipalForfeit()),
				BigDecimal.ZERO));
		fineAmt = fineAmt.add(AmtUtils.processNegativeAmt(sh
				.getInterestForfeit().subtract(sh.getPayInterestForfeit()),
				BigDecimal.ZERO));
		fineAmt = fineAmt.add(AmtUtils.processNegativeAmt(sh.getFeeForfeit()
				.subtract(sh.getPayFeeForfeit()), BigDecimal.ZERO));
		fineAmt = fineAmt.add(AmtUtils.processNegativeAmt(sh
				.getWrtrPrinForfeit().subtract(sh.getPayWrtrPrinForfeit()),
				BigDecimal.ZERO));
		fineAmt = fineAmt.add(AmtUtils.processNegativeAmt(
				sh.getWrtrInterestForfeit().subtract(
						sh.getPayWrtrInterestForfeit()), BigDecimal.ZERO));
		BigDecimal totalAmt = BigDecimal.ZERO;
		totalAmt = totalAmt.add(prinAmt);
		totalAmt = totalAmt.add(intrAmt);
		totalAmt = totalAmt.add(feeAmt);
		totalAmt = totalAmt.add(fineAmt);
		EWarrantyType wrtrType = product.getWarrantyType();
		BigDecimal wrtrAmt = getWrtrAmt(sh, product);
		switch (payStatus) {
		case NORMAL:
			this.remindFncr(EMessageType.SMS, fncrId, pkgId, seqStr, payDate,
					totalAmt, prinAmt, intrAmt, feeAmt, fineAmt);
			this.remindFncr(EMessageType.MESSAGE, fncrId, pkgId, seqStr,
					payDate, totalAmt, prinAmt, intrAmt, feeAmt, fineAmt);
			this.remindWrtr(EMessageType.SMS, wrtrId, pkgId, seqStr,
                    payDate, totalAmt, prinAmt, intrAmt, feeAmt, fineAmt);
            this.remindWrtr(EMessageType.MESSAGE, wrtrId, pkgId, seqStr,
                    payDate, totalAmt, prinAmt, intrAmt, feeAmt, fineAmt);
			
			break;
		case OVERDUE:
			this.remindFncr(EMessageType.SMS, fncrId, pkgId, seqStr, payDate,
					totalAmt, prinAmt, intrAmt, feeAmt, fineAmt);
			this.remindFncr(EMessageType.MESSAGE, fncrId, pkgId, seqStr,
					payDate, totalAmt, prinAmt, intrAmt, feeAmt, fineAmt);
			boolean today2Cmpns = todayOverdue2Cmpns(payDate, product);
			if (today2Cmpns) {
				this.remindWrtrOfCmpns(EMessageType.SMS, wrtrId, pkgId, seqStr,
						payDate, totalAmt, prinAmt, intrAmt, feeAmt, fineAmt,
						wrtrType, wrtrAmt);
				this.remindWrtrOfCmpns(EMessageType.MESSAGE, wrtrId, pkgId,
						seqStr, payDate, totalAmt, prinAmt, intrAmt, feeAmt,
						fineAmt, wrtrType, wrtrAmt);
			} else {
				this.remindWrtr(EMessageType.SMS, wrtrId, pkgId, seqStr,
						payDate, totalAmt, prinAmt, intrAmt, feeAmt, fineAmt);
				this.remindWrtr(EMessageType.MESSAGE, wrtrId, pkgId, seqStr,
						payDate, totalAmt, prinAmt, intrAmt, feeAmt, fineAmt);
			}
			this.sendMsgToBizRole(EMessageType.MESSAGE, Arrays.asList(
                    EBizRole.PLATFORM_RISK_CONTROL_MANAGER,
                    EBizRole.PLATFORM_RISK_CONTROL_SUPERVISOR, 
                    EBizRole.PLATFORM_RISK_CONTROL_MAKER, 
                    EBizRole.PLATFORM_RISK_CONTROL_LOAD_APPROVE,
                    EBizRole.PLATFORM_RISK_CONTROL_POST_LENDING,
                    EBizRole.PLATFORM_RISK_CONTROL_PRODUCT_APPROVE), pkgId, seqStr);
			break;
		case COMPENSATING:
//			this.remindFncr(EMessageType.SMS, fncrId, pkgId, seqStr, payDate,
//					totalAmt, prinAmt, intrAmt, feeAmt, fineAmt);
			this.remindFncr(EMessageType.MESSAGE, fncrId, pkgId, seqStr,
					payDate, totalAmt, prinAmt, intrAmt, feeAmt, fineAmt);
			this.remindWrtrOfCmpns(EMessageType.SMS, wrtrId, pkgId, seqStr,
					payDate, totalAmt, prinAmt, intrAmt, feeAmt, fineAmt,
					wrtrType, wrtrAmt);
			this.remindWrtrOfCmpns(EMessageType.MESSAGE, wrtrId, pkgId, seqStr,
					payDate, totalAmt, prinAmt, intrAmt, feeAmt, fineAmt,
					wrtrType, wrtrAmt);			
			break;
		case COMPENSATORY:
			this.remindFncr(EMessageType.SMS, fncrId, pkgId, seqStr, payDate,
					totalAmt, prinAmt, intrAmt, feeAmt, fineAmt);
			this.remindFncr(EMessageType.MESSAGE, fncrId, pkgId, seqStr,
					payDate, totalAmt, prinAmt, intrAmt, feeAmt, fineAmt);
			this.remindWrtr(EMessageType.SMS, wrtrId, pkgId, seqStr, payDate,
					totalAmt, prinAmt, intrAmt, feeAmt, fineAmt);
			this.remindWrtr(EMessageType.MESSAGE, wrtrId, pkgId, seqStr,
					payDate, totalAmt, prinAmt, intrAmt, feeAmt, fineAmt);
			this.sendMsgToSettle(EMessageType.MESSAGE, Arrays.asList(
                    EBizRole.PLATFORM_SETTLEMENT_MANAGER,
                    EBizRole.PLATFORM_SETTLEMENT_DOUBLE_CHECK_1
                    ), pkgId, seqStr);
			break;
		default:
			LOGGER.debug("~~~~~~~状态不合法~~~~~~");
		}

	}

	private boolean todayOverdue2Cmpns(Date payDate, Product product) {
		boolean bool = false;
		EWarrantyType wrtrType = product.getWarrantyType();
		if (EWarrantyType.PRINCIPAL != wrtrType
				&& EWarrantyType.PRINCIPALINTEREST != wrtrType) {
			return bool;
		}
		Long graceDay = product.getCmpnsGracePeriod();
		graceDay = AmtUtils.max(graceDay - 1, Long.valueOf(0));
		Date workDate = CommonBusinessUtil.getCurrentWorkDate();
		Date limitDate = DateUtils.add(payDate, Calendar.DATE,
				graceDay.intValue());
		String dateStr = DateUtils.formatDate(limitDate, "yyyy-MM-dd");
		limitDate = DateUtils.getDate(dateStr, "yyyy-MM-dd");
		if (workDate.compareTo(limitDate) >= 0) {
			bool = true;
		}
		return bool;
	}

	private BigDecimal getWrtrAmt(PaymentSchedule sh, Product product) {
		BigDecimal wrtrAmt = BigDecimal.ZERO;
		BigDecimal prinAmt = AmtUtils.processNegativeAmt(sh.getPrincipalAmt()
				.subtract(sh.getPayPrincipalAmt()), BigDecimal.ZERO);
		BigDecimal intrAmt = AmtUtils.processNegativeAmt(sh.getInterestAmt()
				.subtract(sh.getPayInterestAmt()), BigDecimal.ZERO);
		EWarrantyType wrtrType = product.getWarrantyType();
		if (EWarrantyType.PRINCIPAL == wrtrType) {
			wrtrAmt = wrtrAmt.add(prinAmt);
		} else if (EWarrantyType.PRINCIPALINTEREST == wrtrType) {
			wrtrAmt = wrtrAmt.add(prinAmt).add(intrAmt);
		}
		return wrtrAmt;
	}

	@Transactional(readOnly = true)
	public Product getProductByPkgId(String pkgId, Map<String, Product> cache) {
		Product product = null;
		if (cache.containsKey(pkgId)) {
			product = cache.get(pkgId);
		} else {
			ProductPackage pkg = productPackageRepository.findOne(pkgId);
			product = productRepository.findOne(pkg.getProductId());
			cache.put(pkgId, product);
		}
		return product;
	}

	@Transactional(readOnly = true)
	public Integer getTotalSeq(String pkgId, Map<String, Integer> cache) {
		Integer totalSeq = Integer.valueOf(0);
		if (cache.containsKey(pkgId)) {
			totalSeq = cache.get(pkgId);
		} else {
			totalSeq = paymentScheduleRepository.countByPackageId(pkgId);
			cache.put(pkgId, totalSeq);
		}
		return totalSeq;
	}

}
