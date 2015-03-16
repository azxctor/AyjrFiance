package com.hengxin.platform.risk.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.hengxin.platform.risk.domain.InvestCountEvalPo;

@Repository
public interface InvestCountEvalRepository extends
		PagingAndSortingRepository<InvestCountEvalPo, String>,
		JpaSpecificationExecutor<InvestCountEvalPo> {
	
	@Query(value = "SELECT * FROM (SELECT NVL(BEAR_SCORE, 0.00) FROM RK_INVS_COUNT_EVAL WHERE USER_ID = ?1 ORDER BY CREATE_TS DESC) WHERE rownum = 1", nativeQuery = true)
	public BigDecimal getAvgInvsCountsRiskBearScoreByUserIdAndyyyyMM(String userId);
	
}
