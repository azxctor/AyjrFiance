/*
 * Project Name: kmfex-platform
 * File Name: FinancePackageDefaultController.java
 * Class Name: FinancePackageDefaultController
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hengxin.platform.common.constant.UrlConstant;
import com.hengxin.platform.common.controller.BaseController;
import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.domain.EnumOption;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.fund.service.FinancierBreachFundClearingService;
import com.hengxin.platform.fund.service.FreezeReserveDtlService;
import com.hengxin.platform.fund.service.FundAcctBalService;
import com.hengxin.platform.product.ProdPkgUtil;
import com.hengxin.platform.product.converter.PackageDefaultConverter;
import com.hengxin.platform.product.converter.PackageDefaultViewConverter;
import com.hengxin.platform.product.domain.FinancingPackageView;
import com.hengxin.platform.product.domain.PackagePaymentView;
import com.hengxin.platform.product.domain.PaymentSchedule;
import com.hengxin.platform.product.domain.ProductPackage;
import com.hengxin.platform.product.dto.FinancePackageDefaultDto;
import com.hengxin.platform.product.dto.ScheduleSearchDto;
import com.hengxin.platform.product.enums.ENumberOperator;
import com.hengxin.platform.product.enums.EPayStatus;
import com.hengxin.platform.product.enums.EWarrantyType;
import com.hengxin.platform.product.service.ClearingPkgService;
import com.hengxin.platform.product.service.ClearingPkgWithRulesService;
import com.hengxin.platform.product.service.FinancingPackageDefaultService;
import com.hengxin.platform.product.service.FinancingPackageService;
import com.hengxin.platform.product.service.PkgWarrantCmpnsMgtService;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;

/**
 * Class Name: FinancePackageDefaultController
 * 
 * @author shengzhou
 * 
 */
@Controller
public class FinancePackageDefaultController extends BaseController {

    @Autowired
    private SecurityContext securityContext;

    @Autowired
    private FinancingPackageDefaultService financingPackageDefaultService;

    @Autowired
    private FinancierBreachFundClearingService financierBreachFundClearingService;

    @Autowired
    private FundAcctBalService fundAcctBalService;

    @Autowired
    private ClearingPkgService clearingPkgService;
    
    @Autowired
    private ClearingPkgWithRulesService clearingPkgWithRulesService;

    @Autowired
    private FinancingPackageService financingPackageService;

    @Autowired
    private PkgWarrantCmpnsMgtService pkgWarrantCmpnsMgtService;

    @Autowired
    private FreezeReserveDtlService freezeReserveDtlService;

    private static final String CLEARE_SUCCESS = "清分成功";

    private static final String UNFREEZE_SUCCESS = "解冻成功";

    private static final String UNFREEZETRANSFER_SUCCESS = "解冻转账成功";

