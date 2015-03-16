package com.hengxin.platform.product.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.entity.ActionHistoryPo;
import com.hengxin.platform.common.enums.EntityType;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.service.ActionHistoryService;
import com.hengxin.platform.common.util.PaginationUtil;
import com.hengxin.platform.member.dto.ActionHistoryDto;
import com.hengxin.platform.product.dto.ProductLogDTO;
import com.hengxin.platform.product.repository.ProductRepository;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.service.UserService;

@Controller
@RequestMapping(value = "product")
public class FinanceLogController {

    @Autowired
    private SecurityContext securityContext;

    @Autowired
    private ActionHistoryService actionHistoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductRepository productRepository;

    /**
     * 
    * Description: 查询日志
    *
    * @param productLogDTO
    * @param request
    * @param session
    * @param model
    * @return
    * @throws BizServiceException
     */
    @RequestMapping(value = "productLog")
    @ResponseBody
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_VIEW,
            Permissions.PRODUCT_FINANCIER_FINANCING_STATUS_VIEW, Permissions.MEMBER_FINANCING_APPLY_VIEW }, logical = Logical.OR)
    public DataTablesResponseDto<ActionHistoryDto> getProductLogList(@RequestBody ProductLogDTO productLogDTO,
            HttpServletRequest request, HttpSession session, Model model) throws BizServiceException {

        DataTablesResponseDto<ActionHistoryDto> result = new DataTablesResponseDto<ActionHistoryDto>();
        if (productLogDTO == null || StringUtils.isBlank(productLogDTO.getProductId())) {
            return result;
        }

        Pageable pageRequest = PaginationUtil.buildPageRequest(productLogDTO);
        Page<ActionHistoryPo> pageProductLogs = this.actionHistoryService.findProductLogByProductId(
                productLogDTO.getProductId(), pageRequest);
        if (pageProductLogs == null) {
            return result;
        }

        List<ActionHistoryDto> actionHistoryDtos = new ArrayList<ActionHistoryDto>();
        for (ActionHistoryPo actionHistoryPo : pageProductLogs) {
            if (actionHistoryPo.getEntityType() == EntityType.FUND) {
                continue;
            }
            ActionHistoryDto acHistoryDto = ConverterService.convert(actionHistoryPo, ActionHistoryDto.class);
//            StringBuffer sBuffer = new StringBuffer();
//            sBuffer.append(acHistoryDto.getResult()).append(": ").append(acHistoryDto.getComment());
//            acHistoryDto.setComment(sBuffer.toString());

            if (actionHistoryPo.getUser() != null) {
                acHistoryDto.setOperateUser(actionHistoryPo.getUser().getName());
            }

            actionHistoryDtos.add(acHistoryDto);
        }

        result.setEcho(productLogDTO.getEcho());
        result.setData(actionHistoryDtos);
        result.setTotalDisplayRecords(pageProductLogs.getTotalElements());
        result.setTotalRecords(pageProductLogs.getTotalElements());

        return result;
    }
}
