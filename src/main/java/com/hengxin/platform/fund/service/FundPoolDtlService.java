package com.hengxin.platform.fund.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.dto.DataTablesRequestDto;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.fund.dto.FundPoolDtlDto;
import com.hengxin.platform.fund.dto.FundTrxJnlDto;
import com.hengxin.platform.fund.dto.FundTrxJnlSearchDto;
import com.hengxin.platform.fund.dto.PoolCheckDtlDto;
import com.hengxin.platform.fund.entity.SubAcctTrxJnlPo;
import com.hengxin.platform.fund.enums.ECashPool;
import com.hengxin.platform.fund.enums.EFundUseType;
import com.hengxin.platform.fund.repository.AcctRepository;
import com.hengxin.platform.fund.repository.BankTrxJnlRepository;
import com.hengxin.platform.fund.repository.SubAcctTrxJnlRepository;
import com.hengxin.platform.fund.util.AmtUtils;
import com.hengxin.platform.fund.util.DateUtils;

@Service
public class FundPoolDtlService {

    @Autowired
    private SubAcctTrxJnlRepository subAcctTrxJnlRepository;
    @Autowired
    private AcctRepository acctRepository;
    @Autowired
    private BankTrxJnlRepository bankTrxJnlRepository;

    private static List<EFundUseType> includeUseTypes = Arrays.asList(
            // EFundUseType.INTERNAL,
            // EFundUseType.ACCTASSETRESERVED, 
            EFundUseType.REPAYAMTOFERROR2EXCH, 
            EFundUseType.PROFITINVS2EXCH,
            EFundUseType.CMPNSAMT_PAY, 
            EFundUseType.FNCR_REPAYMENT_PENALTY, 
            EFundUseType.INTR_CMPNS_OVERDUE_FINE,
            EFundUseType.PRIN_CMPNS_OVERDUE_FINE, 
            EFundUseType.PREPAY_FINE, 
            EFundUseType.CMPNSAMT_FINE_REPAYMENT,
            EFundUseType.CMPNSAMT_REPAYMENT, 
            EFundUseType.TRADEEXPENSE_FINE, 
            EFundUseType.TRADEEXPENSE,
            EFundUseType.INTR_FINE_REPAYMENT, 
            EFundUseType.INTR_REPAYMENT, 
            EFundUseType.PRIN_FINE_REPAYMENT,
            EFundUseType.REVOKE_ORDER_FINE, 
            EFundUseType.PRIN_REPAYMENT, 
            EFundUseType.FNCR_REPAYMENT,
            EFundUseType.SUBSCRIBEFEE, 
            EFundUseType.TRANSFERCR, 
            EFundUseType.TRANSFERCR_FEE, 
            EFundUseType.FINANCING, 
            EFundUseType.SUBSCRIBE, 
            EFundUseType.TRANSFERPM, 
            EFundUseType.TRANSFERMM, 
			// EFundUseType.XWBINTEREST,
            EFundUseType.INTEREST, 
            EFundUseType.CHANGE_POOL, 
            EFundUseType.PAYFEE);

