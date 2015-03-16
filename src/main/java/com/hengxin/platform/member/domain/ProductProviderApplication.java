/*
 * Project Name: kmfex-platform
 * File Name: ProductProviderApplicationPo.java
 * Class Name: ProductProviderApplicationPo
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

package com.hengxin.platform.member.domain;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.hengxin.platform.common.dto.annotation.BusinessName;
import com.hengxin.platform.common.enums.EOptionCategory;
import com.hengxin.platform.security.entity.UserPo;

/**
 * Class Name: ProductProviderApplicationPo
 *
 * @author runchen
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "UM_PROD_SERV_APPL")
public class ProductProviderApplication extends BaseApplication implements Serializable {

    @Column(name = "APP_FORM_IMG1")
    private String applicationImg1;

    @Column(name = "APP_FORM_IMG2")
    private String applicationImg2;

    @BusinessName("特许经营许可证")
    @Column(name = "PRIVILEGE_LIC")
    private String privilegeLic;

    @BusinessName(value = "服务地区范围", optionCategory = EOptionCategory.REGION)
    @Column(name = "SERV_REGION_CD")
    private String servRegionCd;

    @BusinessName(value = "服务行业范围", optionCategory = EOptionCategory.ORG_INDUSTRY)
    @Column(name = "SERV_INDUSTRY_CD")
    private String servIndustryCd;

    @BusinessName("经办人")
    @Column(name = "AGENT")
    private String agent;

    @BusinessName(value = "等级", optionCategory = EOptionCategory.PRODUCT_SERVICE_LEVEL)
    @Column(name = "PROD_SERV_LVL")
    private String proSeverLevel;
    
    @Column(name = "DESC_TX")
    private String desc;
    
    @Column(name = "WRTR_CREDIT_FILE_ID")
	private String wrtrCreditFile;

    @ManyToOne
    @JoinColumn(name = "USER_ID", insertable = false, updatable = false)
    private UserPo user;

    @OneToOne(cascade={CascadeType.DETACH})
    @JoinColumn(name = "APPL_ID", insertable = false, updatable = false)
    private AgencyApplication agencyApplication;

    @Override
    public ProductProviderApplication clone() {
        ProductProviderApplication clone = null;
        try {
            clone = (ProductProviderApplication) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }

    /**
     * @return return the value of the var agencyApplication
     */

    public AgencyApplication getAgencyApplication() {
        return agencyApplication;
    }

    /**
     * @param agencyApplication Set agencyApplication value
     */

    public void setAgencyApplication(AgencyApplication agencyApplication) {
        this.agencyApplication = agencyApplication;
    }

    public UserPo getUser() {
        return user;
    }

    public void setUserPo(UserPo user) {
        this.user = user;
    }

    public String getApplicationImg1() {
        return applicationImg1;
    }

    public void setApplicationImg1(String applicationImg1) {
        this.applicationImg1 = applicationImg1;
    }

    public String getApplicationImg2() {
        return applicationImg2;
    }

    public void setApplicationImg2(String applicationImg2) {
        this.applicationImg2 = applicationImg2;
    }

    public String getPrivilegeLic() {
        return privilegeLic;
    }

    public void setPrivilegeLic(String privilegeLic) {
        this.privilegeLic = privilegeLic;
    }

    public String getServRegionCd() {
        return servRegionCd;
    }

    public void setServRegionCd(String servRegionCd) {
        this.servRegionCd = servRegionCd;
    }

    public String getServIndustryCd() {
        return servIndustryCd;
    }

    public void setServIndustryCd(String servIndustryCd) {
        this.servIndustryCd = servIndustryCd;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    /**
     * @param user
     *            Set user value
     */

    public void setUser(UserPo user) {
        this.user = user;
    }

    public String getProSeverLevel() {
        return proSeverLevel;
    }

    public void setProSeverLevel(String proSeverLevel) {
        this.proSeverLevel = proSeverLevel;
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
