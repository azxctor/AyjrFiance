/**
 *
 */
package com.hengxin.platform.common.enums;

import static com.hengxin.platform.common.constant.UrlConstant.*;

/**
 * @author yingchangwang
 *
 */
public enum EMenuConstant{
    
    //会员管理
	MEMBER_MANAGEMENT("2000", "会员管理", MEMBER_MANAGEMENT_URL, "fa-users",null),
	MEMBER_MANAGEMENT_INVEST_FIN_APPLY_APPROVE(	"2001", "会员信息审核", MEMBER_MANAGEMENT_INVEST_FIN_APPLY_APPROVE_URL, "icon-huiyuanxxsh",MEMBER_MANAGEMENT_URL),
	MEMBER_MANAGEMENT_SERVICE_CENTER_APPLY_APPROVE("2002", "会员信息审核", MEMBER_MANAGEMENT_SERVICE_CENTER_APPLY_APPROVE_URL, "icon-huiyuanxxsh",MEMBER_MANAGEMENT_URL),
	MEMBER_MANAGEMENT_PRODUCT_SERVICE_APPLY_APPROVE("2003", "会员信息审核", MEMBER_MANAGEMENT_PRODUCT_SERVICE_APPLY_APPROVE_URL, "icon-huiyuanxxsh",MEMBER_MANAGEMENT_URL),
	MEMBER_MANAGER_INVEST_FIN_INFO_MAINT("2004", "会员信息维护", MEMBER_MANAGER_INVEST_FIN_INFO_MAINT_URL, "icon-vcard",MEMBER_MANAGEMENT_URL),
	MEMBER_MANAGER_AGENT_INVEST_FIN_INFO_MAINT("2005", "会员信息维护", MEMBER_MANAGER_AGENT_INVEST_FIN_INFO_MAINT_URL, "icon-vcard",MEMBER_MANAGEMENT_URL),
	MEMBER_MANAGER_AGENCY_INFO_MAINT("2006", "会员信息维护", MEMBER_MANAGER_AGENCY_INFO_MAINT_URL, "icon-vcard",MEMBER_MANAGEMENT_URL),
	MEMBER_MANAGER_PASSWORD_RESET_MAINT("2007", "会员密码重置", MEMBER_MANAGEMENT_PASSWORD_RESET_URL, "fa-cog",MEMBER_MANAGEMENT_URL),
	MEMBER_MANAGER_REGISTER_AGENT("2008", "会员代注册", MEMBER_MANAGEMENT_REGISTER_FOR_USER_URL, "fa-cog",MEMBER_MANAGEMENT_URL),
	/** version 20150307  新增游客信息查询导出功能 ,添加菜单 **/
	MEMBER_MANAGER_INFO_SEARCH("2009", "游客信息查询", MEMBER_MANAGER_INFO_SEARCH_URL, "fa-cog",MEMBER_MANAGEMENT_URL),

	//融资管理
	FINANCING_MANAGEMENT("9000", "融资管理", FINANCING_MANAGEMENT_URL, "fa-users",null),
    PRODUCT_PROD_SERV_PROJECT_COMMIT("9001","融资项目管理",PRODUCT_PROD_SERV_PROJECT_COMMIT_URL,"fa-list-ul",FINANCING_MANAGEMENT_URL),//会员管理-融资项目管理
    FINANCING_PACKAGE_MANAGEMENT_WARR("9002","融资包管理",FINANCING_PACKAGE_MANAGEMENT_WARR_URL,"fa-th",FINANCING_MANAGEMENT_URL),//会员管理-融资包管理

    //行情展示   
    //FINANCING_MARKETING("3000","交易行情",FINANCING_MARKETING_URL,"fa-bar-chart-o",null),
    FINANCING_MARKETING("3000","我要投资",FINANCING_MARKETING_URL,"fa-bar-chart-o",null),
    FINANCING_MARKETING_VIEW("3001","债权申购",FINANCING_MARKETING_VIEW_URL,"icon-stats2",FINANCING_MARKETING_URL),
    FINANCING_MARKETING_TRANSFER_VIEW("3002","债权转让",FINANCING_MARKETING_TRANSFER_VIEW_URL,"icon-zhaiquanzrhq",FINANCING_MARKETING_URL),
    FINANCING_MARKETING_HISTORY_VIEW("3003","历史项目",FINANCING_MARKETING_HISTORY_VIEW_URL,"fa-history",FINANCING_MARKETING_URL),

