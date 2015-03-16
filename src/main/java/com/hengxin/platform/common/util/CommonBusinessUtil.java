package com.hengxin.platform.common.util;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

import com.hengxin.platform.common.entity.id.CachingDatabaseSequenceProducer.CacheWatcher;
import com.hengxin.platform.common.enums.EBusinessSigninStatus;
import com.hengxin.platform.common.enums.ESystemParam;
import com.hengxin.platform.common.service.CommonBusinessService;

public final class CommonBusinessUtil {

	static CommonBusinessService commonBusinessService;
	static CacheWatcher cacheWatcher;

	public static void init() {
		commonBusinessService.refresh();
		cacheWatcher.refresh();
	}

	/**
	 * 当前是否在开市状态
	 */
	public static boolean isMarketOpen() {
		return EBusinessSigninStatus.OPEN.getCode().equals(
				commonBusinessService.getBusinessSigninStatus("MKT"));
	}
	
	/**
	 * 当前工作日
	 */
	public static Date getCurrentWorkDate() {
		return commonBusinessService.getCurrentWorkDate();
	}

	/**
	 * 制定投资会员级别可投资的单个融资包最大比例
	 */
	public static BigDecimal getMaxSubscribeRate(String investorLevel) {
		return commonBusinessService.getMaxSubscribeRate(investorLevel);
	}

	/**
	 * 担保公司还款保证金比例
	 */
	public static BigDecimal getWarrantorMarginRate() {
		return new BigDecimal(
				commonBusinessService.getSystemParam(ESystemParam.FP0001));
	}

	/**
	 * 借款合同履约保证金比例
	 */
	public static BigDecimal getLoanMarginRate() {
		return new BigDecimal(
				commonBusinessService.getSystemParam(ESystemParam.FP0002));
	}

	/**
	 * 会员席位费融资额阀值
	 */
	public static BigDecimal getSeatFeeThreshold() {
		return new BigDecimal(
				commonBusinessService.getSystemParam(ESystemParam.FP0003));
	}

	/**
	 * 会员席位费（小于融资额阀值时收取）
	 */
	public static BigDecimal getSeatFeeLowAmt() {
		return new BigDecimal(
				commonBusinessService.getSystemParam(ESystemParam.FP0004));
	}

	/**
	 * 会员席位费（大于融资额阀值时收取）
	 */
	public static BigDecimal getSeatFeeHighAmt() {
		return new BigDecimal(
				commonBusinessService.getSystemParam(ESystemParam.FP0005));
	}

	/**
	 * 发布保证金金额（月利息单位值）
	 */
	public static BigDecimal getPublishMarginRate() {
		return new BigDecimal(
				commonBusinessService.getSystemParam(ESystemParam.FP0006));
	}

	/**
	 * 撤单时发布保证金平台扣除比例
	 */
	public static BigDecimal getPublishMarginDeductRate() {
		return new BigDecimal(
				commonBusinessService.getSystemParam(ESystemParam.FP0007));
	}

	/**
	 * 还款利息（包括罚息）平台扣除比例
	 */
	public static BigDecimal getPaymentInterestDeductRate() {
		return new BigDecimal(
				commonBusinessService.getSystemParam(ESystemParam.FP0008));
	}

	/**
	 * 清分利息平台扣除比例
	 */
	public static BigDecimal getClearingInterestDeductRate() {
		return new BigDecimal(
				commonBusinessService.getSystemParam(ESystemParam.FP0009));
	}

	/**
	 * 债权转让平台收取手续费比例
	 */
	public static BigDecimal getTransferFeeRate() {
		return new BigDecimal(
				commonBusinessService.getSystemParam(ESystemParam.FP0010));
	}
	
	/**
	 * 自动申购手续费比例
	 */
	public static BigDecimal getAutoSubscribeFeeRate() {
		return new BigDecimal(
				commonBusinessService.getSystemParam(ESystemParam.FP0011));
	}

	/**
	 * 违约宽限天数(宽限期以内，不收违约金)
	 */
	public static Integer getBreachGraceDays() {
		return Integer.valueOf(
				commonBusinessService.getSystemParam(ESystemParam.FP0012));
	}

	/**
	 * 融资包签约期限(天)
	 */
	public static BigDecimal getFinancingPackageSigningTerm() {
		return new BigDecimal(
				commonBusinessService.getSystemParam(ESystemParam.FP0013));
	}
	
	/**
	 * 融资包还款提前提醒天数
	 * @return
	 */
	public static Integer getPreRemindRepaymentDays(){
		return Integer.valueOf(
				commonBusinessService.getSystemParam(ESystemParam.FP0014));
	}

	/**
	 * 债券转让报价下限剩余本金比例
	 */
	public static BigDecimal getTransferMinRate() {
		return new BigDecimal(
				commonBusinessService.getSystemParam(ESystemParam.FP0015));
	}

	/**
	 * 债券转让手续费平台提成比例
	 */
	public static BigDecimal getTransferFeePlatformDeductRate() {
		return new BigDecimal(
				commonBusinessService.getSystemParam(ESystemParam.FP0016));
	}
	
	/**
	 * 提前还款，退还交易费用的比例
	 * @return
	 */
	public static BigDecimal getPrePayRefundFeeRt(){
		return new BigDecimal(
				commonBusinessService.getSystemParam(ESystemParam.FP0017));
	}
	
	/**
	 * 债权持有期限
	 */
	public static Integer getInverstorHoldDays() {
		return Integer.valueOf(
				commonBusinessService.getSystemParam(ESystemParam.FP0018));
	}
	
	/**
	 * 单笔申购面额
	 * @return
	 */
	public static BigDecimal getUnitFaceValue(){
		return BigDecimal.valueOf(1000);
	}

