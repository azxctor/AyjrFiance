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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hengxin.platform.common.dto.annotation.BusinessName;
import com.hengxin.platform.common.enums.EOptionCategory;
import com.hengxin.platform.security.entity.UserPo;

/**
 * Class Name: ServiceCenterInfoPo
 * 
 * @author runchen,chunlinwang
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "UM_AUTHZD_CTR_APPL")
public class ServiceCenterApplication extends BaseApplication implements Serializable {

    @BusinessName("合作形式")
    @Column(name = "COOP_FORM")
    private String cooperationForm;

    @BusinessName("合作协议起始日")
    @Temporal(TemporalType.DATE)
    @Column(name = "COOP_START_DT")
    private Date cooperationBeginDate;

    @BusinessName("合作协议截止日")
    @Temporal(TemporalType.DATE)
    @Column(name = "COOP_END_DT")
    private Date cooperationEndDate;

    @BusinessName("注册地")
    @Column(name = "REGIST_PLACE")
    private String registrationPlace;

    @BusinessName("注册时间")
    @Temporal(TemporalType.DATE)
    @Column(name = "REGIST_DT")
    private Date registrationDate;

    @BusinessName("注册资本")
    @Column(name = "REGIST_CAP")
    private String registrationCapital;

    @BusinessName("员工人数")
    @Column(name = "EMPLOYEE_NBR")
    private String employeeNo;

    @BusinessName("经办人")
    @Column(name = "AGENT")
    private String agent;// 经办人

    @BusinessName(value = "等级", optionCategory = EOptionCategory.SERVICE_CENTER_LEVEL)
    @Column(name = "AUTHZD_CTR_LVL")
    private String level;

//    @OneToOne
//    @JoinColumn(name = "APPL_ID", insertable = false, updatable = false)
//    private AgencyApplicationPo agencyApplication;

    @ManyToOne
    @JoinColumn(name = "USER_ID", insertable = false, updatable = false)
    private UserPo user;
    
    /**
     * version 20150317 渠道部编辑服务中心显示名称  新增字段
     */
    @Column(name = "FULL_NAME")
    private String serviceCenterDesc;

    @Override
    public ServiceCenterApplication clone() {
        ServiceCenterApplication clone = null;
        try {
            clone = (ServiceCenterApplication) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
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

	public String getServiceCenterDesc() {
		return serviceCenterDesc;
	}

	public void setServiceCenterDesc(String serviceCenterDesc) {
		this.serviceCenterDesc = serviceCenterDesc;
	}

}
