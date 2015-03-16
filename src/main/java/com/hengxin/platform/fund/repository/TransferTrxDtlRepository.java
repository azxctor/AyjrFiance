package com.hengxin.platform.fund.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.hengxin.platform.fund.entity.ReconTrxDtlPo;

@Repository
public interface TransferTrxDtlRepository extends
		PagingAndSortingRepository<ReconTrxDtlPo, String>,
		JpaSpecificationExecutor<ReconTrxDtlPo> {

    @Query(value = "select count(*) from ac_recon_trx_dtl where trx_dir = '1' and trx_date = ?1", nativeQuery = true)
    Integer getRechargeCount(String trxDt);
    
    @Query(value = "select count(*) from ac_recon_trx_dtl where trx_dir = '2' and trx_date = ?1", nativeQuery = true)
    Integer getWithdrawalCount(String trxDt);
    
    @Query(value = "select sum(trx_amt) from ac_recon_trx_dtl where trx_dir = '1' and trx_date = ?1", nativeQuery = true)
    BigDecimal getRechargeSumAmt(String trxDt);
    
    @Query(value = "select sum(trx_amt) from ac_recon_trx_dtl where trx_dir = '2' and trx_date = ?1", nativeQuery = true)
    BigDecimal getWithdrawalSumAmt(String trxDt);
	
}
