package com.hengxin.platform.common.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kubek2k.springockito.annotations.SpringockitoContextLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.hengxin.platform.ebc.consts.EBCConsts;
import com.hengxin.platform.ebc.dto.EbcAccountBalanceQueryRequest;
import com.hengxin.platform.ebc.dto.EbcBankListRequest;
import com.hengxin.platform.ebc.dto.EbcBindingBankCardRequest;
import com.hengxin.platform.ebc.dto.EbcBindingResultQueryRequest;
import com.hengxin.platform.ebc.dto.EbcRechargeBankListRequest;
import com.hengxin.platform.ebc.dto.EbcRechargeBankListResponse;
import com.hengxin.platform.ebc.dto.EbcRechargeRequest;
import com.hengxin.platform.ebc.dto.EbcTransferRequest;
import com.hengxin.platform.ebc.dto.bank.PayeeBank;
import com.hengxin.platform.ebc.dto.bank.RechargeBank;
import com.hengxin.platform.escrow.dto.CommandRequest;
import com.hengxin.platform.escrow.dto.CommandResponse;
import com.hengxin.platform.escrow.enums.EEbcUserType;
import com.hengxin.platform.escrow.service.EswBaseService;
import com.hengxin.platform.escrow.utils.EswUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = SpringockitoContextLoader.class, locations = { "classpath:/test/spring/hengxin-platform-service-test.xml" })
@TransactionConfiguration(defaultRollback = false)
public class EbcTest extends EswBaseService {

	CommandRequest request;
	CommandResponse response;

	@Test
	public void bindingBankCardTest() {
		request = buildEbcBindingBankCardRequest();
		response = request.execute();
		fail(response);
	}

	private CommandRequest buildEbcBindingBankCardRequest() {
		EbcBindingBankCardRequest request = new EbcBindingBankCardRequest();
		request.setMerchNo("000000000000000");
		request.setVersion("0120");
		request.setOrderSn(System.currentTimeMillis() + "");
		request.setOwnerId("EBC");
		request.setUserNo("ff4f50b79d094a1485794713e9f008e3");
		request.setMediumNo("0100980002605529");
		request.setUserName("cong");
		request.setCardNo("6222021204005924458");
		request.setAbtNo("000002");
		request.setCurrency("CNY");
		request.setUserType("1");
		request.setBankId("102100099996");
		request.setBankCode("102331002044");
		request.setBankName("中国工商银行杭州市西湖支行");
		request.setPayeeBankid("102");
		request.setProvinceCode("3310");
		request.setCityCode("3310");
		return request;
	}

	private void fail(Object o) {
		System.out.println(o);
	}

	@Test
	public void getBankList() {
		request = buildEbcBankListRequest();
		response = request.execute();
		fail(response);

	}

	// 3.25 获取联行号
	private CommandRequest buildEbcBankListRequest() {
		EbcBankListRequest request = new EbcBankListRequest();
		request.setOrderSn(System.currentTimeMillis() + "");
		request.setUserNo("ff4f50b79d094a1485794713e9f008e3");
		request.setMediumNo("0100980002605529");
		request.setOwnerId("EBC");
		request.setQueryType("2");
		// request.setProvinceCode("3310");
		// request.setPayeeBankId("102");
		// request.setCityCode("3310");
		return request;
	}

	@Test
	public void jsonTest() throws JsonParseException, JsonMappingException,
			IOException {
		String data = "[{payeebankid=102, payeebankname=中国工商银行}, {payeebankid=103, payeebankname=中国农业银行}, {payeebankid=104, payeebankname=中国银行}, {payeebankid=105, payeebankname=中国建设银行}, {payeebankid=301, payeebankname=交通银行}, {payeebankid=302, payeebankname=中信银行}, {payeebankid=303, payeebankname=中国光大银行}, {payeebankid=304, payeebankname=华夏银行}, {payeebankid=305, payeebankname=中国民生银行}, {payeebankid=306, payeebankname=广东发展银行}, {payeebankid=307, payeebankname=平安银行}, {payeebankid=308, payeebankname=招商银行}, {payeebankid=309, payeebankname=福建兴业银行}, {payeebankid=310, payeebankname=上海浦东发展银行}, {payeebankid=313, payeebankname=城市商业银行}, {payeebankid=314, payeebankname=农村商业银行}, {payeebankid=315, payeebankname=恒丰银行}, {payeebankid=316, payeebankname=浙商银行}, {payeebankid=317, payeebankname=农村合作银行}, {payeebankid=318, payeebankname=渤海银行}, {payeebankid=319, payeebankname=徽商银行}, {payeebankid=322, payeebankname=上海农村商业银行}, {payeebankid=402, payeebankname=农村信用合作社}, {payeebankid=403, payeebankname=中国邮政储蓄银行}, {payeebankid=591, payeebankname=韩国外换银行}, {payeebankid=593, payeebankname=友利银行（中国）有限公司}, {payeebankid=595, payeebankname=新韩银行（中国）有限公司}, {payeebankid=596, payeebankname=韩国中小企业银行（中国）有限公司}, {payeebankid=597, payeebankname=韩亚银行（中国）有限公司}]";
		ObjectMapper mapper = new ObjectMapper();
		// List bankInfo = JsonUtil.json_2_bean(data, List.class);
		// fail(bankInfo);
	}

