package com.hengxin.platform.market.dto.upstream;

import java.io.Serializable;
import java.util.Date;

import com.hengxin.platform.common.dto.DataTablesRequestDto;
import com.hengxin.platform.market.enums.ETransferType;

public class TransferMarketSearchDto extends DataTablesRequestDto implements
		Serializable {

	private static final long serialVersionUID = 1L;

	private String productId;

	private String packageId;

	private ETransferType transferType = ETransferType.OTHER;
	
    private Date createTime; // 当前系统时间戳

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public ETransferType getTransferType() {
		return transferType;
	}

	public void setTransferType(ETransferType transferType) {
		this.transferType = transferType;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
