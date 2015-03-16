package com.hengxin.platform.product.service;

import java.text.ParseException;
import java.util.ArrayList;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.constant.LiteralConstant;
import com.hengxin.platform.common.dto.DataTablesRequestDto;
import com.hengxin.platform.common.service.BaseService;
import com.hengxin.platform.fund.service.FinancierEntrustDepositService;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.product.domain.FinancingPackageView;
import com.hengxin.platform.product.domain.PackagePaymentView;
import com.hengxin.platform.product.domain.PaymentSchedule;
import com.hengxin.platform.product.dto.ScheduleSearchDto;
import com.hengxin.platform.product.enums.EPayStatus;
import com.hengxin.platform.product.repository.PaymentScheduleRepository;

/**
 * 违约处理.
 * 
 * @author shengzhou
 * 
 */

@Service
public class FinancingPackageDefaultService extends BaseService {

	private static final Logger LOGGER = LoggerFactory.getLogger(FinancingPackageDefaultService.class);
	
    @Autowired
    ProductService productService;

    @Autowired
    private PaymentScheduleRepository paymentScheduleRepository;

    @Autowired
    FinancierEntrustDepositService financierEntrustDepositService;

    private static final String MAX_DATE = "9999-12-31 23:59:59";
    private static final String MIN_DATE = "1970-01-01 00:00:00";

    /**
     * 
     * @param requestDto
     * @return
     */
    private Pageable buildPageRequest(DataTablesRequestDto requestDto) {
        List<String> sortList = new ArrayList<String>();
        sortList.add("financingPackageView.financierName");
        sortList.add("packageId");
        Sort sort = new Sort(Direction.ASC, sortList);
        PageRequest page = new PageRequest(requestDto.getDisplayStart() / requestDto.getDisplayLength(),
                requestDto.getDisplayLength(), sort);
        return page;
    }

    /**
     * Description: TODO
     * 
     * @param userId
     * @param searchDto
     * @return
     */

    @Transactional(readOnly = true)
    public Page<PaymentSchedule> getFinancingPackageListByUserId(final String userId, final ScheduleSearchDto searchDto,
            final Boolean iscleared) {
        Pageable pageable = buildPageRequest(searchDto);
        final String packageName = searchDto.getPackageName();
        final String packageId = searchDto.getPackageId();
        final String financierName = searchDto.getFinancierName();
        Specification<PaymentSchedule> spec = new Specification<PaymentSchedule>() {
            @Override
            public Predicate toPredicate(Root<PaymentSchedule> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Join<PaymentSchedule, FinancingPackageView> joinPackageView = root.join("financingPackageView",
                        JoinType.LEFT);
                Predicate conjunction = cb.conjunction();
                List<Expression<Boolean>> expressions = conjunction.getExpressions();
                if(StringUtils.isNotBlank(financierName)){
                    expressions.add(cb.like(cb.lower(joinPackageView.<String> get("financierName")), "%"
                            + financierName.trim().toLowerCase() + "%"));
                }
                if (StringUtils.isNotBlank(packageId)) {
                    expressions.add(cb.like(cb.lower(root.<String> get("packageId")), "%"
                            + packageId.trim().toLowerCase() + "%"));
                }
                if (StringUtils.isNotBlank(packageName)) {
                    expressions.add(cb.like(cb.lower(joinPackageView.<String> get("packageName")), "%"
                            + packageName.trim().toLowerCase() + "%"));
                }
                if (iscleared) {
                    expressions.add(cb.equal(root.<EPayStatus> get(LiteralConstant.STATUS), EPayStatus.CLEARED));
                } else {
                    Predicate disjunction = cb.disjunction();
                    List<Expression<Boolean>> expressions2 = disjunction.getExpressions();
                    expressions2.add(cb.equal(root.<EPayStatus> get(LiteralConstant.STATUS), EPayStatus.OVERDUE));
                    expressions2.add(cb.equal(root.<EPayStatus> get(LiteralConstant.STATUS), EPayStatus.COMPENSATING));
                    expressions2.add(cb.equal(root.<EPayStatus> get(LiteralConstant.STATUS), EPayStatus.COMPENSATORY));
                    expressions.add(disjunction);
                }
                
                
                String startDate = searchDto.getStartPaymentDate();
                String endDate = searchDto.getEndPaymentDate();
                Date startPayment = DateUtils.getDate(MIN_DATE, LiteralConstant.YYYY_MM_DD_HH_MM_SS);
                Date endPayment = DateUtils.getDate(MAX_DATE, LiteralConstant.YYYY_MM_DD_HH_MM_SS);
                try {
                    if (StringUtils.isNotBlank(startDate)) {
                        startPayment = DateUtils.getStartDate(DateUtils.getDate(startDate, LiteralConstant.YYYY_MM_DD));
                    }
                    if (StringUtils.isNotBlank(endDate)) {
                        endPayment = DateUtils.getEndDate(DateUtils.getDate(endDate, LiteralConstant.YYYY_MM_DD));
                    }
                } catch (ParseException e) {
                	LOGGER.error("ParseException {}", e);
                }

                if (StringUtils.isNotBlank(startDate) || StringUtils.isNotBlank(endDate)) {
                    expressions.add(cb.between(root.<Date> get("paymentDate"), startPayment, endPayment));
                }
                return conjunction;
            }
        };
        Page<PaymentSchedule> resultList = paymentScheduleRepository.findAll(spec, pageable);
        if (resultList != null && resultList.hasContent()) {
            for (PaymentSchedule ps : resultList) {
                ps.getFinancingPackageView().getId();
            }
        }
        return resultList;
    }