    /**
     * 获取资金池汇总明细（分页）
     * 
     * @param searchDto
     * @return
     */
    public DataTablesResponseDto<FundTrxJnlDto> getFundTrxJnlInfo(final FundTrxJnlSearchDto searchDto) {

        Specification<SubAcctTrxJnlPo> specification = new Specification<SubAcctTrxJnlPo>() {

            @Override
            public Predicate toPredicate(Root<SubAcctTrxJnlPo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                if (searchDto.getUseType() != null && searchDto.getUseType() != EFundUseType.INOUTALL) {
                    expressions.add(cb.equal(root.get("useType"), searchDto.getUseType()));
                } else {
                    expressions.add(root.<EFundUseType> get("useType").in(includeUseTypes));
                }
                if (null != searchDto.getAcctNo() && !"".equals(searchDto.getAcctNo())) {
                    expressions.add(cb.equal(root.get("acctNo"), searchDto.getAcctNo()));
                }
                if (null != searchDto.getCashPool() && ECashPool.ALL != searchDto.getCashPool()) {
                    expressions.add(cb.equal(root.get("cashPool"), searchDto.getCashPool()));
                }
                if (null != searchDto.getFromDate()) {
                    try {
                        expressions.add(cb.greaterThanOrEqualTo(root.get("trxDt").as(Date.class),
                                DateUtils.getStartDate(searchDto.getFromDate())));
                    } catch (ParseException e) {
                        throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR);
                    }
                }
                if (null != searchDto.getToDate()) {
                    try {
                        expressions.add(cb.lessThanOrEqualTo(root.get("trxDt").as(Date.class),
                                DateUtils.getEndDate(searchDto.getToDate())));
                    } catch (ParseException e) {
                        throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR);
                    }
                }
                return predicate;
            }
        };
        Pageable pageable = buildPageRequest(searchDto);

        Page<SubAcctTrxJnlPo> subAcctTrxJnlPos = subAcctTrxJnlRepository.findAll(specification, pageable);
        return packAccountDetailsDtos(subAcctTrxJnlPos);
    }

    /**
     * 获取资金池汇总明细（不分页）
     * 
     * @param searchDto
     * @return
     */
    public List<FundTrxJnlDto> getFundTrxJnlInfos(final FundTrxJnlSearchDto searchDto, String sortColumn, String sortDir) {

        Specification<SubAcctTrxJnlPo> specification = new Specification<SubAcctTrxJnlPo>() {

            @Override
            public Predicate toPredicate(Root<SubAcctTrxJnlPo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                if (searchDto.getUseType() != null && searchDto.getUseType() != EFundUseType.INOUTALL) {
                    expressions.add(cb.equal(root.get("useType"), searchDto.getUseType()));
                } else {
                    expressions.add(root.<EFundUseType> get("useType").in(includeUseTypes));
                }
                if (null != searchDto.getAcctNo() && !"".equals(searchDto.getAcctNo())) {
                    expressions.add(cb.equal(root.get("acctNo"), searchDto.getAcctNo()));
                }
                if (null != searchDto.getCashPool() && ECashPool.ALL != searchDto.getCashPool()) {
                    expressions.add(cb.equal(root.get("cashPool"), searchDto.getCashPool()));
                }
                if (null != searchDto.getFromDate()) {
                    try {
                        expressions.add(cb.greaterThanOrEqualTo(root.get("trxDt").as(Date.class),
                                DateUtils.getStartDate(searchDto.getFromDate())));
                    } catch (ParseException e) {
                        throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR);
                    }
                }
                if (null != searchDto.getToDate()) {
                    try {
                        expressions.add(cb.lessThanOrEqualTo(root.get("trxDt").as(Date.class),
                                DateUtils.getEndDate(searchDto.getToDate())));
                    } catch (ParseException e) {
                        throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR);
                    }
                }
                return predicate;
            }
        };

        Sort sort = null;
        if (StringUtils.isNotBlank(sortColumn) && StringUtils.isNotBlank(sortDir)) {
            sort = new Sort(Direction.fromString(sortDir), sortColumn);
            sort = sort.and(new Sort(Direction.fromString(sortDir), "jnlNo"));
        } else {
            sort = new Sort(Direction.fromString(sortDir), "jnlNo");
        }

        List<SubAcctTrxJnlPo> subAcctTrxJnlPos = subAcctTrxJnlRepository.findAll(specification, sort);

        List<FundTrxJnlDto> fundTrxJnlDtos = new ArrayList<FundTrxJnlDto>();
        Map<String, HashMap<String, String>> cache = new ConcurrentHashMap<String, HashMap<String, String>>();

        for (SubAcctTrxJnlPo subAcctTrxJnlPo : subAcctTrxJnlPos) {
            FundTrxJnlDto fundTrxJnlDto = ConverterService.convert(subAcctTrxJnlPo, FundTrxJnlDto.class);
            String userId = null;
            String userName = null;
            if (cache.containsKey(fundTrxJnlDto.getAcctNo())) {
                HashMap<String, String> dataMap = cache.get(fundTrxJnlDto.getAcctNo());
                userId = dataMap.get("userId");
                userName = dataMap.get("userName");
            } else {
                List<Object[]> dataList = acctRepository.getUserAcctInfoByAcctNo(fundTrxJnlDto.getAcctNo());
                Object[] objs = new Object[] { "", "" };
                if (dataList != null && !dataList.isEmpty()) {
                    objs = dataList.get(0);
                }
                userId = String.valueOf(objs[0]);
                userName = String.valueOf(objs[1]);
                HashMap<String, String> dataMap = new HashMap<String, String>();
                dataMap.put("userId", userId);
                dataMap.put("userName", userName);
                cache.put(fundTrxJnlDto.getAcctNo(), dataMap);
            }
            fundTrxJnlDto.setUserId(userId);
            fundTrxJnlDto.setUserName(userName);
            fundTrxJnlDtos.add(fundTrxJnlDto);
        }

        return fundTrxJnlDtos;
    }

    /**
     * 包装为返回Dto
     * 
     * @param subAcctTrxJnlPos
     * @return
     */
    private DataTablesResponseDto<FundTrxJnlDto> packAccountDetailsDtos(Page<SubAcctTrxJnlPo> subAcctTrxJnlPos) {
        DataTablesResponseDto<FundTrxJnlDto> result = new DataTablesResponseDto<FundTrxJnlDto>();
        List<FundTrxJnlDto> fundTrxJnlDtos = new ArrayList<FundTrxJnlDto>();
        Map<String, HashMap<String, String>> cache = new ConcurrentHashMap<String, HashMap<String, String>>();
        for (SubAcctTrxJnlPo subAcctTrxJnlPo : subAcctTrxJnlPos) {
            FundTrxJnlDto fundTrxJnlDto = ConverterService.convert(subAcctTrxJnlPo, FundTrxJnlDto.class);
            String userId = null;
            String userName = null;
            if (cache.containsKey(fundTrxJnlDto.getAcctNo())) {
                HashMap<String, String> dataMap = cache.get(fundTrxJnlDto.getAcctNo());
                userId = dataMap.get("userId");
                userName = dataMap.get("userName");
            } else {
                List<Object[]> dataList = acctRepository.getUserAcctInfoByAcctNo(fundTrxJnlDto.getAcctNo());
                Object[] objs = new Object[] { "", "" };
                if (dataList != null && !dataList.isEmpty()) {
                    objs = dataList.get(0);
                }
                userId = String.valueOf(objs[0]);
                userName = String.valueOf(objs[1]);
                HashMap<String, String> dataMap = new HashMap<String, String>();
                dataMap.put("userId", userId);
                dataMap.put("userName", userName);
                cache.put(fundTrxJnlDto.getAcctNo(), dataMap);
            }
            fundTrxJnlDto.setUserId(userId);
            fundTrxJnlDto.setUserName(userName);
            fundTrxJnlDtos.add(fundTrxJnlDto);
        }
        result.setData(fundTrxJnlDtos);
        result.setTotalDisplayRecords(subAcctTrxJnlPos.getTotalElements());
        result.setTotalRecords(subAcctTrxJnlPos.getTotalElements());
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
     * 获取资金池汇总参数
     * 
     * @param searchDto
     * @return
     */
    public FundPoolDtlDto getSumCashPooltrxAmt(FundTrxJnlSearchDto searchDto) {

        String fromDate = "";
        if (searchDto.getFromDate() != null) {
            fromDate = DateUtils.formatDate(searchDto.getFromDate(), "yyyy-MM-dd");
        }

        String toDate = "";
        if (searchDto.getToDate() != null) {
            toDate = DateUtils.formatDate(searchDto.getToDate(), "yyyy-MM-dd");
        }

        String workDateStr = DateUtils.formatDate(CommonBusinessUtil.getCurrentWorkDate(), "yyyy-MM-dd");

        String cashPool = "";
        if (searchDto.getCashPool() != null && ECashPool.ALL != searchDto.getCashPool()) {
            cashPool = searchDto.getCashPool().getCode();
        }

        List<String> useTypeCodes = null;
        if (searchDto.getUseType() != null && searchDto.getUseType() != EFundUseType.INOUTALL) {
            useTypeCodes = Arrays.asList(searchDto.getUseType().getCode());
        } else {
            useTypeCodes = this.getIncludeUseTypeCodes();
        }

        List<Object[]> dtlList = subAcctTrxJnlRepository.getCashPoolStockDtl(cashPool, searchDto.getAcctNo(), fromDate,
                toDate, useTypeCodes);

        FundPoolDtlDto dto = new FundPoolDtlDto();
        ECashPool ecashPool = ECashPool.CMB_COMMON;
        BigDecimal totalAmt = BigDecimal.ZERO;
        BigDecimal payAmt = BigDecimal.ZERO;
        BigDecimal recvAmt = BigDecimal.ZERO;
        BigDecimal cashRecvAmt = BigDecimal.ZERO;
        for (Object[] data : dtlList) {
            String pool = data[0] == null ? "" : String.valueOf(data[0]);
            if (StringUtils.isNotBlank(pool)) {
                ecashPool = EnumHelper.translate(ECashPool.class, pool);
            }
            totalAmt = totalAmt.add(AmtUtils.processNullAmt(data[1], BigDecimal.ZERO));
            payAmt = payAmt.add(AmtUtils.processNullAmt(data[2], BigDecimal.ZERO));
            recvAmt = recvAmt.add(AmtUtils.processNullAmt(data[3], BigDecimal.ZERO));
            cashRecvAmt = cashRecvAmt.add(AmtUtils.processNullAmt(data[4], BigDecimal.ZERO));
        }
        dto.setCashPool(ecashPool);
        if (dtlList.size() > 1) {
            dto.setCashPool(ECashPool.ALL);
        }

        BigDecimal totalBal = subAcctTrxJnlRepository.getTotalBal(cashPool, searchDto.getAcctNo(), fromDate, toDate,
                workDateStr);
        dto.setTotalAmt(totalAmt);
        dto.setPayAmt(payAmt);
        dto.setRecvAmt(recvAmt);
        dto.setCashRecvAmt(cashRecvAmt);
        dto.setTotalBal(totalBal);
        return dto;
    }

    private List<String> getIncludeUseTypeCodes() {
        List<String> codes = new ArrayList<String>();
        for (EFundUseType useType : includeUseTypes) {
            codes.add(useType.getCode());
        }
        return codes;
    }

    public List<EFundUseType> getFundPoolDtlUseTypes() {
        List<EFundUseType> dicts = includeUseTypes;
        Collections.sort(dicts, new Comparator<EFundUseType>() {
            @Override
            public int compare(EFundUseType obj1, EFundUseType obj2) {
                return obj1.getCode().compareToIgnoreCase(obj2.getCode());
            }
        });
        return dicts;
    }
    
    public List<PoolCheckDtlDto> getPoolCheck(String curr,String yest){
    	ArrayList<PoolCheckDtlDto> ls = new ArrayList<PoolCheckDtlDto>();
    	List<String> ps = this.bankTrxJnlRepository.getPool(curr,yest);
    	if(null!=ps){
    		PoolCheckDtlDto sum = new PoolCheckDtlDto();
    		sum.setPool("合计");
    		sum.setTotal(true);
    		for(int i=0;i<ps.size();i++){
    			String[] subs = ps.get(i).split("@");
    			PoolCheckDtlDto p = new PoolCheckDtlDto();
        		p.setAccDate(subs[0]);//会计日期
        		p.setPool(subs[1]);//资金池
        		p.setYestAmt(new BigDecimal(subs[2]));//昨日余额
        		p.setCurrRecv(new BigDecimal(subs[3]));//当日收
        		p.setCurrPay(new BigDecimal(subs[4]));//当日付
        		p.setCurrAmt(new BigDecimal(subs[5]));//当日余额
        		p.setCheckAmt(p.getYestAmt().add(p.getCurrRecv()).subtract(p.getCurrPay()));//核对金额
        		p.setMistake(p.getCurrAmt().subtract(p.getCheckAmt()));//差额=当日余额-核对金额
        		if(p.getMistake().doubleValue()==0){
        			p.setResult("√");
        		}else{
        			p.setResult("×");
        		}
        		sum.setYestAmt(sum.getYestAmt().add(p.getYestAmt()));//昨日余额，汇总
        		sum.setCurrRecv(sum.getCurrRecv().add(p.getCurrRecv()));//当日收，汇总
        		sum.setCurrPay(sum.getCurrPay().add(p.getCurrPay()));//当日付，汇总
        		sum.setCurrAmt(sum.getCurrAmt().add(p.getCurrAmt()));//当日余额，汇总
        		sum.setCheckAmt(sum.getCheckAmt().add(p.getCheckAmt()));//核对金额，汇总
        		sum.setMistake(sum.getMistake().add(p.getMistake()));//差额，汇总
        		ls.add(p);
    		}
    		ls.add(sum);
    	}
    	return ls;
    }
}
