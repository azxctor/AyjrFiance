/*
 * Project Name: kmfex
 * File Name: FreezeReverseQueryController.java
 * Class Name: FreezeReverseQueryController
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

package com.hengxin.platform.fund.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.Logical;
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
import com.hengxin.platform.common.domain.EnumOption;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.fund.dto.AcctFreezeSumAmtDto;
import com.hengxin.platform.fund.dto.FreezeReserveDtlDto;
import com.hengxin.platform.fund.dto.FreezeReserveDtlSearchDto;
import com.hengxin.platform.fund.enums.EFnrStatus;
import com.hengxin.platform.fund.enums.EFundUseType;
import com.hengxin.platform.fund.service.AcctService;
import com.hengxin.platform.fund.service.FreezeReserveDtlService;
import com.hengxin.platform.fund.util.AmtUtils;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;

/**
 * Class Name: FreezeReverseQueryController
 * 
 * @author congzhou
 * 
 */
@Controller
public class FundFreezeReverseDtlQueryController extends BaseController {

	@Autowired
    AcctService acctService;
	
    @Autowired
    SecurityContext securityContext;

    @Autowired
    FreezeReserveDtlService freezeReserveDtlService;

    /**
     * Description: 获得会员冻结保留明细页面
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */

    @RequestMapping(value = UrlConstant.MY_ACCOUNT_FREEZE_RESERVE_DETAILS_URL, method = RequestMethod.GET)
    @RequiresPermissions(value = Permissions.MEMBER_FREEZE_RESERVE_DETAILS)
    public String getUserFundFreezeReverseDtlView(HttpServletRequest request, Model model) {
        model.addAttribute("workDate", CommonBusinessUtil.getCurrentWorkDate());
        model.addAttribute("fundUseType", this.getFundUseType());
        model.addAttribute("fnrStatusItems", this.getFnrStatusItems());
        return "myaccount/freeze_reserved_details";
    }

    /**
     * Description: 获得平台冻结保留明细页面
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */

    @RequestMapping(value = UrlConstant.SETTLEMENT_FREEZE_RESERVE_DETAILS_URL, method = RequestMethod.GET)
    @RequiresPermissions(value = Permissions.PLATFORM_FREEZE_RESERVE_DETAILS)
    public String getPlatformFundFreezeReverseDtlView(HttpServletRequest request, Model model) {
        model.addAttribute("isPlatFormUser", securityContext.isPlatformUser());
//        List<EMemberType> enumList = Arrays.asList(EMemberType.NULL, EMemberType.INVESTOR, EMemberType.FINANCER,
//                EMemberType.INVESTOR_FINANCER, EMemberType.PRODUCTSERVICE);
//        List<EnumOption> options = new ArrayList<EnumOption>();
//        for (EMemberType e : enumList) {
//            options.add(new EnumOption(e.name(), ((PageEnum) e).getText()));
//        }
//        model.addAttribute("userTypeList", options);
        model.addAttribute("fundUseType", this.getFundUseType());
        model.addAttribute("fnrStatusItems", this.getFnrStatusItems());
        model.addAttribute("workDate", CommonBusinessUtil.getCurrentWorkDate());
        return "myaccount/freeze_reserved_details";
    }
    
    private List<EnumOption> getFundUseType(){
        List<EnumOption> result = new ArrayList<EnumOption>();
        result.add(new EnumOption(EFundUseType.INOUTALL.name(), EFundUseType.INOUTALL.getText()));
        result.add(new EnumOption(EFundUseType.POSTDEPOSIT.name(), EFundUseType.POSTDEPOSIT.getText()));
//      result.add(new EnumOption(EFundUseType.LOANDEPOSIT.name(), EFundUseType.LOANDEPOSIT.getText()));
//      result.add(new EnumOption(EFundUseType.WARRANTDEPOSIT.name(), EFundUseType.WARRANTDEPOSIT.getText()));
        result.add(new EnumOption(EFundUseType.SUBSCRIBE.name(), EFundUseType.SUBSCRIBE.getText()));
        result.add(new EnumOption(EFundUseType.FINANCING.name(), EFundUseType.FINANCING.getText()));
        result.add(new EnumOption(EFundUseType.CASH.name(), EFundUseType.CASH.getText()));
        result.add(new EnumOption(EFundUseType.FNCR_REPAYMENT_PENALTY.name(), EFundUseType.FNCR_REPAYMENT_PENALTY.getText()));
        result.add(new EnumOption(EFundUseType.BIZFREEZEFUNDACCT.name(), EFundUseType.BIZFREEZEFUNDACCT.getText()));
        result.add(new EnumOption(EFundUseType.MGTFREEZEFUNDACCT.name(), EFundUseType.MGTFREEZEFUNDACCT.getText()));
        result.add(new EnumOption(EFundUseType.ACCTASSETRESERVED.name(), EFundUseType.ACCTASSETRESERVED.getText()));
        return result;
    }
    
