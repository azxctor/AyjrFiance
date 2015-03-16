/*
 * Project Name: kmfex-platform
 * File Name: FundDetailsService.java
 * Class Name: FundDetailsService
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

package com.hengxin.platform.account.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
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

import com.hengxin.platform.account.dto.AccountDetailsDto;
import com.hengxin.platform.account.dto.downstream.AccountDetailsSearchDto;
import com.hengxin.platform.account.dto.upstream.AccountBalDto;
import com.hengxin.platform.common.constant.LiteralConstant;
import com.hengxin.platform.common.dto.DataTablesRequestDto;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.consts.DictConsts;
import com.hengxin.platform.fund.entity.AcctPo;
import com.hengxin.platform.fund.entity.SubAcctPo;
import com.hengxin.platform.fund.entity.SubAcctTrxJnlPo;
import com.hengxin.platform.fund.enums.EAcctType;
import com.hengxin.platform.fund.enums.EFundUseType;
import com.hengxin.platform.fund.enums.ESubAcctNo;
import com.hengxin.platform.fund.enums.ESubAcctType;
import com.hengxin.platform.fund.repository.SubAcctRepository;
import com.hengxin.platform.fund.repository.SubAcctTrxJnlRepository;
import com.hengxin.platform.fund.service.AcctService;
import com.hengxin.platform.fund.service.FundAcctBalService;
import com.hengxin.platform.fund.service.SubAcctService;
import com.hengxin.platform.fund.service.support.BusinessAcctUtils;
import com.hengxin.platform.fund.util.AmtUtils;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.security.SecurityContext;

/**
 * Class Name: FundDetailsService.
 * 
 * @author congzhou
 * 
 */
@Service
public class AccountDetailsService {
    @Autowired
    AcctService acctService;
    @Autowired
    SubAcctService subAcctService;
    @Autowired
    SecurityContext securityContext;
    @Autowired
    SubAcctRepository subAcctRepository;
    @Autowired
    FundAcctBalService fundAcctBalService;
    @Autowired
    SubAcctTrxJnlRepository subAcctTrxJnlRepository;
    
