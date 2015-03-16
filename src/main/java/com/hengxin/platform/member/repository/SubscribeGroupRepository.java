package com.hengxin.platform.member.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hengxin.platform.member.domain.SubscribeGroup;

/**
 * Class Name: SubscribeGroupRepository Description: TODO
 * 
 * @author junwei
 * 
 */
public interface SubscribeGroupRepository extends JpaRepository<SubscribeGroup, Integer>,
        JpaSpecificationExecutor<SubscribeGroup> {
    
	int countGroupByGroupIdNotAndGroupNameIgnoreCase(Integer groupId, String groupName);

	int countGroupByGroupNameIgnoreCase(String groupName);
	
	@Query("SELECT e FROM SubscribeGroup e WHERE e.status = :status")
	List<SubscribeGroup> findByActiveGroups(@Param("status") boolean status);
	
	@Query("SELECT COUNT(e) FROM PackageAIPGroup e WHERE e.group.groupId = :groupId")
	int countAIPGroup(@Param("groupId") Integer groupId);
	
	@Modifying
	@Query("UPDATE SubscribeGroup SET status = :status WHERE groupId = :groupId")
	void updateGroupStatus(@Param("groupId") Integer groupId, @Param("status") Boolean status);

}
