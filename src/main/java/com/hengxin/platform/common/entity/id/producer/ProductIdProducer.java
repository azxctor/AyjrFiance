package com.hengxin.platform.common.entity.id.producer;

import java.text.MessageFormat;

import com.hengxin.platform.common.entity.id.Context;
import com.hengxin.platform.common.entity.id.DefaultIdProducer;
import com.hengxin.platform.common.entity.id.SequenceConsumer;
import com.hengxin.platform.product.domain.Product;

/**
 * 
 * 
 * @author yeliangjin
 * 
 */
public class ProductIdProducer extends DefaultIdProducer<Product> {

	private static final String ID_FORMAT = "{0}{1,date,yy}{2}";
	private static final int SEQ_LENGTH = 6;

	@Override
	protected String getSequenceType() {
		return SequenceConsumer.Interval.YEARLY.value();
	}

	@Override
	protected String getSequenceNameSuffix(Context ctx) {
		return "_" + getType(ctx);
	}

	@Override
	protected Object doProduceId(Context ctx) {
		return MessageFormat.format(ID_FORMAT, getType(ctx), ctx.getSequence()
				.getDate(), formatSeq(ctx));
	}

	@Override
	protected int getSeqLength() {
		return SEQ_LENGTH;
	}

	private String getType(Context ctx) {
		return getEntity(ctx).getWarrantyType().getCode();
	}

	@Override
	protected boolean cacheEnabled() {
		return false;
	}

}
