package com.hengxin.platform.fund.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import com.hengxin.platform.common.entity.id.IdInjectionEntityListener;

/**
 * 
 * @author dcliu
 * 投资会员产品包头寸份额表
 */
@SuppressWarnings("serial")
@Entity
@Table(name="AC_POSITION_LOT")
@EntityListeners(IdInjectionEntityListener.class)
public class PositionLotPo implements Serializable {
	@Id
	@Column(name="LOT_ID")
	private String lotId;
	
	@Column(name="POSITION_ID")
	private String positionId;
	
	@Column(name="CR_ID")
	private String crId;
	
	@Column(name="UNIT")
	private Integer unit;
	
	@Column(name="LOT_BUY_PRICE")
	private BigDecimal lotBuyPrice;
	
	@Column(name="ACCUM_CR_AMT")
	private BigDecimal accumCrAmt;
	
	@Temporal(TemporalType.DATE)
	@Column(name="SUBS_DT")
	private Date subsDt;
	
	@Temporal(TemporalType.DATE)
	@Column(name="SETTLE_DT")
	private Date settleDt;
	
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
	
    @Column(name="CNTRCT_ID")
    private String contractId;
	
	@Version
    @Column(name = "VERSION_CT")
	private Long versionCt;

    @ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.DETACH })
    @JoinColumn(name = "POSITION_ID", insertable = false, updatable = false)
    private PositionPo position;

	public Long getVersionCt() {
		return versionCt;
	}

	public void setVersionCt(Long versionCt) {
		this.versionCt = versionCt;
	}

	public String getLotId() {
		return lotId;
	}

	public void setLotId(String lotId) {
		this.lotId = lotId;
	}

	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	public Integer getUnit() {
		return unit;
	}

	public void setUnit(Integer unit) {
		this.unit = unit;
	}

	public Date getSubsDt() {
		return subsDt;
	}

	public void setSubsDt(Date subsDt) {
		this.subsDt = subsDt;
	}

	public Date getSettleDt() {
		return settleDt;
	}

	public void setSettleDt(Date settleDt) {
		this.settleDt = settleDt;
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

	public BigDecimal getLotBuyPrice() {
		return lotBuyPrice;
	}

	public void setLotBuyPrice(BigDecimal lotBuyPrice) {
		this.lotBuyPrice = lotBuyPrice;
	}

	public BigDecimal getAccumCrAmt() {
		return accumCrAmt;
	}

	public void setAccumCrAmt(BigDecimal accumCrAmt) {
		this.accumCrAmt = accumCrAmt;
	}

    /**
     * @return return the value of the var position
     */

    public PositionPo getPosition() {
        return position;
    }

    /**
     * @param position
     *            Set position value
     */

    public void setPosition(PositionPo position) {
        this.position = position;
    }

	public String getCrId() {
		return crId;
	}

	public void setCrId(String crId) {
		this.crId = crId;
	}

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }
}
