package com.hengxin.platform.batch.entity.converter;

import javax.persistence.AttributeConverter;

import com.hengxin.platform.batch.enums.EJobWorkStatus;
import com.hengxin.platform.common.util.EnumHelper;

public class EJobWorkStatusConverter implements AttributeConverter<EJobWorkStatus, String> {

    @Override
    public String convertToDatabaseColumn(EJobWorkStatus attribute) {
        return attribute.getCode();
    }

    @Override
    public EJobWorkStatus convertToEntityAttribute(String dbData) {
        return EnumHelper.translate(EJobWorkStatus.class, dbData);
    }
}
