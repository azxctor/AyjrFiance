package com.hengxin.platform.market.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.broker.SimpleBrokerMessageHandler;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hengxin.platform.common.constant.ApplicationConstant;
import com.hengxin.platform.common.constant.UrlConstant;
import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.common.util.MaskUtil;
import com.hengxin.platform.common.util.MessageUtil;
import com.hengxin.platform.common.util.NumberUtil;
import com.hengxin.platform.fund.service.FundAcctBalService;
import com.hengxin.platform.job.bean.PackageStatusSyncJob;
import com.hengxin.platform.market.dto.MarketItemDto;
import com.hengxin.platform.market.dto.downstream.FinancingMarketItemDetailDto;
import com.hengxin.platform.market.dto.upstream.FinancingMarketHistorySearchDto;
import com.hengxin.platform.market.dto.upstream.FinancingMarketPurchaseDto;
import com.hengxin.platform.market.dto.upstream.FinancingMarketSearchDto;
import com.hengxin.platform.market.dto.upstream.FinancingMarketPurchaseDto.PurchasePackage;
import com.hengxin.platform.market.service.FinancingMarketService;
import com.hengxin.platform.market.service.FinancingResourceService;
import com.hengxin.platform.market.service.PackageSubscirbeService;
import com.hengxin.platform.market.utils.SubscribeUtils;
import com.hengxin.platform.member.domain.Agency;
import com.hengxin.platform.member.service.MemberService;
import com.hengxin.platform.product.domain.ProductPackage;
import com.hengxin.platform.product.dto.ProductDetailsDto;
import com.hengxin.platform.product.dto.ProductPackageDto;
import com.hengxin.platform.product.enums.EPackageStatus;
import com.hengxin.platform.product.enums.EWarrantyType;
import com.hengxin.platform.risk.service.InvestorPurchaseLimitationService;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;

