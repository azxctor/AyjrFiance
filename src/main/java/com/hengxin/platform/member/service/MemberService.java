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

package com.hengxin.platform.member.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.domain.DynamicOption;
import com.hengxin.platform.common.dto.upstream.AgencyRegisterInfoDto;
import com.hengxin.platform.common.entity.ActionHistoryPo;
import com.hengxin.platform.common.entity.DynamicOptionPo;
import com.hengxin.platform.common.entity.id.IdUtil;
import com.hengxin.platform.common.enums.ActionResult;
import com.hengxin.platform.common.enums.ActionType;
import com.hengxin.platform.common.enums.EOptionCategory;
import com.hengxin.platform.common.enums.EntityType;
import com.hengxin.platform.common.repository.DynamicOptionRepository;
import com.hengxin.platform.common.service.ActionHistoryService;
import com.hengxin.platform.common.util.SystemDictUtil;
import com.hengxin.platform.fund.domain.Acct;
import com.hengxin.platform.fund.entity.AcctPo;
import com.hengxin.platform.fund.entity.BankAcctPo;
import com.hengxin.platform.fund.repository.BankAcctRepository;
import com.hengxin.platform.fund.service.AcctService;
import com.hengxin.platform.member.domain.Agency;
import com.hengxin.platform.member.domain.AgencyApplication;
import com.hengxin.platform.member.domain.FinancierApplication;
import com.hengxin.platform.member.domain.FinancierInfo;
import com.hengxin.platform.member.domain.InvestorApplication;
import com.hengxin.platform.member.domain.InvestorInfo;
import com.hengxin.platform.member.domain.Member;
import com.hengxin.platform.member.domain.MemberApplication;
import com.hengxin.platform.member.domain.ProductProviderApplication;
import com.hengxin.platform.member.domain.ProductProviderInfo;
import com.hengxin.platform.member.domain.ServiceCenterApplication;
import com.hengxin.platform.member.domain.ServiceCenterInfo;
import com.hengxin.platform.member.dto.downstream.ProductProviderApplicationSearchDto;
import com.hengxin.platform.member.dto.downstream.ServiceCenterApplicationDto;
import com.hengxin.platform.member.dto.upstream.AgencyApplicationCommonDto;
import com.hengxin.platform.member.dto.upstream.AgencyApplicationDto;
import com.hengxin.platform.member.dto.upstream.AgencyApplicationSearchDto;
import com.hengxin.platform.member.dto.upstream.AgencyDto;
import com.hengxin.platform.member.dto.upstream.ProductProviderDto;
import com.hengxin.platform.member.dto.upstream.ServiceCenterDto;
import com.hengxin.platform.member.enums.EApplicationStatus;
import com.hengxin.platform.member.enums.EMemberType;
import com.hengxin.platform.member.enums.EUserType;
import com.hengxin.platform.member.repository.AgencyApplicationRepository;
import com.hengxin.platform.member.repository.AgencyRepository;
import com.hengxin.platform.member.repository.FinancierApplicationRepository;
import com.hengxin.platform.member.repository.FinancierInfoRepository;
import com.hengxin.platform.member.repository.InvestorApplicationRepository;
import com.hengxin.platform.member.repository.InvestorInfoRepository;
import com.hengxin.platform.member.repository.MemberApplicationRepository;
import com.hengxin.platform.member.repository.MemberRepository;
import com.hengxin.platform.member.repository.ProductProviderApplicationRepository;
import com.hengxin.platform.member.repository.ProductProviderRepository;
import com.hengxin.platform.member.repository.ServiceCenterApplicationRepository;
import com.hengxin.platform.member.repository.ServiceCenterInfoRepository;
import com.hengxin.platform.member.repository.UserRepository;
import com.hengxin.platform.security.domain.User;
import com.hengxin.platform.security.enums.EBizRole;
import com.hengxin.platform.security.service.AuthorizationService;
import com.hengxin.platform.security.service.UserService;

/**
 * Class Name: MemberService
 * 
 * @author runchen
 * 
 */
