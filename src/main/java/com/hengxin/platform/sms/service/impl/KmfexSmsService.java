package com.hengxin.platform.sms.service.impl;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;

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
@Qualifier("kmfexSmsService")
public class KmfexSmsService extends AbstractSmsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(KmfexSmsService.class);

	@Override
	public boolean sendRegisterSms(String mobile, String content, String cid, String ...args) {
    	String smsUrl = getRegSmsUrl();
    	String userName = getRegSmsUserName();
        String passWord = getRegSmsPwd();
		try {
			content = getSmsSuffix()+content;
			content = java.net.URLEncoder.encode(content, "utf8");
	        content = content.replace(" ", "%20");
	        Object[] objects = new Object[]{userName, passWord, mobile, content};
	        smsUrl = MessageFormat.format(smsUrl, objects);
			LOGGER.info("SMS URL {} ", smsUrl);
			HttpResponse resp = HttpClientUtils.httpPostExecute(smsUrl);
			LOGGER.debug("SMS status: {}", resp.getStatusLine().getStatusCode());
			if (HttpStatus.SC_OK == resp.getStatusLine().getStatusCode()) {
				LOGGER.debug("SMS response: {}", EntityUtils.toString(resp.getEntity()));
			} else {
				return false;
			}
		} catch (Exception e) {
			LOGGER.error("SMS failed : {}", e);
			return false;
		}
		return true;
	}
	
	@Override
	public String sendSms(String mobile, String content, String cid, String ...args) {
		String smsUrl = getTodoSmsUrl();
    	String userName = getTodoSmsUserName();
        String passWord = getTodoSmsPwd();
        try {
        	content = getSmsSuffix()+content;
            StringBuffer buffer = new StringBuffer();
            buffer.append(smsUrl);
            buffer.append("?reg=");
            buffer.append(userName);
            buffer.append("&pwd=");
            buffer.append(passWord);
            buffer.append("&sourceadd=&phone=");
            buffer.append(mobile);
            buffer.append("&content=");
            buffer.append(this.paraTo16(content));
			LOGGER.info("SMS URL {} ", buffer.toString());
			HttpResponse resp = HttpClientUtils.httpPostExecute(buffer.toString());
			LOGGER.debug("SMS status: {}", resp.getStatusLine().getStatusCode());
			if (HttpStatus.SC_OK == resp.getStatusLine().getStatusCode()) {
				LOGGER.debug("SMS response: {}", EntityUtils.toString(resp.getEntity()));
			} else {
				return "f";
			}
		} catch (Exception e) {
			LOGGER.error("SMS failed : {}", e);
			return "f";
		}
        return "s";
	}

	/**
	 * 转为16进制方法
	 * @param str
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	private String paraTo16(String str) throws UnsupportedEncodingException {
		String hs = "";
		byte[] byStr = str.getBytes("UTF-8");
		for (int i=0;i<byStr.length;i++) {
			String temp = "";
			temp = (Integer.toHexString(byStr[i]&0xFF));
			if(temp.length()==1) temp = "%0"+temp;
			else temp = "%"+temp;
			hs = hs+temp;
		}
		return hs.toUpperCase();
	}
	
	
}
