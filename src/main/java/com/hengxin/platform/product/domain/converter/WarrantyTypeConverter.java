package com.hengxin.platform.product.domain.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.product.enums.EWarrantyType;

@Converter
public class WarrantyTypeConverter implements
		AttributeConverter<EWarrantyType, String> {

	@Override
	public String convertToDatabaseColumn(EWarrantyType attribute) {
		return attribute.getCode();
	}

	@Override
	public EWarrantyType convertToEntityAttribute(String dbData) {
		return EnumHelper.translate(EWarrantyType.class, dbData);
	}

}
