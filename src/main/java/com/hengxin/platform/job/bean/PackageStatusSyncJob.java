package com.hengxin.platform.job.bean;

import java.util.List;
import java.util.Map;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.hengxin.platform.market.dto.MarketItemDto;
import com.hengxin.platform.market.service.FinancingResourceService;
import com.hengxin.platform.market.service.PackageSubscirbeService;

@DisallowConcurrentExecution
public class PackageStatusSyncJob extends QuartzJobBean {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PackageStatusSyncJob.class);
	private static final long PACKAGE_EXPIRE_TIME = 300000L; // 待签约融资包在缓存中的保留时间（毫秒）

	@Autowired
	private transient FinancingResourceService resourceService;

	@Autowired
	private transient PackageSubscirbeService subcribeService;

	public PackageStatusSyncJob() {

	}

	public PackageStatusSyncJob(FinancingResourceService resourceService,
			PackageSubscirbeService subcribeService) {
		this.resourceService = resourceService;
		this.subcribeService = subcribeService;
	}

	public void run() {
		try {
			executeInternal(null);
		} catch (JobExecutionException e) {
			LOGGER.error("error in run()", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		List<MarketItemDto> list = resourceService.getAsList();
		LOGGER.info(
				"synchronizing financing packages, total number of packages: {}",
				list.size());
		Map<String, Object[]> subscribeSumDetail = subcribeService
				.getSubscribeSumDetail();
		for (MarketItemDto item : list) {
			LOGGER.debug("synchronizing package: {}, progress: {}",
					item.getId(), item.getProgress());
			if (subscribeSumDetail.containsKey(item.getId())) {
				LOGGER.debug("new subscription detected for package: {}",
						item.getId());
				subcribeService.updatePackageDetail(subscribeSumDetail.get(item
						.getId()));
			}
			// 移除已签约的融资包
			if (item.getExpiredTime() > 0
					&& System.currentTimeMillis() - item.getExpiredTime() > PACKAGE_EXPIRE_TIME) {
				resourceService.removeItem(item.getId(), true);
			}
		}
	}

}
