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
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.dto.DataTablesRequestDto;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.member.domain.MemberApplicationView;
import com.hengxin.platform.member.dto.MemberSearchDto;
import com.hengxin.platform.member.dto.MemberViewDto;
import com.hengxin.platform.member.enums.EApplicationStatus;
import com.hengxin.platform.member.enums.EMemberType;
import com.hengxin.platform.member.enums.EUserType;
import com.hengxin.platform.member.repository.MemberApplicationViewRepository;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;

/**
 * Class Name: MemberApplicationViewService
 *
 * @author shengzhou
 *
 */
@Service
public class MemberApplicationViewService {

    @Autowired
    private MemberApplicationViewRepository applicationViewRepository;

    @Autowired
    private SecurityContext securityContext;
    
    
    /**
     * 
     * @param requestDto
     * @return
     */
    private Pageable buildPageRequest(DataTablesRequestDto requestDto) {

        Sort sort = null;
        List<Integer> sortedColumns = requestDto.getSortedColumns();
        List<String> sortDirections = requestDto.getSortDirections();
        List<String> dataProps = requestDto.getDataProps();
        List<Order> orderList = new ArrayList<Sort.Order>();
        for (Integer item : sortedColumns) {
            String sortColumn = dataProps.get(item);
            int indexOf = 0;
            if (StringUtils.isNotBlank(sortColumn) && sortColumn.endsWith(".text")) {
                indexOf = sortColumn.lastIndexOf(".text");
            } else if (StringUtils.isNotBlank(sortColumn) && sortColumn.endsWith(".fullText")) {
                indexOf = sortColumn.lastIndexOf(".fullText");
            }
            if (indexOf > 0) {
                sortColumn = sortColumn.substring(0, indexOf);
            }
            String sortDir = sortDirections.get(0);
            Order order = new Order(Direction.fromString(sortDir), sortColumn);
            orderList.add(order);
            sort = new Sort(Direction.fromString(sortDir), sortColumn);
        }
        orderList.add(new Order(Direction.ASC, "userId"));
        sort = new Sort(orderList);

        PageRequest page = new PageRequest(requestDto.getDisplayStart() / requestDto.getDisplayLength(),
                requestDto.getDisplayLength(), sort);
        return page;
    }

