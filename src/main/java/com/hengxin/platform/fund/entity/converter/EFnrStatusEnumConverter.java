package com.hengxin.platform.fund.entity.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.fund.enums.EFnrStatus;

@Converter
public class EFnrStatusEnumConverter implements AttributeConverter<EFnrStatus, String> {

    @Override
    public String convertToDatabaseColumn(EFnrStatus attribute) {
        return attribute.getCode();
    }

    @Override
    public EFnrStatus convertToEntityAttribute(String dbData) {
        return EnumHelper.translate(EFnrStatus.class, dbData);
    }
}
