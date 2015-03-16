//package com.hengxin.platform.common.util;
//
//import java.io.IOException;
//import java.io.Serializable;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.codehaus.jackson.JsonGenerationException;
//import org.codehaus.jackson.map.JsonMappingException;
//import org.codehaus.jackson.map.ObjectMapper;
//import org.junit.Test;
//
//import com.hengxin.platform.ebc.dto.EbcMobileVerifyRequest;
//import com.hengxin.platform.ebc.dto.EbcSignUpRequest;
//import com.hengxin.platform.ebc.dto.EbcSignUpResponse;
//import com.hengxin.platform.ebc.util.MD5;
//import com.hengxin.platform.escrow.chinapnr.constant.ParamConstant;
//import com.hengxin.platform.escrow.chinapnr.dto.RechargeRequest;
//import com.hengxin.platform.escrow.chinapnr.util.ParamUtil;
//import com.hengxin.platform.escrow.dto.CommandRequest;
//import com.hengxin.platform.escrow.dto.CommandResponse;
//import com.hengxin.platform.fund.util.DateUtils;
//import com.yst.m2.sdk.util.JsonUtil;
//
//public class ChinapnrTest {
//
//	public class A implements Serializable {
//
//		private static final long serialVersionUID = 1L;
//
//		public A() {
//			super();
//		}
//
//		public String usertype = "0";
//	};
//
//	@Test
//	public void httpParamTest() {
//		RechargeRequest request = new RechargeRequest();
//		request.setVersion(ParamConstant.VERSION_10);
//		request.setCmdId("NetSave");
//		request.setMerCustId("ht");
//		request.setUsrCustId("winry");
//		request.setOrdId("001");
//		request.setOrdDate(DateUtils.formatDate(new Date(), "yyyyMMdd"));
//		request.setGateBusiId("B2C");
//		Map<String, String> params = ParamUtil.writeParams(request);
//		for (String s : params.keySet()) {
//			System.out.println(s + ":" + params.get(s));
//		}
//	}
//
//	@Test
//	public void jsonTest() throws JsonGenerationException,
//			JsonMappingException, IOException {
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("usertype", "0");
//		System.out.print(JsonUtil.to_json(map));
//		A a = new A();
//		ObjectMapper mapper = new ObjectMapper();
//		System.out.print(mapper.writeValueAsString(a));
//	}
//
//	@Test
//	public void signUpTest() {
//		CommandRequest request = buildSignUpRequest();
//		EbcSignUpResponse response = (EbcSignUpResponse) request.execute();
//		fail(response);
//	}
//
//	private EbcSignUpRequest buildSignUpRequest() {
//		EbcSignUpRequest request = new EbcSignUpRequest();
//		request.setMerchNo("000000000000000");
//		request.setOrderSn(System.currentTimeMillis() + "");
//		request.setOwnerId("EBC");
//		request.setEnterName("winry");
//		request.setLoginType("5");
//		request.setUserType("1");
//		request.setUserName("cong");
//		request.setIdType("00");
//		request.setIdCard("110101201401015659");
//		request.setMobile("18658831602");
//		request.setPayPass(MD5.md5("580685"));
//		request.setWltNo("000001");
//		request.setCurrency("CNY");
//		return request;
//	}
//
//	private void fail(Object o) {
//		System.out.println(o);
//	}
//
//	@Test
//	public void smsTest() {
//		CommandRequest request = buildEbcMobileVerifyRequest();
//		CommandResponse response = request.execute();
//		fail(response);
//	}
//
//	private CommandRequest buildEbcMobileVerifyRequest() {
//		EbcMobileVerifyRequest request = new EbcMobileVerifyRequest();
//		request.setMerchNo("000000000000000");
//		request.setOrderSn(System.currentTimeMillis() + "");
//		request.setOwnerId("EBC");
//		request.setUserNo("ff4f50b79d094a1485794713e9f008e3");
//		request.setMediumNo("0100980002605529");
//		request.setMobile("18658831602");
//		request.setSmsModel("m");
//		return request;
//	}
//
// }
