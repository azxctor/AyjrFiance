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
import com.hengxin.platform.member.domain.MemberLessView;
import com.hengxin.platform.member.dto.MemberSearchDto;
import com.hengxin.platform.member.dto.MemberViewDto;
import com.hengxin.platform.member.enums.EMemberType;
import com.hengxin.platform.member.repository.MemberLessViewRepository;
import com.hengxin.platform.security.SecurityContext;

/**
 * Class Name: MemberLessViewService
 *
 * @author ycc
 *
 */
@Service
public class MemberLessViewService {

    @Autowired
    private MemberLessViewRepository lessViewRepository;

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
     * 
     * 
     * @param searchDo
     * @return
     */
	public DataTablesResponseDto<MemberViewDto> getMembersForPasswordReset(final MemberSearchDto searchDo) {
		//final String currentUserId = StringUtils.isNotBlank(securityContext.getCurrentOwnerId()) ? securityContext.getCurrentOwnerId() : securityContext.getCurrentUserId();
		//final boolean isServiceCenter = this.securityContext.isAuthServiceCenter();
		final boolean isCustomerServiceUser = this.securityContext.isCustomerServiceUser();//客服
		final boolean isChannelDeptUser = this.securityContext.isChannelDeptUser();//渠道
		final boolean isProdDeptUser = this.securityContext.isProdDeptUser();//产品部
		final boolean isSystemAdmin = this.securityContext.isSystemAdmin();
		
		Pageable pageRequest = buildPageRequest(searchDo);

		Specification<MemberLessView> specification = new Specification<MemberLessView>() {
			@Override
			public Predicate toPredicate(Root<MemberLessView> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				List<Expression<Boolean>> expressions = predicate.getExpressions();
				if (searchDo != null) {
					String userName = searchDo.getUserName();
					String name = searchDo.getName();
					String transno = searchDo.getTransno();
					
					if(isCustomerServiceUser){
						Expression<Boolean> expression1 = cb.equal(root.<EMemberType> get("userRole"), EMemberType.INVESTOR);
						Expression<Boolean> expression2 = cb.equal(root.<EMemberType> get("userRole"), EMemberType.FINANCER);
						Expression<Boolean> expression3 = cb.equal(root.<EMemberType> get("userRole"), EMemberType.INVESTOR_FINANCER);
						Expression<Boolean> expression4 = cb.equal(root.<EMemberType> get("userRole"), EMemberType.FINANCER_INVESTOR);
						Expression<Boolean> expression5 = cb.or(expression1,expression2);
						Expression<Boolean> expression6 = cb.or(expression3,expression4);
						expressions.add(cb.or(expression5,expression6));
					}else if(isChannelDeptUser){
						Expression<Boolean> expression1 = cb.equal(root.<EMemberType> get("userRole"), EMemberType.AUTHZDCENTER);
						Expression<Boolean> expression2 = cb.equal(root.<EMemberType> get("userRole"), EMemberType.AUTHZD_PRODSERV);
						Expression<Boolean> expression3 = cb.or(expression1,expression2);
						expressions.add(cb.or(expression3,cb.equal(root.<EMemberType> get("userRole"), EMemberType.PRODSERV_AUTHZD)));
					}else if(isProdDeptUser){
						Expression<Boolean> expression1 = cb.equal(root.<EMemberType> get("userRole"), EMemberType.PRODUCTSERVICE);
						Expression<Boolean> expression2 = cb.equal(root.<EMemberType> get("userRole"), EMemberType.AUTHZD_PRODSERV);
						Expression<Boolean> expression3 = cb.or(expression1,expression2);
						expressions.add(cb.or(expression3,cb.equal(root.<EMemberType> get("userRole"), EMemberType.PRODSERV_AUTHZD)));
					}else if(isSystemAdmin){
						
					}else{
						expressions.add(cb.equal(cb.lower(root.<String> get("accountNo")), "unaccessable"));
					}

					if (StringUtils.isNotBlank(userName)) {
						expressions.add(cb.like(cb.lower(root.<String> get("userName")), "%" + userName.toLowerCase() + "%"));
					}

					if (StringUtils.isNotBlank(name)) {
						expressions.add(cb.like(cb.lower(root.<String> get("name")), "%" + name.toLowerCase() + "%"));
					}

					if (StringUtils.isNotBlank(transno)) {
						expressions.add(cb.like(cb.lower(root.<String> get("accountNo")), "%" + transno.toLowerCase() + "%"));
					}
				}
				return predicate;
			}
		};

		Page<MemberLessView> members = lessViewRepository.findAll(specification, pageRequest);

		DataTablesResponseDto<MemberViewDto> memberView = new DataTablesResponseDto<MemberViewDto>();
		List<MemberViewDto> memberList = new ArrayList<MemberViewDto>();
		memberView.setEcho(searchDo.getEcho());
		for (MemberLessView memberApplicationViewPo : members) {
			memberList.add(ConverterService.convert(memberApplicationViewPo, MemberViewDto.class));
		}
		memberView.setData(memberList);
		memberView.setTotalDisplayRecords(members.getTotalElements());
		memberView.setTotalRecords(members.getTotalElements());
		return memberView;
	}
    
}
