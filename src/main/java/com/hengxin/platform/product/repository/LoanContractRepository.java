package com.hengxin.platform.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hengxin.platform.product.domain.LoanContract;

public interface LoanContractRepository extends
		JpaRepository<LoanContract, String> {

}
