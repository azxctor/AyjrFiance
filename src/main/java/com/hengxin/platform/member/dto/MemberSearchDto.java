/*
 * Project Name: kmfex-platform
 * File Name: MemberSearch.java
 * Class Name: MemberSearch
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

import com.hengxin.platform.common.dto.DataTablesRequestDto;
import com.hengxin.platform.member.enums.EApplicationStatus;
import com.hengxin.platform.member.enums.EMemberType;
import com.hengxin.platform.member.enums.EUserType;

/**
 * Class Name: MemberSearch
 * 
 * @author shengzhou
 * 
 */
public class MemberSearchDto extends DataTablesRequestDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("user_type")
    private EUserType userType;

    @JsonProperty("user_id")
    private String userId;
    
    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("name")
    private String name;

    @JsonProperty("user_role")
    private EMemberType userRole;

    @JsonProperty("finance_status")
    private EApplicationStatus financeStatus;

    @JsonProperty("investor_status")
    private EApplicationStatus investorStatus;
    
    @JsonProperty("authzdcenter_status")
    private EApplicationStatus serviceCenterStatus;

    @JsonProperty("productservice_status")
    private EApplicationStatus productServiceStatus;
    
    @JsonProperty("audit_content")
    private EMemberType auditContent;

    @JsonProperty("audit_status")
    private EApplicationStatus auditStatus;
    
    @JsonProperty("invstr_agent")
    private String investorAgent;
    
    @JsonProperty("fncr_agent")
    private String financeAgent;

    @JsonProperty("current_user")
    private String currentUser;
    
    @JsonProperty("regist_time")
    private String registTime;
    
    @JsonProperty("agent_register")
    private String agentRegister;
    
    @JsonProperty("audit_pass")
    private String auditPass;

    @JsonProperty("agent_name")
    private String agentname;
    
    @JsonProperty("trans_no")
    private String transno;
    
    

 

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
     * @return return the value of the var userName
     */
    
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName Set userName value
     */
    
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
     * @return return the value of the var serviceCenterStatus
     */
    
    public EApplicationStatus getServiceCenterStatus() {
        return serviceCenterStatus;
    }

    /**
     * @param serviceCenterStatus Set serviceCenterStatus value
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
     * @param productServiceStatus Set productServiceStatus value
     */
    
    public void setProductServiceStatus(EApplicationStatus productServiceStatus) {
        this.productServiceStatus = productServiceStatus;
    }

    /**
     * @return return the value of the var auditContent
     */
    
    public EMemberType getAuditContent() {
        return auditContent;
    }

    /**
     * @param auditContent Set auditContent value
     */
    
    public void setAuditContent(EMemberType auditContent) {
        this.auditContent = auditContent;
    }

    /**
     * @return return the value of the var auditStatus
     */
    
    public EApplicationStatus getAuditStatus() {
        return auditStatus;
    }

    /**
     * @param auditStatus Set auditStatus value
     */
    
    public void setAuditStatus(EApplicationStatus auditStatus) {
        this.auditStatus = auditStatus;
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
     * @return return the value of the var currentUser
     */
    
    public String getCurrentUser() {
        return currentUser;
    }

    /**
     * @param currentUser Set currentUser value
     */
    
    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * @return return the value of the var registTime
     */
    
    public String getRegistTime() {
        return registTime;
    }

    /**
     * @param registTime Set registTime value
     */
    
    public void setRegistTime(String registTime) {
        this.registTime = registTime;
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
	 * @return the agentname
	 */
	public String getAgentname() {
		return agentname;
	}

	/**
	 * @param agentname the agentname to set
	 */
	public void setAgentname(String agentname) {
		this.agentname = agentname;
	}
	
	/**
	 * 
	 * @return acct_no
	 */
	public String getTransno() {
		return transno;
	}
	/**
	 * 
	 * @param transno acct_no
	 */
	public void setTransno(String transno) {
		this.transno = transno;
	}

}