    /**
     * Get member info
     *
     * @param searchDo
     * @param currentUserId
     * @return
     */
    //FIXME Not allowed DTO returned from service
    public DataTablesResponseDto<MemberViewDto> getMembers(final MemberSearchDto searchDo) {
        final String currentUserId = StringUtils.isNotBlank(securityContext.getCurrentOwnerId()) ? securityContext
                .getCurrentOwnerId() : securityContext.getCurrentUserId();
        final boolean isServiceCenter = this.securityContext.isAuthServiceCenter();
        final boolean isCustomerServiceUser = this.securityContext.isCustomerServiceUser();
        Pageable pageRequest = buildPageRequest(searchDo);

        Specification<MemberApplicationView> specification = new Specification<MemberApplicationView>() {
            @Override
            public Predicate toPredicate(Root<MemberApplicationView> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();

                if (searchDo != null) {
                    String userName = searchDo.getUserName();
                    String name = searchDo.getName();
                    EUserType userType = searchDo.getUserType();
                    EMemberType auditContent = searchDo.getAuditContent();
                    EApplicationStatus auditStatus = searchDo.getAuditStatus();
                    EApplicationStatus investorStatus = searchDo.getInvestorStatus();
                    EApplicationStatus financeStatus = searchDo.getFinanceStatus();
                    EMemberType userRole = searchDo.getUserRole();
                    String registDate = searchDo.getRegistTime();
                    String agentRegister = searchDo.getAgentRegister();
                    String agentName = searchDo.getAgentname();
                    String transno = searchDo.getTransno();

                    if (userType != null) {
                    	expressions.add(cb.equal(root.<EUserType> get("userType"), userType));
                    }
                    /** filter user who is Canceled. **/
                    if (isServiceCenter || isCustomerServiceUser) {
                    	expressions.add(cb.equal(root.<String> get("activeStatus"), "A"));
					}
                    if (StringUtils.isNotBlank(userName)) {
                        expressions
                                .add(cb.like(cb.lower(root.<String> get("userName")), "%" + userName.toLowerCase() + "%"));
                    }

                    if (StringUtils.isNotBlank(name)) {
                        expressions.add(cb.like(cb.lower(root.<String> get("name")), "%" + name.toLowerCase() + "%"));
                    }
                    if (userRole != null && StringUtils.isNotBlank(currentUserId)
                           // && securityContext.hasRole(EBizRole.AUTHZ_CNTL.getCode())) {
                            && securityContext.hasPermission(Permissions.MEMBER_REGISTER_FOR_USER)) {
                        if (userRole == EMemberType.INVESTOR_FINANCER) {
                            expressions.add(cb.equal(root.<String> get("investorAgent"), currentUserId));
                            expressions.add(cb.equal(root.<String> get("financeAgent"), currentUserId));
                        } else if (userRole == EMemberType.INVESTOR) {
                            expressions.add(cb.equal(root.<String> get("investorAgent"), currentUserId));
                        } else if (userRole == EMemberType.FINANCER) {
                            expressions.add(cb.equal(root.<String> get("financeAgent"), currentUserId));
                        } else if (userRole == EMemberType.NULL) {
                            Expression<Boolean> expression1 = cb
                                    .equal(root.<String> get("financeAgent"), currentUserId);
                            Expression<Boolean> expression2 = cb.equal(root.<String> get("investorAgent"),
                                    currentUserId);
                            expressions.add(cb.or(expression1, expression2));
                        }
                        if (StringUtils.isNotBlank(registDate)) {
                            Date startDate = DateUtils.getDate(registDate, "yyyy-MM-dd");
                            Date endDate = DateUtils.addHours(startDate, 24);
                            expressions.add(cb.between(root.<Date> get("lastestTs"), startDate, endDate));
                        }
                        if (StringUtils.isNotBlank(agentRegister)) {
                            expressions.add(cb.equal(root.<String> get("agentRegister"), agentRegister));
                        }
                        expressions.add(cb.equal(root.<String> get("auditPass"), "Y"));
                    }

                    if (investorStatus != null && investorStatus != EApplicationStatus.NULL) {
                        expressions.add(cb.equal(root.<EApplicationStatus> get("investorStatus"), investorStatus));
                    }

                    if (financeStatus != null && financeStatus != EApplicationStatus.NULL) {
                        expressions.add(cb.equal(root.<EApplicationStatus> get("financeStatus"), financeStatus));
                    }

                    if (userRole != null && userRole != EMemberType.NULL) {
                        expressions.add(cb.equal(root.<EMemberType> get("userRole"), userRole));
                    }

                    if (auditStatus == EApplicationStatus.PENDDING) {
                        if (auditContent == EMemberType.BASIC) {
                            expressions.add(cb.equal(root.<EApplicationStatus> get("basicStatus"),
                                    EApplicationStatus.PENDDING));
                        } else if (auditContent == EMemberType.INVESTOR) {
                            expressions.add(cb.equal(root.<EApplicationStatus> get("investorStatus"),
                                    EApplicationStatus.PENDDING));
                        } else if (auditContent == EMemberType.FINANCER) {
                            expressions.add(cb.equal(root.<EApplicationStatus> get("financeStatus"),
                                    EApplicationStatus.PENDDING));
                        }
                        // expressions.add(cb.notEqual(root.<EApplicationStatus> get("basicStatus"),
                        // EApplicationStatus.REJECT));
                        expressions.add(cb.equal(root.<EApplicationStatus> get("unAuditedStatus"),
                                EApplicationStatus.PENDDING));
                    }

                    if (auditStatus == EApplicationStatus.AUDITED) {
                        expressions.add(cb.equal(root.<EApplicationStatus> get("auditedStatus"),
                                EApplicationStatus.AUDITED));
                    }
                    
                    if (StringUtils.isNotBlank(agentName)) {
                    	expressions.add(cb.like(cb.lower(root.<String> get("agentName")), "%" + agentName.toLowerCase() + "%"));
					}
                    if (StringUtils.isNotBlank(transno)) {
                    	expressions.add(cb.like(cb.lower(root.<String> get("accountNo")), "%" + transno.toLowerCase() + "%"));
                    }
                }
                return predicate;
            }
        };

        Page<MemberApplicationView> members = applicationViewRepository.findAll(specification, pageRequest);

        DataTablesResponseDto<MemberViewDto> memberView = new DataTablesResponseDto<MemberViewDto>();
        List<MemberViewDto> memberList = new ArrayList<MemberViewDto>();
        memberView.setEcho(searchDo.getEcho());
        for (MemberApplicationView memberApplicationViewPo : members) {
            memberList.add(ConverterService.convert(memberApplicationViewPo, MemberViewDto.class));
        }
        memberView.setData(memberList);
        memberView.setTotalDisplayRecords(members.getTotalElements());
        memberView.setTotalRecords(members.getTotalElements());
        return memberView;
    }
    
}
