package com.hengxin.platform.fund.util;

import static com.hengxin.platform.common.enums.EErrorCode.TECH_DATA_INVALID;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.apache.commons.lang.StringUtils;

import com.hengxin.platform.common.exception.BizServiceException;

public class AmtUtils {

    public static BigDecimal processNegativeAmt(BigDecimal amtObj, BigDecimal defaultAmt) throws BizServiceException {
        BigDecimal target = null;
        if (amtObj != null) {
            if (amtObj.compareTo(BigDecimal.ZERO) < 0) {
                throw new BizServiceException(TECH_DATA_INVALID, "金额必须大于等于0.00");
            }
            target = max(amtObj, BigDecimal.ZERO);
        } else {
            if (defaultAmt != null) {
                if (defaultAmt.compareTo(BigDecimal.ZERO) < 0) {
                    throw new BizServiceException(TECH_DATA_INVALID, "金额必须大于等于0.00");
                }
                target = defaultAmt;
            }
        }
        return target;
    }

    public static BigDecimal processNullAmt(BigDecimal amtObj, BigDecimal defaultAmt) {
        BigDecimal target = null;
        if (amtObj != null) {
            target = amtObj;
        } else {
            if (defaultAmt != null) {
                target = defaultAmt;
            }
        }
        return target;
    }

    public static BigDecimal processNullAmt(Object amtObj, BigDecimal defaultAmt) {
        BigDecimal target = null;
        if (amtObj != null) {
            target = (BigDecimal) amtObj;
        } else {
            if (defaultAmt != null) {
                target = defaultAmt;
            }
        }
        return target;
    }
    
    public static String formatAmt2TwoDecimal(BigDecimal amtObj){
        return formatAmt(amtObj, "##,##0.00", StringUtils.EMPTY);
    }

    public static String formatAmt(BigDecimal amtObj, String formatStr, String defaultStr) {
        String amtStr = defaultStr;
        if (amtObj != null) {
            DecimalFormat format = new DecimalFormat();
            format.applyPattern(formatStr);
            amtStr = format.format(amtObj.doubleValue());
        }
        return amtStr;
    }

    public static BigDecimal max(BigDecimal a, BigDecimal b) {
        return a.compareTo(b) >= 0 ? a : b;
    }

    public static BigDecimal min(BigDecimal a, BigDecimal b) {
        return a.compareTo(b) <= 0 ? a : b;
    }

    public static Long processLong(Long val, Long defaultVal) {
        return val == null ? defaultVal : val;
    }

    public static Long max(Long a, Long b) {
        return a.compareTo(b) >= 0 ? a : b;
    }
    
    public static String formateRate(BigDecimal rate){
    	String result = null;
    	NumberFormat percent = NumberFormat.getPercentInstance();
        percent.setMinimumFractionDigits(6);
        result = percent.format(rate).replace("%", "");
        if(result.indexOf(".") > 0){  
        	result = result.replaceAll("0+?$", "");  
        	result = result.replaceAll("[.]$", ".0");
        }
        return result.concat("%");
    }    

}
