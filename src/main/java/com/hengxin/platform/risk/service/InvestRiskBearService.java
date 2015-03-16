package com.hengxin.platform.risk.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.fund.util.AmtUtils;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.risk.enums.ERiskBearLevel;
import com.hengxin.platform.risk.repository.InvestAssetsEvalRepository;
import com.hengxin.platform.risk.repository.InvestCountEvalRepository;
import com.hengxin.platform.risk.repository.InvestQuestionEvalRepository;
import com.hengxin.platform.risk.repository.InvestRiskStatisticsRepository;

/**
 * InvestRiskBearService.
 *
 */
@Service
public class InvestRiskBearService {
	
	private static final BigDecimal ASSETS_WEIGHT_VALUE = BigDecimal.valueOf(0.5);
	private static final BigDecimal QUESTIONNAIRE_WEIGHT_VALUE = BigDecimal.valueOf(0.3);
	private static final BigDecimal INVSCOUNT_WEIGHT_VALUE = BigDecimal.valueOf(0.2);
	private static final BigDecimal BOUNDARYLEVEL1_VALUE = BigDecimal.valueOf(1.7);
	private static final BigDecimal BOUNDARYLEVEL2_VALUE = BigDecimal.valueOf(2.4);
	private static final Integer INVS_FRESH_COUNT_VALUE = Integer.valueOf(2);
	
	// cache veteran invs count more than 2 times
	private static final Map<String, Integer> VETERAN_INVS_COUNT_CACHE = new ConcurrentHashMap<String, Integer>();
	// cache user risk eval count, more than zero
	private static final Map<String, Integer> USER_EVAL_COUNT_CACHE = new ConcurrentHashMap<String, Integer>();
	
	@Autowired
	private InvestAssetsEvalRepository investAssetsEvalRepository;
	@Autowired
	private InvestCountEvalRepository investCountEvalRepository;
	@Autowired
	private InvestQuestionEvalRepository investQuestionEvalRepository;
	@Autowired
	private InvestRiskStatisticsRepository investRiskStatisticsRepository;
	@Autowired
	private InvestorQuestionnaireService investorQuestionnaireService;
	
	/**
	 * 获取会员风险承受等级.
	 * @param userId
	 * @return
	 */
	@Transactional(readOnly = true)
	public ERiskBearLevel getUserRiskBearLevel(String userId) {
		if (isRookieInverstor(userId)) {
			return ERiskBearLevel.ROOKIE;
		}
		if (!hasQuestionnaire(userId)) {
			// 针对尚未参与评级的会员
			return ERiskBearLevel.CAUTIOUS;
		}
		Date workDate = CommonBusinessUtil.getCurrentWorkDate();
		BigDecimal totalRiskBearScore = getUserTotalRiskBearScore(userId, workDate);
		if (totalRiskBearScore.compareTo(BOUNDARYLEVEL1_VALUE) < 0) {
			return ERiskBearLevel.CAUTIOUS;
		} else if (totalRiskBearScore.compareTo(BOUNDARYLEVEL2_VALUE) >= 0) {
			return ERiskBearLevel.AGGRESSIVE;
		}
		return ERiskBearLevel.STEADY;
	}
	
	// 风险承受力总评分 = 资产评分×0.5+风险测评评分×0.3+投资次数评分×0.2;
	private BigDecimal getUserTotalRiskBearScore(String userId, Date workDate) {
		//Date lastMonth = DateUtils.add(workDate, Calendar.DAY_OF_MONTH, -1);
		String yyyy = DateUtils.formatDate(workDate, "yyyy");
		//String yyyyMM = DateUtils.formatDate(lastMonth, "yyyyMM");
		BigDecimal assetsBearScore = investAssetsEvalRepository.getAvgAssetsRiskBearScoreByUserIdAndyyyyMM(userId);
		assetsBearScore = AmtUtils.processNullAmt(assetsBearScore, BigDecimal.ZERO);
		BigDecimal quesBearScore = investQuestionEvalRepository.getAvgQuesEvalRiskBearScoreByUserIdAndyyyy(userId, yyyy);
		quesBearScore = AmtUtils.processNullAmt(quesBearScore, BigDecimal.ZERO);
		BigDecimal invsCountBearScore = investCountEvalRepository.getAvgInvsCountsRiskBearScoreByUserIdAndyyyyMM(userId);
		invsCountBearScore = AmtUtils.processNullAmt(invsCountBearScore, BigDecimal.ZERO);
//		List<Object[]> objList = investAssetsEvalRepository.getAvgBearScore(userId, yyyy, yyyyMM);
//		Object[] objs = objList.get(0);
//		BigDecimal assetsBearScore = AmtUtils.processNullAmt(objs[0], BigDecimal.ZERO);
//		BigDecimal invsCountBearScore = AmtUtils.processNullAmt(objs[1], BigDecimal.ZERO);
//		BigDecimal quesBearScore = AmtUtils.processNullAmt(objs[2], BigDecimal.ZERO);
		BigDecimal value = BigDecimal.ZERO;
		value = value.add(assetsBearScore.multiply(ASSETS_WEIGHT_VALUE));
		value = value.add(quesBearScore.multiply(QUESTIONNAIRE_WEIGHT_VALUE));
		value = value.add(invsCountBearScore.multiply(INVSCOUNT_WEIGHT_VALUE));
		return value;
	}
	
	/**
	 * 获取会员投资次数.
	 * @param userId
	 * @return true if rookie.
	 */
	@Transactional(readOnly = true)
	public boolean isRookieInverstor(String userId) {
		boolean bool = false;
		Integer count = Integer.valueOf(0);
		if (!VETERAN_INVS_COUNT_CACHE.containsKey(userId)) {
			count = investRiskStatisticsRepository.getUserInvestCount(userId);
		} else {
			count = VETERAN_INVS_COUNT_CACHE.get(userId);
		}
		if (count.compareTo(INVS_FRESH_COUNT_VALUE)<0) {
			bool = true;
		}
		if (!VETERAN_INVS_COUNT_CACHE.containsKey(userId) && !bool) {
			VETERAN_INVS_COUNT_CACHE.put(userId, count);
		}
		return bool;
	}
	
	private boolean hasQuestionnaire(String userId) {
		return investorQuestionnaireService.hasQuestionnaire(userId);
	}
	
	/**
	 * 是否参加过日终评分.
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unused")
	private boolean hasEvalData(String userId) {
		boolean bool = false;
		Integer count = Integer.valueOf(0);
		if (!USER_EVAL_COUNT_CACHE.containsKey(userId)) {
			count = investAssetsEvalRepository.countEvalTotalAssetsTimes(userId);
		} else {
			count = USER_EVAL_COUNT_CACHE.get(userId);
		}
		bool = count.compareTo(Integer.valueOf(0))>0;
		if (!USER_EVAL_COUNT_CACHE.containsKey(userId) && bool) {
			USER_EVAL_COUNT_CACHE.put(userId, count);
		}
		return bool;
	}
	
	/**
	 * 获取会员总资产.
	 * @param userId
	 * @return
	 */
	public BigDecimal getUserTotalAssets(String userId) {
		BigDecimal totalAssets = investRiskStatisticsRepository.getUserTotalAssets(userId);
		totalAssets = AmtUtils.processNullAmt(totalAssets, BigDecimal.ZERO);
		return totalAssets;
	}
	
}
