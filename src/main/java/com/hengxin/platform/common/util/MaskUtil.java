package com.hengxin.platform.common.util;

import org.apache.commons.lang.StringUtils;

/**
 * Class Name: MaskUtil Description: TODO
 * 
 * @author shengzhou
 * 
 */
public class MaskUtil {

    /**
     * Description: 保留后4位，其他替换为*号.
     * 
     * @param value
     * @return
     */
    public static String maskCardNumber(String value) {
        if (!StringUtils.isBlank(value)) {
            int length = value.length() - 4;
            value = StringUtils.repeat("*", length) + value.substring(length, value.length());
        }
        return value;
    }

    /**
     * Description: 隐藏手机号中间4位 ，替换为*号.
     * 
     * @param value
     * @return
     */
    public static String maskPhone(String value) {
        if (!StringUtils.isBlank(value) && value.length() == 11) {
            value = value.substring(0, 3) + "****" + value.substring(7, 11);
        }
        return value;
    }

    /**
     * Description: 将名替换为**号.
     * 
     * @param value
     * @return
     */
    public static String maskChinsesName(String value) {
        if (!StringUtils.isBlank(value)) {
            value = value.substring(0, 1) + "**";
        }
        return value;
    }

    /**
     * Description: 企业名称，mask3-6个字符，如小于6位则mask除前两位外所有字符.
     * 
     * @param value
     * @return
     */
    public static String maskEnterpriseName(String value) {
        if (!StringUtils.isBlank(value)) {
        	if (value.length() >= 6) {
        		value = value.substring(0, 2) + "****" + value.substring(6);
        	} else if (value.length() >= 2) {
        		value = value.substring(0, 2) + StringUtils.repeat("*", value.length() - 2);
        	}
        }
        return value;
    }

}
