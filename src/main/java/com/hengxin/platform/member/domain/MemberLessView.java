/*
 * Project Name: kmfex-platform
 * File Name: MemberLessView.java
 * Class Name: MemberLessView
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

import com.hengxin.platform.member.domain.converter.MemberTypeEnumConverter;
import com.hengxin.platform.member.domain.converter.UserStatusEnumConverter;
import com.hengxin.platform.member.enums.EMemberType;
import com.hengxin.platform.member.enums.EUserStatus;

/**
 * Class Name: MemberLessView
 *
 * @author ycc
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "V_MEMBER_LESS")
public class MemberLessView implements Serializable {

    @Id
    @Column(name = "USER_ID")
    private String userId;
    
    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "NAME")
    private String name;
    
    @Column(name = "ACCT_NO")
    private String accountNo;
   
    @Column(name = "USER_ROLE")
    @Convert(converter = MemberTypeEnumConverter.class)
    private EMemberType userRole;
    
    @Column(name = "ACTIVE_STATUS")
    @Convert(converter = UserStatusEnumConverter.class)
    private EUserStatus activeStatus;

    @Column(name = "REGION_CD")
    private String region;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EARLIEST_TS")
    private Date earliestTs;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public EMemberType getUserRole() {
		return userRole;
	}

	public void setUserRole(EMemberType userRole) {
		this.userRole = userRole;
	}

	public EUserStatus getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(EUserStatus activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public Date getEarliestTs() {
		return earliestTs;
	}

	public void setEarliestTs(Date earliestTs) {
		this.earliestTs = earliestTs;
	}
    
    

}
