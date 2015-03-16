/*
 * Project Name: kmfex-platform
 * File Name: PositionLotService.java
 * Class Name: PositionLotService
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

package com.hengxin.platform.fund.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.fund.entity.PositionLotPo;
import com.hengxin.platform.fund.entity.PositionPo;
import com.hengxin.platform.fund.repository.PositionLotRepository;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.product.converter.InvestorPackageConverter;
import com.hengxin.platform.product.dto.FinancePackageInvestorDto;
import com.hengxin.platform.product.dto.PackageAmount;
import com.hengxin.platform.product.service.FinancingPackageAmountService;
import com.hengxin.platform.product.service.PaymentScheduleService;

/**
 * Class Name: PositionLotService Description: TODO
 * 
 * @author tingwang
 * 
 */
@Service
public class PositionLotService {

    @Autowired
    PositionLotRepository positionLotRepository;
    
    @Autowired
    private PaymentScheduleService paymentScheduleService;
    
    @Autowired
    private FinancingPackageAmountService financingPackageAmountService;

    public BigDecimal getAccumCrAmt(String pkgId, String userId) {
        return positionLotRepository.getAccumCrAmt(pkgId, userId);
    }

    public List<PositionLotPo> getLotsByPkgId(String pkgId) {
        return positionLotRepository.getLotsByPkgId(pkgId);
    }

    public List<PositionLotPo> savePositionLots(List<PositionLotPo> lots) {
        return positionLotRepository.save(lots);
    };

    public List<PositionLotPo> getLotsByPositionId(String positionId) {
        return positionLotRepository.getLotsByPositionId(positionId);
    };
    
    public PositionLotPo getPositionLotById(String lotId) {
        return positionLotRepository.findOne(lotId);
    };
    
    @Transactional(readOnly = true)
    public FinancePackageInvestorDto getFullPositionLotById(String lotId) {
    	PositionLotPo positionLot = getPositionLotById(lotId);
        String pkgId = positionLot.getPosition().getPkgId();
		FinancePackageInvestorDto financePackageInvestorDto = ConverterService.convert(positionLot, FinancePackageInvestorDto.class,
						InvestorPackageConverter.class);
        PackageAmount packageAmount = financingPackageAmountService.getPackageAmount(pkgId, positionLot.getUnit());
        financePackageInvestorDto.setRemTermLength(packageAmount.getRemTermLength());
        financePackageInvestorDto.setResidualInterestAmt(packageAmount.getResidualInterestAmt());
        financePackageInvestorDto.setResidualPrincipalAmt(packageAmount.getResidualPrincipalAmt());
        financePackageInvestorDto.setLastAmount(packageAmount.getResidualPrincipalAndInterestAmt());
        
        calculateTransferMaxPrice(financePackageInvestorDto, positionLot);
        return financePackageInvestorDto;
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

    /**
     * 
    * Description: TODO
    *
    * @param packageId
    * @return
     */
    public List<PositionLotPo> getPositionLotList(final String packageId) {
        Specification<PositionLotPo> spec = new Specification<PositionLotPo>() {
            @Override
            public Predicate toPredicate(Root<PositionLotPo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                Join<PositionLotPo, PositionPo> joinPosition = root.join("position", JoinType.INNER);
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                expressions.add(cb.equal(joinPosition.<String> get("pkgId"), packageId));
                return predicate;
            }
        };
        
        return positionLotRepository.findAll(spec);
    }

}
