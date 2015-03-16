/*
 * Project Name: kmfex-platform
 * File Name: ExchagePayeeListBuildReq.java
 * Class Name: ExchagePayeeListBuildReq
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
 * Class Name: ExchagePayeeListBuildReq Description: TODO
 * 
 * @author tingwang
 * 
 */

public class ExchagePayeeListBuildReq {

    private BigDecimal feePlat = BigDecimal.ZERO;

    private BigDecimal feeForfPlat = BigDecimal.ZERO;

    private BigDecimal deviation = BigDecimal.ZERO;

    private BigDecimal buyer2PlatTotalAc = BigDecimal.ZERO;

    private String exchangeUserId;

    private String sequenceId = "";

    private String msg;

    private String pkgId;

    private BigDecimal intrRate;

    public BigDecimal getFeePlat() {
        return feePlat;
    }

    public void setFeePlat(BigDecimal feePlat) {
        this.feePlat = feePlat;
    }

    public BigDecimal getFeeForfPlat() {
        return feeForfPlat;
    }

    public void setFeeForfPlat(BigDecimal feeForfPlat) {
        this.feeForfPlat = feeForfPlat;
    }

    public String getExchangeUserId() {
        return exchangeUserId;
    }

    public void setExchangeUserId(String exchangeUserId) {
        this.exchangeUserId = exchangeUserId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getPkgId() {
        return pkgId;
    }

    public void setPkgId(String pkgId) {
        this.pkgId = pkgId;
    }

    public BigDecimal getDeviation() {
        return deviation;
    }

    public void setDeviation(BigDecimal deviation) {
        this.deviation = deviation;
    }

    public BigDecimal getBuyer2PlatTotalAc() {
        return buyer2PlatTotalAc;
    }

    public void setBuyer2PlatTotalAc(BigDecimal buyer2PlatTotalAc) {
        this.buyer2PlatTotalAc = buyer2PlatTotalAc;
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
