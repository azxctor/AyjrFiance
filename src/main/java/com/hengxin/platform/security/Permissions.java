/*
 * Project Name: kmfex-platform-trunk
 * File Name: Permissions.java
 * Class Name: Permissions
 *
 * Copyright 2014 Hengtian Software Inc
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

package com.hengxin.platform.security;

/**
 * Class Name: Permissions Description.
 * 
 * @author zhengqingye
 * 
 */

public class Permissions {

    // 我的设置
    /**
     * 基本信息-基本信息录入
     */
    public static final String MEMBER_BASIC_INFO_ADD = "member:basic_info:add";

    /**
     * 基本信息-基本信息view
     */
    public static final String MEMBER_BASIC_INFO_VIEW = "member:basic_info:view";

    /**
     * 基本信息-会员信息变更-会员自主修改
     */
    public static final String MEMBER_BASIC_INFO_UPDATE = "member:basic_info:update";

    /**
     * 投资权限申请-投资权限申请
     */
    public static final String MEMBER_PERM_APPLY_INVERSTER = "member:perm_apply:investor";

    /**
     * 投资权限申请-投资权限信息view
     */
    public static final String MEMBER_PERM_APPLY_INVERSTER_VIEW = "member:perm_apply:investor:view";

    /**
     * 投资权限-会员信息变更-会员自主修改
     */
    public static final String MEMBER_PROFILE_INVESTER_UPDATE = "member:prolile:investor:update";

    /**
     * 融资权限申请-融资权限申请
     */
    public static final String MEMBER_PERM_APPLY_FINANCIER = "member:perm_apply:financier";

    /**
     * 融资权限申请-融资权限信息view
     */
    public static final String MEMBER_PERM_APPLY_FINANCIER_VIEW = "member:perm_apply:financier:view";

    /**
     * 融资权限-会员信息变更-会员自主修改
     */
    public static final String MEMBER_PROFILE_FINANCIER_UPDATE = "member:prolile:financier:update";

    /**
     * 服务中心权限申请-服务中心权限申请
     */
    public static final String MEMBER_PERM_APPLY_ATHD_CNTL = "member:perm_apply:authenticated_service_center";

    /**
     * 服务中心权限申请-服务中心权限信息view
     */
    public static final String MEMBER_PERM_APPLY_ATHD_CNTL_VIEW = "member:perm_apply:authenticated_service_center:view";

    /**
     * 服务中心权限申请-服务中心权限信息-自主修改
     */
    public static final String MEMBER_PROFILE_ATHD_CNTL_UPDATE = "member:prolile:authenticated_service_center:update";

    /**
     * 担保机构权限申请-担保机构权限申请
     */
    public static final String MEMBER_PERM_APPLY_PROD_SERV = "member:perm_apply:prod_serv";

    /**
     * 担保机构权限申请-担保机构权限信息view
     */
    public static final String MEMBER_PERM_APPLY_PROD_SERV_VIEW = "member:perm_apply:prod_serv:view";

    /**
     * 担保机构权限申请-担保机构权限信息-自主修改
     */
    public static final String MEMBER_PROFILE_PROD_SERV_UPDATE = "member:prolile:prod_serv:update";
    /**
     * 会员信息审核-投融资会员
     */
    public static final String MEMBER_PROFILE_INVESTOR_FINANCIER_APPROVE = "member:prolile:investor_financier:approve";

    /**
     * 会员信息审核-担保机构
     */
    public static final String MEMBER_PROFILE_PROD_SERV_APPROVE = "member:prolile:prod_serv:approve";

    /**
     * 会员信息审核-服务中心
     */
    public static final String MEMBER_PROFILE_ATHD_CNTL_APPROVE = "member:prolile:authenticated_service_center:approve";

    /**
     * 会员信息维护-会员查询-投融资会员
     */
    public static final String MEMBER_ADV_MAINT_INVST_FIN_VIEW = "member:advanced_maint:invester_financier:view";

    /**
     * 会员信息维护-会员查询-担保机构、服务中心
     */
    public static final String MEMBER_ADV_MAINT_PROD_SERV_VIEW = "member:advanced_maint:prod_serv:view";

