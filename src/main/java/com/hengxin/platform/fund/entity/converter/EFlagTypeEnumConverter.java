package com.hengxin.platform.fund.entity.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.fund.enums.EFlagType;

@Converter
public class EFlagTypeEnumConverter implements AttributeConverter<EFlagType, String> {

    @Override
    public String convertToDatabaseColumn(EFlagType attribute) {
        return attribute.getCode();
    }

    @Override
    public EFlagType convertToEntityAttribute(String dbData) {
        return EnumHelper.translate(EFlagType.class, dbData);
    }
}