    /**
     * 
     * Description: 违约处理列表页面加载
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @Deprecated
    //@RequestMapping(value = UrlConstant.SETTLE_MANAGEMENT_DEFAULT_HANDLING_URL)
    @RequiresPermissions(value = { Permissions.SETTLE_MANAGEMENT_DEFAULT_HANDLING })
    public String renderDefaultScheduleList(HttpServletRequest request, HttpSession session, Model model) {
        model.addAttribute("cleared", false);
        List<EnumOption> staticOptions = getStaticOptions(ENumberOperator.class, true);
        model.addAttribute("operationList", staticOptions);
        return null;//"product/financerepayment_list";
    }

    /**
     * 
     * Description: 解冻或解冻划转页面加载
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.RISK_MANAGEMENT_COMPENSATORY_HANDLING_URL)
    @RequiresPermissions(value = { Permissions.RISK_MANAGEMENT_COMPENSATORY_HANDLING })
    public String renderClearedScheduleList(HttpServletRequest request, HttpSession session, Model model) {
        model.addAttribute("cleared", true);
        return "product/financerepayment_list";
    }

    /**
     * 
     * Description: 查询违约处理列表数据
     * 
     * @param request
     * @param searchDto
     * @return
     */
    @Transactional(readOnly = true)
    @RequestMapping(value = "product/search/getdefaultlist")
    @ResponseBody
    @RequiresPermissions(value = { Permissions.SETTLE_MANAGEMENT_DEFAULT_HANDLING })
    public DataTablesResponseDto<FinancePackageDefaultDto> getDefaultScheduleList(HttpServletRequest request,
            @RequestBody ScheduleSearchDto searchDto) {
        DataTablesResponseDto<FinancePackageDefaultDto> result = new DataTablesResponseDto<FinancePackageDefaultDto>();
        result.setEcho(searchDto.getEcho());
        if (searchDto.getTotalAmt() != null && searchDto.getAmtOperation() != ENumberOperator.NULL) {
            return getAllDefaultList(request, searchDto);
        }
        Page<PaymentSchedule> scheduleDefualtList = financingPackageDefaultService.getFinancingPackageListByUserId(
                securityContext.getCurrentUserId(), searchDto, false);
        List<FinancePackageDefaultDto> packageDefaultDtoList = new ArrayList<FinancePackageDefaultDto>();
        Date workDate = CommonBusinessUtil.getCurrentWorkDate();
        for (PaymentSchedule paymentSchedule : scheduleDefualtList) {
            FinancePackageDefaultDto financePackageDefaultDto = ConverterService.convert(paymentSchedule,
                    FinancePackageDefaultDto.class, PackageDefaultConverter.class);
            FinancingPackageView packageDetails = paymentSchedule.getFinancingPackageView();
            financePackageDefaultDto.setCleared(false);
            // 应付欠款
            BigDecimal debt = paymentSchedule.getPrincipalAmt().add(paymentSchedule.getInterestAmt())
                    .add(paymentSchedule.getFeeAmt());
            // 免交逾期违约罚金宽限期
            Integer brkGraceDays = ProdPkgUtil.getBreachGraceDays(paymentSchedule.getProductId());
            boolean needPayFine = ProdPkgUtil.needPayFineAmt(paymentSchedule.getPaymentDate(), workDate,
                    Long.valueOf(brkGraceDays.intValue()));
            if (needPayFine) {
                debt = debt.add(paymentSchedule.getPrincipalForfeit()).add(paymentSchedule.getInterestForfeit())
                        .add(paymentSchedule.getFeeForfeit());
            }

            debt = debt.subtract(paymentSchedule.getPayPrincipalAmt()).subtract(paymentSchedule.getPayInterestAmt())
                    .subtract(paymentSchedule.getPayAmt()).subtract(paymentSchedule.getPayPrincipalForfeit())
                    .subtract(paymentSchedule.getPayInterestForfeit()).subtract(paymentSchedule.getPayFeeForfeit());
            boolean needPayCmpnsFine = ProdPkgUtil
                    .needPayOverdueCmpnsFineAmt(paymentSchedule.getPaymentDate(), workDate,
                            Arrays.asList(packageDetails.getOverdue2CmpnsDays(), packageDetails.getCmpnsGracePriod()));

            if (needPayCmpnsFine) {
                debt = debt
                        .add(paymentSchedule.getWrtrInterestForfeit() == null ? BigDecimal.ZERO : paymentSchedule
                                .getWrtrInterestForfeit())
                        .add(paymentSchedule.getWrtrPrinForfeit() == null ? BigDecimal.ZERO : paymentSchedule
                                .getWrtrPrinForfeit())
                        .subtract(
                                paymentSchedule.getPayWrtrInterestForfeit() == null ? BigDecimal.ZERO : paymentSchedule
                                        .getPayWrtrInterestForfeit())
                        .subtract(
                                paymentSchedule.getPayWrtrPrinForfeit() == null ? BigDecimal.ZERO : paymentSchedule
                                        .getPayWrtrPrinForfeit());
            }

            financePackageDefaultDto.setDebt(debt);
            // 融资人，担保机构可付欠款
            BigDecimal financerAmount = financierBreachFundClearingService.getUserCurrentAvlAmtIgnoreFrzAmt(
                    paymentSchedule.getFinancerId(), true);
            BigDecimal warrantAmount = BigDecimal.ZERO;
            // 担保类型：本金担保；本息担保
            if (packageDetails.getWarrantyType() == EWarrantyType.PRINCIPAL
                    || packageDetails.getWarrantyType() == EWarrantyType.PRINCIPALINTEREST) {
                warrantAmount = fundAcctBalService.getUserCurrentAcctAvlBal(paymentSchedule.getWarrantId());
            }
            if ("N".equals(paymentSchedule.getLastFlag())) {
                financePackageDefaultDto.setFinancerAmount(financerAmount);
                if (paymentSchedule.getStatus() == EPayStatus.COMPENSATING) {
                    financePackageDefaultDto.setWarrantAmount(warrantAmount);
                    financePackageDefaultDto.setTotalAmount(financerAmount.add(warrantAmount));
                } else {
                    financePackageDefaultDto.setWarrantAmount(null);
                    financePackageDefaultDto.setTotalAmount(financerAmount);
                }
            } else {
                // 如果是最后一期要加上保证金
                BigDecimal loadFnrAmt = freezeReserveDtlService.getUnFnrAbleAmt(packageDetails.getLoanFnrJnl());
                BigDecimal wrtrFnrAmt = freezeReserveDtlService.getUnFnrAbleAmt(packageDetails.getWrtrFnrJNL());
                BigDecimal totalFinancierAmt = financerAmount.add(loadFnrAmt);
                financePackageDefaultDto.setFinancerAmount(totalFinancierAmt);
                if (paymentSchedule.getStatus() == EPayStatus.COMPENSATING) {
                    BigDecimal totalWrtrAmt = warrantAmount.add(wrtrFnrAmt);
                    financePackageDefaultDto.setWarrantAmount(totalWrtrAmt);
                    financePackageDefaultDto.setTotalAmount(totalFinancierAmt.add(totalWrtrAmt));
                } else {
                    financePackageDefaultDto.setWarrantAmount(null);
                    financePackageDefaultDto.setTotalAmount(totalFinancierAmt);
                }
            }
            packageDefaultDtoList.add(financePackageDefaultDto);
        }

        long size = scheduleDefualtList.getTotalElements();
        result.setTotalDisplayRecords(size);
        result.setTotalRecords(size);
        result.setData(packageDefaultDtoList);
        return result;
    }

