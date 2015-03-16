package com.hengxin.platform.fund.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hengxin.platform.fund.entity.PositionPo;

/**
 * 头寸. 
 *
 */
@Repository
public interface PositionRepository extends JpaRepository<PositionPo, String>,
        JpaSpecificationExecutor<PositionPo> {

    /**
     * 通过会员编号和融资包编号查询综合账户信息
     * 
     * @param userId
     * @param pkgId
     * @return PositionPo
     */
    PositionPo findPositionByUserIdAndPkgId(String userId, String pkgId);

    @Query(value = "SELECT SUM(ROUND(B.INTR_PY_AMT*A.UNIT/C.UNIT,4)) FROM AC_POSITION A,FP_PKG_SCHD_PAY B,FP_PKG C WHERE A.USER_ID = ?1 AND A.UNIT != 0 AND A.PKG_ID = B.PKG_ID AND A.PKG_ID = C.PKG_ID", nativeQuery = true)
    BigDecimal getProspectiveEarnings(String userId);

    @Query(value = "SELECT SUM(ROUND(B.INTR_PY_AMT*A.UNIT/C.UNIT,4)) FROM AC_POSITION A,FP_PKG_SCHD_PAY B,FP_PKG C WHERE A.USER_ID = ?1 AND A.UNIT != 0 AND A.PKG_ID = B.PKG_ID AND B.PAY_STATUS = ?2  AND A.PKG_ID = C.PKG_ID", nativeQuery = true)
    BigDecimal getUnCollecteEarnings(String userId, String payStatus);

    @Query(value = "SELECT SUM(ROUND((B.PRIN_PY_AMT+B.PRIN_FINE_PY_AMT+B.INTR_FINE_PY_AMT+B.CMPNS_INTR_AMT)*A.UNIT/C.UNIT,4)) FROM AC_POSITION A,FP_PKG_SCHD_PAY B,FP_PKG C WHERE A.USER_ID = ?1 AND A.UNIT != 0 AND A.PKG_ID = B.PKG_ID AND B.PAY_STATUS != ?2  AND A.PKG_ID = C.PKG_ID", nativeQuery = true)
    BigDecimal getCollecteEarnings(String userId, String payStatus);

    @Query(value = "SELECT SUM(ROUND(B.PRIN_PY_AMT*A.UNIT/C.UNIT,4)) FROM AC_POSITION A,FP_PKG_SCHD_PAY B,FP_PKG C WHERE A.USER_ID = ?1 AND A.UNIT != 0 AND A.PKG_ID = B.PKG_ID AND B.PAY_STATUS = ?2  AND A.PKG_ID = C.PKG_ID", nativeQuery = true)
    BigDecimal getUnCollecteAmt(String userId, String payStatus);

    List<PositionPo> getPositionByPkgId(String pkgId);

    @Query(value = "select distinct a.userId from PositionPo a where a.pkgId=?1", nativeQuery = false)
    List<String> getUsersByPkgId(String pkgId);
    
    @Query(value = "select count(a.userId) from PositionPo a where a.pkgId=?1", nativeQuery = false)
    int getUsersCountByPkgId(String pkgId);
    
    @Query(value= "select round(nvl(sum(receivable_prin_amt),0.00),2) as total_amt "
    		      + "from ("
			      + "     select ps.pkg_id, ps.user_id," 
				  + "            (px.recv_prin_amt * ps.pos_amt)/px.subs_amt as receivable_prin_amt "
				  + "       from (select p.pkg_id, "
				  + "                    p.unit*p.unit_face_value as pos_amt, "
				  + "                    p.user_id "
			      + "               from ac_position p "
			      + "              where user_id = ?1 and unit > 0 ) ps,"
				  + "             (select sp.pkg_id, "
				  + "   				  kg.subs_amt,"
				  + "					  (sp.prin_py_amt-sp.prin_pd_amt) as recv_prin_amt "
				  + "    			 from fp_pkg_schd_pay sp, fp_pkg kg "
				  + "				where sp.pkg_id = kg.pkg_id "
				  + "     			  and sp.pay_status in ('01','02','03') "
				  + "				  and sp.prin_py_amt > 0.00 "
				  + "              ) px where ps.pkg_id = px.pkg_id "
				  + "                     and px.subs_amt > 0.00"
				  + ")", nativeQuery = true)
    BigDecimal getUserReceivableTotalPrincipalByUserId(String userId);
}
