package com.hengxin.platform.account.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.multipart.MultipartFile;

import com.hengxin.platform.account.dto.FundTransferDto;
import com.hengxin.platform.account.dto.TransApplPriInfoSearchDto;
import com.hengxin.platform.account.dto.TransferApplApproveDto;
import com.hengxin.platform.account.dto.TransferApplDto;
import com.hengxin.platform.account.dto.downstream.CurrentAccountSearchDto;
import com.hengxin.platform.account.dto.downstream.TransApplPriInfoDto;
import com.hengxin.platform.account.dto.downstream.TransferDetailsSearchDto;
import com.hengxin.platform.account.dto.upstream.BatchTransferExcelMsgDto;
import com.hengxin.platform.account.service.AccountOverviewService;
import com.hengxin.platform.account.service.FundTransferService;
import com.hengxin.platform.account.service.PrintInfoService;
import com.hengxin.platform.common.constant.UrlConstant;
import com.hengxin.platform.common.controller.BaseController;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.common.util.MessageUtil;
import com.hengxin.platform.fund.consts.DictConsts;
import com.hengxin.platform.fund.enums.EFundApplStatus;
import com.hengxin.platform.fund.enums.EFundUseType;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;

/**
 * 
 * Class Name: TransferAccountController
 * 
 * @author jishen
 * 
 */
