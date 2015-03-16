/*
 * Project Name: kmfex-platform
 * File Name: PkgMinSubsDtMsgHolder.java
 * Class Name: PkgMinSubsDtMsgHolder
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

import java.util.Date;

/**
 * Class Name: PkgMinSubsDtMsgHolder Description: TODO
 * 
 * @author tingwang
 * 
 */

public class PkgMinSubsDtMsgHolder {

    private Date subsDt;

    private String userId;

    private String pkgId;

    public Date getSubsDt() {
        return subsDt;
    }

    public void setSubsDt(Date subsDt) {
        this.subsDt = subsDt;
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

}
