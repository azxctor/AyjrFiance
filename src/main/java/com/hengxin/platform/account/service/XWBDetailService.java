/*
 * Project Name: kmfex-platform
 * File Name: XWBDetailService.java
 * Class Name: XWBDetailService
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.account.dto.biz.req.XWBTransferReq;
import com.hengxin.platform.account.dto.downstream.XWBTrxHistorySearchDto;
import com.hengxin.platform.account.dto.upstream.XWBOverviewDto;
import com.hengxin.platform.account.enums.EXWBTradeType;
import com.hengxin.platform.batch.service.JobWorkService;
import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.dto.DataTablesRequestDto;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.entity.id.IdUtil;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.domain.SubAcctTrxJnl;
import com.hengxin.platform.fund.dto.biz.req.atom.InternalAcctTransferInfo;
import com.hengxin.platform.fund.entity.AcctPo;
import com.hengxin.platform.fund.entity.SubAcctPo;
import com.hengxin.platform.fund.entity.SubAcctTrxJnlPo;
import com.hengxin.platform.fund.entity.converter.SubAcctTrxJnl2XWBDetailConverter;
import com.hengxin.platform.fund.enums.EFundPayRecvFlag;
import com.hengxin.platform.fund.enums.EFundUseType;
import com.hengxin.platform.fund.enums.ESubAcctType;
import com.hengxin.platform.fund.service.AcctService;
import com.hengxin.platform.fund.service.SubAcctService;
import com.hengxin.platform.fund.service.SubAcctTrxJnlService;
import com.hengxin.platform.fund.service.atom.FundAcctService;
import com.hengxin.platform.fund.service.support.BusinessAcctUtils;
import com.hengxin.platform.fund.util.AmtUtils;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.security.service.UserService;

/**
 * Class Name: XWBDetailService Description: TODO
 * 
 * @author tingwang
 * 
 */
@Service
public class XWBDetailService {

    @Autowired
    UserService userService;

    @Autowired
    AcctService acctService;

    @Autowired
    SubAcctService subAcctService;

    @Autowired
    SubAcctTrxJnlService subAcctTrxJnlService;

    @Autowired
    FundAcctService fundAcctService;
    
    @Autowired
    JobWorkService jobWorkService;

    /**
     * 获取小微宝概览 Description: TODO
     * 
     * @param userId
     * @return
     */
    public XWBOverviewDto getXWBOverview(String userId) {
        XWBOverviewDto dto = new XWBOverviewDto();
        if (StringUtils.isBlank(userId)) {
            throw new BizServiceException(EErrorCode.ACCT_USERID_NOT_EXIST);
        }
        AcctPo acct = acctService.getAcctByUserId(userId);
        if (acct == null) {
            throw new BizServiceException(EErrorCode.ACCT_NOT_EXIST);
        }
        SubAcctPo subAcct = subAcctService.getSubAcct(acct.getAcctNo(),
                BusinessAcctUtils.getSubAcctNoBySubAcctType(ESubAcctType.XWB.getCode()));
        if (subAcct == null) {
            throw new BizServiceException(EErrorCode.SUB_ACCT_NOT_EXIST);
        }
        BigDecimal bal = AmtUtils.processNegativeAmt(subAcct.getBal(), BigDecimal.ZERO);
        BigDecimal profit = AmtUtils.processNegativeAmt(
                subAcctTrxJnlService.getXWBToalProfit(subAcct.getAcctNo(), subAcct.getSubAcctNo()), BigDecimal.ZERO);

        dto.setTotalAmount(bal.setScale(2, BigDecimal.ROUND_HALF_UP));
        dto.setTotalProfit(profit.setScale(2, BigDecimal.ROUND_HALF_UP));

        return dto;
    };

