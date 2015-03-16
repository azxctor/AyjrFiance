package com.hengxin.platform.common.entity.id;

/**
 * Sequence producer.
 * 
 * @author yeliangjin
 * 
 */
public interface SequenceProducer {

	/**
	 * Produces a sequence for given name.
	 */
	Sequence produce(String name, String type);

}
