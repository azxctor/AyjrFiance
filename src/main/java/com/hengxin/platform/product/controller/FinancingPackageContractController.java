/*
 * Project Name: kmfex-platform
 * File Name: FinancingPackageContractController.java
 * Class Name: FinancingPackageContractController
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

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.util.AppConfigUtil;
import com.hengxin.platform.common.util.MaskUtil;
import com.hengxin.platform.fund.entity.PkgTradeJnlPo;
import com.hengxin.platform.fund.service.AcctService;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.member.domain.Member;
import com.hengxin.platform.member.service.MemberService;
import com.hengxin.platform.product.dto.FinancingPackageContractDto;
import com.hengxin.platform.product.dto.FinancingPackageContractRequestDto;
import com.hengxin.platform.product.service.ContractExcelService;
import com.hengxin.platform.product.service.FinancingPackageContractService;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.domain.User;
import com.hengxin.platform.security.service.UserService;

/**
 * Class Name: FinancingPackageContractController
 * 
 * @author yingchangwang
 * 
 */
@Controller
public class FinancingPackageContractController {

    @Autowired
    private FinancingPackageContractService contractService;

    @Autowired
    private UserService userService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ContractExcelService contractExcelService;

    @Autowired
    private SecurityContext securityContext;

    @Autowired
    private AcctService acctService;

    /**
     * 
     * Description: 查询融资包的合同列表
     * 
     * @param request
     * @param requestDto
     * @return
     */
    @RequestMapping(value = "package/getcontractlist")
    @ResponseBody
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_PACKAGE_FIN_PROD_DETAIL_VIEW,
            Permissions.PRODUCT_FINANCING_PACKAGE_PLATFORM_DETAIL_VIEW }, logical = Logical.OR)
    @Transactional(readOnly = true)
    public DataTablesResponseDto<FinancingPackageContractDto> getContractList(HttpServletRequest request,
            @RequestBody FinancingPackageContractRequestDto requestDto) {
        DataTablesResponseDto<FinancingPackageContractDto> result = new DataTablesResponseDto<FinancingPackageContractDto>();
        Page<PkgTradeJnlPo> contranctListForPackage = contractService.getContranctListForPackage(requestDto);

        List<FinancingPackageContractDto> contractList = new ArrayList<FinancingPackageContractDto>();
        FinancingPackageContractDto dto = null;
        if (contranctListForPackage.hasContent()) {
            for (PkgTradeJnlPo item : contranctListForPackage) {
                dto = new FinancingPackageContractDto();
                dto.setPackageId(item.getPkgId());
                dto.setSignedAmt(item.getLotBuyPrice());
                if (item.getProductPackage().getSigningDt() != null) {
                    dto.setSignDate(DateUtils.formatDate(item.getProductPackage().getSigningDt(), "yyyy-MM-dd"));
                }
                String investorId = item.getBuyerUserId();
                dto.setUserId(investorId);
                User user = userService.getUserByUserId(investorId);
                if (securityContext.cannotViewRealName(securityContext.getCurrentUserId(), false)) {
                    dto.setUserName(MaskUtil.maskChinsesName(user.getName()));
                } else {
                    dto.setUserName(user.getName());
                }
                Member member = memberService.getMemberByUserId(investorId);
                String personIdCardNumber = member.getPersonIdCardNumber();
                String orgLegalPersonIdCardNumber = member.getOrgLegalPersonIdCardNumber();

                if (this.securityContext.hasPermission(Permissions.RISK_CONTROL_DEPT)
                        || this.securityContext.hasPermission(Permissions.TRANSACTION_DEPT)
                        || this.securityContext.hasPermission(Permissions.SETTLEMENT_DEMPARTMENT)) {
                    personIdCardNumber = MaskUtil.maskCardNumber(personIdCardNumber);
                    orgLegalPersonIdCardNumber = MaskUtil.maskCardNumber(orgLegalPersonIdCardNumber);
                }

                dto.setUserIdNumber(StringUtils.isBlank(personIdCardNumber) ? orgLegalPersonIdCardNumber
                        : personIdCardNumber);
                String contractId = item.getPositionLot().getContractId();
                dto.setContranctNo(contractId == null ? "" : contractId);
                dto.setLotId(item.getLotId());
                contractList.add(dto);
            }
        }
        result.setData(contractList);
        result.setEcho(requestDto.getEcho());
        result.setTotalDisplayRecords(contranctListForPackage.getTotalElements());
        result.setTotalRecords(contranctListForPackage.getTotalElements());
        return result;
    }

    /**
     * 
     * Description: 导出融资包合同列表
     * 
     * @param request
     * @param model
     * @param packageId
     * @return
     */
    @RequestMapping(value = "contract/exportExcel", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.PARTIAL_CONTENT)
    @RequiresPermissions(value = { Permissions.PRODUCT_FINANCING_PACKAGE_FIN_PROD_DETAIL_VIEW,
            Permissions.PRODUCT_FINANCING_PACKAGE_PLATFORM_DETAIL_VIEW }, logical = Logical.OR)
    public ModelAndView getExcel(HttpServletRequest request, Model model,
            @RequestParam(value = "packageId") String packageId) {
        try {
            List<PkgTradeJnlPo> contranctListForPackage = contractService.getAllContranctListForPackage(packageId);

            List<FinancingPackageContractDto> contractList = new ArrayList<FinancingPackageContractDto>();
            FinancingPackageContractDto dto = null;
            if (contranctListForPackage != null && !contranctListForPackage.isEmpty()) {
                for (PkgTradeJnlPo item : contranctListForPackage) {
                    dto = new FinancingPackageContractDto();
                    dto.setPackageId(item.getPkgId());
                    dto.setSignedAmt(item.getLotBuyPrice());
                    if (item.getProductPackage().getSigningDt() != null) {
                        dto.setSignDate(DateUtils.formatDate(item.getProductPackage().getSigningDt(), "yyyy-MM-dd"));
                    }
                    String investorId = item.getBuyerUserId();
                    dto.setUserId(investorId);
                    User user = userService.getUserByUserId(investorId);
                    if (securityContext.cannotViewRealName(securityContext.getCurrentUserId(), false)) {
                        dto.setUserName(MaskUtil.maskChinsesName(user.getName()));
                    } else {
                        dto.setUserName(user.getName());
                    }
                    Member member = memberService.getMemberByUserId(investorId);
                    String personIdCardNumber = member.getPersonIdCardNumber();
                    String orgLegalPersonIdCardNumber = member.getOrgLegalPersonIdCardNumber();
                    dto.setUserIdNumber(StringUtils.isBlank(personIdCardNumber) ? orgLegalPersonIdCardNumber
                            : personIdCardNumber);
                    String contractId = item.getPositionLot().getContractId();
                    dto.setContranctNo(contractId == null ? "" : contractId);
                    dto.setLotId(item.getLotId());
                    contractList.add(dto);
                }
            }
            String tempPath = AppConfigUtil.getExcelPackageContractTemplatePath();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("contractList", contractList);
            map.put("fileName", packageId + ".xls");
            map.put("tempPath", tempPath);
            return new ModelAndView(contractExcelService, map);
        } catch (Exception e) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR);
        }
    }
}
