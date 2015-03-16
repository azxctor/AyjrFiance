/*
 * Project Name: kmfex-platform-trunk
 * File Name: AuthorizationService.java
 * Class Name: AuthorizationService
 *
 * Copyright 2014 Hengtian Software Inc
 *
 * Licensed under the Hengtiansoft
 *
 * http://www.hengtiansoft.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hengxin.platform.common.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.dto.NoticeDto;
import com.hengxin.platform.common.entity.Notice;
import com.hengxin.platform.common.repository.NoticeRepository;
import com.hengxin.platform.common.util.PaginationUtil;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.member.domain.UserRole;
import com.hengxin.platform.member.dto.MessageCriteria;
import com.hengxin.platform.member.repository.UserRepository;
import com.hengxin.platform.member.repository.UserRoleRepository;
import com.hengxin.platform.product.ProductUtil;
import com.hengxin.platform.security.repository.RoleRepository;
 
@Service
@Transactional
public class NoticeServiceImpl implements NoticeService {


	@Autowired
	private NoticeRepository noticeRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private RoleRepository roleRepository;
    
    @Transactional
    public Notice findOne(String id) {
    	return noticeRepository.findOne(id); 
    }
    
	public Predicate toPredicateRole(Root<Notice> root,CriteriaQuery<?> query, CriteriaBuilder builder,List<UserRole> urs) {
 		List<Predicate> predicates = new ArrayList<Predicate>(); 
		for(UserRole role:urs){ 
			predicates.add(builder.like(root.<String> get("showfilter"), "%," + role.getRoleId() + ",%"));
		}
		// 将所有条件用OR 联合起来
		if (predicates.size() > 0) {
			return builder.or(predicates.toArray(new Predicate[predicates.size()]));
		} 
		return builder.conjunction();
	}
	 
    public List<NoticeDto> getAllNotices(final String userId) { 
    	Specification<Notice> specification = new Specification<Notice>() { 
            @Override
            public Predicate toPredicate(Root<Notice> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
    			 
            	try {
					List<UserRole> urs=userRoleRepository.findByUserId(userId);
					 
					
					List<Predicate> list = new ArrayList<Predicate>();
					
					for(UserRole role:urs){  
					   list.add(cb.like(root.<String> get("showfilter"), "%," + role.getRoleId() + ",%"));
 					} 
					list.add(cb.isNull(root.get("showfilter")));
					Long now=DateUtils.date2yyyyMMddLong(new Date());
 					Predicate p1=cb.lessThanOrEqualTo(root.<Long> get("startTime"),now);
					Predicate p2=cb.greaterThanOrEqualTo(root.<Long> get("endTime"),now); 
          
					//添加排序的功能  
					query.orderBy(cb.desc(root.get("startTime").as(Long.class)));  
					
					Predicate[] p = new Predicate[list.size()];
					Predicate p3=cb.or(list.toArray(p)); 
					query.where(cb.and(p3,cb.and(p1,p2)));  
				} catch (Exception e) { 
					e.printStackTrace();
				} 
            	return query.getRestriction();
            }
        };
 
        List<Notice> reDatas = noticeRepository.findAll(specification);  
        
        List<NoticeDto> dtos = new ArrayList<NoticeDto>();
        for (Notice po : reDatas) {
        	NoticeDto dto = new NoticeDto(); 
        	dto.setContent(po.getContent());
        	dto.setTitle(po.getTitle());
        	dto.setNoticeId(po.getNoticeId());
        	dto.setShowfilter(po.getShowfilter());
        	dto.setType(po.getNoticeType().getText());
        	dto.setCreateTs(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        	dtos.add(dto);
        }
        return dtos; 
  
    } 
    
    @Transactional(readOnly = true)
    public DataTablesResponseDto<NoticeDto> getNotices(String userId) {
        MessageCriteria searchCriteria = new MessageCriteria();
        searchCriteria.setUserId(userId); 
        List<Integer> list = new ArrayList<Integer>();
        list.add(0);
        List<String> lis = new ArrayList<String>();
        lis.add("createTs");
        List<String> der = new ArrayList<String>();
        der.add("desc");
        searchCriteria.setSortedColumns(list);
        searchCriteria.setSortDirections(der);
        searchCriteria.setDataProp(lis);
        searchCriteria.setDisplayStart(1);
        searchCriteria.setDisplayLength(10); 
        searchCriteria.setReadable(true);
        return getNotices(searchCriteria); 
    }
    @Transactional(readOnly = true)
    public DataTablesResponseDto<NoticeDto> getNoticeDias(String userId,String showMethod,int length) {
    	MessageCriteria searchCriteria = new MessageCriteria();
    	searchCriteria.setUserId(userId); 
    	List<Integer> list = new ArrayList<Integer>();
    	list.add(0);
    	List<String> lis = new ArrayList<String>();
    	lis.add("createTs");
    	List<String> der = new ArrayList<String>();
    	der.add("desc");
    	searchCriteria.setSortedColumns(list);
    	searchCriteria.setSortDirections(der);
    	searchCriteria.setDataProp(lis);
    	searchCriteria.setDisplayStart(1);
    	searchCriteria.setDisplayLength(length); 
    	searchCriteria.setReadable(true);
    	searchCriteria.setShowMethod(showMethod);
    	return getNotices(searchCriteria); 
    }
    @Transactional(readOnly = true)
    public DataTablesResponseDto<NoticeDto> getNotices(final MessageCriteria searchCriteria) {
         Pageable pageRequest = PaginationUtil.buildPageRequest(searchCriteria);
         Specification<Notice> specification = new Specification<Notice>() {
            @Override
            public Predicate toPredicate(Root<Notice> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                  if (searchCriteria != null) { 
      
                	if(null==searchCriteria.getShowMethod()||"".equals(searchCriteria.getShowMethod())){
                		searchCriteria.setShowMethod("C");
                	}
   					List<UserRole> urs=userRoleRepository.findByUserId(searchCriteria.getUserId());
 					List<Predicate> list = new ArrayList<Predicate>();
 					for(UserRole role:urs){  
					   list.add(cb.like(root.<String> get("showfilter"), "%," + role.getRoleId() + ",%"));
 					} 
 					list.add(cb.isNull(root.get("showfilter")));
					Long now=DateUtils.date2yyyyMMddLong(new Date());
 					Predicate p1=cb.lessThanOrEqualTo(root.<Long> get("startTime"),now);
					Predicate p2=cb.greaterThanOrEqualTo(root.<Long> get("endTime"),now);
					Predicate p3=cb.equal(root.<String> get("showMethod"),searchCriteria.getShowMethod());
					Predicate p5=null;
					if (StringUtils.isNotBlank(searchCriteria.getKeyword())) {
					    p5=cb.or(cb.like(
								cb.lower(root.<String> get("content")), "%"
										+ searchCriteria.getKeyword().trim()
												.toLowerCase() + "%"), cb.like(
								cb.lower(root.<String> get("title")), "%"
										+ searchCriteria.getKeyword().trim()
												.toLowerCase() + "%"));
					}
					//添加排序的功能  .
					query.orderBy(cb.desc(root.get("startTime").as(Long.class)));  
					
					Predicate[] p = new Predicate[list.size()];
					Predicate p4=cb.or(list.toArray(p)); 
					if(null!=p5){
						query.where(cb.and(p4,cb.and(p1,p2,p3),p5));  
					}else{
						query.where(cb.and(p4,cb.and(p1,p2,p3)));
					}
					
                }
                return query.getRestriction();
            }
        };
        Page<Notice> messages = noticeRepository.findAll(specification, pageRequest);
        DataTablesResponseDto<NoticeDto> msgDtos = new DataTablesResponseDto<NoticeDto>();
        List<NoticeDto> msgList = new ArrayList<NoticeDto>();
        msgDtos.setEcho(searchCriteria.getEcho());
        for (Notice n : messages) {
        	NoticeDto msgDto = new NoticeDto();
            msgDto.setNoticeId(n.getNoticeId());
            msgDto.setType(n.getNoticeType().getCode());
            msgDto.setTitle(n.getTitle());
            msgDto.setContent(n.getContent()); 
            msgDto.setCreateTs(ProductUtil.formatDate(n.getCreateTs(),"yyyy-MM-dd HH:mm:ss"));
            msgList.add(msgDto);
        }
        msgDtos.setData(msgList);
        msgDtos.setTotalDisplayRecords(messages.getTotalElements());
        msgDtos.setTotalRecords(messages.getTotalElements()); 
        return msgDtos;
    }
 

}
