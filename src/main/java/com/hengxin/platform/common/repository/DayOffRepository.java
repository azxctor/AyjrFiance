package com.hengxin.platform.common.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.hengxin.platform.common.entity.DayOffPo;

@Repository
public interface DayOffRepository extends PagingAndSortingRepository<DayOffPo, Date>,JpaSpecificationExecutor<DayOffPo> {

}
