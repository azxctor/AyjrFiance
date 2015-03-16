package com.hengxin.platform.escrow.service;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.entity.id.IdUtil;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.service.ActionHistoryService;
import com.hengxin.platform.ebc.consts.EBCConsts;
import com.hengxin.platform.ebc.dto.EbcEditAcctRequest;
import com.hengxin.platform.ebc.dto.EbcErrorResponse;
import com.hengxin.platform.ebc.dto.EbcSignUpRequest;
import com.hengxin.platform.ebc.dto.EbcSignUpResponse;
import com.hengxin.platform.ebc.util.MD5;
import com.hengxin.platform.escrow.dto.CommandResponse;
import com.hengxin.platform.escrow.dto.EswAcctDto;
import com.hengxin.platform.escrow.dto.EswMobileDto;
import com.hengxin.platform.escrow.dto.EswPayPwdDto;
import com.hengxin.platform.escrow.dto.EswSignupDto;
import com.hengxin.platform.escrow.entity.EswAcctPo;
import com.hengxin.platform.escrow.enums.EEbcUserType;
import com.hengxin.platform.escrow.repository.EswAcctRepository;
import com.hengxin.platform.escrow.utils.EswUtils;
import com.hengxin.platform.fund.service.AcctService;
import com.hengxin.platform.member.domain.Agency;
import com.hengxin.platform.member.domain.AgencyApplication;
import com.hengxin.platform.member.domain.FinancierInfo;
import com.hengxin.platform.member.domain.Member;
import com.hengxin.platform.member.domain.MemberApplication;
import com.hengxin.platform.member.enums.EUserType;
import com.hengxin.platform.member.repository.AgencyApplicationRepository;
import com.hengxin.platform.member.repository.AgencyRepository;
import com.hengxin.platform.member.repository.MemberApplicationRepository;
import com.hengxin.platform.member.repository.MemberRepository;
import com.hengxin.platform.member.repository.UserRepository;
import com.hengxin.platform.member.service.MemberService;
import com.hengxin.platform.security.entity.UserPo;
import com.hengxin.platform.security.enums.BindingCardStatusEnum;
import com.hengxin.platform.security.enums.EswAcctStatusEnum;
import com.hengxin.platform.security.service.UserService;

/**
 * 第三方托管账户
 * 
 * @author chenwulou
 * 
 */
