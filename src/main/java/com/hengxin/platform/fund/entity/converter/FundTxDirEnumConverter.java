package com.hengxin.platform.fund.entity.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.fund.enums.EFundTxDir;

/**
 * Class Name: FundTxDirEnumConverter
 * <p>
 * Description: TODO
 * 
 * @author jishen
 * 
 */
@Converter
public class FundTxDirEnumConverter implements AttributeConverter<EFundTxDir, String> {

	@Override
	public String convertToDatabaseColumn(EFundTxDir attribute) {
		return attribute.getCode();
	}

	@Override
	public EFundTxDir convertToEntityAttribute(String dbData) {
		return EnumHelper.translate(EFundTxDir.class, dbData);
	}

}
