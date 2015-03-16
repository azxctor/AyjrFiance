package com.hengxin.platform.member.enums.converter;

import javax.persistence.AttributeConverter;

import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.member.enums.EAgencyFeeContractType;

public class EAgencyFeeContractTypeConverter implements AttributeConverter<EAgencyFeeContractType, String> {

	@Override
	public String convertToDatabaseColumn(EAgencyFeeContractType attribute) {
		return attribute.getCode();
	}

	@Override
	public EAgencyFeeContractType convertToEntityAttribute(String dbData) {
		return EnumHelper.translate(EAgencyFeeContractType.class, dbData);
	}
}
