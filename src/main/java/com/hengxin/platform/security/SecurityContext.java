/*
 * Project Name: kmfex-platform-trunk
 * File Name: PageSecurityContext.java
 * Class Name: PageSecurityContext
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

package com.hengxin.platform.security;

import org.springframework.stereotype.Component;

/**
 * Class Name: PageSecurityContext Description: TODO
 * 
 * @author zhengqingye
 * 
 */
@Component
public class SecurityContext extends BaseSecurityContext {
    /**
     * Description:是否平台用户
     * 
     * @return
     */
    public boolean isPlatformUser() {
        return hasPermission(Permissions.PLATFORM_USER);
    }

    /**
     * Description:是否客服部人员
     * 
     * @return
     */
    public boolean isCustomerServiceUser() {
        return hasPermission(Permissions.CUSTOMER_SERVICE_DEPT);
    }

    /**
     * Description:是否客服部经理
     * 
     * @return
     */
    public boolean isCustomerServiceManager() {
        return hasPermission(Permissions.CUSTOMER_SERVICE_MANAGER);
    }

    /**
     * Description:是否产品部人员
     * 
     * @return
     */
    public boolean isProdDeptUser() {
        return hasPermission(Permissions.PRODUCT_DEMPARTMENT);
    }

    /**
     * Description:是否渠道部人员
     * 
     * @return
     */
    public boolean isChannelDeptUser() {
        return hasPermission(Permissions.CHANNEL_DEMPARTMENT);
    }

    /**
     * Description:是否风控部人员
     * 
     * @return
     */
    public boolean isRiskControlDeptUser() {
        return hasPermission(Permissions.RISK_CONTROL_DEPT);
    }

    /**
     * Description:是否结算部人员
     * 
     * @return
     */
    public boolean isSettlementDeptUser() {
        return hasPermission(Permissions.SETTLEMENT_DEMPARTMENT);
    }

    /**
     * Description:是否交易部人员
     * 
     * @return
     */
    public boolean isTransDeptUser() {
        return hasPermission(Permissions.TRANSACTION_DEPT);
    }
    
    /**
     * Description:是否财务部人员
     * 
     * @return
     */
    public boolean isFinanceDeptUser() {
        return hasPermission(Permissions.FINANCE_DEMPARTMENT);
    }

    /**
     * Description:是否系统管理员
     * 
     * @return
     */
    public boolean isSystemAdmin() {
        return hasPermission(Permissions.SYSTEM_ADMIN);
    }
    
    /**
     * Description:是否投资会员
     * 
     * @return
     */
    public boolean isInvestor() {
        return hasPermission(Permissions.INVESTOR_MEMBER);
    }
    /**
     * Description:是否融资资会员
     * 
     * @return
     */
    public boolean isFinancier() {
        return hasPermission(Permissions.FINANCIER_MEMBER);
    }
    /**
     * Description:是否担保机构
     * 
     * @return
     */
    public boolean isProdServ() {
        return hasPermission(Permissions.PROD_SERV_MEMBER);
    }
    /**
     * Description:是否游客(投融资)
     * 
     * @return
     */
    public boolean isInvestorFinancierTourist() {
        return hasPermission(Permissions.TOURIST_MEMBER);
    }
    /**
     * Description:是否游客(担保机构/服务中心)
     * 
     * @return
     */
    public boolean isProdServAthdCenterTourist() {
        return hasPermission(Permissions.TOURIST_AGENCY_MEMBER);
    }

    /**
     * Description: 获取SecurityContext
     * 
     * @return
     */
    public static SecurityContext getInstance() {
        return new SecurityContext();
    }

    /**
     * Description: 例子：可以代注册
     * 
     * @return
     */
    public boolean canRegisterForMember() {
        return hasPermission(Permissions.MEMBER_REGISTER_FOR_USER);
    }

    /**
     * 
     * Description: 可以修改会员基本信息
     * 
     * @return
     */
    public boolean canEditMemberBasicInfo() {
        return hasAnyPermission(new String[] { Permissions.MEMBER_ADV_MAINT_INV_FIN_LEVEL_CHANGE,
        		Permissions.MEMBER_ADV_MAINT_INVST_FIN_UPDATE, Permissions.MEMBER_BASIC_INFO_UPDATE });
    }

    /**
     * 
     * Description: 有平台修改会员基本信息权限
     * 
     * @return
     */
    public boolean canAdvancedBasicInfoEdit() {
        return hasAnyPermission(new String[] { Permissions.MEMBER_ADV_MAINT_INV_FIN_LEVEL_CHANGE, Permissions.MEMBER_ADV_MAINT_INVST_FIN_UPDATE });
    }

