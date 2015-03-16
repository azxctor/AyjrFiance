/*
 * Project Name: kmfex-platform
 * File Name: FinanceAuditController.java
 * Class Name: FinanceAuditController
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

package com.hengxin.platform.product.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hengxin.platform.common.constant.ApplicationConstant;
import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.util.MessageUtil;
import com.hengxin.platform.member.enums.EMessageType;
import com.hengxin.platform.member.service.MemberMessageService;
import com.hengxin.platform.product.domain.Product;
import com.hengxin.platform.product.domain.ProductContractTemplate;
import com.hengxin.platform.product.dto.AuditDto;
import com.hengxin.platform.product.dto.ProductContractTemplateDto;
import com.hengxin.platform.product.repository.ProductRepository;
import com.hengxin.platform.product.service.FinanceAuditService;
import com.hengxin.platform.product.service.ProductService;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.enums.EBizRole;

/**
 * Class Name: FinanceAuditController Description: TODO
 * 
 * @author huanbinzhang
 * 
 */

@Controller
@RequestMapping(value = "product")
public class FinanceAuditController {
	@Autowired
	private SecurityContext securityContext;

	@Autowired
	private ProductService productService;

	@Autowired
	private FinanceAuditService financeAuditService;

	@Autowired
	private MemberMessageService messageService;

	@Autowired
	private ProductRepository productRepository;

	/**
	 * 
	 * Description: 项目审核
	 * 
	 * @return
	 */
	@RequestMapping(value = "getTemplateList", method = RequestMethod.GET)
	@ResponseBody
	@RequiresPermissions({ Permissions.PRODUCT_FINANCING_PLATFORM_APPROVE })
	public List<ProductContractTemplateDto> getTemplateList() {
		List<ProductContractTemplate> templateList = productService
				.getAllTemplateList();
		List<ProductContractTemplateDto> templateDtoList = new ArrayList<ProductContractTemplateDto>();
		for (ProductContractTemplate template : templateList) {
			ProductContractTemplateDto templateDto = ConverterService.convert(
					template, ProductContractTemplateDto.class);
			templateDtoList.add(templateDto);
		}
		return templateDtoList;
	}

	@RequestMapping(value = "audit", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions({ Permissions.PRODUCT_FINANCING_PLATFORM_APPROVE })
	public ResultDto productAudit(@RequestBody AuditDto auditDto,
			HttpServletRequest request, HttpSession session, Model model) {
		financeAuditService
				.productAudit(auditDto.getProductId(), auditDto.isPassed(),
						auditDto.getTemplateId(), auditDto.getComments(),
						securityContext.getCurrentUserId(),
						auditDto.getOverdueToCompDays(),
						auditDto.getCompensatoryDays());
		
		sendAuditMessage(auditDto);
		
		return new ResultDto(ApplicationConstant.OPERATE_SUCCESS_FLAG,
				MessageUtil.getMessage("product.approve.success"), null);
	}

	/**
	 * 项目审核后发送待办
	 * 
	 * @param auditDto
	 */
	private void sendAuditMessage(AuditDto auditDto) {
		String messageKey = null;
		Product product = StringUtils.isBlank(auditDto.getProductId()) ? null
				: productRepository.findOne(auditDto.getProductId());
		messageKey = "product.audit.financier.message";
		messageService.sendMessage(EMessageType.TODO, messageKey,
				product.getApplUserId(), product.getProductName(),
				auditDto.isPassed() ? "通过" : "拒绝");
		
		messageKey = "product.audit.guarantor.message";
		messageService.sendMessage(EMessageType.TODO, messageKey,
				product.getWarrantId(), product.getProductName(),
				auditDto.isPassed() ? "通过" : "拒绝");

		if (auditDto.isPassed()) {
			messageKey = "product.audit.riskcontrol.message";
			List<EBizRole> roleList = Arrays.asList(
					EBizRole.PLATFORM_RISK_CONTROL_MAKER, 
					EBizRole.PLATFORM_RISK_CONTROL_POST_LENDING);
			messageService.sendMessage(EMessageType.TODO, messageKey,
					roleList, product.getProductName());
		}
	}
}
