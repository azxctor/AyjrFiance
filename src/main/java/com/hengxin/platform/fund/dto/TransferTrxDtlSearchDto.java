package com.hengxin.platform.fund.dto;

import java.io.Serializable;
import java.util.Date;

import com.hengxin.platform.common.dto.DataTablesRequestDto;

/**
 * Class Name: TransferTrxDtlSearchDto
 * Description: TODO
 * @author jishen
 *
 */

public class TransferTrxDtlSearchDto extends DataTablesRequestDto implements
		Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
     * 系统交易日期
     */
	private Date trxDt;
	
	/*
     * 银行交易日期
     */
	private Date txDt;
	
	private String txDate;

	public Date getTrxDt() {
		return trxDt;
	}

	public void setTrxDt(Date trxDt) {
		this.trxDt = trxDt;
	}

	public String getTxDate() {
		return txDate;
	}

	public void setTxDate(String txDate) {
		this.txDate = txDate;
	}

	public Date getTxDt() {
		return txDt;
	}

	public void setTxDt(Date txDt) {
		this.txDt = txDt;
	}
}
