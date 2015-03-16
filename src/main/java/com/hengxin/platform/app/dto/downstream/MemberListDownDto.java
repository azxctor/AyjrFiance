/*
 * Project Name: kmfex-platform
 * File Name: MemberListDownDto.java
 * Class Name: MemberListDownDto
 *
 * Copyright 2014 Hengtian Software Inc
 *
 * Licensed under the Hengtiansoft
 *
 * http://www.hengtiansoft.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hengxin.platform.app.dto.downstream;

import java.io.Serializable;

/**
 * MemberListDownDto.
 * 
 * @author yicai
 *
 */
public class MemberListDownDto implements Serializable {
	private static final long serialVersionUID = 5L;
	
	private String userId; // 用户编号
    private String userName; // 用户名
    private String accountNo; // 交易账号
    private String lastestTs; // 注册日期
	private String agentRegister; // 代注册
	private String userRole; // 会员类型
	private String name; // 姓名 / 企业名称
	private String region; // 所在区域
	private String personJob; // 职业
	private String personEducation; // 学历状况
	private String agent; // 经办人
	private String agentName; // 介绍人
	private String investorStatus; // 投资权限状态
	private String financeStatus; // 融资权限状态
	
	private String orgIndustry; // 所属行业
	private String orgNature; // 企业性质
	private String orgType; // 企业类型
	
	
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
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getLastestTs() {
		return lastestTs;
	}
	public void setLastestTs(String lastestTs) {
		this.lastestTs = lastestTs;
	}
	public String getAgentRegister() {
		return agentRegister;
	}
	public void setAgentRegister(String agentRegister) {
		this.agentRegister = agentRegister;
	}
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getPersonJob() {
		return personJob;
	}
	public void setPersonJob(String personJob) {
		this.personJob = personJob;
	}
	public String getPersonEducation() {
		return personEducation;
	}
	public void setPersonEducation(String personEducation) {
		this.personEducation = personEducation;
	}
	public String getAgent() {
		return agent;
	}
	public void setAgent(String agent) {
		this.agent = agent;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public String getInvestorStatus() {
		return investorStatus;
	}
	public void setInvestorStatus(String investorStatus) {
		this.investorStatus = investorStatus;
	}
	public String getFinanceStatus() {
		return financeStatus;
	}
	public void setFinanceStatus(String financeStatus) {
		this.financeStatus = financeStatus;
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
}
