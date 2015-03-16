package com.hengxin.platform.common.entity.id;

/**
 * Strategy interface for building the identifier of an entity.
 * 
 * @author yeliangjin
 * 
 */
interface IdProducer<T> {

	/**
	 * Produces an identifier with provided context.
	 */
	Object produce(Context ctx);

}
