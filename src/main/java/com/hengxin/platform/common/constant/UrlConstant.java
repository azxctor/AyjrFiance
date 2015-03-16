/*
 * Project Name: kmfex-platform
 * File Name: UrlConstant.java
 * Class Name: UrlConstant
 *
 *
 * Licensed under the Hengtiansoft
 *
 * http://www.hengtiansoft.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hengxin.platform.common.constant;

/**
 * Class Name: UrlConstant
 * 
 * @author yingchangwang
 * 
 */

public interface UrlConstant {
	// 公共页面
	String ERROR_BROWSER_COMPATABILITY = "error/browser_compatability";
	// 会员管理菜单Url
	String MEMBER_MANAGEMENT_URL = "#members_management";
	String MEMBER_MANAGEMENT_REGISTER_FOR_USER_URL = "members/registeragent";
	String MEMBER_MANAGEMENT_INVEST_FIN_APPLY_APPROVE_URL = "audit/members";
	String MEMBER_MANAGER_INVEST_FIN_INFO_MAINT_URL = "search/members";
	String MEMBER_MANAGER_AGENT_INVEST_FIN_INFO_MAINT_URL = "search/agent";
	String MEMBER_MANAGER_AGENCY_INFO_MAINT_URL = "search/agency";
	String MEMBER_MANAGEMENT_SERVICE_CENTER_APPLY_APPROVE_URL = "audit/servicecenterapps";
	String MEMBER_MANAGEMENT_PRODUCT_SERVICE_APPLY_APPROVE_URL = "audit/productprovider";
	String MEMBER_BASIC_INFO_URL = "members/basicinfo";
	String MEMBER_MANAGEMENT_PASSWORD_RESET_URL = "members/password/reset"; // 会员管理-会员密码重置
	String MEMBER_MANAGER_INFO_SEARCH_URL ="search/touristinfo";// 会员管理-游客信息查询

	String FINANCING_MANAGEMENT_URL = "#finance_management";
	String PRODUCT_PROD_SERV_PROJECT_COMMIT_URL = "product/financelist/commit";// 会员管理-融资项目管理
	String FINANCING_PACKAGE_MANAGEMENT_WARR_URL = "financingpackage/maintain";// 会员管理-融资包管理

	// 我的设置菜单url
	String MEMBER_MY_SETTINGS_URL = "#mysettings";
	String MEMBER_PERM_APPLY_INVERSTER_URL = "members/investorinfo";
	String MEMBER_PERM_APPLY_FINANCIER_URL = "members/financierinfo";
	String MEMBER_PERM_APPLY_ATHD_CNTL_URL = "agency/servicecentorinfo/view/";
	String MEMBER_PERM_APPLY_PROD_SERV_URL = "agency/productproviderinfo/view/";
	String MEMBER_MY_SETTINGS_CHANGE_PASSWORD_URL = "members/password/update";// Password
																				// &
																				// Nickname
																				// Update.
	String MY_MESSAGE_WAITING_TODO_URL = "getMessageView";
	String ESCROW_ACCT_MGT_URL = "esw/eswinfo/mgt";

	// 参数管理菜单url
	String PARAM_MANAGEMENT_URL = "#param";
	String PARAM_MANAGEMENT_SUBSCRIBER_URL = "subscriber/groupinfo";
	String MEMBER_PERM_AUTO_SUBSCRIBE_URL = "autosubscribe";

	// 我的债务菜单Url
	String MY_DEBT_URL = "#myDebt";
	String PRODUCT_FINANCIER_PROJECT_CHECK_URL = "product/financelist/check";
	String FINANCING_PACKAGE_MANAGEMENT_FINANCIER_URL = "financingpackage/check";
	String MY_PAYMENT_TABLE_VIEW_URL = "";

