/*
 * Project Name: kmfex-platform
 * File Name: PaymentScheduleService.java
 * Class Name: PaymentScheduleService
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

package com.hengxin.platform.product.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.product.domain.PaymentSchedule;
import com.hengxin.platform.product.enums.EPayStatus;
import com.hengxin.platform.product.repository.PaymentScheduleRepository;

/**
 * Class Name: PaymentScheduleService Description: TODO
 *
 * @author yingchangwang
 *
 */
@Service
public class PaymentScheduleService {
    @Autowired
    private PaymentScheduleRepository paymentScheduleRepository;

    /**
     *
    * Description: 获取当天手工还款记录
    *
    * @param packageId
    * @return
    * @throws ParseException
     */
    public PaymentSchedule getCurrentPeriodPayment(final String packageId) throws ParseException {
        Date currentDate = CommonBusinessUtil.getCurrentWorkDate();
        final Date startDate = DateUtils.getStartDate(currentDate);
        final Date endDate = DateUtils.getEndDate(currentDate);
        Specification<PaymentSchedule> spec = new Specification<PaymentSchedule>() {

            @Override
            public Predicate toPredicate(Root<PaymentSchedule> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                expressions.add(cb.equal(root.<EPayStatus> get("status"), EPayStatus.NORMAL));
                expressions.add(cb.equal(root.<String> get("packageId"), packageId));
                expressions.add(cb.between(root.<Date> get("paymentDate"), startDate, endDate));
                return predicate;
            }
        };

        List<PaymentSchedule> paymentScheduleList = paymentScheduleRepository.findAll(spec);
        if (CollectionUtils.isNotEmpty(paymentScheduleList)) {
            return paymentScheduleList.get(0);
        }
        return null;
    }


    /**
     *
    * Description: 获取所有还款中记录
    *
    * @param packageId
    * @return
     */
    public List<PaymentSchedule> getAllNormalPaymentSchedules(String packageId){
        List<EPayStatus> statusList = new ArrayList<EPayStatus>();
        statusList.add(EPayStatus.NORMAL);
        return paymentScheduleRepository.getByPackageIdAndStatusInOrderBySequenceIdAsc(packageId, statusList);
    }
    
    public Date findPreviousPaymentDate(String pkgId, Date currentWorkDate) {
    	return paymentScheduleRepository.findPreviousPaymentDate(pkgId, currentWorkDate);
    }
    
    public BigDecimal getTotalPayedInterest(String packageId) {
    	return paymentScheduleRepository.getTotalPayedInterest(packageId);
    }
}
