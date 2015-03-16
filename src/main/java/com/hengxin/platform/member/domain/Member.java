/*
 * Project Name: kmfex-platform
 * File Name: MemberPo.java
 * Class Name: MemberPo
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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hengxin.platform.member.domain.converter.GenderEnumConverter;
import com.hengxin.platform.member.enums.EGender;
import com.hengxin.platform.security.entity.UserPo;

/**
 * Class Name: MemberPo
 *
 * @author shengzhou
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "UM_USER_INFO")
public class Member implements Serializable {

	@Id
	@Column(name = "USER_ID")
	private String userId;

	@Column(name = "P_EMAIL")
	private String personEmail;

	@Column(name = "P_MOBILE")
	private String personMobile;

	@Column(name = "P_NAME")
	private String personName;

	@Column(name = "P_REGION_CD")
	private String personRegion;

	@Column(name = "P_GENDER")
	@Convert(converter = GenderEnumConverter.class)
	private EGender personSex;

	@Column(name = "P_PHONE")
	private String personPhone;

	@Column(name = "P_ADDRESS")
	private String personAddress;

	@Column(name = "P_ZIP")
	private String personPostCode;

	@Column(name = "P_QQ")
	private String personQQ;

	@Temporal(TemporalType.DATE)
	@Column(name = "P_BIRTH_DT")
	private Date personBirthday;

	@Column(name = "P_ID_CARD_NO")
	private String personIdCardNumber;

	@Column(name = "P_ID_CARD_IMG1")
	private String personIdCardFrontImg;

	@Column(name = "P_ID_CARD_IMG2")
	private String personIdCardBackImg;

	@Column(name = "P_AGE_CD")
	private String personAge;

	@Column(name = "P_EDUCN_CD")
	private String personEducation;

	@Column(name = "P_JOB")
	private String personJob;

	@Column(name = "O_EMAIL")
	private String orgEmail;

	@Column(name = "O_MOBILE")
	private String orgMobile;

	@Column(name = "O_NAME")
	private String orgName;

	@Column(name = "O_REGION_CD")
	private String orgRegion;

	@Column(name = "O_INDUSTRY_CD")
	private String orgIndustry;

	@Column(name = "O_NATURE_CD")
	private String orgNature;

	@Column(name = "O_TYPE_CD")
	private String orgType;

	@Column(name = "O_PHONE")
	private String orgPhone;

	@Column(name = "O_ADDRESS")
	private String orgAddress;

	@Column(name = "O_QQ")
	private String orgQQ;

	@Column(name = "O_ZIP")
	private String orgPostCode;

	@Column(name = "O_FAX")
	private String orgFax;

	@Column(name = "O_REP")
	private String orgLegalPerson;

	@Column(name = "O_REP_GENDER")
	@Convert(converter = GenderEnumConverter.class)
	private EGender orgLegalPersonGender;

	@Column(name = "O_REP_BIRTH_DT")
	@Temporal(TemporalType.DATE)
	private Date orgLegalPersonBirthday;

	@Column(name = "O_REP_ID_CARD_NO")
	private String orgLegalPersonIdCardNumber;

	@Column(name = "O_REP_ID_CARD_IMG1")
	private String orgLegalPersonIdCardFrontImg;

	@Column(name = "O_REP_ID_CARD_IMG2")
	private String orgLegalPersonIdCardBackImg;

	@Column(name = "O_REP_AGE_CD")
	private String orgLegalPersonAge;

	@Column(name = "O_REP_EDUCN_CD")
	private String orgLegalPersonEduction;

	@Column(name = "O_CONTACT")
	private String contact;

	@Column(name = "O_CONTACT_PHONE")
	private String contactPhone;

	@Column(name = "O_CONTACT_MOBILE")
	private String contactMobile;

	@Column(name = "O_CONTACT_FAX")
	private String contactFax;

	@Column(name = "O_CONTACT_EMAIL")
	private String contactEmail;

	@Column(name = "O_MAIN_BIZ")
	private String orgMainBusiness;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TS", updatable = false)
	private Date createTs;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH })
    @JoinColumn(name = "USER_ID", insertable = false, updatable = false)
	private UserPo userPo;

	public UserPo getUserPo() {
		return userPo;
	}

	public void setUserPo(UserPo userPo) {
		this.userPo = userPo;
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
	 * @param orgFax
	 *            Set orgFax value
	 */

	public void setOrgFax(String orgFax) {
		this.orgFax = orgFax;
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

	public void setOrgLegalPersonIdCardFrontImg(
			String orgLegalPersonIdCardFrontImg) {
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

	public void setOrgLegalPersonIdCardBackImg(
			String orgLegalPersonIdCardBackImg) {
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

	public EGender getOrgLegalPersonGender() {
		return orgLegalPersonGender;
	}

	public void setOrgLegalPersonGender(EGender orgLegalPersonGender) {
		this.orgLegalPersonGender = orgLegalPersonGender;
	}

	public Date getOrgLegalPersonBirthday() {
		return orgLegalPersonBirthday;
	}

	public void setOrgLegalPersonBirthday(Date orgLegalPersonBirthday) {
		this.orgLegalPersonBirthday = orgLegalPersonBirthday;
	}

}
