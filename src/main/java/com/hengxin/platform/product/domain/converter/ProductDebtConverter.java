package com.hengxin.platform.product.domain.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.product.enums.EProductDebtType;

@Converter
public class ProductDebtConverter implements
		AttributeConverter<EProductDebtType, String> {

	@Override
	public String convertToDatabaseColumn(EProductDebtType attribute) {
		return attribute.getCode();
	}

	@Override
	public EProductDebtType convertToEntityAttribute(String dbData) {
		return EnumHelper.translate(EProductDebtType.class, dbData);
	}

}
