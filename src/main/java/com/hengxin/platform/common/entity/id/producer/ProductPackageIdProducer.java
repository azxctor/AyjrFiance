package com.hengxin.platform.common.entity.id.producer;

import java.text.MessageFormat;

import com.hengxin.platform.common.entity.id.Context;
import com.hengxin.platform.common.entity.id.DefaultIdProducer;
import com.hengxin.platform.product.domain.ProductPackage;

/**
 *
 *
 * @author yeliangjin
 *
 */
public class ProductPackageIdProducer extends DefaultIdProducer<ProductPackage> {

	private static final String ID_FORMAT = "{0}{1}{2,number,00}";

	@Override
	protected Object doProduceId(Context ctx) {
		return MessageFormat.format(ID_FORMAT, getRiskLevel(ctx),
				getEntity(ctx).getProductId(), getEntity(ctx).getIndex());
	}

	private String getRiskLevel(Context ctx) {
        return getEntity(ctx).getProduct().getProductLevel().getCode();
	}

	@Override
	protected void doProduceSequence(Context ctx) {
	}

}
