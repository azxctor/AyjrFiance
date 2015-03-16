package com.hengxin.platform.product.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hengxin.platform.product.domain.ProductPackage;
import com.hengxin.platform.product.enums.EPackageStatus;

public interface ProductPackageRepository extends JpaRepository<ProductPackage, String>,
        JpaSpecificationExecutor<ProductPackage> {

    List<ProductPackage> findByProductId(String productId);

    ProductPackage getProductPackageById(String packageId);

    @Modifying
    @Query("UPDATE ProductPackage Set autoSubscribeFlag = 'P', lastTime = :date, lastOperatorId = :userId  WHERE id = :packageId")
    void updateAutoSubscribe(@Param("packageId") String packageId, @Param("userId") String userId,
            @Param("date") Date currentDate);

    List<ProductPackage> findByStatusNotIn(List<EPackageStatus> statusList);

    @Query("SELECT pkg FROM ProductPackage pkg INNER JOIN FETCH pkg.product product INNER JOIN FETCH product.userPo WHERE pkg.status= :status AND pkg.supplyStartTime < :supplyStartTime")
    List<ProductPackage> findByStatusAndSupplyStartTimeLessThan(@Param("status") EPackageStatus status,
            @Param("supplyStartTime") Date supplyStartTime);

    List<ProductPackage> findByStatusAndPrePublicTimeLessThan(EPackageStatus status, Date prePublicTime);

    List<ProductPackage> findByStatusAndSupplyEndTimeLessThan(EPackageStatus status, Date SupplyEndTime);
    
    List<ProductPackage> findByStatus(EPackageStatus status);

    @Query(value = "SELECT pkg FROM ProductPackage pkg INNER JOIN FETCH pkg.product product INNER JOIN FETCH product.userPo WHERE pkg.status= :status")
    List<ProductPackage> findAllFetchProduct(@Param("status") EPackageStatus status);

    @Query(value = "SELECT pkg FROM ProductPackage pkg INNER JOIN FETCH pkg.product product INNER JOIN FETCH product.userPo WHERE pkg.status= :status AND pkg.id= :packageId")
    ProductPackage findPackageByPackageIdFetchProduct(@Param("status") EPackageStatus status, @Param("packageId") String packageId);

    @Query("SELECT pkg FROM ProductPackage pkg INNER JOIN FETCH pkg.aipGroups groups INNER JOIN FETCH groups.users users WHERE pkg.id=:pkgId AND users.userId=:userId")
    List<ProductPackage> findByPkgIdAndAIPGroupUser(@Param("pkgId") String pkgId, @Param("userId") String userId);

    @Query("SELECT COUNT(e) FROM PackageSubscribes e WHERE e.pkgId = :pkgId")
    int countSubPackage(@Param("pkgId") String packageId);
    
    @Modifying
    @Query(value="update fp_pkg f set f.status = ?3,f.last_mnt_opid=?5,f.last_mnt_ts = ?6, f.version_ct = nvl(f.version_ct,0)+1 where f.pkg_id=?1 and f.status=?2 and not exists( select null from fp_pkg_schd_pay s where s.pkg_id = f.pkg_id and s.pay_status <> ?4)",nativeQuery=true)
    public void updatePkgStatusToEnd(String pkgId, String pkgSrcStatus, String pkgTargetStatus, String schdEndStatus, String currOpid, Date nowDate);
    
    @Query(value="select f.* from fp_pkg f where f.pkg_id=?1 and f.status=?2 and not exists( select null from fp_pkg_schd_pay s where s.pkg_id = f.pkg_id and s.pay_status <> ?3)",nativeQuery=true)
    public List<Object[]> getPkgStatusToEnd(String pkgId, String pkgSrcStatus, String schdEndStatus);
    
}
