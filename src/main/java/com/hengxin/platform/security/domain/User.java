/*
 * Project Name: kmfex-platform
 * File Name: User.java
 * Class Name: User
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

package com.hengxin.platform.security.domain;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.hengxin.platform.common.constant.ApplicationConstant;
import com.hengxin.platform.common.dto.upstream.RegisterDto.RegisterGroup;
import com.hengxin.platform.common.service.validator.ExistUserNameAndIdCheck;
import com.hengxin.platform.common.service.validator.ExistUserNameCheck;
import com.hengxin.platform.common.service.validator.UserNameLengthCheck;
import com.hengxin.platform.member.enums.EUserType;
import com.hengxin.platform.security.enums.BindingCardStatusEnum;
import com.hengxin.platform.security.enums.EswAcctStatusEnum;

/**
 * Class Name: User
 * 
 * @author shengzhou
 * 
 */
@SuppressWarnings("serial")
public class User implements Serializable {

	private String userId;

	@NotEmpty(message = "{member.error.username.empty}", groups = {
			RegisterGroup.class, NickNameGroup.class })
	// @Length(min = 4, max=15, message = "{member.error.username.length}",
	// groups = { RegisterGroup.class, NickNameGroup.class })
	@UserNameLengthCheck(groups = { RegisterGroup.class, NickNameGroup.class })
	@Pattern(regexp = ApplicationConstant.USER_NAME_REGEXP, groups = {
			RegisterGroup.class, NickNameGroup.class }, message = "{member.error.username.format}")
	@ExistUserNameCheck(groups = { RegisterGroup.class })
	@ExistUserNameAndIdCheck(groups = { NickNameGroup.class })
	private String username;

	@NotEmpty(message = "{member.error.password.empty}", groups = { RegisterGroup.class })
	@Length(min = 6, message = "{member.error.password.length}", groups = { RegisterGroup.class })
	private String password;

	@NotEmpty(message = "{member.error.email.empty}", groups = { RegisterGroup.class })
	@Length(min = 4, message = "{member.error.email.length}", groups = { RegisterGroup.class })
	@Email(regexp = ApplicationConstant.EMAIL_REGEXP, message = "{member.error.email.format}", groups = { RegisterGroup.class })
	private String email;

	@NotEmpty(message = "{member.error.mobile.empty}", groups = { RegisterGroup.class })
	@Pattern(regexp = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$", message = "{member.error.phone.invaild}", groups = { RegisterGroup.class })
	private String mobile;

	private String status;

	// P for person, O for organization
	private EUserType type;

	private String name;

	private Long failureCount;

	// null for register by oneself
	private String creator;

	private Date lastLoginTs;

	private Date createTs;

	private String lastMntOpid;

	private Date lastMntTs;

	private String region;

	private String captcha;

	private String ownerId;

	private Boolean showBulletin;

	private String companyId;

	private String frozenOperator;

	private String frozenDate;

	private String frozenMemo;

	private EswAcctStatusEnum eswAcctStatus;// 第三方支付平台是否激活

	private BindingCardStatusEnum bindingCardStatus;// 绑卡状态

	public String getFrozenOperator() {
		return frozenOperator;
	}

	public void setFrozenOperator(String frozenOperator) {
		this.frozenOperator = frozenOperator;
	}

	public String getFrozenDate() {
		return frozenDate;
	}

	public void setFrozenDate(String frozenDate) {
		this.frozenDate = frozenDate;
	}

	public String getFrozenMemo() {
		return frozenMemo;
	}

	public void setFrozenMemo(String frozenMemo) {
		this.frozenMemo = frozenMemo;
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
	 * @return return the value of the var captcha
	 */
	public String getCaptcha() {
		return captcha;
	}

	/**
	 * @param captcha
	 *            Set captcha value
	 */
	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	/**
	 * @return return the value of the var username
	 */

	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            Set username value
	 */

	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return return the value of the var password
	 */

	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            Set password value
	 */

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return return the value of the var email
	 */

	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            Set email value
	 */

	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return return the value of the var mobile
	 */

	public String getMobile() {
		return mobile;
	}

	/**
	 * @param mobile
	 *            Set mobile value
	 */

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * @return return the value of the var status
	 */

	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            Set status value
	 */

	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return return the value of the var type
	 */

	public EUserType getType() {
		return type;
	}

	/**
	 * @param type
	 *            Set type value
	 */

	public void setType(EUserType type) {
		this.type = type;
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
	 * @return return the value of the var failureCount
	 */

	public Long getFailureCount() {
		return failureCount;
	}

	/**
	 * @param failureCount
	 *            Set failureCount value
	 */

	public void setFailureCount(Long failureCount) {
		this.failureCount = failureCount;
	}

	/**
	 * @return return the value of the var creator
	 */

	public String getCreator() {
		return creator;
	}

	/**
	 * @param creator
	 *            Set creator value
	 */

	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * @return return the value of the var lastLoginTs
	 */

	public Date getLastLoginTs() {
		return lastLoginTs;
	}

	/**
	 * @param lastLoginTs
	 *            Set lastLoginTs value
	 */

	public void setLastLoginTs(Date lastLoginTs) {
		this.lastLoginTs = lastLoginTs;
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
	 * @return return the value of the var lastMntOpid
	 */

	public String getLastMntOpid() {
		return lastMntOpid;
	}

	/**
	 * @param lastMntOpid
	 *            Set lastMntOpid value
	 */

	public void setLastMntOpid(String lastMntOpid) {
		this.lastMntOpid = lastMntOpid;
	}

	/**
	 * @return return the value of the var lastMntTs
	 */

	public Date getLastMntTs() {
		return lastMntTs;
	}

	/**
	 * @param lastMntTs
	 *            Set lastMntTs value
	 */

	public void setLastMntTs(Date lastMntTs) {
		this.lastMntTs = lastMntTs;
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
	 * @return return the value of the var ownerId
	 */

	public String getOwnerId() {
		return ownerId;
	}

	/**
	 * @param ownerId
	 *            Set ownerId value
	 */

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public Boolean getShowBulletin() {
		return showBulletin;
	}

	public void setShowBulletin(Boolean showBulletin) {
		this.showBulletin = showBulletin;
	}

	/**
	 * <strong>Fields and methods should be before inner classes.</strong>
	 */
	public interface NickNameGroup {

	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
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
