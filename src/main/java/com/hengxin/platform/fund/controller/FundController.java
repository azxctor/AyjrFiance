package com.hengxin.platform.fund.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hengxin.platform.common.constant.UrlConstant;
import com.hengxin.platform.common.controller.BaseController;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.fund.consts.DictConsts;
import com.hengxin.platform.fund.dto.BnkTransferTrxDtlDto;
import com.hengxin.platform.fund.dto.TransferTrxDtlDto;
import com.hengxin.platform.fund.dto.TransferTrxDtlSearchDto;
import com.hengxin.platform.fund.service.AcctService;
import com.hengxin.platform.fund.service.FundAcctFreezeMgtService;
import com.hengxin.platform.fund.service.TransferTrxDtlService;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.security.Permissions;

/**
 * Class Name: FundController Description: TODO
 * 
 * @author jishen
 * 
 */
@Controller
public class FundController extends BaseController {

    @Autowired
    private AcctService acctService;

    @Autowired
    private TransferTrxDtlService transferTrxDtlService;

    @Autowired
    private FundAcctFreezeMgtService fundAcctFreezeMgtService;

    /**
     * 转账交易对账明细 Description: TODO
     * 
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.MY_ACCOUNT_TRANSFER_TRX_DETAIL_URL, method = RequestMethod.GET)
    @RequiresPermissions(value={Permissions.BANK_MANAGEMENT_CMB_TRX_DETAILS})
    public String getAccountOverviewDetails(HttpServletRequest request, Model model) {
        return "fund/statement";
    }

    /**
     * 获取转账交易对账明细数据
     * 
     * @param request
     * @param model
     * @param tradeDetailsSearchDto
     * @return
     */
    @ResponseBody
    @RequestMapping("fund/transferTrxDetails")
    @RequiresPermissions(value={Permissions.BANK_MANAGEMENT_CMB_TRX_DETAILS})
    public DataTablesResponseDto<TransferTrxDtlDto> getTradeDetailsList(HttpServletRequest request, Model model,
            @RequestBody TransferTrxDtlSearchDto transferTrxDtlSearchDto) {
        transferTrxDtlSearchDto.setTxDate(DateUtils.formatDate(transferTrxDtlSearchDto.getTxDt(),
                DictConsts.TRSF_DATE_FORMAT));
        DataTablesResponseDto<TransferTrxDtlDto> result = transferTrxDtlService
                .getTransferTrxDtls(transferTrxDtlSearchDto);
        return result;
    } 
    
    /**
     * 获取对账汇总数据
     * 
     * @param request
     * @param model
     * @param tradeDetailsSearchDto
     * @return
     */
    @ResponseBody
    @RequestMapping("fund/transferTrxDetailsSum")
    @RequiresPermissions(value={Permissions.BANK_MANAGEMENT_CMB_TRX_DETAILS})
    public BnkTransferTrxDtlDto getTradeDetailsSumData(HttpServletRequest request, Model model,
            @RequestBody TransferTrxDtlSearchDto transferTrxDtlSearchDto) {
        transferTrxDtlSearchDto.setTxDate(DateUtils.formatDate(transferTrxDtlSearchDto.getTxDt(),
                DictConsts.TRSF_DATE_FORMAT));
        BnkTransferTrxDtlDto result = transferTrxDtlService
                .getTransferTrxDtlSum(transferTrxDtlSearchDto);
        return result;
    }
}
