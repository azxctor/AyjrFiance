/*
 * Project Name: kmfex-platform
 * File Name: FinanceProductController.java
 * Class Name: FinanceProductController
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.kie.api.task.model.Status;
import org.kie.api.task.model.TaskSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hengxin.platform.common.controller.BaseController;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.common.service.ExtendJbpmService;
import com.hengxin.platform.common.util.WebUtil;
import com.hengxin.platform.product.dto.JbpmProductDto;
import com.hengxin.platform.product.dto.ProductListDto;
import com.hengxin.platform.security.SecurityContext;

/**
 * 
 * Class Name: JbpmProductController Description: TODO
 * 
 * @author minhuang
 * 
 */
@Controller
@RequestMapping(value = "/product/jbpm")
public class JbpmProductController extends BaseController {

    @Autowired
    private ExtendJbpmService extendJbpmService;

    @Autowired
    private SecurityContext securityContext;

    @Autowired
    private transient WebUtil webUtil;

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String getcreate(HttpServletRequest request, HttpSession session, Model model) {
        return "/product/jbpm/jbpmproduct_create";
    }

    /**
     * 
     * Description: TODO
     * 
     * @param jbpmProductDto
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResultDto create(@RequestBody JbpmProductDto jbpmProductDto, HttpServletRequest request,
            HttpSession session, ModelMap model) {
        final String userId = securityContext.getCurrentUserId();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("productId", jbpmProductDto.getProductId());
        params.put("approve", true);
        extendJbpmService.createProcess("com.hengxin.platform.product.finance.process", params);
        List<TaskSummary> list = extendJbpmService.getJbpmService().getTasksByUser(userId, "en-UK");
        extendJbpmService.doTask(params, userId, list.get(0).getId());
        return ResultDtoFactory.toRedirect(webUtil.getFullUrlBasedOn("/web/product/jbpm/list/"));
    }

    /**
     * 
     * Description: TODO
     * 
     * @param request
     * @param model
     * @param userId
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(HttpServletRequest request, Model model) {
        final String userId = securityContext.getCurrentUserId();
        List<TaskSummary> taskList = extendJbpmService.getJbpmService().getTasksByUser(userId, "en-UK");
        List<ProductListDto> productListDtos = new ArrayList<ProductListDto>();
        for (TaskSummary taskSummary : taskList) {
            final long piId = extendJbpmService.getProcessInstanceIdByTaskId(userId, taskSummary.getId());
            Map<String, Object> varMap = extendJbpmService.getJbpmService().getProcessInstanceVariables(
                    String.valueOf(piId));
            final String productId = (String) (varMap.get("Process_ProductId"));
            ProductListDto productListDto = new ProductListDto();
            productListDto.setProductId(productId);
            productListDto.setProductName(productId + "融资包");
            productListDtos.add(productListDto);
        }
        model.addAttribute("productListDtos", productListDtos);
        return "/product/jbpm/jbpmproduct_list";
    }

    /**
     * 
     * Description: TODO
     * 
     * @param jbpmProductDto
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/approve", method = RequestMethod.GET)
    @ResponseBody
    public ResultDto approve(@RequestBody JbpmProductDto jbpmProductDto, HttpServletRequest request, Model model) {
        final String userId = securityContext.getCurrentUserId();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("productId", jbpmProductDto.getProductId());
        params.put("approve", jbpmProductDto.isApprove());
        List<Status> status = new ArrayList<Status>();
        status.add(Status.Reserved);
        final TaskSummary task = extendJbpmService
                .getTaskByIdAndStatusAndProcessId("com.hengxin.platform.product.finance.process", userId, status,
                        jbpmProductDto.getProductId(), null, null);
        extendJbpmService.doTask(params, userId, task.getId());
        return ResultDtoFactory.toRedirect(webUtil.getFullUrlBasedOn("/web/product/jbpm/list/"));
    }
}