    //我的债务
    MY_DEBT("4000","我的债务",MY_DEBT_URL,"fa-sliders",null),
    PRODUCT_FINANCIER_PROJECT_CHECK("4001","融资项目管理",PRODUCT_FINANCIER_PROJECT_CHECK_URL,"fa-list-ul",MY_DEBT_URL),
    FINANCING_PACKAGE_MANAGEMENT_FINANCIER("4002","融资包管理",FINANCING_PACKAGE_MANAGEMENT_FINANCIER_URL,"fa-th",MY_DEBT_URL),
    MY_PAYMENT_TABLE_VIEW("4003","我的还款计划表",MY_PAYMENT_TABLE_VIEW_URL,"icon-drawer",MY_DEBT_URL),
    QUERY_STATISTICS_FINANCING_PACKAGE_PAYMENT_QUERY("4004","我的还款计划表",QUERY_STATISTICS_FINANCING_PACKAGE_PAYMENT_QUERY_URL,"icon-drawer",MY_DEBT_URL),
    
    //风险管理
    RISK_MANAGEMENT("5000","风险管理",RISK_MANAGEMENT_URL,"fa-shield",null),
    PRODUCT_RISK_CONTROL_PROJECT_APPROVE("5001","项目审核",PRODUCT_RISK_CONTROL_PROJECT_APPROVE_URL,"fa-gavel",RISK_MANAGEMENT_URL),
    PRODUCT_RISK_CONTROL_PROJECT_EVALUATE("5002","风险评级",PRODUCT_RISK_CONTROL_PROJECT_EVALUATE_URL,"fa-star-half-o",RISK_MANAGEMENT_URL),
    PRODUCT_RISK_CONTROL_PROJECT_FREEZE("5003","保证金冻结",PRODUCT_RISK_CONTROL_PROJECT_FREEZE_URL,"icon-stop",RISK_MANAGEMENT_URL),
    FINANCING_PACKAGE_LOAN_APPROVE("5004","放款审批",FINANCING_PACKAGE_LOAN_APPROVE_URL,"icon-coin",RISK_MANAGEMENT_URL),
    FINANCING_PACKAGE_LOAN_APPROVE_CONFIRM("5005","放款审批复核",FINANCING_PACKAGE_LOAN_APPROVE_CONFIRM_URL,"icon-task",RISK_MANAGEMENT_URL),
    RISK_MANAGEMENT_COMPENSATORY_HANDLING("5007","代偿处理",RISK_MANAGEMENT_COMPENSATORY_HANDLING_URL,"icon-daichangchuli",RISK_MANAGEMENT_URL),
    
	
    //交易管理
    TRANS_MANAGEMENT("6000","交易管理",TRANS_MANAGEMENT_URL,"icon-jiaoyigl",null),
    PRODUCT_TRANS_DEPT_PROJECT_PUBLISH("6001","产品发布",PRODUCT_TRANS_DEPT_PROJECT_PUBLISH_URL,"icon-th-small",TRANS_MANAGEMENT_URL),
    FINANCING_PACKAGE_STOP("6002","终止申购",FINANCING_PACKAGE_STOP_URL,"icon-cancel",TRANS_MANAGEMENT_URL),
    FINANCING_PACKAGE_FILTER("6005","自动申购筛选",FINANCING_MARKETING_AUTO_SUBSCRIBE_HOME_URL,"icon-database",TRANS_MANAGEMENT_URL),
    PARAM_MANAGEMENT_SUBSCRIBER("6006", "定投组维护", PARAM_MANAGEMENT_SUBSCRIBER_URL, "icon-popup", TRANS_MANAGEMENT_URL),
    FINANCING_PACKAGE_OPEN_CLOSE("6007","开市闭市",FINANCING_PACKAGE_OPEN_CLOSE_URL,"icon-stats",TRANS_MANAGEMENT_URL),
    FINANCING_PACKAGE_CMB_SIGN("6008","招行签到",FINANCING_PACKAGE_CMB_SIGN_URL,"fa-book",TRANS_MANAGEMENT_URL),
//    FINANCING_PACKAGE_CANDIDATES("6009","投资人设置",FINANCING_MARKETING_AUTO_SUBSCRIBE_PRINCIPALS_URL,"icon-database",TRANS_MANAGEMENT_URL),
    /** version 20150317 合同维护界面化 **/
    TRANS_MANAGEMENT_CONTRACT_RATE("6010","合同维护",CONTRACT_RATE_MAINTAIN_URL,"fa-book",TRANS_MANAGEMENT_URL),
    /** version 20150317 合同维护界面化 **/
    TRANS_MANAGEMENT_AGENCY_FEE_CONTRACT_MANAGE("6011","代理费合同维护",TRANS_MANAGEMENT_AGENCY_FEE_CONSTRACT_MANAGE_URL,"fa-cog",TRANS_MANAGEMENT_URL),
    
