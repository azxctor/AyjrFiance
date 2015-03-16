/**
 * Copyright ArgusHealth. All rights reserved.
 */
package com.hengxin.platform.report;


/**
 * Configurations for reporting.
 * 
 * @author DT82275(Binghua Wu)
 * 
 */
public interface ReportConfig {

	/**
	 * Default cutting threshold.
	 */
	int DEFAULT_CUTTING = 200;

	/**
	 * Default batch size.
	 */
	int DEFAULT_BATCH_SIZE = 100;

	/**
	 * Controls how many records at maximum returned from database for each round during
	 * reporting.
	 * 
	 * @return max. number of records per iteration
	 */
	int getBatchSize();

	/**
	 * Controls how many pages of data at maximum for a single PDF in reporting.
	 * 
	 * @return max. number of pages of data per iteration
	 */
	int getCuttingThreshold();

	/**
	 * Gets the place where to save report.
	 * 
	 * @return OS dependent path of the folder
	 */
	String getReportSavingFolder();
}
