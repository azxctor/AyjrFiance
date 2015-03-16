/*
 * Project Name: kmfex-platform
 * File Name: AgencyApplicationPo.java
 * Class Name: AgencyApplicationPo
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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.hengxin.platform.common.dto.annotation.BusinessName;
import com.hengxin.platform.common.enums.EOptionCategory;

/**
 * Class Name: AgencyApplicationPo
 * 
 * @author shengzhou,chunlinwang
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "UM_ORG_APPL")
public class AgencyApplication extends BaseApplication implements Serializable {

    // @Column(name = "USER_TYPE")
    // @Convert(converter = UserTypeEnumConverter.class)
    // private EUserType userType;

    @BusinessName("企业名称")
    @Column(name = "O_NAME")
    private String name;

    @BusinessName("法人Email")
    @Column(name = "O_EMAIL")
    private String email;

    @BusinessName("法人手机")
    @Column(name = "O_MOBILE")
    private String mobile;

    @BusinessName(value = "所在区域", optionCategory = EOptionCategory.REGION)
    @Column(name = "O_REGION_CD")
    private String region;

    @BusinessName("企业性质")
    @Column(name = "O_NATURE_CD")
    private String orgNature;

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
    private String fax;

    @BusinessName("法人QQ")
    @Column(name = "O_QQ")
    private String orgQQ;

    @BusinessName("法人代表")
    @Column(name = "O_REP")
    private String orgLegalPerson;

    @BusinessName("法人身份证号")
    @Column(name = "O_REP_ID_CARD_NO")
    private String orgLegalPersonIdCardNumber;

    @BusinessName("法人身份证正面扫描图")
    @Column(name = "O_REP_ID_CARD_IMG1")
    private String orgLegalPersonIdCardFrontImg;

    @BusinessName("法人身份证反面扫描图")
    @Column(name = "O_REP_ID_CARD_IMG2")
    private String orgLegalPersonIdCardBackImg;

    @BusinessName("法人联系电话")
    @Column(name = "O_REP_PHONE")
    private String orgLeagalPersonPhone;

    @Column(name = "O_REP_MOBILE")
    private String orgLeagalPersonMobile;

    @BusinessName("法人QQ")
    @Column(name = "O_REP_QQ")
    private String orgLeagalPersonQQ;

    @BusinessName("法人Email")
    @Column(name = "O_REP_EMAIL")
    private String orgLeagalPersonEmail;

    // @Column(name = "O_REP_AGE_CD")
    // private String orgLegalPersonAge;
    //
    // @Column(name = "O_REP_EDUCN_CD")
    // private String orgLegalPersonEduction;

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

    @BusinessName("联系人QQ")
    @Column(name = "O_CONTACT_QQ")
    private String contactQQ;

    @BusinessName("联系人Email")
    @Column(name = "O_CONTACT_EMAIL")
    private String contactEmail;

    @BusinessName("组织机构代码")
    @Column(name = "O_ORG_NO")
    private String orgNo;

    @BusinessName("组织机构代码扫描图")
    @Column(name = "O_ORG_NO_IMG")
    private String orgNoImg;

    @BusinessName("营业执照代码")
    @Column(name = "O_LICENCE_NO")
    private String licenceNo;

    @BusinessName("营业执照扫描图")
    @Column(name = "O_LICENCE_NO_IMG")
    private String licenceNoImg;

    @BusinessName("税务登记号码")
    @Column(name = "O_TAX_NO")
    private String taxNo;

    @BusinessName("税务登记证扫描图")
    @Column(name = "O_TAX_NO_IMG")
    private String taxNoImg;

    @BusinessName(value = "开户行", optionCategory = EOptionCategory.BANK)
    @Column(name = "BNK_CD")
    private String bankShortName;

    @BusinessName("开户行全称")
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

    @BusinessName(value = "银行账户开户省(直辖市)", optionCategory = EOptionCategory.REGION)
    @Column(name = "BNK_OPEN_PROV")
    private String bankOpenProvince;

    @BusinessName(value = "银行账户开户市(县)", optionCategory = EOptionCategory.REGION)
    @Column(name = "BNK_OPEN_CITY")
    private String bankOpenCity;

    @BusinessName("银行账户开户银行网点")
    @Column(name = "BNK_OPEN_BRCH")
    private String bankOpenBranch;

    @Override
    public AgencyApplication clone() {
        AgencyApplication clone = null;
        try {
            clone = (AgencyApplication) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }

    /**
     * @return return the value of the var name
     */

    public String getName() {
        return name;
    }

    /**
     * @param name
     *            Set name value
     */

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return return the value of the var email
     */

    public String getEmail() {
        return email;
    }

    /**
     * @param email
     *            Set email value
     */

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return return the value of the var mobile
     */

    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile
     *            Set mobile value
     */

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @return return the value of the var region
     */

    public String getRegion() {
        return region;
    }

    /**
     * @param region
     *            Set region value
     */

    public void setRegion(String region) {
        this.region = region;
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
     * @return return the value of the var fax
     */

    public String getFax() {
        return fax;
    }

    /**
     * @param fax
     *            Set fax value
     */

    public void setFax(String fax) {
        this.fax = fax;
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
     * @return return the value of the var orgLeagalPersonPhone
     */

    public String getOrgLeagalPersonPhone() {
        return orgLeagalPersonPhone;
    }

    /**
     * @param orgLeagalPersonPhone
     *            Set orgLeagalPersonPhone value
     */

    public void setOrgLeagalPersonPhone(String orgLeagalPersonPhone) {
        this.orgLeagalPersonPhone = orgLeagalPersonPhone;
    }

    /**
     * @return return the value of the var orgLeagalPersonMobile
     */

    public String getOrgLeagalPersonMobile() {
        return orgLeagalPersonMobile;
    }

    /**
     * @param orgLeagalPersonMobile
     *            Set orgLeagalPersonMobile value
     */

    public void setOrgLeagalPersonMobile(String orgLeagalPersonMobile) {
        this.orgLeagalPersonMobile = orgLeagalPersonMobile;
    }

    /**
     * @return return the value of the var orgLeagalPersonQQ
     */

    public String getOrgLeagalPersonQQ() {
        return orgLeagalPersonQQ;
    }

    /**
     * @param orgLeagalPersonQQ
     *            Set orgLeagalPersonQQ value
     */

    public void setOrgLeagalPersonQQ(String orgLeagalPersonQQ) {
        this.orgLeagalPersonQQ = orgLeagalPersonQQ;
    }

    /**
     * @return return the value of the var orgLeagalPersonEmail
     */

    public String getOrgLeagalPersonEmail() {
        return orgLeagalPersonEmail;
    }

    /**
     * @param orgLeagalPersonEmail
     *            Set orgLeagalPersonEmail value
     */

    public void setOrgLeagalPersonEmail(String orgLeagalPersonEmail) {
        this.orgLeagalPersonEmail = orgLeagalPersonEmail;
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
     * @return return the value of the var contactQQ
     */

    public String getContactQQ() {
        return contactQQ;
    }

    /**
     * @param contactQQ
     *            Set contactQQ value
     */

    public void setContactQQ(String contactQQ) {
        this.contactQQ = contactQQ;
    }

    /**
     * @return return the value of the var contactEmail
     */

    public String getContactEmail() {
        return contactEmail;
    }

    /**
     * @param contactEmail
     *            Set contactEmail value
     */

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    /**
     * @return return the value of the var orgNo
     */

    public String getOrgNo() {
        return orgNo;
    }

    /**
     * @param orgNo
     *            Set orgNo value
     */

    public void setOrgNo(String orgNo) {
        this.orgNo = orgNo;
    }

    /**
     * @return return the value of the var orgNoImg
     */

    public String getOrgNoImg() {
        return orgNoImg;
    }

    /**
     * @param orgNoImg
     *            Set orgNoImg value
     */

    public void setOrgNoImg(String orgNoImg) {
        this.orgNoImg = orgNoImg;
    }

    /**
     * @return return the value of the var licenceNo
     */

    public String getLicenceNo() {
        return licenceNo;
    }

    /**
     * @param licenceNo
     *            Set licenceNo value
     */

    public void setLicenceNo(String licenceNo) {
        this.licenceNo = licenceNo;
    }

    /**
     * @return return the value of the var licenceNoImg
     */

    public String getLicenceNoImg() {
        return licenceNoImg;
    }

    /**
     * @param licenceNoImg
     *            Set licenceNoImg value
     */

    public void setLicenceNoImg(String licenceNoImg) {
        this.licenceNoImg = licenceNoImg;
    }

    /**
     * @return return the value of the var taxNo
     */

    public String getTaxNo() {
        return taxNo;
    }

    /**
     * @param taxNo
     *            Set taxNo value
     */

    public void setTaxNo(String taxNo) {
        this.taxNo = taxNo;
    }

    /**
     * @return return the value of the var taxNoImg
     */

    public String getTaxNoImg() {
        return taxNoImg;
    }

    /**
     * @param taxNoImg
     *            Set taxNoImg value
     */

    public void setTaxNoImg(String taxNoImg) {
        this.taxNoImg = taxNoImg;
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

}