    /**
     * 会员信息维护-会员查询-授权服务中心
     */
    public static final String MEMBER_ADV_MAINT_ATHD_CNTR_VIEW = "member:advanced_maint:athd_cntr:view";

    /**
     * 会员信息维护-投融资会员信息变更-特殊字段
     */
    public static final String MEMBER_ADV_MAINT_INV_FIN_LEVEL_CHANGE = "member:advanced_maint:invester_financier:level:update";
    /**
     * 会员信息维护-会员信息变更-密码重置
     */
    public static final String MEMBER_ADV_MAINT_PASSWORD_RESET = "member:advanced_maint:password:reset";

    /**
     * 会员管理-担保机构会员信息变更-特殊字段
     */
    public static final String MEMBER_ADV_MAINT_PROD_SERV_UPDATE = "member:advanced_maint:prod_serv:update";

    /**
     * 会员管理-服务中心会员信息变更-特殊字段
     */
    public static final String MEMBER_ADV_MAINT_ATHD_CNTR_UPDATE = "member:advanced_maint:athd_cntr:update";

    /**
     * 会员信息维护-投融资会员信息变更-其他信息
     */
    public static final String MEMBER_ADV_MAINT_INVST_FIN_UPDATE = "member:advanced_maint:invester_financier:update";
    /**
     * 会员信息维护-投融资会员代注册
     */
    public static final String MEMBER_REGISTER_FOR_USER = "member:register:agent";

    /**
     * 债权行情-债权行情
     */
    public static final String MARKETING_CREDITOR_VIEW = "marketing:creditor:view";
    /**
     * 债权行情-平台查看行情
     */
    public static final String MARKETING_CREDITOR_ADVANCED_VIEW = "marketing:creditor:advanced:view";
    /**
     * 债权行情-债权申购
     */
    public static final String MARKETING_CREDITOR_PURCHASE = "marketing:creditor:purchase";
    /**
     * 债权行情-定投债权查看及申购
     */
    public static final String MARKETING_CREDITOR_AIP_PURCHASE_VIEW = "marketing:creditor:aip_purchase_view";

    /**
     * 债权转让行情-债权转让行情
     */
    public static final String MARKETING_CREDITOR_ASSIGNMENT_VIEW = "marketing:creditor_assignment:view";
    /**
     * 债权转让行情-债权转让买入
     */
    public static final String MARKETING_CREDITOR_ASSIGNMENT_PURCHASE = "marketing:creditor_assignment:purchase";
    /**
     * 会员管理-会员融资项目管理-融资项目申请-担保机构
     */
    public static final String PRODUCT_FINANCING_PROJECT_APPLY = "product:financing_project:apply";
    /**
     * 我的债务-融资项目管理-融资项目申请-融资人
     */
    public static final String PRODUCT_FINANCING_PROJECT_APPLY_FOR_FINANCIER = "product:financing_project:financier_apply";
    /**
     * 会员管理-会员融资项目管理-融资项目修改-担保机构
     */
    public static final String PRODUCT_FINANCING_PROJECT_PROFILE_UPDATE = "product:financing_project:profile:update";
    /**
     * 会员管理-会员融资项目管理-融资项目撤单-担保机构
     */
    public static final String PRODUCT_FINANCING_PROJECT_CANCEL = "product:financing_project:cancel";

    /**
     * 会员管理-会员融资项目管理-会员融资项目所有状态查询-针对担保机构
     */
    public static final String MEMBER_FINANCING_APPLY_VIEW = "member:financing_apply:view";

    /**
     * 我的债务-融资项目管理-融资人融资项目所有状态查询-针对融资人自己
     */
    public static final String PRODUCT_FINANCIER_FINANCING_STATUS_VIEW = "product:financier:financing_status:view";

