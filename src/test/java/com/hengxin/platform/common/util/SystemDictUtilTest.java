package com.hengxin.platform.common.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kubek2k.springockito.annotations.SpringockitoContextLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.hengxin.platform.common.domain.DynamicOption;
import com.hengxin.platform.common.enums.EOptionCategory;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = SpringockitoContextLoader.class, locations = { "classpath:/test/spring/hengxin-platform-service-test.xml" })
@TransactionConfiguration(defaultRollback = false)
public class SystemDictUtilTest {
	@Test
	@SuppressWarnings("unused")
	public void testGetDictByCategoryAndCode() {
		DynamicOption dy = SystemDictUtil.getDictByCategoryAndCode(EOptionCategory.REGION, "4211");
		DynamicOption parent = SystemDictUtil.getParentDict(dy);
	}

}
