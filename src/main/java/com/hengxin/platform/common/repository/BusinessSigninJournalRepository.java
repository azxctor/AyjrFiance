package com.hengxin.platform.common.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.hengxin.platform.common.domain.BusinessSigninJournal;

/**
 * Class Name: BusinessSigninJournalRepository Description: TODO
 * 
 * @author junwei
 * 
 */

public interface BusinessSigninJournalRepository extends JpaRepository<BusinessSigninJournal, Integer>,
        JpaSpecificationExecutor<BusinessSigninJournal> {

    @Override
    public Page<BusinessSigninJournal> findAll(Specification<BusinessSigninJournal> spec, Pageable pageable);
}
