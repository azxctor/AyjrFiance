package com.hengxin.platform.fund.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.hengxin.platform.common.controller.BaseController;
import com.hengxin.platform.common.domain.EnumOption;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.util.AppConfigUtil;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.fund.dto.FundPoolDtlDto;
import com.hengxin.platform.fund.dto.FundPoolDtlSearchDto;
import com.hengxin.platform.fund.dto.FundTrxJnlDto;
import com.hengxin.platform.fund.dto.FundTrxJnlSearchDto;
import com.hengxin.platform.fund.dto.PoolCheckDtlDto;
import com.hengxin.platform.fund.enums.ECashPool;
import com.hengxin.platform.fund.enums.EFundUseType;
import com.hengxin.platform.fund.service.FundPoolDtlService;
import com.hengxin.platform.fund.service.FundTrxJnlExcelService;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.security.Permissions;

/**
 * Class Name: FundPoolDetailController Description: 资金池出入金汇总明细.
 * 
 * @author jishen
 * 
 */
@Controller
public class FundPoolDetailController extends BaseController {

    @Autowired
    private FundPoolDtlService fundPoolDtlService;
    @Autowired
    private FundTrxJnlExcelService fundTrxJnlExcelService;
    
    private List<EnumOption> getCashPoolOptions(){
    	List<EnumOption> options = new ArrayList<EnumOption>();
    	options.add(new EnumOption(ECashPool.ALL.name(), ECashPool.ALL.getText()));
    	options.add(new EnumOption(ECashPool.ESCROW_EBC.name(), ECashPool.ESCROW_EBC.getText()));
        return options;
    }

