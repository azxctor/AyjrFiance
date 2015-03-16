package com.hengxin.platform.fund.service.support;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.dto.biz.req.atom.DedicatedTransferInfo;
import com.hengxin.platform.fund.dto.biz.req.atom.TransferInfo;
import com.hengxin.platform.fund.entity.AcctPo;
import com.hengxin.platform.fund.entity.SubAcctPo;
import com.hengxin.platform.fund.enums.EAcctStatus;
import com.hengxin.platform.fund.enums.ECashPool;
import com.hengxin.platform.fund.enums.ESubAcctNo;
import com.hengxin.platform.fund.exception.AcctNotExistException;
import com.hengxin.platform.fund.exception.AcctStatusIllegalException;
import com.hengxin.platform.fund.util.AmtUtils;

public class AcctUtils {

	/**
	 * 计算子账户的可用余额
	 * 
	 * @return
	 */
	public static BigDecimal getSubAcctAvlBal(SubAcctPo subAcct)
			throws BizServiceException {

		BigDecimal avlBal = BigDecimal.ZERO;

		if (subAcct != null) {

			BigDecimal bal = AmtUtils.processNegativeAmt(subAcct.getBal(),
					BigDecimal.ZERO);

			BigDecimal frzAmt = AmtUtils.processNegativeAmt(
					subAcct.getFreezableAmt(), BigDecimal.ZERO);

			BigDecimal resvAmt = AmtUtils.processNegativeAmt(
					subAcct.getReservedAmt(), BigDecimal.ZERO);

			avlBal = AmtUtils.max(bal.subtract(frzAmt).subtract(resvAmt),
					BigDecimal.ZERO);

		}

		return avlBal;
	}

	/**
	 * 计算子账户的可用余额（忽略冻结金额）
	 * 
	 * @return
	 */
	public static BigDecimal getSubAcctAvlBalIgnoreFrzAmt(SubAcctPo subAcct)
			throws BizServiceException {

		BigDecimal avlBal = BigDecimal.ZERO;

		if (subAcct != null) {

			BigDecimal bal = AmtUtils.processNegativeAmt(subAcct.getBal(),
					BigDecimal.ZERO);

			BigDecimal resvAmt = AmtUtils.processNegativeAmt(
					subAcct.getReservedAmt(), BigDecimal.ZERO);

			avlBal = AmtUtils.max(bal.subtract(resvAmt), BigDecimal.ZERO);

		}

		return avlBal;
	}

	/**
	 * 检查综合账户状态是否为正常状态，若为非正常状态，则抛异常
	 * 
	 * @param acAcctPo
	 * @throws ServiceException
	 */
	public static void checkAcctNormalStatus(AcctPo acAcctPo)
			throws AcctNotExistException, AcctStatusIllegalException {
		if (acAcctPo == null) {
			throw new AcctNotExistException("会员的综合账户不存在");
		}
		String acctStatus = acAcctPo.getAcctStatus();
		if (!StringUtils.equals(acctStatus, EAcctStatus.NORMAL.getCode())) {
			throw new AcctStatusIllegalException("会员的综合账户不可用, 综合账户编号为"
					+ acAcctPo.getAcctNo());
		}
	}

	/**
	 * 检查综合账户的状态，若为只收不付状态，则抛异常提示
	 * 
	 * @param acAcctPo
	 * @throws AcctNotExistException
	 * @throws ServiceException
	 */
	public static void checkAcctNoPayStatus(AcctPo acctPo)
			throws AcctStatusIllegalException, AcctNotExistException {
		boolean bool = isCanNotPayStatus(acctPo);
		if (bool) {
			throw new BizServiceException(EErrorCode.FUND_ACCT_CAN_NOT_PAY,
					"会员的综合账户不可用, 综合账户编号为" + acctPo.getAcctNo());
		}
	}

	/**
	 * 判断综合账户的状态是否为只收不付状态，若是 返回true，否则 返回false
	 * 
	 * @param acAcctPo
	 * @return
	 * @throws ServiceException
	 */
	public static boolean isCanNotPayStatus(AcctPo acAcctPo)
			throws AcctNotExistException {
		boolean bool = false;
		if (acAcctPo == null) {
			throw new AcctNotExistException("会员的综合账户不存在");
		}
		String acctStatus = acAcctPo.getAcctStatus();
		if (!StringUtils.equals(acctStatus, EAcctStatus.NORMAL.getCode())) {
			bool = true;
		}
		return bool;
	}

	/**
	 * 判断是否小微宝账户
	 * 
	 * @param subAcctNo
	 * @return
	 */
	public static boolean isXWBSubAcctNo(String subAcctNo) {
		return StringUtils.equals(ESubAcctNo.XWB.getCode(), subAcctNo);
	}

	/**
	 * 判断是否活期账户
	 * 
	 * @param subAcctNo
	 * @return
	 */
	public static boolean isCurrentSubAcctNo(String subAcctNo) {
		return StringUtils.equals(ESubAcctNo.CURRENT.getCode(), subAcctNo);
	}

	public static String substr(String str, int maxLen) {
		StringBuffer strBuffer = new StringBuffer(str);
		int strLen = strBuffer.length();
		if (strLen > maxLen) {
			int len = strLen - maxLen;
			int start = len > 0 ? len : 0;
			str = strBuffer.substring(start);
		}
		return str;
	}
	
	public static String getValidCashPool(ECashPool cashPool){
		
		String pool = null;
		
		if(cashPool!=null && cashPool!=ECashPool.ALL){
			pool = cashPool.getCode();
		}
		
		return pool;
	}
    
    public static List<TransferInfo> convertToTransferInfoList(List<DedicatedTransferInfo> DedicatedTransferInfoList){
    	List<TransferInfo> list = new ArrayList<TransferInfo>();
    	for(DedicatedTransferInfo dt : DedicatedTransferInfoList){
    		TransferInfo tsfr = ConverterService.convert(dt, TransferInfo.class);
    		list.add(tsfr);
    	}
    	return list;
    }

	public static void main(String[] args) {

		String str = "中华人民共和国哈哈哈哈哈哈哈哈A";

		System.out.println(substr(str, 16));

	}

}
