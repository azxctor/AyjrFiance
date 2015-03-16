package com.hengxin.platform.member.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@SuppressWarnings("serial")
@Entity
@Table(name = "UM_INVSTR_LVL")
public class InvestorLevel implements Serializable {

    @Id
    @Column(name = "LVL_ID")
    private String levelId;

    @Column(name = "LVL_NAME")
    private String levelName;

    @Column(name = "LVL_DESC")
    private String levelDesc;
    
    /**
     * 手续费折扣比率
     */
    @Column(name = "FEE_DISCOUNT_RT")
    private BigDecimal feeDiscountRate;

	@Version
	@Column(name = "VERSION_CT")
	private Long version;
    
    @Column(name = "CREATE_OPID")
    private String creator;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TS", updatable = false)
    private Date createTs;

    @Column(name = "LAST_MNT_OPID")
    private String lastMntOpid;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_MNT_TS")
    private Date lastMntTs;

	public String getLevelId() {
		return levelId;
	}

	public void setLevelId(String levelId) {
		this.levelId = levelId;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public String getLevelDesc() {
		return levelDesc;
	}

	public void setLevelDesc(String levelDesc) {
		this.levelDesc = levelDesc;
	}

	public BigDecimal getFeeDiscountRate() {
		return feeDiscountRate;
	}

	public void setFeeDiscountRate(BigDecimal feeDiscountRate) {
		this.feeDiscountRate = feeDiscountRate;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
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
    
}
