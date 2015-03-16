/*
 * Project Name: kmfex-platform
 * File Name: ProdPkgUtil.java
 * Class Name: ProdPkgUtil
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

package com.hengxin.platform.product;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.hengxin.platform.fund.dto.biz.req.atom.TransferInfo;
import com.hengxin.platform.fund.enums.EFundUseType;
import com.hengxin.platform.fund.util.AmtUtils;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.product.domain.PaymentSchedule;
import com.hengxin.platform.product.service.MsgHolder.PayScheMsgHolder;
import com.hengxin.platform.product.service.MsgHolder.req.ExchagePayeeListBuildReq;
import com.hengxin.platform.product.service.MsgHolder.req.InvestorPayeeListBuildReq;
import com.hengxin.platform.product.service.MsgHolder.PkgUtils;
import com.hengxin.platform.product.domain.Product;
import com.hengxin.platform.product.enums.ETermType;
import com.hengxin.platform.product.enums.EWarrantyType;
import com.hengxin.platform.common.util.ApplicationContextUtil;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.product.ProdPkgUtil;
import com.hengxin.platform.product.repository.ProductRepository;

/**
 * Class Name: ProdPkgUtil Description: TODO
 * 
 * @author tingwang
 * 
 */

public class ProdPkgUtil {
	
	/**
	 * 高流动性天数范围
	 */
    private static final int HIGH_FLUIDITY_DAY_15 = 15;

    private ProdPkgUtil() {}

    /**
     * 解析还款表
     * 
     * @param schedule
     * @param needPrin
     * @param needIntr
     * @param needFee
     * @param needPrinProf
     * @param needIntrProf
     * @param needFeeProf
     * @return
     */
    public static PayScheMsgHolder resolvePaymentSchedule(PaymentSchedule schedule, boolean needPrin, boolean needIntr,
            boolean needFee, boolean needPrinProf, boolean needIntrProf, boolean needFeeProf) {

        // 应付本金
        BigDecimal principalAmt = AmtUtils.processNegativeAmt(schedule.getPrincipalAmt(), BigDecimal.ZERO);
        // 应付利息
        BigDecimal interestAmt = AmtUtils.processNegativeAmt(schedule.getInterestAmt(), BigDecimal.ZERO);
        // 应付费用
        BigDecimal feeAmt = AmtUtils.processNegativeAmt(schedule.getFeeAmt(), BigDecimal.ZERO);
        // 应付本金罚金
        BigDecimal principalForfeit = AmtUtils.processNegativeAmt(schedule.getPrincipalForfeit(), BigDecimal.ZERO);
        // 应付利息罚金
        BigDecimal interestForfeit = AmtUtils.processNegativeAmt(schedule.getInterestForfeit(), BigDecimal.ZERO);
        // 应付费用罚金
        BigDecimal feeForfeit = AmtUtils.processNegativeAmt(schedule.getFeeForfeit(), BigDecimal.ZERO);
        // 实付本金
        BigDecimal payPrincipalAmt = AmtUtils.processNegativeAmt(schedule.getPayPrincipalAmt(), BigDecimal.ZERO);
        // 实付利息
        BigDecimal payInterestAmt = AmtUtils.processNegativeAmt(schedule.getPayInterestAmt(), BigDecimal.ZERO);
        // 实付费用
        BigDecimal payAmt = AmtUtils.processNegativeAmt(schedule.getPayAmt(), BigDecimal.ZERO);
        // 实付本金罚金
        BigDecimal payPrincipalForfeit = AmtUtils
                .processNegativeAmt(schedule.getPayPrincipalForfeit(), BigDecimal.ZERO);
        // 实付利息罚金
        BigDecimal payInterestForfeit = AmtUtils.processNegativeAmt(schedule.getPayInterestForfeit(), BigDecimal.ZERO);
        // 实付费用罚金
        BigDecimal payFeeForfeit = AmtUtils.processNegativeAmt(schedule.getPayFeeForfeit(), BigDecimal.ZERO);

        // 本金
        BigDecimal prinPay = needPrin ? AmtUtils.processNegativeAmt(principalAmt.subtract(payPrincipalAmt),
                BigDecimal.ZERO) : BigDecimal.ZERO;

        // 利息
        BigDecimal intrPay = needIntr ? AmtUtils.processNegativeAmt(interestAmt.subtract(payInterestAmt),
                BigDecimal.ZERO) : BigDecimal.ZERO;

        // 费用
        BigDecimal feePay = needFee ? AmtUtils.processNegativeAmt(feeAmt.subtract(payAmt), BigDecimal.ZERO)
                : BigDecimal.ZERO;

        // 本金罚金
        BigDecimal prinFinePay = needPrinProf ? AmtUtils.processNegativeAmt(
                principalForfeit.subtract(payPrincipalForfeit), BigDecimal.ZERO) : BigDecimal.ZERO;

        // 利息罚金
        BigDecimal intrFinePay = needIntrProf ? AmtUtils.processNegativeAmt(
                interestForfeit.subtract(payInterestForfeit), BigDecimal.ZERO) : BigDecimal.ZERO;

        // 费用罚金
        BigDecimal feeFinePay = needFeeProf ? AmtUtils.processNegativeAmt(feeForfeit.subtract(payFeeForfeit),
                BigDecimal.ZERO) : BigDecimal.ZERO;

        BigDecimal principal = prinPay.add(prinFinePay);
        BigDecimal interest = intrPay.add(intrFinePay);
        BigDecimal fee = feePay.add(feeFinePay);

        BigDecimal fncrPayTotalAmt = principal.add(interest).add(fee);

        PayScheMsgHolder msgHolder = new PayScheMsgHolder();

        msgHolder.setPrinPay(prinPay);
        msgHolder.setIntrPay(intrPay);
        msgHolder.setFeePay(feePay);

        msgHolder.setPrinFinePay(prinFinePay);
        msgHolder.setIntrFinePay(intrFinePay);
        msgHolder.setFeeFinePay(feeFinePay);

        msgHolder.setPrincipal(principal);
        msgHolder.setInterest(interest);
        msgHolder.setFee(fee);

        msgHolder.setFncrPayTotalAmt(fncrPayTotalAmt);

        return msgHolder;
    }

