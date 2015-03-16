/*
 * Project Name: kmfex-platform
 * File Name: WithdrawDepositApplStatus.java
 * Class Name: WithdrawDepositApplStatus
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

package com.hengxin.platform.fund.enums;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.hengxin.platform.common.enums.PageEnum;
import com.hengxin.platform.common.util.PageEnumSerializer;

/**
 * Class Name: WithdrawDepositApplStatus Description: TODO
 * 
 * @author tingwang
 * 
 */
@JsonSerialize(using = PageEnumSerializer.class)
public enum EFundApplStatus implements PageEnum {

    NULL("", ""), ALL("ALL", "全部"), WAIT_APPROVAL("WA", "待审批"), APPROVED("A", "审批通过"), REJECT("R", "审批驳回");

    private String code;

    private String text;

    EFundApplStatus(String code, String text) {
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