    //结算管理
    SETTLEMENT_MANAGEMENT("7000","结算管理",SETTLEMENT_MANAGEMENT_URL,"icon-calculator",null),
    FINANCING_PACKAGE_MANUAL_PAY("7001","手工还款",FINANCING_PACKAGE_MANUAL_PAY_URL,"fa-credit-card",SETTLEMENT_MANAGEMENT_URL),
    MY_ACCOUNT_UNSIGNED_RECHARGE_VIEW("7002","非签约会员充值",MY_ACCOUNT_UNSIGNED_RECHARGE_URL,"fa-shield",SETTLEMENT_MANAGEMENT_URL),
    MY_ACCOUNT_WITHDRAW_APPLY_CONFIRM_VIEW("7003","非签约会员提现确认",MY_ACCOUNT_WITHDRAW_APPLY_CONFIRM_URL,"fa-usd",SETTLEMENT_MANAGEMENT_URL),
    MY_ACCOUNT_TRANSFER_MANUAL_PM_VIEW("7004","平台转账",MY_ACCOUNT_TRANSFER_PM_URL,"icon-tab",SETTLEMENT_MANAGEMENT_URL),
    MY_ACCOUNT_TRANSFER_MANUAL_MM_VIEW("7005","内部转账",MY_ACCOUNT_TRANSFER_MM_URL,"icon-loop2",SETTLEMENT_MANAGEMENT_URL),
    SETTLE_MANAGEMENT_DEFAULT_HANDLING("7006","违约处理",SETTLE_MANAGEMENT_DEFAULT_HANDLING_URL,"icon-weiyuecl",SETTLEMENT_MANAGEMENT_URL),
    PLATFORM_ACCOUNT_OVERVIEW("7007","平台账户总览",PLATFORM_ACCOUNT_OVERVIEW_URL,"icon-newspaper",SETTLEMENT_MANAGEMENT_URL),
    PLATFORM_ACCOUNT_OVERVIEW_FINANCE("70018","平台账户总览",PLATFORM_ACCOUNT_OVERVIEW_URL_FINANCE,"icon-newspaper",SETTLEMENT_MANAGEMENT_URL),
    SETTLEMENT_PLATFORM_ACCOUNT_DETAILS("7008","平台账户明细",SETTLEMENT_PLATFORM_ACCOUNT_DETAILS_URL,"icon-text",SETTLEMENT_MANAGEMENT_URL),
    FINANCING_PACKAGE_PRE_PAYMENT("7009","提前还款",FINANCING_PACKAGE_PRE_PAYMENT_URL,"icon-box-add",SETTLEMENT_MANAGEMENT_URL),
    MY_ACCOUNT_WITHDRAW_APPLY_APPROVE_VIEW("7010","非签约会员提现审批",MY_ACCOUNT_WITHDRAW_APPLY_APPROVE_URL,"icon-hammer",SETTLEMENT_MANAGEMENT_URL),
    MY_ACCOUNT_UNSIGNED_RECHARGE_VIEW_APPROVE("7011","非签约会员充值审批",MY_ACCOUNT_UNSIGNED_RECHARGE_APPROVE_URL,"icon-inbox",SETTLEMENT_MANAGEMENT_URL),
    SETTLEMENT_MANAGEMENT_REVERSE_APPROVE("7012","出入金冲正复核",SETTLEMENT_REVERSE_LIST_URL,"icon-target",SETTLEMENT_MANAGEMENT_URL),
    SETTLEMENT_MANAGEMENT_REVERSE("7013","出入金冲正申请",SETTLEMENT_REVERSE_APPL_LIST_URL,"icon-upload",SETTLEMENT_MANAGEMENT_URL),
    MY_ACCOUNT_TRANSFER_APPROVE_PM_VIEW("7014","平台转账审核",MY_ACCOUNT_TRANSFER_APPROVE_PM_URL,"icon-upload2",SETTLEMENT_MANAGEMENT_URL),
    MY_ACCOUNT_TRANSFER_APPROVE_MM_VIEW("7015","内部转账审核",MY_ACCOUNT_TRANSFER_APPROVE_MM_URL,"icon-in-alt",SETTLEMENT_MANAGEMENT_URL),
    MY_ACCOUNT_FUNDPOOL_DETAIL_VIEW("7016","资金汇总明细",MY_ACCOUNT_FUNDPOOL_DETAIL_URL,"icon-brightness",SETTLEMENT_MANAGEMENT_URL),
    SETTLEMENT_MANAGEMENT_FREEZE_RESERVE_DETAILS("7017","冻结保留明细",SETTLEMENT_FREEZE_RESERVE_DETAILS_URL,"icon-circle-half",SETTLEMENT_MANAGEMENT_URL),
    POOL_CHECK("7018","资金对账",POOL_CHECK_URL,"icon-circle-half",SETTLEMENT_MANAGEMENT_URL),
    BATCH_TASK_REPAYMENT("7019","批量还款",SYSTEM_MANAGEMENT_BATCH_JOB_TASK_REPAYMENT_LIST_URL,"icon-circle-half",SETTLEMENT_MANAGEMENT_URL),
    
