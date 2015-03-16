package com.hengxin.platform.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hengxin.platform.common.domain.CurrentWorkDate;

public interface CurrentWorkDateRepository extends
		JpaRepository<CurrentWorkDate, String> {

}
