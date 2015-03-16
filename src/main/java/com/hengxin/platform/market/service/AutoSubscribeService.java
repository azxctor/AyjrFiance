package com.hengxin.platform.market.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.domain.BusinessSigninStatus;
import com.hengxin.platform.common.domain.DynamicOption;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.enums.EBusinessSigninStatus;
import com.hengxin.platform.common.enums.EOptionCategory;
import com.hengxin.platform.common.repository.BusinessSigninStatusRepository;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.common.util.NumberUtil;
import com.hengxin.platform.common.util.PaginationUtil;
import com.hengxin.platform.common.util.SystemDictUtil;
import com.hengxin.platform.fund.dto.biz.req.atom.ReserveReq;
import com.hengxin.platform.fund.entity.AcctPo;
import com.hengxin.platform.fund.entity.SubAcctPo;
import com.hengxin.platform.fund.enums.EAcctStatus;
import com.hengxin.platform.fund.enums.EFundUseType;
import com.hengxin.platform.fund.enums.ESubAcctNo;
import com.hengxin.platform.fund.service.InvestorSubscribeService;
import com.hengxin.platform.market.comparator.AutoSubscribeAmountComparator;
import com.hengxin.platform.market.comparator.AutoSubscribeScoreComparator;
import com.hengxin.platform.market.dto.AutoSubscribeCandidateDto;
import com.hengxin.platform.market.dto.AutoSubscribeCriteriaDto;
import com.hengxin.platform.market.dto.AutoSubscribeResultDto;
import com.hengxin.platform.market.dto.AutoSubscribeSearchDto;
import com.hengxin.platform.market.dto.AutoSubscriberFinancingPackageDto;
import com.hengxin.platform.market.dto.CandidateCriteria;
import com.hengxin.platform.market.dto.SkinnyMarketProductDto;
import com.hengxin.platform.market.utils.SubscribeUtils;
import com.hengxin.platform.member.domain.AutoSubscribeParam;
import com.hengxin.platform.member.domain.InvestorInfo;
import com.hengxin.platform.member.domain.SubscribeGroup;
import com.hengxin.platform.member.domain.SubscribeGroupUser;
import com.hengxin.platform.member.repository.AutoSubscribeParamRepository;
import com.hengxin.platform.member.repository.UserInvestRuleRepository;
import com.hengxin.platform.product.domain.PackageSubscribes;
import com.hengxin.platform.product.domain.Product;
import com.hengxin.platform.product.domain.ProductPackage;
import com.hengxin.platform.product.enums.EAutoSubscribeStatus;
import com.hengxin.platform.product.enums.EPackageStatus;
import com.hengxin.platform.product.enums.EPayMethodType;
import com.hengxin.platform.product.enums.ERiskLevel;
import com.hengxin.platform.product.enums.ETermType;
import com.hengxin.platform.product.enums.EWarrantyType;
import com.hengxin.platform.product.repository.PackageSubscribesRepository;
import com.hengxin.platform.product.repository.ProductPackageRepository;
import com.hengxin.platform.product.repository.ProductRepository;
import com.hengxin.platform.product.service.ProdPkgMsgRemindService;
import com.hengxin.platform.risk.dto.PurchaseRiskResultDto;
import com.hengxin.platform.risk.service.InvestorPurchaseLimitationService;
import com.hengxin.platform.security.entity.SimpleUserPo;
import com.hengxin.platform.security.entity.UserPo;

/**
 * Core service for auto subscribe.
 * 
 * @author yeliangjin
 * 
 */
