package com.hengxin.platform.fund.entity.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.fund.enums.EFundCurCode;

/**
 * Class Name: FundCurCodeEnumConverter
 * <p>
 * Description: TODO
 * 
 * @author jishen
 * 
 */
@Converter
public class FundCurCodeEnumConverter implements AttributeConverter<EFundCurCode, String> {

	@Override
	public String convertToDatabaseColumn(EFundCurCode attribute) {
		return attribute.getCode();
	}

	@Override
	public EFundCurCode convertToEntityAttribute(String dbData) {
		return EnumHelper.translate(EFundCurCode.class, dbData);
	}

}
