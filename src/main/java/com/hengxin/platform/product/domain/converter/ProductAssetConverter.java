package com.hengxin.platform.product.domain.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.product.enums.EProductAssetType;

@Converter
public class ProductAssetConverter implements
		AttributeConverter<EProductAssetType, String> {

	@Override
	public String convertToDatabaseColumn(EProductAssetType attribute) {
		return attribute.getCode();
	}

	@Override
	public EProductAssetType convertToEntityAttribute(String dbData) {
		return EnumHelper.translate(EProductAssetType.class, dbData);
	}

}
