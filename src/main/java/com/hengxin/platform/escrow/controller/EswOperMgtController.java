package com.hengxin.platform.escrow.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hengxin.platform.batch.service.JobWorkService;
import com.hengxin.platform.common.controller.BaseController;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.common.util.CommonBusinessUtil;

@Controller
public class EswOperMgtController extends BaseController {

	@Autowired
	private JobWorkService jobWorkService;

	@ResponseBody
	@RequestMapping(value = "esw/isAllowConnect", method = RequestMethod.GET)
	public ResultDto isAllowEswConnect(HttpServletRequest request, Model model){
		boolean bool = CommonBusinessUtil.isMarketOpen();
		if(!bool){
			return ResultDtoFactory.toNack("当前闭市状态，请开市之后再做交易...");
		}
		if (jobWorkService.isBatchBizTaskProcessing()) {
			return ResultDtoFactory.toNack("当前批量处理中，请稍微操作...");
		}
		return ResultDtoFactory.toAck("成功");
	}
}
