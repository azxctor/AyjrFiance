/*
 * Project Name: kmfex-platform
 * File Name: FinanceListController.java
 * Class Name: FinanceListController
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hengxin.platform.common.constant.UrlConstant;
import com.hengxin.platform.common.controller.BaseController;
import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.domain.EnumOption;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.fund.entity.AcctPo;
import com.hengxin.platform.fund.service.AcctService;
import com.hengxin.platform.product.domain.Product;
import com.hengxin.platform.product.dto.ProductListDto;
import com.hengxin.platform.product.dto.ProductQueryConditionDto;
import com.hengxin.platform.product.enums.EProductActionType;
import com.hengxin.platform.product.enums.EProductStatus;
import com.hengxin.platform.product.enums.EResultType;
import com.hengxin.platform.product.enums.EWarrantyType;
import com.hengxin.platform.product.service.ProductListService;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;

/**
 * Class Name: FinanceListController
 * 
 * @author chunlinwang
 * 
 */
@Controller
public class FinanceListController extends BaseController {

    @Autowired
    private ProductListService productListService;

    @Autowired
    private SecurityContext securityContext;

    @Autowired
    private AcctService acctService;

    /**
     * 
     * Description: Render product list page for Financier
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCIER_FINANCING_STATUS_VIEW })
    @RequestMapping(value = UrlConstant.PRODUCT_FINANCIER_PROJECT_CHECK_URL)
    public String renderProductListForFinancier(HttpServletRequest request, HttpSession session, Model model) {
        EProductActionType actionType = EProductActionType.CHECK;
        if (securityContext.hasPermission(Permissions.PRODUCT_FINANCING_PROJECT_APPLY_FOR_FINANCIER)) {
            actionType = EProductActionType.COMMIT;
            model.addAttribute("financierApply", true);
            model.addAttribute("displayFlag", "CHECK");
        }
        model.addAttribute("actionType", actionType);
        this.addAllBasicOptionsToModel(model, actionType);
        return "product/financeproduct_list";
    }

    /**
     * 
     * Description: Render product list page for Warrant
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.PRODUCT_PROD_SERV_PROJECT_COMMIT_URL)
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_PROJECT_APPLY })
    public String renderProductListForWarrant(HttpServletRequest request, HttpSession session, Model model) {
        EProductActionType actionType = EProductActionType.COMMIT;
        model.addAttribute("actionType", actionType);
        this.addAllBasicOptionsToModel(model, actionType);
        return "product/financeproduct_list";
    }

    /**
     * 
     * Description: Render product list page for Approve
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.PRODUCT_RISK_CONTROL_PROJECT_APPROVE_URL)
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_PLATFORM_APPROVE }, logical = Logical.OR)
    public String renderProductListForApprove(HttpServletRequest request, HttpSession session, Model model) {
        EProductActionType actionType = EProductActionType.APPROVE;
        model.addAttribute("actionType", actionType);
        this.addAllBasicOptionsToModel(model, actionType);
        return "product/financeproduct_list";
    }

    /**
     * 
     * Description: Render product list page for Evaluate
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.PRODUCT_RISK_CONTROL_PROJECT_EVALUATE_URL)
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_PLATFORM_RATING })
    public String renderProductListForEvaluate(HttpServletRequest request, HttpSession session, Model model) {
        EProductActionType actionType = EProductActionType.EVALUATE;
        model.addAttribute("actionType", actionType);
        this.addAllBasicOptionsToModel(model, actionType);
        return "product/financeproduct_list";
    }

    /**
     * 
     * Description: Render product list page for Freeze
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.PRODUCT_RISK_CONTROL_PROJECT_FREEZE_URL)
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_DEPOSIT_FREEZE })
    public String renderProductListForFreeze(HttpServletRequest request, HttpSession session, Model model) {
        EProductActionType actionType = EProductActionType.FREEZE;
        model.addAttribute("actionType", actionType);
        this.addAllBasicOptionsToModel(model, actionType);
        return "product/financeproduct_list";
    }

    /**
     * 
     * Description: Render product list page for Publish
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.PRODUCT_TRANS_DEPT_PROJECT_PUBLISH_URL)
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_TRANSACTION_SETTINGS })
    public String renderProductListForPublish(HttpServletRequest request, HttpSession session, Model model) {
        EProductActionType actionType = EProductActionType.PUBLISH;
        model.addAttribute("actionType", actionType);
        this.addAllBasicOptionsToModel(model, actionType);
        return "product/financeproduct_list";
    }

    /**
     * 
     * Description: Render product list page for Search
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.PRODUCT_PLATFORM_PROJECT_SEARCH_RISK_URL)
    @RequiresPermissions(value = { Permissions.RISK_CONTROL_DEPT })
    public String renderProductListForRiskDeptSearch(HttpServletRequest request, HttpSession session, Model model) {
        EProductActionType actionType = EProductActionType.SEARCH;
        model.addAttribute("actionType", actionType);
        this.addAllBasicOptionsToModel(model, actionType);
        return "product/financeproduct_list";
    }

    /**
     * 
     * Description: Render product list page for Search
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.PRODUCT_PLATFORM_PROJECT_SEARCH_TRANS_URL)
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_PACKAGE_VIEW_FOR_TRANS })
    public String renderProductListForTransDeptSearch(HttpServletRequest request, HttpSession session, Model model) {
        return this.renderProductListForRiskDeptSearch(request, session, model);
    }

    private void addAllBasicOptionsToModel(Model model, EProductActionType actionType) {
        if (actionType == EProductActionType.SEARCH) {
            model.addAttribute("productStatusList", getStaticOptions(EProductStatus.class, false));
        } else if (actionType == EProductActionType.CHECK || actionType == EProductActionType.COMMIT) {
            List<EnumOption> productStatusList = new ArrayList<EnumOption>();
            productStatusList.add(new EnumOption("STANDBY", "待提交"));
            productStatusList.add(new EnumOption("WAITTOAPPROVE", "审核中"));
            productStatusList.add(new EnumOption("WAITTOPUBLISH", "发布中"));
            productStatusList.add(new EnumOption("FINISHED", "发布完成"));
            productStatusList.add(new EnumOption("ABANDONED", "项目废弃"));
            model.addAttribute("productStatusList", productStatusList);
        } else if (actionType == EProductActionType.PUBLISH) {
            List<EnumOption> productStatusList = new ArrayList<EnumOption>();
            productStatusList.add(new EnumOption("WAITTOPUBLISH", "待发布"));
            productStatusList.add(new EnumOption("PRE_PUBLISH", "预发布"));
            productStatusList.add(new EnumOption("WAIT_SUBSCRIBE", "待申购"));
            productStatusList.add(new EnumOption("SUBSCRIBE", "申购中"));
            model.addAttribute("productStatusList", productStatusList);
        } else {
            model.addAttribute("resultList", getStaticOptions(EResultType.class, false));
        }
    }

    /**
     * product List
     * 
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "product/getproductlist")
    @ResponseBody
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_VIEW,
            Permissions.PRODUCT_FINANCIER_FINANCING_STATUS_VIEW, Permissions.MEMBER_FINANCING_APPLY_VIEW }, logical = Logical.OR)
    public DataTablesResponseDto<ProductListDto> getProductList(@RequestBody ProductQueryConditionDto query,
            HttpServletRequest request, HttpSession session, Model model) {

        DataTablesResponseDto<ProductListDto> result = new DataTablesResponseDto<ProductListDto>();
        if (securityContext.hasPermission(Permissions.PRODUCT_FINANCING_PROJECT_APPLY_FOR_FINANCIER)) {
            query.setProductActionType(EProductActionType.CHECK);
        }
        Page<Product> pageProducts = this.productListService.getProductList(query);

        if (pageProducts == null) {
            return result;
        }

        EProductStatus pStatus = null;
        try {
            pStatus = EProductStatus.valueOf(query.getComString());
        } catch (Exception e) {
            // pacStatus = EPackageStatus.valueOf(query.getComString());
        }

        List<ProductListDto> productListDtos = new ArrayList<ProductListDto>();
        for (Product product : pageProducts) {
            ProductListDto auditOperation = this.auditOperation(product, query.getProductActionType(), pStatus);
            if(product.getWarrantyType()==EWarrantyType.MONITORASSETS||product.getWarrantyType()==EWarrantyType.NOTHING){
                auditOperation.setGuaranteeInstitution("--");
                auditOperation.setGuaranteeInstitutionShow("--");
            }
            productListDtos.add(auditOperation);
        }
        result.setEcho(query.getEcho());
        result.setData(productListDtos);
        result.setTotalDisplayRecords(pageProducts.getTotalElements());
        result.setTotalRecords(pageProducts.getTotalElements());

        return result;
    }

    private ProductListDto auditOperation(Product product, EProductActionType productActionType, EProductStatus pStatus) {

        if (product.getStatus() == null) {
            product.setStatus(EProductStatus.NULL);
        }

        ProductListDto productListDto = ConverterService.convert(product, ProductListDto.class);       
        BigDecimal appliedQuotaUnit = product.getAppliedQuota();
        // BigDecimal appBigDecimal = appliedQuotaUnit.setScale(1, BigDecimal.ROUND_HALF_UP);
        productListDto.setAppliedQuotaUnit(appliedQuotaUnit.toString());
        if (product.getProductProviderInfo() != null && product.getProductProviderInfo().getUserPo() != null) {
            productListDto.setGuaranteeInstitution(product.getProductProviderInfo().getUserPo().getName());
        }
        if(product.getUserPoWrtrShow() != null){
            productListDto.setGuaranteeInstitutionShow(product.getUserPoWrtrShow().getName());
        }
        if (product.getUser() != null) {
            productListDto.setFinancierName(product.getUser().getName());
        }
        if (productListDto.getWarrantyType() == EWarrantyType.MONITORASSETS
                || productListDto.getWarrantyType() == EWarrantyType.NOTHING) {
            productListDto.setGuaranteeInstitution(null);
        }
        productListDto.setCanCheck(true);

        if (productActionType == EProductActionType.CHECK) {
            if (productListDto.getStatus() == EProductStatus.WAITTOAPPROVE
                    || productListDto.getStatus() == EProductStatus.WAITTOEVALUATE
                    || productListDto.getStatus() == EProductStatus.WAITTOFREEZE) {
                productListDto.setStatus(EProductStatus.WAITTOAPPROVE);
            }
        }

        if (productActionType == EProductActionType.COMMIT||securityContext.hasPermission(Permissions.PRODUCT_FINANCING_PROJECT_APPLY_FOR_FINANCIER)) {
            AcctPo acctPo = null;
            if (productListDto.getApplUserId() != null) {
                acctPo = this.acctService.getAcctByUserId(productListDto.getApplUserId());
            }
            if (acctPo != null) {
                productListDto.setAcctNo(acctPo.getAcctNo());
            }
            if (productListDto.getStatus() == EProductStatus.STANDBY) {
                productListDto.setCanModify(true);
                productListDto.setCanWithdraw(true);
                productListDto.setCanCheck(false);
            }

            if (productListDto.getStatus() == EProductStatus.WAITTOAPPROVE
                    || productListDto.getStatus() == EProductStatus.WAITTOEVALUATE
                    || productListDto.getStatus() == EProductStatus.WAITTOFREEZE) {
                productListDto.setStatus(EProductStatus.WAITTOAPPROVE);
            }
        } else if (productActionType == EProductActionType.APPROVE) {
            productListDto.setCanApprove(true);
            productListDto.setCanCheck(false);
        } else if (productActionType == EProductActionType.EVALUATE) {
            productListDto.setCanEvaluate(true);
            productListDto.setCanCheck(false);
        } else if (productActionType == EProductActionType.FREEZE) {
            productListDto.setCanFreeze(true);
            productListDto.setCanCheck(false);
        } else if (productActionType == EProductActionType.PUBLISH) {

            if (pStatus == EProductStatus.WAITTOPUBLISH) {
                productListDto.setCanPublish(true);
                productListDto.setCanCheck(false);
                if (securityContext.hasPermission(Permissions.PRODUCT_FINANCING_WAIT_PUBLISH_CANCEL)) {
                    productListDto.setCanWithdraw(true);
                }
            } else {
                productListDto.setCanUpdate(true);
                productListDto.setCanCheck(false);
            }
        }

        return productListDto;
    }

    
    /* getter && setter */
	public ProductListService getProductListService() {
		return productListService;
	}

	public void setProductListService(ProductListService productListService) {
		this.productListService = productListService;
	}

	public SecurityContext getSecurityContext() {
		return securityContext;
	}

	public void setSecurityContext(SecurityContext securityContext) {
		this.securityContext = securityContext;
	}

	public AcctService getAcctService() {
		return acctService;
	}

	public void setAcctService(AcctService acctService) {
		this.acctService = acctService;
	}
}
