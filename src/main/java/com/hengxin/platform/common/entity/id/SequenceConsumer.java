package com.hengxin.platform.common.entity.id;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker interface to annotate an entity that consumes sequence.
 * 
 * @author yeliangjin
 * 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SequenceConsumer {

	/**
	 * The interval to recycle the sequence.
	 */
	Interval interval();

	enum Interval {

		NO("N"), DAILY("D"), MONTHLY("M"), QUARTERLY("Q"), YEARLY("Y");

		private String value;

		Interval(String value) {
			this.value = value;
		}

		public String value() {
			return this.value;
		}

	}
}
