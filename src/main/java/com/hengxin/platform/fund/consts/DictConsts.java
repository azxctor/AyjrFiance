package com.hengxin.platform.fund.consts;

import java.util.Arrays;
import java.util.List;

import com.hengxin.platform.fund.enums.EFundDealStatus;
import com.hengxin.platform.fund.enums.EFundUseType;

/**
 * DictConsts.
 *
 */
public class DictConsts {

	/**
	 * 系统批处理时的操作用户.
	 */
    public static final String SYS_OP_USER_ID = "88888888";

    public static final String WORK_DATE_FORMAT = "yyyy-MM-dd";
    public static final String TRSF_DATE_FORMAT = "yyyyMMdd";
    public static final String SEARCH_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String START_DATE_TIME = "00:00:00";
    public static final String END_DATE_TIME = "23:59:59";

    /**
     * 冻结保留默认失效日期.
     */
    public static final String DEFAULT_MAX_DATE_VALUE = "9999-12-31";
    public static final String DEFAULT_MIN_DATE_VALUE = "1990-01-01";

    /**
     * 申请状态.
     */
    public static final String APPL_STATUS_DEALING = "01";                         // 审批中 
    public static final String APPL_STATUS_PASS = "02";                            // 通过
    public static final String APPL_STATUS_REJECT = "03";                          // 否决  
    public static final String APPL_STATUS_CANCEL = "04";                          // 作废

    /**
     * 融资包签约状态.
     */
    public static final String PROD_STATUS_SUCCEED = "S";							// 融资包签约成功
    public static final String PROD_STATUS_BREAK = "F";								// 融资包签约失败(违约)

    /**
     * 系统自动处理操作ID.
     */
    public static final String SYSTEM_OPID = "SYS";

    public static final String TYPE_CONFIRM = "01";									// 非签约会员	提现确认类型			
    public static final String TYPE_APPROVE = "02";									// 非签约会员	提现审批类型
    public static final String TYPE_VIEW = "03";									// 非签约会员	查看提现状态类型

    public static final String MEMBER_TO_MEMBER = "MM";								//资金划转类型-会员转会员
    public static final String PLATFORM_TO_MEMBER = "PM";							//资金划转类型-会员转会员

    public static final String ICBC_SHEETNAME = "工行";								//excel工作表名称
    public static final String CONTRACT_SHEETNAME = "合同";                              //excel工作表名称
    public static final String POOLTRXJNL_SHEETNAME = "资金池汇总明细";                              //excel工作表名称
    public final static String MY_ACCOUNT_DETAILS_SHEETNAME = "我的账户明细";                              //excel工作表名称
    
    /**
     * 出入金(全部).
     */
    public static final List<EFundUseType> IN_OUT_USETYPE = Arrays.asList(
//            EFundUseType.INOUTALL,
            EFundUseType.RECHARGE,
            EFundUseType.CASH,
            EFundUseType.PAYFEE,
//安益暂时不用            EFundUseType.INTEREST,
            EFundUseType.TRANSFERMM,
            EFundUseType.TRANSFERPM,
//            EFundUseType.POSTDEPOSIT,
            EFundUseType.SUBSCRIBE,
            EFundUseType.SUBSCRIBEFEE,
            EFundUseType.FINANCING,
//            EFundUseType.LOANDEPOSIT,
//            EFundUseType.WARRANTDEPOSIT,
            EFundUseType.TRANSFERCR,
            EFundUseType.TRANSFERCR_FEE,
            EFundUseType.FNCR_REPAYMENT,
            EFundUseType.PRIN_REPAYMENT,
            EFundUseType.PRIN_FINE_REPAYMENT,
            EFundUseType.INTR_REPAYMENT,
            EFundUseType.INTR_FINE_REPAYMENT,
            EFundUseType.TRADEEXPENSE,
			EFundUseType.REVOKE_ORDER_FINE,
            EFundUseType.TRADEEXPENSE_FINE,
            EFundUseType.CMPNSAMT_REPAYMENT,
            EFundUseType.CMPNSAMT_FINE_REPAYMENT,
            EFundUseType.CMPNSAMT_PAY,
            EFundUseType.PREPAY_FINE,
//            EFundUseType.XWBINTEREST,
            EFundUseType.PROFITINVS2EXCH,
            EFundUseType.FNCR_REPAYMENT_PENALTY,
//            EFundUseType.RECHARGE_REVERSE,
//            EFundUseType.CASH_REVERSE,
            EFundUseType.ACTIVITY_RETURN_AMT,
            EFundUseType.PRIN_CMPNS_OVERDUE_FINE,
            EFundUseType.INTR_CMPNS_OVERDUE_FINE);
    
