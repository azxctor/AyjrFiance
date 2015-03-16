package com.hengxin.platform.risk.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.hengxin.platform.risk.domain.InvestQuestionEvalPo;

@Repository
public interface InvestRiskStatisticsRepository extends
	PagingAndSortingRepository<InvestQuestionEvalPo, String>,
	JpaSpecificationExecutor<InvestQuestionEvalPo> {
	
	@Query(value="select (nvl(t_a,0)+nvl(t_b,0)) as ct from ("
			   + "  select "
			   + "  (select count(1) from fp_pkg_subs x where x.user_id = ?1) as t_a,"
			   + "  (select count(1) from ac_pkg_trade_jnl x where x.trd_type = '02' and x.buyer_user_id = ?1) as t_b"
			   + "  from dual)", nativeQuery = true)
	public Integer getUserInvestCount(String userId);
	
	@Query(value="select (nvl(t_prin,0.00)+nvl(t_bal,0.00)) as total_assets from ("
			   + " select "
			   + " (select round(nvl(sum(receivable_prin_amt),0.00),2) from ("
			   + "  select ps.pkg_id, ps.user_id,(px.recv_prin_amt * ps.pos_amt)/px.subs_amt as receivable_prin_amt"
			   + "   from (select p.pkg_id, p.unit*p.unit_face_value as pos_amt, p.user_id from ac_position p where user_id = ?1 and unit > 0 ) ps,"
			   + "        (select sp.pkg_id, kg.subs_amt, (sp.prin_py_amt-sp.prin_pd_amt) as recv_prin_amt from fp_pkg_schd_pay sp, fp_pkg kg"
			   + " 			where sp.pkg_id = kg.pkg_id and sp.pay_status in ('01','02','03') and sp.prin_py_amt > 0.00) px where ps.pkg_id = px.pkg_id and px.subs_amt > 0.00"
			   + "   )) as t_prin,"
			   + " (select nvl(sum(x1.bal),0.00) from ac_sub_acct x1,ac_acct x2 where x1.acct_no = x2.acct_no and x2.user_id = ?1) as t_bal "
			   + " from dual)", nativeQuery = true)
	public BigDecimal getUserTotalAssets(String userId);
	
//	@Query(value = "select sum(px.recv_prin_amt * ps.pos_amt/px.subs_amt) as receivable_prin_amt"
//			   + "  from (select p.pkg_id, p.unit*p.unit_face_value as pos_amt, p.user_id from ac_position p where user_id = ?1 and unit > 0 "
//			   + "			and pkg_id = ?2 ) ps, "
//			   + "       (select sp.pkg_id, kg.subs_amt, (sp.prin_py_amt-sp.prin_pd_amt) as recv_prin_amt from fp_pkg_schd_pay sp, fp_pkg kg "
//			   + " 			where sp.pkg_id = kg.pkg_id and sp.pay_status in ('01','02','03') and sp.prin_py_amt > 0.00 and kg.pkg_id = ?2) px "
//			   + " 			where ps.pkg_id = px.pkg_id and px.subs_amt > 0.00",
//			   nativeQuery = true)
//	public BigDecimal getUserCreditorAssetsByPackagetId(String userId, String packageId);
	
	@Query(value = "select sum(px.recv_prin_amt * ps.pos_amt/px.subs_amt) as receivable_prin_amt"
			   + "  from (select p.pkg_id, p.unit*p.unit_face_value as pos_amt, p.user_id from ac_position p where user_id = ?1 and unit > 0 "
			   + "			and pkg_id in (SELECT PKG_ID FROM FP_PKG WHERE PROD_ID = ?2) ) ps, "
			   + "       (select sp.pkg_id, kg.subs_amt, (sp.prin_py_amt-sp.prin_pd_amt) as recv_prin_amt from fp_pkg_schd_pay sp, fp_pkg kg "
			   + " 			where sp.pkg_id = kg.pkg_id and sp.pay_status in ('01','02','03') and sp.prin_py_amt > 0.00 and sp.prod_id = ?2) px "
			   + " 			where ps.pkg_id = px.pkg_id and px.subs_amt > 0.00",
			   nativeQuery = true)
	public BigDecimal getUserCreditorAssetsByProductId(String userId, String prodId);

	@Query(value = "select sum(px.recv_prin_amt * ps.pos_amt/px.subs_amt) as receivable_prin_amt "
			+ " from (select p.pkg_id, p.unit*p.unit_face_value as pos_amt, p.user_id from ac_position p where user_id = ?1 and unit > 0 "
			+ " and pkg_id in (SELECT PKG_ID FROM FP_PKG WHERE PROD_ID IN (SELECT PROD_ID FROM FP_PROD WHERE PROD_LVL = ?2)) ) ps, "
			+ " (select sp.pkg_id, kg.subs_amt, (sp.prin_py_amt-sp.prin_pd_amt) as recv_prin_amt from fp_pkg_schd_pay sp, fp_pkg kg "
			+ " where sp.pkg_id = kg.pkg_id and sp.pay_status in ('01','02','03') and sp.prin_py_amt > 0.00 and sp.prod_id IN (SELECT PROD_ID FROM FP_PROD WHERE PROD_LVL = ?2) ) px "
			+ " where ps.pkg_id = px.pkg_id and px.subs_amt > 0.00", nativeQuery = true)
	public BigDecimal getUserCreditorAssetsByProductLevel(String userId, String productLevel);

}
