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

package com.hengxin.platform.account.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.hengxin.platform.account.dto.TransactionJournalDto;
import com.hengxin.platform.account.dto.upstream.TransactionJournalSearchDto;
import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.dto.DataTablesRequestDto;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.entity.TransactionJournalPo;
import com.hengxin.platform.fund.enums.EFundUseType;
import com.hengxin.platform.fund.repository.TransactionJournalRepository;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.member.repository.UserRepository;
import com.hengxin.platform.security.SecurityContext;

/**
 * @author qimingzou
 * 
 */
@Service
public class TransactionJournalService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(TransactionJournalService.class);

	@Autowired
	private TransactionJournalRepository transactionJournalRepository;

	@Autowired
	private SecurityContext securityContext;
	
	@Autowired
	private UserRepository userRepository;

	@Transactional(readOnly = true)
	public DataTablesResponseDto<TransactionJournalDto> getTransactionJournalItems(
			final TransactionJournalSearchDto searchDto) {
		LOGGER.info("getTransactionJournalItems invoked, searchDto {}",
				searchDto);
		Pageable pageRequest = buildPageRequest(searchDto);

		Specification<TransactionJournalPo> specification = new Specification<TransactionJournalPo>() {

			@Override
			public Predicate toPredicate(Root<TransactionJournalPo> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				List<Expression<Boolean>> expressions = predicate
						.getExpressions();

				String authzdCtrId = securityContext.getCurrentUser()
						.getOwnerId() != null ? securityContext
						.getCurrentUser().getOwnerId() : securityContext
						.getCurrentUserId();
						
				expressions.add(cb.equal(root.<String> get("ownerAthzdCtrId"), authzdCtrId));

				if (searchDto != null) {
					if (StringUtils.isNotBlank(searchDto.getKeyword())) {
						expressions.add(cb.or(cb.like(
								cb.lower(root.<String> get("acctNo")), "%"
										+ searchDto.getKeyword().trim()
												.toLowerCase() + "%"), cb.like(
								cb.lower(root.<String> get("name")), "%"
										+ searchDto.getKeyword().trim()
												.toLowerCase() + "%")));
					}

					if (StringUtils.isNotBlank(searchDto.getAgent())) {
						expressions.add(cb.like(
								cb.lower(root.<String> get("agent")), "%"
										+ searchDto.getAgent().trim()
												.toLowerCase() + "%"));
					}

					if (StringUtils.isNotBlank(searchDto.getAgentName())) {
						expressions.add(cb.like(
								cb.lower(root.<String> get("agentName")), "%"
										+ searchDto.getAgentName().trim()
												.toLowerCase() + "%"));
					}

					if (searchDto.getUseType() != null && searchDto.getUseType() != EFundUseType.INOUTALL) {
						expressions.add(cb.equal(
								root.<EFundUseType> get("useType"),
								searchDto.getUseType()));
					}

					try {
						if (searchDto.getBeginDate() != null) {
							expressions.add(cb.greaterThanOrEqualTo(root
									.<Date> get("trxDt"), DateUtils
									.getStartDate(searchDto.getBeginDate())));
						}
						if (searchDto.getEndDate() != null) {
							expressions.add(cb.lessThanOrEqualTo(root
									.<Date> get("trxDt"), DateUtils
									.getEndDate(searchDto.getEndDate())));
						}
					} catch (ParseException e) {
						throw new BizServiceException(
								EErrorCode.ACCT_DATE_PARSE_FAILED);
					}
				}

				return predicate;
			}
		};

		Page<TransactionJournalPo> transactionJournals = transactionJournalRepository
				.findAll(specification, pageRequest);

		DataTablesResponseDto<TransactionJournalDto> responseDtoList = new DataTablesResponseDto<TransactionJournalDto>();
		List<TransactionJournalDto> itemList = new ArrayList<TransactionJournalDto>();
		responseDtoList.setEcho(searchDto.getEcho());
		for (TransactionJournalPo tj : transactionJournals) {
			TransactionJournalDto dto = ConverterService.convert(tj,
					TransactionJournalDto.class);
			itemList.add(dto);
		}
		responseDtoList.setData(itemList);
		responseDtoList.setTotalDisplayRecords(transactionJournals
				.getTotalElements());
		responseDtoList.setTotalRecords(transactionJournals.getTotalElements());
		return responseDtoList;
	}
	
	@Transactional(readOnly = true)
	public List<TransactionJournalDto> getTransactionJournalExItems(
			final TransactionJournalSearchDto searchDto) {
		LOGGER.info("getTransactionJournalExItems invoked, searchDto {}",
				searchDto); 
		Specification<TransactionJournalPo> specification = new Specification<TransactionJournalPo>() {
			
			@Override
			public Predicate toPredicate(Root<TransactionJournalPo> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				List<Expression<Boolean>> expressions = predicate
						.getExpressions();
				
				String authzdCtrId = securityContext.getCurrentUser()
						.getOwnerId() != null ? securityContext
								.getCurrentUser().getOwnerId() : securityContext
								.getCurrentUserId();
								
								expressions.add(cb.equal(root.<String> get("ownerAthzdCtrId"), authzdCtrId));
								
								if (searchDto != null) {
									if (StringUtils.isNotBlank(searchDto.getKeyword())) {
										expressions.add(cb.or(cb.like(
												cb.lower(root.<String> get("acctNo")), "%"
														+ searchDto.getKeyword().trim()
														.toLowerCase() + "%"), cb.like(
																cb.lower(root.<String> get("name")), "%"
																		+ searchDto.getKeyword().trim()
																		.toLowerCase() + "%")));
									}
									
									if (StringUtils.isNotBlank(searchDto.getAgent())) {
										expressions.add(cb.like(
												cb.lower(root.<String> get("agent")), "%"
														+ searchDto.getAgent().trim()
														.toLowerCase() + "%"));
									}
									
									if (StringUtils.isNotBlank(searchDto.getAgentName())) {
										expressions.add(cb.like(
												cb.lower(root.<String> get("agentName")), "%"
														+ searchDto.getAgentName().trim()
														.toLowerCase() + "%"));
									}
									
									if (searchDto.getUseType() != null && searchDto.getUseType() != EFundUseType.INOUTALL) {
										expressions.add(cb.equal(
												root.<EFundUseType> get("useType"),
												searchDto.getUseType()));
									}
									
									try {
										if (searchDto.getBeginDate() != null) {
											expressions.add(cb.greaterThanOrEqualTo(root
													.<Date> get("trxDt"), DateUtils
													.getStartDate(searchDto.getBeginDate())));
										}
										if (searchDto.getEndDate() != null) {
											expressions.add(cb.lessThanOrEqualTo(root
													.<Date> get("trxDt"), DateUtils
													.getEndDate(searchDto.getEndDate())));
										}
									} catch (ParseException e) {
										throw new BizServiceException(
												EErrorCode.ACCT_DATE_PARSE_FAILED);
									}
								}
								
								return predicate;
			}
		};
		
		List<TransactionJournalPo> transactionJournals = transactionJournalRepository
				.findAll(specification);
	    List<TransactionJournalDto> itemList = new ArrayList<TransactionJournalDto>();
	 
		for (TransactionJournalPo tj : transactionJournals) {
			TransactionJournalDto dto = ConverterService.convert(tj,
					TransactionJournalDto.class);
			itemList.add(dto);
		} 
		return itemList;
	}
	
	@Transactional(readOnly = true)
	public Map<String,BigDecimal> getTransactionJournalItemsSum(final TransactionJournalSearchDto searchDto) {
		LOGGER.info("getTransactionJournalItemsSum invoked, searchDto {}",searchDto);
		
		String authzdCtrId = securityContext.getCurrentUser()
				.getOwnerId() != null ? securityContext
				.getCurrentUser().getOwnerId() : securityContext
				.getCurrentUserId();
		Map<String,BigDecimal> result = new HashMap<String, BigDecimal>();
		BigDecimal [] summaryobj =  transactionJournalRepository.getTransactionJournalNumSumArr(authzdCtrId
				,searchDto.getKeyword()
				,searchDto.getUseType()
				,searchDto.getBeginDate()
				,searchDto.getEndDate()
				,searchDto.getAgent()
				,searchDto.getAgentName());
		result.put("receiveAmtSum", summaryobj[0]);
		result.put("payAmtSum", summaryobj[1]);
		
		
		return result;
	}

	private Pageable buildPageRequest(DataTablesRequestDto requestDto) {
		Sort sort = null;
		List<Integer> sortedColumns = requestDto.getSortedColumns();
		List<String> sortDirections = requestDto.getSortDirections();
		List<String> dataProps = requestDto.getDataProps();

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

		PageRequest page = new PageRequest(requestDto.getDisplayStart()
				/ requestDto.getDisplayLength(), requestDto.getDisplayLength(),
				sort);
		return page;
	}
}
