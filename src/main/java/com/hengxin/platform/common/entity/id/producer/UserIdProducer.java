package com.hengxin.platform.common.entity.id.producer;

import java.text.MessageFormat;

import com.hengxin.platform.common.entity.id.Context;
import com.hengxin.platform.common.entity.id.DefaultIdProducer;
import com.hengxin.platform.common.entity.id.SequenceConsumer;
import com.hengxin.platform.security.entity.UserPo;

/**
 * Produces internal user id.
 * 
 * @author yeliangjin
 * 
 */
public class UserIdProducer extends DefaultIdProducer<UserPo> {

	private static final String ID_FORMAT = "88{0}";
	private static final int SEQ_LENGTH = 10;

	@Override
	protected String getSequenceType() {
		return SequenceConsumer.Interval.NO.value();
	}

	@Override
	protected Object doProduceId(Context ctx) {
		return MessageFormat.format(ID_FORMAT, formatSeq(ctx));
	}

	@Override
	protected int getSeqLength() {
		return SEQ_LENGTH;
	}

	@Override
	protected boolean cacheEnabled() {
		return false;
	}

}
