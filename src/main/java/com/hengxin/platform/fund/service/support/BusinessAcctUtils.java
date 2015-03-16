package com.hengxin.platform.fund.service.support;

import static com.hengxin.platform.common.enums.EErrorCode.TECH_DATA_INVALID;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.fund.entity.FreezeReserveDtlPo;
import com.hengxin.platform.fund.enums.EFnrOperType;
import com.hengxin.platform.fund.enums.EFnrStatus;
import com.hengxin.platform.fund.enums.ESubAcctNo;
import com.hengxin.platform.fund.enums.ESubAcctType;
import com.hengxin.platform.fund.util.AmtUtils;
import com.hengxin.platform.fund.util.DateUtils;

public class BusinessAcctUtils {

	private final static Logger LOG = LoggerFactory
			.getLogger(BusinessAcctUtils.class);

	private final static String CONTACT_STR = "_";

	private final static Map<String, String> subAcctTypeNoMapping = new HashMap<String, String>();

	private final static Map<String, Boolean> internalTransferValidMapping = new HashMap<String, Boolean>();

	private static void loadSubAcctTypeMappingData() {
		subAcctTypeNoMapping.put(ESubAcctType.CURRENT.getCode(),
				ESubAcctNo.CURRENT.getCode());
		subAcctTypeNoMapping.put(ESubAcctType.LOAN.getCode(),
				ESubAcctNo.LOAN.getCode());
		subAcctTypeNoMapping.put(ESubAcctType.PLEDGE.getCode(),
				ESubAcctNo.PLEDGE.getCode());
		subAcctTypeNoMapping.put(ESubAcctType.XWB.getCode(),
				ESubAcctNo.XWB.getCode());
	}

	private static void loadInternalTransferValidMappingData() {
		internalTransferValidMapping.put(ESubAcctType.CURRENT.getCode()
				+ CONTACT_STR + ESubAcctType.CURRENT.getCode(),
				Boolean.valueOf(false));
		internalTransferValidMapping.put(ESubAcctType.CURRENT.getCode()
				+ CONTACT_STR + ESubAcctType.LOAN.getCode(),
				Boolean.valueOf(false));
		internalTransferValidMapping.put(ESubAcctType.CURRENT.getCode()
				+ CONTACT_STR + ESubAcctType.PLEDGE.getCode(),
				Boolean.valueOf(true));
		internalTransferValidMapping.put(ESubAcctType.CURRENT.getCode()
				+ CONTACT_STR + ESubAcctType.XWB.getCode(),
				Boolean.valueOf(true));

		internalTransferValidMapping.put(ESubAcctType.LOAN.getCode()
				+ CONTACT_STR + ESubAcctType.CURRENT.getCode(),
				Boolean.valueOf(true));
		internalTransferValidMapping.put(ESubAcctType.LOAN.getCode()
				+ CONTACT_STR + ESubAcctType.LOAN.getCode(),
				Boolean.valueOf(false));
		internalTransferValidMapping.put(ESubAcctType.LOAN.getCode()
				+ CONTACT_STR + ESubAcctType.PLEDGE.getCode(),
				Boolean.valueOf(true));
		internalTransferValidMapping.put(ESubAcctType.LOAN.getCode()
				+ CONTACT_STR + ESubAcctType.XWB.getCode(),
				Boolean.valueOf(false));

		internalTransferValidMapping.put(ESubAcctType.PLEDGE.getCode()
				+ CONTACT_STR + ESubAcctType.CURRENT.getCode(),
				Boolean.valueOf(true));
		internalTransferValidMapping.put(ESubAcctType.PLEDGE.getCode()
				+ CONTACT_STR + ESubAcctType.LOAN.getCode(),
				Boolean.valueOf(false));
		internalTransferValidMapping.put(ESubAcctType.PLEDGE.getCode()
				+ CONTACT_STR + ESubAcctType.PLEDGE.getCode(),
				Boolean.valueOf(false));
		internalTransferValidMapping.put(ESubAcctType.PLEDGE.getCode()
				+ CONTACT_STR + ESubAcctType.XWB.getCode(),
				Boolean.valueOf(false));

		internalTransferValidMapping.put(ESubAcctType.XWB.getCode()
				+ CONTACT_STR + ESubAcctType.CURRENT.getCode(),
				Boolean.valueOf(true));
		internalTransferValidMapping.put(ESubAcctType.XWB.getCode()
				+ CONTACT_STR + ESubAcctType.LOAN.getCode(),
				Boolean.valueOf(false));
		internalTransferValidMapping.put(ESubAcctType.XWB.getCode()
				+ CONTACT_STR + ESubAcctType.PLEDGE.getCode(),
				Boolean.valueOf(false));
		internalTransferValidMapping.put(ESubAcctType.XWB.getCode()
				+ CONTACT_STR + ESubAcctType.XWB.getCode(),
				Boolean.valueOf(false));
	}

	public static String getSubAcctNoBySubAcctType(String subAcctType) {
		if (subAcctTypeNoMapping.isEmpty()) {
			loadSubAcctTypeMappingData();
		}
		return subAcctTypeNoMapping.get(subAcctType);
	}