    /**
     * 交易管理/风险管理-融资项目查询-融资项目查询-针对小微平台人员
     */
    public static final String PRODUCT_FINANCING_VIEW = "product:financing:view";
    /**
     * 风险管理-项目审核-融资项目平台审核
     */
    public static final String PRODUCT_FINANCING_PLATFORM_APPROVE = "product:financing:platform_approve";
    /**
     * 风险管理-风险评级-融资项目平台评级
     */
    public static final String PRODUCT_FINANCING_PLATFORM_RATING = "product:financing:platform_rating";
    /**
     * 风险管理-保证金冻结-融资项目平台冻结
     */
    public static final String PRODUCT_FINANCING_DEPOSIT_FREEZE = "product:financing:deposit_freeze";
    /**
     * 风险管理-保证金冻结-融资项目平台冻结修改
     */
    public static final String PRODUCT_FINANCING_DEPOSIT_UPDATE = "product:financing:deposit:update";
    /**
     * 风险管理-放款审批-融资包放款审批
     */
    public static final String PRODUCT_FINANCING_LOAN_APPROVE = "product:financing:loan_approve";
    /**
     * 风险管理-放款审批-融资包放款审批确认
     */
    public static final String PRODUCT_FINANCING_LOAN_APPROVE_CONFIRM = "product:financing:loan_approve_confirm";

    /**
     * 风险管理-提现审批-非签约会员提现审批
     */
    public static final String PRODUCT_FINANCING_WITHDRAW_APPROVE = "product:financing:withdraw_approve";
    /**
    /**
     * 风险管理-提现审批-非签约会员提现审批导出
     */
    public static final String PRODUCT_FINANCING_WITHDRAW_APPROVE_EX = "product:financing:withdraw_ex_approve";
    /**
     * 风险管理-提现审批-非签约会员提现审批打印
     */
    public static final String PRODUCT_FINANCING_WITHDRAW_APPROVE_PRINT = "product:financing:withdraw_print_approve";
    /**
     * 风险管理-提现审批-非签约会员提现确认打印
     */
    public static final String PRODUCT_FINANCING_WITHDRAW_APPROVE_PRINT2 = "product:financing:withdraw_print2_approve";
    /**
     * 风险管理-代偿处理-代偿处理（冻结/解冻）
     */
    public static final String RISK_MANAGEMENT_COMPENSATORY_HANDLING = "product:financing_package:compensatory:handling";
    /**
     * 交易管理-产品发布-交易参数设置
     */
    public static final String PRODUCT_FINANCING_TRANSACTION_SETTINGS = "product:financing:transaction_settings";
    /**
     * 交易管理-产品发布-待发布融资项目撤单
     */
    public static final String PRODUCT_FINANCING_WAIT_PUBLISH_CANCEL = "product:financing:wait_publish_project:cancel";
    /**
     * 交易管理-产品发布-交易参数修改
     */
    public static final String PRODUCT_FINANCING_PURCHASE_TRANSACTION_SETTINGS_UPDATE = "product:financing:purchase_transaction_settings:update";
    /**
     * 交易管理-产品发布-申购截止时间更改
     */
    public static final String PRODUCT_FINANCING_PURCHASE_END_TIME_UPDATE = "product:financing:purchase_end_time:update";
    /**
     * 交易管理-开市闭市-开市闭市
     */
    public static final String PRODUCT_FINANCING_TRANSACTION_OPEN_CLOSE = "product:financing:transaction_open_close";
    /**
     * 交易管理-手工还款-手工还款（针对当天还款期）
     */
    public static final String PRODUCT_FINANCING_TRANSACTION_MANUAL_PAYMENTS = "product:financing:transaction_manual_payments";
    /**
     * 交易管理-定向发行组维护-定性发行组新增与编辑
     */
    public static final String PRODUCT_FINANCING_SUBSCRIBERGROUP = "product:financing:subscribergroup";
    /**
     * 结算管理-提前还款-提前还款（违约金不计）
     */
    public static final String PRODUCT_FINANCING_PACKAGE_PREPAYMENT_NO_PENALTY = "product:financing_package:prepayment_no_penalty";
    /**
     * 结算管理-资金池明细汇总查询
     */
    public static final String PRODUCT_FINANCING_SEARCH_SUMMARIZING_DETAIL = "product:financing_package:search_summarizing_detail";
    /**
     * 交易管理-自动申购融资包筛选-自动申购融资包筛选
     */
    public static final String TRANS_MANAGEMENT_AUTO_SUBS_PACKAGE_FILTER = "product:financing_package:auto_subs:filter";
    /**
     * 风险管理/结算管理-融资包管理-所有融资包所有状态查询
     */
    public static final String PRODUCT_FINANCING_PACKAGE_VIEW = "product:financing_package:view";

