/*
 * Project Name: kmfex-platform
 * File Name: ProductProviderDto.java
 * Class Name: ProductProviderDto
 *
 * Copyright 2014 KMFEX Inc
 *
 * 
 *
 * http://www.kmfex.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hengxin.platform.member.dto.upstream;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import com.hengxin.platform.common.converter.OptionCategory;
import com.hengxin.platform.common.domain.DynamicOption;
import com.hengxin.platform.common.dto.upstream.AgencyRegisterInfoDto.SubmitAgency;
import com.hengxin.platform.common.enums.EOptionCategory;
import com.hengxin.platform.common.util.SystemDictUtil;

/**
 * Class Name: ProductProviderDto
 * 
 * @author shengzhou
 * 
 */
public class ProductProviderDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("application_img_1")
    private String applicationImg1;

    @JsonProperty("application_img_2")
    private String applicationImg2;

    @NotEmpty(groups = { SubmitAgency.class }, message = "{member.error.field.empty}")
    @JsonProperty("privilege_license")
    private String privilegeLic;

    @JsonProperty("service_region")
    @OptionCategory(EOptionCategory.REGION)
    private DynamicOption servRegionCd;
    
    @JsonProperty("service_industry")
    @OptionCategory(EOptionCategory.ORG_INDUSTRY)
    private DynamicOption servIndustryCd;

    @JsonProperty("agent")
    private String agent;
    
    @OptionCategory(EOptionCategory.PRODUCT_SERVICE_LEVEL)
    private DynamicOption level;
    
    @JsonProperty("desc")
    private String desc;
    
    @JsonProperty("wrtrCreditFile")
    private String wrtrCreditFile;

    @JsonProperty("org_province")
    public String getProvince() {
        return SystemDictUtil.getMultilayerDetail(servRegionCd, 3)[0];
    }

    @JsonProperty("org_city")
    public String getCity() {
        return SystemDictUtil.getMultilayerDetail(servRegionCd, 3)[1];
    }

    @JsonProperty("org_county")
    public String getCounty() {
        return SystemDictUtil.getMultilayerDetail(servRegionCd, 3)[2];
    }

    /**
     * @return return the value of the var applicationImg1
     */
    
    public String getApplicationImg1() {
        return applicationImg1;
    }

    /**
     * @param applicationImg1 Set applicationImg1 value
     */
    
    public void setApplicationImg1(String applicationImg1) {
        this.applicationImg1 = applicationImg1;
    }

    /**
     * @return return the value of the var applicationImg2
     */
    
    public String getApplicationImg2() {
        return applicationImg2;
    }

    /**
     * @param applicationImg2 Set applicationImg2 value
     */
    
    public void setApplicationImg2(String applicationImg2) {
        this.applicationImg2 = applicationImg2;
    }

    /**
     * @return return the value of the var privilegeLic
     */
    
    public String getPrivilegeLic() {
        return privilegeLic;
    }

    /**
     * @param privilegeLic Set privilegeLic value
     */
    
    public void setPrivilegeLic(String privilegeLic) {
        this.privilegeLic = privilegeLic;
    }

    /**
     * @return return the value of the var servRegionCd
     */
    
    public DynamicOption getServRegionCd() {
        return servRegionCd;
    }

    /**
     * @param servRegionCd Set servRegionCd value
     */
    
    public void setServRegionCd(DynamicOption servRegionCd) {
        this.servRegionCd = servRegionCd;
    }

    /**
     * @return return the value of the var servIndustryCd
     */
    
    public DynamicOption getServIndustryCd() {
        return servIndustryCd;
    }

    /**
     * @param servIndustryCd Set servIndustryCd value
     */
    
    public void setServIndustryCd(DynamicOption servIndustryCd) {
        this.servIndustryCd = servIndustryCd;
    }

    /**
     * @return return the value of the var agent
     */
    
    public String getAgent() {
        return agent;
    }

    /**
     * @param agent Set agent value
     */
    
    public void setAgent(String agent) {
        this.agent = agent;
    }

	/**
	 * @return the level
	 */
	public DynamicOption getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(DynamicOption level) {
		this.level = level;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getWrtrCreditFile() {
		return wrtrCreditFile;
	}

	public void setWrtrCreditFile(String wrtrCreditFile) {
		this.wrtrCreditFile = wrtrCreditFile;
	}

}