	/**
	 * 交易所综合账户编号
	 */
	public static String getExchangeAccountNo() {
		return commonBusinessService.getSystemParam(ESystemParam.AC0001);
	}

	/**
	 * 交易所会员编号
	 */
	public static String getExchangeUserId() {
		return commonBusinessService.getSystemParam(ESystemParam.AC0002);
	}

	/**
	 * 银行接口系统用户编号
	 */
	public static String getBankInterfaceAccountNo() {
		return commonBusinessService.getSystemParam(ESystemParam.AC0030);
	}

	/**
	 * 活期利率
	 */
	public static BigDecimal getInterestRate() {
		return new BigDecimal(
				commonBusinessService.getSystemParam(ESystemParam.AC0050));
	}

	/**
	 * 小微宝利率
	 */
	public static BigDecimal getXwbRate() {
		return new BigDecimal(
				commonBusinessService.getSystemParam(ESystemParam.AC0051));
	}

	/**
	 * 小微宝当日转入计息截止时间
	 */
	public static Long getXwbInterestEndTime() {
		return Long.valueOf(commonBusinessService
				.getSystemParam(ESystemParam.AC0052));
	}

	/**
	 * 资金托管结算往来账户
	 */
	public static String getSupportSettleCurrentAcctNo() {
		return commonBusinessService
				.getSystemParam(ESystemParam.AC0080);
	}

	/**
	 * 资金托管商户号
	 */
	public static String getEswCustAcctNo() {
		return commonBusinessService
				.getSystemParam(ESystemParam.AC0081);
	}
	
	/**
	 * 资金账户是否托管模式
	 * @return
	 */
	public static boolean isEscrowedMode(){
//		String bool = commonBusinessService
//				.getSystemParam(ESystemParam.AC0082);
		return true;//Boolean.valueOf(bool);
	}
	
	/**
	 * 托管运营商
	 * @return
	 */
	public static String getEscrowProvider(){
		return commonBusinessService
				.getSystemParam(ESystemParam.AC0083);
	}
	
	/**
	 * 日终批量任务组编号
	 * @return
	 */
	public static String getDayEndBatchTaskGroupId(){
		return commonBusinessService
				.getSystemParam(ESystemParam.GL0001);
	}

	/**
	 * Android手机端版本号
	 * 
	 * @return
	 */
	public static String getAndroidAppVersion() {
		return commonBusinessService.getSystemParam(ESystemParam.GL0002);
	}

	/**
	 * Android手机端下载链接
	 * 
	 * @return
	 */
	public static String getAndroidAppDownloadUrl() {
		return commonBusinessService.getSystemParam(ESystemParam.GL0003);
	}

	/**
	 * IOS手机端版本号
	 * 
	 * @return
	 */
	public static String getIOSAppVersion() {
		return commonBusinessService.getSystemParam(ESystemParam.GL0004);
	}

	/**
	 * IOS手机端下载链接
	 * 
	 * @return
	 */
	public static String getIOSAppDownloadUrl() {
		return commonBusinessService.getSystemParam(ESystemParam.GL0005);
	}

	/**
	 * Android平板端版本号, 适用担保机构&&服务中心(三期 app)
	 * 
	 * @return
	 */
	public static String getAndroidAppVersionV3() {
		return commonBusinessService.getSystemParam(ESystemParam.GL0010);
	}

	/**
	 * Android平板端下载链接, 适用担保机构&&服务中心(三期 app)
	 * 
	 * @return
	 */
	public static String getAndroidAppDownloadUrlV3() {
		return commonBusinessService.getSystemParam(ESystemParam.GL0011);
	}

	/**
	 * IOS平板端版本号, 适用担保机构&&服务中心(三期 app)
	 * 
	 * @return
	 */
	public static String getIOSAppVersionV3() {
		return commonBusinessService.getSystemParam(ESystemParam.GL0012);
	}

	/**
	 * IOS平板端下载链接, 适用担保机构&&服务中心(三期 app)
	 * 
	 * @return
	 */
	public static String getIOSAppDownloadUrlV3() {
		return commonBusinessService.getSystemParam(ESystemParam.GL0013);
	}
	
	/**
	 * 获取托管系统异步请求url
	 * @return
	 */
	public static String getEbcRespUrl(){
		return commonBusinessService.getSystemParam(ESystemParam.GL0014);
	}
	
	/**
	 * 充值提交POST url
	 * @return
	 */
	public static String getEbcRechargePostUrl(){
		return commonBusinessService.getSystemParam(ESystemParam.GL0015);
	}
	
	/**
	 * 充值提交成功，跳转url
	 * @return
	 */
	public static String getEbcRechargeSuccessRedirectUrl(){
		return commonBusinessService.getSystemParam(ESystemParam.GL0016);
	}
	
	/**
	 * 
	 * @return true active risk.
	 */
	public static boolean isRiskActive() {
		String flag = commonBusinessService.getSystemParam(ESystemParam.GL0006);
		return "1".equals(flag);
	}

	/**
	 * 
	 * @return true active risk.
	 */
	public static boolean isRiskActiveTemp() {
		String flag = commonBusinessService.getSystemParam(ESystemParam.GL0009);
		return "1".equals(flag);
	}

	/**
	 * 
	 * 公告结束时间
	 */
	public static Date getBulletinEndDate() {
		String dateStr = commonBusinessService
				.getSystemParam(ESystemParam.GL0007);
		try {
			return DateUtils.parseDate(dateStr, new String[] {"yyyy/MM/dd"});
		} catch (Exception e) {
			// ignore
			return null;
		}
	}

	/**
	 * 
	 * 无验证码登陆接口是否启用
	 */
	public static boolean isInternalLoginOpen() {
		String flag = commonBusinessService.getSystemParam(ESystemParam.GL0008);
		return "Y".equals(flag);
	}

}
