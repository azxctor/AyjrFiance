package com.hengxin.platform.product.domain.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.product.enums.EProductStatus;

@Converter
public class ProductStatusConverter implements AttributeConverter<EProductStatus, String> {

    @Override
    public String convertToDatabaseColumn(EProductStatus attribute) {
        return attribute.getCode();
    }

    @Override
    public EProductStatus convertToEntityAttribute(String dbData) {
        return EnumHelper.translate(EProductStatus.class, dbData);
    }

}
