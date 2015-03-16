/*
 * Project Name: kmfex-platform
 * File Name: FinanceRevokeController.java
 * Class Name: FinanceRevokeController
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
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.product.dto.RetreatDto;
import com.hengxin.platform.product.service.FinanceRevokeService;
import com.hengxin.platform.product.service.ProductService;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.enums.EBizRole;

/**
 * Class Name: FinanceRevokeController
 * 
 * @author chunlinwang
 * 
 */
@Controller
@RequestMapping(value = "product")
public class FinanceRevokeController extends BaseController {
    @Autowired
    private SecurityContext securityContext;

    @Autowired
    private ProductService productService;

    @Autowired
    private FinanceRevokeService financeRevokeService;

    /**
     * 
    * Description: 项目撤单
    *
    * @param revokeDto
    * @param request
    * @param session
    * @param model
    * @return
     */
    @RequestMapping(value = "revoke", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_WAIT_PUBLISH_CANCEL,
            Permissions.PRODUCT_FINANCING_PROJECT_CANCEL, Permissions.PRODUCT_FINANCING_PROJECT_APPLY_FOR_FINANCIER }, logical = Logical.OR)
    public ResultDto productRevoke(@RequestBody RetreatDto revokeDto, HttpServletRequest request, HttpSession session,
            Model model) {
        EBizRole role = null;
        if (securityContext.isProdServ()) {
            role = EBizRole.PROD_SERV;
        } else if (securityContext.hasPermission(Permissions.PRODUCT_FINANCING_WAIT_PUBLISH_CANCEL)) {
            role = EBizRole.TRANS_MANAGER;
        }
        financeRevokeService.productRevoke(revokeDto.getProductId(), securityContext.getCurrentUserId(),
                revokeDto.getComment(), role);

        return ResultDtoFactory.toAck(ApplicationConstant.REVOKE_SUCCESS_FLAG, "Success");

    }
}
