package com.hengxin.platform.fund.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hengxin.platform.fund.entity.PositionLotPo;

@Repository
public interface PositionLotRepository extends JpaRepository<PositionLotPo, String>,
        JpaSpecificationExecutor<PositionLotPo> {

    PositionLotPo findPositionLotByLotId(String lotId);

    @Query("select SUM(A.lotBuyPrice) from PositionLotPo A,PositionPo B where B.userId = ?1 and B.positionId = A.positionId")
    BigDecimal getTotalInvestment(String userId);

    @Query("select SUM(a.accumCrAmt) from PositionLotPo a, PositionPo b where b.pkgId = ?1 and b.userId = ?2 and b.positionId = a.positionId")
    BigDecimal getAccumCrAmt(String pakageId, String userId);

    List<PositionLotPo> findByPositionUserId(String userId);
    
    @Override
    Page<PositionLotPo> findAll(Specification<PositionLotPo> spec, Pageable pageable);
    
    @Query(value = "select a from PositionLotPo a, PositionPo b where b.pkgId = ?1 and b.positionId = a.positionId")
    List<PositionLotPo> getLotsByPkgId(String pkgId);

    List<PositionLotPo> getLotsByPositionId(String positionId);
    
    List<PositionLotPo> findByPositionIdAndUnitGreaterThan(String positionId, Integer unit);
    
    @Override
    List<PositionLotPo> findAll(Specification<PositionLotPo> spec);

    List<PositionLotPo> findByCrIdOrderByCreateTsAsc(String crId);

}
