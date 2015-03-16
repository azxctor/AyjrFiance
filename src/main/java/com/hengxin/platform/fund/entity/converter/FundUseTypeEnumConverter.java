/**
 * 
 */
package com.hengxin.platform.fund.entity.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.fund.enums.EFundUseType;

/**
 * @author congzhou
 *
 */
@Converter
public class FundUseTypeEnumConverter implements AttributeConverter<EFundUseType, String>{

    @Override
    public String convertToDatabaseColumn(EFundUseType attribute) {
    	return attribute.getCode();
    }

    @Override
    public EFundUseType convertToEntityAttribute(String dbData) {
    	return EnumHelper.translate(EFundUseType.class, dbData);
    }

}
