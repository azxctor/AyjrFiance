package com.hengxin.platform.fund.service.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.util.PaginationUtil;
import com.hengxin.platform.fund.dto.AcctFreezeLogDto;
import com.hengxin.platform.fund.dto.AcctFreezeSearchDto;
import com.hengxin.platform.fund.dto.biz.req.atom.FreezeReq;
import com.hengxin.platform.fund.dto.biz.req.atom.UnFreezeReq;
import com.hengxin.platform.fund.entity.UserAcctFreezeDtlView;
import com.hengxin.platform.fund.enums.EFnrOperType;
import com.hengxin.platform.fund.enums.EFnrStatus;
import com.hengxin.platform.fund.repository.FreezeReserveDtlRepository;
import com.hengxin.platform.fund.repository.UserAcctFreezeDtlViewRepository;
import com.hengxin.platform.fund.service.AcctService;
import com.hengxin.platform.fund.service.FundAcctFreezeMgtService;
import com.hengxin.platform.fund.service.atom.FundAcctService;

@Service
@Qualifier("fundAcctFreezeMgtService")
public class FundAcctFreezeMgtServiceImpl implements FundAcctFreezeMgtService {

    @Autowired
    private FundAcctService fundAcctService;

    @Autowired
    private AcctService acctService;

    @Autowired
    private FreezeReserveDtlRepository freezeReserveDtlRepository;

    @Autowired
    private UserAcctFreezeDtlViewRepository userAcctFreezeDtlViewRepository;

    @Override
    public String freezeAcct(FreezeReq req, boolean isBizFreeze) throws BizServiceException {
        return fundAcctService.freezeAcct(req, isBizFreeze);
    }

    @Override
    public BigDecimal unFreezeAcct(UnFreezeReq req, boolean isBizFreeze) throws BizServiceException {
        return fundAcctService.unFreezeAcct(req, isBizFreeze);
    }

    @Override
    public Page<UserAcctFreezeDtlView> getUserAcctFreezeList(final AcctFreezeSearchDto queryDto) {
        Specification<UserAcctFreezeDtlView> specification = new Specification<UserAcctFreezeDtlView>() {
            @Override
            public Predicate toPredicate(Root<UserAcctFreezeDtlView> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                expressions.add(cb.equal(root.<EFnrStatus> get("status"), EFnrStatus.ACTIVE));
                expressions.add(cb.equal(root.<EFnrOperType> get("operType"), EFnrOperType.FREEZE_MGT_NOPAY));
                if (StringUtils.isNotBlank(queryDto.getAcctNo())) {
                    expressions.add(cb.like(cb.lower(root.<String> get("acctNo")), "%"
                            + queryDto.getAcctNo().trim().toLowerCase() + "%"));
                }
                if (StringUtils.isNotBlank(queryDto.getUserName())) {
                    expressions.add(cb.like(cb.lower(root.<String> get("userName")), "%"
                            + queryDto.getUserName().trim().toLowerCase() + "%"));
                }
                return predicate;
            }
        };
        Pageable pageable = PaginationUtil.buildPageRequest(queryDto);
        Page<UserAcctFreezeDtlView> frrPage = this.userAcctFreezeDtlViewRepository.findAll(specification, pageable);
        return frrPage;
    }

    @Override
    public Page<UserAcctFreezeDtlView> getUserAcctFreezeLogList(final AcctFreezeLogDto queryDto) {
        Specification<UserAcctFreezeDtlView> specification = new Specification<UserAcctFreezeDtlView>() {
            @Override
            public Predicate toPredicate(Root<UserAcctFreezeDtlView> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                if (StringUtils.isNotBlank(queryDto.getAcctNo())) {
                    expressions.add(cb.like(cb.lower(root.<String> get("acctNo")), "%"
                            + queryDto.getAcctNo().trim().toLowerCase() + "%"));
                }
                return predicate;
            }
        };
        Pageable pageable = PaginationUtil.buildPageRequest(queryDto);
        Page<UserAcctFreezeDtlView> frrPage = this.userAcctFreezeDtlViewRepository.findAll(specification, pageable);
        return frrPage;
    }
}
