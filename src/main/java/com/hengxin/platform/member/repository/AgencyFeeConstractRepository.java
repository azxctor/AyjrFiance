package com.hengxin.platform.member.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.hengxin.platform.member.domain.AgencyFeeConstractPo;

public interface AgencyFeeConstractRepository extends JpaRepository<AgencyFeeConstractPo, String>,
		JpaSpecificationExecutor<AgencyFeeConstractPo> {

	Page<AgencyFeeConstractPo> findAll(Specification<AgencyFeeConstractPo> spec, Pageable pageable);

}