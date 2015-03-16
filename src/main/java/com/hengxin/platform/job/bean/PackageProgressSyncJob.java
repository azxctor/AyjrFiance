package com.hengxin.platform.job.bean;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.hengxin.platform.market.dto.MarketItemDto;
import com.hengxin.platform.market.service.FinancingResourceService;
import com.hengxin.platform.market.service.FinancingResourceService.ItemVisitor;

@DisallowConcurrentExecution
public class PackageProgressSyncJob extends QuartzJobBean {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PackageProgressSyncJob.class);

	@Autowired
	private transient FinancingResourceService resourceService;

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		for (MarketItemDto itemDto : resourceService.getAsList()) {
			long remainAmount = resourceService.get(itemDto.getId());
			if (remainAmount < 0) { // 已满额或申购结束
				continue;
			}
			BigDecimal progress = new BigDecimal(itemDto.getPackageQuota()
					.longValue() - remainAmount).divide(
					itemDto.getPackageQuota(), 3, RoundingMode.DOWN);
			progress = progress.min(BigDecimal.ONE);
			boolean changed = itemDto.getProgress() == null || progress.compareTo(itemDto.getProgress()) != 0;
			itemDto.setProgress(progress);
			LOGGER.debug("package: {}, progress: {}", itemDto.getId(), progress);
			if (changed) {
				final BigDecimal prog = progress;
				resourceService.updateItem(itemDto.getId(), new ItemVisitor() {
					@Override
					public void visit(MarketItemDto item) {
						item.setProgress(prog);
					}
				});
			}
		}
	}

}
