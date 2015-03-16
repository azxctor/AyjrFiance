package com.hengxin.platform.fund.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.hengxin.platform.fund.entity.TransferTrxDtlView;
import com.hengxin.platform.fund.enums.EFlagType;

@Repository
public interface VTrsfTrxDtlRepository extends PagingAndSortingRepository<TransferTrxDtlView, String>,JpaSpecificationExecutor<TransferTrxDtlView> {

	
	@Query(value = "SELECT * FROM V_TRSF_TRX_DTL WHERE TO_CHAR(TRX_DT,'YYYYMMDD') = ?1  OR TX_DATE = ?1", nativeQuery = true)
    public List<TransferTrxDtlView> getTrsfTrxDtlDetails(String trxDt);
	
	public Page<TransferTrxDtlView> findBySignedFlgAndTrxDtBetweenOrTxDate(EFlagType singedFlg, Date trxSD, Date trxED, String txDate, Pageable pageable);
	
	@Query(value = "SELECT X.USER_ID, X.USER_NAME, X.ACCT_NO, X.SUB_ACCT_NO, X.SIGNED_FLG, X.RVS_FLG, X.STATUS, X.PAY_RECV_FLG, X.TRX_DT, X.TRX_AMT, X.REL_BNK_ID, X.BATCH_NO, X.BANK_ID, X.DEAL_ID, X.TRX_DATE, X.TRX_TIME, X.BNK_SERIAL, X.BNK_ACCT_NO, X.FUND_ACCT_NO, X.CUST_NAME, X.TRX_OPT, X.TRX_DIR, X.CUR_CODE, X.CUR_FLAG, X.BNK_TRX_AMT, ROWNUM AS ID " + 
	               "  FROM (SELECT A.USER_ID, A.USER_NAME, A.ACCT_NO, A.SUB_ACCT_NO, A.SIGNED_FLG, A.RVS_FLG, A.STATUS, A.PAY_RECV_FLG, A.TRX_DT, A.TRX_AMT, A.REL_BNK_ID, B.BATCH_NO, B.BANK_ID, B.DEAL_ID, B.TRX_DATE, B.TRX_TIME, B.BNK_SERIAL, B.BNK_ACCT_NO, B.FUND_ACCT_NO, B.CUST_NAME, B.TRX_OPT, B.TRX_DIR, B.CUR_CODE, B.CUR_FLAG, B.TRX_AMT AS BNK_TRX_AMT FROM AC_BANK_TRX_JNL A FULL JOIN AC_RECON_TRX_DTL B ON A.REL_BNK_ID = B.BNK_SERIAL ) X " + 
	               " WHERE X.SIGNED_FLG = 'Y' AND X.RVS_FLG = 'N' and x.status='N' AND to_char(X.trx_dt,'yyyyMMdd') = ?1 OR X.TRX_DATE = ?2 ", nativeQuery = true)
	public List<TransferTrxDtlView> getTransferTrxDtl(String trxDt, String txDt);
		
}