    @Transactional(readOnly=true)
    public List<AccountDetailsDto> getAccountExcDetails(final AccountDetailsSearchDto searchDto,String userId) {
        // 通过 会员编号获取 会员综合账户
        AcctPo acctPo = acctService.getAcctByUserId(userId);
        if (acctPo == null) {
            throw new BizServiceException(EErrorCode.ACCT_NOT_EXIST);
        }

        final String acctNo = acctPo.getAcctNo();
        Specification<SubAcctTrxJnlPo> specification = new Specification<SubAcctTrxJnlPo>() {

            @Override
            public Predicate toPredicate(Root<SubAcctTrxJnlPo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                expressions.add(root.get("subAcctNo").in(Arrays.asList(ESubAcctNo.CURRENT.getCode(), ESubAcctNo.LOAN.getCode())));
                expressions.add(cb.equal(root.get("acctNo"), acctNo));
                if(!EFundUseType.INOUTALL.equals(searchDto.getUseType())){
                    expressions.add(cb.equal(root.get(LiteralConstant.USE_TYPE), searchDto.getUseType()));
                } else {
                	List<EFundUseType> dicts = getFilterDictsByRoles();
                    dicts.add(EFundUseType.CASH_REVERSE);
                    dicts.add(EFundUseType.RECHARGE_REVERSE);
                    expressions.add(root.get(LiteralConstant.USE_TYPE).in(dicts));
                }
               if (null != searchDto.getFromDate()) {
                    try {
                        expressions.add(cb.greaterThanOrEqualTo(root.get(LiteralConstant.TRX_DATE).as(Date.class),
                                DateUtils.getStartDate(searchDto.getFromDate())));
                    } catch (ParseException e) {
                        throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "", e);
                    }
                }
                if (null != searchDto.getToDate()) {
                    try {
                        expressions.add(cb.lessThanOrEqualTo(root.get(LiteralConstant.TRX_DATE).as(Date.class),
                                DateUtils.getEndDate(searchDto.getToDate())));
                    } catch (ParseException e) {
                        throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "", e);
                    }
                }
                return predicate;
            }
        };
        
        /**
         * Sort sort = new Sort("desc", "trxDt").and(new Sort("desc", "jnlNo"));
         */
        List<SubAcctTrxJnlPo> subAcctTrxJnlPos = subAcctTrxJnlRepository.findAll(specification);
        
        List<AccountDetailsDto> accountDetailsDtos = new ArrayList<AccountDetailsDto>();
        for (SubAcctTrxJnlPo subAcctTrxJnlPo : subAcctTrxJnlPos) {
            AccountDetailsDto accountDetailsDto = new AccountDetailsDto();
            accountDetailsDto.setTrxAmt(subAcctTrxJnlPo.getTrxAmt());
            accountDetailsDto.setTrxDt(subAcctTrxJnlPo.getTrxDt());
            accountDetailsDto.setPayRecvFlg(subAcctTrxJnlPo.getPayRecvFlg().getCode());
            accountDetailsDto.setTrxMemo(subAcctTrxJnlPo.getTrxMemo());
            accountDetailsDto.setUseType(subAcctTrxJnlPo.getUseType());
            accountDetailsDtos.add(accountDetailsDto);
        }
        return accountDetailsDtos;
        
  
    } 
    /**
     * 获取账户明细
     * 
     * @return
     */
    @Transactional(readOnly=true)
    public DataTablesResponseDto<AccountDetailsDto> getAccountDetails(final AccountDetailsSearchDto searchDto, String userId) {
        // 通过 会员编号获取 会员综合账户
        AcctPo acctPo = acctService.getAcctByUserId(userId);
        if (acctPo == null) {
            throw new BizServiceException(EErrorCode.ACCT_NOT_EXIST);
        }

        final String acctNo = acctPo.getAcctNo();

        Specification<SubAcctTrxJnlPo> specification = new Specification<SubAcctTrxJnlPo>() {

            @Override
            public Predicate toPredicate(Root<SubAcctTrxJnlPo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();   
                expressions.add(root.get("subAcctNo").in(Arrays.asList(ESubAcctNo.CURRENT.getCode(), ESubAcctNo.LOAN.getCode())));
                expressions.add(cb.equal(root.get("acctNo"), acctNo));
                if(!EFundUseType.INOUTALL.equals(searchDto.getUseType())){
                    expressions.add(cb.equal(root.get(LiteralConstant.USE_TYPE), searchDto.getUseType()));
                } else {
                	List<EFundUseType> dicts = getFilterDictsByRoles();
                    dicts.add(EFundUseType.CASH_REVERSE);
                    dicts.add(EFundUseType.RECHARGE_REVERSE);
                    expressions.add(root.get(LiteralConstant.USE_TYPE).in(dicts));
                }
                if(null != searchDto.getFromDate()) {
                    try {
                        expressions.add(cb.greaterThanOrEqualTo(root.get(LiteralConstant.TRX_DATE).as(Date.class), DateUtils.getStartDate(searchDto.getFromDate())));
                    } catch (ParseException e) {
                        throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "", e);
                    }
                }
                if(null != searchDto.getToDate()) {
                    try {
                        expressions.add(cb.lessThanOrEqualTo(root.get(LiteralConstant.TRX_DATE).as(Date.class), DateUtils.getEndDate(searchDto.getToDate())));
                    } catch (ParseException e) {
                        throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "", e);
                    }
                }
                if(null != searchDto.getCreateDate()){
                    expressions.add(cb.lessThanOrEqualTo(root.get("createTs").as(Date.class), searchDto.getCreateDate()));
                }
                return predicate;
            }
        };
        
        Pageable pageable = this.buildPageRequest(searchDto);
        Page<SubAcctTrxJnlPo> subAcctTrxJnlPos = subAcctTrxJnlRepository.findAll(specification, pageable);
        return packAccountDetailsDtos(subAcctTrxJnlPos);
    }
    
    /**
     * 获取账户明细
     * 
     * @return
     * @throws ServiceException
     */
    @Transactional(readOnly=true)
    public List<AccountDetailsDto> getAccountDetailsList(final AccountDetailsSearchDto searchDto, String userId) {
        // 通过 会员编号获取 会员综合账户
        AcctPo acctPo = acctService.getAcctByUserId(userId);
        if (acctPo == null) {
            throw new BizServiceException(EErrorCode.ACCT_NOT_EXIST);
        }

        final String acctNo = acctPo.getAcctNo();

        Specification<SubAcctTrxJnlPo> specification = new Specification<SubAcctTrxJnlPo>() {

            @Override
            public Predicate toPredicate(Root<SubAcctTrxJnlPo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();   
                expressions.add(root.get("subAcctNo").in(Arrays.asList(ESubAcctNo.CURRENT.getCode(), ESubAcctNo.LOAN.getCode())));
                expressions.add(cb.equal(root.get("acctNo"), acctNo));
                if(!EFundUseType.INOUTALL.equals(searchDto.getUseType())){
                    expressions.add(cb.equal(root.get(LiteralConstant.USE_TYPE), searchDto.getUseType()));
                } else {
                	List<EFundUseType> dicts = getFilterDictsByRoles();
                    dicts.add(EFundUseType.CASH_REVERSE);
                    dicts.add(EFundUseType.RECHARGE_REVERSE);
                    expressions.add(root.get(LiteralConstant.USE_TYPE).in(dicts));
                }
                if(null != searchDto.getFromDate()) {
                    try {
                        expressions.add(cb.greaterThanOrEqualTo(root.get(LiteralConstant.TRX_DATE).as(Date.class), DateUtils.getStartDate(searchDto.getFromDate())));
                    } catch (ParseException e) {
                        throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "", e);
                    }
                }
                if(null != searchDto.getToDate()) {
                    try {
                        expressions.add(cb.lessThanOrEqualTo(root.get(LiteralConstant.TRX_DATE).as(Date.class), DateUtils.getEndDate(searchDto.getToDate())));
                    } catch (ParseException e) {
                        throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "", e);
                    }
                }
                if(null != searchDto.getCreateDate()){
                    expressions.add(cb.lessThanOrEqualTo(root.get("createTs").as(Date.class), searchDto.getCreateDate()));
                }
               
                return predicate;
            }
        };
        List<SubAcctTrxJnlPo> subAcctTrxJnlPos = subAcctTrxJnlRepository.findAll(specification);
        
        List<AccountDetailsDto> accountDetailsDtos = new ArrayList<AccountDetailsDto>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (SubAcctTrxJnlPo subAcctTrxJnlPo : subAcctTrxJnlPos) {
            AccountDetailsDto accountDetailsDto = new AccountDetailsDto();
            accountDetailsDto.setTrxAmt(subAcctTrxJnlPo.getTrxAmt());
            accountDetailsDto.setTrxDt(subAcctTrxJnlPo.getTrxDt());
            accountDetailsDto.setPayRecvFlg(subAcctTrxJnlPo.getPayRecvFlg().getCode());
            accountDetailsDto.setTrxMemo(subAcctTrxJnlPo.getTrxMemo());
            accountDetailsDto.setUseType(subAcctTrxJnlPo.getUseType());
            accountDetailsDto.setCreateTs(sdf.format(subAcctTrxJnlPo.getCreateTs()));
            accountDetailsDto.setRelBizId(subAcctTrxJnlPo.getRelBizId());
            accountDetailsDtos.add(accountDetailsDto);
        }
        
        return accountDetailsDtos;
    }
    
    /**
     * 
    * Description: build cmb sign PageRequest
    *
    * @param requestDto
    * @return
     */
    private Pageable buildPageRequest(DataTablesRequestDto requestDto) {
        Sort sort = null;
        List<Integer> sortedColumns = requestDto.getSortedColumns();
        List<String> sortDirections = requestDto.getSortDirections();
        List<String> dataProps = requestDto.getDataProps();

        for (Integer item : sortedColumns) {
            String sortColumn = dataProps.get(item);
            int indexOf = 0;
            if (StringUtils.isNotBlank(sortColumn) && sortColumn.endsWith(".text")) {
                indexOf = sortColumn.lastIndexOf(".text");
            } else if (StringUtils.isNotBlank(sortColumn) && sortColumn.endsWith(".fullText")) {
                indexOf = sortColumn.lastIndexOf(".fullText");
            }
            if (indexOf > 0) {
                sortColumn = sortColumn.substring(0, indexOf);
            }
            String sortDir = sortDirections.get(0);
            sort = new Sort(Direction.fromString(sortDir), sortColumn);
            sort = sort.and(new Sort(Direction.fromString(sortDir), "jnlNo"));
        }

        PageRequest page = new PageRequest(requestDto.getDisplayStart() / requestDto.getDisplayLength(),
                requestDto.getDisplayLength(), sort);
        return page;
    }
    
    /**
     * 
    * Description: 获取最近十条账户明细
    *
    * @param userId
    * @return
     */
    @Transactional(readOnly=true)
    public List<AccountDetailsDto> getLastAccountDetails(String userId) {
        // 通过 会员编号获取 会员综合账户
        AcctPo acctPo = acctService.getAcctByUserId(userId);
        
        if (acctPo == null) {
            throw new BizServiceException(EErrorCode.ACCT_NOT_EXIST);
        }

        final String acctNo = acctPo.getAcctNo();

        List<EFundUseType> dicts = getFilterDictsByRoles();
        
        List<String> typeList = new ArrayList<String>();
        
        for(EFundUseType type :dicts) {
            typeList.add(type.getCode());
        }
        
        List<SubAcctTrxJnlPo> subAcctTrxJnlPos = subAcctTrxJnlRepository.getLastTenByAcct(acctNo,Arrays.asList(ESubAcctNo.CURRENT.getCode(), ESubAcctNo.LOAN.getCode()), typeList);
        
        List<AccountDetailsDto> accountDetailsDtos = new ArrayList<AccountDetailsDto>();
        for (SubAcctTrxJnlPo subAcctTrxJnlPo : subAcctTrxJnlPos) {
            AccountDetailsDto accountDetailsDto = new AccountDetailsDto();
            accountDetailsDto.setTrxAmt(subAcctTrxJnlPo.getTrxAmt());
            accountDetailsDto.setTrxDt(subAcctTrxJnlPo.getTrxDt());
            accountDetailsDto.setPayRecvFlg(subAcctTrxJnlPo.getPayRecvFlg().getCode());
            accountDetailsDto.setTrxMemo(subAcctTrxJnlPo.getTrxMemo());
            accountDetailsDto.setUseType(subAcctTrxJnlPo.getUseType());
            accountDetailsDtos.add(accountDetailsDto);
        }
        return accountDetailsDtos;
    }
    
    /**
    *   包装为返回Dto
    * @param subAcctTrxJnlPos
    * @return
    */
    	
    private DataTablesResponseDto<AccountDetailsDto> packAccountDetailsDtos(Page<SubAcctTrxJnlPo> subAcctTrxJnlPos) {
    	//TODO 
        DataTablesResponseDto<AccountDetailsDto> result = new DataTablesResponseDto<AccountDetailsDto>();
        List<AccountDetailsDto> accountDetailsDtos = new ArrayList<AccountDetailsDto>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (SubAcctTrxJnlPo subAcctTrxJnlPo : subAcctTrxJnlPos) {
            AccountDetailsDto accountDetailsDto = new AccountDetailsDto();
            accountDetailsDto.setTrxAmt(subAcctTrxJnlPo.getTrxAmt());
            accountDetailsDto.setTrxDt(subAcctTrxJnlPo.getTrxDt());
            accountDetailsDto.setPayRecvFlg(subAcctTrxJnlPo.getPayRecvFlg().getCode());
            accountDetailsDto.setTrxMemo(subAcctTrxJnlPo.getTrxMemo());
            accountDetailsDto.setUseType(subAcctTrxJnlPo.getUseType());
            accountDetailsDto.setCreateTs(sdf.format(subAcctTrxJnlPo.getCreateTs()));
            accountDetailsDtos.add(accountDetailsDto);
        }
        result.setData(accountDetailsDtos);
        result.setTotalDisplayRecords(subAcctTrxJnlPos.getTotalElements());
        result.setTotalRecords(subAcctTrxJnlPos.getTotalElements());
        return result;
    }

    public AccountBalDto getCurrentAcctBal(String userId) {
        AccountBalDto dto = new AccountBalDto();
        if (StringUtils.isBlank(userId)) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR);
        }
        AcctPo acct = acctService.getAcct(userId, EAcctType.DEBT);
        if (acct == null) {
            throw new BizServiceException(EErrorCode.ACCT_NOT_EXIST);
        }
        SubAcctPo subAcct = subAcctService.getSubAcct(acct.getAcctNo(),
                BusinessAcctUtils.getSubAcctNoBySubAcctType(ESubAcctType.CURRENT.getCode()));
        if (subAcct == null) {
            throw new BizServiceException(EErrorCode.SUB_ACCT_NOT_EXIST);
        }
        return dto;
    }
    	
    public AccountBalDto getCurrentAcctCashableAmt(String userId) {
        AccountBalDto dto = new AccountBalDto();
        if (StringUtils.isBlank(userId)) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR);
        }
        BigDecimal amt = fundAcctBalService.getUserCashableAmt(userId);
        dto.setCashableAmt(AmtUtils.processNegativeAmt(amt, BigDecimal.ZERO));
        return dto;
    }
    
    public List<EFundUseType> getFilterDictsByRoles(){
    	
    	List<EFundUseType> list = new ArrayList<EFundUseType>();
    	List<EFundUseType> dicts = null;
    	if(securityContext.isPlatformUser()){
    		dicts = DictConsts.PLATFORM_IN_OUT_USETYPE;
    	}else{
    		Map<String,EFundUseType> dictsMap = new HashMap<String, EFundUseType>(); 
    		if(securityContext.isInvestor()){
    			this.putInDictsMap(dictsMap, DictConsts.INVS_IN_OUT_USETYPE);
    		}
    		if(securityContext.isFinancier()){
    			this.putInDictsMap(dictsMap, DictConsts.FNCR_IN_OUT_USETYPE);
    		}
    		Collection<EFundUseType> clt = dictsMap.values();
    		if(!clt.isEmpty()){
        		ArrayList<EFundUseType> arrayList = new ArrayList<EFundUseType>();
    			arrayList.addAll(clt);
    			dicts = arrayList;
    		}
    		if(dicts==null||dicts.isEmpty()){
    			dicts = DictConsts.IN_OUT_USETYPE;
    		}
    	}
		Collections.sort(dicts, new Comparator<EFundUseType>(){
			 @Override
			 public int compare(EFundUseType obj1, EFundUseType obj2){
				 return obj1.getCode().compareToIgnoreCase(obj2.getCode());
			 }
		});
		Iterator<EFundUseType> iter = dicts.iterator();
		while(iter.hasNext()){
			list.add(iter.next());
		}
    	return list;
    }
    
    private void putInDictsMap(Map<String,EFundUseType> dictsMap, List<EFundUseType> dicts){
    	if(dicts!=null&&!dicts.isEmpty()){
    		for(EFundUseType ut:dicts){
    			dictsMap.put(ut.getCode(), ut);
    		}
    	}
    }
    
}
