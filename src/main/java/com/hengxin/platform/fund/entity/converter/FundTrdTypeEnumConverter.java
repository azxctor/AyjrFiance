package com.hengxin.platform.fund.entity.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.fund.enums.EFundTrdType;

/**
 * Class Name: FundEFundTrdTypeEnumConverter
 * <p>
 * Description: TODO
 * 
 * @author congzhou
 * 
 */
@Converter
public class FundTrdTypeEnumConverter implements AttributeConverter<EFundTrdType, String> {

	@Override
	public String convertToDatabaseColumn(EFundTrdType attribute) {
		return attribute.getCode();
	}

	@Override
	public EFundTrdType convertToEntityAttribute(String dbData) {
		return EnumHelper.translate(EFundTrdType.class, dbData);
	}
}
