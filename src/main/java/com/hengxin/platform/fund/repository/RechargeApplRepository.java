package com.hengxin.platform.fund.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.hengxin.platform.fund.entity.RechargeApplPo;

@Repository
public interface RechargeApplRepository extends PagingAndSortingRepository<RechargeApplPo, String>,
	JpaSpecificationExecutor<RechargeApplPo> {
	
	RechargeApplPo getByApplNo(String applNo);
	
	RechargeApplPo findByRelBnkJnlNo(String relBnkJnlNo);
	
	@Query(value = "select count(1) from ac_recharge_appl where voucher_no = ?1", nativeQuery = true)
	Integer existsRecordByVoucherNo(String voucherNo);
	
}
