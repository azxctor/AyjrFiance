package com.hengxin.platform.escrow.converter;

import javax.persistence.AttributeConverter;

import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.escrow.enums.EOrderStatusEnum;

public class EOrderStatusEnumConverter implements AttributeConverter<EOrderStatusEnum, String> {
	
	@Override
    public String convertToDatabaseColumn(EOrderStatusEnum attribute) {
        return attribute.getCode();
    }

    @Override
    public EOrderStatusEnum convertToEntityAttribute(String dbData) {
        return EnumHelper.translate(EOrderStatusEnum.class, dbData);
    }
}
