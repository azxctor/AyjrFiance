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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.domain.DynamicOption;
import com.hengxin.platform.common.enums.ActionResult;
import com.hengxin.platform.common.enums.ActionType;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.enums.EOptionCategory;
import com.hengxin.platform.common.enums.EntityType;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.service.PagingService;
import com.hengxin.platform.common.util.SystemDictUtil;
import com.hengxin.platform.escrow.service.EswAccountService;
import com.hengxin.platform.fund.dto.biz.req.FundAcctCreateReq;
import com.hengxin.platform.fund.service.BankAcctService;
import com.hengxin.platform.fund.service.FundAcctManageService;
import com.hengxin.platform.member.domain.Agency;
import com.hengxin.platform.member.domain.AgencyApplication;
import com.hengxin.platform.member.domain.BaseApplication;
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
import com.hengxin.platform.member.dto.AuditDto;
import com.hengxin.platform.member.enums.EApplicationStatus;
import com.hengxin.platform.member.enums.EMemberType;
import com.hengxin.platform.member.enums.EMessageType;
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
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.domain.User;
import com.hengxin.platform.security.enums.EBizRole;
import com.hengxin.platform.security.service.AuthorizationService;
import com.hengxin.platform.security.service.UserService;

/**
 * Class Name: AuditService.
 * 
 * @author Ryan
 * 
 */
@Service
public class AuditService extends PagingService<BaseApplication> {

    private static final String DEFAULT_INV_FIN_LEVEL = "01";
    private static final String COMMENTS = "基本信息审核不通过.";
    private static final String TYPE = "t";
    private static final String BANK_ACCOUNT = "ba";
    private static final String MOBILE_PERSON = "mp";
    private static final String MOBILE_ORG_CONTACT = "moc";
    private static final String MOBILE_ORG_LEGAL = "mol";

    @Autowired
    MemberApplicationRepository memberApplicationRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    FinancierApplicationRepository financierApplicationRepository;

    @Autowired
    InvestorApplicationRepository investorApplicationRepository;

    @Autowired
    InvestorInfoRepository investorInfoRepository;

    @Autowired
    FinancierInfoRepository financierInfoRepository;

    @Autowired
    private UserService userService;

    @Autowired
    FundAcctManageService fundAcctManageService;

    @Autowired
    ServiceCenterApplicationRepository serviceCenterApplicationRepository;

    @Autowired
    ServiceCenterInfoRepository serviceCenterInfoRepository;

    @Autowired
    AgencyApplicationRepository agencyApplicationRepository;

    @Autowired
    AgencyRepository agencyRepository;

    @Autowired
    ProductProviderApplicationRepository productProviderApplicationRepository;

    @Autowired
    ProductProviderRepository productProviderRepository;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private SecurityContext securityContext;

    @Autowired
    private BankAcctService bankService;

    @Autowired
    private MemberMessageService messageService;
    
    @Autowired
    private EswAccountService eswAccountService;

    /**
     * Commit bank info
     * 
     * @param userId
     * @param mp
     */
	private void processMemberBankAccount(String userId, MemberApplication mp, String currentUser, Map<String, String> backupInfo) {
		/*
		 * 安益金融在托管账户端维护银行卡信息，此处注释掉
        List<BankAcct> bankAccountList = bankService.findBankAcctByUserIdWihoutCheck(userId);
        if (!bankAccountList.isEmpty()) {
        	backupInfo.put(BANK_ACCOUNT, bankAccountList.get(0).getBnkAcctNo());
            return;
        }
        Date currentDate = new Date();
        BankAcct bankAcct = new BankAcct();
        bankAcct.setUserId(userId);
        bankAcct.setBnkAcctNo(mp.getBankAccount());
        bankAcct.setBnkCd(mp.getBankShortName());
        bankAcct.setBnkName(mp.getBankFullName());
        bankAcct.setBnkCardImg(mp.getBankCardFrontImg());
        bankAcct.setBnkAcctName(mp.getBankAccountName());
        bankAcct.setSignedFlg(EFlagType.NO.getCode());
        bankAcct.setBnkOpenProv(mp.getBankOpenProvince());
        bankAcct.setBnkOpenCity(mp.getBankOpenCity());
        bankAcct.setBnkBrch(mp.getBankOpenBranch());
        bankAcct.setCreateTs(currentDate);
        bankAcct.setLastMntTs(currentDate);
        bankAcct.setCreateOpid(currentUser);
        bankAcct.setLastMntOpid(currentUser);
        bankAcct.setOldBnkAcctNo(mp.getBankAccount());
        bankService.createBankAcct(bankAcct);
        */
    }

