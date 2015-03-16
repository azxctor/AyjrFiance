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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.domain.DynamicOption;
import com.hengxin.platform.common.enums.EOptionCategory;
import com.hengxin.platform.common.enums.EntityType;
import com.hengxin.platform.common.service.PagingService;
import com.hengxin.platform.common.util.SystemDictUtil;
import com.hengxin.platform.fund.domain.Acct;
import com.hengxin.platform.fund.domain.BankAcct;
import com.hengxin.platform.fund.entity.AcctPo;
import com.hengxin.platform.fund.repository.BankAcctRepository;
import com.hengxin.platform.fund.service.AcctService;
import com.hengxin.platform.fund.service.BankAcctService;
import com.hengxin.platform.member.converter.FinancierApplicationConverter;
import com.hengxin.platform.member.converter.FinancierInfoConverter;
import com.hengxin.platform.member.converter.InvestorApplicationConverter;
import com.hengxin.platform.member.converter.InvestorInfoConverter;
import com.hengxin.platform.member.converter.MemberInfoConverter;
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
import com.hengxin.platform.member.dto.FinancierDto;
import com.hengxin.platform.member.dto.InvestorDto;
import com.hengxin.platform.member.dto.OrganizationDto;
import com.hengxin.platform.member.dto.PersonDto;
import com.hengxin.platform.member.dto.upstream.MemberApplicationAuditDto;
import com.hengxin.platform.member.enums.EApplicationStatus;
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

/**
 * Class Name: MemberApplicationService
 * 
 * @author Ryan
 * 
 */
@Service
public class MemberApplicationService extends PagingService<BaseApplication> {

    @Autowired
    private MemberApplicationRepository applicationRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private InvestorApplicationRepository investorApplicationRepository;

    @Autowired
    private InvestorInfoRepository investorInfoRepository;

    @Autowired
    private FinancierApplicationRepository financierApplicationRepository;

    @Autowired
    private FinancierInfoRepository financierInfoRepository;

    @Autowired
    private AcctService acctService;

    @Autowired
    private AgencyApplicationRepository agencyApplicationRepository;

    @Autowired
    private AgencyRepository agencyRepository;

    @Autowired
    private ServiceCenterApplicationRepository serviceCenterApplicationRepository;

    @Autowired
    private ProductProviderApplicationRepository productProviderApplicationRepository;

    @Autowired
    ServiceCenterInfoRepository serviceCenterInfoRepository;

    @Autowired
    ProductProviderRepository productProviderRepository;

    @Autowired
    private BankAcctRepository bankAcctRepository;

    @Autowired
    private BankAcctService bankService;

    /**
     * Get person details info
     * 
     * @param userId
     * @return
     */
    public MemberApplication getLastCreateMember(String userId) {
        Pageable pageable = new PageRequest(0, 1, Direction.DESC, "createTs");
        Page<MemberApplication> basicInfoList = applicationRepository.findByUserId(userId, pageable);
        if (basicInfoList.hasContent()) {
            return basicInfoList.getContent().get(0);
        }
        return null;
    }

    public MemberApplication getMemberWithLatestStatus(String userId) {
        Pageable pageable = new PageRequest(0, 1, Direction.DESC, "lastMntTs");
        Page<MemberApplication> basicInfoList = applicationRepository.findByUserId(userId, pageable);
        if (basicInfoList.hasContent()) {
            return basicInfoList.getContent().get(0);
        }
        return null;
    }

    /**
     * Get investor details info
     * 
     * @param userId
     * @return
     */
    public InvestorApplication getInvestorDetailsWithLatestStatus(String userId) {
        Pageable pageable = new PageRequest(0, 1, Direction.DESC, "lastMntTs");
        Page<InvestorApplication> investorAppPoList = investorApplicationRepository.findByUserId(userId, pageable);
        if (investorAppPoList.hasContent()
                && investorAppPoList.getContent().get(0).getStatus() != EApplicationStatus.UNCOMMITTED) {
            InvestorApplication investorApplicationPo = investorAppPoList.getContent().get(0);
            return investorApplicationPo;
        }
        return null;
    }

    public InvestorApplication getLastCreateInvestorDetails(String userId) {
        Pageable pageable = new PageRequest(0, 1, Direction.DESC, "createTs");
        Page<InvestorApplication> investorAppPoList = investorApplicationRepository.findByUserId(userId, pageable);
        if (investorAppPoList.hasContent()
                && investorAppPoList.getContent().get(0).getStatus() != EApplicationStatus.UNCOMMITTED) {
            InvestorApplication investorApplicationPo = investorAppPoList.getContent().get(0);
            return investorApplicationPo;
        }
        return null;
    }

