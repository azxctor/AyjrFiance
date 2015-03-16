package com.hengxin.platform.member.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hengxin.platform.member.domain.AutoSubscribeParam;

public interface AutoSubscribeParamRepository extends
		JpaRepository<AutoSubscribeParam, String>,
		JpaSpecificationExecutor<AutoSubscribeParam> {

	List<AutoSubscribeParam> findByActiveFlagAndTerminationFlag(@Param("activeFlag") String active, @Param("terminationFlag") String terminate);
	
	List<AutoSubscribeParam> findByUserIdAndActiveFlagAndTerminationFlag(@Param("userId") String userId, @Param("activeFlag") String active, @Param("terminationFlag") String terminate);

	@Modifying
	@Query("DELETE FROM AutoSubscribeParam WHERE userId = :userId")
	void deleteParam(@Param("userId") String userId);
	
}