    //我的账户 
    MY_ACCOUNT("8000","我的账户",MY_ACCOUNT_URL,"fa-user",null),
    MY_ACCOUNT_OVEWVIEW("1001","账户总览",MY_ACCOUNT_OVEWVIEW_URL,"icon-newspaper",MY_ACCOUNT_URL),
    MY_ACCOUNT_MY_CREDITOR("1002","我的债权",MY_ACCOUNT_MY_CREDITOR_URL,"icon-wodezhaiquan",MY_ACCOUNT_URL),
    MY_ACCOUNT_ACCOUNT_DETAILS("1003","账户明细",MY_ACCOUNT_ACCOUNT_DETAILS_URL,"fa-sitemap",MY_ACCOUNT_URL),
    MY_ACCOUNT_XWB("1004","小微宝",MY_ACCOUNT_XWB_URL,"icon-xiaoweibao",MY_ACCOUNT_URL),
    MY_ACCOUNT_TRANS_DETAILS("1005","交易明细",MY_ACCOUNT_TRANS_DETAILS_URL,"icon-jiaoyimingxi",MY_ACCOUNT_URL),
    MY_ACCOUNT_FREEZE_RESERVE_DETAILS("1006","冻结保留明细",MY_ACCOUNT_FREEZE_RESERVE_DETAILS_URL,"icon-circle-half",MY_ACCOUNT_URL),
    MY_ACCOUNT_ESW_RECHARGE_LIST("1007","充值记录查询",ESCROW_RECHARGE_LIST_URL,"icon-circle-half",MY_ACCOUNT_URL),
    MY_ACCOUNT_ESW_WITHDRAWAL_LIST("1008","提现记录查询",ESCROW_WITHDRAWAL_LIST_URL,"icon-circle-half",MY_ACCOUNT_URL),
    
    
	//系统管理
	SYSTEM_MANAGEMENT("10000","系统管理",SYSTEM_MANAGEMENT_URL,"fa-gear",null),
	SYSTEM_MANAGEMENT_ABNORMAL_WITHDRAW("10001","融资包异常撤单",SYSTEM_MANAGEMENT_ABNORMAL_WITHDRAW_URL,"icon-cc-nc",SYSTEM_MANAGEMENT_URL),
	SYSTEM_MANAGEMENT_TRANSFER_RULE_MAINTAIN("10002","转让规则维护",SYSTEM_MANAGEMENT_TRANSFER_RULE_MAINTAIN_URL,"icon-loop",SYSTEM_MANAGEMENT_URL),
	//SYSTEM_MANAGEMENT_JOB_WORK_LIST_MGT("10003","日终管理",SYSTEM_MANAGEMENT_BATCH_JOB_WORK_LIST_URL,"fa-files-o",SYSTEM_MANAGEMENT_URL),
	SYSTEM_MANAGEMENT_MEMBER_ACCOUNT_FREEZE("10004","会员账户冻结",SYSTEM_MANAGEMENT_MEMBER_ACCOUNT_FREEZE_URL,"icon-hyzhdongjie",SYSTEM_MANAGEMENT_URL),
	SYSTEM_MANAGEMENT_CACHE_MANAGEMENT("10005","缓存管理",SYSTEM_MANAGEMENT_CACHE_MANAGEMENT_URL,"fa-cog",SYSTEM_MANAGEMENT_URL),
	/** version 20150317  提供页面，展示与银盈通所有交互的日志 ，添加菜单**/
	SYSTEM_MANAGEMENT_ESWMSGLOG_SEARCH("10006","托管交易日志",SYSTEM_MANAGEMENT_ESWMSGLOG_SEARCH_URL,"icon-zoomin",SYSTEM_MANAGEMENT_URL),

	
	
