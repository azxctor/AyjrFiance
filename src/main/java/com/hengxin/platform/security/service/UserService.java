/*
 * Project Name: kmfex-platform
 * File Name: MemberService.java
 * Class Name: MemberService
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

package com.hengxin.platform.security.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.credential.HashingPasswordService;
import org.apache.shiro.crypto.hash.format.DefaultHashFormatFactory;
import org.apache.shiro.crypto.hash.format.HashFormat;
import org.apache.shiro.crypto.hash.format.HashFormatFactory;
import org.apache.shiro.crypto.hash.format.ParsableHashFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hazelcast.util.StringUtil;
import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.enums.ActionResult;
import com.hengxin.platform.common.enums.ActionType;
import com.hengxin.platform.common.enums.EntityType;
import com.hengxin.platform.common.service.ActionHistoryService;
import com.hengxin.platform.common.service.PagingService;
import com.hengxin.platform.common.util.PaginationUtil;
import com.hengxin.platform.fund.dto.AcctFreezeSearchDto;
import com.hengxin.platform.member.domain.Agency;
import com.hengxin.platform.member.domain.FinancierApplication;
import com.hengxin.platform.member.domain.InvestorApplication;
import com.hengxin.platform.member.domain.Member;
import com.hengxin.platform.member.domain.MemberApplication;
import com.hengxin.platform.member.dto.PasswordDto;
import com.hengxin.platform.member.enums.EMessageType;
import com.hengxin.platform.member.enums.EUserStatus;
import com.hengxin.platform.member.repository.UserRepository;
import com.hengxin.platform.member.service.MemberMessageService;
import com.hengxin.platform.member.service.MemberService;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.authc.LegacyPasswordMatchException;
import com.hengxin.platform.security.domain.User;
import com.hengxin.platform.security.entity.UserPo;
import com.hengxin.platform.security.enums.EBizRole;

/**
 * Class Name: MemberService
 *
 * @author shmilywen
 *
 */
