package com.hengxin.platform.escrow.ebc.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kubek2k.springockito.annotations.SpringockitoContextLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.hengxin.platform.escrow.dto.EswPayPwdDto;
import com.hengxin.platform.escrow.dto.EswSignupDto;
import com.hengxin.platform.escrow.service.EswAccountService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = SpringockitoContextLoader.class, locations = { "classpath:/test/spring/hengxin-platform-service-test.xml" })
@TransactionConfiguration(defaultRollback = false)
public class EbcAccountServiceTest {

	@Autowired
	EswAccountService  es;
	
	@Test
	public void testSignUp() {
     	//EbcAccountService es = new EbcAccountService();
		String userId = "880000000024";
		String payPwd = "11dsadsa1";
		EswSignupDto ebcSingupDto = new EswSignupDto();
		ebcSingupDto.setUserId(userId);
		ebcSingupDto.setPayPwd(payPwd);
		es.signup(ebcSingupDto);
	}
/*
	@Test
	public void testUpdateAcct() {
		EbcUpdateAcctDto updateAcctDto = new EbcUpdateAcctDto();
		updateAcctDto.setOldPayPwd("11dsadsa1");
		updateAcctDto.setNewPayPwd("123456");
		String userId = "880000000024";
		updateAcctDto.setMobile("13588818853");
		updateAcctDto.setAuthCode("778782");
		es.updateAcct(userId, updateAcctDto);
		//updateAcctDto.setMobil("13511111117");
	}
*/
	
	@Test
	public void testupdatePayPwd(){
		EswPayPwdDto ebcPayPwdDto = new EswPayPwdDto();
		ebcPayPwdDto.setOriginalPayPwd("039124");
		ebcPayPwdDto.setNewPayPwd("111111");
		ebcPayPwdDto.setConfirmPayPwd("111111");
		ebcPayPwdDto.setUserId("880000000056");
		es.updatePayPwd(ebcPayPwdDto);
	}

}