    /**
     * Get financier details info
     * 
     * @param userId
     * @return
     */
    public FinancierApplication getLastCreateFinancierDetails(String userId) {
        Pageable pageable = new PageRequest(0, 1, Direction.DESC, "createTs");
        Page<FinancierApplication> financierList = financierApplicationRepository.findByUserId(userId, pageable);
        if (financierList.hasContent()
                && financierList.getContent().get(0).getStatus() != EApplicationStatus.UNCOMMITTED) {
            return financierList.getContent().get(0);
        }
        return null;
    }

    public FinancierApplication getFinancierDetailsWithLatestStatus(String userId) {
        Pageable pageable = new PageRequest(0, 1, Direction.DESC, "lastMntTs");
        Page<FinancierApplication> financierList = financierApplicationRepository.findByUserId(userId, pageable);
        if (financierList.hasContent()
                && financierList.getContent().get(0).getStatus() != EApplicationStatus.UNCOMMITTED) {
            return financierList.getContent().get(0);
        }
        return null;
    }

    /**
     * Get account details
     * 
     * @param userId
     * @return
     */
    public Acct getAccountDetails(String userId) {
        AcctPo acctPo = acctService.getAcctByUserId(userId);
        Acct account = null;
        if (acctPo!=null) {
            account = ConverterService.convert(acctPo, Acct.class);
        }
        return account;
    }

    @Transactional
    public void updateBasicInfo(MemberApplication applicationPo, BankAcct bankAcct, MemberApplication oldInfo) {
        applicationRepository.save(applicationPo);
        bankService.updateBankAcct(bankAcct);
        actionHistoryUtil.saveForMaintain(oldInfo, applicationPo, applicationPo.getUserId(), EntityType.USER);
    }

    @Transactional
    public void updateInvestorInfo(InvestorApplication applicationPo, InvestorApplication oldInfo) {
        investorApplicationRepository.save(applicationPo);
        InvestorInfo investorPo = ConverterService.convert(applicationPo, InvestorInfo.class);
        InvestorInfo originalInfo = investorInfoRepository.findByUserId(applicationPo.getUserId());
        if (originalInfo != null) {
        	investorPo.setInvestorLevelOriginal(originalInfo.getInvestorLevelOriginal());	
		}
        investorInfoRepository.save(investorPo);
        actionHistoryUtil.saveForMaintain(oldInfo, applicationPo, applicationPo.getUserId(), EntityType.USER);
    }

    @Transactional
    public void updateFinancierInfo(FinancierApplication applicationPo, FinancierApplication oldInfo, boolean fromUpdate) {
        financierApplicationRepository.save(applicationPo);
        FinancierInfo financiePo = ConverterService.convert(applicationPo, FinancierInfo.class);
        financierInfoRepository.save(financiePo);
        if (fromUpdate) {
            actionHistoryUtil.saveForMaintain(oldInfo, applicationPo, applicationPo.getUserId(), EntityType.USER);
        }
    }

    @Transactional
    public void updateServiceCenterInfo(ServiceCenterApplication applicationPo, ServiceCenterApplication oldInfo) {
        serviceCenterApplicationRepository.save(applicationPo);
        ServiceCenterInfo serviceCenter = ConverterService.convert(applicationPo, ServiceCenterInfo.class);
        serviceCenterInfoRepository.save(serviceCenter);
        actionHistoryUtil.saveForMaintain(oldInfo, applicationPo, applicationPo.getUserId(), EntityType.USER);
    }

    @Transactional
    public void updateProductProviderInfo(ProductProviderApplication applicationPo, ProductProviderApplication oldInfo) {
        productProviderApplicationRepository.save(applicationPo);
        ProductProviderInfo productProvider = ConverterService.convert(applicationPo, ProductProviderInfo.class);
        productProviderRepository.save(productProvider);
        actionHistoryUtil.saveForMaintain(oldInfo, applicationPo, applicationPo.getUserId(), EntityType.USER);
    }

    @Transactional
    public void updateAgencyInfo(AgencyApplication info, BankAcct bankAcct, AgencyApplication oldInfo) {
        agencyApplicationRepository.save(info);
        bankService.updateBankAcct(bankAcct);
        actionHistoryUtil.saveForMaintain(oldInfo, info, info.getUserId(), EntityType.USER);
    }

