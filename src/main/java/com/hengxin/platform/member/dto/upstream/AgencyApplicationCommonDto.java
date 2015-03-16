/*
 * Project Name: kmfex-platform
 * File Name: AgencyApplicationCommonDto.java
 * Class Name: AgencyApplicationCommonDto
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

package com.hengxin.platform.member.dto.upstream;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.hengxin.platform.common.constant.ApplicationConstant;
import com.hengxin.platform.common.converter.OptionCategory;
import com.hengxin.platform.common.domain.DynamicOption;
import com.hengxin.platform.common.dto.upstream.AgencyRegisterInfoDto.SubmitAgency;
import com.hengxin.platform.common.enums.EOptionCategory;
import com.hengxin.platform.common.service.validator.ExistAuthzdCenterCheck;
import com.hengxin.platform.common.service.validator.ExistIdCardCheck;
import com.hengxin.platform.common.service.validator.OrganizationCodeUniqueCheck;
import com.hengxin.platform.common.util.FileUploadUtil;
import com.hengxin.platform.common.util.MaskUtil;
import com.hengxin.platform.common.util.SystemDictUtil;
import com.hengxin.platform.escrow.validator.ExceptSelfMobileCheck;
import com.hengxin.platform.escrow.validator.IdCardLegalityCheck;
import com.hengxin.platform.member.dto.BaseApplicationDto;
import com.hengxin.platform.member.dto.FinancierDto.SubmitOrgCode;
import com.hengxin.platform.security.SecurityContext;

/**
 * Class Name: AgencyApplicationCommonDto
 * 
 * @author shengzhou
 * 
 */
//@ExistBankAccountCheck(groups = { SubmitAgency.class }, bankAccount = "bankAccount", userId = "userId")
@ExistAuthzdCenterCheck(groups = { SubmitAgency.class }, authzdCenter = "name", userId = "userId")
@OrganizationCodeUniqueCheck(groups = { SubmitOrgCode.class }, orgId = "userId", orgCode = "orgNo", type = "O")
@ExistIdCardCheck(groups = { SubmitAgency.class }, idCardNumber = "orgLegalPersonIdCardNumber", userId = "userId")
@IdCardLegalityCheck(groups = { SubmitAgency.class }, idCardNumber = "orgLegalPersonIdCardNumber",  name = "orgLegalPerson")
@ExceptSelfMobileCheck(groups = { SubmitAgency.class }, userId = "userId", mobile = "contactMobile")
public class AgencyApplicationCommonDto extends BaseApplicationDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String appId;

    private String latestTs;

    @NotEmpty(groups = { SubmitAgency.class }, message = "{member.error.field.empty}")
    @JsonProperty("name")
    private String name;

    private String email;

    private String mobile;

    @Valid
    @JsonProperty("region")
    @OptionCategory(EOptionCategory.REGION)
    private DynamicOption region;

    @Valid
//    @NotEmpty(groups = { SubmitAgency.class }, message = "{member.error.field.empty}")
    @JsonProperty("nature")
    @OptionCategory(EOptionCategory.ORG_NATURE)
    private DynamicOption orgNature;

    @NotEmpty(groups = { SubmitAgency.class }, message = "{member.error.field.empty}")
    @Pattern(groups = { SubmitAgency.class }, regexp = ApplicationConstant.PHONE_REGEXP, message = "{member.error.phone.invaild}")
    @JsonProperty("phone")
    private String orgPhone;

    @NotEmpty(groups = { SubmitAgency.class }, message = "{member.error.field.empty}")
    @JsonProperty("address")
    private String orgAddress;

    @Pattern(groups = { SubmitAgency.class }, regexp = ApplicationConstant.POSTCODE_REGEXP, message = "{member.error.postcode.invaild}")
    @JsonProperty("postcode")
    private String orgPostCode;

    @Pattern(groups = { SubmitAgency.class }, regexp = ApplicationConstant.PHONE_REGEXP, message = "{member.error.fax.invaild}")
    @JsonProperty("fax")
    private String fax;

    private String orgQQ;

    @NotEmpty(groups = { SubmitAgency.class }, message = "{member.error.field.empty}")
    private String orgLegalPerson;

    @NotEmpty(groups = { SubmitAgency.class }, message = "{member.error.field.empty}")
    @Pattern(groups = { SubmitAgency.class }, regexp = ApplicationConstant.ID_CARD_REGEXP, message = "{member.error.idcard.invaild}")
    private String orgLegalPersonIdCardNumber;

