/*
 * Project Name: kmfex-platform
 * File Name: FinancingPackageListController.java
 * Class Name: FinancingPackageListController
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
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hengxin.platform.common.constant.LiteralConstant;
import com.hengxin.platform.common.constant.UrlConstant;
import com.hengxin.platform.common.controller.BaseController;
import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.domain.EnumOption;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.common.util.FormatRate;
import com.hengxin.platform.common.util.NumberUtil;
import com.hengxin.platform.fund.entity.PositionLotPo;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.product.ProdPkgUtil;
import com.hengxin.platform.product.domain.FinancingPackageView;
import com.hengxin.platform.product.domain.PaymentSchedule;
import com.hengxin.platform.product.domain.Product;
import com.hengxin.platform.product.domain.ProductPackage;
import com.hengxin.platform.product.dto.FinancingPackageSearchDto;
import com.hengxin.platform.product.dto.PackageSumDto;
import com.hengxin.platform.product.dto.ProductPackageDetailsDto;
import com.hengxin.platform.product.dto.ProductPackageDto;
import com.hengxin.platform.product.enums.EPackageActionType;
import com.hengxin.platform.product.enums.EPackageStatus;
import com.hengxin.platform.product.enums.EPayStatus;
import com.hengxin.platform.product.enums.EWarrantyType;
import com.hengxin.platform.product.repository.PaymentScheduleRepository;
import com.hengxin.platform.product.service.FinancingPackageService;
import com.hengxin.platform.product.service.PaymentScheduleService;
import com.hengxin.platform.product.service.ProductService;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;

/**
 * Class Name: FinancingPackageListController.
 * 
 * @author yingchangwang
 * 
 */
