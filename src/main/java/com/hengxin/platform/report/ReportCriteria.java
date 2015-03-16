package com.hengxin.platform.report;


/**
 * Criteria of reporting. Application (mostly) needs to extend this interface to provide more
 * criteria.
 * 
 * @author DT82275(Binghua Wu)
 * 
 */
public class ReportCriteria {

	/**
	 * Gets the report format.
	 * 
	 * @return the requested format, see {@link EReportOutputType}
	 */
	private EReportOutputType outputType;
	
	private EReportType reportType;
	
	private Object searchDto;

	public EReportOutputType getOutputType() {
		return outputType;
	}

	public void setOutputType(EReportOutputType outputType) {
		this.outputType = outputType;
	}

	public EReportType getReportType() {
		return reportType;
	}

	public void setReportType(EReportType reportType) {
		this.reportType = reportType;
	}

	public Object getSearchDto() {
		return searchDto;
	}

	public void setSearchDto(Object searchDto) {
		this.searchDto = searchDto;
	}

}