    /**
     * Refresh user info
     * 
     * @param userId
     * @param currentUserId
     * @param mp
     * @return user real name.
     */
    private User processMemberUser(String userId, String currentUserId, MemberApplication mp) {
        User user = userService.getUserByUserId(userId);
        if (EUserType.ORGANIZATION == mp.getUserType()) {
            user.setEmail(mp.getOrgEmail());
            /** Copy contact mobile to user. **/
            user.setMobile(mp.getContactMobile());
            user.setType(EUserType.ORGANIZATION);
            user.setName(mp.getOrgName());
            user.setRegion(mp.getOrgRegion());
        } else if (EUserType.PERSON == mp.getUserType()) {
            user.setEmail(mp.getPersonEmail());
            user.setMobile(mp.getPersonMobile());
            user.setType(EUserType.PERSON);
            user.setName(mp.getPersonName());
            user.setRegion(mp.getPersonRegion());
        }
        user.setLastMntOpid(currentUserId);
        user.setLastMntTs(new Date());
        user = userService.updateUser(user);
        return user;
    }

    /**
     * Description: 同步融资人和投资人的经办人和服务中心
     * 
     * @param userId
     */
    private void mergeAgentAndAuthCenterIdFromInvestor(String userId) {
        Pageable pageable = new PageRequest(0, 1, Direction.DESC, "lastMntTs");
        InvestorInfo investorInfo = investorInfoRepository.findByUserId(userId);
        if (investorInfo != null) {
            String agent = investorInfo.getAgent();
            String authCenterId = investorInfo.getAuthCenterId();

            Page<FinancierApplication> financierApplicationList = financierApplicationRepository.findByUserId(userId,
                    pageable);
            if (financierApplicationList.hasContent()) {
                FinancierApplication financierApplication = financierApplicationList.getContent().get(0);
                financierApplication.setAgent(agent);
                financierApplication.setAuthCenterId(authCenterId);
                financierApplicationRepository.save(financierApplication);
            }

            FinancierInfo financierInfo = financierInfoRepository.findByUserId(userId);
            if (financierInfo != null) {
                financierInfo.setAgent(agent);
                financierInfo.setAuthCenterId(authCenterId);
                financierInfoRepository.save(financierInfo);
            }
        }
    }

    /**
     * Description: 同步融资人和投资人的经办人和服务中心
     * 
     * @param userId
     * @param agent
     * @param authCenterId
     */
    private void mergeAgentAndAuthCenterIdToFinancier(String userId, String agent, String authCenterId) {
        Pageable pageable = new PageRequest(0, 1, Direction.DESC, "lastMntTs");

        Page<FinancierApplication> financierApplicationList = financierApplicationRepository.findByUserId(userId,
                pageable);
        if (financierApplicationList.hasContent()) {
            FinancierApplication financierApplication = financierApplicationList.getContent().get(0);
            financierApplication.setAgent(agent);
            financierApplication.setAuthCenterId(authCenterId);
            financierApplicationRepository.save(financierApplication);
        }

        FinancierInfo financierInfo = financierInfoRepository.findByUserId(userId);
        if (financierInfo != null) {
            financierInfo.setAgent(agent);
            financierInfo.setAuthCenterId(authCenterId);
            financierInfoRepository.save(financierInfo);
        }
    }

