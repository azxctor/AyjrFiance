/*
 * Project Name: kmfex-platform
 * File Name: FreezeReserveDtlService.java
 * Class Name: FreezeReserveDtlService
 *
 * Copyright 2014 Hengtian Software Inc
 *
 * Licensed under the Hengtiansoft
 *
 * http://www.hengtiansoft.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hengxin.platform.fund.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.util.PaginationUtil;
import com.hengxin.platform.fund.dto.FreezeReserveDtlDto;
import com.hengxin.platform.fund.dto.FreezeReserveDtlSearchDto;
import com.hengxin.platform.fund.entity.AcctPo;
import com.hengxin.platform.fund.entity.FreezeReserveDtlPo;
import com.hengxin.platform.fund.enums.EFnrStatus;
import com.hengxin.platform.fund.enums.EFundUseType;
import com.hengxin.platform.fund.repository.AcctRepository;
import com.hengxin.platform.fund.repository.FreezeReserveDtlRepository;
import com.hengxin.platform.fund.util.AmtUtils;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.member.enums.EMemberType;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.entity.UserPo;
import com.hengxin.platform.security.enums.EBizRole;

/**
 * Class Name: FreezeReserveDtlService
 * 
 * @author tingwang
 * 
 */
@Service
public class FreezeReserveDtlService {
	
	@Autowired
    SecurityContext securityContext;

    @Autowired
    AcctRepository acctRepository;

    @Autowired
    FreezeReserveDtlRepository freezeReserveDtlRepository;

    public FreezeReserveDtlPo getByJnlNo(String jnlNo) {
        return freezeReserveDtlRepository.findFreezeReserveDtlByJnlNo(jnlNo);
    }

    public BigDecimal getUnFnrAbleAmt(String jnlNo) {
        BigDecimal ableAmt = BigDecimal.ZERO;
        if (StringUtils.isNotBlank(jnlNo)) {
            FreezeReserveDtlPo dtl = freezeReserveDtlRepository.findFreezeReserveDtlByJnlNo(jnlNo);
            if (dtl != null) {
                EFnrStatus status = dtl.getStatus();
                if (EFnrStatus.ACTIVE == status) {
                    ableAmt = AmtUtils.processNegativeAmt(dtl.getTrxAmt(), BigDecimal.ZERO);
                }
            }
        }
        return ableAmt;
    }

    public boolean isfnrDtlActive(String jnlNo) {
        boolean bool = false;
        if (StringUtils.isBlank(jnlNo)) {
            return bool;
        }
        FreezeReserveDtlPo dtl = freezeReserveDtlRepository.findFreezeReserveDtlByJnlNo(jnlNo);
        if (dtl != null) {
            if (EFnrStatus.ACTIVE == dtl.getStatus()) {
                bool = true;
            }
        }
        return bool;
    }
    
