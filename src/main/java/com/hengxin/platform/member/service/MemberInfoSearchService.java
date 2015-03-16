package com.hengxin.platform.member.service;

import java.lang.reflect.InvocationTargetException;
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

import org.apache.commons.beanutils.BeanUtils;
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
import com.hengxin.platform.common.enums.EOptionCategory;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.util.PaginationUtil;
import com.hengxin.platform.common.util.SystemDictUtil;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.member.domain.UserRole;
import com.hengxin.platform.member.dto.MemberInfoDto;
import com.hengxin.platform.member.dto.MemberInfoSearchDto;
import com.hengxin.platform.member.enums.EUserType;
import com.hengxin.platform.member.repository.UserRepository;
import com.hengxin.platform.security.entity.RolePo;
import com.hengxin.platform.security.entity.UserPo;

/**
 * 会员（游客，投资，融资等角色）信息查询
 * 
 * @author chenwulou
 *
 */
@Service
public class MemberInfoSearchService {

	// private static final Logger LOGGER =
	// LoggerFactory.getLogger(MemberInfoSearchService.class);
	private static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	private static final String YYYY_MM_DD = "yyyy-MM-dd";

	@Autowired
	private UserRepository userRepository;

	/**
	 * 分页查询会员数据
	 * 
	 * @param searchDto
	 * @return
	 * @throws BizServiceException
	 */
	@Transactional
	public DataTablesResponseDto<MemberInfoDto> getMemberInfoList(final MemberInfoSearchDto searchDto)
			throws BizServiceException {
		Pageable pageRequest = PaginationUtil.buildPageRequest(searchDto);
		Specification<UserPo> specification = new Specification<UserPo>() {
			@Override
			public Predicate toPredicate(Root<UserPo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Join<UserPo, UserRole> userPoJoinUserRole = root.join("userRole", JoinType.LEFT);
				Join<UserRole, RolePo> userRoleJoinRolePo = userPoJoinUserRole.join("rolePo", JoinType.LEFT);
				Predicate predicate = cb.conjunction();
				List<Expression<Boolean>> expressions = predicate.getExpressions();
				if (null != searchDto) {
					EUserType userType = searchDto.getUserType();
					String roleName = searchDto.getRoleName();
					String fromDate = searchDto.getFromDate();
					String toDate = searchDto.getToDate();
					if (StringUtils.isNotBlank(roleName) && !StringUtils.equals(roleName, "ALL")) {
						expressions.add(cb.equal(userRoleJoinRolePo.<String> get("name"), roleName));
					}
					if (null != userType && EUserType.ORGANIZATION.equals(userType)) {
						expressions.add(cb.equal(root.<EUserType> get("type"), userType));
					} else if (null != userType && EUserType.PERSON.equals(userType)) {
						expressions.add(cb.or(cb.isNull(root.<EUserType> get("type")),
								cb.equal(root.<EUserType> get("type"), EUserType.PERSON)));
					}
					if (StringUtils.isNotBlank(fromDate)) {
						try {
							expressions.add(cb.greaterThanOrEqualTo(root.<Date> get("createTs"),
									DateUtils.getStartDate(DateUtils.getDate(fromDate, YYYY_MM_DD))));
						} catch (ParseException e) {
							throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "", e);
						}
					}
					if (StringUtils.isNotBlank(toDate)) {
						try {
							expressions.add(cb.lessThanOrEqualTo(root.<Date> get("createTs"),
									DateUtils.getEndDate(DateUtils.getDate(toDate, YYYY_MM_DD))));
						} catch (ParseException e) {
							throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "", e);
						}
					}
				}
				return predicate;
			}
		};
		Page<UserPo> pages = userRepository.findAll(specification, pageRequest);
		DataTablesResponseDto<MemberInfoDto> memberInfoView = new DataTablesResponseDto<MemberInfoDto>();
		List<MemberInfoDto> memberInfoList = new ArrayList<MemberInfoDto>();
		for (UserPo userPo : pages) {
			/** userPo中数据copy到dto **/
			MemberInfoDto memberInfoDto = ConverterService.convert(userPo, MemberInfoDto.class);
			try {
				/** member中数据copy 到dto **/
				if (null != userPo.getMember()) {
					BeanUtils.copyProperties(memberInfoDto, userPo.getMember());
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			if (null != userPo.getCreateTs()) {
				memberInfoDto.setCreateTs(DateUtils.formatDate(userPo.getCreateTs(), YYYY_MM_DD_HH_MM_SS));
			}
			if (null != memberInfoDto.getRegion()) {
				memberInfoDto.setRegion(SystemDictUtil.getDictByCategoryAndCode(EOptionCategory.REGION,
						memberInfoDto.getRegion().getCode()));
			}
			memberInfoList.add(memberInfoDto);
		}
		memberInfoView.setEcho(searchDto.getEcho());
		memberInfoView.setData(memberInfoList);
		memberInfoView.setTotalDisplayRecords(pages.getTotalElements());
		memberInfoView.setTotalRecords(pages.getTotalElements());
		return memberInfoView;
	}

	/**
	 * 数据导出excel
	 * 
	 * @param searchDto
	 * @return
	 * @throws BizServiceException
	 */
	@Transactional
	public List<MemberInfoDto> getMemberInfoExcel(final MemberInfoSearchDto searchDto)
			throws BizServiceException {
		Specification<UserPo> specification = new Specification<UserPo>() {
			@Override
			public Predicate toPredicate(Root<UserPo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Join<UserPo, UserRole> userPoJoinUserRole = root.join("userRole", JoinType.LEFT);
				Join<UserRole, RolePo> userRoleJoinRolePo = userPoJoinUserRole.join("rolePo", JoinType.LEFT);
				Predicate predicate = cb.conjunction();
				List<Expression<Boolean>> expressions = predicate.getExpressions();
				if (null != searchDto) {
					EUserType userType = searchDto.getUserType();
					String roleName = searchDto.getRoleName();
					String fromDate = searchDto.getFromDate();
					String toDate = searchDto.getToDate();
					if (StringUtils.isNotBlank(roleName) && !StringUtils.equals(roleName, "ALL")) {
						expressions.add(cb.equal(userRoleJoinRolePo.<String> get("name"), roleName));
					}
					if (null != userType && EUserType.ORGANIZATION.equals(userType)) {
						expressions.add(cb.equal(root.<EUserType> get("type"), userType));
					} else if (null != userType && EUserType.PERSON.equals(userType)) {
						expressions.add(cb.or(cb.isNull(root.<EUserType> get("type")),
								cb.equal(root.<EUserType> get("type"), EUserType.PERSON)));
					}
					if (StringUtils.isNotBlank(fromDate)) {
						try {
							expressions.add(cb.greaterThanOrEqualTo(root.<Date> get("createTs"),
									DateUtils.getStartDate(DateUtils.getDate(fromDate, YYYY_MM_DD))));
						} catch (ParseException e) {
							throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "", e);
						}
					}
					if (StringUtils.isNotBlank(toDate)) {
						try {
							expressions.add(cb.lessThanOrEqualTo(root.<Date> get("createTs"),
									DateUtils.getEndDate(DateUtils.getDate(toDate, YYYY_MM_DD))));
						} catch (ParseException e) {
							throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "", e);
						}
					}
				}
				return predicate;
			}
		};
		List<UserPo> list = userRepository.findAll(specification);
		List<MemberInfoDto> memberInfoList = new ArrayList<MemberInfoDto>();
		for (UserPo userPo : list) {
			/** userPo中数据copy到dto **/
			MemberInfoDto memberInfoDto = ConverterService.convert(userPo, MemberInfoDto.class);
			try {
				/** member中数据copy 到dto **/
				if (null != userPo.getMember()) {
					BeanUtils.copyProperties(memberInfoDto, userPo.getMember());
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			if (null != userPo.getCreateTs()) {
				memberInfoDto.setCreateTs(DateUtils.formatDate(userPo.getCreateTs(), YYYY_MM_DD_HH_MM_SS));
			}
			if (null != memberInfoDto.getRegion()) {
				memberInfoDto.setRegion(SystemDictUtil.getDictByCategoryAndCode(EOptionCategory.REGION,
						memberInfoDto.getRegion().getCode()));
			}
			memberInfoList.add(memberInfoDto);
		}
		return memberInfoList;
	}

}