@Service
public class AutoSubscribeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AutoSubscribeService.class);

    @Autowired
    transient ProductPackageRepository productPackageRepository;
    @Autowired
    transient AutoSubscribeParamRepository autoSubscribeParamRepository;
    @Autowired
    transient UserInvestRuleRepository userInvestRuleRepository;
    @Autowired
    transient InvestorSubscribeService investorSubscribeService;
    @Autowired
    transient PackageSubscribesRepository packageSubscribesRepository;
    @Autowired
    transient ProductRepository productRepository;
    @Autowired
    transient BusinessSigninStatusRepository businessSigninStatusRepository;
    @Autowired
    transient FinancingResourceService resourceService;
    @Autowired
    transient ProdPkgMsgRemindService prodPkgMsgRemindService;
    @Autowired
    transient InvestorPurchaseLimitationService limitationService;
    
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

    @Transactional(readOnly = true)
    public boolean getBusinessSigninStatus() {
        BusinessSigninStatus bs = businessSigninStatusRepository.findOne("MKT");
        return EBusinessSigninStatus.OPEN.getCode().equals(bs == null ? null : bs.getStatus());
    }

    /**
     * check whether it has made a bargain.
     * 
     * @param packageId
     * @return true if it has made a bargain.
     */
    @Transactional(readOnly = true)
    public boolean isDealt(String packageId) {
        LOGGER.info("isDealt() invoked, package {}", packageId);
        int count = this.productPackageRepository.countSubPackage(packageId);
        return count > 0 ? true : false;
    }

    @Transactional(readOnly = true)
    public ProductPackage findPackage(String packageId) {
        ProductPackage pkg = productPackageRepository.findOne(packageId);
        return pkg;
    }

    @Transactional(readOnly = true)
    public DataTablesResponseDto<AutoSubscriberFinancingPackageDto> findAutoSubscribePackages(
            final AutoSubscribeSearchDto criteria) {
        LOGGER.info("findAutoSubscriberPackages() invoked");
        Pageable pageRequest = PaginationUtil.buildPageRequest(criteria);
        Specification<ProductPackage> specification = new Specification<ProductPackage>() {
            @Override
            public Predicate toPredicate(Root<ProductPackage> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                Predicate predicate = builder.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                if (StringUtils.isNotBlank(criteria.getPackageId())) {
                    expressions.add(builder.like(builder.lower(root.<String> get("id")), "%"
                            + criteria.getPackageId().toLowerCase() + "%"));
                }
                Join<ProductPackage, Product> productJoin = root.join("product");
                expressions.add(builder.and(builder.equal(root.get("productId"), productJoin.get("productId"))));
                if (criteria.isIncludeUser()) {
                    Join<Product, UserPo> userJoin = productJoin.join("user");
                    expressions.add(builder.and(builder.equal(productJoin.get("applUserId"), userJoin.get("userId"))));
                }
                expressions.add(builder.and(builder.equal(root.<EAutoSubscribeStatus> get("autoSubscribeFlag"),
                        EAutoSubscribeStatus.ALLOW)));
                expressions.add(root.<EPackageStatus> get("status").in(EPackageStatus.PRE_PUBLISH,
                        EPackageStatus.WAIT_SUBSCRIBE, EPackageStatus.SUBSCRIBE, EPackageStatus.WAIT_SIGN));
                return predicate;
            }
        };
        Page<ProductPackage> packages = productPackageRepository.findAll(specification, pageRequest);
        DataTablesResponseDto<AutoSubscriberFinancingPackageDto> packageDtos = new DataTablesResponseDto<AutoSubscriberFinancingPackageDto>();
        packageDtos.setEcho(criteria.getEcho());
        packageDtos.setData(this.convertFinancingPackage(packages.getContent()));
        LOGGER.info("size {}", packageDtos.getData().size());
        packageDtos.setTotalDisplayRecords(packages.getTotalElements());
        packageDtos.setTotalRecords(packages.getTotalElements());
        return packageDtos;
    }

    @Transactional(readOnly = true)
    public AutoSubscriberFinancingPackageDto findAutoSubscribePackages(final String packageId) {
        LOGGER.info("findAutoSubscribePackages packageId:{}", packageId);
        Pageable pageRequest = new PageRequest(1, 1);
        Specification<ProductPackage> specification = new Specification<ProductPackage>() {
            @Override
            public Predicate toPredicate(Root<ProductPackage> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                Predicate predicate = builder.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                expressions.add(builder.equal(builder.lower(root.<String> get("id")), packageId.toLowerCase()));
                Join<ProductPackage, Product> productJoin = root.join("product");
                expressions.add(builder.and(builder.equal(root.get("productId"), productJoin.get("productId"))));
                expressions.add(builder.and(builder.equal(root.<EAutoSubscribeStatus> get("autoSubscribeFlag"),
                        EAutoSubscribeStatus.ALLOW)));
                return predicate;
            }
        };
        Page<ProductPackage> packages = productPackageRepository.findAll(specification, pageRequest);
        List<AutoSubscriberFinancingPackageDto> singlePackage = this.convertFinancingPackage(packages.getContent());
        return singlePackage.isEmpty() ? null : singlePackage.get(0);
    }

    private List<AutoSubscriberFinancingPackageDto> convertFinancingPackage(List<ProductPackage> packages) {
        LOGGER.info("convertFinancingPackage() invoked");
        List<AutoSubscriberFinancingPackageDto> list = new ArrayList<AutoSubscriberFinancingPackageDto>();
        for (ProductPackage financingPackage : packages) {
            AutoSubscriberFinancingPackageDto dto = new AutoSubscriberFinancingPackageDto();
            dto.setId(financingPackage.getId());
            dto.setProductName(financingPackage.getPackageName());
            dto.setFinancingRealName(financingPackage.getProduct().getUserPo().getName());
            dto.setFinancingName(financingPackage.getProduct().getUserPo().getUsername());
            if (financingPackage.getProduct().getProductProviderInfo() != null) {
            	dto.setWarrantor(financingPackage.getProduct().getProductProviderInfo().getUserPo().getName());
                dto.setWarrantorShow(financingPackage.getProduct().getUserPoWrtrShow().getName());	
			}
            /** Quota. **/
            dto.setPackageQuota(financingPackage.getPackageQuota());
            dto.setSupplyAmount(financingPackage.getSupplyAmount());
            /** avoid NPE due to dirty data. **/
            if (dto.getPackageQuota() == null) {
                LOGGER.warn("融资包金额   reset as 0");
                dto.setPackageQuota(BigDecimal.ZERO);
            }
            if (dto.getSupplyAmount() == null) {
                LOGGER.warn("已融资金额 为空 reset as 0");
                dto.setSupplyAmount(BigDecimal.ZERO);
            }
            if (dto.getPackageQuota() != null && dto.getSupplyAmount() != null) {
                dto.setAvailableQuota(dto.getPackageQuota().subtract(dto.getSupplyAmount())
                        .setScale(2, RoundingMode.HALF_UP));
            } else {
                dto.setAvailableQuota(BigDecimal.ZERO);
            }
            if (financingPackage.getProduct().getRate() != null) {
                BigDecimal rate = financingPackage.getProduct().getRate().multiply(NumberUtil.getHundred());
                rate = rate.setScale(6, RoundingMode.HALF_UP);
                dto.setRate(rate.toString());
            }
            dto.setTermLength(financingPackage.getProduct().getTermLength()
                    + financingPackage.getProduct().getTermType().getText());
            /** Nested product for sort. **/
            SkinnyMarketProductDto product = new SkinnyMarketProductDto();
            product.setRate(dto.getRate());
            product.setTermLength(dto.getTermLength());
            dto.setProduct(product);

            dto.setPayMethod(financingPackage.getProduct().getPayMethod().getText());
            dto.setStatus(financingPackage.getStatus().getText());

            if (financingPackage.getSupplyAmount() != null && financingPackage.getPackageQuota() != null) {
                // PERCENT ROUND((PGK.SUBS_AMT/PGK.PKG_QUOTA)*100)
                BigDecimal percent = financingPackage.getSupplyAmount().divide(financingPackage.getPackageQuota(), 3,
                        BigDecimal.ROUND_UP);
                percent = percent.multiply(NumberUtil.getHundred());
                percent = percent.setScale(1, RoundingMode.HALF_UP);
                dto.setPercent(percent + "%");
            }

            // region
            String regionCode = financingPackage.getProduct().getUser().getRegion();
            DynamicOption region = SystemDictUtil.getDictByCategoryAndCode(EOptionCategory.REGION, regionCode);
            if (region != null) {
                dto.setRegion(region.getFullText());
            }

            // industry
            String industryCode = financingPackage.getProduct().getFinancierIndustryType();
            DynamicOption industry = SystemDictUtil
                    .getDictByCategoryAndCode(EOptionCategory.ORG_INDUSTRY, industryCode);
            if (industry != null) {
                dto.setIndustry(industry.getText());
            }
            LOGGER.info("AutoSubscriberFinancingPackageDto {}", dto.toString());
            list.add(dto);
        }
        return list;
    }

    /**
     * TODO enhance search criteria.
     * @param criteria
     * @return
     */
    @Transactional(readOnly = true)
    public DataTablesResponseDto<AutoSubscribeCandidateDto> findCandidateSettings(final CandidateCriteria criteria) {
        LOGGER.info("findCandidateSettings() invoked");
        Pageable pageRequest = PaginationUtil.buildPageRequest(criteria);
        Specification<AutoSubscribeParam> specification = new Specification<AutoSubscribeParam>() {
            @Override
            public Predicate toPredicate(Root<AutoSubscribeParam> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                if (criteria.getName() != null) {
                	Join<AutoSubscribeParam, SimpleUserPo> userJoin = root.join("user");
					expressions.add(cb.like(userJoin.<String> get("name"), "%" + criteria.getName() + "%"));
				}
                if (criteria.getAccountId() != null) {
                	Join<AutoSubscribeParam, AcctPo> acctJoin = root.join("acct");
					expressions.add(cb.like(acctJoin.<String> get("acctNo"), "%" + criteria.getAccountId() + "%"));
				}
                if (criteria.getRiskParam() != null) {
                    expressions.add(cb.like(root.<String> get("riskParam"), "%" + criteria.getRiskParam() + "%"));
                }
                if (criteria.getPayMethodParam() != null) {
                    expressions.add(cb.like(root.<String> get("repayment"), "%" + criteria.getPayMethodParam() + "%"));
                }
                if (criteria.getWarrantyTypeParam() != null) {
                    expressions.add(cb.like(root.<String> get("riskGuarantee"), "%" + criteria.getWarrantyTypeParam() + "%"));
                }
                return predicate;
            }
        };
        Page<AutoSubscribeParam> params = autoSubscribeParamRepository.findAll(specification, pageRequest);
        DataTablesResponseDto<AutoSubscribeCandidateDto> packageDtos = new DataTablesResponseDto<AutoSubscribeCandidateDto>();
        packageDtos.setEcho(criteria.getEcho());
        packageDtos.setData(this.convertToCandidates(params.getContent()));
        packageDtos.setTotalDisplayRecords(params.getTotalElements());
        packageDtos.setTotalRecords(params.getTotalElements());
        return packageDtos;
    }
    
    private List<AutoSubscribeCandidateDto> convertToCandidates(List<AutoSubscribeParam> params) {
    	List<AutoSubscribeCandidateDto> candidates = new ArrayList<AutoSubscribeCandidateDto>();
        for (AutoSubscribeParam param : params) {
            AutoSubscribeCandidateDto candidate = new AutoSubscribeCandidateDto();
            candidate.setUserId(param.getUserId());
            candidate.setAccountId(param.getUser().getAccount().getAcctNo());
            candidate.setName(param.getUser().getName());
            /** 资金账户保留额. **/
            candidate.setMinBalance(param.getMinBalance().setScale(2, RoundingMode.HALF_UP));
            /** 单笔申购最大金额. **/
            candidate.setMaxBalance(param.getMaxSubscribeAmount().setScale(2, RoundingMode.HALF_UP));
            candidate.setRiskParam(renderRiskParam(param.getRiskParam()));
            candidate.setPayMethodParam(renderRepaymentParam(param.getRepayment()));
            candidate.setWarrantyTypeParam(renderWarranty(param.getRiskGuarantee()));
            candidate.setMinDayCount(param.getMinDateRange());
            candidate.setMaxDayCount(param.getMaxDateRange());
            candidate.setMinDayRate(param.getMinAPRForDate());
            candidate.setMinMonthCount(param.getMinMonthRange());
            candidate.setMaxMonthCount(param.getMaxMonthRange());
            candidate.setMinMonthRate(param.getMinAPRForMonth().multiply(NumberUtil.getHundred())
                        .setScale(1, RoundingMode.HALF_UP));
            candidate.setScore(param.getScore());
            LOGGER.info("candidate {} ", candidate.toString());
            candidates.add(candidate);
        }
        LOGGER.info("{} candidates found", candidates.size());
        return candidates;
    }

    
    /**
     * 
     * @param packageId
     * @param commit
     *            DO
     * @param accountId
     *            for fuzzy query.
     * @param currentUser
     *            for maintenance user id.
     * @return
     */
    @Transactional
    public List<AutoSubscribeResultDto> autoSubscribe(String packageId, boolean commit, String accountId,
            String currentUser) {
        LOGGER.info("autoSubscribe() invoked");
        AutoSubscribeSearchDto criteria = new AutoSubscribeSearchDto();
        criteria.setPackageId(packageId);
        if (!commit) {
            criteria.setAccountId(accountId);
        }
        List<AutoSubscribeCandidateDto> candidates = findCandidates(criteria);
        ProductPackage pkg = productPackageRepository.findOne(packageId);
        List<AutoSubscribeResultDto> results = drawCandidates(candidates, pkg.getPackageQuota());
        if (commit) {
            BigDecimal totalDealAmount = BigDecimal.ZERO;
            for (int i = 0; i < results.size(); i++) {
                AutoSubscribeResultDto result = results.get(i);
                totalDealAmount = totalDealAmount.add(result.getDealAmount());
                if (i + 1 == results.size()) {
                    autoSubscribe(result, packageId, true);
                } else {
                    autoSubscribe(result, packageId, false);
                }
            }
            LOGGER.info("Quota {} in this Draw", totalDealAmount);
            /** update product available amount. **/
            Date currentDate = new Date();
            /** update Package. **/
            pkg.setSupplyUserCount(pkg.getSupplyUserCount() + results.size());
            pkg.setSupplyAmount(pkg.getSupplyAmount().add(totalDealAmount));
            /** update Package status after bargain. **/
            if (totalDealAmount.compareTo(pkg.getPackageQuota()) == 0) {
                pkg.setStatus(EPackageStatus.WAIT_SIGN);
                // 发送提醒
                Product product = productRepository.findOne(pkg.getProductId());
                prodPkgMsgRemindService.pkgChangesToWaitSign(pkg.getId(), product.getApplUserId(),
                        ProdPkgMsgRemindService.OPSUBSISFULL);
            }
            // else if (totalDealAmount.compareTo(pkg.getPackageQuota()) < 0) {
            // pkg.setStatus(EPackageStatus.SUBSCRIBE);
            // }
            pkg.setLastSubsTime(new Date());
            pkg.setLastTime(currentDate);
            pkg.setLastOperatorId(currentUser);
            productPackageRepository.save(pkg);
            productPackageRepository.flush();
            if (pkg.getStatus() == EPackageStatus.SUBSCRIBE) {
            	resourceService.consume(packageId, pkg.getSupplyAmount().longValue());
            } else if (pkg.getStatus() == EPackageStatus.WAIT_SIGN) {
            	resourceService.removeItem(packageId);
            }
        }
        return results;
    }

    private void autoSubscribe(AutoSubscribeResultDto result, String packageId, boolean isLast) {
        String userId = result.getUserId();
        LOGGER.info("autoSubscribe() invoked, user ID {}", userId);
        Date now = new Date();
        // 保留个人账户资金
        BigDecimal deal = result.getDealAmount();
        BigDecimal fee = deal.multiply(CommonBusinessUtil.getAutoSubscribeFeeRate()).setScale(2, RoundingMode.HALF_UP);
        LOGGER.info("deal {}, fee {}", new Object[] { deal, fee });
        ReserveReq subscribeRequest = new ReserveReq();
        subscribeRequest.setUserId(userId);
        subscribeRequest.setBizId(packageId);
        subscribeRequest.setCurrOpId(userId);
        subscribeRequest.setUseType(EFundUseType.SUBSCRIBE);
        subscribeRequest.setAddXwb(true);
        subscribeRequest.setWorkDate(CommonBusinessUtil.getCurrentWorkDate());
        // 申购金额
        subscribeRequest.setTrxAmt(deal);
        subscribeRequest.setTrxMemo("申购融资包" + packageId + "，保留申购金额" + subscribeRequest.getTrxAmt() + "元");
        String reserveJnlNo = investorSubscribeService.subsPkgPrePayAmt(subscribeRequest);
        String feeReserveJnlNo = null;
        if (NumberUtil.isNotNegative(fee)) {
            // 手续费
            subscribeRequest.setTrxAmt(fee);
            subscribeRequest.setTrxMemo("申购融资包" + packageId + "，保留手续费" + subscribeRequest.getTrxAmt() + "元");
            feeReserveJnlNo = investorSubscribeService.subsFeePrePayAmt(subscribeRequest);
        }

        // 保存融资包申购信息
        PackageSubscribes subscribe = new PackageSubscribes();
        subscribe.setPkgId(packageId);
        subscribe.setUserId(userId);
        subscribe.setUnitAmt(getUnitFaceValue());
        subscribe.setUnit(result.getDealAmount().divide(subscribe.getUnitAmt()).intValue());
        subscribe.setReserveJnlNo(reserveJnlNo);
        subscribe.setFeeReserveJnlNo(feeReserveJnlNo);
        subscribe.setCreateOpid(userId);
        subscribe.setCreateTs(now);
        subscribe.setLastMntOpid(userId);
        subscribe.setLastMntTs(now);
        subscribe.setAutoSubscribe(true);
        subscribe.setSubscribeDate(CommonBusinessUtil.getCurrentWorkDate());
        packageSubscribesRepository.save(subscribe);
        packageSubscribesRepository.flush();
        /** update score. **/
        List<AutoSubscribeParam> list = this.autoSubscribeParamRepository.findByUserIdAndActiveFlagAndTerminationFlag(
                userId, "Y", "N");
        if (list.isEmpty() || list.size() > 1) {
            LOGGER.warn("自动申购参数非法");
        } else {
            AutoSubscribeParam param = list.get(0);
            if (isLast) {
                /** score should be a integer. **/
                BigDecimal score = BigDecimal.TEN.multiply(result.getDealAmount()).divide(result.getMaxAmount(), 1,
                        BigDecimal.ROUND_UP);
                param.setScore(param.getScore().add(score.setScale(0, BigDecimal.ROUND_UP)));
            } else {
                param.setScore(param.getScore().add(BigDecimal.TEN));
            }
            this.autoSubscribeParamRepository.save(param);
            this.autoSubscribeParamRepository.flush();
        }
    }

    /**
     * Find all applicable candidates for auto subscribe to give financing package.
     * 
     * @param packageId
     *            package id
     * @param accountId
     *            accountId
     * @return draw result
     */
    @Transactional(readOnly = true)
    public List<AutoSubscribeCandidateDto> findCandidates(AutoSubscribeSearchDto searchCriteria) {
        LOGGER.info("findApplicableCandidates() invoked for package {}", searchCriteria.getPackageId());
        ProductPackage pkg = productPackageRepository.findOne(searchCriteria.getPackageId());
        AutoSubscribeCriteriaDto criteria = new AutoSubscribeCriteriaDto();
        criteria.setRiskParam(pkg.getProduct().getProductLevel().getCode());
        criteria.setPayMethodParam(pkg.getProduct().getPayMethod().getCode());
        criteria.setWarrantyTypeParam(pkg.getProduct().getWarrantyType().getCode());
        criteria.setTermType(pkg.getProduct().getTermType());
        criteria.setTermCount(pkg.getProduct().getTermLength());
        if (ETermType.DAY == criteria.getTermType()) {
            criteria.setMinDayRate(pkg.getProduct().getRate());
        } else if (ETermType.MONTH == criteria.getTermType()) {
            criteria.setMinMonthRate(pkg.getProduct().getRate());
        }
        /** First step. 融资包额度/200、faceValue 元的较大值. **/
        BigDecimal subsMinAmt = pkg.getPackageQuota().divide(getMaxUserPerPkg()).max(getUnitFaceValue());
        subsMinAmt = SubscribeUtils.roundUnitFaceValue(subsMinAmt);
        criteria.setMinAmount(subsMinAmt);
        criteria.setTotalAmount(pkg.getPackageQuota());
        criteria.setPackageId(searchCriteria.getPackageId());
        /** 忽略定投组. **/
        criteria.setAip(false);
        criteria.setApplyUserId(pkg.getProduct().getApplUserId());
        List<AutoSubscribeCandidateDto> candidates = findCandidates(criteria, pkg.getPackageQuota(),
                searchCriteria.getAccountId());
        return candidates;
    }

    /**
     * Retrieves applicable candidates for auto subscribe with given criteria.
     * 
     * @param criteria
     *            criteria
     * @return list of candidates
     */
    @Transactional(readOnly = true)
    public List<AutoSubscribeCandidateDto> findCandidates(final AutoSubscribeCriteriaDto criteria,
            BigDecimal totalAmount, final String accountId) {
        LOGGER.info("Start query AutoSubscribeParam {}", criteria.toString());
        Specification<AutoSubscribeParam> specification = new Specification<AutoSubscribeParam>() {
            @Override
            public Predicate toPredicate(Root<AutoSubscribeParam> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                expressions.add(cb.equal(root.<String> get("activeFlag"), "Y"));
                expressions.add(cb.notEqual(root.<String> get("userId"), criteria.getApplyUserId()));
                if (criteria.getRiskParam() != null) {
                    expressions.add(cb.like(root.<String> get("riskParam"), "%" + criteria.getRiskParam() + "%"));
                }
                if (criteria.getPayMethodParam() != null) {
                    expressions.add(cb.like(root.<String> get("repayment"), "%" + criteria.getPayMethodParam() + "%"));
                }
                if (criteria.getWarrantyTypeParam() != null) {
                    expressions.add(cb.like(root.<String> get("riskGuarantee"), "%" + criteria.getWarrantyTypeParam()
                            + "%"));
                }
                if (criteria.getTermType() == ETermType.DAY) {
                    if (NumberUtil.isPositive(criteria.getTermCount())) {
                        expressions.add(cb.le(root.<Number> get("minDateRange"), criteria.getTermCount()));
                        expressions.add(cb.ge(root.<Number> get("maxDateRange"), criteria.getTermCount()));
                    }
                    if (NumberUtil.isPositive(criteria.getMinDayRate())) {
                        expressions.add(cb.le(root.<Number> get("minAPRForDate"), criteria.getMinDayRate()));
                    }
                } else if (criteria.getTermType() == ETermType.MONTH) {
                    if (NumberUtil.isPositive(criteria.getTermCount())) {
                        expressions.add(cb.le(root.<Number> get("minMonthRange"), criteria.getTermCount()));
                        expressions.add(cb.ge(root.<Number> get("maxMonthRange"), criteria.getTermCount()));
                    }
                    if (NumberUtil.isPositive(criteria.getMinMonthRate())) {
                        expressions.add(cb.le(root.<Number> get("minAPRForMonth"), criteria.getMinMonthRate()));
                    }
                }
                if (NumberUtil.isPositive(criteria.getMinAmount())) {
                    Subquery<AutoSubscribeParam> sq = query.subquery(AutoSubscribeParam.class);
                    Root<AutoSubscribeParam> sqr = sq.from(AutoSubscribeParam.class);
                    Join<AutoSubscribeParam, SimpleUserPo> userJoin = sqr.join("user");
                    Join<SimpleUserPo, AcctPo> acctJoin = userJoin.join("account");
                    Join<AcctPo, SubAcctPo> subAcctJoin = acctJoin.join("subAccounts");
                    sq.select(sqr).where(
                            cb.and(cb.equal(sqr.<String> get("userId"), root.<String> get("userId")),
                                    cb.and(subAcctJoin.<String> get("subAcctNo").in(ESubAcctNo.CURRENT.getCode(),
                                            ESubAcctNo.XWB.getCode())),
                                    cb.ge(cb.diff(
                                            cb.diff(cb.diff(subAcctJoin.<Number> get("bal"),
                                                    root.<Number> get("minBalance")),
                                                    subAcctJoin.<Number> get("freezableAmt")),
                                            subAcctJoin.<Number> get("reservedAmt")), criteria.getMinAmount())));
                    expressions.add(cb.exists(sq));
                }
                if (NumberUtil.isPositive(criteria.getMinBalance())) {
                    expressions.add(cb.ge(root.<Number> get("minBalance"), criteria.getMinBalance()));
                }
                if (NumberUtil.isPositive(criteria.getMaxSubscribeAmount())) {
                    expressions.add(cb.ge(root.<Number> get("maxSubscribeAmount"), criteria.getMaxSubscribeAmount()));
                }
                if (criteria.isAip()) {
                    Subquery<ProductPackage> sq = query.subquery(ProductPackage.class);
                    Root<ProductPackage> pkg = sq.from(ProductPackage.class);
                    Join<ProductPackage, SubscribeGroup> packageAIP = pkg.join("aipGroups");
                    Join<SubscribeGroup, SubscribeGroupUser> groupUser = packageAIP.join("users");
                    sq.select(pkg).where(
                            cb.and(cb.equal(pkg.<String> get("id"), criteria.getPackageId()),
                                    cb.equal(groupUser.<String> get("userId"), root.<String> get("userId"))));
                    expressions.add(cb.exists(sq));
                }

                /** fuzzy query for account id. **/
                Join<SimpleUserPo, AcctPo> acctJoin = root.join("user").join("account");
                if (accountId != null && !accountId.isEmpty()) {
                    expressions.add(cb.like(acctJoin.<String> get("acctNo"), "%" + accountId + "%"));
                }
                expressions.add(cb.equal(acctJoin.<String> get("acctStatus"), EAcctStatus.NORMAL.getCode()));

                Join<AutoSubscribeParam, InvestorInfo> investorJoin = root.join("investorInfo");
                expressions.add(cb.isNotNull(investorJoin.<String> get("investorLevel")));
                root.fetch("investorInfo");
                root.fetch("user").fetch("account").fetch("subAccounts");
                query.distinct(true);
                return predicate;
            }
        };
        List<AutoSubscribeParam> params = autoSubscribeParamRepository.findAll(specification);
        LOGGER.info(" Param size {} ", params.size());
        List<AutoSubscribeCandidateDto> candidates = convertToCandidates(params, criteria, totalAmount);
        return candidates;
    }

    @Transactional
    public void publish(String packageId, String userId) {
        LOGGER.info("publish invoked(), packageId:{}", packageId);
//        ProductPackage packagePo = this.productPackageRepository.findOne(packageId);
//        EPackageStatus status = packagePo.getStatus();
//        BigDecimal suppplyAmount = packagePo.getSupplyAmount();
//        BigDecimal quota = packagePo.getPackageQuota();
        this.productPackageRepository.updateAutoSubscribe(packageId, userId, new Date());
        this.productPackageRepository.flush();
//        if (EPackageStatus.WAIT_SUBSCRIBE == status || EPackageStatus.PRE_PUBLISH == status) {
//            MarketItemDto item = new MarketItemDto();
//            item.setId(packageId);
//            item.setPackageQuota(quota);
//            item.setSupplyAmount(suppplyAmount);
//            resourceService.addItem(item, true);
//        }
        //TODO 转移到 成交.
    }

    @SuppressWarnings("unchecked")
    private List<AutoSubscribeCandidateDto> convertToCandidates(List<AutoSubscribeParam> params,
            AutoSubscribeCriteriaDto criteria, BigDecimal totalAmount) {
        List<AutoSubscribeCandidateDto> candidates = new ArrayList<AutoSubscribeCandidateDto>();
        for (AutoSubscribeParam param : params) {
            AutoSubscribeCandidateDto candidate = new AutoSubscribeCandidateDto();
            candidate.setUserId(param.getUserId());
            candidate.setAccountId(param.getUser().getAccount().getAcctNo());
            candidate.setName(param.getUser().getName());
            BigDecimal availableBalance = BigDecimal.ZERO;
            for (SubAcctPo subAcct : param.getUser().getAccount().getSubAccounts()) {
                if (ESubAcctNo.CURRENT.getCode().equals(subAcct.getSubAcctNo())
                        || ESubAcctNo.XWB.getCode().equals(subAcct.getSubAcctNo())) {
                    BigDecimal temp = subAcct.getBal().subtract(subAcct.getFreezableAmt())
                            .subtract(subAcct.getReservedAmt());
                    availableBalance = availableBalance.add(temp.setScale(2, RoundingMode.HALF_UP));
                    LOGGER.info(" ACCOUNT {}  available balance {} ", subAcct.getSubAcctNo(), temp);
                }
            }
            candidate.setBalance(availableBalance);
            LOGGER.info("User ID {}, availableBalance {}", candidate.getUserId(), availableBalance);
            /** 资金账户保留额. **/
            if (param.getMinBalance() != null) {
                candidate.setMinBalance(param.getMinBalance().setScale(2, RoundingMode.HALF_UP));
            }
            /** 单笔申购最大金额. **/
            if (param.getMaxSubscribeAmount() != null) {
                candidate.setMaxBalance(param.getMaxSubscribeAmount().setScale(2, RoundingMode.HALF_UP));
            }
            candidate.setRiskParam(renderRiskParam(param.getRiskParam()));
            candidate.setPayMethodParam(renderRepaymentParam(param.getRepayment()));
            candidate.setWarrantyTypeParam(renderWarranty(param.getRiskGuarantee()));
            candidate.setMinDayCount(param.getMinDateRange());
            candidate.setMaxDayCount(param.getMaxDateRange());
            if (param.getMinAPRForDate() != null) {
                candidate.setMinDayRate(param.getMinAPRForDate().multiply(NumberUtil.getHundred())
                        .setScale(1, RoundingMode.HALF_UP));
            }
            candidate.setMinMonthCount(param.getMinMonthRange());
            candidate.setMaxMonthCount(param.getMaxMonthRange());
            if (param.getMinAPRForMonth() != null) {
                candidate.setMinMonthRate(param.getMinAPRForMonth().multiply(NumberUtil.getHundred())
                        .setScale(1, RoundingMode.HALF_UP));
            }
            candidate.setScore(param.getScore());

            if (criteria.getPackageId() != null) {
            	/** 替换原申购上下限. 添加开关控制. **/
            	BigDecimal maxAmount = null;
            	BigDecimal minAmount = null;
            	if (CommonBusinessUtil.isRiskActive()) {
            		PurchaseRiskResultDto limitation = limitationService.getSubscribeLimitation(param.getUserId(), criteria.getPackageId());
            		
            		if (!limitation.isQualified()) {
            			LOGGER.info("用户 {}因等级原因 不能购买高风险产品. ", param.getUserId());
            			continue;
            		}
            		
            		//最小申购额
            		minAmount = limitation.getLowerLimit();
            		if (limitation.getUpperLimit().compareTo(BigDecimal.ZERO) <= 0) {
            			/** 最大申购额  <= 0 , 忽略此候选人 . **/
            			LOGGER.info("用户 {} 最大申购额  {} <= 0,  忽略此候选人", new Object[] { param.getUserId(), limitation.getUpperLimit() });
            			continue;
					}
            		//最大申购额
            		maxAmount = candidate.getBalance().subtract(candidate.getMinBalance());
            		maxAmount = maxAmount.min(limitation.getUpperLimit());
            		if (NumberUtil.isPositive(param.getMaxSubscribeAmount())) {
                        maxAmount = maxAmount.min(param.getMaxSubscribeAmount());
                    }
				} else if (CommonBusinessUtil.isRiskActiveTemp()) {
            		PurchaseRiskResultDto limitation = limitationService.getSubscribeLimitationTemp(param.getUserId(), criteria.getPackageId());
            		if (!limitation.isQualified()) {
            			LOGGER.info("用户 {}因等级原因 不能购买高风险产品. ", param.getUserId());
            			continue;
            		}
            		//最小申购额
            		minAmount = limitation.getLowerLimit();
            		if (limitation.getUpperLimit().compareTo(BigDecimal.ZERO) <= 0) {
            			/** 最大申购额  <= 0 , 忽略此候选人 . **/
            			LOGGER.info("用户 {} 最大申购额  {} <= 0,  忽略此候选人", new Object[] { param.getUserId(), limitation.getUpperLimit() });
            			continue;
					}
            		//最大申购额
            		maxAmount = candidate.getBalance().subtract(candidate.getMinBalance());
            		maxAmount = maxAmount.min(limitation.getUpperLimit());
            		if (NumberUtil.isPositive(param.getMaxSubscribeAmount())) {
                        maxAmount = maxAmount.min(param.getMaxSubscribeAmount());
                    }
				} else {
	                BigDecimal maxSubscribeRate = CommonBusinessUtil.getMaxSubscribeRate(param.getInvestorInfo().getInvestorLevelOriginal());
	                // 最小申购额
	                /** Second step. 融资包额度/200、faceValue元的较大值. **/
	                minAmount = criteria.getMinAmount();
	                // candidate.setMinAmount(criteria.getMinAmount());
	                // 与（资金账户保留额）较大值
	                // .max(candidate.getMinBalance()).setScale(1, RoundingMode.HALF_UP));
	                // 最大申购额
	                // 1. 可用余额 - 用户设置的最小可用余额(资金账户保留额) = 最大可用于申购金额
	                maxAmount = candidate.getBalance().subtract(candidate.getMinBalance());
	                // 2. 基于用户投资等级的融资包最大可申购额
	                maxAmount = maxAmount.min(maxSubscribeRate.multiply(criteria.getTotalAmount()));
	                // 3. 用户设置的单笔申购最大金额
	                if (NumberUtil.isPositive(param.getMaxSubscribeAmount())) {
	                    maxAmount = maxAmount.min(param.getMaxSubscribeAmount());
	                }
				}
            	candidate.setMinAmount(minAmount);
                /** 成交金额. **/
                BigDecimal tempDealAmount = totalAmount.min(maxAmount).setScale(1, RoundingMode.HALF_UP);
                tempDealAmount = tempDealAmount.divideToIntegralValue(BigDecimal.ONE).setScale(0);
                //TODO 调整方法
                candidate.setDealAmount(SubscribeUtils.floorUnitFaceValue(tempDealAmount));
                if (candidate.getDealAmount().compareTo(BigDecimal.ZERO) <= 0) {
        			/** 成交金额  <= 0 , 忽略此候选人 . **/
        			LOGGER.info("用户 {} 成交金额{} <= 0,  忽略此候选人", new Object[] { param.getUserId(), candidate.getDealAmount() });
        			continue;
				}

                /** 重新设置 最大申购金额 MIN{可用余额-最小保留额, 风险投资限制(融资包金额 * 投资比例), 单笔申购最大金额} **/
                candidate.setMaxAmount(SubscribeUtils.floorUnitFaceValue(maxAmount));
            }
            if (candidate.getBalance().subtract(candidate.getMinBalance()).compareTo(candidate.getMinAmount()) < 0) {
                /** 用户 可用余额 - 资金账户保留额 < 最小申购额, 忽略此候选人 . **/
                LOGGER.info("用户 {} 可用余额 {} - 资金账户保留额 {} < 最小申购额  {} 忽略此候选人", new Object[] { param.getUserId(),
                        candidate.getBalance(), param.getMinBalance(), candidate.getMinAmount() });
                continue;
            }
            if (NumberUtil.isPositive(param.getMaxSubscribeAmount())) {
                if (param.getMaxSubscribeAmount().compareTo(candidate.getMinAmount()) < 0) {
                    /** 用户单笔最大申购额 < 最小申购额, 忽略此候选人. **/
                    LOGGER.info("用户 {} 单笔最大申购额 {} < 最小申购额  {} 忽略此候选人",
                            new Object[] { param.getUserId(), param.getMaxSubscribeAmount(), candidate.getMinAmount() });
                    continue;
                }
            }

            LOGGER.info("candidate {} ", candidate.toString());
            candidates.add(candidate);
        }
        Collections.sort(candidates, ComparatorUtils.chainedComparator(new AutoSubscribeScoreComparator(),
                new AutoSubscribeAmountComparator()));
        LOGGER.info("{} candidates found", candidates.size());
        return candidates;
    }

    private List<AutoSubscribeResultDto> drawCandidates(List<AutoSubscribeCandidateDto> candidates,
            BigDecimal totalAmount) {
        List<AutoSubscribeResultDto> results = new ArrayList<AutoSubscribeResultDto>();
        BigDecimal left = totalAmount;
        int i = 0;
        for (AutoSubscribeCandidateDto candidate : candidates) {
            AutoSubscribeResultDto result = new AutoSubscribeResultDto(candidate);
            result.setName(candidate.getName());
            result.setDealAmount(left.min(candidate.getDealAmount()).setScale(1, RoundingMode.HALF_UP));
            result.setMinBalance(candidate.getMinBalance().setScale(1, RoundingMode.HALF_UP));
            result.setMaxBalance(candidate.getMaxBalance().setScale(1, RoundingMode.HALF_UP));
            left = left.subtract(result.getDealAmount());
            results.add(result);
            LOGGER.info("{}th lucky one: {}", ++i, result);
            if (!NumberUtil.isPositive(left)) {
                LOGGER.info("Achieve max balance");
                break;
            }
            /** 200 Upper limit logic. **/
            if (i == getMaxUserPerPkg().intValue()) {
                LOGGER.info("Achieve max upper limit");
                break;
            }
        }
        LOGGER.info("{} subscribed successfully out of {}", totalAmount.subtract(left), totalAmount);
        return results;
    }

    private String renderRiskParam(String riskParam) {
        if (riskParam == null) {
            return null;
        }
        String[] riskParams = riskParam.split("\\,");
        StringBuilder sb = new StringBuilder();
        for (String risk : riskParams) {
            if (risk.equalsIgnoreCase(ERiskLevel.HIGH_QUALITY.getCode())) {
                sb.append(ERiskLevel.HIGH_QUALITY.getLabel()).append(", ");
            } else if (risk.equalsIgnoreCase(ERiskLevel.MEDIUM.getCode())) {
                sb.append(ERiskLevel.MEDIUM.getLabel()).append(", ");
            } else if (risk.equalsIgnoreCase(ERiskLevel.ACCEPTABLE.getCode())) {
                sb.append(ERiskLevel.ACCEPTABLE.getLabel()).append(", ");
            } else if (risk.equalsIgnoreCase(ERiskLevel.HIGH_RISK.getCode())) {
                sb.append(ERiskLevel.HIGH_RISK.getLabel()).append(", ");
            }
        }
        return sb.toString();
    }

    private String renderRepaymentParam(String param) {
        if (param == null) {
            return null;
        }
        String[] params = param.split("\\,");
        StringBuilder sb = new StringBuilder();
        for (String repay : params) {
            if (repay.equalsIgnoreCase(EPayMethodType.MONTH_AVERAGE_INTEREST.getCode())) {
                sb.append(EPayMethodType.MONTH_AVERAGE_INTEREST.getLabel()).append(", ");
            } else if (repay.equalsIgnoreCase(EPayMethodType.MONTH_PRINCIPAL_INTEREST.getCode())) {
                sb.append(EPayMethodType.MONTH_PRINCIPAL_INTEREST.getLabel()).append(", ");
            } else if (repay.equalsIgnoreCase(EPayMethodType.MONTH_INTEREST.getCode())) {
                sb.append(EPayMethodType.MONTH_INTEREST.getLabel()).append(", ");
            } else if (repay.equalsIgnoreCase(EPayMethodType.ONCE_FOR_ALL.getCode())) {
                sb.append(EPayMethodType.ONCE_FOR_ALL.getLabel()).append(", ");
            }
        }
        return sb.toString();
    }

    private String renderWarranty(String param) {
        if (param == null) {
            return null;
        }
        String[] params = param.split("\\,");
        StringBuilder sb = new StringBuilder();
        for (String guarantee : params) {
            if (guarantee.equalsIgnoreCase(EWarrantyType.PRINCIPAL.getCode())) {
                sb.append(EWarrantyType.PRINCIPAL.getLabel()).append(", ");
            } else if (guarantee.equalsIgnoreCase(EWarrantyType.PRINCIPALINTEREST.getCode())) {
                sb.append(EWarrantyType.PRINCIPALINTEREST.getLabel()).append(", ");
            } else if (guarantee.equalsIgnoreCase(EWarrantyType.NOTHING.getCode())) {
                sb.append(EWarrantyType.NOTHING.getLabel()).append(", ");
            } else if (guarantee.equalsIgnoreCase(EWarrantyType.MONITORASSETS.getCode())) {
                sb.append(EWarrantyType.MONITORASSETS.getLabel()).append(", ");
            }
        }
        return sb.toString();
    }

}
