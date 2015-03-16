package com.hengxin.platform.member.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kubek2k.springockito.annotations.SpringockitoContextLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.hengxin.platform.member.dto.MemberInfoSearchDto;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = SpringockitoContextLoader.class, locations = { "classpath:/test/spring/hengxin-platform-service-test.xml" })
@TransactionConfiguration(defaultRollback = false)
public class MemberInfoSearchServiceTest {

	@Autowired
	MemberInfoSearchService memberInfoSearchService;

	@Test
	public void testGetMemberInfoList() {
		MemberInfoSearchDto searchDto = new MemberInfoSearchDto();
		memberInfoSearchService.getMemberInfoList(searchDto);
	}

	@Test
	public void testGetMemberInfoExcel() {
		MemberInfoSearchDto searchDto = new MemberInfoSearchDto();
		memberInfoSearchService.getMemberInfoExcel(searchDto);
	}

}
