package com.hengxin.platform.fund.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import com.hengxin.platform.common.entity.id.IdInjectionEntityListener;
import com.hengxin.platform.product.domain.FinancingPackageView;
import com.hengxin.platform.product.domain.ProductPackage;

/**
 *  Class Name: UserPosition
 * 
 * @author tingwang
 * 投资会员产品包头寸表
 */
@SuppressWarnings("serial")
@Entity
@Table(name="AC_POSITION")
@EntityListeners(IdInjectionEntityListener.class)
public class PositionPo implements Serializable{
	@Id
	@Column(name="POSITION_ID")
	private String positionId;
	
	@Column(name="USER_ID")
	private String userId;
	
	@Column(name="PKG_ID")
	private String pkgId;
	
	@Column(name="UNIT")
	private Integer unit;
	
	@Column(name="UNIT_FACE_VALUE")
	private BigDecimal unitFaceValue;
	
	@Column(name="CREATE_OPID")
	private String createOpid;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATE_TS")
	private Date createTs;
	
	@Column(name="LAST_MNT_OPID")
	private String lastMntOpid;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LAST_MNT_TS")
	private Date lastMntTs;
	
	@Version
    @Column(name = "VERSION_CT")
	private Long versionCt;

    @ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.DETACH })
    @JoinColumn(name = "PKG_ID", insertable = false, updatable = false)
    private ProductPackage productPackage;
    
    @OneToMany(mappedBy="position")
    private List<PositionLotPo> positionLotPos; 
    
    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH })
    @JoinColumn(name = "PKG_ID", insertable = false, updatable = false)
    private FinancingPackageView financingPackageView;

	public Long getVersionCt() {
		return versionCt;
	}

	public void setVersionCt(Long versionCt) {
		this.versionCt = versionCt;
	}

	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

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

	public String getLastMntOpid() {
		return lastMntOpid;
	}

	public void setLastMntOpid(String lastMntOpid) {
		this.lastMntOpid = lastMntOpid;
	}

	public Date getLastMntTs() {
		return lastMntTs;
	}

	public void setLastMntTs(Date lastMntTs) {
		this.lastMntTs = lastMntTs;
	}

	public BigDecimal getUnitFaceValue() {
		return unitFaceValue;
	}

	public void setUnitFaceValue(BigDecimal unitFaceValue) {
		this.unitFaceValue = unitFaceValue;
	}

    /**
     * @return return the value of the var productPackage
     */

    public ProductPackage getProductPackage() {
        return productPackage;
    }

    /**
     * @param productPackage
     *            Set productPackage value
     */

    public void setProductPackage(ProductPackage productPackage) {
        this.productPackage = productPackage;
    }

    /**
     * @return return the value of the var positionLotPos
     */
    
    public List<PositionLotPo> getPositionLotPos() {
        return positionLotPos;
    }

    /**
     * @param positionLotPos Set positionLotPos value
     */
    
    public void setPositionLotPos(List<PositionLotPo> positionLotPos) {
        this.positionLotPos = positionLotPos;
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
	

}
