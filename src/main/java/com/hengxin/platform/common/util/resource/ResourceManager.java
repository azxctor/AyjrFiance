package com.hengxin.platform.common.util.resource;

/**
 * Service layer facade interface to provide resource-oriented service.
 * Implementation of this interface should:<br/>
 * 1. interact with {@link ResourcePool} <br/>
 * 2. implement business logic as needed
 * 
 * @author yeliangjin
 * 
 */
public interface ResourceManager<T> {

	/**
	 * Consume specified amount of resources from the pool.
	 */
	long consume(String key, long count);

	/**
	 * Retrieve current amount of specified resource.
	 */
	long get(String res);

	/**
	 * Offer additional amount of resources into the pool.
	 */
	void offer(String key, long count);

	/**
	 * Add item to pool, also can add some info into map.
	 */
	void addItem(T item, boolean deep);

	/**
	 * Get item in the pool.
	 */
	T getItem(String key);

	/**
	 * Remove item from the pool.
	 */
	void removeItem(String key);

	/**
	 * Remove item from the pool.
	 */
	void removeItem(String key, boolean deep);

}
