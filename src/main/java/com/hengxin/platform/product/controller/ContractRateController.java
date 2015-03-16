package com.hengxin.platform.product.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hengxin.platform.common.constant.UrlConstant;
import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.product.domain.ContractRate;
import com.hengxin.platform.product.domain.ProductContractTemplate;
import com.hengxin.platform.product.dto.ContractRateDto;
import com.hengxin.platform.product.dto.ContractRatePKDto;
import com.hengxin.platform.product.dto.ProductContractTemplateDto;
import com.hengxin.platform.product.enums.ERiskLevel;
import com.hengxin.platform.product.service.ContractRateService;
import com.hengxin.platform.product.service.ProductService;
import com.hengxin.platform.security.Permissions;

/**
 * ContractRateController
 * 
 * @author chenwulou
 *
 */
@Controller
public class ContractRateController {

	private static final String Y = "Y";

	@Autowired
	ContractRateService contractRateService;

	@Autowired
	private ProductService productService;

	@RequestMapping(value = UrlConstant.CONTRACT_RATE_MAINTAIN_URL, method = RequestMethod.GET)
	@RequiresPermissions(value = Permissions.TRANS_CONTRACT_RATE_MAINTAIN)
	public String contractRateHome(Model model) {

		List<ProductContractTemplate> templateList = productService.getAllTemplateList();
		List<ProductContractTemplateDto> templateDtoList = new ArrayList<ProductContractTemplateDto>();
		for (ProductContractTemplate template : templateList) {
			ProductContractTemplateDto templateDto = ConverterService.convert(template,
					ProductContractTemplateDto.class);
			templateDtoList.add(templateDto);
		}

		model.addAttribute("riskLevels", EnumHelper.inspectConstants(ERiskLevel.class, false));
		model.addAttribute("templateDtoList", templateDtoList);

		return "product/contract_rate";
	}

	@RequestMapping(value = "contractRate/save", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = Permissions.TRANS_CONTRACT_RATE_MAINTAIN)
	public ResultDto saveContractRate(@RequestBody ContractRateDto contractRateDto) {
		boolean result = contractRateService.validateAndSave(contractRateDto);
		if (result) {
			return ResultDtoFactory.toAck("保存成功");
		}
		return ResultDtoFactory.toNack("相同的合同及风险等级配置已存在");
	}

	@RequestMapping(value = "contractRate/delete", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = Permissions.TRANS_CONTRACT_RATE_MAINTAIN)
	public ResultDto deleteContractRate(@RequestBody ContractRatePKDto contractRatePKDto) {
		contractRateService.delete(contractRatePKDto);
		return ResultDtoFactory.toAck("删除成功");
	}

	@RequestMapping(value = "contractRate/update", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = Permissions.TRANS_CONTRACT_RATE_MAINTAIN)
	public ResultDto updateContractRate(@RequestBody ContractRateDto contractRateDto) {
		boolean result = contractRateService.update(contractRateDto);
		if (result) {
			return ResultDtoFactory.toAck("更新成功");
		}
		return ResultDtoFactory.toNack("更新失败");
	}

	@RequestMapping(value = "contractRate/getlist", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value = Permissions.TRANS_CONTRACT_RATE_MAINTAIN)
	public DataTablesResponseDto<ContractRateDto> getContractRateDtoList(
			@RequestBody ContractRatePKDto contractRatePKDto) {
		DataTablesResponseDto<ContractRateDto> result = new DataTablesResponseDto<ContractRateDto>();
		Page<ContractRate> pageSms = contractRateService.findAll(contractRatePKDto);
		if (pageSms == null) {
			return result;
		}
		List<ContractRateDto> listCrDto = new ArrayList<ContractRateDto>();

		for (ContractRate cr : pageSms) {

			ContractRateDto crDto = ConverterService.convert(cr, ContractRateDto.class);
			crDto.setPaymentRate(cr.getPaymentPenaltyFineRate().toPlainString());
			crDto.setFinancierRate(cr.getFinancierPrepaymentPenaltyRate().toPlainString());
			crDto.setPlatformRate(cr.getPlatformPrepaymentPenaltyRate().toPlainString());
			crDto.setContractName(cr.getContract().getTemplateName());
			crDto.setContractId(cr.getContract().getTemplateId());
			crDto.setProductLevelTextShort(EnumHelper.translate(ERiskLevel.class, cr.getProductLevelId())
					.getTextShort());
			if (cr.getPrepayDeductIntrFlg() != null) {
				if (Y.equals(cr.getPrepayDeductIntrFlg())) {
					crDto.setDeductFlg(true);
				} else {
					crDto.setDeductFlg(false);
				}
			}
			listCrDto.add(crDto);
		}
		result.setEcho(contractRatePKDto.getEcho());
		result.setTotalRecords(pageSms.getTotalElements());
		result.setTotalDisplayRecords(pageSms.getTotalElements());
		result.setData(listCrDto);

		return result;

	}

}
