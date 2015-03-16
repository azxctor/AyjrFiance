/*
 * Project Name: kmfex-platform
 * File Name: ServiceCenterInfoPo.java
 * Class Name: ServiceCenterInfoPo
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
 * Class Name: ServiceCenterInfoPo
 * 
 * @author runchen,chunlinwang
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "UM_AUTHZD_CTR_INFO")
public class ServiceCenterInfo implements Serializable {

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

    @Column(name = "COOP_FORM")
    private String cooperationForm;

    @Column(name = "COOP_START_DT")
    private Date cooperationBeginDate;

    @Column(name = "COOP_END_DT")
    private Date cooperationEndDate;

    @Column(name = "REGIST_PLACE")
    private String registrationPlace;

    @Column(name = "REGIST_DT")
    private Date registrationDate;

    @Column(name = "REGIST_CAP")
    private String registrationCapital;

    @Column(name = "EMPLOYEE_NBR")
    private String employeeNo;

    @Column(name = "AGENT")
    private String agent;// 经办人

    @Column(name = "AUTHZD_CTR_LVL")
    private String level;

    @Column(name = "FULL_NAME")
    private String serviceCenterDesc;

    @OneToOne
    @JoinColumn(name = "USER_ID", insertable = false, updatable = false)
    private Agency agencyApplication;

    @ManyToOne
    @JoinColumn(name = "USER_ID", insertable = false, updatable = false)
    private UserPo user;

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

    public String getCooperationForm() {
        return cooperationForm;
    }

    public void setCooperationForm(String cooperationForm) {
        this.cooperationForm = cooperationForm;
    }

    public Date getCooperationBeginDate() {
        return cooperationBeginDate;
    }

    public void setCooperationBeginDate(Date cooperationBeginDate) {
        this.cooperationBeginDate = cooperationBeginDate;
    }

    public Date getCooperationEndDate() {
        return cooperationEndDate;
    }

    public void setCooperationEndDate(Date cooperationEndDate) {
        this.cooperationEndDate = cooperationEndDate;
    }

    public String getRegistrationPlace() {
        return registrationPlace;
    }

    public void setRegistrationPlace(String registrationPlace) {
        this.registrationPlace = registrationPlace;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getRegistrationCapital() {
        return registrationCapital;
    }

    public void setRegistrationCapital(String registrationCapital) {
        this.registrationCapital = registrationCapital;
    }

    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public Agency getAgencyApplication() {
        return agencyApplication;
    }

    public void setAgencyApplication(Agency agencyApplication) {
        this.agencyApplication = agencyApplication;
    }

    public UserPo getUser() {
        return user;
    }

    public void setUser(UserPo user) {
        this.user = user;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

	/**
	 * @return the serviceCenterDesc
	 */
	public String getServiceCenterDesc() {
		return serviceCenterDesc;
	}

	/**
	 * @param serviceCenterDesc the serviceCenterDesc to set
	 */
	public void setServiceCenterDesc(String serviceCenterDesc) {
		this.serviceCenterDesc = serviceCenterDesc;
	}

}
