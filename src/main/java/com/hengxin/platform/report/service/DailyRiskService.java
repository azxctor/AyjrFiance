package com.hengxin.platform.report.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.dto.DataTablesRequestDto;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.product.domain.PaymentSchedule;
import com.hengxin.platform.product.enums.EPayStatus;
import com.hengxin.platform.product.repository.FinancingPackageViewRepository;
import com.hengxin.platform.product.repository.PaymentScheduleRepository;
import com.hengxin.platform.report.dto.DailyRiskSearchDto;

@Service
public class DailyRiskService {

    private static final String YYYY_MM_DD = "yyyy-MM-dd";
    private static final String START_DATE = "1111-01-01";
    private static final String END_DATE = "9999-01-01";

    @Autowired
    private FinancingPackageViewRepository financingPackageViewRepository;

    @Autowired
    private PaymentScheduleRepository paymentScheduleRepository;

    /**
     * 当日逾期明细
     * 
     * @param queryDto
     * @return
     * @throws BizServiceException
     */
    @Transactional(readOnly = true)
    public Page<PaymentSchedule> getOverduePackages(final DailyRiskSearchDto queryDto) throws BizServiceException {
        Specification<PaymentSchedule> specification = new Specification<PaymentSchedule>() {
            @Override
            public Predicate toPredicate(Root<PaymentSchedule> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();                
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                String inputStartDate = queryDto.getStartDate();
                String inputEndDate = queryDto.getEndDate();
                if (StringUtils.isBlank(inputStartDate)) {
                    inputStartDate = DateUtils.formatDate(DateUtils.getDate(START_DATE, YYYY_MM_DD), YYYY_MM_DD);
                }
                if (StringUtils.isBlank(inputEndDate)) {
                    inputEndDate = DateUtils.formatDate(DateUtils.getDate(END_DATE, YYYY_MM_DD), YYYY_MM_DD);
                }
                Date startDate = null;
                Date endDate = null;
                try {
                    startDate = DateUtils.getStartDate(DateUtils.getDate(inputStartDate, YYYY_MM_DD));
                    endDate = DateUtils.getEndDate(DateUtils.getDate(inputEndDate, YYYY_MM_DD));
                } catch (ParseException e) {
                    throw new BizServiceException(EErrorCode.TECH_DATA_INVALID, "时间格式错误");
                }
                root.join("financingPackageView", JoinType.INNER);
                List<EPayStatus> overdueList = new ArrayList<EPayStatus>();
                overdueList.add(EPayStatus.OVERDUE);
                overdueList.add(EPayStatus.COMPENSATING);
                expressions.add(root.<EPayStatus> get("status").in(overdueList));
                expressions.add(cb.between(root.<Date> get("paymentDate"), startDate, endDate));
                return predicate;
            }
        };

        Pageable pageable = this.buildPageRequest(queryDto);
        Page<PaymentSchedule> page = this.paymentScheduleRepository.findAll(specification, pageable);
        // 当前事务内加载数据
        if (page != null && page.hasContent()) {
            for (PaymentSchedule ps : page) {
                ps.getFinancingPackageView().getId();
            }
        }
        return page;
    }

    /**
     * 
     * @param requestDto
     * @return
     */
    public Pageable buildPageRequest(DataTablesRequestDto requestDto) {
        PageRequest page = new PageRequest(requestDto.getDisplayStart() / requestDto.getDisplayLength(),
                requestDto.getDisplayLength(), null);
        return page;
    }

    /**
     * 当日逾期明细---导出查询
     * 
     * @param queryDto
     * @return
     * @throws BizServiceException
     */
    @Transactional(readOnly = true)
    public List<PaymentSchedule> getAllPackages(final String startDate, final String endDate)
            throws BizServiceException {
        Specification<PaymentSchedule> specification = new Specification<PaymentSchedule>() {
            @Override
            public Predicate toPredicate(Root<PaymentSchedule> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                String inputStartDate = startDate;
                String inputEndDate = endDate;
                if (StringUtils.isBlank(inputStartDate)) {
                    inputStartDate = DateUtils.formatDate(DateUtils.getDate(START_DATE, YYYY_MM_DD), YYYY_MM_DD);
                }
                if (StringUtils.isBlank(inputEndDate)) {
                    inputEndDate = DateUtils.formatDate(DateUtils.getDate(END_DATE, YYYY_MM_DD), YYYY_MM_DD);
                }
                Date startDate = null;
                Date endDate = null;
                try {
                    startDate = DateUtils.getStartDate(DateUtils.getDate(inputStartDate, YYYY_MM_DD));
                    endDate = DateUtils.getEndDate(DateUtils.getDate(inputEndDate, YYYY_MM_DD));
                } catch (ParseException e) {
                    throw new BizServiceException(EErrorCode.TECH_DATA_INVALID, "时间格式错误");
                }
                root.join("financingPackageView", JoinType.INNER);
                List<EPayStatus> overdueList = new ArrayList<EPayStatus>();
                overdueList.add(EPayStatus.OVERDUE);
                overdueList.add(EPayStatus.COMPENSATING);
                expressions.add(root.<EPayStatus> get("status").in(overdueList));
                expressions.add(cb.between(root.<Date> get("paymentDate"), startDate, endDate));
                return predicate;
            }
        };

        List<PaymentSchedule> list = this.paymentScheduleRepository.findAll(specification);
        // 当前事务内加载数据
        if (list != null && !list.isEmpty()) {
            for (PaymentSchedule ps : list) {
                ps.getFinancingPackageView().getId();
            }
        }
        return list;
    }

