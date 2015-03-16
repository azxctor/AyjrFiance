package com.hengxin.platform.fund.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hengxin.platform.fund.entity.AcctPo;

@Repository
public interface AcctRepository extends
		JpaRepository<AcctPo, String>,
		JpaSpecificationExecutor<AcctPo> {

	/**
	 * 通过会员编号和账户类型查询综合账户信息
	 * @param userId
	 * @param acctType
	 * @return
	 */
	public List<AcctPo> findAcctByUserIdAndAcctType(String userId, String acctType);

	/**
	 * 通过综合账户号与账户类型获取综合账户信息
	 * 
	 * @param acctNo
	 * @param acctType
	 * @return
	 */
	AcctPo findAcctByAcctNoAndAcctType(String acctNo, String acctType);
	
	/**
	 * 
	* Description: TODO
	*
	* @param userId
	* @return
	 */
    AcctPo findByUserId(String userId);
    
    /**
     * 通过综合账户编号获取综合账户信息
     * @param acctNo
     * @return
     */
    AcctPo findByAcctNo(String acctNo);
    
    @Query(value = "SELECT F.USER_ID, F.ACCT_NO FROM AC_ACCT F", nativeQuery = true)
    List<Object[]> getUserIdAndAcctNo();

    /**
    * Description: TODO
    *
    * @param acctNo
    * @param username
    * @return
    */
    @Query(value = "SELECT F.* FROM AC_ACCT F WHERE F.ACCT_NO = ?1 AND F.USER_ID IN (SELECT U.USER_ID FROM UM_USER U WHERE U.NAME = ?2)", nativeQuery = true)
    public AcctPo getAcctByName(String acctNo, String name);
    
    @Query(value = "SELECT F.* FROM AC_ACCT F WHERE F.ACCT_NO = ?1 AND F.USER_ID IN (SELECT A.USER_ID FROM AC_BANK_ACCT A WHERE A.BNK_ACCT_NAME = ?2)", nativeQuery = true)
    public AcctPo getAcctByBnkAcctName(String acctNo, String name);
    
    @Query(value="select ct.user_id,us.name from ac_acct ct,um_user us where ct.user_id=us.user_id and ct.acct_no = ?1", nativeQuery = true)
    List<Object[]> getUserAcctInfoByAcctNo(String acctNo);
    
    @Query(value="SELECT AC.ACCT_NO FROM AC_ACCT AC JOIN UM_AUTHZD_CTR_INFO U ON U.USER_ID = AC.USER_ID", nativeQuery = true)
    List<String> getAcctNoByServiceCenter();
}
