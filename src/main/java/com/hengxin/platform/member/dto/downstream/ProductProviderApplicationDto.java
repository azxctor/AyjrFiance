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

import com.hengxin.platform.common.converter.OptionCategory;
import com.hengxin.platform.common.domain.DynamicOption;
import com.hengxin.platform.common.enums.EOptionCategory;
import com.hengxin.platform.fund.domain.Acct;
import com.hengxin.platform.fund.domain.BankAcct;
import com.hengxin.platform.member.dto.upstream.AgencyApplicationDto;
import com.hengxin.platform.member.enums.EApplicationStatus;
import com.hengxin.platform.security.domain.User;

/**
 * Class Name: ProductProviderApplicationInfo
 *
 * @author Ryan
 *
 */
public class ProductProviderApplicationDto extends AgencyApplicationDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String productPrivilegeLic;

    @OptionCategory(EOptionCategory.REGION)
    private DynamicOption productServiceRegion;

    @OptionCategory(EOptionCategory.ORG_INDUSTRY)
    private DynamicOption productServiceRegionIndustry;

    private String productServiceAgent;

    private User user;

    private BankAcct bank;

    private Acct account;
    
    private String desc;

    @OptionCategory(EOptionCategory.PRODUCT_SERVICE_LEVEL)
    private DynamicOption proSeverLevel;

    private EApplicationStatus productServiceStatus = EApplicationStatus.NULL;

    public String getProductPrivilegeLic() {
        return productPrivilegeLic;
    }

    public void setProductPrivilegeLic(String productPrivilegeLic) {
        this.productPrivilegeLic = productPrivilegeLic;
    }

    public DynamicOption getProductServiceRegion() {
        return productServiceRegion;
    }

    public void setProductServiceRegion(DynamicOption productServiceRegion) {
        this.productServiceRegion = productServiceRegion;
    }

    public DynamicOption getProductServiceRegionIndustry() {
        return productServiceRegionIndustry;
    }

    public void setProductServiceRegionIndustry(DynamicOption productServiceRegionIndustry) {
        this.productServiceRegionIndustry = productServiceRegionIndustry;
    }

    public String getProductServiceAgent() {
        return productServiceAgent;
    }

    public void setProductServiceAgent(String productServiceAgent) {
        this.productServiceAgent = productServiceAgent;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DynamicOption getProSeverLevel() {
        return proSeverLevel;
    }

    public void setProSeverLevel(DynamicOption proSeverLevel) {
        this.proSeverLevel = proSeverLevel;
    }

    public BankAcct getBank() {
        return bank;
    }

    public void setBank(BankAcct bank) {
        this.bank = bank;
    }

    public Acct getAccount() {
        return account;
    }

    public void setAccount(Acct account) {
        this.account = account;
    }

    public EApplicationStatus getProductServiceStatus() {
        return productServiceStatus;
    }

    public void setProductServiceStatus(EApplicationStatus productServiceStatus) {
        this.productServiceStatus = productServiceStatus;
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
}
