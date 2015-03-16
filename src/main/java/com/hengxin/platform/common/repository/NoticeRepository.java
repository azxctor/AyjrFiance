package com.hengxin.platform.common.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hengxin.platform.common.entity.Notice;

public interface NoticeRepository extends PagingAndSortingRepository<Notice, String>,JpaSpecificationExecutor<Notice>{
  
}
