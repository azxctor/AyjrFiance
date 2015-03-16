package com.hengxin.platform.fund.entity.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.fund.enums.EFundCurFlag;

/**
 * Class Name: FundCurFlagEnumConverter
 * <p>
 * Description: TODO
 * 
 * @author jishen
 * 
 */
@Converter
public class FundCurFlagEnumConverter implements AttributeConverter<EFundCurFlag, String> {

	@Override
	public String convertToDatabaseColumn(EFundCurFlag attribute) {
		return attribute.getCode();
	}

	@Override
	public EFundCurFlag convertToEntityAttribute(String dbData) {
		return EnumHelper.translate(EFundCurFlag.class, dbData);
	}

}
