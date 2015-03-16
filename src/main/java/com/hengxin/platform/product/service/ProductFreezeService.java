/*
 * Project Name: kmfex-platform
 * File Name: ProductFreezeService.java
 * Class Name: ProductFreezeService
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
package com.hengxin.platform.product.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.batch.service.JobWorkService;
import com.hengxin.platform.common.enums.ActionType;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.enums.EntityType;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.service.BaseService;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.fund.dto.biz.req.atom.ReserveReq;
import com.hengxin.platform.fund.enums.EFundUseType;
import com.hengxin.platform.fund.service.FinancierEntrustDepositService;
import com.hengxin.platform.fund.service.FundAcctBalService;
import com.hengxin.platform.fund.service.WarrantDepositService;
import com.hengxin.platform.member.enums.EMessageType;
import com.hengxin.platform.member.service.MemberMessageService;
import com.hengxin.platform.product.domain.Product;
import com.hengxin.platform.product.enums.EProductStatus;
import com.hengxin.platform.product.enums.EWarrantyType;
import com.hengxin.platform.product.repository.ProductRepository;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.enums.EBizRole;

/**
 * Class Name: ProductFreezeService
 * 
 * @author chunlinwang
 * 
 */

@Service
@Transactional
public class ProductFreezeService extends BaseService {

    @Autowired
    ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    WarrantDepositService warrantDepositService;

    @Autowired
    FinancierEntrustDepositService financierEntrustDepositService;

    @Autowired
    FundAcctBalService fundAcctBalService;

    @Autowired
    SecurityContext security;

    @Autowired
    transient JobWorkService jobWorkService;
    
    @Autowired
	private MemberMessageService memberMessageService;
    
    @Autowired
	private MessageService messageService;

    @Transactional
    public void freezeProduct(String productId, BigDecimal servMrgnAmtInput, BigDecimal wrtrMrgnRtInput) {

        if (jobWorkService.isBatchBizTaskProcessing()) {
            throw new BizServiceException(EErrorCode.BATCH_EXEC_PROCESSING_WAITING);
        }

        if (StringUtils.isEmpty(productId)) {
            return;
        }

        Product product = productRepository.findOne(productId);
        if (product == null) {
            return;
        }
        boolean prodServ = StringUtils.isNotEmpty(product.getWarrantId());

        BigDecimal financierBalance = fundAcctBalService.getUserCurrentAcctAvlBal(product.getApplUserId());
        if (financierBalance == null || financierBalance.compareTo(servMrgnAmtInput) < 0) {
        	messageService.sendNotEnoughMessage(product,servMrgnAmtInput,EBizRole.FINANCIER);
            throw new BizServiceException(EErrorCode.NO_ENOUGH_FINANCIER_BALANCE);// 保证金冻结失败，融资会员账户余额不足！
        }

        product.setServMrgnAmt(servMrgnAmtInput);
        BigDecimal wrtrMrgnAmount = BigDecimal.ZERO;
        if (prodServ) {
            product.setWrtrMrgnRt(wrtrMrgnRtInput.divide(new BigDecimal("100")));
            wrtrMrgnAmount = calculateWrtrMrgnAmount(product);
            BigDecimal wrtrBalance = fundAcctBalService.getUserCurrentAcctAvlBal(product.getWarrantId());
            if (wrtrBalance == null || wrtrBalance.compareTo(wrtrMrgnAmount) < 0) {
            	messageService.sendNotEnoughMessage(product,wrtrMrgnAmount,EBizRole.PROD_SERV);
                throw new BizServiceException(EErrorCode.NO_ENOUGH_GUARANTOR_BALANCE);// 保证金冻结失败，担保机构账户余额不足！
            }
        }

        BigDecimal servMrgnAmt = product.getServMrgnAmt();
        if (servMrgnAmt == null || BigDecimal.ZERO.compareTo(servMrgnAmt) > 0) {
            throw new BizServiceException(EErrorCode.TECH_DATA_INVALID);// Invalid value for SERV_MRGN_AMT
        }

        String currentUserId = security.getCurrentUserId();
        Date currentDate = new Date();
        Date currentWorkDate = CommonBusinessUtil.getCurrentWorkDate();

        ReserveReq servMrgnAmtReq = new ReserveReq();

        servMrgnAmtReq.setCurrOpId(currentUserId);
        servMrgnAmtReq.setUserId(product.getApplUserId());
        servMrgnAmtReq.setWorkDate(currentWorkDate);
        servMrgnAmtReq.setTrxAmt(product.getServMrgnAmt());
        servMrgnAmtReq.setUseType(EFundUseType.POSTDEPOSIT);
        servMrgnAmtReq.setBizId(product.getProductId());
        servMrgnAmtReq.setTrxMemo("融资产品" + product.getProductId() + "冻结融资服务合同保证金。");
        String servFnrJnlNo = financierEntrustDepositService.payDeposit(servMrgnAmtReq);
        product.setServFnrJnlNo(servFnrJnlNo);

        if (prodServ) {
            ReserveReq wrtrMrgnRtReq = new ReserveReq();
            wrtrMrgnRtReq.setCurrOpId(currentUserId);
            wrtrMrgnRtReq.setUserId(product.getWarrantId());
            wrtrMrgnRtReq.setWorkDate(currentWorkDate);
            wrtrMrgnRtReq.setTrxAmt(wrtrMrgnAmount);
            wrtrMrgnRtReq.setUseType(EFundUseType.WARRANTDEPOSIT);
            wrtrMrgnRtReq.setBizId(product.getProductId());
            wrtrMrgnRtReq.setTrxMemo("融资产品" + product.getProductId() + "冻结担保机构还款保证金。");
            String wrtrFnrJnlNo = warrantDepositService.payDeposit(wrtrMrgnRtReq);
            product.setWrtrFnrJnlNo(wrtrFnrJnlNo);
        }

        product.setLastMntOpid(currentUserId);
        product.setLastMntTs(currentDate);
        product.setFreezeDate(currentDate);
        // product.setStatus(EProductStatus.WAITTOPUBLISH);
        // added by martin for jbpm process
        EProductStatus productStatus = productService.approveProduct(productId, null, product.getStatus(), true);
        product.setStatus(productStatus);
        // end
        productRepository.save(product);
        actionHistoryUtil.save(EntityType.PRODUCT, productId, ActionType.FREEZE, null);
        
        sendTODOMessage(product);
    }
    
