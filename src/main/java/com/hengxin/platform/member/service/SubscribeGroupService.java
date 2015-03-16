package com.hengxin.platform.member.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.domain.DynamicOption;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.enums.EOptionCategory;
import com.hengxin.platform.common.util.PaginationUtil;
import com.hengxin.platform.common.util.SystemDictUtil;
import com.hengxin.platform.fund.entity.AcctPo;
import com.hengxin.platform.member.domain.Agency;
import com.hengxin.platform.member.domain.InvestorInfo;
import com.hengxin.platform.member.domain.InvestorUserInfo;
import com.hengxin.platform.member.domain.SubscribeGroup;
import com.hengxin.platform.member.domain.SubscribeGroupUser;
import com.hengxin.platform.member.dto.GroupSearchDto;
import com.hengxin.platform.member.dto.InvestorSearchDto;
import com.hengxin.platform.member.dto.SkinnyUserDto;
import com.hengxin.platform.member.dto.SubscribeGroupDto;
import com.hengxin.platform.member.repository.InvestorInfoRepository;
import com.hengxin.platform.member.repository.InvestorUserInfoRepository;
import com.hengxin.platform.member.repository.SubscribeGroupRepository;
import com.hengxin.platform.security.entity.UserPo;

/**
 * Class Name: SubscribeGroupService Description:
 * 
 * @author junwei
 * 
 */

