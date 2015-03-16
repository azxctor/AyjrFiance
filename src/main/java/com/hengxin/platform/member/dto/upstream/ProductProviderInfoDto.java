/*
 * Project Name: kmfex-platform
 * File Name: ProductProviderInfo.java
 * Class Name: ProductProviderInfo
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

import com.hengxin.platform.member.dto.BaseApplicationDto;
import com.hengxin.platform.security.domain.User;

/**
 * Class Name: ProductProviderInfo
 * 
 * @author runchen
 * 
 */
public class ProductProviderInfoDto extends BaseApplicationDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String applicationImg1;

    private String applicationImg2;

    private String privilegeLic;

    private String servRegionCd;

    private String servIndustryCd;

    private String agent;

    private AgencyApplicationDto agencyApplication;

    private User user;
    
    private String proSeverLevel;

    /**
     * @return return the value of the var ApplicationImg1
     */

    public String getApplicationImg1() {
        return applicationImg1;
    }

    /**
     * @param ApplicationImg1
     *            Set ApplicationImg1 value
     */

    public void setApplicationImg1(String ApplicationImg1) {
        this.applicationImg1 = ApplicationImg1;
    }

    /**
     * @return return the value of the var ApplicationImg2
     */

    public String getApplicationImg2() {
        return applicationImg2;
    }

    /**
     * @param applicationImg2
     *            Set ApplicationImg2 value
     */

    public void setApplicationImg2(String applicationImg2) {
        this.applicationImg2 = applicationImg2;
    }

	/**
	 * @return the agencyApplication
	 */
	public AgencyApplicationDto getAgencyApplication() {
		return agencyApplication;
	}

	/**
	 * @param agencyApplication the agencyApplication to set
	 */
	public void setAgencyApplication(AgencyApplicationDto agencyApplication) {
		this.agencyApplication = agencyApplication;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
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
    
    public String getServRegionCd() {
        return servRegionCd;
    }

    /**
     * @param servRegionCd Set servRegionCd value
     */
    
    public void setServRegionCd(String servRegionCd) {
        this.servRegionCd = servRegionCd;
    }

    /**
     * @return return the value of the var servIndustryCd
     */
    
    public String getServIndustryCd() {
        return servIndustryCd;
    }

    /**
     * @param servIndustryCd Set servIndustryCd value
     */
    
    public void setServIndustryCd(String servIndustryCd) {
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

    public String getProSeverLevel() {
        return proSeverLevel;
    }

    public void setProSeverLevel(String proSeverLevel) {
        this.proSeverLevel = proSeverLevel;
    }

}
