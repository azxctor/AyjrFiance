package com.hengxin.platform.market.dto.downstream;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.common.util.MessageUtil;
import com.hengxin.platform.common.util.NumberUtil;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.product.dto.ProductDetailsDto;
import com.hengxin.platform.product.dto.ProductPackageDto;
import com.hengxin.platform.product.dto.ProductWarrantEnterpriseDto;
import com.hengxin.platform.product.dto.ProductWarrantPersonDto;
import com.hengxin.platform.product.enums.ERiskLevel;

public class TransferMarketItemDetailDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String lotId;

	private BigDecimal price;

	private BigDecimal rate;

	private BigDecimal profit;

	private Date supplyEndDate;

	private BigDecimal remainingAmount;
	
	private BigDecimal remainIntrAmount;

	private boolean transfered = true; // 是否可买入

	private String reason; // 不可买入的原因

	private ProductPackageDto pkg;

	private ProductDetailsDto product;

	private Date maturityDate;

	private String transferId; // 转让记录ID

	private Integer unit; // 份额

	private boolean closed = false; // 是否已卖出或者撤单
	
	private BigDecimal getEarningsAmt() {
		if (remainingAmount != null && price != null) {
			BigDecimal amount = remainingAmount.subtract(price).subtract(getTransferFee());
			Date signedDate = DateUtils.getDate(pkg.getSigningDt(), "yyyy-MM-dd");
			Date processDate = DateUtils.getDate("2014-05-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
			if (signedDate.equals(processDate) || signedDate.after(processDate)) {
				amount = amount.subtract(
						remainIntrAmount.multiply(
								CommonBusinessUtil.getPaymentInterestDeductRate()));
			}
			return amount.setScale(2, RoundingMode.HALF_UP);
		}
		return BigDecimal.ZERO;
	}
	
	public String getExpectedReturnStr() {
		return NumberUtil.formatMoney(getEarningsAmt());
	}
	
	public String getExpectedReturnRate() {
		if (price != null && !price.equals(BigDecimal.ZERO)) {
			return getEarningsAmt().multiply(BigDecimal.valueOf(36000)).divide(
					price.multiply(BigDecimal.valueOf(DateUtils.betweenDays(
							CommonBusinessUtil.getCurrentWorkDate(),
							maturityDate))), 2, RoundingMode.HALF_UP).toString()
					+ "%";
		}
		return "";
	}

	public String getRatePercentage() {
		return NumberUtil.getPercentStr(rate, 1, true);
	}

	public String getSupplyEndDateStr() {
		if (supplyEndDate != null) {
			return DateFormatUtils.format(supplyEndDate, "yyyy-MM-dd");
		}
		return "";
	}

	public String getMaturityDateStr() {
		if (maturityDate != null) {
			return DateFormatUtils.format(maturityDate, "yyyy-MM-dd");
		}
		return "";
	}
	
	private BigDecimal getTransferFee() {
		if (price != null) {
			return price.multiply(CommonBusinessUtil.getTransferFeeRate()).setScale(2, RoundingMode.HALF_UP);
		}
		return BigDecimal.ZERO;
	}

	public String getTransferFeeStr() {
		return NumberUtil.formatMoney(getTransferFee());
	}

	public String getPriceStr() {
		if (price != null) {
			return NumberUtil.formatMoney(price);
		}
		return "0";
	}

	public ERiskLevel getRiskLevel() {
		if (product != null) {
			return product.getProductLevel();
		}
		return ERiskLevel.NULL;
	}

	public String getWarrantPersonStr() {
		if (product != null
				&& CollectionUtils.isNotEmpty(product
						.getProductWarrantPersonDtoList())) {
			StringBuilder sb = new StringBuilder();
			for (ProductWarrantPersonDto dto : product
					.getProductWarrantPersonDtoList()) {
				sb.append(dto.getName());
				sb.append("、");
			}
			return sb.substring(0, sb.length() - 1);
		}
		return MessageUtil.getMessage("common.record.notfound");
	}

	public String getWarrantEnterpriseStr() {
		if (product != null
				&& CollectionUtils.isNotEmpty(product
						.getProductWarrantEnterpriseDtoList())) {
			StringBuilder sb = new StringBuilder();
			for (ProductWarrantEnterpriseDto dto : product
					.getProductWarrantEnterpriseDtoList()) {
				sb.append(dto.getEnterpriseName());
				sb.append("、");
			}
			return sb.substring(0, sb.length() - 1);
		}
		return MessageUtil.getMessage("common.record.notfound");
	}

	public String getRemainingAmountStr() {
		return remainingAmount == null ? "" : NumberUtil
				.formatMoney(remainingAmount);
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

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public BigDecimal getProfit() {
		return profit;
	}

	public void setProfit(BigDecimal profit) {
		this.profit = profit;
	}

	/**
	 * @return return the value of the var transfered
	 */

	public boolean isTransfered() {
		return transfered;
	}

	/**
	 * @param transfered
	 *            Set transfered value
	 */

	public void setTransfered(boolean transfered) {
		this.transfered = transfered;
	}

	/**
	 * @return return the value of the var reason
	 */

	public String getReason() {
		return reason;
	}

	/**
	 * @param reason
	 *            Set reason value
	 */

	public void setReason(String reason) {
		this.reason = reason;
	}

	/**
	 * @return return the value of the var supplyEndDate
	 */

	public Date getSupplyEndDate() {
		return supplyEndDate;
	}

	/**
	 * @param supplyEndDate
	 *            Set supplyEndDate value
	 */

	public void setSupplyEndDate(Date supplyEndDate) {
		this.supplyEndDate = supplyEndDate;
	}

	public ProductPackageDto getPkg() {
		return pkg;
	}

	public void setPkg(ProductPackageDto pkg) {
		this.pkg = pkg;
	}

	public ProductDetailsDto getProduct() {
		return product;
	}

	public void setProduct(ProductDetailsDto product) {
		this.product = product;
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

	public Integer getUnit() {
		return unit;
	}

	public void setUnit(Integer unit) {
		this.unit = unit;
	}

	public BigDecimal getRemainIntrAmount() {
		return remainIntrAmount;
	}

	public void setRemainIntrAmount(BigDecimal remainIntrAmount) {
		this.remainIntrAmount = remainIntrAmount;
	}

	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}

}
