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

package com.hengxin.platform.member.dto;

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
import com.hengxin.platform.common.enums.EOptionCategory;
import com.hengxin.platform.common.service.validator.ExistIdCardCheck;
import com.hengxin.platform.common.util.FileUploadUtil;
import com.hengxin.platform.common.util.MaskUtil;
import com.hengxin.platform.common.util.SystemDictUtil;
import com.hengxin.platform.escrow.validator.ExceptSelfMobileCheck;
import com.hengxin.platform.escrow.validator.IdCardLegalityCheck;
import com.hengxin.platform.member.dto.OrganizationDto.ContactIdcardExist;
import com.hengxin.platform.member.dto.OrganizationDto.ContactMobileExist;
import com.hengxin.platform.member.enums.EGender;
import com.hengxin.platform.security.SecurityContext;

/**
 * Class Name: Organization
 * 
 * @author runchen
 * 
 */
// 基于基本信息修改页面，隐藏了银行信息填写，取消银行信息验证
// @ExistBankAccountCheck(groups = { BankExist.class }, bankAccount =
// "bankAccount", userId = "userId")
@ExceptSelfMobileCheck(groups = { ContactMobileExist.class }, userId = "userId", mobile = "contactMobile")
@ExistIdCardCheck(groups = { ContactIdcardExist.class }, idCardNumber = "orgLegalPersonIdCardNumber", userId = "userId")
@IdCardLegalityCheck(groups = { ContactIdcardExist.class }, idCardNumber = "orgLegalPersonIdCardNumber",  name = "orgLegalPerson")
public class OrganizationDto extends MemberDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 企业类型.
	 */
	@Valid
	@OptionCategory(EOptionCategory.ORG_TYPE)
	@JsonProperty("org_type")
	private DynamicOption orgType;

	/**
	 * 所属行业.
	 */
	@Valid
	@OptionCategory(EOptionCategory.ORG_INDUSTRY)
	@JsonProperty("industry")
	private DynamicOption orgIndustry;

	/**
	 * 企业性质.
	 */
	@Valid
	// @NotEmpty(groups = { SubmitOrg.class }, message =
	// "{member.error.field.empty}")
	@OptionCategory(EOptionCategory.ORG_NATURE)
	@JsonProperty("nature")
	private DynamicOption orgNature;

	@JsonProperty("postcode")
	@Pattern(regexp = ApplicationConstant.POSTCODE_REGEXP, groups = { SubmitOrg.class }, message = "{member.error.postcode.invaild}")
	private String orgPostCode;

	@NotEmpty(groups = { SubmitOrg.class }, message = "{member.error.field.empty}")
	@JsonProperty("address")
	private String orgAddress;

	@NotEmpty(groups = { SubmitOrg.class }, message = "{member.error.field.empty}")
	@Pattern(regexp = ApplicationConstant.PHONE_REGEXP, groups = { SubmitOrg.class }, message = "{member.error.phone.invaild}")
	@JsonProperty("phone")
	private String orgPhone;

	@JsonProperty("fax")
	@Pattern(regexp = ApplicationConstant.PHONE_REGEXP, groups = { SubmitOrg.class }, message = "{member.error.fax.invaild}")
	private String orgFax;

	/**
	 * 法人QQ.
	 */
	@JsonProperty("qq")
	@Pattern(regexp = ApplicationConstant.QQ_REGEXP, groups = { SubmitOrg.class }, message = "{member.error.qq.invaild}")
	private String orgQQ;

	/**
	 * 企业法人.
	 */
	@NotEmpty(groups = { SubmitOrg.class }, message = "{member.error.field.empty}")
	private String orgLegalPerson;

	@NotEmpty(groups = { SubmitOrg.class }, message = "{member.error.field.empty}")
	@Pattern(regexp = ApplicationConstant.ID_CARD_REGEXP, groups = { SubmitOrg.class }, message = "{member.error.idcard.invaild}")
	private String orgLegalPersonIdCardNumber;

	/**
	 * <Strong>original info instead of mask info when submit the whole
	 * form</strong>.
	 */
	@JsonProperty("legal_id_card_original")
	private String orgLegalPersonIdCardNumberOriginal;

//	@NotEmpty(groups = { SubmitOrg.class }, message = "{member.error.field.empty}")
	private String orgLegalPersonIdCardFrontImg;

	/**
	 * <Strong>original info instead of mask info when submit the whole
	 * form</strong>.
	 */
	@JsonProperty("legal_id_card_front_img_original")
	private String orgLegalPersonIdCardFrontImgOriginal;

