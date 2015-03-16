/*
 * Project Name: kmfex-platform
 * File Name: MemberApplicationViewService.java
 * Class Name: MemberApplicationViewService
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
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.entity.ActionHistoryPo;
import com.hengxin.platform.common.enums.EOptionCategory;
import com.hengxin.platform.common.service.ActionHistoryService;
import com.hengxin.platform.common.service.PagingService;
import com.hengxin.platform.common.util.PaginationUtil;
import com.hengxin.platform.common.util.SystemDictUtil;
import com.hengxin.platform.fund.domain.Acct;
import com.hengxin.platform.fund.domain.BankAcct;
import com.hengxin.platform.fund.entity.AcctPo;
import com.hengxin.platform.fund.service.AcctService;
import com.hengxin.platform.fund.service.BankAcctService;
import com.hengxin.platform.member.domain.Agency;
import com.hengxin.platform.member.domain.AgencyApplicationView;
import com.hengxin.platform.member.domain.ServiceCenterApplication;
import com.hengxin.platform.member.domain.ServiceCenterInfo;
import com.hengxin.platform.member.domain.UserCompanyView;
import com.hengxin.platform.member.dto.ActionHistoryDto;
import com.hengxin.platform.member.dto.AuditLogDto;
import com.hengxin.platform.member.dto.MemberSearchDto;
import com.hengxin.platform.member.dto.downstream.ServiceCenterApplicationDto;
import com.hengxin.platform.member.enums.EApplicationStatus;
import com.hengxin.platform.member.repository.AgencyApplicationRepository;
import com.hengxin.platform.member.repository.AgencyApplicationViewRepository;
import com.hengxin.platform.member.repository.AgencyRepository;
import com.hengxin.platform.member.repository.ServiceCenterInfoRepository;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.domain.User;
import com.hengxin.platform.security.entity.UserPo;

/**
 * Class Name: MemberApplicationViewService
 * 
 * @author chunlinwang
 * 
 */
@Service
public class ServiceCenterService extends PagingService<ServiceCenterApplication> {

    @Autowired
    private ServiceCenterInfoRepository serviceCenterInfoRepository;

    @Autowired
    private AgencyApplicationRepository agencyApplicationRepository;

    @Autowired
    private AgencyRepository agencyRepository;

    @Autowired
    private BankAcctService bankAcctService;

    @Autowired
    private AcctService acctService;

    @Autowired
    private ActionHistoryService actionHistoryService;

    @Autowired
    private AgencyApplicationViewRepository applicationViewRepository;
    
    @Autowired
	private SecurityContext securityContext;
    
    @Autowired
    private UserCompanyViewService userCompanyViewService;

