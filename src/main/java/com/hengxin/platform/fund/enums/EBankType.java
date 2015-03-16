package com.hengxin.platform.fund.enums;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.hengxin.platform.common.enums.PageEnum;
import com.hengxin.platform.common.util.PageEnumSerializer;

@JsonSerialize(using = PageEnumSerializer.class)
public enum EBankType implements PageEnum {

	CMB("CMB", "招商银行"), ICBC("ICBC", "工商银行"), HXB("HXB", "华夏银行"), BOC("BOC",
			"中国银行"), CCB("CCB", "建设银行"), COMMB("COMMB", "交通银行"), ABC("ABC",
			"农业银行"), PAB("PAB", "中国平安银行"), ECITIC("ECITIC", "中信银行"), CMBC(
			"CMBC", "民生银行"), XYB("XYB", "兴业银行"), RCC("RCC", "农村信用社"), SPDB(
			"SPDB", "上海浦东发展银行"), PSBC("PSBC", "中国邮政储蓄银行"), CEB("CEB", "光大银行"), FDB(
			"FDB", "富滇银行"), CGB("CGB", "广东发展银行"),

	OTHER("OTHER", "其他银行");

	private String code;

	private String text;

	private EBankType(String code, String text) {
		this.code = code;
		this.text = text;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public void setText(String text) {
		this.text = text;
	}

	public static EBankType translateFromBankTypeCode(String bankTypeCode) {
		EBankType bankType = null;
		int code = Integer.valueOf(bankTypeCode);
		switch (code) {
		case 102:
			bankType = EBankType.ICBC;
			break;
		case 103:
			bankType = EBankType.ABC;
			break;
		case 104:
			bankType = EBankType.BOC;
			break;
		case 105:
			bankType = EBankType.CCB;
			break;
		case 301:
			bankType = EBankType.COMMB;
			break;
		case 302:
			bankType = EBankType.ECITIC;
			break;
		case 303:
			bankType = EBankType.CEB;
			break;
		case 304:
			bankType = EBankType.HXB;
			break;
		case 305:
			bankType = EBankType.CMBC;
			break;
		case 306:
			bankType = EBankType.CGB;
			break;
		case 307:
			bankType = EBankType.PAB;
			break;
		case 308:
			bankType = EBankType.CMB;
			break;
		case 309:
			bankType = EBankType.XYB;
			break;
		case 310:
			bankType = EBankType.SPDB;
			break;
		case 402:
			bankType = EBankType.RCC;
			break;
		case 403:
			bankType = EBankType.PSBC;
			break;
		case 313:
		case 314:
		case 315:
		case 316:
		case 317:
		case 318:
		case 319:
		case 322:
		case 591:
		case 593:
		case 595:
		case 596:
		case 597:
			bankType = EBankType.OTHER;
			break;
		default:
			break;
		}
		return bankType;
	}

}
