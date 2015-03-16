package com.hengxin.platform.fund.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hengxin.platform.fund.entity.PositionPo;

/**
 * InvestorTradeSumRepository.
 *
 */
@Repository
public interface InvestorTradeSumRepository extends JpaRepository<PositionPo, String>,
	JpaSpecificationExecutor<PositionPo> {
	

    /**
     * 会员已收到利息、罚金(本金、利息)、扣除债权转让费用、交易手续费等总收益.
     * @param userId
     * @return ('42','43','44','50','65')
     */
    @Query(value=" SELECT NVL(SUM(TRX_AMT),0.00) FROM (SELECT DECODE(X.PAY_RECV_FLG,'P', -1*X.TRX_AMT, X.TRX_AMT) AS TRX_AMT"
    		+ " FROM AC_TRX_JNL X, AC_ACCT T "
    		+ " WHERE X.USE_TYPE IN ('42','43','44','65') AND X.ACCT_NO = T.ACCT_NO AND T.ACCT_TYPE = '02' AND T.USER_ID = ?1)", nativeQuery = true)
    BigDecimal getInvsReceivedProfit(String userId);
    
    /**
     * 会员债权转让差价总和.
     * @param userId
     * @return
     */
    @Query(value="SELECT NVL(SUM(NVL(TRX_AMT,0.00)),0.00) FROM ("
            +" SELECT T1.BUYER_USER_ID AS USER_ID,T1.PKG_ID,(-1*T1.LOT_BUY_PRICE) AS TRX_AMT"
            +" FROM AC_PKG_TRADE_JNL T1 WHERE T1.BUYER_USER_ID = ?1 AND T1.TRD_TYPE = '01'"
            +" AND EXISTS(SELECT NULL FROM AC_PKG_TRADE_JNL T2 WHERE T2.CR_ID = T1.CR_ID AND T2.SELLER_USER_ID = T1.BUYER_USER_ID AND T2.PKG_ID = T1.PKG_ID)"
            +" UNION ALL SELECT A.USER_ID,A.PKG_ID,A.TRX_AMT FROM (SELECT T.USER_ID, X.TRX_AMT, SUBSTR(X.REL_BIZ_ID,0,INSTR(X.REL_BIZ_ID,'_')-1) AS PKG_ID FROM AC_TRX_JNL X, AC_ACCT T WHERE X.USE_TYPE IN ('41','43') AND X.ACCT_NO = T.ACCT_NO AND T.ACCT_TYPE = '02' AND T.USER_ID = ?1) A"
            +" WHERE EXISTS( SELECT NULL FROM AC_PKG_TRADE_JNL T1 WHERE T1.BUYER_USER_ID = A.USER_ID AND T1.PKG_ID = A.PKG_ID AND T1.TRD_TYPE = '01' AND EXISTS(SELECT NULL FROM AC_PKG_TRADE_JNL T2 WHERE T2.CR_ID = T1.CR_ID AND T2.SELLER_USER_ID = T1.BUYER_USER_ID AND T2.PKG_ID = T1.PKG_ID))"
            +" UNION ALL SELECT T3.SELLER_USER_ID AS USER_ID, T3.PKG_ID, T3.LOT_BUY_PRICE AS TRX_AMT FROM AC_PKG_TRADE_JNL T3 "
            +" WHERE T3.TRD_TYPE = '02' AND T3.SELLER_USER_ID = ?1)", nativeQuery = true)
    BigDecimal getInvsCrTrsfDiffAmt(String userId);
    
    /**
     * 会员待实现收益.
     * @param userId
     * @param feeRt
     * @return
     */
    @Query(value="SELECT TRUNC(NVL(SUM(NVL(EXPECT_RECVABLE_PROFIT,0.00)), 0.00),2) "
    		 + "    FROM ("
    		 + "       SELECT PKG_ID,USER_ID,ACCT_NO,FEE_DISCOUNT_RT,NVL(EXP_AMT,0.00) AS EXP_AMT,NVL(INVS_RCV_INTR_AMT,0.00) AS INVS_RCV_INTR_AMT,(NVL(INVS_RCV_INTR_AMT,0.00)-NVL(EXP_AMT,0.00)*NVL(FEE_DISCOUNT_RT,1)) AS EXPECT_RECVABLE_PROFIT "
    		 + "         FROM ("
    		 + "            SELECT PS.PKG_ID,PS.USER_ID,CT.ACCT_NO,UL.FEE_DISCOUNT_RT,DECODE(PX.PAY_FEE_FLG,'N', 0.00,(PX.RCV_INTR_AMT * PS.POS_AMT)*?2/PX.SUBS_AMT) AS EXP_AMT,(PX.RCV_INTR_AMT * PS.POS_AMT)/PX.SUBS_AMT AS INVS_RCV_INTR_AMT"
    		 + "              FROM (SELECT P.PKG_ID, P.UNIT*P.UNIT_FACE_VALUE AS POS_AMT, P.USER_ID FROM AC_POSITION P WHERE P.UNIT > 0) PS,"
    		 + "                   (SELECT SP.PKG_ID, KG.SUBS_AMT, KG.SIGNING_DT,DECODE(SIGN(KG.SIGNING_DT-TO_DATE('2014-05-01','yyyy-MM-dd')),-1,'N','Y') AS PAY_FEE_FLG,SP.INTR_PY_AMT,SP.INTR_PD_AMT,(SP.INTR_PY_AMT-SP.INTR_PD_AMT) AS RCV_INTR_AMT FROM FP_PKG_SCHD_PAY SP, FP_PKG KG WHERE SP.PKG_ID = KG.PKG_ID AND SP.PAY_STATUS IN ('01','02','03','04') AND (SP.INTR_PY_AMT-SP.INTR_PD_AMT) > 0.00) PX,"
    		 + "                   AC_ACCT CT,"
    		 + "                   (SELECT F.USER_ID, L.FEE_DISCOUNT_RT FROM UM_INVSTR_INFO F, UM_INVSTR_LVL L WHERE NVL(F.INVSTR_LVL_SW,'00') = L.LVL_ID) UL"
    		 + "             WHERE PS.PKG_ID = PX.PKG_ID AND PS.USER_ID = CT.USER_ID AND CT.USER_ID = UL.USER_ID AND PX.SUBS_AMT > 0.00 AND PS.USER_ID = ?1"
    		 + "   ))", nativeQuery = true)
    BigDecimal getUnReceivedProfit(String userId, BigDecimal deductRt);
    
    /**
     * 预计总收益（计算用）.
     * @param userId
     * @param feeRt
     * @return
     */
    @Query(value= "SELECT TRUNC(NVL(SUM(NVL(EXPECT_RECVABLE_PROFIT,0.00)*360/x3.term_days), 0.00),8) FROM ("
    		+ " SELECT PKG_ID, USER_ID, ACCT_NO, FEE_DISCOUNT_RT,  NVL(EXP_AMT,0.00) AS EXP_AMT,"
    		+ " NVL(INVS_RCV_INTR_AMT,0.00) AS INVS_RCV_INTR_AMT, (NVL(INVS_RCV_INTR_AMT,0.00)-NVL(EXP_AMT,0.00)*NVL(FEE_DISCOUNT_RT,1)) AS EXPECT_RECVABLE_PROFIT"
    		+ " FROM ( SELECT PS.PKG_ID, PS.USER_ID, CT.ACCT_NO, UL.FEE_DISCOUNT_RT,"
    		+ "  DECODE(PX.PAY_FEE_FLG,'N', 0.00, (PX.RCV_INTR_AMT * PS.POS_AMT)*?2/PX.SUBS_AMT) AS EXP_AMT,"
    		+ "  (PX.RCV_INTR_AMT * PS.POS_AMT)/PX.SUBS_AMT AS INVS_RCV_INTR_AMT"
    		//+ " FROM (SELECT P.PKG_ID, P.UNIT*P.UNIT_FACE_VALUE AS POS_AMT, P.USER_ID FROM AC_POSITION P WHERE P.UNIT > 0) PS,"
    		+ " FROM (SELECT P.PKG_ID, P.LOT_BUY_PRICE AS POS_AMT, P.BUYER_USER_ID AS USER_ID FROM AC_PKG_TRADE_JNL P WHERE P.BUYER_USER_ID = ?1) PS,"
    		+ " (SELECT SP.PKG_ID, KG.SUBS_AMT, DECODE(SIGN(KG.SIGNING_DT-TO_DATE('2014-05-01','yyyy-MM-dd')), -1,'N','Y') AS PAY_FEE_FLG, (SP.INTR_PY_AMT) AS RCV_INTR_AMT FROM FP_PKG_SCHD_PAY SP, FP_PKG KG "
    		+ " WHERE SP.PKG_ID = KG.PKG_ID AND (SP.INTR_PY_AMT) > 0.00 ) PX, AC_ACCT CT, "
    		+ " (SELECT F.USER_ID, L.FEE_DISCOUNT_RT FROM UM_INVSTR_INFO F, UM_INVSTR_LVL L WHERE NVL(F.INVSTR_LVL_SW,'00') = L.LVL_ID) UL "
    		+ " WHERE PS.PKG_ID = PX.PKG_ID AND PS.USER_ID = CT.USER_ID AND CT.USER_ID = UL.USER_ID AND PX.SUBS_AMT > 0.00 AND CT.USER_ID = ?1 "
    		+ " )) x1, fp_pkg x2, fp_prod x3 where x1.pkg_id = x2.pkg_id and x2.prod_id = x3.prod_id ", nativeQuery = true)
    BigDecimal getExpectTotalProfitForCal(String userId, BigDecimal deductRt);
    
    /**
     * 已实现收益(计算用).
     * @param userId
     * @return
     */
    @Query(value="SELECT TRUNC(NVL(SUM(NVL(TRX_AMT,0.00)*360/x3.term_days),0.00),2) AS RECVED_AMT FROM ( "
    		+ " SELECT DECODE(X.PAY_RECV_FLG,'P', -1*X.TRX_AMT, X.TRX_AMT) AS TRX_AMT, SUBSTR(X.REL_BIZ_ID,0,INSTR(X.REL_BIZ_ID,'_')-1) AS PKG_ID FROM AC_TRX_JNL X, AC_ACCT T "
    		+ " WHERE X.USE_TYPE IN ('42','43','44','50','65') AND X.ACCT_NO = T.ACCT_NO AND T.ACCT_TYPE = '02' AND T.USER_ID = ?1 ) x1,"
    		+ " fp_pkg x2, fp_prod x3 where x1.pkg_id = x2.pkg_id and x2.prod_id = x3.prod_id ", nativeQuery = true)
    BigDecimal getRealizedRecvProfitForCal(String userId);
    
    /**
     * 会员总投资金额.
     * @param userId
     * @return
     */
    @Query(value = "SELECT NVL(SUM(X.LOT_BUY_PRICE),0.00) FROM AC_PKG_TRADE_JNL X WHERE X.BUYER_USER_ID=?1", nativeQuery = true)
    BigDecimal getTotalInvestmentAmt(String userId);

}
