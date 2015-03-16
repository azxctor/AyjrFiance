/*
 * Project Name: kmfex-platform
 * File Name: InvestorApplicationPo.java
 * Class Name: InvestorApplicationPo
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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.hengxin.platform.common.dto.annotation.BusinessName;
import com.hengxin.platform.common.enums.EOptionCategory;

/**
 * Class Name: InvestorApplicationPo
 * 
 * @author runchen
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "UM_INVSTR_APPL")
public class InvestorApplication extends BaseApplication implements Serializable {

    @BusinessName("会员申请表第一页")
    @Column(name = "APP_FORM_IMG1")
    private String applicationImg1;

    @BusinessName("会员申请表第二页")
    @Column(name = "APP_FORM_IMG2")
    private String applicationImg2;

    @BusinessName("经办人")
    @Column(name = "AGENT")
    private String agent;

    @BusinessName("介绍人")
    @Column(name = "AGENT_NAME")
    private String agentName;

    @BusinessName("授权服务中心")
    @Column(name = "AUTHZD_CTR_ID")
    private String  authCenterId;

    @BusinessName(value = "会员等级", optionCategory = EOptionCategory.INVESTOR_LEVEL)
    @Column(name = "INVSTR_LVL_SW")
    private String investorLevel;

    @Override
    public InvestorApplication clone() {
        InvestorApplication clone = null;
        try {
            clone = (InvestorApplication) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
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

    public String getAuthCenterId() {
        return authCenterId;
    }

    /**
     * @param authCenterId
     */
    public void setAuthCenterId(String authCenterId) {
        this.authCenterId = authCenterId;
    }

    public String getInvestorLevel() {
        return investorLevel;
    }

    /**
     * @param investorLevel
     */
    public void setInvestorLevel(String investorLevel) {
        this.investorLevel = investorLevel;
    }

	/**
	 * @return the agentName
	 */
	public String getAgentName() {
		return agentName;
	}

	/**
	 * @param agentName the agentName to set
	 */
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

}