	//资金清算 -14000
	FUND_CLEARING("14000", "资金清算", FUND_CLEARING_URL, "icon-equalizer",null),
	FUND_ACCT_NETTING_MGT("15001","资金清算",FUND_ACCT_NETTING_MGT_URL,"fa-cog",FUND_CLEARING_URL),
	ESCROW_ACCT_TRANSFER_MGT("15002","资金划拨",ESCROW_ACCT_TRANSFER_MGT_URL,"fa-cog",FUND_CLEARING_URL),
	ESCROW_ACCT_BALANCE_QUERY("15003","核对账户余额",ESCROW_ACCT_BALANCE_MGT_URL,"fa-cog",FUND_CLEARING_URL),
	
	//日终管理  -15000
	DAY_END_MANAGEMENT("15000", "日终管理", DAYEND_MANAGEMENT_URL,"fa-gear",null),
	DAY_END_MANAGEMENT_BATCH_BIZTASK_LIST_URL("15001","日终批量",DAYEND_MANAGEMENT_BATCH_BIZTASK_LIST_URL,"fa-cog",DAYEND_MANAGEMENT_URL),
	DAY_END_MANAGEMENT_BATCH_ROLLDATE_LIST("15002","系统日切",DAYEND_MANAGEMENT_BATCH_ROLLDATE_LIST_URL,"fa-cog",DAYEND_MANAGEMENT_URL),
	
	
	//银行管理
	BANK_MANAGEMENT("11000","银行管理",BANK_MANAGEMENT_URL,"icon-equalizer",null),
	MY_ACCOUNT_TRANSFER_TRX_DETAIL_VIEW("11001","招行对账明细",MY_ACCOUNT_TRANSFER_TRX_DETAIL_URL,"icon-paragraph-left",SETTLEMENT_MANAGEMENT_URL),
	//统计查询
	QUERY_STATISTICS("12000","查询统计",QUERY_STATISTICS_URL,"fa-dashboard",null),
	PRODUCT_PLATFORM_PROJECT_SEARCH_TRANS("12001","融资项目查询",PRODUCT_PLATFORM_PROJECT_SEARCH_TRANS_URL,"fa-list-ul",QUERY_STATISTICS_URL),
	PRODUCT_PLATFORM_PROJECT_SEARCH_RISK("12002","融资项目查询",PRODUCT_PLATFORM_PROJECT_SEARCH_RISK_URL,"fa-list-ul",QUERY_STATISTICS_URL),
    FINANCING_PACKAGE_TRANS_DEPT_VIEW("12003","融资包查询",FINANCING_PACKAGE_TRANS_DEPT_VIEW_URL,"icon-menu",QUERY_STATISTICS_URL),
    FINANCING_PACKAGE_SETTLEMENT_DEPT_VIEW("12004","融资包查询",FINANCING_PACKAGE_SETTLEMENT_DEPT_VIEW_URL,"icon-menu",QUERY_STATISTICS_URL),
    FINANCING_PACKAGE_RISK_DEPT_VIEW("12005","融资包查询",FINANCING_PACKAGE_RISK_DEPT_VIEW_URL,"icon-menu",QUERY_STATISTICS_URL),
    FINANCING_PACKAGE_FINANCW_DEPT_VIEW("12030","融资包查询",FINANCING_PACKAGE_RISK_DEPT_VIEW_URL,"icon-menu",QUERY_STATISTICS_URL),
	//QUERY_STATISTICS_TRADE_FEE_QUERY("12006","交易手续费查询",QUERY_STATISTICS_TRADE_FEE_QUERY_URL,"icon-sharable",QUERY_STATISTICS_URL),
	//QUERY_STATISTICS_TRADE_FEE_QUERY_FOR_ATHD_CENTER("12007","交易手续费查询",QUERY_STATISTICS_TRADE_FEE_QUERY_FOR_ATHD_URL,"icon-sharable",QUERY_STATISTICS_URL),
	QUERY_STATISTICS_MEMBER_APPROVE_QUERY("12008","会员审核查询",QUERY_STATISTICS_MEMBER_APPROVE_QUERY_URL,"icon-hammer",QUERY_STATISTICS_URL),
	QUERY_STATISTICS_MEMBER_BASIC_INFO_QUERY("12009","会员基本信息查询",QUERY_STATISTICS_MEMBER_BASIC_INFO_QUERY_URL,"icon-profile",QUERY_STATISTICS_URL),
	QUERY_STATISTICS_MEMBER_TOPUP_WITHDRAW_QUERY("12010","会员充值/提现信息查询",QUERY_STATISTICS_MEMBER_TOPUP_WITHDRAW_QUERY_URL,"fa-exchange",QUERY_STATISTICS_URL),
	QUERY_STATISTICS_MEMBER_ACCOUNT_QUERY("12011","会员账户查询",QUERY_STATISTICS_MEMBER_ACCOUNT_QUERY_URL,"icon-users",QUERY_STATISTICS_URL),
	QUERY_STATISTICS_LOAN_CONTRACT_STATISTICS("12012","借款合同汇总",QUERY_STATISTICS_LOAN_CONTRACT_STATISTICS_URL,"icon-stack",QUERY_STATISTICS_URL),
	QUERY_STATISTICS_MEMBER_ASSET_QUERY("12013","会员资产查询",QUERY_STATISTICS_MEMBER_ASSET_QUERY_URL,"icon-database",QUERY_STATISTICS_URL),
	//QUERY_STATISTICS_INVESTOR_BIZ_TRACE("12014","投资会员业务跟踪",QUERY_STATISTICS_INVESTOR_BIZ_TRACE_URL,"icon-binoculars",QUERY_STATISTICS_URL),
	QUERY_STATISTICS_MEMBER_MONTH_REPORT("12015","投资会员月度理财报表",QUERY_STATISTICS_MEMBER_MONTH_REPORT_URL,"icon-calendar",QUERY_STATISTICS_URL),
	//QUERY_STATISTICS_ATHD_CNTL_TRADE_TRACE("12016","授权服务中心交易跟踪",QUERY_STATISTICS_ATHD_CNTL_TRADE_TRACE_URL,"icon-zoomin",QUERY_STATISTICS_URL),
	QUERY_STATISTICS_FINANCING_PACKAGE_STATISTICS("12017","融资包统计",QUERY_STATISTICS_FINANCING_PACKAGE_STATISTICS_URL,"fa-th-large",QUERY_STATISTICS_URL),
	//QUERY_STATISTICS_MEMBER_INVESTE_STATISTICS("12017","会员投资统计",QUERY_STATISTICS_MEMBER_INVESTE_STATISTICS_URL,"icon-map",QUERY_STATISTICS_URL),
	QUERY_STATISTICS_CREDITOR_TRANSFER_QUERY("12018","债权转让查询",QUERY_STATISTICS_CREDITOR_TRANSFER_QUERY_URL,"icon-zhaiquanzrcx",QUERY_STATISTICS_URL),
	QUERY_STATISTICS_CREDITOR_TRANSFER_FEE_QUERY("12019","债权转让费用查询",QUERY_STATISTICS_CREDITOR_TRANSFER_FEE_QUERY_URL,"icon-zhaiquanzrfycx",QUERY_STATISTICS_URL),
	//QUERY_STATISTICS_INFO_SERVICE_FEE_QUERY("12020","信息服务费查询",QUERY_STATISTICS_INFO_SERVICE_FEE_QUERY_URL,"icon-mail",QUERY_STATISTICS_URL),
	QUERY_STATISTICS_FINANCING_PACKAGE_PAYMENT_TABLE_QUERY("12021","融资包还款查询 ",QUERY_STATISTICS_FINANCING_PACKAGE_PAYMENT_TABLE_QUERY_URL,"fa-check-circle",QUERY_STATISTICS_URL),
//    MEMBER_WITHDRAW_APPLY_QUERY("12023","会员提现申请查询",MY_ACCOUNT_WITHDRAW_APPLY_VIEW_URL,"fa-book",QUERY_STATISTICS_URL),
    QUERY_STATISTICS_RECENT_PAYMENT_SCHEDULE("12024","近期还款计划",QUERY_STATISTICS_RECENT_PAYMENT_SCHEDULE_URL,"icon-browser",QUERY_STATISTICS_URL),
    QUERY_STATISTICS_FINANCE_FEE_QUERY("12025","融资费用测算",QUERY_STATISTICS_FINANCE_FEE_QUERY_URL,"icon-newspaper2",QUERY_STATISTICS_URL),
    QUERY_STATISTICS_QUERY_STATISTICS_FINANCE_FEE_DETAILS("12026","融资费用实收明细",QUERY_STATISTICS_QUERY_STATISTICS_FINANCE_FEE_DETAILS_URL,"icon-clipboard",QUERY_STATISTICS_URL),
    FINANCING_PACKAGE_CUST_DEPT_VIEW("12027","融资包查询",PACKAGE_LIST_FOR_CUSTOMER_SERVICE_DEPT_URL,"icon-menu",QUERY_STATISTICS_URL),
    REPORT_DAILY_OVERDUE_DETAILS_VIEW("12028","当日逾期明细",REPORT_DAILY_OVERDUE_DETAILS_VIEW_URL,"icon-spinner",QUERY_STATISTICS_URL),
    REPORT_DAILY_COMPENSATORY_DETAILS_VIEW("12029","当日代偿明细",REPORT_DAILY_COMPENSATORY_DETAILS_VIEW_URL,"icon-spinner2",QUERY_STATISTICS_URL),
    
