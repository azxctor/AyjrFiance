package com.hengxin.platform.netting.service;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.dto.DataTablesRequestDto;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.netting.dto.RecvPaySearchDto;
import com.hengxin.platform.netting.entity.RecvPayOrderPo;
import com.hengxin.platform.netting.enums.NettingStatusEnum;
import com.hengxin.platform.netting.repository.RecvPayOrderRepository;
import com.hengxin.platform.security.entity.SimpleUserPo;
import com.hengxin.platform.security.entity.UserPo;

@Service
public class RecvPayOrderQueryService {

	@Autowired
	private RecvPayOrderRepository recvPayOrderRepository;

	@Transactional(readOnly=true)
	public List<RecvPayOrderPo> getRecvPayOrderList(Date trxDate, NettingStatusEnum status){	
		return recvPayOrderRepository.findByTrxDateAndNettingStatusOrderByCreateTsAsc(trxDate, status);
	}
	
	@Transactional(readOnly=true)
	public Page<RecvPayOrderPo> getRecvPayOrderData(final RecvPaySearchDto searchDto, final List<NettingStatusEnum> statusList){
		final String trxDateStr = searchDto.getTrxDate();
		final NettingStatusEnum status = searchDto.getStatus();
		final String acctNo = searchDto.getAcctNo();
		Pageable pageRequest = buildPageRequest(searchDto);
		Specification<RecvPayOrderPo> spec = new Specification<RecvPayOrderPo>() {
			@Override
			public Predicate toPredicate(Root<RecvPayOrderPo> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				List<Expression<Boolean>> expressions = predicate
						.getExpressions();
				if (StringUtils.isNotBlank(trxDateStr)) {
					Date trxDate = DateUtils.getDate(trxDateStr, "yyyy-MM-dd");
					expressions.add(cb.equal(root.<Date> get("trxDate"), trxDate));
				}
				if (status!=null) {
					expressions.add(cb.equal(root.<NettingStatusEnum> get("nettingStatus"), status));
				}
				else {
					expressions.add(root.<NettingStatusEnum> get("nettingStatus").in(statusList));
				}
				
				if(StringUtils.isNotBlank(acctNo)){
					Join<RecvPayOrderPo, SimpleUserPo> joinUser = root.join("userPo", JoinType.INNER);
	                Join<SimpleUserPo, UserPo> joinAcct = joinUser.join("account", JoinType.INNER);
	                expressions.add(cb.equal(joinAcct.get("acctNo"), acctNo));
				}
				return predicate;
			}
		};
		return recvPayOrderRepository.findAll(spec, pageRequest);
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
			sort = sort.and(new Sort(Direction.fromString(sortDir), "orderId"));
		}

		PageRequest page = new PageRequest(requestDto.getDisplayStart()
				/ requestDto.getDisplayLength(), requestDto.getDisplayLength(),
				sort);
		return page;
	}
}