    /**
     * 交易管理-融资包管理-所有融资包所有状态查询
     */
    public static final String PRODUCT_FINANCING_PACKAGE_VIEW_FOR_TRANS = "product:financing_package:trans_dept_view";
    /**
     * 查询统计-客服部-融资包查看
     */
    public static final String PRODUCT_FINANCING_PACKAGE_VIEW_FOR_CUST = "product:financing_package:cust_dept_view";
    /**
     * 查询统计-结算部-融资包查看-打印签约借据
     */
    public static final String PRODUCT_FINANCING_PACKAGE_VIEW_FOR_CUST_PRINT_DEBT = "product:financing_package:cust_dept_view_print_debt";
    /**
     * 我的债务/交易管理-融资包管理-融资包终止
     */
    public static final String PRODUCT_FINANCING_PACKAGE_STOP = "product:financing_package:stop";
    /**
     * 我的债务-融资包管理-融资包撤单
     */
    public static final String PRODUCT_FINANCING_PACKAGE_CANCEL = "product:financing_package:cancel";
    /**
     * 我的债务-融资包管理-融资人融资包所有状态查询
     */
    public static final String PRODUCT_FINANCING_PACKAGE_FINANCIER_VIEW = "product:financing_package:financier_view";
    /**
     * 我的债务-融资包管理-融资包签约
     */
    public static final String PRODUCT_FINANCING_PACKAGE_SIGN_CONTRACT = "product:financing_package:sign_contract";
    /**
     * 我的债务-融资包管理-查看融资包还款表
     */
    public static final String PRODUCT_FINANCING_PACKAGE_PAYMENT_VIEW = "product:financing_package:payment_view";
    /**
     * 我的债务-提前还款-融资包提前还款
     */
    public static final String PRODUCT_FINANCING_PACKAGE_PREPAYMENT = "product:financing_package:prepayment";
    /**
     * 我的债务/会员管理-融资包管理/会员融资包管理-融资包详情-融资、担保
     */
    public static final String PRODUCT_FINANCING_PACKAGE_FIN_PROD_DETAIL_VIEW = "product:financing_package:financier_prod:details_view";
    /**
     * 交易管理/风险管理-融资包管理/会员融资包管理-融资包详情-平台
     */
    public static final String PRODUCT_FINANCING_PACKAGE_PLATFORM_DETAIL_VIEW = "product:financing_package:platform:details_view";
    /**
     * 我的账户-我的债权-融资包详情-投资人
     */
    public static final String PRODUCT_FINANCING_PACKAGE_INVESTOR_DETAILS_VIEW = "product:financing_package:investor:details_view";
    /**
     * 会员管理-会员融资包查询-会员融资包所有状态查询
     */
    public static final String PRODUCT_FINANCING_PACKAGE_MEMBER_VIEW = "product:financing_package:member_view";
    /**
     * 系统管理-融资包异常撤单-融资包异常撤单
     */
    public static final String PRODUCT_FINANCING_PACKAGE_ABNORMAL_CANCEL = "product:financing_package:abnormal_cancel";
    /**
     * 系统管理-转让规则维护-转让规则维护
     */
    public static final String SYSTEM_MANAGEMENT_TRANSFER_RULE_MAINTAIN = "system_management:transfer_rule:maintain";
    /**
     * 结算管理-非签约会员充值-非签约会员充值
     */
    public static final String SETTLEMENT_UNSIGNED_MEMBER_TOPUP = "settlement:unsigned_member:topup";
    /**
     * 结算管理-非签约会员充值-非签约会员充值审批
     */
    public static final String SETTLEMENT_UNSIGNED_MEMBER_TOPUP_APPROVE = "settlement:unsigned_member_topup:approve";
    /**
     * 结算管理-非签约会员充值-非签约会员充值明细查看
     */
    public static final String SETTLEMENT_UNSIGNED_MEMBER_TOPUP_DETAILS = "settlement:unsigned_member:topup:details";
    /**
     * 结算管理-非签约会员充值-非签约会员提现扣款确认
     */
    public static final String SETTLEMENT_UNSIGNED_MEMBER_WITHDRAW_CONFIRM = "settlement:unsigned_member:withdraw:confirm";
    /**
     * 结算管理-非签约会员提现确认页面打印
     */
    public static final String SETTLEMENT_UNSIGNED_MEMBER_WITHDRAW_CONFIRM_PRINT = "settlement:unsigned_member:withdraw_confirm:print";

