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

package com.hengxin.platform.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Class Name: SysDictPo Description: TODO
 * 
 * @author rczhan
 * 
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "GL_DICT")
public class DynamicOptionPo implements Serializable {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "CODE")
    private String code;

    @Column(name = "TEXT_SHORT")
    private String text;

    @Column(name = "TEXT_LONG")
    private String longText;

    @Column(name = "ENABLED")
    private String enabled;

    @Column(name = "PARENT_CODE")
    private String parentCode;

    @Column(name = "LEAF")
    private String leaf;

    @Column(name = "SORT_ORDER")
    private Long order;

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
	 * @return the parentCode
	 */
	public String getParentCode() {
		return parentCode;
	}

	/**
	 * @param parentCode the parentCode to set
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
