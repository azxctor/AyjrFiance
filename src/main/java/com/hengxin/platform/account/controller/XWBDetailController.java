/*
 * Project Name: kmfex-platform
 * File Name: XWBDetailControl.java
 * Class Name: XWBDetailControl
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

package com.hengxin.platform.account.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hengxin.platform.account.dto.biz.req.XWBTransferReq;
import com.hengxin.platform.account.dto.downstream.XWBTransferDto;
import com.hengxin.platform.account.dto.downstream.XWBTrxHistorySearchDto;
import com.hengxin.platform.account.dto.upstream.XWBOverviewDto;
import com.hengxin.platform.account.enums.EXWBTradeType;
import com.hengxin.platform.account.service.XWBDetailService;
import com.hengxin.platform.common.constant.UrlConstant;
import com.hengxin.platform.common.controller.BaseController;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.common.dto.annotation.OnValid;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.common.util.WebUtil;
import com.hengxin.platform.fund.domain.SubAcctTrxJnl;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;

/**
 * Class Name: XWBDetailControl Description: TODO
 * 
 * @author tingwang
 * 
 */
@Controller
public class XWBDetailController extends BaseController {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(XWBDetailController.class);

	@Autowired
	private transient XWBDetailService xwbDetailService;

	@Autowired
	private transient WebUtil webUtil;

	@Autowired
	private transient SecurityContext securityContext;

	/**
	 * 小微宝详情展示（小微宝余额，收益） Description: TODO
	 * 
	 * @param request
	 * @param session
	 * @param model
	 * @param searchDto
	 * @return
	 */
	@RequestMapping(value = UrlConstant.MY_ACCOUNT_XWB_URL, method = RequestMethod.GET)
	@RequiresPermissions(value = { Permissions.MY_ACCOUNT_OVERVIEW_XWB })
	public String xWBDetailView(HttpServletRequest request,
			HttpSession session, Model model) {
		LOGGER.info("xWBDetail() invoked");
		model.addAttribute("tradeTypeList",
				getStaticOptions(EXWBTradeType.class, false));
		return "myaccount/xwb_detailview";
	}

	/**
	 * 小微宝详情展示（小微宝余额，收益） Description: TODO
	 * 
	 * @param request
	 * @param session
	 * @param model
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "myaccount/xwboverview", method = RequestMethod.GET)
	@ResponseBody
	@RequiresPermissions(value = { Permissions.MY_ACCOUNT_OVERVIEW_XWB })
	public XWBOverviewDto xWBOverview(HttpServletRequest request,
			HttpSession session, Model model) {
		LOGGER.info("xWBOverview() invoked");
		String userId = securityContext.getCurrentUserId();
		return xwbDetailService.getXWBOverview(userId);
	}

	/**
	 * 小微宝详情展示（交易列表） Description: TODO
	 * 
	 * @param request
	 * @param session
	 * @param model
	 * @param searchDto
	 * @return
	 */
	@RequestMapping(value = "myaccount/xwbjnlssearch")
	@ResponseBody
	@RequiresPermissions(value = { Permissions.MY_ACCOUNT_OVERVIEW_XWB })
	public DataTablesResponseDto<SubAcctTrxJnl> xWBJnlsSearch(
			HttpServletRequest request, HttpSession session, Model model,
			@RequestBody XWBTrxHistorySearchDto searchDto) {
		LOGGER.info("xWBJnlsSearch() invoked");
		DataTablesResponseDto<SubAcctTrxJnl> result = new DataTablesResponseDto<SubAcctTrxJnl>();
		result.setEcho(searchDto.getEcho());
		String userId = securityContext.getCurrentUserId();
		return xwbDetailService.getXWBTrxHistories(searchDto, userId);
	}

	/**
	 * 小微宝充值 Description: TODO
	 * 
	 * @param request
	 * @param session
	 * @param model
	 * @param transferDto
	 * @return
	 */
	@RequestMapping(value = "myaccount/xwbrecharge", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = { Permissions.MY_ACCOUNT_OVERVIEW_XWB })
	public ResultDto xWBRecharge(HttpServletRequest request,
			HttpSession session, Model model,
			@OnValid @RequestBody XWBTransferDto transferDto) {
		LOGGER.info("xWBRecharge() invoked");
		XWBTransferReq req = new XWBTransferReq();
		String userId = securityContext.getCurrentUserId();
		req.setUserId(userId);
		// req.setPassword(transferDto.getPassword());
		req.setAmount(transferDto.getAmount());
		req.setMemo(transferDto.getMemo());
		Date currentDate = CommonBusinessUtil.getCurrentWorkDate();
		req.setWorkDate(currentDate);
		req.setCurrentOpId(securityContext.getCurrentUserId());
		xwbDetailService.rechargeXWB(req);
		return ResultDtoFactory.toAck("小微宝转入成功");
	}

	/**
	 * 小微宝提现 Description: TODO
	 * 
	 * @param request
	 * @param session
	 * @param model
	 * @param transferDto
	 * @return
	 */
	@RequestMapping(value = "myaccount/xwbwithdrawal", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = { Permissions.MY_ACCOUNT_OVERVIEW_XWB })
	public ResultDto xWBWithdrawal(HttpServletRequest request,
			HttpSession session, Model model,
			@OnValid @RequestBody XWBTransferDto transferDto) {
		LOGGER.info("xWBWithdrawal() invoked");
		XWBTransferReq req = new XWBTransferReq();
		String userId = securityContext.getCurrentUserId();
		req.setUserId(userId);
		// req.setPassword(transferDto.getPassword());
		req.setAmount(transferDto.getAmount());
		req.setMemo(transferDto.getMemo());
		Date currentDate = CommonBusinessUtil.getCurrentWorkDate();
		req.setWorkDate(currentDate);
		req.setCurrentOpId(securityContext.getCurrentUserId());
		xwbDetailService.withdrawalXWB(req);
		return ResultDtoFactory.toAck("小微宝转出成功");
	}

}
