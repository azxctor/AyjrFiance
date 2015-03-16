package com.hengxin.platform.report;

/**
 * Data provider for reports.
 * 
 * @author DT82275(Binghua Wu)
 * 
 */
public interface ReportDataProvider {

	/**
	 * Gets provider specific configuration.
	 * 
	 * @return provider specific configuration
	 */
	ReportConfig getReportConfiguration();

	/**
	 * Gets data used to generate reports.
	 * 
	 * @return report data
	 */
	ReportData getReportData();

	/**
	 * Checks if there are more report data available. It's ONLY used when <b>PDF</b> report is
	 * generated against huge amount ( &gt; 1000 pages of PDF) of data.
	 * 
	 * @return true if it has more data
	 */
	boolean hasMoreDocument();
}
