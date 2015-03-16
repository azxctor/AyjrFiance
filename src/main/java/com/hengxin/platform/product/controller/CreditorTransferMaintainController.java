package com.hengxin.platform.product.controller;

import java.util.ArrayList;
import java.util.List;

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

import com.hengxin.platform.common.constant.UrlConstant;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.common.dto.annotation.OnValid;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.common.util.MessageUtil;
import com.hengxin.platform.product.domain.ProductPackage;
import com.hengxin.platform.product.dto.CreditorTransferInitDto;
import com.hengxin.platform.product.dto.CreditorTransferMaintainDto;
import com.hengxin.platform.product.enums.EPackageStatus;
import com.hengxin.platform.product.enums.ERiskLevel;
import com.hengxin.platform.product.enums.EWarrantyType;
import com.hengxin.platform.product.service.CreditorTransferMaintainServie;
import com.hengxin.platform.product.service.FinancingPackageService;
import com.hengxin.platform.security.Permissions;

@Controller
public class CreditorTransferMaintainController {

    @Autowired
    private FinancingPackageService financingPackageService;

    @Autowired
    private CreditorTransferMaintainServie creditorTransferMaintainServie;

    /**
     * 
    * Description: load the initial package type and package
    *
    * @param request
    * @param session
    * @param model
    * @return
     */
    @RequestMapping(value = UrlConstant.SYSTEM_MANAGEMENT_TRANSFER_RULE_MAINTAIN_URL)
    @RequiresPermissions(value={Permissions.SYSTEM_MANAGEMENT_TRANSFER_RULE_MAINTAIN})
    public String renderInitPage(HttpServletRequest request, HttpSession session, Model model) {
        model.addAttribute("packageTypeListAll", setPackageTypeListAll());
        model.addAttribute("packageListAll", setPackageListAll());
        model.addAttribute("isMarketClose", !CommonBusinessUtil.isMarketOpen());
        return "packet/bond_maintain";
    }

    /**
     * 
    * Description:  load the select package type and package
    *
    * @return
     */
    @RequestMapping(value = "rulemaintain/show")
    @ResponseBody
    @RequiresPermissions(value={Permissions.SYSTEM_MANAGEMENT_TRANSFER_RULE_MAINTAIN})
    public CreditorTransferMaintainDto getRuleMaintain() {
        CreditorTransferMaintainDto creditorTransferMaintainDto = creditorTransferMaintainServie
                .getCreditorTransferMaintainDto();
        return creditorTransferMaintainDto;
    }

    /**
     * 
    * Description: save 
    *
    * @param cTMaintainDto
    * @param request
    * @param session
    * @param model
    * @return
     */
    @RequestMapping(value = "rulemaintain/save", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value={Permissions.SYSTEM_MANAGEMENT_TRANSFER_RULE_MAINTAIN})
    public ResultDto saveRuleMaintain(@OnValid @RequestBody CreditorTransferMaintainDto cTMaintainDto,
            HttpServletRequest request, HttpSession session, Model model) {
    	if (CommonBusinessUtil.isMarketOpen()) {
    		return ResultDtoFactory.toNack(MessageUtil.getMessage("product.error.package.not_in_closed_time"));
    	}
        this.creditorTransferMaintainServie.saveCreditorTransferMaintain(cTMaintainDto);
        return ResultDtoFactory.toAck(MessageUtil.getMessage("system.creditor.transfer.maintain.success"));
    }    

    /**
     * 
    * Description: set PackageTypeList
    *
    * @return
     */
    private List<CreditorTransferInitDto> setPackageTypeListAll() {

        List<CreditorTransferInitDto> packageTypeListAll = new ArrayList<CreditorTransferInitDto>();
        for (ERiskLevel eRiskLevel : ERiskLevel.values()) {
            for (EWarrantyType eWarrantyType : EWarrantyType.values()) {
                if (eRiskLevel == ERiskLevel.NULL || eWarrantyType == EWarrantyType.NULL) {
                    continue;
                }
                CreditorTransferInitDto cti = new CreditorTransferInitDto();
                cti.setId(eRiskLevel.getCode() + eWarrantyType.getCode());
                cti.setText(eRiskLevel.getCode() + eWarrantyType.getCode() + "(" + eRiskLevel.getText() + ","
                        + eWarrantyType.getText() + ")");
                packageTypeListAll.add(cti);
            }
        }
        return packageTypeListAll;
    }
   
    /**
     * 
    * Description: set PackageList
    *
    * @return
     */
    private List<CreditorTransferInitDto> setPackageListAll() {
        List<CreditorTransferInitDto> packageListAll = new ArrayList<CreditorTransferInitDto>();
        List<EPackageStatus> statusList = new ArrayList<EPackageStatus>();
        statusList.add(EPackageStatus.ABANDON);
        statusList.add(EPackageStatus.END);
        List<ProductPackage> productPackages = this.financingPackageService.getProductPackagesStatusNotIn(statusList);

        if (productPackages == null || productPackages.size() == 0) {
            return null;
        }
        for (ProductPackage productPackage : productPackages) {
            CreditorTransferInitDto cti = new CreditorTransferInitDto();
            cti.setId(productPackage.getId());
            cti.setText(productPackage.getPackageName());
            packageListAll.add(cti);
        }
        return packageListAll;
    }
}