    @Transactional()
    // FIXME Not allowed DTO returned from service
    public DataTablesResponseDto<ServiceCenterApplicationDto> getServiceCenters(final MemberSearchDto auditSearch) {

        Pageable pageRequest = PaginationUtil.buildPageRequest(auditSearch);

        Specification<AgencyApplicationView> specification = new Specification<AgencyApplicationView>() {
            @Override
            public Predicate toPredicate(Root<AgencyApplicationView> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();

                if (auditSearch != null) {
                    String userId = auditSearch.getUserId();
                    String orgName = auditSearch.getName();
                    EApplicationStatus auditStatus = auditSearch.getAuditStatus();

                    if (auditStatus == EApplicationStatus.PENDDING) {
                        expressions.add(cb.equal(root.<EApplicationStatus> get("serviceCenterStatus"),
                                EApplicationStatus.PENDDING));
                    } else if (auditStatus == EApplicationStatus.AUDITED) {
                        Predicate pred = cb.disjunction();
                        List<Expression<Boolean>> orExp = pred.getExpressions();
                        orExp.add(cb.equal(root.<EApplicationStatus> get("serviceCenterStatus"),
                                EApplicationStatus.ACCEPT));
                        orExp.add(cb.equal(root.<EApplicationStatus> get("serviceCenterStatus"),
                                EApplicationStatus.REJECT));
                        orExp.add(cb.equal(root.<EApplicationStatus> get("serviceCenterStatus"),
                                EApplicationStatus.ACTIVE));
                        expressions.add(cb.or(pred));
                    }

                    if (StringUtils.isNotBlank(userId)) {
                        expressions
                                .add(cb.like(cb.lower(root.<String> get("userName")), "%" + userId.toLowerCase() + "%"));
                    }
                    if (StringUtils.isNotBlank(orgName)) {
                        expressions
                                .add(cb.like(cb.lower(root.<String> get("name")), "%" + orgName.toLowerCase() + "%"));
                    }
                    
                    //分公司过滤
                    User currentUser = securityContext.getCurrentUser();
                	UserCompanyView uc = userCompanyViewService.getUserCompany(currentUser.getUserId());
                	//无company_id或company_id=1的不进行分公司过滤
                	if(null!=uc&&!"1".equals(uc.getCompany_id())){
                		Predicate pred = cb.disjunction();
                        List<Expression<Boolean>> orExp = pred.getExpressions();
                        String[] subs = uc.getSubs().split(",");
                        for(String sub : subs){
                        	orExp.add(cb.equal(root.<String> get("companyId"),sub));
                        }
                        expressions.add(cb.or(pred));
                	}

                    /**
                     *                     // Predicate pred = cb.disjunction();
                    // List<Expression<Boolean>> orExp = pred.getExpressions();
                    // orExp.add(cb.equal(root.<EMemberType> get("userRole"), EMemberType.AUTHZDCENTER));
                    // orExp.add(cb.equal(root.<EMemberType> get("userRole"), EMemberType.PRODSERV_AUTHZD));
                    // expressions.add(cb.or(pred));
                     */

                }
                return predicate;
            }
        };

        Page<AgencyApplicationView> agencies = applicationViewRepository.findAll(specification, pageRequest);

        List<ServiceCenterApplicationDto> serviceCenterApplications = new ArrayList<ServiceCenterApplicationDto>();

        for (AgencyApplicationView serviceCenter : agencies.getContent()) {

            serviceCenterApplications.add(convert(serviceCenter));
        }

        DataTablesResponseDto<ServiceCenterApplicationDto> result = new DataTablesResponseDto<ServiceCenterApplicationDto>();
        result.setData(serviceCenterApplications);

        result.setTotalDisplayRecords(agencies.getTotalElements());
        result.setTotalRecords(agencies.getTotalElements());
        result.setEcho(auditSearch.getEcho());
        return result;
    }

    public ServiceCenterApplicationDto getServiceCenterApplication(String userId) {
        return convert(applicationViewRepository.findOne(userId));
    }

    // FIXME Not allowed DTO returned from service
    public DataTablesResponseDto<ActionHistoryDto> getLog(AuditLogDto auditLog, boolean inCanViewPage) {
        Pageable pageRequest = PaginationUtil.buildPageRequest(auditLog);
        Page<ActionHistoryPo> userActionHistory = actionHistoryUtil.findUserLogByUserId(auditLog.getUserId(),
                pageRequest);
        DataTablesResponseDto<ActionHistoryDto> result = new DataTablesResponseDto<ActionHistoryDto>();

        List<ActionHistoryDto> actionHistory = new ArrayList<ActionHistoryDto>();
        for (ActionHistoryPo tmp : userActionHistory.getContent()) {
            ActionHistoryDto dto = ConverterService.convert(tmp, ActionHistoryDto.class);
            if (tmp.getUser() != null) {
                dto.setOperateUser(tmp.getUser().getUsername());
            }
            actionHistory.add(dto);
        }
        List<ActionHistoryDto> mergedActionHistorys = actionHistoryService.mergeActionHistory(actionHistory,
                inCanViewPage);

        result.setData(mergedActionHistorys);
        result.setTotalDisplayRecords(userActionHistory.getTotalElements());
        result.setTotalRecords(userActionHistory.getTotalElements());
        result.setEcho(auditLog.getEcho());
        return result;
    }

