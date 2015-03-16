package com.hengxin.platform.common.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hengxin.platform.bnkdocking.service.ManageService;
import com.hengxin.platform.bnkdocking.service.SystemService;
import com.hengxin.platform.common.constant.UrlConstant;
import com.hengxin.platform.common.converter.BusinessSigninJournalConverter;
import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.domain.BusinessSigninJournal;
import com.hengxin.platform.common.dto.BusinessSigninJournalDto;
import com.hengxin.platform.common.dto.DataTablesRequestDto;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.common.service.BusinessSigninStatusService;
import com.hengxin.platform.common.service.CommonBusinessService;
import com.hengxin.platform.common.util.MessageUtil;
import com.hengxin.platform.member.service.MemberMessageService;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.sms.dto.MessageInfoDto;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.ShowStackTraceException;

/**
 * Class Name: BusinessSigninStatusController Description: TODO
 * 
 * @author junwei
 * 
 */

@Controller
public class BusinessSigninStatusController {
	
    private static final String MARKET_TYPE = "MKT";
    private static final String CMB_TYPE = "CMB";
    
    @Autowired
    private SecurityContext securityContext;
    @Autowired
    private SystemService systemService;
    @Autowired
    private ManageService manageService;
    @Autowired
    private CommonBusinessService commonBusinessService;
	@Autowired
	transient MemberMessageService memberMessageService;
    @Autowired
    private BusinessSigninStatusService businessSigninStatusService;

    @RequestMapping(value = UrlConstant.FINANCING_PACKAGE_OPEN_CLOSE_URL)
    @RequiresPermissions(value = Permissions.PRODUCT_FINANCING_TRANSACTION_OPEN_CLOSE)
    public String renderMarketOpen(HttpServletRequest request, Model model) {
        String currentMarketStatus = businessSigninStatusService.getCurrentMarketStatus(MARKET_TYPE);
        model.addAttribute("currentMarketStatus", currentMarketStatus);
        return "product/start_trading";
    }
    
    @RequestMapping(value =UrlConstant.FINANCING_PACKAGE_CMB_SIGN_URL)
    @RequiresPermissions(value = Permissions.TRANS_MANAGEMENT_CMB_SIGN)
    public String renderCmbtSign(HttpServletRequest request, Model model) {
        return "myaccount/cmbt_sign";
    }

    /**
     * Description: 开市闭市操作
     * 
     * @param status
     * @param request
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "signinstatus/save/{status}", method = RequestMethod.POST)
    @RequiresPermissions(value = Permissions.PRODUCT_FINANCING_TRANSACTION_OPEN_CLOSE)
    public ResultDto createSigninStatus(@PathVariable(value = "status") String status, 
    		HttpServletRequest request,
            HttpSession session, Model model) {
    	
        String currentUserId = securityContext.getCurrentUserId();
        
        List<MessageInfoDto> messageList = businessSigninStatusService.saveBusinessSigninStatus(MARKET_TYPE, status, currentUserId);
        
        // refresh common cache
        commonBusinessService.refresh();
        
        //message send
        if (StringUtils.equals("C", status)) {
			sendCRTCloseMessage(messageList);
        }
        
        return ResultDtoFactory.toAck(MessageUtil.getMessage("business.market.openclose"), status);
    }

    /**
     * Description: 招行日初操作
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "signinstatus/cmbsign/{date}", method = RequestMethod.POST)
    @RequiresPermissions(value = Permissions.TRANS_MANAGEMENT_CMB_SIGN)
    public ResultDto cmbSign(@PathVariable(value = "date") String date, HttpServletRequest request,
            HttpSession session, Model model) {
        String currentUserId = securityContext.getCurrentUserId();
        String status = "O";
        //ResultDto resultDto = ResultDtoFactory.toNack("签到失败");
        try {
            if(systemService.applySessionKey()) {
                if(!manageService.dailySign(date)) {
                	throw new ShowStackTraceException(EErrorCode.DAILY_SIGN_ERROR, "签到失败-签到返回失败");
                }
            }
            else {
            	throw new ShowStackTraceException(EErrorCode.DAILY_SIGN_ERROR, "签到失败-申请密钥失败");
            }
            businessSigninStatusService.saveCmbStatus(CMB_TYPE, status, currentUserId, date);
        } catch (Exception e) {
        	throw new ShowStackTraceException(EErrorCode.DAILY_SIGN_ERROR, "签到失败" ,e);
        }
        return ResultDtoFactory.toAck("签到成功");
    }

    /**
     * Description: 开市闭市历史记录
     * 
     * @param request
     * @param searchDto
     * @param status
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "signinstatus/search")
    @RequiresPermissions(value = Permissions.PRODUCT_FINANCING_TRANSACTION_OPEN_CLOSE)
    public DataTablesResponseDto<BusinessSigninJournalDto> getBusinessSigninJournalList(HttpServletRequest request,
            @RequestBody DataTablesRequestDto searchDto) {
        DataTablesResponseDto<BusinessSigninJournalDto> result = new DataTablesResponseDto<BusinessSigninJournalDto>();
        List<BusinessSigninJournalDto> journalDtoList = new ArrayList<BusinessSigninJournalDto>();
        result.setEcho(searchDto.getEcho());
        Page<BusinessSigninJournal> page = businessSigninStatusService.getSigninJournalByWorkType(MARKET_TYPE,
                searchDto);
        if (page != null) {
            List<BusinessSigninJournal> poList = page.getContent();
            for (BusinessSigninJournal journalPo : poList) {
                journalDtoList.add(ConverterService.convert(journalPo, BusinessSigninJournalDto.class,
                        BusinessSigninJournalConverter.class));
            }
            result.setTotalDisplayRecords(page.getTotalElements());
            result.setTotalRecords(page.getTotalElements());
        }
        result.setData(journalDtoList);
        return result;
    }
    
    /**
     * Description: 签到历史记录
     * 
     * @param request
     * @param searchDto
     * @param status
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "signinstatus/cmbsearch")
    @RequiresPermissions(value = Permissions.TRANS_MANAGEMENT_CMB_SIGN)
    public DataTablesResponseDto<BusinessSigninJournalDto> getCmbSigninJournalList(HttpServletRequest request,
            @RequestBody DataTablesRequestDto searchDto) {
        DataTablesResponseDto<BusinessSigninJournalDto> result = new DataTablesResponseDto<BusinessSigninJournalDto>();
        List<BusinessSigninJournalDto> journalDtoList = new ArrayList<BusinessSigninJournalDto>();
        result.setEcho(searchDto.getEcho());
        Page<BusinessSigninJournal> page = businessSigninStatusService.getSigninJournalByWorkType(CMB_TYPE,
                searchDto);
        if (page != null) {
            List<BusinessSigninJournal> poList = page.getContent();
            for (BusinessSigninJournal journalPo : poList) {
                journalDtoList.add(ConverterService.convert(journalPo, BusinessSigninJournalDto.class,
                        BusinessSigninJournalConverter.class));
            }
            result.setTotalDisplayRecords(page.getTotalElements());
            result.setTotalRecords(page.getTotalElements());
        }
        result.setData(journalDtoList);
        return result;
    }

	/**
	 * 发送债权转让闭市自动撤单短信(message send)
	 */
	private void sendCRTCloseMessage(List<MessageInfoDto> messageList) {
		if(!messageList.isEmpty()){
			for(MessageInfoDto dto: messageList){
				memberMessageService
						.sendMessage(
								dto.getMessageType(),
								dto.getMessageKey(),
								dto.getUserId(),
								dto.getContent());
			}
		}
	}

}
