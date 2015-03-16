package com.hengxin.platform.report.enums;

import com.hengxin.platform.common.enums.PageEnum;

public enum EFundUserSearchType implements PageEnum {

    ALL("","全部"),
    RECHARGE("01", "充值"), 
    CASH("02", "提现"), 
    PAYFEE("03","缴费"),
    INTEREST("04","活期结息"),
    TRANSFERMM("06","内部转账"),
    TRANSFERPM("07","平台转账"),
    SUBSCRIBE("22","投资申购"),
    FINANCING("23","融资资金"),
    TRANSFERCR("27","债权转让"),
    SUBSCRIBEFEE("28","投资申购费用"),
    FNCR_REPAYMENT("40","融资人还款"),
    PRIN_REPAYMENT("41","本金还款"),
    PRIN_FINE_REPAYMENT("42","本金罚金还款"),
    INTR_REPAYMENT("43","利息还款"),
    INTR_FINE_REPAYMENT("44","利息罚金还款"),
    TRADEEXPENSE("45","交易费用"),
    TRADEEXPENSE_FINE("46","交易费用罚金"),
    CMPNSAMT_REPAYMENT("47","担保方代偿款还款"),
    CMPNSAMT_FINE_REPAYMENT("48","担保方代偿款罚金还款"),
    CMPNSAMT_PAY("64","担保方付代偿款"),
    REPAYAMTOFERROR2EXCH("66","还款误差金额"),
    PROFITINVS2EXCH("65","投资人交易手续费"),
    REVOKE_ORDER_FINE("29","撤单违约金"),    
    ;

    private String code;

    private String text;

    private EFundUserSearchType(String code, String text) {
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
