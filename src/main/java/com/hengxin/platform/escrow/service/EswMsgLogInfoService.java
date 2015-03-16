package com.hengxin.platform.escrow.service;

import java.text.ParseException;
import java.util.ArrayList;
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

import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.util.PaginationUtil;
import com.hengxin.platform.ebc.entity.EswMsgLogPo;
import com.hengxin.platform.ebc.enums.EEbcCommandType;
import com.hengxin.platform.ebc.enums.EEbcMsgType;
import com.hengxin.platform.escrow.dto.EswMsgLogInfoDto;
import com.hengxin.platform.escrow.dto.EswMsgLogInfoSearchDto;
import com.hengxin.platform.escrow.repository.EswMsgLogInfoRepository;
import com.hengxin.platform.fund.util.DateUtils;

/**
 * 与第三方资金托管交互日志service
 * 
 * @author chenwulou
 *
 */
@Service
public class EswMsgLogInfoService {

//	private static final Logger LOGGER = LoggerFactory.getLogger(EswMsgLogInfoService.class);
	private static final String YYYY_MM_DD = "yyyy-MM-dd";
	private static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

	@Autowired
	private EswMsgLogInfoRepository eswMsgLogInfoRepository;
	
	@Transactional(readOnly=true)
	public DataTablesResponseDto<EswMsgLogInfoDto> getEswMsgLogInfoList(
			final EswMsgLogInfoSearchDto eswMsgLogInfoSearchDto) throws BizServiceException {
		Pageable pageRequest = PaginationUtil.buildPageRequest(eswMsgLogInfoSearchDto);
		Specification<EswMsgLogPo> specification = new Specification<EswMsgLogPo>() {
			@Override
			public Predicate toPredicate(Root<EswMsgLogPo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				List<Expression<Boolean>> expressions = predicate.getExpressions();
				if (null != eswMsgLogInfoSearchDto) {
					String jnlNo = eswMsgLogInfoSearchDto.getJnlNo();
					String trxType = eswMsgLogInfoSearchDto.getTrxType();
					String msgType = eswMsgLogInfoSearchDto.getMsgType();
					String msgBody = eswMsgLogInfoSearchDto.getMsgBody();
					String createTs = eswMsgLogInfoSearchDto.getCreateTs();
					if (StringUtils.isNotBlank(jnlNo)) {
						expressions.add(cb.like(cb.lower(root.<String> get("jnlNo")), "%" + jnlNo.trim().toLowerCase() + "%"));
					}
					if (StringUtils.isNotBlank(trxType)) {
						expressions.add(cb.equal(root.<EEbcCommandType> get("trxType"), trxType));
					}
					if (StringUtils.isNotBlank(msgType)) {
						expressions.add(cb.equal(root.<EEbcMsgType> get("msgType"), msgType));
					}
					if (StringUtils.isNotBlank(msgBody)) {
						expressions.add(cb.like(cb.lower(root.<String> get("msgBody")), "%" + msgBody.trim().toLowerCase() +"%"));
					}
					if (StringUtils.isNotBlank(createTs)) {
						try {
							expressions.add(cb.greaterThanOrEqualTo(root.<Date> get("createTs"), DateUtils.getStartDate(DateUtils.getDate(createTs, YYYY_MM_DD))));
							expressions.add(cb.lessThanOrEqualTo(root.<Date> get("createTs"), DateUtils.getEndDate(DateUtils.getDate(createTs, YYYY_MM_DD))));
						} catch (ParseException e) {
							throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "", e);
						}
					}
				}
				return predicate;
			}
		};
		Page<EswMsgLogPo> pages = eswMsgLogInfoRepository.findAll(specification, pageRequest);
		DataTablesResponseDto<EswMsgLogInfoDto> eswMsgLogInfoView = new DataTablesResponseDto<EswMsgLogInfoDto>();
		List<EswMsgLogInfoDto> eswMsgLogInfoList = new ArrayList<EswMsgLogInfoDto>();
		for (EswMsgLogPo eswMsgLogPo : pages) {
			EswMsgLogInfoDto eswMsgLogInfoDto = ConverterService.convert(eswMsgLogPo, EswMsgLogInfoDto.class);
			if (null != eswMsgLogPo.getCreateTs()) {
				eswMsgLogInfoDto.setCreateTs(DateUtils.formatDate(eswMsgLogPo.getCreateTs(), YYYY_MM_DD_HH_MM_SS));
			}
			eswMsgLogInfoList.add(eswMsgLogInfoDto);
		}
		eswMsgLogInfoView.setEcho(eswMsgLogInfoSearchDto.getEcho());
		eswMsgLogInfoView.setData(eswMsgLogInfoList);
		eswMsgLogInfoView.setTotalDisplayRecords(pages.getTotalElements());
		eswMsgLogInfoView.setTotalRecords(pages.getTotalElements());
		return eswMsgLogInfoView;
	}
}
