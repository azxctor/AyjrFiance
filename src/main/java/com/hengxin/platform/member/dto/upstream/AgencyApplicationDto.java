/*
 * Project Name: kmfex-platform
 * File Name: Organization.java
 * Class Name: Organization
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
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonProperty;

import com.hengxin.platform.common.converter.OptionCategory;
import com.hengxin.platform.common.domain.DynamicOption;
import com.hengxin.platform.common.enums.EOptionCategory;
import com.hengxin.platform.common.util.FileUploadUtil;
import com.hengxin.platform.common.util.MaskUtil;
import com.hengxin.platform.member.dto.BaseApplicationDto;
import com.hengxin.platform.security.SecurityContext;

/**
 * Class Name: AgencyApplication.
 *
 * @author Ryan
 *
 */
public class AgencyApplicationDto extends BaseApplicationDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String appId;
    
    private String userName;

    private String name;

    private String email;

    private String mobile;

    @OptionCategory(EOptionCategory.REGION)
    private DynamicOption region;

    @OptionCategory(EOptionCategory.ORG_NATURE)
    private DynamicOption orgNature;

    private String orgPhone;

    private String orgAddress;

    private String orgPostCode;

    private String fax;

    private String orgQQ;

    private String orgLegalPerson;

    private String orgLegalPersonIdCardNumber;

    private String orgLegalPersonIdCardFrontImg;

    private String orgLegalPersonIdCardBackImg;

    private String orgLeagalPersonPhone;

    private String orgLeagalPersonMobile;

    private String orgLeagalPersonQQ;

    private String orgLeagalPersonEmail;

    private String bankFullName;

    private String bankAccount;

    private String bankCardFrontImg;

    private String bankAccountName;

    @OptionCategory(EOptionCategory.REGION)
    private DynamicOption bankOpenProvince;

    @OptionCategory(EOptionCategory.REGION)
    private DynamicOption bankOpenCity;

    private String bankOpenBranch;
    
    @OptionCategory(EOptionCategory.BANK)
    private DynamicOption bankShortName;

    private String contact;

    private String contactPhone;

    private String contactMobile;

    private String contactFax;

    private String contactQQ;

    private String contactEmail;

    private String orgNo;

    private String orgNoImg;

    private String licenceNo;

    private String licenceNoImg;

    private String taxNo;

    private String taxNoImg;

    private List<DynamicOption> memberLevelList;
    
    private String orgLegalPersonIdCardFrontImgUrl;
                    
    private String orgLegalPersonIdCardBackImgUrl;
    
    private String orgNoImgUrl;
    
    private String licenceNoImgUrl;
    
    private String taxNoImgUrl;
    
    private String bankCardFrontImgUrl;

    public List<DynamicOption> getMemberLevelList() {
        return memberLevelList;
    }

    public void setMemberLevelList(List<DynamicOption> memberLevelList) {
        this.memberLevelList = memberLevelList;
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
    

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
     * @return return the value of the var orgNature
     */
    public String getOrgPhone() {
        return orgPhone;
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
    @JsonProperty("orgLegalPerson")
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
    @JsonProperty("orgLegalPerson")
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
     * @return return the value of the var orgLegalPersonIdCardNumber
     */
    @JsonProperty("orgLegalPersonIdCardNumber")
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
    @JsonProperty("orgLegalPersonIdCardNumber")
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
    @JsonProperty("orgLegalPersonIdCardFrontImg")
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
    @JsonProperty("orgLegalPersonIdCardFrontImg")
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
    @JsonProperty("orgLegalPersonIdCardBackImg")
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
    @JsonProperty("orgLegalPersonIdCardBackImg")
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
     * @return return the value of the var bankCardFrontImg
     */

    public String getBankCardFrontImg() {
        return bankCardFrontImg;
    }
    
    /**
     * @return return the mask value of the var bankCardFrontImg
     */
    @JsonProperty("bankCardFrontImg")
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
    @JsonProperty("bankCardFrontImg")
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
    @JsonProperty("bankAccountName")
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
    @JsonProperty("bankAccountName")
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
     * @return the contactMobile
     */
    @JsonProperty("contactMobile")
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
    @JsonProperty("contactMobile")
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

    public DynamicOption getBankShortName() {
        return bankShortName;
    }

    public void setBankShortName(DynamicOption bankShortName) {
        this.bankShortName = bankShortName;
    }

    public String getBankAccount() {
        return bankAccount;
    }
    
    @JsonProperty("bankAccount")
    public String getMaskBankAccount() {
        if (SecurityContext.getInstance().cannotViewRealBankCardNo(this.getUserId(), inCanViewPage)) {
            bankAccount = MaskUtil.maskCardNumber(bankAccount);
        }
        return bankAccount;
    }

    @JsonProperty("bankAccount")
    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    @JsonProperty(value = "orgLegalPersonIdCardFrontImgUrl")
    public String getOrgLegalPersonIdCardFrontImgUrl() {
        if (StringUtils.isNotBlank(orgLegalPersonIdCardFrontImg)) {
            return FileUploadUtil.getTempFolder() + orgLegalPersonIdCardFrontImg;
        }
        return "";
    }

    @JsonProperty(value = "orgLegalPersonIdCardBackImgUrl")
    public String getOrgLegalPersonIdCardBackImgUrl() {
        if (StringUtils.isNotBlank(orgLegalPersonIdCardBackImg)) {
            return FileUploadUtil.getTempFolder() + orgLegalPersonIdCardBackImg;
        }
        return "";
    }

    @JsonProperty(value = "orgNoImgUrl")
    public String getOrgNoImgUrl() {
        if (StringUtils.isNotBlank(orgNoImg)) {
            return FileUploadUtil.getTempFolder() + orgNoImg;
        }
        return "";
    }

    @JsonProperty(value = "licenceNoImgUrl")
    public String getLicenceNoImgUrl() {
        if (StringUtils.isNotBlank(licenceNoImg)) {
            return FileUploadUtil.getTempFolder() + licenceNoImg;
        }
        return "";
    }

    @JsonProperty(value = "taxNoImgUrl")
    public String getTaxNoImgUrl() {
        if (StringUtils.isNotBlank(taxNoImg)) {
            return FileUploadUtil.getTempFolder() + taxNoImg;
        }
        return "";
    }

    @JsonProperty(value = "bankCardFrontImgUrl")
    public String getBankCardFrontImgUrl() {
        if (StringUtils.isNotBlank(bankCardFrontImg)) {
            return FileUploadUtil.getTempFolder() + bankCardFrontImg;
        }
        return "";
    }

    public void setOrgLegalPersonIdCardFrontImgUrl(String orgLegalPersonIdCardFrontImgUrl) {
        this.orgLegalPersonIdCardFrontImgUrl = orgLegalPersonIdCardFrontImgUrl;
    }

    public void setOrgLegalPersonIdCardBackImgUrl(String orgLegalPersonIdCardBackImgUrl) {
        this.orgLegalPersonIdCardBackImgUrl = orgLegalPersonIdCardBackImgUrl;
    }

    public void setOrgNoImgUrl(String orgNoImgUrl) {
        this.orgNoImgUrl = orgNoImgUrl;
    }

    public void setLicenceNoImgUrl(String licenceNoImgUrl) {
        this.licenceNoImgUrl = licenceNoImgUrl;
    }

    public void setTaxNoImgUrl(String taxNoImgUrl) {
        this.taxNoImgUrl = taxNoImgUrl;
    }

    public void setBankCardFrontImgUrl(String bankCardFrontImgUrl) {
        this.bankCardFrontImgUrl = bankCardFrontImgUrl;
    }

}