@Controller
public class FinancingPackageListController extends BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(FinancingPackageListController.class);

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";

    @Autowired
    private SecurityContext securityContext;

    @Autowired
    private FinancingPackageService financingPackageService;

    @Autowired
    private PaymentScheduleRepository paymentScheduleRepository;

    @Autowired
    private PaymentScheduleService paymentScheduleService;

    @Autowired
    private ProductService productService;

    /**
     * Description: 投资人债权列表页面加载.
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.MY_ACCOUNT_MY_CREDITOR_URL)
    @RequiresPermissions(value = { Permissions.MY_ACCOUNT_MY_CREDITOR_VIEW })
    public String renderInvestorPackageList(HttpServletRequest request, HttpSession session, Model model) {
        model.addAttribute("totalPrincipal", BigDecimal.ZERO);
        model.addAttribute("totalNextPayAmt", BigDecimal.ZERO);
        model.addAttribute("totalRestAmt", BigDecimal.ZERO);
        List<EnumOption> payStatusList = new ArrayList<EnumOption>();
        EnumOption e1 = new EnumOption("NULL", "全部");
        EnumOption e2 = new EnumOption("NORMAL", "还款中");
        EnumOption e3 = new EnumOption("OVERDUE", "违约中");
        EnumOption e4 = new EnumOption("COMPENSATING", "代偿中");
        EnumOption e5 = new EnumOption("COMPENSATORY", "已代偿");
        payStatusList.add(e1);
        payStatusList.add(e2);
        payStatusList.add(e3);
        payStatusList.add(e4);
        payStatusList.add(e5);
        model.addAttribute("payStatusList", payStatusList);
        return "packet/packet_manage_investor";
    }
    
    @RequestMapping(value = UrlConstant.MY_ACCOUNT_MY_CREDITOR_SUM_URL)
    @ResponseBody
    @RequiresPermissions(value = { Permissions.MY_ACCOUNT_MY_CREDITOR_VIEW })
    public Map<String, BigDecimal> processSummaryCreditor(HttpServletRequest request,
            @RequestBody FinancingPackageSearchDto searchDto){
        return processSummaryCreditor(searchDto);
    }
    
    public Map<String, BigDecimal> processSummaryCreditor() {
    	return processSummaryCreditor(null);
    }

    private Map<String, BigDecimal> processSummaryCreditor(FinancingPackageSearchDto searchDto) {
    	Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
    	// 下期预期总收益
    	BigDecimal totalNextPayAmt = BigDecimal.ZERO;
    	// 申购本金总额
        BigDecimal totalPrincipal = BigDecimal.ZERO;
        // 剩余本息总额
        BigDecimal totalRestAmt = BigDecimal.ZERO; 
        BigDecimal nextPayAmt = BigDecimal.ZERO; 
        
        Date currentWorkDate = CommonBusinessUtil.getCurrentWorkDate();
        Date processDate = DateUtils.getDate("2014-05-01 00:00:00", LiteralConstant.YYYY_MM_DD_HH_MM_SS);
        List<PositionLotPo> allCreditors;
        if (searchDto == null) {
        	allCreditors = financingPackageService.getAllCreditors(securityContext.getCurrentUserId());
        } else {
        	allCreditors = financingPackageService.getAllCreditors(securityContext.getCurrentUserId(), searchDto);
        }
        for (PositionLotPo investorPackage : allCreditors) {
            boolean deductFlag = false;
            totalPrincipal = totalPrincipal.add(investorPackage.getLotBuyPrice());
            if (investorPackage.getPosition() != null) {
            	ProductPackage pkg = investorPackage.getPosition().getProductPackage();
            	BigDecimal supplyAmt = pkg.getSupplyAmount();
            	BigDecimal unitAmt = pkg.getUnitAmount();
            	BigDecimal pkgUnit = supplyAmt.divide(unitAmt);
            	BigDecimal invsUnit = BigDecimal.valueOf(investorPackage.getUnit());
            	
                Date signingDt = pkg.getSigningDt();
                if (processDate.compareTo(signingDt) <= 0) {
                    deductFlag = true;
                }

                String productId = pkg.getProductId();
                Product product = productService.getProductById(productId);
                // 按时间升序查询状态为还款中的第一个记录作为当前还款记录
                List<PaymentSchedule> paymentScheduleList = financingPackageService
                        .getCurrentPackageScheduleListByPkgId(pkg.getId());
                if (paymentScheduleList != null && paymentScheduleList.size() > 0) {
                    PaymentSchedule currentPayment = null;
                    // 获取当前Schedule（状态为还款中）
                    for (PaymentSchedule tempPayment : paymentScheduleList) {
                        if (EPayStatus.NORMAL == tempPayment.getStatus()) {
                            currentPayment = tempPayment;
                            break;
                        }
                    }
                    if (currentPayment == null) {
                        nextPayAmt = BigDecimal.ZERO;
                    } else {
                        if (!deductFlag) {
                            nextPayAmt = currentPayment.getPrincipalAmt();
                            nextPayAmt = nextPayAmt.add(currentPayment.getInterestAmt());
                            nextPayAmt = nextPayAmt.add(currentPayment.getInterestForfeit());
                        	nextPayAmt = nextPayAmt.multiply(invsUnit);
                        	nextPayAmt = nextPayAmt.divide(pkgUnit, 2, RoundingMode.HALF_UP);
                        } else {
                        	BigDecimal intrDecuteRate = BigDecimal.ONE.subtract(CommonBusinessUtil
                                    .getPaymentInterestDeductRate());
                            BigDecimal nextInterest = currentPayment.getInterestAmt();
                            nextInterest = nextInterest.add(currentPayment.getInterestForfeit());
                            nextInterest = nextInterest.multiply(intrDecuteRate);
                            
                            nextPayAmt = currentPayment.getPrincipalAmt();
                            nextPayAmt = nextPayAmt.add(nextInterest);
                        	nextPayAmt = nextPayAmt.multiply(invsUnit);
                        	nextPayAmt = nextPayAmt.divide(pkgUnit, 2, RoundingMode.HALF_UP);
                        }
                    }
                    // 本金加利息为下期应付
                    totalNextPayAmt = totalNextPayAmt.add(nextPayAmt);
                    // 累加剩余几期的本金和利息
                    BigDecimal amount = BigDecimal.ZERO;
                    BigDecimal principalAndInterestAmt = BigDecimal.ZERO;
                    BigDecimal principalAndInterestFeitAmt = BigDecimal.ZERO;
                    BigDecimal payPrincipalAndInterestAmt = BigDecimal.ZERO;
                    BigDecimal payPrincipalAndInterestFeitAmt = BigDecimal.ZERO;
                    BigDecimal wrtrPrincipalAndInterestFeitAmt = BigDecimal.ZERO;
                    BigDecimal payWrtrPrincipalAndInterestFeitAmt = BigDecimal.ZERO;
                    for (PaymentSchedule paymentSchedule : paymentScheduleList) {
                        // 应付本金+应付利息
                        principalAndInterestAmt = principalAndInterestAmt.add(paymentSchedule.getPrincipalAmt().add(
                                paymentSchedule.getInterestAmt()));
                        // 实付本金+实付利息
                        payPrincipalAndInterestAmt = payPrincipalAndInterestAmt.add(paymentSchedule
                                .getPayPrincipalAmt().add(paymentSchedule.getPayInterestAmt()));
                        Integer brkGraceDays = ProdPkgUtil.getBreachGraceDays(paymentSchedule.getProductId());
                        if (ProdPkgUtil.needPayFineAmt(paymentSchedule.getPaymentDate(), currentWorkDate,
                        		brkGraceDays.longValue())) {
                            // 应付本金罚金+应付利息罚金
                            principalAndInterestFeitAmt = principalAndInterestFeitAmt.add(paymentSchedule
                                    .getPrincipalForfeit().add(paymentSchedule.getInterestForfeit()));
                            // 实付本金罚金+实付利息罚金
                            payPrincipalAndInterestFeitAmt = payPrincipalAndInterestFeitAmt.add(paymentSchedule
                                    .getPayPrincipalForfeit().add(paymentSchedule.getPayInterestForfeit()));
                        }

                        if (ProdPkgUtil.needPayOverdueCmpnsFineAmt(
                                paymentSchedule.getPaymentDate(),
                                currentWorkDate,
                                Arrays.asList(new Long[] { product.getCmpnsGracePeriod(),
                                        product.getOverdue2CmpnsGracePeriod() }))) {
                            // 担保方应付本金罚金+担保方应付利息罚金
                            wrtrPrincipalAndInterestFeitAmt = wrtrPrincipalAndInterestFeitAmt.add(
                                    paymentSchedule.getWrtrInterestForfeit()).add(paymentSchedule.getWrtrPrinForfeit());
                            // 实付担保方应付本金罚金+实付担保方应付利息罚金
                            payWrtrPrincipalAndInterestFeitAmt = payWrtrPrincipalAndInterestFeitAmt.add(
                                    paymentSchedule.getPayWrtrInterestForfeit()).add(
                                    paymentSchedule.getPayWrtrPrinForfeit());
                        }
                    }
                    amount = principalAndInterestAmt.add(principalAndInterestFeitAmt)
                            .add(wrtrPrincipalAndInterestFeitAmt).subtract(payPrincipalAndInterestFeitAmt)
                            .subtract(payPrincipalAndInterestAmt).subtract(payWrtrPrincipalAndInterestFeitAmt);
                    amount = amount.multiply(invsUnit);
                    amount = amount.divide(pkgUnit, 2, RoundingMode.HALF_UP);
                    
                    totalRestAmt = totalRestAmt.add(amount);
                }
            }
        }
        map.put("totalPrincipal", totalPrincipal);
        map.put("totalNextPayAmt", totalNextPayAmt);
        map.put("totalRestAmt", totalRestAmt);
        return map;
    }
    
    /**
     * 交易部-手工还款列表
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.FINANCING_PACKAGE_MANUAL_PAY_URL)
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_TRANSACTION_MANUAL_PAYMENTS })
    public String renderTransStopPackageList(HttpServletRequest request, HttpSession session, Model model) {
        return "packet/packet_manage_trans_pay";
    }

    /**
     * 交易部-终止申购列表
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.FINANCING_PACKAGE_STOP_URL)
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_PACKAGE_STOP,
            Permissions.PRODUCT_FINANCING_TRANSACTION_SETTINGS })
    public String renderTransPackageList(HttpServletRequest request, HttpSession session, Model model) {
        return "packet/packet_manage_trans_stop";
    }

    /**
     * 交易经理-提前还款列表
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.FINANCING_PACKAGE_PRE_PAYMENT_URL)
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_PACKAGE_PREPAYMENT_NO_PENALTY })
    public String renderTransAdvancePackageList(HttpServletRequest request, HttpSession session, Model model) {
        return "packet/packet_manage_trans_advance";
    }

    /**
     * 风控部-融资包放款审批列表
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.FINANCING_PACKAGE_LOAN_APPROVE_URL)
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_LOAN_APPROVE })
    public String renderWindLoanApprovePackageList(HttpServletRequest request, HttpSession session, Model model) {
        List<EnumOption> productStatusList = new ArrayList<EnumOption>();
        productStatusList.add(new EnumOption("WAIT_LOAD_APPROAL", "待处理"));
        productStatusList.add(new EnumOption("WAIT_LOAD_APPROAL_CONFIRM", "已处理"));
        model.addAttribute("statusList", productStatusList);
        return "packet/packet_manage_wind_loan_approve";
    }

    /**
     * 风控部-融资包放款确认列表
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.FINANCING_PACKAGE_LOAN_APPROVE_CONFIRM_URL)
    @RequiresPermissions(value = Permissions.PRODUCT_FINANCING_LOAN_APPROVE_CONFIRM)
    public String renderWindLoanConfirmList(HttpServletRequest request, HttpSession session, Model model) {
        List<EnumOption> productStatusList = new ArrayList<EnumOption>();
        productStatusList.add(new EnumOption("WAIT_LOAD_APPROAL_CONFIRM", "待处理"));
        productStatusList.add(new EnumOption("PAYING", "已处理"));
        model.addAttribute("statusList", productStatusList);
        return "packet/packet_manage_wind_loan_approve_check";
    }

    /**
     * 管理员-异常撤单列表
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.SYSTEM_MANAGEMENT_ABNORMAL_WITHDRAW_URL)
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_PACKAGE_ABNORMAL_CANCEL })
    public String renderStopPackageList(HttpServletRequest request, HttpSession session, Model model) {
        return "packet/packet_manage_admin";
    }

    /**
     * 融资人-融资包维护列表
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.FINANCING_PACKAGE_MANAGEMENT_FINANCIER_URL)
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_PACKAGE_FINANCIER_VIEW })
    public String renderFinancierPackageList(HttpServletRequest request, HttpSession session, Model model) {
        if (securityContext.isFinancier()) {
            model.addAttribute("isFinancier", true);
        } else {
            model.addAttribute("isFinancier", false);
        }

        this.getEnumPackageStatus(model);
        return "packet/packet_manage_financier";
    }

    /**
     * 担保机构-融资包维护列表
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.FINANCING_PACKAGE_MANAGEMENT_WARR_URL)
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_PACKAGE_MEMBER_VIEW })
    public String renderWarrPackageList(HttpServletRequest request, HttpSession session, Model model) {
        return this.renderFinancierPackageList(request, session, model);
    }

    /**
     * 交易部查看页面
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.FINANCING_PACKAGE_TRANS_DEPT_VIEW_URL)
    @RequiresPermissions(value = Permissions.PRODUCT_FINANCING_PACKAGE_VIEW_FOR_TRANS)
    public String renderTransDeptPackageList(HttpServletRequest request, HttpSession session, Model model) {
        this.getEnumPackageStatus(model);
        Date currentDate = new Date();
        model.addAttribute(LiteralConstant.START_DATE, DateUtils.formatDate(currentDate, LiteralConstant.YYYY_MM_DD));
        return "packet/packet_manage_platform";
    }

    /**
     * 客服部查看页面
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.PACKAGE_LIST_FOR_CUSTOMER_SERVICE_DEPT_URL)
    @RequiresPermissions(value = Permissions.PRODUCT_FINANCING_PACKAGE_VIEW_FOR_CUST)
    public String renderCustomerDeptPackageList(HttpServletRequest request, HttpSession session, Model model) {
        this.getEnumPackageStatus(model);
        Date currentDate = new Date();
        model.addAttribute(LiteralConstant.START_DATE, DateUtils.formatDate(currentDate, LiteralConstant.YYYY_MM_DD));
        return "packet/packet_manage_platform";
    }

    /**
     * 结算部查看页面
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.FINANCING_PACKAGE_SETTLEMENT_DEPT_VIEW_URL)
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_PACKAGE_VIEW,
            Permissions.PRODUCT_FINANCING_SEARCH_SUMMARIZING_DETAIL })
    public String renderDeptPackageList(HttpServletRequest request, HttpSession session, Model model) {
        this.getEnumPackageStatus(model);
        Date currentDate = new Date();
        model.addAttribute(LiteralConstant.START_DATE, DateUtils.formatDate(currentDate, LiteralConstant.YYYY_MM_DD));
        return "packet/packet_manage_platform";
    }

    /**
     * 风控部查看页面
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.FINANCING_PACKAGE_RISK_DEPT_VIEW_URL)
    @RequiresPermissions(value = { Permissions.RISK_CONTROL_DEPT })
    public String renderRiskDeptPackageList(HttpServletRequest request, HttpSession session, Model model) {
        this.getEnumPackageStatus(model);
        Date currentDate = new Date();
        model.addAttribute(LiteralConstant.START_DATE, DateUtils.formatDate(currentDate, LiteralConstant.YYYY_MM_DD));
        return "packet/packet_manage_platform";
    }

    /**
     * 结算部查看页面
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "financingpackage/settdept/view")
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_PACKAGE_VIEW })
    public String renderSettDeptPackageList(HttpServletRequest request, HttpSession session, Model model) {
        this.getEnumPackageStatus(model);
        Date currentDate = new Date();
        model.addAttribute(LiteralConstant.START_DATE, DateUtils.formatDate(currentDate, LiteralConstant.YYYY_MM_DD));
        return "packet/packet_manage_platform";
    }

    /**
     * 
     * Description: 获取融资包列表数据
     * 
     * @param request
     * @param searchDto
     * @return
     */
    @RequestMapping(value = "financingpackage/financier/getpackagelist")
    @ResponseBody
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_PACKAGE_FINANCIER_VIEW,
            Permissions.PRODUCT_FINANCING_PACKAGE_MEMBER_VIEW }, logical = Logical.OR)
    public DataTablesResponseDto<ProductPackageDto> getFinancierPackageList(HttpServletRequest request,
            @RequestBody FinancingPackageSearchDto searchDto) {
        DataTablesResponseDto<ProductPackageDto> result = new DataTablesResponseDto<ProductPackageDto>();
        result.setEcho(searchDto.getEcho());
        boolean isFinancier = securityContext.isFinancier();
        Page<FinancingPackageView> packageListForFinancierList = financingPackageService.getPackageListForFinancier(
                searchDto, securityContext.getCurrentUserId(), isFinancier);
        List<ProductPackageDto> packageDtoList = processPackageListForFinancierList(packageListForFinancierList);
        result.setTotalDisplayRecords(packageListForFinancierList.getTotalElements());
        result.setTotalRecords(packageListForFinancierList.getTotalElements());
        result.setData(packageDtoList);
        return result;
    }

    /**
     * 处理 PackageListForFinancierList 转为 ProductPackageDtoList
     * 
     * @param packageListForFinancierList
     * @return
     */
	public List<ProductPackageDto> processPackageListForFinancierList(
			Page<FinancingPackageView> packageListForFinancierList) {
		List<ProductPackageDto> packageDtoList = new ArrayList<ProductPackageDto>();
        boolean isMarketOpen = CommonBusinessUtil.isMarketOpen();
        for (FinancingPackageView financingPackage : packageListForFinancierList) {
            ProductPackageDto productPackageDto = ConverterService.convert(financingPackage, ProductPackageDto.class);
            productPackageDto.setSignContractTime(processSignContractTime(financingPackage.getSigningDt(),
                    financingPackage.getSignContractTime()));
            productPackageDto.setMarketOpen(isMarketOpen);
            if (financingPackage.getSupplyStartTime() != null) {
                productPackageDto.setSupplyStartTime(DateUtils.formatDate(financingPackage.getSupplyStartTime(),
                        DATE_FORMAT));
            }
            if (financingPackage.getSupplyEndTime() != null) {
                productPackageDto.setSupplyEndTime(DateUtils.formatDate(financingPackage.getSupplyEndTime(),
                        DATE_FORMAT));
            }

            if (productPackageDto.getStatus() == EPackageStatus.SUBSCRIBE && securityContext.canStopFinancingPackage()) {
                productPackageDto.setCanStopPackage(true);
            }
            if (productPackageDto.getStatus() == EPackageStatus.SUBSCRIBE
                    && securityContext.canCancelFinancingPackage()) {
                productPackageDto.setCanCancelPackage(true);
            }
            if (productPackageDto.getStatus() == EPackageStatus.WAIT_SIGN && securityContext.canSignContract()) {
                productPackageDto.setCanSignContract(true);

                BigDecimal financingPackageSigningTerm = CommonBusinessUtil.getFinancingPackageSigningTerm();
                Date lastSubsTime = financingPackage.getLastSubsTime();
                if (lastSubsTime != null) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(lastSubsTime);
                    cal.add(Calendar.DATE, financingPackageSigningTerm.intValue() - 1);
                    Date lastSign = cal.getTime();
                    productPackageDto.setLastSignDate(DateUtils.formatDate(lastSign, LiteralConstant.YYYY_MM_DD));
                }
            }
            if (productPackageDto.getStatus() == EPackageStatus.WAIT_SIGN
                    && securityContext.canCancelFinancingPackage()) {
                productPackageDto.setCanCancelPackage(true);
            }

            if (financingPackage.getWarrantyType() == EWarrantyType.MONITORASSETS
                    || financingPackage.getWarrantyType() == EWarrantyType.NOTHING) {
                productPackageDto.setWrtrName("--");
                productPackageDto.setWrtrNameShow("--");
            }

            // 还款中的记录
            List<PaymentSchedule> paymentSchedules = paymentScheduleService
                    .getAllNormalPaymentSchedules(financingPackage.getId());

            List<PaymentSchedule> dueSchedules = paymentScheduleRepository.getByPackageIdAndStatusIn(
                    financingPackage.getId(),
                    Arrays.asList(EPayStatus.OVERDUE, EPayStatus.COMPENSATING, EPayStatus.COMPENSATORY));

            // 还款中，不是最后一期且当前无违约情况
            if (productPackageDto.getStatus() == EPackageStatus.PAYING
                    && securityContext.canPrepaymentFinancingPackage() && paymentSchedules != null
                    && paymentSchedules.size() > 1 && (dueSchedules == null || dueSchedules.isEmpty())) {
                productPackageDto.setCanPrepayment(true);
            }
            if (productPackageDto.getStatus() == EPackageStatus.PAYING
            		||productPackageDto.getStatus() == EPackageStatus.END) {
                productPackageDto.setCanViewRepayTable(true);
            }
            if (financingPackage.getSubsPercent() != null && financingPackage.getSubsPercent().startsWith(".")) {
                productPackageDto.setSubsPercent("0" + financingPackage.getSubsPercent());
            }
            if (productPackageDto.getWarrantyType() == EWarrantyType.MONITORASSETS
                    || productPackageDto.getWarrantyType() == EWarrantyType.NOTHING) {
                productPackageDto.setWrtrName(null);
                productPackageDto.setWrtrNameShow(null);
            }
            packageDtoList.add(productPackageDto);
        }
		return packageDtoList;
	}

    @RequestMapping(value = "financingpackage/platform/getpackagelist")
    @ResponseBody
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_PACKAGE_VIEW })
    public DataTablesResponseDto<ProductPackageDto> getPlatformPackageList(HttpServletRequest request,
            @RequestBody FinancingPackageSearchDto searchDto) {
        DataTablesResponseDto<ProductPackageDto> result = new DataTablesResponseDto<ProductPackageDto>();
        result.setEcho(searchDto.getEcho());
        Page<FinancingPackageView> packageListForFinancierList = financingPackageService.getPackageListForPlatform(
                searchDto, securityContext.getCurrentUserId());
        List<ProductPackageDto> packageDtoList = new ArrayList<ProductPackageDto>();
        for (FinancingPackageView financingPackage : packageListForFinancierList) {
            ProductPackageDto productPackageDto = modifyProductPackageDto(financingPackage);
            /**
             * productPackageDto.setSignContractTime(processSignContractTime(financingPackage.getSigningDt(), financingPackage.getSignContractTime()));
             **/
            //以上setSignContractTime签约时间：系统工作日期+系统实时时间；有误，调整为如下方式
            if (financingPackage.getSigningDt() == null) {
            	productPackageDto.setSignContractTime("");
            } else {
            	productPackageDto.setSignContractTime(DateUtils.formatDate(financingPackage.getSigningDt(), LiteralConstant.YYYY_MM_DD));
            }
            if (financingPackage.getSupplyStartTime() != null) {
                productPackageDto.setSupplyStartTime(DateUtils.formatDate(financingPackage.getSupplyStartTime(),
                        DATE_FORMAT));
            }
            if (financingPackage.getSupplyEndTime() != null) {
                productPackageDto.setSupplyEndTime(DateUtils.formatDate(financingPackage.getSupplyEndTime(),
                        DATE_FORMAT));
            }
            if (financingPackage.getStatus() == EPackageStatus.END && financingPackage.getLastModifyTs() != null) {
                productPackageDto.setLastTime(DateUtils.formatDate(financingPackage.getLastModifyTs(), "yyyy-MM-dd"));
            } else {
                productPackageDto.setLastTime("--");
            }
            if (financingPackage.getSubsPercent() != null && financingPackage.getSubsPercent().startsWith(".")) {
                productPackageDto.setSubsPercent("0" + financingPackage.getSubsPercent());
            }
            if (productPackageDto.getStatus() == EPackageStatus.PAYING
            		||productPackageDto.getStatus() == EPackageStatus.END) {
                productPackageDto.setCanViewRepayTable(true);
            }
            if (securityContext.canPrintDebtInfo()) {
                productPackageDto.setCanPrintDebtInfo(true);
            }
            if (productPackageDto.getWarrantyType() == EWarrantyType.MONITORASSETS
                    || productPackageDto.getWarrantyType() == EWarrantyType.NOTHING) {
                productPackageDto.setWrtrName(null);
                productPackageDto.setWrtrNameShow(null);
            }else{
                productPackageDto.setWrtrName(financingPackage.getWrtrName());
                productPackageDto.setWrtrNameShow(financingPackage.getWrtrNameShow());
            }
            packageDtoList.add(productPackageDto);
        }
        result.setTotalDisplayRecords(packageListForFinancierList.getTotalElements());
        result.setTotalRecords(packageListForFinancierList.getTotalElements());
        result.setData(packageDtoList);

        return result;
    }
    
    /**
     * 获取融资包汇总数据
     * 
     * @param request
     * @param model
     * @param fundPoolDtlSearchDto
     * @return
     */
    @ResponseBody
    @RequestMapping("financingpackag/pkg_sum")
    public PackageSumDto getSum(HttpServletRequest request, Model model,
            @RequestBody FinancingPackageSearchDto searchDto) {
        return financingPackageService.getPackageSum(searchDto);
    }

    private String processSignContractTime(Date signingDate, Date signContractTime) {
        if (signingDate != null && signContractTime != null) {
            String formatDate = DateUtils.formatDate(signingDate, LiteralConstant.YYYY_MM_DD);
            String formatTime = DateUtils.formatDate(signContractTime, "HH:mm");

            return formatDate + " " + formatTime;
        }
        return null;
    }

    /**
     * Description: 异常撤单表
     * 
     * @param request
     * @param searchDto
     * @return
     */
    @RequestMapping(value = "financingpackage/withdraw/getpackagelist")
    @ResponseBody
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_PACKAGE_ABNORMAL_CANCEL })
    public DataTablesResponseDto<ProductPackageDetailsDto> getAdminPackageList(HttpServletRequest request,
            @RequestBody FinancingPackageSearchDto searchDto) {
        DataTablesResponseDto<ProductPackageDetailsDto> result = new DataTablesResponseDto<ProductPackageDetailsDto>();
        result.setEcho(searchDto.getEcho());
        Page<FinancingPackageView> packageListForFinancierList = financingPackageService.getPackageListForAction(
                searchDto, securityContext.getCurrentUserId(), EPackageActionType.WITHDRAW);
        List<ProductPackageDetailsDto> packageDtoList = new ArrayList<ProductPackageDetailsDto>();
        for (FinancingPackageView financingPackage : packageListForFinancierList) {
            ProductPackageDetailsDto productPackageDetailsDto = ConverterService.convert(financingPackage,
                    ProductPackageDetailsDto.class);
            productPackageDetailsDto.setSignContractTime(processSignContractTime(financingPackage.getSigningDt(),
                    financingPackage.getSignContractTime()));
            if (financingPackage.getSupplyStartTime() != null) {
                productPackageDetailsDto.setSupplyStartTime(DateUtils.formatDate(financingPackage.getSupplyStartTime(),
                        DATE_FORMAT));
            }
            if (financingPackage.getSupplyEndTime() != null) {
                productPackageDetailsDto.setSupplyEndTime(DateUtils.formatDate(financingPackage.getSupplyEndTime(),
                        DATE_FORMAT));
            }
            if ((productPackageDetailsDto.getStatus() == EPackageStatus.SUBSCRIBE || productPackageDetailsDto
                    .getStatus() == EPackageStatus.WAIT_SIGN)) {
                productPackageDetailsDto.setCanWithDraw(true);
            }
            if (financingPackage.getSubsPercent() != null && financingPackage.getSubsPercent().startsWith(".")) {
                productPackageDetailsDto.setSubsPercent("0" + financingPackage.getSubsPercent());
            }
            if (productPackageDetailsDto.getWarrantyType() == EWarrantyType.MONITORASSETS
                    || productPackageDetailsDto.getWarrantyType() == EWarrantyType.NOTHING) {
                productPackageDetailsDto.setWrtrName(null);
                productPackageDetailsDto.setWrtrNameShow(null);
            }else{
                productPackageDetailsDto.setWrtrName(financingPackage.getWrtrName());
                productPackageDetailsDto.setWrtrNameShow(financingPackage.getWrtrNameShow());
            }
            packageDtoList.add(productPackageDetailsDto);
        }
        result.setTotalDisplayRecords(packageListForFinancierList.getTotalElements());
        result.setTotalRecords(packageListForFinancierList.getTotalElements());
        result.setData(packageDtoList);

        return result;
    }

    /**
     * Description: 提前还款
     * 
     * @param request
     * @param searchDto
     * @return
     */
    @RequestMapping(value = "financingpackage/prepayment/getpackagelist")
    @ResponseBody
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_PACKAGE_PREPAYMENT_NO_PENALTY })
    public DataTablesResponseDto<ProductPackageDetailsDto> getPrePayPackageList(HttpServletRequest request,
            @RequestBody FinancingPackageSearchDto searchDto) {
        DataTablesResponseDto<ProductPackageDetailsDto> result = new DataTablesResponseDto<ProductPackageDetailsDto>();
        result.setEcho(searchDto.getEcho());
        Page<FinancingPackageView> packageListForFinancierList = financingPackageService.getPackagePaymentAdvanceList(
                searchDto, securityContext.getCurrentUserId());

        List<ProductPackageDetailsDto> packageDtoList = new ArrayList<ProductPackageDetailsDto>();
        int ontItem = 0;
        for (FinancingPackageView financingPackage : packageListForFinancierList) {
            ProductPackageDetailsDto productPackageDetailsDto = ConverterService.convert(financingPackage,
                    ProductPackageDetailsDto.class);
            productPackageDetailsDto.setSignContractTime(processSignContractTime(financingPackage.getSigningDt(),
                    financingPackage.getSignContractTime()));
            if (financingPackage.getSupplyEndTime() != null) {
                productPackageDetailsDto.setSupplyEndTime(DateUtils.formatDate(financingPackage.getSupplyEndTime(),
                        DATE_FORMAT));
            }
            if (productPackageDetailsDto.getStatus() == EPackageStatus.PAYING) {
                productPackageDetailsDto.setCanprepayment(true);
                productPackageDetailsDto.setCanViewPayTable(true);
            }
            if (financingPackage.getSubsPercent() != null && financingPackage.getSubsPercent().startsWith(".")) {
                productPackageDetailsDto.setSubsPercent("0" + financingPackage.getSubsPercent());
            }
            if (productPackageDetailsDto.getWarrantyType() == EWarrantyType.MONITORASSETS
                    || productPackageDetailsDto.getWarrantyType() == EWarrantyType.NOTHING) {
                productPackageDetailsDto.setWrtrName(null);
                productPackageDetailsDto.setWrtrNameShow(null);
            }else{
                productPackageDetailsDto.setWrtrName(financingPackage.getWrtrName());
                productPackageDetailsDto.setWrtrNameShow(financingPackage.getWrtrNameShow());
            }
            packageDtoList.add(productPackageDetailsDto);
        }

        // 设置页码
        result.setTotalDisplayRecords(packageListForFinancierList.getTotalElements() - ontItem);
        result.setTotalRecords(packageListForFinancierList.getTotalElements() - ontItem);
        result.setData(packageDtoList);

        return result;
    }

    /**
     * Description: 手工还款表
     * 
     * @param request
     * @param searchDto
     * @return
     */
    @RequestMapping(value = "financingpackage/manualpay/getpackagelist")
    @ResponseBody
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_TRANSACTION_MANUAL_PAYMENTS })
    public DataTablesResponseDto<ProductPackageDetailsDto> getTransPackageList(HttpServletRequest request,
            @RequestBody FinancingPackageSearchDto searchDto) {
        DataTablesResponseDto<ProductPackageDetailsDto> result = new DataTablesResponseDto<ProductPackageDetailsDto>();
        result.setEcho(searchDto.getEcho());
        Page<FinancingPackageView> packageListForFinancierList = financingPackageService.getPackageListForAction(
                searchDto, securityContext.getCurrentUserId(), EPackageActionType.MANUALPAY);
        List<ProductPackageDetailsDto> packageDtoList = new ArrayList<ProductPackageDetailsDto>();
        Date formatCurrentWorkDate = DateUtils.getDate(
                DateUtils.formatDate(CommonBusinessUtil.getCurrentWorkDate(), LiteralConstant.YYYY_MM_DD), LiteralConstant.YYYY_MM_DD);
        for (FinancingPackageView financingPackage : packageListForFinancierList) {
            ProductPackageDetailsDto productPackageDetailsDto = ConverterService.convert(financingPackage,
                    ProductPackageDetailsDto.class);

            List<PaymentSchedule> paymentScheduleList = financingPackage.getPaymentScheduleList();
            List<PaymentSchedule> allPaymentScheduleForPackage = this.paymentScheduleRepository
                    .getByPackageIdOrderBySequenceIdAsc(financingPackage.getId());
            if (CollectionUtils.isNotEmpty(paymentScheduleList)
                    && CollectionUtils.isNotEmpty(allPaymentScheduleForPackage)) {
                int size = allPaymentScheduleForPackage.size();

                for (PaymentSchedule ps : paymentScheduleList) {
                    String formatDate = DateUtils.formatDate(ps.getPaymentDate(), LiteralConstant.YYYY_MM_DD);
                    if (DateUtils.getDate(formatDate, LiteralConstant.YYYY_MM_DD).compareTo(formatCurrentWorkDate) == 0
                            && ps.getStatus() == EPayStatus.NORMAL) {
                        productPackageDetailsDto.setItemsString(ps.getSequenceId() + "/" + size);
                        for (PaymentSchedule paymentSche : allPaymentScheduleForPackage) {
                            if (paymentSche.getPaymentDate().compareTo(ps.getPaymentDate()) > 0) {
                                productPackageDetailsDto.setNextPayDate(DateUtils.formatDate(
                                        paymentSche.getPaymentDate(), LiteralConstant.YYYY_MM_DD));
                                break;
                            }
                        }
                        break;
                    }
                }
            }
            if (StringUtils.isBlank(productPackageDetailsDto.getNextPayDate())) {
                productPackageDetailsDto.setNextPayDate("--");
            }
            productPackageDetailsDto.setSignContractTime(processSignContractTime(financingPackage.getSigningDt(),
                    financingPackage.getSignContractTime()));
            if (financingPackage.getSupplyStartTime() != null) {
                productPackageDetailsDto.setSupplyStartTime(DateUtils.formatDate(financingPackage.getSupplyStartTime(),
                        DATE_FORMAT));
            }
            if (financingPackage.getSupplyEndTime() != null) {
                productPackageDetailsDto.setSupplyEndTime(DateUtils.formatDate(financingPackage.getSupplyEndTime(),
                        DATE_FORMAT));
            }
            if (productPackageDetailsDto.getStatus() == EPackageStatus.PAYING) {
                productPackageDetailsDto.setCanManualPay(true);
                productPackageDetailsDto.setCanViewPayTable(true);
            }
            if (financingPackage.getSubsPercent() != null && financingPackage.getSubsPercent().startsWith(".")) {
                productPackageDetailsDto.setSubsPercent("0" + financingPackage.getSubsPercent());
            }
            if (productPackageDetailsDto.getWarrantyType() == EWarrantyType.MONITORASSETS
                    || productPackageDetailsDto.getWarrantyType() == EWarrantyType.NOTHING) {
                productPackageDetailsDto.setWrtrName(null);
                productPackageDetailsDto.setWrtrNameShow(null);
            }else{
                productPackageDetailsDto.setWrtrName(financingPackage.getWrtrName());
                productPackageDetailsDto.setWrtrNameShow(financingPackage.getWrtrNameShow());
            }
            packageDtoList.add(productPackageDetailsDto);
        }
        result.setTotalDisplayRecords(packageListForFinancierList.getTotalElements());
        result.setTotalRecords(packageListForFinancierList.getTotalElements());
        result.setData(packageDtoList);

        return result;
    }

    /**
     * Description: 终止申购表
     * 
     * @param request
     * @param searchDto
     * @return
     */
    @RequestMapping(value = "financingpackage/stop/getpackagelist")
    @ResponseBody
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_PACKAGE_STOP })
    public DataTablesResponseDto<ProductPackageDetailsDto> getTransManagerPackageList(HttpServletRequest request,
            @RequestBody FinancingPackageSearchDto searchDto) {
        DataTablesResponseDto<ProductPackageDetailsDto> result = new DataTablesResponseDto<ProductPackageDetailsDto>();
        result.setEcho(searchDto.getEcho());
        Page<FinancingPackageView> packageListForFinancierList = financingPackageService.getPackageListForAction(
                searchDto, securityContext.getCurrentUserId(), EPackageActionType.STOP);
        List<ProductPackageDetailsDto> packageDtoList = new ArrayList<ProductPackageDetailsDto>();
        for (FinancingPackageView financingPackage : packageListForFinancierList) {
            ProductPackageDetailsDto productPackageDetailsDto = ConverterService.convert(financingPackage,
                    ProductPackageDetailsDto.class);
            productPackageDetailsDto.setSignContractTime(processSignContractTime(financingPackage.getSigningDt(),
                    financingPackage.getSignContractTime()));
            if (financingPackage.getSupplyStartTime() != null) {
                productPackageDetailsDto.setSupplyStartTime(DateUtils.formatDate(financingPackage.getSupplyStartTime(),
                        DATE_FORMAT));
            }
            if (financingPackage.getSupplyEndTime() != null) {
                productPackageDetailsDto.setSupplyEndTime(DateUtils.formatDate(financingPackage.getSupplyEndTime(),
                        DATE_FORMAT));
            }
            if (productPackageDetailsDto.getStatus() == EPackageStatus.SUBSCRIBE) {
                productPackageDetailsDto.setCanStopFinancingPackage(true);
            }
            if (financingPackage.getSubsPercent() != null && financingPackage.getSubsPercent().startsWith(".")) {
                productPackageDetailsDto.setSubsPercent("0" + financingPackage.getSubsPercent());
            }
            if (productPackageDetailsDto.getWarrantyType() == EWarrantyType.MONITORASSETS
                    || productPackageDetailsDto.getWarrantyType() == EWarrantyType.NOTHING) {
                productPackageDetailsDto.setWrtrName(null);
                productPackageDetailsDto.setWrtrNameShow(null);
            }else{
                productPackageDetailsDto.setWrtrName(financingPackage.getWrtrName());
                productPackageDetailsDto.setWrtrNameShow(financingPackage.getWrtrNameShow());
            }
            packageDtoList.add(productPackageDetailsDto);
        }
        result.setTotalDisplayRecords(packageListForFinancierList.getTotalElements());
        result.setTotalRecords(packageListForFinancierList.getTotalElements());
        result.setData(packageDtoList);

        return result;
    }

    /**
     * Description: 放款审批表
     * 
     * @param request
     * @param searchDto
     * @return
     */
    @RequestMapping(value = "financingpackage/approve/getpackagelist")
    @ResponseBody
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_LOAN_APPROVE })
    public DataTablesResponseDto<ProductPackageDetailsDto> getLoanPackageList(HttpServletRequest request,
            @RequestBody FinancingPackageSearchDto searchDto) {
        DataTablesResponseDto<ProductPackageDetailsDto> result = new DataTablesResponseDto<ProductPackageDetailsDto>();
        result.setEcho(searchDto.getEcho());
        Page<FinancingPackageView> packageListForFinancierList = financingPackageService.getPackageListForAction(
                searchDto, securityContext.getCurrentUserId(), EPackageActionType.APPROVELOAN);
        List<ProductPackageDetailsDto> packageDtoList = new ArrayList<ProductPackageDetailsDto>();
        for (FinancingPackageView financingPackage : packageListForFinancierList) {
            ProductPackageDetailsDto productPackageDetailsDto = ConverterService.convert(financingPackage,
                    ProductPackageDetailsDto.class);
            productPackageDetailsDto.setSignContractTime(processSignContractTime(financingPackage.getSigningDt(),
                    financingPackage.getSignContractTime()));
            if (financingPackage.getSupplyStartTime() != null) {
                productPackageDetailsDto.setSupplyStartTime(DateUtils.formatDate(financingPackage.getSupplyStartTime(),
                        DATE_FORMAT));
            }
            if (financingPackage.getSupplyEndTime() != null) {
                productPackageDetailsDto.setSupplyEndTime(DateUtils.formatDate(financingPackage.getSupplyEndTime(),
                        DATE_FORMAT));
            }
            if (productPackageDetailsDto.getStatus() == EPackageStatus.WAIT_LOAD_APPROAL) {
                productPackageDetailsDto.setCanLoanApprove(true);
            }
            if (financingPackage.getSubsPercent() != null && financingPackage.getSubsPercent().startsWith(".")) {
                productPackageDetailsDto.setSubsPercent("0" + financingPackage.getSubsPercent());
            }
            if (productPackageDetailsDto.getWarrantyType() == EWarrantyType.MONITORASSETS
                    || productPackageDetailsDto.getWarrantyType() == EWarrantyType.NOTHING) {
                productPackageDetailsDto.setWrtrName(null);
                productPackageDetailsDto.setWrtrNameShow(null);
            }else{
                productPackageDetailsDto.setWrtrName(financingPackage.getWrtrName());
                productPackageDetailsDto.setWrtrNameShow(financingPackage.getWrtrNameShow());
            }
            productPackageDetailsDto.setRate(FormatRate.formatRate(productPackageDetailsDto.getRate().multiply(NumberUtil.getHundred())));
            packageDtoList.add(productPackageDetailsDto);
        }
        result.setTotalDisplayRecords(packageListForFinancierList.getTotalElements());
        result.setTotalRecords(packageListForFinancierList.getTotalElements());
        result.setData(packageDtoList);

        return result;
    }

    /**
     * Description: 放款表
     * 
     * @param request
     * @param searchDto
     * @return
     */
    @RequestMapping(value = "financingpackage/approveconfirm/getpackagelist")
    @ResponseBody
    @RequiresPermissions(value = Permissions.PRODUCT_FINANCING_LOAN_APPROVE_CONFIRM)
    public DataTablesResponseDto<ProductPackageDetailsDto> getLoanConfirmPackageList(HttpServletRequest request,
            @RequestBody FinancingPackageSearchDto searchDto) {
        DataTablesResponseDto<ProductPackageDetailsDto> result = new DataTablesResponseDto<ProductPackageDetailsDto>();
        result.setEcho(searchDto.getEcho());
        Page<FinancingPackageView> packageListForFinancierList = financingPackageService.getPackageListForAction(
                searchDto, securityContext.getCurrentUserId(), EPackageActionType.APPROVELOAN_CONFIRM);
        List<ProductPackageDetailsDto> packageDtoList = new ArrayList<ProductPackageDetailsDto>();
        for (FinancingPackageView financingPackage : packageListForFinancierList) {
            ProductPackageDetailsDto productPackageDetailsDto = ConverterService.convert(financingPackage,
                    ProductPackageDetailsDto.class);
            productPackageDetailsDto.setSignContractTime(processSignContractTime(financingPackage.getSigningDt(),
                    financingPackage.getSignContractTime()));
            if (financingPackage.getSupplyStartTime() != null) {
                productPackageDetailsDto.setSupplyStartTime(DateUtils.formatDate(financingPackage.getSupplyStartTime(),
                        DATE_FORMAT));
            }
            if (financingPackage.getSupplyEndTime() != null) {
                productPackageDetailsDto.setSupplyEndTime(DateUtils.formatDate(financingPackage.getSupplyEndTime(),
                        DATE_FORMAT));
            }
            if (productPackageDetailsDto.getStatus() == EPackageStatus.WAIT_LOAD_APPROAL_CONFIRM) {
                productPackageDetailsDto.setCanLoanApproveConfirm(true);
            }
            if (financingPackage.getSubsPercent() != null && financingPackage.getSubsPercent().startsWith(".")) {
                productPackageDetailsDto.setSubsPercent("0" + financingPackage.getSubsPercent());
            }
            if (productPackageDetailsDto.getWarrantyType() == EWarrantyType.MONITORASSETS
                    || productPackageDetailsDto.getWarrantyType() == EWarrantyType.NOTHING) {
                productPackageDetailsDto.setWrtrName(null);
                productPackageDetailsDto.setWrtrNameShow(null);
            }else{
                productPackageDetailsDto.setWrtrName(financingPackage.getWrtrName());
                productPackageDetailsDto.setWrtrNameShow(financingPackage.getWrtrNameShow());
            }
            productPackageDetailsDto.setRate(FormatRate.formatRate(productPackageDetailsDto.getRate().multiply(BigDecimal.valueOf(100))));
            packageDtoList.add(productPackageDetailsDto);
        }
        result.setTotalDisplayRecords(packageListForFinancierList.getTotalElements());
        result.setTotalRecords(packageListForFinancierList.getTotalElements());
        result.setData(packageDtoList);

        return result;
    }

    private void getEnumPackageStatus(Model model) {
        List<EnumOption> enumOptions = getStaticOptions(EPackageStatus.class, true);
        for (int i = 0; i < enumOptions.size(); i++) {
            if (enumOptions.get(i).getCode().equals(String.valueOf("TRANSEND"))
                    || enumOptions.get(i).getCode().equals(String.valueOf("TRANSFERING"))) {
                enumOptions.remove(i);
                i = 0;
            }
        }
        model.addAttribute("statusList", enumOptions);
    }

    /**
     * 
     * Description: 获取还款表数据
     * 
     * @param financingPackage
     * @return
     */
    private ProductPackageDto modifyProductPackageDto(FinancingPackageView financingPackage) {
        ProductPackageDto productPackageDto = ConverterService.convert(financingPackage, ProductPackageDto.class);
        List<PaymentSchedule> paymentSchedules = this.paymentScheduleRepository
                .getByPackageIdOrderBySequenceIdAsc(financingPackage.getId());

        Date date = CommonBusinessUtil.getCurrentWorkDate();
        SimpleDateFormat format = new SimpleDateFormat(LiteralConstant.YYYY_MM_DD);
        String dateString = format.format(date);
        Date currentDate = null;
        try {
            currentDate = format.parse(dateString);
        } catch (ParseException e) {
        	LOGGER.error("ParseException {}", e);
        }
        for (PaymentSchedule paymentSchedule : paymentSchedules) {
            if (paymentSchedule != null && currentDate != null && paymentSchedule.getPaymentDate() != null) {
                String dateString2 = format.format(paymentSchedule.getPaymentDate());
                Date paymentDate = null;
                try {
                    paymentDate = format.parse(dateString2);
                } catch (ParseException e) {
                	LOGGER.error("ParseException {}", e);
                }

                if (paymentDate == null) {
                    return productPackageDto;
                }
                if (paymentSchedule.getStatus() == EPayStatus.NORMAL
                        && (paymentDate.after(currentDate) || paymentDate.equals(currentDate))) {
                    String dateString1 = format.format(paymentSchedule.getPaymentDate());
                    productPackageDto.setNextPayDate(dateString1);

                    StringBuffer sBuffer = new StringBuffer();
                    sBuffer.append(paymentSchedule.getSequenceId()).append("/").append(paymentSchedules.size());
                    productPackageDto.setItemsString(sBuffer.toString());
                    break;
                }
            }
        }

        if (productPackageDto.getNextPayDate() == null) {
            productPackageDto.setNextPayDate("- -");
            productPackageDto.setItemsString("- -");
        }

        return productPackageDto;
    }

    /**
     * 
     * Description:
     * 
     * @param financingPackage
     * @return
     */
    @SuppressWarnings("unused")
    private ProductPackageDetailsDto modifyProductPackageDetailsDto(FinancingPackageView financingPackage) {
        ProductPackageDetailsDto productPackageDto = ConverterService.convert(financingPackage,
                ProductPackageDetailsDto.class);
        List<PaymentSchedule> paymentSchedules = this.paymentScheduleRepository
                .getByPackageIdOrderBySequenceIdAsc(financingPackage.getId());

        SimpleDateFormat format = new SimpleDateFormat(LiteralConstant.YYYY_MM_DD);
        String dateString = format.format(CommonBusinessUtil.getCurrentWorkDate());
        Date currentDate = null;
        try {
            currentDate = format.parse(dateString);
        } catch (ParseException e) {
        	LOGGER.error("ParseException {}", e);
        }

        for (PaymentSchedule paymentSchedule : paymentSchedules) {
            if (paymentSchedule != null && currentDate != null && paymentSchedule.getPaymentDate() != null) {
                String dateString2 = format.format(paymentSchedule.getPaymentDate());
                Date paymentDate = null;
                try {
                    paymentDate = format.parse(dateString2);
                } catch (ParseException e) {
                	LOGGER.error("ParseException {}", e);
                }

                if (paymentDate == null) {
                    return productPackageDto;
                }

                if (paymentSchedule.getStatus() == EPayStatus.NORMAL
                        && (paymentDate.after(currentDate) || paymentDate.equals(currentDate))) {
                    String dateString1 = format.format(paymentSchedule.getPaymentDate());
                    productPackageDto.setNextPayDate(dateString1);

                    StringBuffer sBuffer = new StringBuffer();
                    sBuffer.append(paymentSchedule.getSequenceId()).append("/").append(paymentSchedules.size());
                    productPackageDto.setItemsString(sBuffer.toString());
                    break;
                }
            }
        }

        if (productPackageDto.getNextPayDate() == null) {
            productPackageDto.setNextPayDate("--");
            productPackageDto.setItemsString("--");
        }

        return productPackageDto;
    }

    
    /* get和set方法给/app控制器注入使用  */
	public SecurityContext getSecurityContext() {
		return securityContext;
	}

	public void setSecurityContext(SecurityContext securityContext) {
		this.securityContext = securityContext;
	}

	public FinancingPackageService getFinancingPackageService() {
		return financingPackageService;
	}

	public void setFinancingPackageService(
			FinancingPackageService financingPackageService) {
		this.financingPackageService = financingPackageService;
	}

	public PaymentScheduleRepository getPaymentScheduleRepository() {
		return paymentScheduleRepository;
	}

	public void setPaymentScheduleRepository(
			PaymentScheduleRepository paymentScheduleRepository) {
		this.paymentScheduleRepository = paymentScheduleRepository;
	}

	public PaymentScheduleService getPaymentScheduleService() {
		return paymentScheduleService;
	}

	public void setPaymentScheduleService(
			PaymentScheduleService paymentScheduleService) {
		this.paymentScheduleService = paymentScheduleService;
	}

	public ProductService getProductService() {
		return productService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}
    
}
