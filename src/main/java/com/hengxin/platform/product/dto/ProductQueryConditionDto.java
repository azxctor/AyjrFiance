package com.hengxin.platform.product.dto;

import com.hengxin.platform.common.dto.DataTablesRequestDto;
import com.hengxin.platform.product.enums.EProductActionType;
import com.hengxin.platform.product.enums.EProductStatus;
import com.hengxin.platform.product.enums.EResultType;

public class ProductQueryConditionDto extends DataTablesRequestDto {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String searchKeyString;  //关键字
	private EProductStatus productStatus; // 融资项目状态
    private EResultType resultType; //待处理，已处理
    private EProductActionType productActionType; //动作类型
    private String comString; //包或者产品状态    
  		
	public String getSearchKeyString() {
		return searchKeyString;
	}
	public void setSearchKeyString(String searchKeyString) {
		this.searchKeyString = searchKeyString;
	}
	public EProductStatus getProductStatus() {
		return productStatus;
	}
	public void setProductStatus(EProductStatus productStatus) {
		this.productStatus = productStatus;
	}
	public EResultType getResultType() {
		return resultType;
	}
	public void setResultType(EResultType resultType) {
		this.resultType = resultType;
	}
	public EProductActionType getProductActionType() {
		return productActionType;
	}
	public void setProductActionType(EProductActionType productActionType) {
		this.productActionType = productActionType;
	}
	public String getComString() {
		return comString;
	}
	public void setComString(String comString) {
		this.comString = comString;
	}	
}
