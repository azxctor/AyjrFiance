package com.hengxin.platform.risk.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.risk.domain.InvestQuestionEvalPo;
import com.hengxin.platform.risk.repository.InvestQuestionEvalRepository;
import com.hengxin.platform.security.SecurityContext;

@Service
public class InvestorQuestionnaireService {
	
    private static final int saveScale = 2;
    
    private static final BigDecimal boundaryLevel1 = BigDecimal.valueOf(60); 
    
    private static final BigDecimal boundaryLevel2 = BigDecimal.valueOf(75); 

    @Autowired
    private SecurityContext securityContext;

	@Autowired
	private InvestQuestionEvalRepository investQuestionEvalRepository;
	
	@Transactional
	public void doQuestionnaire(String userId, BigDecimal score){
		Date workDate = CommonBusinessUtil.getCurrentWorkDate();
		String currUserId = securityContext.getCurrentUserId();
		BigDecimal quesScore = score.setScale(saveScale, RoundingMode.HALF_UP);
		BigDecimal bearScore = convertQuestionScoreToRiskBearScore(quesScore);
		InvestQuestionEvalPo ques = new InvestQuestionEvalPo();
		ques.setUserId(userId);
		ques.setWorkDate(workDate);
		ques.setQuesScore(quesScore);
		ques.setBearScore(bearScore);
		ques.setCreateOpid(currUserId);
		ques.setCreateTs(new Date());
		investQuestionEvalRepository.save(ques);
	}
	
	/**
	 * 
	 * @param userId
	 * @return true if user has done questionnaire.
	 */
	@Transactional(readOnly = true)
	public boolean hasQuestionnaire(String userId) {
		boolean bool = false;
		Date workDate = CommonBusinessUtil.getCurrentWorkDate();
		String year = DateUtils.formatDate(workDate, "yyyy");
		Integer count = investQuestionEvalRepository.countByUserIdAndyyyy(userId, year);
		if(count.intValue()>0){
			bool = true;
		}
		return bool;
	}
	
	private BigDecimal convertQuestionScoreToRiskBearScore(BigDecimal quesScore){
		if(boundaryLevel1.compareTo(quesScore)>0){
			return BigDecimal.ONE;
		}else if(boundaryLevel2.compareTo(quesScore)<=0){
			return BigDecimal.valueOf(3);
		}
		return BigDecimal.valueOf(2);
	}
	
	public static void main(String[] args){
		
		Date workDate = DateUtils.getDate("2014-09-16", "yyyy-MM-dd");
		String year = DateUtils.formatDate(workDate, "yyyy");
		System.out.println(year);
		
	}
	
}
