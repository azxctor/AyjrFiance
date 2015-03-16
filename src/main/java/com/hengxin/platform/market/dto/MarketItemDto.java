package com.hengxin.platform.market.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.hengxin.platform.common.util.NumberUtil;
import com.hengxin.platform.product.enums.EPackageStatus;

public class MarketItemDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "yyyy-MM-dd HH:mm";

    private String id; // 融资包编号

    private String lotId; // 头寸ID

    private String packageName; // 融资包名称

    private BigDecimal packageQuota; // 融资额

    private Date supplyStartTime; // 起始时间

    private Date supplyEndTime; // 截止时间

    private EPackageStatus status; // 状态

    private BigDecimal supplyAmount; // 已申购金额

    private BigDecimal progress;

    private Boolean aipFlag; // 是否定投

    private Date aipEndTime;

    private MarketProductDto product;

    private BigDecimal minSupplyAmount; // 最小申购额

    private BigDecimal maxSupplyAmount; // 最大申购额

    private BigDecimal price; // 债券转让的价格

    private BigDecimal remainingAmount; // 债券转让的剩余本息
    
    private BigDecimal residualPrincipalAmt; // 债券转让的剩余本金
    
	private BigDecimal residualInterestAmt; // 债券转让的剩余利息
	
	private Integer remTermLength; // 剩余期数
	
	private Integer totalHolderCount; // 累计持有人

    private boolean oneselfOwn; // 自己发布的融资包或转让债券

	private Date maturityDate; // 最后还款日

	private String transferId; // 转让记录ID

	private boolean mine; // 是否是自己的债权，转让用

	private boolean overdue; // 是否存在历史逾期记录，转让用

	@JsonIgnore
	private long expiredTime; // 状态从申购中变为待签约的时间

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((aipFlag == null) ? 0 : aipFlag.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((packageName == null) ? 0 : packageName.hashCode());
        result = prime * result + ((packageQuota == null) ? 0 : packageQuota.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((supplyEndTime == null) ? 0 : supplyEndTime.hashCode());
        result = prime * result + ((supplyStartTime == null) ? 0 : supplyStartTime.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MarketItemDto other = (MarketItemDto) obj;
        if (aipFlag == null) {
            if (other.aipFlag != null)
                return false;
        } else if (!aipFlag.equals(other.aipFlag))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (packageName == null) {
            if (other.packageName != null)
                return false;
        } else if (!packageName.equals(other.packageName))
            return false;
        if (packageQuota == null) {
            if (other.packageQuota != null)
                return false;
        } else if (!packageQuota.equals(other.packageQuota))
            return false;
        if (status != other.status)
            return false;
        if (supplyEndTime == null) {
            if (other.supplyEndTime != null)
                return false;
        } else if (!supplyEndTime.equals(other.supplyEndTime))
            return false;
        if (supplyStartTime == null) {
            if (other.supplyStartTime != null)
                return false;
        } else if (!supplyStartTime.equals(other.supplyStartTime))
            return false;
        return true;
    }

    public String getPackageQuotaStr() {
        return packageQuota == null ? "" : NumberUtil.formatMoney(packageQuota);
    }

    public String getSupplyAmountStr() {
        return supplyAmount == null ? "" : NumberUtil.formatMoney(supplyAmount);
    }

    public String getPriceStr() {
        return price == null ? "" : NumberUtil.formatMoney(price);
    }

    public String getRemainingAmountStr() {
        return remainingAmount == null ? "" : NumberUtil.formatMoney(remainingAmount);
    }
    
    public String getResidualPrincipalAmtStr() {
        return residualPrincipalAmt == null ? "" : NumberUtil.formatMoney(residualPrincipalAmt);
    }
    
    public String getResidualInterestAmtStr() {
        return residualInterestAmt == null ? "" : NumberUtil.formatMoney(residualInterestAmt);
    }

    public String getMinSupplyAmountStr() {
        return minSupplyAmount == null ? "" : NumberUtil.formatMoney(minSupplyAmount);
    }

    public String getMaxSupplyAmountStr() {
        return maxSupplyAmount == null ? "" : NumberUtil.formatMoney(maxSupplyAmount);
    }

    /**
     * @return return the value of the var id
     */

    public String getId() {
        return id;
    }

    /**
     * @param id
     *            Set id value
     */

    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return return the value of the var lotId
     */

    public String getLotId() {
        return lotId;
    }

    /**
     * @param lotId
     *            Set lotId value
     */

    public void setLotId(String lotId) {
        this.lotId = lotId;
    }

    /**
     * @return return the value of the var packageName
     */

    public String getPackageName() {
        return packageName;
    }

    /**
     * @param packageName
     *            Set packageName value
     */

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * @return return the value of the var packageQuota
     */

    public BigDecimal getPackageQuota() {
        return packageQuota;
    }

    /**
     * @param packageQuota
     *            Set packageQuota value
     */

    public void setPackageQuota(BigDecimal packageQuota) {
        this.packageQuota = packageQuota;
    }

    /**
     * @return return the value of the var supplyStartTime
     */

    public Date getSupplyStartTime() {
        return supplyStartTime;
    }

    /**
     * @param supplyStartTime
     *            Set supplyStartTime value
     */

    public void setSupplyStartTime(Date supplyStartTime) {
        this.supplyStartTime = supplyStartTime;
    }

    /**
     * @return return the value of the var supplyEndTime
     */

    public Date getSupplyEndTime() {
        return supplyEndTime;
    }

    /**
     * @param supplyEndTime
     *            Set supplyEndTime value
     */

    public void setSupplyEndTime(Date supplyEndTime) {
        this.supplyEndTime = supplyEndTime;
    }

    /**
     * @return return the value of the var status
     */

    public EPackageStatus getStatus() {
        return status;
    }

    /**
     * @param status
     *            Set status value
     */

    public void setStatus(EPackageStatus status) {
        this.status = status;
    }

    /**
     * @return return the value of the var progress
     */

    public BigDecimal getProgress() {
        return progress;
    }

    /**
     * @param progress
     *            Set progress value
     */

    public void setProgress(BigDecimal progress) {
        this.progress = progress;
    }

    /**
     * @return return the value of the var product
     */

    public MarketProductDto getProduct() {
        return product;
    }

    /**
     * @param product
     *            Set product value
     */

    public void setProduct(MarketProductDto product) {
        this.product = product;
    }

    /**
     * @return return the value of the var supplyAmount
     */

    public BigDecimal getSupplyAmount() {
        return supplyAmount;
    }

    /**
     * @param supplyAmount
     *            Set supplyAmount value
     */

    public void setSupplyAmount(BigDecimal supplyAmount) {
        this.supplyAmount = supplyAmount;
    }

    public String getSubscribeStartTimeStr() {
        if (supplyStartTime != null) {
            return DateFormatUtils.format(supplyStartTime, TIME_FORMAT);
        }
        return "";
    }

    public String getSubscribeEndTimeStr() {
        if (supplyStartTime != null) {
            return DateFormatUtils.format(supplyEndTime, DATE_FORMAT);
        }
        return "";
    }

	public String getMaturityDateStr() {
		if (maturityDate != null) {
            return DateFormatUtils.format(maturityDate, "yyyy-MM-dd");
		}
		return "";
	}

    /**
     * @return return the value of the var price
     */

    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price
     *            Set price value
     */

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * @return return the value of the var remainingAmount
     */

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    /**
     * @param remainingAmount
     *            Set remainingAmount value
     */

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    /**
     * @return return the value of the var aipFlag
     */

    public Boolean getAipFlag() {
        return aipFlag;
    }

    /**
     * @return return the value of the var aipEndTime
     */

    public Date getAipEndTime() {
        return aipEndTime;
    }

    /**
     * @param aipEndTime
     *            Set aipEndTime value
     */

    public void setAipEndTime(Date aipEndTime) {
        this.aipEndTime = aipEndTime;
    }

    /**
     * @param aipFlag
     *            Set aipFlag value
     */

    public void setAipFlag(Boolean aipFlag) {
        this.aipFlag = aipFlag;
    }

    public BigDecimal getMinSupplyAmount() {
        return minSupplyAmount;
    }

    public void setMinSupplyAmount(BigDecimal minSupplyAmount) {
        this.minSupplyAmount = minSupplyAmount;
    }

    public BigDecimal getMaxSupplyAmount() {
        return maxSupplyAmount;
    }

    public void setMaxSupplyAmount(BigDecimal maxSupplyAmount) {
        this.maxSupplyAmount = maxSupplyAmount;
    }

    /**
     * @return return the value of the var oneselfOwn
     */

    public boolean isOneselfOwn() {
        return oneselfOwn;
    }

    /**
     * @param oneselfOwn
     *            Set oneselfOwn value
     */

    public void setOneselfOwn(boolean oneselfOwn) {
        this.oneselfOwn = oneselfOwn;
    }

	public Date getMaturityDate() {
		return maturityDate;
	}

	public void setMaturityDate(Date maturityDate) {
		this.maturityDate = maturityDate;
	}

	public String getTransferId() {
		return transferId;
	}

	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}

	@Override
	public String toString() {
		return "MarketItemDto[id=" + id + ",progress=" + progress + "]";
	}

	public boolean getMine() {
		return mine;
	}

	public void setMine(boolean mine) {
		this.mine = mine;
	}

	public BigDecimal getResidualPrincipalAmt() {
		return residualPrincipalAmt;
	}

	public void setResidualPrincipalAmt(BigDecimal residualPrincipalAmt) {
		this.residualPrincipalAmt = residualPrincipalAmt;
	}

	public BigDecimal getResidualInterestAmt() {
		return residualInterestAmt;
	}

	public void setResidualInterestAmt(BigDecimal residualInterestAmt) {
		this.residualInterestAmt = residualInterestAmt;
	}

	public Integer getRemTermLength() {
		return remTermLength;
	}

	public void setRemTermLength(Integer remTermLength) {
		this.remTermLength = remTermLength;
	}

	public Integer getTotalHolderCount() {
		return totalHolderCount;
	}

	public void setTotalHolderCount(Integer totalHolderCount) {
		this.totalHolderCount = totalHolderCount;
	}

	public boolean isOverdue() {
		return overdue;
	}

	public void setOverdue(boolean overdue) {
		this.overdue = overdue;
	}

	public long getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(long expiredTime) {
		this.expiredTime = expiredTime;
	}

}