    /**
     * 生成收款方(投资人)
     * 
     * @param req
     * @param payerList
     * @param isAddFrezz
     * @param profPayerList
     */
    public static void buildPayeeListOfInvestor(InvestorPayeeListBuildReq req, List<TransferInfo> payeeList,
            List<TransferInfo> profPayerList) {

        BigDecimal intrBuyerAc = req.getIntrBuyerAc();

        BigDecimal intrForfBuyerAc = req.getIntrForfBuyerAc();

        BigDecimal prinBuyerAc = req.getPrinBuyerAc();

        BigDecimal prinForfBuyerAc = req.getPrinForfBuyerAc();

        BigDecimal penaltyBuyerAc = req.getPenaltyBuyerAc();

        BigDecimal intrBuyer2PlatAc = req.getIntrBuyer2PlatAc();

        String userId = req.getUserId();

        String packageId = req.getPkgId();

        String msg = req.getMsg();

        String intrRate = req.getIntrRate();

        String sequenceId = req.getSequenceId();

        if (intrBuyerAc.compareTo(BigDecimal.ZERO) > 0) {
            TransferInfo intrReq = new TransferInfo();
            intrReq.setUserId(userId);
            intrReq.setRelZQ(false);
            intrReq.setTrxAmt(intrBuyerAc);
            intrReq.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "_" + sequenceId + msg + "，收到利息" + intrBuyerAc + "元");
            intrReq.setUseType(EFundUseType.INTR_REPAYMENT);
            payeeList.add(intrReq);
        }

