package com.hengxin.platform.report.service;


import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hengxin.platform.report.dto.PlatformOperDataDto;
import com.hengxin.platform.report.repository.PlatformOperDataRepository;
import com.hengxin.platform.security.SecurityContext;

@Service
public class PlatformOperDataService {

	@SuppressWarnings("unused")
	private final static Logger LOGGER = LoggerFactory.getLogger(PlatformOperDataService.class);
    
	@Autowired
    private SecurityContext securityContext;
	
	@Autowired
    private PlatformOperDataRepository reposity;

    @Transactional
	public PlatformOperDataDto getQuarterOperData(String searchDate ) {
    	return reposity.getQuarterOperData(searchDate);
	}
}
