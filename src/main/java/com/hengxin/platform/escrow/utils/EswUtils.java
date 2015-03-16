package com.hengxin.platform.escrow.utils;

import java.util.Random;

import com.hengxin.platform.common.util.CommonBusinessUtil;

public final class EswUtils {
	
	public static String getEswMerChNo(){
		return CommonBusinessUtil.getEswCustAcctNo();
	}
	
	public static String getEswServProv(){
		return CommonBusinessUtil.getEscrowProvider();
	}
	
	public static String getRandomStr(int length){
		Random random = new Random(); 
		Double num = random.nextDouble(); 
		String randomStr = num + "";
		return randomStr.substring(3,3+length); 
	}
}
