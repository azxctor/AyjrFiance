package com.hengxin.platform.product.domain.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.product.enums.EAutoSubscribeStatus;

@Converter
public class AutoSubscribeConverter implements
		AttributeConverter<EAutoSubscribeStatus, String> {

	@Override
	public String convertToDatabaseColumn(EAutoSubscribeStatus attribute) {
		return attribute.getCode();
	}

	@Override
	public EAutoSubscribeStatus convertToEntityAttribute(String dbData) {
		return EnumHelper.translate(EAutoSubscribeStatus.class, dbData);
	}

}
