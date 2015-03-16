/*
 * Project Name: kmfex-platform
 * File Name: MyCreditorRightsView.java
 * Class Name: MyCreditorRightsView
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

package com.hengxin.platform.product.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Class Name: MyCreditorRightsView Description:
 * 
 * @author yingchangwang
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "V_MY_CREDITOR")
public class MyCreditorRightsView implements Serializable {
    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "LOT_ID")
    private String lotId;

    @Column(name = "SUBS_DT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date subsDate;

    @Column(name = "UNIT")
    private Long unit;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "LOT_BUY_PRICE")
    private BigDecimal lotBuyPrice;

    @ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.DETACH })
    @JoinColumn(name = "PKG_ID", insertable = false, updatable = false)
    private FinancingPackageView financingPackageView;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLotId() {
        return lotId;
    }

    public void setLotId(String lotId) {
        this.lotId = lotId;
    }

    public Date getSubsDate() {
        return subsDate;
    }

    public void setSubsDate(Date subsDate) {
        this.subsDate = subsDate;
    }

    public Long getUnit() {
        return unit;
    }

    public void setUnit(Long unit) {
        this.unit = unit;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getLotBuyPrice() {
        return lotBuyPrice;
    }

    public void setLotBuyPrice(BigDecimal lotBuyPrice) {
        this.lotBuyPrice = lotBuyPrice;
    }

    public FinancingPackageView getFinancingPackageView() {
        return financingPackageView;
    }

    public void setFinancingPackageView(FinancingPackageView financingPackageView) {
        this.financingPackageView = financingPackageView;
    }
}
