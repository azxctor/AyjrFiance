package com.hengxin.platform.common.entity.id;

import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.apache.commons.lang.StringUtils.leftPad;
import static org.apache.commons.lang.time.DateFormatUtils.format;

import java.lang.annotation.Annotation;

import javax.persistence.Table;

import com.hengxin.platform.common.util.ApplicationContextUtil;

/**
 * A default implementation of {@link IdProducer}. <br>
 * Simply concatenates 6-byte current date and the sequence number.
 * 
 * @author yeliangjin
 * 
 */
public class DefaultIdProducer<T> implements IdProducer<T> {

	private static final String PREFIX = "yyMMdd";
	private static final int SEQ_LENGTH = 8;

	@Override
	public Object produce(Context ctx) {
		doProduceSequence(ctx);
		return doProduceId(ctx);
	}

	/**
	 * Template method where the identifier is really produced.
	 */
	protected Object doProduceId(Context ctx) {
		StringBuilder sb = new StringBuilder();
		sb.append(format(ctx.getSequence().getDate(), PREFIX));
		sb.append(formatSeq(ctx));
		return sb.toString();
	}

	/**
	 * The length of the sequence that will be padded into the identifier.
	 */
	protected int getSeqLength() {
		return SEQ_LENGTH;
	}

	/**
	 * Format sequence to expected length.
	 */
	protected String formatSeq(Context ctx) {
		return leftPad(ctx.getSequence().getSeq().toString(), getSeqLength(),
				'0');
	}

	/**
	 * Produce sequence first.
	 */

	protected void doProduceSequence(Context ctx) {
		SequenceProducer sequenceProducer = ApplicationContextUtil
				.getBean(cacheEnabled() ? "cachingSequenceProducer"
						: "sequenceProducer", SequenceProducer.class);
		Sequence seq = sequenceProducer.produce(getSequenceName(getEntity(ctx))
				+ getSequenceNameSuffix(ctx), getSequenceType());
		ctx.setSequence(seq);
	}

	protected boolean cacheEnabled() {
		return true;
	}

	@SuppressWarnings("unchecked")
	protected T getEntity(Context ctx) {
		return (T) ctx.getEntity();
	}

	/**
	 * Get the sequence name for target entity.
	 */
	protected String getSequenceName(T entity) {
		if (entity instanceof String) {
			// as a short cut in case the name is provided intentionally
			return (String) entity;
		}
		Table table = findAnnotation(entity.getClass(), Table.class);
		if (table == null) {
			throw new IllegalArgumentException(
					"A invalid object received when generating sequence: "
							+ entity.getClass());
		}
		return table.name();
	}

	/**
	 * Get the sequence name suffix for target entity, in case multiple
	 * sequences are needed for one type.
	 */
	protected String getSequenceNameSuffix(Context ctx) {
		return EMPTY;
	}

	/**
	 * Get the sequence type for target entity.
	 */
	protected String getSequenceType() {
		return SequenceConsumer.Interval.DAILY.value();
	}

	protected <AT extends Annotation> AT findAnnotation(Class<?> entityType,
			Class<AT> annotationType) {
		Class<?> clz = entityType;
		AT annotation = null;
		while (annotation == null && clz != Object.class) {
			annotation = clz.getAnnotation(annotationType);
			clz = clz.getSuperclass();
		}
		return annotation;
	}

}
