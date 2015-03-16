package com.hengxin.platform.fund.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.hengxin.platform.fund.entity.FeePayStatePo;

@Repository
public interface FeePayStateRepository extends PagingAndSortingRepository<FeePayStatePo,String>,JpaSpecificationExecutor<FeePayStatePo> {

	FeePayStatePo findByUserIdAndFeeType(String userId, String feeType);
}
