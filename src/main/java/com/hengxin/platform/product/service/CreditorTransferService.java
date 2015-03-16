package com.hengxin.platform.product.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.hibernate.StaleStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.service.BaseService;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.fund.entity.PositionLotPo;
import com.hengxin.platform.fund.entity.PositionPo;
import com.hengxin.platform.fund.repository.PositionLotRepository;
import com.hengxin.platform.product.domain.CreditorRightsTransfer;
import com.hengxin.platform.product.domain.PaymentSchedule;
import com.hengxin.platform.product.domain.Product;
import com.hengxin.platform.product.domain.ProductPackage;
import com.hengxin.platform.product.dto.PackageAmount;
import com.hengxin.platform.product.dto.TransferPriceDto;
import com.hengxin.platform.product.enums.ETransferStatus;
import com.hengxin.platform.product.repository.CreditorRightsTransferRepository;
import com.hengxin.platform.product.repository.PaymentScheduleRepository;
import com.hengxin.platform.product.repository.ProductPackageRepository;
import com.hengxin.platform.product.repository.ProductRepository;
import com.hengxin.platform.security.SecurityContext;

/**
 * 债权转让service Class Name: CreditorTransferService Description: TODO
 * 
 * @author tiexiyu
 * 
 */

@Service
public class CreditorTransferService extends BaseService {

    @Autowired
    private ProductPackageRepository productPackageRepository;

    @Autowired
    private PaymentScheduleRepository paymentScheduleRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CreditorRightsTransferRepository creditorRightsTransferRepository;

    @Autowired
    private PositionLotRepository positionLotRepository;

    @Autowired
    private SecurityContext securityContext;

    @Autowired
    private FinancingPackageService financingPackageService;
    
    @Autowired
    private FinancingPackageAmountService financingPackageAmountService;

    public ProductPackage getPackageInfoByLotId(final String lotId) {
        ProductPackage ppk = null;
        Specification<ProductPackage> specification = new Specification<ProductPackage>() {
            @Override
            public Predicate toPredicate(Root<ProductPackage> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                query.distinct(true);
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                Join<ProductPackage, PositionPo> join = root.joinList("positionPos", JoinType.INNER);
                Join<PositionPo, PositionLotPo> join2 = join.joinList("positionLotPos", JoinType.INNER);
                expressions.add(cb.equal(join2.<String> get("lotId"), lotId));
                return predicate;
            }
        };
        ppk = this.productPackageRepository.findOne(specification);
        return ppk;
    }

    public List<PaymentSchedule> getPayInfoByLotId(String lotId) {
        List<PaymentSchedule> paymentSchedules = null;
        ProductPackage productPackage = getPackageInfoByLotId(lotId);
        if (productPackage != null && StringUtils.isNotBlank(productPackage.getId())) {
            paymentSchedules = this.paymentScheduleRepository
                    .getByPackageIdOrderBySequenceIdAsc(productPackage.getId());
        }
        return paymentSchedules;
    }