    /**
     * 结算管理-资金冲正复核
     */
    public static final String SETTLEMENT_FUND_ACCT_REVERSE = "settlement:fund:acct:reverse";
    /**
     * 结算管理-资金冲正申请
     */
    public static final String SETTLEMENT_FUND_ACCT_REVERSE_APPL = "settlement:fund:acct:reverse_appl";

    /**
     * 结算管理-违约处理-违约处理（清分）
     */
    public static final String SETTLE_MANAGEMENT_DEFAULT_HANDLING = "product:financing_package:default:handling";

    /**
     * 结算管理-手工转账-平台-会员转账复核
     */
    public static final String SETTLE_MANAGEMENT_PLATFORM_MEMBER_TRANSFER_CHECK = "product:financing_package:platform_member:transfer_check";
    /**
     * 结算管理-手工转账-平台-会员转账及明细查询
     */
    public static final String SETTLE_MANAGEMENT_PLATFORM_MEMBER_TRANSFER = "product:financing_package:platform_member:transfer";

    /**
     * 结算管理-内部转账-批量转帐
     */
    public static final String SETTLE_MANAGEMENT_BATCH_TRANSFER = "product:financing_package:batch:transfer";
    /**
     * 结算管理-手工转账-会员-会员转账复核
     */
    public static final String SETTLE_MANAGEMENT_MEMBER_MEMBER_TRANSFER_CHECK = "product:financing_package:member_member:transfer_check";

    /**
     * 结算管理-手工转账-会员-会员转账及明细查询
     */
    public static final String SETTLE_MANAGEMENT_MEMBER_MEMBER_TRANSFER = "product:financing_package:member_member:transfer";
    /**
     * 银行管理-招行对账明细-招行对账明细查询
     */
    public static final String BANK_MANAGEMENT_CMB_TRX_DETAILS = "bank_mng:cmb_trx:details";
    /**
     * 我的账户-我的债权-债权查看功能
     */
    public static final String MY_ACCOUNT_MY_CREDITOR_VIEW = "account:my_creditor:view";
    /**
     * 我的账户-我的债权-查看收益表
     */
    public static final String MY_ACCOUNT_MY_CREDITOR_INCOME_TABLE = "account:my_creditor:view_income_table";
    /**
     * 我的账户-我的债权-债权转让
     */
    public static final String MY_ACCOUNT_MY_CREDITOR_CREDITOR_ASSIGNMENT = "account:my_creditor:assignment";
    /**
     * 我的账户-我的债权-债权转让撤单
     */
    public static final String MY_ACCOUNT_MY_CREDITOR_CREDITOR_ASSIGNMENT_WITHDRAW = "account:my_creditor:assignment:withdraw";
    /**
     * 我的账户-账户总揽-小微宝
     */
    public static final String MY_ACCOUNT_OVERVIEW_XWB = "account:overview:xwb";
    /**
     * 我的账户-账户总揽-其他
     */
    public static final String MY_ACCOUNT_OVERVIEW_OTHERS = "account:overview:others";
    /**
     * 我的账户-账户总揽-投资收益
     */
    public static final String MY_ACCOUNT_OVERVIEW_ESTIMATED_INCOME = "account:overview:estimated:income";
    /**
     * 我的账户-账户总揽-账户充值（签约用户）
     */
    public static final String MY_ACCOUNT_OVERVIEW_DEPOSITE = "account:overview:deposite";
    /**
     * 我的账户-账户总揽-账户提现（签约用户）
     */
    public static final String MY_ACCOUNT_OVERVIEW_WITHDRAW = "account:overview:withdraw";
    /**
     * 我的账户-账户总揽-非签约会员提现申请
     */
    public static final String MY_ACCOUNT_OVERVIEW_UNSIGNED_MEMBER_WITHDRAW_APPLY = "account:overview:unsigned_member:withdraw_apply";
    /**
     * 我的账户-账户总揽-非签约会员提现申请记录
     */
    public static final String MY_ACCOUNT_OVERVIEW_UNSIGNED_MEMBER_WITHDRAW_APPLY_VIEW = "account:overview:unsigned_member:withdraw_apply:view";
    /**
     * 我的账户-账户明细-账户明细
     */
    public static final String MY_ACCOUNT_ACCOUNT_DETAILS = "account:account:details";
    /**
     * 我的账户-小微宝-小微宝
     */
    public static final String MY_ACCOUNT_XWB = "account:xwb";
    /**
     * 我的账户-交易明细-交易明细
     */
    public static final String MY_ACCOUNT_TRANS_DETAILS = "account:trans:details";
    /**
     * 我的设置-密码设置-密码设置
     */
    public static final String MY_SETTINGS_PASSWORD_SETTINGS = "my_settings:password:settings";
    /**
     * 我的设置-自动申购设置-自动申购设置
     */
    public static final String MY_SETTINGS_AUTOSUBS_SETTINGS = "my_settings:auto_subs:settings";
    /**
     * 会员管理-会员信息查看-会员姓名查看
     */
    public static final String MEMBER_VIEW_REAL_NAME = "member:member_info_name:view";

