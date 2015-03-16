/*
 * Project Name: kmfex-platform
 * File Name: MemberView.java
 * Class Name: MemberView
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
import java.util.List;

import com.hengxin.platform.common.domain.DynamicOption;
import com.hengxin.platform.fund.domain.Acct;
import com.hengxin.platform.member.dto.FinancierDto;
import com.hengxin.platform.member.dto.InvestorDto;
import com.hengxin.platform.member.dto.OrganizationDto;
import com.hengxin.platform.member.dto.PersonDto;
import com.hengxin.platform.security.domain.User;

/**
 * Class Name: MemberApplicationAudit
 *
 * @author yingchangwang
 *
 */

public class MemberApplicationAuditDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PersonDto person;
    private OrganizationDto organization;
    private InvestorDto investorInfo;
    private FinancierDto financierInfo;
    private Acct account;
    private User user;
    private boolean changeInvestorLevel;
    private boolean changeFinancierLevel;
    List<DynamicOption> investorLevelList;
    List<DynamicOption> financierLevelList;
    private boolean basicApproved;

    public PersonDto getPerson() {
        return person;
    }

    public void setPerson(PersonDto person) {
        this.person = person;
    }

    public OrganizationDto getOrganization() {
        return organization;
    }

    /**
     * @param organization
     *            Set organization value
     */
    public void setOrganization(OrganizationDto organization) {
        this.organization = organization;
    }

    public InvestorDto getInvestorInfo() {
        return investorInfo;
    }

    /**
     *
     * @param investorInfo
     *            Set investorInfo value
     */
    public void setInvestorInfo(InvestorDto investorInfo) {
        this.investorInfo = investorInfo;
    }

    public FinancierDto getFinancierInfo() {
        return financierInfo;
    }

    /**
     *
     * @param financierInfo
     *            Set financierInfo value
     */
    public void setFinancierInfo(FinancierDto financierInfo) {
        this.financierInfo = financierInfo;
    }

    public Acct getAccount() {
        return account;
    }

    public void setAccount(Acct account) {
        this.account = account;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isChangeInvestorLevel() {
        return changeInvestorLevel;
    }

    public void setChangeInvestorLevel(boolean changeInvestorLevel) {
        this.changeInvestorLevel = changeInvestorLevel;
    }

    public boolean isChangeFinancierLevel() {
        return changeFinancierLevel;
    }

    public void setChangeFinancierLevel(boolean changeFinancierLevel) {
        this.changeFinancierLevel = changeFinancierLevel;
    }

    public List<DynamicOption> getInvestorLevelList() {
        return investorLevelList;
    }

    public void setInvestorLevelList(List<DynamicOption> investorLevelList) {
        this.investorLevelList = investorLevelList;
    }

    public List<DynamicOption> getFinancierLevelList() {
        return financierLevelList;
    }

    public void setFinancierLevelList(List<DynamicOption> financierLevelList) {
        this.financierLevelList = financierLevelList;
    }

    public boolean isBasicApproved() {
        return basicApproved;
    }

    public void setBasicApproved(boolean basicApproved) {
        this.basicApproved = basicApproved;
    }


}
