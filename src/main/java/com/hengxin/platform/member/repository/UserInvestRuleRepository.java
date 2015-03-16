package com.hengxin.platform.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hengxin.platform.member.domain.UserInvestRule;

public interface UserInvestRuleRepository extends
		JpaRepository<UserInvestRule, String> {

}