    private List<EnumOption> getFnrStatusItems(){
        List<EnumOption> result = new ArrayList<EnumOption>();
        result.add(new EnumOption(EFnrStatus.ALL.name(), EFnrStatus.ALL.getText()));
        result.add(new EnumOption(EFnrStatus.ACTIVE.name(), EFnrStatus.ACTIVE.getText()));
        result.add(new EnumOption(EFnrStatus.CLOSE.name(), EFnrStatus.CLOSE.getText()));
        return result;
    }

    /**
     * 
     * Description: 获取会员冻结保留明细
     * 
     * @param request
     * @param model
     * @param searchDto
     * @return
     */
    @ResponseBody
    @RequestMapping("fund/getUserFundFreezeReverseDetails")
    @RequiresPermissions(value = Permissions.MEMBER_FREEZE_RESERVE_DETAILS)
    public DataTablesResponseDto<FreezeReserveDtlDto> getUserFundFreezeReverseDetails(HttpServletRequest request,
            Model model, @RequestBody FreezeReserveDtlSearchDto searchDto) {
        String userId = securityContext.getCurrentUserId();
        DataTablesResponseDto<FreezeReserveDtlDto> result = freezeReserveDtlService.getUserFreezeReserveDetails(
                searchDto, userId);
        result.setEcho(searchDto.getEcho());
        return result;
    }

    /**
     * 
     * Description: 获取所有冻结保留明细
     * 
     * @param request
     * @param model
     * @param searchDto
     * @return
     */
    @ResponseBody
    @RequestMapping("fund/getPlatformFundFreezeReverseDetails")
    @RequiresPermissions(value = Permissions.PLATFORM_FREEZE_RESERVE_DETAILS)
    public DataTablesResponseDto<FreezeReserveDtlDto> getPlatformFundFreezeReverseDetails(HttpServletRequest request,
            Model model, @RequestBody FreezeReserveDtlSearchDto searchDto) {
        DataTablesResponseDto<FreezeReserveDtlDto> result = freezeReserveDtlService
                .getAllFrezzeReserveDetails(searchDto);
        result.setEcho(searchDto.getEcho());
        return result;
    }

    /**
     * 
    * Description: 获取冻结明细总金额
    *
    * @return
     */
    @ResponseBody
    @RequestMapping(value = "fund/getFreezeSumAmt", method = RequestMethod.POST)
    @RequiresPermissions(value = {Permissions.PLATFORM_FREEZE_RESERVE_DETAILS,
    		Permissions.PLATFORM_FREEZE_RESERVE_DETAILS}, logical = Logical.OR)
    public AcctFreezeSumAmtDto getFreezeSumAmt(HttpServletRequest request,
            Model model, @RequestBody FreezeReserveDtlSearchDto searchDto) {
    	AcctFreezeSumAmtDto sumDto = new AcctFreezeSumAmtDto();
    	Integer count = Integer.valueOf(0);
    	BigDecimal sumAmt = BigDecimal.ZERO;
    	if(!securityContext.isPlatformUser()){
    		String userId = securityContext.getCurrentUserId();
    		String acctNo = acctService.getAcctByUserId(userId).getAcctNo();
    		searchDto.setAcctNo(acctNo);
    	}
    	Object[] sumData = freezeReserveDtlService.getSumData(searchDto);
    	if(sumData!=null&&sumData.length==2){
    		sumAmt = AmtUtils.processNullAmt(sumData[0], BigDecimal.ZERO);
    		count = sumData[1]==null?count:Integer.valueOf(String.valueOf(sumData[1]));
    	}
    	sumDto.setCount(count);
    	sumDto.setSumAmount(sumAmt);
        return sumDto;
    }
}
