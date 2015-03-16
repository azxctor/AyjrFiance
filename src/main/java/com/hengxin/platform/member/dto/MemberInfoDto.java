package com.hengxin.platform.member.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.hengxin.platform.common.converter.OptionCategory;
import com.hengxin.platform.common.domain.DynamicOption;
import com.hengxin.platform.common.enums.EOptionCategory;
import com.hengxin.platform.member.domain.UserRole;
import com.hengxin.platform.member.enums.EGender;
import com.hengxin.platform.member.enums.EUserType;
import com.hengxin.platform.security.enums.BindingCardStatusEnum;
import com.hengxin.platform.security.enums.EswAcctStatusEnum;

public class MemberInfoDto implements Serializable {

	private static final long serialVersionUID = 1L;

	/** UM_USER **/

	private String userId;

	private String username;

	private String password;

	private String email;

	private String mobile;

	/**
	 * 状态: A-Active,I-Inactive,C-Cancel
	 */
	private String status;

	/**
	 * 类型，P:个人/O:机构
	 */
	private EUserType type;

	private String name;

	/**
	 * 所在地区
	 */
	@OptionCategory(EOptionCategory.REGION)
	private DynamicOption region;

	/**
	 * 登录失败次数
	 */
	private Long failureCount;

	/**
	 * 创建者
	 */
	private String creator;

	/**
	 * 最近一次登录时间
	 */
	private Date lastLoginTs;

	private String lastMntOpid;

	private Date lastMntTs;

	private String ownerId;

	private Boolean showBulletin;

	private List<UserRole> userRole;

	/**
	 * 所属公司编号
	 */
	private String companyId;

	/**
	 * 冻结操作人
	 */
	private String frozenOperator;

	/**
	 * 冻结时间
	 */
	private Date frozenDate;

	/**
	 * 备注
	 */
	private String frozenMemo;

	/**
	 * 会员头像
	 */
	private String iconFileId;

	/**
	 * 开户状态
	 */
	private EswAcctStatusEnum eswAcctStatus;

	/**
	 * 绑卡状态
	 */
	private BindingCardStatusEnum bindingCardStatus;

	/** UM_USER_INFO **/
	private String personEmail;

	private String personMobile;

	private String personName;

	/**
	 * 地区
	 */
	private String personRegion;

	private EGender personSex;

	private String personPhone;

	private String personAddress;

	/**
	 * 邮编
	 */
	private String personPostCode;

	private String personQQ;

	private Date personBirthday;

	private String personIdCardNumber;

	private String personIdCardFrontImg;

	private String personIdCardBackImg;

	private String personAge;

	private String personEducation;

	private String personJob;

	private String orgEmail;

	private String orgMobile;

	private String orgName;

	private String orgRegion;

	/**
	 * 所属行业
	 */
	private String orgIndustry;

	/**
	 * 企业性质
	 */
	private String orgNature;

	/**
	 * 企业类型
	 */
	private String orgType;

	private String orgPhone;

	private String orgAddress;

	private String orgQQ;

	private String orgPostCode;

	/**
	 * 传真
	 */
	private String orgFax;

	/**
	 * 法人
	 */
	private String orgLegalPerson;

	private EGender orgLegalPersonGender;

	private Date orgLegalPersonBirthday;

	private String orgLegalPersonIdCardNumber;

	private String orgLegalPersonIdCardFrontImg;

	private String orgLegalPersonIdCardBackImg;

	private String orgLegalPersonAge;

	private String orgLegalPersonEduction;

	/**
	 * 联系人
	 */
	private String contact;

	private String contactPhone;

	private String contactMobile;

	private String contactFax;

	private String contactEmail;

	private String orgMainBusiness;

	private String createTs;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public EUserType getType() {
		return type;
	}