//	@NotEmpty(groups = { SubmitOrg.class }, message = "{member.error.field.empty}")
	private String orgLegalPersonIdCardBackImg;

	/**
	 * <Strong>original info instead of mask info when submit the whole
	 * form</strong>.
	 */
	@JsonProperty("legal_id_card_back_img_original")
	private String orgLegalPersonIdCardBackImgOriginal;

	@JsonProperty("legal_age")
	private String orgLegalPersonAge;

	@OptionCategory(EOptionCategory.EDUCATION)
	@JsonProperty("legal_education")
	private DynamicOption orgLegalPersonEduction;

	@JsonProperty("legal_gender")
	private EGender orgLegalPersonGender;

	@JsonProperty("legal_birthday")
	private String orgLegalPersonBirthday;

	/**
	 * 主营业务.
	 */
	@NotEmpty(groups = { SubmitOrg.class }, message = "{member.error.field.empty}")
	@JsonProperty("main_business")
	private String orgMainBusiness;

	/**
	 * 联系人.
	 */
	@NotEmpty(groups = { SubmitOrg.class }, message = "{member.error.field.empty}")
	private String contact;

	@Pattern(regexp = ApplicationConstant.PHONE_REGEXP, groups = { SubmitOrg.class }, message = "{member.error.phone.invaild}")
	@JsonProperty("contact_phone")
	private String contactPhone;

	@NotEmpty(groups = { SubmitOrg.class }, message = "{member.error.field.empty}")
	@Pattern(regexp = ApplicationConstant.MOBILE_REGEXP, groups = { SubmitOrg.class }, message = "{member.error.mobile.invaild}")
	private String contactMobile;

	/**
	 * <Strong>original info instead of mask info when submit the whole
	 * form</strong>.
	 */
	@JsonProperty("contact_mobile_original")
	private String contactMobileOriginal;

	@JsonProperty("contact_fax")
	@Pattern(regexp = ApplicationConstant.PHONE_REGEXP, groups = { SubmitOrg.class }, message = "{member.error.fax.invaild}")
	private String contactFax;

	@JsonProperty("contact_email")
//	@NotEmpty(groups = { SubmitOrg.class }, message = "{member.error.field.empty}")
	@Email(regexp = ApplicationConstant.EMAIL_REGEXP, groups = { SubmitOrg.class }, message = "{member.error.email.invaild}")
	private String contactEmail;

