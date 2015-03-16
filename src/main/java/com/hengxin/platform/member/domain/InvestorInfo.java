/*
 * Project Name: kmfex-platform
 * File Name: InvestorInfoPo.java
 * Class Name: InvestorInfoPo
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
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Class Name: InvestorInfoPo
 * 
 * @author runchen
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "UM_INVSTR_INFO")
public class InvestorInfo implements Serializable {

    @Id
    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "CREATE_OPID")
    private String creator;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TS", updatable = false)
    private Date createTs;

    @Column(name = "LAST_MNT_OPID")
    private String lastMntOpid;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_MNT_TS")
    private Date lastMntTs;

    @Column(name = "APP_FORM_IMG1")
    private String applicationImg1;

    @Column(name = "APP_FORM_IMG2")
    private String applicationImg2;

    @Column(name = "AGENT")
    private String agent;

    @Column(name = "AGENT_NAME")
    private String agentName;

    @Column(name = "AUTHZD_CTR_ID")
    private String authCenterId;

    /**
     * latest investor level.
     */
    @Column(name = "INVSTR_LVL_SW")
    private String investorLevel;

    @Column(name = "INVSTR_LVL")
    private String investorLevelOriginal;

    /**
     * @return return the value of the var userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId
     *            Set userId value
     */

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateTs() {
        return createTs;
    }

    public void setCreateTs(Date createTs) {
        this.createTs = createTs;
    }

    public String getLastMntOpid() {
        return lastMntOpid;
    }

    public void setLastMntOpid(String lastMntOpid) {
        this.lastMntOpid = lastMntOpid;
    }

    public Date getLastMntTs() {
        return lastMntTs;
    }

    public void setLastMntTs(Date lastMntTs) {
        this.lastMntTs = lastMntTs;
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
     * @param agent
     *            Set agent value
     */

    public void setAgent(String agent) {
        this.agent = agent;
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

	/**
     * @return return the value of the var authCenterId
     */

    public String getAuthCenterId() {
        return authCenterId;
    }

    /**
     * @param authCenterId
     *            Set authCenterId value
     */

    public void setAuthCenterId(String authCenterId) {
        this.authCenterId = authCenterId;
    }

    /**
     * @return return the value of the var investorLevel
     */
    public String getInvestorLevel() {
        return investorLevel;
    }

    /**
     * @param investorLevel
     *            Set investorLevel value
     */
    public void setInvestorLevel(String investorLevel) {
        this.investorLevel = investorLevel;
    }

	public String getInvestorLevelOriginal() {
		return investorLevelOriginal;
	}

	public void setInvestorLevelOriginal(String investorLevelOriginal) {
		this.investorLevelOriginal = investorLevelOriginal;
	}

}
