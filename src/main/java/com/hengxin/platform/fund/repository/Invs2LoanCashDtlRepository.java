package com.hengxin.platform.fund.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.hengxin.platform.fund.entity.Invs2LoanCashDtlPo;

@Repository
public interface Invs2LoanCashDtlRepository extends
		PagingAndSortingRepository<Invs2LoanCashDtlPo, String>,
			JpaSpecificationExecutor<Invs2LoanCashDtlPo> {

}
