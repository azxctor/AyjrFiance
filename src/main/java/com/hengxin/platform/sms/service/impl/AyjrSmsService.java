package com.hengxin.platform.sms.service.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hengxin.platform.sms.service.AbstractSmsService;
import com.hengxin.platform.sms.utils.HttpClientUtils;

@Service
@Qualifier("ayjrSmsService")
public class AyjrSmsService extends AbstractSmsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AyjrSmsService.class);
	
	@Override
	public boolean sendRegisterSms(String mobile, String content, String cid, String ...args) {
		try {
			String smsUrl = getTodoSmsUrl();
	    	String userName = getTodoSmsUserName();
	        String passWord = getTodoSmsPwd();
			sendSMS(smsUrl, userName, passWord, mobile, content, cid, args);
		} catch (Exception e) {
			LOGGER.error("SMS failed : {}", e);
			return false;
		}
		return true;
	}

	@Override
	public String sendSms(String mobile, String content, String cid, String ...args) {
        try {
    		String smsUrl = getTodoSmsUrl();
        	String userName = getTodoSmsUserName();
            String passWord = getTodoSmsPwd();
            sendSMS(smsUrl, userName, passWord, mobile, content, cid, args);
		} catch (Exception e) {
			LOGGER.error("SMS failed : {}", e);
			return "f";
		}
        return "s";
	}
	
	private void sendSMS(String smsUrl, String userName, String pwd, String mobile, String content, String cid, String ...args)throws Exception{
		StringBuffer urlStr = new StringBuffer();
		urlStr.append(smsUrl);
        List<String> params = new ArrayList<String>();
        params.add(userName);
        params.add(pwd);
        params.add(mobile);
        if(StringUtils.isNotBlank(cid)){
            params.add(cid);
        	urlStr.append("&cid={").append(params.size()-1).append("}");
        	if(args!=null){
        		int idx = 1;
	        	for(String pm:args){
	        		params.add(java.net.URLEncoder.encode(pm, "utf8"));
	        		urlStr.append("&p");
	        		urlStr.append(idx);
	        		urlStr.append("={");
	        		urlStr.append(params.size()-1);
	        		urlStr.append("}");
	        		idx++;
	        	}
        	}
        }
        else{
        	content = getSmsSuffix()+content;
        	content = java.net.URLEncoder.encode(content, "utf8");
            content = content.replace(" ", "%20");
            params.add(content);
        }
        Object[] objs = params.toArray();
        String url = MessageFormat.format(urlStr.toString(), objs);
		LOGGER.info("SMS URL {} ", url);
		HttpResponse resp = HttpClientUtils.httpGetExecute(url);
		LOGGER.debug("SMS status: {}", resp.getStatusLine().getStatusCode());
		if (HttpStatus.SC_OK == resp.getStatusLine().getStatusCode()) {
			LOGGER.debug("SMS response: {}", EntityUtils.toString(resp.getEntity()));
		}
		else{
			throw new Exception("返回状态为非成功"+resp.getStatusLine().getStatusCode());
		}
	}
	
	public static void main(String[] args){
		String smsUrl = "http://api.weimi.cc/2/sms/send.html?uid={0}&pas={1}&mob={2}";
		String userName = "g1s9qEh4WCi4";
		String pwd = "xkq3ryxh";
		String mobile = "18814868391";
		String code = "212131";
		String cid = "tegHVNcir549";
		System.out.println(code);
		System.out.println(smsUrl);
		AyjrSmsService sms = new AyjrSmsService();
		try {
			sms.sendSMS(smsUrl, userName, pwd, mobile, null, cid, code);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
