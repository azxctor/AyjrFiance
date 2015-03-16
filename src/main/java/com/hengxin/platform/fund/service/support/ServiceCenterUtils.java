package com.hengxin.platform.fund.service.support;

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

import com.hengxin.platform.common.util.ApplicationContextUtil;
import com.hengxin.platform.member.domain.InvestorInfo;
import com.hengxin.platform.member.repository.InvestorInfoRepository;

public class ServiceCenterUtils {
	
	private final static ReentrantLock lock = new ReentrantLock();

	private final static ThreadLocal<HashMap<String, String>> threadLocal = new ThreadLocal<HashMap<String, String>>();

	public static String getAuthzdCtrIdByUserId(String userId) {
		return getAuthzdCtrId(userId);
	}

	private static InvestorInfo getInvestorInfo(String userId) {
		return ApplicationContextUtil.getBean(InvestorInfoRepository.class)
				.findByUserId(userId);
	}

	private static String getAuthzdCtrId(String userId) {
		String value = null;
		if (existsCache(userId)) {
			value = getDataMap().get(userId);
		} else {
			InvestorInfo invsInfo = getInvestorInfo(userId);
			if (invsInfo != null) {
				value = invsInfo.getAuthCenterId();
			}
			putCache(userId, value);
		}
		return value;
	}

	private static boolean existsCache(String key) {
		return getDataMap().containsKey(key);
	}

	private static HashMap<String, String> getDataMap() {
		HashMap<String, String> dataMap = threadLocal.get();
		if (dataMap == null) {
			try{
				lock.lock();
				dataMap = new HashMap<String, String>();
				threadLocal.set(dataMap);
			}finally{
				lock.unlock();
			}
		}
		return dataMap;
	}

	private static void putCache(String userId, String authzdCtrId) {
		HashMap<String, String> dataMap = getDataMap();
		try{
			lock.lock();
			dataMap.put(userId, authzdCtrId);
			threadLocal.set(dataMap);
		}finally{
			lock.unlock();
		}
	}

}