@Service
public class MemberService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MemberService.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private MemberApplicationRepository memberApplRepository;

	@Autowired
	private InvestorInfoRepository investorInfoRepository;

	@Autowired
	private InvestorApplicationRepository investorApplRepository;

	@Autowired
	private FinancierApplicationRepository financierApplRepository;

	@Autowired
	private FinancierInfoRepository financierInfoRepository;

	@Autowired
	private AgencyApplicationRepository agencyApplicationRepository;

	@Autowired
	private ServiceCenterApplicationRepository serviceCenterApplicationRepository;

	@Autowired
	private ServiceCenterInfoRepository serviceCenterInfoRepository;

	@Autowired
	private ProductProviderApplicationRepository productProviderApplicationRepository;

	@Autowired
	private ProductProviderRepository productProviderRepository;

	@Autowired
	private AcctService acctService;

	@Autowired
	private BankAcctRepository bankAcctRepository;

	@Autowired
	private AgencyRepository agencyRepository;

	@Autowired
	private AuthorizationService authorizationService;

	@Autowired
	private DynamicOptionRepository dynamicOptionRepository;

	@Autowired
	protected ActionHistoryService actionHistoryUtil;

	public Member getMemberByUserId(String userId) {
		Member memberPo = memberRepository.findByUserId(userId);
		return memberPo;
	}

	public Agency getAgencyByUserId(String userId) {
		Agency agencyPo = agencyRepository.findByUserId(userId);
		return agencyPo;
	}

	public Agency getAgencyByNameAndUserIdNot(String userId, String name) {
		List<Agency> agencyPo = null;
		if (StringUtils.isNotBlank(userId)) {
			agencyPo = agencyRepository.findByNameAndUserIdNot(name, userId);
		} else {
			agencyPo = agencyRepository.findByName(name);
		}
		if (agencyPo.isEmpty()) {
			return null;
		}
		return agencyPo.get(0);
	}

	public MemberApplication getMemberWithLatestStatus(String userId) {
		Pageable pageable = new PageRequest(0, 1, Direction.DESC, "lastMntTs");
		Page<MemberApplication> memberAppPoList = memberApplRepository.findByUserId(userId, pageable);
		if (memberAppPoList.hasContent()) {
			return memberAppPoList.getContent().get(0);
		}
		return null;
	}

	public MemberApplication getLatestMemberAppl(String userId) {
		Pageable pageable = new PageRequest(0, 1, Direction.DESC, "lastMntTs");
		Page<MemberApplication> memberAppPoList = memberApplRepository.findByUserId(userId, pageable);
		if (memberAppPoList.hasContent()) {
			return memberAppPoList.getContent().get(0);
		}
		return null;
	}

	public MemberApplication getLatestMemberApplWithoutReject(String userId) {
		Pageable pageable = new PageRequest(0, 1, Direction.DESC, "lastMntTs");
		Page<MemberApplication> memberAppPoList = memberApplRepository.findByUserIdAndStatusNot(userId,
				EApplicationStatus.REJECT, pageable);
		if (memberAppPoList.hasContent()) {
			return memberAppPoList.getContent().get(0);
		}
		return null;
	}

	public InvestorApplication getInvestorWithLatestStatus(String userId) {
		Pageable pageable = new PageRequest(0, 1, Direction.DESC, "lastMntTs");
		Page<InvestorApplication> investorAppPoList = investorApplRepository.findByUserId(userId, pageable);
		if (investorAppPoList.hasContent()) {
			return investorAppPoList.getContent().get(0);
		}
		return null;
	}

	public InvestorApplication getLatestInvestorWithoutReject(String userId) {
		Pageable pageable = new PageRequest(0, 1, Direction.DESC, "lastMntTs");
		Page<InvestorApplication> investorAppPoList = investorApplRepository.findByUserIdAndStatusNot(userId,
				EApplicationStatus.REJECT, pageable);
		if (investorAppPoList.hasContent()) {
			return investorAppPoList.getContent().get(0);
		}
		return null;
	}

	public InvestorInfo getInvestorById(String userId) {
		return investorInfoRepository.findByUserId(userId);
	}

	public FinancierApplication getFinancierWithLatestStatus(String userId) {
		Pageable pageable = new PageRequest(0, 1, Direction.DESC, "lastMntTs");
		Page<FinancierApplication> financierAppPoList = financierApplRepository
				.findByUserId(userId, pageable);
		if (financierAppPoList.hasContent()) {
			return financierAppPoList.getContent().get(0);
		}
		return null;
	}

	public FinancierApplication getLatestFinancierWithoutReject(String userId) {
		Pageable pageable = new PageRequest(0, 1, Direction.DESC, "lastMntTs");
		Page<FinancierApplication> financierAppPoList = financierApplRepository.findByUserIdAndStatusNot(
				userId, EApplicationStatus.REJECT, pageable);
		if (financierAppPoList.hasContent()) {
			return financierAppPoList.getContent().get(0);
		}
		return null;
	}

	public FinancierInfo getFinancierById(String userId) {
		return financierInfoRepository.findByUserId(userId);
	}

	@Transactional(readOnly = true)
	public ProductProviderApplication getProductProviderWithLatestStatus(String userId) {
		LOGGER.info("getProductProviderInfoWithLatestStatus() invoked, {}", userId);
		Pageable pageable = new PageRequest(0, 1, Direction.DESC, "lastMntTs");
		Page<ProductProviderApplication> productProviderList = productProviderApplicationRepository
				.findByUserId(userId, pageable);
		if (productProviderList.hasContent()) {
			ProductProviderApplication productProviderApplication = productProviderList.getContent().get(0);
			LOGGER.info("getProductProviderInfoWithLatestStatus() completed");
			return productProviderApplication;
		}
		LOGGER.warn("No match userId: {} in UM_PROD_SERV_APPL", userId);
		return null;
	}

	@Transactional(readOnly = true)
	public ProductProviderApplication getLatestProductProviderNotReject(String userId) {
		LOGGER.info("getProductProviderInfoWithLatestStatus() invoked, {}", userId);
		Pageable pageable = new PageRequest(0, 1, Direction.DESC, "lastMntTs");
		Page<ProductProviderApplication> productProviderList = productProviderApplicationRepository
				.findByUserIdAndStatusNot(userId, EApplicationStatus.REJECT, pageable);
		if (productProviderList.hasContent()) {
			ProductProviderApplication productProviderApplication = productProviderList.getContent().get(0);
			LOGGER.info("getProductProviderInfoWithLatestStatus() completed");
			return productProviderApplication;
		}
		return null;
	}

	@Transactional(readOnly = true)
	public AgencyApplication getAgencyApplicationWithLatestStatus(String userId) {
		LOGGER.info("getAgencyApplicationWithLatestStatus() invoked, {}", userId);
		Pageable agencyPageable = new PageRequest(0, 1, Direction.DESC, "lastMntTs");
		Page<AgencyApplication> agencyPoList = agencyApplicationRepository.findByUserId(userId,
				agencyPageable);
		if (agencyPoList.hasContent()) {
			AgencyApplication agencyApplication = agencyPoList.getContent().get(0);
			LOGGER.info("getAgencyApplicationWithLatestStatus() completed");
			return agencyApplication;
		}
		LOGGER.warn("No match userId: {} in UM_ORG_APPL", userId);
		return null;
	}

	@Transactional(readOnly = true)
	public AgencyApplication getLatestAgencyApplicationNotReject(String userId) {
		LOGGER.info("getAgencyApplicationWithLatestStatus() invoked, {}", userId);
		Pageable agencyPageable = new PageRequest(0, 1, Direction.DESC, "lastMntTs");
		Page<AgencyApplication> agencyPoList = agencyApplicationRepository.findByUserIdAndStatusNot(userId,
				EApplicationStatus.REJECT, agencyPageable);
		if (agencyPoList.hasContent()) {
			AgencyApplication agencyApplication = agencyPoList.getContent().get(0);
			LOGGER.info("getAgencyApplicationWithLatestStatus() completed");
			return agencyApplication;
		}
		return null;
	}

	@Transactional(readOnly = true)
	public ServiceCenterApplication getServiceCenterWithLatestStatus(String userId) {
		LOGGER.info("getServiceCenterWithLatestStatus() invoked, {}", userId);
		Pageable pageable = new PageRequest(0, 1, Direction.DESC, "lastMntTs");
		Page<ServiceCenterApplication> serviceCenterList = serviceCenterApplicationRepository.findByUserId(
				userId, pageable);
		if (serviceCenterList.hasContent()) {
			ServiceCenterApplication serviceCenterPo = serviceCenterList.getContent().get(0);
			LOGGER.info("getServiceCenterWithLatestStatus() completed");
			return serviceCenterPo;
		}
		LOGGER.warn("No match userId: {} in UM_AUTHZD_CTR_APPL", userId);
		return null;
	}

	@Transactional(readOnly = true)
	public ServiceCenterApplication getLatsetServiceCenterNotReject(String userId) {
		LOGGER.info("getServiceCenterWithLatestStatus() invoked, {}", userId);
		Pageable pageable = new PageRequest(0, 1, Direction.DESC, "lastMntTs");
		Page<ServiceCenterApplication> serviceCenterList = serviceCenterApplicationRepository
				.findByUserIdAndStatusNot(userId, EApplicationStatus.REJECT, pageable);
		if (serviceCenterList.hasContent()) {
			ServiceCenterApplication serviceCenterPo = serviceCenterList.getContent().get(0);
			LOGGER.info("getServiceCenterWithLatestStatus() completed");
			return serviceCenterPo;
		}
		return null;
	}

	@Transactional(readOnly = true)
	public ProductProviderInfo getProductProviderInfoById(String userId) {
		return productProviderRepository.findByUserId(userId);
	}

	@Transactional(readOnly = true)
	public ServiceCenterInfo getServiceCenterInfoById(String userId) {
		return serviceCenterInfoRepository.findByUserId(userId);
	}

	@Transactional
	public void saveBasicInfo(MemberApplication member, User user, ActionType action,
			MemberApplication oldInfo) {
		// if new member, save user type in user table
		if (member.getUserType() != user.getType()) {
			user.setType(member.getUserType());
		}
		userService.updateUser(user);
		memberApplRepository.save(member);
		if (member.getStatus() != EApplicationStatus.UNCOMMITTED) {
			if (action == ActionType.EDIT) {
				actionHistoryUtil.saveForEdit(oldInfo, member, member.getUserId(), EntityType.USER);
			} else if (action == ActionType.MAINTAIN) {
				actionHistoryUtil.saveForMaintain(oldInfo, member, member.getUserId(), EntityType.USER);
			} else {
				actionHistoryUtil.save(EntityType.USER, member.getUserId(), action,
						ActionResult.BASIC_INFO_NEW);
			}
		}
	}

	@Transactional
	public void saveInvestorInfo(InvestorApplication applicationPo, ActionType action,
			InvestorApplication oldInfo) {
		applicationPo = investorApplRepository.save(applicationPo);
		if (applicationPo.getStatus() != EApplicationStatus.UNCOMMITTED) {
			if (action == ActionType.EDIT) {
				actionHistoryUtil.saveForEdit(oldInfo, applicationPo, applicationPo.getUserId(),
						EntityType.USER);
			} else if (action == ActionType.MAINTAIN) {
				actionHistoryUtil.saveForMaintain(oldInfo, applicationPo, applicationPo.getUserId(),
						EntityType.USER);
			} else {
				actionHistoryUtil.save(EntityType.USER, applicationPo.getUserId(), action,
						ActionResult.INVESTOR_NEW);
			}
		}
	}

	@Transactional
	public void saveFinancierInfo(FinancierApplication applicationPo, ActionType action,
			FinancierApplication oldInfo) {
		applicationPo = financierApplRepository.save(applicationPo);
		if (applicationPo.getStatus() != EApplicationStatus.UNCOMMITTED) {
			if (action == ActionType.EDIT) {
				actionHistoryUtil.saveForEdit(oldInfo, applicationPo, applicationPo.getUserId(),
						EntityType.USER);
			} else if (action == ActionType.MAINTAIN) {
				actionHistoryUtil.saveForMaintain(oldInfo, applicationPo, applicationPo.getUserId(),
						EntityType.USER);
			} else {
				actionHistoryUtil.save(EntityType.USER, applicationPo.getUserId(), action,
						ActionResult.FINANCIER_NEW);
			}
		}
	}

	/**
	 * insert agency and its children info.
	 * 
	 * @param user
	 * @param agency
	 * @param service
	 * @param provider
	 * @param currentUser
	 */
	@Transactional
	public void saveAgency(User user, AgencyApplication agency, ServiceCenterApplication service,
			ProductProviderApplication provider, String currentUser) {
		LOGGER.info("saveAgency() invoked");
		user.setLastMntOpid(currentUser);
		Date currentDate = new Date();
		user.setLastMntTs(currentDate);
		user.setCreateTs(currentDate);
		user.setCreator(currentUser);
		User createUser = userService.createUser(user, EBizRole.TOURIST_AGENCY);
		agency.setUserId(createUser.getUserId());
		agency.setLastMntOpid(currentUser);
		agency.setLastMntTs(currentDate);
		agency.setCreateTs(currentDate);
		agency.setCreator(currentUser);
		agency.setAppId(IdUtil.produce());
		agencyApplicationRepository.save(agency);
		if (service != null) {
			service.setUserId(createUser.getUserId());
			service.setCreateTs(currentDate);
			service.setCreator(currentUser);
			service.setLastMntOpid(currentUser);
			service.setLastMntTs(currentDate);
			/** child app id should be consistent with agency id. **/
			service.setAppId(agency.getAppId());
			/** version 20150317   新增 编辑 授权服务中心的显示名称 (提交申请时,就在appl表的fullName字段set进数据)  **/
			DynamicOption region = SystemDictUtil.getDictByCategoryAndCode(EOptionCategory.REGION, agency.getRegion());
	        DynamicOption parent = SystemDictUtil.getParentDict(region);
			String fullName = parent.getLongText() + region.getLongText() + agency.getName();
			service.setServiceCenterDesc(fullName);
			serviceCenterApplicationRepository.save(service);
			if (EApplicationStatus.PENDDING == service.getStatus()) {
				actionHistoryUtil
						.save(constructActionHistory(EntityType.USER, createUser.getUserId(), currentDate,
								ActionType.NEW, ActionResult.SERVICE_CENTER_NEW, createUser.getUserId()));
			}
		}

		if (provider != null) {
			provider.setUserId(createUser.getUserId());
			provider.setCreateTs(currentDate);
			provider.setCreator(currentUser);
			provider.setLastMntOpid(currentUser);
			provider.setLastMntTs(currentDate);
			/** child app id should be consistent with agency id. **/
			provider.setAppId(agency.getAppId());
			this.setWrtrCreditFileIdIfIsNull(provider);
			productProviderApplicationRepository.save(provider);
			if (EApplicationStatus.PENDDING == provider.getStatus()) {
				actionHistoryUtil
						.save(constructActionHistory(EntityType.USER, createUser.getUserId(), currentDate,
								ActionType.NEW, ActionResult.PROVIDER_CENTER_NEW, createUser.getUserId()));
			}
		}
		LOGGER.debug("saveAgency() completed");
	}

	/**
	 * save Service Center info.
	 * 
	 * @param serviceCentor
	 */
	@Transactional
	public void updateAgency(AgencyRegisterInfoDto agency, String currentUser, EApplicationStatus status) {
		LOGGER.info("updateAgency() invoked");
		Date currentDate = new Date();
		User user = userService.getUserByUserId(currentUser);
		user.setType(EUserType.ORGANIZATION);
		user.setLastMntOpid(currentUser);
		user.setLastMntTs(currentDate);
		user.setCompanyId(agency.getCompanyId());
		userService.updateUser(user);
		if (EMemberType.AUTHZDCENTER == agency.getType()) {
			this.saveServiceCenter(agency.getCommon(), agency.getServiceCenter(), currentUser, status);
		} else if (EMemberType.PRODUCTSERVICE == agency.getType()) {
			this.saveProductProvider(agency.getCommon(), agency.getProductProvider(), currentUser, status);
		}
	}

	private void saveServiceCenter(AgencyApplicationCommonDto common, ServiceCenterDto serviceCenter,
			String currentUser, EApplicationStatus status) {
		Date currentDate = new Date();
		AgencyApplication agencyPo = ConverterService.convert(common, AgencyApplication.class);
		agencyPo.setLastMntOpid(currentUser);
		agencyPo.setLastMntTs(currentDate);
		agencyPo.setUserId(currentUser);
		agencyPo.setStatus(status);
		ServiceCenterApplication serviceCenterPo = ConverterService.convert(serviceCenter,
				ServiceCenterApplication.class);
		serviceCenterPo.setLastMntOpid(currentUser);
		serviceCenterPo.setLastMntTs(currentDate);
		serviceCenterPo.setUserId(currentUser);
		serviceCenterPo.setStatus(status);
		ServiceCenterApplication serviceCenterApplication = getLatsetServiceCenterNotReject(currentUser);
		if (serviceCenterApplication != null) {
			serviceCenterPo.setLevel(serviceCenterApplication.getLevel());
		}

		AgencyApplication oldAgency = this.getLatestAgencyApplicationNotReject(currentUser);

		if (serviceCenterApplication != null
				&& EApplicationStatus.UNCOMMITTED == serviceCenterApplication.getStatus()) {
			serviceCenterPo.setAppId(serviceCenterApplication.getAppId());
			agencyPo.setAppId(serviceCenterApplication.getAppId());
			agencyApplicationRepository.save(agencyPo);
			serviceCenterApplicationRepository.save(serviceCenterPo);
			LOGGER.warn("Status is {}, allow update.", serviceCenterApplication.getStatus());
		} else {
			/** ServiceCenterPo & agencyPo have same appId for one request. **/
			agencyPo.setCreator(currentUser);
			agencyPo.setCreateTs(currentDate);
			serviceCenterPo.setCreator(currentUser);
			serviceCenterPo.setCreateTs(currentDate);
			agencyPo.setAppId(IdUtil.produce());
			serviceCenterPo.setAppId(agencyPo.getAppId());
			agencyApplicationRepository.save(agencyPo);
			serviceCenterApplicationRepository.save(serviceCenterPo);
		}
		if (EApplicationStatus.PENDDING == serviceCenterPo.getStatus()) {
			if (serviceCenterApplication != null) {
				Collection<ActionHistoryPo> historyList = actionHistoryUtil.saveForEdit(oldAgency, agencyPo,
						currentUser, EntityType.USER);
				if (!historyList.isEmpty()) {
					actionHistoryUtil.saveForEditWithEventId(serviceCenterApplication, serviceCenterPo,
							currentUser, EntityType.USER, historyList.iterator().next().getEventId());
				} else {
					actionHistoryUtil.saveForEdit(serviceCenterApplication, serviceCenterPo, currentUser,
							EntityType.USER);
				}
			} else {
				actionHistoryUtil.save(EntityType.USER, currentUser, ActionType.NEW,
						ActionResult.SERVICE_CENTER_NEW);
			}
		}
		LOGGER.debug("saveServiceCenter() completed");
	}

	/**
	 * save Product Provider info.
	 * 
	 * @param productProvider
	 */
	private void saveProductProvider(AgencyApplicationCommonDto common, ProductProviderDto productProvider,
			String currentUser, EApplicationStatus status) {
		LOGGER.info("saveProductProvider() invoked");
		Date currentDate = new Date();
		AgencyApplication agencyPo = ConverterService.convert(common, AgencyApplication.class);
		agencyPo.setLastMntOpid(currentUser);
		agencyPo.setLastMntTs(currentDate);
		agencyPo.setUserId(currentUser);
		agencyPo.setStatus(status);
		ProductProviderApplication productProviderPo = ConverterService.convert(productProvider,
				ProductProviderApplication.class);
		productProviderPo.setLastMntOpid(currentUser);
		productProviderPo.setLastMntTs(currentDate);
		productProviderPo.setUserId(currentUser);
		productProviderPo.setStatus(status);
		ProductProviderApplication productProviderApplication = getLatestProductProviderNotReject(currentUser);
		if (productProviderApplication != null) {
			productProviderPo.setProSeverLevel(productProviderApplication.getProSeverLevel());
		}
		AgencyApplication oldAgency = this.getLatestAgencyApplicationNotReject(currentUser);
		// set wrtrCreditFile value
		this.setWrtrCreditFileIdIfIsNull(productProviderPo);
		if (productProviderApplication != null
				&& EApplicationStatus.UNCOMMITTED == productProviderApplication.getStatus()) {
			productProviderPo.setAppId(productProviderApplication.getAppId());
			agencyPo.setAppId(productProviderApplication.getAppId());
			agencyApplicationRepository.save(agencyPo);
			productProviderApplicationRepository.save(productProviderPo);
			LOGGER.warn("Status is {}, allow update.", productProviderApplication.getStatus());
		} else {
			/** productProviderPo & agencyPo have same appId for one request. **/
			productProviderPo.setCreateTs(currentDate);
			productProviderPo.setCreator(currentUser);
			agencyPo.setCreateTs(currentDate);
			agencyPo.setCreator(currentUser);
			agencyPo.setAppId(IdUtil.produce());
			productProviderPo.setAppId(agencyPo.getAppId());
			agencyApplicationRepository.save(agencyPo);
			productProviderApplicationRepository.save(productProviderPo);
		}
		if (EApplicationStatus.PENDDING == productProviderPo.getStatus()) {
			if (productProviderApplication != null) {

				Collection<ActionHistoryPo> historyList = actionHistoryUtil.saveForEdit(oldAgency, agencyPo,
						currentUser, EntityType.USER);
				if (!historyList.isEmpty()) {
					actionHistoryUtil.saveForEditWithEventId(productProviderApplication, productProviderPo,
							currentUser, EntityType.USER, historyList.iterator().next().getEventId());
				} else {
					actionHistoryUtil.saveForEdit(productProviderApplication, productProviderPo, currentUser,
							EntityType.USER);
				}
			} else {
				actionHistoryUtil.save(EntityType.USER, currentUser, ActionType.NEW,
						ActionResult.PROVIDER_CENTER_NEW);
			}
		}
		LOGGER.debug("saveProductProvider() completed");
	}

	private void setWrtrCreditFileIdIfIsNull(ProductProviderApplication productProvider) {
		if (productProvider != null) {
			if (StringUtils.isBlank(productProvider.getWrtrCreditFile())
					&& StringUtils.isNotBlank(productProvider.getUserId())) {
				ProductProviderInfo prodServInfo = this.getProductProviderInfoById(productProvider
						.getUserId());
				if (prodServInfo != null && StringUtils.isNotBlank(prodServInfo.getWrtrCreditFile())) {
					productProvider.setWrtrCreditFile(prodServInfo.getWrtrCreditFile());
				}
			}
		}
	}

	@Transactional
	public MemberApplication acceptBasicInfo(MemberApplication applicationPo) {
		applicationPo.setStatus(EApplicationStatus.ACCEPT);
		applicationPo = memberApplRepository.save(applicationPo);
		Member memberPo = ConverterService.convert(applicationPo, Member.class);
		memberRepository.save(memberPo);
		// need status field, so convert from application
		return applicationPo;
	}

	@Transactional(readOnly = true)
	public MemberApplication getMemberByIdCardNum(String userId, String userType, String idCardNum) {
		Page<MemberApplication> applicationPoList = null;
		Pageable pageable = new PageRequest(0, 1);
		if (StringUtils.isNotBlank(userId)) {
			applicationPoList = memberApplRepository
					.findByPersonIdCardNumberAndUserIdNotAndStatusNotAndStatusNot(idCardNum, userId,
							EApplicationStatus.UNCOMMITTED, EApplicationStatus.REJECT, pageable);
		} else {
			applicationPoList = memberApplRepository.findByPersonIdCardNumberAndStatusNotAndStatusNot(
					idCardNum, EApplicationStatus.UNCOMMITTED, EApplicationStatus.REJECT, pageable);
		}
		if (applicationPoList.hasContent()) {
			return applicationPoList.getContent().get(0);
		}
		return null;
	}

	@Transactional(readOnly = true)
	public MemberApplication getMemberByBankAccout(String userId, String bankAccout) {
		Page<MemberApplication> applicationPoList = null;
		Pageable pageable = new PageRequest(0, 1);
		if (StringUtils.isNotBlank(userId)) {
			applicationPoList = memberApplRepository.findByBankAccountAndUserIdNotAndStatusNotAndStatusNot(
					bankAccout, userId, EApplicationStatus.UNCOMMITTED, EApplicationStatus.REJECT, pageable);
		} else {
			applicationPoList = memberApplRepository.findByBankAccountAndStatusNotAndStatusNot(bankAccout,
					EApplicationStatus.UNCOMMITTED, EApplicationStatus.REJECT, pageable);
		}
		if (applicationPoList.hasContent()) {
			return applicationPoList.getContent().get(0);
		}
		return null;
	}

	// FIXME Not allowed DTO returned from service
	@Transactional(readOnly = true)
	public AgencyApplicationSearchDto getMemberAgencyDetails(String userId) {

		AgencyApplicationSearchDto agency = new AgencyApplicationSearchDto();
		Acct account = null;
		Pageable pageable = new PageRequest(0, 1, Direction.DESC, "lastMntTs");
		/** filter UNCOMMITTED status. **/
		Page<AgencyApplication> basicInfoList = agencyApplicationRepository.findByUserIdAndStatusNot(userId,
				EApplicationStatus.UNCOMMITTED, pageable);
		if (basicInfoList.hasContent()) {
			AgencyApplication agencyAppPo = basicInfoList.getContent().get(0);
			// 拒绝状态下，有原信息取原信息
			if (agencyAppPo.getStatus() == EApplicationStatus.REJECT) {
				Agency oldAgency = agencyRepository.findByUserId(userId);
				if (oldAgency != null) {
					AgencyApplicationDto dto = ConverterService
							.convert(oldAgency, AgencyApplicationDto.class);
					List<BankAcctPo> bankList = bankAcctRepository.findBankAcctByUserId(dto.getUserId());
					if (CollectionUtils.isNotEmpty(bankList)) {
						BankAcctPo bank = bankList.get(0);
						dto.setBankAccount(bank.getBnkAcctNo());
						dto.setBankAccountName(bank.getBnkAcctName());
						dto.setBankShortName(SystemDictUtil.getDictByCategoryAndCode(EOptionCategory.BANK,
								bank.getBnkCd()));
						dto.setBankFullName(bank.getBnkName());
						dto.setBankCardFrontImg(bank.getBnkCardImg());
						DynamicOption option = SystemDictUtil.getDictByCategoryAndCode(
								EOptionCategory.REGION, bank.getBnkOpenProv());
						dto.setBankOpenProvince(option);
						DynamicOption option2 = SystemDictUtil.getDictByCategoryAndCode(
								EOptionCategory.REGION, bank.getBnkOpenCity());
						dto.setBankOpenCity(option2);
						dto.setBankOpenBranch(bank.getBnkBrch());
						agency.setAgency(dto);
					}
				} else {
					agency.setAgency(ConverterService.convert(agencyAppPo, AgencyApplicationDto.class));
				}
			} else {
				agency.setAgency(ConverterService.convert(agencyAppPo, AgencyApplicationDto.class));
			}
		}
		Page<ServiceCenterApplication> serviceCenterList = serviceCenterApplicationRepository
				.findByUserIdAndStatusNot(userId, EApplicationStatus.UNCOMMITTED, pageable);
		if (serviceCenterList.hasContent()) {
			ServiceCenterApplication serAppPo = serviceCenterList.getContent().get(0);
			// 拒绝状态下，有原信息取原信息
			if (serAppPo.getStatus() == EApplicationStatus.REJECT) {
				ServiceCenterInfo oldService = serviceCenterInfoRepository.findByUserId(userId);
				if (oldService != null) {
					agency.setServiceCenter(ConverterService.convert(oldService,
							ServiceCenterApplicationDto.class));
				} else {
					agency.setServiceCenter(ConverterService.convert(serAppPo,
							ServiceCenterApplicationDto.class));
				}
			} else {
				agency.setServiceCenter(ConverterService.convert(serAppPo, ServiceCenterApplicationDto.class));
			}
		}
		Page<ProductProviderApplication> productProviderList = productProviderApplicationRepository
				.findByUserIdAndStatusNot(userId, EApplicationStatus.UNCOMMITTED, pageable);
		if (productProviderList.hasContent()) {
			ProductProviderApplication proAppPo = productProviderList.getContent().get(0);
			// 拒绝状态下，有原信息取原信息
			if (proAppPo.getStatus() == EApplicationStatus.REJECT) {
				ProductProviderInfo oldPro = productProviderRepository.findByUserId(userId);
				if (oldPro != null) {
					agency.setProductProvider(ConverterService.convert(oldPro,
							ProductProviderApplicationSearchDto.class));
				} else {
					agency.setProductProvider(ConverterService.convert(proAppPo,
							ProductProviderApplicationSearchDto.class));
				}
			} else {
				agency.setProductProvider(ConverterService.convert(proAppPo,
						ProductProviderApplicationSearchDto.class));
			}
		}
		AcctPo acctPo = acctService.getAcctByUserId(userId);
		if (acctPo != null) {
			account = ConverterService.convert(acctPo, Acct.class);
		}
		agency.setAccount(account);
		return agency;
	}

	@Deprecated
	public List<AgencyDto> queryAgencyList() {
		List<AgencyDto> dtoList = new ArrayList<AgencyDto>();
		List<Agency> list = agencyRepository.findAll();
		for (Agency po : list) {
			dtoList.add(ConverterService.convert(po, AgencyDto.class));
		}
		return dtoList;
	}

	public List<DynamicOptionPo> getDynOptions(String category, String enabled) {
		return dynamicOptionRepository.findByCategoryAndEnabled(category, enabled);
	}

	private ActionHistoryPo constructActionHistory(EntityType entityType, String entityId, Date time,
			ActionType action, ActionResult result, String operatorId) {
		ActionHistoryPo actionHistory = new ActionHistoryPo();
		actionHistory.setAction(action);
		actionHistory.setResult(result == null ? null : result.getText());
		actionHistory.setOperateTime(time);
		actionHistory.setOperateUser(operatorId);
		actionHistory.setEntityId(entityId);
		actionHistory.setEntityType(entityType);
		return actionHistory;
	}

	/**
	 * Unique org code check for Product Provider & Service Center check.
	 * 
	 * @param currentUserId
	 * @param groupName
	 * @return true if exist.
	 */
	@Transactional(readOnly = true)
	public boolean isExistingOrgCodeForOrganization(String userId, String orgCode) {
		LOGGER.info("isExistingOrgCode() invoked, userId {} code {}", userId, orgCode);
		if (userId == null || userId.isEmpty()) {
			int x = this.agencyApplicationRepository.countByOrgNumberIgnoreCaseAndStatusOrStatus(orgCode
					.trim().toLowerCase(), EApplicationStatus.ACCEPT, EApplicationStatus.PENDDING);
			return x == 0 ? false : true;
		} else {
			int x = this.agencyApplicationRepository.countByUserIdNotAndOrgNumberIgnoreCaseAndStatusOrStatus(
					userId, orgCode.trim().toLowerCase(), EApplicationStatus.ACCEPT,
					EApplicationStatus.PENDDING);
			return x == 0 ? false : true;
		}
	}

	/**
	 * Unique org code check for Financier.
	 * 
	 * @param currentUserId
	 * @param groupName
	 * @return true if exist.
	 */
	@Transactional(readOnly = true)
	public boolean isExistingOrgCodeForIndividual(String userId, String orgCode) {
		LOGGER.info("isExistingOrgCode() invoked, userId {} code {}", userId, orgCode);
		if (userId == null || userId.isEmpty()) {
			int x = this.financierApplRepository.countByOrgNumberIgnoreCaseAndStatusOrStatus(orgCode.trim()
					.toLowerCase(), EApplicationStatus.ACCEPT, EApplicationStatus.PENDDING);
			return x == 0 ? false : true;
		} else {
			int x = this.financierApplRepository.countByUserIdNotAndOrgNumberIgnoreCaseAndStatusOrStatus(
					userId, orgCode.trim().toLowerCase(), EApplicationStatus.ACCEPT,
					EApplicationStatus.PENDDING);
			return x == 0 ? false : true;
		}
	}
}
