package com.hengxin.platform.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Pattern;

public final class NumberUtil {

    private static final BigDecimal hundred = BigDecimal.valueOf(100);
//    private static final BigDecimal twoHundred = BigDecimal.valueOf(200);
//    private static final BigDecimal thousand = BigDecimal.valueOf(1000);
//    private static final BigDecimal fiveThousand = BigDecimal.valueOf(5000);
    private static final BigDecimal tenThousand = BigDecimal.valueOf(10000);

    private NumberUtil() {
    }
    
    public static BigDecimal getHundred() {
    	return hundred;
    }
    
//    public static BigDecimal getTwoHundred() {
//    	return twoHundred;
//    }
    
//    public static BigDecimal getThousand() {
//    	return thousand;
//    }
    
//    public static BigDecimal getFiveThousand() {
//    	return fiveThousand;
//    }
//    
    public static BigDecimal getTenThousand() {
    	return tenThousand;
    }
    
    /**
     * 格式化金额为 万元 单位
     * @param value
     * @return
     */
    public static BigDecimal formatTenThousandUnitAmt(BigDecimal value){
    	return value.divide(getTenThousand());
    }

    public static boolean isPositive(Integer i) {
        return i != null && i > 0;
    }

    public static boolean isPositive(BigDecimal i) {
        return i != null && i.compareTo(BigDecimal.ZERO) > 0;
    }

    public static boolean isNotNegative(BigDecimal i) {
    	return i != null && i.compareTo(BigDecimal.ZERO) >= 0;
    }

    public static String getPercentStr(BigDecimal value, int scale, boolean hasSign) {
        String sign = hasSign ? "%" : "";
        if (value != null) {
            BigDecimal multiply = value.multiply(hundred).stripTrailingZeros();
			if (multiply.scale() < scale) {
				multiply = multiply.setScale(scale);
			}
            return multiply.toString() + sign;
        }
        return BigDecimal.ZERO.setScale(scale).toString() + sign;
    }

    public static String[] getPercentSplitStr(BigDecimal value, int scale) {
        return getPercentStr(value, scale, false).split("\\.");
    }

    public static String formatMoney(BigDecimal value, boolean hasDecimal) {
        if (value != null) {
            return formatMoney(value.doubleValue(), hasDecimal);
        }
        return formatMoney(0d, hasDecimal);
    }

    public static String formatMoney(BigDecimal value) {
        if (value != null) {
            return formatMoney(value.doubleValue(), true);
        }
        return "0.00";
    }

    public static String formatMoney(Long value, boolean hasDecimal) {
        if (value != null) {
            return formatMoney(value.doubleValue(), hasDecimal);
        }
        return formatMoney(0d, hasDecimal);
    }

    public static String formatMoney(Long value) {
        if (value != null) {
            return formatMoney(value.doubleValue(), true);
        }
        return "0.00";
    }

    public static String formatMoney(Double value, boolean hasDecimal) {
        String regexp = ",###.##";
        if (hasDecimal) {
            regexp = ",##0.00";
        }
        NumberFormat formatter = new DecimalFormat(regexp);
        formatter.setRoundingMode(RoundingMode.DOWN);
        return formatter.format(value);
    }

    public static String formatLargeMoney(BigDecimal value) {
        if (value != null && BigDecimal.ZERO.compareTo(value) < 0) {
            NumberFormat formatter = new DecimalFormat(",###");
            return formatter.format(value.divide(tenThousand, 0, RoundingMode.DOWN));
        }
        return "0";
    }

    public static String formatToThousands(BigDecimal value) {
        if (value != null) {
            NumberFormat formatter = new DecimalFormat(",###");
            return formatter.format(value);
        }
        return "0";
    }
    public static String formatToThousandsWithFractionDigits(BigDecimal value) {
        if (value != null) {
            NumberFormat formatter = new DecimalFormat(",##0.00");
            return formatter.format(value);
        }
        return "0";
    }

    public static String toRMB(String money) {
    	return toRMB(Double.valueOf(money));
    }

    public static String toRMB(double money) {
        char[] s1 = { '零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖' };
        char[] s4 = { '分', '角', '元', '拾', '佰', '仟', '万', '拾', '佰', '仟', '亿', '拾', '佰', '仟', '万' };
        String str = String.valueOf(Math.round(money * 100 + 0.00001));
        String result = "";

        for (int i = 0; i < str.length(); i++) {
            int n = str.charAt(str.length() - 1 - i) - '0';
            result = s1[n] + "" + s4[i] + result;
        }

        result = result.replaceAll("零仟", "零");
        result = result.replaceAll("零佰", "零");
        result = result.replaceAll("零拾", "零");
        result = result.replaceAll("零亿", "亿");
        result = result.replaceAll("零万", "万");
        result = result.replaceAll("零元", "元");
        result = result.replaceAll("零角", "零");
        result = result.replaceAll("零分", "零");

        result = result.replaceAll("零零", "零");
        result = result.replaceAll("零亿", "亿");
        result = result.replaceAll("零零", "零");
        result = result.replaceAll("零万", "万");
        result = result.replaceAll("零零", "零");
        result = result.replaceAll("零元", "元");
        result = result.replaceAll("亿万", "亿");

        result = result.replaceAll("零$", "");
        result = result.replaceAll("元$", "元整");
        result = result.replaceAll("角$", "角整");

        return result;
    }
    
    public static boolean isNumeric(String str) {
		return isInteger(str) || isDouble(str);
	}
    
	public static boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}

	public static boolean isDouble(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
		return pattern.matcher(str).matches();
	}
}
