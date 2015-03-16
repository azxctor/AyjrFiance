/*
 * Project Name: kmfex-platform
 * File Name: ProductPackageInvestorDetailsDto.java
 * Class Name: ProductPackageInvestorDetailsDto
 *
 * Copyright 2014 Hengtian Software Inc
 *
 * Licensed under the Hengtiansoft
 *
 * http://www.hengtiansoft.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hengxin.platform.app.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hengxin.platform.app.dto.downstream.MyFinancierPackagesDetailDownDto;
import com.hengxin.platform.app.dto.downstream.MyFinancierPackagesDownDto;
import com.hengxin.platform.app.dto.upstream.FinancierPackagesUpDto;
import com.hengxin.platform.common.controller.BaseController;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.common.dto.annotation.OnValid;
import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.common.util.FormatRate;
import com.hengxin.platform.common.util.MessageUtil;
import com.hengxin.platform.common.util.velocity.NumberTool;
import com.hengxin.platform.member.service.MemberService;
import com.hengxin.platform.product.controller.FinancingPackageController;
import com.hengxin.platform.product.controller.FinancingPackageListController;
import com.hengxin.platform.product.domain.FinancingPackageView;
import com.hengxin.platform.product.dto.FinancingPackageSearchDto;
import com.hengxin.platform.product.dto.ProductPackageDetailsDto;
import com.hengxin.platform.product.dto.ProductPackageDto;
import com.hengxin.platform.product.enums.EPackageStatus;
import com.hengxin.platform.product.repository.PaymentScheduleRepository;
import com.hengxin.platform.product.service.FinancingPackageService;
import com.hengxin.platform.product.service.PaymentScheduleService;
import com.hengxin.platform.product.service.ProductService;
import com.hengxin.platform.product.service.SignPkgService;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;

/**
 * 我的债务控制器类：融资包管理.
 * 
 * 二期新增接口控制器类
 * 
 * @author yicai
 *
 */
@Controller
public class MyFinancierDebtController extends BaseController  {
	
	@Autowired
	private SecurityContext securityContext;
	
	@Autowired
	private FinancingPackageService financingPackageService;
	
	@Autowired
    private PaymentScheduleRepository paymentScheduleRepository;

    @Autowired
    private PaymentScheduleService paymentScheduleService;
    
    @Autowired
    private MemberService memberService;
    
    @Autowired
    private SignPkgService signPkgService;
    
    @Autowired
    private ProductService productService;
	    
	// inject web controller
	private FinancingPackageListController financingPackageListController; 
	
	// inject web controller 
	private FinancingPackageController financingPackageController;
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(MyInvestListController.class);
	
	@PostConstruct
	public void init() {
		LOGGER.debug("init financingPackageListController");
		financingPackageListController = new FinancingPackageListController();
		financingPackageListController.setPaymentScheduleRepository(paymentScheduleRepository);
		financingPackageListController.setPaymentScheduleService(paymentScheduleService);
		financingPackageListController.setSecurityContext(securityContext);
		LOGGER.debug("init financingPackageController");
		financingPackageController = new FinancingPackageController();
		financingPackageController.setFinancingPackageService(financingPackageService);
		financingPackageController.setSecurityContext(securityContext);
		financingPackageController.setMemberService(memberService);
		financingPackageController.setProductService(productService);
	}
	
