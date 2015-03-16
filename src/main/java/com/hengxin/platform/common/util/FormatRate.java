package com.hengxin.platform.common.util;

import java.math.BigDecimal;

public class FormatRate {
  
    /**
     * 年利率去掉多余的零，并保留一位小数
    * Description: TODO
    *
    * @param s
    * @return
     */
    public static BigDecimal formatRate(BigDecimal s) {
        String format = s.toString();
        if (format.indexOf(".") > 0) {
            format = format.replaceAll("0+?$", "");// 去掉多余的0
            format = format.replaceAll("[.]$", ".0");// 如最后一位是.则替换为".0"
        }
        BigDecimal result = new BigDecimal(format);
        return result;
    }

}
