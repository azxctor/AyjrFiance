package com.hengxin.platform.product.domain.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.product.enums.ETermType;

@Converter
public class ProductTermConverter implements
		AttributeConverter<ETermType, String> {

	@Override
	public String convertToDatabaseColumn(ETermType attribute) {
		return attribute.getCode();
	}

	@Override
	public ETermType convertToEntityAttribute(String dbData) {
		return EnumHelper.translate(ETermType.class, dbData);
	}

}
