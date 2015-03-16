/*
 * Project Name: kmfex-platform
 * File Name: PkgSubsUnitSumMsgHolder.java
 * Class Name: PkgSubsUnitSumMsgHolder
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

package com.hengxin.platform.product.service.MsgHolder;

import java.math.BigDecimal;

/**
 * Class Name: PkgSubsUnitSumMsgHolder Description: TODO
 * 
 * @author tingwang
 * 
 */

public class PkgSubsUnitSumMsgHolder {
    private String userId;

    private BigDecimal unit;

    private String pkgId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getUnit() {
        return unit;
    }

    public void setUnit(BigDecimal unit) {
        this.unit = unit;
    }

    public String getPkgId() {
        return pkgId;
    }

    public void setPkgId(String pkgId) {
        this.pkgId = pkgId;
    }

}
