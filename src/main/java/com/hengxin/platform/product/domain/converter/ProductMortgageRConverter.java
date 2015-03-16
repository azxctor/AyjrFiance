package com.hengxin.platform.product.domain.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.product.enums.EProductMortgageRType;

@Converter
public class ProductMortgageRConverter implements AttributeConverter<EProductMortgageRType, String> {

	@Override
	public String convertToDatabaseColumn(EProductMortgageRType attribute) {
		return attribute.getCode();
	}

	@Override
	public EProductMortgageRType convertToEntityAttribute(String dbData) {
		return EnumHelper.translate(EProductMortgageRType.class, dbData);
	}

}