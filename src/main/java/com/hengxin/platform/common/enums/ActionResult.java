/*
 * Project Name: kmfex-platform
 * File Name: ActionResult.java
 * Class Name: ActionResult
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

package com.hengxin.platform.common.enums;

/**
 * Class Name: ActionResult Description: TODO
 *
 * @author chunlinwang
 *
 */

public enum ActionResult implements PageEnum {
    /**
    * code格式规范： 1xxx - 会员； 2xxx - 产品；3xxx - 资金
    */
    EMPTY("0000", ""),
    BASIC_INFO_AUDIT_PASS("1001", "基本信息审核通过"),
    BASIC_INFO_AUDIT_REJECT("1002", "基本信息审核拒绝"),
    INVESTOR_AUDIT_PASS("1003", "投资权限审核通过"),
    INVESTOR_AUDIT_REJECT("1004", "投资权限审核拒绝"),
    FINANCIER_AUDIT_PASS("1005", "融资权限审核通过"),
    FINANCIER_AUDIT_REJECT("1006", "融资权限审核拒绝"),
    SERVICE_CENTER_AUDIT_PASS("1007", "服务中心审核通过"),
    SERVICE_CENTER_AUDIT_REJECT("1008","服务中心审核拒绝"),
    PROVIDER_CENTER_AUDIT_PASS("1009", "担保机构审核通过"),
    PROVIDER_CENTER_AUDIT_REJECT("1010","担保机构审核拒绝"),
    INVESTOR_NEW("1011", "投资权限申请"),
    FINANCIER_NEW("1012", "融资权限申请"),
    BASIC_INFO_NEW("1013","基本信息申请"),
    SERVICE_CENTER_NEW("1014", "服务中心权限申请"),
    PROVIDER_CENTER_NEW("1015", "担保机构权限申请"),
    BASIC_INFO_EDIT("1016", "基本信息修改"),
    INVESTOR_EDIT("1017", "投资信息修改"),
    FINANCIER_EDIT("1018", "融资信息修改"),
    PROVIDER_CENTER_EDIT("1019", "担保机构信息修改"),
    SERVICE_CENTER_EDIT("1020", "服务中心信息修改"),
    PASSWORD_RESET("1021", "会员密码重置"),
    PRODUCT_PACKAGE_TERMINATION("2001", "融资包终止"),
    PRODUCT_PACKAGE_CANCEL("2002", "融资包撤销"),
    PRODUCT_PACKAGE_SIGN("2003", "融资包签约"),
    PRODUCT_PACKAGE_PREPAY("2004", "融资包提前还款"),
    PRODUCT_PACKAGE_LOAN_APPROVAL("2005", "放款审批"),
    PRODUCT_AUDIT_PASS("2006", "融资项目审核通过"),
    PRODUCT_AUDIT_REJECT("2007", "融资项目审核拒绝"),
    PRODUCT_REVOKE("2008", "融资项目撤销"),
    PRODUCT_PUBLISH_SUCCESS("2009","项目发布成功"),
    PRODUCT_THAW_PRODUCT_PROVIDER("2010","解保留担保公司还款保证金"),
    PRODUCT_THAW_FINANCE_SERVICE("2011","解保留融资服务合同保证金"),
    PRODUCT_PACKAGE_MANUAL_REPAY("2012", "融资包手工还款"),
    PRODUCT_PACKAGE_ABNORMAL_CANCEL("2013", "融资包异常撤销"),
    PRODUCT_PACKAGE_WAIT_SUBSCRIBE("2014", "融资包到预发布时间待申购"), 
    PRODUCT_PACKAGE_WAIT_SIGN("2015", "融资包到期待签约"),
    PRODUCT_PACKAGE_SIGN_EXPIRED("2016", "融资包到期未签约"),
    PRODUCT_PACKAGE_CLEANING("2017", "融资包清分"),
    PRODUCT_PACKAGE_SETTINGS("2018","拆包成功"),
    PRODUCT_PACKAGE_CHANGE_TO_SUBSCRIBE("2019","融资包到期申购中"),
    PRODUCT_PACKAGE_SETTINGS_UPDATE("2020","编辑融资包"),
    PRODUCT_PACKAGE_LOAN_APPROVAL_CONFIRM("2021", "放款"),
    FUND_WITHDDEPAPPL_APPROVED("3001", "提现申请审批通过"),
    FUND_WITHDDEPAPPL_REJECT("3002", "提现申请审批否决");

    private String code;

    private String text;

    ActionResult(String code, String text) {
        this.code = code;
        this.text = text;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }
}
