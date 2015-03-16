package com.hengxin.platform.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.hengxin.platform.report.domain.AuthzdCtrTransFee;

/**
 * @author qimingzou
 * 
 */
public interface AuthzdCtrTransFeeRepository extends JpaRepository<AuthzdCtrTransFee, String>,
		JpaSpecificationExecutor<AuthzdCtrTransFee> {
}
