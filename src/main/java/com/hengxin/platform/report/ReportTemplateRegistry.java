package com.hengxin.platform.report;



/**
 * Calculates template file location for reporting.
 * 
 * @author DT82275(Binghua Wu)
 * 
 */
public interface ReportTemplateRegistry {

	String PDF_EXT = "xhtml";
	String CSV_EXT = "txt";
	String EXCEL_EXT = "xml";
	String XML_EXT = "xml";

	/**
	 * Calculates the location of template file by given report context.
	 * 
	 * @param criteria
	 *            report criteria
	 * @return classpath (relative to application root) of the requested template
	 *         file
	 */
	String getTemplate(ReportCriteria criteria);

	/**
	 * Gets class path of report template files.
	 * 
	 * @return class path of folder where template files are placed
	 */
	String getTemplateHomePath();
}
