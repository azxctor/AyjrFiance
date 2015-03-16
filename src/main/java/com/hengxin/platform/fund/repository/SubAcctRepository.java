package com.hengxin.platform.fund.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.hengxin.platform.fund.entity.SubAcctPo;

/**
 * 子账户. 
 *
 */
@Repository
public interface SubAcctRepository extends
		PagingAndSortingRepository<SubAcctPo, String>,
		JpaSpecificationExecutor<SubAcctPo> {
	
	/**
	 * 通过综合账户和综合子账户查询.
	 * @param acctNo
	 * @param SubAcctNo
	 * @return
	 */
	SubAcctPo findSubAcctByAcctNoAndSubAcctNo(String acctNo, String subAcctNo);
	
	/**
	 * 通过综合账户编号查询所有子账户.
	 * @param acctNo
	 * @return
	 */
	List<SubAcctPo> findByAcctNo(String acctNo);

    @Query(value = "select "
	             + "       (st.bal-st.freezable_amt-st.reserved_amt) as avl_amt "
	             + "  from ac_sub_acct st, "
	             + "       (select x.* from ac_acct x where x.user_id=?1 ) ct"
	             + " where st.sub_acct_no = ?2"
	             + "   and st.acct_no = ct.acct_no", nativeQuery = true)
    BigDecimal getUserSubAcctAvlAmt(String userId, String subAcctNo);

    @Query(value = "select "
	        	 + "       ((st1.bal-st1.freezable_amt-st1.reserved_amt) + "
	        	 + "       (st2.bal-st2.freezable_amt-st2.reserved_amt)) "
	        	 + "       as avl_amt "
	        	 + "  from ac_sub_acct st1, ac_sub_acct st2, "
	        	 + "  (select x.* from ac_acct x where x.user_id=?1) ct "
	        	 + " where st1.acct_no = ct.acct_no and st2.acct_no = ct.acct_no "
	        	 + "   and st1.sub_acct_no='000001' and st2.sub_acct_no='000004' ", nativeQuery = true)
    BigDecimal getUserCurrentAndXwbSubAcctAvlAmt(String userId);
    
    @Query(value = "select (st.bal-st.reserved_amt) as avl_amt "
            + "   from ac_sub_acct st, ac_acct ct "
            + "  where st.acct_no = ct.acct_no "
            + "    and ct.user_id = ?1 and st.sub_acct_no = ?2", nativeQuery = true)
    BigDecimal getUserSubAcctAvlAmtIgnoreFrzAmt(String userId, String subAcctNo);
    
    
    @Query(value = "select "
       	 + "       ((st1.bal-st1.reserved_amt) + "
       	 + "       (st2.bal-st2.reserved_amt)) "
       	 + "       as avl_amt "
       	 + "  from ac_sub_acct st1, ac_sub_acct st2, "
       	 + "  (select x.* from ac_acct x where x.user_id=?1) ct "
       	 + " where st1.acct_no = ct.acct_no and st2.acct_no = ct.acct_no "
       	 + "   and st1.sub_acct_no='000001' and st2.sub_acct_no='000004' ", nativeQuery = true)
    BigDecimal getUserCurrentAndXwbSubAcctAvlAmtIgnoreFrzAmt(String userId);
    
    @Query(value = "SELECT sum(bal) FROM ac_sub_acct where acct_no = ?1", nativeQuery = true)
    BigDecimal getAcctTotalBalByAcctNo(String acctNo);
    

}
