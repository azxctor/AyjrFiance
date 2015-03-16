package com.hengxin.platform.report.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hengxin.platform.common.constant.LiteralConstant;
import com.hengxin.platform.common.constant.UrlConstant;
import com.hengxin.platform.common.controller.BaseController;
import com.hengxin.platform.common.domain.DynamicOption;
import com.hengxin.platform.common.domain.EnumOption;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.enums.EOptionCategory;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.util.AppConfigUtil;
import com.hengxin.platform.common.util.SystemDictUtil;
import com.hengxin.platform.fund.enums.ECashPool;
import com.hengxin.platform.fund.enums.ERechargeWithdrawFlag;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.member.enums.EApplicationStatus;
import com.hengxin.platform.product.enums.EFeePayMethodType;
import com.hengxin.platform.product.enums.EPayStatus;
import com.hengxin.platform.product.enums.ETransferStatus;
import com.hengxin.platform.report.consts.ReportConsts;
import com.hengxin.platform.report.dto.AnotherTimeSelectDto;
import com.hengxin.platform.report.dto.CommonParameter;
import com.hengxin.platform.report.dto.CreditTransferReportDto;
import com.hengxin.platform.report.dto.CreditorDto;
import com.hengxin.platform.report.dto.FinanceFeeReceiptDetailDto;
import com.hengxin.platform.report.dto.InvestorBusinessTraceDto;
import com.hengxin.platform.report.dto.InvestorMonthReportDto;
import com.hengxin.platform.report.dto.LoanContractDto;
import com.hengxin.platform.report.dto.MemberAssetDto;
import com.hengxin.platform.report.dto.MemberChargeWithdrawDto;
import com.hengxin.platform.report.dto.MemberInfoDto;
import com.hengxin.platform.report.dto.MemberStatisticsDto;
import com.hengxin.platform.report.dto.PackageRepaymentDto;
import com.hengxin.platform.report.dto.PaymentDetailDto;
import com.hengxin.platform.report.dto.PaymentStatisticsDto;
import com.hengxin.platform.report.dto.RecentPaymentDto;
import com.hengxin.platform.report.dto.ReportParameterStringDto;
import com.hengxin.platform.report.dto.ServiceFeeDto;
import com.hengxin.platform.report.dto.SimpleLot;
import com.hengxin.platform.report.dto.TimeSelectDto;
import com.hengxin.platform.report.dto.TransferContractDetailsDto;
import com.hengxin.platform.report.enums.EAssetLevel;
import com.hengxin.platform.report.enums.ECreditTransferFeeType;
import com.hengxin.platform.report.enums.ECreditTransferKeyType;
import com.hengxin.platform.report.enums.ECreditTransferTimeType;
import com.hengxin.platform.report.enums.EDebtStatus;
import com.hengxin.platform.report.enums.EFeePayMethod;
import com.hengxin.platform.report.enums.EFundUserSearchType;
import com.hengxin.platform.report.enums.EMemberStatisticsType;
import com.hengxin.platform.report.enums.EMemberType;
import com.hengxin.platform.report.enums.EMemeberLevel;
import com.hengxin.platform.report.enums.EPayDateType;
import com.hengxin.platform.report.enums.ETimeType;
import com.hengxin.platform.report.service.ReportService;
import com.hengxin.platform.report.service.TransferContractService;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.domain.User;
import com.hengxin.platform.security.entity.UserPo;
import com.hengxin.platform.security.service.AuthorizationService;
import com.hengxin.platform.security.service.UserService;

/**
 * ReportController.
 *
 */