    /**
     * 当日代偿明细
     * 
     * @param queryDto
     * @return
     * @throws BizServiceException
     */
    @Transactional(readOnly = true)
    public Page<PaymentSchedule> getCompensatoryPaymentList(final DailyRiskSearchDto queryDto)
            throws BizServiceException {
        Specification<PaymentSchedule> specification = new Specification<PaymentSchedule>() {
            @Override
            public Predicate toPredicate(Root<PaymentSchedule> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                String inputStartDate = queryDto.getStartDate();
                String inputEndDate = queryDto.getEndDate();
                if (StringUtils.isBlank(inputStartDate)) {
                    inputStartDate = DateUtils.formatDate(DateUtils.getDate(START_DATE, YYYY_MM_DD), YYYY_MM_DD);
                }
                if (StringUtils.isBlank(inputEndDate)) {
                    inputEndDate = DateUtils.formatDate(DateUtils.getDate(END_DATE, YYYY_MM_DD), YYYY_MM_DD);
                }
                Date startDate = null;
                Date endDate = null;
                try {
                    startDate = DateUtils.getStartDate(DateUtils.getDate(inputStartDate, YYYY_MM_DD));
                    endDate = DateUtils.getEndDate(DateUtils.getDate(inputEndDate, YYYY_MM_DD));
                } catch (ParseException e) {
                    throw new BizServiceException(EErrorCode.TECH_DATA_INVALID, "时间格式错误");
                }
                root.join("financingPackageView", JoinType.INNER);
                expressions.add(cb.greaterThan(root.<BigDecimal> get("cmpnsPyAmt"),BigDecimal.ZERO ));
                expressions.add(cb.between(root.<Date> get("paymentDate"), startDate, endDate));
                return predicate;
            }
        };

        Pageable pageable = this.buildPageRequest(queryDto);
        Page<PaymentSchedule> page = this.paymentScheduleRepository.findAll(specification, pageable);
        // 当前事务内加载数据
        if (page != null && page.hasContent()) {
            for (PaymentSchedule ps : page) {
                ps.getFinancingPackageView().getId();
            }
        }
        return page;
    }

    /**
     * 11.911.8 当日代偿明细---导出查询
     * 
     * @param queryDto
     * @return
     * @throws BizServiceException
     */
    @Transactional(readOnly = true)
    public List<PaymentSchedule> getAllCompensatoryPayment(final String startDate, final String endDate)
            throws BizServiceException {
        Specification<PaymentSchedule> specification = new Specification<PaymentSchedule>() {
            @Override
            public Predicate toPredicate(Root<PaymentSchedule> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                String inputStartDate = startDate;
                String inputEndDate = endDate;
                if (StringUtils.isBlank(inputStartDate)) {
                    inputStartDate = DateUtils.formatDate(DateUtils.getDate(START_DATE, YYYY_MM_DD), YYYY_MM_DD);
                }
                if (StringUtils.isBlank(inputEndDate)) {
                    inputEndDate = DateUtils.formatDate(DateUtils.getDate(END_DATE, YYYY_MM_DD), YYYY_MM_DD);
                }
                Date startDate = null;
                Date endDate = null;
                try {
                    startDate = DateUtils.getStartDate(DateUtils.getDate(inputStartDate, YYYY_MM_DD));
                    endDate = DateUtils.getEndDate(DateUtils.getDate(inputEndDate, YYYY_MM_DD));
                } catch (ParseException e) {
                    throw new BizServiceException(EErrorCode.TECH_DATA_INVALID, "时间格式错误");
                }
                root.join("financingPackageView", JoinType.INNER);
                
                expressions.add(cb.greaterThan(root.<BigDecimal> get("cmpnsPyAmt"),BigDecimal.ZERO ));
                
                expressions.add(cb.between(root.<Date> get("paymentDate"), startDate, endDate));
                return predicate;
            }
        };
        List<PaymentSchedule> list = this.paymentScheduleRepository.findAll(specification);
        // 当前事务内加载数据
        if (list != null && !list.isEmpty()) {
            for (PaymentSchedule ps : list) {
                ps.getFinancingPackageView().getId();
            }
        }
        return list;
    }

}
