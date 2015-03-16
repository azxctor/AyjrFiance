package com.hengxin.platform.product.domain.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.product.enums.EPayStatus;

@Converter
public class EPayStatusConverter implements AttributeConverter<EPayStatus, String> {

    @Override
    public String convertToDatabaseColumn(EPayStatus attribute) {
        return attribute.getCode();
    }

    @Override
    public EPayStatus convertToEntityAttribute(String dbData) {
        return EnumHelper.translate(EPayStatus.class, dbData);
    }

}