    //INVSTR_TRANSACTION_JOURNAL_REPORT("12030","会员资金流水明细",ACCOUNT_TRANSACTION_JOURNAL,"fa-book",QUERY_STATISTICS_URL),
    //QUERY_AUTHZD_CTR_TRANSFER_FEE("12031","债权转让手续费",REPORT_AUTHZD_CTR_TRANSFER_FEE,"fa-book",QUERY_STATISTICS_URL),
	
    //调整新顺序
    QUERY_STATISTICS_TRADE_FEE_QUERY("12006","交易手续费查询",QUERY_STATISTICS_TRADE_FEE_QUERY_URL,"icon-sharable",QUERY_STATISTICS_URL),
    QUERY_STATISTICS_TRADE_FEE_QUERY_FOR_ATHD_CENTER("12007","交易手续费查询",QUERY_STATISTICS_TRADE_FEE_QUERY_FOR_ATHD_URL,"icon-sharable",QUERY_STATISTICS_URL),
	QUERY_STATISTICS_INFO_SERVICE_FEE_QUERY("12014","信息服务费查询",QUERY_STATISTICS_INFO_SERVICE_FEE_QUERY_URL,"icon-mail",QUERY_STATISTICS_URL),
	QUERY_AUTHZD_CTR_TRANSFER_FEE("12016","债权转让手续费",REPORT_AUTHZD_CTR_TRANSFER_FEE,"fa-book",QUERY_STATISTICS_URL),
	QUERY_STATISTICS_INVESTOR_BIZ_TRACE("12017","投资会员业务跟踪",QUERY_STATISTICS_INVESTOR_BIZ_TRACE_URL,"icon-binoculars",QUERY_STATISTICS_URL),
    QUERY_STATISTICS_MEMBER_INVESTE_STATISTICS("12020","会员投资统计",QUERY_STATISTICS_MEMBER_INVESTE_STATISTICS_URL,"icon-map",QUERY_STATISTICS_URL),
    QUERY_STATISTICS_ATHD_CNTL_TRADE_TRACE("12030","授权服务中心交易跟踪",QUERY_STATISTICS_ATHD_CNTL_TRADE_TRACE_URL,"icon-zoomin",QUERY_STATISTICS_URL),
	INVSTR_TRANSACTION_JOURNAL_REPORT("12031","会员资金流水明细",ACCOUNT_TRANSACTION_JOURNAL,"fa-book",QUERY_STATISTICS_URL),
	     
