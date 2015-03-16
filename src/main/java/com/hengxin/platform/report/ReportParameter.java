package com.hengxin.platform.report;

import java.io.Serializable;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;

@SuppressWarnings("serial")
public class ReportParameter implements Serializable {
    @JsonProperty(value = "PageName")
    private String pageName;

    @JsonProperty(value = "UId")
    private String uId;

    @JsonProperty(value = "RId")
    private String rId;

    @JsonProperty(value = "Filter")
    private Map<String, Object> filter;

    @JsonProperty(value = "Features")
    private Map<String, String> feature;

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getrId() {
        return rId;
    }

    public void setrId(String rId) {
        this.rId = rId;
    }

    public Map<String, Object> getFilter() {
        return filter;
    }

    public void setFilter(Map<String, Object> filter) {
        this.filter = filter;
    }

    public Map<String, String> getFeature() {
        return feature;
    }

    public void setFeature(Map<String, String> feature) {
        this.feature = feature;
    }

}
