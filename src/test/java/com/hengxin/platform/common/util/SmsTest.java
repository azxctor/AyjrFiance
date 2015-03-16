package com.hengxin.platform.common.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.util.EntityUtils;
import org.junit.Ignore;
import org.junit.Test;

import com.hengxin.platform.sms.service.MessageSendService;

public class SmsTest {

	@Ignore
	@Test
	public void testSMSFromService() {
		String mobile = "18606507727";
		String content = "UT欢迎注册小微金融交易平台, 您的手机验证码为:12345";
		MessageSendService service = new MessageSendService();
		service.sendRegisterSMS(mobile, content, null, new String[]{});
	}
	
	@Ignore
	@Test
	public void testSMS() {
		String user = "SDK-XWQY-0071";
		String pw = "GJ4ASF";
		String mobile = "15381134620";
//		String mobile = "18658831602";
//		String mobile = "15381134620";
//		String mobile = "13857177334";
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String d = df.format(new Date());
		String content = "Hello欢迎步入小薇金融1234!@ 验证码 :12345 date = " + d;
//		String content = "A啊1验证码1234";
		content = content.replace(" ", "%20");
		String urlStr = "http://124.173.70.59:8081/SmsAndMms/mg?Sn="+user+"&Pwd="+pw+"&mobile="+mobile+"&content="+content;
		System.out.println(urlStr);
		URL url;
		try {
			url = new URL(urlStr);
			URLConnection urlConnection = url.openConnection();
			HttpURLConnection httpConnection = (HttpURLConnection) urlConnection;
			httpConnection.setRequestMethod("POST");
			int responseCode = httpConnection.getResponseCode();
			System.out.println("responseCode = "+responseCode);
			if (responseCode == HttpURLConnection.HTTP_OK) {
				System.err.println("成功");
				InputStream urlStream = httpConnection.getInputStream();
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(urlStream));
				String sCurrentLine = "";
				String sTotalString = "";
				while ((sCurrentLine = bufferedReader.readLine()) != null) {
					sTotalString += sCurrentLine;
				}
				System.err.println(sTotalString);
				// 假设该url页面输出为"OK"
				if (sTotalString.equals("OK")) {
				} else {
				}
			} else {
				System.err.println("失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	@Ignore
	@Test
	public void testDate(){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String d = df.format(new Date());
		String content = "Hello欢迎步入小薇金融1234!@ 验证码 :12345 date = " + d;
		System.out.println("content : " + content);
	}

	
	@Test
	public void testSendMessage(){
		
		 String mobile = "15381134620";
		//9. String content = "【安益金融】您正在申请修改您的系统账户密码，验证码为：00000，有效期3分钟。非本人操作，请忽略本消息。";
		//8. String content = "【安益金融】您于2014/01/23，提现：80000元，已操作成功，实际到账时间根据银行处理时间而定，请注意查收。";
		//7. String content = "【安益金融】您于2014/09/12充值：充值2000元，已成功到账。";
		 //6.  String content = "【安益金融】您申购的融资项目：A001，已于2014/11/12发生逾期，我们已启动逾期处理机制，如有最新消息，会第一时间进行告知。";
	     //5.String content = "【安益金融】您申购的融资项目：A001，第2期已还款完成，本期应得总额：10000元，扣除交易手续费：20元，实得金额：8000元。";
	     //4.String content = "【安益金融】您申购的融资项目：A001，已成功签约，从2014-10-11开始进行计息，感谢您的支持！";
		//2.String content = "【安益金融】尊敬的用户，您已成功注册为安益金融会员，非常感谢您的支持。为了确保您的账户安全，请妥善保管您的账户和密码。";
		 String content = "【安益金融】您正在注册交易系统账户，验证码为：888,有效期3分钟。如非本人操作，请忽略本消息。";
			
		if (mobile == null || mobile.isEmpty() || mobile.equals("88888888888")) {
			//return false;
		}
    	String smsUrl = "http://api.weimi.cc/2/sms/send.html?uid={0}&pas={1}&mob={2}&con={3}";
    	String userName = "g1s9qEh4WCi4";
        String passWord = "xkq3ryxh";
		try {
			content = java.net.URLEncoder.encode(content, "utf8");
	        content = content.replace(" ", "%20");
	        Object[] objects = new Object[]{userName, passWord, mobile, content};
	        smsUrl = MessageFormat.format(smsUrl, objects);
			//LOGGER.info("SMS URL {} ", smsUrl);
			HttpClient client = HttpClientBuilder.create()
					.setRedirectStrategy(new LaxRedirectStrategy()).build();
			HttpGet req = new HttpGet(smsUrl);
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(3000).setConnectTimeout(3000).build();
			req.setConfig(requestConfig);
			HttpResponse resp = client.execute(req);
			//LOGGER.debug("SMS status: {}", resp.getStatusLine().getStatusCode());
			if (HttpStatus.SC_OK == resp.getStatusLine().getStatusCode()) {
				//LOGGER.debug("SMS response: {}",
						//EntityUtils.toString(resp.getEntity()));
				System.out.println(EntityUtils.toString(resp.getEntity()));
			} else {
				//return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			//LOGGER.error("SMS failed : {}", e);
		//	return false;
		}
    	//return true;
	}
	
	@Test
	public void testSendMobileMessage(){
		
		String mobile = "15068748061";
		/*1.String cid = "tegHVNcir549";
		String yzm = "1234";*/
		//2.String cid = "JWXaaRZ80J9t";
		/*  3.
		String cid = "LPamWd1KxC9D";
		String projectID = "A001";
		String purchaseAmount = "6000";*/
		/*4.
		String cid = "NQjiPgAUovcW";
		String projectID = "2001";
		String signingDate = "2014/12/11";*/
		/*5.
		String cid = "0zGvIh6rpsHj";
		String projectID = "B2001";
		String num = "二";
		int totalAmount = 6200;
		int handlingCharge = 1200; 
		int actualAmount = 5000;*/
		/*6.
		String cid = "8EVNWr2eZis9";
		String projectID = "C3001";
		String shouldReturnDate = "2014/11/23";*/
		/*7.
		String cid = "terSpxkKqoEY";
		String rechargeDate = "2014/11/03";
		String rechargeAmount = "80";*/
		/*8.
		String cid = "1Q1ZeOKYuR0F";
		String withdrawDate = "2014/12/03";
		String withdrawAmount = "50";*/
		
		String cid = "aSaURxTYniKl";
		String yzm = "456";
		if (mobile == null || mobile.isEmpty() || mobile.equals("88888888888")) {
			//return false;
		}
		//2.
		//String smsUrl = "http://api.weimi.cc/2/sms/send.html?uid={0}&pas={1}&mob={2}&cid={3}";
		//1.9.
		String smsUrl = "http://api.weimi.cc/2/sms/send.html?uid={0}&pas={1}&mob={2}&cid={3}&p1={4}";
    	//3.4.6.7.8.
		//String smsUrl = "http://api.weimi.cc/2/sms/send.html?uid={0}&pas={1}&mob={2}&cid={3}&p1={4}&p2={5}";
		//5.
		//String smsUrl = "http://api.weimi.cc/2/sms/send.html?uid={0}&pas={1}&mob={2}&cid={3}&p1={4}&p2={5}&p3={6}&p4={7}&p5={8}";
    	String userName = "g1s9qEh4WCi4";
        String passWord = "xkq3ryxh";
		try {
			//1.Object[] objects = new Object[]{userName, passWord, mobile, cid, yzm};
			//2.Object[] objects = new Object[]{userName, passWord, mobile, cid};
			//3.Object[] objects = new Object[]{userName, passWord, mobile, cid, projectID, purchaseAmount};
			//4.Object[] objects = new Object[]{userName, passWord, mobile, cid, projectID, signingDate};
			//5.Object[] objects = new Object[]{userName, passWord, mobile, cid, projectID, num, totalAmount, handlingCharge, actualAmount};
			//6.Object[] objects = new Object[]{userName, passWord, mobile, cid, projectID, shouldReturnDate};
			//7.Object[] objects = new Object[]{userName, passWord, mobile, cid, rechargeDate, rechargeAmount};
			//8.Object[] objects = new Object[]{userName, passWord, mobile, cid, withdrawDate, withdrawAmount};
			Object[] objects = new Object[]{userName, passWord, mobile, cid, yzm};
			smsUrl = MessageFormat.format(smsUrl, objects);
	        //LOGGER.info("SMS URL {} ", smsUrl);
			HttpClient client = HttpClientBuilder.create()
					.setRedirectStrategy(new LaxRedirectStrategy()).build();
			HttpGet req = new HttpGet(smsUrl);
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(3000).setConnectTimeout(3000).build();
			req.setConfig(requestConfig);
			HttpResponse resp = client.execute(req);
			//LOGGER.debug("SMS status: {}", resp.getStatusLine().getStatusCode());
			if (HttpStatus.SC_OK == resp.getStatusLine().getStatusCode()) {
				//LOGGER.debug("SMS response: {}",
						//EntityUtils.toString(resp.getEntity()));
			} else {
				//return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			//LOGGER.error("SMS failed : {}", e);
		//	return false;
		}
    	//return true;
	}
}