	// 交易管理菜单Url
	String TRANS_MANAGEMENT_URL = "#trans_management";
	String PRODUCT_TRANS_DEPT_PROJECT_PUBLISH_URL = "product/financelist/publish";
	String PRODUCT_PLATFORM_PROJECT_SEARCH_TRANS_URL = "product/financelist/transdept/search";
	String FINANCING_PACKAGE_TRANS_DEPT_VIEW_URL = "financingpackage/transdept/view";
	String FINANCING_PACKAGE_OPEN_CLOSE_URL = "market/openclose";
	String FINANCING_PACKAGE_CMB_SIGN_URL = "market/cmbtsign";
	String FINANCING_PACKAGE_MANUAL_PAY_URL = "financingpackage/manualpay";
	String FINANCING_PACKAGE_PRE_PAYMENT_URL = "financingpackage/prepayment";
	String FINANCING_PACKAGE_STOP_URL = "financingpackage/stop";
	String CONTRACT_RATE_MAINTAIN_URL = "contractrate/maintain";
	String TRANS_MANAGEMENT_AGENCY_FEE_CONSTRACT_MANAGE_URL = "member/authzdctr/agencyFee/constract/manage";// 代理费合同管理
	String TRANS_MANAGEMENT_AGENCY_FEE_CONSTRACT_MANAGE_AUTHZDCTR_LIST_URL = "member/authzdctr/agencyFee/constract/manage/authzdctr/list";// 代理费合同管理-服务中心列表
    String TRANS_MANAGEMENT_AGENCY_FEE_CONSTRACT_MANAGE_CONSTRACT_LIST_URL = "member/authzdctr/agencyFee/constract/manage/constract/list";// 代理费合同管理-服务中心列表
    String TRANS_MANAGEMENT_AGENCY_FEE_CONSTRACT_MANAGE_CONSTRACT_INFO_URL = "member/authzdctr/agencyFee/constract/manage/constract/info/{contractId}";// 代理费合同管理-服务中心列表
    String TRANS_MANAGEMENT_AGENCY_FEE_CONSTRACT_MANAGE_CONSTRACT_SAVE_URL = "member/authzdctr/agencyFee/constract/manage/constract/save";// 代理费合同管理-服务中心列表

	// 风险管理菜单Url
	String RISK_MANAGEMENT_URL = "#risk_management";
	String PRODUCT_RISK_CONTROL_PROJECT_APPROVE_URL = "product/financelist/approve";
	String PRODUCT_RISK_CONTROL_PROJECT_EVALUATE_URL = "product/financelist/evaluate";
	String PRODUCT_RISK_CONTROL_PROJECT_FREEZE_URL = "product/financelist/freeze";
	String PRODUCT_PLATFORM_PROJECT_SEARCH_RISK_URL = "product/financelist/riskdept/search";
	String FINANCING_PACKAGE_LOAN_APPROVE_URL = "financingpackage/approve";
	String FINANCING_PACKAGE_LOAN_APPROVE_CONFIRM_URL = "financingpackage/loanapprove/confirm";
	String FINANCING_PACKAGE_RISK_DEPT_VIEW_URL = "financingpackage/riskdept/view";
	String FINANCING_PACKAGE_FINANCE_DEPT_VIEW_URL = "financingpackage/financedept/view";
	String MY_ACCOUNT_WITHDRAW_APPLY_APPROVE_URL = "myaccount/withddepapplsapproveview";
	String MY_ACCOUNT_WITHDRAW_APPLY_EX_APPROVE_URL = "myaccount/withDspExView";
	String RISK_MANAGEMENT_COMPENSATORY_HANDLING_URL = "product/view/cleared";

