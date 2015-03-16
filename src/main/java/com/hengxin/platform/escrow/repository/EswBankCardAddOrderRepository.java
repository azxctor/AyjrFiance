package com.hengxin.platform.escrow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.hengxin.platform.escrow.entity.EswBankCardAddOrderPo;
import com.hengxin.platform.escrow.enums.EOrderStatusEnum;

@Repository
public interface EswBankCardAddOrderRepository extends
	JpaRepository<EswBankCardAddOrderPo, String>,
	JpaSpecificationExecutor<EswBankCardAddOrderPo> {

	public EswBankCardAddOrderPo findByUserId(String userId);
	
	public List<EswBankCardAddOrderPo> findByUserIdAndStatusOrderByCreateTsDesc(
			String userId, EOrderStatusEnum status);
	
	public List<EswBankCardAddOrderPo> findByUserIdAndBankCardNoAndStatusOrderByCreateTsDesc(
			String userId, String bankCardNo, EOrderStatusEnum status);
}
