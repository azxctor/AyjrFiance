package com.hengxin.platform.fund.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hengxin.platform.fund.entity.BankAcctPo;

/**
 * BankAcctRepository.
 */
@Repository
public interface BankAcctRepository extends JpaRepository<BankAcctPo, String>,
		JpaSpecificationExecutor<BankAcctPo> {

	List<BankAcctPo> findBankAcctByUserId(String userId);

	BankAcctPo findBankAcctByBnkAcctNo(String bankAcc);

	BankAcctPo findBankAcctByBnkAcctNoAndSignedFlg(String bankAcc, String signedFlg);

	BankAcctPo findByBnkAcctNoAndUserId(String bnkAcctNo, String userId);

	List<BankAcctPo> findByUserIdAndSignedFlg(String userId, String signedFlg);
	
	List<BankAcctPo> findByBnkAcctName(String bnkAcctName);

	@Query(value = "select * from AC_ACCT A,AC_BANK_ACCT B WHERE B.USER_ID = A.USER_ID AND A.ACCT_NO = ?1", nativeQuery = true)
	List<BankAcctPo> findBankAcctByAcctNo(String acctNo);
	
	@Query(value = "SELECT COUNT(*) FROM BankAcctPo WHERE bnkAcctNo = :bankCard")
	int countByBankAccount(@Param("bankCard") String acctNo);
	
	@Query(value = "SELECT COUNT(*) FROM BankAcctPo WHERE bnkAcctNo = :bankCard AND userId != :userId")
	int countByBankAccountAndUserIdNot(@Param("bankCard") String acctNo, @Param("userId") String userId);
}
