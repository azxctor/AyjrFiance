package com.hengxin.platform.common.entity.id;

import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IAtomicLong;
import com.hazelcast.core.IMap;
import com.hengxin.platform.common.util.CacheUtil;

/**
 * Database based sequence generator with cache support to improve performance.
 * 
 * @author yeliangjin
 * 
 */

@Component("cachingSequenceProducer")
public class CachingDatabaseSequenceProducer extends DatabaseSequenceProducer {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CachingDatabaseSequenceProducer.class);
	private static final String CACHE_NAME = "seqCache";
	private static final Long CACHE_LIMIT = 100L;
	private static final String SEQ_KEY_PREFIX = "SEQ:";

	@Autowired
	private HazelcastInstance hcInstance;

	/**
	 * Produces a sequence number via SP.
	 */
	protected Sequence doProduce(String seqName, String type) {
		if (!CacheUtil.isCacheEnabled()) {
			return super.doProduce(seqName, type);
		}
		IMap<Object, Object> seqMap = hcInstance.getMap(CACHE_NAME);
		Sequence limit = (Sequence) seqMap.get(seqName);
		if (limit != null) {
			long newSeq = getSequenceHolder(seqName).incrementAndGet();
			if (newSeq <= limit.getSeq()) {
				return new Sequence(newSeq, limit.getDate());
			}

		}
		seqMap.lock(seqName);
		try {
			long curSeq = getSequenceHolder(seqName).get();
			if (!seqMap.containsKey(seqName)
					|| curSeq > ((Sequence) seqMap.get(seqName)).getSeq()) {
				Sequence seq = super.doProduce(seqName, type);
				seq.setSeq(seq.getSeq() - 1);
				long incremented = incrementSqeuence(seqName, CACHE_LIMIT - 1);
				long expected = seq.getSeq() + CACHE_LIMIT;
				if (incremented != expected) {
					LOGGER.warn(
							"sequence {} was not incremented as expected: {}, actual: {}",
							seqName, expected, incremented);
					getSequenceHolder(seqName).set(
							incremented - CACHE_LIMIT + 1);
				} else {
					getSequenceHolder(seqName).set(seq.getSeq());

				}
				seqMap.put(seqName, new Sequence(incremented, seq.getDate()));
			}
		} finally {
			seqMap.unlock(seqName);
		}
		return doProduce(seqName, type);
	}

	protected IAtomicLong getSequenceHolder(String seqName) {
		return hcInstance.getAtomicLong(SEQ_KEY_PREFIX + seqName);
	}

	/**
	 * Increment a sequence.
	 */
	protected Long incrementSqeuence(String seqName, Long delta) {
		LOGGER.debug("incrementing sqeuence: {}, by: {}", seqName, delta);
		StoredProcedureQuery query = em
				.createStoredProcedureQuery("PKG_UTIL.P_INCR_SEQ");
		query.registerStoredProcedureParameter(1, String.class,
				ParameterMode.IN);
		query.setParameter(1, seqName);
		query.registerStoredProcedureParameter(2, Long.class, ParameterMode.IN);
		query.setParameter(2, delta);
		query.registerStoredProcedureParameter(3, Long.class, ParameterMode.OUT);
		query.execute();
		Long result = (Long) query.getOutputParameterValue(3);
		LOGGER.debug("incremented sqeuence: {}, by: {}, now: {}", seqName,
				delta, result);
		return result;
	}

	@Component
	public static final class CacheWatcher {

		@Autowired
		private HazelcastInstance hcInstance;

		public void refresh() {
			hcInstance.getMap(CACHE_NAME).clear();
		}

	}

}