    /**
     * 会员管理-会员信息查看-会员身份证查看
     */
    public static final String MEMBER_VIEW_REAL_IDCARD_NO = "member:member_info_idcard:view";

    /**
     * 会员管理-会员信息查看-会员银行卡查看
     */
    public static final String MEMBER_VIEW_REAL_BANKCARD_NO = "member:member_info_bankacct:view";

    /**
     * 会员管理-会员信息查看-会员手机号查看
     */
    public static final String MEMBER_VIEW_REAL_PHONE_NO = "member:member_info_mobile_number:view";

    /**
     * 系统管理-日终处理-批量任务
     */
    public static final String BATCH_JOB_WORK_LIST_MGT = "system_management:batch:job_work_list";

    /**
     * 系统管理-缓存-刷新
     */
    public static final String SYSTEM_MANAGEMENT_CACHE_MANAGEMENT = "system_management:cache:management";

    /**
     * 系统管理-会员账户冻结-会员账户冻结
     */
    public static final String SYSTEM_MANAGEMENT_MEMBER_ACCOUNT_FREEZE = "system:member_account:freeze";
    
    /**
     * 系统管理-托管交易日志 -查询
     */
    public static final String SYSTEM_MANAGEMENT_ESWMSGLOG_QUERY= "system_management:eswmsglog:query";

    /**
     * 会员投资行为分析-会员资产查询
     */
    public static final String MEMBER_ASSERT_QUERY = "member:assert:query";
    /**
     * 会员投资行为分析-投资会员业务跟踪
     */
    public static final String MEMBER_INVESTOR_BIZ_TRACE = "member:investor_biz:trace";

    /**
     * 会员投资行为分析-投资会员月度理财报表
     */
    public static final String MEMBER_INVESTOR_MONTH_REPORT = "member:investor_month:report";

    /**
     * 会员投资行为分析-交易手续费查询
     */
    public static final String MEMBER_TRADE_FEE_QUERY = "member:trade_fee:query";

