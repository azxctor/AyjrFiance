package com.hengxin.platform.product;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.hengxin.platform.common.util.NumberUtil;

public final class ProductUtil {

    private ProductUtil() {

    }

    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        return s.format(date);
    }
    
    public static String formatDate(Date date, String format) {
    	if (date == null || format == null) {
            return "";
        }
        SimpleDateFormat s = new SimpleDateFormat(format);
        return s.format(date);
    }

    public static Date getDate() {
        Calendar cd = Calendar.getInstance();
        cd.setTime(new Date());
        cd.add(Calendar.DATE, -1);
        return cd.getTime();
    }
    
    public static BigDecimal getMinQuotaValue(){
    	return NumberUtil.getTenThousand();
    }

}
