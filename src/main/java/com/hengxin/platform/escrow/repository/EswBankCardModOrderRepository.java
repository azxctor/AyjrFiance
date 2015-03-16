package com.hengxin.platform.escrow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.hengxin.platform.escrow.entity.EswBankCardModOrderPo;

@Repository
public interface EswBankCardModOrderRepository extends
	JpaRepository<EswBankCardModOrderPo, String>,
	JpaSpecificationExecutor<EswBankCardModOrderPo> {
	
	public EswBankCardModOrderPo findByUserId(String userId);

}