    /**
     * 会员投资行为分析-交易手续费查询(服务中心)
     */
    public static final String MEMBER_TRADE_FEE_QUERY_FOR_ATHD = "athd_center:member:trade_fee:query";
    /**
     * 服务质量管理数据-授权服务中心交易跟踪
     */
    public static final String ATHD_CNTL_TRADE_TRACE = "athd_cntl:trade:trace";
    /**
     * 服务质量管理数据-信息服务费查询
     */
    public static final String INFO_SERVICE_FEE_QUERY = "info_service_fee:query";
    /**
     * 服务质量管理数据-融资包统计
     */
    public static final String FINANCING_PACKAGE_STATISTICS = "financing_package:statistics";
    /**
     * 服务质量管理数据-会员审核查询
     */
    public static final String MEMBER_APPROVE_QUERY = "member:approve:query";
    /**
     * 服务质量管理数据-融资包还款查询
     */
    public static final String FINANCING_PACKAGE_PAYMENT_QUERY = "financing_package:payment:query";
    /**
     * 服务质量管理数据-我的还款计划表
     */
    public static final String MY_REPAYMENT_SCHEDULE_TABLE = "my:repayment_schedule:table";
    /**
     * 服务质量管理数据-近期还款计划
     */
    public static final String RECENT_PAYMENT_SCHEDULE = "recent:payment:schedule";
    /**
     * 服务受理-会员基本信息查询
     */
    public static final String MEMBER_BASIC_INFO_QUERY = "report:member:basic_info:query";
    /**
     * 服务受理-会员充值/提现信息查询
     */
    public static final String MEMBER_TOPUP_WITHDRAW_INFO_QUERY = "member:topup:withdraw:query";
    /**
     * 服务受理-会员提现申请查询
     */
    public static final String MEMBER_WITHDRAW_APPLY_QUERY = "member:withdraw_apply:query";
    /**
     * 服务受理-会员账户查询
     */
    public static final String MEMBER_ACCOUNT_QUERY = "member:account:query";

    /**
     * 服务受理-会员投资统计
     */
    public static final String MEMBER_INVESTMENT_STATISTICS = "member:investment:statistics";
    /**
     * 服务受理-借款合同汇总
     */
    public static final String LOAN_CONTRACT_SUMMARY = "loan:contract:summary";
    /**
     * 服务受理-债权转让查询
     */
    public static final String CREDITOR_TRANSFER_QUERY = "creditor:transfer:query";
    /**
     * 服务受理-债权转让费用查询
     */
    public static final String CREDITOR_TRANSFER_FEE_QUERY = "creditor:transfer_fee:query";
    /**
     * 融资费用查询
     */
    public static final String FINANCING_FEE_QUERY = "financing:fee:query";

    /**
     * 融资费用收入明细
     */
    public static final String FINANCING_FEE_DETAILS_QUERY = "financing:fee_details:query";

    /**
     * 平台综合账户-平台综合账户查看
     */
    public static final String PLATFORM_UNIVERSAL_ACCOUNT_VIEW = "platform:universal_account:view";
    /**
     * 平台综合账户-平台账户明细
     */
    public static final String PLATFORM_UNIVERSAL_ACCOUNT_VIEW_DETAIL = "platform:universal_account:detail_view";
    /**
    /**
     * 平台综合账户-平台账户明细导出
     */
    public static final String PLATFORM_UNIVERSAL_ACCOUNT_VIEW_DETAIL_EX = "platform:universal_account:detail_ex_view";
    /**
     * 平台综合账户-平台综合账户充值
     */
    public static final String PLATFORM_UNIVERSAL_ACCOUNT_DEPOSITE = "platform:universal_account:deposite";
    /**
     * 平台综合账户-平台综合账户提现
     */
    public static final String PLATFORM_UNIVERSAL_ACCOUNT_WITHDRAW = "platform:universal_account:withdraw";
    /**
     * 小微平台角色
     */
    public static final String PLATFORM_USER = "role:platform:user";
    /**
     * 风控部人员
     */
    public static final String RISK_CONTROL_DEPT = "role:risk_control:dept";
    /**
     * 交易部人员
     */
    public static final String TRANSACTION_DEPT = "role:transaction:dept";
    /**
     * 客服部人员
     */
    public static final String CUSTOMER_SERVICE_DEPT = "role:customer_service:dept";
    /**
     * 客服部人员经理
     */
    public static final String CUSTOMER_SERVICE_MANAGER = "role:customer_service:manager";
    /**
     * 产品部人员
     */
    public static final String PRODUCT_DEMPARTMENT = "role:product:department";
    /**
     * 渠道部人员
     */
    public static final String CHANNEL_DEMPARTMENT = "role:channel:department";
    /**
     * 结算部人员
     */
    public static final String SETTLEMENT_DEMPARTMENT = "role:settlement:department";
    /**
     * 财务部人员
     */
    public static final String FINANCE_DEMPARTMENT = "role:finance:dept";
    /**
     * 系统管理员
     */
    public static final String SYSTEM_ADMIN = "role:system:admin";
    /**
     * 服务中心人员
     */
    public static final String ATHD_CENTER_MEMBER = "role:athd_center:member";
    /**
     * 服务中心经理
     */
    public static final String ATHD_CENTER_MANAGER = "role:athd_center:manager";
    /**
     * 投资会员
     */
    public static final String INVESTOR_MEMBER = "role:investor:member";
    /**
     * 融资会员
     */
    public static final String FINANCIER_MEMBER = "role:financier:member";
    /**
     * 担保机构会员
     */
    public static final String PROD_SERV_MEMBER = "role:prod_serv:member";
    /**
     * 游客(投融资)
     */
    public static final String TOURIST_MEMBER = "role:tourist:member";
    /**
     * 游客(担保机构/服务中心)
     */
    public static final String TOURIST_AGENCY_MEMBER = "role:tourist_agency:member";

