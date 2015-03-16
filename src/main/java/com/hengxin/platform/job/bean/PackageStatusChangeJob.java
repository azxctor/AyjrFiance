package com.hengxin.platform.job.bean;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.hengxin.platform.product.service.PackageJobService;

@DisallowConcurrentExecution
public class PackageStatusChangeJob extends QuartzJobBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(PackageStatusChangeJob.class);

    @Autowired
    @Qualifier("packageStatusChangeJobBean")
    private Object bean;

    @Autowired
    private PackageJobService service;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        LOGGER.info("packageStatusChangeJob invoked");

        // 包状态变更，预发布—>代申购
        try {
            service.preannouncePackages();
        } catch (Exception e) {
            LOGGER.info("Process Failed.", e);
        }
        // 包状态变更，待申购 —>申购中
        try {
            service.releasePackages();
        } catch (Exception e) {
            LOGGER.info("Process Failed.", e);
        }
        // 包状态变更，申购中—>待签约
        try {
            service.endPackages();
        } catch (Exception e) {
            LOGGER.info("Process Failed.", e);
        }
        LOGGER.info("packageStatusChangeJob completed");
    }

}
