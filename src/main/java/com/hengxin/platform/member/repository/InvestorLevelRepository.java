package com.hengxin.platform.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.hengxin.platform.member.domain.InvestorLevel;

public interface InvestorLevelRepository extends JpaRepository<InvestorLevel, String>,
	JpaSpecificationExecutor<InvestorLevel> {

}