    public static final List<EFundUseType> INVS_IN_OUT_USETYPE =  Arrays.asList(
//    		EFundUseType.INOUTALL,
            EFundUseType.RECHARGE,
            EFundUseType.CASH,
//安益暂时不用             EFundUseType.INTEREST,
            EFundUseType.TRANSFERMM,
            EFundUseType.TRANSFERPM,
            EFundUseType.SUBSCRIBE,
            EFundUseType.SUBSCRIBEFEE,
            EFundUseType.TRANSFERCR,
            EFundUseType.TRANSFERCR_FEE,
            EFundUseType.PRIN_REPAYMENT,
            EFundUseType.PRIN_FINE_REPAYMENT,
            EFundUseType.INTR_REPAYMENT,
            EFundUseType.INTR_FINE_REPAYMENT,
			EFundUseType.REVOKE_ORDER_FINE,
            EFundUseType.PREPAY_FINE,
            EFundUseType.PROFITINVS2EXCH,
            EFundUseType.ACTIVITY_RETURN_AMT,
            EFundUseType.PRIN_CMPNS_OVERDUE_FINE,
            EFundUseType.INTR_CMPNS_OVERDUE_FINE
    );
    
    public static final List<EFundUseType> FNCR_IN_OUT_USETYPE =  Arrays.asList(
//    		EFundUseType.INOUTALL,
            EFundUseType.RECHARGE,
            EFundUseType.CASH,
            EFundUseType.PAYFEE,
          //安益暂时不用            EFundUseType.INTEREST,
            EFundUseType.TRANSFERMM,
            EFundUseType.TRANSFERPM,
            EFundUseType.FINANCING,
            EFundUseType.TRADEEXPENSE,
			EFundUseType.REVOKE_ORDER_FINE,
            EFundUseType.FNCR_REPAYMENT,
            EFundUseType.CMPNSAMT_REPAYMENT,
            EFundUseType.CMPNSAMT_FINE_REPAYMENT,
            EFundUseType.CMPNSAMT_PAY,
            EFundUseType.ACTIVITY_RETURN_AMT,
            EFundUseType.PRIN_CMPNS_OVERDUE_FINE,
            EFundUseType.INTR_CMPNS_OVERDUE_FINE
    );
    
    public static final List<EFundUseType> WRTR_IN_OUT_USETYPE =  Arrays.asList(
//    		EFundUseType.INOUTALL,
            EFundUseType.RECHARGE,
            EFundUseType.CASH,
            EFundUseType.INTEREST,
            EFundUseType.TRANSFERMM,
            EFundUseType.TRANSFERPM,
            EFundUseType.ACTIVITY_RETURN_AMT,
            EFundUseType.CMPNSAMT_PAY
    );
    
    /**
     * 平台出入金(全部).
     */
    public static final List<EFundUseType> PLATFORM_IN_OUT_USETYPE = Arrays.asList(
//            EFundUseType.INOUTALL,
            EFundUseType.RECHARGE,
            EFundUseType.CASH,
            EFundUseType.PAYFEE,
          //安益暂时不用            EFundUseType.INTEREST,
            EFundUseType.TRANSFERMM,
            EFundUseType.TRANSFERPM,
//            EFundUseType.POSTDEPOSIT,
            EFundUseType.SUBSCRIBE,
            EFundUseType.FINANCING,
//            EFundUseType.LOANDEPOSIT,
//            EFundUseType.WARRANTDEPOSIT,
            EFundUseType.TRANSFERCR,
            EFundUseType.TRANSFERCR_FEE,
            EFundUseType.SUBSCRIBEFEE,
            EFundUseType.FNCR_REPAYMENT,
            EFundUseType.PRIN_REPAYMENT,
			EFundUseType.REVOKE_ORDER_FINE,
            EFundUseType.PRIN_FINE_REPAYMENT,
            EFundUseType.INTR_REPAYMENT,
            EFundUseType.INTR_FINE_REPAYMENT,
            EFundUseType.TRADEEXPENSE,
            EFundUseType.TRADEEXPENSE_FINE,
            EFundUseType.CMPNSAMT_REPAYMENT,
            EFundUseType.CMPNSAMT_FINE_REPAYMENT,
            EFundUseType.CMPNSAMT_PAY,
//            EFundUseType.XWBINTEREST,
            EFundUseType.ACTIVITY_RETURN_AMT,
            EFundUseType.REPAYAMTOFERROR2EXCH,
            EFundUseType.PROFITINVS2EXCH
//            EFundUseType.RECHARGE_REVERSE,
//            EFundUseType.CASH_REVERSE
            );

    public static final List<EFundDealStatus> CONFIRM_OPTION = Arrays.asList(
            EFundDealStatus.ALL,
            EFundDealStatus.DEALING,
            EFundDealStatus.SUCCED);

	private DictConsts() {
		super();
	}

}
