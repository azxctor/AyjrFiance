/*
 * Project Name: kmfex-platform
 * File Name: PackageSubscribesRepository.java
 * Class Name: PackageSubscribesRepository
 *
 * Copyright 2014 Hengtian Software Inc
 *
 * Licensed under the Hengtiansoft
 *
 * http://www.hengtiansoft.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hengxin.platform.product.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.hengxin.platform.product.domain.PackageSubscribes;

/**
 * Class Name: PackageSubscribesRepository Description: TODO
 * 
 * @author tingwang
 * 
 */

public interface PackageSubscribesRepository extends JpaRepository<PackageSubscribes, String>,
        JpaSpecificationExecutor<PackageSubscribes> {

    List<PackageSubscribes> getSubsByPkgId(String pkgId);

    List<PackageSubscribes> getSubsByPkgIdAndUserId(String pkgId, String userId);

    /**
     * 
     * Description: 一个融资包购买的总金额和人数
     * 
     * @param spec
     * @return
     */
    @Query(value = "select FP_PKG_SUBS.PKG_ID, SUM(FP_PKG_SUBS.UNIT * FP_PKG_SUBS.UNIT_AMT), COUNT(DISTINCT FP_PKG_SUBS.USER_ID), MAX(FP_PKG_SUBS.CREATE_TS) FROM FP_PKG_SUBS INNER JOIN FP_PKG ON FP_PKG_SUBS.PKG_ID = FP_PKG.PKG_ID WHERE FP_PKG.STATUS='S' GROUP BY FP_PKG_SUBS.PKG_ID HAVING SUM(FP_PKG_SUBS.UNIT * FP_PKG_SUBS.UNIT_AMT) != MAX(FP_PKG.SUBS_AMT)", nativeQuery = true)
    List<Object[]> getAmountAndCount();
    
    @Query(value = "select SUM(FP_PKG_SUBS.UNIT * FP_PKG_SUBS.UNIT_AMT), COUNT(DISTINCT FP_PKG_SUBS.USER_ID), MAX(FP_PKG_SUBS.CREATE_TS) FROM FP_PKG_SUBS INNER JOIN FP_PKG ON FP_PKG_SUBS.PKG_ID = FP_PKG.PKG_ID WHERE FP_PKG.STATUS='S' AND FP_PKG.PKG_ID=?1 GROUP BY FP_PKG_SUBS.PKG_ID", nativeQuery = true)
    List<Object[]> getAmountAndCountByPkgId(String pkgId);

    @Query(value = "SELECT SUM(F.UNIT), F.USER_ID FROM FP_PKG_SUBS F WHERE F.PKG_ID = ?1 GROUP BY F.USER_ID", nativeQuery = true)
    List<Object[]> getUnitSumByPkgIdGroupByUserId(String pkgId);
    
    @Query(value = "SELECT MIN(F.SUBS_DT), F.USER_ID FROM FP_PKG_SUBS F WHERE F.PKG_ID = ?1 GROUP BY F.USER_ID", nativeQuery = true)
    List<Object[]> getMinSubsDtByPkgIdGroupByUserId(String pkgId);
    
    @Query(value = "SELECT COUNT(DISTINCT F.USER_ID) FROM FP_PKG_SUBS F WHERE F.PKG_ID = ?1", nativeQuery = true)
    List<BigDecimal> getSupplyCountByPkgId(String pkgId);
    
    /**
     * 查询指定用户所买入的同一个项目下的所有融资包金额总和.
     * @param userId
     * @param prodId
     * @return
     */
    @Query(value = "SELECT SUM(UNIT * UNIT_AMT) FROM FP_PKG_SUBS WHERE USER_ID = ?1 AND PKG_ID IN (SELECT PKG_ID FROM FP_PKG WHERE PROD_ID = ?2 AND STATUS IN ('S', 'WN'))", nativeQuery = true)
    BigDecimal getSumUnitByUserAndProd(String userId, String prodId);

    /**
     * 查询指定用户所买入的同一个类型所有融资项目所有融资包金额总和.
     */
    @Query(value = "SELECT SUM(UNIT * UNIT_AMT) FROM FP_PKG_SUBS WHERE USER_ID = ?1 AND PKG_ID IN (SELECT PKG_ID FROM FP_PKG WHERE STATUS IN ('S', 'WN') AND PROD_ID IN (SELECT PROD_ID FROM FP_PROD WHERE FP_PROD.PROD_LVL = ?2))", nativeQuery = true)
    BigDecimal getSumUnitByUserAndProdLevel(String userId, String level);

}
