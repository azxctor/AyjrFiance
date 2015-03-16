package com.hengxin.platform.fund.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.hengxin.platform.fund.entity.UserAcctFreezeDtlView;

@Repository
public interface UserAcctFreezeDtlViewRepository extends PagingAndSortingRepository<UserAcctFreezeDtlView, String>,
        JpaSpecificationExecutor<UserAcctFreezeDtlView> {
}
