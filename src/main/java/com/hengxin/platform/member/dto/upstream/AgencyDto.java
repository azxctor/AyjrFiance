package com.hengxin.platform.member.dto.upstream;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

import com.hengxin.platform.common.converter.OptionCategory;
import com.hengxin.platform.common.domain.DynamicOption;
import com.hengxin.platform.common.enums.EOptionCategory;
import com.hengxin.platform.common.util.MaskUtil;
import com.hengxin.platform.security.SecurityContext;

public class AgencyDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String userId;

    private Date createTs;

    @JsonProperty("name")
    private String name;

    private String desc;

    private String email;

    private String mobile;

    @JsonProperty("region")
    private String region;

    @JsonProperty("address")
    private String orgAddress;

    @OptionCategory(EOptionCategory.ORG_NATURE)
    @JsonProperty("nature")
    private DynamicOption orgNature;

    @JsonProperty("phone")
    private String orgPhone;

    @JsonProperty("postcode")
    private String orgPostCode;

    @JsonProperty("fax")
    private String fax;

    private String orgQQ;

    private String orgLegalPerson;

    private String orgLegalPersonIdCardNumber;

    private String orgLegalPersonIdCardFrontImg;

    private String orgLegalPersonIdCardBackImg;

    @JsonProperty("legal_phone")
    private String orgLeagalPersonPhone;

    private String orgLeagalPersonMobile;

    @JsonProperty("legal_qq")
    private String orgLeagalPersonQQ;

    @JsonProperty("legal_email")
    private String orgLeagalPersonEmail;

    private String contact;

    @JsonProperty("contact_phone")
    private String contactPhone;

    private String contactMobile;

    @JsonProperty("contact_fax")
    private String contactFax;

    @JsonProperty("contact_qq")
    private String contactQQ;

    @JsonProperty("contact_email")
    private String contactEmail;

    @JsonProperty("org_no")
    private String orgNo;

    @JsonProperty("org_no_img")
    private String orgNoImg;

    @JsonProperty("licence_no")
    private String licenceNo;

    @JsonProperty("licence_no_img")
    private String licenceNoImg;

    @JsonProperty("tax_no")
    private String taxNo;

    @JsonProperty("tax_no_img")
    private String taxNoImg;

    private String bankAccount;

    @JsonProperty("bank_name")
    private DynamicOption bankname;

    @JsonProperty("bank_full_name")
    private String bankFullName;

    private String bankCardFrontImg;

    private String bankAccountName;

    @JsonProperty("bank_province")
    private String bankOpenProvince;

    @JsonProperty("bank_city")
    private String bankOpenCity;

    @JsonProperty("bank_branch")
    private String bankOpenBranch;

    private boolean inCanViewPage;

    @Override
    public String toString() {
        return "AgencyDto [userId=" + userId + ", createTs=" + createTs + ", name=" + name + ", email=" + email
                + ", mobile=" + mobile + ", region=" + region + ", orgAddress=" + orgAddress + ", orgNature="
                + orgNature + ", orgPhone=" + orgPhone + ", orgPostCode=" + orgPostCode + ", fax=" + fax + ", orgQQ="
                + orgQQ + ", orgLegalPerson=" + orgLegalPerson + ", orgLegalPersonIdCardNumber="
                + orgLegalPersonIdCardNumber + ", orgLegalPersonIdCardFrontImg=" + orgLegalPersonIdCardFrontImg
                + ", orgLegalPersonIdCardBackImg=" + orgLegalPersonIdCardBackImg + ", orgLeagalPersonPhone="
                + orgLeagalPersonPhone + ", orgLeagalPersonMobile=" + orgLeagalPersonMobile + ", orgLeagalPersonQQ="
                + orgLeagalPersonQQ + ", orgLeagalPersonEmail=" + orgLeagalPersonEmail + ", contact=" + contact
                + ", contactPhone=" + contactPhone + ", contactMobile=" + contactMobile + ", contactFax=" + contactFax
                + ", contactQQ=" + contactQQ + ", contactEmail=" + contactEmail + ", orgNo=" + orgNo + ", orgNoImg="
                + orgNoImg + ", licenceNo=" + licenceNo + ", licenceNoImg=" + licenceNoImg + ", taxNo=" + taxNo
                + ", taxNoImg=" + taxNoImg + ", bankAccount=" + bankAccount + ", bankname=" + bankname
                + ", bankFullName=" + bankFullName + ", bankCardFrontImg=" + bankCardFrontImg + ", bankAccountName="
                + bankAccountName + ", bankOpenProvince=" + bankOpenProvince + ", bankOpenCity=" + bankOpenCity
                + ", bankOpenBranch=" + bankOpenBranch + "]";
    }

    /**
     * @return return the value of the var userId
     */

    public String getUserId() {
        return userId;
    }

    /**
     * @param userId
     *            Set userId value
     */

    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return return the value of the var createTs
     */

    public Date getCreateTs() {
        return createTs;
    }

    /**
     * @param createTs
     *            Set createTs value
     */

    public void setCreateTs(Date createTs) {
        this.createTs = createTs;
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
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
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
     * @return return the mask value of the var mobile
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
     * @return return the value of the var orgNature
     */

    public DynamicOption getOrgNature() {
        return orgNature;
    }

    /**
     * @param orgNature
     *            Set orgNature value
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
     * @return return the mask value of the var orgLegalPerson
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
     * @return return the msak value of the var orgLegalPersonIdCardNumber
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
     * @return return the value of the var orgLeagalPersonMobile
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
     * @return return the value of the var contact
     */

    public String getContact() {
        return contact;
    }
    
    /**
     * @return return the mask value of the var contact
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
     *            Set contact value
     */
    @JsonProperty("contact")
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
     * @return return the mask value of the var contactMobile
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
     *            Set contactMobile value
     */
    @JsonProperty("contact_mobile")
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
     * @return return the value of the var bankAccount
     */

    public String getBankAccount() {
        return bankAccount;
    }
    
    /**
     * @return return the mask value of the var bankAccount
     */
    @JsonProperty("bank_account")
    public String geMasktBankAccount() {
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
     * @return the bankAccountName
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
     *            the bankAccountName to set
     */
    @JsonProperty("bank_account_name")
    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    /**
     * @return the bankOpenProvince
     */
    public String getBankOpenProvince() {
        return bankOpenProvince;
    }

    /**
     * @param bankOpenProvince
     *            the bankOpenProvince to set
     */
    public void setBankOpenProvince(String bankOpenProvince) {
        this.bankOpenProvince = bankOpenProvince;
    }

    /**
     * @return the bankOpenCity
     */
    public String getBankOpenCity() {
        return bankOpenCity;
    }

    /**
     * @param bankOpenCity
     *            the bankOpenCity to set
     */
    public void setBankOpenCity(String bankOpenCity) {
        this.bankOpenCity = bankOpenCity;
    }

    /**
     * @return the bankOpenBranch
     */
    public String getBankOpenBranch() {
        return bankOpenBranch;
    }

    /**
     * @param bankOpenBranch
     *            the bankOpenBranch to set
     */
    public void setBankOpenBranch(String bankOpenBranch) {
        this.bankOpenBranch = bankOpenBranch;
    }

    /**
     * @return the bankname
     */
    public DynamicOption getBankname() {
        return bankname;
    }

    /**
     * @param bankname
     *            the bankname to set
     */
    public void setBankname(DynamicOption bankname) {
        this.bankname = bankname;
    }

    public String getBankFullName() {
        return bankFullName;
    }

    public void setBankFullName(String bankFullName) {
        this.bankFullName = bankFullName;
    }

    public String getBankCardFrontImg() {
        return bankCardFrontImg;
    }
    
    @JsonProperty("bank_card_img")
    public String getMaskBankCardFrontImg() {
        if (SecurityContext.getInstance().cannotViewRealBankCardNo(this.getUserId(), inCanViewPage)) {
            bankCardFrontImg = "";
        }
        return bankCardFrontImg;
    }

    @JsonProperty("bank_card_img")
    public void setBankCardFrontImg(String bankCardFrontImg) {
        this.bankCardFrontImg = bankCardFrontImg;
    }

    /**
     * @return return the value of the var inCanViewPage
     */

    public boolean isInCanViewPage() {
        return inCanViewPage;
    }

    /**
     * @param inCanViewPage
     *            Set inCanViewPage value
     */

    public void setInCanViewPage(boolean inCanViewPage) {
        this.inCanViewPage = inCanViewPage;
    }

}
