/*
 * Project Name: kmfex-platform
 * File Name: UserPo.java
 * Class Name: UserPo
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

package com.hengxin.platform.security.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import com.hengxin.platform.common.entity.id.IdInjectionEntityListener;
import com.hengxin.platform.member.domain.Member;
import com.hengxin.platform.member.domain.UserRole;
import com.hengxin.platform.member.domain.converter.UserTypeEnumConverter;
import com.hengxin.platform.member.enums.EUserType;
import com.hengxin.platform.product.domain.converter.BooleanStringConverter;
import com.hengxin.platform.security.convert.BindingCardStatusEnumConvert;
import com.hengxin.platform.security.convert.EswAcctStatusEnumConvert;
import com.hengxin.platform.security.enums.BindingCardStatusEnum;
import com.hengxin.platform.security.enums.EswAcctStatusEnum;

/**
 * Class Name: UserPo
 * 
 * @author shengzhou
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "UM_USER")
@EntityListeners(IdInjectionEntityListener.class)
public class UserPo implements Serializable {

	@Id
	@Column(name = "USER_ID")
	private String userId;

	@Column(name = "USER_NAME")
	private String username;

	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "MOBILE")
	private String mobile;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "USER_TYPE")
	@Convert(converter = UserTypeEnumConverter.class)
	private EUserType type;

	@Column(name = "NAME")
	private String name;

	@Column(name = "REGION_CD")
	private String region;

	@Column(name = "LOGIN_FAILURE_CT")
	private Long failureCount;

	@Column(name = "CREATE_OPID")
	private String creator;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_LOGIN_TS")
	private Date lastLoginTs;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TS")
	private Date createTs;

	@Column(name = "LAST_MNT_OPID")
	private String lastMntOpid;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MNT_TS")
	private Date lastMntTs;

	@Column(name = "OWNER_ID")
	private String ownerId;

	@Column(name = "SHOW_BULLETIN_FLG")
	@Convert(converter = BooleanStringConverter.class)
	private Boolean showBulletin;

	@Version
	@Column(name = "VERSION_CT")
	private Long versionCt;

	@OneToMany(mappedBy = "userPo", fetch = FetchType.LAZY)
	private List<UserRole> userRole;

	@Column(name = "COMPANY_ID", nullable = true)
	private String companyId;

	@Column(name = "FRZ_USER")
	private String frozenOperator;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FRZ_DATE")
	private Date frozenDate;

	@Column(name = "FRZ_MEMO")
	private String frozenMemo;

	@Column(name = "ICON_FILE_ID")
	private String iconFileId;

	@Column(name = "ESW_ACCT_STATUS")
	@Convert(converter = EswAcctStatusEnumConvert.class)
	private EswAcctStatusEnum eswAcctStatus;

	@Column(name = "BINDING_CARD_STATUS")
	@Convert(converter = BindingCardStatusEnumConvert.class)
	private BindingCardStatusEnum bindingCardStatus;

	// @OneToOne(mappedBy = "userPo", fetch = FetchType.LAZY)
	// private AcctPo account;

	@OneToOne(mappedBy = "userPo", fetch = FetchType.LAZY)
	private Member member;

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

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

	public Date getCreateTs() {
		return createTs;
	}

	public void setCreateTs(Date createTs) {
		this.createTs = createTs;
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

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public Long getVersionCt() {
		return versionCt;
	}

	public void setVersionCt(Long versionCt) {
		this.versionCt = versionCt;
	}

	public List<UserRole> getUserRole() {
		return userRole;
	}

	public void setUserRole(List<UserRole> userRole) {
		this.userRole = userRole;
	}

	public Boolean getShowBulletin() {
		return showBulletin;
	}

	public void setShowBulletin(Boolean showBulletin) {
		this.showBulletin = showBulletin;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	// public AcctPo getAccount() {
	// return account;
	// }
	//
	// public void setAccount(AcctPo account) {
	// this.account = account;
	// }

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

}
