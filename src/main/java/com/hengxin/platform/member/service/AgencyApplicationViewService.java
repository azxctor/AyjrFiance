/*
 * Project Name: kmfex-platform
 * File Name: AgencyApplicationViewService.java
 * Class Name: AgencyApplicationViewService
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.util.PaginationUtil;
import com.hengxin.platform.member.domain.AgencyApplicationView;
import com.hengxin.platform.member.domain.UserCompanyView;
import com.hengxin.platform.member.dto.MemberSearchDto;
import com.hengxin.platform.member.dto.MemberViewDto;
import com.hengxin.platform.member.enums.EApplicationStatus;
import com.hengxin.platform.member.enums.EMemberType;
import com.hengxin.platform.member.repository.AgencyApplicationViewRepository;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.domain.User;

/**
 * Class Name: AgencyApplicationViewService
 * 
 * @author shengzhou
 * 
 */
@Service
public class AgencyApplicationViewService {

    @Autowired
    private AgencyApplicationViewRepository applicationViewRepository;
    
    @Autowired
	private SecurityContext securityContext;
    
    @Autowired
    private UserCompanyViewService userCompanyViewService;

    //FIXME Not allowed DTO returned from service
    public DataTablesResponseDto<MemberViewDto> getAgencies(final MemberSearchDto searchDo) {

        Pageable pageRequest = PaginationUtil.buildPageRequest(searchDo);

        Specification<AgencyApplicationView> specification = new Specification<AgencyApplicationView>() {
            @Override
            public Predicate toPredicate(Root<AgencyApplicationView> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();

                if (searchDo != null) {
                    String userName = searchDo.getUserName();
                    String name = searchDo.getName();
                    EApplicationStatus serviceCenterStatus = searchDo.getServiceCenterStatus();
                    EApplicationStatus productServiceStatus = searchDo.getProductServiceStatus();
                    EMemberType userRole = searchDo.getUserRole();

                    if (StringUtils.isNotBlank(userName)) {
                        expressions.add(cb.like(cb.lower(root.<String> get("userName")), "%" + userName.toLowerCase() + "%"));
                    }

                    if (StringUtils.isNotBlank(name)) {
                        expressions.add(cb.like(cb.lower(root.<String> get("name")), "%" + name.toLowerCase() + "%"));
                    }

                    if (serviceCenterStatus != null && serviceCenterStatus != EApplicationStatus.NULL) {
                        expressions.add(cb.equal(root.<EApplicationStatus> get("serviceCenterStatus"), serviceCenterStatus));
                    }

                    if (productServiceStatus != null && productServiceStatus != EApplicationStatus.NULL) {
                        expressions.add(cb.equal(root.<EApplicationStatus> get("productServiceStatus"), productServiceStatus));
                    }

                    if (userRole != null && userRole != EMemberType.NULL) {
                        expressions.add(cb.equal(root.<EMemberType> get("userRole"), userRole));
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
                }
                return predicate;
            }
        };

        Page<AgencyApplicationView> agencies = applicationViewRepository.findAll(specification, pageRequest);

        DataTablesResponseDto<MemberViewDto> memberView = new DataTablesResponseDto<MemberViewDto>();
        List<MemberViewDto> memberList = new ArrayList<MemberViewDto>();
        memberView.setEcho(searchDo.getEcho());
        for (AgencyApplicationView agencyApplicationViewPo : agencies) {
            memberList.add(ConverterService.convert(agencyApplicationViewPo, MemberViewDto.class));
        }
        memberView.setData(memberList);
        memberView.setTotalDisplayRecords(agencies.getTotalElements());
        memberView.setTotalRecords(agencies.getTotalElements());
        return memberView;
    }
    
}
