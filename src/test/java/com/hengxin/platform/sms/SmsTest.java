package com.hengxin.platform.sms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kubek2k.springockito.annotations.SpringockitoContextLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.hengxin.platform.member.service.RegisterService;
import com.hengxin.platform.sms.service.impl.AyjrSmsService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = SpringockitoContextLoader.class, 
 locations = {"classpath:/test/spring/hengxin-platform-service-test.xml"})
@TransactionConfiguration(defaultRollback = false)
public class SmsTest {
	
	@Autowired
	private AyjrSmsService smsService;
	
	@Autowired
	private RegisterService regService;
	
	@Test
	public void sendSmsTest(){
		smsService.sendSms("15381134620", "短信发送测试", null, new String[]{});
	}
	
	@Test
	public void sendAuthCode(){
		regService.smsVerify("15381134620");
	}
	
	
	public static void main(String[] args){
		Boolean bool = Boolean.valueOf("Y");
		System.out.println(bool.booleanValue());
	}
	
	
}
