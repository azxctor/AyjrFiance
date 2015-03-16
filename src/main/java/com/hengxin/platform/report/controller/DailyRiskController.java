package com.hengxin.platform.report.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.hengxin.platform.common.constant.UrlConstant;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.util.AppConfigUtil;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.product.domain.FinancingPackageView;
import com.hengxin.platform.product.domain.PaymentSchedule;
import com.hengxin.platform.product.repository.PaymentScheduleRepository;
import com.hengxin.platform.report.dto.DailyCompensatoryDetailDto;
import com.hengxin.platform.report.dto.DailyOverDueDetailDto;
import com.hengxin.platform.report.dto.DailyRiskSearchDto;
import com.hengxin.platform.report.service.DailyCompensatoryExcelService;
import com.hengxin.platform.report.service.DailyOverdueExcelService;
import com.hengxin.platform.report.service.DailyRiskService;
import com.hengxin.platform.security.Permissions;

@Controller
public class DailyRiskController {
    private static final String YYYY_MM_DD = "yyyy-MM-dd";

    @Autowired
    private DailyRiskService dailyRiskService;

    @Autowired
    private PaymentScheduleRepository paymentScheduleRepository;

    @Autowired
    private DailyOverdueExcelService importExcelService;

    @Autowired
    private DailyCompensatoryExcelService dailyCompensatoryExcelService;

    /**
     * 11.811.7 当日逾期明细--加载页面
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.REPORT_DAILY_OVERDUE_DETAILS_VIEW_URL)
    @RequiresPermissions(value = { Permissions.REPORT_DAILY_OVERDUE_DETAILS })
    public String renderDailyOverDueDetailPage(HttpServletRequest request, HttpSession session, Model model) {
        Date date = new Date();
        String dateString = DateUtils.formatDate(date, "yyyy-MM-dd");
        model.addAttribute("selectDate", dateString);
        return "packet/report_overdue";
    }

    /**
     * 11.811.7 当日逾期明细
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "dailyrisk/getoverduedetail")
    @ResponseBody
    @RequiresPermissions(value = { Permissions.REPORT_DAILY_OVERDUE_DETAILS })
    public DataTablesResponseDto<DailyOverDueDetailDto> getDailyOverDueList(@RequestBody DailyRiskSearchDto query,
            HttpServletRequest request, HttpSession session, Model model) {
        DataTablesResponseDto<DailyOverDueDetailDto> result = new DataTablesResponseDto<DailyOverDueDetailDto>();
        Page<PaymentSchedule> overdueList = this.dailyRiskService.getOverduePackages(query);
        List<DailyOverDueDetailDto> dailyOverDueDetailDtos = new ArrayList<DailyOverDueDetailDto>();
        int sequenceId = query.getDisplayStart() + 1;
        for (PaymentSchedule paymentSchedule : overdueList) {
            DailyOverDueDetailDto dto = this.populateDailyOverDueDetailDto(paymentSchedule);
            dto.setSequenceId(sequenceId);
            dailyOverDueDetailDtos.add(dto);
            sequenceId++;
        }
        result.setTotalDisplayRecords(overdueList.getTotalElements());
        result.setTotalRecords(overdueList.getTotalElements());
        result.setData(dailyOverDueDetailDtos);
        result.setEcho(query.getEcho());
        return result;
    }

    /**
     * 设置期数---当日逾期明细
     * 
     * @param financingPackageView
     * @return
     */
    private DailyOverDueDetailDto populateDailyOverDueDetailDto(PaymentSchedule paymentSchedule) {

        DailyOverDueDetailDto result = new DailyOverDueDetailDto();
        Date currentDate = CommonBusinessUtil.getCurrentWorkDate();
        FinancingPackageView financingPackageView = paymentSchedule.getFinancingPackageView();

        result.setAccountNo(financingPackageView.getAccountNo());
        result.setFinancierName(financingPackageView.getFinancierName());
        result.setPackageName(financingPackageView.getPackageName());
        result.setSignContractTime(DateUtils.formatDate(financingPackageView.getSigningDt(), YYYY_MM_DD));
        result.setId(financingPackageView.getId());
        result.setPackageQuota(financingPackageView.getSupplyAmount());
        result.setPayDate(DateUtils.formatDate(paymentSchedule.getPaymentDate(), YYYY_MM_DD));
        result.setWrtrName(financingPackageView.getWrtrName());
        result.setWrtrNameShow(financingPackageView.getWrtrNameShow());
        result.setWarrantyType(financingPackageView.getWarrantyType());
        result.setMonthPrincipal(paymentSchedule.getPrincipalAmt().add(paymentSchedule.getInterestAmt())
                .add(paymentSchedule.getPrincipalForfeit()).add(paymentSchedule.getInterestForfeit())
                .add(paymentSchedule.getFeeAmt()).add(paymentSchedule.getFeeForfeit())
                .add(paymentSchedule.getWrtrPrinForfeit()).add(paymentSchedule.getWrtrInterestForfeit()));

        result.setOverdueDay(DateUtils.betweenDays(paymentSchedule.getPaymentDate(), currentDate));
        List<PaymentSchedule> paymentSchedules = this.paymentScheduleRepository
                .getByPackageIdOrderBySequenceIdDesc(paymentSchedule.getPackageId());
        if (paymentSchedules != null) {
            result.setItem(paymentSchedule.getSequenceId() + "/" + paymentSchedules.size());
        }

        return result;
    }

