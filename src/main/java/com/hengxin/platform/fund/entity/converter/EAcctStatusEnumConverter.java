package com.hengxin.platform.fund.entity.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.fund.enums.EAcctStatus;

@Converter
public class EAcctStatusEnumConverter implements AttributeConverter<EAcctStatus, String> {

    @Override
    public String convertToDatabaseColumn(EAcctStatus attribute) {
        return attribute.getCode();
    }

    @Override
    public EAcctStatus convertToEntityAttribute(String dbData) {
        return EnumHelper.translate(EAcctStatus.class, dbData);
    }
}