    /**
     * 获取小微宝交易日志列表 Description: TODO
     * 
     * @param searchDto
     * @return
     */
    public DataTablesResponseDto<SubAcctTrxJnl> getXWBTrxHistories(final XWBTrxHistorySearchDto searchDto, final String userId) {
        Pageable pageRequest = buildPageRequest(searchDto);
        Specification<SubAcctTrxJnlPo> specification = new Specification<SubAcctTrxJnlPo>() {

            @Override
            public Predicate toPredicate(Root<SubAcctTrxJnlPo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                if(!StringUtils.isBlank(userId)){
                    AcctPo acct = acctService.getAcctByUserId(userId);
                    if (acct == null) {
                    	throw new BizServiceException(EErrorCode.ACCT_NOT_EXIST);
                    }
                    String acctNo = acct.getAcctNo();
                    String subAcctNo = BusinessAcctUtils.getSubAcctNoBySubAcctType(ESubAcctType.XWB.getCode());
                    if (!StringUtils.isBlank(acctNo)) {
                        expressions.add(cb.equal(root.<String> get("acctNo"), acctNo));
                    }
                    if (!StringUtils.isBlank(subAcctNo)) {
                        expressions.add(cb.equal(root.<String> get("subAcctNo"), subAcctNo));
                    }
                }
                if (searchDto != null) {                   
                    if (searchDto.getTradeType() != null && searchDto.getTradeType() != EXWBTradeType.NULL && searchDto.getTradeType() != EXWBTradeType.ALL) {
                        if (searchDto.getTradeType() == EXWBTradeType.IN) {
                            expressions.add(cb.equal(root.<EFundUseType> get("useType"), EFundUseType.INTERNAL));
                            expressions.add(cb.equal(root.<EFundPayRecvFlag> get("payRecvFlg"), EFundPayRecvFlag.RECV));
                        } else if (searchDto.getTradeType() == EXWBTradeType.OUT) {
                            expressions.add(cb.equal(root.<EFundUseType> get("useType"), EFundUseType.INTERNAL));
                            expressions.add(cb.equal(root.<EFundPayRecvFlag> get("payRecvFlg"), EFundPayRecvFlag.PAY));
                        } else if (searchDto.getTradeType() == EXWBTradeType.PROFIT) {
                            expressions.add(cb.equal(root.<EFundUseType> get("useType"), EFundUseType.XWBINTEREST));
                            expressions.add(cb.equal(root.<EFundPayRecvFlag> get("payRecvFlg"), EFundPayRecvFlag.RECV));
                        }
                    }
                    try {
                        Date fromDate = searchDto.getFromDate();
                        Date toDate = searchDto.getToDate();

                        if (fromDate != null) {
                            expressions.add(cb.greaterThanOrEqualTo(root.<Date> get("trxDt"),
                                    DateUtils.getStartDate(fromDate)));
                        }
                        if (toDate != null) {
                            expressions
                                    .add(cb.lessThanOrEqualTo(root.<Date> get("trxDt"), DateUtils.getEndDate(toDate)));
                        }
                    } catch (ParseException e) {
                        throw new BizServiceException(EErrorCode.ACCT_DATE_PARSE_FAILED);
                    }
                }
                return predicate;
            }
        };
        Page<SubAcctTrxJnlPo> pages = subAcctTrxJnlService.getPageableSubAcctTrxJnls(specification, pageRequest);
        List<SubAcctTrxJnl> list = new ArrayList<SubAcctTrxJnl>();
        DataTablesResponseDto<SubAcctTrxJnl> dto = new DataTablesResponseDto<SubAcctTrxJnl>();
        if (pages.hasContent()) {
            for (SubAcctTrxJnlPo po : pages.getContent()) {
                po.setTrxAmt(po.getTrxAmt().setScale(2, BigDecimal.ROUND_HALF_UP));
                list.add(ConverterService.convert(po, SubAcctTrxJnl.class, SubAcctTrxJnl2XWBDetailConverter.class));
            }
        }
        dto.setEcho(searchDto.getEcho());
        dto.setData(list);
        dto.setTotalDisplayRecords(pages.getTotalElements());
        dto.setTotalRecords(pages.getTotalElements());
        return dto;
    };

    /**
     * 获取小微宝余额 Description: TODO
     * 
     * @param userId
     * @return
     */
    public BigDecimal getXWBBal(String userId) {
        if (StringUtils.isBlank(userId)) {
        	throw new BizServiceException(EErrorCode.ACCT_USERID_NOT_EXIST);
        }
        BigDecimal avlBal = AmtUtils.processNegativeAmt(fundAcctService.getUserSubAcctAvlAmt(userId, ESubAcctType.XWB),
                BigDecimal.ZERO);
        return avlBal.setScale(2, BigDecimal.ROUND_HALF_UP);
    };