    /**
     * 是否有权限显示小微宝
     * 
     * @return
     */
    public boolean canOpenXWB() {
        return hasPermission(Permissions.MY_ACCOUNT_OVERVIEW_XWB);
    }

    
    /**
     * Description:服务中心
     * 
     * @return
     */
    public boolean isAuthServiceCenter() {
        return hasPermission(Permissions.ATHD_CENTER_MEMBER);
    }

    
    /**
     * Description:服务中心经理
     * 
     * @return
     */
    public boolean isAuthServiceCenterManager() {
        return hasPermission(Permissions.ATHD_CENTER_MANAGER);
    }

    /**
     * 是否是 <strong>代理</strong> 服务中心.
     * @return
     */
    public boolean isAuthServiceCenterAgent() {
    	return isAuthServiceCenter() && !hasPermission(Permissions.ATHD_CNTL_TRADE_TRACE);
    }

    /**
     * 是否是  <strong>普通</strong> 服务中心.
     * @return
     */
    public boolean isAuthServiceCenterGeneral() {
    	return isAuthServiceCenter() && !hasPermission(Permissions.ATHD_CNTL_TRADE_TRACE) && !hasPermission(Permissions.MARKETING_CREDITOR_VIEW);
    }
    
    /**
     * 
     * Description: 可以修改会员投资信息
     * 
     * @return
     */
    public boolean canEditMemberInvestorInfo() {
        return hasAnyPermission(new String[] { Permissions.MEMBER_ADV_MAINT_INVST_FIN_UPDATE, Permissions.MEMBER_PROFILE_INVESTER_UPDATE });
    }

    /**
     * 
     * Description: 可以修改会员融资信息
     * 
     * @return
     */
    public boolean canEditMemberFinancierInfo() {
        return hasAnyPermission(new String[] { Permissions.MEMBER_ADV_MAINT_INVST_FIN_UPDATE, Permissions.MEMBER_PROFILE_FINANCIER_UPDATE });
    }

    /**
     * 
     * Description: 可以变更投资会员的等级
     * 
     * @return
     */
    public boolean canEditInvestorLevel() {
        // return isPermitted(Permissions.MEMBER_ATHD_SERV_CNTL_UPDATE);
        return false;
    }

    /**
     * 
     * Description: 可以变更投资会员的授权服务中心和经办人
     * 
     * @return
     */
    public boolean canEditAgencyAndServerCenter() {
        // return isPermitted(Permissions.MEMBER_ATHD_SERV_CNTL_UPDATE);
        return false;
    }

    /**
     * 
     * Description: 可以变更融资会员的等级
     * 
     * @return
     */
    public boolean canEditFinancierLevel() {
        // return isPermitted(Permissions.MEMBER_ATHD_SERV_CNTL_UPDATE);
        return false;
    }

    /**
     * 更改投融资会员信息
     * 
     * @return
     */
    public boolean canUpdateInvstFinInfo() {
        return hasPermission(Permissions.MEMBER_ADV_MAINT_INVST_FIN_UPDATE);
    }

    /**
     * 更改担保机构信息
     * 
     * @return
     */
    public boolean canUpdateProdServInfo() {
        String[] array = new String[] { Permissions.MEMBER_ADV_MAINT_PROD_SERV_UPDATE, Permissions.MEMBER_ADV_MAINT_ATHD_CNTR_UPDATE };
        return hasAnyPermission(array);
    }

    /**
     * 审核融资项目
     * 
     * @return
     */
    public boolean canApproveFinancingProduct() {
        String[] array = new String[] { Permissions.PRODUCT_FINANCING_PLATFORM_APPROVE };
        return hasAnyPermission(array);
    }

    /**
     * 融资项目评级
     * 
     * @return
     */
    public boolean canRatingFinancingProduct() {
        String[] array = new String[] { Permissions.PRODUCT_FINANCING_PLATFORM_RATING };
        return hasAnyPermission(array);

    }

    /**
     * 融资项目保证金冻结
     * 
     * @return
     */
    public boolean canFreezeFinancingProductDeposit() {
        String[] array = new String[] { Permissions.PRODUCT_FINANCING_DEPOSIT_FREEZE };
        return hasAnyPermission(array);

    }

    /**
     * 融资项目平台冻结金额修改
     * 
     * @return
     */
    public boolean canUpdateFinancingProductDeposit() {
        String[] array = new String[] { Permissions.PRODUCT_FINANCING_DEPOSIT_UPDATE };
        return hasAnyPermission(array);

    }

