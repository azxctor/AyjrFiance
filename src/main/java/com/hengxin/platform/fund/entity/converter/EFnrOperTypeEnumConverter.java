package com.hengxin.platform.fund.entity.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.fund.enums.EFnrOperType;

@Convert
public class EFnrOperTypeEnumConverter implements AttributeConverter<EFnrOperType, String> {

    @Override
    public String convertToDatabaseColumn(EFnrOperType attribute) {
        return attribute.getCode();
    }

    @Override
    public EFnrOperType convertToEntityAttribute(String dbData) {
        return EnumHelper.translate(EFnrOperType.class, dbData);
    }

}
