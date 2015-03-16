package com.hengxin.platform.fund.entity.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.fund.enums.EFundPayRecvFlag;

/**
 * Class Name: FundRecvPayTypeEnumConverter
 * <p>
 * Description: TODO
 * 
 * @author jishen
 * 
 */
@Converter
public class FundPayRecvFlagEnumConverter implements AttributeConverter<EFundPayRecvFlag, String> {

	@Override
	public String convertToDatabaseColumn(EFundPayRecvFlag attribute) {
		return attribute.getCode();
	}

	@Override
	public EFundPayRecvFlag convertToEntityAttribute(String dbData) {
		return EnumHelper.translate(EFundPayRecvFlag.class, dbData);
	}

}
