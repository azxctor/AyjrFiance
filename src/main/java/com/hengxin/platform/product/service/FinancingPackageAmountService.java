package com.hengxin.platform.product.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hengxin.platform.common.service.BaseService;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.product.ProdPkgUtil;
import com.hengxin.platform.product.domain.FinancingPackageView;
import com.hengxin.platform.product.domain.PaymentSchedule;
import com.hengxin.platform.product.domain.Product;
import com.hengxin.platform.product.dto.PackageAmount;
import com.hengxin.platform.product.enums.EPayStatus;
import com.hengxin.platform.product.repository.FinancingPackageViewRepository;

/**
 * 产品包
 * 
 * @author tiexiyu
 * 
 */

@Service
public class FinancingPackageAmountService extends BaseService {
	
	@Autowired
    private FinancingPackageService financingPackageService;
	
	@Autowired
	private FinancingPackageViewRepository financingPackageViewRepository;
	
	@Autowired
    private ProductService productService;
	
	public PackageAmount getPackageAmount(String pkgId, Integer unit) {
		PackageAmount packageAmount = new PackageAmount();
		
        // 按时间升序查询状态为还款中的第一个记录作为当前还款记录
        List<PaymentSchedule> paymentScheduleList = financingPackageService
                .getCurrentPackageScheduleListByPkgId(pkgId);
        if (paymentScheduleList != null && paymentScheduleList.size() > 0) {
        	
        	FinancingPackageView financingPackageView = financingPackageViewRepository.findOne(pkgId);
        	
            BigDecimal totalUnit = financingPackageView.getSupplyAmount().divide(financingPackageView.getUnitAmount());
            
            String productId = financingPackageView.getProductId();
            Product product = productService.getProductById(productId);
            Date currentWorkDate = CommonBusinessUtil.getCurrentWorkDate();
        	
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
            packageAmount.setResidualPrincipalAndInterestAmt(amount.multiply(new BigDecimal(unit)).divide(totalUnit, 2, RoundingMode.HALF_UP));
			BigDecimal residualPrincipalAmt = principalAmt
					.add(principalFeitAmt).add(wrtrPrincipalFeitAmt)
					.subtract(payPrincipalAmt)
					.subtract(payPrincipalFeitAmt)
					.subtract(payWrtrPrincipalFeitAmt);

			BigDecimal residualInterestAmt = interestAmt
					.add(interestFeitAmt).add(wrtrInterestFeitAmt)
					.subtract(payInterestAmt).subtract(payInterestFeitAmt)
					.subtract(payWrtrInterestFeitAmt);
			packageAmount.setResidualPrincipalAmt(residualPrincipalAmt.multiply(new BigDecimal(unit)).divide(totalUnit, 2, RoundingMode.HALF_UP));
			packageAmount.setResidualInterestAmt(residualInterestAmt.multiply(new BigDecimal(unit)).divide(totalUnit, 2, RoundingMode.HALF_UP));
			packageAmount.setRemTermLength(remTermLength);
        }
        return packageAmount;
	}
}
