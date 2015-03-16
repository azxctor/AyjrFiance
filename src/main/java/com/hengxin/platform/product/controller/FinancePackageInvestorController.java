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
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hengxin.platform.account.enums.ETradeType;
import com.hengxin.platform.common.controller.BaseController;
import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.domain.DynamicOption;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.common.dto.annotation.OnValid;
import com.hengxin.platform.common.enums.EOptionCategory;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.common.util.FormatRate;
import com.hengxin.platform.common.util.MessageUtil;
import com.hengxin.platform.fund.entity.PositionLotPo;
import com.hengxin.platform.fund.enums.EFundTrdType;
import com.hengxin.platform.fund.repository.PkgTradeJnlRepository;
import com.hengxin.platform.fund.service.PositionLotService;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.market.service.FinancingMarketService;
import com.hengxin.platform.member.domain.Agency;
import com.hengxin.platform.member.repository.UserRepository;
import com.hengxin.platform.member.service.MemberService;
import com.hengxin.platform.member.service.SubscribeGroupService;
import com.hengxin.platform.product.ProdPkgUtil;
import com.hengxin.platform.product.converter.InvestorPackageConverter;
import com.hengxin.platform.product.domain.CreditorRightsTransfer;
import com.hengxin.platform.product.domain.FinancingPackageView;
import com.hengxin.platform.product.domain.PaymentSchedule;
import com.hengxin.platform.product.domain.Product;
import com.hengxin.platform.product.dto.FinancePackageInvestorDto;
import com.hengxin.platform.product.dto.FinancingPackageSearchDto;
import com.hengxin.platform.product.dto.ProductDetailsDto;
import com.hengxin.platform.product.dto.ProductPackageDetailsDto;
import com.hengxin.platform.product.dto.ProductPackageInvestorDetailsDto;
import com.hengxin.platform.product.dto.TransferPriceDto;
import com.hengxin.platform.product.enums.EFinancierScoreType;
import com.hengxin.platform.product.enums.EPackageStatus;
import com.hengxin.platform.product.enums.EPayStatus;
import com.hengxin.platform.product.enums.EProductScoreType;
import com.hengxin.platform.product.enums.EWarrantyType;
import com.hengxin.platform.product.service.CreditorTransferMaintainServie;
import com.hengxin.platform.product.service.CreditorTransferService;
import com.hengxin.platform.product.service.FeeGroupService;
import com.hengxin.platform.product.service.FinancingPackageService;
import com.hengxin.platform.product.service.PaymentScheduleService;
import com.hengxin.platform.product.service.ProductService;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.entity.UserPo;

/**
 * Class Name: FinancePackageInvestorController
 * 
 * @author shengzhou
 * 
 */
@RequestMapping(value = "financingpackage/investor")
@Controller
public class FinancePackageInvestorController extends BaseController {

    private static final String YYYY_MM_DD = "yyyy-MM-dd";
    private static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    @Autowired
    private SecurityContext securityContext;

    @Autowired
    private FinancingPackageService financingPackageService;

    @Autowired
    private SubscribeGroupService subscribeGroupService;

    @Autowired
    private FeeGroupService feeGroupService;

    @Autowired
    private FinancingMarketService financingMarketService;

    @Autowired
    private CreditorTransferService creditorTransferService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PositionLotService positionLotService;

    @Autowired
    private CreditorTransferMaintainServie creditorTransferMaintainServie;

    @Autowired
    private MemberService memberService;

    @Autowired
    private PkgTradeJnlRepository pkgTradeJnlRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PaymentScheduleService paymentScheduleService;

