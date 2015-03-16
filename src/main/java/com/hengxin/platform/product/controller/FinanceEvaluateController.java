/*
 * Project Name: kmfex-platform
 * File Name: FinanceProductEvaluateController.java
 * Class Name: FinanceProductEvaluateController
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
import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.annotation.OnValid;
import com.hengxin.platform.common.util.MessageUtil;
import com.hengxin.platform.product.domain.Product;
import com.hengxin.platform.product.dto.EvaluateDto;
import com.hengxin.platform.product.service.ProductEvaluateService;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;

/**
 * Class Name: FinanceProductEvaluateController
 * 
 * @author chunlinwang
 * 
 */
@Controller
@RequestMapping(value = "product")
public class FinanceEvaluateController extends BaseController {

    @Autowired
    ProductEvaluateService productEvaluateService;

    @Autowired
    private SecurityContext securityContext;

    /**
     * 
    * Description: 项目评级
    *
    * @param evaluateDto
    * @param request
    * @param session
    * @param model
    * @return
     */
    @RequestMapping(value = "evaluate", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value=Permissions.PRODUCT_FINANCING_PLATFORM_RATING)
    public ResultDto evaluateProduct(@OnValid @RequestBody EvaluateDto evaluateDto, HttpServletRequest request,
            HttpSession session, Model model) {

        Product product = ConverterService.convert(evaluateDto, Product.class);
        product.setLastMntOpid(securityContext.getCurrentUserId());
        productEvaluateService.evaluateProduct(product);
        return new ResultDto(ApplicationConstant.OPERATE_SUCCESS_FLAG,
                MessageUtil.getMessage("product.evaluation.success"), null);
    }
}
