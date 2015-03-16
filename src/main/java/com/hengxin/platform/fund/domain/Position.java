package com.hengxin.platform.fund.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 *  Class Name: UserPosition
 * 
 * @author liudc
 *
 */
@SuppressWarnings("serial")
public class Position implements Serializable{
	
	private String positionId;
	
	private String userId;
	
	private String pkgId;
	
	private Integer unit;
	
	private BigDecimal unitFaceValue;
	
	private String createOpid;
	
	private Date createTs;
	
	private String lastMntOpid;
	
	private Date lastMntTs;

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

}
