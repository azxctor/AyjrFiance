
/*
* Project Name: kmfex
* File Name: PackageTradeDetailView.java
* Class Name: PackageTradeDetailView
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

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hengxin.platform.account.enums.ETradeDirection;
import com.hengxin.platform.account.enums.converter.ETradeDirectionConverter;
import com.hengxin.platform.fund.entity.converter.FundTrdTypeEnumConverter;
import com.hengxin.platform.fund.enums.EFundTrdType;
import com.hengxin.platform.product.domain.converter.ERiskLevelConverter;
import com.hengxin.platform.product.domain.converter.PackageStatusConverter;
import com.hengxin.platform.product.domain.converter.ProductTermConverter;
import com.hengxin.platform.product.enums.EPackageStatus;
import com.hengxin.platform.product.enums.ERiskLevel;
import com.hengxin.platform.product.enums.ETermType;


/**
 * Class Name: PackageTradeDetailView
 * Description: TODO
 * @author congzhou
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "V_PKG_TRD_DTL")
public class PackageTradeDetailView implements Serializable{
    
    //ID
    @Id
    @Column(name = "ID")
    private String id;

    //用户ID
    @Column(name="USER_ID")
    private String userId;
    
    //融资包ID
    @Column(name="PKG_ID")
    private String pkgId;
    
    //份数
    @Column(name="UNIT")
    private Integer unit;
    
    //每份金额
    @Column(name="AMT")
    private BigDecimal amount;
    
    //交易类型
    @Column(name="TRD_TYPE")
    @Convert(converter = FundTrdTypeEnumConverter.class)
    private EFundTrdType trdType;
    
    //方向
    @Column(name="DIRECTION")
    @Convert(converter = ETradeDirectionConverter.class)
    private ETradeDirection direction;
    
    //交易日期
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="TRD_DT")
    private Date trdDt;
    
    //融资包简称
    @Column(name="PROD_NAME")
    private String productName;
    
    // 期限类型 D-Day,M-Month,Y-Year';
    @Column(name = "TERM_TYPE")
    @Convert(converter = ProductTermConverter.class)
    private ETermType termType; 
    
    //期限
    @Column(name="TERM_LENGTH")
    private String termLength;
    
    //年利率
    @Column(name="RATE")
    private BigDecimal rate;
    
    //风险等级
    @Column(name="PROD_GRADE")
    private BigDecimal garade;
    
    //状态
    @Column(name = "STATUS")
    @Convert(converter = PackageStatusConverter.class)
    private EPackageStatus status;
    
    // 头寸编号 
    @Column(name="LOT_ID")
    private String lotId;

    @Column(name = "PROD_LVL")
	@Convert(converter = ERiskLevelConverter.class)
	private ERiskLevel productLevel; // 融资项目级别

    @Column(name = "CREATE_TS")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTs;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPkgId() {
        return pkgId;
    }

    public void setPkgId(String pkgId) {
        this.pkgId = pkgId;
    }

    public Integer getUnit() {
        return unit;
    }

    public void setUnit(Integer unit) {
        this.unit = unit;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public EFundTrdType getTrdType() {
        return trdType;
    }

    public void setTrdType(EFundTrdType trdType) {
        this.trdType = trdType;
    }

    public ETradeDirection getDirection() {
        return direction;
    }

    public void setDirection(ETradeDirection direction) {
        this.direction = direction;
    }

    public Date getTrdDt() {
        return trdDt;
    }

    public void setTrdDt(Date trdDt) {
        this.trdDt = trdDt;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getTermLength() {
        return termLength;
    }

    public void setTermLength(String termLength) {
        this.termLength = termLength;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getGarade() {
        return garade;
    }

    public void setGarade(BigDecimal garade) {
        this.garade = garade;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EPackageStatus getStatus() {
        return status;
    }

    public void setStatus(EPackageStatus status) {
        this.status = status;
    }

    public ETermType getTermType() {
        return termType;
    }

    public void setTermType(ETermType termType) {
        this.termType = termType;
    }

	public String getLotId() {
		return lotId;
	}

	public void setLotId(String lotId) {
		this.lotId = lotId;
	}

	public ERiskLevel getProductLevel() {
		return productLevel;
	}

	public void setProductLevel(ERiskLevel productLevel) {
		this.productLevel = productLevel;
	}

	public Date getCreateTs() {
		return createTs;
	}

	public void setCreateTs(Date createTs) {
		this.createTs = createTs;
	}

}
