package com.hengxin.platform.common.entity.id;

import java.io.Serializable;
import java.util.Date;

/**
 * Holds all information to make up a sequence.
 * 
 * @author yeliangjin
 * 
 */
public class Sequence implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long seq;

	private Date date;

	public Sequence() {

	}

	public Sequence(Long seq, Date date) {
		this.seq = seq;
		this.date = date;
	}

	public Long getSeq() {
		return seq;
	}

	public void setSeq(Long seq) {
		this.seq = seq;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
