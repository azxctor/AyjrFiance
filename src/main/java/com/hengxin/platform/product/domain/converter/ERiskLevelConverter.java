package com.hengxin.platform.product.domain.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.apache.commons.lang.StringUtils;

import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.product.enums.ERiskLevel;

@Converter
public class ERiskLevelConverter implements AttributeConverter<ERiskLevel, String> {

    @Override
    public String convertToDatabaseColumn(ERiskLevel attribute) {
        return attribute.getCode();
    }

    @Override
    public ERiskLevel convertToEntityAttribute(String dbData) {
    	if (StringUtils.isBlank(dbData)) {
    		return ERiskLevel.NULL;
    	}
        return EnumHelper.translate(ERiskLevel.class, dbData);
    }

}
