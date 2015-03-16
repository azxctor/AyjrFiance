package com.hengxin.platform.product.domain.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.product.enums.EFeePayMethodType;

@Converter
public class FeeTypeConverter implements AttributeConverter<EFeePayMethodType, String> {

	@Override
	public String convertToDatabaseColumn(EFeePayMethodType attribute) {
		return attribute.getCode();
	}

	@Override
	public EFeePayMethodType convertToEntityAttribute(String dbData) {
		return EnumHelper.translate(EFeePayMethodType.class, dbData);
	}

}