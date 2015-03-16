/*
 * Project Name: kmfex-platform
 * File Name: PayeeListBuildReq.java
 * Class Name: PayeeListBuildReq
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

package com.hengxin.platform.product.service.MsgHolder.req;

import java.math.BigDecimal;

/**
 * Class Name: PayeeListBuildReq Description: TODO
 * 
 * @author tingwang
 * 
 */

public class InvestorPayeeListBuildReq {

    private BigDecimal intrBuyerAc = BigDecimal.ZERO;

    private BigDecimal intrForfBuyerAc = BigDecimal.ZERO;

    private BigDecimal prinBuyerAc = BigDecimal.ZERO;

    private BigDecimal prinForfBuyerAc = BigDecimal.ZERO;

    private BigDecimal penaltyBuyerAc = BigDecimal.ZERO;

    private BigDecimal intrBuyer2PlatAc = BigDecimal.ZERO;

    private String userId;

    private String pkgId;

    private String sequenceId = "";

    private String msg;
    
    private BigDecimal intrRate;

    public BigDecimal getIntrBuyerAc() {
        return intrBuyerAc;
    }

    public void setIntrBuyerAc(BigDecimal intrBuyerAc) {
        this.intrBuyerAc = intrBuyerAc;
    }

    public BigDecimal getIntrForfBuyerAc() {
        return intrForfBuyerAc;
    }

    public void setIntrForfBuyerAc(BigDecimal intrForfBuyerAc) {
        this.intrForfBuyerAc = intrForfBuyerAc;
    }

    public BigDecimal getPrinBuyerAc() {
        return prinBuyerAc;
    }

    public void setPrinBuyerAc(BigDecimal prinBuyerAc) {
        this.prinBuyerAc = prinBuyerAc;
    }

    public BigDecimal getPrinForfBuyerAc() {
        return prinForfBuyerAc;
    }

    public void setPrinForfBuyerAc(BigDecimal prinForfBuyerAc) {
        this.prinForfBuyerAc = prinForfBuyerAc;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPkgId() {
        return pkgId;
    }

    public void setPkgId(String pkgId) {
        this.pkgId = pkgId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public BigDecimal getPenaltyBuyerAc() {
        return penaltyBuyerAc;
    }

    public void setPenaltyBuyerAc(BigDecimal penaltyBuyerAc) {
        this.penaltyBuyerAc = penaltyBuyerAc;
    }

    public BigDecimal getIntrBuyer2PlatAc() {
        return intrBuyer2PlatAc;
    }

    public void setIntrBuyer2PlatAc(BigDecimal intrBuyer2PlatAc) {
        this.intrBuyer2PlatAc = intrBuyer2PlatAc;
    }

    public String getSequenceId() {
        return "_" + sequenceId;
    }

    public void setSequenceId(String sequenceId) {
        this.sequenceId = sequenceId;
    }

    public String getIntrRate() {
        return intrRate.multiply(BigDecimal.valueOf(100L)).toString().concat("%");
    }
	
    public void setIntrRate(BigDecimal intrRate) {
        this.intrRate = intrRate;
    }
}
