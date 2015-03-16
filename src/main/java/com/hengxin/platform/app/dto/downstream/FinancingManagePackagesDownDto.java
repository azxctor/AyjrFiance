/*
 * Project Name: kmfex-platform
 * File Name: FinancingManagePackagesDownDto.java
 * Class Name: FinancingManagePackagesDownDto
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
package com.hengxin.platform.app.dto.downstream;

import java.io.Serializable;

/**
 * FinancingManagePackagesDownDto：融资管理-融资包管理列表下行DTO.
 * 
 * @author yicai
 *
 */
public class FinancingManagePackagesDownDto implements Serializable {

	private static final long serialVersionUID = 5L;

	private String id; // 融资包编号
	private String packageName; // 融资包名称
	private String financierName; // 融资人
	private String subsPercent; // 申购进度
	private String signContractTime; // 签约时间
	private String status; // 状态
	private String rate; // 年利率
	private String duration; // 融资期限
	private String packageQuota; // 融资额（元）
	private String supplyAmount;// 已申购金额, 实际申购额
	
	// permission
	private boolean canCancelPackage; // 撤单
	private boolean canStopPackage; // 终止申购
	private boolean canSignContract; // 签约
	private boolean canViewRepayTable; // 查看还款表
	private boolean canPrepayment; // 提前还款
	private boolean canPrintDebtInfo; // 打印
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getSignContractTime() {
		return signContractTime;
	}
	public void setSignContractTime(String signContractTime) {
		this.signContractTime = signContractTime;
	}
	public String getPackageQuota() {
		return packageQuota;
	}
	public void setPackageQuota(String packageQuota) {
		this.packageQuota = packageQuota;
	}
	public boolean isCanCancelPackage() {
		return canCancelPackage;
	}
	public void setCanCancelPackage(boolean canCancelPackage) {
		this.canCancelPackage = canCancelPackage;
	}
	public boolean isCanStopPackage() {
		return canStopPackage;
	}
	public void setCanStopPackage(boolean canStopPackage) {
		this.canStopPackage = canStopPackage;
	}
	public boolean isCanSignContract() {
		return canSignContract;
	}
	public void setCanSignContract(boolean canSignContract) {
		this.canSignContract = canSignContract;
	}
	public boolean isCanViewRepayTable() {
		return canViewRepayTable;
	}
	public void setCanViewRepayTable(boolean canViewRepayTable) {
		this.canViewRepayTable = canViewRepayTable;
	}
	public boolean isCanPrepayment() {
		return canPrepayment;
	}
	public void setCanPrepayment(boolean canPrepayment) {
		this.canPrepayment = canPrepayment;
	}
	public boolean isCanPrintDebtInfo() {
		return canPrintDebtInfo;
	}
	public void setCanPrintDebtInfo(boolean canPrintDebtInfo) {
		this.canPrintDebtInfo = canPrintDebtInfo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSupplyAmount() {
		return supplyAmount;
	}
	public void setSupplyAmount(String supplyAmount) {
		this.supplyAmount = supplyAmount;
	}
	public String getFinancierName() {
		return financierName;
	}
	public void setFinancierName(String financierName) {
		this.financierName = financierName;
	}
	public String getSubsPercent() {
		return subsPercent;
	}
	public void setSubsPercent(String subsPercent) {
		this.subsPercent = subsPercent;
	}
	
}
