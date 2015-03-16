/*
 * Project Name: kmfex-platform
 * File Name: PackageSubscribes.java
 * Class Name: PackageSubscribes
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
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hengxin.platform.common.entity.id.IdInjectionEntityListener;
import com.hengxin.platform.product.domain.converter.BooleanStringConverter;
import com.hengxin.platform.security.entity.UserPo;

/**
 * 融资包申购 Class Name: PackageSubscribes Description: TODO
 * 
 * @author tingwang
 * 
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "FP_PKG_SUBS")
@EntityListeners(IdInjectionEntityListener.class)
public class PackageSubscribes extends BaseInfo implements Serializable {

    @Id
    @Column(name = "SUBS_ID")
    private String id;

    @Column(name = "PKG_ID")
    private String pkgId;

    /**
     * 申购用户
     */
    @Column(name = "USER_ID")
    private String userId;
    /**
     * 申购份数
     */
    @Column(name = "UNIT")
    private Integer unit;
    /**
     * 每份金额
     */
    @Column(name = "UNIT_AMT")
    private BigDecimal unitAmt;
    /**
     * 资金保留流水号
     */
    @Column(name = "RSRV_JNL_NO")
    private String reserveJnlNo;
    /**
     * 费用保留流水号
     */
    @Column(name = "FEE_RSRV_JNL_NO")
    private String feeReserveJnlNo;

    /**
     * 是否自动申购产生
     */
    @Column(name = "AUTO_SUBS_FLG")
    @Convert(converter = BooleanStringConverter.class)
    private Boolean autoSubscribe = false;

    /**
     * 申购日期
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "SUBS_DT")
    private Date subscribeDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH })
    @JoinColumn(name = "PKG_ID", insertable = false, updatable = false)
    private FinancingPackageView financingPackageView;

    @ManyToOne
    @JoinColumn(name = "USER_ID", insertable = false, updatable = false)
    private UserPo user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPkgId() {
        return pkgId;
    }

    public void setPkgId(String pkgId) {
        this.pkgId = pkgId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getUnit() {
        return unit;
    }

    public void setUnit(Integer unit) {
        this.unit = unit;
    }

    public BigDecimal getUnitAmt() {
        return unitAmt;
    }

    public void setUnitAmt(BigDecimal unitAmt) {
        this.unitAmt = unitAmt;
    }

    public String getReserveJnlNo() {
        return reserveJnlNo;
    }

    public void setReserveJnlNo(String reserveJnlNo) {
        this.reserveJnlNo = reserveJnlNo;
    }

    /**
     * @return return the value of the var financingPackageView
     */

    public FinancingPackageView getFinancingPackageView() {
        return financingPackageView;
    }

    /**
     * @param financingPackageView
     *            Set financingPackageView value
     */

    public void setFinancingPackageView(FinancingPackageView financingPackageView) {
        this.financingPackageView = financingPackageView;
    }

    public UserPo getUser() {
        return user;
    }

    public void setUser(UserPo user) {
        this.user = user;
    }

    public Boolean getAutoSubscribe() {
        return autoSubscribe;
    }

    public void setAutoSubscribe(Boolean autoSubscribe) {
        this.autoSubscribe = autoSubscribe;
    }

    public Date getSubscribeDate() {
        return subscribeDate;
    }

    public void setSubscribeDate(Date subscribeDate) {
        this.subscribeDate = subscribeDate;
    }

    public String getFeeReserveJnlNo() {
        return feeReserveJnlNo;
    }

    public void setFeeReserveJnlNo(String feeReserveJnlNo) {
        this.feeReserveJnlNo = feeReserveJnlNo;
    }

}
