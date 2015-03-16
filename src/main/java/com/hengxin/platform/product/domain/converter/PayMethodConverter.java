package com.hengxin.platform.product.domain.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.product.enums.EPayMethodType;

@Converter
public class PayMethodConverter implements
		AttributeConverter<EPayMethodType, String> {

	@Override
	public String convertToDatabaseColumn(EPayMethodType attribute) {
		return attribute.getCode();
	}

	@Override
	public EPayMethodType convertToEntityAttribute(String dbData) {
		return EnumHelper.translate(EPayMethodType.class, dbData);
	}

}