//	@NotEmpty(groups = { SubmitOrg.class }, message = "{member.error.field.empty}")
	@Email(regexp = ApplicationConstant.EMAIL_REGEXP, groups = { SubmitOrg.class }, message = "{member.error.email.invaild}")
	@JsonProperty("legal_email")
	private String orgEmail;

	@NotEmpty(groups = { SubmitOrg.class }, message = "{member.error.field.empty}")
	@Pattern(regexp = ApplicationConstant.MOBILE_REGEXP, groups = { SubmitOrg.class }, message = "{member.error.mobile.invaild}")
	private String orgMobile;

	/**
	 * <Strong>original info instead of mask info when submit the whole
	 * form</strong>.
	 */
	@JsonProperty("legal_mobile_original")
	private String orgMobileOriginal;

	@NotEmpty(groups = { SubmitOrg.class }, message = "{member.error.field.empty}")
	@JsonProperty("org_name")
	private String orgName;

	@Valid
	@OptionCategory(EOptionCategory.REGION)
	@JsonProperty("region")
	private DynamicOption orgRegion;

	private String personIdCardFrontImgUrl;

	private String personIdCardBackImgUrl;

	@JsonProperty("legal_id_card_front_img_url")
	public String getPersonIdCardFrontImgUrl() {
		if (StringUtils.isNotBlank(orgLegalPersonIdCardFrontImg)
				&& !SecurityContext.getInstance().cannotViewRealIdCardNo(this.getUserId(), inCanViewPage)) {
			return FileUploadUtil.getTempFolder() + orgLegalPersonIdCardFrontImg;
		}
		return "";
	}

	@JsonProperty("legal_id_card_back_img_url")
	public String getPersonIdCardBackImgUrl() {
		if (StringUtils.isNotBlank(orgLegalPersonIdCardBackImg)
				&& !SecurityContext.getInstance().cannotViewRealIdCardNo(this.getUserId(), inCanViewPage)) {
			return FileUploadUtil.getTempFolder() + orgLegalPersonIdCardBackImg;
		}
		return "";
	}

	@JsonProperty("org_province")
	public String getProvince() {
		return SystemDictUtil.getMultilayerDetail(orgRegion, 3)[0];
	}

	@JsonProperty("org_city")
	public String getCity() {
		return SystemDictUtil.getMultilayerDetail(orgRegion, 3)[1];
	}

	@JsonProperty("org_county")
	public String getCounty() {
		return SystemDictUtil.getMultilayerDetail(orgRegion, 3)[2];
	}

	/**
	 * @return the orgType
	 */
	public DynamicOption getOrgType() {
		return orgType;
	}

	/**
	 * @param orgType
	 *            the orgType to set
	 */
	public void setOrgType(DynamicOption orgType) {
		this.orgType = orgType;
	}

	/**
	 * @return return the value of the var orgIndustry
	 */

	public DynamicOption getOrgIndustry() {
		return orgIndustry;
	}

	/**
	 * @param orgIndustry
	 *            Set orgIndustry value
	 */

	public void setOrgIndustry(DynamicOption orgIndustry) {
		this.orgIndustry = orgIndustry;
	}

	/**
	 * @return the orgNature
	 */
	public DynamicOption getOrgNature() {
		return orgNature;
	}

	/**
	 * @param orgNature
	 *            the orgNature to set
	 */
	public void setOrgNature(DynamicOption orgNature) {
		this.orgNature = orgNature;
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
	 * @return return the value of the var orgLegalPersonIdCardNumber
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
		this.orgLegalPersonIdCardNumberOriginal = orgLegalPersonIdCardNumber;
	}

	/**
	 * @return return the value of the var orgLegalPersonIdCardFrontImg
	 */

	public String getOrgLegalPersonIdCardFrontImg() {
		return orgLegalPersonIdCardFrontImg;
	}

	/**
	 * @return return the value of the var orgLegalPersonIdCardFrontImg
	 */
	@JsonProperty("legal_id_card_front_img")
	public String getMaskOrgLegalPersonIdCardFrontImg() {
		if (SecurityContext.getInstance().cannotViewRealIdCardNo(this.getUserId(), inCanViewPage)) {
			// orgLegalPersonIdCardFrontImg = "";
		}
		return orgLegalPersonIdCardFrontImg;
	}

	/**
	 * @param orgLegalPersonIdCardFrontImg
	 *            Set orgLegalPersonIdCardFrontImg value
	 */
	@JsonProperty("legal_id_card_front_img")
	public void setOrgLegalPersonIdCardFrontImg(String orgLegalPersonIdCardFrontImg) {
		this.orgLegalPersonIdCardFrontImg = orgLegalPersonIdCardFrontImg;
		this.orgLegalPersonIdCardFrontImgOriginal = orgLegalPersonIdCardFrontImg;
	}

	/**
	 * @return return the value of the var orgLegalPersonIdCardBackImg
	 */

	public String getOrgLegalPersonIdCardBackImg() {
		return orgLegalPersonIdCardBackImg;
	}

	/**
	 * @return return the msak value of the var orgLegalPersonIdCardBackImg
	 */
	@JsonProperty("legal_id_card_back_img")
	public String getMaskOrgLegalPersonIdCardBackImg() {
		if (SecurityContext.getInstance().cannotViewRealIdCardNo(this.getUserId(), inCanViewPage)) {
			// orgLegalPersonIdCardBackImg = "";
		}
		return orgLegalPersonIdCardBackImg;
	}

	/**
	 * @param orgLegalPersonIdCardBackImg
	 *            Set orgLegalPersonIdCardBackImg value
	 */
	@JsonProperty("legal_id_card_back_img")
	public void setOrgLegalPersonIdCardBackImg(String orgLegalPersonIdCardBackImg) {
		this.orgLegalPersonIdCardBackImg = orgLegalPersonIdCardBackImg;
		this.orgLegalPersonIdCardBackImgOriginal = orgLegalPersonIdCardBackImg;
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
	 * @return return the value of the var orgLegalPersonEduction
	 */

	public DynamicOption getOrgLegalPersonEduction() {
		return orgLegalPersonEduction;
	}

	/**
	 * @param orgLegalPersonEduction
	 *            Set orgLegalPersonEduction value
	 */

	public void setOrgLegalPersonEduction(DynamicOption orgLegalPersonEduction) {
		this.orgLegalPersonEduction = orgLegalPersonEduction;
	}

	/**
	 * @return the orgMainBusiness
	 */
	public String getOrgMainBusiness() {
		return orgMainBusiness;
	}

	/**
	 * @param orgMainBusiness
	 *            the orgMainBusiness to set
	 */
	public void setOrgMainBusiness(String orgMainBusiness) {
		this.orgMainBusiness = orgMainBusiness;
	}

	/**
	 * @return return the value of the var contact
	 */

	public String getContact() {
		return contact;
	}

	/**
	 * @return return the value of the var contact
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
	 * @return return the value of the var contactMobile
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
		this.contactMobileOriginal = contactMobile;
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

	public String getOrgFax() {
		return orgFax;
	}

	public void setOrgFax(String orgFax) {
		this.orgFax = orgFax;
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
	 * @return return the value of the var orgMobile
	 */
	@JsonProperty("legal_mobile")
	public String getMaskOrgMobile() {
		if (SecurityContext.getInstance().cannotViewRealPhoneNo(this.getUserId(), inCanViewPage)) {
			orgMobile = MaskUtil.maskPhone(orgMobile);
		}
		return orgMobile;
	}

	/**
	 * @param orgMobile
	 *            Set orgMobile value
	 */
	@JsonProperty("legal_mobile")
	public void setOrgMobile(String orgMobile) {
		this.orgMobile = orgMobile;
		this.orgMobileOriginal = orgMobile;
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

	public DynamicOption getOrgRegion() {
		return orgRegion;
	}

	/**
	 * @param orgRegion
	 *            Set orgRegion value
	 */

	public void setOrgRegion(DynamicOption orgRegion) {
		this.orgRegion = orgRegion;
	}

	/**
	 * @return the orgLegalPersonGender
	 */
	public EGender getOrgLegalPersonGender() {
		return orgLegalPersonGender;
	}

	/**
	 * @param orgLegalPersonGender
	 *            the orgLegalPersonGender to set
	 */
	public void setOrgLegalPersonGender(EGender orgLegalPersonGender) {
		this.orgLegalPersonGender = orgLegalPersonGender;
	}

	/**
	 * @return the orgLegalPersonBirthday
	 */
	public String getOrgLegalPersonBirthday() {
		if (orgLegalPersonBirthday != null && orgLegalPersonBirthday.matches("\\d{4}-\\d{2}-\\d{2}")) {
			orgLegalPersonBirthday = orgLegalPersonBirthday.substring(0, 7);
		}
		return orgLegalPersonBirthday;
	}

	/**
	 * @param orgLegalPersonBirthday
	 *            the orgLegalPersonBirthday to set
	 */
	public void setOrgLegalPersonBirthday(String orgLegalPersonBirthday) {
		this.orgLegalPersonBirthday = orgLegalPersonBirthday;
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

	public void setPersonIdCardFrontImgUrl(String personIdCardFrontImgUrl) {
		this.personIdCardFrontImgUrl = personIdCardFrontImgUrl;
	}

	public void setPersonIdCardBackImgUrl(String personIdCardBackImgUrl) {
		this.personIdCardBackImgUrl = personIdCardBackImgUrl;
	}

	/**
	 * @return the orgLegalPersonIdCardNumberOriginal
	 */
	public String getOrgLegalPersonIdCardNumberOriginal() {
		return orgLegalPersonIdCardNumberOriginal;
	}

	/**
	 * @param orgLegalPersonIdCardNumberOriginal
	 *            the orgLegalPersonIdCardNumberOriginal to set
	 */
	public void setOrgLegalPersonIdCardNumberOriginal(String orgLegalPersonIdCardNumberOriginal) {
		this.orgLegalPersonIdCardNumberOriginal = orgLegalPersonIdCardNumberOriginal;
	}

	/**
	 * @return the orgLegalPersonIdCardFrontImgOriginal
	 */
	public String getOrgLegalPersonIdCardFrontImgOriginal() {
		return orgLegalPersonIdCardFrontImgOriginal;
	}

	/**
	 * @param orgLegalPersonIdCardFrontImgOriginal
	 *            the orgLegalPersonIdCardFrontImgOriginal to set
	 */
	public void setOrgLegalPersonIdCardFrontImgOriginal(String orgLegalPersonIdCardFrontImgOriginal) {
		this.orgLegalPersonIdCardFrontImgOriginal = orgLegalPersonIdCardFrontImgOriginal;
	}

	/**
	 * @return the orgLegalPersonIdCardBackImgOriginal
	 */
	public String getOrgLegalPersonIdCardBackImgOriginal() {
		return orgLegalPersonIdCardBackImgOriginal;
	}

	/**
	 * @param orgLegalPersonIdCardBackImgOriginal
	 *            the orgLegalPersonIdCardBackImgOriginal to set
	 */
	public void setOrgLegalPersonIdCardBackImgOriginal(String orgLegalPersonIdCardBackImgOriginal) {
		this.orgLegalPersonIdCardBackImgOriginal = orgLegalPersonIdCardBackImgOriginal;
	}

	/**
	 * @return the contactMobileOriginal
	 */
	public String getContactMobileOriginal() {
		return contactMobileOriginal;
	}

	/**
	 * @param contactMobileOriginal
	 *            the contactMobileOriginal to set
	 */
	public void setContactMobileOriginal(String contactMobileOriginal) {
		this.contactMobileOriginal = contactMobileOriginal;
	}

	/**
	 * <strong>Fields and methods should be before inner classes.</strong>
	 */
	public interface SubmitOrg {

	}

	/**
	 * <strong>Fields and methods should be before inner classes.</strong>
	 */
	public interface ContactMobileExist {

	}
	
	/**
	 * <strong>Fields and methods should be before inner classes.</strong>
	 */
	public interface ContactIdcardExist {

	}

}