@Controller
public class TransferAccountController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransferAccountController.class);

    @Autowired
    private SecurityContext securityContext;
    @Autowired
    private FundTransferService fundTransferService;
    @Autowired
    private AccountOverviewService accountOverviewService;
    @Autowired
    private PrintInfoService printInfoService;

    /**
     * 资金划转界面(平台转会员)
     * 
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.MY_ACCOUNT_TRANSFER_PM_URL, method = RequestMethod.GET)
    @RequiresPermissions(value = { Permissions.SETTLE_MANAGEMENT_PLATFORM_MEMBER_TRANSFER })
    public String platformTransferToMemberView(HttpServletRequest request, Model model) {
        LOGGER.info("platformTransferToMemberView() invoked");
        String platformAcctNo = CommonBusinessUtil.getExchangeAccountNo();
        model.addAttribute("type", DictConsts.PLATFORM_TO_MEMBER);
        model.addAttribute("platformAcctNo", platformAcctNo);
        model.addAttribute("useType", EFundUseType.TRANSFERPM);
        return "myaccount/fund_transferview";
    }

    /**
     * 资金划转界面(会员转会员)
     * 
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.MY_ACCOUNT_TRANSFER_MM_URL, method = RequestMethod.GET)
    @RequiresPermissions(value = { Permissions.SETTLE_MANAGEMENT_MEMBER_MEMBER_TRANSFER })
    public String memberTransferToMemberView(HttpServletRequest request, Model model) {
        LOGGER.info("memberTransferToMemberView() invoked");
        model.addAttribute("type", DictConsts.MEMBER_TO_MEMBER);
        model.addAttribute("useType", EFundUseType.TRANSFERMM);
        return "myaccount/fund_transferview";
    }

    /**
     * 资金划转申请审批界面(平台转会员)
     * 
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.MY_ACCOUNT_TRANSFER_APPROVE_PM_URL, method = RequestMethod.GET)
    @RequiresPermissions(value = { Permissions.SETTLE_MANAGEMENT_PLATFORM_MEMBER_TRANSFER_CHECK })
    public String transferApplApprovePMView(HttpServletRequest request, Model model) {
        LOGGER.info("transferApplApproveView() invoked");
        model.addAttribute("applStatus", getStaticOptions(EFundApplStatus.class, false));
        model.addAttribute("useType", EFundUseType.TRANSFERPM);
        return "myaccount/transfer_appl_approve_view";
    }

    /**
     * 资金划转申请审批界面(会员转会员)
     * 
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.MY_ACCOUNT_TRANSFER_APPROVE_MM_URL, method = RequestMethod.GET)
    @RequiresPermissions(value = { Permissions.SETTLE_MANAGEMENT_MEMBER_MEMBER_TRANSFER_CHECK })
    public String transferApplApproveMMView(HttpServletRequest request, Model model) {
        LOGGER.info("transferApplApproveView() invoked");
        model.addAttribute("applStatus", getStaticOptions(EFundApplStatus.class, false));
        model.addAttribute("useType", EFundUseType.TRANSFERMM);
        return "myaccount/transfer_appl_approve_view";
    }

    /**
     * 资金划转申请
     * 
     * @param request
     * @param mode
     * @param transferDto
     * @return
     */
    @RequestMapping(value = "myaccount/fundtransferappl", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = { Permissions.SETTLE_MANAGEMENT_PLATFORM_MEMBER_TRANSFER,
            Permissions.SETTLE_MANAGEMENT_MEMBER_MEMBER_TRANSFER }, logical = Logical.OR)
    public ResultDto fundTransferAppl(HttpServletRequest request, Model mode,
            @RequestBody FundTransferDto fundTransferDto) {
        LOGGER.info("fundTransferAppl() invoked");
        try {
            fundTransferService.transferAppl(fundTransferDto);
        } catch (BizServiceException ex) {
            return ResultDtoFactory.toNack(ex.getError());
        }
        return ResultDtoFactory.toAck("资金划转申请成功");
    }

    /**
     * 资金划转申请审批（批量或单个）
     * 
     * @param request
     * @param mode
     * @param transferDto
     * @return
     */
    @RequestMapping(value = "myaccount/fundtransferapplApprove", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = { Permissions.SETTLE_MANAGEMENT_PLATFORM_MEMBER_TRANSFER_CHECK,
            Permissions.SETTLE_MANAGEMENT_MEMBER_MEMBER_TRANSFER_CHECK }, logical = Logical.OR)
    public ResultDto fundTransferApplApprove(HttpServletRequest request, Model mode,
            @RequestBody TransferApplApproveDto transferApplApproveDto) {
        LOGGER.info("fundTransferApplApprove() invoked");
        try {
            fundTransferService.transferAccount(transferApplApproveDto);
        } catch (BizServiceException ex) {
            return ResultDtoFactory.toNack(ex.getError());
        }
        Boolean isBatch = transferApplApproveDto.getApplNos().size() > 1;
        EFundApplStatus applStatus = transferApplApproveDto.getApplStatus();
        if (applStatus == EFundApplStatus.APPROVED) {
            return ResultDtoFactory.toAck(isBatch ? "资金划转申请批量审批成功" : "资金划转申请审批成功");
        } else {
            return ResultDtoFactory.toAck(isBatch ? "资金划转申请批量审批驳回" : "资金划转申请审批驳回");
        }

    }

    /**
     * 获取打印信息
     * 
     * @param request
     * @param session
     * @param model
     * @param searchDto
     * @return
     */
    @RequestMapping(value = "myaccount/queryTransferApplPrintInfo")
    @ResponseBody
    @RequiresPermissions(value = { Permissions.SETTLE_MANAGEMENT_PLATFORM_MEMBER_TRANSFER_CHECK,
            Permissions.SETTLE_MANAGEMENT_MEMBER_MEMBER_TRANSFER_CHECK }, logical = Logical.OR)
    public TransApplPriInfoDto queryTransApplPrintInfo(HttpServletRequest request, HttpSession session, Model model,
            @RequestBody TransApplPriInfoSearchDto searchDto) {
        LOGGER.info("queryTransApplPrintInfo() invoked");
        return printInfoService.getTransApplPrintInfoDetail(searchDto);
    }

    /**
     * 获取活期账户详情
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "myaccount/currentAccountInfo")
    @ResponseBody
    @RequiresPermissions(value = { Permissions.SETTLE_MANAGEMENT_PLATFORM_MEMBER_TRANSFER,
            Permissions.SETTLE_MANAGEMENT_MEMBER_MEMBER_TRANSFER }, logical = Logical.OR)
    public ResultDto getCurrentAccountInfo(HttpServletRequest request, HttpSession session, Model model,
            @RequestBody CurrentAccountSearchDto query) {
        LOGGER.info("getCurrentAccountInfo() invoked");
        return fundTransferService.getCurrentAccountDetail(query.getAcctNo(), query.getAcctType());
    }

    /**
     * 获取资金划转申请数据
     * 
     * @param request
     * @param model
     * @param query
     * @return
     */
    @RequestMapping("myaccount/getTransferAppls")
    @ResponseBody
    @RequiresPermissions(value = { Permissions.SETTLE_MANAGEMENT_PLATFORM_MEMBER_TRANSFER,
            Permissions.SETTLE_MANAGEMENT_MEMBER_MEMBER_TRANSFER,
            Permissions.SETTLE_MANAGEMENT_PLATFORM_MEMBER_TRANSFER_CHECK,
            Permissions.SETTLE_MANAGEMENT_MEMBER_MEMBER_TRANSFER_CHECK }, logical = Logical.OR)
    public DataTablesResponseDto<TransferApplDto> getTradeDetailsList(HttpServletRequest request, Model model,
            @RequestBody TransferDetailsSearchDto query) {
        DataTablesResponseDto<TransferApplDto> result = fundTransferService.getTransferDetails(query);
        result.setEcho(query.getEcho());
        return result;
    }

    /**
     * excel批量导入页面
     * 
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "myaccount/batchTransferview")
    @ResponseStatus(value = HttpStatus.PARTIAL_CONTENT)
    // @RequiresPermissions(value = {
    // Permissions.SETTLE_MANAGEMENT_MEMBER_MEMBER_TRANSFER })
    public String batchTransferView(HttpServletRequest request, Model model) {
        LOGGER.info("member batchTransferView() invoked");
        return "myaccount/fund_batch_transferview";
    }

    /**
     * 导入ECXEL并获取EXCEL文件内容
     * 
     * @param file
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("myaccount/uploadAndGetBatchTansferExcelMsg")
    @ResponseBody
    public ResultDto uploadAndGetBatchTansferExcelMsg(@RequestParam MultipartFile file, HttpServletRequest request,
            HttpServletResponse response) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("uploadFile start: ");
        }
        BatchTransferExcelMsgDto excelMsgDto = new BatchTransferExcelMsgDto();
        excelMsgDto.setTransferMsgs(fundTransferService.resolveExcelToTransferMsg(file));
        excelMsgDto.setPath("/uploadAndGetBatchTansferExcelMsg.xls");
        String fileName = file.getOriginalFilename();
        if (fileName.contains("\\")) {
            fileName = fileName.substring(fileName.lastIndexOf('\\'));
        }
        if (fileName.contains("/")) {
            fileName = fileName.substring(fileName.lastIndexOf('/'));
        }
        excelMsgDto.setFileName(fileName);
        return ResultDtoFactory.toAck("EXCEL文件导入成功", excelMsgDto);
    }

    /**
     * 批量转账申请
     * 
     * @param request
     * @param session
     * @param model
     * @param filePath
     * @return
     */
    @RequestMapping(value = "myaccount/createfundBatchTransferAppl", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = { Permissions.SETTLE_MANAGEMENT_PLATFORM_MEMBER_TRANSFER,
            Permissions.SETTLE_MANAGEMENT_MEMBER_MEMBER_TRANSFER }, logical = Logical.OR)
    public ResultDto createfundBatchTransferAppl(HttpServletRequest request, HttpSession session, Model model,
            @RequestBody BatchTransferExcelMsgDto msgDto) {
        String currOpId = securityContext.getCurrentUserId();
        try {
            fundTransferService.createTransferAppl(msgDto, currOpId);
        } catch (BizServiceException ex) {
            return ResultDtoFactory.toNack(ex.getError());
        }
        return ResultDtoFactory.toAck(MessageUtil.getMessage("account.transfer.application.success"));
    }

}
