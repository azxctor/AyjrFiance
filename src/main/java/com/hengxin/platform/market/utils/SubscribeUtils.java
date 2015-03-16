package com.hengxin.platform.market.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.common.util.NumberUtil;

public final class SubscribeUtils {
	
	/**
	 * 融资包单元面值
	 * @return
	 */
	public static BigDecimal getUnitFaceValue(){
		return CommonBusinessUtil.getUnitFaceValue();
	}
	
	/**
	 * 单个融资包最大申购人数
	 * @return
	 */
	public static BigDecimal getPerPkgMaxSubsUserCount(){
		return BigDecimal.valueOf(200);
	}
	
	public static BigDecimal roundUnitFaceValue(BigDecimal value){
		BigDecimal unitFaceValue = getUnitFaceValue();
		return value.divide(unitFaceValue, 0, RoundingMode.UP).setScale(value.scale()).multiply(unitFaceValue);
	}
	
	public static BigDecimal floorUnitFaceValue(BigDecimal value){
		BigDecimal unitFaceValue = getUnitFaceValue();
		return value.divide(unitFaceValue, 0, RoundingMode.DOWN).setScale(value.scale()).multiply(unitFaceValue);
	}

    public static BigDecimal formatSubsProgress(BigDecimal progress) {
    	BigDecimal unitFaceValue = getUnitFaceValue();
        long value = progress.multiply(unitFaceValue).longValue();
        if (value % 10 == 0) {
            return progress.multiply(NumberUtil.getHundred()).setScale(0, RoundingMode.DOWN);
        } else {
            return progress.multiply(NumberUtil.getHundred()).setScale(1, RoundingMode.DOWN);
        }
    }
    
    public static BigDecimal fiveUnitFaceValue(){
    	BigDecimal unitFaceValue = getUnitFaceValue();
    	return BigDecimal.valueOf(5).multiply(unitFaceValue);
    }
    
    public static boolean isValidFaceValue(String amtStr){
    	boolean bool = false;
    	try{
    		long amt = Long.valueOf(amtStr).longValue();
    		BigDecimal unitValue = getUnitFaceValue();
    		long val = unitValue.longValue();
    		long mod = amt%val;
    		if(mod==0){
        		bool = true;
    		}
    	}catch(Exception ex){
    		ex.printStackTrace();;
    	}
    	return bool;
    }
	
}