	// 行情菜单Url
	String FINANCING_MARKETING_URL = "#financing_marketing";
	String FINANCING_MARKETING_VIEW_URL = "market/financing";
	String FINANCING_MARKETING_HISTORY_VIEW_URL = "market/financing/history";
	String FINANCING_MARKETING_TRANSFER_VIEW_URL = "market/transfer";
	String FINANCING_MARKETING_AUTO_SUBSCRIBE_HOME_URL = "market/autosubscribe";
	String FINANCING_MARKETING_AUTO_SUBSCRIBE_VIEW_URL = "market/autosubscribe/view";
	String FINANCING_MARKETING_AUTO_SUBSCRIBE_CANDIDATES_URL = "market/autosubscribe/candidates/{packageId}";
	String FINANCING_MARKETING_AUTO_SUBSCRIBE_CANDIDATES_LIST_URL = "market/autosubscribe/candidatesList";
	String FINANCING_MARKETING_AUTO_SUBSCRIBE_DRAW_URL = "market/autosubscribe/draw";
	String FINANCING_MARKETING_AUTO_SUBSCRIBE_DRAW_LIST_URL = "market/autosubscribe/drawList";
	String FINANCING_MARKETING_AUTO_SUBSCRIBE_DRAW_DONE_URL = "market/autosubscribe/drawdone/{packageId}";
	String FINANCING_MARKETING_AUTO_SUBSCRIBE_PUBILSH_URL = "market/autosubscribe/publish/{packageId}";
	String FINANCING_MARKETING_AUTO_SUBSCRIBE_PRINCIPALS_URL = "market/autosubscribe/principals/query";

	// 我的账户
	String MY_ACCOUNT_URL = "#my_account";
	String MY_ACCOUNT_MY_CREDITOR_URL = "financingpackage/mycreditors";
	String MY_ACCOUNT_MY_CREDITOR_SUM_URL = "financingpackage/mycreditors/sum";
	String MY_ACCOUNT_OVEWVIEW_URL = "myaccount/accountoverview";
	String MY_ACCOUNT_ACCOUNT_DETAILS_URL = "myaccount/accountdetails";
	String MY_ACCOUNT_XWB_URL = "myaccount/xwbdetailview";
	String MY_ACCOUNT_TRANS_DETAILS_URL = "myaccount/tradedetails";
	String MY_ACCOUNT_FREEZE_RESERVE_DETAILS_URL = "fund/getUserFundFreezeReverseDtlView";
	String ESCROW_RECHARGE_LIST_URL = "esw/recharge/list";
	String ESCROW_WITHDRAWAL_LIST_URL = "esw/withdrawal/list";

	// 结算管理
	String SETTLEMENT_MANAGEMENT_URL = "#settlement_management";
	String FINANCING_PACKAGE_SETTLEMENT_DEPT_VIEW_URL = "financingpackage/settledept/view";
	String MY_ACCOUNT_UNSIGNED_RECHARGE_URL = "myaccount/unsigned/acct/recharge/appl/view";
	String MY_ACCOUNT_UNSIGNED_RECHARGE_APPROVE_URL = "myaccount/unsigned/acct/recharge/appl/approve/view";
	String MY_ACCOUNT_WITHDRAW_APPLY_CONFIRM_URL = "myaccount/withddepapplsconfirmview";
	// String SETTLE_MANAGEMENT_DEFAULT_HANDLING_URL = "product/view/default";
	String SETTLE_MANAGEMENT_DEFAULT_HANDLING_URL = "payment/clear/pkg/schd/list";
	String MY_ACCOUNT_TRANSFER_PM_URL = "myaccount/platformtransfertomemberview";
	String MY_ACCOUNT_TRANSFER_MM_URL = "myaccount/membertransfertomemberview";
	String PLATFORM_ACCOUNT_OVERVIEW_URL = "myaccount/platformaccountoverview";
	String PLATFORM_ACCOUNT_OVERVIEW_URL_FINANCE = "myaccount/platformaccountoverviewforfinance";
	String SETTLEMENT_BATCH_PROC_URL = "settlement/batchprocview";
	String SETTLEMENT_REVERSE_LIST_URL = "settlement/fund/acct/reverse/list";
	String SETTLEMENT_REVERSE_APPL_LIST_URL = "settlement/fund/acct/reverse/appl/list";
	String SETTLEMENT_PLATFORM_ACCOUNT_DETAILS_URL = "myaccount/platformaccountdetails";
	String MY_ACCOUNT_TRANSFER_APPROVE_PM_URL = "myaccount/transferApplApprovePMview";
	String MY_ACCOUNT_TRANSFER_APPROVE_MM_URL = "myaccount/transferApplApproveMMview";
	String MY_ACCOUNT_FUNDPOOL_DETAIL_URL = "fund/cash/pool/detail/view";
	String PLATFORM_ACCOUNT_DETAIL_EX_URL = "myaccount/platformaccountexceldetails";
	String SETTLEMENT_FREEZE_RESERVE_DETAILS_URL = "fund/getPlatformFundFreezeReverseDtlView";
	String POOL_CHECK_URL = "fund/cash/pool/view";

