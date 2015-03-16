/*
 * Project Name: kmfex-platform
 * File Name: Person.java
 * Class Name: Person
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
import com.hengxin.platform.member.dto.PersonDto.IdCardExist;
import com.hengxin.platform.member.dto.PersonDto.PersionMobileExist;
import com.hengxin.platform.member.enums.EGender;
import com.hengxin.platform.security.SecurityContext;

/**
 * Class Name: Person
 * 
 * @author runchen
 * 
 */
// 基于基本信息修改页面，隐藏了银行信息填写，取消银行信息验证
// @ExistBankAccountCheck(groups = { BankExist.class }, bankAccount =
// "bankAccount", userId = "userId")
@ExistIdCardCheck(groups = { IdCardExist.class }, idCardNumber = "personIdCardNumber", userId = "userId")
@IdCardLegalityCheck(groups = { IdCardExist.class }, idCardNumber = "personIdCardNumber",  name = "personName")
@ExceptSelfMobileCheck(groups = {PersionMobileExist.class }, userId = "userId", mobile = "personMobile")
public class PersonDto extends MemberDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("gender")
	private EGender personSex;

	@Pattern(regexp = ApplicationConstant.PHONE_REGEXP, groups = { SubmitPerson.class }, message = "{member.error.phone.invaild}")
	@JsonProperty("phone")
	private String personPhone;

	@NotEmpty(groups = { SubmitPerson.class }, message = "{member.error.field.empty}")
	@JsonProperty("address")
	private String personAddress;

	@JsonProperty("postcode")
	@Pattern(regexp = ApplicationConstant.POSTCODE_REGEXP, groups = { SubmitPerson.class }, message = "{member.error.postcode.invaild}")
	private String personPostCode;

	@JsonProperty("qq")
	@Pattern(regexp = ApplicationConstant.QQ_REGEXP, groups = { SubmitPerson.class }, message = "{member.error.qq.invaild}")
	private String personQQ;

	@JsonProperty("birthday")
	private String personBirthday;

	@NotEmpty(groups = { SubmitPerson.class }, message = "{member.error.field.empty}")
	@Pattern(regexp = ApplicationConstant.ID_CARD_REGEXP, groups = { SubmitPerson.class }, message = "{member.error.idcard.invaild}")
	private String personIdCardNumber;

	/**
	 * <Strong>original info instead of mask info when submit the whole
	 * form</strong>.
	 */
	@JsonProperty("id_card_number_original")
	private String personIdCardNumberOriginal;

//	@NotEmpty(groups = { SubmitPerson.class }, message = "{member.error.field.empty}")
	private String personIdCardFrontImg;

//	@NotEmpty(groups = { SubmitPerson.class }, message = "{member.error.field.empty}")
	private String personIdCardBackImg;

	@Valid
	@OptionCategory(EOptionCategory.JOB)
	@JsonProperty("job")
	private DynamicOption personJob;

	@JsonProperty("age")
	private String personAge;

	@OptionCategory(EOptionCategory.EDUCATION)
	@JsonProperty("education")
	private DynamicOption personEducation;