    /**
     * 
     * Description: 查询违约处理列表数据
     * 
     * @param request
     * @param searchDto
     * @return
     */
    public DataTablesResponseDto<FinancePackageDefaultDto> getAllDefaultList(HttpServletRequest request,
            ScheduleSearchDto searchDto) {
        DataTablesResponseDto<FinancePackageDefaultDto> result = new DataTablesResponseDto<FinancePackageDefaultDto>();
        result.setEcho(searchDto.getEcho());
        List<PaymentSchedule> scheduleDefualtList = financingPackageDefaultService.getOverdueFinancingPackageList(
                securityContext.getCurrentUserId(), searchDto);
        List<FinancePackageDefaultDto> packageDefaultDtoList = new ArrayList<FinancePackageDefaultDto>();
        Date workDate = CommonBusinessUtil.getCurrentWorkDate();
        for (PaymentSchedule paymentSchedule : scheduleDefualtList) {
            FinancePackageDefaultDto financePackageDefaultDto = ConverterService.convert(paymentSchedule,
                    FinancePackageDefaultDto.class, PackageDefaultViewConverter.class);
            PackagePaymentView packageDetails = paymentSchedule.getPackagePaymentView();
            financePackageDefaultDto.setCleared(false);
            // 应付欠款
            BigDecimal debt = paymentSchedule.getPrincipalAmt().add(paymentSchedule.getInterestAmt())
                    .add(paymentSchedule.getFeeAmt());
            // 免交逾期违约罚金宽限期
            Integer brkGraceDays = ProdPkgUtil.getBreachGraceDays(paymentSchedule.getProductId());
            boolean needPayFine = ProdPkgUtil.needPayFineAmt(paymentSchedule.getPaymentDate(), workDate,
                    Long.valueOf(brkGraceDays.intValue()));
            if (needPayFine) {
                debt = debt.add(paymentSchedule.getPrincipalForfeit()).add(paymentSchedule.getInterestForfeit())
                        .add(paymentSchedule.getFeeForfeit());
            }

            debt = debt.subtract(paymentSchedule.getPayPrincipalAmt()).subtract(paymentSchedule.getPayInterestAmt())
                    .subtract(paymentSchedule.getPayAmt()).subtract(paymentSchedule.getPayPrincipalForfeit())
                    .subtract(paymentSchedule.getPayInterestForfeit()).subtract(paymentSchedule.getPayFeeForfeit());
            boolean needPayCmpnsFine = ProdPkgUtil
                    .needPayOverdueCmpnsFineAmt(paymentSchedule.getPaymentDate(), workDate,
                            Arrays.asList(packageDetails.getOverdue2CmpnsDays(), packageDetails.getCmpnsGracePriod()));

            if (needPayCmpnsFine) {
                debt = debt
                        .add(paymentSchedule.getWrtrInterestForfeit() == null ? BigDecimal.ZERO : paymentSchedule
                                .getWrtrInterestForfeit())
                        .add(paymentSchedule.getWrtrPrinForfeit() == null ? BigDecimal.ZERO : paymentSchedule
                                .getWrtrPrinForfeit())
                        .subtract(
                                paymentSchedule.getPayWrtrInterestForfeit() == null ? BigDecimal.ZERO : paymentSchedule
                                        .getPayWrtrInterestForfeit())
                        .subtract(
                                paymentSchedule.getPayWrtrPrinForfeit() == null ? BigDecimal.ZERO : paymentSchedule
                                        .getPayWrtrPrinForfeit());
            }

            financePackageDefaultDto.setDebt(debt);
            // 融资人，担保机构可付欠款
            BigDecimal financerAmount = packageDetails.getFncrAvlAmt();
            // financierBreachFundClearingService.getUserCurrentAvlAmtIgnoreFrzAmt(paymentSchedule.getFinancerId(),
            // true);

            BigDecimal warrantAmount = BigDecimal.ZERO;
            // 担保类型：本金担保；本息担保
            if (packageDetails.getWarrantyType() == EWarrantyType.PRINCIPAL
                    || packageDetails.getWarrantyType() == EWarrantyType.PRINCIPALINTEREST) {
                warrantAmount = packageDetails.getWrtrAvlAmt();
                // fundAcctBalService.getUserCurrentAcctAvlBal(paymentSchedule.getWarrantId());
            }
            if ("N".equals(paymentSchedule.getLastFlag())) {
                financePackageDefaultDto.setFinancerAmount(financerAmount);
                if (paymentSchedule.getStatus() == EPayStatus.COMPENSATING) {
                    financePackageDefaultDto.setWarrantAmount(warrantAmount);
                    financePackageDefaultDto.setTotalAmount(financerAmount.add(warrantAmount));
                } else {
                    financePackageDefaultDto.setWarrantAmount(null);
                    financePackageDefaultDto.setTotalAmount(financerAmount);
                }
            } else {
                // 如果是最后一期要加上保证金
                BigDecimal loadFnrAmt = packageDetails.getLoanAmt();
                // freezeReserveDtlService.getUnFnrAbleAmt(packageDetails.getLoanFnrJnl());
                BigDecimal wrtrFnrAmt = packageDetails.getWrtrAmt();
                // freezeReserveDtlService.getUnFnrAbleAmt(packageDetails.getWrtrFnrJNL());
                BigDecimal totalFinancierAmt = financerAmount.add(loadFnrAmt);
                financePackageDefaultDto.setFinancerAmount(totalFinancierAmt);
                if (paymentSchedule.getStatus() == EPayStatus.COMPENSATING) {
                    BigDecimal totalWrtrAmt = warrantAmount.add(wrtrFnrAmt);
                    financePackageDefaultDto.setWarrantAmount(totalWrtrAmt);
                    financePackageDefaultDto.setTotalAmount(totalFinancierAmt.add(totalWrtrAmt));
                } else {
                    financePackageDefaultDto.setWarrantAmount(null);
                    financePackageDefaultDto.setTotalAmount(totalFinancierAmt);
                }
            }
            packageDefaultDtoList.add(financePackageDefaultDto);
        }
        BigDecimal totalAmt = searchDto.getTotalAmt();
        ENumberOperator operator = searchDto.getAmtOperation();
        List<FinancePackageDefaultDto> datalist = new ArrayList<FinancePackageDefaultDto>();
        for (FinancePackageDefaultDto item : packageDefaultDtoList) {
            if (operator == ENumberOperator.EQ && item.getTotalAmount().equals(totalAmt)) {
                datalist.add(item);
            } else if (operator == ENumberOperator.GREAT_EQ && item.getTotalAmount().compareTo(totalAmt) >= 0) {
                datalist.add(item);
            } else if (operator == ENumberOperator.GREAT_THAN && item.getTotalAmount().compareTo(totalAmt) > 0) {
                datalist.add(item);
            } else if (operator == ENumberOperator.LESS_EQ && item.getTotalAmount().compareTo(totalAmt) <= 0) {
                datalist.add(item);
            } else if (operator == ENumberOperator.LESS_THAN && item.getTotalAmount().compareTo(totalAmt) < 0) {
                datalist.add(item);
            }

        }
        long size = datalist.size();
        result.setTotalDisplayRecords(size);
        result.setTotalRecords(size);
        int displayStart = searchDto.getDisplayStart();
        int displayLength = searchDto.getDisplayLength();
        result.setData(datalist.subList(displayStart, (displayStart + displayLength) > (int) size ? (int) size
                : (displayStart + displayLength)));
        return result;
    }