    //我的设置
    MEMBER_MY_SETTINGS("13000", "我的设置", MEMBER_MY_SETTINGS_URL, "fa-cogs",null),
    MEMBER_BASIC_INFO("13001", "基本信息", MEMBER_BASIC_INFO_URL, "fa-file-text",MEMBER_MY_SETTINGS_URL),
    MEMBER_PERM_APPLY_INVERSTER("13002", "投资权限", MEMBER_PERM_APPLY_INVERSTER_URL, "icon-tou",MEMBER_MY_SETTINGS_URL),
    MEMBER_PERM_APPLY_FINANCIER("13004", "融资权限", MEMBER_PERM_APPLY_FINANCIER_URL, "icon-rong",MEMBER_MY_SETTINGS_URL),
    MEMBER_PERM_APPLY_ATHD_CNTL("13006", "服务中心权限", MEMBER_PERM_APPLY_ATHD_CNTL_URL, "icon-home",MEMBER_MY_SETTINGS_URL),
    MEMBER_PERM_APPLY_PROD_SERV("13008", "担保机构权限",MEMBER_PERM_APPLY_PROD_SERV_URL, "icon-office",MEMBER_MY_SETTINGS_URL),
    MEMBER_PERM_PW("13009", "个人设置", MEMBER_MY_SETTINGS_CHANGE_PASSWORD_URL, "fa-cog",MEMBER_MY_SETTINGS_URL),
    MEMBER_PERM_AUTO_SUBSCRIBE("13010", "自动申购设置", MEMBER_PERM_AUTO_SUBSCRIBE_URL, "icon-zidongsgsz", MEMBER_MY_SETTINGS_URL),
    MY_MESSAGE_WAITING_TODO("13011","消息待办",MY_MESSAGE_WAITING_TODO_URL,"icon-alarm",MEMBER_MY_SETTINGS_URL),
    ESCROW_ACCT_INFO_MGT("13012","托管信息设置",ESCROW_ACCT_MGT_URL,"fa-credit-card",MEMBER_MY_SETTINGS_URL),
    
