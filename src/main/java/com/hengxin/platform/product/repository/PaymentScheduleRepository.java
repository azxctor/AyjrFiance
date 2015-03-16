/*
 * Project Name: kmfex-platform
 * File Name: PaymentScheduleRepository.java
 * Class Name: PaymentScheduleRepository
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
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.hengxin.platform.product.domain.PaymentSchedule;
import com.hengxin.platform.product.enums.EPayStatus;

/**
 * Class Name: PaymentScheduleRepository Description: TODO
 * 
 * @author tingwang
 * 
 */

public interface PaymentScheduleRepository extends JpaRepository<PaymentSchedule, String>,
        JpaSpecificationExecutor<PaymentSchedule> {

    PaymentSchedule getByPackageIdAndSequenceId(String packageId, int sequenceId);

    List<PaymentSchedule> getByPackageIdAndStatusOrderBySequenceIdAsc(String packageId, EPayStatus status);

    List<PaymentSchedule> getByPackageIdAndStatusIn(String packageId, List<EPayStatus> status);
    
    List<PaymentSchedule> getByPackageIdAndStatusInOrderBySequenceIdAsc(String packageId, List<EPayStatus> status);
    
    List<PaymentSchedule> getByPackageIdAndStatusNotIn(String packageId, List<EPayStatus> status);

    @Override
    public Page<PaymentSchedule> findAll(Specification<PaymentSchedule> spec, Pageable pageable);

    List<PaymentSchedule> getByPackageId(String packageId);

    List<PaymentSchedule> getByPackageIdOrderBySequenceIdAsc(String packageId);

    List<PaymentSchedule> getByPackageIdOrderBySequenceIdDesc(String packageId);
    
    List<PaymentSchedule> findByPaymentDateLessThanAndStatusInOrderByFinancerIdAscPaymentDateAscSequenceIdAsc(Date lessThanDate, List<EPayStatus> inStatus);

    List<PaymentSchedule> findByPaymentDateAndStatusInOrderByFinancerIdAscPaymentDateAscSequenceIdAsc(Date payDate, List<EPayStatus> inStatus);
    
    @Query(value="select count(1) from fp_pkg_schd_pay where pkg_id = ?1",nativeQuery=true)
    Integer countByPackageId(String pkgId);
    
    @Query(value = "select TO_CHAR(PAY_DT-(NVL(1,0)-1),'iw'), "
            + "      SUM(INTR_PD_AMT+INTR_FINE_PD_AMT+PRIN_FINE_PD_AMT)  " 
    		+ "from FP_PKG_SCHD_PAY "
            + "where PAY_DT between sysdate-84 and sysdate and PKG_ID = ?1 "
            + "group by TO_CHAR(PAY_DT-(NVL(1,0)-1),'iw')  " 
            + "order by TO_CHAR(PAY_DT-(NVL(1,0)-1),'iw') desc", nativeQuery = true)
    List<Object[]> getInvestBeniftByPkgId(String pkgId);
    
	@Query(value = "select a from PaymentSchedule a where a.packageId = ?1 and a.paymentDate = ?2")
    List<PaymentSchedule> findOneByPackageIdAndPaymentDate(String packageId, Date paymentDate);
	
	@Query(value = "select max(a.paymentDate) from PaymentSchedule a where a.packageId = ?1 and a.paymentDate < ?2 and a.lastPayTs is not null")
	Date findPreviousPaymentDate(String packageId, Date currentWorkDate);
	
	@Query(value = "select sum(a.payInterestAmt) from PaymentSchedule a where a.packageId = ?1")
	BigDecimal getTotalPayedInterest(String packageId);
    
}