        if (intrForfBuyerAc.compareTo(BigDecimal.ZERO) > 0) {
            TransferInfo intrForfReq = new TransferInfo();
            intrForfReq.setUserId(userId);
            intrForfReq.setRelZQ(false);
            intrForfReq.setTrxAmt(intrForfBuyerAc);
            intrForfReq.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "_" + sequenceId + msg + "，收到利息罚金" + intrForfBuyerAc + "元");
            intrForfReq.setUseType(EFundUseType.INTR_FINE_REPAYMENT);
            payeeList.add(intrForfReq);
        }

        if (prinBuyerAc.compareTo(BigDecimal.ZERO) > 0) {
            TransferInfo prinReq = new TransferInfo();
            prinReq.setUserId(userId);
            prinReq.setRelZQ(true);
            prinReq.setTrxAmt(prinBuyerAc);
            prinReq.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "_" + sequenceId + msg + "，收到本金" + prinBuyerAc + "元");
            prinReq.setUseType(EFundUseType.PRIN_REPAYMENT);
            payeeList.add(prinReq);
        }

        if (prinForfBuyerAc.compareTo(BigDecimal.ZERO) > 0) {
            TransferInfo prinForfReq = new TransferInfo();
            prinForfReq.setUserId(userId);
            prinForfReq.setRelZQ(true);
            prinForfReq.setTrxAmt(prinForfBuyerAc);
            prinForfReq.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "_" + sequenceId + msg + "，收到本金罚金" + prinForfBuyerAc + "元");
            prinForfReq.setUseType(EFundUseType.PRIN_FINE_REPAYMENT);
            payeeList.add(prinForfReq);
        }

        if (penaltyBuyerAc.compareTo(BigDecimal.ZERO) > 0) {
            TransferInfo penaltyReq = new TransferInfo();
            penaltyReq.setUserId(userId);
            penaltyReq.setRelZQ(true);
            penaltyReq.setTrxAmt(penaltyBuyerAc);
            penaltyReq.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "_" + sequenceId + msg + "，收到违约金" + penaltyBuyerAc + "元");
            penaltyReq.setUseType(EFundUseType.PREPAY_FINE);
            payeeList.add(penaltyReq);
        }

        if (intrBuyer2PlatAc.compareTo(BigDecimal.ZERO) > 0) {
            TransferInfo intrBuyer2PlatPayReq = new TransferInfo();
            intrBuyer2PlatPayReq.setRelZQ(false);
            intrBuyer2PlatPayReq.setTrxAmt(intrBuyer2PlatAc);
            intrBuyer2PlatPayReq.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "_" + sequenceId + msg + "，划出利息、利息罚金收益的" + intrRate + "，金额为"
                    + intrBuyer2PlatAc + "元");
            intrBuyer2PlatPayReq.setUserId(userId);
            intrBuyer2PlatPayReq.setUseType(EFundUseType.PROFITINVS2EXCH);
            profPayerList.add(intrBuyer2PlatPayReq);
        }
    }

    /**
     * 生成收款方(平台)
     * 
     * @param req
     * @param payeeList
     * @param isAddFrezz
     * @param profPayeeList
     */
    public static void buildPayeeListOfExchange(ExchagePayeeListBuildReq req, List<TransferInfo> payeeList,
            List<TransferInfo> profPayeeList) {

        BigDecimal feePlat = req.getFeePlat();

        BigDecimal feeForfPlat = req.getFeeForfPlat();

        BigDecimal deviation = req.getDeviation();

        BigDecimal buyer2PlatTotalAc = req.getBuyer2PlatTotalAc();

        String exchangeUserId = req.getExchangeUserId();

        String packageId = req.getPkgId();

        String msg = req.getMsg();

        String sequenceId = req.getSequenceId();

        String intrRate = req.getIntrRate();

        if (buyer2PlatTotalAc.compareTo(BigDecimal.ZERO) > 0) {
            TransferInfo prfReq = new TransferInfo();
            prfReq.setUserId(exchangeUserId);
            prfReq.setRelZQ(false);
            prfReq.setTrxAmt(buyer2PlatTotalAc);
            prfReq.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "_" + sequenceId + msg + "，收到利息、利息罚金收益的" + intrRate + ",金额为"
                    + buyer2PlatTotalAc + "元");
            prfReq.setUseType(EFundUseType.PROFITINVS2EXCH);
            profPayeeList.add(prfReq);
        }

        if (feePlat.compareTo(BigDecimal.ZERO) > 0) {
            TransferInfo feeReq = new TransferInfo();
            feeReq.setUserId(exchangeUserId);
            feeReq.setRelZQ(false);
            feeReq.setTrxAmt(feePlat);
            feeReq.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "_" + sequenceId + msg + "，收到费用" + feePlat + "元");
            feeReq.setUseType(EFundUseType.TRADEEXPENSE);
            payeeList.add(feeReq);
        }

        if (feeForfPlat.compareTo(BigDecimal.ZERO) > 0) {
            TransferInfo feeForfReq = new TransferInfo();
            feeForfReq.setUserId(exchangeUserId);
            feeForfReq.setRelZQ(false);
            feeForfReq.setTrxAmt(feeForfPlat);
            feeForfReq.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "_" + sequenceId + msg + "，收到费用罚金" + feeForfPlat + "元");
            feeForfReq.setUseType(EFundUseType.TRADEEXPENSE_FINE);
            payeeList.add(feeForfReq);
        }

        if (deviation.compareTo(BigDecimal.ZERO) > 0) {
            TransferInfo deviationReq = new TransferInfo();
            deviationReq.setUserId(exchangeUserId);
            deviationReq.setRelZQ(false);
            deviationReq.setTrxAmt(deviation);
            deviationReq.setTrxMemo(PkgUtils.getPkgIdNameStr(packageId) + "_" + sequenceId + msg + "，收到还款误差" + deviation + "元");
            deviationReq.setUseType(EFundUseType.REPAYAMTOFERROR2EXCH);
            payeeList.add(deviationReq);
        }
    }

    /**
     * 是否需要交罚金（违约宽限期）
     * 
     * @param payDate
     * @param workDate
     * @param graceDays
     * @return
     */
    public static boolean needPayFineAmt(Date payDate, Date workDate, Long graceDays) {
        Long graceDay = AmtUtils.processLong(graceDays, Long.valueOf(0));
        graceDay = AmtUtils.max(graceDay, Long.valueOf(0));
        payDate = DateUtils.add(payDate, Calendar.DATE, graceDay.intValue());
        String dateStr = DateUtils.formatDate(payDate, "yyyy-MM-dd");
        payDate = DateUtils.getDate(dateStr, "yyyy-MM-dd");
        return workDate.compareTo(payDate) >= 0;
    }
    
    public static void main(String[] args){
    	Long graceDays = Long.valueOf(3);
    	Date payDate = DateUtils.getDate("2014-08-16", "yyyy-MM-dd");
    	Date workDate = DateUtils.getDate("2014-08-18", "yyyy-MM-dd");
    	boolean bool = needPayFineAmt(payDate, workDate, graceDays);
    	System.out.println(bool);
    }

    /**
     * 是否需要交代偿罚金（代偿宽限期）
     * 
     * @param payDate
     * @param workDate
     * @param graceDaysList
     * @return
     */
    public static boolean needPayOverdueCmpnsFineAmt(Date payDate, Date workDate, List<Long> graceDaysList) {
        Long graceDay = Long.valueOf(0);
        for (Long day : graceDaysList) {
            day = AmtUtils.processLong(day, Long.valueOf(0));
            graceDay = graceDay + day;
        }
        graceDay = AmtUtils.max(graceDay, Long.valueOf(0));
        payDate = DateUtils.add(payDate, Calendar.DATE, graceDay.intValue());
        String dateStr = DateUtils.formatDate(payDate, "yyyy-MM-dd");
        payDate = DateUtils.getDate(dateStr, "yyyy-MM-dd");
        return workDate.compareTo(payDate) >= 0;
    }
    
    /**
     * 是否高流动性包.
     * @param product
     * @return
     */
    public static boolean isHighFluidityProduct(Product product){
    	boolean highFluidity = false;
        ETermType termType = product.getTermType();
        EWarrantyType warrantyType = product.getWarrantyType();
        Integer termLength = product.getTermLength();
        highFluidity = termType == ETermType.DAY;
        highFluidity = highFluidity&& termLength.intValue()<=HIGH_FLUIDITY_DAY_15;
        highFluidity = highFluidity&& warrantyType == EWarrantyType.MONITORASSETS;
        return highFluidity;
    }
    
    /**
     * 违约宽限期
     * @param prodId
     * @return
     */
    public static Integer getBreachGraceDays(String prodId){
    	// 免交逾期违约罚金宽限期
        Integer brkGraceDays = CommonBusinessUtil.getBreachGraceDays();
        ProductRepository repo = ApplicationContextUtil.getBean(ProductRepository.class);
        Product prod = repo.findOne(prodId);
        boolean hf = ProdPkgUtil.isHighFluidityProduct(prod);
        return hf?Integer.valueOf(0):brkGraceDays;
    }
    
    /**
     * 获取违约宽限期
     * 高流动性包违约宽限期为 0 
     * @return
     */
    public static Integer getBreachGraceDays(Product product){
    	boolean hf = ProdPkgUtil.isHighFluidityProduct(product);
    	return hf?Integer.valueOf(0):CommonBusinessUtil.getBreachGraceDays();
    }

}
