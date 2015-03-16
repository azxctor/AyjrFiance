package com.hengxin.platform.common.util.resource;

import java.io.Serializable;

/**
 * A resource pool should provide cluster-wide pool where resources can be
 * offered/consumed in a concurrent-safe manner.
 * 
 * @author yeliangjin
 * 
 */
public interface ResourcePool extends Serializable {

	/**
	 * Set the amount of resources in the pool.
	 */
	void put(String key, long count);

	/**
	 * Retrieve current amount of specified resource.
	 */
	long get(String key);

	/**
	 * Offer additional amount of resources into the pool.
	 */
	void offer(String key, long count);

	/**
	 * Consume specified amount of resources from the pool.
	 */
	long consume(String key, long count);

	/**
	 * Remove specified resource from the pool.
	 */
	void remove(String key);

}