	List<PayeeBank> getPayeeBankList() {
		List<PayeeBank> bankInfo = new ArrayList<PayeeBank>();
		PayeeBank bank = new PayeeBank();
		bank.setPayeeBankId("102");
		bank.setPayeeBankName("中国工商银行");
		bankInfo.add(bank);

		PayeeBank bank2 = new PayeeBank();
		bank2.setPayeeBankId("103");
		bank2.setPayeeBankName("中国农业银行");
		bankInfo.add(bank2);

		PayeeBank bank3 = new PayeeBank();
		bank2.setPayeeBankId("104");
		bank2.setPayeeBankName("中国银行");
		bankInfo.add(bank2);
		return bankInfo;
	}

	@Test
	public void jsonsTest() throws JsonGenerationException,
			JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		List<PayeeBank> bankInfo = new ArrayList<PayeeBank>();
		PayeeBank bank = new PayeeBank();
		bank.setPayeeBankId("102");
		bank.setPayeeBankName("中国工商银行");
		bankInfo.add(bank);
		String result = mapper.writeValueAsString(bankInfo);
		fail(result);
	}

	@Test
	@Deprecated
	public void bindingResultQueryTest() {
		request = buildEbcBindingResultQueryRequest();
		response = request.execute();
		fail(response);
	}

	private CommandRequest buildEbcBindingResultQueryRequest() {
		EbcBindingResultQueryRequest request = new EbcBindingResultQueryRequest();
		request.setOrderSn(System.currentTimeMillis() + "");
		request.setMerchNo("000000000000000");
		request.setUserNo("ff4f50b79d094a1485794713e9f008e3");
		request.setMediumNo("0100980002605529");
		request.setOwnerId("EBC");
		request.setCardNo("6222021204005924458");
		return request;
	}

	@Test
	public void rechargeTest() {
		request = buildEbcRechargeRequest();
		response = request.execute();
		fail(response);
	}

	private CommandRequest buildEbcRechargeRequest() {
		EbcRechargeRequest request = new EbcRechargeRequest();
		request.setOrderSn(System.currentTimeMillis() + "");
		request.setMerchNo("000000000000000");
		request.setDsOrderId(System.currentTimeMillis() + "");
		request.setUserNo("ff4f50b79d094a1485794713e9f008e3");
		request.setMediumNo("0100980002605529");
		request.setOwnerId("EBC");
		request.setCardNo("6222021204005924458");
		request.setCurrency("CNY");
		request.setAmount(new BigDecimal("100"));
		// request.setEbcBankId(ebcBankId);
		// request.setUserType(userType);
		// request.setDsybUrl(dsybUrl);
		// request.setDstbUrl(dstbUrl);
		// request.setDstbDataSign(dstbDataSign);
		return null;
	}

	@Test
	public void rechargeBankTest() {

		List<RechargeBank> result = new ArrayList<RechargeBank>();
		EbcRechargeBankListRequest request = (EbcRechargeBankListRequest) buildEbcRechargeBankListRequest();
		EbcRechargeBankListResponse response = (EbcRechargeBankListResponse) request
				.execute();
		result.addAll(response.getBankList());
		fail(response);
	}

	private CommandRequest buildEbcRechargeBankListRequest() {
		EbcRechargeBankListRequest request = new EbcRechargeBankListRequest();
		// request.setMerchNo("000000000000000");
		// request.setUserType("1");

		request.setMerchNo(EswUtils.getEswMerChNo());
		request.setUserType(EEbcUserType.PERSON.getCode());
		return request;
	}

	@Test
	public void transferTest() {
		request = buildEbcTransferRequest();
		response = request.execute();
		fail(response);
	}

	private CommandRequest buildEbcTransferRequest() {
		EbcTransferRequest request = new EbcTransferRequest();
		request = (EbcTransferRequest) super.buildRequest(request);
		request.setDsOrderId(System.currentTimeMillis() + "");
		request.setOrderSn(System.currentTimeMillis() + "");
		request.setUserNo("199adf12198a43bbb5ccc3642754fa1d");
		request.setMediumNo("0100980002911820");
		request.setCardNo("9001990003328529");
		request.setIncardNo("9001990002994196");
		request.setPayPass("B517D4C96CB47604A57822E738303911");
		request.setCurrency("CNY");
		request.setAmount(new BigDecimal("0.01"));
		request.setAccountType("02");
		request.setDescribe("lou to cong, transfer 0.01 yuan");
		request.setOrderType("转账");
		return request;
	}

	@Test
	public void jacksonTest() throws JsonGenerationException,
			JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		request = buildEbcTransferRequest();
		String requestJson = mapper.writeValueAsString(request);
		fail(requestJson);
	}

	@Test
	public void accountBanlanceQueryTest() {
		EbcAccountBalanceQueryRequest request = new EbcAccountBalanceQueryRequest();
		request = (EbcAccountBalanceQueryRequest) super.buildRequest(request);
		request.setUserNo("ff4f50b79d094a1485794713e9f008e3");
		request.setMediumNo("0100980002605529");
		request.setCardNo("9001990002994196");
		request.setOrderSn(System.currentTimeMillis() + "");
		response = request.execute();
		fail(response);
	}

}
