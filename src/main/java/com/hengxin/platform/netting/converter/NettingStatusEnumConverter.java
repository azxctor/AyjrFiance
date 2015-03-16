package com.hengxin.platform.netting.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.netting.enums.NettingStatusEnum;

@Converter
public class NettingStatusEnumConverter implements AttributeConverter<NettingStatusEnum, String>{
	
	@Override
    public String convertToDatabaseColumn(NettingStatusEnum attribute) {
    	return attribute.getCode();
    }

    @Override
    public NettingStatusEnum convertToEntityAttribute(String dbData) {
    	return EnumHelper.translate(NettingStatusEnum.class, dbData);
    }
}
