package com.hengxin.platform.escrow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.hengxin.platform.escrow.entity.EswReconEbcPo;

@Repository
public interface EswReconEbcRepository extends
		JpaRepository<EswReconEbcPo, String>,
		JpaSpecificationExecutor<EswReconEbcPo> {


}