//    @NotEmpty(groups = { SubmitAgency.class }, message = "{member.error.field.empty}")
    private String orgLegalPersonIdCardFrontImg;

//    @NotEmpty(groups = { SubmitAgency.class }, message = "{member.error.field.empty}")
    private String orgLegalPersonIdCardBackImg;

    @NotEmpty(groups = { SubmitAgency.class }, message = "{member.error.field.empty}")
    @JsonProperty("legal_phone")
    @Pattern(groups = { SubmitAgency.class }, regexp = ApplicationConstant.PHONE_REGEXP, message = "{member.error.phone.invaild}")
    private String orgLeagalPersonPhone;

    private String orgLeagalPersonMobile;

    @Pattern(groups = { SubmitAgency.class }, regexp = ApplicationConstant.QQ_REGEXP, message = "{member.error.qq.invaild}")
    @JsonProperty("legal_qq")
    private String orgLeagalPersonQQ;

    @Email(regexp = ApplicationConstant.EMAIL_REGEXP, groups = { SubmitAgency.class }, message = "{member.error.email.invaild}")
    @JsonProperty("legal_email")
    private String orgLeagalPersonEmail;

    @NotEmpty(groups = { SubmitAgency.class }, message = "{member.error.field.empty}")
    private String contact;

    @Pattern(groups = { SubmitAgency.class }, regexp = ApplicationConstant.PHONE_REGEXP, message = "{member.error.phone.invaild}")
    @JsonProperty("contact_phone")
    private String contactPhone;

    @NotEmpty(groups = { SubmitAgency.class }, message = "{member.error.field.empty}")
    @Pattern(groups = { SubmitAgency.class }, regexp = ApplicationConstant.MOBILE_REGEXP, message = "{member.error.mobile.invaild}")
    private String contactMobile;

    @Pattern(groups = { SubmitAgency.class }, regexp = ApplicationConstant.PHONE_REGEXP, message = "{member.error.fax.invaild}")
    @JsonProperty("contact_fax")
    private String contactFax;

    @Pattern(groups = { SubmitAgency.class }, regexp = ApplicationConstant.QQ_REGEXP, message = "{member.error.qq.invaild}")
    @JsonProperty("contact_qq")
    private String contactQQ;

    @Email(regexp = ApplicationConstant.EMAIL_REGEXP, groups = { SubmitAgency.class }, message = "{member.error.email.invaild}")
    @JsonProperty("contact_email")
    private String contactEmail;

    @NotEmpty(groups = { SubmitAgency.class }, message = "{member.error.field.empty}")
    @JsonProperty("org_no")
    private String orgNo;

    @NotEmpty(groups = { SubmitAgency.class }, message = "{member.error.field.empty}")
    @JsonProperty("org_no_img")
    private String orgNoImg;

    @NotEmpty(groups = { SubmitAgency.class }, message = "{member.error.field.empty}")
    @JsonProperty("licence_no")
    private String licenceNo;

    @NotEmpty(groups = { SubmitAgency.class }, message = "{member.error.field.empty}")
    @JsonProperty("licence_no_img")
    private String licenceNoImg;

    @JsonProperty("tax_no")
    private String taxNo;

    @JsonProperty("tax_no_img")
    private String taxNoImg;

//    @NotEmpty(groups = { SubmitAgency.class }, message = "{member.error.field.empty}")
//    @Pattern(groups = { SubmitAgency.class }, regexp = ApplicationConstant.BANK_CARD_REGEXP, message = "{member.error.bank.invaild}")
    private String bankAccount;

    @JsonProperty("bank_full_name")
    private String bankFullName;

//    @NotEmpty(groups = { SubmitAgency.class }, message = "{member.error.field.empty}")
    private String bankCardFrontImg;

//    @NotEmpty(groups = { SubmitAgency.class }, message = "{member.error.field.empty}")
    private String bankAccountName;

//    @Valid
    @JsonProperty("bank_province")
    private DynamicOption bankOpenProvince;

//    @Valid
    @JsonProperty("bank_city")
    private DynamicOption bankOpenCity;

//    @NotEmpty(groups = { SubmitAgency.class}, message = "{member.error.field.empty}")
    @JsonProperty("bank_branch")
    private String bankOpenBranch;
    
