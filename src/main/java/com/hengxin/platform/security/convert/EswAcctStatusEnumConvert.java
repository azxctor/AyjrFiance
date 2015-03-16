package com.hengxin.platform.security.convert;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.security.enums.EswAcctStatusEnum;

@Convert
public class EswAcctStatusEnumConvert implements AttributeConverter<EswAcctStatusEnum, String> {

	@Override
	public String convertToDatabaseColumn(EswAcctStatusEnum attribute) {
		return attribute.getCode();
	}

	@Override
	public EswAcctStatusEnum convertToEntityAttribute(String dbData) {
		return EnumHelper.translate(EswAcctStatusEnum.class, dbData);
	}

}
