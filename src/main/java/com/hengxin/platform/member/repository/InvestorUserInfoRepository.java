package com.hengxin.platform.member.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.hengxin.platform.member.domain.InvestorUserInfo;

public interface InvestorUserInfoRepository extends JpaRepository<InvestorUserInfo, String>,
		JpaSpecificationExecutor<InvestorUserInfo> {

    @Query("SELECT i FROM InvestorUserInfo i INNER JOIN FETCH i.user INNER JOIN FETCH i.authorizedCenter")
    List<InvestorUserInfo> findAllInvestorInfo();
    
}
