/*
 * Project Name: kmfex-platform
 * File Name: FinancingPackagePaymentController.java
 * Class Name: FinancingPackagePaymentController
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
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hengxin.platform.product.domain.FinancingPackageView;
import com.hengxin.platform.product.domain.PackageContract;
import com.hengxin.platform.product.domain.PaymentSchedule;
import com.hengxin.platform.product.dto.ManualPaymentDto;
import com.hengxin.platform.product.dto.PrePaymentDto;
import com.hengxin.platform.product.service.FinancingPackageService;
import com.hengxin.platform.product.service.PaymentScheduleService;
import com.hengxin.platform.product.service.ProductContractTemplateService;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;

/**
 * Class Name: FinancingPackagePaymentController
 * 
 * @author yingchangwang
 * 
 */
@Controller
@RequestMapping(value = "financingpackage/payment")
public class FinancingPackagePaymentController {

//    private static final BigDecimal UNIT_MONTH_EACH_YEAR = new BigDecimal(12);

    @Autowired
    private PaymentScheduleService paymentScheduleService;

    @Autowired
    private FinancingPackageService financingPackageService;

    @Autowired
    private ProductContractTemplateService productContractTemplateService;

    @Autowired
    private SecurityContext securityContext;

    /**
     * 
     * Description: 每期手工还款数据查询
     * 
     * @param packageId
     * @param request
     * @param model
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "{packageId}/getmanualdetails")
    @ResponseBody
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_TRANSACTION_MANUAL_PAYMENTS })
    public ManualPaymentDto getManulPaymentDetails(@PathVariable(value = "packageId") String packageId,
            HttpServletRequest request, Model model) throws ParseException {
        PaymentSchedule currentPeriodPayment = paymentScheduleService.getCurrentPeriodPayment(packageId);
        ManualPaymentDto dto = new ManualPaymentDto();
        if (currentPeriodPayment != null) {
            dto.setPackageId(currentPeriodPayment.getPackageId());
            dto.setPeriod(currentPeriodPayment.getSequenceId() + "");
            dto.setTotalPayment(currentPeriodPayment.getPrincipalAmt().add(currentPeriodPayment.getInterestAmt())
                    .add(currentPeriodPayment.getFeeAmt()));
            dto.setPrincipalBalance(currentPeriodPayment.getPrincipalAmt().setScale(2, RoundingMode.HALF_UP));
            dto.setInterest(currentPeriodPayment.getInterestAmt().setScale(2, RoundingMode.HALF_UP));
            dto.setFeeAmt(currentPeriodPayment.getFeeAmt());
        }
        return dto;
    }

    /**
     * 
     * Description: 平台提前还款数据查询
     * 
     * @param packageId
     * @param request
     * @return
     */
    @RequestMapping(value = "{packageId}/getprepaymentdetails")
    @ResponseBody
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_PACKAGE_PREPAYMENT_NO_PENALTY })
    public PrePaymentDto getPrePaymentDetails(@PathVariable(value = "packageId") String packageId,
            HttpServletRequest request) {
        PrePaymentDto dto = new PrePaymentDto();
        List<PaymentSchedule> allNormalPaymentSchedules = paymentScheduleService
                .getAllNormalPaymentSchedules(packageId);
        if (allNormalPaymentSchedules != null && !allNormalPaymentSchedules.isEmpty()) {
            BigDecimal totalAmt = BigDecimal.ZERO;
            BigDecimal totalPrincipal = BigDecimal.ZERO;
            BigDecimal totalfeit = BigDecimal.ZERO;
            BigDecimal oneMonthInterest = BigDecimal.ZERO;
            BigDecimal feeAmt = BigDecimal.ZERO;
            int idx = 0;
            for (PaymentSchedule paymentSchedule : allNormalPaymentSchedules) {
                totalPrincipal = totalPrincipal.add(paymentSchedule.getPrincipalAmt());
                if(idx==0){
                	feeAmt = feeAmt.add(paymentSchedule.getFeeAmt());
                }
                idx++;
            }

            dto.setPackageId(packageId);
            dto.setTotalPrincipaBalance(totalPrincipal);
            dto.setFeeAmt(feeAmt);

            FinancingPackageView packageDetails = financingPackageService.getPackageDetails(packageId);
            BigDecimal supplyAmount = packageDetails.getSupplyAmount();
            // BigDecimal rate = packageDetails.getRate();
            // String contractTemplateId = packageDetails.getContractTemplateId();
            PackageContract packageContractDetails = financingPackageService.getPackageContractDetails(packageId);
            BigDecimal financierPrepaymentPenaltyRate = packageContractDetails.getFncrPrepayPenaltyRt();
            BigDecimal platformPrepaymentPenaltyRate = packageContractDetails.getPlatformPrepayPenaltyRt();
            String prepayDeductIntrFlg = packageContractDetails.getPrePayDeductIntrFlag();
            if ("Y".equalsIgnoreCase(prepayDeductIntrFlg)) {
                // 一个月利息
                oneMonthInterest = allNormalPaymentSchedules.get(0).getInterestAmt();
                // supplyAmount.multiply(rate).divide(UNIT_MONTH_EACH_YEAR, 20, RoundingMode.HALF_UP)
                // .setScale(2, RoundingMode.HALF_UP);
                dto.setTotalInterestAmt(oneMonthInterest);
            }
            if(securityContext.isSettlementDeptUser()){
                totalfeit = platformPrepaymentPenaltyRate.multiply(supplyAmount).setScale(2,
                        RoundingMode.DOWN);
            }else if(securityContext.isFinancier()){
                totalfeit = financierPrepaymentPenaltyRate.multiply(supplyAmount).setScale(2, RoundingMode.DOWN);
            }
            totalAmt = totalPrincipal.add(oneMonthInterest).add(totalfeit).add(feeAmt);
            dto.setTotalAmt(totalAmt);
            dto.setTotalPenalty(totalfeit);
        }
        return dto;
    }

    /**
     * 
     * Description: 融资人提前还款数据查询
     * 
     * @param packageId
     * @param request
     * @return
     */
    @RequestMapping(value = "{packageId}/getprepayments")
    @ResponseBody
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_PACKAGE_PREPAYMENT })
    public PrePaymentDto getPrePaymentDetailsForFinincier(@PathVariable(value = "packageId") String packageId,
            HttpServletRequest request) {
        PrePaymentDto dto = new PrePaymentDto();
        List<PaymentSchedule> allNormalPaymentSchedules = paymentScheduleService
                .getAllNormalPaymentSchedules(packageId);
        if (allNormalPaymentSchedules != null && !allNormalPaymentSchedules.isEmpty()) {
            BigDecimal totalAmt = BigDecimal.ZERO;
            BigDecimal totalPrincipal = BigDecimal.ZERO;
            BigDecimal totalPenalty = BigDecimal.ZERO;
            BigDecimal oneMonthInterest = BigDecimal.ZERO;
            BigDecimal indivFeit = BigDecimal.ZERO;
            BigDecimal feeAmt = BigDecimal.ZERO;
            int idx = 0;
            for (PaymentSchedule paymentSchedule : allNormalPaymentSchedules) {
                totalPrincipal = totalPrincipal.add(paymentSchedule.getPrincipalAmt());
                if(idx==0){
                	feeAmt = feeAmt.add(paymentSchedule.getFeeAmt());
                }
                idx++;
            }

            FinancingPackageView packageDetails = financingPackageService.getPackageDetails(packageId);
            BigDecimal supplyAmount = packageDetails.getSupplyAmount();
            // BigDecimal rate = packageDetails.getRate();

            PackageContract packageContractDetails = financingPackageService.getPackageContractDetails(packageId);
            BigDecimal financierPrepaymentPenaltyRate = packageContractDetails.getFncrPrepayPenaltyRt();
            String prepayDeductIntrFlg = packageContractDetails.getPrePayDeductIntrFlag();
            if ("Y".equalsIgnoreCase(prepayDeductIntrFlg)) {
                oneMonthInterest = allNormalPaymentSchedules.get(0).getInterestAmt();
                // supplyAmount.multiply(rate).divide(UNIT_MONTH_EACH_YEAR, 20, RoundingMode.HALF_UP)
                // .setScale(2, RoundingMode.HALF_UP);
            }
            // 个人罚金
            indivFeit = financierPrepaymentPenaltyRate.multiply(supplyAmount).setScale(2, RoundingMode.DOWN);

            totalPenalty = totalPenalty.add(indivFeit);
            totalAmt = totalPrincipal.add(oneMonthInterest).add(totalPenalty).add(feeAmt);
            dto.setPackageId(packageId);
            dto.setTotalAmt(totalAmt);
            dto.setTotalPrincipaBalance(totalPrincipal);
            dto.setTotalInterestAmt(oneMonthInterest);
            dto.setTotalPenalty(totalPenalty);
            dto.setFeeAmt(feeAmt);
        }
        return dto;
    }

}
