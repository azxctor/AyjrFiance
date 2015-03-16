package com.hengxin.platform.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hengxin.platform.product.domain.CreditorsRightTransferContract;

public interface CreditorsRightTransferContractRepository extends
		JpaRepository<CreditorsRightTransferContract, String> {

	@Query(value = "SELECT COUNT(*) FROM FP_CR_TSFR_CNTRCT WHERE CR_ID=?1", nativeQuery = true)
	public Long getCountByCreditorsRight(String crId);

	public CreditorsRightTransferContract findBySellerLotId(String sellerLotId);

	public CreditorsRightTransferContract findByBuyerLotId(String buyerLotId);

}
