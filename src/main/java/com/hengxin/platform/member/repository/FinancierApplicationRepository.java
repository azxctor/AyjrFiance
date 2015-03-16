/*
 * Project Name: kmfex-platform
 * File Name: FinancierApplicationPo.java
 * Class Name: FinancierApplicationPo
 *
 * Copyright 2014 KMFEX Inc
 *
 * 
 *
 * http://www.kmfex.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hengxin.platform.member.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.hengxin.platform.member.domain.FinancierApplication;
import com.hengxin.platform.member.enums.EApplicationStatus;

/**
 * Class Name: FinancierApplicationPo
 * 
 * @author runchen
 * 
 */

public interface FinancierApplicationRepository extends PagingAndSortingRepository<FinancierApplication, String>,
	JpaSpecificationExecutor<FinancierApplication> {

    Page<FinancierApplication> findByUserId(String userId, Pageable pageable);

    Page<FinancierApplication> findByUserIdAndStatusNot(String userId, EApplicationStatus status, Pageable pageable);
    
    @Query("SELECT COUNT(E) FROM FinancierApplication E WHERE E.userId <> :userId AND LOWER(E.orgNumber) = :orgNumber AND (E.status = :primeS OR E.status = :minorS)")
	int countByUserIdNotAndOrgNumberIgnoreCaseAndStatusOrStatus(@Param("userId") String userId, @Param("orgNumber") String orgNumber, @Param("primeS") EApplicationStatus primeStatus, @Param("minorS") EApplicationStatus minorStatus);

	@Query("SELECT COUNT(E) FROM FinancierApplication E WHERE LOWER(E.orgNumber) = :orgNumber AND (E.status = :primeS OR E.status = :minorS)")
	int countByOrgNumberIgnoreCaseAndStatusOrStatus(@Param("orgNumber") String orgNumber, @Param("primeS") EApplicationStatus primeStatus, @Param("minorS") EApplicationStatus minorStatus);

}
