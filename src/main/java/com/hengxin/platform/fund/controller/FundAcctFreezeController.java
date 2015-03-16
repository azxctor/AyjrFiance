package com.hengxin.platform.fund.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hengxin.platform.common.constant.ApplicationConstant;
import com.hengxin.platform.common.constant.UrlConstant;
import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.fund.dto.AcctFreezeLogDto;
import com.hengxin.platform.fund.dto.AcctFreezeLogListDto;
import com.hengxin.platform.fund.dto.AcctFreezeOrUnDto;
import com.hengxin.platform.fund.dto.AcctFreezeSearchDto;
import com.hengxin.platform.fund.dto.UserAcctFreezeDtlViewDto;
import com.hengxin.platform.fund.dto.UserAcctInfoViewDto;
import com.hengxin.platform.fund.dto.biz.req.atom.FreezeReq;
import com.hengxin.platform.fund.dto.biz.req.atom.UnFreezeReq;
import com.hengxin.platform.fund.entity.UserAcctFreezeDtlView;
import com.hengxin.platform.fund.entity.UserAcctInfoView;
import com.hengxin.platform.fund.enums.EFundUseType;
import com.hengxin.platform.fund.service.AcctService;
import com.hengxin.platform.fund.service.FundAcctFreezeMgtService;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.domain.User;
import com.hengxin.platform.security.service.UserService;

@Controller
public class FundAcctFreezeController {

    @Autowired
    private AcctService acctService;

    @Autowired
    private SecurityContext securityContext;

    @Autowired
    private FundAcctFreezeMgtService fundAcctFreezeMgtService;
    
	@Autowired
	private UserService userService;

    /**
     * 待冻结 Description: TODO
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.SYSTEM_MANAGEMENT_MEMBER_ACCOUNT_FREEZE_URL)
    @RequiresPermissions(value = { Permissions.SYSTEM_MANAGEMENT_MEMBER_ACCOUNT_FREEZE })
    public String renderFrozenList(HttpServletRequest request, HttpSession session, Model model) {
        return "fund/account_frozen";
    }

//    /**
//     * 待解冻 Description: TODO
//     * 
//     * @param request
//     * @param session
//     * @param model
//     * @return
//     */
//    @RequestMapping(value = "fund/user/acct/list/tounfrozen")
//    public String renderUnFrozenList(HttpServletRequest request, HttpSession session, Model model) {
//        return "fund/account_unfrozen";
//    }

    /**
     * 得到待冻结的list Description: TODO
     * 
     * @param request
     * @param model
     * @param acctFreezeSearchDto
     * @return
     */
    @RequestMapping(value = "fund/user/acct/frozenlist")
    @ResponseBody
    public DataTablesResponseDto<UserAcctInfoViewDto> getUserAcctList(HttpServletRequest request, Model model,
            @RequestBody AcctFreezeSearchDto acctFreezeSearchDto) {
        DataTablesResponseDto<UserAcctInfoViewDto> dataDto = new DataTablesResponseDto<UserAcctInfoViewDto>();
        List<UserAcctInfoViewDto> queryList = new ArrayList<UserAcctInfoViewDto>();
        Page<UserAcctInfoView> acctInfoViews = acctService.getAcctUnFrozenList(acctFreezeSearchDto);
        for (UserAcctInfoView acctInfoView : acctInfoViews) {
            UserAcctInfoViewDto accountFrozenListDto = ConverterService
                    .convert(acctInfoView, UserAcctInfoViewDto.class);
            queryList.add(accountFrozenListDto);
        }
        dataDto.setTotalDisplayRecords(acctInfoViews.getTotalElements());
        dataDto.setTotalRecords(acctInfoViews.getTotalElements());
        dataDto.setData(queryList);
        dataDto.setEcho(acctFreezeSearchDto.getEcho());
        return dataDto;
    }