//    @Valid
    @JsonProperty("bank_name")
    @OptionCategory(EOptionCategory.BANK)
    private DynamicOption bankShortName;

    @JsonProperty("basic_province")
    public String getProvince() {
        return SystemDictUtil.getMultilayerDetail(region, 3)[0];
    }

    @JsonProperty("basic_city")
    public String getCity() {
        return SystemDictUtil.getMultilayerDetail(region, 3)[1];
    }

    @JsonProperty("basic_county")
    public String getCounty() {
        return SystemDictUtil.getMultilayerDetail(region, 3)[2];
    }

    @JsonProperty("legal_idcard_img1_url")
    public String getOrgLegalPersonIdCardFrontImgUrl() {
        if (StringUtils.isNotBlank(orgLegalPersonIdCardFrontImg)) {
            return FileUploadUtil.getTempFolder() + orgLegalPersonIdCardFrontImg;
        }
        return "";
    }

    @JsonProperty("legal_idcard_img2_url")
    public String getOrgLegalPersonIdCardBackImgUrl() {
        if (StringUtils.isNotBlank(orgLegalPersonIdCardBackImg)) {
            return FileUploadUtil.getTempFolder() + orgLegalPersonIdCardBackImg;
        }
        return "";
    }

    @JsonProperty("bank_card_img_url")
    public String getBankCardFrontImgUrl() {
        if (StringUtils.isNotBlank(bankCardFrontImg)) {
            return FileUploadUtil.getTempFolder() + bankCardFrontImg;
        }
        return "";
    }

    @JsonProperty("org_no_img_url")
    public String getOrgNoImgUrl() {
        if (StringUtils.isNotBlank(orgNoImg)) {
            return FileUploadUtil.getTempFolder() + orgNoImg;
        }
        return "";
    }

    @JsonProperty("licence_no_img_url")
    public String getLicenceNoImgUrl() {
        if (StringUtils.isNotBlank(licenceNoImg)) {
            return FileUploadUtil.getTempFolder() + licenceNoImg;
        }
        return "";
    }

    @JsonProperty("tax_no_img_url")
    public String getTaxNoImgUrl() {
        if (StringUtils.isNotBlank(taxNoImg)) {
            return FileUploadUtil.getTempFolder() + taxNoImg;
        }
        return "";
    }

    /**
     * @return return the value of the var appId
     */

    @Override
    public String getAppId() {
        return appId;
    }

    /**
     * @param appId
     *            Set appId value
     */

    @Override
    public void setAppId(String appId) {
        this.appId = appId;
    }

    /**
     * @return return the value of the var name
     */

    public String getName() {
        return name;
    }

    /**
     * @return return the value of the var latestTs
     */

    @Override
    public String getLatestTs() {
        return latestTs;
    }

    /**
     * @param latestTs
     *            Set latestTs value
     */

    @Override
    public void setLatestTs(String latestTs) {
        this.latestTs = latestTs;
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
     * @return return the value of the var mobile
     */
    @JsonProperty("mobile")
    public String getMaskMobile() {
        if (SecurityContext.getInstance().cannotViewRealPhoneNo(this.getUserId(), inCanViewPage)) {
            mobile = MaskUtil.maskPhone(mobile);
        }
        return mobile;
    }

    /**
     * @param mobile
     *            Set mobile value
     */
    @JsonProperty("mobile")
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @return return the value of the var region
     */

    public DynamicOption getRegion() {
        return region;
    }

    /**
     * @param region
     *            Set region value
     */

    public void setRegion(DynamicOption region) {
        this.region = region;
    }

    /**
	 * @return the orgNature
	 */
	public DynamicOption getOrgNature() {
		return orgNature;
	}

	/**
	 * @param orgNature the orgNature to set
	 */
	public void setOrgNature(DynamicOption orgNature) {
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
     * @return return the value of the var orgLegalPerson
     */
    @JsonProperty("legal_person")
    public String getMaskOrgLegalPerson() {
        if (SecurityContext.getInstance().cannotViewRealName(this.getUserId(), inCanViewPage)) {
            orgLegalPerson = MaskUtil.maskChinsesName(orgLegalPerson);
        }
        return orgLegalPerson;
    }

    /**
     * @param orgLegalPerson
     *            Set orgLegalPerson value
     */
    @JsonProperty("legal_person")
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
     * @return return the mask value of the var orgLegalPersonIdCardNumber
     */
    @JsonProperty("legal_id_card")
    public String getMaskOrgLegalPersonIdCardNumber() {
        if (SecurityContext.getInstance().cannotViewRealIdCardNo(this.getUserId(), inCanViewPage)) {
            orgLegalPersonIdCardNumber = MaskUtil.maskCardNumber(orgLegalPersonIdCardNumber);
        }
        return orgLegalPersonIdCardNumber;
    }
    
    /**
     * @param orgLegalPersonIdCardNumber
     *            Set orgLegalPersonIdCardNumber value
     */
    @JsonProperty("legal_id_card")
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
     * @return return the mask value of the var orgLegalPersonIdCardFrontImg
     */
    @JsonProperty("legal_idcard_img1")
    public String getMaskOrgLegalPersonIdCardFrontImg() {
        if (SecurityContext.getInstance().cannotViewRealIdCardNo(this.getUserId(), inCanViewPage)) {
            orgLegalPersonIdCardFrontImg = "";
        }
        return orgLegalPersonIdCardFrontImg;
    }

    /**
     * @param orgLegalPersonIdCardFrontImg
     *            Set orgLegalPersonIdCardFrontImg value
     */
    @JsonProperty("legal_idcard_img1")
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
     * @return return the mask value of the var orgLegalPersonIdCardBackImg
     */
    @JsonProperty("legal_idcard_img2")
    public String getMaskOrgLegalPersonIdCardBackImg() {
        if (SecurityContext.getInstance().cannotViewRealIdCardNo(this.getUserId(), inCanViewPage)) {
            orgLegalPersonIdCardBackImg = "";
        }
        return orgLegalPersonIdCardBackImg;
    }

    /**
     * @param orgLegalPersonIdCardBackImg
     *            Set orgLegalPersonIdCardBackImg value
     */
    @JsonProperty("legal_idcard_img2")
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
     * @return return the mask value of the var orgLeagalPersonMobile
     */
    @JsonProperty("orgLeagalPersonMobile")
    public String getMaskOrgLeagalPersonMobile() {
        if (SecurityContext.getInstance().cannotViewRealPhoneNo(this.getUserId(), inCanViewPage)) {
            orgLeagalPersonMobile = MaskUtil.maskPhone(orgLeagalPersonMobile);
        }
        return orgLeagalPersonMobile;
    }

    /**
     * @param orgLeagalPersonMobile
     *            Set orgLeagalPersonMobile value
     */
    @JsonProperty("orgLeagalPersonMobile")
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
     * @return return the value of the var bankShortName
     */

    public DynamicOption getBankShortName() {
        return bankShortName;
    }

    /**
     * @param bankShortName
     *            Set bankShortName value
     */

    public void setBankShortName(DynamicOption bankShortName) {
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
     * @return return the value of the var bankAccount
     */
    @JsonProperty("bank_account")
    public String getMaskBankAccount() {
        if (SecurityContext.getInstance().cannotViewRealBankCardNo(this.getUserId(), inCanViewPage)) {
            bankAccount = MaskUtil.maskCardNumber(bankAccount);
        }
        return bankAccount;
    }

    /**
     * @param bankAccount
     *            Set bankAccount value
     */
    @JsonProperty("bank_account")
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
     * @return return the mask value of the var bankCardFrontImg
     */
    @JsonProperty("bank_card_img")
    public String getMaskBankCardFrontImg() {
        if (SecurityContext.getInstance().cannotViewRealBankCardNo(this.getUserId(), inCanViewPage)) {
            bankCardFrontImg = "";
        }
        return bankCardFrontImg;
    }

    /**
     * @param bankCardFrontImg
     *            Set bankCardFrontImg value
     */
    @JsonProperty("bank_card_img")
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
     * @return return the mask value of the var bankAccountName
     */
    @JsonProperty("bank_account_name")
    public String getMaskBankAccountName() {
        if (SecurityContext.getInstance().cannotViewRealBankCardNo(this.getUserId(), inCanViewPage)
                || SecurityContext.getInstance().cannotViewRealName(this.getUserId(), inCanViewPage)) {
            bankAccountName = MaskUtil.maskChinsesName(bankAccountName);
        }
        return bankAccountName;
    }
    
    /**
     * @param bankAccountName
     *            Set bankAccountName value
     */
    @JsonProperty("bank_account_name")
    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    /**
	 * @return the bankOpenProvince
	 */
	public DynamicOption getBankOpenProvince() {
		return bankOpenProvince;
	}

	/**
	 * @param bankOpenProvince the bankOpenProvince to set
	 */
	public void setBankOpenProvince(DynamicOption bankOpenProvince) {
		this.bankOpenProvince = bankOpenProvince;
	}

	/**
	 * @return the bankOpenCity
	 */
	public DynamicOption getBankOpenCity() {
		return bankOpenCity;
	}

	/**
	 * @param bankOpenCity the bankOpenCity to set
	 */
	public void setBankOpenCity(DynamicOption bankOpenCity) {
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

    /**
     * @return the contact
     */
    public String getContact() {
        return contact;
    }
    
    /**
     * @return the contact
     */
    @JsonProperty("contact")
    public String getMaskContact() {
        if (SecurityContext.getInstance().cannotViewRealName(this.getUserId(), inCanViewPage)) {
            contact = MaskUtil.maskChinsesName(contact);
        }
        return contact;
    }

    /**
     * @param contact
     *            the contact to set
     */
    @JsonProperty("contact")
    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * @return the contactPhone
     */
    public String getContactPhone() {
        return contactPhone;
    }

    /**
     * @param contactPhone
     *            the contactPhone to set
     */
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    /**
     * @return the contactMobile
     */
    public String getContactMobile() {
        return contactMobile;
    }
    
    /**
     * @return the mask contactMobile
     */
    @JsonProperty("contact_mobile")
    public String getMaskContactMobile() {
        if (SecurityContext.getInstance().cannotViewRealPhoneNo(this.getUserId(), inCanViewPage)) {
            contactMobile = MaskUtil.maskPhone(contactMobile);
        }
        return contactMobile;
    }

    /**
     * @param contactMobile
     *            the contactMobile to set
     */
    @JsonProperty("contact_mobile")
    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    /**
     * @return the contactFax
     */
    public String getContactFax() {
        return contactFax;
    }

    /**
     * @param contactFax
     *            the contactFax to set
     */
    public void setContactFax(String contactFax) {
        this.contactFax = contactFax;
    }

    /**
     * @return the contactQQ
     */
    public String getContactQQ() {
        return contactQQ;
    }

    /**
     * @param contactQQ
     *            the contactQQ to set
     */
    public void setContactQQ(String contactQQ) {
        this.contactQQ = contactQQ;
    }

    /**
     * @return the contactEmail
     */
    public String getContactEmail() {
        return contactEmail;
    }

    /**
     * @param contactEmail
     *            the contactEmail to set
     */
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    /**
     * @return the orgNo
     */
    public String getOrgNo() {
        return orgNo;
    }

    /**
     * @param orgNo
     *            the orgNo to set
     */
    public void setOrgNo(String orgNo) {
        this.orgNo = orgNo;
    }

    /**
     * @return the orgNoImg
     */
    public String getOrgNoImg() {
        return orgNoImg;
    }

    /**
     * @param orgNoImg
     *            the orgNoImg to set
     */
    public void setOrgNoImg(String orgNoImg) {
        this.orgNoImg = orgNoImg;
    }

    /**
     * @return the licenceNo
     */
    public String getLicenceNo() {
        return licenceNo;
    }

    /**
     * @param licenceNo
     *            the licenceNo to set
     */
    public void setLicenceNo(String licenceNo) {
        this.licenceNo = licenceNo;
    }

    /**
     * @return the licenceNoImg
     */
    public String getLicenceNoImg() {
        return licenceNoImg;
    }

    /**
     * @param licenceNoImg
     *            the licenceNoImg to set
     */
    public void setLicenceNoImg(String licenceNoImg) {
        this.licenceNoImg = licenceNoImg;
    }

    /**
     * @return the taxNo
     */
    public String getTaxNo() {
        return taxNo;
    }

    /**
     * @param taxNo
     *            the taxNo to set
     */
    public void setTaxNo(String taxNo) {
        this.taxNo = taxNo;
    }

    /**
     * @return the taxNoImg
     */
    public String getTaxNoImg() {
        return taxNoImg;
    }

    /**
     * @param taxNoImg
     *            the taxNoImg to set
     */
    public void setTaxNoImg(String taxNoImg) {
        this.taxNoImg = taxNoImg;
    }

}
