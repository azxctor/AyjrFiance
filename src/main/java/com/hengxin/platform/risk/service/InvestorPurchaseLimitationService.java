package com.hengxin.platform.risk.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.common.util.MessageUtil;
import com.hengxin.platform.fund.service.FundAcctBalService;
import com.hengxin.platform.market.utils.SubscribeUtils;
import com.hengxin.platform.member.domain.InvestorInfo;
import com.hengxin.platform.member.repository.InvestorInfoRepository;
import com.hengxin.platform.product.domain.PackageSubscribes;
import com.hengxin.platform.product.domain.Product;
import com.hengxin.platform.product.domain.ProductPackage;
import com.hengxin.platform.product.enums.ERiskLevel;
import com.hengxin.platform.product.repository.PackageSubscribesRepository;
import com.hengxin.platform.product.repository.ProductPackageRepository;
import com.hengxin.platform.risk.dto.PurchaseRiskResultDto;
import com.hengxin.platform.risk.enums.ERiskBearLevel;
import com.hengxin.platform.risk.repository.InvestRiskStatisticsRepository;

/**
 * 风险分散机制体系.<br/>
 * 用于确定投资人能否购买融资包, 以及可以购买融资包的上下限.
 *
 */
@Service
public class InvestorPurchaseLimitationService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InvestorPurchaseLimitationService.class);
	
	private static final String ERROR_SUBSCRIBE_OVERFLOW2   = "market.error.subscribe.amount.upperlimit2";
	private static final String ERROR_SUBSCRIBE_LACK_ORIGINAL = "market.error.balance.lack";
	private static final String ERROR_SUBSCRIBE_LACK        = "market.error.subscribe.amount.lack";
	private static final String ERROR_SUBSCRIBE_OVERFLOW    = "market.error.subscribe.amount.overflow";
	private static final String ERROR_SUBSCRIBE_UNQUALIFIED = "market.error.userlevel.unqualified";
	
	private static final Map<ERiskBearLevel, Map<ERiskLevel, BigDecimal>> MATRIX_CATEGORY
		= new HashMap<ERiskBearLevel, Map<ERiskLevel, BigDecimal>>();
	private static final Map<ERiskBearLevel, Map<ERiskLevel, BigDecimal>> MATRIX_PROJECT
		= new HashMap<ERiskBearLevel, Map<ERiskLevel, BigDecimal>>();
	private static final Map<ERiskBearLevel, Map<ERiskLevel, BigDecimal>> MATRIX_PACKAGE
		= new HashMap<ERiskBearLevel, Map<ERiskLevel, BigDecimal>>();
	
	@Autowired
	private InvestRiskBearService investRiskBearService;
	
	@Autowired
	private ProductPackageRepository productPackageRepository;
	
	@Autowired
	private PackageSubscribesRepository subcribeRepository;
	
	@Autowired
	private InvestRiskStatisticsRepository investRiskStatisticsRepository;
	
    @Autowired
    private transient InvestorInfoRepository investorRepository;
    
    @Autowired
    private transient FundAcctBalService fundAccoutService;
    
    /**
     * 每个包最大申购用户数量
     */
    private BigDecimal getMaxUserPerPkg(){
    	return SubscribeUtils.getPerPkgMaxSubsUserCount();
    }
    /**
     * 申购单元金额
     */
    private BigDecimal getUnitFaceValue(){
    	return SubscribeUtils.getUnitFaceValue();
    }
	
	/**
	 * 同一风险类型项目上限 : 总资产  * 比例.
	 * @param userId
	 * @param projectLevel
	 * @return
	 */
	private BigDecimal getUpperLimitationForAcrossProjects(
			String userId, ERiskBearLevel userLevel, ERiskLevel productRiskLevel) {
		LOGGER.info("getUpperLimitationForAcrossProjects() user level {}, product level {}",
				userLevel.getCode(), productRiskLevel.getCode());
		if (ERiskBearLevel.ROOKIE == userLevel) {
			return BigDecimal.ZERO;
		}
		BigDecimal totalAssets = this.investRiskBearService.getUserTotalAssets(userId);
		BigDecimal rate = getMatrixCategory().get(userLevel).get(productRiskLevel);
		BigDecimal result = totalAssets.multiply(rate);
		LOGGER.info("Single Across projects Total assets {}, rate {}, limit {}",
				new Object[]{totalAssets, rate, result});
		return result;
	}
	
	/**
	 * 10% total assets for high risk user & project.
	 * @param userId
	 * @return
	 */
	private BigDecimal getUpperLimitationForAcrossProjectsTemp(String userId) {
		LOGGER.info("getUpperLimitationForAcrossProjectsTemp()");
		BigDecimal totalAssets = this.investRiskBearService.getUserTotalAssets(userId);
		BigDecimal rate = BigDecimal.valueOf(0.10);
		BigDecimal result = totalAssets.multiply(rate);
		LOGGER.info("Single Across projects Total assets {}, rate {}, limit {}",
				new Object[]{totalAssets, rate, result});
		return result;
	}
	
	/**
	 * 单个项目上限 : 总资产 * 比例.
	 * @param userId
	 * @param projectLevel
	 * @return
	 */
	private BigDecimal getUpperLimitationForAcrossPackages(
			String userId, ERiskBearLevel userLevel, ERiskLevel productRiskLevel) {
		LOGGER.info("getUpperLimitationForAcrossPackages() user level {}, product level {}",
				userLevel.getCode(), productRiskLevel.getCode());
		if (ERiskBearLevel.ROOKIE == userLevel) {
			if (ERiskLevel.HIGH_RISK == productRiskLevel) {
				return BigDecimal.ZERO;
			}
			return SubscribeUtils.fiveUnitFaceValue();
		}
		BigDecimal totalAssets = this.investRiskBearService.getUserTotalAssets(userId);
		BigDecimal rate = getMatrixProject().get(userLevel).get(productRiskLevel);
		BigDecimal result = totalAssets.multiply(rate);
		LOGGER.info("Single Across packages Total assets {}, rate {}, limit {}",
				new Object[]{totalAssets, rate, result});
		return result;
	}
	
	/**
	 * 单个融资包上限 : 融资包金额 * 比例.
	 * @param packageId
	 * @return
	 */
	private BigDecimal getUpperLimitationForSinglePackage(
			String packageId, ERiskBearLevel userLevel, ERiskLevel productRiskLevel) {
		LOGGER.info("getUpperLimitationForSinglePackage() user level {}, product level {}",
				userLevel.getCode(), productRiskLevel.getCode());
		if (ERiskBearLevel.ROOKIE == userLevel) {
			if (ERiskLevel.HIGH_RISK == productRiskLevel) {
				return BigDecimal.ZERO;
			}
			return SubscribeUtils.fiveUnitFaceValue();
		}
		ProductPackage p = productPackageRepository.findOne(packageId);
		BigDecimal quota = p.getPackageQuota();
		BigDecimal rate = getMatrixPackage().get(userLevel).get(productRiskLevel);
		BigDecimal result = quota.multiply(rate);
		LOGGER.info("Single Package quota {}, rate {}, limit {}",
				new Object[]{quota, rate, result});
		return result;
	}
	
	/**
	 * 同一个风险类型下所有项目所有买入融资包金额总和. <br/>
	 * SELECT * FROM FP_PKG_SUBS WHERE USER_ID = ?userId AND PKG_ID IN (
  	 *	 SELECT PKG_ID FROM FP_PKG WHERE STATUS IN ('S', 'WN') AND PROD_ID IN (SELECT PROD_ID FROM FP_PROD WHERE FP_PROD.PROD_LVL = ?level)
	 * );
	 * @param userId
	 * @param level  the project level of this package which user intend to purchase.
	 * @return
	 */
	private BigDecimal getAccumulativeValueForAcrossProjects(String userId, ERiskLevel level) {
		LOGGER.info("getAccumulativeValueForAcrossProjects() invoked");
		BigDecimal sumUnit = this.subcribeRepository.getSumUnitByUserAndProdLevel(userId, level.getCode());
		LOGGER.info("SUM amount from FP_PKG_SUBS {}", sumUnit);
		BigDecimal shareholding = this.investRiskStatisticsRepository.getUserCreditorAssetsByProductLevel(userId, level.getCode());
		LOGGER.info("SUM amount from AC_POSITION {}", shareholding);
		if (sumUnit == null) {
			sumUnit = BigDecimal.ZERO;
		}
		if (shareholding != null) {
			sumUnit = sumUnit.add(shareholding);
		}
		LOGGER.info("User {}, Same risk level products all accumulative assets {}", new Object[]{userId, sumUnit});
		return sumUnit;
	}

	/**
	 * 此融资包所隶属的项目所有买入融资包金额总和.<br/>
	 * SELECT SUM(UNIT) FROM FP_PKG_SUBS WHERE USER_ID = ?userId AND PKG_ID IN (SELECT PKG_ID FROM FP_PKG WHERE PROD_ID = ?prodId AND STATUS IN ('S', 'WN'));
	 * @param userId
	 * @param productId
	 * @return
	 */
	private BigDecimal getAccumulativeValueForAcrossPackages(String userId, String productId) {
		LOGGER.info("getAccumulativeValueForAcrossPackages() invoked");
		BigDecimal sumUnit = this.subcribeRepository.getSumUnitByUserAndProd(userId, productId);
		LOGGER.info("SUM amount from FP_PKG_SUBS {}", sumUnit);
		BigDecimal shareholding = this.investRiskStatisticsRepository.getUserCreditorAssetsByProductId(userId, productId);
		LOGGER.info("SUM amount from AC_POSITION {}", shareholding);
		if (sumUnit == null) {
			sumUnit = BigDecimal.ZERO;
		}
		if (shareholding != null) {
			sumUnit = sumUnit.add(shareholding);
		}
		LOGGER.info("User {}, single product all accumulative packages assets {}", new Object[]{userId, sumUnit});
		return sumUnit;
	}
	
	/**
	 * 此融资包已经购买金额.<br/>
	 * 
	 * @param userId
	 * @param packageId
	 * @return
	 */
	private BigDecimal getAccumulativeValueForSinglePackages(String userId, String packageId) {
		LOGGER.info("getAccumulativeValueForSinglePackages() invoked");
		List<PackageSubscribes> subs = this.subcribeRepository.getSubsByPkgIdAndUserId(packageId, userId);
        BigDecimal subsAmount = BigDecimal.ZERO;
        if (CollectionUtils.isNotEmpty(subs)) {
            for (PackageSubscribes sub : subs) {
                subsAmount = subsAmount.add(sub.getUnitAmt().multiply(new BigDecimal(sub.getUnit())));
            }
        }
        LOGGER.info("SUM amount from FP_PKG_SUBS {}", subsAmount);
        LOGGER.info("User {}, single package all accumulative packages assets {}", new Object[]{userId, subsAmount});
        return subsAmount;
	}
	
	/**
	 * 获取临时申购上下限金额,可用余额,已申购金额.
	 * @param userId
	 * @param packageId
	 * @param amount
	 * @return DO NOT subscribe when Qualified is false.
	 */
	@Transactional(readOnly = true)
	public PurchaseRiskResultDto getSubscribeLimitationTemp(String userId, String packageId) {
		LOGGER.info("getSubscribeLimitationTemp() invoked, user {}, package {}", userId, packageId);
		PurchaseRiskResultDto result = new PurchaseRiskResultDto();
		ERiskBearLevel userLevel = this.investRiskBearService.getUserRiskBearLevel(userId);
		ProductPackage pkg = this.getPackage(packageId);
		Product product = this.getProductByPackage(pkg);
		ERiskLevel productRiskLevel = product.getProductLevel();
		result.setUserLevel(userLevel);
		result.setProjectLevel(productRiskLevel);
		/** 可用余额. **/
        BigDecimal balance = getBalance(userId);
        result.setBalance(balance);
		BigDecimal maxAmount = BigDecimal.ZERO;
		if (ERiskLevel.HIGH_RISK == productRiskLevel
				 && (ERiskBearLevel.ROOKIE == userLevel || ERiskBearLevel.CAUTIOUS == userLevel || ERiskBearLevel.STEADY == userLevel)) {
			result.setQualified(false);
			result.setMessage(MessageUtil.getMessage(ERROR_SUBSCRIBE_UNQUALIFIED));
			return result;
		}
		if (ERiskLevel.HIGH_RISK == productRiskLevel && ERiskBearLevel.AGGRESSIVE == userLevel) {
			BigDecimal limitForSinglePackage = SubscribeUtils.fiveUnitFaceValue();
			BigDecimal accumulativeAmountForSinglePackage = this.getAccumulativeValueForSinglePackages(userId, packageId);
			BigDecimal remainLimitForSinglePackage = limitForSinglePackage.subtract(accumulativeAmountForSinglePackage);
			/** 超过单个融资包上限. **/
			if (remainLimitForSinglePackage.compareTo(BigDecimal.ZERO) <= 0) {
				result.setQualified(false);
				result.setMaxSubscribeAmount(limitForSinglePackage);
				result.setSubscribeAmount(accumulativeAmountForSinglePackage);
				result.setMessage(MessageUtil.getMessage(ERROR_SUBSCRIBE_LACK,
						result.getMaxSubscribeAmount(), result.getSubscribeAmount()));
				return result;
			}
			BigDecimal limitForMultiProjects = this.getUpperLimitationForAcrossProjectsTemp(userId);
			LOGGER.info("Limitation for multi projects {}", limitForMultiProjects);
			result.setUpperLimitForSameCategory(limitForMultiProjects);
			BigDecimal acrossProjectAmount = this.getAccumulativeValueForAcrossProjects(userId, productRiskLevel);
			LOGGER.info("Accumulative amount for multi projects {}", acrossProjectAmount);
			result.setAccumulativeAmount(acrossProjectAmount);
			BigDecimal remainlimitForMultiProjects = limitForMultiProjects.subtract(acrossProjectAmount);
			/** 超过同一风险等级项目融资上限. **/
			if (remainlimitForMultiProjects.compareTo(BigDecimal.ZERO) <= 0) {
				result.setQualified(false);
				result.setMaxSubscribeAmount(limitForSinglePackage.min(limitForMultiProjects));
				result.setSubscribeAmount(acrossProjectAmount);
				result.setMessage(MessageUtil.getMessage(ERROR_SUBSCRIBE_OVERFLOW));
				return result;
			}
			maxAmount = remainLimitForSinglePackage.min(remainlimitForMultiProjects);	
			
			BigDecimal minAmount = getMinLimitation(pkg.getPackageQuota());
			if (accumulativeAmountForSinglePackage.compareTo(BigDecimal.ZERO) > 0) {
				/** 已申购过，累计申购开始最小申购额为faceValue. **/
				minAmount = getUnitFaceValue();
				result.setSubscribeAmount(accumulativeAmountForSinglePackage);
			}
	        // 可用余额小于最大可申购额，以可用余额为准
            maxAmount = maxAmount.min(balance);
            if (maxAmount.compareTo(minAmount) < 0) {
                // 最大可申购余额小于最小申购额，不可申购
            	result.setQualified(false);
            	result.setMaxSubscribeAmount(limitForMultiProjects);
				result.setSubscribeAmount(acrossProjectAmount);
				result.setMessage(MessageUtil.getMessage(ERROR_SUBSCRIBE_OVERFLOW));
                maxAmount = minAmount;
            }
            maxAmount = SubscribeUtils.floorUnitFaceValue(maxAmount);
	        result.setLowerLimit(minAmount);
	        result.setUpperLimit(maxAmount);
		} else {
			BigDecimal minAmount = getMinLimitation(pkg.getPackageQuota());
            InvestorInfo investorInfo = investorRepository.findOne(userId);
            if (investorInfo != null && StringUtils.isNotBlank(investorInfo.getInvestorLevelOriginal())) {
                // 通过可购买比例计算最大购买额
                maxAmount = CommonBusinessUtil.getMaxSubscribeRate(investorInfo.getInvestorLevelOriginal())
                		.multiply(pkg.getPackageQuota());
                result.setMaxSubscribeAmount(maxAmount);
                /** 减去同一个融资包累计购买金额. **/
    			BigDecimal singlePackageAmount = this.getAccumulativeValueForSinglePackages(userId, packageId);
    			if (singlePackageAmount.compareTo(BigDecimal.ZERO) > 0) {
    				/** 已申购过，累计申购开始最小申购额为faceValue. **/
    				minAmount = getUnitFaceValue();
    				result.setSubscribeAmount(singlePackageAmount);
    			}
                maxAmount = maxAmount.subtract(singlePackageAmount);
                if (maxAmount.compareTo(BigDecimal.ZERO) <= 0) {
                	/** 已经达到申购上限. **/
                	result.setQualified(false);
                	result.setMessage(MessageUtil.getMessage(ERROR_SUBSCRIBE_OVERFLOW2,
    						result.getMaxSubscribeAmount(), result.getSubscribeAmount()));
        			result.setLowerLimit(minAmount);
        			result.setUpperLimit(maxAmount);
                	return result;
                }
                // 可用余额小于最大可申购额，以可用余额为准
                maxAmount = maxAmount.min(balance);
                if (maxAmount.compareTo(minAmount) < 0) {
                    // 最大可申购余额小于最小申购额，不可申购
                	result.setQualified(false);
                	result.setMessage(MessageUtil.getMessage(ERROR_SUBSCRIBE_LACK_ORIGINAL,
    						result.getMaxSubscribeAmount(), result.getSubscribeAmount()));
                    maxAmount = minAmount;
                }
                // 如果计算得出的值不是faceValue的整数倍，往下取faceValue的倍数
                maxAmount = SubscribeUtils.floorUnitFaceValue(maxAmount);
            }
			result.setLowerLimit(minAmount);
			result.setUpperLimit(maxAmount);
		}
		return result;
	}
	
	/**
	 * 
	 * @param userId
	 * @param packageId
	 * @return
	 */
	@Transactional(readOnly = true)
	public PurchaseRiskResultDto getSubscribeLimitation(String userId, String packageId) {
		LOGGER.info("getSubscribeLimitation() invoked, user {}, package {}", userId, packageId);
		PurchaseRiskResultDto result = new PurchaseRiskResultDto();
		ERiskBearLevel userLevel = this.investRiskBearService.getUserRiskBearLevel(userId);
		ProductPackage pkg = this.getPackage(packageId);
		Product product = this.getProductByPackage(pkg);
		ERiskLevel productRiskLevel = product.getProductLevel();
		result.setUserLevel(userLevel);
		result.setProjectLevel(productRiskLevel);

		/** 可用余额. **/
        BigDecimal balance = getBalance(userId);
        result.setBalance(balance);
        
		if (ERiskLevel.NULL == productRiskLevel) {
			/** set default high risk. **/
			productRiskLevel = ERiskLevel.HIGH_RISK;
		}

		BigDecimal minAmount = getUnitFaceValue();
		// 最小值为max(faceValue, 总额/200)
        BigDecimal unitAmount = pkg.getPackageQuota().divide(getMaxUserPerPkg(), 0, RoundingMode.UP);
        if (unitAmount.compareTo(minAmount) > 0) {
            // 整除200份并且向上进位到faceValue的倍数，结果肯定>=faceValue直接用来做最小购买额
            minAmount = unitAmount.divide(minAmount, 0, RoundingMode.UP).multiply(minAmount);
        }
        
		BigDecimal limitForSinglePackage = this.getUpperLimitationForSinglePackage(packageId, userLevel, productRiskLevel);
		BigDecimal limitForSingleProject = this.getUpperLimitationForAcrossPackages(userId, userLevel, productRiskLevel);
		
		/** 减去同一个融资包累计购买金额. **/
		BigDecimal singlePackageAmount = this.getAccumulativeValueForSinglePackages(userId, packageId);
		limitForSinglePackage = limitForSinglePackage.subtract(singlePackageAmount);
		if (singlePackageAmount.compareTo(BigDecimal.ZERO) > 0) {
			/** 已申购过，累计申购开始最小申购额为faceValue. **/
			minAmount = getUnitFaceValue();
			result.setSubscribeAmount(singlePackageAmount);
		}
		
		/** 减去同一个项目下的其他融资包金额. **/
		BigDecimal acrossPackagesAmount = this.getAccumulativeValueForAcrossPackages(userId, product.getProductId());
		limitForSingleProject = limitForSingleProject.subtract(acrossPackagesAmount);
		
		result.setLowerLimit(minAmount);
		result.setUpperLimit(limitForSinglePackage.min(limitForSingleProject));
		
		/** 针对已经买入同一包&同一项目的高风险项目, 重新设置买入上限. **/
		if (singlePackageAmount.compareTo(BigDecimal.ZERO) == 0
				&& acrossPackagesAmount.compareTo(BigDecimal.ZERO) == 0
				&& ERiskLevel.HIGH_QUALITY == productRiskLevel
				&& result.getUpperLimit().compareTo(getUnitFaceValue()) < 0) {
			result.setUpperLimit(getUnitFaceValue());
		}

		BigDecimal accumulativeAmount = this.getAccumulativeValueForAcrossProjects(userId, productRiskLevel);
		result.setAccumulativeAmount(accumulativeAmount);
		
		if (ERiskBearLevel.ROOKIE != userLevel) {
			BigDecimal rate = getMatrixCategory().get(userLevel).get(productRiskLevel);
			result.setRate(rate);
		}
		
		if ((ERiskBearLevel.ROOKIE == userLevel || ERiskBearLevel.CAUTIOUS == userLevel)
				&& ERiskLevel.HIGH_RISK == productRiskLevel) {
			result.setQualified(false);
			result.setMessage(MessageUtil.getMessage(ERROR_SUBSCRIBE_UNQUALIFIED));
		}
		return result;
	}
	
	/**
	 * Check warning on same category for multiple projects.
	 * @param userId
	 * @param packageId
	 * @param amount
	 * @return true if exceed upper limitation.
	 */
	@Transactional(readOnly = true)
	public boolean checkWarningOnCategory(String userId, String packageId, BigDecimal amount) {
		LOGGER.info("checkWarningOnCategory() invoked, user {}, package {}, amount {}",
				new Object[]{userId, packageId, amount});
		try {
			ERiskBearLevel userLevel = this.investRiskBearService.getUserRiskBearLevel(userId);
			if (ERiskBearLevel.ROOKIE == userLevel) {
				return false;
			}
			ProductPackage pkg = this.getPackage(packageId);
			Product product = this.getProductByPackage(pkg);
			ERiskLevel productRiskLevel = product.getProductLevel();
			
			if (ERiskLevel.NULL == productRiskLevel) {
				/** set default high risk. **/
				productRiskLevel = ERiskLevel.HIGH_RISK;
			}
			
			BigDecimal limitForMultiProjects = this.getUpperLimitationForAcrossProjects(userId, userLevel, productRiskLevel);
			LOGGER.info("Limitation for multi projects {}", limitForMultiProjects);
			BigDecimal acrossProjectAmount = this.getAccumulativeValueForAcrossProjects(userId, productRiskLevel);
			LOGGER.info("Accumulative amount for multi projects {}", acrossProjectAmount);
			if (acrossProjectAmount.add(amount).compareTo(limitForMultiProjects) > 0) {
				return true;
			}
			return false;
		} catch (Exception e) {
			LOGGER.error("Exception {} ", e);
			return false;
		}
	}

	/**
	 * 获得可用余额.
	 * @param userId
	 * @return
	 */
	private BigDecimal getBalance(String userId) {
		return fundAccoutService.getUserCurrentAcctAvlBal(userId, true);
	}
	
	/**
	 * 获得申购下限.
	 * @param packageQuota 融资包金额.
	 * @return
	 */
	private BigDecimal getMinLimitation(BigDecimal packageQuota) {
		BigDecimal minAmount = getUnitFaceValue();
		// 最小值为max(faceValue, 总额/200)
		BigDecimal unitAmount = packageQuota.divide(getMaxUserPerPkg(), 0, RoundingMode.UP);
		if (unitAmount.compareTo(minAmount) > 0) {
		    // 整除200份并且向上进位到faceValue的倍数，结果肯定>=faceValue直接用来做最小购买额
			minAmount = SubscribeUtils.roundUnitFaceValue(unitAmount);
		}
		return minAmount;
	}
	
	private ProductPackage getPackage(String packageId) {
		return productPackageRepository.findOne(packageId);
	}
	
	private Product getProductByPackage(ProductPackage ppackage) {
		return ppackage.getProduct();
	}
	

	public static Map<ERiskBearLevel, Map<ERiskLevel, BigDecimal>> getMatrixProject() {
		if (MATRIX_PROJECT.isEmpty()) {
			init();
		}
		return MATRIX_PROJECT;
	}
	
	public static Map<ERiskBearLevel, Map<ERiskLevel, BigDecimal>> getMatrixPackage() {
		if (MATRIX_PACKAGE.isEmpty()) {
			init();
		}
		return MATRIX_PACKAGE;
	}

	public static Map<ERiskBearLevel, Map<ERiskLevel, BigDecimal>> getMatrixCategory() {
		if (MATRIX_CATEGORY.isEmpty()) {
			init();
		}
		return MATRIX_CATEGORY;
	}

	private static void init() {
		final BigDecimal p100 = BigDecimal.ONE;
		final BigDecimal p90 = BigDecimal.valueOf(0.90);
		final BigDecimal p70 = BigDecimal.valueOf(0.70);
		final BigDecimal p60 = BigDecimal.valueOf(0.60);
		final BigDecimal p50 = BigDecimal.valueOf(0.50);
		final BigDecimal p40 = BigDecimal.valueOf(0.40);
		final BigDecimal p30 = BigDecimal.valueOf(0.30);
		final BigDecimal p20 = BigDecimal.valueOf(0.20);
		final BigDecimal p15 = BigDecimal.valueOf(0.15);
		final BigDecimal p10 = BigDecimal.valueOf(0.10);
		final BigDecimal p05 = BigDecimal.valueOf(0.05);
		final BigDecimal p00 = BigDecimal.ZERO;
		/** 同一风险类别上限. **/
		Map<ERiskLevel, BigDecimal> aggressiveForCategory = new HashMap<ERiskLevel, BigDecimal>() {
			private static final long serialVersionUID = 1L;
			{
				put(ERiskLevel.HIGH_QUALITY, p100);
				put(ERiskLevel.MEDIUM, p100);
				put(ERiskLevel.ACCEPTABLE, p70);
				put(ERiskLevel.HIGH_RISK, p20);
			}
		};
		
		Map<ERiskLevel, BigDecimal> steadyForCategory = new HashMap<ERiskLevel, BigDecimal>() {
			private static final long serialVersionUID = 1L;
			{
				put(ERiskLevel.HIGH_QUALITY, p100);
				put(ERiskLevel.MEDIUM, p90);
				put(ERiskLevel.ACCEPTABLE, p60);
				put(ERiskLevel.HIGH_RISK, p15);
			}
		};
		
		Map<ERiskLevel, BigDecimal> cautiousForCategory = new HashMap<ERiskLevel, BigDecimal>() {
			private static final long serialVersionUID = 1L;
			{
				put(ERiskLevel.HIGH_QUALITY, p100);
				put(ERiskLevel.MEDIUM, p90);
				put(ERiskLevel.ACCEPTABLE, p60);
				put(ERiskLevel.HIGH_RISK, p00);
			}
		};
		
		MATRIX_CATEGORY.put(ERiskBearLevel.AGGRESSIVE, aggressiveForCategory);
		MATRIX_CATEGORY.put(ERiskBearLevel.STEADY, steadyForCategory);
		MATRIX_CATEGORY.put(ERiskBearLevel.CAUTIOUS, cautiousForCategory);
		
		
		/** 单个项目上限. **/
		Map<ERiskLevel, BigDecimal> aggressiveForProject = new HashMap<ERiskLevel, BigDecimal>() {
			private static final long serialVersionUID = 1L;
			{
				put(ERiskLevel.HIGH_QUALITY, p50);
				put(ERiskLevel.MEDIUM, p40);
				put(ERiskLevel.ACCEPTABLE, p30);
				put(ERiskLevel.HIGH_RISK, p10);
			}
		};
		
		Map<ERiskLevel, BigDecimal> steadyForProject = new HashMap<ERiskLevel, BigDecimal>() {
			private static final long serialVersionUID = 1L;
			{
				put(ERiskLevel.HIGH_QUALITY, p50);
				put(ERiskLevel.MEDIUM, p30);
				put(ERiskLevel.ACCEPTABLE, p20);
				put(ERiskLevel.HIGH_RISK, p05);
			}
		};
		
		Map<ERiskLevel, BigDecimal> cautiousForProject = new HashMap<ERiskLevel, BigDecimal>() {
			private static final long serialVersionUID = 1L;
			{
				put(ERiskLevel.HIGH_QUALITY, p50);
				put(ERiskLevel.MEDIUM, p30);
				put(ERiskLevel.ACCEPTABLE, p20);
				put(ERiskLevel.HIGH_RISK, p00);
			}
		};
		
		MATRIX_PROJECT.put(ERiskBearLevel.AGGRESSIVE, aggressiveForProject);
		MATRIX_PROJECT.put(ERiskBearLevel.STEADY, steadyForProject);
		MATRIX_PROJECT.put(ERiskBearLevel.CAUTIOUS, cautiousForProject);
		
		/** 单个包上限. **/
		Map<ERiskLevel, BigDecimal> aggressiveForPackage = new HashMap<ERiskLevel, BigDecimal>() {
			private static final long serialVersionUID = 1L;
			{
				put(ERiskLevel.HIGH_QUALITY, p50);
				put(ERiskLevel.MEDIUM, p50);
				put(ERiskLevel.ACCEPTABLE, p50);
				put(ERiskLevel.HIGH_RISK, p50);
			}
		};
		
		Map<ERiskLevel, BigDecimal> steadyForPackage = new HashMap<ERiskLevel, BigDecimal>() {
			private static final long serialVersionUID = 1L;
			{
				put(ERiskLevel.HIGH_QUALITY, p20);
				put(ERiskLevel.MEDIUM, p20);
				put(ERiskLevel.ACCEPTABLE, p20);
				put(ERiskLevel.HIGH_RISK, p20);
			}
		};
		
		Map<ERiskLevel, BigDecimal> cautiousForPackage = new HashMap<ERiskLevel, BigDecimal>() {
			private static final long serialVersionUID = 1L;
			{
				put(ERiskLevel.HIGH_QUALITY, p20);
				put(ERiskLevel.MEDIUM, p20);
				put(ERiskLevel.ACCEPTABLE, p20);
				put(ERiskLevel.HIGH_RISK, p20);
			}
		};
		
		MATRIX_PACKAGE.put(ERiskBearLevel.AGGRESSIVE, aggressiveForPackage);
		MATRIX_PACKAGE.put(ERiskBearLevel.STEADY, steadyForPackage);
		MATRIX_PACKAGE.put(ERiskBearLevel.CAUTIOUS, cautiousForPackage);
	}
}
