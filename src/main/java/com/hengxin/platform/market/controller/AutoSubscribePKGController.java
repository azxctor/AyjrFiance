package com.hengxin.platform.market.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hengxin.platform.batch.service.JobWorkService;
import com.hengxin.platform.common.constant.UrlConstant;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.common.util.MessageUtil;
import com.hengxin.platform.common.util.NumberUtil;
import com.hengxin.platform.market.dto.AutoSubscribeCandidateDto;
import com.hengxin.platform.market.dto.AutoSubscribeResultDto;
import com.hengxin.platform.market.dto.AutoSubscribeSearchDto;
import com.hengxin.platform.market.dto.AutoSubscriberFinancingPackageDto;
import com.hengxin.platform.market.dto.CandidateCriteria;
import com.hengxin.platform.market.dto.SkinnyDrawDto;
import com.hengxin.platform.market.service.AutoSubscribeService;
import com.hengxin.platform.market.utils.SubscribeUtils;
import com.hengxin.platform.product.domain.ProductPackage;
import com.hengxin.platform.product.enums.EAutoSubscribeStatus;
import com.hengxin.platform.product.enums.EPackageStatus;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;

@Controller
public class AutoSubscribePKGController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AutoSubscribePKGController.class);

    @Autowired
    private transient AutoSubscribeService autoSubscribeService;

    @Autowired
    private transient SecurityContext securityContext;
    
    @Autowired
    private transient JobWorkService jobWorkService;

    @RequestMapping(value=UrlConstant.FINANCING_MARKETING_AUTO_SUBSCRIBE_HOME_URL, method = RequestMethod.GET)
    @RequiresPermissions(value=Permissions.TRANS_MANAGEMENT_AUTO_SUBS_PACKAGE_FILTER)
    public String home() {
        LOGGER.debug("home() invoked");
        return "market/market_auto_subscribe";
    }
    
    @RequestMapping(value=UrlConstant.FINANCING_MARKETING_AUTO_SUBSCRIBE_VIEW_URL)
    @RequiresPermissions(value=Permissions.PRODUCT_FINANCING_PACKAGE_VIEW)
    @ResponseBody
    public DataTablesResponseDto<AutoSubscriberFinancingPackageDto> getSubscribeList(HttpServletRequest request, Model model,
            @RequestBody AutoSubscribeSearchDto searchCriteria) {
    	LOGGER.info("getSubscribeList() invoked");
    	DataTablesResponseDto<AutoSubscriberFinancingPackageDto> packages = autoSubscribeService.findAutoSubscribePackages(searchCriteria);
		return packages;
    }
    
    /**
     * Jump to candidate page, init data.
     * @param model
     * @param packageId
     * @return
     */
    @RequestMapping(value=UrlConstant.FINANCING_MARKETING_AUTO_SUBSCRIBE_CANDIDATES_URL, method = RequestMethod.GET)
    @RequiresPermissions(value=Permissions.PRODUCT_FINANCING_PACKAGE_VIEW)
    @ResponseStatus(value=HttpStatus.PARTIAL_CONTENT)
    public String viewCandidates(Model model, @PathVariable("packageId") String packageId) {
    	AutoSubscribeSearchDto criteria = new AutoSubscribeSearchDto();
    	criteria.setPackageId(packageId);
    	List<AutoSubscribeCandidateDto> candidates = this.autoSubscribeService.findCandidates(criteria);
    	BigDecimal availableAmount = BigDecimal.ZERO;
    	BigDecimal summarizedAvailableAmount = BigDecimal.ZERO;
    	BigDecimal dealAmount = BigDecimal.ZERO;
    	BigDecimal unitFaceValue = SubscribeUtils.getUnitFaceValue();
    	for (AutoSubscribeCandidateDto candidate : candidates) {
    		/** 可用金额. **/
    		availableAmount = availableAmount.add(candidate.getBalance());
    		BigDecimal summarizedAmount = candidate.getBalance().divideToIntegralValue(unitFaceValue).setScale(0).multiply(unitFaceValue);
    		summarizedAvailableAmount = summarizedAvailableAmount.add(summarizedAmount);
    		/** 委托金额. **/
    		dealAmount = dealAmount.add(candidate.getDealAmount());
		}
    	ProductPackage packageInfo = this.autoSubscribeService.findPackage(packageId);
    	SkinnyDrawDto drawDto = new SkinnyDrawDto();
    	drawDto.setPackageId(packageId);
    	drawDto.setPackageName(packageInfo.getPackageName());
    	drawDto.setAvailableAmountDesc(NumberUtil.formatMoney(availableAmount.setScale(2, RoundingMode.HALF_UP)));
    	drawDto.setSummarizedAmountDesc(NumberUtil.formatMoney(summarizedAvailableAmount.setScale(2, RoundingMode.HALF_UP)));
    	drawDto.setDealAmountDesc(NumberUtil.formatMoney(dealAmount.setScale(2, RoundingMode.HALF_UP)));
    	drawDto.setCandidatesNumber(candidates.size());
    	model.addAttribute("bannerInfo", drawDto);
    	return "market/market_candidates";
    }
    
    /**
     * Render data table.
     * @param request
     * @param model
     * @param searchCriteria
     * @return
     * @see viewCandidates
     */
    @RequestMapping(value=UrlConstant.FINANCING_MARKETING_AUTO_SUBSCRIBE_CANDIDATES_LIST_URL)
    @RequiresPermissions(value=Permissions.PRODUCT_FINANCING_PACKAGE_VIEW)
    @ResponseBody
    public DataTablesResponseDto<AutoSubscribeCandidateDto> getCandidateList(
    		@RequestBody AutoSubscribeSearchDto criteria, HttpServletRequest request, Model model) {
    	LOGGER.info("getdCandidateList() invoked");
    	List<AutoSubscribeCandidateDto> candidates = this.autoSubscribeService.findCandidates(criteria);
        DataTablesResponseDto<AutoSubscribeCandidateDto> results = new DataTablesResponseDto<AutoSubscribeCandidateDto>();
        results.setEcho(criteria.getEcho());
        results.setData(candidates);
        results.setTotalDisplayRecords(results.getData().size());
        results.setTotalRecords(results.getData().size());
		return results;
    }
    
    /**
     * Jump to draw page, init data.
     * @param model
     * @param packageId
     * @param totalAmount
     * @param availableAmount
     * @return
     */
    @RequestMapping(value=UrlConstant.FINANCING_MARKETING_AUTO_SUBSCRIBE_DRAW_URL, method = RequestMethod.POST)
    @RequiresPermissions(value=Permissions.PRODUCT_FINANCING_PACKAGE_VIEW)
    @ResponseStatus(value=HttpStatus.PARTIAL_CONTENT)
    public String viewDraw(@RequestBody SkinnyDrawDto drawDto,Model model) {
    	ProductPackage packageInfo = this.autoSubscribeService.findPackage(drawDto.getPackageId());
    	if (drawDto.getTotalAmount() != null && drawDto.getAvailableAmount() != null) {
			drawDto.setDealAmount(drawDto.getTotalAmount().subtract(drawDto.getAvailableAmount()));
		}
    	drawDto.setTotalAmountDesc(NumberUtil.formatMoney(drawDto.getTotalAmount().setScale(2, RoundingMode.HALF_UP)));
    	drawDto.setAvailableAmountDesc(NumberUtil.formatMoney(drawDto.getAvailableAmount().setScale(2, RoundingMode.HALF_UP)));
    	drawDto.setDealAmountDesc(NumberUtil.formatMoney(drawDto.getDealAmount().setScale(2, RoundingMode.HALF_UP)));
    	drawDto.setPackageName(packageInfo.getPackageName());
    	model.addAttribute("bannerInfo", drawDto);
    	model.addAttribute("dto", drawDto);
    	
    	/** enable bargain button. **/
    	boolean enable = false;
    	if (EPackageStatus.PRE_PUBLISH == packageInfo.getStatus()
    			|| EPackageStatus.WAIT_SUBSCRIBE == packageInfo.getStatus()
    			|| EPackageStatus.SUBSCRIBE == packageInfo.getStatus()) {
    		/** check whether it has been bargained and published. **/
    		if (EAutoSubscribeStatus.PUBLISH == packageInfo.getAutoSubscribeFlag()){
    			enable = false;	
    		} else {
    			enable = !this.autoSubscribeService.isDealt(drawDto.getPackageId());
    		}
    	}
    	LOGGER.info("enable {}", enable);
//    	boolean open = CommonBusinessUtil.isMarketOpen();
    	/** Get GL_BIZ_SIGN info directly instead of invoke above common business service due to cache issue. **/
    	boolean open = this.autoSubscribeService.getBusinessSigninStatus();
    	LOGGER.info("open ============= {}", open);
    	enable = enable && !open;
    	/** check whether job is running or not. **/
    	if (enable) {
    		boolean flag = jobWorkService.isBatchBizTaskProcessing();
    		LOGGER.info("Batch processing ============= {}", flag);
    		enable = !flag;
		}
    	LOGGER.info("enable {}", enable);
    	model.addAttribute("enable", enable);
    	return "market/market_auto_subscribe_draw";
    }
    
    /**
     * Render data table.
     * @param request
     * @param model
     * @param searchCriteria
     * @return
     * @see viewDraw
     */
    @RequestMapping(value=UrlConstant.FINANCING_MARKETING_AUTO_SUBSCRIBE_DRAW_LIST_URL)
    @RequiresPermissions(value=Permissions.PRODUCT_FINANCING_PACKAGE_VIEW)
    @ResponseBody
    public DataTablesResponseDto<AutoSubscribeResultDto> getDrawList(HttpServletRequest request, Model model,
            @RequestBody AutoSubscribeSearchDto criteria) {
    	LOGGER.info("getdDrawList() invoked");
    	List<AutoSubscribeResultDto> draws = this.autoSubscribeService.autoSubscribe(criteria.getPackageId(), false, criteria.getAccountId(), null);
    	DataTablesResponseDto<AutoSubscribeResultDto> results = new DataTablesResponseDto<AutoSubscribeResultDto>();
        results.setEcho(criteria.getEcho());
        results.setData(draws);
        results.setTotalDisplayRecords(results.getData().size());
        results.setTotalRecords(results.getData().size());
    	return results;
    }
    
    /**
     * bargain.
     * @param packageId
     * @return
     */
    @RequestMapping(value=UrlConstant.FINANCING_MARKETING_AUTO_SUBSCRIBE_DRAW_DONE_URL, method = RequestMethod.POST)
    @RequiresPermissions(value=Permissions.PRODUCT_FINANCING_PACKAGE_VIEW)
    @ResponseBody
    public ResultDto draw(@PathVariable("packageId") String packageId) {
    	LOGGER.info("Draw() invoked");
    	boolean isDealt = this.autoSubscribeService.isDealt(packageId);
    	if (!isDealt) {
    		this.autoSubscribeService.autoSubscribe(packageId, true, null, securityContext.getCurrentUserId());
        	String message = MessageUtil.getMessage(new String("market.autosubscribe.draw.success"));
        	return ResultDtoFactory.toAck(message);	
		}
    	String message = MessageUtil.getMessage(new String("market.autosubscribe.draw.processed"));
    	return ResultDtoFactory.toNack(message);
    }
    
    /**
     * 
     * @param packageId
     * @return
     */
    @RequestMapping(value=UrlConstant.FINANCING_MARKETING_AUTO_SUBSCRIBE_PUBILSH_URL, method = RequestMethod.POST)
    @RequiresPermissions(value=Permissions.PRODUCT_FINANCING_PACKAGE_VIEW)
    @ResponseBody
    public ResultDto publish(@PathVariable("packageId") String packageId) {
    	LOGGER.info("publish() invoked");
    	this.autoSubscribeService.publish(packageId, securityContext.getCurrentUserId());
    	String message = MessageUtil.getMessage(new String("market.autosubscribe.pubilsh.success"));
    	return ResultDtoFactory.toAck(message);
    }
    
    @RequestMapping(value=UrlConstant.FINANCING_MARKETING_AUTO_SUBSCRIBE_PRINCIPALS_URL, method = RequestMethod.GET)
    @RequiresPermissions(value=Permissions.TRANS_MANAGEMENT_AUTO_SUBS_PACKAGE_FILTER)
    public String queryPrincipals() {
        LOGGER.debug("home() invoked");
        return "market/market_auto_principal_search";
    }
    
    /**
     * 等同于自动申购融资包筛选中的候选人.
     * @return
     */
    @RequestMapping(value=UrlConstant.FINANCING_MARKETING_AUTO_SUBSCRIBE_PRINCIPALS_URL, method = RequestMethod.POST)
    @RequiresPermissions(value=Permissions.PRODUCT_FINANCING_PACKAGE_VIEW)
    @ResponseBody
    public DataTablesResponseDto<AutoSubscribeCandidateDto> queryPrincipals(@RequestBody CandidateCriteria criteria, Model model) {
    	LOGGER.info("queryPrincipals() invoked");
    	DataTablesResponseDto<AutoSubscribeCandidateDto> candidates = this.autoSubscribeService.findCandidateSettings(criteria);
    	return candidates;
    }
}