    /**
     * 资金池出入金汇总明细.
     * 
     * @param request
     * @param model
     * @return
     */
    @ResponseStatus(value = HttpStatus.PARTIAL_CONTENT)
    @RequestMapping(value = UrlConstant.MY_ACCOUNT_FUNDPOOL_DETAIL_URL, method = RequestMethod.GET)
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_SEARCH_SUMMARIZING_DETAIL })
    public String fundPoolDetailView(HttpServletRequest request, Model model) {
        model.addAttribute("cashPool", getCashPoolOptions());
        model.addAttribute("workDate", CommonBusinessUtil.getCurrentWorkDate());
        model.addAttribute("useTypes", getUseTypeOptions());
        return "fund/fund_pool_trx_list";
    }

    private List<EnumOption> getUseTypeOptions() {
        List<EFundUseType> useTypes = fundPoolDtlService.getFundPoolDtlUseTypes();
        List<EnumOption> options = new ArrayList<EnumOption>();
        options.add(new EnumOption(EFundUseType.INOUTALL.name(), EFundUseType.INOUTALL.getText()));
        for (EFundUseType em : useTypes) {
            options.add(new EnumOption(em.name(), em.getText()));
        }
        return options;
    }

    /**
     * 获取资金池出入金汇总数据.
     * 
     * @param request
     * @param model
     * @param fundPoolDtlSearchDto
     * @return
     */
    @ResponseBody
    @RequestMapping("fund/pool/amt/summarizing")
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_SEARCH_SUMMARIZING_DETAIL })
    public FundPoolDtlDto getTradeDetailsList(HttpServletRequest request, Model model,
            @RequestBody FundTrxJnlSearchDto searchDto) {
        return fundPoolDtlService.getSumCashPooltrxAmt(searchDto);
    }

    /**
     * 获取资金池交易明细数据.
     * 
     * @param request
     * @param model
     * @param fundPoolDtlSearchDto
     * @return
     */
    @ResponseBody
    @RequestMapping("fund/pool/trx/list")
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_SEARCH_SUMMARIZING_DETAIL })
    public DataTablesResponseDto<FundTrxJnlDto> getFundTrxJnlList(HttpServletRequest request, Model model,
            @RequestBody FundTrxJnlSearchDto fundTrxJnlSearchDto) {
        DataTablesResponseDto<FundTrxJnlDto> result = fundPoolDtlService.getFundTrxJnlInfo(fundTrxJnlSearchDto);
        result.setEcho(fundTrxJnlSearchDto.getEcho());
        return result;
    }

    /**
     * 
     * 导出资金池交易明细excel.
     * 
     * @param request
     * @param model
     */
    @ResponseStatus(value = HttpStatus.PARTIAL_CONTENT)
    @RequestMapping(value = "fund/pool/trx/exportexcel", method = RequestMethod.POST)
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_SEARCH_SUMMARIZING_DETAIL })
    public ModelAndView printFundTrxJnlInExcel(HttpServletRequest request, Model model,
            @RequestParam("fromDate") String fromDate, @RequestParam("toDate") String toDate,
            @RequestParam("acctNo") String acctNo, @RequestParam("cashPool") String cashPool,
            @RequestParam("useType") String useType, @RequestParam("sortColumn") String sortColumn,
            @RequestParam("sortDir") String sortDir) {
        try {
            FundTrxJnlSearchDto searchDto = new FundTrxJnlSearchDto();
            searchDto.setAcctNo(acctNo);
            if (!StringUtils.isBlank(cashPool)) {
                searchDto.setCashPool(ECashPool.valueOf(cashPool));
            }
            if (!StringUtils.isBlank(fromDate)) {
                searchDto.setFromDate(DateUtils.getDate(fromDate, "yyyy-MM-dd"));
            }
            if (!StringUtils.isBlank(toDate)) {
                searchDto.setToDate(DateUtils.getDate(toDate, "yyyy-MM-dd"));
            }
            if (!StringUtils.isBlank(useType)) {
                searchDto.setUseType(EFundUseType.valueOf(useType));
            }
            
            List<FundTrxJnlDto> fundTrxJnlList = fundPoolDtlService.getFundTrxJnlInfos(searchDto, sortColumn, sortDir);
            FundPoolDtlDto fundPoolDtl = fundPoolDtlService.getSumCashPooltrxAmt(searchDto);

            String fileName = "资金池汇总明细.xls";
            String tempPath = AppConfigUtil.getPollTrxJnlExcelTemplatePath();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("fundTrxJnlList", fundTrxJnlList);
            map.put("fundPoolDtl", fundPoolDtl);
            map.put("fileName", fileName);
            map.put("tempPath", tempPath);

            return new ModelAndView(fundTrxJnlExcelService, map);
        } catch (Exception e) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"",e);
        }
    }
    
    /**
     * 资金池对账 .
     * 
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.POOL_CHECK_URL, method = RequestMethod.GET)
    @RequiresPermissions(value={Permissions.POOL_CHECK})
    public String poolCheckView(HttpServletRequest request, Model model) {
        return "fund/pool_check";
    }
    
    /**
     * 获取资金池对账数据.
     * 
     * @param request
     * @param model
     * @param tradeDetailsSearchDto
     * @return
     */
    @ResponseBody
    @RequestMapping("fund/cash/pool/Datas")
    @RequiresPermissions(value={Permissions.POOL_CHECK})
    public DataTablesResponseDto<PoolCheckDtlDto> getPoolCheckData(HttpServletRequest request, Model model,
    		@RequestBody FundPoolDtlSearchDto searchDto) {
    	DataTablesResponseDto<PoolCheckDtlDto> result = new DataTablesResponseDto<PoolCheckDtlDto>();
    	List<PoolCheckDtlDto> ls = new ArrayList<PoolCheckDtlDto>();
		if (null != searchDto && null != searchDto.getFromDate()) {
    		String format = "yyyyMMdd";
        	String curr = DateUtils.formatDate(searchDto.getFromDate(), format);
        	String yest = DateUtils.formatDate(DateUtils.add(searchDto.getFromDate(), Calendar.DAY_OF_MONTH, -1), format);
        	ls = this.fundPoolDtlService.getPoolCheck(curr, yest);
    	}
    	result.setData(ls);
    	result.setTotalRecords(ls.size());
    	result.setTotalDisplayRecords(ls.size());
    	if (null != searchDto && searchDto.getEcho() != null) {
    		result.setEcho(searchDto.getEcho());
		}
        return result;
    }
}
