package com.hengxin.platform.report.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kubek2k.springockito.annotations.SpringockitoContextLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = SpringockitoContextLoader.class, 
	locations = {"classpath:/test/spring/hengxin-platform-service-test.xml"})
@TransactionConfiguration(defaultRollback = false)
public class PlatformOperDataServiceTest {

	@Autowired
	private PlatformOperDataService service;

	@Test
	public void testGetQuarterOperData() {
//		fail("Not yet implemented");
//		PlatformOperDataRepository repo = ApplicationContextUtil.getBean(PlatformOperDataRepository.class);
		service.getQuarterOperData("2015-1-11");
	}

}
