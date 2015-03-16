package com.hengxin.platform.fund.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.hengxin.platform.fund.entity.SubAcctTrxJnlPo;
import com.hengxin.platform.fund.enums.EFundUseType;

/**
 * SubAcctTrxJnlRepository.
 *
 */
@Repository
public interface SubAcctTrxJnlRepository extends PagingAndSortingRepository<SubAcctTrxJnlPo, String>,
        JpaSpecificationExecutor<SubAcctTrxJnlPo> {

    @Query("select SUM(trxAmt) from SubAcctTrxJnlPo s where s.acctNo=?1 and s.subAcctNo=?2 and s.useType=?3")
    BigDecimal getTrxAmtByAcctAndUseType(String acctNo, String subAcctNo, EFundUseType useType);
        
    @Query("select trxAmt from SubAcctTrxJnlPo s where s.acctNo=?1 and s.subAcctNo=?2 and s.useType=?3 and s.trxDt between ?4 and ?5")
    BigDecimal getTrxAmtByAcctAndUseTypeAndDate(String acctNo, String subAcctNo, EFundUseType useType, Date startDate, Date endDate);
    
    @Query(value="SELECT * FROM (select * from ac_trx_jnl s where s.acct_no=?1 and s.sub_acct_no in ?2 and s.use_type in ?3 order by s.create_ts desc)where rownum<=10",nativeQuery=true)
    List<SubAcctTrxJnlPo> getLastTenByAcct(String acctNo, List<String> subAcctNo, List<String> set);
    
//    @Query(value="select TO_CHAR(trx_dt-(NVL(1,0)-1),'iw'),SUM(trx_amt) \r\n" + 
//            "from ac_trx_jnl\r\n" + 
//            "where trx_dt between sysdate-84 and sysdate and acct_no = ?1 and sub_acct_no = ?2 and use_type in (42,43,44)\r\n" + 
//            "group by TO_CHAR(trx_dt-(NVL(1,0)-1),'iw') \r\n" + 
//            "order by TO_CHAR(trx_dt-(NVL(1,0)-1),'iw') desc",nativeQuery = true)
    @Query(value="select \r\n" + 
            "CASE\r\n" + 
            "  when trx_dt between cur.curMon-77 and cur.curMon-70 then 1\r\n" + 
            "  when trx_dt between cur.curMon-70 and cur.curMon-63 then 2\r\n" + 
            "  when trx_dt between cur.curMon-63 and cur.curMon-56 then 3\r\n" + 
            "  when trx_dt between cur.curMon-56 and cur.curMon-49 then 4\r\n" + 
            "  when trx_dt between cur.curMon-49 and cur.curMon-42 then 5\r\n" + 
            "  when trx_dt between cur.curMon-42 and cur.curMon-35 then 6\r\n" + 
            "  when trx_dt between cur.curMon-35 and cur.curMon-28 then 7\r\n" + 
            "  when trx_dt between cur.curMon-28 and cur.curMon-21 then 8\r\n" + 
            "  when trx_dt between cur.curMon-21 and cur.curMon-14 then 9\r\n" + 
            "  when trx_dt between cur.curMon-14 and cur.curMon-7 then 10\r\n" + 
            "  when trx_dt between cur.curMon-7 and cur.curMon then 11\r\n" + 
            "  when trx_dt between cur.curMon and sysdate then 12\r\n" + 
            "END week,\r\n" + 
            "nvl(sum(trx_amt),0)\r\n" + 
            "from ac_trx_jnl,(select trunc(sysdate,'iw') as curMon from dual) cur\r\n" + 
            "where acct_no = ?1 and sub_acct_no = ?2 and use_type in (42,43,44) and trx_dt between cur.curMon-77 and sysdate\r\n" + 
            "group by \r\n" + 
            "CASE\r\n" + 
            "  when trx_dt between cur.curMon-77 and cur.curMon-70 then 1\r\n" + 
            "  when trx_dt between cur.curMon-70 and cur.curMon-63 then 2\r\n" + 
            "  when trx_dt between cur.curMon-63 and cur.curMon-56 then 3\r\n" + 
            "  when trx_dt between cur.curMon-56 and cur.curMon-49 then 4\r\n" + 
            "  when trx_dt between cur.curMon-49 and cur.curMon-42 then 5\r\n" + 
            "  when trx_dt between cur.curMon-42 and cur.curMon-35 then 6\r\n" + 
            "  when trx_dt between cur.curMon-35 and cur.curMon-28 then 7\r\n" + 
            "  when trx_dt between cur.curMon-28 and cur.curMon-21 then 8\r\n" + 
            "  when trx_dt between cur.curMon-21 and cur.curMon-14 then 9\r\n" + 
            "  when trx_dt between cur.curMon-14 and cur.curMon-7 then 10\r\n" + 
            "  when trx_dt between cur.curMon-7 and cur.curMon then 11\r\n" + 
            "  when trx_dt between cur.curMon and sysdate then 12\r\n" + 
            "END",nativeQuery = true)
    List<Object[]> getInvestBeniftByAcct(String acctNo, String subAcctNo);
    
    SubAcctTrxJnlPo getTrxJnlByJnlNo(String jnlNo);
	
	@Query(value = "select t.cash_pool,"
			     + "       nvl(t.total_amt,0.00),  "
			     + "       nvl(t.t_pay_amt,0.00), "
			     + "       nvl(t.t_recv_amt,0.00), "
			     + "       (nvl(t.t_recv_amt,0.00)-nvl(t.t_pay_amt,0.00)) as net_amt "
			     + "  from "
			     + "       (select x.cash_pool, "
			     + "               sum(x.trx_amt) as total_amt, "
			     + "               (select sum(x1.trx_amt) "
			     + "                  from ac_trx_jnl x1 "
			     + "                 where x1.pay_recv_flg   = 'R' "
			     + "                   and x1.cash_pool      = x.cash_pool"
			     + "                   and x1.cash_pool      = nvl(?1, x1.cash_pool) "
			     + "                   and x1.acct_no        = nvl(?2, x1.acct_no) "
			     + "                   and (trim(?3) is null or to_char(x1.trx_dt,'yyyy-MM-dd')>=?3) "
			     + "                   and (trim(?4) is null or to_char(x1.trx_dt,'yyyy-MM-dd')<=?4) "
			     + "                   and x1.use_type in (?5) "
			     + "                ) as t_recv_amt, "
			     + "               (select sum(x2.trx_amt) "
			     + "                  from ac_trx_jnl x2 "
			     + "                 where x2.pay_recv_flg    = 'P' "
			     + "                   and x2.cash_pool       = x.cash_pool"
			     + "                   and x2.cash_pool       = nvl(?1, x2.cash_pool) "
			     + "                   and x2.acct_no         = nvl(?2, x2.acct_no) "
			     + "                   and (trim(?3) is null or to_char(x2.trx_dt,'yyyy-MM-dd')>=?3) "
			     + "                   and (trim(?4) is null or to_char(x2.trx_dt,'yyyy-MM-dd')<=?4) "
			     + "                   and x2.use_type in (?5) "
			     + "                ) as t_pay_amt "
			     + "         from ac_trx_jnl x "
			     + "        where x.cash_pool      = nvl(?1, x.cash_pool) "
			     + "          and x.acct_no        = nvl(?2, x.acct_no) "
			     + "          and (trim(?3) is null or to_char(x.trx_dt,'yyyy-MM-dd')>=?3) "
			     + "          and (trim(?4) is null or to_char(x.trx_dt,'yyyy-MM-dd')<=?4) "
			     + "          and x.use_type in (?5) "
			     + "        group by x.cash_pool ) t", nativeQuery = true)
	List<Object[]> getCashPoolStockDtl(String cashPool, String acctNo, String fromDate, String toDate, List<String> useTypeCodes);
	
	/**
	 * 资金池余额汇总查询.
	 * @param cashPool
	 * @param acctNo
	 * @param fromDate
	 * @param toDate
	 * @param workDateStr
	 * @return
	 */
	@Query(value=" select nvl(sum(t_bal),0.00) from ( "
			     + " select x.* from ("
			     + "    select "
			     + "      to_date(?5,'yyyy-MM-dd') as work_dt,"
			     + "      cash_pool,"
			     + "      t_bal"
			     + "    from (  "
			     + "      select t.cash_pool,"
			     + "             sum(s.bal) as t_bal"
			     + "      from ac_sub_acct s, ac_acct t "
			     + "     where s.acct_no = t.acct_no"
			     + "       and t.cash_pool = nvl(?1, t.cash_pool) "
			     + "       and t.acct_no = nvl(?2, t.acct_no) "
			     + "       group by t.cash_pool "
			     + "  )"
			     + "  union "
			     + "  select ht.work_dt,"
			     + "         ht.cash_pool,"
			     + "         sum(ht.bal) as t_bal"
			     + "   from ac_sub_acct_hist ht"
			     + "  where ht.cash_pool = nvl(?1, ht.cash_pool) "
			     + "    and ht.acct_no = nvl(?2, ht.acct_no) "
			     + "    and (trim(?3) is null or to_char(ht.work_dt,'yyyy-MM-dd')>=?3) "
			     + "    and (trim(?4) is null or to_char(ht.work_dt,'yyyy-MM-dd')<=?4) "
			     + "   group by ht.work_dt, ht.cash_pool"
			     + " ) x where (trim(?3) is null or to_char(x.work_dt,'yyyy-MM-dd')>=?3) "
			     + "       and (trim(?4) is null or to_char(x.work_dt,'yyyy-MM-dd')<=?4) "
			     + ") ", nativeQuery=true)
	BigDecimal getTotalBal(String cashPool, String acctNo, String fromDate, String toDate, String workDateStr);
	
	
    
}
