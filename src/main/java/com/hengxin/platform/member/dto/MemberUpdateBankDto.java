package com.hengxin.platform.member.dto;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import com.hengxin.platform.common.constant.ApplicationConstant;
import com.hengxin.platform.common.converter.OptionCategory;
import com.hengxin.platform.common.domain.DynamicOption;
import com.hengxin.platform.common.enums.EOptionCategory;
import com.hengxin.platform.common.service.validator.ExistBankAccountCheck;
import com.hengxin.platform.member.dto.MemberUpdateBankDto.SubmitPerson;

/**
 * Pay attention validation groups..... it belongs to this class, rather than Person.Class.
 * 
 */
@ExistBankAccountCheck(groups = { SubmitPerson.class }, bankAccount = "bankAccount", userId = "userId")
public class MemberUpdateBankDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotEmpty(groups = { SubmitPerson.class }, message = "{member.error.field.empty}")
    @Pattern(regexp = ApplicationConstant.BANK_CARD_REGEXP, groups = { SubmitPerson.class }, message = "{member.error.bank.invaild}")
    private String bankAccount;

    @NotEmpty(groups = { SubmitPerson.class }, message = "{member.error.field.empty}")
    private String bankCardFrontImg;

    @NotEmpty(groups = { SubmitPerson.class }, message = "{member.error.field.empty}")
    private String bankAccountName;

    private String bankFullName;

    /** Using SubmitPerson.class belongs to Person class. **/
    @Valid
    @OptionCategory(EOptionCategory.REGION)
    private DynamicOption bankOpenProvince;

    @Valid
    @OptionCategory(EOptionCategory.REGION)
    private DynamicOption bankOpenCity;

    @Valid
    @OptionCategory(EOptionCategory.BANK)
    private DynamicOption bankShortName;
    
    @NotEmpty(groups = { SubmitPerson.class }, message = "{member.error.field.empty}")
    private String bankOpenBranch;
    
    private String userId;
    
    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBankCardFrontImg() {
        return bankCardFrontImg;
    }

    public void setBankCardFrontImg(String bankCardFrontImg) {
        this.bankCardFrontImg = bankCardFrontImg;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public String getBankFullName() {
        return bankFullName;
    }

    public void setBankFullName(String bankFullName) {
        this.bankFullName = bankFullName;
    }

	public DynamicOption getBankShortName() {
		return bankShortName;
	}

	public void setBankShortName(DynamicOption bankShortName) {
		this.bankShortName = bankShortName;
	}

	public DynamicOption getBankOpenProvince() {
		return bankOpenProvince;
	}

	public void setBankOpenProvince(DynamicOption bankOpenProvince) {
		this.bankOpenProvince = bankOpenProvince;
	}

	public DynamicOption getBankOpenCity() {
		return bankOpenCity;
	}

	public void setBankOpenCity(DynamicOption bankOpenCity) {
		this.bankOpenCity = bankOpenCity;
	}

	public String getBankOpenBranch() {
        return bankOpenBranch;
    }

    public void setBankOpenBranch(String bankOpenBranch) {
        this.bankOpenBranch = bankOpenBranch;
    }

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public interface SubmitPerson {

    }

}
