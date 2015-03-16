package com.hengxin.platform.fund.entity.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.fund.enums.EBnkTrxStatus;

@Converter
public class EBnkTrxStatusEnumConverter implements AttributeConverter<EBnkTrxStatus, String> {

    @Override
    public String convertToDatabaseColumn(EBnkTrxStatus attribute) {
        return attribute.getCode();
    }

    @Override
    public EBnkTrxStatus convertToEntityAttribute(String dbData) {
        return EnumHelper.translate(EBnkTrxStatus.class, dbData);
    }

}