@Service
public class SubscribeGroupService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SubscribeGroupService.class);
	
    @Autowired
    private SubscribeGroupRepository subscribeGroupRepository;
    
    @Autowired
    private InvestorUserInfoRepository investorUserInfoRepository;
    
    @Autowired
    private InvestorInfoRepository investorInfoRepository;
    
    @Transactional
    public void saveGroup(SubscribeGroupDto group, String userId) {
    	LOGGER.info("saveGroup() invoked");
    	Date currentDate = new Date();
    	if (group.getGroupId() == null || group.getGroupId().intValue() == 0) {
    		SubscribeGroup entity = new SubscribeGroup();
    		entity.setCreateTs(currentDate);
    		entity.setCreator(userId);
    		entity.setLastMntTs(currentDate);
    		entity.setLastMntOpid(userId);
    		entity.setGroupName(group.getGroupName());
    		entity.setGroupDescribe(group.getGroupDescribe());
    		Set<String> investorIds = this.processDuplicateInvestors(group);
    		for (String investorId : investorIds) {
    			SubscribeGroupUser userEntity = new SubscribeGroupUser();
    			userEntity.setUserId(investorId);
    			userEntity.setCreateTs(currentDate);
        		userEntity.setCreator(userId);
        		userEntity.setLastMntTs(currentDate);
        		userEntity.setLastMntOpid(userId);
    			entity.addUser(userEntity);
			}
    		this.subscribeGroupRepository.save(entity);	
		} else {
			SubscribeGroup entity = this.subscribeGroupRepository.findOne(group.getGroupId());
			entity.setGroupName(group.getGroupName());
    		entity.setGroupDescribe(group.getGroupDescribe());
    		entity.setLastMntTs(currentDate);
    		entity.setLastMntOpid(userId);
    		entity.getUsers().clear();
    		this.subscribeGroupRepository.flush();
    		Set<String> investorIds = this.processDuplicateInvestors(group);
    		for (String investorId : investorIds) {
    			SubscribeGroupUser userEntity = new SubscribeGroupUser();
    			userEntity.setUserId(investorId);
    			userEntity.setCreateTs(currentDate);
        		userEntity.setCreator(userId);
        		userEntity.setLastMntTs(currentDate);
        		userEntity.setLastMntOpid(userId);
    			entity.addUser(userEntity);
			}
    		this.subscribeGroupRepository.save(entity);	
		}
    	LOGGER.debug("saveGroup() completed");
    }
    
    @Transactional
    public boolean updateGroupStatus(int groupId, boolean status) {
    	LOGGER.info("activeGroup() invoked");
    	this.subscribeGroupRepository.updateGroupStatus(groupId, status);
    	return true;
    }

    @Transactional
    public boolean deleteGroup(int groupId) {
    	LOGGER.info("deleteGroup() invoked");
    	int count = this.subscribeGroupRepository.countAIPGroup(groupId);
    	if (count > 0) {
			return false;
		}
    	this.subscribeGroupRepository.delete(groupId);
    	LOGGER.debug("deleteGroup() completed");
    	return true;
    }
    
    /**
     * remove duplicate investor id.
     * @return unique investor.
     */
    private Set<String> processDuplicateInvestors(SubscribeGroupDto group) {
    	Set<String> investors = new HashSet<String>();
    	for (SkinnyUserDto user : group.getUsers()) {
			investors.add(user.getUserId());
		}
    	List<String> serviceCenterIds = group.getServiceCenterIds();
    	if (!serviceCenterIds.isEmpty()) {
    		List<InvestorInfo> investorList = investorInfoRepository.findByServiceCenterIds(serviceCenterIds);
        	for (InvestorInfo investorUserInfo : investorList) {
    			investors.add(investorUserInfo.getUserId());
    		}
		}
    	LOGGER.debug("The final investor number is {}", investors.size());
    	return investors;
    }
    
    @Transactional(readOnly = true)
    public SubscribeGroupDto findGruopInfo(int groupId) {
    	LOGGER.info("findGruopInfo() invoked");
    	SubscribeGroup groupEntity = subscribeGroupRepository.findOne(groupId);
    	SubscribeGroupDto groupDTO = new SubscribeGroupDto();
    	groupDTO.setGroupId(groupEntity.getGroupId());
    	groupDTO.setGroupName(groupEntity.getGroupName());
    	groupDTO.setGroupDescribe(groupEntity.getGroupDescribe());
    	Map<String, String> levelMap = new HashMap<String, String>();
    	List<DynamicOption> levels = SystemDictUtil.getRootDictList(EOptionCategory.INVESTOR_LEVEL);
        for (DynamicOption dynamicOption : levels) {
         	levelMap.put(dynamicOption.getCode(), dynamicOption.getText());
 		}
    	for (SubscribeGroupUser userEntity : groupEntity.getUsers()) {
			SkinnyUserDto user = new SkinnyUserDto();
			user.setUserId(userEntity.getUserId());
			try {
    			user.setUserName(userEntity.getUser().getUser().getName());	
			} catch (Exception e) {
				e.printStackTrace();
				user.setUserName("");
			}
			try {
    			user.setAccountNo(userEntity.getUser().getAccount().getAcctNo());	
			} catch (Exception e) {
				LOGGER.error("The investor {} does not exist account, ex : {}", user.getUserId(), e);
				user.setAccountNo("");
			}
    		user.setLevel(levelMap.containsKey(userEntity.getUser().getInvestorLevel())
					? levelMap.get(userEntity.getUser().getInvestorLevel()) : userEntity.getUser().getInvestorLevel());
    		try {
    			this.processServiceCenterName(user, userEntity.getUser().getAuthorizedCenter());
        	} catch (EntityNotFoundException ex) {
        		LOGGER.error("Dirty Data : {}", ex);
    			user.setAuthorizedCenterName("");
    		}
			groupDTO.getUsers().add(user);
		}
    	LOGGER.info("findGruopInfo() completed");
    	return groupDTO;
    }

    public DataTablesResponseDto<SubscribeGroupDto> getGroupInfo(final GroupSearchDto searchDo) {
    	LOGGER.info("getGroupInfo() invoked");
        Pageable pageRequest = PaginationUtil.buildPageRequest(searchDo);
        Specification<SubscribeGroup> specification = new Specification<SubscribeGroup>() {
            @Override
            public Predicate toPredicate(Root<SubscribeGroup> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                if (searchDo != null) {
                    String groupName = searchDo.getGroupName();
                    if (StringUtils.isNotBlank(groupName)) {
                        expressions.add(cb.like(cb.lower(root.<String> get("groupName")), "%" + groupName.toLowerCase() + "%"));
                    }
                    String status = searchDo.getStatus();
                    if (StringUtils.isNotBlank(status)) {
                    	expressions.add(cb.equal(root.<String> get("status"), "Y".equals(status) ? true : false));
					}
                }
                return predicate;
            }
        };
        Page<SubscribeGroup> groups = subscribeGroupRepository.findAll(specification, pageRequest);
        DataTablesResponseDto<SubscribeGroupDto> groupDtos = new DataTablesResponseDto<SubscribeGroupDto>();
        List<SubscribeGroupDto> groupList = new ArrayList<SubscribeGroupDto>();
        groupDtos.setEcho(searchDo.getEcho());
        for (SubscribeGroup group : groups) {
        	SubscribeGroupDto groupDto = new SubscribeGroupDto();
        	groupDto.setGroupId(group.getGroupId());
        	groupDto.setGroupName(group.getGroupName());
        	groupDto.setGroupDescribe(group.getGroupDescribe());
        	groupDto.setActvie(group.getStatus());
            groupList.add(groupDto);
        }
        groupDtos.setData(groupList);
        groupDtos.setTotalDisplayRecords(groups.getTotalElements());
        groupDtos.setTotalRecords(groups.getTotalElements());
        LOGGER.debug("getGroupInfo() completed");
        return groupDtos;
    }
    
    /**
     * load investor name and level.
     * @param investorSearchDto
     * @return
     */
    @Transactional(readOnly = true)
    public DataTablesResponseDto<SkinnyUserDto> getInvestorInfos(final InvestorSearchDto investorSearchDto) {
    	LOGGER.info("getInvestorInfos() invoked");
    	DataTablesResponseDto<SkinnyUserDto> investors = new DataTablesResponseDto<SkinnyUserDto>();
    	/**
//    	CriteriaBuilder cb = em.getCriteriaBuilder();
//      CriteriaQuery cq = cb.createQuery();
//      Root<InvestorUserInfo> e = cq.from(InvestorUserInfo.class);
//      Fetch a = e.fetch("user", JoinType.INNER);
//      cq.select(e);
//      cq.where(cb.equal(e.get("investorLevel"), "02"));
//      Query query = em.createQuery(cq);
//      List<InvestorUserInfo> list = query.getResultList();
    	 */
        Pageable pageRequest = PaginationUtil.buildPageRequest(investorSearchDto);
        Specification<InvestorUserInfo> specification = new Specification<InvestorUserInfo>() {
            @Override
            public Predicate toPredicate(Root<InvestorUserInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                if (investorSearchDto != null) {
                	String serviceCenterName = investorSearchDto.getServiceCenterName();
                    if (StringUtils.isNotBlank(serviceCenterName)) {
                    	Join<InvestorUserInfo, Agency> scJoin = root.join("authorizedCenter");
                        expressions.add(cb.like(cb.lower(scJoin.<String> get("name")), "%" + serviceCenterName.toLowerCase() + "%"));
                    }
                    String investorName = investorSearchDto.getInvestorName();
                    if (StringUtils.isNotBlank(investorName)) {
                    	Join<InvestorUserInfo, UserPo> scJoin = root.join("user");
                        expressions.add(cb.like(cb.lower(scJoin.<String> get("name")), "%" + investorName.toLowerCase() + "%"));
                    }
                    String accountNo = investorSearchDto.getAccountNo();
                    if (StringUtils.isNotBlank(accountNo)) {
                    	Join<InvestorUserInfo, AcctPo> scJoin = root.join("account");
                        expressions.add(cb.like(cb.lower(scJoin.<String> get("acctNo")), "%" + accountNo.toLowerCase() + "%"));
                    }
                }
                return predicate;
            }
        };
        Page<InvestorUserInfo> list = investorUserInfoRepository.findAll(specification, pageRequest);
        
        List<SkinnyUserDto> groupList = new ArrayList<SkinnyUserDto>();
        investors.setEcho(investorSearchDto.getEcho());
        List<DynamicOption> levels = SystemDictUtil.getRootDictList(EOptionCategory.INVESTOR_LEVEL);
        Map<String, String> levelMap = new HashMap<String, String>();
        for (DynamicOption dynamicOption : levels) {
        	levelMap.put(dynamicOption.getCode(), dynamicOption.getText());
		}
        for(InvestorUserInfo info : list) {
        	SkinnyUserDto dto = new SkinnyUserDto();
        	dto.setUserId(info.getUserId());
        	try {
        		dto.setUserName(info.getUser().getName());
        	} catch (Exception e) {
				e.printStackTrace();
				dto.setUserName("");
			}
        	try {
        		dto.setAccountNo(info.getAccount().getAcctNo());
			} catch (Exception e) {
				LOGGER.error("The investor {} does not exist account, ex : {}", dto.getUserId(), e);
				dto.setAccountNo("");
			}
        	dto.setLevel(levelMap.containsKey(info.getInvestorLevel()) ? levelMap.get(info.getInvestorLevel()) : info.getInvestorLevel());
        	try {
        		this.processServiceCenterName(dto, info.getAuthorizedCenter());
        	} catch (EntityNotFoundException ex) {
        		LOGGER.error("Dirty Data : {}", ex);
        		dto.setAuthorizedCenterName("");
        	}
        	groupList.add(dto);
        }
        investors.setData(groupList);
        investors.setTotalDisplayRecords(list.getTotalElements());
        investors.setTotalRecords(list.getTotalElements());
        LOGGER.debug("getInvestorInfos() completed");
    	return investors;
    }

    private void processServiceCenterName(SkinnyUserDto dto, Agency serviceCenter) {
    	if (serviceCenter == null) {
    		dto.setAuthorizedCenterName("");
		} else {
			dto.setAuthorizedCenterName(serviceCenter.getName());
		}
    }
    
	/**
	 * Active group...
	 * @return
	 */
    public List<SubscribeGroupDto> getAllGroups() {
        List<SubscribeGroupDto> dtoList = new ArrayList<SubscribeGroupDto>();
        Iterable<SubscribeGroup> list = subscribeGroupRepository.findByActiveGroups(true);
        for(SubscribeGroup info : list) {
            SubscribeGroupDto dto = new SubscribeGroupDto();
            dto.setGroupId(info.getGroupId());
            dto.setGroupName(info.getGroupName());
            dto.setGroupDescribe(info.getGroupDescribe());
            dto.setGroupType(info.getGroupType());
            dtoList.add(dto);
        }
        return dtoList;
    }

    /**
     * 
     * @param currentUserId
     * @param groupName
     * @return true if exist.
     */
    @Transactional(readOnly = true)
	public boolean isExistingGroup(Integer groupId, String groupName) {
		LOGGER.info("isExistingGroup() invoked");
		if (groupId == null || groupId.intValue() == 0) {
			return this.subscribeGroupRepository.countGroupByGroupNameIgnoreCase(groupName.trim().toUpperCase()) == 0 ? false : true;
		} else {
			return this.subscribeGroupRepository.countGroupByGroupIdNotAndGroupNameIgnoreCase(groupId, groupName.trim().toUpperCase()) == 0 ? false : true;	
		}
	}
}
