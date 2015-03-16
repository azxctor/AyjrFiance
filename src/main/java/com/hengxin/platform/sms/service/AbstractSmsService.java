package com.hengxin.platform.sms.service;

import com.hengxin.platform.common.util.AppConfigUtil;
import com.hengxin.platform.sms.consts.SmsConsts;

public abstract class AbstractSmsService {
    
    protected String getRegSmsUrl(){
    	return AppConfigUtil.getConfig(SmsConsts.REG_SMS_URL);
    }
    
    protected String getRegSmsUserName(){
    	return AppConfigUtil.getConfig(SmsConsts.REG_SMS_USERNAME);
    }
    
    protected String getRegSmsPwd(){
    	return AppConfigUtil.getConfig(SmsConsts.REG_SMS_PASSWORD);
    }
    
    protected String getTodoSmsUrl(){
    	return AppConfigUtil.getConfig(SmsConsts.SMS_TODO_URL);
    }
    
    protected String getTodoSmsUserName(){
    	return AppConfigUtil.getConfig(SmsConsts.SMS_TODO_USERNAME);
    }
    
    protected String getTodoSmsPwd(){
    	return AppConfigUtil.getConfig(SmsConsts.SMS_TODO_PASSWORD);
    }
    
    protected final String getSmsSuffix(){
    	return AppConfigUtil.getConfig(SmsConsts.SMS_SUFFIX);
    }
	
    /**
     * 注册短信发送方法
     * @param mobile
     * @param content
     * @return
     */
	public abstract boolean sendRegisterSms(String mobile, String content, String cid, String ...args);
	
	/**
	 * 待办短信发送方法
	 * @param mobile
	 * @param content
	 * @return
	 */
	public abstract String sendSms(String mobile, String content, String cid, String ...args);
	
}
