package com.hengxin.platform.fund.entity;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hengxin.platform.common.entity.id.IdInjectionEntityListener;
import com.hengxin.platform.fund.entity.converter.FundTrdTypeEnumConverter;
import com.hengxin.platform.fund.enums.EFundTrdType;
import com.hengxin.platform.product.domain.FinancingPackageView;
import com.hengxin.platform.product.domain.ProductPackage;

/**
 * 
 *  Class Name: AcExchangeJnlPo
 *  融资包交易日志表
 * @author tingwang
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name="AC_PKG_TRADE_JNL")
@EntityListeners(IdInjectionEntityListener.class)
public class PkgTradeJnlPo implements Serializable{
	
	@Id
	@Column(name="JNL_NO")
	private String jnlNo;
	
	@Column(name="BUYER_USER_ID")
	private String buyerUserId;
	
	@Column(name="SELLER_USER_ID")
	private String sellerUserId;
	
	@Column(name="PKG_ID")
	private String pkgId;
	
	@Column(name="LOT_ID")
	private String lotId;
	
	@Column(name="CR_ID")
	private String crId;
	
	@Column(name="UNIT")
	private Integer unit;

	@Column(name="LOT_BUY_PRICE")
	private BigDecimal lotBuyPrice;
	
	@Column(name="TRD_TYPE")
	@Convert(converter = FundTrdTypeEnumConverter.class)
	private EFundTrdType trdType;
	
	@Temporal(TemporalType.DATE)
	@Column(name="TRD_DT")
	private Date trdDt;
	
	@Column(name="TRD_MEMO")
	private String trdMemo;
	
	@Column(name="CREATE_OPID")
	private String createOpid;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATE_TS")
	private Date createTs;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = { CascadeType.DETACH })
    @JoinColumn(name = "PKG_ID", insertable = false, updatable = false)
    private ProductPackage productPackage;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.DETACH })
    @JoinColumn(name = "PKG_ID", insertable = false, updatable = false)
    private FinancingPackageView financingPackageView;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.DETACH })
    @JoinColumn(name = "LOT_ID", insertable = false, updatable = false)
    private PositionLotPo positionLot;

	public BigDecimal getLotBuyPrice() {
        return lotBuyPrice;
    }

    public void setLotBuyPrice(BigDecimal lotBuyPrice) {
        this.lotBuyPrice = lotBuyPrice;
    }

    public ProductPackage getProductPackage() {
        return productPackage;
    }

    public void setProductPackage(ProductPackage productPackage) {
        this.productPackage = productPackage;
    }

    public String getJnlNo() {
		return jnlNo;
	}

	public void setJnlNo(String jnlNo) {
		this.jnlNo = jnlNo;
	}

	public String getBuyerUserId() {
		return buyerUserId;
	}

	public void setBuyerUserId(String buyerUserId) {
		this.buyerUserId = buyerUserId;
	}

	public String getSellerUserId() {
		return sellerUserId;
	}

	public void setSellerUserId(String sellerUserId) {
		this.sellerUserId = sellerUserId;
	}

	public String getPkgId() {
		return pkgId;
	}

	public void setPkgId(String pkgId) {
		this.pkgId = pkgId;
	}

	public String getLotId() {
		return lotId;
	}

	public void setLotId(String lotId) {
		this.lotId = lotId;
	}

	public Integer getUnit() {
		return unit;
	}

	public void setUnit(Integer unit) {
		this.unit = unit;
	}

	public EFundTrdType getTrdType() {
		return trdType;
	}

	public void setTrdType(EFundTrdType trdType) {
		this.trdType = trdType;
	}

	public Date getTrdDt() {
		return trdDt;
	}

	public void setTrdDt(Date trdDt) {
		this.trdDt = trdDt;
	}

	public String getTrdMemo() {
		return trdMemo;
	}

	public void setTrdMemo(String trdMemo) {
		this.trdMemo = trdMemo;
	}

	public String getCreateOpid() {
		return createOpid;
	}

	public void setCreateOpid(String createOpid) {
		this.createOpid = createOpid;
	}

	public Date getCreateTs() {
		return createTs;
	}

	public void setCreateTs(Date createTs) {
		this.createTs = createTs;
	}

    /**
     * @return return the value of the var financingPackageView
     */
    
    public FinancingPackageView getFinancingPackageView() {
        return financingPackageView;
    }

    /**
     * @param financingPackageView Set financingPackageView value
     */
    
    public void setFinancingPackageView(FinancingPackageView financingPackageView) {
        this.financingPackageView = financingPackageView;
    }

	public String getCrId() {
		return crId;
	}

	public void setCrId(String crId) {
		this.crId = crId;
	}

    public PositionLotPo getPositionLot() {
        return positionLot;
    }

    public void setPositionLot(PositionLotPo positionLot) {
        this.positionLot = positionLot;
    }
}