@Transactional
@Service
public class UserService extends PagingService<UserPo> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(UserService.class);
	@Autowired
	private MemberMessageService memberMessageService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MemberService memberService;

	@Autowired
	private AuthorizationService authorizationService;

	@Autowired
	private HashingPasswordService passwordService;

	@Autowired
	private ActionHistoryService actionHistoryService;

	/**
	 * @param userRepository
	 *            Set userRepository value
	 */

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User updateUser(User user) {
		UserPo userPo = userRepository.findUserByUserId(user.getUserId());
		/**
		 * It cannot allow updating user info to blank value, this is only a
		 * precaution.
		 **/
		if (user.getUsername() != null && !user.getUsername().isEmpty()) {
			userPo.setUsername(user.getUsername());
		} else {
			LOGGER.info(" {} user name is null", user.getUserId());
		}
		if (user.getType() != null) {
			userPo.setType(user.getType());
		} else {
			LOGGER.info(" {} user type is null", user.getUserId());
		}
		if (user.getStatus() != null && !user.getStatus().isEmpty()) {
			userPo.setStatus(user.getStatus());
		} else {
			LOGGER.info(" {} user status is null", user.getUserId());
		}
		if (user.getRegion() != null && !user.getRegion().isEmpty()) {
			userPo.setRegion(user.getRegion());
		} else {
			LOGGER.info(" {} user region is null", user.getUserId());
		}
		if (user.getName() != null && !user.getName().isEmpty()) {
			userPo.setName(user.getName());
		} else {
			LOGGER.info(" {} user real name is null", user.getUserId());
		}
		if (user.getMobile() != null && !user.getMobile().isEmpty()) {
			userPo.setMobile(user.getMobile());
		} else {
			LOGGER.info(" {} user mobile is null", user.getUserId());
		}
		if (user.getEmail() != null && !user.getEmail().isEmpty()) {
			userPo.setEmail(user.getEmail());
		} else {
			LOGGER.info(" {} user email is null", user.getUserId());
		}
		userPo.setLastMntOpid(user.getLastMntOpid());
		userPo.setLastMntTs(user.getLastMntTs());
		userPo.setCompanyId(user.getCompanyId());
		userPo = userRepository.save(userPo);
		return ConverterService.convert(userPo, User.class);
	}

	public User getUserByUserId(String userId) {
		UserPo userPo = userRepository.findUserByUserId(userId);
		User user = ConverterService.convert(userPo, User.class);
		return user;
	}

	public User getUserByUserName(String userName) {
		UserPo userPo = userRepository.findUserByUsernameIgnoreCase(userName);
		User user = ConverterService.convert(userPo, User.class);
		return user;
	}

	public User getUserBySignIn(String userId, String password) {
		UserPo userPo = userRepository.findUserByUserIdAndPassword(userId,
				password);
		return ConverterService.convert(userPo, User.class);
	}

	public User createUser(User user, EBizRole role) {
		/**
		 * I know it is redundant due to it will set status for PO, but it
		 * really does not work without it.
		 **/
		user.setStatus(EUserStatus.ACTIVE.getCode());
		UserPo userPo = ConverterService.convert(user, UserPo.class);
		if (isPasswordPlainText(userPo.getPassword())) {// TODO: this need to be
														// removed after member
														// controller is
														// refactored.
			userPo.setPassword(passwordService.encryptPassword(user
					.getPassword()));
		}
		userPo.setStatus(EUserStatus.ACTIVE.getCode());
		userPo.setShowBulletin(Boolean.TRUE);
		Date now = new Date();
		userPo.setCreateTs(now);
		userPo.setLastMntTs(now);
		userRepository.save(userPo);
		userRepository.flush();
		authorizationService.assignRole(role, userPo.getUserId());
		user.setUserId(userPo.getUserId());
		return user;
	}

	public void createUserByAgency(User user, MemberApplication member,
			InvestorApplication investor, FinancierApplication financier) {
		LOGGER.info("createUserByAgency() invoked");
		createUser(user, EBizRole.TOURIST);
		LOGGER.debug("MEMBER ==== {}", member.toString());
		member.setUserId(user.getUserId());
		memberService.saveBasicInfo(member, user, ActionType.AGENTREGISTER,
				null);
		if (investor != null) {
			investor.setUserId(user.getUserId());
			LOGGER.debug("investor ==== {}", investor.toString());
			memberService.saveInvestorInfo(investor, ActionType.AGENTREGISTER,
					null);
		}
		if (financier != null) {
			financier.setUserId(user.getUserId());
			LOGGER.debug("financier ==== {}", financier.toString());
			memberService.saveFinancierInfo(financier,
					ActionType.AGENTREGISTER, null);
		}
	}

	@Transactional
	public void updatePassword(PasswordDto passwordDto) {
		LOGGER.info("updatePassword() invoked");
		userRepository.updatePassword(passwordDto.getUserName(),
				passwordService.encryptPassword(passwordDto.getNewPassword()),
				new Date());
		SecurityContext.clearAuthcCache(passwordDto.getUserName());// have to
																	// clear
																	// cache to
																	// take
																	// efffect
		LOGGER.debug("updatePassword() completed");
	}

	@Transactional
	public void resetPassword(PasswordDto passwordDto) {
		LOGGER.info("resetPassword() invoked");
		UserPo user = userRepository.findOne(passwordDto.getUserId());
		if (!StringUtil.isNullOrEmpty(passwordDto.getNewPassword())) {
			userRepository.updatePassword(user.getUsername(), passwordService
					.encryptPassword(passwordDto.getNewPassword()), new Date());
			actionHistoryService.save(EntityType.USER, user.getUserId(),
					ActionType.PASSWDRESER, ActionResult.PASSWORD_RESET,
					"密码重置为:" + passwordDto.getNewPassword());
		}
		try {
			String messageKey = "member.resetpassword.message";
			memberMessageService.sendMessage(EMessageType.MESSAGE, messageKey,
					passwordDto.getUserId(), passwordDto.getNewPassword());
			memberMessageService.sendMessage(EMessageType.SMS, messageKey,
					passwordDto.getUserId(), passwordDto.getNewPassword());
		} catch (Exception e) {
			LOGGER.error("Ex {}", e);
		}
		LOGGER.debug("resetPassword() completed");
	}

	@Transactional
	public void updateUserName(String userId, String newName, String oldName) {
		LOGGER.info("updateUserName() invoked");
		userRepository.updateUserName(userId, newName, new Date());
		this.actionHistoryService.saveUserLog(userId, oldName, newName, "用户名");
		SecurityContext.clearAuthcCache(oldName);
		SecurityContext.clearAuthzCache(oldName);
		SecurityContext.clearAuthcCache(newName);
		SecurityContext.clearAuthzCache(newName);
		SecurityContext.simulateLogin(newName);
		SecurityContext.resetUser(userId);
		LOGGER.debug("updateUserName() completed");
	}

	public boolean isValidPassward(String submittedPassword, String userId) {
		String encryptedPwd = userRepository.findOne(userId).getPassword();
		try {
			return passwordService.passwordsMatch(submittedPassword.trim(),
					encryptedPwd);
		} catch (LegacyPasswordMatchException ex) {
			return true;
		}
	}

	public boolean isExistingUser(String userName) {
		UserPo user = userRepository.findUserByUsernameIgnoreCase(userName);
		return user != null;
	}

	/**
	 * check user name duplicate or not by userId.
	 */
	public boolean isExistingUser(String userId, String userName) {
		LOGGER.info("isExistingUser, user id {}; user name {}", userId,
				userName);
		UserPo user = userRepository.findUserByUserIdNotAndUsernameIgnoreCase(
				userId, userName.trim().toLowerCase());
		return user != null;
	}

	private boolean isPasswordPlainText(String password) {
		HashFormatFactory hashFormatFactory = new DefaultHashFormatFactory();
		HashFormat discoveredFormat = hashFormatFactory.getInstance(password);

		if (discoveredFormat != null
				&& discoveredFormat instanceof ParsableHashFormat) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param queryDto
	 * @param status
	 *            , find frozen user if false.
	 * @return
	 */
	@Transactional(readOnly = true)
	public DataTablesResponseDto<User> getFrozenUsers(
			final AcctFreezeSearchDto queryDto, final boolean status) {
		LOGGER.info("getFrozenUsers() invoked");
		Pageable pageable = PaginationUtil.buildPageRequest(queryDto);
		Specification<UserPo> specification = new Specification<UserPo>() {
			@Override
			public Predicate toPredicate(Root<UserPo> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				List<Expression<Boolean>> expressions = predicate
						.getExpressions();
				if (!status) {
					expressions.add(cb.equal(root.<String> get("status"), "F"));
				}
				if (StringUtils.isNotBlank(queryDto.getUserName())) {
					expressions.add(cb.or(cb.like(
							cb.lower(root.<String> get("name")), "%"
									+ queryDto.getUserName().trim()
											.toLowerCase() + "%"), cb.like(
							cb.lower(root.<String> get("username")), "%"
									+ queryDto.getUserName().trim()
											.toLowerCase() + "%")));
				}
				return predicate;
			}
		};
		Page<UserPo> acctPage = this.userRepository.findAll(specification,
				pageable);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DataTablesResponseDto<User> dataDto = new DataTablesResponseDto<User>();
		List<User> queryList = new ArrayList<User>();
		for (UserPo userPo : acctPage) {
			User user = ConverterService.convert(userPo, User.class);
			try {
				String date = df.format(userPo.getFrozenDate());
				user.setFrozenDate(date);
			} catch (Exception e) {
				/** Ignore if date is invalid. **/
			}
			queryList.add(user);
		}
		dataDto.setTotalDisplayRecords(acctPage.getTotalElements());
		dataDto.setTotalRecords(acctPage.getTotalElements());
		dataDto.setData(queryList);
		dataDto.setEcho(queryDto.getEcho());
		return dataDto;
	}

	/**
	 * 
	 * @param userId
	 * @param status
	 *            , freeze user if false.
	 * @param memo
	 * @param operator
	 */
	@Transactional
	public void updateUserStatus(String userId, boolean status, String memo,
			String operator) {
		LOGGER.info("updateUserStatus() invoked");
		UserPo user = this.userRepository.findOne(userId);
		user.setStatus(status ? "A" : "F");
		user.setFrozenMemo(memo);
		user.setFrozenDate(new Date());
		user.setFrozenOperator(operator);
		this.userRepository.save(user);
	}

	/**
	 * 获取身份证号
	 * 
	 * @param userId
	 * @return
	 */
	public String getIdNo(String userId) {
		User user = getUserByUserId(userId);
		String idCard = null;
		switch (user.getType()) {
		case ORGANIZATION:
			Agency agency = memberService.getAgencyByUserId(user.getUserId());
			if (null != agency)
				idCard = agency.getOrgLegalPersonIdCardNumber();
			else {
				Member member = memberService.getMemberByUserId(user
						.getUserId());
				idCard = member.getOrgLegalPersonIdCardNumber();
			}
			break;
		case PERSON:
			Member member = memberService.getMemberByUserId(user.getUserId());
			idCard = member.getPersonIdCardNumber();
			break;
		default:
			break;
		}
		return idCard;
	}
}
