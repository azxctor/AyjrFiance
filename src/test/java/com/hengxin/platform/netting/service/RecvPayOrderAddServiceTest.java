package com.hengxin.platform.netting.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kubek2k.springockito.annotations.SpringockitoContextLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.hengxin.platform.common.entity.id.IdUtil;
import com.hengxin.platform.fund.dto.biz.req.atom.TransferInfo;
import com.hengxin.platform.fund.enums.ECashPool;
import com.hengxin.platform.fund.enums.EFundUseType;
import com.hengxin.platform.fund.util.DateUtils;

import java.math.BigDecimal;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = SpringockitoContextLoader.class, locations = { "classpath:/test/spring/hengxin-platform-service-test.xml" })
@TransactionConfiguration(defaultRollback = false)
public class RecvPayOrderAddServiceTest {

	@Autowired
	private RecvPayOrderAddService recvPayOrderAddService;
	private List<TransferInfo> payerList = new ArrayList<TransferInfo>();
	private List<TransferInfo> payeeList = new ArrayList<TransferInfo>();

	@Test
	public void createRecvPayOrder() {
		String eventId = "1001";
		String bizId = "";
		String pkgId = "";
		String seqId = "";
		String currOpId = "";
		Date workDate;
		TransferInfo trans = new TransferInfo();
		trans.setUserId("880000000004");
		BigDecimal bd = new BigDecimal(1000000);
		trans.setTrxAmt(bd);
		trans.setTrxMemo("test1");
		System.out.println("1111111111111");
		System.out.println(trans.getTrxMemo() + trans.getUserId() + trans.getTrxAmt());
		payerList.add(trans);
		payeeList.add(trans);
		workDate = DateUtils.getDate("2014-12-30", "yyyy-MM-dd");
		recvPayOrderAddService.createRecvPayOrder(eventId, payerList,
				payeeList, bizId, pkgId, seqId, currOpId, workDate);
	}
	
	@Test
	public void batchCreateOrder(){
		String eventId = IdUtil.produce();
		String bizId = "PKG1232323_12";
		String pkgId = "PKG1232323";
		String seqId = "12";
		String currOpId = "TEST";
		ECashPool cashPool = ECashPool.ESCROW_EBC;
		EFundUseType useType = EFundUseType.FNCR_REPAYMENT;
		Date workDate = DateUtils.getDate("2015-01-07", "yyyy-MM-dd");
		
		List<String> payerUserInfos = Arrays.asList(
				 "880000000026#14475",
				 "880000000022#25350",
				 "880000000025#33528.12",
				 "880000000004#6625.37");
		for(String uf:payerUserInfos){
			String[] strs = uf.split("#");
			String userId  = strs[0];
			BigDecimal trxAmt = BigDecimal.valueOf(Double.valueOf(strs[1]));
			TransferInfo info = new TransferInfo();
			info.setBizId(bizId);
			info.setCashPool(cashPool);
			info.setRelZQ(false);
			info.setTrxAmt(trxAmt);
			info.setTrxMemo("付款");
			info.setUserId(userId);
			info.setUseType(useType);
			payerList.add(info);
		}
		
		List<String> payeeUserInfos = Arrays.asList(
				"880000000005#15000",
				"880000000023#1088.97",
				"880000000027#6666",
				"880000000025#15600",
				"880000000014#1234.52",
				"880000000021#2632",
				"880000000020#15233",
				"880000000010#22524");
		for(String uf:payeeUserInfos){
			String[] strs = uf.split("#");
			String userId  = strs[0];
			BigDecimal trxAmt = BigDecimal.valueOf(Double.valueOf(strs[1]));
			TransferInfo info = new TransferInfo();
			info.setBizId(bizId);
			info.setCashPool(cashPool);
			info.setRelZQ(false);
			info.setTrxAmt(trxAmt);
			info.setTrxMemo("收款");
			info.setUserId(userId);
			info.setUseType(useType);
			payeeList.add(info);
		}

		recvPayOrderAddService.createRecvPayOrder(eventId, payerList,
				payeeList, bizId, pkgId, seqId, currOpId, workDate);
	}
	
	public static void main(String[] args){
		BigDecimal payAmt = BigDecimal.valueOf(14475);
		payAmt = payAmt.add(BigDecimal.valueOf(25350));
		payAmt = payAmt.add(BigDecimal.valueOf(33528.12));
		payAmt = payAmt.add(BigDecimal.valueOf(6625.37));

		BigDecimal recvAmt = BigDecimal.valueOf(15000);
		recvAmt = recvAmt.add(BigDecimal.valueOf(1088.97));
		recvAmt = recvAmt.add(BigDecimal.valueOf(6666));
		recvAmt = recvAmt.add(BigDecimal.valueOf(15600));
		recvAmt = recvAmt.add(BigDecimal.valueOf(1234.52));
		recvAmt = recvAmt.add(BigDecimal.valueOf(2632));
		recvAmt = recvAmt.add(BigDecimal.valueOf(15233));
		recvAmt = recvAmt.add(BigDecimal.valueOf(22524));
		
		System.out.println("payAmt ---->"+payAmt);
		
		System.out.println("recvAmt---->"+recvAmt);
	}

}
