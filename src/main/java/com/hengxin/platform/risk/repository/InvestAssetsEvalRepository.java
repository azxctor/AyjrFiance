package com.hengxin.platform.risk.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.hengxin.platform.risk.domain.InvestAssetsEvalPo;

@Repository
public interface InvestAssetsEvalRepository extends
		PagingAndSortingRepository<InvestAssetsEvalPo, String>,
		JpaSpecificationExecutor<InvestAssetsEvalPo> {
	
	//@Query(value = "SELECT NVL(AVG(BEAR_SCORE),0.00) FROM RK_INVS_ASSETS_EVAL WHERE USER_ID = ?1 AND TO_CHAR(WORK_DT,'yyyyMM') = ?2", nativeQuery = true)
	@Query(value = "SELECT * FROM (SELECT NVL(BEAR_SCORE, 0.00) FROM RK_INVS_ASSETS_EVAL WHERE USER_ID = ?1 ORDER BY CREATE_TS DESC) WHERE rownum = 1", nativeQuery = true)
	public BigDecimal getAvgAssetsRiskBearScoreByUserIdAndyyyyMM(String userId);
	
	@Query(value = " SELECT "
			     + " (SELECT NVL(AVG(BEAR_SCORE),0.00) FROM RK_INVS_ASSETS_EVAL WHERE USER_ID = ?1 AND TO_CHAR(WORK_DT,'yyyyMM') = ?3) AS ASSETS_BEAR_SCORE,"
			     + " (SELECT NVL(AVG(BEAR_SCORE),0.00) FROM RK_INVS_COUNT_EVAL WHERE USER_ID = ?1 AND TO_CHAR(WORK_DT,'yyyyMM') = ?3) AS INVSCOUNT_BEAR_SCORE,"
			     + " (SELECT NVL(AVG(BEAR_SCORE),0.00) FROM RK_INVS_QUES_EVAL WHERE USER_ID = ?1 AND TO_CHAR(WORK_DT,'yyyy') = ?2) AS QUES_BEAR_SCORE "
			     + " FROM DUAL", nativeQuery = true)
	public List<Object[]> getAvgBearScore(String userId, String yyyy, String yyyyMM);
	
	@Query(value= "SELECT COUNT(1) FROM RK_INVS_ASSETS_EVAL WHERE USER_ID = ?1", nativeQuery = true)
	public Integer countEvalTotalAssetsTimes(String userId);
	
}
