package com.hengxin.platform.product.repository;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hengxin.platform.product.domain.FinancingPackageView;

/**
 * 
 * @author yingchangwang
 * 
 */
public interface FinancingPackageViewRepository extends PagingAndSortingRepository<FinancingPackageView, String>,
        JpaSpecificationExecutor<FinancingPackageView> {
    @Override
    public Page<FinancingPackageView> findAll(Specification<FinancingPackageView> spec, Pageable pageable);

    @Query(value ="select c.tmplt_id from fp_cntrct_tmplt c join  v_pkg_search v on c.tmplt_id = v.cntrct_tmplt_id join ac_position p on v.pkg_id = p.pkg_id join ac_position_lot l on p.position_id = l.position_id where l.lot_id = ?1", nativeQuery = true)
    public BigDecimal getContractTemplateIdByLotId(String lotId);
    
    //融资包汇总
    @Query(value ="select nvl(sum(v.PKG_QUOTA),0.00)||'@'||nvl(sum(v.SUBS_AMT),0.00)||'@'||count(1) from v_pkg_search v "
    		+ "where (?7=0 or to_char(v.SIGNING_DT,'yyyyMMdd') between ?1 and ?2) "
    		+ "and (?8=0 or to_char(v.LAST_MNT_TS,'yyyyMMdd') between ?3 and ?4) "
    		+ "and (trim(?5) is null "
    		+ "or (LOWER(v.PKG_ID) like ?5 or LOWER(v.PKG_NAME) like ?5 or LOWER(v.WRTR_NAME) like ?5 or LOWER(v.FINANCIER_NAME) like ?5)"
    		+ ") "
    		+ "and v.status = nvl(trim(?6),v.status)", nativeQuery = true)
    public String getSumAll(String start,String end,String jq_start,String jq_end,String keyword,String status,int signed,int ended);
}
