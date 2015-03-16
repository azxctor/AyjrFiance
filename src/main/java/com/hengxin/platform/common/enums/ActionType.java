/*
 * Project Name: kmfex-platform
 * File Name: ActionType.java
 * Class Name: ActionType
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

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.hengxin.platform.common.util.PageEnumSerializer;

/**
 * Class Name: ActionType Description: TODO
 * 
 * @author chunlinwang
 * 
 */
@JsonSerialize(using = PageEnumSerializer.class)
public enum ActionType implements PageEnum {

    NEW("NEW", "新建"), EDIT("EDIT", "修改"), AUDIT("AUDIT", "会员审核"), AGENTREGISTER("AGENTREGISTER", "代注册"), MAINTAIN(
            "MAINTAIN", "会员信息维护"),PASSWDRESER("PASSWDRESER","密码重置"), TERMINATION("TERMINATION", "终止"), CANCEL("CANCEL", "撤销"), SIGN("SIGN", "签约"), PREPAY(
            "PREPAY", "提前还款"), NEWPRODUCT("NEWPRODUCT", "提交项目"), APPROVE("APPROVE", "融资项目审核"), EVUALUATE("EVUALUATE",
            "风险评级"), FREEZE("FREEZE", "冻结"), PUBLISH("PUBLISH", "参数设置"), RETREAT("RETREAT", "退回上一级"), REVOKE("REVOKE",
            "融资项目撤销"), APPLAPPROVAL("APPLAPPROVAL", "提现申请审核"), THAW("THAW", "解冻"), WAITSUBS("WAITSUBS", "待申购"), SUBSCRIBE(
            "SUBSCRIBE", "申购中"), WAITSIGN("WAITSIGN", "待签约"), PKGSIGN("PKGSIGN", "融资包签约"), PKGLOANAPPROVE(
            "PKGLOANAPPROVE", "融资包放款审批"), PKGTERMINATION("PKGTERMINATION", "融资包终止"), PKGPREPAY("PKGPREPAY", "融资包提前还款"), PKGMANUALREPAY(
            "PKGMANUALREPAY", "融资包手工还款"), PKGCANCEL("PKGCANCEL", "融资包撤销"), PKGABNORMALCANCEL("PKGABNORMALCANCEL",
            "融资包异常撤销"), PKGEXPIRED("PKGEXPIRED", "融资包逾期未签约"), PKGCLEANING("PKGCLEANING", "融资包清分"), PKGLOANAPPROVECONF(
                    "PKGLOANAPPROVECONF", "融资包放款");

    private String code;

    private String text;

    ActionType(String code, String text) {
        this.code = code;
        this.text = text;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }
}
