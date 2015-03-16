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
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.service.PagingService;
import com.hengxin.platform.common.util.PaginationUtil;
import com.hengxin.platform.fund.repository.AcctRepository;
import com.hengxin.platform.fund.service.BankAcctService;
import com.hengxin.platform.member.domain.AgencyApplicationView;
import com.hengxin.platform.member.domain.ProductProviderApplication;
import com.hengxin.platform.member.dto.MemberSearchDto;
import com.hengxin.platform.member.dto.downstream.ProductProviderApplicationDto;
import com.hengxin.platform.member.enums.EApplicationStatus;
import com.hengxin.platform.member.repository.AgencyApplicationRepository;
import com.hengxin.platform.member.repository.AgencyApplicationViewRepository;
import com.hengxin.platform.member.repository.AgencyRepository;
import com.hengxin.platform.member.repository.ProductProviderApplicationRepository;
import com.hengxin.platform.member.repository.ProductProviderRepository;

/**
 * Class Name: ProductProviderApplicationAuditService
 * 
 * @author Ryan
 * 
 */
@Service
public class ProductProviderService extends PagingService<ProductProviderApplication> {

    @Autowired
    ProductProviderApplicationRepository productProviderApplicationRepository;

    @Autowired
    ProductProviderRepository productProviderRepository;

    @Autowired
    AgencyApplicationRepository agencyApplicationRepository;

    @Autowired
    AgencyRepository agencyRepository;

    @Autowired
    private BankAcctService bankAcctService;

    @Autowired
    private AcctRepository acctRepository;

    @Autowired
    private AgencyApplicationViewRepository applicationViewRepository;

    /**
     * 
     * Description: get all application information for productProvider
     * 
     * @param audit
     */
    // FIXME Not allowed DTO returned from service
    @Transactional(readOnly = true)
    public DataTablesResponseDto<ProductProviderApplicationDto> getProductProviderApplicationMembers(
            final MemberSearchDto memberSearch) {
        Pageable pageRequest = PaginationUtil.buildPageRequest(memberSearch);

        Specification<AgencyApplicationView> spec = new Specification<AgencyApplicationView>() {

            @Override
            public Predicate toPredicate(Root<AgencyApplicationView> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                if (memberSearch != null) {
                    String userId = memberSearch.getUserId();
                    String orgName = memberSearch.getName();
                    EApplicationStatus auditStatus = memberSearch.getAuditStatus();
                    if (auditStatus == EApplicationStatus.PENDDING) {
                        expressions.add(cb.equal(root.<EApplicationStatus> get("productServiceStatus"),
                                EApplicationStatus.PENDDING));
                    } else if (auditStatus == EApplicationStatus.AUDITED) {
                        Predicate pred = cb.disjunction();
                        List<Expression<Boolean>> orExp = pred.getExpressions();
                        orExp.add(cb.equal(root.<EApplicationStatus> get("productServiceStatus"),
                                EApplicationStatus.ACCEPT));
                        orExp.add(cb.equal(root.<EApplicationStatus> get("productServiceStatus"),
                                EApplicationStatus.REJECT));
                        orExp.add(cb.equal(root.<EApplicationStatus> get("productServiceStatus"),
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

                }
                return predicate;
            }
        };

        Page<AgencyApplicationView> agencies = applicationViewRepository.findAll(spec, pageRequest);

        List<ProductProviderApplicationDto> productProviderApplications = new ArrayList<ProductProviderApplicationDto>();
        for (AgencyApplicationView agencyApplicationView : agencies.getContent()) {
            productProviderApplications.add(ConverterService.convert(agencyApplicationView,
                    ProductProviderApplicationDto.class));
        }
        DataTablesResponseDto<ProductProviderApplicationDto> result = new DataTablesResponseDto<ProductProviderApplicationDto>();
        result.setData(productProviderApplications);

        result.setTotalDisplayRecords(agencies.getTotalElements());
        result.setTotalRecords(agencies.getTotalElements());
        result.setEcho(memberSearch.getEcho());
        return result;
    }

    /**
     * 
     * @param userId
     * @return
     */
    public ProductProviderApplication getProductProviderApplication(String userId) {
        Pageable pageable = new PageRequest(0, 1, Direction.DESC, "lastMntTs");
        Page<ProductProviderApplication> productProviderList = productProviderApplicationRepository.findByUserId(
                userId, pageable);
        if (productProviderList.hasContent()) {
            return productProviderList.getContent().get(0);
        }
        return null;

    }

    /**
     * 
     * @param providerId
     * @return
     */
    public AgencyApplicationView getAgencyApplicationView(String providerId) {
        AgencyApplicationView agencyApplicationView = applicationViewRepository.findOne(providerId);
        return agencyApplicationView;
    }
}
