package com.hengxin.platform.common.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.domain.BusinessSigninStatus;
import com.hengxin.platform.common.enums.ESystemParam;
import com.hengxin.platform.common.repository.BusinessSigninStatusRepository;
import com.hengxin.platform.common.repository.CurrentWorkDateRepository;
import com.hengxin.platform.common.repository.SystemParamRepository;
import com.hengxin.platform.common.util.CacheUtil;
import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.member.domain.InvestorLevel;
import com.hengxin.platform.member.dto.InvestorLevelDto;
import com.hengxin.platform.member.enums.EInvestorLevel;
import com.hengxin.platform.member.repository.InvestorLevelRepository;
import com.hengxin.platform.member.repository.UserInvestRuleRepository;

/**
 * Provides common business oriented utility.
 * 
 * @author yeliangjin
 * 
 */
@Service
@Transactional
public class CommonBusinessService {

	@Autowired
	transient CurrentWorkDateRepository currentWorkDateRepository;
	@Autowired
	transient SystemParamRepository systemParamRepository;
	@Autowired
	transient UserInvestRuleRepository userInvestRuleRepository;
	@Autowired
	transient BusinessSigninStatusRepository businessSigninStatusRepository;
	@Autowired
	transient InvestorLevelRepository investorLevelRepository;

	/**
	 * Returns the current work date of the system.
	 */
	@Cacheable(value = CacheUtil.CACHE_NAME, key = "'CurrentWorkDate'", condition = "T(com.hengxin.platform.common.util.CacheUtil).isCacheEnabled()")
	public Date getCurrentWorkDate() {
		return currentWorkDateRepository.findAll().get(0).getCurrentWorkDate();
	}

	/**
	 * Returns the value of given system parameter.
	 */
	@Cacheable(value = CacheUtil.CACHE_NAME, key = "'SystemParam_' + #param")
	public String getSystemParam(ESystemParam param) {
		return systemParamRepository.findOne(param.name()).getParamValue();
	}

	/**
	 * Returns the maximum percentage that a user with given investor level can
	 * subscribe for a single financing package.
	 */
	@Cacheable(value = CacheUtil.CACHE_NAME, key = "'MaxSubscribeRate_' + #investorLevel")
	public BigDecimal getMaxSubscribeRate(String investorLevel) {
		return userInvestRuleRepository.findOne(investorLevel)
				.getMaxSubscribeRate();
	}

	/**
	 * Refresh cache.
	 */
	@CacheEvict(value = CacheUtil.CACHE_NAME, allEntries = true)
	public void refresh() {

	}

	/**
	 * Returns business signin status for target business type.
	 */
	@Cacheable(value = CacheUtil.CACHE_NAME, key = "'BusinessSigninStatus_' + #type")
	public String getBusinessSigninStatus(String type) {
		BusinessSigninStatus bs = businessSigninStatusRepository.findOne(type);
		return bs == null ? null : bs.getStatus();
	}
	
	@Cacheable(value = CacheUtil.CACHE_NAME, key = "'InvestorLevel'")
	public Map<String, InvestorLevelDto> getInvestorLevelInfo() {
		List<InvestorLevel> list = investorLevelRepository.findAll();
		Map<String, InvestorLevelDto> map = new HashMap<String, InvestorLevelDto>();
		for (InvestorLevel investorLevel : list) {
			if (!map.containsKey(investorLevel.getLevelId())) {
				InvestorLevelDto dto = new InvestorLevelDto();
				dto.setLevel(EnumHelper.translate(EInvestorLevel.class, investorLevel.getLevelId()));
				dto.setRate(investorLevel.getFeeDiscountRate());
				map.put(investorLevel.getLevelId(), dto);
			}
		}
		return map;
	}
}
