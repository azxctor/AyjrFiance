package com.hengxin.platform.security.convert;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.security.enums.BindingCardStatusEnum;

@Converter
public class BindingCardStatusEnumConvert implements AttributeConverter<BindingCardStatusEnum, String> {

	@Override
	public String convertToDatabaseColumn(BindingCardStatusEnum attribute) {
		return attribute.getCode();
	}

	@Override
	public BindingCardStatusEnum convertToEntityAttribute(String dbData) {
		return EnumHelper.translate(BindingCardStatusEnum.class, dbData);
	}

}
