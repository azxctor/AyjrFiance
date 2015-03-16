package com.hengxin.platform.common.util.resource.hazelcast;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IFunction;
import com.hazelcast.core.IMap;
import com.hengxin.platform.common.util.resource.ResourceException;
import com.hengxin.platform.common.util.resource.ResourcePool;

/**
 * Hazelcast implementation of the {@link ResourcePool}.
 * 
 * @author yeliangjin
 * 
 */
public class HazelcastResourcePool implements ResourcePool {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = org.slf4j.LoggerFactory
			.getLogger(HazelcastResourcePool.class);
	@Autowired
	private transient HazelcastInstance inst;

	@Override
	public void put(String key, long count) {
		inst.getAtomicLong(key).set(count);
	}

	@Override
	public void offer(String key, final long count) {
		inst.getAtomicLong(key).alter(new IFunction<Long, Long>() {
			private static final long serialVersionUID = 1L;

			@Override
            public Long apply(Long input) {
				return input + count;
			}
		});
	}

	@Override
	public long consume(String key, final long count) {
		Long oldValue = inst.getAtomicLong(key).getAndAlter(
				new IFunction<Long, Long>() {
					private static final long serialVersionUID = 1L;

					@Override
                    public Long apply(Long input) {
						if (input == 0) {
							LOG.debug("now empty");
							return input;
						}
						if (input < 0) {
							LOG.warn("now negative:{}", input);
							return 0L;
						}
						final long newValue = input - count;
						return newValue < 0 ? 0 : newValue;
					}
				});
		long ret = 0;
		if (oldValue < 0) {
			throw new ResourceException("BUG, old value=" + oldValue);
		} else if (oldValue == 0) {
			LOG.debug("nothing consumed");
		} else {
			if (oldValue < count) {
				// last lucky one
				ret = oldValue;
				LOG.debug("NOT satisfied. expect {} but only {} available",
						count, ret);
			} else {
				ret = count;
				LOG.debug("satisfied");
			}
		}
		return ret;
	}

    public IMap<String, Object> getMap(String name){
        return inst.getMap(name);
	}

	@Override
	public void remove(String key) {
		inst.getAtomicLong(key).set(-1);
	}

	@Override
	public long get(String key) {
		return inst.getAtomicLong(key).get();
	}
}
