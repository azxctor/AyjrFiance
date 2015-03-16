package com.hengxin.platform.fund.entity.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.fund.enums.EFundTxOpt;

/**
 * Class Name: FundTxOptEnumConverter
 * <p>
 * Description: TODO
 * 
 * @author jishen
 * 
 */
@Converter
public class FundTxOptEnumConverter implements AttributeConverter<EFundTxOpt, String> {

	@Override
	public String convertToDatabaseColumn(EFundTxOpt attribute) {
		return attribute.getCode();
	}

	@Override
	public EFundTxOpt convertToEntityAttribute(String dbData) {
		return EnumHelper.translate(EFundTxOpt.class, dbData);
	}
}