@Service
public class EswAccountService extends EswBaseService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(EswAccountService.class);
	
	@Autowired
	private UserService userService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private AcctService acctService;
	@Autowired
	private EswAcctService eswAcctService;
	@Autowired
	private EswMobileService ebcMobileService;
	@Autowired
	private EswAcctRepository eswAcctRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private AgencyRepository agencyRepository;
	@Autowired
	private MemberApplicationRepository memberApplicationRepository;
	@Autowired
	private AgencyApplicationRepository agencyApplicationRepository;
	@Autowired
	private ActionHistoryService actionHistoryService;

	/**
	 * 会员注册审核通过，默认创建
	 * 
	 * @param userId
	 * @param acctNo
	 * @param currOpId
	 */
	public void createEswAcct(String userId, String acctNo, String currOpId) {
		// EswAcctPo eswAcctPo = new EswAcctPo();
		// eswAcctPo.setAcctNo(acctNo);
		// eswAcctPo.setUserId(userId);
		// eswAcctPo.setCreateOpId(currOpId);
		// eswAcctPo.setCreateTs(new Date());
		// eswAcctRepository.save(eswAcctPo);
	}

	/**
	 * 开通托管账户
	 * 
	 * @param userId
	 * @param payPwd
	 *            支付密码
	 * @return
	 */
	@Transactional
	public void signup(EswSignupDto eswSignupDto) {
		String userId = eswSignupDto.getUserId();
		LOGGER.info("signup() invoked, {}", userId);
		EbcSignUpRequest request = buildSignUpRequest();
		UserPo userPo = userRepository.findUserByUserId(userId);
		String idCard = userService.getIdNo(userId);
		String orgNo = null;
		String licenceNo = null;
		Agency agency = memberService.getAgencyByUserId(userId);
		if (agency != null) {
			// 担保机构 或者 服务中心
			orgNo = agency.getOrgNo();
			licenceNo = agency.getLicenceNo();
			request.setBusiness(orgNo);
			request.setOrganization(licenceNo);
			request.setUserType(EEbcUserType.ENTERPRISE.getCode());
		} else {
			if (userPo.getType() == EUserType.ORGANIZATION) {
				// 融资机构
				FinancierInfo fncrInfo = memberService.getFinancierById(userId);
				if (fncrInfo != null) {
					orgNo = fncrInfo.getOrgNumber();
					licenceNo = fncrInfo.getLicenceNumber();
					request.setBusiness(orgNo);
					request.setOrganization(licenceNo);
					request.setUserType(EEbcUserType.ENTERPRISE.getCode());
				}
			}
		}
		request.setEnterName(userPo.getUserId());
		request.setUserName(userPo.getName());
		request.setIdCard(idCard);
		request.setMobile(userPo.getMobile());
		request.setPayPass(MD5.md5(eswSignupDto.getPayPwd()));
		CommandResponse rawResponse = request.execute();
		if (isSuccessRespsonse(rawResponse)) {
			EbcSignUpResponse response = (EbcSignUpResponse) rawResponse;
			String acctNo = acctService.getAcctByUserId(userId).getAcctNo();
			// 第三方托管账户信息入库
			EswAcctPo eswAcctPo = new EswAcctPo();
			eswAcctPo.setAcctNo(acctNo);
			eswAcctPo.setUserId(userId);
			eswAcctPo.setEswCustAcctNo(request.getMerchNo());
			eswAcctPo.setEswAcctNo(response.getMediumNo());
			eswAcctPo.setEswSubAcctNo(response.getCardNo());
			eswAcctPo.setEswUserNo(response.getUserNo());
			eswAcctPo.setEswUserType(request.getUserType());
			// 实际字段长度超过数据库字段长度，故先不存入
			// eswAcctPo.setEswLoginPwd(userPo.getPassword());
			eswAcctPo.setEswPayPwd(request.getPayPass());
			eswAcctPo.setStatus(response.getIsInit());
			eswAcctPo.setCurr(request.getCurrency());
			eswAcctPo.setServProv(request.getOwnerId());
			eswAcctPo.setCreateOpId(userId);
			eswAcctPo.setCreateTs(new Date());
			userPo.setBindingCardStatus(BindingCardStatusEnum.UNDO);
			userPo.setEswAcctStatus(EswAcctStatusEnum.DONE);
			eswAcctRepository.save(eswAcctPo);
			userRepository.save(userPo);
		} else {
			EbcErrorResponse response = (EbcErrorResponse) rawResponse;
			EErrorCode errorCode = EErrorCode.EBC_SIGNUP_ERROR;
			errorCode.setArgs(new Object[] { response.getErrText() });
			throw new BizServiceException(errorCode);
		}
	}

	/**
	 * 默认返回信息录入
	 * 
	 * @return
	 */
	private EbcSignUpRequest buildSignUpRequest() {
		EbcSignUpRequest request = new EbcSignUpRequest();
		request.setMerchNo(EswUtils.getEswMerChNo());
		request.setOrderSn(IdUtil.produce());
		request.setOwnerId(EBCConsts.OWNER_ID);
		request.setLoginType(EBCConsts.LOGIN_TYPE);
		request.setIdType(EBCConsts.ID_TYPE);
		request.setWltNo(EBCConsts.WLT_NO);
		request.setCurrency(EBCConsts.CURRENCY);
		request.setUserType(EEbcUserType.PERSON.getCode());
		request.setBusiness(null);
		request.setOrganization(null);
		return request;
	}

	/**
	 * 修改支付密码
	 * 
	 * @param eswPayPwdDto
	 */
	@Transactional
	public void updatePayPwd(EswPayPwdDto eswPayPwdDto) {
		LOGGER.info("updatePayPwd() invoked");
		String userId = eswPayPwdDto.getUserId();
		String newPayPwd = eswPayPwdDto.getNewPayPwd();
		String oldPayPwd = eswPayPwdDto.getOriginalPayPwd();
		oldPayPwd = MD5.md5(oldPayPwd);
		newPayPwd = MD5.md5(newPayPwd);
		String authCode = eswPayPwdDto.getAuthCode();
		EbcEditAcctRequest request = buildUpdateAcctRequest();
		EswAcctPo eswAcctPo = eswAcctRepository.findByUserId(userId);// 获取账户信息
		String mediumNo = eswAcctPo.getEswAcctNo();// 钱包介质ID
		String userNo = eswAcctPo.getEswUserNo();// 用户号
		request.setUserNo(userNo);
		request.setMediumNo(mediumNo);
		request.setOldpayPass(oldPayPwd);
		request.setPayPass(newPayPwd);
		request.setAutoMobile(authCode);
		CommandResponse rawResponse = request.execute();
		if (isSuccessRespsonse(rawResponse)) {
			eswAcctPo.setEswPayPwd(newPayPwd);
			eswAcctPo.setLastMntTs(new Date());
			eswAcctPo.setLastMntOpId(userId);
			eswAcctRepository.save(eswAcctPo);
		} else {
			EbcErrorResponse response = (EbcErrorResponse) rawResponse;
			EErrorCode errorCode = EErrorCode.EBC_EDIT_ACCOUNT_ERROR;
			errorCode.setArgs(new Object[] { response.getErrText() });
			throw new BizServiceException(errorCode);
		}
		LOGGER.debug("updatePayPwd() completed");
	}
	
	/**
	 * 重置支付密码
	 * 
	 * @param eswPayPwdDto
	 */
	@Transactional
	public String resetPayPwd(EswPayPwdDto eswPayPwdDto){
		LOGGER.info("resetPayPwd() invoked");
		String userId = eswPayPwdDto.getUserId();
		EswAcctPo eswAcctPo = eswAcctRepository.findByUserId(userId);// 获取账户信息
		String oldPayPwd = eswAcctPo.getEswPayPwd();
		/** 产生6位随机数字密码 **/
		String newPayPwd = EswUtils.getRandomStr(6);
		String md5NewPayPwd = MD5.md5(newPayPwd);
		String authCode = eswPayPwdDto.getAuthCode();
		String mediumNo = eswAcctPo.getEswAcctNo();// 钱包介质ID
		String userNo = eswAcctPo.getEswUserNo();// 用户号
		EbcEditAcctRequest request = buildUpdateAcctRequest();
		request.setUserNo(userNo);
		request.setMediumNo(mediumNo);
		request.setOldpayPass(oldPayPwd);
		request.setPayPass(md5NewPayPwd);
		request.setAutoMobile(authCode);
		CommandResponse rawResponse = request.execute();
		if (isSuccessRespsonse(rawResponse)) {
			eswAcctPo.setEswPayPwd(md5NewPayPwd);
			eswAcctPo.setLastMntTs(new Date());
			eswAcctPo.setLastMntOpId(userId);
			eswAcctRepository.save(eswAcctPo);
		} else {
			EbcErrorResponse response = (EbcErrorResponse) rawResponse;
			EErrorCode errorCode = EErrorCode.EBC_EDIT_ACCOUNT_ERROR;
			errorCode.setArgs(new Object[] { response.getErrText() });
			throw new BizServiceException(errorCode);
		}
		return newPayPwd;
	}

	/**
	 * 修改手机号
	 * 
	 * @param userId
	 * @param mobileNo
	 */
	@Transactional
	public void updateMobile(EswMobileDto eswMobileDto) {
		LOGGER.info("updateMobile() invoked");
		String userId = eswMobileDto.getUserId();
		String mobile = eswMobileDto.getMobile();
		String authCode = eswMobileDto.getAuthCode();
		EbcEditAcctRequest request = buildUpdateAcctRequest();
		EswAcctPo eswAcctPo = eswAcctRepository.findByUserId(userId);// 获取第三方托管账户信息
		String mediumNo = eswAcctPo.getEswAcctNo();// 钱包介质ID
		String userNo = eswAcctPo.getEswUserNo();// 用户号
		request.setMediumNo(mediumNo);
		request.setUserNo(userNo);
		request.setPhone(mobile);
		request.setAutoMobile(authCode);
		CommandResponse rawResponse = request.execute();
		if (isSuccessRespsonse(rawResponse)) {
			UserPo userPo = userRepository.findUserByUserId(userId);// 获取用户基本信息
			userPo.setMobile(mobile);
			Agency agency = memberService.getAgencyByUserId(userId);
			if (null != agency) {
				agency.setContactMobile(mobile);
				agencyRepository.save(agency);
				AgencyApplication agencyApplication = memberService
						.getAgencyApplicationWithLatestStatus(userId);
				agencyApplication.setContactMobile(mobile);
				agencyApplicationRepository.save(agencyApplication);
			} else {
				Member member = memberService.getMemberByUserId(userId);
				member.setPersonMobile(mobile);
				memberRepository.save(member);
				MemberApplication memberApplication = memberService
						.getMemberWithLatestStatus(userId);
				memberApplication.setPersonMobile(mobile);
				memberApplicationRepository.save(memberApplication);
			}
			eswAcctPo.setLastMntTs(new Date());
			eswAcctPo.setLastMntOpId(userId);
			eswAcctRepository.save(eswAcctPo);
			userRepository.save(userPo);
		} else {
			EbcErrorResponse response = (EbcErrorResponse) rawResponse;
			EErrorCode errorCode = EErrorCode.EBC_EDIT_ACCOUNT_ERROR;
			errorCode.setArgs(new Object[] { response.getErrText() });
			throw new BizServiceException(errorCode);
		}
		LOGGER.debug("updateMobile() completed");
	}

	/**
	 * 默认返回信息录入
	 * 
	 * @return
	 */
	private EbcEditAcctRequest buildUpdateAcctRequest() {
		EbcEditAcctRequest request = new EbcEditAcctRequest();
		request.setMerchNo(EswUtils.getEswMerChNo());
		request.setOrderSn(IdUtil.produce());
		request.setOwnerId(EswUtils.getEswServProv());
		return request;
	}

	/**
	 * 判断是否上传身份证图片
	 * 
	 * @param userId
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean isIdCardImgNull(String userId) {
		boolean isIdCardImgNull = true;
		Member member = memberService.getMemberByUserId(userId);
		if (member != null) {
			String personIdCardFrontImg = member.getPersonIdCardFrontImg();
			String personIdCardBackImg = member.getPersonIdCardBackImg();
			String orgIdCardFrontImg = member.getOrgLegalPersonIdCardFrontImg();
			String orgIdCardBackImg = member.getOrgLegalPersonIdCardBackImg();
			isIdCardImgNull = StringUtils.isBlank(orgIdCardBackImg)
					&& StringUtils.isBlank(orgIdCardFrontImg)
					&& StringUtils.isBlank(personIdCardBackImg)
					&& StringUtils.isBlank(personIdCardFrontImg);
		} else {
			Agency agency = memberService.getAgencyByUserId(userId);
			if (agency != null) {
				String orgIdCardFrontImg = agency
						.getOrgLegalPersonIdCardFrontImg();
				String orgIdCardBackImg = agency
						.getOrgLegalPersonIdCardBackImg();
				isIdCardImgNull = StringUtils.isBlank(orgIdCardBackImg)
						&& StringUtils.isBlank(orgIdCardFrontImg);
			}
		}
		return isIdCardImgNull;
	}

	/**
	 * 校验账户是否已经激活
	 * 
	 * @return
	 * @param userId
	 */
	@Transactional(readOnly = true)
	public boolean isExistingUser(String userId) {
		LOGGER.info("isExistingUser, userId {}", userId);
		EswAcctPo eswAcctPo = eswAcctRepository.findByUserId(userId);
		return eswAcctPo == null;
	}

	/**
	 * 验证输入的原支付密码是否与实际相同
	 * 
	 * @param submittedPayPwd
	 * @param userId
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean isValidPayPwd(String submittedPayPwd, String userId) {
		String encryptedPayPwd = eswAcctRepository.findByUserId(userId).getEswPayPwd();
		String md5SubmittedPayPwd = MD5.md5(submittedPayPwd);
		return StringUtils.equals(encryptedPayPwd, md5SubmittedPayPwd);
	}

	/**
	 * 校验银行卡是否已被绑定
	 * 
	 * @param bankCardNo
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean isExistingBankCardNo(String bankCardNo) {
		LOGGER.info("isExistingBankCardNo,  bankCardNo {}", bankCardNo);
		return null == eswAcctRepository.findByBankCardNo(bankCardNo);
	}

	public EswAcctDto findByAcctNo(String acctNo){
		EswAcctPo eswAcctPo = eswAcctRepository.findOne(acctNo);
		return ConverterService.convert(eswAcctPo, EswAcctDto.class);
	}

}