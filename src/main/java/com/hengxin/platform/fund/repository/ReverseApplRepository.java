package com.hengxin.platform.fund.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.hengxin.platform.fund.entity.ReverseApplPo;
import com.hengxin.platform.fund.enums.EFundApplStatus;

@Repository
public interface ReverseApplRepository extends PagingAndSortingRepository<ReverseApplPo, String>,
	JpaSpecificationExecutor<ReverseApplPo> {
	
	List<ReverseApplPo> findByRvsJnlNo(String rvsJnlNo);
	
	List<ReverseApplPo> findByRvsJnlNoAndApplStatusIn(String rvsJnlNo, List<EFundApplStatus> applStatus);
	
}
