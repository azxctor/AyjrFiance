/*
 * Project Name: kmfex-platform
 * File Name: ServiceCenterApplicationViewPo.java
 * Class Name: ServiceCenterApplicationViewPo
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
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hengxin.platform.member.domain.converter.ApplicationStatusEnumConverter;
import com.hengxin.platform.member.domain.converter.MemberTypeEnumConverter;
import com.hengxin.platform.member.domain.converter.UserTypeEnumConverter;
import com.hengxin.platform.member.enums.EApplicationStatus;
import com.hengxin.platform.member.enums.EMemberType;
import com.hengxin.platform.member.enums.EUserType;

/**
 * Class Name: ServiceCenterApplicationViewPo
 * 
 * @author shengzhou
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "V_AUTHZD_APPL")
public class ServiceCenterApplicationView implements Serializable {

    @Id
    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "USER_TYPE")
    @Convert(converter = UserTypeEnumConverter.class)
    private EUserType userType;

    @Column(name = "NAME")
    private String name;

    @Column(name = "REGION_CD")
    private String region;

    @Column(name = "P_JOB")
    private String personJob;

    @Column(name = "P_EDUCN_CD")
    private String personEducation;

    @Column(name = "O_TYPE_CD")
    private String orgType;

    @Column(name = "O_INDUSTRY_CD")
    private String orgIndustry;

    @Column(name = "O_NATURE_CD")
    private String orgNature;

    @Column(name = "O_MAIN_BIZ")
    private String orgMainBusiness;

    @Column(name = "BASIC_STATUS")
    @Convert(converter = ApplicationStatusEnumConverter.class)
    private EApplicationStatus basicStatus;

    @Column(name = "FINANCIER_STATUS")
    @Convert(converter = ApplicationStatusEnumConverter.class)
    private EApplicationStatus financeStatus;

    @Column(name = "INVESTOR_STATUS")
    @Convert(converter = ApplicationStatusEnumConverter.class)
    private EApplicationStatus investorStatus;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LASTEST_TS")
    private Date lastestTs;
    
    @Column(name = "USER_ROLE")
    @Convert(converter = MemberTypeEnumConverter.class)
    private EMemberType userRole;

    @Column(name = "INVSTR_AGENT")
    private String investorAgent;
    
    @Column(name = "FNCR_AGENT")
    private String financeAgent;

    /**
     * @return return the value of the var userId
     */
    
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId Set userId value
     */
    
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return return the value of the var userType
     */
    
    public EUserType getUserType() {
        return userType;
    }

    /**
     * @param userType Set userType value
     */
    
    public void setUserType(EUserType userType) {
        this.userType = userType;
    }

    /**
     * @return return the value of the var name
     */
    
    public String getName() {
        return name;
    }

    /**
     * @param name Set name value
     */
    
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return return the value of the var region
     */
    
    public String getRegion() {
        return region;
    }

    /**
     * @param region Set region value
     */
    
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * @return return the value of the var personJob
     */
    
    public String getPersonJob() {
        return personJob;
    }

    /**
     * @param personJob Set personJob value
     */
    
    public void setPersonJob(String personJob) {
        this.personJob = personJob;
    }

    /**
     * @return return the value of the var personEducation
     */
    
    public String getPersonEducation() {
        return personEducation;
    }

    /**
     * @param personEducation Set personEducation value
     */
    
    public void setPersonEducation(String personEducation) {
        this.personEducation = personEducation;
    }

    /**
     * @return return the value of the var orgType
     */
    
    public String getOrgType() {
        return orgType;
    }

    /**
     * @param orgType Set orgType value
     */
    
    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    /**
     * @return return the value of the var orgIndustry
     */
    
    public String getOrgIndustry() {
        return orgIndustry;
    }

    /**
     * @param orgIndustry Set orgIndustry value
     */
    
    public void setOrgIndustry(String orgIndustry) {
        this.orgIndustry = orgIndustry;
    }

    /**
     * @return return the value of the var orgNature
     */
    
    public String getOrgNature() {
        return orgNature;
    }

    /**
     * @param orgNature Set orgNature value
     */
    
    public void setOrgNature(String orgNature) {
        this.orgNature = orgNature;
    }

    /**
     * @return return the value of the var orgMainBusiness
     */
    
    public String getOrgMainBusiness() {
        return orgMainBusiness;
    }

    /**
     * @param orgMainBusiness Set orgMainBusiness value
     */
    
    public void setOrgMainBusiness(String orgMainBusiness) {
        this.orgMainBusiness = orgMainBusiness;
    }

    /**
     * @return return the value of the var basicStatus
     */
    
    public EApplicationStatus getBasicStatus() {
        return basicStatus;
    }

    /**
     * @param basicStatus Set basicStatus value
     */
    
    public void setBasicStatus(EApplicationStatus basicStatus) {
        this.basicStatus = basicStatus;
    }

    /**
     * @return return the value of the var financeStatus
     */
    
    public EApplicationStatus getFinanceStatus() {
        return financeStatus;
    }

    /**
     * @param financeStatus Set financeStatus value
     */
    
    public void setFinanceStatus(EApplicationStatus financeStatus) {
        this.financeStatus = financeStatus;
    }

    /**
     * @return return the value of the var investorStatus
     */
    
    public EApplicationStatus getInvestorStatus() {
        return investorStatus;
    }

    /**
     * @param investorStatus Set investorStatus value
     */
    
    public void setInvestorStatus(EApplicationStatus investorStatus) {
        this.investorStatus = investorStatus;
    }

    /**
     * @return return the value of the var lastestTs
     */
    
    public Date getLastestTs() {
        return lastestTs;
    }

    /**
     * @param lastestTs Set lastestTs value
     */
    
    public void setLastestTs(Date lastestTs) {
        this.lastestTs = lastestTs;
    }

    /**
     * @return return the value of the var userRole
     */
    
    public EMemberType getUserRole() {
        return userRole;
    }

    /**
     * @param userRole Set userRole value
     */
    
    public void setUserRole(EMemberType userRole) {
        this.userRole = userRole;
    }

    /**
     * @return return the value of the var investorAgent
     */
    
    public String getInvestorAgent() {
        return investorAgent;
    }

    /**
     * @param investorAgent Set investorAgent value
     */
    
    public void setInvestorAgent(String investorAgent) {
        this.investorAgent = investorAgent;
    }

    /**
     * @return return the value of the var financeAgent
     */
    
    public String getFinanceAgent() {
        return financeAgent;
    }

    /**
     * @param financeAgent Set financeAgent value
     */
    
    public void setFinanceAgent(String financeAgent) {
        this.financeAgent = financeAgent;
    }
    
}
