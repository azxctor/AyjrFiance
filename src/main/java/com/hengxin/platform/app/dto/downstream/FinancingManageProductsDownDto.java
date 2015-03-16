/*
 * Project Name: kmfex-platform
 * File Name: FinancingManageProductsDownDto.java
 * Class Name: FinancingManageProductsDownDto
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

import com.hengxin.platform.product.enums.EPayMethodType;
import com.hengxin.platform.product.enums.EProductStatus;
import com.hengxin.platform.product.enums.EWarrantyType;

/**
 * 融资项目下行DTO.
 * 
 * @author yicai
 *
 */
public class FinancingManageProductsDownDto implements Serializable {
	private static final long serialVersionUID = 5L;

	private String productId;// 项目编号
	private String productName;// 项目简称
	private String financierName; // 融资人姓名
	private String lastMntTs; // 最后编辑时间
	private String acctNo; // 交易账号 
	private String applyDate;// 申请日期
	private String appliedQuotaUnit;// 融资申请额(万元)
	private EWarrantyType warrantyType;// 风险保障
	private String finaceTerm;// 融资期限
	private EPayMethodType payMethod;// 还款方式
	private EProductStatus status;// 项目状态
	
//  private String applUserId; // userId
//	private String guaranteeInstitution;// 担保机构
//	private String guaranteeInstitutionShow;// 担保机构
//	private Date applDate;// 申请日期
//	private String apprDate;// 审核时间
//	private String evaluateDate;// 评级时间
//	private String freezeDate;// 冻结时间
//	private String publishDate;// 发布时间
//	private BigDecimal appliedQuota;// 融资申请额
//	private Integer termLength; // 期限长度
//	private ETermType termType; // 期限类型 D-Day,M-Month,Y-Year';

	private boolean canCheck; // 可查看
	private boolean canModify; // 可修改
	private boolean canWithdraw; // 可撤单(担保机构，交易经理)
	private boolean canEvaluate; // 可评级
	private boolean canApprove; // 可审核
	private boolean canFreeze; // 可冻结
	private boolean canPublish; // 可发布
	private boolean canUpdate; // 发布之后的修改
	
	
	
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getFinancierName() {
		return financierName;
	}
	public void setFinancierName(String financierName) {
		this.financierName = financierName;
	}
	public String getLastMntTs() {
		return lastMntTs;
	}
	public void setLastMntTs(String lastMntTs) {
		this.lastMntTs = lastMntTs;
	}
	public String getAcctNo() {
		return acctNo;
	}
	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}
	public String getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}
	public String getAppliedQuotaUnit() {
		return appliedQuotaUnit;
	}
	public void setAppliedQuotaUnit(String appliedQuotaUnit) {
		this.appliedQuotaUnit = appliedQuotaUnit;
	}
	public EWarrantyType getWarrantyType() {
		return warrantyType;
	}
	public void setWarrantyType(EWarrantyType warrantyType) {
		this.warrantyType = warrantyType;
	}
	public String getFinaceTerm() {
		return finaceTerm;
	}
	public void setFinaceTerm(String finaceTerm) {
		this.finaceTerm = finaceTerm;
	}
	public EPayMethodType getPayMethod() {
		return payMethod;
	}
	public void setPayMethod(EPayMethodType payMethod) {
		this.payMethod = payMethod;
	}
	public EProductStatus getStatus() {
		return status;
	}
	public void setStatus(EProductStatus status) {
		this.status = status;
	}
	public boolean isCanCheck() {
		return canCheck;
	}
	public void setCanCheck(boolean canCheck) {
		this.canCheck = canCheck;
	}
	public boolean isCanModify() {
		return canModify;
	}
	public void setCanModify(boolean canModify) {
		this.canModify = canModify;
	}
	public boolean isCanWithdraw() {
		return canWithdraw;
	}
	public void setCanWithdraw(boolean canWithdraw) {
		this.canWithdraw = canWithdraw;
	}
	public boolean isCanEvaluate() {
		return canEvaluate;
	}
	public void setCanEvaluate(boolean canEvaluate) {
		this.canEvaluate = canEvaluate;
	}
	public boolean isCanApprove() {
		return canApprove;
	}
	public void setCanApprove(boolean canApprove) {
		this.canApprove = canApprove;
	}
	public boolean isCanFreeze() {
		return canFreeze;
	}
	public void setCanFreeze(boolean canFreeze) {
		this.canFreeze = canFreeze;
	}
	public boolean isCanPublish() {
		return canPublish;
	}
	public void setCanPublish(boolean canPublish) {
		this.canPublish = canPublish;
	}
	public boolean isCanUpdate() {
		return canUpdate;
	}
	public void setCanUpdate(boolean canUpdate) {
		this.canUpdate = canUpdate;
	}
}
