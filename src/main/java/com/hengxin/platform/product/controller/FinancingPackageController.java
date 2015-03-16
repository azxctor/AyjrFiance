/*
 * Project Name: kmfex-platform
 * File Name: FinanceProductController.java
 * Class Name: FinanceProductController
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

package com.hengxin.platform.product.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hengxin.platform.account.service.PrintInfoService;
import com.hengxin.platform.common.controller.BaseController;
import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.dto.IdListDto;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.common.dto.annotation.OnValid;
import com.hengxin.platform.common.entity.ActionHistoryPo;
import com.hengxin.platform.common.service.ActionHistoryService;
import com.hengxin.platform.common.util.FormatRate;
import com.hengxin.platform.common.util.MaskUtil;
import com.hengxin.platform.common.util.MessageUtil;
import com.hengxin.platform.common.util.PaginationUtil;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.market.service.FinancingMarketService;
import com.hengxin.platform.member.domain.Agency;
import com.hengxin.platform.member.dto.ActionHistoryDto;
import com.hengxin.platform.member.dto.SubscribeGroupDto;
import com.hengxin.platform.member.service.MemberService;
import com.hengxin.platform.member.service.SubscribeGroupService;
import com.hengxin.platform.product.converter.ProductPackageSubscribesConverter;
import com.hengxin.platform.product.domain.FinancingPackageView;
import com.hengxin.platform.product.domain.PackageSubscribes;
import com.hengxin.platform.product.domain.Product;
import com.hengxin.platform.product.domain.ProductPackage;
import com.hengxin.platform.product.dto.DebtBillPriInfoDto;
import com.hengxin.platform.product.dto.DebtBillPriInfoSearchDto;
import com.hengxin.platform.product.dto.FinancingPackageLogDto;
import com.hengxin.platform.product.dto.FinancingTransactionSettingsDto;
import com.hengxin.platform.product.dto.ProductPackageDetailsDto;
import com.hengxin.platform.product.dto.ProductPackageDto;
import com.hengxin.platform.product.dto.ProductPackageSubscribesDto;
import com.hengxin.platform.product.enums.EFinancingPackageOperate;
import com.hengxin.platform.product.enums.EPackageStatus;
import com.hengxin.platform.product.enums.EWarrantyType;
import com.hengxin.platform.product.service.CancelPkgService;
import com.hengxin.platform.product.service.FeeGroupService;
import com.hengxin.platform.product.service.FinancingPackageService;
import com.hengxin.platform.product.service.LoanApprovalPkgService;
import com.hengxin.platform.product.service.ManualRepayService;
import com.hengxin.platform.product.service.PrepayPkgService;
import com.hengxin.platform.product.service.ProductContractTemplateService;
import com.hengxin.platform.product.service.ProductService;
import com.hengxin.platform.product.service.SignPkgService;
import com.hengxin.platform.product.service.TerminatePkgService;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.domain.User;
import com.hengxin.platform.security.service.UserService;

/**
 * Class Name: FinancingPackageController
 * 
 * @author yingchangwang
 * 
 */
@Controller
public class FinancingPackageController extends BaseController {

    private static final String SUCCESS = "SUCCESS";

    @Autowired
    private SecurityContext securityContext;

    @Autowired
    private FinancingPackageService financingPackageService;

    @Autowired
    private SubscribeGroupService subscribeGroupService;

    @Autowired
    private FeeGroupService feeGroupService;

    @Autowired
    private SignPkgService signPkgService;

    @Autowired
    private PrepayPkgService prepayPkgService;

    @Autowired
    private ManualRepayService manualRepayService;

    @Autowired
    private CancelPkgService cancelPkgService;

    @Autowired
    private TerminatePkgService terminatePkgService;

    @Autowired
    private LoanApprovalPkgService loanApprovalPkgService;

    @Autowired
    private ActionHistoryService actionHistoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private PrintInfoService printInfoService;

    @Autowired
    private FinancingMarketService financingMarketService;

