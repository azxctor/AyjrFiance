package com.hengxin.platform.escrow.ebc.service;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kubek2k.springockito.annotations.SpringockitoContextLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.hengxin.platform.escrow.dto.EswTransferOrderDto;
import com.hengxin.platform.escrow.enums.EOrderStatusEnum;
import com.hengxin.platform.escrow.service.EswTransferService;
import com.hengxin.platform.fund.util.DateUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = SpringockitoContextLoader.class, locations = { "classpath:/test/spring/hengxin-platform-service-test.xml" })
@TransactionConfiguration(defaultRollback = false)
public class TransferServiceTest {
	
	@Autowired
	private EswTransferService transferService;
	
	@Test
	public void queryOrderToTransfer(){
		Date trxDate = DateUtils.getDate("2014-12-03", "yyyy-MM-dd");
		
		List<EswTransferOrderDto> list = transferService.getTransferOrderToTransfer(trxDate, EOrderStatusEnum.WAITING, Integer.valueOf(10));
	
		System.out.println(list.size());
	}
	
}
