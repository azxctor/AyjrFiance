/*
 * Project Name: kmfex-platform
 * File Name: MemberApplicationPo.java
 * Class Name: MemberApplicationPo
 *
 * Copyright 2014 KMFEX Inc
 *
 * 
 *
 * http://www.kmfex.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hengxin.platform.member.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hengxin.platform.common.dto.annotation.BusinessName;
import com.hengxin.platform.common.enums.EOptionCategory;
import com.hengxin.platform.member.domain.converter.GenderEnumConverter;
import com.hengxin.platform.member.domain.converter.UserTypeEnumConverter;
import com.hengxin.platform.member.enums.EGender;
import com.hengxin.platform.member.enums.EUserType;

/**
 * Class Name: MemberApplicationPo
 * 
 * @author shengzhou
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "UM_USER_APPL")
public class MemberApplication extends BaseApplication implements Serializable, Cloneable {

    @Column(name = "USER_TYPE")
    @Convert(converter = UserTypeEnumConverter.class)
    private EUserType userType;

    @BusinessName("Email")
    @Column(name = "P_EMAIL")
    private String personEmail;

    @BusinessName("手机")
    @Column(name = "P_MOBILE")
    private String personMobile;

    @BusinessName("姓名")
    @Column(name = "P_NAME")
    private String personName;

    @BusinessName(value = "所在区域", optionCategory = EOptionCategory.REGION)
    @Column(name = "P_REGION_CD")
    private String personRegion;

    @BusinessName("性别")
    @Column(name = "P_GENDER")
    @Convert(converter = GenderEnumConverter.class)
    private EGender personSex;

    @BusinessName("座机")
    @Column(name = "P_PHONE")
    private String personPhone;

    @BusinessName("常住地址")
    @Column(name = "P_ADDRESS")
    private String personAddress;

    @BusinessName("邮编")
    @Column(name = "P_ZIP")
    private String personPostCode;

    @BusinessName("QQ")
    @Column(name = "P_QQ")
    private String personQQ;

    @BusinessName(ignore = true)
    @Temporal(TemporalType.DATE)
    @Column(name = "P_BIRTH_DT")
    private Date personBirthday;

    @BusinessName("身份证号")
    @Column(name = "P_ID_CARD_NO")
    private String personIdCardNumber;

    @BusinessName("身份证正面扫描图")
    @Column(name = "P_ID_CARD_IMG1")
    private String personIdCardFrontImg;

    @BusinessName("身份证反面扫描图")
    @Column(name = "P_ID_CARD_IMG2")
    private String personIdCardBackImg;

    @BusinessName(value = "职业", optionCategory = EOptionCategory.JOB)
    @Column(name = "P_JOB")
    private String personJob;

    @BusinessName(ignore = true)
    @Column(name = "P_AGE_CD")
    private String personAge;

    @BusinessName(value = "学历状况", optionCategory = EOptionCategory.EDUCATION)
    @Column(name = "P_EDUCN_CD")
    private String personEducation;

    @BusinessName("法人Email")
    @Column(name = "O_EMAIL")
    private String orgEmail;

    @BusinessName("法人手机")
    @Column(name = "O_MOBILE")
    private String orgMobile;

    @BusinessName("企业名称")
    @Column(name = "O_NAME")
    private String orgName;

    @BusinessName(value = "所在区域", optionCategory = EOptionCategory.REGION)
    @Column(name = "O_REGION_CD")
    private String orgRegion;

    @BusinessName("企业座机")
    @Column(name = "O_PHONE")
    private String orgPhone;

    @BusinessName("企业地址")
    @Column(name = "O_ADDRESS")
    private String orgAddress;

    @BusinessName("邮编")
    @Column(name = "O_ZIP")
    private String orgPostCode;

    @BusinessName("传真号")
    @Column(name = "O_FAX")
    private String orgFax;

    @BusinessName("法人QQ")
    @Column(name = "O_QQ")
    private String orgQQ;

    @BusinessName(value = "企业类型", optionCategory = EOptionCategory.ORG_TYPE)
    @Column(name = "O_TYPE_CD")
    private String orgType;

    @BusinessName(value = "所属行业", optionCategory = EOptionCategory.ORG_INDUSTRY)
    @Column(name = "O_INDUSTRY_CD")
    private String orgIndustry;

    @BusinessName("企业性质")
    @Column(name = "O_NATURE_CD")
    private String orgNature;

    @BusinessName("法人代表")
    @Column(name = "O_REP")
    private String orgLegalPerson;

    @BusinessName("法人性别")
    @Column(name = "O_REP_GENDER")
    @Convert(converter = GenderEnumConverter.class)
    private EGender orgLegalPersonGender;

    @BusinessName("法人身份证号")
    @Column(name = "O_REP_ID_CARD_NO")
    private String orgLegalPersonIdCardNumber;

    @BusinessName("法人身份证正面扫描图")
    @Column(name = "O_REP_ID_CARD_IMG1")
    private String orgLegalPersonIdCardFrontImg;

    @BusinessName("法人身份证反面扫描图")
    @Column(name = "O_REP_ID_CARD_IMG2")
    private String orgLegalPersonIdCardBackImg;

    @BusinessName(ignore = true)
    @Column(name = "O_REP_AGE_CD")
    private String orgLegalPersonAge;

    @BusinessName(ignore = true)
    @Column(name = "O_REP_BIRTH_DT")
    @Temporal(TemporalType.DATE)
    private Date orgLegalPersonBirthday;

    @BusinessName(value = "法人学历状况", optionCategory = EOptionCategory.EDUCATION)
    @Column(name = "O_REP_EDUCN_CD")
    private String orgLegalPersonEduction;

    @BusinessName("联系人")
    @Column(name = "O_CONTACT")
    private String contact;

    @BusinessName("联系人座机")
    @Column(name = "O_CONTACT_PHONE")
    private String contactPhone;

    @BusinessName("联系人手机")
    @Column(name = "O_CONTACT_MOBILE")
    private String contactMobile;

    @BusinessName("联系人传真")
    @Column(name = "O_CONTACT_FAX")
    private String contactFax;

    @BusinessName("联系人Email")
    @Column(name = "O_CONTACT_EMAIL")
    private String contactEmail;

    @BusinessName("主营业务")
    @Column(name = "O_MAIN_BIZ")
    private String orgMainBusiness;

    @BusinessName(value = "开户银行", optionCategory = EOptionCategory.BANK)
    @Column(name = "BNK_CD")
    private String bankShortName;

    @Column(name = "BNK_NAME")
    private String bankFullName;

    @BusinessName("银行账号")
    @Column(name = "BNK_ACCT_NO")
    private String bankAccount;

    @BusinessName("银行卡正面扫描图")
    @Column(name = "BNK_CARD_IMG")
    private String bankCardFrontImg;

    @BusinessName("银行账户名")
    @Column(name = "BNK_ACCT_NAME")
    private String bankAccountName;

    @BusinessName(value = "开户银行所在省", optionCategory = EOptionCategory.REGION)
    @Column(name = "BNK_OPEN_PROV")
    private String bankOpenProvince;

    @BusinessName(value = "开户银行所在市", optionCategory = EOptionCategory.REGION)
    @Column(name = "BNK_OPEN_CITY")
    private String bankOpenCity;

    @BusinessName("支行/营业所")
    @Column(name = "BNK_OPEN_BRCH")
    private String bankOpenBranch;

    @Override
    public MemberApplication clone() {
        MemberApplication clone = null;
        try {
            clone = (MemberApplication) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }

    /**
     * @return return the value of the var userType
     */

    public EUserType getUserType() {
        return userType;
    }

    /**
     * @param userType
     *            Set userType value
     */

    public void setUserType(EUserType userType) {
        this.userType = userType;
    }

    /**
     * @return return the value of the var personEmail
     */

    public String getPersonEmail() {
        return personEmail;
    }

    /**
     * @param personEmail
     *            Set personEmail value
     */

    public void setPersonEmail(String personEmail) {
        this.personEmail = personEmail;
    }

    /**
     * @return return the value of the var personMobile
     */

    public String getPersonMobile() {
        return personMobile;
    }

    /**
     * @param personMobile
     *            Set personMobile value
     */

    public void setPersonMobile(String personMobile) {
        this.personMobile = personMobile;
    }

    /**
     * @return return the value of the var personName
     */

    public String getPersonName() {
        return personName;
    }

    /**
     * @param personName
     *            Set personName value
     */

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    /**
     * @return return the value of the var personRegion
     */

    public String getPersonRegion() {
        return personRegion;
    }

    /**
     * @param personRegion
     *            Set personRegion value
     */

    public void setPersonRegion(String personRegion) {
        this.personRegion = personRegion;
    }

    /**
     * @return return the value of the var personSex
     */

    public EGender getPersonSex() {
        return personSex;
    }

    /**
     * @param personSex
     *            Set personSex value
     */

    public void setPersonSex(EGender personSex) {
        this.personSex = personSex;
    }

    /**
     * @return return the value of the var personPhone
     */

    public String getPersonPhone() {
        return personPhone;
    }

    /**
     * @param personPhone
     *            Set personPhone value
     */

    public void setPersonPhone(String personPhone) {
        this.personPhone = personPhone;
    }

    /**
     * @return return the value of the var personAddress
     */

    public String getPersonAddress() {
        return personAddress;
    }

    /**
     * @param personAddress
     *            Set personAddress value
     */

    public void setPersonAddress(String personAddress) {
        this.personAddress = personAddress;
    }

    /**
     * @return return the value of the var personPostCode
     */

    public String getPersonPostCode() {
        return personPostCode;
    }

    /**
     * @param personPostCode
     *            Set personPostCode value
     */

    public void setPersonPostCode(String personPostCode) {
        this.personPostCode = personPostCode;
    }

    /**
     * @return return the value of the var personQQ
     */

    public String getPersonQQ() {
        return personQQ;
    }

    /**
     * @param personQQ
     *            Set personQQ value
     */

    public void setPersonQQ(String personQQ) {
        this.personQQ = personQQ;
    }

    /**
     * @return return the value of the var personBirthday
     */

    public Date getPersonBirthday() {
        return personBirthday;
    }

    /**
     * @param personBirthday
     *            Set personBirthday value
     */

    public void setPersonBirthday(Date personBirthday) {
        this.personBirthday = personBirthday;
    }

    /**
     * @return return the value of the var personIdCardNumber
     */

    public String getPersonIdCardNumber() {
        return personIdCardNumber;
    }

    /**
     * @param personIdCardNumber
     *            Set personIdCardNumber value
     */

    public void setPersonIdCardNumber(String personIdCardNumber) {
        this.personIdCardNumber = personIdCardNumber;
    }

    /**
     * @return return the value of the var personIdCardFrontImg
     */

    public String getPersonIdCardFrontImg() {
        return personIdCardFrontImg;
    }

    /**
     * @param personIdCardFrontImg
     *            Set personIdCardFrontImg value
     */

    public void setPersonIdCardFrontImg(String personIdCardFrontImg) {
        this.personIdCardFrontImg = personIdCardFrontImg;
    }

    /**
     * @return return the value of the var personIdCardBackImg
     */

    public String getPersonIdCardBackImg() {
        return personIdCardBackImg;
    }

    /**
     * @param personIdCardBackImg
     *            Set personIdCardBackImg value
     */

    public void setPersonIdCardBackImg(String personIdCardBackImg) {
        this.personIdCardBackImg = personIdCardBackImg;
    }

    /**
     * @return return the value of the var personJob
     */

    public String getPersonJob() {
        return personJob;
    }

    /**
     * @param personJob
     *            Set personJob value
     */

    public void setPersonJob(String personJob) {
        this.personJob = personJob;
    }

    /**
     * @return return the value of the var personAge
     */

    public String getPersonAge() {
        return personAge;
    }

    /**
     * @param personAge
     *            Set personAge value
     */

    public void setPersonAge(String personAge) {
        this.personAge = personAge;
    }

    /**
     * @return return the value of the var personEducation
     */

    public String getPersonEducation() {
        return personEducation;
    }

    /**
     * @param personEducation
     *            Set personEducation value
     */

    public void setPersonEducation(String personEducation) {
        this.personEducation = personEducation;
    }

    /**
     * @return return the value of the var orgEmail
     */

    public String getOrgEmail() {
        return orgEmail;
    }

    /**
     * @param orgEmail
     *            Set orgEmail value
     */

    public void setOrgEmail(String orgEmail) {
        this.orgEmail = orgEmail;
    }

    /**
     * @return return the value of the var orgMobile
     */

    public String getOrgMobile() {
        return orgMobile;
    }

    /**
     * @param orgMobile
     *            Set orgMobile value
     */

    public void setOrgMobile(String orgMobile) {
        this.orgMobile = orgMobile;
    }

    /**
     * @return return the value of the var orgName
     */

    public String getOrgName() {
        return orgName;
    }

    /**
     * @param orgName
     *            Set orgName value
     */

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    /**
     * @return return the value of the var orgRegion
     */

    public String getOrgRegion() {
        return orgRegion;
    }

    /**
     * @param orgRegion
     *            Set orgRegion value
     */

    public void setOrgRegion(String orgRegion) {
        this.orgRegion = orgRegion;
    }

    /**
     * @return return the value of the var orgPhone
     */

    public String getOrgPhone() {
        return orgPhone;
    }

    /**
     * @param orgPhone
     *            Set orgPhone value
     */

    public void setOrgPhone(String orgPhone) {
        this.orgPhone = orgPhone;
    }

    /**
     * @return return the value of the var orgAddress
     */

    public String getOrgAddress() {
        return orgAddress;
    }

    /**
     * @param orgAddress
     *            Set orgAddress value
     */

    public void setOrgAddress(String orgAddress) {
        this.orgAddress = orgAddress;
    }

    /**
     * @return return the value of the var orgPostCode
     */

    public String getOrgPostCode() {
        return orgPostCode;
    }

    /**
     * @param orgPostCode
     *            Set orgPostCode value
     */

    public void setOrgPostCode(String orgPostCode) {
        this.orgPostCode = orgPostCode;
    }

    /**
     * @return return the value of the var orgFax
     */

    public String getOrgFax() {
        return orgFax;
    }

    /**
     * @param fax
     *            Set orgFax value
     */

    public void setOrgFax(String orgFax) {
        this.orgFax = orgFax;
    }

    /**
     * @return return the value of the var orgQQ
     */

    public String getOrgQQ() {
        return orgQQ;
    }

    /**
     * @param orgQQ
     *            Set orgQQ value
     */

    public void setOrgQQ(String orgQQ) {
        this.orgQQ = orgQQ;
    }

    /**
     * @return return the value of the var orgType
     */

    public String getOrgType() {
        return orgType;
    }

    /**
     * @param orgType
     *            Set orgType value
     */

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    /**
     * @return return the value of the var orgIndustry
     */

    public String getOrgIndustry() {
        return orgIndustry;
    }

    /**
     * @param orgIndustry
     *            Set orgIndustry value
     */

    public void setOrgIndustry(String orgIndustry) {
        this.orgIndustry = orgIndustry;
    }

    /**
     * @return return the value of the var orgNature
     */

    public String getOrgNature() {
        return orgNature;
    }

    /**
     * @param orgNature
     *            Set orgNature value
     */

    public void setOrgNature(String orgNature) {
        this.orgNature = orgNature;
    }

    /**
     * @return return the value of the var orgLegalPerson
     */

    public String getOrgLegalPerson() {
        return orgLegalPerson;
    }

    /**
     * @param orgLegalPerson
     *            Set orgLegalPerson value
     */

    public void setOrgLegalPerson(String orgLegalPerson) {
        this.orgLegalPerson = orgLegalPerson;
    }

    /**
     * @param orgLegalPersonGender
     *            Set orgLegalPersonGender value
     */
    public EGender getOrgLegalPersonGender() {
        return orgLegalPersonGender;
    }

    public void setOrgLegalPersonGender(EGender orgLegalPersonGender) {
        this.orgLegalPersonGender = orgLegalPersonGender;
    }

    /**
     * @return return the value of the var orgLegalPersonIdCardNumber
     */

    public String getOrgLegalPersonIdCardNumber() {
        return orgLegalPersonIdCardNumber;
    }

    /**
     * @param orgLegalPersonIdCardNumber
     *            Set orgLegalPersonIdCardNumber value
     */

    public void setOrgLegalPersonIdCardNumber(String orgLegalPersonIdCardNumber) {
        this.orgLegalPersonIdCardNumber = orgLegalPersonIdCardNumber;
    }

    /**
     * @return return the value of the var orgLegalPersonIdCardFrontImg
     */

    public String getOrgLegalPersonIdCardFrontImg() {
        return orgLegalPersonIdCardFrontImg;
    }

    /**
     * @param orgLegalPersonIdCardFrontImg
     *            Set orgLegalPersonIdCardFrontImg value
     */

    public void setOrgLegalPersonIdCardFrontImg(String orgLegalPersonIdCardFrontImg) {
        this.orgLegalPersonIdCardFrontImg = orgLegalPersonIdCardFrontImg;
    }

    /**
     * @return return the value of the var orgLegalPersonIdCardBackImg
     */

    public String getOrgLegalPersonIdCardBackImg() {
        return orgLegalPersonIdCardBackImg;
    }

    /**
     * @param orgLegalPersonIdCardBackImg
     *            Set orgLegalPersonIdCardBackImg value
     */

    public void setOrgLegalPersonIdCardBackImg(String orgLegalPersonIdCardBackImg) {
        this.orgLegalPersonIdCardBackImg = orgLegalPersonIdCardBackImg;
    }

    /**
     * @return return the value of the var orgLegalPersonAge
     */

    public String getOrgLegalPersonAge() {
        return orgLegalPersonAge;
    }

    /**
     * @param orgLegalPersonAge
     *            Set orgLegalPersonAge value
     */

    public void setOrgLegalPersonAge(String orgLegalPersonAge) {
        this.orgLegalPersonAge = orgLegalPersonAge;
    }

    /**
     * @param orgLegalPersonBirthday
     *            Set orgLegalPersonBirthday value
     */
    public Date getOrgLegalPersonBirthday() {
        return orgLegalPersonBirthday;
    }

    /**
     * @param orgLegalPersonBirthday
     *            Set orgLegalPersonBirthday value
     */
    public void setOrgLegalPersonBirthday(Date orgLegalPersonBirthday) {
        this.orgLegalPersonBirthday = orgLegalPersonBirthday;
    }

    /**
     * @return return the value of the var orgLegalPersonEduction
     */

    public String getOrgLegalPersonEduction() {
        return orgLegalPersonEduction;
    }

    /**
     * @param orgLegalPersonEduction
     *            Set orgLegalPersonEduction value
     */

    public void setOrgLegalPersonEduction(String orgLegalPersonEduction) {
        this.orgLegalPersonEduction = orgLegalPersonEduction;
    }

    /**
     * @return return the value of the var contact
     */

    public String getContact() {
        return contact;
    }

    /**
     * @param contact
     *            Set contact value
     */

    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * @return return the value of the var contactPhone
     */

    public String getContactPhone() {
        return contactPhone;
    }

    /**
     * @param contactPhone
     *            Set contactPhone value
     */

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    /**
     * @return return the value of the var contactMobile
     */

    public String getContactMobile() {
        return contactMobile;
    }

    /**
     * @param contactMobile
     *            Set contactMobile value
     */

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    /**
     * @return return the value of the var contactFax
     */

    public String getContactFax() {
        return contactFax;
    }

    /**
     * @param contactFax
     *            Set contactFax value
     */

    public void setContactFax(String contactFax) {
        this.contactFax = contactFax;
    }

    /**
     * @return return the value of the var orgMainBusiness
     */

    public String getOrgMainBusiness() {
        return orgMainBusiness;
    }

    /**
     * @param orgMainBusiness
     *            Set orgMainBusiness value
     */

    public void setOrgMainBusiness(String orgMainBusiness) {
        this.orgMainBusiness = orgMainBusiness;
    }

    /**
     * @return return the value of the var bankShortName
     */

    public String getBankShortName() {
        return bankShortName;
    }

    /**
     * @param bankShortName
     *            Set bankShortName value
     */

    public void setBankShortName(String bankShortName) {
        this.bankShortName = bankShortName;
    }

    /**
     * @return return the value of the var bankFullName
     */

    public String getBankFullName() {
        return bankFullName;
    }

    /**
     * @param bankFullName
     *            Set bankFullName value
     */

    public void setBankFullName(String bankFullName) {
        this.bankFullName = bankFullName;
    }

    /**
     * @return return the value of the var bankAccount
     */

    public String getBankAccount() {
        return bankAccount;
    }

    /**
     * @param bankAccount
     *            Set bankAccount value
     */

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    /**
     * @return return the value of the var bankCardFrontImg
     */

    public String getBankCardFrontImg() {
        return bankCardFrontImg;
    }

    /**
     * @param bankCardFrontImg
     *            Set bankCardFrontImg value
     */

    public void setBankCardFrontImg(String bankCardFrontImg) {
        this.bankCardFrontImg = bankCardFrontImg;
    }

    /**
     * @return return the value of the var bankAccountName
     */

    public String getBankAccountName() {
        return bankAccountName;
    }

    /**
     * @param bankAccountName
     *            Set bankAccountName value
     */

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    /**
     * @return return the value of the var bankOpenProvince
     */

    public String getBankOpenProvince() {
        return bankOpenProvince;
    }

    /**
     * @param bankOpenProvince
     *            Set bankOpenProvince value
     */

    public void setBankOpenProvince(String bankOpenProvince) {
        this.bankOpenProvince = bankOpenProvince;
    }

    /**
     * @return return the value of the var bankOpenCity
     */

    public String getBankOpenCity() {
        return bankOpenCity;
    }

    /**
     * @param bankOpenCity
     *            Set bankOpenCity value
     */

    public void setBankOpenCity(String bankOpenCity) {
        this.bankOpenCity = bankOpenCity;
    }

    /**
     * @return return the value of the var bankOpenBranch
     */

    public String getBankOpenBranch() {
        return bankOpenBranch;
    }

    /**
     * @param bankOpenBranch
     *            Set bankOpenBranch value
     */

    public void setBankOpenBranch(String bankOpenBranch) {
        this.bankOpenBranch = bankOpenBranch;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

}
