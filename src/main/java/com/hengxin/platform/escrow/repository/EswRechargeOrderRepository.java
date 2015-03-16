package com.hengxin.platform.escrow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.hengxin.platform.escrow.entity.EswRechargeOrderPo;

@Repository
public interface EswRechargeOrderRepository extends
	JpaRepository<EswRechargeOrderPo, String>,
	JpaSpecificationExecutor<EswRechargeOrderPo> {

	EswRechargeOrderPo findByOrderId(String orderId);
	

}
