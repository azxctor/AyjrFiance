package com.hengxin.platform.app.dto.upstream;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * CreditorMarketupDto.
 * 
 */
public class CreditorMarketupDto extends PagingDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotEmpty(message = "{member.error.field.empty}")
	private String state = "";
	
	@NotEmpty(message = "{member.error.field.empty}")
	private String id = "";
	

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