    public MemberApplicationAuditDto getMemberInfoDetails(String userId, String userType) {
        MemberApplicationAuditDto dto = new MemberApplicationAuditDto();
        Pageable pageable = new PageRequest(0, 1, Direction.DESC, "lastMntTs");
        /** filter UNCOMMITTED status. **/
        Page<MemberApplication> basicInfoList = applicationRepository.findByUserIdAndStatusNot(userId,EApplicationStatus.UNCOMMITTED, pageable);
        if (basicInfoList.hasContent()) {
            MemberApplication memberAppPo = basicInfoList.getContent().get(0);
            if (EUserType.PERSON == EUserType.valueOf(userType)) {
                // 拒绝状态下，有原信息取原信息
                if (memberAppPo.getStatus() == EApplicationStatus.REJECT) {
                    Member oldMember = memberRepository.findByUserId(userId);
                    if (oldMember != null) {
                        dto.setPerson(ConverterService.convert(oldMember, PersonDto.class, MemberInfoConverter.class));
                    } else {
                        dto.setPerson(ConverterService.convert(memberAppPo, PersonDto.class));
                    }
                } else {
                    dto.setPerson(ConverterService.convert(memberAppPo, PersonDto.class));
                }
                DynamicOption option = SystemDictUtil.getDictByCategoryAndCode(EOptionCategory.REGION, memberAppPo.getBankOpenProvince());
                dto.getPerson().setBankOpenProvince(option);
                DynamicOption option2 = SystemDictUtil.getDictByCategoryAndCode(EOptionCategory.REGION, memberAppPo.getBankOpenCity());
                dto.getPerson().setBankOpenCity(option2);
                DynamicOption option3 = SystemDictUtil.getDictByCategoryAndCode(EOptionCategory.REGION, memberAppPo.getPersonRegion());
                dto.getPerson().setPersonRegion(option3);
            }
            if (EUserType.ORGANIZATION == EUserType.valueOf(userType)) {
                // 拒绝状态下，有原信息取原信息
                if (memberAppPo.getStatus() == EApplicationStatus.REJECT) {
                    Member oldMember = memberRepository.findByUserId(userId);
                    if (oldMember != null) {
                        dto.setOrganization(ConverterService.convert(oldMember, OrganizationDto.class,
                                MemberInfoConverter.class));
                    } else {
                        dto.setOrganization(ConverterService.convert(memberAppPo, OrganizationDto.class));
                    }
                } else {
                    dto.setOrganization(ConverterService.convert(memberAppPo, OrganizationDto.class));
                }
                DynamicOption option = SystemDictUtil.getDictByCategoryAndCode(EOptionCategory.REGION, memberAppPo.getBankOpenProvince());
                dto.getOrganization().setBankOpenProvince(option);
                DynamicOption option2 = SystemDictUtil.getDictByCategoryAndCode(EOptionCategory.REGION, memberAppPo.getBankOpenCity());
                dto.getOrganization().setBankOpenCity(option2);
                DynamicOption option3 = SystemDictUtil.getDictByCategoryAndCode(EOptionCategory.REGION, memberAppPo.getOrgRegion());
                dto.getOrganization().setOrgRegion(option3);
            }
        }
        Page<InvestorApplication> investorAppPoList = investorApplicationRepository.findByUserIdAndStatusNot(userId, EApplicationStatus.UNCOMMITTED, pageable);
        if (investorAppPoList.hasContent()) {
            InvestorApplication investorApplicationPo = investorAppPoList.getContent().get(0);
            // 拒绝状态下，有原信息取原信息
            if (investorApplicationPo.getStatus() == EApplicationStatus.REJECT) {
                InvestorInfo oldInvestor = investorInfoRepository.findByUserId(userId);
                if (oldInvestor != null) {
                    dto.setInvestorInfo(ConverterService.convert(oldInvestor, InvestorDto.class,
                            InvestorInfoConverter.class));
                } else {
                    dto.setInvestorInfo(ConverterService.convert(investorApplicationPo, InvestorDto.class,
                            InvestorApplicationConverter.class));
                }
            } else {
                dto.setInvestorInfo(ConverterService.convert(investorApplicationPo, InvestorDto.class,
                        InvestorApplicationConverter.class));
            }
        }
        Page<FinancierApplication> financierList = financierApplicationRepository.findByUserIdAndStatusNot(userId, EApplicationStatus.UNCOMMITTED, pageable);
        if (financierList.hasContent()) {
            FinancierApplication FinaAppPo = financierList.getContent().get(0);
            // 拒绝状态下，有原信息取原信息
            if (FinaAppPo.getStatus() == EApplicationStatus.REJECT) {
                FinancierInfo oldFinancier = financierInfoRepository.findByUserId(userId);
                if (oldFinancier != null) {
                    dto.setFinancierInfo(ConverterService.convert(oldFinancier, FinancierDto.class,
                            FinancierInfoConverter.class));
                } else {
                    dto.setFinancierInfo(ConverterService.convert(FinaAppPo, FinancierDto.class,
                            FinancierApplicationConverter.class));
                }
            } else {
                dto.setFinancierInfo(ConverterService.convert(FinaAppPo, FinancierDto.class,
                        FinancierApplicationConverter.class));
            }
        }
        return dto;
    }
}