	public void setType(EUserType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DynamicOption getRegion() {
		return region;
	}

	public void setRegion(DynamicOption region) {
		this.region = region;
	}

	public Long getFailureCount() {
		return failureCount;
	}

	public void setFailureCount(Long failureCount) {
		this.failureCount = failureCount;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getLastLoginTs() {
		return lastLoginTs;
	}

	public void setLastLoginTs(Date lastLoginTs) {
		this.lastLoginTs = lastLoginTs;
	}

	public String getLastMntOpid() {
		return lastMntOpid;
	}

	public void setLastMntOpid(String lastMntOpid) {
		this.lastMntOpid = lastMntOpid;
	}

	public Date getLastMntTs() {
		return lastMntTs;
	}

	public void setLastMntTs(Date lastMntTs) {
		this.lastMntTs = lastMntTs;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public Boolean getShowBulletin() {
		return showBulletin;
	}

	public void setShowBulletin(Boolean showBulletin) {
		this.showBulletin = showBulletin;
	}

	public List<UserRole> getUserRole() {
		return userRole;
	}

	public void setUserRole(List<UserRole> userRole) {
		this.userRole = userRole;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getFrozenOperator() {
		return frozenOperator;
	}

	public void setFrozenOperator(String frozenOperator) {
		this.frozenOperator = frozenOperator;
	}

	public Date getFrozenDate() {
		return frozenDate;
	}

	public void setFrozenDate(Date frozenDate) {
		this.frozenDate = frozenDate;
	}

	public String getFrozenMemo() {
		return frozenMemo;
	}

	public void setFrozenMemo(String frozenMemo) {
		this.frozenMemo = frozenMemo;
	}

	public String getIconFileId() {
		return iconFileId;
	}

	public void setIconFileId(String iconFileId) {
		this.iconFileId = iconFileId;
	}

	public EswAcctStatusEnum getEswAcctStatus() {
		return eswAcctStatus;
	}

	public void setEswAcctStatus(EswAcctStatusEnum eswAcctStatus) {
		this.eswAcctStatus = eswAcctStatus;
	}

	public BindingCardStatusEnum getBindingCardStatus() {
		return bindingCardStatus;
	}

	public void setBindingCardStatus(BindingCardStatusEnum bindingCardStatus) {
		this.bindingCardStatus = bindingCardStatus;
	}

	public String getPersonEmail() {
		return personEmail;
	}

	public void setPersonEmail(String personEmail) {
		this.personEmail = personEmail;
	}

	public String getPersonMobile() {
		return personMobile;
	}

	public void setPersonMobile(String personMobile) {
		this.personMobile = personMobile;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getPersonRegion() {
		return personRegion;
	}

	public void setPersonRegion(String personRegion) {
		this.personRegion = personRegion;
	}

	public EGender getPersonSex() {
		return personSex;
	}

	public void setPersonSex(EGender personSex) {
		this.personSex = personSex;
	}

	public String getPersonPhone() {
		return personPhone;
	}

	public void setPersonPhone(String personPhone) {
		this.personPhone = personPhone;
	}

	public String getPersonAddress() {
		return personAddress;
	}

	public void setPersonAddress(String personAddress) {
		this.personAddress = personAddress;
	}

	public String getPersonPostCode() {
		return personPostCode;
	}

	public void setPersonPostCode(String personPostCode) {
		this.personPostCode = personPostCode;
	}

	public String getPersonQQ() {
		return personQQ;
	}

	public void setPersonQQ(String personQQ) {
		this.personQQ = personQQ;
	}

	public Date getPersonBirthday() {
		return personBirthday;
	}

	public void setPersonBirthday(Date personBirthday) {
		this.personBirthday = personBirthday;
	}

	public String getPersonIdCardNumber() {
		return personIdCardNumber;
	}

	public void setPersonIdCardNumber(String personIdCardNumber) {
		this.personIdCardNumber = personIdCardNumber;
	}

	public String getPersonIdCardFrontImg() {
		return personIdCardFrontImg;
	}

	public void setPersonIdCardFrontImg(String personIdCardFrontImg) {
		this.personIdCardFrontImg = personIdCardFrontImg;
	}

	public String getPersonIdCardBackImg() {
		return personIdCardBackImg;
	}

	public void setPersonIdCardBackImg(String personIdCardBackImg) {
		this.personIdCardBackImg = personIdCardBackImg;
	}

	public String getPersonAge() {
		return personAge;
	}

	public void setPersonAge(String personAge) {
		this.personAge = personAge;
	}

	public String getPersonEducation() {
		return personEducation;
	}

	public void setPersonEducation(String personEducation) {
		this.personEducation = personEducation;
	}

	public String getPersonJob() {
		return personJob;
	}

	public void setPersonJob(String personJob) {
		this.personJob = personJob;
	}

	public String getOrgEmail() {
		return orgEmail;
	}

	public void setOrgEmail(String orgEmail) {
		this.orgEmail = orgEmail;
	}

	public String getOrgMobile() {
		return orgMobile;
	}

	public void setOrgMobile(String orgMobile) {
		this.orgMobile = orgMobile;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgRegion() {
		return orgRegion;
	}

	public void setOrgRegion(String orgRegion) {
		this.orgRegion = orgRegion;
	}

	public String getOrgIndustry() {
		return orgIndustry;
	}

	public void setOrgIndustry(String orgIndustry) {
		this.orgIndustry = orgIndustry;
	}

	public String getOrgNature() {
		return orgNature;
	}

	public void setOrgNature(String orgNature) {
		this.orgNature = orgNature;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public String getOrgPhone() {
		return orgPhone;
	}

	public void setOrgPhone(String orgPhone) {
		this.orgPhone = orgPhone;
	}

	public String getOrgAddress() {
		return orgAddress;
	}

	public void setOrgAddress(String orgAddress) {
		this.orgAddress = orgAddress;
	}

	public String getOrgQQ() {
		return orgQQ;
	}

	public void setOrgQQ(String orgQQ) {
		this.orgQQ = orgQQ;
	}

	public String getOrgPostCode() {
		return orgPostCode;
	}

	public void setOrgPostCode(String orgPostCode) {
		this.orgPostCode = orgPostCode;
	}

	public String getOrgFax() {
		return orgFax;
	}

	public void setOrgFax(String orgFax) {
		this.orgFax = orgFax;
	}

	public String getOrgLegalPerson() {
		return orgLegalPerson;
	}

	public void setOrgLegalPerson(String orgLegalPerson) {
		this.orgLegalPerson = orgLegalPerson;
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

	public String getOrgLegalPersonIdCardNumber() {
		return orgLegalPersonIdCardNumber;
	}

	public void setOrgLegalPersonIdCardNumber(String orgLegalPersonIdCardNumber) {
		this.orgLegalPersonIdCardNumber = orgLegalPersonIdCardNumber;
	}

	public String getOrgLegalPersonIdCardFrontImg() {
		return orgLegalPersonIdCardFrontImg;
	}

	public void setOrgLegalPersonIdCardFrontImg(String orgLegalPersonIdCardFrontImg) {
		this.orgLegalPersonIdCardFrontImg = orgLegalPersonIdCardFrontImg;
	}

	public String getOrgLegalPersonIdCardBackImg() {
		return orgLegalPersonIdCardBackImg;
	}

	public void setOrgLegalPersonIdCardBackImg(String orgLegalPersonIdCardBackImg) {
		this.orgLegalPersonIdCardBackImg = orgLegalPersonIdCardBackImg;
	}

	public String getOrgLegalPersonAge() {
		return orgLegalPersonAge;
	}

	public void setOrgLegalPersonAge(String orgLegalPersonAge) {
		this.orgLegalPersonAge = orgLegalPersonAge;
	}

	public String getOrgLegalPersonEduction() {
		return orgLegalPersonEduction;
	}

	public void setOrgLegalPersonEduction(String orgLegalPersonEduction) {
		this.orgLegalPersonEduction = orgLegalPersonEduction;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getContactMobile() {
		return contactMobile;
	}

	public void setContactMobile(String contactMobile) {
		this.contactMobile = contactMobile;
	}

	public String getContactFax() {
		return contactFax;
	}

	public void setContactFax(String contactFax) {
		this.contactFax = contactFax;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getOrgMainBusiness() {
		return orgMainBusiness;
	}

	public void setOrgMainBusiness(String orgMainBusiness) {
		this.orgMainBusiness = orgMainBusiness;
	}

	public String getCreateTs() {
		return createTs;
	}

	public void setCreateTs(String createTs) {
		this.createTs = createTs;
	}

}
