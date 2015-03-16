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

package com.hengxin.platform.member.dto.downstream;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.hengxin.platform.common.converter.OptionCategory;
import com.hengxin.platform.common.domain.DynamicOption;
import com.hengxin.platform.common.enums.EOptionCategory;
import com.hengxin.platform.common.util.FileUploadUtil;
import com.hengxin.platform.fund.domain.Acct;
import com.hengxin.platform.fund.domain.BankAcct;
import com.hengxin.platform.member.dto.BaseApplicationDto;
import com.hengxin.platform.member.dto.upstream.AgencyApplicationDto;
import com.hengxin.platform.member.enums.EApplicationStatus;
import com.hengxin.platform.security.domain.User;

/**
 * Class Name: ProductProviderApplicationInfo
 *
 * @author Ryan
 *
 */
public class ProductProviderApplicationSearchDto extends BaseApplicationDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String applicationImg1;

    private String applicationImg2;

    private String privilegeLic;

    @OptionCategory(EOptionCategory.REGION)
    private DynamicOption servRegionCd;

    @OptionCategory(EOptionCategory.ORG_INDUSTRY)
    private DynamicOption servIndustryCd;

    private String agent;

    private AgencyApplicationDto agencyApplication;

    private User user;

    private BankAcct bankAccount;

    private Acct account;

    @OptionCategory(EOptionCategory.PRODUCT_SERVICE_LEVEL)
    private DynamicOption proSeverLevel;

    private EApplicationStatus productServiceStatus = EApplicationStatus.NULL;

    @OptionCategory(EOptionCategory.REGION)
    private DynamicOption region;

    private String applicationImg1Url;

    private String applicationImg2Url;
    
    private String desc;
    
    private String wrtrCreditFile;
    /**
     * @return return the value of the var region
     */

    public DynamicOption getRegion() {
        return region;
    }

    /**
     * @param region Set region value
     */

    public void setRegion(DynamicOption region) {
        this.region = region;
    }

    public String getPrivilegeLic() {
        return privilegeLic;
    }

    public void setPrivilegeLic(String privilegeLic) {
        this.privilegeLic = privilegeLic;
    }

    public DynamicOption getServRegionCd() {
        return servRegionCd;
    }

    public void setServRegionCd(DynamicOption servRegionCd) {
        this.servRegionCd = servRegionCd;
    }


    public DynamicOption getServIndustryCd() {
        return servIndustryCd;
    }

    public void setServIndustryCd(DynamicOption servIndustryCd) {
        this.servIndustryCd = servIndustryCd;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AgencyApplicationDto getAgencyApplication() {
        return agencyApplication;
    }

    public void setAgencyApplication(AgencyApplicationDto agencyApplication) {
        this.agencyApplication = agencyApplication;
    }

    public DynamicOption getProSeverLevel() {
        return proSeverLevel;
    }

    public void setProSeverLevel(DynamicOption proSeverLevel) {
        this.proSeverLevel = proSeverLevel;
    }

    public BankAcct getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAcct bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Acct getAccount() {
        return account;
    }

    public void setAccount(Acct account) {
        this.account = account;
    }


    /**
    * @return return the value of the var productServiceStatus
    */

    public EApplicationStatus getProductServiceStatus() {
        return productServiceStatus;
    }


    /**
    * @param productServiceStatus Set productServiceStatus value
    */

    public void setProductServiceStatus(EApplicationStatus productServiceStatus) {
        this.productServiceStatus = productServiceStatus;
    }

    public String getApplicationImg1Url() {
        if (StringUtils.isNotBlank(applicationImg1)) {
            return FileUploadUtil.getTempFolder() + applicationImg1;
        }
        return "";    }

    public void setApplicationImg1Url(String applicationImg1Url) {
        this.applicationImg1Url = applicationImg1Url;
    }

    public String getApplicationImg2Url() {
        if (StringUtils.isNotBlank(applicationImg2)) {
            return FileUploadUtil.getTempFolder() + applicationImg2;
        }
        return "";    }

    public void setApplicationImg2Url(String applicationImg2Url) {
        this.applicationImg2Url = applicationImg2Url;
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