    // FIXME Not allowed DTO returned from service
    public ServiceCenterApplicationDto convert(AgencyApplicationView serviceCenter) {
        if (serviceCenter == null) {
            return null;
        }
        ServiceCenterApplicationDto sca = ConverterService.convert(serviceCenter, ServiceCenterApplicationDto.class);
        sca.setLevel(SystemDictUtil.getDictByCategoryAndCode(EOptionCategory.SERVICE_CENTER_LEVEL,
                serviceCenter.getServiceCenterLevel()));
        /** load agency info by user id, sort by time DESC. **/
        String userId = serviceCenter.getUserId();
        List<BankAcct> bankAcct = bankAcctService.findBankAcctByUserIdWihoutCheck(userId);
        if (bankAcct != null && !bankAcct.isEmpty()) {

            sca.setBankAcct(bankAcct.get(0));
        }
        AcctPo acct = acctService.getAcctByUserId(userId);
        if (acct != null) {
            sca.setAccount(ConverterService.convert(acct, Acct.class));
        }

        return sca;
    }

    /**
     * It does not display service center region info.
     * @return
     */
    public List<ServiceCenterInfo> getAllServiceCentersWithoutRegion() {
    	Specification<ServiceCenterInfo> specification = new Specification<ServiceCenterInfo>() {
            @Override
            public Predicate toPredicate(Root<ServiceCenterInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                Join<ServiceCenterInfo, UserPo> join = root.join("user");
            	expressions.add(cb.equal(join.<String> get("status"), "A"));
                return predicate;
            }
        };
        List<ServiceCenterInfo> lists = serviceCenterInfoRepository.findAll(specification);
        for (ServiceCenterInfo serviceCenter : lists) {
			this.processServiceCenterName(serviceCenter, true);
		}
    	return lists;
    }

    public List<ServiceCenterInfo> getAllServiceCenters() {
    	Specification<ServiceCenterInfo> specification = new Specification<ServiceCenterInfo>() {
            @Override
            public Predicate toPredicate(Root<ServiceCenterInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                Join<ServiceCenterInfo, UserPo> join = root.join("user");
            	expressions.add(cb.equal(join.<String> get("status"), "A"));
            	expressions.add(cb.isNotNull(root.get("serviceCenterDesc")));
                return predicate;
            }
        };
        List<ServiceCenterInfo> lists = serviceCenterInfoRepository.findAll(specification);
        for (ServiceCenterInfo serviceCenter : lists) {
			this.processServiceCenterName(serviceCenter, false);
		}
    	return lists;
    }

	private void processServiceCenterName(ServiceCenterInfo serviceCenter, boolean ignore) {
		if (ignore) {
			serviceCenter.setServiceCenterDesc(serviceCenter.getAgencyApplication().getName());
		} else {
			if (serviceCenter.getServiceCenterDesc() == null) {
				serviceCenter.setServiceCenterDesc(serviceCenter.getAgencyApplication().getName());
			}
		}
	}
    
    public List<ServiceCenterInfo> getServiceCenters(final String name) {
    	Specification<ServiceCenterInfo> specification = new Specification<ServiceCenterInfo>() {
            @Override
            public Predicate toPredicate(Root<ServiceCenterInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                if (name != null) {
                	expressions.add(cb.like(cb.lower(root.<String> get("serviceCenterDesc")), "%" + name.toLowerCase().trim() + "%"));
                }
                return predicate;
            }
        };
        return serviceCenterInfoRepository.findAll(specification);
    }

    public Agency getAgencyById(String userId) {
        return agencyRepository.findByUserId(userId);
    }

    public ServiceCenterInfo getServiceCenter(String userId) {
    	return serviceCenterInfoRepository.findOne(userId);
    }

}
