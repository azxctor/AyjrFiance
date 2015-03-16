/*
 * Project Name: kmfex-platform
 * File Name: BaseDo.java
 * Class Name: BaseDo
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

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.hengxin.platform.product.domain.converter.PackageStatusConverter;
import com.hengxin.platform.product.domain.converter.WarrantyTypeConverter;
import com.hengxin.platform.product.enums.EPackageStatus;
import com.hengxin.platform.product.enums.EWarrantyType;

/**
 * 
 * @author yingchangwang
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "V_PKG_PAYMENT")
public class PackagePaymentView implements Serializable {

    @Id
    @Column(name = "PKG_ID")
    private String id;// 融资包编号

    @Column(name = "PROD_ID")
    private String productId;

    @Column(name = "PKG_NAME")
    private String packageName;// 融资包简称

    @Column(name = "SUBS_AMT")
    private BigDecimal supplyAmount;

    @Column(name = "STATUS")
    @Convert(converter = PackageStatusConverter.class)
    private EPackageStatus status;

    @Column(name = "WRTR_ID")
    private String watrid;
    
    @Column(name = "WRTR_ID_SW")
    private String watridSw;

    @Column(name = "FINANCIER_ID")
    private String financierId;// 融资人

    @Column(name = "FINANCIER_NAME")
    private String financierName;// 融资人名字

    @Column(name = "WRTR_NAME")
    private String wrtrName;// 担保机构
    
    @Column(name = "WRTR_NAME_SW")
    private String wrtrNameShow;// 担保机构

    @Column(name = "WRTY_TYPE")
    @Convert(converter = WarrantyTypeConverter.class)
    private EWarrantyType warrantyType;// 风险保障

    @Column(name = "OVRD2CMPNS_GRCE_PRD")
    private Long overdue2CmpnsDays;

    @Column(name = "CMPNS_GRCE_PRD")
    private Long cmpnsGracePriod;

    @Column(name = "LOAN_FNR_JNL_NO")
    private String loanFnrJnl;

    @Column(name = "WRTR_FNR_JNL_NO")
    private String wrtrFnrJNL;

    @Column(name = "FNCRAVLAMT")
    private BigDecimal fncrAvlAmt;

    @Column(name = "WRTRAVLAMT")
    private BigDecimal wrtrAvlAmt;

    @Column(name = "LOANAMT")
    private BigDecimal loanAmt;

    @Column(name = "WRTRAMT")
    private BigDecimal wrtrAmt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public BigDecimal getSupplyAmount() {
        return supplyAmount;
    }

    public void setSupplyAmount(BigDecimal supplyAmount) {
        this.supplyAmount = supplyAmount;
    }

    public EPackageStatus getStatus() {
        return status;
    }

    public void setStatus(EPackageStatus status) {
        this.status = status;
    }

    public String getWatrid() {
        return watrid;
    }

    public void setWatrid(String watrid) {
        this.watrid = watrid;
    }

    public String getFinancierId() {
        return financierId;
    }

    public void setFinancierId(String financierId) {
        this.financierId = financierId;
    }

    public String getFinancierName() {
        return financierName;
    }

    public void setFinancierName(String financierName) {
        this.financierName = financierName;
    }

    public String getWrtrName() {
        return wrtrName;
    }

    public void setWrtrName(String wrtrName) {
        this.wrtrName = wrtrName;
    }

    public EWarrantyType getWarrantyType() {
        return warrantyType;
    }

    public void setWarrantyType(EWarrantyType warrantyType) {
        this.warrantyType = warrantyType;
    }

    public Long getOverdue2CmpnsDays() {
        return overdue2CmpnsDays;
    }

    public void setOverdue2CmpnsDays(Long overdue2CmpnsDays) {
        this.overdue2CmpnsDays = overdue2CmpnsDays;
    }

    public Long getCmpnsGracePriod() {
        return cmpnsGracePriod;
    }

    public void setCmpnsGracePriod(Long cmpnsGracePriod) {
        this.cmpnsGracePriod = cmpnsGracePriod;
    }

    public String getLoanFnrJnl() {
        return loanFnrJnl;
    }

    public void setLoanFnrJnl(String loanFnrJnl) {
        this.loanFnrJnl = loanFnrJnl;
    }

    public String getWrtrFnrJNL() {
        return wrtrFnrJNL;
    }

    public void setWrtrFnrJNL(String wrtrFnrJNL) {
        this.wrtrFnrJNL = wrtrFnrJNL;
    }

    public BigDecimal getFncrAvlAmt() {
        return fncrAvlAmt;
    }

    public void setFncrAvlAmt(BigDecimal fncrAvlAmt) {
        this.fncrAvlAmt = fncrAvlAmt;
    }

    public BigDecimal getWrtrAvlAmt() {
        return wrtrAvlAmt;
    }

    public void setWrtrAvlAmt(BigDecimal wrtrAvlAmt) {
        this.wrtrAvlAmt = wrtrAvlAmt;
    }

    public BigDecimal getLoanAmt() {
        return loanAmt;
    }

    public void setLoanAmt(BigDecimal loanAmt) {
        this.loanAmt = loanAmt;
    }

    public BigDecimal getWrtrAmt() {
        return wrtrAmt;
    }

    public void setWrtrAmt(BigDecimal wrtrAmt) {
        this.wrtrAmt = wrtrAmt;
    }

	public String getWatridSw() {
		return watridSw;
	}

	public void setWatridSw(String watridSw) {
		this.watridSw = watridSw;
	}

	public String getWrtrNameShow() {
		return wrtrNameShow;
	}

	public void setWrtrNameShow(String wrtrNameShow) {
		this.wrtrNameShow = wrtrNameShow;
	}
}
