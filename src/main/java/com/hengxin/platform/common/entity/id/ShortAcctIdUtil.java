package com.hengxin.platform.common.entity.id;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ShortAcctIdUtil {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ShortAcctIdUtil.class);

	private ShortAcctIdUtil() {

	}

	public static String generateId(String userId) {
		return produce(userId);
	}

	/**
	 * In case a unique id is needed for no specific business purpose.
	 */
	private static String produce(final String userId) {
		long start = System.currentTimeMillis();
		IdProducer<ShortAcctIdContext> p = IdProducerFactory
				.getProducer(ShortAcctIdContext.class);
		Object id = p.produce(new Context(new ShortAcctIdContext() {
			@Override
			public String getUserId() {
				return userId;
			}
		}));
		LOGGER.info("SHORT_ACCT identifier generated in {} ms: {}",
				System.currentTimeMillis() - start, id);
		return id.toString();
	}

	public static void main(String[] args) {
		String seqNo = "124657";
		String acctNo = StringUtils.leftPad(seqNo, 3, '0');
		System.out.println(acctNo);
	}
}