	// 银行管理
	String BANK_MANAGEMENT_URL = "#bank_management";
	String MY_ACCOUNT_TRANSFER_TRX_DETAIL_URL = "fund/transfertrxdetailview";

	// 系统管理
	String SYSTEM_MANAGEMENT_URL = "#system_management";
	String SYSTEM_MANAGEMENT_ABNORMAL_WITHDRAW_URL = "financingpackage/withdraw";
	String SYSTEM_MANAGEMENT_TRANSFER_RULE_MAINTAIN_URL = "rulemaintain/initpage";
	String SYSTEM_MANAGEMENT_MEMBER_ACCOUNT_FREEZE_URL = "fund/user/acct/list/tobefrozen";
	String SYSTEM_MANAGEMENT_CACHE_MANAGEMENT_URL = "admin/cachemanagement";
	String SYSTEM_MANAGEMENT_BATCH_JOB_WORK_LIST_URL = "batch/jobworklist";
	String SYSTEM_MANAGEMENT_BATCH_JOB_TASK_REPAYMENT_LIST_URL = "batch/repayment/list";
	String SYSTEM_MANAGEMENT_ESWMSGLOG_SEARCH_URL = "esw/eswmsglog";

	// 资金清算
	String FUND_CLEARING_URL = "#fund_clearing";
	String FUND_ACCT_NETTING_MGT_URL = "netting/list/mgt";
	String ESCROW_ACCT_TRANSFER_MGT_URL = "esw/transfer/list/mgt";
	String ESCROW_ACCT_BALANCE_MGT_URL = "esw/acct/balance/query";

	// 日终管理
	String DAYEND_MANAGEMENT_URL = "#dayend_management";
	String DAYEND_MANAGEMENT_BATCH_BIZTASK_LIST_URL = "batch/bizTask/list";
	String DAYEND_MANAGEMENT_BATCH_ROLLDATE_LIST_URL = "batch/rollDate/list";