    /**
     * Description: 查询违约列表
     * 
     * @param userId
     * @param searchDto
     * @return
     */

    @Transactional(readOnly = true)
    public List<PaymentSchedule> getOverdueFinancingPackageList(final String userId, final ScheduleSearchDto searchDto) {
        final String packageName = searchDto.getPackageName();
        final String packageId = searchDto.getPackageId();
        final String financierName = searchDto.getFinancierName();
        Specification<PaymentSchedule> spec = new Specification<PaymentSchedule>() {
            @Override
            public Predicate toPredicate(Root<PaymentSchedule> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Join<PaymentSchedule, PackagePaymentView> joinPackageView = root.join("packagePaymentView",
                        JoinType.INNER);
                Predicate conjunction = cb.conjunction();
                List<Expression<Boolean>> expressions = conjunction.getExpressions();
                if (StringUtils.isNotBlank(packageId)) {
                    expressions.add(cb.like(cb.lower(root.<String> get("packageId")), "%"
                            + packageId.trim().toLowerCase() + "%"));
                }
                if(StringUtils.isNotBlank(financierName)){
                    expressions.add(cb.like(cb.lower(joinPackageView.<String> get("financierName")), "%"
                            + financierName.trim().toLowerCase() + "%"));
                }
                if (StringUtils.isNotBlank(packageName)) {
                    expressions.add(cb.like(cb.lower(joinPackageView.<String> get("packageName")), "%"
                            + packageName.trim().toLowerCase() + "%"));
                }
                
                String startDate = searchDto.getStartPaymentDate();
                String endDate = searchDto.getEndPaymentDate();
                Date startPayment = DateUtils.getDate(MIN_DATE, LiteralConstant.YYYY_MM_DD_HH_MM_SS);
                Date endPayment = DateUtils.getDate(MAX_DATE, LiteralConstant.YYYY_MM_DD_HH_MM_SS);
                try {
                    if (StringUtils.isNotBlank(startDate)) {
                        startPayment = DateUtils.getStartDate(DateUtils.getDate(startDate, LiteralConstant.YYYY_MM_DD));
                    }
                    if (StringUtils.isNotBlank(endDate)) {
                        endPayment = DateUtils.getEndDate(DateUtils.getDate(endDate, LiteralConstant.YYYY_MM_DD));
                    }
                } catch (ParseException e) {
                	LOGGER.error("ParseException {}", e);
                }

                if (StringUtils.isNotBlank(startDate) || StringUtils.isNotBlank(endDate)) {
                    expressions.add(cb.between(root.<Date> get("paymentDate"), startPayment, endPayment));
                }
                
                Predicate disjunction = cb.disjunction();
                List<Expression<Boolean>> expressions2 = disjunction.getExpressions();
                expressions2.add(cb.equal(root.<EPayStatus> get(LiteralConstant.STATUS), EPayStatus.OVERDUE));
                expressions2.add(cb.equal(root.<EPayStatus> get(LiteralConstant.STATUS), EPayStatus.COMPENSATING));
                expressions2.add(cb.equal(root.<EPayStatus> get(LiteralConstant.STATUS), EPayStatus.COMPENSATORY));
                expressions.add(disjunction);
                return conjunction;
            }
        };
        List<PaymentSchedule> findAll = paymentScheduleRepository.findAll(spec);
        if (findAll != null && !findAll.isEmpty()) {
            for (PaymentSchedule ps : findAll) {
                ps.getPackagePaymentView().getId();
            }
        }
        return paymentScheduleRepository.findAll(spec);
    }

    /**
     * Description: 还款期数.
     * 
     * @param packageId
     * @param period
     * @return
     */
    @Transactional(readOnly = true)
    public PaymentSchedule getPaymentScheduleByPackageIdAndPeriod(final String packageId, final int period) {
    	return paymentScheduleRepository.getByPackageIdAndSequenceId(packageId, period);
    }

}
