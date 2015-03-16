package com.hengxin.platform.sms.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;

public class HttpClientUtils {

	public static HttpResponse httpGetExecute(String smsUrl) {
		HttpClient client = HttpClientBuilder.create()
				.setRedirectStrategy(new LaxRedirectStrategy()).build();
		HttpGet req = new HttpGet(smsUrl);
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(3000).setConnectTimeout(3000).build();
		req.setConfig(requestConfig);
		HttpResponse resp = null;
		try {
			resp = client.execute(req);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	public static HttpResponse httpPostExecute(String smsUrl) {
		HttpClient client = HttpClientBuilder.create()
				.setRedirectStrategy(new LaxRedirectStrategy()).build();
		HttpGet req = new HttpGet(smsUrl);
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(3000).setConnectTimeout(3000).build();
		req.setConfig(requestConfig);
		HttpResponse resp = null;
		try {
			resp = client.execute(req);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

}
