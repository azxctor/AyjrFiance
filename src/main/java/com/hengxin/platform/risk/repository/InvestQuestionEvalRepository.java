package com.hengxin.platform.risk.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.hengxin.platform.risk.domain.InvestQuestionEvalPo;

@Repository
public interface InvestQuestionEvalRepository extends
		PagingAndSortingRepository<InvestQuestionEvalPo, String>,
		JpaSpecificationExecutor<InvestQuestionEvalPo> {
	
	@Query(value="SELECT COUNT(1) FROM RK_INVS_QUES_EVAL WHERE USER_ID = ?1 AND TO_CHAR(WORK_DT,'yyyy') = ?2", nativeQuery = true)
	public Integer countByUserIdAndyyyy(String userId, String yyyy);
		
	@Query(value = "SELECT NVL(AVG(BEAR_SCORE),0.00) FROM RK_INVS_QUES_EVAL WHERE USER_ID = ?1 AND TO_CHAR(WORK_DT,'yyyy') = ?2", nativeQuery = true)
	public BigDecimal getAvgQuesEvalRiskBearScoreByUserIdAndyyyy(String userId, String yyyy);
	
	
}
