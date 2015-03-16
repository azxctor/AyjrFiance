package com.hengxin.platform.report.service;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hengxin.platform.report.EReportOutputType;
import com.hengxin.platform.report.EReportType;
import com.hengxin.platform.report.ReportContext;
import com.hengxin.platform.report.ReportCriteria;
import com.hengxin.platform.report.ReportDataProvider;
import com.hengxin.platform.report.ReportDataProviderFactory;
import com.hengxin.platform.report.ReportGenerator;
import com.hengxin.platform.report.ReportTemplateRegistry;
import com.hengxin.platform.report.ReportingException;
import com.hengxin.platform.report.data.AuthzdCtrTransFeeReportDataProvider;
import com.hengxin.platform.report.dto.AuthzdCtrTransFeeSearchDto;

@Service
public class ReportingService implements ReportDataProviderFactory, ReportTemplateRegistry {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReportingService.class);

	private static final String TEMPLATE_HOME = "/report/template/";

	@Override
	public ReportDataProvider createDataProvider(ReportContext ctx) {
		EReportType reportType = ctx.getCriteria().getReportType();
		switch (reportType) {
		case AuthzdCtrTransFee:
			AuthzdCtrTransFeeSearchDto searchDto = (AuthzdCtrTransFeeSearchDto) ctx.getCriteria().getSearchDto();
			AuthzdCtrTransFeeService service = (AuthzdCtrTransFeeService) ctx.getBusinessService();
			return new AuthzdCtrTransFeeReportDataProvider(searchDto, service);
		}
		return null;

	}

	@Override
	public String getTemplate(ReportCriteria criteria) {
		String fileName = null;
		EReportType reportType = criteria.getReportType();
		fileName = MessageFormat.format("{0}.{1}", reportType.name(), criteria.getOutputType() == EReportOutputType.Delimited ? CSV_EXT : PDF_EXT);
		return MessageFormat.format("{0}{1}", TEMPLATE_HOME, fileName);
	}

	@Override
	public String getTemplateHomePath() {
		return TEMPLATE_HOME;
	}

	public void createReport(ReportContext ctx) throws ReportingException {
		try {
			new ReportGenerator(ctx, this, this).generate();
		} catch (ReportingException e) {
			LOGGER.error("failed to create report");
			throw new ReportingException(ctx, e);
		}
	}
	
}
