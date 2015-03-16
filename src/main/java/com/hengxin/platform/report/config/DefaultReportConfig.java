package com.hengxin.platform.report.config;

import com.hengxin.platform.common.util.AppConfigUtil;
import com.hengxin.platform.report.ReportConfig;


/**
 * This is a default config class, include report generate folder.
 * Used by the ReportDataProvider.
 * 
 */
public class DefaultReportConfig
		implements ReportConfig {

	private static final int BATCH_SIZE = 2000;
	private static final int CUTTING = 7;

	@Override
	public int getBatchSize() {
		return BATCH_SIZE;
	}

	@Override
	public int getCuttingThreshold() {
		return CUTTING;
	}

	@Override
	public String getReportSavingFolder() {
		return AppConfigUtil.getReportFilePath();
	}

}
