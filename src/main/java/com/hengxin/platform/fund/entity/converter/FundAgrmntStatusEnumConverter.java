package com.hengxin.platform.fund.entity.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.fund.enums.EAgrmntStatus;

/**
 * Class Name: FundTxTypeEnumConverter
 * <p>
 * Description: TODO
 * 
 * @author congzhou
 * 
 */
@Converter
public class FundAgrmntStatusEnumConverter implements AttributeConverter<EAgrmntStatus, String> {

	@Override
	public String convertToDatabaseColumn(EAgrmntStatus attribute) {
		return attribute.getCode();
	}

	@Override
	public EAgrmntStatus convertToEntityAttribute(String dbData) {
		return EnumHelper.translate(EAgrmntStatus.class, dbData);
	}
}
