/*
 * Project Name: kmfex-platform
 * File Name: ProductProviderInfoPo.java
 * Class Name: ProductProviderInfoPo
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hengxin.platform.security.entity.UserPo;

/**
 * Class Name: ProductProviderInfoPo
 * 
 * @author runchen
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "UM_PROD_SERV_INFO")
public class ProductProviderInfo implements Serializable {

    @Id
    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "APP_FORM_IMG1")
    private String applicationImg1;

    @Column(name = "APP_FORM_IMG2")
    private String applicationImg2;

    @Column(name = "PRIVILEGE_LIC")
    private String privilegeLic;

    @Column(name = "SERV_REGION_CD")
    private String servRegionCd;

    @Column(name = "SERV_INDUSTRY_CD")
    private String servIndustryCd;

    @Column(name = "AGENT")
    private String agent;

    @Column(name = "CREATE_OPID")
    private String creator;

    @Column(name = "LAST_MNT_OPID")
    private String lastMntOpid;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TS", updatable = false)
    private Date createTs;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_MNT_TS")
    private Date lastMntTs;
    
    @Column(name = "DESC_TX")
    private String desc;

    @OneToOne
    @JoinColumn(name = "USER_ID", insertable = false, updatable = false)
    private Agency agencyPo;

    @ManyToOne
    @JoinColumn(name = "USER_ID", insertable = false, updatable = false)
    private UserPo userPo;

    @Column(name = "PROD_SERV_LVL")
    private String proSeverLevel;
    
    @Column(name = "WRTR_CREDIT_FILE_ID")
	private String wrtrCreditFile;
    
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getLastMntOpid() {
        return lastMntOpid;
    }

    public void setLastMntOpid(String lastMntOpid) {
        this.lastMntOpid = lastMntOpid;
    }

    public Date getCreateTs() {
        return createTs;
    }

    public void setCreateTs(Date createTs) {
        this.createTs = createTs;
    }

    public Date getLastMntTs() {
        return lastMntTs;
    }

    public void setLastMntTs(Date lastMntTs) {
        this.lastMntTs = lastMntTs;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Agency getAgencyPo() {
        return agencyPo;
    }

    public void setAgencyPo(Agency agencyPo) {
        this.agencyPo = agencyPo;
    }

    public UserPo getUserPo() {
        return userPo;
    }

    public void setUserPo(UserPo userPo) {
        this.userPo = userPo;
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
