/*
 * Project Name: kmfex-platform
 * File Name: MemberView.java
 * Class Name: MemberView
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

import org.codehaus.jackson.annotate.JsonProperty;

import com.hengxin.platform.common.converter.OptionCategory;
import com.hengxin.platform.common.domain.DynamicOption;
import com.hengxin.platform.common.enums.EOptionCategory;
import com.hengxin.platform.common.util.MaskUtil;
import com.hengxin.platform.member.enums.EApplicationStatus;
import com.hengxin.platform.member.enums.EMemberType;
import com.hengxin.platform.member.enums.EUserStatus;
import com.hengxin.platform.member.enums.EUserType;
import com.hengxin.platform.security.SecurityContext;

/**
 * Class Name: MemberView Description: Domain of MemberApplicationViewPo
 * 
 * @author shengzhou
 * 
 */
public class MemberViewDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private EUserType userType;

    private String userId;

    private String userName;

    private String name;

    @OptionCategory(EOptionCategory.REGION)
    private DynamicOption region;

    @OptionCategory(EOptionCategory.JOB)
    private DynamicOption personJob;

    @OptionCategory(EOptionCategory.EDUCATION)
    private DynamicOption personEducation;

    @OptionCategory(EOptionCategory.ORG_TYPE)
    private DynamicOption orgType;

    @OptionCategory(EOptionCategory.ORG_INDUSTRY)
    private DynamicOption orgIndustry;

    @OptionCategory(EOptionCategory.ORG_NATURE)
    private DynamicOption orgNature;

    private String orgMainBusiness;

    private String orgLegalPerson;

    private EApplicationStatus financeStatus;

    private EApplicationStatus investorStatus;

    private EApplicationStatus basicStatus;

    private EApplicationStatus serviceCenterStatus;

    private EApplicationStatus productServiceStatus;

    private String lastestTs;

    private String earliestTs;

    private EMemberType userRole;

    private String investorAgent;

    private String financeAgent;

    private String agentRegister;
    
    /** 经办人. **/
    private String agent;
    
    /** 介绍人. **/
    private String agentName;

    private boolean inCanViewPage;

    private String accountNo;
    
    private EUserStatus activeStatus;

    /**
     * @return return the value of the var userType
     */

    public EUserType getUserType() {
        return userType;
    }

    /**
     * @param userType
     *            Set userType value
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
     * @return return the mask value of the var name
     */
    @JsonProperty("name")
    public String geMasktName() {
        // 非企业用户名
        if (EUserType.PERSON == userType
                && (EMemberType.INVESTOR == userRole || EMemberType.FINANCER == userRole
                        || EMemberType.INVESTOR_FINANCER == userRole || EMemberType.GUEST == userRole)) {
            if (SecurityContext.getInstance().cannotViewRealName(this.getUserId(), inCanViewPage)) {
                name = MaskUtil.maskChinsesName(name);
            }
        }
        return name;
    }

    /**
     * @param name
     *            Set name value
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
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
     * @return return the value of the var orgType
     */

    public DynamicOption getOrgType() {
        return orgType;
    }

    /**
     * @param orgType
     *            Set orgType value
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
	 * @param orgNature the orgNature to set
	 */
	public void setOrgNature(DynamicOption orgNature) {
		this.orgNature = orgNature;
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
     * @return return the value of the var financeStatus
     */

    public EApplicationStatus getFinanceStatus() {
        return financeStatus;
    }

    /**
     * @param financeStatus
     *            Set financeStatus value
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
     * @param investorStatus
     *            Set investorStatus value
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
     * @param basicStatus
     *            Set basicStatus value
     */

    public void setBasicStatus(EApplicationStatus basicStatus) {
        this.basicStatus = basicStatus;
    }

    /**
     * @return return the value of the var lastestTs
     */

    public String getLastestTs() {
        return lastestTs;
    }

    /**
     * @param lastestTs
     *            Set lastestTs value
     */

    public void setLastestTs(String lastestTs) {
        this.lastestTs = lastestTs;
    }

    /**
     * @return return the value of the var earliestTs
     */

    public String getEarliestTs() {
        return earliestTs;
    }

    /**
     * @param earliestTs
     *            Set earliestTs value
     */

    public void setEarliestTs(String earliestTs) {
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
     * @return return the value of the var orgLegalPerson
     */

    public String getOrgLegalPerson() {
        return orgLegalPerson;
    }
    
    /**
     * @return return the mask value of the var orgLegalPerson
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
     * @return return the value of the var investorAgent
     */

    public String getInvestorAgent() {
        return investorAgent;
    }

    /**
     * @param investorAgent
     *            Set investorAgent value
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
     * @param financeAgent
     *            Set financeAgent value
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
     * @param agentRegister
     *            Set agentRegister value
     */

    public void setAgentRegister(String agentRegister) {
        this.agentRegister = agentRegister;
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

	public EUserStatus getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(EUserStatus activeStatus) {
		this.activeStatus = activeStatus;
	}

}
