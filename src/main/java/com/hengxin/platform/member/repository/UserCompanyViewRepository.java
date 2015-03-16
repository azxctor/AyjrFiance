package com.hengxin.platform.member.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hengxin.platform.member.domain.UserCompanyView;

public interface UserCompanyViewRepository extends PagingAndSortingRepository<UserCompanyView, String> {

    @Query(value = "SELECT * FROM V_USER_COMPANY C WHERE C.USER_ID = ?1", nativeQuery = true)
    public UserCompanyView getUserCompany(String userId);
}