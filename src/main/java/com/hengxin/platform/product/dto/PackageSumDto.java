package com.hengxin.platform.product.dto;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * Class Name: PackageSumDto Description: TODO
 * 
 * @author linuxp
 * 
 */
public class PackageSumDto implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 融资额汇总
	 */
	private BigDecimal sum_quota;
	
	/**
	 * 实际申购额汇总
	 */
	private BigDecimal sum_invstr;
	
	private String count;

	public BigDecimal getSum_quota() {
		return sum_quota;
	}

	public void setSum_quota(BigDecimal sum_quota) {
		this.sum_quota = sum_quota;
	}

	public BigDecimal getSum_invstr() {
		return sum_invstr;
	}

	public void setSum_invstr(BigDecimal sum_invstr) {
		this.sum_invstr = sum_invstr;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}
}