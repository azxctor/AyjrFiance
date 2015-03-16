package com.hengxin.platform.member.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.hengxin.platform.member.domain.AgencyApplication;
import com.hengxin.platform.member.enums.EApplicationStatus;

public interface AgencyApplicationRepository extends
	PagingAndSortingRepository<AgencyApplication, String>,
	JpaSpecificationExecutor<AgencyApplication> {

	Page<AgencyApplication> findByUserId(String userId, Pageable pageable);
	
    Page<AgencyApplication> findByUserIdAndStatusNot(String userId, EApplicationStatus status, Pageable pageable);

    @Query("SELECT COUNT(E) FROM AgencyApplication E WHERE E.userId <> :userId AND LOWER(E.orgNo) = :orgNumber AND (E.status = :primeS OR E.status = :minorS)")
	int countByUserIdNotAndOrgNumberIgnoreCaseAndStatusOrStatus(@Param("userId") String userId, @Param("orgNumber") String orgNumber, @Param("primeS") EApplicationStatus primeStatus, @Param("minorS") EApplicationStatus minorStatus);

	@Query("SELECT COUNT(E) FROM AgencyApplication E WHERE LOWER(E.orgNo) = :orgNumber AND (E.status = :primeS OR E.status = :minorS)")
	int countByOrgNumberIgnoreCaseAndStatusOrStatus(@Param("orgNumber") String orgNumber, @Param("primeS") EApplicationStatus primeStatus, @Param("minorS") EApplicationStatus minorStatus);
    
}
