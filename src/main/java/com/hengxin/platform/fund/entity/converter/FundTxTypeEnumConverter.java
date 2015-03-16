package com.hengxin.platform.fund.entity.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.fund.enums.EFundTxType;

/**
 * Class Name: FundTxTypeEnumConverter
 * <p>
 * Description: TODO
 * 
 * @author congzhou
 * 
 */
@Converter
public class FundTxTypeEnumConverter implements AttributeConverter<EFundTxType, String> {

	@Override
	public String convertToDatabaseColumn(EFundTxType attribute) {
		return attribute.getCode();
	}

	@Override
	public EFundTxType convertToEntityAttribute(String dbData) {
		return EnumHelper.translate(EFundTxType.class, dbData);
	}
}
