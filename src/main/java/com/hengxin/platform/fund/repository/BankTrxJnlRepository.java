package com.hengxin.platform.fund.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.hengxin.platform.fund.entity.BankTrxJnlPo;
import com.hengxin.platform.fund.enums.EBnkTrxStatus;
import com.hengxin.platform.fund.enums.ERechargeWithdrawFlag;

@Repository
public interface BankTrxJnlRepository extends
		PagingAndSortingRepository<BankTrxJnlPo, String>,
		JpaSpecificationExecutor<BankTrxJnlPo> {

	BankTrxJnlPo findByJnlNo(String jnlNo);

	List<BankTrxJnlPo> findByRelBnkId(String relBnkId);

	BankTrxJnlPo findByJnlNoAndTrxStatus(String jnlNo, EBnkTrxStatus trxStatus);

	BankTrxJnlPo findByJnlNoAndTrxStatusAndRechargeWithdrawFlag(String jnlNo,
			EBnkTrxStatus trxStatus, ERechargeWithdrawFlag rechargeWithdrawFlag);
	
	@Query(value = "select count(*) from ac_bank_trx_jnl where cash_pool = '02' and pay_recv_flg = 'R' and status = 'N' and rvs_flg = 'N' and to_char(trx_dt,'yyyyMMdd') = ?1", nativeQuery = true)
	Integer getRechargeCount(String trxDt);
	
	@Query(value = "select count(*) from ac_bank_trx_jnl where cash_pool = '02' and pay_recv_flg = 'W' and status = 'N' and rvs_flg = 'N' and to_char(trx_dt,'yyyyMMdd') = ?1", nativeQuery = true)
	Integer getWithdrawalCount(String trxDt);
	
	@Query(value = "select sum(trx_amt) from ac_bank_trx_jnl where cash_pool = '02' and pay_recv_flg = 'R' and status = 'N' and rvs_flg = 'N' and to_char(trx_dt,'yyyyMMdd') = ?1", nativeQuery = true)
	BigDecimal getRechargeSumAmt(String trxDt);
	
	@Query(value = "select sum(trx_amt) from ac_bank_trx_jnl where cash_pool = '02' and pay_recv_flg = 'W' and status = 'N' and rvs_flg = 'N' and to_char(trx_dt,'yyyyMMdd') = ?1", nativeQuery = true)
	BigDecimal getWithdrawalSumAmt(String trxDt);
	
	@Query(value = "select x.accDate||'@'||x.pool||'@'||x.yestAmt||'@'||x.currRecv||'@'||x.currPay||'@'||x.currAmt from"
			+ " ("
			+ "select to_char(to_date(?1,'yyyy-MM-dd'),'yyyy-MM-dd') accDate,'招行普通' pool,(select nvl(sum(sach.bal),0) yestAmt from ac_sub_acct_hist sach where 1=1 and to_char(sach.work_dt,'yyyyMMdd') = ?2 and sach.cash_pool='01') yestAmt,(select nvl(sum(jnl.trx_amt),0) currRecv from ac_trx_jnl jnl where 1=1 and to_char(jnl.trx_dt,'yyyyMMdd') = ?1 and use_type <> '00' and jnl.pay_recv_flg = 'R' and jnl.cash_pool='01') currRecv,(select nvl(sum(jnl.trx_amt),0) currPay from ac_trx_jnl jnl where 1=1 and to_char(jnl.trx_dt,'yyyyMMdd') = ?1 and use_type <> '00' and jnl.pay_recv_flg = 'P' and jnl.cash_pool='01') currPay,(select nvl(sum(sach.bal),0) currAmt from ac_sub_acct_hist sach where 1=1 and to_char(sach.work_dt,'yyyyMMdd') = ?1 and sach.cash_pool='01') currAmt from dual "
			+ " union "
			+ "select to_char(to_date(?1,'yyyy-MM-dd'),'yyyy-MM-dd') accDate,'招行专用' pool,(select nvl(sum(sach.bal),0) yestAmt from ac_sub_acct_hist sach where 1=1 and to_char(sach.work_dt,'yyyyMMdd') = ?2 and sach.cash_pool='02') yestAmt,(select nvl(sum(jnl.trx_amt),0) currRecv from ac_trx_jnl jnl where 1=1 and to_char(jnl.trx_dt,'yyyyMMdd') = ?1 and use_type <> '00' and jnl.pay_recv_flg = 'R' and jnl.cash_pool='02') currRecv,(select nvl(sum(jnl.trx_amt),0) currPay from ac_trx_jnl jnl where 1=1 and to_char(jnl.trx_dt,'yyyyMMdd') = ?1 and use_type <> '00' and jnl.pay_recv_flg = 'P' and jnl.cash_pool='02') currPay,(select nvl(sum(sach.bal),0) currAmt from ac_sub_acct_hist sach where 1=1 and to_char(sach.work_dt,'yyyyMMdd') = ?1 and sach.cash_pool='02') currAmt from dual "
			+ " union "
			+ "select to_char(to_date(?1,'yyyy-MM-dd'),'yyyy-MM-dd') accDate,'工行普通' pool,(select nvl(sum(sach.bal),0) yestAmt from ac_sub_acct_hist sach where 1=1 and to_char(sach.work_dt,'yyyyMMdd') = ?2 and sach.cash_pool='03') yestAmt,(select nvl(sum(jnl.trx_amt),0) currRecv from ac_trx_jnl jnl where 1=1 and to_char(jnl.trx_dt,'yyyyMMdd') = ?1 and use_type <> '00' and jnl.pay_recv_flg = 'R' and jnl.cash_pool='03') currRecv,(select nvl(sum(jnl.trx_amt),0) currPay from ac_trx_jnl jnl where 1=1 and to_char(jnl.trx_dt,'yyyyMMdd') = ?1 and use_type <> '00' and jnl.pay_recv_flg = 'P' and jnl.cash_pool='03') currPay,(select nvl(sum(sach.bal),0) currAmt from ac_sub_acct_hist sach where 1=1 and to_char(sach.work_dt,'yyyyMMdd') = ?1 and sach.cash_pool='03') currAmt from dual "
			+ " ) x order by x.pool desc"
			, nativeQuery = true)
	List<String> getPool(String curr,String yest);
}
