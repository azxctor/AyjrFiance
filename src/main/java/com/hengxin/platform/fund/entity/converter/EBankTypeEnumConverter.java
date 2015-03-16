package com.hengxin.platform.fund.entity.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.fund.enums.EBankType;

@Converter
public class EBankTypeEnumConverter implements AttributeConverter<EBankType, String> {

    @Override
    public String convertToDatabaseColumn(EBankType attribute) {
        return attribute.getCode();
    }

    @Override
    public EBankType convertToEntityAttribute(String dbData) {
        return EnumHelper.translate(EBankType.class, dbData);
    }
    
}
