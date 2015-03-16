/*
 * Project Name: kmfex-platform
 * File Name: FinanceFreezeController.java
 * Class Name: FinanceFreezeController
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
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.annotation.OnValid;
import com.hengxin.platform.common.util.MessageUtil;
import com.hengxin.platform.product.dto.FreezeDto;
import com.hengxin.platform.product.service.ProductFreezeService;
import com.hengxin.platform.security.Permissions;

/**
 * Class Name: FinanceFreezeController
 * 
 * @author chunlinwang
 * 
 */
@Controller
@RequestMapping(value = "product")
public class FinanceFreezeController extends BaseController {

    @Autowired
    ProductFreezeService ProductFreezeService;

    @RequestMapping(value = "freeze", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value=Permissions.PRODUCT_FINANCING_DEPOSIT_FREEZE)
    public ResultDto evaluateProduct(@OnValid @RequestBody FreezeDto freezeProduct, HttpServletRequest request,
            HttpSession session, Model model) {

        // try {
        ProductFreezeService.freezeProduct(freezeProduct.getProductId(), freezeProduct.getServMrgnAmt(),
                freezeProduct.getWrtrMrgnRt());
        // } catch (BizServiceException e) {
        // if (EErrorCode.NO_ENOUGH_FINANCIER_BALANCE == e.getError()) {
        // return new ResultDto(code, message, data)
        // return ResultDtoFactory.toNack("Failed", "FINANCIER_INSUFFICINET");
        // } else if ((EErrorCode.NO_ENOUGH_GUARANTOR_BALANCE == e.getError())) {
        // return ResultDtoFactory.toNack("Failed", "GUARANTOR_INSUFFICINET");
        //
        // }
        // throw e;
        // }

        return new ResultDto(ApplicationConstant.OPERATE_SUCCESS_FLAG,
                MessageUtil.getMessage("product.freeze.success"), null);
    }
}