    /**
     * 消息待办
     */
    public static final String MESSAGE_WAITING_TODO = "message:waiting:todo";

    /**
     * 风控-当日逾期详情
     */
    public static final String REPORT_DAILY_OVERDUE_DETAILS = "report:daily_overdue:details";
    /**
     * 风控-当日代偿详情
     */
    public static final String REPORT_DAILY_COMPENSATORY_DETAILS = "report:daily_compensatory:details";

    /**
     * 会员-冻结保留明细
     */
    public static final String MEMBER_FREEZE_RESERVE_DETAILS = "freeze:reserve_details:member";

    /**
     * 平台-冻结保留明细
     */
    public static final String PLATFORM_FREEZE_RESERVE_DETAILS = "freeze:reserve_details:platform";
    /**
     * 交易管理招行签到
     */
    public static final String TRANS_MANAGEMENT_CMB_SIGN = "trans_management:cmb_sign";
    
    /**
     * 查询统计-会员资金流水明细
     */
    public static final String INVSTR_TRANSACTION_JOURNAL_QUERY = "report:trx:jnl:query";
    /**
     * 查询统计-会员资金流水明细导出  
     */
    public static final String INVSTR_TRANSACTION_JOURNAL_EX_QUERY = "report:trx:jnl:exquery";
    
    /**
     * 查询统计-债权转让手续费
     */
    public static final String REPORT_AUTHZD_CTR_TRANS_FEE = "report:authzd:ctr:trans:fee";
    
    /**
     * 通用部门
     */
    public static final String COMMON_DEPT = "common:dept";
    
    /**
     * 结算管理-资金池对账
     */
    public static final String POOL_CHECK = "pool:check";
    
    /**
     * 托管账户管理
     */
    public static final String ESW_ACCT_MGT = "escrow:acct:mgt";
    
    /**
     * 托管账户余额核对
     */
    public static final String ESW_ACCT_BAL_QRY = "escrow:acct:bal:query";
    
    /**
     * 托管账户充值
     */
    public static final String ESW_ACCT_RECHARGE = "escrow:acct:recharge";
    
    /**
     * 托管账户提现
     */
    public static final String ESW_ACCT_CASH = "escrow:acct:cash";
    
    /**
     * 托管账户转账
     */
    public static final String ESW_ACCT_TRANSFER = "escrow:acct:transfer";
    
    /**
     * 资金轧差
     */
    public static final String FUND_ACCT_NETTING = "fund:acct:netting";
    
    /**
     * version 20150317 合同维护界面化
     * 
     * 交易管理-合同维护
     */
    public static final String TRANS_CONTRACT_RATE_MAINTAIN = "trans:contract_rate:maintain";
    
    /**
     * version 20150317
     * 
     * 交易管理-代理费合同维护
     */
    public static final String TRANS_AGENCY_FEE_CONTRACT_MANAGE = "trans:agency_fee_contract:mamage";
}
