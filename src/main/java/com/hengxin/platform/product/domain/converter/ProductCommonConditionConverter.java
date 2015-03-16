package com.hengxin.platform.product.domain.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.product.enums.EProductCommonCondition;

@Converter
public class ProductCommonConditionConverter implements AttributeConverter<EProductCommonCondition, String> {

	@Override
	public String convertToDatabaseColumn(EProductCommonCondition attribute) {
		return attribute.getCode();
	}

	@Override
	public EProductCommonCondition convertToEntityAttribute(String dbData) {
		return EnumHelper.translate(EProductCommonCondition.class, dbData);
	}

}