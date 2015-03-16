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

@DisallowConcurrentExecution
public class RemindRepaymentJob extends QuartzJobBean {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(RemindRepaymentJob.class);
	@Autowired
	private BatchRemindRepaymentService batchRemindRepaymentService;
	
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		LOGGER.debug("~~~~remind fncr for repayment in 02,03,04 ~~~~");
		LOGGER.debug("~~~~remind fncr for repayment in 02,03,04 ~~~~");
		LOGGER.debug("~~~~remind fncr for repayment in 02,03,04 ~~~~");
		LOGGER.debug("~~~~remind fncr for repayment in 02,03,04 ~~~~");
		Date workDate = CommonBusinessUtil.getCurrentWorkDate();
		List<PaymentSchedule> remindList = batchRemindRepaymentService.getTodayRepaymentRemindList(workDate);
		Map<String, Integer> pkgSeqCountCache = new ConcurrentHashMap<String,Integer>();
		Map<String, Product> pkgProductCache = new ConcurrentHashMap<String,Product>();
		for(PaymentSchedule sh : remindList) {
			String pkgId = sh.getPackageId();
			try{
				Integer totalSeq = batchRemindRepaymentService.getTotalSeq(pkgId, pkgSeqCountCache);
				Product product = batchRemindRepaymentService.getProductByPkgId(pkgId, pkgProductCache);
				batchRemindRepaymentService.remindSchduleRepayment(sh, totalSeq, product);
			}catch(Exception ex){
				LOGGER.error("~~~~提醒发送失败~~~~", ex);
			}
		}
		
	}

}
