package com.hengxin.platform.report;

/**
 * ArgusHealth and HT. All rights reserved.
 */

import java.io.File;
import java.io.Serializable;
import java.text.MessageFormat;

import com.hengxin.platform.common.util.AppConfigUtil;

/**
 * @author Wang Qiang(DT82271)
 *
 */
public class ReportContext implements Serializable {
	
	private static final long serialVersionUID = 5L;
	
	private Object businessService;
	
	private ReportContext me = this;

	private ReportOutput output = new ReportOutput() {
		private String fileName;
		
		@Override
		public String getFileName() {
			if (fileName == null) {
				String folder = AppConfigUtil.getReportFilePath();
				File f = new File(folder, "./");

				if (!f.exists()) {
					f.mkdirs();
				}

				long requestId = System.currentTimeMillis();
				fileName = MessageFormat.format("{0}{1}_{2}.{3}", folder,
						me.getCriteria().getReportType().name(),
						String.valueOf(requestId), me.getCriteria().getOutputType()
								.getLabel());
			}
			return fileName;
		}
	};
	
	private ReportCriteria criteria;
	
	/**
	 * @return the output
	 */
	public ReportOutput getOutput() {
		return output;
	}

	/**
	 * @param output the output to set
	 */
	public void setOutput(ReportOutput output) {
		this.output = output;
	}

	/**
	 * @param criteria
	 *            the criteria to set
	 */
	public void setCriteria(ReportCriteria criteria) {
		this.criteria = criteria;
	}

	/**
	 * @return the criteria
	 */
	public ReportCriteria getCriteria() {
		return criteria;
	}
	
	@Override
	public String toString() {
		return "ReportContext [output=" + output + ", criteria=" + criteria + "]";
	}

	public Object getBusinessService() {
		return businessService;
	}

	public void setBusinessService(Object businessService) {
		this.businessService = businessService;
	}
	
}
