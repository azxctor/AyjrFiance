/*
 * Project Name: kmfex-platform
 * File Name: SysDictPo.java
 * Class Name: SysDictPo
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

package com.hengxin.platform.common.domain;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

import com.hengxin.platform.common.dto.upstream.AgencyRegisterInfoDto.SubmitAgency;
import com.hengxin.platform.common.dto.upstream.RegisterDto.RegisterGroup;
import com.hengxin.platform.common.util.SystemDictUtil;
import com.hengxin.platform.member.dto.FinancierDto.SubmitFinance;
import com.hengxin.platform.member.dto.OrganizationDto.SubmitOrg;
import com.hengxin.platform.member.dto.PersonDto.SubmitPerson;

/**
 * Class Name: SysDictPo Description: TODO
 * 
 * @author rczhan
 * 
 */

@SuppressWarnings("serial")
public class DynamicOption implements Serializable {

    private Long id;

    private String category;

    @NotEmpty(message = "{member.error.field.empty}", groups = { RegisterGroup.class, SubmitPerson.class,
            SubmitOrg.class, SubmitFinance.class, SubmitAgency.class })
    private String code;

    private String text;

    private String longText;

    private String enabled;

    private String parentCode;

    private String leaf;

    private Long order;

    // just used to display long text with parent long text in page
    private String fullText;

    public DynamicOption() {

    }

    public DynamicOption(String category, String code, String text) {
        this.category = category;
        this.code = code;
        this.setText(text);
    }

    public String getFullText() {
        fullText = SystemDictUtil.getFullTextWithParent(this);
        return fullText;
    }

    /**
     * @return return the value of the var id
     */

    public Long getId() {
        return id;
    }

    /**
     * @param id
     *            Set id value
     */

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return return the value of the var category
     */

    public String getCategory() {
        return category;
    }

    /**
     * @param category
     *            Set category value
     */

    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @return return the value of the var code
     */

    public String getCode() {
        return code;
    }

    /**
     * @param code
     *            Set code value
     */

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return return the value of the var text
     */

    public String getText() {
        return text;
    }

    /**
     * @param text
     *            Set text value
     */

    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return return the value of the var longText
     */

    public String getLongText() {
        return longText;
    }

    /**
     * @param longText
     *            Set longText value
     */

    public void setLongText(String longText) {
        this.longText = longText;
    }

    /**
     * @return return the value of the var enabled
     */

    public String getEnabled() {
        return enabled;
    }

    /**
     * @param enabled
     *            Set enabled value
     */

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    /**
     * @return return the value of the var parentCode
     */

    public String getParentCode() {
        return parentCode;
    }

    /**
     * @param parentCode
     *            Set parentCode value
     */

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    /**
     * @return return the value of the var leaf
     */

    public String getLeaf() {
        return leaf;
    }

    /**
     * @param leaf
     *            Set leaf value
     */

    public void setLeaf(String leaf) {
        this.leaf = leaf;
    }

    /**
     * @return return the value of the var order
     */

    public Long getOrder() {
        return order;
    }

    /**
     * @param order
     *            Set order value
     */

    public void setOrder(Long order) {
        this.order = order;
    }
}
