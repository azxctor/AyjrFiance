/*
 * Project Name: kmfex-platform
 * File Name: AgencyApplicationViewPo.java
 * Class Name: AgencyApplicationViewPo
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
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hengxin.platform.member.domain.converter.ApplicationStatusEnumConverter;
import com.hengxin.platform.member.domain.converter.MemberTypeEnumConverter;
import com.hengxin.platform.member.enums.EApplicationStatus;
import com.hengxin.platform.member.enums.EMemberType;

/**
 * Class Name: AgencyApplicationViewPo
 *
 * @author shengzhou
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "V_ORG_APPL")
public class AgencyApplicationView implements Serializable {

    @Id
    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "APPL_ID")
    private String appId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "REGION_CD")
    private String region;

    @Column(name = "O_NATURE_CD")
    private String orgNature;

    @Column(name = "BASIC_STATUS")
    @Convert(converter = ApplicationStatusEnumConverter.class)
    private EApplicationStatus basicStatus;

    @Column(name = "AUTHZD_STATUS")
    @Convert(converter = ApplicationStatusEnumConverter.class)
    private EApplicationStatus serviceCenterStatus;

    @Column(name = "PRODSERV_STATUS")
    @Convert(converter = ApplicationStatusEnumConverter.class)
    private EApplicationStatus productServiceStatus;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LASTEST_TS")
    private Date latestTs;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EARLIEST_TS")
    private Date earliestTs;

    @Column(name = "USER_ROLE")
    @Convert(converter = MemberTypeEnumConverter.class)
    private EMemberType userRole;

    // ========================
    @Column(name = "O_PHONE")
    private String orgPhone;

    @Column(name = "O_ADDRESS")
    private String orgAddress;

    @Column(name = "O_ZIP")
    private String orgPostCode;

    @Column(name = "O_FAX")
    private String fax;

    @Column(name = "O_REP")
    private String orgLegalPerson;

    @Column(name = "O_REP_ID_CARD_NO")
    private String orgLegalPersonIdCardNumber;

    @Column(name = "O_REP_ID_CARD_IMG1")
    private String orgLegalPersonIdCardFrontImg;

    @Column(name = "O_REP_ID_CARD_IMG2")
    private String orgLegalPersonIdCardBackImg;

    @Column(name = "O_REP_PHONE")
    private String orgLeagalPersonPhone;

    @Column(name = "O_REP_MOBILE")
    private String orgLeagalPersonMobile;

    @Column(name = "O_REP_QQ")
    private String orgLeagalPersonQQ;

    @Column(name = "O_REP_EMAIL")
    private String orgLeagalPersonEmail;

    @Column(name = "O_CONTACT")
    private String contact;

    @Column(name = "O_CONTACT_PHONE")
    private String contactPhone;

    @Column(name = "O_CONTACT_MOBILE")
    private String contactMobile;

    @Column(name = "O_CONTACT_FAX")
    private String contactFax;

    @Column(name = "O_CONTACT_QQ")
    private String contactQQ;

    @Column(name = "O_CONTACT_EMAIL")
    private String contactEmail;

    @Column(name = "O_ORG_NO")
    private String orgNo;

    @Column(name = "O_ORG_NO_IMG")
    private String orgNoImg;

    @Column(name = "O_LICENCE_NO")
    private String licenceNo;

    @Column(name = "O_LICENCE_NO_IMG")
    private String licenceNoImg;

    @Column(name = "O_TAX_NO")
    private String taxNo;

    @Column(name = "O_TAX_NO_IMG")
    private String taxNoImg;

    @Column(name = "BNK_CD")
    private String bankShortName;

    @Column(name = "BNK_NAME")
    private String bankFullName;

    @Column(name = "BNK_ACCT_NO")
    private String bankAccount;

    @Column(name = "BNK_CARD_IMG")
    private String bankCardFrontImg;

    @Column(name = "BNK_ACCT_NAME")
    private String bankAccountName;

    @Column(name = "BNK_OPEN_PROV")
    private String bankOpenProvince;

    @Column(name = "BNK_OPEN_CITY")
    private String bankOpenCity;

    @Column(name = "BNK_OPEN_BRCH")
    private String bankOpenBranch;

    // =====================
    @Column(name = "SERVICE_COOP_FORM")
    private String cooperationForm;

    @Temporal(TemporalType.DATE)
    @Column(name = "SERVICE_COOP_START_DT")
    private Date cooperationBeginDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "SERVICE_COOP_END_DT")
    private Date cooperationEndDate;

    @Column(name = "SERVICE_REGIST_PLACE")
    private String registrationPlace;

    @Temporal(TemporalType.DATE)
    @Column(name = "SERVICE_REGIST_DT")
    private Date registrationDate;

    @Column(name = "SERVICE_REGIST_CAP")
    private String registrationCapital;

    @Column(name = "SERVICE_EMPLOYEE_NBR")
    private String employeeNo;

    @Column(name = "SERVICE_AGENT")
    private String agent;// 经办人

    @Column(name = "SERVICE_AUTHZD_CTR_LVL")
    private String serviceCenterLevel;
    // product service
    @Column(name = "PRODUCT_PRIVILEGE_LIC")
    private String productPrivilegeLic;

    @Column(name = "PRODUCT_SERV_REGION_CD")
    private String productServiceRegion;

    @Column(name = "PRODUCT_SERV_INDUSTRY_CD")
    private String productServiceRegionIndustry;

    @Column(name = "PRODUCT_AGENT")
    private String productServiceAgent;

    @Column(name = "PRODUCT_SERV_LEVEL")
    private String proSeverLevel;

    @Column(name = "ACCT_NO")
    private String accountNo;
    
    @Column(name = "DESC_TX")
    private String desc;
    
    @Column(name = "COMPANY_ID")
    private String companyId;

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return return the value of the var appId
     */

    public String getAppId() {
        return appId;
    }

    /**
     * @param appId
     *            Set appId value
     */

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
     * @param name
     *            Set name value
     */

    public void setName(String name) {
        this.name = name;
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
     * @return return the value of the var basicStatus
     */

    public EApplicationStatus getBasicStatus() {
        return basicStatus;
    }

    /**
     * @param basicStatus
     *            Set basicStatus value
     */

    public void setBasicStatus(EApplicationStatus basicStatus) {
        this.basicStatus = basicStatus;
    }

    /**
     * @return return the value of the var lastestTS
     */

    public Date getLastestTs() {
        return latestTs;
    }

    /**
     * @param lastestTS
     *            Set lastestTS value
     */

    public void setLastestTs(Date lastestTs) {
        this.latestTs = lastestTs;
    }

    /**
     * @return return the value of the var earliestTs
     */

    public Date getEarliestTs() {
        return earliestTs;
    }

    /**
     * @param earliestTs
     *            Set earliestTs value
     */

    public void setEarliestTs(Date earliestTs) {
        this.earliestTs = earliestTs;
    }

    /**
     * @return return the value of the var userRole
     */

    public EMemberType getUserRole() {
        return userRole;
    }

    /**
     * @param userRole
     *            Set userRole value
     */

    public void setUserRole(EMemberType userRole) {
        this.userRole = userRole;
    }

    /**
     * @return return the value of the var productServiceStatus
     */

    public EApplicationStatus getProductServiceStatus() {
        return productServiceStatus;
    }

    /**
     * @param productServiceStatus
     *            Set productServiceStatus value
     */

    public void setProductServiceStatus(EApplicationStatus productServiceStatus) {
        this.productServiceStatus = productServiceStatus;
    }

    /**
     * @return return the value of the var cooperationForm
     */

    public String getCooperationForm() {
        return cooperationForm;
    }

    /**
     * @param cooperationForm
     *            Set cooperationForm value
     */

    public void setCooperationForm(String cooperationForm) {
        this.cooperationForm = cooperationForm;
    }

    /**
     * @return return the value of the var cooperationBeginDate
     */

    public Date getCooperationBeginDate() {
        return cooperationBeginDate;
    }

    /**
     * @param cooperationBeginDate
     *            Set cooperationBeginDate value
     */

    public void setCooperationBeginDate(Date cooperationBeginDate) {
        this.cooperationBeginDate = cooperationBeginDate;
    }

    /**
     * @return return the value of the var cooperationEndDate
     */

    public Date getCooperationEndDate() {
        return cooperationEndDate;
    }

    /**
     * @param cooperationEndDate
     *            Set cooperationEndDate value
     */

    public void setCooperationEndDate(Date cooperationEndDate) {
        this.cooperationEndDate = cooperationEndDate;
    }

    /**
     * @return return the value of the var registrationPlace
     */

    public String getRegistrationPlace() {
        return registrationPlace;
    }

    /**
     * @param registrationPlace
     *            Set registrationPlace value
     */

    public void setRegistrationPlace(String registrationPlace) {
        this.registrationPlace = registrationPlace;
    }

    /**
     * @return return the value of the var registrationDate
     */

    public Date getRegistrationDate() {
        return registrationDate;
    }

    /**
     * @param registrationDate
     *            Set registrationDate value
     */

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    /**
     * @return return the value of the var registrationCapital
     */

    public String getRegistrationCapital() {
        return registrationCapital;
    }

    /**
     * @param registrationCapital
     *            Set registrationCapital value
     */

    public void setRegistrationCapital(String registrationCapital) {
        this.registrationCapital = registrationCapital;
    }

    /**
     * @return return the value of the var employeeNo
     */

    public String getEmployeeNo() {
        return employeeNo;
    }

    /**
     * @param employeeNo
     *            Set employeeNo value
     */

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }

    /**
     * @return return the value of the var agent
     */

    public String getAgent() {
        return agent;
    }

    /**
     * @param agent
     *            Set agent value
     */

    public void setAgent(String agent) {
        this.agent = agent;
    }

    /**
     * @return return the value of the var serviceCenterLevel
     */

    public String getServiceCenterLevel() {
        return serviceCenterLevel;
    }

    /**
     * @param serviceCenterLevel
     *            Set serviceCenterLevel value
     */

    public void setServiceCenterLevel(String serviceCenterLevel) {
        this.serviceCenterLevel = serviceCenterLevel;
    }

    /**
     * @return return the value of the var serviceCenterStatus
     */

    public EApplicationStatus getServiceCenterStatus() {
        return serviceCenterStatus;
    }

    /**
     * @param serviceCenterStatus
     *            Set serviceCenterStatus value
     */

    public void setServiceCenterStatus(EApplicationStatus serviceCenterStatus) {
        this.serviceCenterStatus = serviceCenterStatus;
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

    public Date getLatestTs() {
        return latestTs;
    }

    public void setLatestTs(Date latestTs) {
        this.latestTs = latestTs;
    }

    public String getProductPrivilegeLic() {
        return productPrivilegeLic;
    }

    public void setProductPrivilegeLic(String productPrivilegeLic) {
        this.productPrivilegeLic = productPrivilegeLic;
    }

    public String getProductServiceRegion() {
        return productServiceRegion;
    }

    public void setProductServiceRegion(String productServiceRegion) {
        this.productServiceRegion = productServiceRegion;
    }

    public String getProductServiceRegionIndustry() {
        return productServiceRegionIndustry;
    }

    public void setProductServiceRegionIndustry(String productServiceRegionIndustry) {
        this.productServiceRegionIndustry = productServiceRegionIndustry;
    }

    public String getProductServiceAgent() {
        return productServiceAgent;
    }

    public void setProductServiceAgent(String productServiceAgent) {
        this.productServiceAgent = productServiceAgent;
    }

    public String getProSeverLevel() {
        return proSeverLevel;
    }

    public void setProSeverLevel(String proSeverLevel) {
        this.proSeverLevel = proSeverLevel;
    }

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
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

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

}