    /**
     * 
     * Description: 我的债权列表
     * 
     * @param request
     * @param searchDto
     * @return
     */
    @Transactional(readOnly = true)
    @RequestMapping(value = "search/getpackagelist")
    @ResponseBody
    @RequiresPermissions(value = { Permissions.MY_ACCOUNT_MY_CREDITOR_VIEW })
    public DataTablesResponseDto<FinancePackageInvestorDto> getInvestorPackageList(HttpServletRequest request,
            @RequestBody FinancingPackageSearchDto searchDto) {
        DataTablesResponseDto<FinancePackageInvestorDto> result = new DataTablesResponseDto<FinancePackageInvestorDto>();
        result.setEcho(searchDto.getEcho());
        Page<PositionLotPo> packageListForInverstorList = financingPackageService.getFinancingPackageListByUserId(
                securityContext.getCurrentUserId(), searchDto);
        List<FinancePackageInvestorDto> packageDtoList = new ArrayList<FinancePackageInvestorDto>();
        Date currentWorkDate = CommonBusinessUtil.getCurrentWorkDate();
        Date processDate = DateUtils.getDate("2014-05-01 00:00:00", YYYY_MM_DD_HH_MM_SS);
        for (PositionLotPo investorPackage : packageListForInverstorList) {
        	FinancePackageInvestorDto financePackageInvestorDto = processPositionLotPo(
					currentWorkDate, processDate, investorPackage);
            packageDtoList.add(financePackageInvestorDto);
        }
        long size = packageListForInverstorList.getTotalElements();
        result.setTotalDisplayRecords(size);
        result.setTotalRecords(size);
        result.setData(packageDtoList);
        return result;
    }
    
    private void calculateTransferMinPrice(FinancePackageInvestorDto financePackageInvestorDto, PositionLotPo investorPackage) {
    	if (financePackageInvestorDto.getResidualPrincipalAmt() == null || CommonBusinessUtil.getTransferMinRate() == null) {
    		financePackageInvestorDto.setTransferMinPrice(BigDecimal.ZERO);
    	} else {
    		FinancingPackageView pkgView = investorPackage.getPosition().getFinancingPackageView();
        	BigDecimal supplyAmt = pkgView.getSupplyAmount();
        	BigDecimal unitAmt = pkgView.getUnitAmount();
        	BigDecimal pkgUnit = supplyAmt.divide(unitAmt);
        	BigDecimal invsUnit = BigDecimal.valueOf(investorPackage.getUnit());
    		
	    	BigDecimal totalPayedInterestAmt = paymentScheduleService.getTotalPayedInterest(financePackageInvestorDto.getPkgId());
	    	if (totalPayedInterestAmt == null) {
	    		totalPayedInterestAmt = BigDecimal.ZERO;
	    	}
			financePackageInvestorDto.setTransferMinPrice(financePackageInvestorDto
					.getResidualPrincipalAmt().subtract(totalPayedInterestAmt.multiply(invsUnit).divide(pkgUnit, 2, RoundingMode.HALF_UP))
					.multiply(CommonBusinessUtil.getTransferMinRate()).setScale(2, RoundingMode.HALF_UP));
    	}
    }
    
    private void calculateTransferMaxPrice(FinancePackageInvestorDto financePackageInvestorDto, PositionLotPo investorPackage) {
    	BigDecimal transferMaxPrice = BigDecimal.ZERO;
    	
    	Date currentWorkDate = CommonBusinessUtil.getCurrentWorkDate();
    	Date signedDate = investorPackage.getPosition().getFinancingPackageView().getSigningDt();
    	Date previousPaymentDate = paymentScheduleService.findPreviousPaymentDate(investorPackage.getPosition().getPkgId(), currentWorkDate);
    	if (previousPaymentDate == null) {
    		previousPaymentDate = signedDate;
    	}
    	BigDecimal rate = financePackageInvestorDto.getRate().divide(BigDecimal.valueOf(100));
    	BigDecimal residualPrincipalAmt = financePackageInvestorDto.getResidualPrincipalAmt();
    	if (residualPrincipalAmt == null) {
    		residualPrincipalAmt = BigDecimal.ZERO;
    	}
    	BigDecimal buyAmt = financePackageInvestorDto.getAmount();
    	BigDecimal daysOfPreviousPaymentDate = BigDecimal.valueOf(DateUtils.betweenDays(previousPaymentDate, currentWorkDate));
    	BigDecimal daysOfSignedDate = BigDecimal.valueOf(DateUtils.betweenDays(signedDate, currentWorkDate));
    	BigDecimal yearDays = BigDecimal.valueOf(360);
    	
    	switch (financePackageInvestorDto.getPayMethod()) {
    	case MONTH_AVERAGE_INTEREST:
    		transferMaxPrice = transferMaxPrice.add(residualPrincipalAmt).add(rate.multiply(residualPrincipalAmt).multiply(daysOfPreviousPaymentDate).divide(yearDays, 2, RoundingMode.HALF_UP));
    		break;
    	case MONTH_PRINCIPAL_INTEREST:
    	case MONTH_INTEREST:
    		transferMaxPrice = transferMaxPrice.add(residualPrincipalAmt).add(rate.multiply(buyAmt).multiply(daysOfPreviousPaymentDate).divide(yearDays, 2, RoundingMode.HALF_UP));
    		break;
    	case ONCE_FOR_ALL:
    		transferMaxPrice = transferMaxPrice.add(residualPrincipalAmt).add(rate.multiply(buyAmt).multiply(daysOfSignedDate).divide(yearDays, 2, RoundingMode.HALF_UP));
    		break;
    	default:
    		break;
    	}
    	
    	financePackageInvestorDto.setTransferMaxPrice(transferMaxPrice);
    }