    //磁贴
    DASHBOARD_ACCOUNT_OVERVIEW("","账户总览",MY_ACCOUNT_OVEWVIEW_URL,"",""),
    DASHBOARD_MY_SETTINGS("","我的设置",MEMBER_MY_SETTINGS_CHANGE_PASSWORD_URL,"",""),
    DASHBOARD_FINANCING_PRODUCT_MANAGEMENRT("","融资项目管理",PRODUCT_FINANCIER_PROJECT_CHECK_URL,"",""),
    DASHBOARD_MY_PAYMENT_SHCEDULE_TABLE("","我的还款计划表",QUERY_STATISTICS_FINANCING_PACKAGE_PAYMENT_QUERY_URL,"",""),
    DASHBOARD_MY_CREDITORS("","我的债权",MY_ACCOUNT_MY_CREDITOR_URL,"",""),
    //DASHBOARD_CREDITOR_MARKETING("","交易行情",FINANCING_MARKETING_VIEW_URL,"",""),
    DASHBOARD_CREDITOR_MARKETING("","我要投资",FINANCING_MARKETING_VIEW_URL,"",""),
    DASHBOARD_FINANCING_PACKAGE_MANAGEMENT("","会员融资包管理",FINANCING_PACKAGE_MANAGEMENT_WARR_URL,"",""),
    DASHBOARD_FINANCING_PRODUCT_APPLY("","融资项目申请",PRODUCT_PROD_SERV_PROJECT_COMMIT_URL,"",""),
    DASHBOARD_MEMBER_INFO_MAINTAIN("","会员管理",MEMBER_MANAGER_AGENT_INVEST_FIN_INFO_MAINT_URL,"",""),
    DASHBOARD_MEMBER_BASIC_INFO("","基本信息",MEMBER_BASIC_INFO_URL,"",""),
    DASHBOARD_MEMBER_INVESTOR_APPLY("","投资权限",MEMBER_PERM_APPLY_INVERSTER_URL,"",""),
    DASHBOARD_MEMBER_FINANCIER_APPLY("","融资权限",MEMBER_PERM_APPLY_FINANCIER_URL,"",""),
    DASHBOARD_MEMBER_PROD_SERV_APPLY("","担保机构权限",MEMBER_PERM_APPLY_PROD_SERV_URL,"",""),
    DASHBOARD_MEMBER_ATHD_SERV_APPLY("","服务中心权限",MEMBER_PERM_APPLY_ATHD_CNTL_URL,"",""),
    DASHBOARD_QUERY_STATISTICS("","查询统计",QUERY_STATISTICS_INFO_SERVICE_FEE_QUERY_URL,"",""),
    DASHBOARD_ACCOUNT_OVER_MORE("","更多",MY_ACCOUNT_OVEWVIEW_URL,"",""),
    DASHBOARD_EMPTY("","","","",""),
    DASHBOARD_MARKETING_MORE("","更多",FINANCING_MARKETING_VIEW_URL,"","");

    private String code;
    private String menuName;
    private String menuUrl;
    private String menuIcon;
    private String parentUrl;

    EMenuConstant(String code, String menuName, String menuUrl, String menuIcon,String parentUrl) {
        this.code = code;
        this.menuName = menuName;
        this.menuUrl = menuUrl;
        this.menuIcon = menuIcon;
        this.parentUrl=parentUrl;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }

    public String getParentUrl() {
        return parentUrl;
    }

    public void setParentUrl(String parentUrl) {
        this.parentUrl = parentUrl;
    }
}