    /**
     * 交易参数设置
     * 
     * @return
     */
    public boolean canSetFinancingProductTransSettings() {
        String[] array = new String[] { Permissions.PRODUCT_FINANCING_TRANSACTION_SETTINGS };
        return hasAnyPermission(array);

    }

    /**
     * 可以撤单
     * 
     * @return
     */
    public boolean canCancelFinancingPackage() {
        String[] array = new String[] { Permissions.PRODUCT_FINANCING_PACKAGE_CANCEL };
        return hasAnyPermission(array);
    }

    /**
     * 可以提前还款
     * 
     * @return
     */
    public boolean canPrepaymentFinancingPackage() {
        String[] array = new String[] { Permissions.PRODUCT_FINANCING_PACKAGE_PREPAYMENT };
        return hasAnyPermission(array);
    }

    /**
     * 可以签约
     * 
     * @return
     */
    public boolean canSignContract() {
        String[] array = new String[] { Permissions.PRODUCT_FINANCING_PACKAGE_SIGN_CONTRACT };
        return hasAnyPermission(array);
    }

    /**
     * 可以终止融资包
     * 
     * @return
     */
    public boolean canStopFinancingPackage() {
        String[] array = new String[] { Permissions.PRODUCT_FINANCING_PACKAGE_STOP };
        return hasAnyPermission(array);
    }

    /**
     * 可以查看融资包还款表
     * 
     * @return
     */
    public boolean canViewRepayTable() {
        String[] array = new String[] { Permissions.PRODUCT_FINANCING_PACKAGE_PAYMENT_VIEW };
        return hasAnyPermission(array);
    }

    /**
     * 可以查看提现申请表
     * 
     * @return
     */
    public boolean canViewWithdrawDepositApplTable() {
        String[] array = new String[] { Permissions.MY_ACCOUNT_OVERVIEW_UNSIGNED_MEMBER_WITHDRAW_APPLY_VIEW };
        return hasAnyPermission(array);
    }

    /**
     * 可以审批提现申请表
     * 
     * @return
     */
    public boolean canApproveWithdrawDepositAppl() {
        String[] array = new String[] { Permissions.PRODUCT_FINANCING_WITHDRAW_APPROVE };
        return hasAnyPermission(array);
    }

    /**
     * 可以决定提现
     * 
     * @return
     */
    public boolean canConfirmWithdrawDepositAppl() {
        String[] array = new String[] { Permissions.SETTLEMENT_UNSIGNED_MEMBER_WITHDRAW_CONFIRM };
        return hasAnyPermission(array);
    }

    /**
     * 可以查看用户真实姓名
     * 
     * @return
     */
    public boolean cannotViewRealName(String userId, boolean inCanViewPage) {
    	if (inCanViewPage) {
			return false;
		}
    	if (hasPermission(Permissions.CUSTOMER_SERVICE_DEPT)) {
    		if (hasPermission(Permissions.MEMBER_ADV_MAINT_INV_FIN_LEVEL_CHANGE)) {
    			/** 客服经理. **/
    			return false;
			} else {
				/** 客服专员. **/
//				return true;
				return false;
			}
		} else if (hasPermission(Permissions.ATHD_CENTER_MEMBER)) {
			/** 服务中心. **/
			String[] array = new String[] { Permissions.MEMBER_VIEW_REAL_NAME };
			boolean flag = !hasAnyPermission(array) && isNotCurrentUser(userId);
	        return flag;
		} else {
			return false;
		}
    }

    /**
     * 可以查看用户真实身份证
     * 
     * @return
     */
    public boolean cannotViewRealIdCardNo(String userId, boolean inCanViewPage) {
    	if (inCanViewPage) {
			return false;
		}
    	if (hasPermission(Permissions.CUSTOMER_SERVICE_DEPT)) {
    		if (hasPermission(Permissions.MEMBER_ADV_MAINT_INV_FIN_LEVEL_CHANGE)) {
    			/** 客服经理. **/
    			return false;
			} else {
				/** 客服专员. **/
//				return true;
				return false;
			}
		} else if (hasPermission(Permissions.ATHD_CENTER_MEMBER)) {
			/** 服务中心. **/
			String[] array = new String[] { Permissions.MEMBER_VIEW_REAL_IDCARD_NO };
			boolean flag = !hasAnyPermission(array) && isNotCurrentUser(userId);
	        return flag;
		} else {
			return false;
		}
    }

