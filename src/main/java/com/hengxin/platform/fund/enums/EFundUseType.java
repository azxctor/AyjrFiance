package com.hengxin.platform.fund.enums;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.hengxin.platform.common.enums.PageEnum;
import com.hengxin.platform.common.util.PageEnumSerializer;

/**
 * 资金用途
 * @author dcliu
 *
 */
@JsonSerialize(using = PageEnumSerializer.class)
public enum EFundUseType implements PageEnum {
	
	// 资金用途定义
	INTERNAL("00","内部子账户划转"),
	RECHARGE("01","充值"),
	CASH("02","提现"),
	PAYFEE("03","缴费(席位费)"),
	INTEREST("04","活期结息"),
	XWBINTEREST("05","小微宝结息"),
	TRANSFERMM("06","内部转账"),
	TRANSFERPM("07","平台转账"),
	RECHARGE_REVERSE("08","充值冲正"),
	CASH_REVERSE("09","提现冲正"),
	CHANGE_POOL("10","换托管服务资金调拨"),

	POSTDEPOSIT("21","融资服务合同保证金"),
	SUBSCRIBE("22","投资申购"),
	FINANCING("23","融资资金"),
	LOANDEPOSIT("25","借款合同履约保证金"),
	WARRANTDEPOSIT("26","担保公司还款保证金"),
	TRANSFERCR("27","债权转让金额"),
	SUBSCRIBEFEE("28","投资申购费用"),
	REVOKE_ORDER_FINE("29","撤单违约金"),
	
	FNCR_REPAYMENT("40","融资人还款"),
	PRIN_REPAYMENT("41","本金还款"),
	PRIN_FINE_REPAYMENT("42","本金罚金还款"),
	INTR_REPAYMENT("43","利息还款"),
	INTR_FINE_REPAYMENT("44","利息罚金还款"),
	TRADEEXPENSE("45","交易费用"),
	TRADEEXPENSE_FINE("46","交易费用罚金"),
	CMPNSAMT_REPAYMENT("47","担保方代偿款还款"),
	CMPNSAMT_FINE_REPAYMENT("48","担保方代偿款罚金还款"),
	PREPAY_FINE("49","违约还款罚金"),
	TRANSFERCR_FEE("50","债权转让交易手续费"),
	
	PRIN_CMPNS_OVERDUE_FINE("59","本金担保代偿逾期罚金"),
	INTR_CMPNS_OVERDUE_FINE("60","利息担保代偿逾期罚金"),
	
	FNCR_REPAYMENT_PENALTY("63","融资人还款违约"),
	CMPNSAMT_PAY("64","担保方付代偿款"),
	PROFITINVS2EXCH("65","投资人交易手续费"),
	REPAYAMTOFERROR2EXCH("66","还款误差金额"),
	BIZFREEZEFUNDACCT("67","业务类资金账户冻结"),
	MGTFREEZEFUNDACCT("68","管理类资金账户冻结"),
	ACCTASSETRESERVED("69","账户资产保留"),
	
	ACTIVITY_RETURN_AMT("81","活动返现"),
	
	INOUTALL("98","全部"),
	OTHER("99","其他用途"),
	;
    
	private String code;

    private String text;
	
	private EFundUseType(String code, String text){
		this.code = code;
		this.text = text;
	}
	
	public static List<EFundUseType> getInvstrUseTypes()  {
		List<EFundUseType> list = new ArrayList<EFundUseType>();
		list.add(INOUTALL);
		list.add(RECHARGE);
		list.add(CASH);
		list.add(INTEREST);
		list.add(TRANSFERMM);
		list.add(TRANSFERPM);
		list.add(SUBSCRIBE);
		list.add(SUBSCRIBEFEE);
		list.add(PRIN_REPAYMENT);
		list.add(PRIN_FINE_REPAYMENT);
		list.add(INTR_REPAYMENT);
		list.add(INTR_FINE_REPAYMENT);
		list.add(PREPAY_FINE);
		list.add(PROFITINVS2EXCH);
		list.add(PRIN_CMPNS_OVERDUE_FINE);
		list.add(INTR_CMPNS_OVERDUE_FINE);
		list.add(REVOKE_ORDER_FINE);
		return list;
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
