/*
 * Project Name: kmfex-platform
 * File Name: SubAcctTrxJnl2XWBDetailConverter.java
 * Class Name: SubAcctTrxJnl2XWBDetailConverter
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

package com.hengxin.platform.fund.entity.converter;

import com.hengxin.platform.account.enums.EXWBTradeType;
import com.hengxin.platform.common.converter.ObjectConverter;
import com.hengxin.platform.fund.domain.SubAcctTrxJnl;
import com.hengxin.platform.fund.entity.SubAcctTrxJnlPo;
import com.hengxin.platform.fund.enums.EFundPayRecvFlag;
import com.hengxin.platform.fund.enums.EFundUseType;

/**
 * Class Name: SubAcctTrxJnl2XWBDetailConverter Description: TODO
 * 
 * @author tingwang
 * 
 */

public class SubAcctTrxJnl2XWBDetailConverter implements ObjectConverter<SubAcctTrxJnlPo, SubAcctTrxJnl> {

    @Override
    public void convertFromDomain(SubAcctTrxJnl domain, SubAcctTrxJnlPo target) {
        // TODO Auto-generated method stub

    }

    @Override
    public void convertToDomain(SubAcctTrxJnlPo source, SubAcctTrxJnl domain) {
        if (source.getUseType()==EFundUseType.INTERNAL) {
            if (EFundPayRecvFlag.PAY==source.getPayRecvFlg()) {
                domain.setTrxType(EXWBTradeType.OUT.getText());
            } else if (EFundPayRecvFlag.RECV==source.getPayRecvFlg()) {
                domain.setTrxType(EXWBTradeType.IN.getText());
            }
        } else if (source.getUseType()==EFundUseType.XWBINTEREST) {
            if (EFundPayRecvFlag.RECV==source.getPayRecvFlg()) {
                domain.setTrxType(EXWBTradeType.PROFIT.getText());
            }
        }
    }

}
