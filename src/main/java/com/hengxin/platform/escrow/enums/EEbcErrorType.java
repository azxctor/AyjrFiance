package com.hengxin.platform.escrow.enums;

import com.hengxin.platform.common.enums.PageEnum;

public enum EEbcErrorType implements PageEnum {

	NULL("", ""),
	SUCCESS("00", "操作成功"),
	CARDNO_NOT_EXIST("01", "指定卡号不存在"),
	PAYP_ASSWORD_INVALID("02", "支付密码错误"), 
	CARDNO_ALREADY_BINDED("03", "卡号已被绑定"),
	DEFAULT_CASH_ACCT_CAN_NOT_BE_DELETED("04", "默认现金账号不能删除"),
	GET_MOBILE_VERIFY_CODE_FAILED("05", "获取验证码失败"),
	WALLET_CREATE_FAILED("06", "钱包创建失败"),
	CARD_STATUS_INACTIVE("07", "卡状态异常，非活动状态"),
	MOBILE_VERIFY_FAILED("08", "验证码出错"),
	MOBILE_VERIFY_CODE_UNEFFECTIVE("09", "验证码超时失效"),
	TRANSFER_AMOUNT_OVER_LIMIT("10", "转账金额超过限额"),
	CARDNO_NOT_REGISTERED("11", "卡不存在  、卡未注册"),
	VERIFY_CODE_NET_ERROR("12", "网络激活码错误"),
	PASSWORD_INVALID("13", "密码错误"),
	TRANSFER_FAILED("14", "转账失败"),
	WITHDRAWAL_FAILED("15", "提现失败"),
	ADD_CARDNO_BANK_VERIFY_FAILED("16", "添加银行卡银行验证失败"),
	CARD_TYPE_INVALID("17", "卡类型错误"),
	MOBILE_ALREDY_USED("18", "该手机号已被其它账号使用，请更换手机号"),
	VERIFY_FAILED("19", "校验失败"), QUERY_USER_NOT_EXIST("20", "查询用户不存在"),
	ORDED_ALREDY_PAID("21", "该订单您已支付过了，无需重复支付"),
	PAYMENT_FAILED("22", "支付失败"), RECHARGE_AMOUNT_LIMITED("23", "该卡充值限额"),
	RECHARGE_FAILED("24", "充值失败"), DEAL_FAILED("25", "交易失败"),
	ASSIGNED_WALLET_NOT_EXIST("26", "指定钱包不存在"),
	BALANCE_NOT_ENOUGH("27", "未找到发卡行"),
	BANK_DEPOSIT_NOT_FOUND("28", "未找到发卡行"),
	MERCH_BANED("29", "该商户被禁用，请联系管理员"),
	INFO_INCOMPLETED("30", "请求信息不全，请补齐"),
	ALREDY_RECHARGED("31", "充值过了，无需重复充值"),
	NAME_INCONSISTENT("32", "添加银行卡姓名与钱包姓名不一致"),
	CARD_NOT_AUTHENTICATED("33", "该卡未认证"),
	HANDLING_BY_BANK("34", "银行受理中"),
	RECONCILIATION_FALIED("35", "对账失败"),
	ORDER_NOT_PAID("36", "订单未付款"),
	CARD_INFO_QUERY_FAILED("37", "卡信息查询失败"),
	BALANCE_QUERY_FAILED("38", "查询余额失败"),
	CARDNO_DISMATCHED("39", "发卡行与卡号不匹配"),
	UNSUPPROT_DEAL_TYPE("40", "不支持的交易类型"),
	WALLET_ALREADY_EXIST("41", "钱包已存在"),
	EBC_ACCT_NOT_EXIST("42", "电子账户不存在"),
	MEDIUMNO_DISMATCHED("43", "用户ID与介质ID匹配无记录"),
	USERNAME_ALREADY_EXIST("44", "用户名已存在"),
	ORDER_ALREADY_EXIST("45", "电汇订单已存在，请勿重复提交"),
	UNDIFINED_SYSTEM_ERROR("99", "未知系统错误");

	private String code;

	private String text;

	EEbcErrorType(String code, String text) {
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

}
