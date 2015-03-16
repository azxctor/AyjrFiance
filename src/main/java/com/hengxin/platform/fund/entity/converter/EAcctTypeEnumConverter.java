package com.hengxin.platform.fund.entity.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.fund.enums.EAcctType;

@Converter
public class EAcctTypeEnumConverter implements AttributeConverter<EAcctType, String> {

    @Override
    public String convertToDatabaseColumn(EAcctType attribute) {
        return attribute.getCode();
    }

    @Override
    public EAcctType convertToEntityAttribute(String dbData) {
        return EnumHelper.translate(EAcctType.class, dbData);
    }
}