	public static void checkDataPreUnFreeze(FreezeReserveDtlPo freezeDtlPo)
			throws BizServiceException {

		checkDataPreUnFnr(freezeDtlPo);

		if (EFnrOperType.FREEZE!=freezeDtlPo.getOperType()
				&& EFnrOperType.FREEZE_BIZ_NOPAY!=
						freezeDtlPo.getOperType()
						&& EFnrOperType.FREEZE_MGT_NOPAY!=
								freezeDtlPo.getOperType()) {
			throw new BizServiceException(TECH_DATA_INVALID, "冻结明细操作类型不正确");
		}
	}

	public static void checkDataPreUnReserve(FreezeReserveDtlPo reserveDtlPo)
			throws BizServiceException {

		checkDataPreUnFnr(reserveDtlPo);

		if (EFnrOperType.RESERVE!=reserveDtlPo.getOperType()) {
			throw new BizServiceException(TECH_DATA_INVALID, "保留明细操作类型不正确");
		}
	}

	public static void checkDataPreUnFnr(FreezeReserveDtlPo fnrDtlPo)
			throws BizServiceException {

		if (EFnrOperType.RESERVE!=fnrDtlPo.getOperType()
				&& EFnrOperType.FREEZE!=fnrDtlPo.getOperType()
				&& EFnrOperType.FREEZE_BIZ_NOPAY!=fnrDtlPo.getOperType()
						&& EFnrOperType.FREEZE_MGT_NOPAY!=fnrDtlPo.getOperType()) {
			throw new BizServiceException(TECH_DATA_INVALID, "冻结保留明细操作类型不正确");
		}

		if (EFnrStatus.CLOSE==fnrDtlPo.getStatus()) {
			throw new BizServiceException(TECH_DATA_INVALID,
					"冻结保留明细状态为关闭，已被解冻或解保留");
		}
	}

	public static boolean isFreezeDtl(EFnrOperType operType) {
		return operType==EFnrOperType.FREEZE;
	}

	private static String getAcctTypeKey(String from, String to) {
		return from + CONTACT_STR + to;
	}

	public static void checkInternalAcctValidTransfer(String fromAcctType,
			String toAcctType) throws BizServiceException {

		StringBuffer msg = new StringBuffer("内部账户转账: ");
		msg.append(fromAcctType);
		msg.append(" to ");
		msg.append(toAcctType);

		LOG.debug(msg.toString());

		if (internalTransferValidMapping.isEmpty()) {
			loadInternalTransferValidMappingData();
		}

		String acctTypeKey = getAcctTypeKey(fromAcctType, toAcctType);

		if (!internalTransferValidMapping.containsKey(acctTypeKey)) {
			msg.append(" 为无效操作");
			LOG.debug(msg.toString());
			throw new BizServiceException(TECH_DATA_INVALID, msg.toString());
		}

		Boolean valid = internalTransferValidMapping.get(acctTypeKey);

		if (valid) {
			msg.append(" 为有效操作");
			LOG.debug(msg.toString());
		} else {
			msg.append(" 为无效操作");
			LOG.debug(msg.toString());
			throw new BizServiceException(TECH_DATA_INVALID, msg.toString());
		}
	}

	/**
	 * 计算小微宝收款时可计息金额 规定时间(默认15:00)之后进入小微宝的金额，当日不计息。
	 * 
	 * @param intrBalAmt
	 * @param trxAmt
	 * @param currDate
	 * @return
	 */
	public static BigDecimal calXWBNowIntrBalAmtForRecv(BigDecimal intrBalAmt,
			BigDecimal trxAmt, Date currDate) {
		BigDecimal newIntrBalAmt = BigDecimal.ZERO;
		Long endTime = CommonBusinessUtil.getXwbInterestEndTime();
		Long nowTime = Long.valueOf(DateUtils.formatDate(currDate, "HHmm"));
		if (nowTime < endTime) {
			newIntrBalAmt = intrBalAmt.add(trxAmt);
		} else {
			newIntrBalAmt = intrBalAmt;
		}
		return newIntrBalAmt;
	}

	/**
	 * 计算小微宝付款时可计息金额 优先划转非可计息金额
	 * 
	 * @param intrAmt
	 * @param balAmt
	 * @param trxAmt
	 * @return
	 */
	public static BigDecimal calXWBNowIntrBalAmtForPay(BigDecimal intrAmt,
			BigDecimal balAmt, BigDecimal trxAmt) {
		BigDecimal newIntrBalAmt = BigDecimal.ZERO;
		BigDecimal unIntrAmt = AmtUtils.max(balAmt.subtract(intrAmt),
				BigDecimal.ZERO);
		if (unIntrAmt.compareTo(trxAmt) >= 0) {
			newIntrBalAmt = newIntrBalAmt.add(intrAmt);
		} else {
			BigDecimal netAmt = AmtUtils.max(trxAmt.subtract(unIntrAmt),
					BigDecimal.ZERO);
			newIntrBalAmt = AmtUtils.max(intrAmt.subtract(netAmt),
					BigDecimal.ZERO);
		}
		return newIntrBalAmt;
	}

}
