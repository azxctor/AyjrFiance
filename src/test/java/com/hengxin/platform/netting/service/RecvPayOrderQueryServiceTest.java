package com.hengxin.platform.netting.service;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kubek2k.springockito.annotations.SpringockitoContextLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.netting.entity.RecvPayOrderPo;
import com.hengxin.platform.netting.enums.NettingStatusEnum;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = SpringockitoContextLoader.class, locations = { "classpath:/test/spring/hengxin-platform-service-test.xml" })
@TransactionConfiguration(defaultRollback = false)
public class RecvPayOrderQueryServiceTest {

	@Autowired
	private RecvPayOrderQueryService recvPayOrderQueryService;

	@Test
	public void getRecvOrderQueryList() {

		Date trxDate = DateUtils.getDate("2014-12-30", "yyyy-MM-dd");
		List<RecvPayOrderPo> listD = recvPayOrderQueryService
				.getRecvPayOrderList(trxDate, NettingStatusEnum.D);
		List<RecvPayOrderPo> listW = recvPayOrderQueryService
				.getRecvPayOrderList(trxDate, NettingStatusEnum.W);
		System.out.println(trxDate);
		System.out.println("已轧差："+listD.size());
		System.out.println("待轧差："+listW.size());
	}
}
