/*
 * Project Name: kmfex-platform
 * File Name: FinanceRetreatController.java
 * Class Name: FinanceRetreatController
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hengxin.platform.common.constant.ApplicationConstant;
import com.hengxin.platform.common.controller.BaseController;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.util.MessageUtil;
import com.hengxin.platform.product.dto.RetreatDto;
import com.hengxin.platform.product.enums.EProductStatus;
import com.hengxin.platform.product.service.ProductRetreatService;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;

/**
 * Class Name: FinanceRetreatController
 * 
 * @author chunlinwang
 * 
 */
@Controller
@RequestMapping(value = "product")
public class FinanceRetreatController extends BaseController {
    @Autowired
    private SecurityContext securityContext;
    @Autowired
    ProductRetreatService productRetreatService;

    /**
     * 
    * Description: 回退操作
    *
    * @param retreatDto
    * @param request
    * @param session
    * @param model
    * @return
     */
    @RequestMapping(value = "retreat", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_PLATFORM_APPROVE,
            Permissions.PRODUCT_FINANCING_PLATFORM_RATING, Permissions.PRODUCT_FINANCING_DEPOSIT_FREEZE,
            Permissions.PRODUCT_FINANCING_TRANSACTION_SETTINGS }, logical = Logical.OR)
    public ResultDto retreatProduct(@RequestBody RetreatDto retreatDto, HttpServletRequest request,
            HttpSession session, Model model) {

        EProductStatus result = productRetreatService.retreatProduct(retreatDto.getProductId(), retreatDto.getStatus(),
                retreatDto.getComment(), securityContext.getCurrentUserId());
        return new ResultDto(ApplicationConstant.OPERATE_SUCCESS_FLAG,
                MessageUtil.getMessage("product.retreat.success"), result);
    }

}
