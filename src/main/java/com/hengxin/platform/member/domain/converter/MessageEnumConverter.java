package com.hengxin.platform.member.domain.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.member.enums.EMessageType;

@Converter
public class MessageEnumConverter implements AttributeConverter<EMessageType, String> {

	@Override
	public String convertToDatabaseColumn(EMessageType attribute) {
		return attribute.getCode();
	}

	@Override
	public EMessageType convertToEntityAttribute(String dbData) {
		return EnumHelper.translate(EMessageType.class, dbData);
	}

}
