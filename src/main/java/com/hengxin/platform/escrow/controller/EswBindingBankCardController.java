package com.hengxin.platform.escrow.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.common.dto.annotation.OnValid;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.ebc.dto.bank.BankInfo;
import com.hengxin.platform.escrow.dto.EswBankDto;
import com.hengxin.platform.escrow.dto.EswEditBankDto;
import com.hengxin.platform.escrow.dto.bank.City;
import com.hengxin.platform.escrow.dto.bank.PayeeBank;
import com.hengxin.platform.escrow.dto.bank.Province;
import com.hengxin.platform.escrow.entity.EswAcctPo;
import com.hengxin.platform.escrow.enums.EOrderStatusEnum;
import com.hengxin.platform.escrow.service.EswAcctService;
import com.hengxin.platform.escrow.service.EswBankCardOrderService;
import com.hengxin.platform.escrow.service.EswBindingService;
import com.hengxin.platform.member.repository.UserRepository;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.entity.UserPo;
import com.hengxin.platform.security.enums.BindingCardStatusEnum;

@Controller
public class EswBindingBankCardController {

	@Autowired
	private transient SecurityContext securityContext;

	@Autowired
	private EswBindingService eswBindingService;

	@Autowired
	private EswAcctService eswAcctService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EswBankCardOrderService eswBankCardOrderService;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(EswBindingBankCardController.class);

	@RequestMapping(value = "esw/bindingui")
	@RequiresPermissions(value = { Permissions.ESW_ACCT_MGT })
	public String doBindingUi(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		String userId = securityContext.getCurrentUserId();
		List<PayeeBank> bankList = eswBindingService.getPayeeBankInfo(userId);
		List<Province> provinceList = eswBindingService.getProvinceInfo(userId);
		EswAcctPo eswAcct = eswAcctService.getEscrowAccountByUserId(userId);
		// boolean flag = StringUtils.isNotBlank(eswAcct.getBankAssetsId());
		UserPo userPo = userRepository.findUserByUserId(userId);
		BindingCardStatusEnum status = userPo.getBindingCardStatus();
		if (BindingCardStatusEnum.DONE.equals(status)) {
			String bankCardName = eswAcct.getBankCardName();
			String bankCardNo = eswAcct.getBankCardNo();
			String payeeBankId = eswAcct.getBankTypeCode();
			String provinceCode = eswAcct.getProvCode();
			String cityCode = eswAcct.getCityCode();
			String bankName = eswAcct.getBankName();
			EswBankDto dto = new EswBankDto(bankCardNo, null, null,
					payeeBankId, provinceCode, cityCode, bankName, bankCardName);
			model.addAttribute("bankDto", dto);
		}
		model.addAttribute("bankList", bankList);
		model.addAttribute("provinceList", provinceList);
		model.addAttribute("status", status);
		return "escrow/binding";
	}

	@ResponseBody
	@RequiresPermissions(value = { Permissions.ESW_ACCT_MGT })
	@RequestMapping(value = "esw/binding", method = RequestMethod.POST)
	public ResultDto doBinding(HttpServletRequest request,
			HttpServletResponse response, @OnValid @RequestBody EswBankDto dto) {
		LOGGER.debug("doBinding() start: ");
		StringBuffer errMsg = new StringBuffer();
		EOrderStatusEnum status = null;
		try {
			status = eswBindingService.binding(securityContext.getCurrentUserId(), dto);
		} catch (BizServiceException e) {
			e.printStackTrace();
			LOGGER.debug(e.getMessage());
			errMsg.append(e.getMessage());
			return ResultDtoFactory.toNack(errMsg.toString());
		}
		if (status.equals(EOrderStatusEnum.SUCCESS)) 
			return ResultDtoFactory.toAck("绑卡成功");
		else
			return ResultDtoFactory.toAck("处理中");
		
	}
	
	@ResponseBody
	@RequiresPermissions(value = { Permissions.ESW_ACCT_MGT })
	@RequestMapping(value = "esw/unbinding", method = RequestMethod.GET)
	public ResultDto unBinding(HttpServletRequest request,
			HttpServletResponse response) {
		LOGGER.debug("unBinding() start: ");
		try {
			eswBindingService.unbinding(securityContext.getCurrentUserId());
		} catch (BizServiceException e) {
			e.printStackTrace();
			LOGGER.debug(e.getMessage());
			return ResultDtoFactory.toNack("解绑失败");
		}
		return ResultDtoFactory.toAck("解绑成功");
	}

	@ResponseBody
	@RequiresPermissions(value = { Permissions.ESW_ACCT_MGT })
	@RequestMapping(value = "esw/editbinding", method = RequestMethod.POST)
	public ResultDto editBinding(HttpServletRequest request,
			HttpServletResponse response,
			@OnValid @RequestBody EswEditBankDto dto) {
		LOGGER.debug("doEditBinding() start: ");
		EOrderStatusEnum status = eswBindingService.editBinding(
				securityContext.getCurrentUserId(), dto);
		if (status.equals(EOrderStatusEnum.SUCCESS)) {
			return ResultDtoFactory.toAck("修改绑卡信息成功");
		} else {
			return ResultDtoFactory.toNack("修改绑卡信息失败");
		}
	}

	@ResponseBody
	@RequestMapping(value = "esw/getPayeeBanInfo")
	public List<PayeeBank> getPayeeBankInfo(HttpServletRequest request,
			HttpServletResponse response) {
		return eswBindingService.getPayeeBankInfo(securityContext
				.getCurrentUserId());
	}

	@ResponseBody
	@RequestMapping(value = "esw/getProvinceInfo")
	public List<Province> getProvinceInfo(HttpServletRequest request,
			HttpServletResponse response) {
		return eswBindingService.getProvinceInfo(securityContext
				.getCurrentUserId());
	}

	@ResponseBody
	@RequestMapping(value = "esw/getCityInfo")
	public List<City> getCityInfo(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String provinceCode) {
		return eswBindingService.getCityInfo(
				securityContext.getCurrentUserId(), provinceCode);
	}

	@ResponseBody
	@RequestMapping(value = "esw/getBankInfo")
	public BankInfo getBankInfo(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String payeeBankId,
			@RequestParam String cityCode) {
		return eswBindingService.getBankInfo(
				securityContext.getCurrentUserId(), payeeBankId, cityCode);
	}
	
	@ResponseBody
	@RequestMapping(value = "esw/getBindedBankInfo")
	public EswBankDto getBindedBankInfo(HttpServletRequest request,
			HttpServletResponse response,@RequestParam String bankCardNo) {
		return eswBindingService.getBindedBankInfo(securityContext.getCurrentUserId(),bankCardNo);
	}

	@ResponseBody
	@RequestMapping(value = "esw/getBindedBankList")
	public DataTablesResponseDto<EswBankDto> getBindedBankList(HttpServletRequest request,
			HttpServletResponse response) {
		List<EswBankDto> bankDtoList = eswBindingService.getBindedBankList(securityContext.getCurrentUserId());
		DataTablesResponseDto<EswBankDto> responseDtoList = new DataTablesResponseDto<EswBankDto>();
		responseDtoList.setData(bankDtoList);
		responseDtoList.setEcho("1");
		responseDtoList.setTotalDisplayRecords(bankDtoList.size());
		responseDtoList.setTotalRecords(bankDtoList.size());
		return responseDtoList;
	}
}
