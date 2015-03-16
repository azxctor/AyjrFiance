package com.hengxin.platform.escrow.dto;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.hengxin.platform.escrow.validator.BankCardNoCheck;

/**
 * 
 * @author juhuahuang
 *
 */
public class EswBankDto implements Serializable{

	private static final long serialVersionUID = 1;
	
	@NotEmpty(message = "{escrow.error.bankcardno.empty}")
	@Length(min = 15, max = 20, message = "{escrow.error.bankcardno.length}")
	@BankCardNoCheck()
	private String bankCardNo;		//	卡号/存折

	private String bankId;			//	总行联行号
	@NotEmpty(message = "{escrow.error.bankname.empty}")
	private String bankCode;		//	支行/分行联行号
	@NotEmpty
	private String bankTypeCode;	//	行别码
	@NotEmpty
	private String provinceCode;	//	省份编码
	@NotEmpty
	private String cityCode;		//	城市编码

	private String bankName;		//	发卡行名称
	@NotEmpty(message = "{escrow.error.bankcardname.empty}")
	private String bankCardName;	//  持卡人姓名
	
	private String bankAssetsId;	//  银行卡资产编号

	public EswBankDto(){}

	public EswBankDto(String bankCardNo, String bankId, String bankCode,
			String bankTypeCode, String provinceCode, String cityCode,
			String bankName, String bankCardName) {
		super();
		this.bankCardNo = bankCardNo;
		this.bankId = bankId;
		this.bankCode = bankCode;
		this.bankTypeCode = bankTypeCode;
		this.provinceCode = provinceCode;
		this.cityCode = cityCode;
		this.bankName = bankName;
		this.bankCardName = bankCardName;
	}

	public String getBankAssetsId() {
		return bankAssetsId;
	}
	public void setBankAssetsId(String bankAssetsId) {
		this.bankAssetsId = bankAssetsId;
	}
	public String getBankCardNo() {
		return bankCardNo;
	}
	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}
	public String getBankCardName() {
		return bankCardName;
	}
	public void setBankCardName(String bankCardName) {
		this.bankCardName = bankCardName;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankId() {
		return bankId;
	}
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getBankTypeCode() {
		return bankTypeCode;
	}
	public void setBankTypeCode(String bankTypeCode) {
		this.bankTypeCode = bankTypeCode;
	}
}
