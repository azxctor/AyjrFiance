/*
 * Project Name: kmfex-platform-trunk
 * File Name: AppConfig.java
 * Class Name: AppConfig
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

package com.hengxin.platform.common.util;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;

/**
 * Class Name: AppConfigUtil Description: 获取应用全局配置
 * 
 * @author zhengqingye
 *
 */
public class AppConfigUtil {
	private static final int DEFAULT_BATCH_SIZE = 100;

	private static final String REPORT_SERV_ADDR = "report.server.address";
	private static final String UPLOAD_FILE_PATH = "upload.file.path";
	private static final String UPLOAD_FILE_URL = "upload.file.url";
	private static final String REPORT_FILE_PATH = "report.file.path";
	private static final String ENVIRONMENT = "environment";
	private static final String IS_BNK_ENABLED = "bnk.is_enabled";
	private static final String EXCEL_TEMPLATE_PATH = "excel.template.path";
	private static final String EXCEL_OVERDUE_TEMPLATE_PATH = "excel.overduetemplate.path";
	private static final String EXCEL_COMPENSATORY_TEMPLATE_PATH = "excel.compensatorytemplate.path";
	private static final String EXCEL_BATCH_TRANSFER_TEMPLATE_PATH = "excel.batch.transfer.template.path";
	private static final String EXCEL_PACKAGE_CONTRACT_PATH = "excel.package.contract.template.path";
	private static final String LEGACY_SYSTEM_URL = "legacy.system.url";
	private static final String EXCEL_POOL_TRX_JNL_TEMPLATE_PATH = "excel.pool.trx.jnl.template.path";
	private static final String EXCEL_PLATACCOUNTDETAIL_TEMPLATE_PATH = "excel.platform.account.details.template.path";
	private static final String EXCEL_PERSONMEMBERINFO_PATH = "excel.personMemberInfo.path";
	private static final String EXCEL_ORGANIZATIONMEMBERINFO_PATH = "excel.organizationMemberInfo.path";
	
	private static final String EXCEL_TRAN_JOUR_TEMPLATE_PATH = "excel.tran.jour.template.path";
	private static final String EXCEL_MYACCOUNTDETAIL_TEMPLATE_PATH = "excel.my.account.details.template.path";
	private static final String EXCEL_WDAA_TEMPLATE_PATH = "excel.withdrawDepositApplsApprovetemplate.path";
	private static final String REPORT_BATCH_SIZE = "report.batch.size";
	private static final String CHINESE_FONT_PATH = "chinese.font.path";

	private static MessageSource messageSource;

	public static String getConfig(String key) {
		return messageSource.getMessage(key, null, Locale.ROOT);
	}

	public static int getReportBatchSize() {
		String value = getConfig(REPORT_BATCH_SIZE);
		if (value == null) {
			return DEFAULT_BATCH_SIZE;
		}
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			return DEFAULT_BATCH_SIZE;
		}
	}

	public static String getChineseFontPath() {
		return getConfig(CHINESE_FONT_PATH);
	}

	public static String getExcelTemplatePath() {
		return getConfig(EXCEL_TEMPLATE_PATH);
	}

	public static String getOverdueExcelTemplatePath() {
		return getConfig(EXCEL_OVERDUE_TEMPLATE_PATH);
	}

	public static String getPollTrxJnlExcelTemplatePath() {
		return getConfig(EXCEL_POOL_TRX_JNL_TEMPLATE_PATH);
	}

	public static String getPlatAccountDetailExcelTemplatePath() {
		return getConfig(EXCEL_PLATACCOUNTDETAIL_TEMPLATE_PATH);
	}

	public static String getPersonMemberInfoExcelTemplatePath() {
		return getConfig(EXCEL_PERSONMEMBERINFO_PATH);
	}
	
	public static String getOrganizationMemberInfoExcelTemplatePath() {
		return getConfig(EXCEL_ORGANIZATIONMEMBERINFO_PATH);
	}
	
	public static String getTransactionJournalExcelTemplatePath() {
		return getConfig(EXCEL_TRAN_JOUR_TEMPLATE_PATH);
	}

	public static String getMyAccountDetailExcelTemplatePath() {
		return getConfig(EXCEL_MYACCOUNTDETAIL_TEMPLATE_PATH);
	}

	public static String getWithdrawDAATExcelTemplatePath() {
		return getConfig(EXCEL_WDAA_TEMPLATE_PATH);
	}

	public static String getCOMPENSATORYExcelTemplatePath() {
		return getConfig(EXCEL_COMPENSATORY_TEMPLATE_PATH);
	}

	public static String getExcelBatchTransferTemplatePath() {
		return getConfig(EXCEL_BATCH_TRANSFER_TEMPLATE_PATH);
	}

	public static String getExcelPackageContractTemplatePath() {
		return getConfig(EXCEL_PACKAGE_CONTRACT_PATH);
	}

	/**
	 * 当前环境是否是生产环境
	 *
	 * @return
	 */
	public static boolean isProdEnv() {
		return getConfig(ENVIRONMENT).equalsIgnoreCase("PROD");
	}

	/**
	 * 当前环境是否是本地开发环境
	 *
	 * @return
	 */
	public static boolean isDevEnv() {
		return getConfig(ENVIRONMENT).equalsIgnoreCase("DEV");
	}

	/**
	 * EBI报表服务器的入口地址URL
	 *
	 * @return
	 */
	public static String getReportServerAddr() {
		return getConfig(REPORT_SERV_ADDR);
	}

	/**
	 * 文件上传内部路径
	 *
	 * @return
	 */
	public static String getUploadFilePath() {
		return getConfig(UPLOAD_FILE_PATH);
	}

	/**
	 * 上传文件对外URL
	 *
	 * @return
	 */
	public static String getUploadFileURL() {
		return getConfig(UPLOAD_FILE_URL);
	}

	/**
	 * @return
	 */
	public static String getReportFilePath() {
		return getConfig(REPORT_FILE_PATH);
	}

	/**
	 * @param messageSource
	 *            Set messageSource value
	 */
	public static void setMessageSource(MessageSource messageSource) {
		AppConfigUtil.messageSource = messageSource;
	}

	/**
	 * 是否使用招行WebService
	 *
	 * @return
	 */
	public static boolean isBnkEnabled() {
		return getConfig(IS_BNK_ENABLED).equalsIgnoreCase("true");
	}

	/**
	 * 老系统URL
	 */
	public static String getLegacySystemUrl() {
		return getConfig(LEGACY_SYSTEM_URL);
	}

	/**
	 * 获取ebc秘钥
	 * 
	 * @return
	 */
	public static String getEbcSignKey() {
		return getConfig("ebc.sign.key");
	}

	/**
	 * esw是否生产环境
	 * 
	 * @return
	 */
	public static boolean isEswProd() {
		return StringUtils.equalsIgnoreCase("true", getConfig("ebc.isProd"));
	}

	/**
	 * 获取ebc ftp ip
	 * 
	 * @return
	 */
	public static String getEbcFtpHost() {
		return getConfig("ebc.ftp.host.ip");
	}

	/**
	 * 获取ebc ftp用户名
	 * 
	 * @return
	 */
	public static String getEbcFtpUserName() {
		return getConfig("ebc.ftp.username");
	}

	/**
	 * 获取ebc ftp密码
	 * 
	 * @return
	 */
	public static String getEbcFtpPassword() {
		return getConfig("ebc.ftp.password");
	}

	/**
	 * 获取ebc对账文件目录
	 * 
	 * @return
	 */
	public static String getEbcReconPath() {
		return getConfig("ebc.recon.file.save.path");
	}
}
