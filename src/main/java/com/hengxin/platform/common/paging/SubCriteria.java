package com.hengxin.platform.common.paging;

/**
 * 
 * @author Gregory
 */
public class SubCriteria {

	private EOperator operator;
	
	private Object value;
	
	public SubCriteria(EOperator operator, Object value) {
		super();
		this.operator = operator;
		this.value = value;
	}
	
	public EOperator getOperator() {
		return operator;
	}
	
	public Object getValue() {
		return value;
	}
	
}