    /**
     * Audit basic info, investment info and financing info.<br/>
     * Add SMS support.
     * 
     * @param audit
     * @param currentUser
     * @throws ServiceException
     */
    @Transactional
    public void auditMembers(AuditDto audit, String currentUser) {
        validate(audit);
        EMemberType memberType = audit.getMemberType();
        String memberId = audit.getUserId();
        Pageable pageable = new PageRequest(0, 1, Direction.DESC, "createTs");
        Date currentDate = new Date();
        EApplicationStatus status = audit.isPassed() ? EApplicationStatus.ACCEPT : EApplicationStatus.REJECT;
        MemberApplication mp = null;
        EApplicationStatus basicInfoStatus = null;
        Page<MemberApplication> mpList = memberApplicationRepository.findByUserId(memberId, pageable);
        
        /** used Only when approved. **/
        Map<String, String> backupInfo = new HashMap<String, String>();
        Map<String, String> currentInfo = new HashMap<String, String>();
        if (mpList.hasContent()) {
            mp = mpList.getContent().get(0);
            currentInfo.put(TYPE, mp.getUserType().getCode());
            currentInfo.put(BANK_ACCOUNT, mp.getBankAccount());
            if (EUserType.PERSON == mp.getUserType()) {
            	currentInfo.put(MOBILE_PERSON, mp.getPersonMobile());	
			} else if (EUserType.ORGANIZATION == mp.getUserType()) {
				currentInfo.put(MOBILE_ORG_LEGAL, mp.getOrgMobile());
	            currentInfo.put(MOBILE_ORG_CONTACT, mp.getContactMobile());	
			}
            basicInfoStatus = mp.getStatus();
        }
        boolean firstTime = false;
        String name = "";
        String userName = "";
        // Approve basic info
        if (mp != null && memberType == EMemberType.BASIC) {

            checkCurrentStatus(basicInfoStatus);

            mp.setComments(audit.getComments());
            mp.setStatus(status);
            mp.setLastMntTs(currentDate);
            mp.setLastMntOpid(currentUser);

            memberApplicationRepository.save(mp);

            if (!audit.isPassed()) {
            	/** check whether it is first time to save user info. **/
                Member originalMember = memberRepository.findByUserId(memberId);
                if (originalMember == null) {
					firstTime = true;
				}
                User user = userService.getUserByUserId(memberId);
            	name = user.getName();
            	if (name == null || name.isEmpty()) {
					name = mp.getPersonName() == null ? mp.getOrgName() : mp.getPersonName();
				}
            	userName = user.getUsername();
                Page<InvestorApplication> investorList = investorApplicationRepository.findByUserId(memberId, pageable);
                InvestorInfo investorInfo = investorInfoRepository.findOne(memberId);
                // 如果基本信息拒绝且投资权限没有授予过
                if (investorList.hasContent() && investorInfo == null) {
                    InvestorApplication investorApplication = investorList.getContent().get(0);
                    investorApplication.setStatus(EApplicationStatus.REJECT);
                    investorApplication.setComments(COMMENTS);
                    investorApplication.setLastMntTs(currentDate);
                    investorApplication.setLastMntOpid(currentUser);
                    investorApplicationRepository.save(investorApplication);
                }
                Page<FinancierApplication> financierList = financierApplicationRepository.findByUserId(memberId,
                        pageable);
                FinancierInfo financierInfo = financierInfoRepository.findOne(memberId);
                // 如果基本信息拒绝且融资权限没有授予过
                if (financierList.hasContent() && financierInfo == null) {
                    FinancierApplication financier = financierList.getContent().get(0);
                    financier.setStatus(EApplicationStatus.REJECT);
                    financier.setComments(COMMENTS);
                    financier.setLastMntTs(currentDate);
                    financier.setLastMntOpid(currentUser);
                    financierApplicationRepository.save(financier);
                }
            }

            if (audit.isPassed()) {
                /** check whether it is first time to save user info. **/
                Member originalMember = memberRepository.findByUserId(memberId);
                if (originalMember == null) {
					firstTime = true;
				} else {
		            backupInfo.put(MOBILE_PERSON, originalMember.getPersonMobile());
		            backupInfo.put(MOBILE_ORG_LEGAL, originalMember.getOrgMobile());
		            backupInfo.put(MOBILE_ORG_CONTACT, originalMember.getContactMobile());
				}
                Member memberPo = ConverterService.convert(mp, Member.class);
                memberRepository.save(memberPo);
                User user = processMemberUser(memberId, currentUser, mp);
                name = user.getName();
            	userName = user.getUsername();
                processMemberBankAccount(memberId, mp, currentUser, backupInfo);
                actionHistoryUtil.save(EntityType.USER, memberId, ActionType.AUDIT, ActionResult.BASIC_INFO_AUDIT_PASS,
                        audit.getComments());

            } else {
                actionHistoryUtil.save(EntityType.USER, memberId, ActionType.AUDIT,
                        ActionResult.BASIC_INFO_AUDIT_REJECT, audit.getComments());
            }
        }
        // Approve investor info
        if (memberType == EMemberType.INVESTOR) {
            Page<InvestorApplication> investorList = investorApplicationRepository.findByUserId(memberId, pageable);
            if (investorList.hasContent()) {
                InvestorApplication investor = investorList.getContent().get(0);

                investor.setComments(audit.getComments());
                investor.setStatus(status);
                investor.setLastMntTs(currentDate);
                investor.setLastMntOpid(currentUser);
                if (audit.isPassed()) {
                    investor.setInvestorLevel(String.valueOf(audit.getLevel()));
                }
                investorApplicationRepository.save(investor);

                if (audit.isPassed()) {
                    checkChangeInvestAndFinLevelPermission(audit.getLevel());
                    //FIXME Set default value for first time approval, it should be removed when starting risk mechanism.
                    InvestorInfo investorInfo = ConverterService.convert(investor, InvestorInfo.class);
                    InvestorInfo originalInvestorInfo = this.investorInfoRepository.findByUserId(memberId);
                    if (originalInvestorInfo == null) {
                    	investorInfo.setInvestorLevelOriginal("01");
					} else {
						investorInfo.setInvestorLevelOriginal(originalInvestorInfo.getInvestorLevelOriginal());
					}
                    investorInfoRepository.save(investorInfo);
                    processAccountNo(memberId, EBizRole.INVESTER, currentUser);
                    mergeAgentAndAuthCenterIdToFinancier(memberId, investor.getAgent(), investor.getAuthCenterId());
                    authorizationService.assignRole(EBizRole.INVESTER, investorInfo.getUserId());
                    authorizationService.revokeRole(EBizRole.TOURIST, investorInfo.getUserId());
                    // log
                    actionHistoryUtil.save(EntityType.USER, memberId, ActionType.AUDIT,
                            ActionResult.INVESTOR_AUDIT_PASS, audit.getComments());
                } else {
                    actionHistoryUtil.save(EntityType.USER, memberId, ActionType.AUDIT,
                            ActionResult.INVESTOR_AUDIT_REJECT, audit.getComments());
                }
            }
        }
        if (memberType == EMemberType.FINANCER) {

            Page<FinancierApplication> financierList = financierApplicationRepository.findByUserId(memberId, pageable);
            if (financierList.hasContent()) {
                FinancierApplication financier = financierList.getContent().get(0);

                financier.setComments(audit.getComments());
                financier.setStatus(status);
                financier.setLastMntTs(currentDate);
                financier.setLastMntOpid(currentUser);
                if (audit.isPassed()) {
                    financier.setFinancierLevel(String.valueOf(audit.getLevel()));
                }
                financierApplicationRepository.save(financier);

                // Push data
                if (audit.isPassed()) {
                    checkChangeInvestAndFinLevelPermission(audit.getLevel());
                    FinancierInfo financierInfo = ConverterService.convert(financier, FinancierInfo.class);
                    financierInfoRepository.save(financierInfo);
                    processAccountNo(memberId, EBizRole.FINANCIER, currentUser);
                    mergeAgentAndAuthCenterIdFromInvestor(memberId);
                    authorizationService.assignRole(EBizRole.FINANCIER, financierInfo.getUserId());
                    authorizationService.revokeRole(EBizRole.TOURIST, financierInfo.getUserId());
                    actionHistoryUtil.save(EntityType.USER, memberId, ActionType.AUDIT,
                            ActionResult.FINANCIER_AUDIT_PASS, audit.getComments());
                } else {
                    actionHistoryUtil.save(EntityType.USER, memberId, ActionType.AUDIT,
                            ActionResult.FINANCIER_AUDIT_REJECT, audit.getComments());
                }
            }
        }
        /** Send message to user when he become a member first time. **/
        if (firstTime) {
        	if (audit.isPassed()) {
        		String messageKey = "member.formal.message";
            	messageService.sendMessage(EMessageType.SMS, messageKey, memberId, name, userName);	
			} else {
				String messageKey = "member.formal.fail.message";
	        	messageService.sendMessage(EMessageType.SMS, messageKey, memberId, name);
			}
		}
        /** When bank account or mobile is updated. **/
        if (audit.isPassed() && EMemberType.BASIC == memberType && mpList.hasContent()) {
        	this.processInfoUpdateMessage(memberId, currentInfo, backupInfo, name);
		}
        processTodo(audit, memberType, audit.getUserId(), name);
    }