    /**
     * 得到待解冻的list Description: TODO
     * 
     * @param request
     * @param model
     * @param acctFreezeSearchDto
     * @return
     */
    @RequestMapping(value = "fund/user/acct/unfrozenlist")
    @ResponseBody
    public DataTablesResponseDto<UserAcctFreezeDtlViewDto> getUserAccountFreezeList(HttpServletRequest request,
            Model model, @RequestBody AcctFreezeSearchDto acctFreezeSearchDto) {
        DataTablesResponseDto<UserAcctFreezeDtlViewDto> dataDto = new DataTablesResponseDto<UserAcctFreezeDtlViewDto>();
        List<UserAcctFreezeDtlViewDto> queryList = new ArrayList<UserAcctFreezeDtlViewDto>();
        Page<UserAcctFreezeDtlView> fnrDtlList = fundAcctFreezeMgtService.getUserAcctFreezeList(acctFreezeSearchDto);
        for (UserAcctFreezeDtlView fnrDtl : fnrDtlList) {
            UserAcctFreezeDtlViewDto fnrDtlDto = ConverterService.convert(fnrDtl, UserAcctFreezeDtlViewDto.class);
            queryList.add(fnrDtlDto);
        }
        dataDto.setTotalDisplayRecords(fnrDtlList.getTotalElements());
        dataDto.setTotalRecords(fnrDtlList.getTotalElements());
        dataDto.setData(queryList);
        dataDto.setEcho(acctFreezeSearchDto.getEcho());
        return dataDto;
    }

    @RequestMapping(value = "fund/user/acct/frozenlog/")
    @ResponseBody
    public DataTablesResponseDto<AcctFreezeLogListDto> getAccountFreezeLogList(HttpServletRequest request, Model model,
            @RequestBody AcctFreezeLogDto acctFreezeLogDto) {
        DataTablesResponseDto<AcctFreezeLogListDto> dataDto = new DataTablesResponseDto<AcctFreezeLogListDto>();
        List<AcctFreezeLogListDto> queryList = new ArrayList<AcctFreezeLogListDto>();
        Page<UserAcctFreezeDtlView> fnrDtlList = fundAcctFreezeMgtService.getUserAcctFreezeLogList(acctFreezeLogDto);
        for (UserAcctFreezeDtlView fnrDtl : fnrDtlList) {
            AcctFreezeLogListDto fnrDtlDto = ConverterService.convert(fnrDtl, AcctFreezeLogListDto.class);
            queryList.add(fnrDtlDto);
        }
        dataDto.setTotalDisplayRecords(fnrDtlList.getTotalElements());
        dataDto.setTotalRecords(fnrDtlList.getTotalElements());
        dataDto.setData(queryList);
        dataDto.setEcho(acctFreezeLogDto.getEcho());
        return dataDto;
    }

    /**
     * 冻结 Description: TODO
     * 
     * @param request
     * @param model
     * @param acctFreezeDto
     */
    @RequestMapping(value = "fund/user/acct/freeze")
    @ResponseBody
    public ResultDto freezeAcct(HttpServletRequest request, Model model, @RequestBody AcctFreezeOrUnDto acctFreezeDto) {
        FreezeReq frReq = new FreezeReq();
        frReq.setTrxAmt(BigDecimal.ZERO);
        frReq.setBizId(acctFreezeDto.getUserId());
        frReq.setUserId(acctFreezeDto.getUserId());
        frReq.setTrxMemo(acctFreezeDto.getTrxMemo());
        frReq.setUseType(EFundUseType.MGTFREEZEFUNDACCT);
        frReq.setCurrOpId(securityContext.getCurrentUserId());
        frReq.setWorkDate(CommonBusinessUtil.getCurrentWorkDate());
        fundAcctFreezeMgtService.freezeAcct(frReq, false);
        return ResultDtoFactory.toAck(ApplicationConstant.OPERATE_SUCCESS_MESSAGE);
    }

