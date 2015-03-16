package com.hengxin.platform.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hengxin.platform.member.domain.UserCompanyView;
import com.hengxin.platform.member.repository.UserCompanyViewRepository;

@Service
public class UserCompanyViewService {

    @Autowired
    private UserCompanyViewRepository userCompanyViewRepository;

    public UserCompanyView getUserCompany(final String userId){
    	return this.userCompanyViewRepository.getUserCompany(userId);
    }
    
}