    /**
     * 融资项目保证金冻结后给交易部发送待办，提醒设置交易参数
     * @param product
     */
    private void sendTODOMessage(Product product) {
    	//TODO add new roles 平台交易部日常管理1、日常管理2
		String messageKey = "product.freeze.margin.message";
		List<EBizRole> roleList = Arrays.asList(
				EBizRole.TRANS_TRADE_MGR1,
				EBizRole.TRANS_TRADE_MGR2);
		memberMessageService.sendMessage(EMessageType.TODO, messageKey,
				roleList, product.getProductName());
    }

    /**
     * 
     * Description: 根据担保类型、融资期限计算担保机构还款保证金
     * 
     * 若担保类型为本金担保或本息担保，则返回： 融资额 * WRTR_MRGN_RT
     * 
     * 若担保类型为其他，返回0
     * 
     * @param product
     * @return
     */
    public BigDecimal calculateWrtrMrgnAmount(Product product) {

        BigDecimal quota = product.getAppliedQuota();
        // BigDecimal rate = product.getRate();
        EWarrantyType warrantyType = product.getWarrantyType();
        // ETermType termType = product.getTermType();
        // Integer termLength = product.getTermLength();
        // Integer termToDays = product.getTermToDays();
        BigDecimal wrtrMrgnRt = product.getWrtrMrgnRt();

        if (warrantyType == null || quota == null || wrtrMrgnRt == null) {

            return BigDecimal.ZERO;
        }

        if (EWarrantyType.PRINCIPAL == warrantyType || EWarrantyType.PRINCIPALINTEREST == warrantyType) {
            return quota.multiply(wrtrMrgnRt);
        }

        // if (EWarrantyType.PRINCIPAL == warrantyType && termLength != null) {
        // // 本金担保
        // // quota * wrtrMrgnRt
        // return quota.multiply(wrtrMrgnRt);
        //
        // } else if (EWarrantyType.PRINCIPALINTEREST == warrantyType) {
        // // 本息担保
        // if (termType == ETermType.YEAR && termLength != null) {
        // // (quota * rate * termLength + quota) * wrtrMrgnRt
        // return quota.multiply(new BigDecimal(termLength)).add(quota).multiply(wrtrMrgnRt);
        // } else if (termType == ETermType.MONTH && termLength != null) {
        // // (quota * rate * termLength / 12 + quota) * wrtrMrgnRt
        // return quota.multiply(rate).multiply(new BigDecimal(termLength))
        // .divide(new BigDecimal("12"), RoundingMode.CEILING).add(quota).multiply(wrtrMrgnRt);
        //
        // } else if (termType == ETermType.DAY && termToDays != null) {
        // // (quota * rate * termToDays / 365 + quota) * wrtrMrgnRt
        // return quota.multiply(rate).multiply(new BigDecimal(termToDays))
        // .divide(new BigDecimal("365"), RoundingMode.CEILING).add(quota).multiply(wrtrMrgnRt);
        // }
        // }
        return BigDecimal.ZERO;

    }
    
    
}