    /**
     * 导出excel---当日逾期明细
     * 
     * @param request
     * @param model
     * @param searchDto
     * @return
     */
    @RequestMapping(value = "dailyrisk/exportoverdueExcel", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.PARTIAL_CONTENT)
    @RequiresPermissions(value = { Permissions.REPORT_DAILY_OVERDUE_DETAILS })
    public ModelAndView getOverdueExcel(HttpServletRequest request, Model model,
            @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {
        try {
            List<DailyOverDueDetailDto> dailyOverDueDetailDtos = new ArrayList<DailyOverDueDetailDto>();
            List<PaymentSchedule> allOverdueList = this.dailyRiskService.getAllPackages(startDate, endDate);
            int seq = 1;
            for (PaymentSchedule ps : allOverdueList) {
                DailyOverDueDetailDto dailyOverDueDetailDto = this.populateDailyOverDueDetailDto(ps);
                dailyOverDueDetailDto.setSequenceId(seq);
                dailyOverDueDetailDtos.add(dailyOverDueDetailDto);
                seq++;
            }

            String fileName = "当日逾期明细.xls";
            String tempPath = AppConfigUtil.getOverdueExcelTemplatePath();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("applList", dailyOverDueDetailDtos);
            map.put("fileName", fileName);
            map.put("tempPath", tempPath);
            return new ModelAndView(importExcelService, map);
        } catch (Exception e) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR);
        }
    }

    /**
     * 11.911.8 当日代偿明细 --加载页面
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.REPORT_DAILY_COMPENSATORY_DETAILS_VIEW_URL)
    @RequiresPermissions(value = { Permissions.REPORT_DAILY_COMPENSATORY_DETAILS })
    public String renderDailyCompensatoryDetailPage(HttpServletRequest request, HttpSession session, Model model) {
        Date date = new Date();
        String dateString = DateUtils.formatDate(date, "yyyy-MM-dd");
        model.addAttribute("selectDate", dateString);
        return "packet/report_pay";
    }

    /**
     * 11.911.8 当日代偿明细
     * 
     * @param query
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "dailyrisk/getcompensatorydetail")
    @ResponseBody
    @RequiresPermissions(value = { Permissions.REPORT_DAILY_COMPENSATORY_DETAILS })
    public DataTablesResponseDto<DailyCompensatoryDetailDto> getDailyCompensatoryDetail(
            @RequestBody DailyRiskSearchDto query, HttpServletRequest request, HttpSession session, Model model) {
        DataTablesResponseDto<DailyCompensatoryDetailDto> result = new DataTablesResponseDto<DailyCompensatoryDetailDto>();
        Page<PaymentSchedule> paymentScheduleList = this.dailyRiskService.getCompensatoryPaymentList(query);
        List<DailyCompensatoryDetailDto> dailyCompensatoryDetailDtos = new ArrayList<DailyCompensatoryDetailDto>();
        int sequenceId = query.getDisplayStart() + 1;
        for (PaymentSchedule paymentSchedule : paymentScheduleList) {
            DailyCompensatoryDetailDto dto = this.populateDailyCompensatoryDetailDto(paymentSchedule);
            dto.setSequenceId(sequenceId);
            dailyCompensatoryDetailDtos.add(dto);
            sequenceId++;
        }
        result.setTotalDisplayRecords(paymentScheduleList.getTotalElements());
        result.setTotalRecords(paymentScheduleList.getTotalElements());
        result.setData(dailyCompensatoryDetailDtos);
        result.setEcho(query.getEcho());
        return result;
    }

    /**
     * 设置期数---当日代偿明细
     * 
     * @param packageCompensatoryView
     * @return
     */
    private DailyCompensatoryDetailDto populateDailyCompensatoryDetailDto(PaymentSchedule paymentSchedule) {

        DailyCompensatoryDetailDto result = new DailyCompensatoryDetailDto();
        FinancingPackageView financingPackageView = paymentSchedule.getFinancingPackageView();
        result.setPackageId(paymentSchedule.getPackageId());
        result.setPackageName(financingPackageView.getPackageName());
        if (paymentSchedule.getLastPayTs() != null) {
            result.setLastPayTs(DateUtils.formatDate(paymentSchedule.getLastPayTs(), YYYY_MM_DD));
        }
        if (paymentSchedule.getPaymentDate() != null) {
            result.setPaymentDate(DateUtils.formatDate(paymentSchedule.getPaymentDate(), YYYY_MM_DD));
        }
        if (financingPackageView.getSigningDt() != null) {
            result.setSignTs(DateUtils.formatDate(financingPackageView.getSigningDt(), YYYY_MM_DD));
        }
        result.setWrtrName(financingPackageView.getWrtrName());
        result.setWrtrNameShow(financingPackageView.getWrtrNameShow());
        if (paymentSchedule.getCmpnsTs() != null) {
            result.setCmpnsTs(DateUtils.formatDate(paymentSchedule.getCmpnsTs(), "yyyy-MM-dd"));
        }

        List<PaymentSchedule> paymentSchedules = this.paymentScheduleRepository.getByPackageId(financingPackageView
                .getId());
        result.setItem(paymentSchedule.getSequenceId() + "/" + paymentSchedules.size());
        BigDecimal cmpnsPyAmt = paymentSchedule.getCmpnsPyAmt() != null ? paymentSchedule
                .getCmpnsPyAmt() : BigDecimal.ZERO;
        
        result.setCmpnsPdAmt(cmpnsPyAmt);

        return result;
    }

    /**
     * 导出excel---当日代偿明细
     * 
     * @param request
     * @param model
     * @param searchDto
     * @return
     */
    @RequestMapping(value = "dailyrisk/exportcompensatoryExcel", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.PARTIAL_CONTENT)
    @RequiresPermissions(value = { Permissions.REPORT_DAILY_COMPENSATORY_DETAILS })
    public ModelAndView getCompensatoryExcel(HttpServletRequest request, Model model,
            @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {
        try {
            List<DailyCompensatoryDetailDto> dailyCompensatoryDetailDtos = new ArrayList<DailyCompensatoryDetailDto>();
            List<PaymentSchedule> packageCompensatoryViews = this.dailyRiskService.getAllCompensatoryPayment(startDate,
                    endDate);
            int seq = 1;
            for (PaymentSchedule packageCompensatoryView : packageCompensatoryViews) {
                DailyCompensatoryDetailDto dailyCompensatoryDetailDto = this
                        .populateDailyCompensatoryDetailDto(packageCompensatoryView);
                dailyCompensatoryDetailDto.setSequenceId(seq);
                dailyCompensatoryDetailDtos.add(dailyCompensatoryDetailDto);
                seq++;
            }

            String fileName = "当日代偿明细.xls";
            String tempPath = AppConfigUtil.getCOMPENSATORYExcelTemplatePath();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("applList", dailyCompensatoryDetailDtos);
            map.put("fileName", fileName);
            map.put("tempPath", tempPath);
            return new ModelAndView(dailyCompensatoryExcelService, map);
        } catch (Exception e) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR);
        }
    }

}
