package com.hengxin.platform.escrow.ebc.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kubek2k.springockito.annotations.SpringockitoContextLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.hengxin.platform.escrow.service.EswMobileService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = SpringockitoContextLoader.class, locations = { "classpath:/test/spring/hengxin-platform-service-test.xml" })
@TransactionConfiguration(defaultRollback = false)
public class EbcMobileServiceTest {
	@Autowired 
	EswMobileService ee;

	@Test
	public void testGetMobileVerifyCode() {
		String userId = "880000000024";
		String newMobile = "18658831602";
		ee.getMobileVerifyCode(userId, newMobile); 
	}
}
