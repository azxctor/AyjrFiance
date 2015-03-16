/*
 * Project Name: kmfex-platform
 * File Name: ProductPkgMessageRemindService.java
 * Class Name: ProductPkgMessageRemindService
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
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.fund.util.AmtUtils;
import com.hengxin.platform.member.enums.EMessageType;
import com.hengxin.platform.member.service.MemberMessageService;
import com.hengxin.platform.product.ProdPkgUtil;
import com.hengxin.platform.product.domain.PaymentSchedule;
import com.hengxin.platform.product.domain.Product;
import com.hengxin.platform.product.domain.ProductPackage;
import com.hengxin.platform.product.repository.PaymentScheduleRepository;
import com.hengxin.platform.product.repository.ProductPackageRepository;
import com.hengxin.platform.product.repository.ProductRepository;
import com.hengxin.platform.product.service.MsgHolder.PayScheMsgHolder;
import com.hengxin.platform.security.enums.EBizRole;

/**
 * Class Name: ProductPkgMessageRemindService
 * 
 * @author tingwang
 * 
 */
@Service
public class ProdPkgMsgRemindService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProdPkgMsgRemindService.class);
    @Autowired
    private MemberMessageService memberMessageService;
    @Autowired
    private PaymentScheduleRepository paymentScheduleRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductPackageRepository productPackageRepository;

    public static final boolean RUNFLAG = true;

    // 撤单保证金不足额（融资人）
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void pkgCancleWhenCompnsAmtNotEnough(String pkgId, String financerId) {
        if (RUNFLAG) {
            String pkgName = productPackageRepository.findOne(pkgId).getPackageName();
            String messageKey = "package.cancel.failed.message";
            memberMessageService.sendMessage(EMessageType.MESSAGE, messageKey, Arrays.asList(
                    EBizRole.ADMIN,
                    EBizRole.PLATFORM_RISK_CONTROL_MANAGER,
                    EBizRole.PLATFORM_RISK_CONTROL_SUPERVISOR, 
                    EBizRole.PLATFORM_RISK_CONTROL_MAKER, 
                    EBizRole.PLATFORM_RISK_CONTROL_LOAD_APPROVE,
                    EBizRole.PLATFORM_RISK_CONTROL_POST_LENDING,
                    EBizRole.PLATFORM_RISK_CONTROL_PRODUCT_APPROVE), pkgName, financerId);
        }
    }

    // 撤单成功（平台系统管理员）
    @Transactional
    public void pkgAbnormalCancleSuccess(String pkgId) {
        if (RUNFLAG) {
            String pkgName = productPackageRepository.findOne(pkgId).getPackageName();
            String messageKey = "package.abnormal.cancel.sucess.message";
            memberMessageService.sendMessage(EMessageType.MESSAGE, messageKey, Arrays.asList(
                    EBizRole.PLATFORM_SETTLEMENT_MANAGER, 
                    EBizRole.PLATFORM_SETTLEMENT_DOUBLE_CHECK_1, 
                    EBizRole.PLATFORM_SETTLEMENT_DOUBLE_CHECK_2,
                    EBizRole.PLATFORM_SETTLEMENT_MAKER_1,
                    EBizRole.PLATFORM_SETTLEMENT_MAKER_2,
                    EBizRole.PLATFORM_SETTLEMENT_CMB_MAKER,
                    EBizRole.PLATFORM_SETTLEMENT_ICBC_MAKER,
                    EBizRole.PLATFORM_SETTLEMENT_SEARCH), pkgName);
        }
    }

    // 融资包每期还款担保公司‘已代偿’时
    @Transactional
    public void pkgScheduleClearToCompensatry(PaymentSchedule schedule, String financerId, String proSevId) {
        if (RUNFLAG) {
            String pkgId = schedule.getPackageId();
            int period = schedule.getSequenceId();
            int sequence = paymentScheduleRepository.countByPackageId(pkgId).intValue();

            PayScheMsgHolder holder = ProdPkgUtil.resolvePaymentSchedule(schedule, true, true, true, true, true, true);
            BigDecimal prinPayx = holder.getPrinPay();
            BigDecimal intrPayx = holder.getIntrPay();
            BigDecimal feePayx = holder.getFeePay();

            BigDecimal prinFinePayx = holder.getPrinFinePay();
            BigDecimal intrFinePayx = holder.getIntrFinePay();
            BigDecimal feeFinePayx = holder.getFeeFinePay();
            BigDecimal finePayx = prinFinePayx.add(intrFinePayx).add(feeFinePayx);

            BigDecimal totalx = holder.getFncrPayTotalAmt();

            String pkgName = productPackageRepository.findOne(pkgId).getPackageName();

            String messageKeyForFinancier = "package.clear.compensatory.for.financier.message";
            memberMessageService.sendMessage(EMessageType.MESSAGE, messageKeyForFinancier, financerId, pkgName,
                    String.valueOf(period), sequence, totalx, prinPayx, intrPayx, feePayx, finePayx);
            memberMessageService.sendMessage(EMessageType.SMS, messageKeyForFinancier, financerId, pkgName,
                    String.valueOf(period), sequence, totalx, prinPayx, intrPayx, feePayx, finePayx);

            if(StringUtils.isNotBlank(proSevId)){
                String messageKeyForProdServ = "package.clear.compensatory.for.prodser.message";
                memberMessageService.sendMessage(EMessageType.MESSAGE, messageKeyForProdServ, proSevId, pkgName,
                        String.valueOf(period), sequence, totalx, prinPayx, intrPayx, feePayx, finePayx);
                memberMessageService.sendMessage(EMessageType.SMS, messageKeyForProdServ, proSevId, pkgName,
                        String.valueOf(period), sequence, totalx, prinPayx, intrPayx, feePayx, finePayx);
            }
            
            // 结算经理
            String messageKeyForSettleDept = "package.clear.compensatory.for.settledept.message";
            memberMessageService.sendMessage(EMessageType.TODO, messageKeyForSettleDept, Arrays.asList(
                    EBizRole.PLATFORM_SETTLEMENT_MANAGER,
                    EBizRole.PLATFORM_SETTLEMENT_DOUBLE_CHECK_1),
                     pkgName, String.valueOf(period), sequence);
        }
    }

    // 融资包每期还款“已清分”时
    @Transactional
    public void pkgScheduleClearToCleared(PaymentSchedule schedule, String financerId, String proSevId) {
        if (RUNFLAG) {
            String pkgId = schedule.getPackageId();
            int period = schedule.getSequenceId();

            BigDecimal cmpnsPyAmt = AmtUtils.processNullAmt(schedule.getCmpnsPyAmt(), BigDecimal.ZERO);
            BigDecimal cmpnsFinePyAmt = AmtUtils.processNullAmt(schedule.getCmpnsFinePyAmt(), BigDecimal.ZERO);
            BigDecimal cmpnsPdAmt = AmtUtils.processNullAmt(schedule.getCmpnsPdAmt(), BigDecimal.ZERO);
            BigDecimal cmpnsFinePdAmt = AmtUtils.processNullAmt(schedule.getCmpnsFinePdAmt(), BigDecimal.ZERO);
            BigDecimal netCmpnsAmt = AmtUtils.processNullAmt(cmpnsPyAmt.subtract(cmpnsPdAmt), BigDecimal.ZERO);
            BigDecimal netCmpnsFineAmt = AmtUtils.processNullAmt(cmpnsFinePyAmt.subtract(cmpnsFinePdAmt),
                    BigDecimal.ZERO);
            List<PaymentSchedule> shcs = paymentScheduleRepository.getByPackageId(pkgId);

            String pkgName = productPackageRepository.findOne(pkgId).getPackageName();

            String messageKeyForFinancier = "package.clear.cleared.for.financier.message";
            memberMessageService
                    .sendMessage(EMessageType.MESSAGE, messageKeyForFinancier, financerId, pkgName,
                            String.valueOf(period), shcs.size(), netCmpnsAmt.add(netCmpnsFineAmt), netCmpnsAmt,
                            netCmpnsFineAmt);
            memberMessageService
                    .sendMessage(EMessageType.SMS, messageKeyForFinancier, financerId, pkgName, String.valueOf(period),
                            shcs.size(), netCmpnsAmt.add(netCmpnsFineAmt), netCmpnsAmt, netCmpnsFineAmt);

            if(StringUtils.isNotBlank(proSevId)){
                String messageKeyForProdServ = "package.clear.cleared.for.prodser.message";
                memberMessageService
                        .sendMessage(EMessageType.MESSAGE, messageKeyForProdServ, proSevId, pkgName,
                                String.valueOf(period), shcs.size(), netCmpnsAmt.add(netCmpnsFineAmt), netCmpnsAmt,
                                netCmpnsFineAmt);
                memberMessageService
                .sendMessage(EMessageType.SMS, messageKeyForProdServ, proSevId, pkgName,
                        String.valueOf(period), shcs.size(), netCmpnsAmt.add(netCmpnsFineAmt), netCmpnsAmt,
                        netCmpnsFineAmt);
            }
            
            // 风控经理
            String messageKeyForRiskDept = "package.clear.cleared.for.riskcontrl.message";
            memberMessageService.sendMessage(EMessageType.TODO, messageKeyForRiskDept,
                    Arrays.asList(EBizRole.PLATFORM_RISK_CONTROL_MANAGER, 
                            EBizRole.PLATFORM_RISK_CONTROL_SUPERVISOR),
                    pkgName, String.valueOf(period), shcs.size(), netCmpnsAmt.add(netCmpnsFineAmt));
        }
    }

    public static final String OPSUBSTERMINATE = "申购终止";
    public static final String OPSUBSREACHEND = "申购到期";
    public static final String OPSUBSISFULL = "申购满额";

    // 融资包‘待签约’（终止，自动申购，手动申购，到期未满额）
    @Transactional
    public void pkgChangesToWaitSign(String pkgId, String financerId,String oprateStr) {
        if (RUNFLAG) {
        	ProductPackage pkg=productPackageRepository.findOne(pkgId);
            String pkgName = pkg.getPackageName();
            String messageKey = "package.wait.sign.message";
            memberMessageService.sendMessage(EMessageType.MESSAGE, messageKey, financerId, pkgName, oprateStr);
            memberMessageService.sendMessage(EMessageType.SMS, messageKey, financerId, pkgName, oprateStr);
            //满额--担保公司发短信 
            if(OPSUBSISFULL.equalsIgnoreCase(oprateStr)){
            	try {
					Product product = productRepository.findOne(pkg.getProductId());
					String messageWrtrKey = "package.wait.wrtr.message";
					if(null!=product.getWarrantId()&&!product.getWarrantId().isEmpty()){ 
					     memberMessageService.sendMessage(EMessageType.MESSAGE, messageWrtrKey, product.getWarrantId(), pkgName, oprateStr);
					     memberMessageService.sendMessage(EMessageType.SMS, messageWrtrKey, product.getWarrantId(), pkgName, oprateStr);
					}
				} catch (Exception e) { 
					LOGGER.error("Ex {}", e);
				}
            }

        }

    }

}
