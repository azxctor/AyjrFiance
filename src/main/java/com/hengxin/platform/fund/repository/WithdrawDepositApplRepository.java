package com.hengxin.platform.fund.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.hengxin.platform.fund.domain.WithdrawDepositAppl;
import com.hengxin.platform.fund.entity.WithdrawDepositApplPo;
import com.hengxin.platform.fund.enums.ECashPool;
import com.hengxin.platform.fund.enums.EFundApplStatus;
import com.hengxin.platform.fund.enums.EFundDealStatus;

/**
 * WithdrawDepositApplRepository.
 *
 */
@Repository
public interface WithdrawDepositApplRepository extends
		PagingAndSortingRepository<WithdrawDepositApplPo, String>,
		JpaSpecificationExecutor<WithdrawDepositApplPo> {

	WithdrawDepositApplPo findByUserIdAndDealStatus(String userId,
			String dealStatus);

	WithdrawDepositApplPo findByApplNo(String applNo);

	WithdrawDepositApplPo findByRelBnkJnlNo(String relBnkJnlNo);

	@Modifying
	@Query("update WithdrawDepositApplPo a set a.dealStatus = ?3 where a.applNo = ?1 and a.dealStatus = ?2")
	int updateDealStatus(String applNo, String dealStatusFrom,
			String dealStatusTo);

	@Modifying
	@Query("update WithdrawDepositApplPo a set a.dealStatus = ?3 where a.applNo = ?1 and a.dealStatus = ?2")
	int update(WithdrawDepositAppl appl);

	List<WithdrawDepositApplPo> findByUserIdAndApplStatus(String userId,
			EFundApplStatus applStatus);

	@Query(value = "select a from WithdrawDepositApplPo a where a.applStatus = ?1 and a.dealStatus = ?2 and a.cashPool = ?3")
	List<WithdrawDepositApplPo> findByCashPoolAndApplStatusAndDealStatus(
			EFundApplStatus applStatus, EFundDealStatus dealStatus, ECashPool cashPool);
	
	@Query(value = "SELECT "
			+ " (SELECT COUNT(1) FROM AC_RECHARGE_APPL WHERE USER_ID = ?1 AND DEAL_STATUS = '01') AS RECH_CT,"
			+ " (SELECT COUNT(1) FROM AC_WDRW_APPL WHERE USER_ID = ?1 AND DEAL_STATUS = '01') AS WDRW_CT,"
			+ "  0 AS TSFR_CT,"
			+ " (SELECT COUNT(1) FROM AC_RVS_APPL WHERE USER_ID = ?1 AND APPL_STATUS = 'WA') AS RVS_CT"
			+ " FROM DUAL", nativeQuery = true)
	List<Object[]> countUnEndAppls(String userId);

}
