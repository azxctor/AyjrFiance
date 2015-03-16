package com.hengxin.platform.netting.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.hengxin.platform.netting.entity.RecvPayOrderPo;
import com.hengxin.platform.netting.enums.NettingStatusEnum;

@Repository
public interface RecvPayOrderRepository extends
	JpaRepository<RecvPayOrderPo, String>,
	JpaSpecificationExecutor<RecvPayOrderPo> {
	
	List<RecvPayOrderPo> findByTrxDateAndNettingStatusOrderByCreateTsAsc(Date trxDate, NettingStatusEnum status);

}
