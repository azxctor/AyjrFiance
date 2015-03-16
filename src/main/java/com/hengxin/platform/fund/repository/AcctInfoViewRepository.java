package com.hengxin.platform.fund.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.hengxin.platform.fund.entity.UserAcctInfoView;

@Repository
public interface AcctInfoViewRepository extends
		PagingAndSortingRepository<UserAcctInfoView, String>,
		JpaSpecificationExecutor<UserAcctInfoView> {
}