	public FinancePackageInvestorDto processPositionLotPo(Date currentWorkDate, Date processDate,
			PositionLotPo investorPackage) {
		boolean deductFlag = false;
        FinancePackageInvestorDto financePackageInvestorDto = ConverterService.convert(investorPackage,
                FinancePackageInvestorDto.class, InvestorPackageConverter.class);
        
        Date signingDt = investorPackage.getPosition().getFinancingPackageView().getSigningDt();

        if (processDate.compareTo(signingDt) <= 0) {
            deductFlag = true;
        }
        financePackageInvestorDto.setDeductFlag(deductFlag);
        if (investorPackage.getPosition() != null) {
        	FinancingPackageView pkgView = investorPackage.getPosition().getFinancingPackageView();
        	BigDecimal supplyAmt = pkgView.getSupplyAmount();
        	BigDecimal unitAmt = pkgView.getUnitAmount();
        	BigDecimal pkgUnit = supplyAmt.divide(unitAmt);
        	BigDecimal invsUnit = BigDecimal.valueOf(investorPackage.getUnit());

        	String productId = pkgView.getProductId();
            Product product = productService.getProductById(productId);
            // 按时间升序查询状态为还款中的第一个记录作为当前还款记录
            List<PaymentSchedule> paymentScheduleList = financingPackageService
                    .getCurrentPackageScheduleListByPkgId(pkgView.getId());
            if (paymentScheduleList != null && paymentScheduleList.size() > 0) {
                PaymentSchedule currentPayment = null;
                // 获取当前Schedule（状态为还款中）
                for (PaymentSchedule tempPayment : paymentScheduleList) {
                    if (EPayStatus.NORMAL == tempPayment.getStatus()) {
                        currentPayment = tempPayment;
                        break;
                    }
                }
                PaymentSchedule lastPayment = null;
                if (currentPayment == null) {
                    financePackageInvestorDto.setNextPayAmount(BigDecimal.ZERO);
                    financePackageInvestorDto.setNextPayDay("--");
                } else {
                    lastPayment = paymentScheduleList.get(paymentScheduleList.size() - 1);
                    // 下一支付日期
                    String nextDate = DateUtils.formatDate(currentPayment.getPaymentDate(), YYYY_MM_DD);
                    financePackageInvestorDto.setNextPayDay(nextDate);
                    // 本金加利息为下期应付
                    BigDecimal nextInterest = currentPayment.getInterestAmt().add(
                            currentPayment.getInterestForfeit());
                    BigDecimal nextPayAmt = BigDecimal.ZERO;
                    if (!deductFlag) {
                    	nextPayAmt = currentPayment.getPrincipalAmt();
                    	nextPayAmt = nextPayAmt.add(nextInterest);
                    	nextPayAmt = nextPayAmt.multiply(invsUnit);
                    	nextPayAmt = nextPayAmt.divide(pkgUnit, 2, RoundingMode.HALF_UP);
                    } else {
                    	BigDecimal dectRate = CommonBusinessUtil.getPaymentInterestDeductRate();
                    	BigDecimal intrRate = BigDecimal.ONE.subtract(dectRate);
                    	nextPayAmt =  currentPayment.getPrincipalAmt();
                    	nextPayAmt = nextPayAmt.add(nextInterest.multiply(intrRate));
                    	nextPayAmt = nextPayAmt.multiply(invsUnit);
                    	nextPayAmt = nextPayAmt.divide(pkgUnit, 2, RoundingMode.HALF_UP);
                    }
                    financePackageInvestorDto.setNextPayAmount(nextPayAmt);
                }
                // 累加剩余几期的本金和利息
                BigDecimal amount = BigDecimal.ZERO;
                BigDecimal principalAndInterestAmt = BigDecimal.ZERO;
                BigDecimal principalAndInterestFeitAmt = BigDecimal.ZERO;
                BigDecimal payPrincipalAndInterestAmt = BigDecimal.ZERO;
                BigDecimal payPrincipalAndInterestFeitAmt = BigDecimal.ZERO;
                BigDecimal wrtrPrincipalAndInterestFeitAmt = BigDecimal.ZERO;
                BigDecimal payWrtrPrincipalAndInterestFeitAmt = BigDecimal.ZERO;
                
                // 应付本金
                BigDecimal principalAmt = BigDecimal.ZERO;
                // 应付利息
                BigDecimal interestAmt = BigDecimal.ZERO;
                // 应付本金罚金
                BigDecimal principalFeitAmt = BigDecimal.ZERO;
                // 应付利息罚金
                BigDecimal interestFeitAmt = BigDecimal.ZERO;
                // 实付本金
                BigDecimal payPrincipalAmt = BigDecimal.ZERO;
                // 实付利息
                BigDecimal payInterestAmt = BigDecimal.ZERO;
                // 实付本金罚金
                BigDecimal payPrincipalFeitAmt = BigDecimal.ZERO;
                // 实付利息罚金
                BigDecimal payInterestFeitAmt = BigDecimal.ZERO;
                // 担保方应付本金罚金
                BigDecimal wrtrPrincipalFeitAmt = BigDecimal.ZERO;
                // 担保方应付利息罚金
                BigDecimal wrtrInterestFeitAmt = BigDecimal.ZERO;
                // 实付担保方应付本金罚金
                BigDecimal payWrtrPrincipalFeitAmt = BigDecimal.ZERO;
                // 实付担保方应付利息罚金
                BigDecimal payWrtrInterestFeitAmt = BigDecimal.ZERO;
                
                // 剩余期数
                Integer remTermLength = 0;
                
                for (PaymentSchedule paymentSchedule : paymentScheduleList) {
                	if (paymentSchedule.getStatus() == EPayStatus.NORMAL
    						|| paymentSchedule.getStatus() == EPayStatus.OVERDUE
    						|| paymentSchedule.getStatus() == EPayStatus.COMPENSATING
    						|| paymentSchedule.getStatus() == EPayStatus.COMPENSATORY) {
    					remTermLength++;
                	}
                	
                    // 应付本金+应付利息
                	principalAmt = principalAmt.add(paymentSchedule.getPrincipalAmt());
                    interestAmt = interestAmt.add(paymentSchedule.getInterestAmt());
                    principalAndInterestAmt = principalAndInterestAmt.add(paymentSchedule.getPrincipalAmt().add(
                            paymentSchedule.getInterestAmt()));
                    // 实付本金+实付利息
                    payPrincipalAmt = payPrincipalAmt.add(paymentSchedule.getPayPrincipalAmt());
                    payInterestAmt = payInterestAmt.add(paymentSchedule.getPayInterestAmt());
                    payPrincipalAndInterestAmt = payPrincipalAndInterestAmt.add(paymentSchedule
                            .getPayPrincipalAmt().add(paymentSchedule.getPayInterestAmt()));
                    Integer brkGraceDays = ProdPkgUtil.getBreachGraceDays(paymentSchedule.getProductId());
                    if (ProdPkgUtil.needPayFineAmt(paymentSchedule.getPaymentDate(), currentWorkDate,
                    		brkGraceDays.longValue())) {
                        // 应付本金罚金+应付利息罚金
                    	principalFeitAmt = principalFeitAmt.add(paymentSchedule.getPrincipalForfeit());
                    	interestFeitAmt = interestFeitAmt.add(paymentSchedule.getInterestForfeit());
                        principalAndInterestFeitAmt = principalAndInterestFeitAmt.add(paymentSchedule
                                .getPrincipalForfeit().add(paymentSchedule.getInterestForfeit()));
                        // 实付本金罚金+实付利息罚金
                        payPrincipalFeitAmt = payPrincipalFeitAmt.add(paymentSchedule.getPayPrincipalForfeit());
                    	payInterestFeitAmt = payInterestFeitAmt.add(paymentSchedule.getPayInterestForfeit());
                        payPrincipalAndInterestFeitAmt = payPrincipalAndInterestFeitAmt.add(paymentSchedule
                                .getPayPrincipalForfeit().add(paymentSchedule.getPayInterestForfeit()));
                    }

                    if (ProdPkgUtil.needPayFineAmt(paymentSchedule.getPaymentDate(), currentWorkDate,
                    		brkGraceDays.longValue())
                            && ProdPkgUtil.needPayOverdueCmpnsFineAmt(
                                    paymentSchedule.getPaymentDate(),
                                    currentWorkDate,
                                    Arrays.asList(new Long[] { product.getCmpnsGracePeriod(),
                                            product.getOverdue2CmpnsGracePeriod() }))) {
                        // 担保方应付本金罚金+担保方应付利息罚金
                    	wrtrPrincipalFeitAmt = wrtrPrincipalFeitAmt.add(paymentSchedule.getWrtrPrinForfeit());
                    	wrtrInterestFeitAmt = wrtrInterestFeitAmt.add(paymentSchedule.getWrtrInterestForfeit());
                        wrtrPrincipalAndInterestFeitAmt = wrtrPrincipalAndInterestFeitAmt.add(
                                paymentSchedule.getWrtrInterestForfeit()).add(paymentSchedule.getWrtrPrinForfeit());
                        // 实付担保方应付本金罚金+实付担保方应付利息罚金
                        payWrtrPrincipalFeitAmt = payWrtrPrincipalFeitAmt.add(paymentSchedule.getPayWrtrPrinForfeit());
                    	payWrtrInterestFeitAmt = payWrtrInterestFeitAmt.add(paymentSchedule.getPayWrtrInterestForfeit());
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
                financePackageInvestorDto.setLastAmount(amount);
				BigDecimal residualPrincipalAmt = principalAmt
						.add(principalFeitAmt).add(wrtrPrincipalFeitAmt)
						.subtract(payPrincipalAmt)
						.subtract(payPrincipalFeitAmt)
						.subtract(payWrtrPrincipalFeitAmt);
				residualPrincipalAmt = residualPrincipalAmt.multiply(invsUnit);
				residualPrincipalAmt = residualPrincipalAmt.divide(pkgUnit, 2, RoundingMode.HALF_UP);

				BigDecimal residualInterestAmt = interestAmt
						.add(interestFeitAmt).add(wrtrInterestFeitAmt)
						.subtract(payInterestAmt).subtract(payInterestFeitAmt)
						.subtract(payWrtrInterestFeitAmt);
				residualInterestAmt = residualInterestAmt.multiply(invsUnit);
				residualInterestAmt = residualInterestAmt.divide(pkgUnit, 2, RoundingMode.HALF_UP);
				
				financePackageInvestorDto.setResidualPrincipalAmt(residualPrincipalAmt);
				financePackageInvestorDto.setResidualInterestAmt(residualInterestAmt);
                if (lastPayment == null) {
                    financePackageInvestorDto.setLastPayDay("--");
                } else {
                    financePackageInvestorDto.setLastPayDay(DateUtils.formatDate(lastPayment.getPaymentDate(),
                            YYYY_MM_DD));
                }
                // 获得系统费率和还款费率
                financePackageInvestorDto.setSysRate(CommonBusinessUtil.getTransferFeeRate());
                financePackageInvestorDto.setPaymentRate(CommonBusinessUtil.getPaymentInterestDeductRate()
                        .multiply(new BigDecimal(100)).setScale(0));
                financePackageInvestorDto.setRemTermLength(remTermLength);
            }
        }
        // 转让表中有数据就是转让中，转让中才能撤单
        if (investorPackage.getLotId() != null) {
            CreditorRightsTransfer creditorRightsTransfer = financingPackageService
                    .getCreditorRightsTransfer(investorPackage.getLotId());
            if (creditorRightsTransfer != null
                    && securityContext
                            .hasPermission(Permissions.MY_ACCOUNT_MY_CREDITOR_CREDITOR_ASSIGNMENT_WITHDRAW)) {
                financePackageInvestorDto.setCanTransferCancel(true);
                financePackageInvestorDto.setStatus(EPackageStatus.TRANSFERING);
                financePackageInvestorDto.setTransferPrice(creditorRightsTransfer.getPrice());
            }
            // 还款中且满足系统转让规则才能转让
            if (EPackageStatus.PAYING == financePackageInvestorDto.getStatus()
                    && creditorTransferMaintainServie.canPackageTransfer(investorPackage.getLotId())
                    && securityContext.hasPermission(Permissions.MY_ACCOUNT_MY_CREDITOR_CREDITOR_ASSIGNMENT)) {
                financePackageInvestorDto.setCanCreditorRightsTransfer(true);
                calculateTransferMaxPrice(financePackageInvestorDto, investorPackage);
                calculateTransferMinPrice(financePackageInvestorDto, investorPackage);
            }
        }
        financePackageInvestorDto.setCostPrice(investorPackage.getLotBuyPrice().subtract(investorPackage.getAccumCrAmt()));
		financePackageInvestorDto.setFromTransfer(pkgTradeJnlRepository.findByBuyerUserIdAndLotIdAndTrdType(
								securityContext.getCurrentUserId(),
								investorPackage.getLotId(),
								EFundTrdType.BONDASSIGN) != null);
		return financePackageInvestorDto;
	}

    /**
     * 
     * Description: 我的债权详情
     * 
     * @param request
     * @param model
     * @param pkgId
     * @return
     */
    @RequestMapping(value = "/details/{pkgId}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.PARTIAL_CONTENT)
    @RequiresPermissions(value = { Permissions.MY_ACCOUNT_MY_CREDITOR_VIEW })
    public String getFinancierPackageDetails(HttpServletRequest request, Model model,
            @PathVariable(value = "pkgId") String pkgId, @RequestParam(required = false) String lotId) {
        ProductPackageInvestorDetailsDto dto = processFinancierPackageDetails(pkgId, lotId);
        model.addAttribute("dto", dto);
        if (securityContext.isInvestor()) {
            model.addAttribute("isInvestor", true);
        }
        return "packet/packet_manage_details";
    }

    public ProductPackageInvestorDetailsDto processFinancierPackageDetails(String pkgId, String lotId) {
    	ProductPackageInvestorDetailsDto dto = new ProductPackageInvestorDetailsDto();
    	// 获取包信息
        FinancingPackageView packagePo = financingPackageService.getPackageDetails(pkgId);
        ProductPackageDetailsDto pkgDto = ConverterService.convert(packagePo, ProductPackageDetailsDto.class);
        EPackageStatus status = packagePo.getStatus();
        // 判断是否为待放款审批及以后状态
        if ((status == EPackageStatus.WAIT_LOAD_APPROAL) || (status == EPackageStatus.WAIT_LOAD_APPROAL_CONFIRM)
                || (status == EPackageStatus.PAYING) || (status == EPackageStatus.END)) {
            dto.setStatusAfterWaitLoadApproal(true);
        }
        dto.setLotId(lotId);
        dto.setContractTemplateId(packagePo.getContractTemplateId());
        if (pkgDto != null) {
            
            pkgDto.setRate(FormatRate.formatRate(pkgDto.getRate().multiply(new BigDecimal(100))));
        	
            // 获取产品信息
            ProductDetailsDto productDto = financingMarketService.getProductDetail(pkgDto.getProductId());
            if (pkgDto.getWarrantyType() == EWarrantyType.MONITORASSETS
                    || pkgDto.getWarrantyType() == EWarrantyType.NOTHING) {
                pkgDto.setWrtrName("--");
                pkgDto.setWrtrNameShow("--");
            } else if (StringUtils.isNotBlank(productDto.getWarrantIdShow())) {
                Agency agency = memberService.getAgencyByUserId(productDto.getWarrantIdShow());
                dto.setGuaranteeLicenseNoImg(agency != null ? agency.getLicenceNoImg() : "");
            }
            if(StringUtils.isNotBlank(productDto.getWarrantIdShow())
            		&&!StringUtils.equals(productDto.getWarrantIdShow(), productDto.getWarrantId())){
            	UserPo user = userRepository.findUserByUserId(productDto.getWarrantIdShow());
                pkgDto.setWrtrNameShow(user.getName());
            }else{
            	pkgDto.setWrtrNameShow(pkgDto.getWrtrName());
            }

            pkgDto.setRate(FormatRate.formatRate(pkgDto.getRate()));
            dto.setPackageDetailsDto(pkgDto);
            
            if (productDto != null) {
                Product product = productService.getProductBasicInfo(pkgDto.getProductId());
                dto.setProductDetailsDto(productDto);
                if (product != null) {
                    productDto.setDfinancierIndustryType(ConverterService.convert(
                            productService.getDicByCode(EOptionCategory.ORG_INDUSTRY.getCode(),
                                    product.getFinancierIndustryType()), DynamicOption.class));
                    productDto.setProductGrageClass(product.getProductLevel().getText());
                    productDto.setFinancierGrageClass(product.getFinancierLevel());
                    productDto.setWarrantGrageClass(product.getWarrantorLevel());
                }
            }
            // 剩余融资额
            dto.setLeftAmount(pkgDto.getPackageQuota().subtract(pkgDto.getSupplyAmount()));
            pkgDto.setTradeStatus(convertStatus(pkgDto.getStatus()));
        }
        return dto;
    }
    
    /**
     * Description: 转换融资包状态为交易状态
     * 
     * @param status
     * @return
     */

    private ETradeType convertStatus(EPackageStatus status) {
        ETradeType result;
        switch (status) {
        case SUBSCRIBE:
            result = ETradeType.WAIT_SIGN;
            break;
        case WAIT_SIGN:
            result = ETradeType.WAIT_SIGN;
            break;
        case WAIT_LOAD_APPROAL_CONFIRM:
            result = ETradeType.PAYING;
            break;
        case WAIT_LOAD_APPROAL:
            result = ETradeType.PAYING;
            break;
        case PAYING:
            result = ETradeType.PAYING;
            break;
        case ABANDON:
            result = ETradeType.CANCELD;
            break;
        case END:
            result = ETradeType.END;
            break;
        default:
            result = ETradeType.NULL;
            break;
        }
        return result;
    }

    /**
     * 
     * Description: 债权转让撤单
     * 
     * @param request
     * @param lotId
     * @return
     */
    @RequestMapping(value = "/transfer/cancel/{lotId}", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = { Permissions.MY_ACCOUNT_MY_CREDITOR_CREDITOR_ASSIGNMENT_WITHDRAW })
    public ResultDto cancelTransfer(HttpServletRequest request, @PathVariable(value = "lotId") String lotId) {
        creditorTransferService.cancelCreditorTransfer(lotId);
        return ResultDtoFactory.toAck(MessageUtil.getMessage("mycreditor.transfer.cancel.success"), null);
    }

    /**
     * 
     * Description: 债权转让报价
     * 
     * @param transferPriceDto
     * @param lotId
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "transfer/save/{lotId}", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = { Permissions.MY_ACCOUNT_MY_CREDITOR_CREDITOR_ASSIGNMENT })
    public ResultDto saveTransfer(@OnValid @RequestBody TransferPriceDto transferPriceDto, String lotId,
            HttpServletRequest request, HttpSession session, Model model) {
    	if (!CommonBusinessUtil.isMarketOpen()) {
    		return ResultDtoFactory.toNack(MessageUtil.getMessage("market.error.market.close.transfer"), null);
    	}
        this.creditorTransferService.saveCreditorTransfer(transferPriceDto);
        return ResultDtoFactory.toAck(MessageUtil.getMessage("mycreditor.transfer.price.success"), null);
    }

    public String formateProductScore(BigDecimal sc) {
        String score = String.valueOf(sc);
        if (EProductScoreType.AZERO.getCode().equals(score)) {
            return EProductScoreType.AZERO.getText();
        }
        if (EProductScoreType.AMIN.getCode().equals(score)) {
            return EProductScoreType.AMIN.getText();
        }
        if (EProductScoreType.BZERO.getCode().equals(score)) {
            return EProductScoreType.BZERO.getText();
        }
        if (EProductScoreType.BMIN.getCode().equals(score)) {
            return EProductScoreType.BMIN.getText();
        }
        if (EProductScoreType.CZERO.getCode().equals(score)) {
            return EProductScoreType.CZERO.getText();
        }
        if (EProductScoreType.CMIN.getCode().equals(score)) {
            return EProductScoreType.CMIN.getText();
        }
        if (EProductScoreType.DZERO.getCode().equals(score)) {
            return EProductScoreType.DZERO.getText();
        }
        return StringUtils.EMPTY;
    }

    public String formateFinancierScore(BigDecimal sc) {
        String score = String.valueOf(sc);
        if (EFinancierScoreType.AZERO.getCode().equals(score)) {
            return EFinancierScoreType.AZERO.getText();
        }
        if (EFinancierScoreType.AMIN.getCode().equals(score)) {
            return EFinancierScoreType.AMIN.getText();
        }
        if (EFinancierScoreType.BZERO.getCode().equals(score)) {
            return EFinancierScoreType.BZERO.getText();
        }
        if (EFinancierScoreType.BMIN.getCode().equals(score)) {
            return EFinancierScoreType.BMIN.getText();
        }
        if (EFinancierScoreType.CZERO.getCode().equals(score)) {
            return EFinancierScoreType.CZERO.getText();
        }
        if (EFinancierScoreType.CMIN.getCode().equals(score)) {
            return EFinancierScoreType.CMIN.getText();
        }
        if (EFinancierScoreType.DZERO.getCode().equals(score)) {
            return EFinancierScoreType.DZERO.getText();
        }
        return StringUtils.EMPTY;
    }

    
    /* get和set方法给/app控制器注入使用 */
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

	public SubscribeGroupService getSubscribeGroupService() {
		return subscribeGroupService;
	}

	public void setSubscribeGroupService(SubscribeGroupService subscribeGroupService) {
		this.subscribeGroupService = subscribeGroupService;
	}

	public FeeGroupService getFeeGroupService() {
		return feeGroupService;
	}

	public void setFeeGroupService(FeeGroupService feeGroupService) {
		this.feeGroupService = feeGroupService;
	}

	public FinancingMarketService getFinancingMarketService() {
		return financingMarketService;
	}

	public void setFinancingMarketService(
			FinancingMarketService financingMarketService) {
		this.financingMarketService = financingMarketService;
	}

	public CreditorTransferService getCreditorTransferService() {
		return creditorTransferService;
	}

	public void setCreditorTransferService(
			CreditorTransferService creditorTransferService) {
		this.creditorTransferService = creditorTransferService;
	}

	public ProductService getProductService() {
		return productService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	public PositionLotService getPositionLotService() {
		return positionLotService;
	}

	public void setPositionLotService(PositionLotService positionLotService) {
		this.positionLotService = positionLotService;
	}

	public CreditorTransferMaintainServie getCreditorTransferMaintainServie() {
		return creditorTransferMaintainServie;
	}

	public void setCreditorTransferMaintainServie(
			CreditorTransferMaintainServie creditorTransferMaintainServie) {
		this.creditorTransferMaintainServie = creditorTransferMaintainServie;
	}

	public MemberService getMemberService() {
		return memberService;
	}

	public void setMemberService(MemberService memberService) {
		this.memberService = memberService;
	}

	public PkgTradeJnlRepository getPkgTradeJnlRepository() {
		return pkgTradeJnlRepository;
	}

	public void setPkgTradeJnlRepository(PkgTradeJnlRepository pkgTradeJnlRepository) {
		this.pkgTradeJnlRepository = pkgTradeJnlRepository;
	}

	public UserRepository getUserRepository() {
		return userRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public PaymentScheduleService getPaymentScheduleService() {
		return paymentScheduleService;
	}

	public void setPaymentScheduleService(
			PaymentScheduleService paymentScheduleService) {
		this.paymentScheduleService = paymentScheduleService;
	}
    
}