    /**
     * Check permission for changing investor or financier level, default level = 1
     * 
     * @param level
     */
    private void checkChangeInvestAndFinLevelPermission(String level) {
        if (!DEFAULT_INV_FIN_LEVEL.equals(level)) {
            securityContext.checkPermission(Permissions.MEMBER_ADV_MAINT_INV_FIN_LEVEL_CHANGE);
        }
    }

    /**
     * Handle errors
     * 
     * @param status
     */
    private void checkCurrentStatus(EApplicationStatus status) {
        if (status != EApplicationStatus.PENDDING) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "审核失败");
        }
    }

    /**
     * Description: 创建用户交易账号
     * 
     * @param userId
     *            String
     * @param currentUser
     *            String
     */
    private void processAccountNo(String userId, EBizRole assignRole, String currentUser) {
        boolean existsAcct = fundAcctManageService.existsAcct(userId);
        if (!existsAcct) {
            FundAcctCreateReq fundAcctCreateReq = new FundAcctCreateReq();
            fundAcctCreateReq.setUserId(userId);
            fundAcctCreateReq.setCurrOpId(currentUser);
            fundAcctCreateReq.setAssignRole(assignRole);
            fundAcctManageService.createUserAcct(fundAcctCreateReq);
        }
    }

    /**
     * Description:审核服务中心申请
     * 
     * @param audit
     *            AuditDto
     * @param currentUser
     *            String
     */
    @Transactional
    public void auditServiceCenter(AuditDto audit, String currentUser) {
        validate(audit);
        if (audit == null || StringUtils.isEmpty(audit.getAppId())) {
            return;
        }
        String userId = audit.getUserId();

        ServiceCenterApplication application = serviceCenterApplicationRepository.findOne(audit.getAppId());
        if (application == null
                || (application.getStatus() != EApplicationStatus.PENDDING && application.getStatus() != EApplicationStatus.NULL)) {
            return;
        }
        EApplicationStatus status = audit.isPassed() ? EApplicationStatus.ACCEPT : EApplicationStatus.REJECT;
        Date currentDate = new Date();

        Map<String, String> currentInfo = new HashMap<String, String>();
        Map<String, String> backupInfo = new HashMap<String, String>();
        AgencyApplication agencyApplication = agencyApplicationRepository.findOne(audit.getAppId());
        if (agencyApplication != null) {
            EApplicationStatus currentStatus = agencyApplication.getStatus();
            checkCurrentStatus(currentStatus);

            agencyApplication.setComments(audit.getComments());
            agencyApplication.setStatus(status);
            agencyApplication.setLastMntTs(currentDate);
            agencyApplication.setLastMntOpid(currentUser);

            agencyApplicationRepository.save(agencyApplication);
            
            currentInfo.put(TYPE, EUserType.ORGANIZATION.getCode());
            currentInfo.put(MOBILE_ORG_CONTACT, agencyApplication.getContactMobile());
            /** User cannot update bank account by himself. **/
            
        } else {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "No agency info, user Id is " + userId);
        }

        application.setComments(audit.getComments());
        application.setStatus(status);
        application.setLastMntTs(currentDate);
        application.setLastMntOpid(currentUser);
        if (audit.isPassed()) {
            application.setLevel(audit.getLevel());
        }
        serviceCenterApplicationRepository.save(application);
        String name = null;
        String userName = null;
        boolean firstTime = false;
        if (audit.isPassed()) {
        	Agency originalAgency = this.agencyRepository.findOne(userId);
        	if (originalAgency == null) {
				firstTime = true;
			} else {
				backupInfo.put(MOBILE_ORG_CONTACT, originalAgency.getContactMobile());
//				try {
//					List<BankAcctPo> list = bankAcctRepository.findBankAcctByUserId(userId);
//					BankAcctPo bankAcctPo = list.get(0);
//					backupInfo.put(BANK_ACCOUNT, bankAcctPo.getBnkAcctNo());	
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
			}
            Agency agency = ConverterService.convert(agencyApplication, Agency.class);
            agencyRepository.save(agency);
            User user = processUser(userId, currentUser, agency);
            name = user.getName();
            userName = user.getUsername();
            ServiceCenterInfo serviceCenter = ConverterService.convert(application, ServiceCenterInfo.class);
            serviceCenter.setLevel(String.valueOf(audit.getLevel()));
            /** recover service center full name......... **/
//          ServiceCenterInfo originalSC = this.serviceCenterInfoRepository.findByUserId(userId);
//          if (originalSC != null) {
//				serviceCenter.setServiceCenterDesc(originalSC.getServiceCenterDesc());
//			}
            DynamicOption region = SystemDictUtil.getDictByCategoryAndCode(EOptionCategory.REGION, agency.getRegion());
            DynamicOption parent = SystemDictUtil.getParentDict(region);
            // 省 +市+机构名称
            String fullName = parent.getLongText() + region.getLongText() + agency.getName();
            serviceCenter.setServiceCenterDesc(fullName);
            serviceCenterInfoRepository.save(serviceCenter);

            processAccountNo(application.getUserId(), EBizRole.AUTHZ_CNTL, currentUser);
            createBankAccount(userId, agencyApplication, currentUser);

            authorizationService.assignRole(EBizRole.AUTHZ_CNTL, agency.getUserId());
            authorizationService.revokeRole(EBizRole.TOURIST_AGENCY, agency.getUserId());

            actionHistoryUtil.save(EntityType.USER, application.getUserId(), ActionType.AUDIT,
                    ActionResult.SERVICE_CENTER_AUDIT_PASS, audit.getComments());
        } else {
        	Agency originalAgency = this.agencyRepository.findOne(userId);
        	if (originalAgency == null) {
				firstTime = true;
				User user = userService.getUserByUserId(userId);
				name = user.getName();
				userName = user.getUsername();
			}
            actionHistoryUtil.save(EntityType.USER, application.getUserId(), ActionType.AUDIT,
                    ActionResult.SERVICE_CENTER_AUDIT_REJECT, audit.getComments());
        }
        this.processTodo(audit, EMemberType.AUTHZDCENTER, audit.getUserId(), name);
        this.processRegisterMessage(firstTime, audit.getUserId(), audit.isPassed(), agencyApplication.getName(), userName);
        this.processInfoUpdateMessage(userId, currentInfo, backupInfo, name);
    }

    /**
     * Refresh user info
     * 
     * @param userId
     * @param currentUserId
     * @param agency
     * @return user real name.
     */
    private User processUser(String userId, String currentUserId, Agency agency) {
        User user = userService.getUserByUserId(userId);
        if (user == null) {
            return null;
        }
        user.setEmail(agency.getEmail());
        /** Copy contact mobile to user. **/
        user.setMobile(agency.getContactMobile());
        user.setType(EUserType.ORGANIZATION);
        user.setName(agency.getName());
        user.setRegion(agency.getRegion());
        user.setLastMntOpid(currentUserId);
        user.setLastMntTs(new Date());
        userService.updateUser(user);
        return user;
    }

    /**
     * Commit bank info for specific user
     * 
     * @param userId
     * @param agency
     * @return
     */
    private void createBankAccount(String userId, AgencyApplication agency, String currentUserId) {

//        List<BankAcct> bankAccountList = bankService.findBankAcctByUserIdWihoutCheck(userId);
//        if (!bankAccountList.isEmpty()) {
//            return;
//        }
//        Date currentDate = new Date();
//        BankAcct bankAcct = new BankAcct();
//        bankAcct.setUserId(userId);
//        bankAcct.setBnkAcctNo(agency.getBankAccount());
//        bankAcct.setBnkCd(agency.getBankShortName());
//        bankAcct.setBnkName(agency.getBankFullName());
//        bankAcct.setBnkCardImg(agency.getBankCardFrontImg());
//        bankAcct.setBnkAcctName(agency.getBankAccountName());
//        bankAcct.setSignedFlg(EFlagType.NO.getCode());
//        bankAcct.setBnkOpenProv(agency.getBankOpenProvince());
//        bankAcct.setBnkOpenCity(agency.getBankOpenCity());
//        bankAcct.setBnkBrch(agency.getBankOpenBranch());
//        bankAcct.setCreateTs(currentDate);
//        bankAcct.setLastMntTs(currentDate);
//        bankAcct.setCreateOpid(currentUserId);
//        bankAcct.setLastMntOpid(currentUserId);
//        bankAcct.setOldBnkAcctNo(agency.getBankAccount());
//
//        bankService.createBankAcct(bankAcct);

    }

    /**
     * Approve the apply
     * 
     * @param audit
     * @param currentUser
     * @throws ServiceException
     */
    @Transactional
    public void auditProductProviderApplicationMember(AuditDto audit, String currentUser) {
        validate(audit);
        if (audit == null || StringUtils.isEmpty(audit.getAppId())) {
            return;
        }
        String userId = audit.getUserId();

        ProductProviderApplication application = productProviderApplicationRepository.findOne(audit.getAppId());
        if (application == null
                || (application.getStatus() != EApplicationStatus.PENDDING && application.getStatus() != EApplicationStatus.NULL)) {
            return;
        }
        EApplicationStatus status = audit.isPassed() ? EApplicationStatus.ACCEPT : EApplicationStatus.REJECT;
        Date currentDate = new Date();

        Map<String, String> currentInfo = new HashMap<String, String>();
        Map<String, String> backupInfo = new HashMap<String, String>();
        AgencyApplication agencyApplication = agencyApplicationRepository.findOne(audit.getAppId());
        if (agencyApplication != null) {
            EApplicationStatus currentStatus = agencyApplication.getStatus();
            checkCurrentStatus(currentStatus);

            agencyApplication.setComments(audit.getComments());
            agencyApplication.setStatus(status);
            agencyApplication.setLastMntTs(currentDate);
            agencyApplication.setLastMntOpid(currentUser);

            agencyApplicationRepository.save(agencyApplication);
            
            currentInfo.put(TYPE, EUserType.ORGANIZATION.getCode());
            currentInfo.put(MOBILE_ORG_CONTACT, agencyApplication.getContactMobile());
            /** User cannot update bank account by himself. **/
            
        } else {
        	throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "No agency info, user Id is " + userId);
        }

        application.setComments(audit.getComments());
        application.setStatus(status);
        application.setLastMntTs(currentDate);
        application.setLastMntOpid(currentUser);
        if (audit.isPassed()) {
            application.setProSeverLevel(audit.getLevel());
        }

        productProviderApplicationRepository.save(application);
        String userName = null;
        boolean firstTime = false;
        if (audit.isPassed()) {
        	Agency originalAgency = this.agencyRepository.findOne(userId);
        	if (originalAgency == null) {
				firstTime = true;
			} else {
				backupInfo.put(MOBILE_ORG_CONTACT, originalAgency.getContactMobile());
			}
            Agency agency = ConverterService.convert(agencyApplication, Agency.class);

            agencyRepository.save(agency);

            User user = processUser(userId, currentUser, agency);
			userName = user.getUsername();
            ProductProviderInfo productProviderInfo = ConverterService.convert(application, ProductProviderInfo.class);

            productProviderInfo.setProSeverLevel(String.valueOf(audit.getLevel()));

            productProviderRepository.save(productProviderInfo);

            processAccountNo(application.getUserId(), EBizRole.PROD_SERV, currentUser);

            createBankAccount(userId, agencyApplication, currentUser);
            authorizationService.assignRole(EBizRole.PROD_SERV, agency.getUserId());
            authorizationService.revokeRole(EBizRole.TOURIST_AGENCY, agency.getUserId());

            actionHistoryUtil.save(EntityType.USER, application.getUserId(), ActionType.AUDIT,
                    ActionResult.PROVIDER_CENTER_AUDIT_PASS, audit.getComments());
        } else {
        	Agency originalAgency = this.agencyRepository.findOne(userId);
        	if (originalAgency == null) {
				firstTime = true;
				User user = userService.getUserByUserId(userId);
				userName = user.getUsername();
			}
            actionHistoryUtil.save(EntityType.USER, application.getUserId(), ActionType.AUDIT,
                    ActionResult.PROVIDER_CENTER_AUDIT_REJECT, audit.getComments());
        }
        this.processTodo(audit, EMemberType.PRODUCTSERVICE, audit.getUserId(), userName);
        this.processRegisterMessage(firstTime, audit.getUserId(), audit.isPassed(), agencyApplication.getName(), userName);
        this.processInfoUpdateMessage(userId, currentInfo, backupInfo, userName);
    }

    private void processRegisterMessage(boolean firstTime, String userId, boolean isPassed, String name, String userName) {
        /** Send message to user when he become a member first time. **/
    	if (!firstTime) {
			return;
		}
    	if (isPassed) {
    		String messageKey = "member.formal.message";
        	messageService.sendMessage(EMessageType.SMS, messageKey, userId, name, userName);	
		} else {
			String messageKey = "member.formal.fail.message";
        	messageService.sendMessage(EMessageType.SMS, messageKey, userId, name);
		}
    }
    
    /**
     * 
     * @param userId
     * @param currentInfo
     * @param backupInfo
     * @param name  real name.
     */
    private void processInfoUpdateMessage(String userId, Map<String, String> currentInfo, Map<String, String> backupInfo, String name) {
    	if (currentInfo.containsKey(BANK_ACCOUNT) && backupInfo.containsKey(BANK_ACCOUNT)) {
			if (backupInfo.get(BANK_ACCOUNT) == null || !backupInfo.get(BANK_ACCOUNT).equals(currentInfo.get(BANK_ACCOUNT))) {
				String messageKey = "member.bankaccount.change.message";
	        	String content = currentInfo.get(BANK_ACCOUNT);
	        	messageService.sendMessage(EMessageType.SMS, messageKey, userId, content, name);
	        	messageService.sendMessage(EMessageType.MESSAGE, messageKey, userId, content, name);	
			}
		}
		if (EUserType.PERSON.getCode().equals(currentInfo.get(TYPE))) {
			if (currentInfo.containsKey(MOBILE_PERSON) && backupInfo.containsKey(MOBILE_PERSON)) {
				if (backupInfo.get(MOBILE_PERSON) == null || !backupInfo.get(MOBILE_PERSON).equals(currentInfo.get(MOBILE_PERSON))) {
					String messageKey = "member.mobile.change.message";
		        	String content = currentInfo.get(MOBILE_PERSON);
		        	messageService.sendMessage(EMessageType.SMS, messageKey, userId, content);
		        	messageService.sendMessage(EMessageType.MESSAGE, messageKey, userId, content);	
				}
			}
		} else if (EUserType.ORGANIZATION.getCode().equals(currentInfo.get(TYPE))) {
			if (currentInfo.containsKey(MOBILE_ORG_LEGAL) && backupInfo.containsKey(MOBILE_ORG_LEGAL)) {
				if (backupInfo.get(MOBILE_ORG_LEGAL) == null || !backupInfo.get(MOBILE_ORG_LEGAL).equals(currentInfo.get(MOBILE_ORG_LEGAL))) {
					String messageKey = "member.mobile.change.message";
		        	String content = currentInfo.get(MOBILE_ORG_LEGAL);
		        	messageService.sendMessage(EMessageType.SMS, messageKey, userId, content);
		        	messageService.sendMessage(EMessageType.MESSAGE, messageKey, userId, content);
				}
			}
			if (currentInfo.containsKey(MOBILE_ORG_CONTACT) && backupInfo.containsKey(MOBILE_ORG_CONTACT)) {
				if (backupInfo.get(MOBILE_ORG_CONTACT) == null || !backupInfo.get(MOBILE_ORG_CONTACT).equals(currentInfo.get(MOBILE_ORG_CONTACT))) {
					String messageKey = "member.mobile.change.message";
		        	String content = currentInfo.get(MOBILE_ORG_CONTACT);
		        	messageService.sendMessage(EMessageType.SMS, messageKey, userId, content);
		        	messageService.sendMessage(EMessageType.MESSAGE, messageKey, userId, content);	
				}
			}
		}
    }

	private void processTodo(AuditDto audit, EMemberType memberType, String userId, String userName) {
		String action = "通过";
		if (!audit.isPassed()) {
			action = "未通过";
		}
    	String messageKey = "member.audit.feedback.message";
    	if (EMemberType.BASIC == memberType) {
        	String content = EMemberType.BASIC.getText();
        	messageService.sendMessage(EMessageType.MESSAGE, messageKey, userId, content, action, userName);
		} else if (EMemberType.INVESTOR == memberType) {
			String content = EMemberType.INVESTOR.getText() + "权限";
        	messageService.sendMessage(EMessageType.MESSAGE, messageKey, userId, content, action, userName);
		} else if (EMemberType.FINANCER == memberType) {
			String content = EMemberType.FINANCER.getText() + "权限";
        	messageService.sendMessage(EMessageType.MESSAGE, messageKey, userId, content, action, userName);
		} else if (EMemberType.AUTHZDCENTER == memberType) {
			String content = EMemberType.AUTHZDCENTER.getText() + "权限";
        	messageService.sendMessage(EMessageType.MESSAGE, messageKey, userId, content, action, userName);
		} else if (EMemberType.PRODUCTSERVICE == memberType) {
			String content = EMemberType.PRODUCTSERVICE.getText() + "权限";
        	messageService.sendMessage(EMessageType.MESSAGE, messageKey, userId, content, action, userName);
		} else if (EMemberType.PRODSERV_AUTHZD == memberType) {
			String content = EMemberType.PRODSERV_AUTHZD.getText() + "权限";
        	messageService.sendMessage(EMessageType.MESSAGE, messageKey, userId, content, action, userName);
		}
	}

}
