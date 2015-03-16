package com.hengxin.platform.escrow.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.hengxin.platform.escrow.entity.EswCashOrderPo;

@Repository
public interface EswCashOrderRepository extends
	JpaRepository<EswCashOrderPo, String>,
	JpaSpecificationExecutor<EswCashOrderPo> {
	
	Page<EswCashOrderPo> findAll(Specification<EswCashOrderPo> spec, Pageable pageable);
}
