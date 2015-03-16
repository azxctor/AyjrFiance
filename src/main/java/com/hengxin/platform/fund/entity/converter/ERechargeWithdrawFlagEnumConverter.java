package com.hengxin.platform.fund.entity.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.fund.enums.ERechargeWithdrawFlag;

@Converter
public class ERechargeWithdrawFlagEnumConverter implements AttributeConverter<ERechargeWithdrawFlag, String> {

    @Override
    public String convertToDatabaseColumn(ERechargeWithdrawFlag attribute) {
        return attribute.getCode();
    }

    @Override
    public ERechargeWithdrawFlag convertToEntityAttribute(String dbData) {
        return EnumHelper.translate(ERechargeWithdrawFlag.class, dbData);
    }
}
