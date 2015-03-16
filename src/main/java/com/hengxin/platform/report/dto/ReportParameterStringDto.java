package com.hengxin.platform.report.dto;

import java.io.Serializable;

public class ReportParameterStringDto implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String parameterString;

    public String getParameterString() {
        return parameterString;
    }
    
    public ReportParameterStringDto(){
    }
    
    public ReportParameterStringDto(String param){
    	this.parameterString = param;
    }

    public void setParameterString(String parameterString) {
        this.parameterString = parameterString;
    }

}
