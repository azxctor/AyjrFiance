package com.hengxin.platform.common.entity.id;


/**
 * Holds all context information needed for building an id.
 * 
 * @author yeliangjin
 * 
 */
public class Context {

	/**
	 * Target entity.
	 */
	private Object entity;

	/**
	 * Sequence of target entity.
	 */
	private Sequence sequence;

	/**
	 * Additional parameter.
	 */
	private Object param;

	Context() {

	}

	Context(Object entity) {
		this.entity = entity;
	}

	public Object getEntity() {
		return entity;
	}

	public void setEntity(Object entity) {
		this.entity = entity;
	}

	public Sequence getSequence() {
		return sequence;
	}

	public void setSequence(Sequence sequence) {
		this.sequence = sequence;
	}

	public Object getParam() {
		return param;
	}

	public void setParam(Object param) {
		this.param = param;
	}

}