	/**
     * 我的债务列表, post, 获取返回所有融资包列表通过过滤条件
     * 
     * refer to financingpackage/financier/getpackagelist
     * 
     * @param request
     * @param searchDto
     * @return
     */
    @RequestMapping(value = "/financier/packages", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_PACKAGE_FINANCIER_VIEW,
            Permissions.PRODUCT_FINANCING_PACKAGE_MEMBER_VIEW }, logical = Logical.OR)
    public ResultDto myFinancierPackageList(@OnValid @RequestBody FinancierPackagesUpDto financierPackagesUpDto) {
    	if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("myFinacierPackageList() start: ");
		}
    	FinancingPackageSearchDto searchDto = initFinancingPackageSearchDto(financierPackagesUpDto);
    	boolean isFinancier = securityContext.isFinancier();
        Page<FinancingPackageView> packageListForFinancierList = financingPackageService.getPackageListForFinancier(
                searchDto, securityContext.getCurrentUserId(), isFinancier);
        List<ProductPackageDto> packageDtoList = 
        		financingPackageListController.processPackageListForFinancierList(packageListForFinancierList);
        List<MyFinancierPackagesDownDto> myFinancierPackagesListDownDtoList = 
        		convert2MyFinancierPackagesListDownDto(packageListForFinancierList, packageDtoList);
        return ResultDtoFactory.toAck("获取成功", myFinancierPackagesListDownDtoList);
    }
    
    /**
     * convert to MyFinancierPackagesListDownDto
     * 
     * @param packageListForFinancierList
     * @param packageDtoList
     * @return
     */
    private List<MyFinancierPackagesDownDto> convert2MyFinancierPackagesListDownDto(
			Page<FinancingPackageView> packageListForFinancierList,
			List<ProductPackageDto> packageDtoList) {
    	NumberTool tool = new NumberTool();
    	List<MyFinancierPackagesDownDto> myFinancierPackagesListDownDtoList = 
    			new ArrayList<MyFinancierPackagesDownDto>();
    	MyFinancierPackagesDownDto myFinancierPackagesListDownDto = null;
    	FinancingPackageView tempPackagePo = null;
    	for (ProductPackageDto currentProductPackageDto : packageDtoList) {
    		myFinancierPackagesListDownDto = new MyFinancierPackagesDownDto();
    		myFinancierPackagesListDownDtoList.add(myFinancierPackagesListDownDto);
    		tempPackagePo = financingPackageService.getPackageDetails(currentProductPackageDto.getId());
    		
    		myFinancierPackagesListDownDto.setId(currentProductPackageDto.getId());
    		myFinancierPackagesListDownDto.setPackageName(currentProductPackageDto.getPackageName());
    		myFinancierPackagesListDownDto.setPackageQuota(tool.formatMoney(currentProductPackageDto.getPackageQuota()));
    		myFinancierPackagesListDownDto.setRate(FormatRate.formatRate(tempPackagePo.getRate().multiply(new BigDecimal(100))) + "%");
    		myFinancierPackagesListDownDto.setSignContractTime(currentProductPackageDto.getSignContractTime());
    		myFinancierPackagesListDownDto.setDuration(currentProductPackageDto.getDuration());
    		myFinancierPackagesListDownDto.setSupplyAmount(tool.formatMoney(currentProductPackageDto.getSupplyAmount()));
    		myFinancierPackagesListDownDto.setStatus(currentProductPackageDto.getStatus().getText());
    		myFinancierPackagesListDownDto.setCanCancelPackage(currentProductPackageDto.isCanCancelPackage());
    		myFinancierPackagesListDownDto.setCanPrepayment(currentProductPackageDto.isCanPrepayment());
    		myFinancierPackagesListDownDto.setCanPrintDebtInfo(currentProductPackageDto.isCanPrintDebtInfo());
    		myFinancierPackagesListDownDto.setCanSignContract(currentProductPackageDto.isCanSignContract());
    		myFinancierPackagesListDownDto.setCanStopPackage(currentProductPackageDto.isCanStopPackage());
    		myFinancierPackagesListDownDto.setCanViewRepayTable(currentProductPackageDto.isCanViewRepayTable());
    	}
		return myFinancierPackagesListDownDtoList;
	}

	/**
	 * init FinancingPackageSearchDto from InvestorPackagesUpDto
	 * 
	 * @param investorPackagesUpDto
	 * @return
	 */
	private FinancingPackageSearchDto initFinancingPackageSearchDto(
			FinancierPackagesUpDto financierPackagesUpDto) {
		FinancingPackageSearchDto searchDto = new FinancingPackageSearchDto();
		// 模拟前端参数-字段排序列
		List<Integer> aiSortCol = new ArrayList<Integer>();
		List<String> asSortDir = new ArrayList<String>();
		List<String> amDataProp = new ArrayList<String>();
		searchDto.setSortedColumns(aiSortCol);
		searchDto.setSortDirections(asSortDir);
		searchDto.setDataProp(amDataProp);
		aiSortCol.add(0);
		asSortDir.add("desc");
		amDataProp.add("id");
		searchDto.setKeyword(StringUtils.EMPTY);
		searchDto.setPackageStatus(EPackageStatus.NULL);
		searchDto.setDisplayLength(10); // default 10 item per page
		searchDto.setDisplayStart(Integer.parseInt(financierPackagesUpDto
				.getDisplayStart()));
		searchDto.setCreateTime(new Date(System.currentTimeMillis())); // default current time
		if (financierPackagesUpDto.getDisplayLength() != 0) {
			searchDto.setDisplayLength(financierPackagesUpDto.getDisplayLength());
		}
		if (!StringUtils.isEmpty(financierPackagesUpDto.getKeyword())) {
			searchDto.setKeyword(financierPackagesUpDto.getKeyword());
		}
		if (!StringUtils.isEmpty(financierPackagesUpDto.getPackageStatus()) &&
				!"NULL".equals(financierPackagesUpDto.getPackageStatus())) {
			searchDto.setPackageStatus(EnumHelper.translate(EPackageStatus.class,
					financierPackagesUpDto.getPackageStatus()));
		}
		if (financierPackagesUpDto.getCreateTime() != 0) {
			searchDto.setCreateTime(new Date(financierPackagesUpDto.getCreateTime()));
		}
		return searchDto;
	}
	
	/**
	 * 获取融资包详情
	 * 
	 * refer to financingpackage/details/{packageId}/{mode}
	 * 
	 * @return
	 */
	@RequestMapping(value = "/financier/package/detail/{packageId}", method = RequestMethod.GET)
	@ResponseBody
	@RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_PACKAGE_FIN_PROD_DETAIL_VIEW,
			Permissions.PRODUCT_FINANCING_PACKAGE_PLATFORM_DETAIL_VIEW }, logical = Logical.OR)
	public ResultDto myFinancierPackageDetail(@PathVariable(value = "packageId") String packageId) {
		String mode = "view";
		FinancingPackageView packagePo = financingPackageService.getPackageDetails(packageId);
		ProductPackageDetailsDto detailsDto = financingPackageController
				.processGetFinancierPackageDetail(packageId, mode, packagePo);
		MyFinancierPackagesDetailDownDto myFinancierPackagesDetailDownDto = convert2MyFinancierPackagesDownDetailDto(
				detailsDto, packagePo);
		return ResultDtoFactory.toAck("获取成功", myFinancierPackagesDetailDownDto);
	}

	private MyFinancierPackagesDetailDownDto convert2MyFinancierPackagesDownDetailDto(ProductPackageDetailsDto detailsDto, 
			FinancingPackageView packagePo) {
		NumberTool tool = new NumberTool();
		MyFinancierPackagesDetailDownDto myFinancierPackagesDetailDownDto = new MyFinancierPackagesDetailDownDto();
		myFinancierPackagesDetailDownDto.setId(detailsDto.getId());
		myFinancierPackagesDetailDownDto.setPackageName(detailsDto.getPackageName());
		String rateString = "0.0%";
		BigDecimal rate = FormatRate.formatRate(detailsDto.getRate());
		if (detailsDto.getRate() != null) {
			rateString = rate.toString() + "%";
		}
		myFinancierPackagesDetailDownDto.setRate(rateString);
		myFinancierPackagesDetailDownDto.setDuration(detailsDto.getDuration());
		myFinancierPackagesDetailDownDto.setSignContractTime(detailsDto.getSignContractTime());
		myFinancierPackagesDetailDownDto.setPackageQuota(tool.formatMoney(detailsDto.getPackageQuota()));
		myFinancierPackagesDetailDownDto.setSupplyAmount(tool.formatMoney(detailsDto.getSupplyAmount()));
		myFinancierPackagesDetailDownDto.setOverdue2CmpnsDays(detailsDto.getOverdue2CmpnsDays().toString() + "天");
		myFinancierPackagesDetailDownDto.setFinancierName(detailsDto.getFinancierName());
		myFinancierPackagesDetailDownDto.setAccountNo(detailsDto.getAccountNo());
		myFinancierPackagesDetailDownDto.setWrtrName(!"--".equals(detailsDto.getWrtrName()) ? 
				detailsDto.getWrtrName() : "/");
		myFinancierPackagesDetailDownDto.setPayMethod(detailsDto.getPayMethod().getText());
		myFinancierPackagesDetailDownDto.setLoanPurpose(detailsDto.getLoanPurpose());
		myFinancierPackagesDetailDownDto.setGuaranteeLicenseNoImg(detailsDto.getGuaranteeLicenseNoImg());
		myFinancierPackagesDetailDownDto.setGuaranteeLicenseNoImgUrl(detailsDto.getGuaranteeLicenseNoImgUrl());
		myFinancierPackagesDetailDownDto.setWarrantyType(detailsDto.getWarrantyType().getText());
		myFinancierPackagesDetailDownDto.setProductLevel(detailsDto.getProductLevel());
		return myFinancierPackagesDetailDownDto;
	}
	
	/**
	 * 融资包签约, post
	 * 
	 * refer to financingpackage/{packageId}/signed
	 */
	@RequestMapping(value = "/financier/package/sign/{packageId}", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_PACKAGE_SIGN_CONTRACT })
	public ResultDto signFinancingPackages(@PathVariable(value = "packageId") String packageId) {
		String currentUserId = securityContext.getCurrentUserId();
        signPkgService.signProductPackageInWaitSign(packageId, currentUserId);
        return ResultDtoFactory.toAck(MessageUtil.getMessage("package.sign.success"));
	}
	
}
