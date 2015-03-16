/*
 * Project Name: kmfex-platform
 * File Name: MemberService.java
 * Class Name: MemberService
 *
 * Copyright 2014 KMFEX Inc
 *
 * 
 *
 * http://www.kmfex.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hengxin.platform.report.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
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

import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.dto.DataTablesRequestDto;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.report.domain.AuthzdCtrTransFee;
import com.hengxin.platform.report.dto.AuthzdCtrTransFeeDto;
import com.hengxin.platform.report.dto.AuthzdCtrTransFeeSearchDto;
import com.hengxin.platform.report.repository.AuthzdCtrTransFeeRepository;
import com.hengxin.platform.security.SecurityContext;

/**
 * @author qimingzou
 * 
 */
@Service
public class AuthzdCtrTransFeeService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthzdCtrTransFeeService.class);

	@Autowired
	private AuthzdCtrTransFeeRepository authzdCtrTransFeeRepository;

	@Autowired
	private SecurityContext securityContext;
	
	@PersistenceContext(unitName = "default")
	private EntityManager em;
	
	@Transactional(readOnly = true)
	public BigDecimal getTotalFee(final AuthzdCtrTransFeeSearchDto searchDto) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder jpqlSql = new StringBuilder("select sum(a.trxAmt) from AuthzdCtrTransFee a where 1 = 1 ");
		
		if (!(securityContext.isCustomerServiceUser()
				|| securityContext.isChannelDeptUser() || securityContext
					.isFinanceDeptUser())) {
			jpqlSql.append(" and a.authzdCtrId = :authzdCtrId");
			params.put("authzdCtrId", securityContext.getCurrentUserId());
		}
		if (searchDto != null) {
			if (StringUtils.isNotBlank(searchDto.getKeyword())) {
				jpqlSql.append(" and (lower(a.acctNo) like :keyword or lower(a.userName) like :keyword or lower(a.pkgId) like :keyword or lower(a.agentName) like :keyword or lower(a.authzdCtrName) like :keyword or lower(a.authzdCtrAcctNo) like :keyword)");
				params.put("keyword", "%" + searchDto.getKeyword().trim().toLowerCase() + "%");
			}
			try {
				if (StringUtils.isNotBlank(searchDto.getBeginDate())) {
					jpqlSql.append(" and a.trxDt >= :beginDate");
					params.put("beginDate", DateUtils.getStartDate(DateUtils.getFirstDateOfMonth(searchDto.getBeginDate())));
				}
				if (StringUtils.isNotBlank(searchDto.getEndDate())) {
					jpqlSql.append(" and a.trxDt <= :endDate");
					params.put("endDate", DateUtils.getEndDate(DateUtils.getLastDateOfMonth(searchDto.getEndDate())));
				}
			} catch (ParseException e) {
				throw new BizServiceException(EErrorCode.ACCT_DATE_PARSE_FAILED);
			}
		}
		
		Query query = em.createQuery(jpqlSql.toString());
		for (Entry<String, Object> entry : params.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		
		List<?> result =  query.getResultList();
		if (!result.isEmpty() && result.get(0) != null) {
			return ((BigDecimal) result.get(0)).setScale(2, RoundingMode.DOWN);
		}
		
		return BigDecimal.ZERO;
	}
	
	@Transactional(readOnly = true)
	public DataTablesResponseDto<AuthzdCtrTransFeeDto> getAuthzdCtrTransFeeItems(
			final AuthzdCtrTransFeeSearchDto searchDto) {
		LOGGER.info("getAuthzdCtrTransFeeItems invoked, searchDto {}",
				searchDto);
		Pageable pageRequest = buildPageRequest(searchDto);

		Specification<AuthzdCtrTransFee> specification = getSpecification(searchDto);

		Page<AuthzdCtrTransFee> authzdCtrTransFees = authzdCtrTransFeeRepository.findAll(specification, pageRequest);

		DataTablesResponseDto<AuthzdCtrTransFeeDto> responseDtoList = new DataTablesResponseDto<AuthzdCtrTransFeeDto>();
		List<AuthzdCtrTransFeeDto> itemList = new ArrayList<AuthzdCtrTransFeeDto>();
		responseDtoList.setEcho(searchDto.getEcho());
		for (AuthzdCtrTransFee fee : authzdCtrTransFees) {
			AuthzdCtrTransFeeDto dto = ConverterService.convert(fee, AuthzdCtrTransFeeDto.class);
			itemList.add(dto);
		}
		responseDtoList.setData(itemList);
		responseDtoList.setTotalDisplayRecords(authzdCtrTransFees.getTotalElements());
		responseDtoList.setTotalRecords(authzdCtrTransFees.getTotalElements());
		return responseDtoList;
	}

	private Pageable buildPageRequest(DataTablesRequestDto requestDto) {
		Sort sort = null;
		List<Integer> sortedColumns = requestDto.getSortedColumns();
		List<String> sortDirections = requestDto.getSortDirections();
		List<String> dataProps = requestDto.getDataProps();

		if (sortedColumns != null) {
			for (Integer item : sortedColumns) {
				String sortColumn = dataProps.get(item);
				int indexOf = 0;
				if (StringUtils.isNotBlank(sortColumn)
						&& sortColumn.endsWith(".text")) {
					indexOf = sortColumn.lastIndexOf(".text");
				} else if (StringUtils.isNotBlank(sortColumn)
						&& sortColumn.endsWith(".fullText")) {
					indexOf = sortColumn.lastIndexOf(".fullText");
				}
				if (indexOf > 0) {
					sortColumn = sortColumn.substring(0, indexOf);
				}
				String sortDir = sortDirections.get(0);
				sort = new Sort(Direction.fromString(sortDir), sortColumn);
				sort = sort.and(new Sort(Direction.fromString(sortDir), "jnlNo"));
			}
		}
		PageRequest page = new PageRequest(requestDto.getDisplayStart()
				/ requestDto.getDisplayLength(), requestDto.getDisplayLength(), sort);
		return page;
	}
	
	private Specification<AuthzdCtrTransFee> getSpecification(final AuthzdCtrTransFeeSearchDto searchDto) {
		return new Specification<AuthzdCtrTransFee>() {

			@Override
			public Predicate toPredicate(Root<AuthzdCtrTransFee> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				List<Expression<Boolean>> expressions = predicate.getExpressions();

				if (!(securityContext.isCustomerServiceUser()
						|| securityContext.isChannelDeptUser() || securityContext
							.isFinanceDeptUser())) {
					expressions.add(cb.equal(root.<String> get("authzdCtrId"),
							securityContext.getCurrentUserId()));
				}

				if (searchDto != null) {
					if (StringUtils.isNotBlank(searchDto.getKeyword())) {
						expressions.add(cb.or(cb.like(
								cb.lower(root.<String> get("acctNo")), "%"
										+ searchDto.getKeyword().trim()
												.toLowerCase() + "%"), cb.like(
								cb.lower(root.<String> get("userName")), "%"
										+ searchDto.getKeyword().trim()
												.toLowerCase() + "%"), cb.like(
								cb.lower(root.<String> get("pkgId")), "%"
										+ searchDto.getKeyword().trim()
												.toLowerCase() + "%"), cb.like(
								cb.lower(root.<String> get("agentName")), "%"
										+ searchDto.getKeyword().trim()
												.toLowerCase() + "%"), cb.like(
								cb.lower(root.<String> get("authzdCtrName")), "%"
										+ searchDto.getKeyword().trim()
												.toLowerCase() + "%"), cb.like(
								cb.lower(root.<String> get("authzdCtrAcctNo")), "%"
										+ searchDto.getKeyword().trim()
												.toLowerCase() + "%")));
					}

					try {
						if (StringUtils.isNotBlank(searchDto.getBeginDate())) {
							expressions.add(cb.greaterThanOrEqualTo(root.<Date> get("trxDt"), DateUtils.getStartDate(DateUtils.getFirstDateOfMonth(searchDto.getBeginDate()))));
						}
						if (StringUtils.isNotBlank(searchDto.getEndDate())) {
							expressions.add(cb.lessThanOrEqualTo(root.<Date> get("trxDt"), DateUtils.getEndDate(DateUtils.getLastDateOfMonth(searchDto.getEndDate()))));
						}
					} catch (ParseException e) {
						throw new BizServiceException(EErrorCode.ACCT_DATE_PARSE_FAILED);
					}
				}

				return predicate;
			}
		};
	}
}