    /**
     * 
     * Description: 查询解冻、解冻划转数据
     * 
     * @param request
     * @param searchDto
     * @return
     */
    @Transactional(readOnly = true)
    @RequestMapping(value = "product/search/getclearedlist")
    @ResponseBody
    @RequiresPermissions(value = { Permissions.RISK_MANAGEMENT_COMPENSATORY_HANDLING })
    public DataTablesResponseDto<FinancePackageDefaultDto> getClearedScheduleList(HttpServletRequest request,
            @RequestBody ScheduleSearchDto searchDto) {
        DataTablesResponseDto<FinancePackageDefaultDto> result = new DataTablesResponseDto<FinancePackageDefaultDto>();
        result.setEcho(searchDto.getEcho());
        Page<PaymentSchedule> scheduleDefualtList = financingPackageDefaultService.getFinancingPackageListByUserId(
                securityContext.getCurrentUserId(), searchDto, true);
        List<FinancePackageDefaultDto> packageDefaultDtoList = new ArrayList<FinancePackageDefaultDto>();
        for (PaymentSchedule paymentSchedule : scheduleDefualtList) {
            FinancePackageDefaultDto financePackageDefaultDto = ConverterService.convert(paymentSchedule,
                    FinancePackageDefaultDto.class, PackageDefaultConverter.class);
            financePackageDefaultDto.setCleared(true);
            // 应付欠款
            BigDecimal debt = paymentSchedule.getCmpnsPyAmt().add(paymentSchedule.getCmpnsFinePyAmt());
            financePackageDefaultDto.setDebt(debt);
            // 融资人，担保机构可付欠款
            BigDecimal financerAmount = fundAcctBalService.getUserCurrentAcctAvlBal(paymentSchedule.getFinancerId(),
                    true);
            BigDecimal warrantAmount = fundAcctBalService.getUserCurrentAcctAvlBal(paymentSchedule.getWarrantId());
            if ("N".equals(paymentSchedule.getLastFlag())) {
                financePackageDefaultDto.setFinancerAmount(financerAmount);
                financePackageDefaultDto.setWarrantAmount(warrantAmount);
                financePackageDefaultDto.setTotalAmount(financerAmount.add(warrantAmount));
            } else {
                // 如果是最后一期要加上保证金
                ProductPackage productPackage = financingPackageService.getProductPackageById(paymentSchedule
                        .getPackageId());
                BigDecimal loadFnrAmt = freezeReserveDtlService.getUnFnrAbleAmt(productPackage.getLoanFnrJnlNo());
                BigDecimal wrtrFnrAmt = freezeReserveDtlService.getUnFnrAbleAmt(productPackage.getWrtrFnrJnlNo());
                BigDecimal totalFinancierAmt = financerAmount.add(loadFnrAmt);
                BigDecimal totalWrtrAmt = warrantAmount.add(wrtrFnrAmt);
                financePackageDefaultDto.setFinancerAmount(totalFinancierAmt);
                financePackageDefaultDto.setWarrantAmount(totalWrtrAmt);
                financePackageDefaultDto.setTotalAmount(totalFinancierAmt.add(totalWrtrAmt));
            }
            packageDefaultDtoList.add(financePackageDefaultDto);
        }

        long size = scheduleDefualtList.getTotalElements();
        result.setTotalDisplayRecords(size);
        result.setTotalRecords(size);
        result.setData(packageDefaultDtoList);
        return result;
    }

