/*
 * Project Name: kmfex-platform
 * File Name: MemberApplicationViewPo.java
 * Class Name: MemberApplicationViewPo
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
import com.hengxin.platform.member.domain.converter.UserTypeEnumConverter;
import com.hengxin.platform.member.enums.EApplicationStatus;
import com.hengxin.platform.member.enums.EMemberType;
import com.hengxin.platform.member.enums.EUserType;

/**
 * Class Name: MemberApplicationViewPo
 *
 * @author shengzhou
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "V_USER_APPL")
public class MemberApplicationView implements Serializable {

    @Id
    @Column(name = "USER_ID")
    private String userId;
    
    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "USER_TYPE")
    @Convert(converter = UserTypeEnumConverter.class)
    private EUserType userType;

    @Column(name = "ACTIVE_STATUS")
    private String activeStatus;

    @Column(name = "NAME")
    private String name;

    @Column(name = "REGION_CD")
    private String region;

    @Column(name = "P_JOB")
    private String personJob;

    @Column(name = "P_EDUCN_CD")
    private String personEducation;

    @Column(name = "O_TYPE_CD")
    private String orgType;

    @Column(name = "O_INDUSTRY_CD")
    private String orgIndustry;

    @Column(name = "O_NATURE_CD")
    private String orgNature;

    @Column(name = "O_MAIN_BIZ")
    private String orgMainBusiness;

    @Column(name = "BASIC_STATUS")
    @Convert(converter = ApplicationStatusEnumConverter.class)
    private EApplicationStatus basicStatus;

    @Column(name = "FINANCIER_STATUS")
    @Convert(converter = ApplicationStatusEnumConverter.class)
    private EApplicationStatus financeStatus;

    @Column(name = "INVESTOR_STATUS")
    @Convert(converter = ApplicationStatusEnumConverter.class)
    private EApplicationStatus investorStatus;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LASTEST_TS")
    private Date lastestTs;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EARLIEST_TS")
    private Date earliestTs;

    @Column(name = "USER_ROLE")
    @Convert(converter = MemberTypeEnumConverter.class)
    private EMemberType userRole;

    @Column(name = "NOT_AUDITED_STATUS")
    @Convert(converter = ApplicationStatusEnumConverter.class)
    private EApplicationStatus unAuditedStatus;

    @Column(name = "AUDITED_STATUS")
    @Convert(converter = ApplicationStatusEnumConverter.class)
    private EApplicationStatus auditedStatus;

    @Column(name = "INVSTR_AGENT")
    private String investorAgent;

    @Column(name = "FNCR_AGENT")
    private String financeAgent;
    
    @Column(name = "AGENT")
    private String agent;

    @Column(name = "AGENT_NAME")
    private String agentName;

    @Column(name = "IS_AGENT_REGISTER")
    private String agentRegister;

    @Column(name = "IS_AUDIT_PASS")
    private String auditPass;

    @Column(name = "ACCT_NO")
    private String accountNo;

    /**
     * @return return the value of the var userType
     */

    public EUserType getUserType() {
        return userType;
    }

    /**
     * @param userType Set userType value
     */

    public void setUserType(EUserType userType) {
        this.userType = userType;
    }


    public String getUserId() {
        return userId;
    }

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
     * @return return the value of the var name
     */

    public String getName() {
        return name;
    }

    /**
     * @param name Set name value
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
     * @param region Set region value
     */

    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * @return return the value of the var personJob
     */

    public String getPersonJob() {
        return personJob;
    }

    /**
     * @param personJob Set personJob value
     */

    public void setPersonJob(String personJob) {
        this.personJob = personJob;
    }

    /**
     * @return return the value of the var personEducation
     */

    public String getPersonEducation() {
        return personEducation;
    }

    /**
     * @param personEducation Set personEducation value
     */

    public void setPersonEducation(String personEducation) {
        this.personEducation = personEducation;
    }

    /**
     * @return return the value of the var orgType
     */

    public String getOrgType() {
        return orgType;
    }

    /**
     * @param orgType Set orgType value
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
     * @param orgIndustry Set orgIndustry value
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
     * @param orgNature Set orgNature value
     */

    public void setOrgNature(String orgNature) {
        this.orgNature = orgNature;
    }

    /**
     * @return return the value of the var orgMainBusiness
     */

    public String getOrgMainBusiness() {
        return orgMainBusiness;
    }

    /**
     * @param orgMainBusiness Set orgMainBusiness value
     */

    public void setOrgMainBusiness(String orgMainBusiness) {
        this.orgMainBusiness = orgMainBusiness;
    }

    /**
     * @return return the value of the var financeStatus
     */

    public EApplicationStatus getFinanceStatus() {
        return financeStatus;
    }

    /**
     * @param financeStatus Set financeStatus value
     */

    public void setFinanceStatus(EApplicationStatus financeStatus) {
        this.financeStatus = financeStatus;
    }

    /**
     * @return return the value of the var investorStatus
     */

    public EApplicationStatus getInvestorStatus() {
        return investorStatus;
    }

    /**
     * @param investorStatus Set investorStatus value
     */

    public void setInvestorStatus(EApplicationStatus investorStatus) {
        this.investorStatus = investorStatus;
    }

    /**
     * @return return the value of the var basicStatus
     */

    public EApplicationStatus getBasicStatus() {
        return basicStatus;
    }

    /**
     * @param basicStatus Set basicStatus value
     */

    public void setBasicStatus(EApplicationStatus basicStatus) {
        this.basicStatus = basicStatus;
    }

    /**
     * @return return the value of the var lastestTS
     */

    public Date getLastestTs() {
        return lastestTs;
    }

    /**
     * @param lastestTS Set lastestTS value
     */

    public void setLastestTs(Date lastestTs) {
        this.lastestTs = lastestTs;
    }

    /**
     * @return return the value of the var earliestTs
     */

    public Date getEarliestTs() {
        return earliestTs;
    }

    /**
     * @param earliestTs Set earliestTs value
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
     * @param userRole Set userRole value
     */

    public void setUserRole(EMemberType userRole) {
        this.userRole = userRole;
    }

    public EApplicationStatus getUnAuditedStatus() {
        return unAuditedStatus;
    }

    public void setUnAuditedStatus(EApplicationStatus unAuditedStatus) {
        this.unAuditedStatus = unAuditedStatus;
    }

    public EApplicationStatus getAuditedStatus() {
        return auditedStatus;
    }

    public void setAuditedStatus(EApplicationStatus auditedStatus) {
        this.auditedStatus = auditedStatus;
    }

    /**
     * @return return the value of the var investorAgent
     */

    public String getInvestorAgent() {
        return investorAgent;
    }

    /**
     * @param investorAgent Set investorAgent value
     */

    public void setInvestorAgent(String investorAgent) {
        this.investorAgent = investorAgent;
    }

    /**
     * @return return the value of the var financeAgent
     */

    public String getFinanceAgent() {
        return financeAgent;
    }

    /**
     * @param financeAgent Set financeAgent value
     */

    public void setFinanceAgent(String financeAgent) {
        this.financeAgent = financeAgent;
    }

    /**
     * @return return the value of the var agentRegister
     */

    public String getAgentRegister() {
        return agentRegister;
    }

    /**
     * @param agentRegister Set agentRegister value
     */

    public void setAgentRegister(String agentRegister) {
        this.agentRegister = agentRegister;
    }

    /**
     * @return return the value of the var auditPass
     */

    public String getAuditPass() {
        return auditPass;
    }

    /**
     * @param auditPass Set auditPass value
     */

    public void setAuditPass(String auditPass) {
        this.auditPass = auditPass;
    }

    /**
     * @return return the value of the var agent
     */
    
    public String getAgent() {
        return agent;
    }

    /**
     * @param agent Set agent value
     */
    
    public void setAgent(String agent) {
        this.agent = agent;
    }

	/**
	 * @return the agentName
	 */
	public String getAgentName() {
		return agentName;
	}

	/**
	 * @param agentName the agentName to set
	 */
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	/**
	 * @return the accountNo
	 */
	public String getAccountNo() {
		return accountNo;
	}

	/**
	 * @param accountNo the accountNo to set
	 */
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}

}
