package com.hengxin.platform.fund.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.util.PaginationUtil;
import com.hengxin.platform.fund.dto.BnkTransferTrxDtlDto;
import com.hengxin.platform.fund.dto.TransferTrxDtlDto;
import com.hengxin.platform.fund.dto.TransferTrxDtlSearchDto;
import com.hengxin.platform.fund.entity.TransferTrxDtlView;
import com.hengxin.platform.fund.enums.EFlagType;
import com.hengxin.platform.fund.repository.BankTrxJnlRepository;
import com.hengxin.platform.fund.repository.TransferTrxDtlRepository;
import com.hengxin.platform.fund.repository.VTrsfTrxDtlRepository;
import com.hengxin.platform.fund.util.DateUtils;

@Service
@Qualifier("transferTrxDtlService")
public class TransferTrxDtlService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(TransferTrxDtlService.class);

	@Autowired
	VTrsfTrxDtlRepository vTrsfTrxDtlRepository;
	
	@Autowired
	BankTrxJnlRepository bankTrxJnlRepository;
	
	@Autowired
	TransferTrxDtlRepository transferTrxDtlRepository;
	
	public final static String DATE_FORMAT = "yyyyMMdd";

	/**
	 * 获取转账交易对账明细
	 * 
	 * @param searchDto
	 * @return
	 */
	@Deprecated
	public DataTablesResponseDto<TransferTrxDtlDto> getTransferTrxDtlDetailInfo(
			final TransferTrxDtlSearchDto searchDto) throws BizServiceException {
		Pageable pageable = PaginationUtil.buildPageRequest(searchDto);
		Page<TransferTrxDtlView> transferTrxDtlViews = null;

		try {
			Date startDate = DateUtils.getStartDate(searchDto.getTrxDt());
			Date endDate = DateUtils.getEndDate(searchDto.getTrxDt());
			transferTrxDtlViews = this.vTrsfTrxDtlRepository
					.findBySignedFlgAndTrxDtBetweenOrTxDate(EFlagType.YES,
							startDate, endDate, searchDto.getTxDate(), pageable);
		} catch (ParseException e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("~~~~", e);
			}
			throw new BizServiceException(EErrorCode.TECH_DATA_INVALID,
					"获取转账交易对账明细数据失败", e);
		}

		return packDto(transferTrxDtlViews, searchDto);

	}
	
	/**
     * 获取转账交易对账明细
     * 
     * @param searchDto
     * @return
     */
    public DataTablesResponseDto<TransferTrxDtlDto> getTransferTrxDtls(
            final TransferTrxDtlSearchDto searchDto) throws BizServiceException {
        List<TransferTrxDtlView> query = null;
        try {
            //获取平台,银行交易日期
            String platformTrxDt = DateUtils.formatDate(searchDto.getTrxDt(), DATE_FORMAT);
            String bankTrxDt = DateUtils.formatDate(searchDto.getTxDt(), DATE_FORMAT);
            query = this.vTrsfTrxDtlRepository
                    .getTransferTrxDtl(platformTrxDt, bankTrxDt);
        } catch (Exception e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("~~~~", e);
            }
            throw new BizServiceException(EErrorCode.TECH_DATA_INVALID,
                    "获取转账交易对账明细数据失败", e);
        }
        return packQuery(query, searchDto);
    }
    
    /**
     * 获取对账汇总数据
     * 
     * @param searchDto
     * @return
     */
    public BnkTransferTrxDtlDto getTransferTrxDtlSum(
            final TransferTrxDtlSearchDto searchDto) throws BizServiceException {
        BnkTransferTrxDtlDto result = new BnkTransferTrxDtlDto(); 
        try {
            //获取平台,银行交易日期
            String platformTrxDt = DateUtils.formatDate(searchDto.getTrxDt(), DATE_FORMAT);
            String bankTrxDt = DateUtils.formatDate(searchDto.getTxDt(), DATE_FORMAT);
            //获取总笔数,金额
            result.setPlatformRechargeCount(bankTrxJnlRepository.getRechargeCount(platformTrxDt));
            result.setPlatformWithdrawalCount(bankTrxJnlRepository.getWithdrawalCount(platformTrxDt));
            result.setPlatformRechargeAmt(bankTrxJnlRepository.getRechargeSumAmt(platformTrxDt));
            result.setPlatformWithdrawalAmt(bankTrxJnlRepository.getWithdrawalSumAmt(platformTrxDt));
            result.setBnkRechargeCount(transferTrxDtlRepository.getRechargeCount(bankTrxDt));
            result.setBnkWithdrawalCount(transferTrxDtlRepository.getWithdrawalCount(bankTrxDt));
            result.setBnkRechargeAmt(transferTrxDtlRepository.getRechargeSumAmt(bankTrxDt));
            result.setBnkWithdrawalAmt(transferTrxDtlRepository.getWithdrawalSumAmt(bankTrxDt));
        } catch (Exception e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("~~~~", e);
            }
            throw new BizServiceException(EErrorCode.TECH_DATA_INVALID,
                    "获取转账交易对账明细数据失败", e);
        }
        return result;
    }

	
    /**
    * Description: 包装为返回Dto
    *
    * @param query
    * @return
    */
    	
    private DataTablesResponseDto<TransferTrxDtlDto> packQuery(List<TransferTrxDtlView> views, TransferTrxDtlSearchDto searchDto) {
        DataTablesResponseDto<TransferTrxDtlDto> result = new DataTablesResponseDto<TransferTrxDtlDto>();
        List<TransferTrxDtlDto> dtos = new ArrayList<TransferTrxDtlDto>();
        TransferTrxDtlDto dto = null;
        for (TransferTrxDtlView view : views) {
            dto = new TransferTrxDtlDto();
            dto.setAcctNo(view.getAcctNo());
            dto.setBankAccNo(view.getBankAccNo());
            dto.setBankId(view.getBankId());
            dto.setBatchNo(view.getBatchNo());
            dto.setBkSerial(view.getBkSerial());
            dto.setFundAccNo(view.getFundAccNo());
            dto.setPayRecvFlag(view.getPayRecvFlag());
            dto.setTrxDt(view.getTrxDt());
            dto.setRvsFlg(view.getRvsFlg());
            dto.setStatus(view.getStatus());
            dto.setTrxAmt(view.getTrxAmt());
            dto.setTxAmt(view.getTxAmt());
//            dto.setTxDate((null != view.getTxDate() && 8 == view
//                    .getTxDate().length()) ? view.getTxDate().substring(0, 4)
//                    + "-" + view.getTxDate().substring(4, 6) + "-"
//                    + view.getTxDate().substring(6) : "");
            String txDateStr = "";
            if(StringUtils.isNotBlank(view.getTxDate())){
            	Date txDate = DateUtils.getDate(view.getTxDate(), "yyyyMMdd");
            	txDateStr = DateUtils.formatDate(txDate, "yyyy-MM-dd");
            }
            dto.setTxDate(txDateStr);
            dto.setTxDir(view.getTxDir());
            dto.setTxOpt(view.getTxOpt());
            dto.setUserName(view.getUserName());
            dto.setCustName(view.getCustName());
            if (null == view.getTrxAmt() || null == view.getTxAmt()) {
                dto.setResult(false);
            } else {
                dto.setResult(view.getTrxAmt().compareTo(
                        view.getTxAmt()) == 0);
            }
            dtos.add(dto);
        }
        result.setEcho(searchDto.getEcho());
        result.setData(dtos);
        result.setTotalDisplayRecords(dtos.size());
        result.setTotalRecords(dtos.size());
        return result;
    }

    /**
	 * 
	 * 包装为返回Dto
	 * 
	 * @param bankTrxJnlPos
	 * @return
	 */
	private DataTablesResponseDto<TransferTrxDtlDto> packDto(
			Page<TransferTrxDtlView> transferTrxDtlViews,
			TransferTrxDtlSearchDto searchDto) {
		DataTablesResponseDto<TransferTrxDtlDto> result = new DataTablesResponseDto<TransferTrxDtlDto>();
		List<TransferTrxDtlDto> transferTrxDtlDtos = new ArrayList<TransferTrxDtlDto>();
		TransferTrxDtlDto transferTrxDtlDto = null;
		for (TransferTrxDtlView view : transferTrxDtlViews) {
			transferTrxDtlDto = new TransferTrxDtlDto();
			transferTrxDtlDto.setAcctNo(view.getAcctNo());
			transferTrxDtlDto.setBankAccNo(view.getBankAccNo());
			transferTrxDtlDto.setBankId(view.getBankId());
			transferTrxDtlDto.setBatchNo(view.getBatchNo());
			transferTrxDtlDto.setBkSerial(view.getBkSerial());
			transferTrxDtlDto.setFundAccNo(view.getFundAccNo());
			transferTrxDtlDto.setPayRecvFlag(view.getPayRecvFlag());
			transferTrxDtlDto.setTrxDt(view.getTrxDt());
			transferTrxDtlDto.setRvsFlg(view.getRvsFlg());
			transferTrxDtlDto.setStatus(view.getStatus());
			transferTrxDtlDto.setTrxAmt(view.getTrxAmt());
			transferTrxDtlDto.setTxAmt(view.getTxAmt());
			transferTrxDtlDto.setTxDate((null != view.getTxDate() && 8 == view
					.getTxDate().length()) ? view.getTxDate().substring(0, 4)
					+ "-" + view.getTxDate().substring(4, 6) + "-"
					+ view.getTxDate().substring(6) : "");
			transferTrxDtlDto.setTxDir(view.getTxDir());
			transferTrxDtlDto.setTxOpt(view.getTxOpt());
			transferTrxDtlDto.setUserName(view.getUserName());
			if (null == view.getTrxAmt() || null == view.getTxAmt()) {
				transferTrxDtlDto.setResult(false);
			} else {
				transferTrxDtlDto.setResult(view.getTrxAmt().compareTo(
						view.getTxAmt()) == 0);
			}
			transferTrxDtlDtos.add(transferTrxDtlDto);
		}
		result.setEcho(searchDto.getEcho());
		result.setData(transferTrxDtlDtos);
		result.setTotalDisplayRecords(transferTrxDtlViews.getTotalElements());
		result.setTotalRecords(transferTrxDtlViews.getTotalElements());
		return result;
	}
}
