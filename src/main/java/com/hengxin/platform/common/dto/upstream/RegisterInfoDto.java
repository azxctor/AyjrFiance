package com.hengxin.platform.common.dto.upstream;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import com.hengxin.platform.common.constant.ApplicationConstant;
import com.hengxin.platform.common.service.validator.ExistUserNameCheck;
import com.hengxin.platform.common.service.validator.UserNameLengthCheck;
import com.hengxin.platform.member.dto.FinancierDto;
import com.hengxin.platform.member.dto.InvestorDto;
import com.hengxin.platform.member.dto.OrganizationDto;
import com.hengxin.platform.member.dto.PersonDto;
import com.hengxin.platform.member.dto.PersonDto.SubmitPerson;
import com.hengxin.platform.member.enums.EUserType;

/**
 * register investor by agent.
 * 
 * @author tingyu
 * 
 */
public class RegisterInfoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotEmpty(groups = { SubmitPerson.class }, message = "{member.error.field.empty}")
    @Pattern(regexp=ApplicationConstant.USER_NAME_REGEXP, groups = { SubmitPerson.class }, message = "{member.error.username.format}")
    @ExistUserNameCheck(groups = { SubmitPerson.class }, message = "{member.error.username.exist}")
    @UserNameLengthCheck(groups = { SubmitPerson.class })
    private String username;

    private String password;

    private EUserType type;

    @Valid
    private PersonDto person;

    @Valid
    private OrganizationDto org;

    @Valid
    private InvestorDto investor;

    @Valid
    private FinancierDto financier;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public EUserType getType() {
        return type;
    }

    public void setType(EUserType type) {
        this.type = type;
    }

    /**
     * @return return the value of the var person
     */

    public PersonDto getPerson() {
        return person;
    }

    /**
     * @param person
     *            Set person value
     */

    public void setPerson(PersonDto person) {
        this.person = person;
    }

    /**
     * @return return the value of the var org
     */

    public OrganizationDto getOrg() {
        return org;
    }

    /**
     * @param org
     *            Set org value
     */

    public void setOrg(OrganizationDto org) {
        this.org = org;
    }

    /**
     * @return return the value of the var financier
     */

    public FinancierDto getFinancier() {
        return financier;
    }

    /**
     * @return return the value of the var investor
     */

    public InvestorDto getInvestor() {
        return investor;
    }

    /**
     * @param investor
     *            Set investor value
     */

    public void setInvestor(InvestorDto investor) {
        this.investor = investor;
    }

    /**
     * @param financier
     *            Set financier value
     */

    public void setFinancier(FinancierDto financier) {
        this.financier = financier;
    }

    /**
     * @return return the value of the var serialversionuid
     */

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
