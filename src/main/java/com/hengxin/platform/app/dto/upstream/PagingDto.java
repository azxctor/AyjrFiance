package com.hengxin.platform.app.dto.upstream;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * PagingDto.
 *
 */
public class PagingDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@NotEmpty(message = "{member.error.field.empty}")
	private String displayStart;

	public String getDisplayStart() {
		return displayStart;
	}

	public void setDisplayStart(String displayStart) {
		this.displayStart = displayStart;
	}

}
