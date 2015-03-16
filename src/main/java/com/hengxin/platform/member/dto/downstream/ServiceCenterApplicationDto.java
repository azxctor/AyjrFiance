/*
 * Project Name: kmfex-platform
 * File Name: ServiceCenterApplication.java
 * Class Name: ServiceCenterApplication
 *
 * Copyright 2014 Hengtian Software Inc
 *
 * Licensed under the Hengtiansoft
 *
 * http://www.hengtiansoft.com
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

/**
 * Class Name: ServiceCenterApplication Description: TODO
 *
 * @author chunlinwang
 *
 */

public class ServiceCenterApplicationDto extends AgencyApplicationDto // extends BaseApplicationDto
        implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String cooperationForm;

    private String cooperationBeginDate;

    private String cooperationEndDate;

    private String registrationPlace;

    private String registrationDate;

    private String registrationCapital;

    private String employeeNo;

    private String agent;// 经办人

    private BankAcct bankAcct;
    private Acct account;

    @OptionCategory(EOptionCategory.SERVICE_CENTER_LEVEL)
    private DynamicOption level;

    private EApplicationStatus serviceCenterStatus = EApplicationStatus.NULL;

    private String serviceCenterDesc;

    /**
     * @return return the value of the var cooperationForm
     */

    public String getCooperationForm() {
        return cooperationForm;
    }

    /**
     * @param cooperationForm
     *            Set cooperationForm value
     */

    public void setCooperationForm(String cooperationForm) {
        this.cooperationForm = cooperationForm;
    }

    /**
     * @return return the value of the var cooperationBeginDate
     */

    public String getCooperationBeginDate() {
        return cooperationBeginDate;
    }

    /**
     * @param cooperationBeginDate
     *            Set cooperationBeginDate value
     */

    public void setCooperationBeginDate(String cooperationBeginDate) {
        this.cooperationBeginDate = cooperationBeginDate;
    }

    /**
     * @return return the value of the var cooperationEndDate
     */

    public String getCooperationEndDate() {
        return cooperationEndDate;
    }

    /**
     * @param cooperationEndDate
     *            Set cooperationEndDate value
     */

    public void setCooperationEndDate(String cooperationEndDate) {
        this.cooperationEndDate = cooperationEndDate;
    }

    /**
     * @return return the value of the var registrationPlace
     */

    public String getRegistrationPlace() {
        return registrationPlace;
    }

    /**
     * @param registrationPlace
     *            Set registrationPlace value
     */

    public void setRegistrationPlace(String registrationPlace) {
        this.registrationPlace = registrationPlace;
    }

    /**
     * @return return the value of the var registrationDate
     */

    public String getRegistrationDate() {
        return registrationDate;
    }

    /**
     * @param registrationDate
     *            Set registrationDate value
     */

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    /**
     * @return return the value of the var registrationCapital
     */

    public String getRegistrationCapital() {
        return registrationCapital;
    }

    /**
     * @param registrationCapital
     *            Set registrationCapital value
     */

    public void setRegistrationCapital(String registrationCapital) {
        this.registrationCapital = registrationCapital;
    }

    /**
     * @return return the value of the var employeeNo
     */

    public String getEmployeeNo() {
        return employeeNo;
    }

    /**
     * @param employeeNo
     *            Set employeeNo value
     */

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
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
     * @return return the value of the var bankAcct
     */

    public BankAcct getBankAcct() {
        return bankAcct;
    }

    /**
     * @param bankAcct
     *            Set bankAcct value
     */

    public void setBankAcct(BankAcct bankAcct) {
        this.bankAcct = bankAcct;
    }

    /**
     * @return return the value of the var account
     */

    public Acct getAccount() {
        return account;
    }

    /**
     * @param account
     *            Set account value
     */

    public void setAccount(Acct account) {
        this.account = account;
    }

    /**
     * @return return the value of the var level
     */

    public DynamicOption getLevel() {
        return level;
    }

    /**
     * @param level
     *            Set level value
     */

    public void setLevel(DynamicOption level) {
        this.level = level;
    }

    /**
     * @return return the value of the var serviceCenterStatus
     */

    public EApplicationStatus getServiceCenterStatus() {
        return serviceCenterStatus;
    }

    /**
     * @param serviceCenterStatus
     *            Set serviceCenterStatus value
     */

    public void setServiceCenterStatus(EApplicationStatus serviceCenterStatus) {
        this.serviceCenterStatus = serviceCenterStatus;
    }

	public String getServiceCenterDesc() {
		return serviceCenterDesc;
	}

	public void setServiceCenterDesc(String serviceCenterDesc) {
		this.serviceCenterDesc = serviceCenterDesc;
	}

}