    /**
     * 向小微宝转入金额 Description: TODO
     * 
     * @param userId
     * @param amount
     * @param password
     */
    @Transactional
    public void rechargeXWB(XWBTransferReq req) {
        if (jobWorkService.isBatchBizTaskProcessing()) {
            throw new BizServiceException(EErrorCode.BATCH_EXEC_PROCESSING_WAITING);
        }
        if (BigDecimal.ZERO.compareTo(req.getAmount()) > 0) {
            throw new BizServiceException(EErrorCode.ACCT_AMT_NOT_GREATER_ZERO);
        }
        /*
         * User user = userService.getUserBySignIn(req.getUserId(), req.getPassword()); if (user == null) { throw new
         * ServiceException("-1", "密码错误"); }
         */
        
//        AcctPo acctPo = acctService.getAcctByUserId(req.getUserId());
//        if(EAcctStatus.RECNOPAY.getCode().equals(acctPo.getAcctStatus())){
//        	throw new BizServiceException(EErrorCode.FUND_ACCT_CAN_NOT_PAY);
//        }
        
        InternalAcctTransferInfo infoReq = new InternalAcctTransferInfo();
        infoReq.setEventId(IdUtil.produce());
        infoReq.setUserId(req.getUserId());
        infoReq.setUseType(EFundUseType.INTERNAL);
        infoReq.setFromAcctType(ESubAcctType.CURRENT);
        infoReq.setToAcctType(ESubAcctType.XWB);
        infoReq.setTrxAmt(req.getAmount());
        infoReq.setTrxMemo(req.getMemo());
        if (req.getMemo() == null || StringUtils.isBlank(req.getMemo())) {
            infoReq.setTrxMemo("转入 " + req.getAmount() + "元");
        }
        infoReq.setBizId(IdUtil.produce());
        infoReq.setCurrOpId(req.getCurrentOpId());
        infoReq.setWorkDate(req.getWorkDate());
        fundAcctService.internalAcctTransferAmt(infoReq);
    };

    /**
     * 从小微宝转出金额 Description: TODO
     * 
     * @param userId
     * @param amount
     * @param password
     */
    @Transactional
    public void withdrawalXWB(XWBTransferReq req) {
        if (jobWorkService.isBatchBizTaskProcessing()) {
            throw new BizServiceException(EErrorCode.BATCH_EXEC_PROCESSING_WAITING);
        }
        if (BigDecimal.ZERO.compareTo(req.getAmount()) > 0) {
        	throw new BizServiceException(EErrorCode.ACCT_AMT_NOT_GREATER_ZERO);
        }
        /*
         * User user = userService.getUserBySignIn(req.getUserId(), req.getPassword()); if (user == null) { throw new
         * ServiceException("-1", "密码错误"); }
         */
        
//        AcctPo acctPo = acctService.getAcctByUserId(req.getUserId());
//        if(EAcctStatus.RECNOPAY.getCode().equals(acctPo.getAcctStatus())){
//        	throw new BizServiceException(EErrorCode.FUND_ACCT_CAN_NOT_PAY);
//        }
        
        BigDecimal avl = fundAcctService.getUserSubAcctAvlAmt(req.getUserId(), ESubAcctType.XWB);
        if (avl.compareTo(req.getAmount()) < 0) {
        	throw new BizServiceException(EErrorCode.ACCT_AMT_NOT_LESS_BAL);
        }
        InternalAcctTransferInfo infoReq = new InternalAcctTransferInfo();
        infoReq.setEventId(IdUtil.produce());
        infoReq.setUserId(req.getUserId());
        infoReq.setUseType(EFundUseType.INTERNAL);
        infoReq.setFromAcctType(ESubAcctType.XWB);
        infoReq.setToAcctType(ESubAcctType.CURRENT);
        infoReq.setTrxAmt(req.getAmount());
        infoReq.setTrxMemo(req.getMemo());
        if (req.getMemo() == null || StringUtils.isBlank(req.getMemo())) {
            infoReq.setTrxMemo("转出 " + req.getAmount() + "元");
        }
        infoReq.setBizId(IdUtil.produce());
        infoReq.setCurrOpId(req.getCurrentOpId());
        infoReq.setWorkDate(req.getWorkDate());
        fundAcctService.internalAcctTransferAmt(infoReq);
    }
    
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
            if (sortColumn.equals("trxDt")) {
                sortColumn = "createTs";
            }
            String sortDir = sortDirections.get(0);
            sort = new Sort(Direction.fromString(sortDir), sortColumn);
            sort = sort.and(new Sort(Direction.fromString(sortDir),"jnlNo"));
        }

        PageRequest page = new PageRequest(requestDto.getDisplayStart() / requestDto.getDisplayLength(),
                requestDto.getDisplayLength(), sort);
        return page;
    }
}
