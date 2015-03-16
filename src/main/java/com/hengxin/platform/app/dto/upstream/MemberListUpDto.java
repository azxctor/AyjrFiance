/*
 * Project Name: kmfex-platform
 * File Name: MemberListUpDto.java
 * Class Name: MemberListUpDto
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
package com.hengxin.platform.app.dto.upstream;

/**
 * MemberListUpDto.
 * 
 */
public class MemberListUpDto extends PagingDto {
	private static final long serialVersionUID = 5L;
	
	private String userRole; // 会员类型
	private String userName; // 用户名
	private String userType; // 机构还是个人
	private String name; // 姓名
	private String registTime; // 代注册时间
	private String investorStatus; // 投资权限状态
	private String financeStatus; // 融资权限状态
	private String agentRegister; // 是否为代注册
	private String agentName; // 介绍人名称
//	private String displayStart;
    private int displayLength; // 每页显示条目数
	
	
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRegistTime() {
		return registTime;
	}
	public void setRegistTime(String registTime) {
		this.registTime = registTime;
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
	public String getAgentRegister() {
		return agentRegister;
	}
	public void setAgentRegister(String agentRegister) {
		this.agentRegister = agentRegister;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public int getDisplayLength() {
		return displayLength;
	}
	public void setDisplayLength(int displayLength) {
		this.displayLength = displayLength;
	}
}
