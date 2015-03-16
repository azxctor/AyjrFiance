package com.hengxin.platform.report;


/**
 * Describes the output requirement for report.
 * 
 * @author DT82275(Binghua Wu)
 * 
 */
public interface ReportOutput {

	/**
	 * Gets full path of the file.
	 * 
	 * @return OS dependent path
	 */
	String getFileName();
}
