package com.hengxin.platform.common.paging;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.PageRequest;

/**
 * 
 * @author Gregory
 */
public class SearchCriteria {

	private PageRequest page;
	
	private String sEcho;
	
	/**
	 * key attribute of PO.
	 */
	Map<String, SubCriteria> map = new HashMap<String, SubCriteria>();
	
	public SearchCriteria(PageRequest page) {
		super();
		this.page = page;
	}

	/**
	 * 
	 * @param attribute  field of PO, it should be consistent with Domain object.<br/>
	 * 		If it is a field in an embedded PO, it should use '<strong>.</strong>' to connect its attribute in current PO and its attribute in child PO.<br/>
	 * 		EG :  address<strong>.</strong>street.
	 * @param value
	 * @param operator
	 */
	public void add(String attribute, Object value, EOperator operator) {
		if (attribute == null || operator == null) {
			return;
		}
		map.put(attribute, new SubCriteria(operator, value));
	}
	
	public void add(String attribute, Object value, EOperator operator, Class<?> type) {
		
	}
	
	public Map<String, SubCriteria> getMap() {
		return map;
	}

	public PageRequest getPage() {
		return page;
	}

	public String getsEcho() {
		return sEcho;
	}

	public void setsEcho(String sEcho) {
		this.sEcho = sEcho;
	}

}
