package com.hengxin.platform.product.service.MsgHolder;

import org.apache.commons.lang.StringUtils;

import com.hengxin.platform.fund.enums.EFlagType;
import com.hengxin.platform.product.domain.PaymentSchedule;

public class PkgUtils {
	
	/**
	 * 融资包名称显示
	 * @param pkgId
	 * @return
	 */
	public static String getPkgIdNameStr(String pkgId){
		StringBuffer buffer = new StringBuffer();
		buffer.append("融资项目");
		buffer.append(pkgId);
		return buffer.toString();
	}
	
	/**
	 * 资金流水中 bizId属性处理，格式化 融资包编号 与 还款期数
	 * @param pkgId
	 * @param seqId
	 * @return
	 */
	public static String formatBizId(String pkgId, Integer seqId){
		StringBuffer buffer = new StringBuffer();
		buffer.append(pkgId);
		buffer.append("_");
		buffer.append(seqId);
		return buffer.toString();
	}
	
	public static boolean isLastSchedule(PaymentSchedule schedule){
		return StringUtils.equals(schedule.getLastFlag(), EFlagType.YES.getCode());
	}
	
}