    /**
     * 解冻 Description: TODO
     * 
     * @param request
     * @param model
     * @param acctFreezeOrUnDto
     */
    @RequestMapping(value = "fund/user/acct/unfreeze")
    @ResponseBody
    public ResultDto unFreezeAcct(HttpServletRequest request, Model model,
            @RequestBody AcctFreezeOrUnDto acctFreezeOrUnDto) {
        UnFreezeReq unFrReq = new UnFreezeReq();
        unFrReq.setUserId(acctFreezeOrUnDto.getUserId());
        unFrReq.setTrxMemo(acctFreezeOrUnDto.getTrxMemo());
        unFrReq.setCurrOpId(securityContext.getCurrentUserId());
        unFrReq.setFreezeJnlNo(acctFreezeOrUnDto.getFreezeJnlNo());
        unFrReq.setWorkDate(CommonBusinessUtil.getCurrentWorkDate());
        fundAcctFreezeMgtService.unFreezeAcct(unFrReq, false);
        return ResultDtoFactory.toAck(ApplicationConstant.OPERATE_SUCCESS_MESSAGE);
    }
    

    @RequestMapping(value = "normalUsers", method = RequestMethod.POST)
    //@RequiresPermissions(value = { Permissions.SYSTEM_MANAGEMENT_MEMBER_ACCOUNT_FREEZE })
    @ResponseBody
    public DataTablesResponseDto<User> getNormalUsers(HttpServletRequest request, Model model,
            @RequestBody AcctFreezeSearchDto acctFreezeSearchDto) {
        return loadFrozenUsers(acctFreezeSearchDto, true);
    }
    
    @RequestMapping(value = "frozenUsers")
   // @RequiresPermissions(value = { Permissions.SYSTEM_MANAGEMENT_MEMBER_ACCOUNT_FREEZE })
    @ResponseBody
    public DataTablesResponseDto<User> getFrozenUsers(HttpServletRequest request, Model model,
            @RequestBody AcctFreezeSearchDto acctFreezeSearchDto) {
        return loadFrozenUsers(acctFreezeSearchDto, false);
    }

	/**
	 * @param acctFreezeSearchDto
	 * @param stauts, load freeze user if false.
	 * @return
	 */
	private DataTablesResponseDto<User> loadFrozenUsers(
			AcctFreezeSearchDto acctFreezeSearchDto, boolean status) {
        return userService.getFrozenUsers(acctFreezeSearchDto, status);
	}
	
	@RequestMapping(value = "unfreeze")
	@RequiresPermissions(value = { Permissions.SYSTEM_MANAGEMENT_MEMBER_ACCOUNT_FREEZE })
	@ResponseBody
	public ResultDto unfreezeUser(HttpServletRequest request, Model model,
			@RequestBody AcctFreezeOrUnDto acctFreezeDto) {
		return this.doFreezeUser(acctFreezeDto, true);
	}

	@RequestMapping(value = "freeze")
	@RequiresPermissions(value = { Permissions.SYSTEM_MANAGEMENT_MEMBER_ACCOUNT_FREEZE })
	@ResponseBody
	public ResultDto freezeUser(HttpServletRequest request, Model model,
			@RequestBody AcctFreezeOrUnDto acctFreezeDto) {
		return doFreezeUser(acctFreezeDto, false);
	}

	/**
	 * @param acctFreezeDto
	 * @param stauts, freeze user if false.
	 * @return
	 */
	private ResultDto doFreezeUser(AcctFreezeOrUnDto acctFreezeDto,
			boolean status) {
		String operator = this.securityContext.getCurrentUserId();
		String userId = acctFreezeDto.getUserId();
		String memo = acctFreezeDto.getTrxMemo();
		this.userService.updateUserStatus(userId, status, memo, operator);
		return ResultDtoFactory
				.toAck(ApplicationConstant.OPERATE_SUCCESS_MESSAGE);
	}
}