    /**
     * 
    * Description: 获取会员冻结保留明细
    *
    * @param searchDto
    * @param userId
    * @return
     */
    @Transactional(readOnly = true)
    public DataTablesResponseDto<FreezeReserveDtlDto> getUserFreezeReserveDetails(
            final FreezeReserveDtlSearchDto searchDto, final String userId) {

        final String acctNo = acctRepository.findByUserId(userId).getAcctNo();

        Specification<FreezeReserveDtlPo> specification = new Specification<FreezeReserveDtlPo>() {
            @Override
            public Predicate toPredicate(Root<FreezeReserveDtlPo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                expressions.add(cb.equal(root.get("acctNo"), acctNo));
                expressions.add(cb.equal(root.get("status"), EFnrStatus.ACTIVE));
                if (null != searchDto.getFromDate()) {
                    try {
                        expressions.add(cb.greaterThanOrEqualTo(root.get("effectDt").as(Date.class),
                                DateUtils.getStartDate(searchDto.getFromDate())));
                    } catch (ParseException e) {
                        throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR);
                    }
                }
                if (null != searchDto.getToDate()) {
                    try {
                        expressions.add(cb.lessThanOrEqualTo(root.get("effectDt").as(Date.class),
                                DateUtils.getEndDate(searchDto.getToDate())));
                    } catch (ParseException e) {
                        throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR);
                    }
                }
                if (null != searchDto.getFundUseType() && !searchDto.getFundUseType().equals(EFundUseType.INOUTALL)) {
                    expressions.add(cb.equal(root.get("useType"), searchDto.getFundUseType()));
                }
                return predicate;
            }

        };
        Pageable pageable = PaginationUtil.buildPageRequest(searchDto);
        Page<FreezeReserveDtlPo> pos = freezeReserveDtlRepository.findAll(specification, pageable);
        return packUserDto(pos);
    }
    
    @SuppressWarnings("unused")
	private List<String> getIncludeRoles(EMemberType userType){
    	List<String> roles = new ArrayList<String>();
    	if(userType==null||userType==EMemberType.NULL){
    		roles = Arrays.asList(
    				EBizRole.INVESTER.getCode(), 
    				EBizRole.FINANCIER.getCode(), 
    				EBizRole.PROD_SERV.getCode());
    	}else{
    		switch(userType){
    			case INVESTOR:
    				roles = Arrays.asList(EBizRole.INVESTER.getCode());
    				break;
    			case FINANCER:
    				roles = Arrays.asList(EBizRole.FINANCIER.getCode());
    				break;
    			case INVESTOR_FINANCER:
    				roles = Arrays.asList(
    	    				EBizRole.INVESTER.getCode(), 
    	    				EBizRole.FINANCIER.getCode());
    				break;
    			case PRODUCTSERVICE:
    				roles = Arrays.asList(EBizRole.PROD_SERV.getCode());
    				break;
    			default:
    	    		roles = Arrays.asList(
    	    				EBizRole.INVESTER.getCode(), 
    	    				EBizRole.FINANCIER.getCode(), 
    	    				EBizRole.PROD_SERV.getCode());
    		}
    	}
    	return roles;
    }
    
    @SuppressWarnings("unused")
	private List<String> getExcludeRoles(List<String> inBizRoles){
    	List<String> notInBizRoles = new ArrayList<String>();
    	if(inBizRoles.size()>1){
    		notInBizRoles.add("-9999");
    	}else{
    		String roleName = inBizRoles.get(0);
    		if(StringUtils.endsWithIgnoreCase(roleName, EBizRole.INVESTER.getCode())){
    			notInBizRoles.add(EBizRole.FINANCIER.getCode());
    		}else if(StringUtils.endsWithIgnoreCase(roleName, EBizRole.FINANCIER.getCode())){
    			notInBizRoles.add(EBizRole.INVESTER.getCode());
    		}else{
    			notInBizRoles.add("-9999");
    		}
    	}
    	return notInBizRoles;
    }
    
    public Object[] getSumData(FreezeReserveDtlSearchDto searchDto){
    	String acctNo = searchDto.getAcctNo();
    	if(StringUtils.isBlank(acctNo)){
    		acctNo = "";
    	}
    	String userName = searchDto.getName();
    	if(StringUtils.isBlank(userName)){
    		userName = "";
    	}
    	
    	EFundUseType euseType = searchDto.getFundUseType();
    	String useType = null;
    	if(euseType==null||euseType==EFundUseType.INOUTALL){
    		useType = "";
    	}else{
    		useType = euseType.getCode();
    	}
    	Date fromDate = searchDto.getFromDate();
    	String startDate = "";
    	if(fromDate!=null){
    		startDate = DateUtils.formatDate(fromDate, "yyyy-MM-dd");
    	}
    	Date toDate = searchDto.getToDate();
    	String endDate = "";
    	if(toDate!=null){
    		endDate = DateUtils.formatDate(toDate, "yyyy-MM-dd");
    	}
    	EFnrStatus fnrStatus = searchDto.getStatus();
    	String status = "";
    	if(!securityContext.isPlatformUser()){
    		status = EFnrStatus.ACTIVE.getCode();
    	}else{
    		if(fnrStatus!=null&&fnrStatus!=EFnrStatus.ALL){
    			status = fnrStatus.getCode();
    		}
    	}
    	List<Object[]> objs = freezeReserveDtlRepository.getSumData(acctNo, userName, 
    			status, useType, startDate, endDate);
    	return objs.get(0);
    }
    

    /**
     * Description: pack 会员冻结保留明细
     * 
     * @param pos
     * @return
     */

    private DataTablesResponseDto<FreezeReserveDtlDto> packUserDto(Page<FreezeReserveDtlPo> pos) {
        DataTablesResponseDto<FreezeReserveDtlDto> result = new DataTablesResponseDto<FreezeReserveDtlDto>();
        List<FreezeReserveDtlDto> frezzeReserveDetailsDtos = new ArrayList<FreezeReserveDtlDto>();
        for (FreezeReserveDtlPo po : pos) {
            FreezeReserveDtlDto dto = new FreezeReserveDtlDto();
            dto = ConverterService.convert(po, dto);
            frezzeReserveDetailsDtos.add(dto);
        }
        result.setData(frezzeReserveDetailsDtos);
        result.setTotalDisplayRecords(pos.getTotalElements());
        result.setTotalRecords(pos.getTotalElements());
        return result;
    }

    /**
     * 
    * Description: 获取平台冻结保留明细
    *
    * @param searchDto
    * @return
     */
    @Transactional(readOnly = true)
    public DataTablesResponseDto<FreezeReserveDtlDto> getAllFrezzeReserveDetails(
            final FreezeReserveDtlSearchDto searchDto) {
        Specification<FreezeReserveDtlPo> specification = new Specification<FreezeReserveDtlPo>() {

            @Override
            public Predicate toPredicate(Root<FreezeReserveDtlPo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                if (null != searchDto.getFromDate()) {
                    try {
                        expressions.add(cb.greaterThanOrEqualTo(root.get("effectDt").as(Date.class),
                                DateUtils.getStartDate(searchDto.getFromDate())));
                    } catch (ParseException e) {
                        throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR);
                    }
                }
                if (null != searchDto.getToDate()) {
                    try {
                        expressions.add(cb.lessThanOrEqualTo(root.get("effectDt").as(Date.class),
                                DateUtils.getEndDate(searchDto.getToDate())));
                    } catch (ParseException e) {
                        throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR);
                    }
                }
                if (StringUtils.isNotBlank(searchDto.getAcctNo())) {
                    expressions.add(cb.equal(root.get("acctNo"), searchDto.getAcctNo()));
                }
                if (StringUtils.isNotBlank(searchDto.getName())) {
                    Join<FreezeReserveDtlPo, AcctPo> join = root.join("acctPo", JoinType.INNER);
                    Join<AcctPo, UserPo> join2 = join.join("userPo", JoinType.INNER);
                    expressions.add(cb.equal(join2.get("name"), searchDto.getName()));
                }
                if (null != searchDto.getFundUseType() && !searchDto.getFundUseType().equals(EFundUseType.INOUTALL)) {
                    expressions.add(cb.equal(root.get("useType"), searchDto.getFundUseType()));
                }
                EFnrStatus fnrStatus = searchDto.getStatus();
                if(fnrStatus!=null&&fnrStatus!=EFnrStatus.ALL){
                	expressions.add(cb.equal(root.get("status"), fnrStatus));
        		}
                /*
                EMemberType memberType = searchDto.getUserType();
                if (memberType != null && memberType != EMemberType.NULL) {
                    List<String> bizRoles = transferMemberType2BizRoleNames(memberType);
                    if (!bizRoles.isEmpty()) {
                        for (int i = 0; i < bizRoles.size(); i++) {
                            String roleName = bizRoles.get(i);
                            if (StringUtils.isBlank(roleName)) {
                                continue;
                            }
                            Subquery<AcctPo> sq = query.subquery(AcctPo.class);
                            Root<AcctPo> sqr = sq.from(AcctPo.class);
                            Join<AcctPo, UserPo> join1 = sqr.join("userPo");
                            Join<UserPo, UserRole> join2 = join1.join("userRole");
                            Join<UserRole, RolePo> join3 = join2.join("rolePo");
                            sq.select(sqr).where(
                                    cb.and(cb.equal(sqr.<String> get("acctNo"), root.<String> get("acctNo")),
                                            cb.equal(join3.<String> get("name"), roleName)));
                            if (i == 0 || i != bizRoles.size() - 1) {
                                expressions.add(cb.exists(sq));
                            } else {
                                expressions.add(cb.not(cb.exists(sq)));
                            }
                        }
                    }
                }
                */
                return predicate;
            }

        };
        Pageable pageable = PaginationUtil.buildPageRequest(searchDto);
        Page<FreezeReserveDtlPo> pos = freezeReserveDtlRepository.findAll(specification, pageable);
        return packPlatformDto(pos);
    }

    /**
     * list中最后一个元素是not exist
     * 
     * @param memberType
     * @return
     */
    @SuppressWarnings("unused")
	private List<String> transferMemberType2BizRoleNames(EMemberType memberType) {
        List<String> roles = null;
        switch (memberType) {
        case INVESTOR:
            roles = Arrays.asList(EBizRole.INVESTER.getCode(), EBizRole.FINANCIER.getCode());
            break;
        case FINANCER:
            roles = Arrays.asList(EBizRole.FINANCIER.getCode(), EBizRole.INVESTER.getCode());
            break;
        case INVESTOR_FINANCER:
            roles = Arrays.asList(EBizRole.INVESTER.getCode(), EBizRole.FINANCIER.getCode(), "-9999");
            break;
        case PRODUCTSERVICE:
            roles = Arrays.asList(EBizRole.PROD_SERV.getCode(), "-9999");
            break;
        default:
            ;
        }
        return roles;
    }

    @SuppressWarnings("unused")
	private EMemberType transferBizRole2MemberType(List<EBizRole> bizRoles) {
        EMemberType result = EMemberType.NULL;
        if (bizRoles.isEmpty()) {
            return result;
        }
        if (bizRoles.contains(EBizRole.FINANCIER)) {
            result = EMemberType.FINANCER;
        }
        if (bizRoles.contains(EBizRole.INVESTER)) {
            result = EMemberType.INVESTOR;
        }
        if (bizRoles.contains(EBizRole.PROD_SERV)) {
            result = EMemberType.PRODUCTSERVICE;
        }
        if (bizRoles.contains(EBizRole.INVESTER) && bizRoles.contains(EBizRole.FINANCIER)) {
            result = EMemberType.INVESTOR_FINANCER;
        }
        return result;
    }

    /**
     * Description: pack 平台冻结保留明细
     * 
     * @param pos
     * @return
     */

    private DataTablesResponseDto<FreezeReserveDtlDto> packPlatformDto(Page<FreezeReserveDtlPo> pos) {
        DataTablesResponseDto<FreezeReserveDtlDto> result = new DataTablesResponseDto<FreezeReserveDtlDto>();
        List<FreezeReserveDtlDto> frezzeReserveDetailsDtos = new ArrayList<FreezeReserveDtlDto>();
        for (FreezeReserveDtlPo po : pos) {
            FreezeReserveDtlDto dto = new FreezeReserveDtlDto();
            dto = ConverterService.convert(po, dto);
            UserPo userPo = po.getAcctPo().getUserPo();
            dto.setName(userPo.getName());
//            List<UserRole> userRoles = userPo.getUserRole();
//            List<EBizRole> bizRoles = new ArrayList<EBizRole>();
//            EBizRole[] eRoles = EBizRole.values();
//            for (UserRole ur : userRoles) {
//                String code = ur.getRolePo().getName();
//                for (EBizRole er : eRoles) {
//                    if (StringUtils.equals(er.getCode(), code)) {
//                        bizRoles.add(er);
//                    }
//                }
//            }
//            dto.setType(transferBizRole2MemberType(bizRoles));
            frezzeReserveDetailsDtos.add(dto);
        }
        result.setData(frezzeReserveDetailsDtos);
        result.setTotalDisplayRecords(pos.getTotalElements());
        result.setTotalRecords(pos.getTotalElements());
        return result;
    }

}
