package com.hengxin.platform.report;


/**
 * Factory class to create data provider object by given context.
 * 
 * @author DT82275(Binghua Wu)
 * 
 */
public interface ReportDataProviderFactory {

	/**
	 * Creates data provider object by given context.
	 * 
	 * @param cxt
	 *            given criteria
	 * @return report data provider instance
	 */
	ReportDataProvider createDataProvider(ReportContext ctx);

}
