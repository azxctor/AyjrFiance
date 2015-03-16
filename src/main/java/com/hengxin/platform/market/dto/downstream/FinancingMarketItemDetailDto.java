package com.hengxin.platform.market.dto.downstream;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.collections.CollectionUtils;

import com.hengxin.platform.common.util.MessageUtil;
import com.hengxin.platform.common.util.NumberUtil;
import com.hengxin.platform.product.dto.ProductDetailsDto;
import com.hengxin.platform.product.dto.ProductPackageDto;
import com.hengxin.platform.product.dto.ProductWarrantEnterpriseDto;
import com.hengxin.platform.product.dto.ProductWarrantPersonDto;
import com.hengxin.platform.product.enums.ERiskLevel;

public class FinancingMarketItemDetailDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private BigDecimal balance;
	
    private BigDecimal totalSubscribeAmount; //已购买金额

    private BigDecimal levelSubscribeAmount; //最大可申购金额

    private BigDecimal minSubscribeAmount; //申购下限

    private BigDecimal maxSubscribeAmount; //申购上限

    private BigDecimal accumulativeAmount; //同一风险级别融资项目累计持有上限.

    private BigDecimal rate; //同一风险级别融资项目累计持有上限.

    private BigDecimal profit;

    private long remainingTime;

    private long remainingStartTime;

    private BigDecimal progress;

    /** 是否可输入. **/
    private boolean inputable = true;

    private boolean subscribed = true; // 是否可申购

    private String reason; // 不可申购的原因

    private ProductPackageDto pkg;

    private ProductDetailsDto product;
    
    private boolean expired;

    private boolean activeRisk;

    private boolean displayLevel4;

    public String getBalanceStr() {
        if (balance != null) {
            return NumberUtil.formatMoney(balance, true);
        }
        return "0";
    }

    public String getRateStr() {
    	if (rate != null) {
    		return NumberUtil.getPercentStr(rate, 2, true);
        }
        return "--";
    }
    
    public String getAccumulativeAmountStr() {
        if (accumulativeAmount != null) {
            return NumberUtil.formatMoney(accumulativeAmount);
        }
        return "0";
    }
    
    public String getTotalSubscribeAmountStr() {
        if (totalSubscribeAmount != null) {
            return NumberUtil.formatMoney(totalSubscribeAmount);
        }
        return "0";
    }

    public String getLevelSubscribeAmountStr() {
        if (levelSubscribeAmount != null) {
            return NumberUtil.formatMoney(levelSubscribeAmount);
        }
        return "0";
    }

    public String getMinSubscribeAmountStr() {
        if (minSubscribeAmount != null) {
            return NumberUtil.formatMoney(minSubscribeAmount);
        }
        return "0";
    }

    public String getMaxSubscribeAmountStr() {
        if (maxSubscribeAmount != null) {
            return NumberUtil.formatMoney(maxSubscribeAmount);
        }
        return "0";
    }

    public ERiskLevel getRiskLevel() {
        if (product != null) {
            return product.getProductLevel();
        }
        return ERiskLevel.NULL;
    }

    public String getWarrantPersonStr(){
        if (product != null && CollectionUtils.isNotEmpty(product.getProductWarrantPersonDtoList())) {
            StringBuilder sb = new StringBuilder();
            for (ProductWarrantPersonDto dto : product.getProductWarrantPersonDtoList()) {
                sb.append(dto.getName());
                sb.append("、");
            }
            return sb.substring(0, sb.length() - 1);
        }
        return MessageUtil.getMessage("common.record.notfound");
    }

    public String getWarrantEnterpriseStr() {
        if (product != null && CollectionUtils.isNotEmpty(product.getProductWarrantEnterpriseDtoList())) {
            StringBuilder sb = new StringBuilder();
            for (ProductWarrantEnterpriseDto dto : product.getProductWarrantEnterpriseDtoList()) {
                sb.append(dto.getEnterpriseName());
                sb.append("、");
            }
            return sb.substring(0, sb.length() - 1);
        }
        return MessageUtil.getMessage("common.record.notfound");
    }

    /**
     * @return return the value of the var pkg
     */

    public ProductPackageDto getPkg() {
        return pkg;
    }

    /**
     * @param pkg
     *            Set pkg value
     */

    public void setPkg(ProductPackageDto pkg) {
        this.pkg = pkg;
    }


    /**
     * @return return the value of the var product
     */

    public ProductDetailsDto getProduct() {
        return product;
    }


    /**
     * @param product
     *            Set product value
     */

    public void setProduct(ProductDetailsDto product) {
        this.product = product;
    }
    
    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getMinSubscribeAmount() {
        return minSubscribeAmount;
    }

    public void setMinSubscribeAmount(BigDecimal minSubscribeAmount) {
        this.minSubscribeAmount = minSubscribeAmount;
    }

    public BigDecimal getMaxSubscribeAmount() {
        return maxSubscribeAmount;
    }

    public void setMaxSubscribeAmount(BigDecimal maxSubscribeAmount) {
        this.maxSubscribeAmount = maxSubscribeAmount;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public long getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(long remainingTime) {
        this.remainingTime = remainingTime;
    }

    
    /**
    * @return return the value of the var subscribed
    */
    	
    public boolean isSubscribed() {
        return subscribed;
    }

    
    /**
    * @param subscribed Set subscribed value
    */
    	
    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }

    
    /**
    * @return return the value of the var reason
    */
    	
    public String getReason() {
        return reason;
    }

    
    /**
    * @param reason Set reason value
    */
    	
    public void setReason(String reason) {
        this.reason = reason;
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
     * @return return the value of the var remainingStartTime
     */

    public long getRemainingStartTime() {
        return remainingStartTime;
    }

    /**
     * @param remainingStartTime
     *            Set remainingStartTime value
     */

    public void setRemainingStartTime(long remainingStartTime) {
        this.remainingStartTime = remainingStartTime;
    }

    /**
     * @return return the value of the var totalSubscribeAmount
     */

    public BigDecimal getTotalSubscribeAmount() {
        return totalSubscribeAmount;
    }

    /**
     * 已购买金额
     * @param totalSubscribeAmount
     *            Set totalSubscribeAmount value
     */

    public void setTotalSubscribeAmount(BigDecimal totalSubscribeAmount) {
        this.totalSubscribeAmount = totalSubscribeAmount;
    }

    /**
     * @return return the value of the var levelSubscribeAmount
     */

    public BigDecimal getLevelSubscribeAmount() {
        return levelSubscribeAmount;
    }

    /**
     * 最大可申购值
     * @param levelSubscribeAmount
     *            Set levelSubscribeAmount value
     */

    public void setLevelSubscribeAmount(BigDecimal levelSubscribeAmount) {
        this.levelSubscribeAmount = levelSubscribeAmount;
    }

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}

	public BigDecimal getAccumulativeAmount() {
		return accumulativeAmount;
	}

	/**
	 * 同一风险级别融资项目累计持有上限
	 * @param accumulativeAmount
	 */
	public void setAccumulativeAmount(BigDecimal accumulativeAmount) {
		this.accumulativeAmount = accumulativeAmount;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public boolean isActiveRisk() {
		return activeRisk;
	}

	public void setActiveRisk(boolean activeRisk) {
		this.activeRisk = activeRisk;
	}

	public boolean isDisplayLevel4() {
		return displayLevel4;
	}

	public void setDisplayLevel4(boolean displayLevel4) {
		this.displayLevel4 = displayLevel4;
	}

	public boolean isInputable() {
		return inputable;
	}

	public void setInputable(boolean inputable) {
		this.inputable = inputable;
	}
	
}
