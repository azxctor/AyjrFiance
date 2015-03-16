package com.hengxin.platform.fund.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Class Name: SubAcctBal
 * 
 * @author liudc
 * 
 */
@SuppressWarnings("serial")
public class PositionLot implements Serializable {

	private String lotId;
	
	private String positionId;
	
	private Integer unit;
	
	private BigDecimal lotBuyPrice;
	
	private BigDecimal accumCrAmt;
	
	private Date subsDt;
	
	private Date settleDt;
	
	private String createOpid;
	
	private Date createTs;
	
	private String lastMntOpid;
	
	private Date lastMntTs;

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
	
	
}