//	@NotEmpty(groups = { SubmitPerson.class }, message = "{member.error.field.empty}")
	@Email(regexp = ApplicationConstant.EMAIL_REGEXP, groups = { SubmitPerson.class }, message = "{member.error.email.invaild}")
	@JsonProperty("email")
	private String personEmail;

	@NotEmpty(groups = { SubmitPerson.class }, message = "{member.error.field.empty}")
	@Pattern(regexp = ApplicationConstant.MOBILE_REGEXP, groups = { SubmitPerson.class }, message = "{member.error.mobile.invaild}")
	private String personMobile;

	/**
	 * <Strong>original info instead of mask info when submit the whole
	 * form</strong>.
	 */
	@JsonProperty("mobile_original")
	private String personMobileOriginal;

	@NotEmpty(groups = { SubmitPerson.class }, message = "{member.error.field.empty}")
	private String personName;

	/**
	 * <Strong>original info instead of mask info when submit the whole
	 * form</strong>.
	 */
	@JsonProperty("real_name_original")
	private String personNameOriginal;

	@Valid
	@OptionCategory(EOptionCategory.REGION)
	@JsonProperty("region")
	private DynamicOption personRegion;

	private String personIdCardFrontImgUrl;

	private String personIdCardBackImgUrl;

	/**
	 * <Strong>original info instead of mask info when submit the whole
	 * form</strong>.
	 */
	@JsonProperty("id_card_front_img_url_original")
	private String personIdCardFrontImgUrlOriginal;

	/**
	 * <Strong>original info instead of mask info when submit the whole
	 * form</strong>.
	 */
	@JsonProperty("id_card_back_img_url_original")
	private String personIdCardBackImgUrlOriginal;

	@JsonProperty("id_card_front_img_url")
	public String getPersonIdCardFrontImgUrl() {
		if (StringUtils.isNotBlank(personIdCardFrontImg)
				&& !SecurityContext.getInstance().cannotViewRealIdCardNo(this.getUserId(), inCanViewPage)) {
			return FileUploadUtil.getTempFolder() + personIdCardFrontImg;
		}
		return "";
	}

	@JsonProperty("id_card_back_img_url")
	public String getPersonIdCardBackImgUrl() {
		if (StringUtils.isNotBlank(personIdCardBackImg)
				&& !SecurityContext.getInstance().cannotViewRealIdCardNo(this.getUserId(), inCanViewPage)) {
			return FileUploadUtil.getTempFolder() + personIdCardBackImg;
		}
		return "";
	}

	@JsonProperty("person_province")
	public String getProvince() {
		return SystemDictUtil.getMultilayerDetail(personRegion, 3)[0];
	}

	@JsonProperty("person_city")
	public String getCity() {
		return SystemDictUtil.getMultilayerDetail(personRegion, 3)[1];
	}

	@JsonProperty("person_county")
	public String getCounty() {
		return SystemDictUtil.getMultilayerDetail(personRegion, 3)[2];
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

	public String getPersonBirthday() {
		if (personBirthday != null && personBirthday.matches("\\d{4}-\\d{2}-\\d{2}")) {
			personBirthday = personBirthday.substring(0, 7);
		}
		return personBirthday;
	}

	/**
	 * @param personBirthday
	 *            Set personBirthday value
	 */

	public void setPersonBirthday(String personBirthday) {
		this.personBirthday = personBirthday;
	}

	/**
	 * @return return the value of the var personIdCardNumber
	 */
	public String getPersonIdCardNumber() {
		return personIdCardNumber;
	}

	/**
	 * @return return the mask value of the var personIdCardNumber
	 */
	@JsonProperty("id_card_number")
	public String getMaskPersonIdCardNumber() {
		if (SecurityContext.getInstance().cannotViewRealIdCardNo(this.getUserId(), inCanViewPage)) {
			personIdCardNumber = MaskUtil.maskCardNumber(personIdCardNumber);
		}
		return personIdCardNumber;
	}

	/**
	 * @param personIdCardNumber
	 *            Set personIdCardNumber value
	 */
	@JsonProperty("id_card_number")
	public void setPersonIdCardNumber(String personIdCardNumber) {
		this.personIdCardNumber = personIdCardNumber;
		this.personIdCardNumberOriginal = personIdCardNumber;
	}

	/**
	 * @return return the value of the var personIdCardFrontImg
	 */

	public String getPersonIdCardFrontImg() {
		return personIdCardFrontImg;
	}

	/**
	 * @return return the value of the var personIdCardFrontImg
	 */
	@JsonProperty("id_card_front_img")
	public String getMaskPersonIdCardFrontImg() {
		if (SecurityContext.getInstance().cannotViewRealIdCardNo(this.getUserId(), inCanViewPage)) {
			/** Keep it as it should be posted again when submit form. **/
			// personIdCardFrontImg = "";
		}
		return personIdCardFrontImg;
	}

	/**
	 * @param personIdCardFrontImg
	 *            Set personIdCardFrontImg value
	 */
	@JsonProperty("id_card_front_img")
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
	 * @return return the mask value of the var personIdCardBackImg
	 */
	@JsonProperty("id_card_back_img")
	public String getMaskPersonIdCardBackImg() {
		if (SecurityContext.getInstance().cannotViewRealIdCardNo(this.getUserId(), inCanViewPage)) {
			// personIdCardBackImg = "";
		}
		return personIdCardBackImg;
	}

	/**
	 * @param personIdCardBackImg
	 *            Set personIdCardBackImg value
	 */
	@JsonProperty("id_card_back_img")
	public void setPersonIdCardBackImg(String personIdCardBackImg) {
		this.personIdCardBackImg = personIdCardBackImg;
	}

	/**
	 * @return return the value of the var personJob
	 */

	public DynamicOption getPersonJob() {
		return personJob;
	}

	/**
	 * @param personJob
	 *            Set personJob value
	 */

	public void setPersonJob(DynamicOption personJob) {
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

	public DynamicOption getPersonEducation() {
		return personEducation;
	}

	/**
	 * @param personEducation
	 *            Set personEducation value
	 */

	public void setPersonEducation(DynamicOption personEducation) {
		this.personEducation = personEducation;
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
	 * @return return the mask value of the var personMobile
	 */
	@JsonProperty("mobile")
	public String getMakPersonMobile() {
		if (SecurityContext.getInstance().cannotViewRealPhoneNo(this.getUserId(), inCanViewPage)) {
			personMobile = MaskUtil.maskPhone(personMobile);
		}
		return personMobile;
	}

	/**
	 * @param personMobile
	 *            Set personMobile value
	 */
	@JsonProperty("mobile")
	public void setPersonMobile(String personMobile) {
		this.personMobile = personMobile;
		this.personMobileOriginal = personMobile;
	}

	/**
	 * @return return the value of the var personName
	 */

	public String getPersonName() {
		return personName;
	}

	/**
	 * @return return the mask value of the var personName
	 */
	@JsonProperty("real_name")
	public String getMaskPersonName() {
		if (SecurityContext.getInstance().cannotViewRealName(this.getUserId(), inCanViewPage)) {
			personName = MaskUtil.maskChinsesName(personName);
		}
		return personName;
	}

	/**
	 * @param personName
	 *            Set personName value
	 */
	@JsonProperty("real_name")
	public void setPersonName(String personName) {
		this.personName = personName;
		this.personNameOriginal = personName;
	}

	/**
	 * @return return the value of the var personRegion
	 */

	public DynamicOption getPersonRegion() {
		return personRegion;
	}

	/**
	 * @param personRegion
	 *            Set personRegion value
	 */

	public void setPersonRegion(DynamicOption personRegion) {
		this.personRegion = personRegion;
	}

	public void setPersonIdCardFrontImgUrl(String personIdCardFrontImgUrl) {
		this.personIdCardFrontImgUrl = personIdCardFrontImgUrl;
		this.personIdCardFrontImgUrlOriginal = personIdCardFrontImgUrl;
	}

	public void setPersonIdCardBackImgUrl(String personIdCardBackImgUrl) {
		this.personIdCardBackImgUrl = personIdCardBackImgUrl;
		this.personIdCardBackImgUrlOriginal = personIdCardBackImgUrl;
	}

	/**
	 * @return the personIdCardNumberOriginal
	 */
	public String getPersonIdCardNumberOriginal() {
		return personIdCardNumberOriginal;
	}

	/**
	 * @param personIdCardNumberOriginal
	 *            the personIdCardNumberOriginal to set
	 */
	public void setPersonIdCardNumberOriginal(String personIdCardNumberOriginal) {
		this.personIdCardNumberOriginal = personIdCardNumberOriginal;
	}

	/**
	 * @return the personMobileOriginal
	 */
	public String getPersonMobileOriginal() {
		return personMobileOriginal;
	}

	/**
	 * @param personMobileOriginal
	 *            the personMobileOriginal to set
	 */
	public void setPersonMobileOriginal(String personMobileOriginal) {
		this.personMobileOriginal = personMobileOriginal;
	}

	/**
	 * @return the personNameOriginal
	 */
	public String getPersonNameOriginal() {
		return personNameOriginal;
	}

	/**
	 * @param personNameOriginal
	 *            the personNameOriginal to set
	 */
	public void setPersonNameOriginal(String personNameOriginal) {
		this.personNameOriginal = personNameOriginal;
	}

	/**
	 * @return the personIdCardFrontImgUrlOriginal
	 */
	public String getPersonIdCardFrontImgUrlOriginal() {
		return personIdCardFrontImgUrlOriginal;
	}

	/**
	 * @param personIdCardFrontImgUrlOriginal
	 *            the personIdCardFrontImgUrlOriginal to set
	 */
	public void setPersonIdCardFrontImgUrlOriginal(String personIdCardFrontImgUrlOriginal) {
		this.personIdCardFrontImgUrlOriginal = personIdCardFrontImgUrlOriginal;
	}

	/**
	 * @return the personIdCardBackImgUrlOriginal
	 */
	public String getPersonIdCardBackImgUrlOriginal() {
		return personIdCardBackImgUrlOriginal;
	}

	/**
	 * @param personIdCardBackImgUrlOriginal
	 *            the personIdCardBackImgUrlOriginal to set
	 */
	public void setPersonIdCardBackImgUrlOriginal(String personIdCardBackImgUrlOriginal) {
		this.personIdCardBackImgUrlOriginal = personIdCardBackImgUrlOriginal;
	}

	/**
	 * <strong>Fields and methods should be before inner classes.</strong>
	 */
	public interface SubmitPerson {

	}

	/**
	 * <strong>Fields and methods should be before inner classes.</strong>
	 */
	public interface IdCardExist {

	}

	/**
	 * <strong>Fields and methods should be before inner classes.</strong>
	 */
	public interface PersionMobileExist {

	}
}
