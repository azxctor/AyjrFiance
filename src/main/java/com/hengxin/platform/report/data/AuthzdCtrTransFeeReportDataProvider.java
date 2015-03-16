package com.hengxin.platform.report.data;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.util.AppConfigUtil;
import com.hengxin.platform.common.util.NumberUtil;
import com.hengxin.platform.report.ReportConfig;
import com.hengxin.platform.report.ReportData;
import com.hengxin.platform.report.ReportDataProvider;
import com.hengxin.platform.report.config.DefaultReportConfig;
import com.hengxin.platform.report.dto.AuthzdCtrTransFeeDto;
import com.hengxin.platform.report.dto.AuthzdCtrTransFeeSearchDto;
import com.hengxin.platform.report.service.AuthzdCtrTransFeeService;

public class AuthzdCtrTransFeeReportDataProvider implements ReportData, ReportDataProvider {
	
	private AuthzdCtrTransFeeSearchDto searchDto;
	
	private List<AuthzdCtrTransFeeDto> fees;
	
	private AuthzdCtrTransFeeService authzdCtrTransFeeService;
	
	public AuthzdCtrTransFeeReportDataProvider(
			AuthzdCtrTransFeeSearchDto searchDto,
			AuthzdCtrTransFeeService authzdCtrTransFeeService) {
		super();
		this.searchDto = searchDto;
		this.authzdCtrTransFeeService = authzdCtrTransFeeService;
	}

	@Override
	public ReportConfig getReportConfiguration() {
		return new DefaultReportConfig();
	}

	@Override
	public ReportData getReportData() {
		return this;
	}

	@Override
	public boolean hasMoreDocument() {
		return false;
	}
	
	public Iterable<Collection<AuthzdCtrTransFeeDto>> getFees() {
		return new Iterable<Collection<AuthzdCtrTransFeeDto>>() {
			@Override
			public Iterator<Collection<AuthzdCtrTransFeeDto>> iterator() {
				return new Iterator<Collection<AuthzdCtrTransFeeDto>>() {
					private int start = 0;

					@Override
					public boolean hasNext() {
						return fees == null || fees.size() == AppConfigUtil.getReportBatchSize();
					}

					@Override
					public Collection<AuthzdCtrTransFeeDto> next() {
						searchDto.setDisplayStart(start);
						searchDto.setDisplayLength(AppConfigUtil.getReportBatchSize());
						DataTablesResponseDto<AuthzdCtrTransFeeDto> result = authzdCtrTransFeeService.getAuthzdCtrTransFeeItems(searchDto);
						
						start += AppConfigUtil.getReportBatchSize();
						
						fees = result.getData();
						return fees;
					}

					@Override
					public void remove() {
					}
					
				};
			}
		};
	}
	
	public String getTotalFee() {
		BigDecimal fee = authzdCtrTransFeeService.getTotalFee(searchDto);
		return NumberUtil.formatMoney(fee);
	}
	
	public String getAmtForDisplay(BigDecimal amt) {
		if (amt == null) {
			return "0.00";
		}
		return NumberUtil.formatMoney(amt);
	}
	
}
