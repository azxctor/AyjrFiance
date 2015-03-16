package com.hengxin.platform.fund.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hengxin.platform.fund.entity.PkgTradeJnlPo;
import com.hengxin.platform.fund.enums.EFundTrdType;

/**
 * PkgTradeJnlRepository.
 *
 */
@Repository
public interface PkgTradeJnlRepository extends JpaRepository<PkgTradeJnlPo,String>,JpaSpecificationExecutor<PkgTradeJnlPo> {

    @Query(value="select distinct a.buyerUserId from PkgTradeJnlPo a where a.pkgId=?1 and a.sellerUserId=?2", nativeQuery=false)
    List<String> getBuyersByProductPkgId(String pkgId, String sellerUserId);

    @Override
    Page<PkgTradeJnlPo> findAll(Specification<PkgTradeJnlPo> spec, Pageable pageable);
    
    List<PkgTradeJnlPo> findByBuyerUserId(String userId);

	PkgTradeJnlPo findByBuyerUserIdAndLotIdAndTrdType(String userId, String lotId, EFundTrdType tradeType);

    @Query(value = "select a.trdDt from PkgTradeJnlPo a where a.lotId = ?1", nativeQuery = false)
	Date getPkgTradeDate(String lotId);
    
    @Query(value = "select count(distinct a.buyerUserId) from PkgTradeJnlPo a where a.pkgId = ?1", nativeQuery = false)
    Integer getBuyerCount(String pkgId);
    
}