	// 统计查询
	String QUERY_STATISTICS_URL = "#query_statistics";
	String PACKAGE_LIST_FOR_CUSTOMER_SERVICE_DEPT_URL = "financingpackage/dept/view";
	String QUERY_STATISTICS_TRADE_FEE_QUERY_URL = "report/authorizationpage";
	String QUERY_STATISTICS_TRADE_FEE_QUERY_FOR_ATHD_URL = "report/authorizationselfpage";
	String QUERY_STATISTICS_MEMBER_APPROVE_QUERY_URL = "report/getmemberapproveInfo";
	String QUERY_STATISTICS_MEMBER_BASIC_INFO_QUERY_URL = "report/getmemberinfopage";
	String QUERY_STATISTICS_MEMBER_TOPUP_WITHDRAW_QUERY_URL = "report/getmemberchargewithdrawpage";
	String QUERY_STATISTICS_MEMBER_ACCOUNT_QUERY_URL = "report/getmemberaccountpage";
	String QUERY_STATISTICS_LOAN_CONTRACT_STATISTICS_URL = "report/getloancontractpage";
	String QUERY_STATISTICS_MEMBER_ASSET_QUERY_URL = "report/memberassetpage";
	String QUERY_STATISTICS_INVESTOR_BIZ_TRACE_URL = "report/memberbusinesstracepage";
	String QUERY_STATISTICS_MEMBER_MONTH_REPORT_URL = "report/investormonthreportpage";
	String QUERY_STATISTICS_ATHD_CNTL_TRADE_TRACE_URL = "report/authorizationtradetracepage";
	String QUERY_STATISTICS_CREDITOR_TRANSFER_QUERY_URL = "report/getcredittransferpage";
	String QUERY_STATISTICS_FINANCING_PACKAGE_STATISTICS_URL = "report/packagestatisticspage";
	String QUERY_STATISTICS_MEMBER_INVESTE_STATISTICS_URL = "report/memberstatisticspage";
	String QUERY_STATISTICS_CREDITOR_TRANSFER_FEE_QUERY_URL = "report/getcredittransferfeepage";
	String QUERY_STATISTICS_INFO_SERVICE_FEE_QUERY_URL = "report/servicefeepage";
	String QUERY_STATISTICS_FINANCING_PACKAGE_PAYMENT_TABLE_QUERY_URL = "report/packagerepaymentpage";
	String QUERY_STATISTICS_FINANCING_PACKAGE_PAYMENT_QUERY_URL = "report/myrepaymentschedule";
	// String MY_ACCOUNT_WITHDRAW_APPLY_VIEW_URL =
	// "myaccount/withddepapplsview";
	String QUERY_STATISTICS_RECENT_PAYMENT_SCHEDULE_URL = "report/recentrepaymentschedule";
	String QUERY_STATISTICS_FINANCE_FEE_QUERY_URL = "report/getfinancefeestatisticspage";
	String QUERY_STATISTICS_QUERY_STATISTICS_FINANCE_FEE_DETAILS_URL = "report/getfinancefeedetailpage";
	String QUERY_PAYMENT_DETAILS_STRING = "report/getrepaymentdetail";
	String REPORT_DAILY_OVERDUE_DETAILS_VIEW_URL = "dailyrisk/overduedetailpage";
	String REPORT_DAILY_COMPENSATORY_DETAILS_VIEW_URL = "dailyrisk/compensatorydetailpage";

	//监管部门
	String QUERY_STATISTICS_PLATFORM_QUARTER_OPER_DATA_LIST_URL = "report/quarterOperDataList";
	String QUERY_STATISTICS_PLATFORM_QUARTER_OPER_DATA_URL = "report/quarterOperData";
	String QUERY_STATISTICS_PLATFORM_RISK_OPER_DATA_URL = "report/riskOperData";
	
	// 财务部

	// 通用部门,进入待办消息
	String COMMON_DEPT = "getMessageView";

	// 资金流水明细
	String ACCOUNT_TRANSACTION_JOURNAL = "report/account/transaction/journal";
	String ACCOUNT_TRANSACTION_JOURNAL_SEARCH = "report/account/transaction/journal/search";
	String ACCOUNT_TRANSACTION_JOURNAL_EX_SEARCH = "report/account/transaction/journal/exsearch";
	String ACCOUNT_TRANSACTION_JOURNAL_SEARCH_SUM = "report/account/transaction/journal/search_sum";

	// 债权转让手续费报表
	String REPORT_AUTHZD_CTR_TRANSFER_FEE = "report/authzd/ctr/transfer/fee";
	String REPORT_AUTHZD_CTR_TRANSFER_FEE_SEARCH = "report/authzd/ctr/transfer/fee/search";
	String REPORT_AUTHZD_CTR_TRANSFER_FEE_SEARCH_TOTAL = "report/authzd/ctr/transfer/fee/search/total";
	String REPORT_AUTHZD_CTR_TRANSFER_FEE_DOWNLOAD = "report/authzd/ctr/transfer/fee/download";

	// 充值提交URL
	String RECHARGE_OPEN_IE_TO_BANK_URL = "ws/OpenIE/Browser/post/to/bank";
}