    @Autowired
    private ProductContractTemplateService productContractTemplateService;
    /**
     * Description:风控部-放款操作(批量放款) 
     */  
    @RequestMapping(value = "financingpackage/batchLoanapproveconfirm", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = Permissions.PRODUCT_FINANCING_LOAN_APPROVE_CONFIRM)
    public boolean financingPackageloanApproveConfirmBatch(@RequestBody IdListDto idListDto) {
        String currentUserId = securityContext.getCurrentUserId();
        return loanApprovalPkgService.loanApprovalProductPackageBatck(idListDto.getIdList(), currentUserId); 
    }
    /**
     * Description:风控部-放款审批操作 (批量放款审批)
     */ 
    @RequestMapping(value = "financingpackage/batchLoanapprove", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = Permissions.PRODUCT_FINANCING_LOAN_APPROVE)
    public boolean financingPackageloanApproveBatch(@RequestBody IdListDto idListDto) {
        String currentUserId = securityContext.getCurrentUserId();
        return loanApprovalPkgService.loanApprovalPassProductPackage(idListDto.getIdList(), currentUserId); 
    }

    /**
     * 
     * Description: 获取融资包详情
     * 
     * @param request
     * @param model
     * @param packageId
     * @param mode
     * @return
     */
    @RequestMapping(value = "financingpackage/details/{packageId}/{mode}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.PARTIAL_CONTENT)
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_PACKAGE_FIN_PROD_DETAIL_VIEW,
            Permissions.PRODUCT_FINANCING_PACKAGE_PLATFORM_DETAIL_VIEW }, logical = Logical.OR)
    public String getFinancierPackageDetails(HttpServletRequest request, Model model,
            @PathVariable(value = "packageId") String packageId, @PathVariable(value = "mode") String mode) {
        FinancingPackageView packagePo = financingPackageService.getPackageDetails(packageId);
        ProductPackageDetailsDto detailsDto = processGetFinancierPackageDetail(
				packageId, mode, packagePo);

        model.addAttribute("detailsDto", detailsDto);
        return "packet/packet_manage_details";
    }

	public ProductPackageDetailsDto processGetFinancierPackageDetail(
			String packageId, String mode, FinancingPackageView packagePo) {
		ProductPackageDetailsDto detailsDto = ConverterService.convert(packagePo, ProductPackageDetailsDto.class);
        if (packagePo != null) {

            if (packagePo.getSigningDt() != null) {
                detailsDto.setSigningDt(DateUtils.formatDate(packagePo.getSigningDt(), "yyyy-MM-dd HH:mm"));
            }
            detailsDto.setSignContractTime(processSignContractTime(packagePo.getSigningDt(),
                    packagePo.getSignContractTime()));

            if (packagePo.getSubsPercent() != null && packagePo.getSubsPercent().startsWith(".")) {
                detailsDto.setSubsPercent("0" + packagePo.getSubsPercent());
            }

            if (securityContext.cannotViewRealName(packagePo.getFinancierId(), false)) {
                detailsDto.setFinancierName(MaskUtil.maskChinsesName(packagePo.getFinancierName()));
            }

            int supplyCountForPackage = financingPackageService.getSupplyCountForPackage(packageId);
            detailsDto.setSupplyUserCount(BigDecimal.valueOf(supplyCountForPackage));

            EPackageStatus status = packagePo.getStatus();
            // 在待申购和申购中显示申购时间
            if ((EPackageStatus.WAIT_SUBSCRIBE == status) || (EPackageStatus.SUBSCRIBE == status)) {
                detailsDto.setCanDisplaySupplyTime(true);
            }
            // 判断是否为待放款审批及以后状态
            if ((status == EPackageStatus.WAIT_LOAD_APPROAL) || (status == EPackageStatus.WAIT_LOAD_APPROAL_CONFIRM)
                    || (status == EPackageStatus.PAYING) || (status == EPackageStatus.END)) {
                detailsDto.setStatusAfterWaitLoadApproal(true);
            }
            // 判断是否为申购中及以后状态
            if ((status == EPackageStatus.SUBSCRIBE) || (status == EPackageStatus.WAIT_SIGN)
                    || (status == EPackageStatus.WAIT_LOAD_APPROAL) || (status == EPackageStatus.ABANDON)
                    || (status == EPackageStatus.WAIT_LOAD_APPROAL_CONFIRM) || (status == EPackageStatus.PAYING)
                    || (status == EPackageStatus.END)) {
                detailsDto.setStatusAfterSubscribe(true);
            }
            
            // 获取产品信息
        	Product prod = productService.getProductById(packagePo.getProductId());
        	
            if (packagePo.getWarrantyType() == EWarrantyType.MONITORASSETS
                    || packagePo.getWarrantyType() == EWarrantyType.NOTHING) {
                detailsDto.setWrtrName("--");
                detailsDto.setWrtrNameShow("--");
            } else if (StringUtils.isNotBlank(prod.getWarrantIdShow())) {
                Agency agency = memberService.getAgencyByUserId(prod.getWarrantIdShow());
                detailsDto.setGuaranteeLicenseNoImg(agency != null ? agency.getLicenceNoImg() : "");
                
                if(StringUtils.isNotBlank(prod.getWarrantIdShow())
            			&& !StringUtils.equals(prod.getWarrantIdShow(), prod.getWarrantId())){
                	User user = userService.getUserByUserId(prod.getWarrantIdShow());
                	detailsDto.setWrtrNameShow(user.getName());
            	}
            }

            EFinancingPackageOperate operate = EFinancingPackageOperate.valueOf(mode.toUpperCase());
            if (EFinancingPackageOperate.LOANAPPROVE == operate
                    && securityContext.hasPermission(Permissions.PRODUCT_FINANCING_LOAN_APPROVE)) {
                detailsDto.setCanLoanApprove(true);
            } else if (EFinancingPackageOperate.MANUALPAY == operate
                    && securityContext.hasPermission(Permissions.PRODUCT_FINANCING_TRANSACTION_MANUAL_PAYMENTS)) {
                detailsDto.setCanManualPay(true);
            } else if (EFinancingPackageOperate.STOP == operate
                    && securityContext.hasPermission(Permissions.PRODUCT_FINANCING_PACKAGE_STOP)) {
                detailsDto.setCanStopFinancingPackage(true);
            } else if (EFinancingPackageOperate.WITHDRAW == operate
                    && securityContext.hasPermission(Permissions.PRODUCT_FINANCING_PACKAGE_ABNORMAL_CANCEL)) {
                detailsDto.setCanWithDraw(true);
            } else if (EFinancingPackageOperate.PREPAYMENT == operate
                    && securityContext.hasPermission(Permissions.PRODUCT_FINANCING_PACKAGE_PREPAYMENT_NO_PENALTY)) {
                detailsDto.setCanprepayment(true);
            } else if (EFinancingPackageOperate.LOANAPPROVECONFIRM == operate
                    && securityContext.hasPermission(Permissions.PRODUCT_FINANCING_LOAN_APPROVE_CONFIRM)) {
                detailsDto.setCanLoanApproveConfirm(true);
            }

            detailsDto.setRate(FormatRate.formatRate(detailsDto.getRate().multiply(BigDecimal.valueOf(100))));
        }
		return detailsDto;
	}

    /**
     * 
     * Description: 拼接签约时间
     * 
     * @param signingDate
     * @param signContractTime
     * @return
     */
    private String processSignContractTime(Date signingDate, Date signContractTime) {
        if (signingDate != null && signContractTime != null) {
            String formatDate = DateUtils.formatDate(signingDate, "yyyy-MM-dd");
            String formatTime = DateUtils.formatDate(signContractTime, "HH:mm");

            return formatDate + " " + formatTime;
        }
        return null;
    }

    /**
     * Description: 交易参数设置操作
     * 
     * @param request
     * @param session
     * @param model
     * @param transDto
     * @return
     */
    @RequestMapping(value = "product/financingpackage/save", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = Permissions.PRODUCT_FINANCING_TRANSACTION_SETTINGS)
    public ResultDto createPackages(@OnValid @RequestBody FinancingTransactionSettingsDto transDto,
            HttpServletRequest request, HttpSession session, Model model) {
        String currentUserId = securityContext.getCurrentUserId();
        if (transDto != null) {
            String productId = transDto.getProductId();
            Product product = productService.getProductById(productId);
            if (product == null) {
                return ResultDtoFactory.toNack(MessageUtil.getMessage("package.trans.settings.product.not.exist"),
                        transDto);
            }
            BigDecimal totalQuota = BigDecimal.ZERO;
            for (ProductPackageDto dto : transDto.getPackageList()) {
                totalQuota = totalQuota.add(dto.getPackageQuota());
            }
            if (totalQuota.compareTo(product.getAppliedQuota()) != 0) {
                return ResultDtoFactory.toNack(
                        MessageUtil.getMessage("package.trans.settings.quota.mismath", totalQuota,
                                product.getAppliedQuota()), transDto);
            }
        }
        financingPackageService.createFinancingPackages(transDto, currentUserId);
        return new ResultDto(SUCCESS, MessageUtil.getMessage("package.trans.settings.success"), transDto);
    }

    /**
     * 
     * Description: 拆包编辑操作
     * 
     * @param transDto
     * @param request
     * @return
     */
    @RequestMapping(value = "product/financingpackage/edit", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = Permissions.PRODUCT_FINANCING_TRANSACTION_SETTINGS)
    public ResultDto editTransactionSettings(@OnValid @RequestBody FinancingTransactionSettingsDto transDto,
            HttpServletRequest request) {
        String currentUserId = securityContext.getCurrentUserId();
        if (transDto != null) {
            String productId = transDto.getProductId();
            Product product = productService.getProductById(productId);
            if (product == null) {
                return ResultDtoFactory.toNack(MessageUtil.getMessage("package.trans.settings.product.not.exist"),
                        transDto);
            }
            BigDecimal totalQuota = BigDecimal.ZERO;
            for (ProductPackageDto dto : transDto.getPackageList()) {
                totalQuota = totalQuota.add(dto.getPackageQuota());
            }
            if (totalQuota.compareTo(product.getAppliedQuota()) != 0) {
                return ResultDtoFactory.toNack(
                        MessageUtil.getMessage("package.trans.settings.quota.mismath", totalQuota,
                                product.getAppliedQuota()), transDto);
            }
        }
        financingPackageService.updateTransactionSettings(transDto, currentUserId);
        return new ResultDto(SUCCESS, MessageUtil.getMessage("package.trans.edit.success"), transDto);
    }

    /**
     * 
     * Description: 查询所有的定投组
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "financingpackage/getallgroups")
    @ResponseBody
    @RequiresPermissions(value = Permissions.PRODUCT_FINANCING_TRANSACTION_SETTINGS)
    public List<SubscribeGroupDto> getGroups(HttpServletRequest request) {
        List<SubscribeGroupDto> allGroups = subscribeGroupService.getAllGroups();
        return allGroups;
    }

    /**
     * Description: 融资会员/交易经理-终止申购
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */

    @RequestMapping(value = "financingpackage/{packageId}/stop", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = Permissions.PRODUCT_FINANCING_PACKAGE_STOP)
    public ResultDto financingStopPackages(HttpServletRequest request,
            @PathVariable(value = "packageId") String packageId, HttpSession session, Model model) {
        String currentUserId = securityContext.getCurrentUserId();
        boolean checkMarketOpen = false;
        if (securityContext.isFinancier()) {
            checkMarketOpen = true;
        }
        terminatePkgService.terminateProductPackageInApply(packageId, currentUserId, checkMarketOpen);
        return new ResultDto(SUCCESS, MessageUtil.getMessage("package.stop.success"), null);
    }

    /**
     * Description: 融资会员-撤单
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */

    @RequestMapping(value = "financingpackage/{packageId}/withdrawals", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = Permissions.PRODUCT_FINANCING_PACKAGE_CANCEL)
    public ResultDto financingWithdrawalsPackages(HttpServletRequest request,
            @PathVariable(value = "packageId") String packageId, HttpSession session, Model model) {

        String currentUserId = securityContext.getCurrentUserId();
        ProductPackage productPackage = financingPackageService.getProductPackageById(packageId);
        if (productPackage == null) {
            return ResultDtoFactory.toAck("Failed.", null);
        }
        if (productPackage.getStatus() == EPackageStatus.SUBSCRIBE) {
            cancelPkgService.cancelProductPackageInApply(packageId, currentUserId);
        } else if (productPackage.getStatus() == EPackageStatus.WAIT_SIGN) {
            cancelPkgService.cancelProductPackageInSign(packageId, currentUserId);
        }
        return ResultDtoFactory.toAck(MessageUtil.getMessage("package.cancel.success"));
    }

    /**
     * Description: 系统管理员异常撤单
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */

    @RequestMapping(value = "financingpackage/{packageId}/abnormalwithdraw", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = Permissions.PRODUCT_FINANCING_PACKAGE_ABNORMAL_CANCEL)
    public ResultDto sysadminWithdrawalsPackages(HttpServletRequest request,
            @PathVariable(value = "packageId") String packageId, HttpSession session, Model model) {
        String currentUserId = securityContext.getCurrentUserId();
        cancelPkgService.abnormalCancelProductPackage(packageId, currentUserId);
        return new ResultDto(SUCCESS, MessageUtil.getMessage("package.withdraw.success"), null);
    }

    /**
     * Description:交易部-手工还款操作
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */

    @RequestMapping(value = "financingpackage/{packageId}/manualpay/{period}", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = Permissions.PRODUCT_FINANCING_TRANSACTION_MANUAL_PAYMENTS)
    public ResultDto financingPackageManualPay(HttpServletRequest request,
            @PathVariable(value = "packageId") String packageId, @PathVariable(value = "period") String period,
            HttpSession session, Model model) {
        String currentUserId = securityContext.getCurrentUserId();
        manualRepayService.manualRePayProductPackage(packageId, Integer.valueOf(period), currentUserId);
        return new ResultDto(SUCCESS, MessageUtil.getMessage("package.manualpay.success"), null);
    }

    /**
     * Description:结算部-提前还款操作
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */

    @RequestMapping(value = "financingpackage/{packageId}/prepayment", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_PACKAGE_PREPAYMENT_NO_PENALTY,
            Permissions.PRODUCT_FINANCING_PACKAGE_PREPAYMENT }, logical = Logical.OR)
    public ResultDto financingPackagePrepayment(HttpServletRequest request,
            @PathVariable(value = "packageId") String packageId, HttpSession session, Model model) {
        String currentUserId = securityContext.getCurrentUserId();
        if (securityContext.hasAllPermissions(Permissions.PRODUCT_FINANCING_PACKAGE_PREPAYMENT_NO_PENALTY)) {
            prepayPkgService.prepayProductPackageByManager(packageId, currentUserId);
            return new ResultDto(SUCCESS, MessageUtil.getMessage("package.prepayment.success"), null);
        } else {
            prepayPkgService.prepayProductPackageByFinancer(packageId, currentUserId);
            return ResultDtoFactory.toAck(MessageUtil.getMessage("package.prepayment.success"));
        }
    }

    /**
     * Description:风控部-放款审批操作
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */

    @RequestMapping(value = "financingpackage/{packageId}/loanapprove", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = Permissions.PRODUCT_FINANCING_LOAN_APPROVE)
    public ResultDto financingPackageloanApprove(HttpServletRequest request,
            @PathVariable(value = "packageId") String packageId, HttpSession session, Model model) {
        String currentUserId = securityContext.getCurrentUserId();
        loanApprovalPkgService.loanApprovalPassProductPackage(packageId, currentUserId);
        return new ResultDto(SUCCESS, MessageUtil.getMessage("package.loan.approve.success"), null);
    }

    /**
     * Description:风控部-放款操作
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */

    @RequestMapping(value = "financingpackage/{packageId}/loanapproveconfirm", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = Permissions.PRODUCT_FINANCING_LOAN_APPROVE_CONFIRM)
    public ResultDto financingPackageloanApproveConfirm(HttpServletRequest request,
            @PathVariable(value = "packageId") String packageId, HttpSession session, Model model) {
        String currentUserId = securityContext.getCurrentUserId();
        loanApprovalPkgService.loanApprovalProductPackage(packageId, currentUserId);
        return new ResultDto(SUCCESS, MessageUtil.getMessage("package.loan.approve.success"), null);
    }

    /**
     * 申购人数及金额列表
     * 
     * @param request
     * @param searchDto
     * @param packageId
     * @return
     */

    @RequestMapping(value = "search/getsubscribes")
    @ResponseBody
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_PACKAGE_FIN_PROD_DETAIL_VIEW,
            Permissions.PRODUCT_FINANCING_PACKAGE_PLATFORM_DETAIL_VIEW }, logical = Logical.OR)
    public DataTablesResponseDto<ProductPackageSubscribesDto> getSubscribesPackageList(HttpServletRequest request,
            @RequestBody FinancingPackageLogDto searchDto) {
        DataTablesResponseDto<ProductPackageSubscribesDto> result = new DataTablesResponseDto<ProductPackageSubscribesDto>();
        List<ProductPackageSubscribesDto> packageDtoList = new ArrayList<ProductPackageSubscribesDto>();
        result.setEcho(searchDto.getEcho());
        String packageId = searchDto.getPackageId();
        Page<PackageSubscribes> page = financingPackageService.getPackageSubscribesByPackageId(packageId, searchDto);
        int count = searchDto.getDisplayStart();
        for (PackageSubscribes packageSubscribes : page) {
            ProductPackageSubscribesDto ppsDto = ConverterService.convert(packageSubscribes,
                    ProductPackageSubscribesDto.class, ProductPackageSubscribesConverter.class);
            ppsDto.setSupplyAmount(packageSubscribes.getUnitAmt().multiply(
                    BigDecimal.valueOf(packageSubscribes.getUnit())));
            count++;
            ppsDto.setNumber(String.valueOf(count));
            // if (securityContext.cannotViewRealName(packageSubscribes.getUserId(),
            // false)&&!securityContext.isFinancier()) {
            // ppsDto.setUserName(MaskUtil.maskChinsesName(ppsDto.getUserName()));
            // }
            packageDtoList.add(ppsDto);
        }
        result.setData(packageDtoList);
        result.setTotalDisplayRecords(page.getTotalElements());
        result.setTotalRecords(page.getTotalElements());
        return result;
    }

    /**
     * Description: 融资会员-签约操作
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "financingpackage/{packageId}/signed", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_PACKAGE_SIGN_CONTRACT })
    public ResultDto financingSignedPackages(HttpServletRequest request,
            @PathVariable(value = "packageId") String packageId, HttpSession session, Model model) {
        String currentUserId = securityContext.getCurrentUserId();
        signPkgService.signProductPackageInWaitSign(packageId, currentUserId);
        return ResultDtoFactory.toAck(MessageUtil.getMessage("package.sign.success"));
    }

    /**
     * 
     * Description: 查看融资包日志
     * 
     * @param packageLogDTO
     * @param packageId
     * @param request
     * @param session
     * @param model
     * @return
     * @throws ServiceException
     */
    @RequestMapping(value = "view/packagelog")
    @ResponseBody
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_PACKAGE_FIN_PROD_DETAIL_VIEW,
            Permissions.PRODUCT_FINANCING_PACKAGE_PLATFORM_DETAIL_VIEW, Permissions.PRODUCT_FINANCING_PACKAGE_VIEW }, logical = Logical.OR)
    public DataTablesResponseDto<ActionHistoryDto> getPackageLogList(@RequestBody FinancingPackageLogDto packageLogDTO,
            HttpServletRequest request, HttpSession session, Model model) {
        DataTablesResponseDto<ActionHistoryDto> result = new DataTablesResponseDto<ActionHistoryDto>();
        Pageable pageRequest = PaginationUtil.buildPageRequest(packageLogDTO);
        Page<ActionHistoryPo> pagePackageLogs = this.actionHistoryService.findFinancingpackageLogByPackageId(
                packageLogDTO.getPackageId(), pageRequest);
        if (pagePackageLogs == null) {
            return result;
        }
        List<ActionHistoryDto> actionHistoryDtos = new ArrayList<ActionHistoryDto>();
        for (ActionHistoryPo actionHistoryPo : pagePackageLogs) {
            ActionHistoryDto history = ConverterService.convert(actionHistoryPo, ActionHistoryDto.class);
            if (StringUtils.isBlank(history.getOperateUser())) {
                history.setOperateUser("system");
            } else {
                User user = userService.getUserByUserId(history.getOperateUser());
                if (user != null) {
                    history.setOperateUser(user.getName());
                }
            }
            actionHistoryDtos.add(history);

        }
        result.setEcho(packageLogDTO.getEcho());
        result.setData(actionHistoryDtos);
        result.setTotalDisplayRecords(pagePackageLogs.getTotalElements());
        result.setTotalRecords(pagePackageLogs.getTotalElements());
        return result;
    }

    @RequestMapping(value = "financingpackage/platform/getdebtbillpriinfodetail")
    @ResponseBody
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_PACKAGE_VIEW_FOR_CUST_PRINT_DEBT })
    public DebtBillPriInfoDto getDebtBillPriInfoDetail(HttpServletRequest request,
            @RequestBody DebtBillPriInfoSearchDto searchDto) {
        return printInfoService.getDebtBillPriInfoDetail(searchDto);
    }

	public void setFinancingPackageService(
			FinancingPackageService financingPackageService) {
		this.financingPackageService = financingPackageService;
	}

	public void setSecurityContext(SecurityContext securityContext) {
		this.securityContext = securityContext;
	}

	public void setMemberService(MemberService memberService) {
		this.memberService = memberService;
	}

	public ProductService getProductService() {
		return productService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}
	
}
