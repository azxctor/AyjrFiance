
/*
* Project Name: kmfex-platform
* File Name: PackageSchedulePayRemindJob.java
* Class Name: PackageSchedulePayRemindJob
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
	
package com.hengxin.platform.job.bean;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.product.domain.PaymentSchedule;
import com.hengxin.platform.product.domain.Product;
import com.hengxin.platform.product.service.BatchRemindRepaymentService;


/**
 * Class Name: PackageMessageRemindJob
 * @author tingwang
 *
 */
@DisallowConcurrentExecution
public class PreRemindRepaymentJob extends QuartzJobBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(PreRemindRepaymentJob.class);

	@Autowired
	private BatchRemindRepaymentService batchRemindRepaymentService;
   
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
    	LOGGER.debug("~~~~Pre pay date remind fncr  ~~~~");
    	LOGGER.debug("~~~~Pre pay date remind fncr  ~~~~");
    	LOGGER.debug("~~~~Pre pay date remind fncr  ~~~~");
    	LOGGER.debug("~~~~Pre pay date remind fncr  ~~~~");
		Date workDate = CommonBusinessUtil.getCurrentWorkDate();
		Integer preDays = CommonBusinessUtil.getPreRemindRepaymentDays();
		List<PaymentSchedule> remindList = batchRemindRepaymentService.getPreRemindRepaymentList(workDate, preDays.intValue());
		Map<String, Integer> pkgSeqCountCache = new ConcurrentHashMap<String,Integer>();
		Map<String, Product> pkgProductCache = new ConcurrentHashMap<String,Product>();
		for(PaymentSchedule sh : remindList) {
			String pkgId = sh.getPackageId();
			try{
				Integer totalSeq = batchRemindRepaymentService.getTotalSeq(pkgId, pkgSeqCountCache);
				Product product = batchRemindRepaymentService.getProductByPkgId(pkgId, pkgProductCache);
				batchRemindRepaymentService.remindSchduleRepayment(sh, totalSeq, product);
			}catch(Exception ex){
				LOGGER.error("~~~~remind error~~~~", ex);
			}
		}
    }


}