    /**
     * 可以查看用户真实手机号
     * 
     * @return
     */
    public boolean cannotViewRealPhoneNo(String userId, boolean inCanViewPage) {
    	if (inCanViewPage) {
			return false;
		}
    	if (hasPermission(Permissions.CUSTOMER_SERVICE_DEPT)) {
    		if (hasPermission(Permissions.MEMBER_ADV_MAINT_INV_FIN_LEVEL_CHANGE)) {
    			/** 客服经理. **/
    			return false;
			} else {
				/** 客服专员. **/
//				return true;
				return false;
			}
		} else if (hasPermission(Permissions.ATHD_CENTER_MEMBER)) {
			/** 服务中心. **/
			String[] array = new String[] { Permissions.MEMBER_VIEW_REAL_PHONE_NO };
			boolean flag = !hasAnyPermission(array) && isNotCurrentUser(userId);
	        return flag;
		} else {
			return false;
		}
    }

    /**
     * 可以查看用户真实银行卡号
     * 
     * @return
     */
    public boolean cannotViewRealBankCardNo(String userId, boolean inCanViewPage) {
    	if (inCanViewPage) {
			return false;
		}
    	if (hasPermission(Permissions.CUSTOMER_SERVICE_DEPT)) {
    		if (hasPermission(Permissions.MEMBER_ADV_MAINT_INV_FIN_LEVEL_CHANGE)) {
    			/** 客服经理. **/
    			return false;
			} else {
				/** 客服专员. **/
//				return true;
				return false;
			}
		} else if (hasPermission(Permissions.ATHD_CENTER_MEMBER)) {
			/** 服务中心. **/
			String[] array = new String[] { Permissions.MEMBER_VIEW_REAL_BANKCARD_NO };
			boolean flag = !hasAnyPermission(array) && isNotCurrentUser(userId);
	        return flag;
		} else {
			return false;
		}
    }

    /**
     * 可以查看用户真实银行卡号
     * 
     * @return
     */
    public boolean cannotViewApplicationForm(String userId, boolean inCanViewPage) {
    	if (inCanViewPage) {
			return false;
		}
        String[] array = new String[] { Permissions.MEMBER_VIEW_REAL_BANKCARD_NO,
                Permissions.MEMBER_VIEW_REAL_PHONE_NO, Permissions.MEMBER_VIEW_REAL_IDCARD_NO,
                Permissions.MEMBER_VIEW_REAL_NAME };
        return !hasAllPermissions(array) && isNotCurrentUser(userId);
    }

    /**
     * 有信息维护权限
     * 
     * @return
     */
    public boolean canUpdateMemberDetail(boolean inCanViewPage) {
        String[] array = new String[] { Permissions.MEMBER_ADV_MAINT_INV_FIN_LEVEL_CHANGE,
                Permissions.MEMBER_ADV_MAINT_PROD_SERV_UPDATE, Permissions.MEMBER_ADV_MAINT_ATHD_CNTR_UPDATE,
                Permissions.MEMBER_ADV_MAINT_INVST_FIN_UPDATE};
        return hasAnyPermission(array) || inCanViewPage;
    }

    /**
     * 是否为当前用户
     * 
     * @return
     */
    public boolean isNotCurrentUser(String userId) {
        if (getCurrentUser() == null) {
            return false;
        }
        return !getCurrentUserId().equals(userId);
    }
    
    /**
     * 是否有打印签约借据信息权限
    *
    * @return
     */
    public boolean canPrintDebtInfo(){
        String[] array = new String[] { Permissions.PRODUCT_FINANCING_PACKAGE_VIEW_FOR_CUST_PRINT_DEBT};
        return hasAnyPermission(array);
    };

    /**
     * 是否有查看债权转让行情权限
     * 
     * @return 
     */
    public boolean canViewTransferMarket() {
        return hasAnyPermission(new String[] { Permissions.MARKETING_CREDITOR_ASSIGNMENT_VIEW });
    }
    
    /**
     * 是否有编辑介绍人权限（客服经理，服务中心经理可以/客服，服务中心其他角色不行）
     * 
     * @param userId
     * @return true 客服经理，服务中心管理员可以编辑.
     */
    public boolean canEditAgentName(){	
    	if(isCustomerServiceManager()){
    		// 客服经理
    		return true; 
    	}else if(isAuthServiceCenterManager()){
    		// 服务中心经理
    		return true;
    	}else{
	    	// 其他角色
	    	return false;
    	}
    }
}