    public Product getProductByLotId(final String lotId) {
        Product product = null;
        Specification<Product> specification = new Specification<Product>() {
            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                query.distinct(true);
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                Join<Product, ProductPackage> join = root.joinList("productPackages", JoinType.INNER);
                Join<ProductPackage, PositionPo> join1 = join.joinList("positionPos", JoinType.INNER);
                Join<PositionPo, PositionLotPo> join2 = join1.joinList("positionLotPos", JoinType.INNER);
                expressions.add(cb.equal(join2.<String> get("lotId"), lotId));
                return predicate;
            }
        };
        product = this.productRepository.findOne(specification);
        return product;
    }

    private PositionLotPo gePositionLotByLotId(final String lotId) {
        PositionLotPo plp = null;
        Specification<PositionLotPo> specification = new Specification<PositionLotPo>() {
            @Override
            public Predicate toPredicate(Root<PositionLotPo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                expressions.add(cb.equal(root.<String> get("lotId"), lotId));
                root.fetch("position");
                return predicate;
            }
        };
        plp = this.positionLotRepository.findOne(specification);
        return plp;
    }

    @Transactional
    public void cancelCreditorTransfer(String lotId) {
        CreditorRightsTransfer tsfr = creditorRightsTransferRepository.findByLotIdAndStatus(lotId,
                ETransferStatus.ACTIVE.getCode());
        if (tsfr == null) {
            throw new HibernateOptimisticLockingFailureException(new StaleStateException(""));
        }
        tsfr.setStatus(ETransferStatus.CANCELLED.getCode());
        tsfr.setLastMntOpid(securityContext.getCurrentUserId());
        tsfr.setLastMntTs(new Date());
        creditorRightsTransferRepository.save(tsfr);
    }

    @Transactional
    public void saveCreditorTransfer(TransferPriceDto transferPriceDto) {
		for (CreditorRightsTransfer transfer : creditorRightsTransferRepository
				.findByLotId(transferPriceDto.getLotId())) {
			if (ETransferStatus.ACTIVE.getCode().equals(transfer.getStatus())
					|| ETransferStatus.SUCCESS.getCode().equals(
							transfer.getStatus())) {
				throw new HibernateOptimisticLockingFailureException(
						new StaleStateException(""));
			}
		}
        CreditorRightsTransfer creditorRightsTransfer = new CreditorRightsTransfer();
        Date now = new Date();
        String opid = this.securityContext.getCurrentUserId();
        creditorRightsTransfer.setStatus(ETransferStatus.ACTIVE.getCode());
        creditorRightsTransfer.setCreateOpid(opid);
        creditorRightsTransfer.setCreateTs(now);
        creditorRightsTransfer.setLastMntOpid(opid);
        creditorRightsTransfer.setLastMntTs(now);
        creditorRightsTransfer.setLotId(transferPriceDto.getLotId());
        creditorRightsTransfer.setPrice(transferPriceDto.getTransPrice());
        creditorRightsTransfer.setTransferTs(this.getTransferDate(now));

        PositionLotPo pLotPo = this.gePositionLotByLotId(transferPriceDto.getLotId());
        if (pLotPo != null) {
            creditorRightsTransfer.setUnit(pLotPo.getUnit());
        }
        ProductPackage productPackage = this.getPackageInfoByLotId(transferPriceDto.getLotId());
        List<PaymentSchedule> paymentSchedules = this.getPayInfoByLotId(transferPriceDto.getLotId());
        PackageAmount packageAmount = financingPackageAmountService.getPackageAmount(productPackage.getId(),
        		pLotPo.getUnit());
        
        creditorRightsTransfer.setRemainAmount(packageAmount.getResidualPrincipalAndInterestAmt());
        creditorRightsTransfer.setRemainPrinAmount(packageAmount.getResidualPrincipalAmt());
        creditorRightsTransfer.setRemainIntrAmount(packageAmount.getResidualInterestAmt());
        creditorRightsTransfer.setRemainPeriods(packageAmount.getRemTermLength());
        creditorRightsTransfer.setMaturityDate(paymentSchedules.get(paymentSchedules.size() - 1).getPaymentDate());
        creditorRightsTransferRepository.save(creditorRightsTransfer);
        pLotPo.setLastMntOpid(opid);
        pLotPo.setLastMntTs(now);
        positionLotRepository.save(pLotPo);
    }
    
    private Date getTransferDate(Date createDate) {
		try {
	        DateFormat df = new SimpleDateFormat("HH:mm:ss");
	        String time = df.format(createDate);
	        Date workDate = CommonBusinessUtil.getCurrentWorkDate();
	        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
	        String date = df2.format(workDate);
	        DateFormat df3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        Date transferDate = df3.parse(date + " " + time);
	        return transferDate;
		} catch (ParseException e) {
			return CommonBusinessUtil.getCurrentWorkDate();
		}
	}

}
