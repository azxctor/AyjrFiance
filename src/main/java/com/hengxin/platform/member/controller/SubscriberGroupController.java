package com.hengxin.platform.member.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.hengxin.platform.common.dto.annotation.OnValid;
import com.hengxin.platform.common.util.MessageUtil;
import com.hengxin.platform.common.util.WebUtil;
import com.hengxin.platform.member.domain.ServiceCenterInfo;
import com.hengxin.platform.member.dto.GroupSearchDto;
import com.hengxin.platform.member.dto.InvestorSearchDto;
import com.hengxin.platform.member.dto.SkinnyUserDto;
import com.hengxin.platform.member.dto.SubscribeGroupDto;
import com.hengxin.platform.member.dto.upstream.ServiceCenterDto;
import com.hengxin.platform.member.service.ServiceCenterService;
import com.hengxin.platform.member.service.SubscribeGroupService;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;

@Controller
public class SubscriberGroupController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriberGroupController.class);

    @Autowired
    private transient SubscribeGroupService subscribeGroupService;

    @Autowired
    private transient ServiceCenterService serviceCenterService;
    
    @Autowired
    private transient WebUtil webUtil;

    @Autowired
    private transient SecurityContext securityContext;

    /**
     * 分页查询投资用户
     *
     * @param model
     * @param response
     * @return
     */
    @RequestMapping(value = "subscriber/groupinfo/investor")
    @ResponseBody
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_SUBSCRIBERGROUP })
    public DataTablesResponseDto<SkinnyUserDto> getInvestorInfo(@RequestBody InvestorSearchDto investorSearch) {
        DataTablesResponseDto<SkinnyUserDto> investors = subscribeGroupService.getInvestorInfos(investorSearch);
        return investors;
    }

    /**
     * 访问页面
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value=UrlConstant.PARAM_MANAGEMENT_SUBSCRIBER_URL, method = RequestMethod.GET)
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_SUBSCRIBERGROUP})
    public String getSubscribeGroupInfoList(HttpServletRequest request, Model model) {
    	List<ServiceCenterDto> serviceCenterDtoList = new ArrayList<ServiceCenterDto>();
        List<ServiceCenterInfo> allServiceCenters = serviceCenterService.getAllServiceCentersWithoutRegion();
        for (ServiceCenterInfo domain : allServiceCenters) {
            serviceCenterDtoList.add(ConverterService.convert(domain, ServiceCenterDto.class));
        }
        model.addAttribute("serviceCenterList", serviceCenterDtoList);
        return "members/subscribe_group";
    }

    /**
     * 查询定投组信息
     *
     * @param groupSearch
     * @return
     */
    @RequestMapping(value = "subscriber/getgroupinfo")
    @ResponseBody
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_SUBSCRIBERGROUP})
    public DataTablesResponseDto<SubscribeGroupDto> getSubscribeGroupInfoList(@RequestBody GroupSearchDto groupSearch) {
        LOGGER.debug("getSubscribeGroupInfoList() invoked");
        DataTablesResponseDto<SubscribeGroupDto> group = subscribeGroupService.getGroupInfo(groupSearch);
        return group;
    }

    /**
     * 查询单个定投组信息.
     *
     * @param groupId
     * @param model
     * @return
     */
    @RequestMapping(value = "subscriber/groupinfo/{groupId}")
    @ResponseBody
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_SUBSCRIBERGROUP})
    public SubscribeGroupDto getGroupInfo(@PathVariable("groupId") String groupId, Model model) {
        SubscribeGroupDto group = subscribeGroupService.findGruopInfo(Integer.parseInt(groupId));
        return group;
    }

    /** Submit methods */
    @RequestMapping(value = "subscriber/groupinfo/save", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_SUBSCRIBERGROUP}, logical = Logical.OR)
    public ResultDto saveGroupInfo(@OnValid @RequestBody SubscribeGroupDto group, HttpServletRequest request,
            Model model) throws Exception {
        LOGGER.debug("saveGroupInfo() invoked");
        this.subscribeGroupService.saveGroup(group, securityContext.getCurrentUserId());
        return ResultDtoFactory.toAck(ApplicationConstant.SAVE_SUCCESS_MESSAGE);
    }

    /** Submit methods */
    @Deprecated
    @RequestMapping(value = "subscriber/groupinfo/delete/{groupId}", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_SUBSCRIBERGROUP}, logical = Logical.OR)
    public ResultDto deleteGroupInfo(@PathVariable("groupId") String groupId, HttpServletRequest request, Model model) throws Exception {
        LOGGER.debug("deleteGroupInfo() invoked");
        boolean flag = this.subscribeGroupService.deleteGroup(Integer.parseInt(groupId));
        if (flag) {
        	return ResultDtoFactory.toAck(ApplicationConstant.SAVE_SUCCESS_MESSAGE);
		} else {
			return ResultDtoFactory.toAck(MessageUtil.getMessage("member.error.subscribergroup.inuse"));
		}
    }
    
    @RequestMapping(value = "subscriber/groupinfo/active/{groupId}", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_SUBSCRIBERGROUP}, logical = Logical.OR)
    public ResultDto activeGroupInfo(@PathVariable("groupId") String groupId, HttpServletRequest request, Model model) throws Exception {
        LOGGER.debug("deleteGroupInfo() invoked");
        this.subscribeGroupService.updateGroupStatus(Integer.parseInt(groupId), true);
    	return ResultDtoFactory.toAck(MessageUtil.getMessage("member.subscribergroup.active.success"));
    }

    @RequestMapping(value = "subscriber/groupinfo/deactive/{groupId}", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_SUBSCRIBERGROUP}, logical = Logical.OR)
    public ResultDto deactiveGroupInfo(@PathVariable("groupId") String groupId, HttpServletRequest request, Model model) throws Exception {
        LOGGER.debug("deleteGroupInfo() invoked");
        this.subscribeGroupService.updateGroupStatus(Integer.parseInt(groupId), false);
    	return ResultDtoFactory.toAck(MessageUtil.getMessage("member.subscribergroup.deactive.success"));
    }
}
