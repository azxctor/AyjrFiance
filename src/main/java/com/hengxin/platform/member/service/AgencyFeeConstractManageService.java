package com.hengxin.platform.member.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.util.PaginationUtil;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.member.domain.AgencyFeeConstractPo;
import com.hengxin.platform.member.domain.AuthzdCtrAgencyFeeContractInfoView;
import com.hengxin.platform.member.dto.AgencyFeeConstractDto;
import com.hengxin.platform.member.dto.AuthzdCtrSearchDto;
import com.hengxin.platform.member.repository.AgencyFeeConstractRepository;
import com.hengxin.platform.member.repository.AgencyFeeContractViewRepository;
import com.hengxin.platform.security.SecurityContext;

@Service
public class AgencyFeeConstractManageService {

	@Autowired
	private AgencyFeeContractViewRepository agencyFeeContractViewRepository;

	@Autowired
	private AgencyFeeConstractRepository agencyFeeConstractRepository;

	@Autowired
	private SecurityContext securityContext;

	public Page<AuthzdCtrAgencyFeeContractInfoView> getAuthzdCtrs(final AuthzdCtrSearchDto searchDo) {

		Pageable pageRequest = PaginationUtil.buildPageRequest(searchDo);

		Specification<AuthzdCtrAgencyFeeContractInfoView> specification = new Specification<AuthzdCtrAgencyFeeContractInfoView>() {
			@Override
			public Predicate toPredicate(Root<AuthzdCtrAgencyFeeContractInfoView> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				List<Expression<Boolean>> expressions = predicate.getExpressions();

				if (searchDo != null) {
					String keyword = searchDo.getKeyword();
					Date start_dt = searchDo.getBeginDate();
					Date end_dt = searchDo.getEndDate();
					if (StringUtils.isNotBlank(keyword)) {
						Predicate preName = cb.or(
								cb.like(cb.lower(root.<String> get("name")), "%" + keyword.toLowerCase()
										+ "%"),
								cb.like(cb.lower(root.<String> get("fullName")), "%" + keyword.toLowerCase()
										+ "%"));
						expressions.add(cb.or(cb.like(root.<String> get("acctNo"), "%" + keyword + "%"),
								preName));
					}

					if (start_dt != null && end_dt != null) {
						expressions.add(cb.between(root.get("endDt").as(Date.class), start_dt, end_dt));
					} else {
						if (start_dt != null) {
							expressions.add(cb.greaterThanOrEqualTo(root.get("endDt").as(Date.class),
									start_dt));
						}
						if (end_dt != null) {
							expressions.add(cb.lessThanOrEqualTo(root.get("endDt").as(Date.class), end_dt));
						}
					}
				}
				return predicate;
			}
		};
		Page<AuthzdCtrAgencyFeeContractInfoView> agencies = agencyFeeContractViewRepository.findAll(
				specification, pageRequest);
		return agencies;
	}

	/**
	 * 
	 * @param searchDo
	 * @return
	 */
	public Page<AgencyFeeConstractPo> getConstracts(final AuthzdCtrSearchDto searchDo) {

		Pageable pageRequest = PaginationUtil.buildPageRequest(searchDo);

		Specification<AgencyFeeConstractPo> specification = new Specification<AgencyFeeConstractPo>() {
			@Override
			public Predicate toPredicate(Root<AgencyFeeConstractPo> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				List<Expression<Boolean>> expressions = predicate.getExpressions();
				if (searchDo != null) {
					String authzdctrId = searchDo.getAuthzd_id();
					if (StringUtils.isNotBlank(authzdctrId)) {
						expressions.add(cb.equal(root.<String> get("orgId"), authzdctrId));
					}
				}
				return predicate;
			}
		};
		Page<AgencyFeeConstractPo> agencies = agencyFeeConstractRepository
				.findAll(specification, pageRequest);
		return agencies;
	}

	public AgencyFeeConstractPo getConstract(final String id) {
		Specification<AgencyFeeConstractPo> specification = new Specification<AgencyFeeConstractPo>() {
			@Override
			public Predicate toPredicate(Root<AgencyFeeConstractPo> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				List<Expression<Boolean>> expressions = predicate.getExpressions();
				if (StringUtils.isNotBlank(id)) {
					expressions.add(cb.equal(root.<String> get("id"), id));
				}
				return predicate;
			}
		};
		AgencyFeeConstractPo object = agencyFeeConstractRepository.findOne(specification);
		return object;
	}

	public AgencyFeeConstractPo save(AgencyFeeConstractPo object) {
		AgencyFeeConstractPo result = this.agencyFeeConstractRepository.save(object);
		return result;
	}

	@Transactional
	public AgencyFeeConstractPo save(AgencyFeeConstractDto dto) {
		AgencyFeeConstractPo object = null;
		final String id = dto.getId() == null ? null : dto.getId().toString();
		// final String orgId = dto.getOrgId();
		AgencyFeeConstractPo target = null;

		if (StringUtils.isNotBlank(id)) {
			Specification<AgencyFeeConstractPo> specification = new Specification<AgencyFeeConstractPo>() {
				@Override
				public Predicate toPredicate(Root<AgencyFeeConstractPo> root, CriteriaQuery<?> query,
						CriteriaBuilder cb) {
					Predicate predicate = cb.conjunction();
					List<Expression<Boolean>> expressions = predicate.getExpressions();
					expressions.add(cb.equal(root.<String> get("id"), id));
					return predicate;
				}
			};
			target = this.agencyFeeConstractRepository.findOne(specification);
		}

		if (target == null) {
			target = new AgencyFeeConstractPo();
		}
		object = load(dto, target);
		AgencyFeeConstractPo result = this.agencyFeeConstractRepository.save(object);
		return result;
	}

	private AgencyFeeConstractPo load(AgencyFeeConstractDto source, AgencyFeeConstractPo target) {
		String opID = securityContext.getCurrentUserId();
		target.setActgStdRT(source.getActgStdRT() == null ? BigDecimal.ZERO : source.getActgStdRT());
		target.setAllocRT(source.getAllocRT() == null ? BigDecimal.ZERO : source.getAllocRT());
		target.setBisunessType(source.getBisunessType());
		target.setChanDirector(source.getChanDirector());
		target.setContractName(source.getContractName());
		target.setContractType(source.getContractType());
		target.setDirector(source.getDirector());
		target.setEndDt(DateUtils.getDate(source.getEndDt(), "yyyy-MM-dd"));
		target.setLastMntTs(new Date());
		target.setLastMntOpId(opID);
		target.setMonth12RT(source.getMonth12RT() == null ? BigDecimal.ZERO : source.getMonth12RT());
		target.setMonth3RT(source.getMonth3RT() == null ? BigDecimal.ZERO : source.getMonth3RT());
		target.setMonth6RT(source.getMonth6RT() == null ? BigDecimal.ZERO : source.getMonth6RT());
		target.setMonth9RT(source.getMonth9RT() == null ? BigDecimal.ZERO : source.getMonth9RT());
		target.setNote(source.getNote());
		target.setSeatFeeAmt(source.getSeatFeeAmt() == null ? BigDecimal.ZERO : source.getSeatFeeAmt());
		target.setStartDt(DateUtils.getDate(source.getStartDt(), "yyyy-MM-dd"));
		target.setState(source.getState());
		target.setVersion(target.getVersion());
		target.setOrgId(source.getOrgId());
		if (target.getId() == null || target.getId() == 0) {
			target.setCreateTs(new Date());
			target.setCreatorOpId(opID);
		}
		return target;
	}
}
