package com.hengxin.platform.fund.entity.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.fund.enums.ECashPool;

@Converter
public class ECashPoolEnumConverter implements AttributeConverter<ECashPool, String> {

    @Override
    public String convertToDatabaseColumn(ECashPool attribute) {
        return attribute.getCode();
    }

    @Override
    public ECashPool convertToEntityAttribute(String dbData) {
        return EnumHelper.translate(ECashPool.class, dbData);
    }
}
