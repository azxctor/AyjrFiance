package com.hengxin.platform.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hengxin.platform.product.domain.ContractRate;
import com.hengxin.platform.product.domain.ContractRatePK;

public interface ContractRateRepository extends JpaRepository<ContractRate, ContractRatePK>,
		JpaSpecificationExecutor<ContractRate>, PagingAndSortingRepository<ContractRate, ContractRatePK> {

}