@Controller
public class FinancingMarketController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FinancingMarketController.class);

    @Autowired
    private SimpleBrokerMessageHandler messageHandler;
    @Autowired
    private SimpMessagingTemplate template;
    private final Map<String, List<String>> topicMap = new ConcurrentHashMap<String, List<String>>();

    @Autowired
    private transient FinancingResourceService resourceService;

    @Autowired
    private transient FinancingMarketService financingMarketService;

    @Autowired
    private transient FundAcctBalService fundAccoutService;

    @Autowired
    private transient PackageSubscirbeService subcribeService;

    @Autowired
    private transient SecurityContext securityContext;

    @Autowired
    private LocalValidatorFactoryBean validator;

    @Autowired
    private transient InvestorPurchaseLimitationService limitationService;

    @Autowired
    private MemberService memberService;

    @PostConstruct
    public void init() {
        new PackageStatusSyncJob(resourceService, subcribeService).run();
        List<MarketItemDto> allItems = financingMarketService.getAllItems();
        for (MarketItemDto item : allItems) {
            resourceService.addItem(item, true);
        }
    }

    @RequestMapping(value = UrlConstant.FINANCING_MARKETING_VIEW_URL, method = RequestMethod.GET)
    @RequiresPermissions(value = Permissions.MARKETING_CREDITOR_VIEW)
    public String home(Model model) {
        LOGGER.debug("home() invoked");
        model.addAttribute("marketClose", !CommonBusinessUtil.isMarketOpen());
        model.addAttribute("hideSub", !securityContext.isInvestorFinancierTourist() 
        			&& !securityContext.isInvestor()
        			&& !securityContext.isFinancier());
        model.addAttribute("investorFlag", securityContext.isInvestor());
        return "market/market_financing";
    }

    @ResponseBody
    @RequiresPermissions(value = Permissions.SYSTEM_MANAGEMENT_CACHE_MANAGEMENT)
    @RequestMapping(value = "market/financing/refreshall", method = RequestMethod.POST)
    public ResultDto refreshPackages() {
        init();
        return ResultDtoFactory.toAck("刷新成功");
    }

    @ResponseBody
    @RequiresPermissions(value = Permissions.SYSTEM_MANAGEMENT_CACHE_MANAGEMENT)
    @RequestMapping(value = "market/financing/refreshone", method = RequestMethod.POST)
    public ResultDto refreshPackage(@RequestParam String pkgId) {
    	Object[] row = subcribeService.getSubscribeSumDetail().get(pkgId);
    	if (row != null) {
    		subcribeService.updatePackageDetail(row);
    	}
    	MarketItemDto item = financingMarketService.getItemById(pkgId);
    	if (item != null) {
    		resourceService.addItem(item, true);
    	} else {
    		throw new BizServiceException(EErrorCode.SUB_PACKAGE_NOT_FOUND);
    	}
        return ResultDtoFactory.toAck("刷新成功");
    }

    @ResponseBody
    @RequestMapping(value = "market/financing/search")
    @RequiresPermissions(value = Permissions.MARKETING_CREDITOR_VIEW)
    public DataTablesResponseDto<MarketItemDto> search(@RequestBody FinancingMarketSearchDto request) {
        LOGGER.debug("search() invoked");
        boolean isPlatformUser = securityContext.isPlatformUser();
        DataTablesResponseDto<MarketItemDto> resp = financingMarketService.getFinancingMarketItems(request,
                securityContext.getCurrentUserId(), isPlatformUser);
        resp.setEcho(request.getEcho());
        return resp;
    }

	@RequestMapping(value = UrlConstant.FINANCING_MARKETING_HISTORY_VIEW_URL, method = RequestMethod.GET)
	@RequiresPermissions(value = Permissions.MARKETING_CREDITOR_VIEW)
	public String historyHome(Model model) {
		LOGGER.debug("home() invoked");
		boolean canPutBigScreen = false;
		if(securityContext.isPlatformUser() || securityContext.isAuthServiceCenter() || securityContext.isProdServ()){
			canPutBigScreen = true;
		}
		model.addAttribute("canPutBigScreen",canPutBigScreen);
		return "market/market_financing_history";
	}
	
	@RequestMapping(value = "market/financing/history/bigscreen", method = RequestMethod.GET)
	@RequiresPermissions(value = Permissions.MARKETING_CREDITOR_VIEW)
	public String historyBigScreenHome(Model model) {
		LOGGER.debug("home() historyBigScreenHome");
		boolean isPlatformUser = securityContext.isPlatformUser();
		DataTablesResponseDto<MarketItemDto> resp = financingMarketService
				.getFinancingMarketHistoryItems(securityContext.getCurrentUserId(), isPlatformUser);
		ObjectMapper mapper = new ObjectMapper();
		String resStr = null;
		try {
			resStr = mapper.writeValueAsString(resp.getData());
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		model.addAttribute("bigScreenData",resp.getData());
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("============================="+resStr);
		}
		return "market/market_financing_history_bigscreen";
	}
	

	@ResponseBody
	@RequestMapping(value = "market/financing/history/search")
	@RequiresPermissions(value = Permissions.MARKETING_CREDITOR_VIEW)
	public DataTablesResponseDto<MarketItemDto> historySearch(
			@RequestBody FinancingMarketHistorySearchDto request) {
		LOGGER.debug("historySearch() invoked");
		boolean isPlatformUser = securityContext.isPlatformUser();
		DataTablesResponseDto<MarketItemDto> resp = financingMarketService
				.getFinancingMarketHistoryItems(request,
						securityContext.getCurrentUserId(), isPlatformUser);
		resp.setEcho(request.getEcho());
		return resp;
	}

    @RequestMapping(value = "market/financing/detail/{id}", method = RequestMethod.GET)
    @RequiresPermissions(value = Permissions.MARKETING_CREDITOR_VIEW)
    public String getDetailPage(@PathVariable(value = "id") String id, HttpServletRequest request, HttpSession session,
            Model model) {
        LOGGER.debug("getDetailPage() invoked");
        FinancingMarketItemDetailDto itemDto = getItemBaseInfo(id);
        ProductPackage pkg = financingMarketService.getPackageDetail(id);
        if (pkg != null) {
            itemDto.setPkg(ConverterService.convert(pkg, ProductPackageDto.class));
            ProductDetailsDto productDetail = financingMarketService.getProductDetail(pkg.getProductId());
            if (productDetail.getWarrantyType() == EWarrantyType.MONITORASSETS
                    || productDetail.getWarrantyType() == EWarrantyType.NOTHING) {
                productDetail.setGuaranteeInstitution("--");
                productDetail.setGuaranteeInstitutionShow("--");
            } else if (StringUtils.isNotBlank(productDetail.getWarrantIdShow())) {
                Agency agency = memberService.getAgencyByUserId(productDetail.getWarrantIdShow());
                productDetail.setGuaranteeLicenseNoImg(agency != null ? agency.getLicenceNoImg() : "");
            }

            if (this.securityContext.hasPermission(Permissions.RISK_CONTROL_DEPT)) {
                productDetail.getUser().setMaskChineseName(productDetail.getUser().getName());
            } else {
                productDetail.getUser().setMaskChineseName(MaskUtil.maskChinsesName(productDetail.getUser().getName()));
            }

            itemDto.setProduct(productDetail);
        }
        boolean isUserTourist = securityContext.isInvestorFinancierTourist() || securityContext.isInvestor()
                || securityContext.isFinancier();
        model.addAttribute("itemDto", itemDto);
        model.addAttribute("marketClose", !CommonBusinessUtil.isMarketOpen());
        model.addAttribute("isUserTourist", isUserTourist);
        model.addAttribute("investorFlag", securityContext.isInvestor());
        return "market/market_financing_detail";
    }

    @ResponseBody
    @RequestMapping(value = "market/financing/purchase/{id}", method = RequestMethod.GET)
    @RequiresPermissions(value = Permissions.MARKETING_CREDITOR_VIEW)
    public FinancingMarketItemDetailDto getPurchaseInfo(@PathVariable(value = "id") String id) {
        LOGGER.debug("getPurchaseInfo() invoked");
        return getItemBaseInfo(id);
    }

    public FinancingMarketItemDetailDto getItemBaseInfo(String id) {
        ProductPackage item = financingMarketService.getPackageDetail(id);
        String userId = securityContext.getCurrentUserId();
        FinancingMarketItemDetailDto resp = subcribeService.getLatestSubcribeAmount(id, userId);
        ProductDetailsDto product = new ProductDetailsDto();
        try {
            if (item != null) {
                resp.setRemainingTime(item.getSupplyEndTime().getTime() - System.currentTimeMillis());
                resp.setRemainingStartTime(item.getSupplyStartTime().getTime() - System.currentTimeMillis());

                BigDecimal progress = item.getSupplyAmount().divide(item.getPackageQuota(), 3, RoundingMode.DOWN);
                if (item.getStatus() == EPackageStatus.SUBSCRIBE) {
                    MarketItemDto itemCache = resourceService.getItem(item.getId());
                    if (itemCache == null) {
                        LOGGER.info("package does NOT exists in cache: {}", item.getId());
                    } else {
                        progress = itemCache.getProgress();
                    }
                }
                resp.setProgress(SubscribeUtils.formatSubsProgress(progress));
                product.setRate(item.getProduct().getRate());
                product.setTermToDays(item.getProduct().getTermToDays());
                product.setPayMethod(item.getProduct().getPayMethod());
                product.setTermLength(item.getProduct().getTermLength());
                if (item.getStatus() == EPackageStatus.WAIT_SUBSCRIBE || item.getStatus() == EPackageStatus.PRE_PUBLISH) {
                    resp.setSubscribed(false);
                    if (EPackageStatus.WAIT_SUBSCRIBE == item.getStatus()) {
                    	/** When WaitSubscribe, display limitation and benefit, enable input. **/
                    	resp.setInputable(true);
					} else {
						resp.setInputable(false);
						resp.setReason(MessageUtil.getMessage("market.error.subsribe.notstart"));
					}
                } else if (item.getStatus() != EPackageStatus.SUBSCRIBE) {
                	resp.setExpired(true);
                    resp.setSubscribed(false);
                    resp.setInputable(false);
                    resp.setReason(MessageUtil.getMessage("market.error.subscribe.haspassed"));
                }
                // 不能申购自己发布的融资包
                if (securityContext.getCurrentUserId().equalsIgnoreCase(item.getProduct().getUser().getUserId())) {
                    resp.setSubscribed(false);
                    resp.setInputable(false);
                    resp.setReason(MessageUtil.getMessage("market.error.subscirbe.youown"));
                }
            } else {
                LOGGER.info("Financing market item not found: " + id);
            }
            if (resp.getBalance().compareTo(resp.getMinSubscribeAmount()) < 0) {
                resp.setSubscribed(false);
                resp.setInputable(false);
				resp.setReason(MessageFormat.format(
						MessageUtil.getMessage("market.error.balance.lack2"),
						resp.getMinSubscribeAmount().setScale(2, RoundingMode.DOWN).toString()));
            }
        } catch (BizServiceException ex) {
            resp.setSubscribed(false);
            resp.setInputable(false);
            resp.setReason(ex.getMessage());
        }

        if (!securityContext.isInvestor()) {
            resp.setSubscribed(false);
            resp.setInputable(false);
            resp.setReason(MessageUtil.getMessage("market.error.subscribe.permissions.nothave"));
        }
        /**
         * Requirement change : Display subscriber limitation and benefit when market is closed.
         * else if (!CommonBusinessUtil.isMarketOpen()) {
         *   resp.setSubscribed(false);
         *   resp.setReason(MessageUtil.getMessage("market.error.market.close"));
         * }
         */
        resp.setProduct(product);
        return resp;
    }

    @ResponseBody
    @RequestMapping(value = "market/financing/purchase", method = RequestMethod.POST)
    @RequiresPermissions(value = Permissions.MARKETING_CREDITOR_PURCHASE)
    public ResultDto purchase(@RequestBody FinancingMarketPurchaseDto request) {
        LOGGER.debug("purchase() invoked");
        long start = System.currentTimeMillis();
        long remain = resourceService.get(request.getId());
        if (remain == 0L) {
            LOGGER.info("[MARKET]failure, package: {}, error: {}", request.getId(),
                    "market.error.subscribe.package.full");
            return ResultDtoFactory.toNack(MessageUtil.getMessage("market.error.subscribe.package.full"));
        }
        // 判断是否开市
        if (!CommonBusinessUtil.isMarketOpen()) {
            LOGGER.info("[MARKET]failure, package: {}, error: {}", request.getId(),
                    "market.error.market.close");
            return ResultDtoFactory.toNack(MessageUtil.getMessage("market.error.market.close"));
        }
        // 验证投资会员
        if (!securityContext.isInvestor()) {
            LOGGER.info("[MARKET]failure, package: {}, error: {}", request.getId(),
                    "market.error.subscribe.permissions.nothave");
            return ResultDtoFactory.toNack(MessageUtil.getMessage("market.error.subscribe.permissions.nothave"));
        }
        // 验证融资包
        MarketItemDto item = resourceService.getItem(request.getId());
        if (item == null || item.getStatus() != EPackageStatus.SUBSCRIBE) {
            LOGGER.info("[MARKET]failure, package: {}, error: {}", request.getId(),
                    "market.error.subscribe.package.notfound");
            return ResultDtoFactory.toNack(MessageUtil.getMessage("market.error.subscribe.package.notfound"));
        }
        // 验证申购金额
        long validationStart = System.currentTimeMillis();
        final Set<ConstraintViolation<FinancingMarketPurchaseDto>> constraintViolations = validator.validate(request,
                PurchasePackage.class);
        LOGGER.info("[MARKET]validate() completed in {}ms", System.currentTimeMillis() - validationStart);
        if (!constraintViolations.isEmpty()) {
        	String msg = constraintViolations.iterator().next().getMessage();
            LOGGER.info("[MARKET]failure, package: {}, error: {}", "validation error", msg);
            return ResultDtoFactory.toNack(msg);
        }
        // 从内存池扣除
        long purchaseAmount = request.getAmount().longValue();
        long consume = resourceService.consume(item.getId(), purchaseAmount);
        if (consume <= 0L) {
            LOGGER.info("[MARKET]failure, package: {}, error: {}", request.getId(),
                    "market.error.subscribe.package.full");
            return ResultDtoFactory.toNack(MessageUtil.getMessage("market.error.subscribe.package.full"));
        } else {
            try {
                long purchaseStart = System.currentTimeMillis();
                // 申购相关表insert
                subcribeService.purchase(securityContext.getCurrentUserId(), item, consume);
                LOGGER.info("[MARKET]purchase() completed in {} ms", System.currentTimeMillis() - purchaseStart);
            } catch (BizServiceException se) {
                resourceService.offer(item.getId(), consume);
                LOGGER.info("[MARKET]failure, package: {}, error: {}", request.getId(), se.getError());
                LOGGER.info("[MARKET]offer: purchased {}, returned {}", purchaseAmount, consume);
                throw se;
            } catch (Exception se) {
                resourceService.offer(item.getId(), consume);
                LOGGER.info("[MARKET]failure, package: {}, error: {}", request.getId(), se);
                LOGGER.info("[MARKET]offer: purchased {}, returned {}", purchaseAmount, consume);
                throw new RuntimeException(se);
            }

        }
        /** Check optional limitation on same product category. **/
        boolean warning = false;
        if (CommonBusinessUtil.isRiskActive()) {
        	warning = limitationService.checkWarningOnCategory(securityContext.getCurrentUserId(), request.getId(), request.getAmount());	
		}
        String message = "";
        if (purchaseAmount > consume) {
            // 部分成交
        	if (warning) {
        		message = MessageUtil.getMessage("market.subscirbe.warning.partdeal",
                        NumberUtil.formatMoney(purchaseAmount), NumberUtil.formatMoney(consume));	
			} else {
	            message = MessageUtil.getMessage("market.subscirbe.success.partdeal",
	            		NumberUtil.formatMoney(purchaseAmount), NumberUtil.formatMoney(consume));	
			}
        } else {
            // 全部成交
        	if (warning) {
        		message = MessageUtil.getMessage("market.subscirbe.warning.alldeal",
        				NumberUtil.formatMoney(purchaseAmount));
			} else {
				message = MessageUtil.getMessage("market.subscirbe.success.alldeal",
						NumberUtil.formatMoney(purchaseAmount));
			}
        }
        LOGGER.info("[MARKET]success, package: {}, completed in {}ms", request.getId(),
                System.currentTimeMillis() - start);
        return ResultDtoFactory.toAck(message);
    }

    @ResponseBody
    @RequestMapping(value = "market/financing/subscribe", method = RequestMethod.POST)
    public ResultDto subscribe(@RequestBody List<String> target) {
        LOGGER.debug("subscribe() invoked");
        if (target == null) {
            return new ResultDto();
        }
        for (Iterator<String> itr = target.iterator(); itr.hasNext();) {
            if (StringUtils.isEmpty(itr.next())) {
                itr.remove();
            }
        }
        if (target.isEmpty()) {
            return new ResultDto();
        }
        Collections.sort(target);
        String token = DigestUtils.md5DigestAsHex(StringUtils.join(target.toArray(), ',').getBytes());
        if (!topicMap.containsKey(token)) {
            topicMap.put(token, target);
        }
        return new ResultDto(null, null, token);

    }

    @Scheduled(cron = "1/3 * * * * ?")
    public void pushTopics() {
        MDC.put(ApplicationConstant.LOG_JOB, "PushTopicsJob");
        Map<String, MarketItemDto> sourceMap = resourceService.getAsMap();
        for (Entry<String, List<String>> entry : new HashSet<Entry<String, List<String>>>(topicMap.entrySet())) {
            String token = entry.getKey();
            List<String> ids = entry.getValue();
            Map<String, Object> resp = new HashMap<String, Object>();
            List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
            if (CommonBusinessUtil.isMarketOpen()) {
	            for (String id : ids) {
	                if (id != null) {
	                    MarketItemDto item = sourceMap.get(id);
	                    if (item != null) {
	                        Map<String, Object> map = new HashMap<String, Object>();
	                        map.put("id", id);
	                        map.put("progress", item.getProgress());
	                        map.put("status", item.getStatus().getText());
	                        data.add(map);
	                    }
	                }
	            }
            }
            resp.put("data", data);
            resp.put("status", !CommonBusinessUtil.isMarketOpen());
            String topic = convertToTopic(token);
            LOGGER.trace("sending data to topic: {}", topic);
            template.convertAndSend(topic, resp);
        }
        MDC.remove(ApplicationConstant.LOG_JOB);
    }

    /**
     * Remove disconnected topics.
     */
    @Scheduled(fixedDelay = 60000)
    public void purgeTopic() {
        MDC.put(ApplicationConstant.LOG_JOB, "PurgeTopicJob");
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        for (String token : new HashSet<String>(topicMap.keySet())) {
            headers.setDestination(convertToTopic(token));
            Message<?> message = MessageBuilder.withPayload(new Object()).setHeaders(headers).build();
            Map<?, ?> subs = messageHandler.getSubscriptionRegistry().findSubscriptions(message);
            if (subs.isEmpty()) {
                topicMap.remove(token);
                LOGGER.debug("removed disconnected topic: {}", token);
            }
        }
        MDC.remove(ApplicationConstant.LOG_JOB);
    }

    private String convertToTopic(String token) {
        return new StringBuilder("/topic/market/financing/").append(token).toString();
    }

    public void setFinancingMarketService(FinancingMarketService financingMarketService) {
        this.financingMarketService = financingMarketService;
    }

    public void setResourceService(FinancingResourceService resourceService) {
        this.resourceService = resourceService;
    }

    public void setSecurityContext(SecurityContext securityContext) {
        this.securityContext = securityContext;
    }

    public void setSubcribeService(PackageSubscirbeService subcribeService) {
        this.subcribeService = subcribeService;
    }

    public void setValidator(LocalValidatorFactoryBean validator) {
        this.validator = validator;
    }

    public void setInvestorPurchaseLimitationService(InvestorPurchaseLimitationService limitationService) {
        this.limitationService = limitationService;
    }

}