@Controller
public class ReportController extends BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReportController.class);
    private static final String MAX_TIME = " 23:59:59";
    private static final String MIN_TIME = " 00:00:00";
    private static final String MAX_DATE = "9999-12-30 23:59:59";
    private static final String MIN_DATE = "1111-01-01 00:00:00";
    private static final String PAY_STATUS_LIST = "payStatusList";
    private static final String SELECT_DATE = "selectDate";
    private static final String KEY_WORD = "KEY_WORD";
    private static final String PAY_STATUS = "PAY_STATUS";
    private static final String SIGNING_TS = "SIGNING_TS";
    private static final String PKG_ID = "PKG_ID";
    private static final String USER_ID = "USER_ID";
    private static final String AVAI_MONEY = "AVAI_MONEY";
    private static final String AUTHZD_CTR_ID_UPPER = "AUTHZD_CTR_ID";
    private static final String AUTHZD_CTR_ID_LOWER = "authzd_ctr_id";
    private static final String SUM_BAL = "SUM_BAL";
    private static final String CREATE_TS = "CREATE_TS";
    private static final String PAY_DT = "PAY_DT";
    private static final String JNL_NO = "JNL_NO";
    private static final int YEAR_2010 = 2010;
    private static final int DAY_BACKWARD_7 = -7;
    
    @Autowired
    private ReportService reportService;

    @Autowired
    private SecurityContext securityContext;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private TransferContractService transferContractService;

    /**
     * 11.1.1 融资包还款查询-加载页面
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.QUERY_STATISTICS_FINANCING_PACKAGE_PAYMENT_TABLE_QUERY_URL)
    @RequiresPermissions(value = { Permissions.FINANCING_PACKAGE_PAYMENT_QUERY })
    public String renderPackageRepaymentPage(HttpServletRequest request, HttpSession session, Model model) {
        List<EnumOption> enumOptions = new ArrayList<EnumOption>();
        for (EnumOption enumOption : getStaticOptions(EPayStatus.class, true)) {
            if (EPayStatus.valueOf(enumOption.getCode()) == EPayStatus.FINISH
                    || EPayStatus.valueOf(enumOption.getCode()) == EPayStatus.NORMAL
                    || EPayStatus.valueOf(enumOption.getCode()) == EPayStatus.BADDEBT) {
                continue;
            }
            enumOptions.add(enumOption);
        }

        enumOptions.add(new EnumOption("BADDEBT", "待追偿"));
        model.addAttribute(PAY_STATUS_LIST, enumOptions);
        Date date = new Date();
        String dateString = DateUtils.formatDate(date, LiteralConstant.YYYY_MM_DD);
        model.addAttribute(SELECT_DATE, dateString);
        return "packet/packet_repay_search";
    }

    /**
     * 11.1.1 融资包还款查询
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "report/getpackagerepayment", method = RequestMethod.POST)
    @RequiresPermissions(value = { Permissions.FINANCING_PACKAGE_PAYMENT_QUERY })
    public ReportParameterStringDto getPackageRepaymentParameter(@RequestBody PackageRepaymentDto query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        StringBuffer startDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getSignStartDate())) {
            startDate.append(query.getSignStartDate()).append(MIN_TIME);
        } else {
            startDate.append(MIN_DATE);
        }
        StringBuffer endDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getSignEndDate())) {
            endDate.append(query.getSignEndDate()).append(MAX_TIME);
        } else {
            endDate.append(MAX_DATE);
        }
        StringBuffer signDateBuffer = new StringBuffer();
        signDateBuffer.append(startDate).append(",").append(endDate);
        if (StringUtils.isNotBlank(query.getSignEndDate()) || StringUtils.isNotBlank(query.getSignStartDate())) {
            filter.put(SIGNING_TS, signDateBuffer.toString());
        }
        if (query.getPayStatus() != null && query.getPayStatus() != EPayStatus.NULL) {
            filter.put(PAY_STATUS, query.getPayStatus().getCode());
        } else {
            filter.put(PAY_STATUS, "01,02,03,04,05,99,BD");
        }

        if (StringUtils.isNotBlank(query.getKeyWord())) {
            filter.put(KEY_WORD, query.getKeyWord().trim());
        }
        String masks = "FNCR_NAME:0";
        if (this.securityContext.hasPermission(Permissions.SETTLEMENT_DEMPARTMENT)
                || this.securityContext.hasPermission(Permissions.RISK_CONTROL_DEPT)) {
            masks = null;
        }

        return this.reportService.GetReportParameterService(ReportConsts.RID_PACKAGERE_REPAYMENT,
                ReportConsts.PAGENAME_COMMON, query.getExportFileExt(), query.getParentClientEvent(),
                query.getEventName(), filter, masks, null);
    }

    /**
     * 11.1.1 融资包还款查询 - 查看明细
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "report/getrepaymentdetail")
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_PACKAGE_PAYMENT_VIEW,
            Permissions.FINANCING_PACKAGE_PAYMENT_QUERY }, logical = Logical.OR)
    public ReportParameterStringDto getRepaymentDetail(@RequestBody CommonParameter query, HttpServletRequest request,
            HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        String[] tmp = query.getCommonId().split("-");
        filter.put(PKG_ID, tmp[0]);
        filter.put("SEQ_ID", tmp[1]);
        String pageTitle = this.reportService.getPackageByPackageId(tmp[0]).getPackageName() + ": 还款明细";
        UserPo userPo = this.reportService.getUserByUserId(getCurrentUserId());
        return this.reportService.GetReportParameterService(ReportConsts.RID_DETAIL, ReportConsts.PAGENAME_DETAIL,
                query.getExportFileExt(), query.getParentClientEvent(), query.getEventName(), filter, null, pageTitle,
                null, null, null, userPo.getName());
    }

    /**
     * 融资包还款表-加载页面
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/repaymentpage")
    @ResponseStatus(value = HttpStatus.PARTIAL_CONTENT)
    public String renderRepayment(HttpServletRequest request, HttpSession session, Model model) {
        return "packet/repayment_schedule";
    }

    /**
     * 11.1.2融资包还款表
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "report/getrepaymentbypackageId")
    public ReportParameterStringDto getRepaymentByPackageId(@RequestBody CommonParameter query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put(PKG_ID, query.getCommonId());
        String fixed = "2";

        String disable = null;
        if (this.securityContext.isFinancier()) {
            disable = "URL_DATAIL";
        }

        return this.reportService.GetReportParameterService(ReportConsts.RID_PACKAGERE_REPAYMENT_BY_PACKAGEID,
                ReportConsts.PAGENAME_COMMON, query.getExportFileExt(), query.getParentClientEvent(),
                query.getEventName(), filter, null, null, fixed, disable);
    }

    /**
     * 11.1.2融资包还款收益表
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "report/getreceiptsbypackageId")
    public ReportParameterStringDto getReceiptsByPackageId(@RequestBody CommonParameter query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put(PKG_ID, query.getCommonId());
        filter.put(USER_ID, securityContext.getCurrentUserId());
        String fixed = "2";

        String pageTitle = this.reportService.getPackageByPackageId(query.getCommonId()).getPackageName() + "收益表";

        return this.reportService.GetReportParameterService(ReportConsts.RID_PACKAGERE_RECEIPTS_BY_PACKAGEID,
                ReportConsts.PAGENAME_COMMON, query.getExportFileExt(), query.getParentClientEvent(),
                query.getEventName(), filter, null, pageTitle, fixed);
    }

    /**
     * 11.1.4我的还款计划表-加载页面
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.QUERY_STATISTICS_FINANCING_PACKAGE_PAYMENT_QUERY_URL)
    @RequiresPermissions(value = Permissions.MY_REPAYMENT_SCHEDULE_TABLE)
    public String renderMyRepaymentSchedule(HttpServletRequest request, HttpSession session, Model model) {
        return "packet/my_repayment_schedule";
    }

    /**
     * 11.1.4我的还款计划表
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "report/getmyrepaymentschedule")
    public ReportParameterStringDto getMyRepaymentSchedule(@RequestBody CommonParameter query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put(USER_ID, getCurrentUserId());
        String fixed = "6";

        return this.reportService.GetReportParameterService(ReportConsts.RID_MY_REPAYMENT_SCHEDULE,
                ReportConsts.PAGENAME_MY_PAYMENT_SCHEDULE, query.getExportFileExt(), query.getParentClientEvent(),
                query.getEventName(), filter, null, null, fixed);
    }

    /**
     * 11.2.1 会员资产查询-加载页面
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.QUERY_STATISTICS_MEMBER_ASSET_QUERY_URL)
    @RequiresPermissions(value = Permissions.MEMBER_ASSERT_QUERY)
    public String renderMemberAssetPage(HttpServletRequest request, HttpSession session, Model model) {
        List<EnumOption> enumOptions = getStaticOptions(EMemberType.class, true);
        List<EnumOption> memeberTypeList = new ArrayList<EnumOption>();
        for (EnumOption enumOption : enumOptions) {
            if (EMemberType.valueOf(enumOption.getCode()) == EMemberType.INVESTOR
                    || EMemberType.valueOf(enumOption.getCode()) == EMemberType.FINANCER
                    || EMemberType.valueOf(enumOption.getCode()) == EMemberType.INVESTOR_FINANCER
                    || EMemberType.valueOf(enumOption.getCode()) == EMemberType.PRODUCTSERVICE)
                memeberTypeList.add(enumOption);
        }
        model.addAttribute("memeberTypeList", memeberTypeList);
        model.addAttribute("channelList", getStaticOptions(ECashPool.class, true));

        return "packet/member_trace_property";
    }

    /**
     * 11.2.1会员资产查询
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "report/getmemberasset", method = RequestMethod.POST)
    @RequiresPermissions(value = Permissions.MEMBER_ASSERT_QUERY)
    public ReportParameterStringDto getMemberAssetParameter(@RequestBody MemberAssetDto query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(query.getAvailableAmount())) {
            filter.put(AVAI_MONEY, query.getAvailableAmount());
        }
        if (StringUtils.isNotBlank(query.getWithdrawAmount())) {
            filter.put("MAX_CASHABLE_AMT", query.getWithdrawAmount());
        }
        if (StringUtils.isNotBlank(query.getFrozenAmount())) {
            filter.put("FREEZABLE_AMT", query.getFrozenAmount());
        }
        if (StringUtils.isNotBlank(query.getKeyWord())) {
            filter.put(KEY_WORD, query.getKeyWord().trim());
        }
        if (query.getMemberType() != EMemberType.NULL && query.getMemberType() != null) {
            filter.put("ROLE_ID", query.getMemberType().getCode());
        }
        if (query.getCashPool() != ECashPool.ALL) {
            filter.put("CASH_POOL", query.getCashPool().getCode());
        }
        if (StringUtils.isNotBlank(query.getIsSigned())) {
            filter.put("SIGNED_FLG", query.getIsSigned());
        }
        String fixed = "4";

        return this.reportService.GetReportParameterService(ReportConsts.RID_MEMBER_ASSET,
                ReportConsts.PAGENAME_PAGING, query.getExportFileExt(), query.getParentClientEvent(),
                query.getEventName(), filter, null, null, fixed);
    }

    /**
     * 11.2.2 投资会员业务跟踪-加载页面
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.QUERY_STATISTICS_INVESTOR_BIZ_TRACE_URL)
    @RequiresPermissions(value = Permissions.MEMBER_INVESTOR_BIZ_TRACE)
    public String renderMemberBusinessTracePage(HttpServletRequest request, HttpSession session, Model model) {
    	List<DynamicOption> investerLevelsPrime = SystemDictUtil.getRootDictList(EOptionCategory.INVESTOR_LEVEL, false);
    	DynamicOption all = new DynamicOption();
    	all.setCategory(EOptionCategory.INVESTOR_LEVEL.getCode());
    	all.setCode("");
    	all.setText("全部");
    	List<DynamicOption> investerLevels = new ArrayList<DynamicOption>();
    	investerLevels.add(all);
    	investerLevels.addAll(investerLevelsPrime);
    	model.addAttribute("memberLevelList", investerLevels);
        List<EnumOption> memeberStatusList = new ArrayList<EnumOption>();
        EnumOption enumOption = new EnumOption("", "全部");
        EnumOption enumOption1 = new EnumOption("A", "正常");
        memeberStatusList.add(enumOption);
        memeberStatusList.add(enumOption1);
        model.addAttribute("memeberStatusList", memeberStatusList);
        model.addAttribute("remainAmountList", getStaticOptions(EAssetLevel.class, true));
        model.addAttribute("totalAmountList", getStaticOptions(EAssetLevel.class, true));
        /** version 20150306 投资会员业务跟踪报表，添加查询条件“授权服务中心” **/
        model.addAttribute("serviceCenterList", reportService.getServiceCenterList());
        Boolean isServiceCenter = securityContext.isAuthServiceCenter();
        model.addAttribute("isServiceCenter", isServiceCenter);
        Date date = new Date();
        String dateString = DateUtils.formatDate(date, LiteralConstant.YYYY_MM_DD);
        model.addAttribute(SELECT_DATE, dateString);
        return "packet/member_trace";
    }

    /**
     * 11.2.2 投资会员业务跟踪
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "report/gememberbusinesstrace", method = RequestMethod.POST)
    public ReportParameterStringDto getMemberBusinessTraceParameter(@RequestBody InvestorBusinessTraceDto query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        if (query.getMemberLevel() != null && !query.getMemberLevel().isEmpty()) {
            filter.put("INVSTR_LVL", query.getMemberLevel());
        }
        if (securityContext.isAuthServiceCenter()) {
            filter.put(AUTHZD_CTR_ID_UPPER, getCurrentUserId());
        }
        if (StringUtils.isNotBlank(query.getApplicationStatus())) {
            filter.put("USER_STATUS", query.getApplicationStatus());
        }

        if (query.getRemainLevel() == EAssetLevel.ONE) {
            filter.put(AVAI_MONEY, "0,10000");
        } else if (query.getRemainLevel() == EAssetLevel.TEN) {
            filter.put(AVAI_MONEY, "10000,100000");
        } else if (query.getRemainLevel() == EAssetLevel.FIFTY) {
            filter.put(AVAI_MONEY, "100000,500000");
        } else if (query.getRemainLevel() == EAssetLevel.HUNDRED) {
            filter.put(AVAI_MONEY, "500000,1000000");
        } else if (query.getRemainLevel() == EAssetLevel.ABOVE) {
            filter.put(AVAI_MONEY, "1000000,9999999999999999");
        }

        if (query.getAssetLevel() == EAssetLevel.ONE) {
            filter.put(SUM_BAL, "0,10000");
        } else if (query.getAssetLevel() == EAssetLevel.TEN) {
            filter.put(SUM_BAL, "10000,100000");
        } else if (query.getAssetLevel() == EAssetLevel.FIFTY) {
            filter.put(SUM_BAL, "100000,500000");
        } else if (query.getAssetLevel() == EAssetLevel.HUNDRED) {
            filter.put(SUM_BAL, "500000,1000000");
        } else if (query.getAssetLevel() == EAssetLevel.ABOVE) {
            filter.put(SUM_BAL, "1000000,9999999999999999");
        }

        if (StringUtils.isNotBlank(query.getKeyWord())) {
            filter.put(KEY_WORD, query.getKeyWord().trim());
        }
        
        if (StringUtils.isNotBlank(query.getServiceCenterId())){
        	filter.put(AUTHZD_CTR_ID_LOWER, query.getServiceCenterId());
        }

        StringBuffer startDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getStartDate())) {
            startDate.append(query.getStartDate()).append(MIN_TIME);
        } else {
            startDate.append(MIN_DATE);
        }
        StringBuffer endDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getEndDate())) {
            endDate.append(query.getEndDate()).append(MAX_TIME);
        } else {
            endDate.append(MAX_DATE);
        }
        StringBuffer signDateBuffer = new StringBuffer();
        signDateBuffer.append(startDate).append(",").append(endDate);
        filter.put(CREATE_TS, signDateBuffer.toString());
        String fixed = "2";
        return this.reportService.GetReportParameterService(ReportConsts.RID_MEMBER_BUSINESS_TRACE,
                ReportConsts.PAGENAME_PAGING, query.getExportFileExt(), query.getParentClientEvent(),
                query.getEventName(), filter, null, null, fixed);
    }

    /**
     * 债权明细-加载页面
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/creditorpage")
    @ResponseStatus(value = HttpStatus.PARTIAL_CONTENT)
    public String renderCreditorPage(HttpServletRequest request, HttpSession session, Model model) {
        model.addAttribute("debtStatusList", getStaticOptions(EDebtStatus.class, true));
        StringBuffer date = new StringBuffer();
        SimpleDateFormat sdf = new SimpleDateFormat(LiteralConstant.YYYY_MM_DD);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, DAY_BACKWARD_7);
        date.append(sdf.format(cal.getTime()));
        model.addAttribute("startDate", date.toString());
        date = new StringBuffer();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -1);
        date.append(sdf.format(cal.getTime()));
        model.addAttribute("endDate", date.toString());
        Date date1 = new Date();
        String dateString = DateUtils.formatDate(date1, LiteralConstant.YYYY_MM_DD);
        model.addAttribute(SELECT_DATE, dateString);
        return "packet/member_trace_detail";
    }

    /**
     * 债权明细
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "report/getcreditor", method = RequestMethod.POST)
    public ReportParameterStringDto getCreditorParameter(@RequestBody CreditorDto query, HttpServletRequest request,
            HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put(USER_ID, query.getCommonId());
        if (StringUtils.isNotBlank(query.getKeyWord())) {
            filter.put(KEY_WORD, query.getKeyWord().trim());
        }
        if (query.getDebtStatus() != EDebtStatus.NUll) {
            filter.put("STATUS", query.getDebtStatus().getCode());
        }
        StringBuffer startDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getStartDate())) {
            startDate.append(query.getStartDate()).append(MIN_TIME);
        } else {
            startDate.append(MIN_DATE);
        }
        StringBuffer endDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getEndDate())) {
            endDate.append(query.getEndDate()).append(MAX_TIME);
        } else {
            endDate.append(MAX_DATE);
        }
        StringBuffer signDateBuffer = new StringBuffer();
        signDateBuffer.append(startDate).append(",").append(endDate);
        if (StringUtils.isNotBlank(query.getStartDate()) || StringUtils.isNotBlank(query.getEndDate())) {
            filter.put(SIGNING_TS, signDateBuffer.toString());
        }
        String specialText = null;
        if (securityContext.isAuthServiceCenter()) {
            specialText = "PKG_NAME:";
            if (StringUtils.isNotBlank(query.getEventName())) {
                String eventName = query.getEventName();
                String[] split = eventName.split(",");
                if (split.length == 2) {
                    query.setEventName(split[1]);
                }
            }
        }

        return this.reportService.GetReportParameterService(ReportConsts.RID_CREDITOR, ReportConsts.PAGENAME_COMMON,
                query.getExportFileExt(), query.getParentClientEvent(), query.getEventName(), filter, null, null, null,
                null, specialText);
    }

    /**
     * 债权明细的查看详细
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "report/getcreditordetailbylotid")
    public ReportParameterStringDto getCreditorDetailBylotId(@RequestBody CommonParameter query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        String[] tmp = query.getCommonId().split("-");
        if (tmp != null && tmp.length >= 2) {
            filter.put(USER_ID, tmp[0]);
            filter.put(PKG_ID, tmp[1]);
        }

        return this.reportService.GetReportParameterService(ReportConsts.RID_CREDITOR_DETAILS,
                ReportConsts.PAGENAME_COMMON, query.getExportFileExt(), query.getParentClientEvent(),
                query.getEventName(), filter);
    }

    /**
     * 回款情况
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "report/getpayment", method = RequestMethod.POST)
    public ReportParameterStringDto getPaymentParameter(@RequestBody TimeSelectDto query, HttpServletRequest request,
            HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put(USER_ID, query.getCommonId());
        StringBuffer startDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getStartDate())) {
            startDate.append(query.getStartDate()).append(MIN_TIME);
        } else {
            startDate.append(MIN_DATE);
        }
        StringBuffer endDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getEndDate())) {
            endDate.append(query.getEndDate()).append(MAX_TIME);
        } else {
            endDate.append(MAX_DATE);
        }
        StringBuffer signDateBuffer = new StringBuffer();
        signDateBuffer.append(startDate).append(",").append(endDate);
        filter.put("TRX_DT", signDateBuffer.toString());

        return this.reportService.GetReportParameterService(ReportConsts.RID_PAYMENT, ReportConsts.PAGENAME_COMMON,
                query.getExportFileExt(), query.getParentClientEvent(), query.getEventName(), filter);
    }

    /**
     * 回款情况查看详细-加载页面
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/paymentdetailpage")
    @ResponseStatus(value = HttpStatus.PARTIAL_CONTENT)
    public String renderPaymentDetailPage(HttpServletRequest request, HttpSession session, Model model) {
        model.addAttribute(PAY_STATUS_LIST, getStaticOptions(EPayStatus.class, true));
        return "packet/member_payback_detail";
    }

    /**
     * 回款情况查看详细
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "report/getpaymentdetail", method = RequestMethod.POST)
    public ReportParameterStringDto getPaymentDetailParameter(@RequestBody PaymentDetailDto query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        if (query.getPayStatus() != null && query.getPayStatus() != EPayStatus.NULL) {
            filter.put(PAY_STATUS, query.getPayStatus().getCode());
        }
        if (StringUtils.isNotBlank(query.getKeyWord())) {
            filter.put(KEY_WORD, query.getKeyWord().trim());
        }

        String[] tmp = query.getCommonId().split("-");
        filter.put(USER_ID, tmp[0]);
        filter.put("PRCD_DT", tmp[1]);

        return this.reportService.GetReportParameterService(ReportConsts.RID_PAYMENT_DETAILS,
                ReportConsts.PAGENAME_COMMON, query.getExportFileExt(), query.getParentClientEvent(),
                query.getEventName(), filter);
    }

    /**
     * 申购情况
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "report/getsubscribe", method = RequestMethod.POST)
    public ReportParameterStringDto getSubscribeParameter(@RequestBody TimeSelectDto query, HttpServletRequest request,
            HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put(USER_ID, query.getCommonId());
        StringBuffer startDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getStartDate())) {
            startDate.append(query.getStartDate()).append(MIN_TIME);
        } else {
            startDate.append(MIN_DATE);
        }
        StringBuffer endDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getEndDate())) {
            endDate.append(query.getEndDate()).append(MAX_TIME);
        } else {
            endDate.append(MAX_DATE);
        }
        StringBuffer signDateBuffer = new StringBuffer();
        signDateBuffer.append(startDate).append(",").append(endDate);
        if (StringUtils.isNotBlank(query.getStartDate()) || StringUtils.isNotBlank(query.getEndDate())) {
            filter.put("MAX_TRD_DT", signDateBuffer.toString());
        }

        return this.reportService.GetReportParameterService(ReportConsts.RID_SUBSCRIBE, ReportConsts.PAGENAME_COMMON,
                query.getExportFileExt(), query.getParentClientEvent(), query.getEventName(), filter);
    }

    /**
     * 申购情况详细
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "report/getsubscribedetail", method = RequestMethod.POST)
    public ReportParameterStringDto getSubscribeDetailParameter(@RequestBody CommonParameter query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        String[] tmp = query.getCommonId().split("-");
        filter.put(USER_ID, tmp[0]);
        if (tmp != null && tmp.length > 1) {
            filter.put("SUB_TRD_DT", tmp[1]);
        }

        return this.reportService.GetReportParameterService(ReportConsts.RID_SUBSCRIBE_DETAIL,
                ReportConsts.PAGENAME_COMMON, query.getExportFileExt(), query.getParentClientEvent(),
                query.getEventName(), filter);
    }

    /**
     * 余额信息
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "report/getbalance", method = RequestMethod.POST)
    public ReportParameterStringDto getBalanceParameter(@RequestBody TimeSelectDto query, HttpServletRequest request,
            HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put(USER_ID, query.getCommonId());

        StringBuffer startDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getStartDate())) {
            startDate.append(query.getStartDate()).append(MIN_TIME);
        } else {
            startDate.append(MIN_DATE);
        }
        StringBuffer endDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getEndDate())) {
            endDate.append(query.getEndDate()).append(MAX_TIME);
        } else {
            endDate.append(MAX_DATE);
        }
        StringBuffer signDateBuffer = new StringBuffer();
        signDateBuffer.append(startDate).append(",").append(endDate);
        filter.put("CLEAR_DATE", signDateBuffer.toString());

        return this.reportService.GetReportParameterService(ReportConsts.RID_BANLANCE, ReportConsts.PAGENAME_COMMON,
                query.getExportFileExt(), query.getParentClientEvent(), query.getEventName(), filter);
    }

    /**
     * 风险分析
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "report/getriskanalysis", method = RequestMethod.POST)
    public ReportParameterStringDto getRiskAnalysisParameter(@RequestBody TimeSelectDto query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put(USER_ID, query.getCommonId());

        return this.reportService.GetReportParameterService(ReportConsts.RID_RISK_ANALYSIS,
                ReportConsts.PAGENAME_COMMON, query.getExportFileExt(), query.getParentClientEvent(),
                query.getEventName(), filter);
    }

    /**
     * 投资会员月度理财报告-加载页面
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.QUERY_STATISTICS_MEMBER_MONTH_REPORT_URL)
    @RequiresPermissions(value = Permissions.MEMBER_INVESTOR_MONTH_REPORT)
    public String renderInvestorMonthReportPage(HttpServletRequest request, HttpSession session, Model model) {
        List<EnumOption> enumOptions = getStaticOptions(EApplicationStatus.class, true);
        List<EnumOption> memeberStatusList = new ArrayList<EnumOption>();
        for (EnumOption enumOption : enumOptions) {
            if (EApplicationStatus.valueOf(enumOption.getCode()) == EApplicationStatus.PENDDING
                    || EApplicationStatus.valueOf(enumOption.getCode()) == EApplicationStatus.ACCEPT
                    || EApplicationStatus.valueOf(enumOption.getCode()) == EApplicationStatus.REJECT)
                memeberStatusList.add(enumOption);
        }
        model.addAttribute("memeberStatusList", memeberStatusList);

        return "packet/month_report";
    }

    /**
     * 投资会员月度理财报告
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "report/getinvestormonthreport", method = RequestMethod.POST)
    @RequiresPermissions(value = Permissions.MEMBER_INVESTOR_MONTH_REPORT)
    public ReportParameterStringDto getInvestorMonthReportParameter(@RequestBody InvestorMonthReportDto query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        if (query.getApplicationStatus() != EApplicationStatus.NULL) {
            filter.put("USER_STATUS", query.getApplicationStatus().getCode());
        }
        if (StringUtils.isNotBlank(query.getKeyWord())) {
            filter.put(KEY_WORD, query.getKeyWord().trim());
        }

        return this.reportService.GetReportParameterService(ReportConsts.RID_INVESTOR_MONTH_REPORT,
                ReportConsts.PAGENAME_COMMON, query.getExportFileExt(), query.getParentClientEvent(),
                query.getEventName(), filter);
    }

    /**
     * 月度理财报告-具体会员月度理财报告-加载页面
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/investormonthreportdetailpage")
    @RequiresPermissions(value = Permissions.MEMBER_INVESTOR_MONTH_REPORT)
    @ResponseStatus(value = HttpStatus.PARTIAL_CONTENT)
    public String renderInvestorMonthReportDetailPage(HttpServletRequest request, HttpSession session, Model model) {
        return "packet/month_report_detail";
    }

    /**
     * 月度理财报告-具体会员月度理财报告
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "report/getinvestormonthreportdetail", method = RequestMethod.POST)
    @RequiresPermissions(value = Permissions.MEMBER_INVESTOR_MONTH_REPORT)
    public ReportParameterStringDto getInvestorMonthReportDetailParameter(@RequestBody CommonParameter query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        return this.reportService.GetReportParameterService(ReportConsts.RID_DETAIL,
                ReportConsts.PAGENAME_INVESTOR_MONTH_REPORT_PERSONAL, query.getExportFileExt(),
                query.getParentClientEvent(), query.getEventName(), filter);
    }

    /**
     * 月度理财报告-本月账户情况-加载页面
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/accountmonthreportpage")
    public String renderAccountMonthReportPage(HttpServletRequest request, HttpSession session, Model model) {
        return "";
    }

    /**
     * 月度理财报告-本月账户情况
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "report/getaccountmonthreport", method = RequestMethod.POST)
    public ReportParameterStringDto getAccountMonthReportParameter(@RequestBody CommonParameter query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put(USER_ID, query.getCommonId());

        return this.reportService.GetReportParameterService(ReportConsts.RID_INVESTOR_MONTH_ACCOUNT,
                ReportConsts.PAGENAME_COMMON, query.getExportFileExt(), query.getParentClientEvent(),
                query.getEventName(), filter);
    }

    /**
     * 月度理财报告-账户总体情况统计-加载页面
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/investormonthtotalreportpage")
    public String renderInvestorMonthTotalReportPage(HttpServletRequest request, HttpSession session, Model model) {
        return "";
    }

    /**
     * 月度理财报告-账户总体情况统计
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "report/getinvestormonthtotalreport", method = RequestMethod.POST)
    public ReportParameterStringDto getInvestorMonthTotalReportParameter(@RequestBody CommonParameter query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put(USER_ID, query.getCommonId());

        return this.reportService.GetReportParameterService(ReportConsts.RID_INVESTOR_MONTH_TOTAL_REPORT,
                ReportConsts.PAGENAME_COMMON, query.getExportFileExt(), query.getParentClientEvent(),
                query.getEventName(), filter);
    }

    /**
     * 月度理财报告-风险情况-加载页面
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/investormonthriskpage")
    public String renderInvestorMonthRiskReportPage(HttpServletRequest request, HttpSession session, Model model) {

        return "";
    }

    /**
     * 月度理财报告-风险情况
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "report/getinvestormonthrisk", method = RequestMethod.POST)
    public ReportParameterStringDto getInvestorMonthRiskParameter(@RequestBody CommonParameter query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put(USER_ID, query.getCommonId());

        return this.reportService.GetReportParameterService(ReportConsts.RID_INVESTOR_MONTH_RISK,
                ReportConsts.PAGENAME_COMMON, query.getExportFileExt(), query.getParentClientEvent(),
                query.getEventName(), filter);
    }

    /**
     * 11.4.111.3.1 授权服务中心交易跟踪-加载页面
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.QUERY_STATISTICS_ATHD_CNTL_TRADE_TRACE_URL)
    @RequiresPermissions(value = Permissions.ATHD_CNTL_TRADE_TRACE)
    public String renderAuthorizeTradeTracePage(HttpServletRequest request, HttpSession session, Model model) {
//        Date date = new Date();
//        String dateString = DateUtils.formatDate(date, LiteralConstant.YYYY_MM_DD);
//        model.addAttribute(SELECT_DATE, dateString);
        return "packet/authorization_service_center_transaction";
    }

    /**
     * 11.4.111.3.1 授权服务中心交易跟踪
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "report/getauthorizationtradetrace", method = RequestMethod.POST)
    @RequiresPermissions(value = Permissions.ATHD_CNTL_TRADE_TRACE)
    public ReportParameterStringDto getAuthorizeTradeTraceParameter(@RequestBody TimeSelectDto query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        StringBuffer startDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getStartDate())) {
            startDate.append(query.getStartDate()).append(MIN_TIME);
        } else {
            startDate.append(MIN_DATE);
        }
        StringBuffer endDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getEndDate())) {
            endDate.append(query.getEndDate()).append(MAX_TIME);
        } else {
            endDate.append(MAX_DATE);
        }
        StringBuffer signDateBuffer = new StringBuffer();
        signDateBuffer.append(startDate).append(",").append(endDate);
        filter.put(CREATE_TS, signDateBuffer.toString());

        if (StringUtils.isNotBlank(query.getKeyWord())) {
            filter.put(KEY_WORD, query.getKeyWord().trim());
        }

        if (securityContext.isAuthServiceCenter()) {
            filter.put(AUTHZD_CTR_ID_UPPER, getCurrentUserId());
        }

        return this.reportService.GetReportParameterService(ReportConsts.RID_AUTHORIZE_TRADE_TRACE,
                ReportConsts.PAGENAME_COMMON, query.getExportFileExt(), query.getParentClientEvent(),
                query.getEventName(), filter);
    }

    /**
     * 交易手续费查询结果(管理员)-加载页面
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.QUERY_STATISTICS_TRADE_FEE_QUERY_URL)
    @RequiresPermissions(value = Permissions.MEMBER_TRADE_FEE_QUERY)
    public String renderAuthorizePage(HttpServletRequest request, HttpSession session, Model model) {
        Date date = new Date();
        String dateString = DateUtils.formatDate(date, LiteralConstant.YYYY_MM_DD);
        model.addAttribute(SELECT_DATE, dateString);
        return "packet/transaction_fees_inquiry";
    }

    /**
     * 交易手续费查询结果(管理员)
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "report/getauthorization", method = RequestMethod.POST)
    @RequiresPermissions(value = Permissions.MEMBER_TRADE_FEE_QUERY)
    public ReportParameterStringDto getAuthorizeParameter(@RequestBody TimeSelectDto query, HttpServletRequest request,
            HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        StringBuffer startDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getStartDate())) {
            startDate.append(query.getStartDate()).append(MIN_TIME);
        } else {
            startDate.append(MIN_DATE);
        }
        StringBuffer endDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getEndDate())) {
            endDate.append(query.getEndDate()).append(MAX_TIME);
        } else {
            endDate.append(MAX_DATE);
        }
        StringBuffer signDateBuffer = new StringBuffer();
        signDateBuffer.append(startDate).append(",").append(endDate);
        filter.put(PAY_DT, signDateBuffer.toString());

        if (StringUtils.isNotBlank(query.getKeyWord())) {
            filter.put(KEY_WORD, query.getKeyWord().trim());
        }
        String masks = "o_contact_mobile:1";

        return this.reportService.GetReportParameterService(ReportConsts.RID_AUTHORIZE_TRADE_FEE,
                ReportConsts.PAGENAME_COMMON, query.getExportFileExt(), query.getParentClientEvent(),
                query.getEventName(), filter, masks);
    }

    /**
     * 交易手续费查询结果(管理员)详情
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "report/getauthorizationdetail", method = RequestMethod.POST)
    @RequiresPermissions(value = Permissions.MEMBER_TRADE_FEE_QUERY)
    public ReportParameterStringDto getAuthorizeDetailParameter(@RequestBody TimeSelectDto query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(query.getCommonId())) {
            filter.put(AUTHZD_CTR_ID_UPPER, query.getCommonId());
        }
        StringBuffer startDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getStartDate())) {
            startDate.append(query.getStartDate()).append(MIN_TIME);
        } else {
            startDate.append(MIN_DATE);
        }
        StringBuffer endDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getEndDate())) {
            endDate.append(query.getEndDate()).append(MAX_TIME);
        } else {
            endDate.append(MAX_DATE);
        }
        StringBuffer signDateBuffer = new StringBuffer();
        signDateBuffer.append(startDate).append(",").append(endDate);
        if (StringUtils.isNotBlank(query.getStartDate()) || StringUtils.isNotBlank(query.getEndDate())) {
            filter.put(PAY_DT, signDateBuffer.toString());
        }
        String pageTitle = this.reportService.getUserByUserId(query.getCommonId()).getName() + ": 交易手续费详情";

        return this.reportService.GetReportParameterService(ReportConsts.RID_AUTHORIZE_TRADE_FEE_DETAIL,
                ReportConsts.PAGENAME_COMMON, query.getExportFileExt(), query.getParentClientEvent(),
                query.getEventName(), filter, null, pageTitle);
    }

    /**
     * 交易手续费查询结果(授权服务中心)-加载页面
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.QUERY_STATISTICS_TRADE_FEE_QUERY_FOR_ATHD_URL)
    @RequiresPermissions(value = Permissions.MEMBER_TRADE_FEE_QUERY_FOR_ATHD)
    public String renderAuthorizeSelfPage(HttpServletRequest request, HttpSession session, Model model) {
        Date date = new Date();
        String dateString = DateUtils.formatDate(date, LiteralConstant.YYYY_MM_DD);
        model.addAttribute(SELECT_DATE, dateString);
        return "packet/transaction_fees_authorized";
    }

    /**
     * 交易手续费查询结果(授权服务中心)
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "report/getauthorizationself", method = RequestMethod.POST)
    @RequiresPermissions(value = Permissions.MEMBER_TRADE_FEE_QUERY_FOR_ATHD)
    public ReportParameterStringDto getAuthorizeSelfParameter(@RequestBody TimeSelectDto query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put("AUTHZD_CTR_ID2", getCurrentUserId());
        StringBuffer startDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getStartDate())) {
            startDate.append(query.getStartDate()).append(MIN_TIME);
        } else {
            startDate.append(MIN_DATE);
        }
        StringBuffer endDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getEndDate())) {
            endDate.append(query.getEndDate()).append(MAX_TIME);
        } else {
            endDate.append(MAX_DATE);
        }
        StringBuffer signDateBuffer = new StringBuffer();
        signDateBuffer.append(startDate).append(",").append(endDate);
        if (StringUtils.isNotBlank(query.getStartDate()) || StringUtils.isNotBlank(query.getEndDate())) {
            filter.put(PAY_DT, signDateBuffer.toString());
        }
        String masks = "o_contact_mobile:1";
        String disable = null;
        if (this.securityContext.isAuthServiceCenter()) {
            disable = "PROFIT";
        }

        return this.reportService.GetReportParameterService(ReportConsts.RID_AUTHORIZE_TRADE_FEE_DETAIL_AUTHORIZE,
                ReportConsts.PAGENAME_COMMON, query.getExportFileExt(), query.getParentClientEvent(),
                query.getEventName(), filter, masks, null, null, disable);
    }

    /**
     * 交易量统计表-加载页面
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/tradestatisticspage")
    @ResponseStatus(value = HttpStatus.PARTIAL_CONTENT)
    public String renderTradeStatisticsPage(HttpServletRequest request, HttpSession session, Model model) {
        List<Integer> yearList = new ArrayList<Integer>();
        Date date = new Date();
        String year = DateUtils.formatDate(date, "yyyy");
        for (int i = Integer.valueOf(year); i > YEAR_2010; i--) {
            yearList.add(i);
        }
        model.addAttribute("yearList", yearList);

        return "packet/volume_statistics";
    }

    /**
     * 交易量统计表列表
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/gettradestatistics", method = RequestMethod.POST)
    @ResponseBody
    public ReportParameterStringDto getTradeStatisticsParameter(@RequestBody CommonParameter query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(query.getKeyWord())) {
            filter.put("SUB_DT", query.getKeyWord());
        }
        filter.put(AUTHZD_CTR_ID_UPPER, query.getCommonId());

        return this.reportService.GetReportParameterService(ReportConsts.RID_AUTHORIZE_TRADE_COUNT,
                ReportConsts.PAGENAME_COMMON, query.getExportFileExt(), query.getParentClientEvent(),
                query.getEventName(), filter);
    }

    /**
     * 交易量统计表柱状图
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/gettradestatisticsbar", method = RequestMethod.POST)
    @ResponseBody
    public ReportParameterStringDto getTradeStatisticsBarParameter(@RequestBody CommonParameter query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(query.getKeyWord())) {
            filter.put("SUB_DT", query.getKeyWord());
        }
        filter.put(AUTHZD_CTR_ID_UPPER, query.getCommonId());

        return this.reportService.GetReportParameterService(ReportConsts.RID_AUTHORIZE_TRADE_COUNT_BAR,
                ReportConsts.PAGENAME_BAR, query.getExportFileExt(), query.getParentClientEvent(),
                query.getEventName(), filter);
    }

    /**
     * 还款统计表（应还）-加载页面
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/paymentstatisticspage")
    @ResponseStatus(value = HttpStatus.PARTIAL_CONTENT)
    public String renderPaymentStatisticsPage(HttpServletRequest request, HttpSession session, Model model) {
        model.addAttribute("payDateTypeList", getStaticOptions(EPayDateType.class, true));
        model.addAttribute(PAY_STATUS_LIST, getStaticOptions(EPayStatus.class, true));
        Date date = new Date();
        String dateString = DateUtils.formatDate(date, LiteralConstant.YYYY_MM_DD);
        model.addAttribute(SELECT_DATE, dateString);
        return "packet/payments_statistics";
    }

    /**
     * 还款统计表（应还）
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/getpaymentstatistics", method = RequestMethod.POST)
    @ResponseBody
    public ReportParameterStringDto getPaymentStatisticsParameter(@RequestBody PaymentStatisticsDto query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        StringBuffer startDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getStartDate())) {
            startDate.append(query.getStartDate()).append(MIN_TIME);
        } else {
            startDate.append(MIN_DATE);
        }
        StringBuffer endDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getEndDate())) {
            endDate.append(query.getEndDate()).append(MAX_TIME);
        } else {
            endDate.append(MAX_DATE);
        }
        StringBuffer signDateBuffer = new StringBuffer();
        signDateBuffer.append(startDate).append(",").append(endDate);

        if (query.getPayDateType() == EPayDateType.NEED
                && (StringUtils.isNotBlank(query.getStartDate()) || StringUtils.isNotBlank(query.getEndDate()))) {
        	// 应还
            filter.put(PAY_DT, signDateBuffer.toString()); 
        } else if (StringUtils.isNotBlank(query.getStartDate()) || StringUtils.isNotBlank(query.getEndDate())) {
            filter.put("PD_PRCD_DT", signDateBuffer.toString());
        }
        filter.put(AUTHZD_CTR_ID_UPPER, query.getCommonId());
        if (query.getPayStatus() != null && query.getPayStatus() != EPayStatus.NULL) {
            filter.put("PRCD_MSG", query.getPayStatus().getCode());
        }

        return this.reportService.GetReportParameterService(ReportConsts.RID_PAYMENT_NEED,
                ReportConsts.PAGENAME_COMMON, query.getExportFileExt(), query.getParentClientEvent(),
                query.getEventName(), filter);
    }

    /**
     * 还款统计表（实还）-加载页面
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/realpaymentstatisticspage")
    @ResponseStatus(value = HttpStatus.PARTIAL_CONTENT)
    public String renderRealPaymentStatisticsPage(HttpServletRequest request, HttpSession session, Model model) {
        model.addAttribute("payDateTypeList", getStaticOptions(EPayDateType.class, true));
        model.addAttribute(PAY_STATUS_LIST, getStaticOptions(EPayStatus.class, true));
        Date date = new Date();
        String dateString = DateUtils.formatDate(date, LiteralConstant.YYYY_MM_DD);
        model.addAttribute(SELECT_DATE, dateString);
        return "packet/payments_statistics";
    }

    /**
     * 还款统计表（实还）
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/getrealpaymentstatistics", method = RequestMethod.POST)
    @ResponseBody
    public ReportParameterStringDto getRealPaymentStatisticsParameter(@RequestBody PaymentStatisticsDto query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        StringBuffer startDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getStartDate())) {
            startDate.append(query.getStartDate()).append(MIN_TIME);
        } else {
            startDate.append(MIN_DATE);
        }
        StringBuffer endDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getEndDate())) {
            endDate.append(query.getEndDate()).append(MAX_TIME);
        } else {
            endDate.append(MAX_DATE);
        }
        StringBuffer signDateBuffer = new StringBuffer();
        signDateBuffer.append(startDate).append(",").append(endDate);

        if (query.getPayDateType() == EPayDateType.NEED
                && (StringUtils.isNotBlank(query.getStartDate()) || StringUtils.isNotBlank(query.getEndDate()))) {
        	// 应还
            filter.put(PAY_DT, signDateBuffer.toString()); 
        } else if (StringUtils.isNotBlank(query.getStartDate()) || StringUtils.isNotBlank(query.getEndDate())) {
            filter.put("PD_PRCD_DT", signDateBuffer.toString());
        }
        if (query.getPayStatus() != null && query.getPayStatus() != EPayStatus.NULL) {
            filter.put("PRCD_MSG", query.getPayStatus().getCode());
        }
        filter.put(AUTHZD_CTR_ID_UPPER, query.getCommonId());

        return this.reportService.GetReportParameterService(ReportConsts.RID_PAYMENT_REAL,
                ReportConsts.PAGENAME_COMMON, query.getExportFileExt(), query.getParentClientEvent(),
                query.getEventName(), filter);
    }

    /**
     * 投资会员活跃度-加载页面
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/investoractivitypage")
    @ResponseStatus(value = HttpStatus.PARTIAL_CONTENT)
    public String renderInvestorActivityPage(HttpServletRequest request, HttpSession session, Model model) {
        Date date = new Date();
        String dateString = DateUtils.formatDate(date, LiteralConstant.YYYY_MM_DD);
        model.addAttribute(SELECT_DATE, dateString);
        return "packet/activity_statistics";
    }

    /**
     * 投资会员活跃度_列表
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/getinvestoractivity", method = RequestMethod.POST)
    @ResponseBody
    public ReportParameterStringDto getInvestorActivityParameter(@RequestBody TimeSelectDto query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        StringBuffer startDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getStartDate())) {
            startDate.append(query.getStartDate()).append(MIN_TIME);
        } else {
            startDate.append(MIN_DATE);
        }
        StringBuffer endDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getEndDate())) {
            endDate.append(query.getEndDate()).append(MAX_TIME);
        } else {
            endDate.append(MAX_DATE);
        }
        StringBuffer signDateBuffer = new StringBuffer();
        signDateBuffer.append(startDate).append(",").append(endDate);
        if (StringUtils.isNotBlank(query.getStartDate()) || StringUtils.isNotBlank(query.getEndDate())) {
            filter.put("SUBUSER_DT", signDateBuffer.toString());
        }
        filter.put(AUTHZD_CTR_ID_UPPER, query.getCommonId());

        return this.reportService.GetReportParameterService(ReportConsts.RID_INVESTOR_ACTIVITY,
                ReportConsts.PAGENAME_COMMON, query.getExportFileExt(), query.getParentClientEvent(),
                query.getEventName(), filter);
    }

    /**
     * 投资会员活跃度_饼图
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/getinvestoractivitypie", method = RequestMethod.POST)
    @ResponseBody
    public ReportParameterStringDto getInvestorActivityPieParameter(@RequestBody TimeSelectDto query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        StringBuffer startDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getStartDate())) {
            startDate.append(query.getStartDate()).append(MIN_TIME);
        } else {
            startDate.append(MIN_DATE);
        }
        StringBuffer endDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getEndDate())) {
            endDate.append(query.getEndDate()).append(MAX_TIME);
        } else {
            endDate.append(MAX_DATE);
        }
        StringBuffer signDateBuffer = new StringBuffer();
        signDateBuffer.append(startDate).append(",").append(endDate);
        if (StringUtils.isNotBlank(query.getStartDate()) || StringUtils.isNotBlank(query.getEndDate())) {
            filter.put("SUBUSER_DT", signDateBuffer.toString());
        }
        filter.put(AUTHZD_CTR_ID_UPPER, query.getCommonId());

        return this.reportService.GetReportParameterService(ReportConsts.RID_INVESTOR_ACTIVITY_PIE,
                ReportConsts.PAGENAME_PIE, query.getExportFileExt(), query.getParentClientEvent(),
                query.getEventName(), filter);
    }

    /**
     * 合同模板
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/getcontracttemplate", method = RequestMethod.POST)
    @ResponseBody
    public ReportParameterStringDto getContractTemplateParameter(@RequestBody CommonParameter query,
            HttpServletRequest request, HttpSession session, Model model) {
        String contractId = query.getCommonId();
        String pageName = null;
        if ("6".equals(contractId)) {
            pageName = ReportConsts.PAGENAME_XINGYI_CONTRACT;
        } else if ("4".equals(contractId)) {
            pageName = ReportConsts.PAGENAME_MUTILP_CONTRACT;
        } else if ("1".equals(contractId)) {
            pageName = ReportConsts.PAGENAME_DAILY_INTEREST_CONTRACT; //安益金融借款合同 hui
        } else if ("2".equals(contractId)) {
            pageName = ReportConsts.PAGENAME_COMMON_DAILY_INTEREST_CONTRACT;
        } else if ("3".equals(contractId)) {
            pageName = ReportConsts.PAGENAME_STANDARD_LOAN_CONTRACT;
        } else if ("5".equals(contractId)) {
            pageName = ReportConsts.PAGENAME_XINGYI_PRODUCT_CONTRACT;
        } else if ("7".equals(contractId)) {
        	pageName = ReportConsts.PAGENAME_OVERNIGHT_LOAN_CONTRACT;
        } else if ("8".equals(contractId)) {
            pageName = ReportConsts.PAGENAME_FACTORING_DEDICATE_CONTRACT;
        } else if ("9".equals(contractId)) {
            pageName = ReportConsts.PAGENAME_DAILY_BUSINESS_FACTORING_CONTRACT;
        }else if ("10".equals(contractId)) {
            pageName = ReportConsts.PAGENAME_STANDARD_LOAN_CONTRACT_PRO;
        }
        if(StringUtils.isNotBlank(query.getProductId())){
        	Map<String, Object> filter = new HashMap<String, Object>();
        	filter.put("LOT_ID", null);
            filter.put("PROD_ID", query.getProductId());
            String mask = null;
            return this.reportService.GetReportParameterService(null, pageName, query.getExportFileExt(),
                    query.getParentClientEvent(), query.getEventName(), filter, mask);
        }

        return new ReportParameterStringDto(AppConfigUtil.getReportServerAddr() + pageName);
    }

    /**
     * 合同
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/getcontract", method = RequestMethod.POST)
    @ResponseBody
    public ReportParameterStringDto getContractParameter(@RequestBody CommonParameter query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        String pageName = null;
        BigDecimal contractId = this.reportService.getContractTemplateId(query.getCommonId());
        SimpleLot lot = reportService.getOriginalLotId(query.getCommonId());
        String mask = null;
        if (contractId != null) {
            if (contractId.equals(BigDecimal.valueOf(6))) {
                pageName = ReportConsts.PAGENAME_XINGYI_CONTRACT;
            } else if (contractId.equals(BigDecimal.valueOf(4))) {
                pageName = ReportConsts.PAGENAME_MUTILP_CONTRACT;
            } else if (contractId.equals(BigDecimal.valueOf(1))) {
                pageName = ReportConsts.PAGENAME_DAILY_INTEREST_CONTRACT;
            } else if (contractId.equals(BigDecimal.valueOf(2))) {
                pageName = ReportConsts.PAGENAME_COMMON_DAILY_INTEREST_CONTRACT;
            } else if (contractId.equals(BigDecimal.valueOf(3))) {
                pageName = ReportConsts.PAGENAME_STANDARD_LOAN_CONTRACT;
            } else if (contractId.equals(BigDecimal.valueOf(5))) {
                pageName = ReportConsts.PAGENAME_XINGYI_PRODUCT_CONTRACT;
            } else if (contractId.equals(BigDecimal.valueOf(7))) {
                pageName = ReportConsts.PAGENAME_OVERNIGHT_LOAN_CONTRACT;
            } else if (contractId.equals(BigDecimal.valueOf(8))) {
                pageName = ReportConsts.PAGENAME_FACTORING_DEDICATE_CONTRACT;
            } else if (contractId.equals(BigDecimal.valueOf(9))) {
                pageName = ReportConsts.PAGENAME_DAILY_BUSINESS_FACTORING_CONTRACT;
            } else if (contractId.equals(BigDecimal.valueOf(10))) {
                pageName = ReportConsts.PAGENAME_STANDARD_LOAN_CONTRACT_PRO;
            } else {
                return reportService.getLegacyContractReport(lot.getLotId());
            }

            if (this.securityContext.hasPermission(Permissions.RISK_CONTROL_DEPT)
                    || this.securityContext.hasPermission(Permissions.SETTLEMENT_DEMPARTMENT)
                    || this.securityContext.hasPermission(Permissions.TRANSACTION_DEPT)) {
                mask = "IDCARD_LENDER:2, IDCARD_BORROWER:2";
            }
        }
        filter.put("PROD_ID", query.getProductId());
        filter.put("LOT_ID", lot.getLotId());
        if (!query.getCommonId().equals(lot.getLotId())) {
        	// 发生过转让
        	mask = "IDCARD_LENDER:2, USER_LENDER:0";
        }
        return this.reportService.GetReportParameterService(null, pageName, query.getExportFileExt(),
                query.getParentClientEvent(), query.getEventName(), filter, mask);
    }

    /**
     * 信息服务费查询表-加载页面
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.QUERY_STATISTICS_INFO_SERVICE_FEE_QUERY_URL)
    @RequiresPermissions(value = Permissions.INFO_SERVICE_FEE_QUERY)
    public String renderServiceFeePage(HttpServletRequest request, HttpSession session, Model model) {
        Date date = new Date();
        String dateString = DateUtils.formatDate(date, LiteralConstant.YYYY_MM_DD);
        model.addAttribute(SELECT_DATE, dateString);
        return "packet/servicefee";
    }

    /**
     * 信息服务费查询表
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/getservicefee", method = RequestMethod.POST)
    @ResponseBody
    public ReportParameterStringDto getServiceFeeParameter(@RequestBody TimeSelectDto query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        if (this.securityContext.isAuthServiceCenter()) {
            filter.put(USER_ID, getCurrentUserId());
        }
        StringBuffer startDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getStartDate())) {
            startDate.append(query.getStartDate()).append(MIN_TIME);
        } else {
            startDate.append(MIN_DATE);
        }
        StringBuffer endDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getEndDate())) {
            endDate.append(query.getEndDate()).append(MAX_TIME);
        } else {
            endDate.append(MAX_DATE);
        }
        StringBuffer signDateBuffer = new StringBuffer();
        signDateBuffer.append(startDate).append(",").append(endDate);
        if (StringUtils.isNotBlank(query.getStartDate()) || StringUtils.isNotBlank(query.getEndDate())) {
            filter.put("CREATE_TIME", signDateBuffer.toString());
        }
        if (StringUtils.isNotBlank(query.getKeyWord())) {
            filter.put(KEY_WORD, query.getKeyWord().trim());
        }

        return this.reportService.GetReportParameterService(ReportConsts.RID_SERVICE_FEE, ReportConsts.PAGENAME_COMMON,
                query.getExportFileExt(), query.getParentClientEvent(), query.getEventName(), filter);
    }

    /**
     * 信息服务费详细账单-加载页面
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/servicefeedetailpage")
    @ResponseStatus(value = HttpStatus.PARTIAL_CONTENT)
    public String renderServiceFeeDetailPage(HttpServletRequest request, HttpSession session, Model model) {
        List<EnumOption> enumOptions = getStaticOptions(EFeePayMethodType.class, true);
        List<EnumOption> enumOptions2 = new ArrayList<EnumOption>();
        for (EnumOption enumOption : enumOptions) {
            if (EFeePayMethodType.valueOf(enumOption.getCode()) != EFeePayMethodType.DAY) {
                if (EFeePayMethodType.valueOf(enumOption.getCode()) == EFeePayMethodType.MONTH) {
                    enumOption.setText("按期");
                }
                enumOptions2.add(enumOption);
            }
        }

        Date date = new Date();
        String dateString = DateUtils.formatDate(date, "yyyy-MM");
        model.addAttribute(SELECT_DATE, dateString);
        model.addAttribute("payMethodList", enumOptions2);

        return "packet/servicefee_detail";
    }

    /**
     * 信息服务费详细账单
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/getservicefeedetail", method = RequestMethod.POST)
    @ResponseBody
    public ReportParameterStringDto getServiceFeeDetailParameter(@RequestBody ServiceFeeDto query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(query.getKeyWord())) {
            filter.put(KEY_WORD, query.getKeyWord().trim());
        }
        filter.put(AUTHZD_CTR_ID_UPPER, query.getCommonId());
        if (StringUtils.isNotBlank(query.getSelectMonth())) {
            String[] yyyymm = query.getSelectMonth().split("-");
            if (yyyymm != null && yyyymm.length == 2 && StringUtils.isNotBlank(yyyymm[0])) {
                filter.put(PAY_DT, yyyymm[0] + yyyymm[1]);
            }
        }
        if (query.getFeePayMethodType() != null && query.getFeePayMethodType() != EFeePayMethodType.NULL) {
            filter.put("IS_INSTALLMENT", query.getFeePayMethodType().getCode());
        }
        if (StringUtils.isNotBlank(query.getTerm())) {
            String[] tmp = query.getTerm().split("-");
            if (tmp.length == 1) {
                filter.put("TERM_TYPE", tmp[0]);
            } else {
                filter.put("TERM_LENGTH", tmp[0]);
                filter.put("TERM_TYPE", tmp[1]);
            }
        }
        String fixed = "4";
        return this.reportService.GetReportParameterService(ReportConsts.RID_SERVICE_FEE_DETAIL,
                ReportConsts.PAGENAME_SERVICE_FEE_DETAIL, query.getExportFileExt(), query.getParentClientEvent(),
                query.getEventName(), filter, null, null, fixed);
    }

    /**
     * 会员投资统计表(按投资人--按融资包)-加载页面
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.QUERY_STATISTICS_MEMBER_INVESTE_STATISTICS_URL)
    @RequiresPermissions(value = Permissions.MEMBER_INVESTMENT_STATISTICS)
    public String renderMemberStatisticsPage(HttpServletRequest request, HttpSession session, Model model) {
        model.addAttribute("memberTypeList", getStaticOptions(EMemberStatisticsType.class, false));
        model.addAttribute("payMethodList", getStaticOptions(ETimeType.class, false));
        Date date = new Date();
        String dateString = DateUtils.formatDate(date, LiteralConstant.YYYY_MM_DD);
        model.addAttribute(SELECT_DATE, dateString);
        return "packet/member_investment_statistics";
    }

    /**
     * 会员投资统计表(按投资人--按融资包)
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/getmemberstatistics", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = Permissions.MEMBER_INVESTMENT_STATISTICS)
    public ReportParameterStringDto getMemberStatisticsParameter(@RequestBody MemberStatisticsDto query,
            HttpServletRequest request, HttpSession session, Model model) {
        String reportId = null;
        Map<String, Object> filter = new HashMap<String, Object>();
        StringBuffer startDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getStartDate())) {
            startDate.append(query.getStartDate()).append(MIN_TIME);
        } else {
            startDate.append(MIN_DATE);
        }
        StringBuffer endDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getEndDate())) {
            endDate.append(query.getEndDate()).append(MAX_TIME);
        } else {
            endDate.append(MAX_DATE);
        }
        StringBuffer signDateBuffer = new StringBuffer();
        signDateBuffer.append(startDate).append(",").append(endDate);

        if (query.getMemberType() == EMemberStatisticsType.FINANCIER) {
            reportId = ReportConsts.RID_MEMBER_STATISTICS_BY_INVERSTOR;
            filter.put("SIGNED_DT", signDateBuffer.toString());

            if (this.securityContext.isAuthServiceCenter()) {
                String athdCenterId = getCurrentUserId();
                User athdUser = userService.getUserByUserId(athdCenterId);

                if (athdUser != null && StringUtils.isNotBlank(athdUser.getOwnerId())) {
                    filter.put(AUTHZD_CTR_ID_LOWER, athdUser.getOwnerId());
                } else {
                    filter.put(AUTHZD_CTR_ID_LOWER, athdCenterId);
                }
            } else {
                filter.put(AUTHZD_CTR_ID_LOWER, "");
            }
        } else {
            filter.put(SIGNING_TS, signDateBuffer.toString());

            if (this.securityContext.isAuthServiceCenter()) {
                String athd_center_id = getCurrentUserId();
                User athdUser = userService.getUserByUserId(athd_center_id);

                if (athdUser != null && StringUtils.isNotBlank(athdUser.getOwnerId())) {
                    filter.put(AUTHZD_CTR_ID_LOWER, athdUser.getOwnerId());
                } else {
                    filter.put(AUTHZD_CTR_ID_LOWER, athd_center_id);
                }
            } else {
                filter.put(AUTHZD_CTR_ID_LOWER, "");
            }

            reportId = ReportConsts.RID_MEMBER_STATISTICS_BY_PACKAGE;
        }

        if (StringUtils.isNotBlank(query.getKeyWord())) {
            filter.put(KEY_WORD, query.getKeyWord().trim());
        }
        String specialText = null;
        if (securityContext.isAuthServiceCenter()) {
            specialText = "PKG_NAME:";
            if (StringUtils.isNotBlank(query.getEventName())) {
                String eventName = query.getEventName();
                String[] split = eventName.split(",");
                if (split.length == 2) {
                    query.setEventName(split[1]);
                }
            }
        }
        return this.reportService.GetReportParameterService(reportId, ReportConsts.PAGENAME_PAGING,
                query.getExportFileExt(), query.getParentClientEvent(), query.getEventName(), filter, null, null, null,
                null, specialText);
    }

    /**
     * 会员融资统计表详情(按投资人)-加载页面
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/memberstatisticsinvestorpage")
    @RequiresPermissions(value = Permissions.MEMBER_INVESTMENT_STATISTICS)
    @ResponseStatus(value = HttpStatus.PARTIAL_CONTENT)
    public String renderMemberStatisticsInvestorPage(HttpServletRequest request, HttpSession session, Model model) {
        return "packet/member_statistics_detail_ins";
    }

    /**
     * 会员融资统计表详情(按投资人)
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/getmemberstatisticsinvestor", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = Permissions.MEMBER_INVESTMENT_STATISTICS)
    public ReportParameterStringDto getMemberStatisticsInvestorParameter(@RequestBody MemberStatisticsDto query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(query.getKeyWord())) {
            filter.put(PKG_ID, query.getKeyWord().trim());
        }
        if (StringUtils.isNotBlank(query.getCommonId())) {
            String[] split = query.getCommonId().split("-");
            if (split != null && split.length > 1) {
                filter.put(USER_ID, split[1]);
                if (securityContext.isAuthServiceCenter()) {
                    filter.put(AUTHZD_CTR_ID_UPPER, split[0]);
                }
            }
        }

        StringBuffer startDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getStartDate())) {
            startDate.append(query.getStartDate()).append(MIN_TIME);
        } else {
            startDate.append(MIN_DATE);
        }
        StringBuffer endDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getEndDate())) {
            endDate.append(query.getEndDate()).append(MAX_TIME);
        } else {
            endDate.append(MAX_DATE);
        }
        StringBuffer signDateBuffer = new StringBuffer();
        signDateBuffer.append(startDate).append(",").append(endDate);
        filter.put("SIGNED_DT", signDateBuffer.toString());

        return this.reportService.GetReportParameterService(ReportConsts.RID_MEMBER_STATISTICS_BY_INVERSTOR_DETAIL,
                ReportConsts.PAGENAME_COMMON, query.getExportFileExt(), query.getParentClientEvent(),
                query.getEventName(), filter);
    }

    /**
     * 会员融资统计表详情(按投融资包)-加载页面
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/memberstatisticspackagepage")
    @RequiresPermissions(value = Permissions.MEMBER_INVESTMENT_STATISTICS)
    @ResponseStatus(value = HttpStatus.PARTIAL_CONTENT)
    public String renderMemberStatisticsPackagePage(HttpServletRequest request, HttpSession session, Model model) {
        return "packet/member_statistics_detail_pac";
    }

    /**
     * 会员融资统计表详情(按融资包)
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/getmemberstatisticspackage", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = Permissions.MEMBER_INVESTMENT_STATISTICS)
    public ReportParameterStringDto getMemberStatisticsPackageParameter(@RequestBody MemberStatisticsDto query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(query.getKeyWord())) {
            filter.put("ACCT_NO", query.getKeyWord().trim());
        }
        if (StringUtils.isNotBlank(query.getCommonId())) {
            String[] split = query.getCommonId().split("-");
            if (split != null && split.length > 1) {
                filter.put(PKG_ID, split[1]);
                if (securityContext.isAuthServiceCenter()) {
                    filter.put(AUTHZD_CTR_ID_UPPER, split[0]);
                } else {
                    filter.put(AUTHZD_CTR_ID_UPPER, "");
                }
            }
        }

        return this.reportService.GetReportParameterService(ReportConsts.RID_MEMBER_STATISTICS_BY_PACKAGE_DETAIL,
                ReportConsts.PAGENAME_COMMON, query.getExportFileExt(), query.getParentClientEvent(),
                query.getEventName(), filter);
    }

    /**
     * 融资包统计-加载页面
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.QUERY_STATISTICS_FINANCING_PACKAGE_STATISTICS_URL)
    @RequiresPermissions(value = Permissions.FINANCING_PACKAGE_STATISTICS)
    public String renderPackageStatisticsPage(HttpServletRequest request, HttpSession session, Model model) {
        Date date = new Date();
        String dateString = DateUtils.formatDate(date, LiteralConstant.YYYY_MM_DD);
        model.addAttribute(SELECT_DATE, dateString);
        return "packet/package_statistics";
    }

    /**
     * 11.4.311.3.3 融资包统计
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/getpackagestatistics", method = RequestMethod.POST)
    @ResponseBody
    public ReportParameterStringDto getPackageStatisticsParameter(@RequestBody AnotherTimeSelectDto query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        String fixed = "1";

        if (StringUtils.isNotBlank(query.getKeyWord())) {
            filter.put(KEY_WORD, query.getKeyWord());
        }
        StringBuffer startDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getStartDate())) {
            startDate.append(query.getStartDate()).append(MIN_TIME);
        } else {
            startDate.append(MIN_DATE);
        }
        StringBuffer endDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getEndDate())) {
            endDate.append(query.getEndDate()).append(MAX_TIME);
        } else {
            endDate.append(MAX_DATE);
        }
        StringBuffer dateBuffer = new StringBuffer();
        dateBuffer.append(startDate).append(",").append(endDate);
        filter.put("SUBS_START_TS", dateBuffer.toString());

        StringBuffer startSignDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getSingStartDate())) {
            startSignDate.append(query.getSingStartDate()).append(MIN_TIME);
        } else {
            startSignDate.append(MIN_DATE);
        }
        StringBuffer endSignDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getSignEndDate())) {
            endSignDate.append(query.getSignEndDate()).append(MAX_TIME);
        } else {
            endSignDate.append(MAX_DATE);
        }
        StringBuffer signDateBuffer = new StringBuffer();
        signDateBuffer.append(startSignDate).append(",").append(endSignDate);
        filter.put("fucr_signing_ts", signDateBuffer.toString());

        return this.reportService.GetReportParameterService(ReportConsts.RID_PACKAGE_STATISTICS,
                ReportConsts.PAGENAME_COMMON, query.getExportFileExt(), query.getParentClientEvent(),
                query.getEventName(), filter, null, null, fixed);
    }

    /**
     * 11.1.5 近期还款计划-加载页面
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.QUERY_STATISTICS_RECENT_PAYMENT_SCHEDULE_URL)
    @RequiresPermissions(value = Permissions.RECENT_PAYMENT_SCHEDULE)
    public String renderRecentRepaymentSchedule(HttpServletRequest request, HttpSession session, Model model) {
        Date date = new Date();
        String dateString = DateUtils.formatDate(date, LiteralConstant.YYYY_MM_DD);

        List<EnumOption> enumOptions = new ArrayList<EnumOption>();
        for (EnumOption enumOption : getStaticOptions(EPayStatus.class, false)) {
            if (EPayStatus.valueOf(enumOption.getCode()) == EPayStatus.BADDEBT) {
                continue;
            }
            enumOptions.add(enumOption);
        }

        enumOptions.add(new EnumOption("BADDEBT", "待追偿"));
        model.addAttribute(PAY_STATUS_LIST, enumOptions);
        model.addAttribute(SELECT_DATE, dateString);
        return "packet/recent_repayment_schedule";
    }

    /**
     * 11.1.5 近期还款计划
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/getrecentrepaymentschedule", method = RequestMethod.POST)
    @ResponseBody
    public ReportParameterStringDto getRecentRepaymentScheduleParameter(@RequestBody RecentPaymentDto query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(query.getKeyWord())) {
            filter.put(KEY_WORD, query.getKeyWord().trim());
        }
        StringBuffer startDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getStartDate())) {
            startDate.append(query.getStartDate()).append(MIN_TIME);
        } else {
            startDate.append(MIN_DATE);
        }
        StringBuffer endDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getEndDate())) {
            endDate.append(query.getEndDate()).append(MAX_TIME);
        } else {
            endDate.append(MAX_DATE);
        }
        StringBuffer signDateBuffer = new StringBuffer();
        signDateBuffer.append(startDate).append(",").append(endDate);
        if (StringUtils.isNotBlank(query.getStartDate()) || StringUtils.isNotBlank(query.getEndDate())) {
            filter.put(PAY_DT, signDateBuffer.toString());
        }
        
        if (query.getPayStatus() != EPayStatus.NULL && query.getPayStatus() != null) {
            filter.put(PAY_STATUS, query.getPayStatus().getCode());
        }

        String masks = "FNCR_NAME:0,BNK_ACCT_NO:2";
        if (this.securityContext.hasPermission(Permissions.SETTLEMENT_DEMPARTMENT)) {
            masks = null;
        }
        if (this.securityContext.hasPermission(Permissions.RISK_CONTROL_DEPT)) {
            masks = "BNK_ACCT_NO:2";
        }

        StringBuffer startDate2 = new StringBuffer();
        if (StringUtils.isNotBlank(query.getSigningStartDate())) {
            startDate2.append(query.getSigningStartDate()).append(MIN_TIME);
        } else {
            startDate2.append(MIN_DATE);
        }
        StringBuffer endDate2 = new StringBuffer();
        if (StringUtils.isNotBlank(query.getSigningEndDate())) {
            endDate2.append(query.getSigningEndDate()).append(MAX_TIME);
        } else {
            endDate2.append(MAX_DATE);
        }
        StringBuffer signDateBuffer2 = new StringBuffer();
        signDateBuffer2.append(startDate2).append(",").append(endDate2);
        
        if (StringUtils.isNotBlank(query.getSigningStartDate()) || StringUtils.isNotBlank(query.getSigningEndDate())) {
			filter.put("SIGNING_TS", signDateBuffer2);
		}

        String fixed = "2";

        return this.reportService.GetReportParameterService(ReportConsts.RID_RECENT_PAYMENT_SCHEDULE,
                ReportConsts.PAGENAME_COMMON, query.getExportFileExt(), query.getParentClientEvent(),
                query.getEventName(), filter, masks, null, fixed);
    }

    /**
     * 11.5.111.4.1 会员基本信息查询-加载页面
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.QUERY_STATISTICS_MEMBER_BASIC_INFO_QUERY_URL)
    @RequiresPermissions(value = Permissions.MEMBER_BASIC_INFO_QUERY)
    public String renderMemberInfoPage(HttpServletRequest request, HttpSession session, Model model) {
        model.addAttribute("memeberTypeList", getStaticOptions(com.hengxin.platform.report.enums.EMemberType.class, true));
        model.addAttribute("provinceList", getDynamicOptions(EOptionCategory.REGION, true));
        model.addAttribute("memeberLevelList", getStaticOptions(EMemeberLevel.class, true));
        Date date = new Date();
        String dateString = DateUtils.formatDate(date, LiteralConstant.YYYY_MM_DD);
        model.addAttribute(SELECT_DATE, dateString);
        return "packet/member_basicinfo_detail";
    }

    /**
     * 11.5.111.4.1 会员基本信息查询.
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/getmemberInfo", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = Permissions.MEMBER_BASIC_INFO_QUERY)
    public ReportParameterStringDto getMemberInfoParameter(@RequestBody MemberInfoDto query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(query.getKeyWord())) {
            filter.put(KEY_WORD, query.getKeyWord().trim());
        }
        if (StringUtils.equals(query.getStatus(), "NORMAL")) {
            filter.put("UM_APPR_STATUS", "A");
        }
        if (query.getMemberType() != com.hengxin.platform.report.enums.EMemberType.NULL && query.getMemberType() != null) {
            filter.put("user_type", query.getMemberType().getCode());
        }
        if (StringUtils.isNotBlank(query.getRegionCode())) {
            filter.put("mbq_region_code", query.getRegionCode());
        }
        StringBuffer startDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getStartDate())) {
            startDate.append(query.getStartDate()).append(MIN_TIME);
        } else {
            startDate.append(MIN_DATE);
        }
        StringBuffer endDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getEndDate())) {
            endDate.append(query.getEndDate()).append(MAX_TIME);
        } else {
            endDate.append(MAX_DATE);
        }

        if (StringUtils.isNotBlank(query.getIsSign())) {
            filter.put("signed_flg", query.getIsSign());
        }

        String disbale = null;
        String specialText = null;
        String masks = "mbq_id_card_no:2,mbq_bnk_no:2,mbq_name:0";
        if (securityContext.hasPermission(Permissions.RISK_CONTROL_DEPT)) {
            disbale = "MBQ_URL_DETAIL";
            masks = "mbq_id_card_no:2,mbq_bnk_no:2";
        } else if (securityContext.hasPermission(Permissions.TRANSACTION_DEPT)) {
            specialText = "MBQ_URL_DETAIL:详细信息";
            masks = "mbq_id_card_no:2";
        } else if (securityContext.hasPermission(Permissions.SETTLEMENT_DEMPARTMENT)) {
            specialText = "MBQ_URL_DETAIL:结算信息";
            masks = "mbq_id_card_no:2";
        }
        StringBuffer signDateBuffer = new StringBuffer();
        signDateBuffer.append(startDate).append(",").append(endDate);
        filter.put("mbq_create_ts", signDateBuffer.toString());
        String fixed = "2";

        return this.reportService.GetReportParameterService(ReportConsts.RID_MEMBER_INFO, ReportConsts.PAGENAME_PAGING,
                query.getExportFileExt(), query.getParentClientEvent(), query.getEventName(), filter, masks, null,
                fixed, disbale, specialText);
    }

    /**
     * 11.5.111.4.1 会员基本信息查询详情
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/getmemberInfodetail", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = Permissions.MEMBER_BASIC_INFO_QUERY)
    public ReportParameterStringDto getMemberInfoDetailParameter(@RequestBody MemberInfoDto query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        String userId = "";
        String[] commonIds = query.getCommonId().split("-");
        if (commonIds != null && commonIds.length > 1) {
            userId = commonIds[0];
            filter.put(USER_ID, commonIds[0]);
            filter.put("USER_TYPE", commonIds[1]);
        }

        String rid = null;
        String pageName = null;
        String pageTitle = this.reportService.getUserByUserId(userId).getName();
        String mask = null;

        if (securityContext.hasPermission(Permissions.TRANSACTION_DEPT)
                || securityContext.hasPermission(Permissions.CUSTOMER_SERVICE_DEPT) || securityContext.isProdDeptUser()) {
            if (this.authorizationService.isInvestor(userId)) {
                pageName = ReportConsts.PAGENAME_MEMBER_INFO_DETAIL_TRAN_INVESTOR;
                rid = ReportConsts.RID_MEMBER_INFO_DETAIL_TRAN_INVESTOR;
                mask = "mdq1_id_card_no:2";
            } else if (this.authorizationService.isFinancier(userId)) {
                pageName = ReportConsts.PAGENAME_MEMBER_INFO_DETAIL_TRAN_FINANCIER;
                rid = ReportConsts.RID_MEMBER_INFO_DETAIL_TRAN_FINANCIER;
                mask = "mdq3_id_card_no:2";
            } else if (this.authorizationService.isAthdServCenter(userId)
                    && this.authorizationService.isProdServ(userId)) {
                pageName = ReportConsts.PAGENAME_MEMBER_INFO_DETAIL_ATHD_AND_SERV;
                rid = ReportConsts.RID_MEMBER_INFO_DETAIL_TRAN_FINANCIER;
                mask = "mdq3_id_card_no:2,O_REP_ID_CARD_NO:2";
            } else if (this.authorizationService.isProdServ(userId)) {
                pageName = ReportConsts.PAGENAME_MEMBER_INFO_DETAIL_PROD_SERV;
                rid = ReportConsts.RID_MEMBER_INFO_DETAIL_TRAN_FINANCIER;
                mask = "mdq3_id_card_no:2,O_REP_ID_CARD_NO:2";
            } else if (this.authorizationService.isAthdServCenter(userId)) {
                pageName = ReportConsts.PAGENAME_MEMBER_INFO_DETAIL_ATHD_CENTER;
                rid = ReportConsts.RID_MEMBER_INFO_DETAIL_TRAN_FINANCIER;
                mask = "mdq3_id_card_no:2,O_REP_ID_CARD_NO:2";
            } else if (this.authorizationService.isTourist(userId)) {
                pageName = ReportConsts.PAGENAME_MEMBER_INFO_DETAIL_TRAN_INVESTOR;
                rid = ReportConsts.RID_MEMBER_INFO_DETAIL_TRAN_INVESTOR;
                mask = "mdq1_id_card_no:2";
            } else if (this.authorizationService.isAgencyTourist(userId)) {
                pageName = ReportConsts.PAGENAME_MEMBER_INFO_DETAIL_PROD_SERV;
                rid = ReportConsts.RID_MEMBER_INFO_DETAIL_TRAN_INVESTOR;
                mask = "mdq1_id_card_no:2";
            }

            if (securityContext.hasPermission(Permissions.CUSTOMER_SERVICE_DEPT) || securityContext.isProdDeptUser()) {
                mask = null;
            }
            pageTitle = pageTitle + ":会员详细信息";
        } else if (securityContext.hasPermission(Permissions.SETTLEMENT_DEMPARTMENT)) {
            if (this.authorizationService.isInvestor(userId)) {
                pageName = ReportConsts.PAGENAME_MEMBER_INFO_DETAIL_SETTLE_INVESTOR;
                rid = ReportConsts.RID_MEMBER_INFO_DETAIL_SETTLE_INVESTOR;
            } else if (this.authorizationService.isFinancier(userId)) {
                pageName = ReportConsts.PAGENAME_MEMBER_INFO_DETAIL_SETTLE_FINANCIER;
                rid = ReportConsts.RID_MEMBER_INFO_DETAIL_SETTLE_FINANCIER;
            } else if (this.authorizationService.isAthdServCenter(userId)
                    && this.authorizationService.isProdServ(userId)) {
                pageName = ReportConsts.PAGENAME_MEMBER_INFO_DETAIL_ATHD_AND_SERV;
                rid = ReportConsts.RID_MEMBER_INFO_DETAIL_TRAN_FINANCIER;
                mask = "mdq3_id_card_no:2";
            } else if (this.authorizationService.isProdServ(userId)) {
                pageName = ReportConsts.PAGENAME_MEMBER_INFO_DETAIL_PROD_SERV;
                rid = ReportConsts.RID_MEMBER_INFO_DETAIL_TRAN_FINANCIER;
                mask = "mdq3_id_card_no:2";
            } else if (this.authorizationService.isAthdServCenter(userId)) {
                pageName = ReportConsts.PAGENAME_MEMBER_INFO_DETAIL_ATHD_CENTER;
                rid = ReportConsts.RID_MEMBER_INFO_DETAIL_TRAN_FINANCIER;
                mask = "mdq3_id_card_no:2";
            } else if (this.authorizationService.isTourist(userId)) {
                pageName = ReportConsts.PAGENAME_MEMBER_INFO_DETAIL_TRAN_INVESTOR;
                rid = ReportConsts.RID_MEMBER_INFO_DETAIL_TRAN_INVESTOR;
                mask = "mdq1_id_card_no:2";
            } else if (this.authorizationService.isAgencyTourist(userId)) {
                pageName = ReportConsts.PAGENAME_MEMBER_INFO_DETAIL_PROD_SERV;
                rid = ReportConsts.RID_MEMBER_INFO_DETAIL_TRAN_INVESTOR;
                mask = "mdq1_id_card_no:2";
            }
            pageTitle = pageTitle + ":会员结算信息";
        }

        return this.reportService.GetReportParameterService(rid, pageName, query.getExportFileExt(),
                query.getParentClientEvent(), query.getEventName(), filter, mask, pageTitle);
    }

    /**
     * 11.4.611.3.4 会员审核查询-加载页面
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.QUERY_STATISTICS_MEMBER_APPROVE_QUERY_URL)
    @RequiresPermissions(value = { Permissions.MEMBER_APPROVE_QUERY })
    public String renderMemberApproveInfoPage(HttpServletRequest request, HttpSession session, Model model) {
        Date date = new Date();
        String dateString = DateUtils.formatDate(date, LiteralConstant.YYYY_MM_DD);
        model.addAttribute(SELECT_DATE, dateString);
        return "packet/member_audit_search";
    }

    /**
     * 11.4.611.3.4 会员审核查询
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/getmemberapproveInfo", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = { Permissions.MEMBER_APPROVE_QUERY })
    public ReportParameterStringDto getMemberApproveInfoParameter(@RequestBody TimeSelectDto query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(query.getKeyWord())) {
            filter.put(KEY_WORD, query.getKeyWord().trim());
        }
        StringBuffer startDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getStartDate())) {
            startDate.append(query.getStartDate()).append(MIN_TIME);
        } else {
            startDate.append(MIN_DATE);
        }
        StringBuffer endDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getEndDate())) {
            endDate.append(query.getEndDate()).append(MAX_TIME);
        } else {
            endDate.append(MAX_DATE);
        }
        StringBuffer signDateBuffer = new StringBuffer();
        signDateBuffer.append(startDate).append(",").append(endDate);
        filter.put("submit_create_ts", signDateBuffer.toString());

        return this.reportService.GetReportParameterService(ReportConsts.RID_MEMBER_APPROVE_INFO,
                ReportConsts.PAGENAME_PAGING, query.getExportFileExt(), query.getParentClientEvent(),
                query.getEventName(), filter);
    }

    /**
     * 11.4.711.3.5 融资费用统计-加载页面
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.QUERY_STATISTICS_FINANCE_FEE_QUERY_URL)
    @RequiresPermissions(value = Permissions.FINANCING_FEE_QUERY)
    public String renderFinanceFeeStatisticsPage(HttpServletRequest request, HttpSession session, Model model) {
        Date date = new Date();
        String dateString = DateUtils.formatDate(date, LiteralConstant.YYYY_MM_DD);
        model.addAttribute(SELECT_DATE, dateString);
        return "packet/financing_cost_statistics";
    }

    /**
     * 11.4.711.3.5 融资费用统计
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/getfinancefeestatistics", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = Permissions.FINANCING_FEE_QUERY)
    public ReportParameterStringDto getFinanceFeeStatisticsParameter(@RequestBody TimeSelectDto query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(query.getKeyWord())) {
            filter.put(KEY_WORD, query.getKeyWord().trim());
        }
        if (this.securityContext.isAuthServiceCenter()) {
            if (this.securityContext.isAuthServiceCenter()) {
                String athd_center_id = getCurrentUserId();
                User athdUser = userService.getUserByUserId(athd_center_id);

                if (athdUser != null && StringUtils.isNotBlank(athdUser.getOwnerId())) {
                    filter.put(AUTHZD_CTR_ID_UPPER, athdUser.getOwnerId());
                } else {
                    filter.put(AUTHZD_CTR_ID_UPPER, athd_center_id);
                }
            }
        } else {
            filter.put(AUTHZD_CTR_ID_UPPER, "");
        }
        StringBuffer startDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getStartDate())) {
            startDate.append(query.getStartDate()).append(MIN_TIME);
        } else {
            startDate.append(MIN_DATE);
        }
        StringBuffer endDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getEndDate())) {
            endDate.append(query.getEndDate()).append(MAX_TIME);
        } else {
            endDate.append(MAX_DATE);
        }
        StringBuffer signDateBuffer = new StringBuffer();
        signDateBuffer.append(startDate).append(",").append(endDate);
        if (StringUtils.isNotBlank(query.getStartDate()) || StringUtils.isNotBlank(query.getEndDate())) {
            filter.put(SIGNING_TS, signDateBuffer.toString());
        }
        String fixed = "4";
        String specialText = null;
        String disable = null;
        if (securityContext.isAuthServiceCenter()) {
            disable = "WRTR_MRGN_AMT,SERV_MRGN_AMT,SEAT_FEE_AMT,PD_RISK_FEE";
            specialText = "PKG_NAME:";
            if (StringUtils.isNotBlank(query.getEventName())) {
                String eventName = query.getEventName();
                String[] split = eventName.split(",");
                if (split.length == 2) {
                    query.setEventName(split[1]);
                }
            }
        }

        return this.reportService.GetReportParameterService(ReportConsts.RID_FINANCE_FEE_STATISTICS,
                ReportConsts.PAGENAME_COMMON, query.getExportFileExt(), query.getParentClientEvent(),
                query.getEventName(), filter, null, null, fixed, disable, specialText);
    }

    /**
     * 11.4.811.3.6 融资费用收入明细 -加载页面
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.QUERY_STATISTICS_QUERY_STATISTICS_FINANCE_FEE_DETAILS_URL)
    @RequiresPermissions(value = Permissions.FINANCING_FEE_DETAILS_QUERY)
    public String renderFinanceFeeDetailPage(HttpServletRequest request, HttpSession session, Model model) {
        model.addAttribute("payMethodList", getStaticOptions(EFeePayMethod.class, true));
        Date date = new Date();
        String dateString = DateUtils.formatDate(date, LiteralConstant.YYYY_MM_DD);
        model.addAttribute(SELECT_DATE, dateString);
        return "packet/financing_fee_detail";
    }

    /**
     * 11.4.811.3.6 融资费用收入明细
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/getfinancefeedetail", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = Permissions.FINANCING_FEE_DETAILS_QUERY)
    public ReportParameterStringDto getFinanceFeeDetailParameter(@RequestBody FinanceFeeReceiptDetailDto query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        if (query.getFeePayMethodType() != null && query.getFeePayMethodType() != EFeePayMethod.NULL) {
            filter.put("PAY_METHOD", query.getFeePayMethodType().getCode());
        }
        StringBuffer startDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getStartDate())) {
            startDate.append(query.getStartDate()).append(MIN_TIME);
        } else {
            startDate.append(MIN_DATE);
        }

        if (StringUtils.isNotBlank(query.getKeyWord())) {
            filter.put(KEY_WORD, query.getKeyWord().trim());
        }

        StringBuffer endDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getEndDate())) {
            endDate.append(query.getEndDate()).append(MAX_TIME);
        } else {
            endDate.append(MAX_DATE);
        }

        StringBuffer signDateBuffer = new StringBuffer();
        signDateBuffer.append(startDate).append(",").append(endDate);
        filter.put("DIFF_AMT_DT", signDateBuffer.toString());

        return this.reportService.GetReportParameterService(ReportConsts.RID_FINANCE_FEE_STATISTICS_DETAIL,
                ReportConsts.PAGENAME_PAGING, query.getExportFileExt(), query.getParentClientEvent(),
                query.getEventName(), filter);
    }

    /**
     * 11.5.211.4.2 会员充值/提现信息查询-加载页面
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.QUERY_STATISTICS_MEMBER_TOPUP_WITHDRAW_QUERY_URL)
    @RequiresPermissions(value = Permissions.MEMBER_TOPUP_WITHDRAW_INFO_QUERY)
    public String renderMemberChargeWithdrawPage(HttpServletRequest request, HttpSession session, Model model) {
        model.addAttribute("channelList", getStaticOptions(ECashPool.class, true));
        Date date = new Date();
        String dateString = DateUtils.formatDate(date, LiteralConstant.YYYY_MM_DD);
        model.addAttribute(SELECT_DATE, dateString);
        model.addAttribute("chargeTypeList", getStaticOptions(ERechargeWithdrawFlag.class, true));

        return "packet/member_deposit_search";
    }

    /**
     * 11.5.211.4.2 会员充值/提现信息查询---资金池
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/getcashpoolchargewithdraw", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = Permissions.MEMBER_TOPUP_WITHDRAW_INFO_QUERY)
    public ReportParameterStringDto getCashPoolChargeWithdrawParameter(@RequestBody MemberChargeWithdrawDto query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();

        if (StringUtils.isNotBlank(query.getKeyWord())) {
            filter.put(KEY_WORD, query.getKeyWord().trim());
        }
        if (query.getFundChannel() != null && query.getFundChannel() != ECashPool.ALL) {
            filter.put("BANK_CASH_POOL", query.getFundChannel().getCode());
        }
        if (StringUtils.isNotBlank(query.getIsSign())) {
            filter.put("SIGNED_FLG", query.getIsSign());
        }
        if (query.getRechargeWithdrawFlag() != ERechargeWithdrawFlag.ALL) {
            filter.put("PAY_RECV_FLG", query.getRechargeWithdrawFlag().getCode());
        }
        StringBuffer startDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getStartDate())) {
            startDate.append(query.getStartDate()).append(MIN_TIME);
        } else {
            startDate.append(MIN_DATE);
        }

        StringBuffer endDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getEndDate())) {
            endDate.append(query.getEndDate()).append(MAX_TIME);
        } else {
            endDate.append(MAX_DATE);
        }

        StringBuffer signDateBuffer = new StringBuffer();
        signDateBuffer.append(startDate).append(",").append(endDate);
        filter.put(CREATE_TS, signDateBuffer.toString());
        String fixed = "4";

        return this.reportService.GetReportParameterService(ReportConsts.RID_MEMBER_CHARGE_WITHDRAW,
                ReportConsts.PAGENAME_CASH_POOL, query.getExportFileExt(), query.getParentClientEvent(),
                query.getEventName(), filter, null, null, fixed);
    }

    /**
     * 11.5.211.4.2 会员充值/提现信息查询
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/getmemberchargewithdraw", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = Permissions.MEMBER_TOPUP_WITHDRAW_INFO_QUERY)
    public ReportParameterStringDto getMemberChargeWithdrawParameter(@RequestBody MemberChargeWithdrawDto query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();

        if (StringUtils.isNotBlank(query.getKeyWord())) {
            filter.put(KEY_WORD, query.getKeyWord().trim());
        }
        if (query.getFundChannel() != null && query.getFundChannel() != ECashPool.ALL) {
            filter.put("BANK_CASH_POOL", query.getFundChannel().getCode());
        }
        if (StringUtils.isNotBlank(query.getIsSign())) {
            filter.put("SIGNED_FLG", query.getIsSign());
        }
        if (query.getRechargeWithdrawFlag() != ERechargeWithdrawFlag.ALL) {
            filter.put("PAY_RECV_FLG", query.getRechargeWithdrawFlag().getCode());
        }
        StringBuffer startDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getStartDate())) {
            startDate.append(query.getStartDate()).append(MIN_TIME);
        } else {
            startDate.append(MIN_DATE);
        }

        StringBuffer endDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getEndDate())) {
            endDate.append(query.getEndDate()).append(MAX_TIME);
        } else {
            endDate.append(MAX_DATE);
        }

        StringBuffer signDateBuffer = new StringBuffer();
        signDateBuffer.append(startDate).append(",").append(endDate);
        filter.put(CREATE_TS, signDateBuffer.toString());
        String fixed = "4";

        return this.reportService.GetReportParameterService(ReportConsts.RID_MEMBER_CHARGE_WITHDRAW,
                ReportConsts.PAGENAME_PAGING, query.getExportFileExt(), query.getParentClientEvent(),
                query.getEventName(), filter, null, null, fixed);
    }

    /**
     * 11.5.311.4.3 会员账户查询-加载页面
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.QUERY_STATISTICS_MEMBER_ACCOUNT_QUERY_URL)
    @RequiresPermissions(value = Permissions.MEMBER_ACCOUNT_QUERY)
    public String renderMemberAccountPage(HttpServletRequest request, HttpSession session, Model model) {
        model.addAttribute("channelList", getStaticOptions(ECashPool.class, true));
        model.addAttribute("fundUserTypeList", getStaticOptions(EFundUserSearchType.class, true));
        Date date = new Date();
        String dateString = DateUtils.formatDate(date, LiteralConstant.YYYY_MM_DD);
        model.addAttribute(SELECT_DATE, dateString);
        return "packet/member_account_search";
    }

    /**
     * 11.5.311.4.3 会员账户查询
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/getmemberaccount", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = Permissions.MEMBER_ACCOUNT_QUERY)
    public ReportParameterStringDto getMemberAccountParameter(@RequestBody MemberChargeWithdrawDto query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(query.getKeyWord())) {
            filter.put(KEY_WORD, query.getKeyWord().trim());
        }
        if (query.getFundChannel() != null && query.getFundChannel() != ECashPool.ALL) {
            filter.put("maq_bank_cash_pool", query.getFundChannel().getCode());
        }
        if (StringUtils.isNotBlank(query.getIsSign())) {
            filter.put("maq_signed_flg", query.getIsSign());
        }
        StringBuffer startDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getStartDate())) {
            startDate.append(query.getStartDate()).append(MIN_TIME);
        } else {
            startDate.append(MIN_DATE);
        }

        StringBuffer endDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getEndDate())) {
            endDate.append(query.getEndDate()).append(MAX_TIME);
        } else {
            endDate.append(MAX_DATE);
        }

        if (query.getFundUserType() != EFundUserSearchType.ALL) {
            filter.put("USE_TYPE", query.getFundUserType().getCode());
        }
        StringBuffer signDateBuffer = new StringBuffer();
        signDateBuffer.append(startDate).append(",").append(endDate);
        filter.put("maq_trx_dt", signDateBuffer.toString());
        String fixed = "4";
        if (StringUtils.isNotBlank(query.getIsSign())) {
            filter.put("maq_signed_flg", query.getIsSign());
        }

        return this.reportService.GetReportParameterService(ReportConsts.RID_MEMBER_ACCOUNT,
                ReportConsts.PAGENAME_PAGING, query.getExportFileExt(), query.getParentClientEvent(),
                query.getEventName(), filter, null, null, fixed);
    }

    /**
     * 11.5.511.4.5 借款合同汇总-加载页面
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.QUERY_STATISTICS_LOAN_CONTRACT_STATISTICS_URL)
    @RequiresPermissions(value = Permissions.LOAN_CONTRACT_SUMMARY)
    public String renderLoanContractPage(HttpServletRequest request, HttpSession session, Model model) {
        model.addAttribute("serviceCenterInfoList", this.reportService.getServiceCenterInfoList());
        Date date = new Date();
        String dateString = DateUtils.formatDate(date, LiteralConstant.YYYY_MM_DD);
        model.addAttribute(SELECT_DATE, dateString);
        return "packet/loan_contract_summary";
    }

    /**
     * 11.5.511.4.5 借款合同汇总
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/getloancontract", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = Permissions.LOAN_CONTRACT_SUMMARY)
    public ReportParameterStringDto getLoanContractParameter(@RequestBody LoanContractDto query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();

        if (StringUtils.isNotBlank(query.getKeyWord())) {
            filter.put(PKG_ID, query.getKeyWord());
        }
        if (StringUtils.isNotBlank(query.getCommonId())) {
            filter.put("WRTR_ACCT_NO", query.getCommonId());
        }
        if (query.getAutorizationList() != null && query.getAutorizationList().size() > 0) {
            if (query.getAutorizationList().size() == 1) {
                filter.put("INVSTR_ACCT_NO", query.getAutorizationList().get(0));
            } else {
                StringBuilder sb = new StringBuilder();

                for (int i = 0; i < query.getAutorizationList().size(); i++) {
                    if (i == query.getAutorizationList().size() - 1) {
                        sb.append(query.getAutorizationList().get(i));
                    } else {
                        sb.append(query.getAutorizationList().get(i)).append(",");
                    }
                }
                filter.put("INVSTR_ACCT_NO", sb.toString());
            }
        }

        StringBuffer startDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getStartDate())) {
            startDate.append(query.getStartDate()).append(MIN_TIME);
        } else {
            startDate.append(MIN_DATE);
        }

        StringBuffer endDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getEndDate())) {
            endDate.append(query.getEndDate()).append(MAX_TIME);
        } else {
            endDate.append(MAX_DATE);
        }

        StringBuffer signDateBuffer = new StringBuffer();
        signDateBuffer.append(startDate).append(",").append(endDate);
        if (StringUtils.isNotBlank(query.getStartDate()) || StringUtils.isNotBlank(query.getEndDate())) {
            filter.put("SIGNING_DT", signDateBuffer.toString());
        }

        return this.reportService.GetReportParameterService(ReportConsts.RID_LOAN_CONTRACT,
                ReportConsts.PAGENAME_PAGING, query.getExportFileExt(), query.getParentClientEvent(),
                query.getEventName(), filter);
    }

    /**
     * 电子合同汇总
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/geteletroniccontract", method = RequestMethod.POST)
    @ResponseBody
    public ReportParameterStringDto getEletronicContractParameter(@RequestBody CommonParameter query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put(PKG_ID, query.getCommonId());
        String pageTitle = this.reportService.getPackageByPackageId(query.getCommonId()).getPackageName() + ": 合同信息";

        return this.reportService.GetReportParameterService(ReportConsts.RID_ELECTRONIC_CONTRACT,
                ReportConsts.PAGENAME_ELECTRONIC_CONTRACT, query.getExportFileExt(), query.getParentClientEvent(),
                query.getEventName(), filter, null, pageTitle);
    }

    /**
     * 11.4.611.3.4 会员审核查询-加载页面
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/getmemberapprovepage")
    public String renderMemberApprovePage(HttpServletRequest request, HttpSession session, Model model) {
        return "";
    }

    /**
     * 11.4.611.3.4 会员审核查询
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/getmemberapprove", method = RequestMethod.POST)
    @ResponseBody
    public ReportParameterStringDto getMemberApproveParameter(@RequestBody TimeSelectDto query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(query.getKeyWord())) {
            filter.put(KEY_WORD, query.getKeyWord());
        }
        StringBuffer startDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getStartDate())) {
            startDate.append(query.getStartDate()).append(MIN_TIME);
        } else {
            startDate.append(MIN_DATE);
        }

        StringBuffer endDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getEndDate())) {
            endDate.append(query.getEndDate()).append(MAX_TIME);
        } else {
            endDate.append(MAX_DATE);
        }

        StringBuffer signDateBuffer = new StringBuffer();
        signDateBuffer.append(startDate).append(",").append(endDate);
        filter.put("SUBMIT_CREATE_TS", signDateBuffer.toString());

        return this.reportService.GetReportParameterService(ReportConsts.RID_MEMBER_APPROVE,
                ReportConsts.PAGENAME_PAGING, query.getExportFileExt(), query.getParentClientEvent(),
                query.getEventName(), filter);
    }

    /**
     * 11.611.5 债权转让查询-加载页面
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.QUERY_STATISTICS_CREDITOR_TRANSFER_QUERY_URL)
    @RequiresPermissions(value = { Permissions.CREDITOR_TRANSFER_QUERY })
    public String renderCreditTransferPage(HttpServletRequest request, HttpSession session, Model model) {
        model.addAttribute("keyTypeList", getStaticOptions(ECreditTransferKeyType.class, true));
        model.addAttribute("dateTypeList", getStaticOptions(ECreditTransferTimeType.class, true));
        model.addAttribute("transferStatusList", getStaticOptions(ETransferStatus.class, false));
        Date date = new Date();
        String dateString = DateUtils.formatDate(date, LiteralConstant.YYYY_MM_DD);
        model.addAttribute(SELECT_DATE, dateString);
        return "packet/debt_assign_detail";
    }

    /**
     * 11.611.5 债权转让查询
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/getcredittransfer", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = { Permissions.CREDITOR_TRANSFER_QUERY })
    public ReportParameterStringDto getCreditTransferParameter(@RequestBody CreditTransferReportDto query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(query.getKeyWord())) {
            if (query.getCreditTransferKeyType() == ECreditTransferKeyType.SELLER) {
                filter.put("SELLER_NAME", query.getKeyWord());
            } else if (query.getCreditTransferKeyType() == ECreditTransferKeyType.BUYER) {
                filter.put("BUYER_NAME", query.getKeyWord());
            } else if (query.getCreditTransferKeyType() == ECreditTransferKeyType.CODE) {
				filter.put("TRX_NO", query.getKeyWord());
            }
        }

        if (query.getTransferStatus() != null && query.getTransferStatus() != ETransferStatus.NULL) {
            filter.put("STATUS", query.getTransferStatus().getCode());
        }

        StringBuffer startDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getStartDate())) {
            startDate.append(query.getStartDate()).append(MIN_TIME);
        } else {
            startDate.append(MIN_DATE);
        }

        StringBuffer endDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getEndDate())) {
            endDate.append(query.getEndDate()).append(MAX_TIME);
        } else {
            endDate.append(MAX_DATE);
        }

        StringBuffer signDateBuffer = new StringBuffer();
        signDateBuffer.append(startDate).append(",").append(endDate);

        if (ECreditTransferTimeType.SELL == query.getCreditTransferTimeType()) {
        	filter.put("SELLER_TS", signDateBuffer.toString());
		} else if (ECreditTransferTimeType.BUY == query.getCreditTransferTimeType()) {
			filter.put("TRD_TS", signDateBuffer.toString());
		}
        String fixed = "2";

        return this.reportService.GetReportParameterService(ReportConsts.RID_CREDIT_TRANSFER,
                ReportConsts.PAGENAME_COMMON, query.getExportFileExt(), query.getParentClientEvent(),
                query.getEventName(), filter, null, null, fixed);
    }

    /**
     * 11.611.5 债权转让查询-交易详情
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/getcredittransferdetail", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = { Permissions.CREDITOR_TRANSFER_QUERY })
    public ReportParameterStringDto getCreditTransferDetailParameter(@RequestBody CommonParameter query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put(JNL_NO, query.getCommonId());

        return this.reportService.GetReportParameterService(ReportConsts.RID_DETAIL,
                ReportConsts.PAGENAME_CREDIT_TRANSFER_DETAIL, query.getExportFileExt(), query.getParentClientEvent(),
                query.getEventName(), filter);
    }

    /**
     * 11.611.5 债权转让查询交易详情-回收计划
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/getcreditwithdraw", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = { Permissions.CREDITOR_TRANSFER_QUERY })
    public ReportParameterStringDto getCreditWithdrawParameter(@RequestBody CommonParameter query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put(JNL_NO, query.getCommonId());

        return this.reportService.GetReportParameterService(ReportConsts.RID_DETAIL,
                ReportConsts.PAGENAME_CREDIT_TRANSFER_DETAIL, query.getExportFileExt(), query.getParentClientEvent(),
                query.getEventName(), filter);
    }

    /**
     * 11.611.5 债权转让查询交易详情-还款情况
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/getcreditpayment", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = { Permissions.CREDITOR_TRANSFER_QUERY })
    public ReportParameterStringDto getCreditPaymentParameter(@RequestBody CommonParameter query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put(JNL_NO, query.getCommonId());

        return this.reportService.GetReportParameterService(ReportConsts.RID_DETAIL,
                ReportConsts.PAGENAME_CREDIT_TRANSFER_DETAIL, query.getExportFileExt(), query.getParentClientEvent(),
                query.getEventName(), filter);
    }

    /**
     * 11.611.5 债权转让查询-转让协议
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/getcredittransfercontract", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = { Permissions.CREDITOR_TRANSFER_QUERY })
    public ReportParameterStringDto getCreditTransferContractParameter(@RequestBody CommonParameter query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put(JNL_NO, query.getCommonId());

        return this.reportService.GetReportParameterService(ReportConsts.RID_DETAIL,
                ReportConsts.PAGENAME_CREDIT_TRANSFER_CONTRACT, query.getExportFileExt(), query.getParentClientEvent(),
                query.getEventName(), filter);
    }

    /**
     * 11.6.211.5.2 债权转让费用-加载页面
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.QUERY_STATISTICS_CREDITOR_TRANSFER_FEE_QUERY_URL)
    @RequiresPermissions(value = { Permissions.CREDITOR_TRANSFER_FEE_QUERY })
    public String renderCreditTransferFeePage(HttpServletRequest request, HttpSession session, Model model) {
        model.addAttribute("keyTypeList", getStaticOptions(ECreditTransferFeeType.class, true));
        Date date = new Date();
        String dateString = DateUtils.formatDate(date, LiteralConstant.YYYY_MM_DD);
        model.addAttribute(SELECT_DATE, dateString);
        return "packet/assignment_credit_cost";
    }

    /**
     * 11.6.211.5.2 债权转让费用
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/getcredittransferfee", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = { Permissions.CREDITOR_TRANSFER_FEE_QUERY })
    public ReportParameterStringDto getCreditTransferFeeParameter(@RequestBody CreditTransferReportDto query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(query.getKeyWord())) {
            if (query.getCreditTransferKeyType() == ECreditTransferKeyType.SELLER) {
                filter.put("SELLER_NAME", query.getKeyWord());
            } else if (query.getCreditTransferKeyType() == ECreditTransferKeyType.BUYER) {
                filter.put("BUYER_NAME", query.getKeyWord());
            } else if (query.getCreditTransferKeyType() == ECreditTransferKeyType.CODE) {
				filter.put("TRX_NO", query.getKeyWord());
            }
        }

        StringBuffer startDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getStartDate())) {
            startDate.append(query.getStartDate()).append(MIN_TIME);
        } else {
            startDate.append(MIN_DATE);
        }

        StringBuffer endDate = new StringBuffer();
        if (StringUtils.isNotBlank(query.getEndDate())) {
            endDate.append(query.getEndDate()).append(MAX_TIME);
        } else {
            endDate.append(MAX_DATE);
        }

        StringBuffer signDateBuffer = new StringBuffer();
        signDateBuffer.append(startDate).append(",").append(endDate);
        filter.put("TRD_TS", signDateBuffer.toString());

        return this.reportService.GetReportParameterService(ReportConsts.RID_CREDIT_TRANSFER_FEE,
                ReportConsts.PAGENAME_COMMON, query.getExportFileExt(), query.getParentClientEvent(),
                query.getEventName(), filter);
    }

    /**
     * 11.6.211.5.2 债权转让费用-转让协议
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "report/getcredittransferfeecontract", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = { Permissions.CREDITOR_TRANSFER_FEE_QUERY })
    public ReportParameterStringDto getCreditTransferFeeContractParameter(@RequestBody CommonParameter query,
            HttpServletRequest request, HttpSession session, Model model) {
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put(JNL_NO, query.getCommonId());

        return this.reportService.GetReportParameterService(ReportConsts.RID_DETAIL,
                ReportConsts.PAGENAME_CREDIT_TRANSFER_CONTRACT, query.getExportFileExt(), query.getParentClientEvent(),
                query.getEventName(), filter);
    }

	/**
	 * 债权转让合同
	 * 
	 * @param lotId
	 * @param type
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "report/transfercontract/{type}/{lotId}", method = RequestMethod.GET)
	@RequiresPermissions(value = { Permissions.MARKETING_CREDITOR_ASSIGNMENT_PURCHASE })
	public String getTransferContract(@PathVariable String lotId,
			@PathVariable String type, Model model) {
		TransferContractDetailsDto contract = transferContractService
				.getTransferContractDetails(lotId, type);
		model.addAttribute("contract", contract);
		return "report/transfer_contract";
	}

	// TODO 会话信息丢失的情况，直接抛错避免信息泄露
	private String getCurrentUserId() {
		String userId = securityContext.getCurrentUserId();
		if (userId == null) {
			BizServiceException ex = new BizServiceException(EErrorCode.TECH_SESSION_TIMEOUT);
			LOGGER.warn("session info lost, no current user id found", ex);
			throw ex;
		}
		return userId;
	}

}