    /**
     * 清分操作
     * @param request
     * @param packageId
     * @param period
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/payment/clear/{packageId}/{period}")
    @RequiresPermissions(value = { Permissions.SETTLE_MANAGEMENT_DEFAULT_HANDLING })
    @Deprecated
    public ResultDto paymentScheduleClear(HttpServletRequest request, @PathVariable String packageId,
            @PathVariable Integer period) {
        String operatorId = securityContext.getCurrentUserId();
        PaymentSchedule schedule = financingPackageDefaultService.getPaymentScheduleByPackageIdAndPeriod(packageId,
                period);
        if (schedule != null) {
            if (EPayStatus.OVERDUE == schedule.getStatus()) {
                clearingPkgService.clearingProductPackageInOverdue(packageId, period, operatorId);
            } else if (EPayStatus.COMPENSATING == schedule.getStatus()) {
                clearingPkgService.clearingProductPackageInCompensating(packageId, period, operatorId);
            } else if (EPayStatus.COMPENSATORY == schedule.getStatus()) {
                clearingPkgService.clearingProductPackageInCompensatory(packageId, period, operatorId);
            }
        }
        return ResultDtoFactory.toAck(CLEARE_SUCCESS);
    }

    /**
     * 
     * Description: 解冻
     * 
     * @param request
     * @param packageId
     * @param period
     * @param userId
     * @return
     */
    @RequestMapping(value = "/payment/unfreeze/{packageId}/{period}/{userId}")
    @ResponseBody
    @RequiresPermissions(value = { Permissions.RISK_MANAGEMENT_COMPENSATORY_HANDLING })
    public ResultDto paymentScheduleUnfreeze(HttpServletRequest request, @PathVariable String packageId,
            @PathVariable Integer period, @PathVariable String userId) {
        String operatorId = securityContext.getCurrentUserId();
        pkgWarrantCmpnsMgtService.pkgWarrantCmpnsAmtRepay(userId, packageId, period, operatorId,
                CommonBusinessUtil.getCurrentWorkDate(), false);
        return ResultDtoFactory.toAck(UNFREEZE_SUCCESS);
    }

    /**
     * 
     * Description: 解冻划转
     * 
     * @param request
     * @param packageId
     * @param period
     * @param userId
     * @return
     */
    @RequestMapping(value = "/payment/unfreezeTransfer/{packageId}/{period}/{userId}")
    @ResponseBody
    @RequiresPermissions(value = { Permissions.RISK_MANAGEMENT_COMPENSATORY_HANDLING })
    public ResultDto paymentScheduleUnfreezeTransfer(HttpServletRequest request, @PathVariable String packageId,
            @PathVariable Integer period, @PathVariable String userId) {
        String operatorId = securityContext.getCurrentUserId();
        pkgWarrantCmpnsMgtService.pkgWarrantCmpnsAmtRepay(userId, packageId, period, operatorId,
                CommonBusinessUtil.getCurrentWorkDate(), true);
        return ResultDtoFactory.toAck(UNFREEZETRANSFER_SUCCESS);
    }
}
