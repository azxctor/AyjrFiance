/*
 * Project Name: kmfex-platform
 * File Name: FinancingPackageContractDto.java
 * Class Name: FinancingPackageContractDto
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

package com.hengxin.platform.product.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Class Name: FinancingPackageContractDto Description: TODO
 * 
 * @author yingchangwang
 * 
 */

public class FinancingPackageContractDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String packageId;
    private String userId;
    private String userName;
    private BigDecimal signedAmt;
    private String signDate;
    private String contractTemplateId;
    private String lotId;
    private String contranctNo;
    private String userIdNumber;

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public BigDecimal getSignedAmt() {
        return signedAmt;
    }

    public void setSignedAmt(BigDecimal signedAmt) {
        this.signedAmt = signedAmt;
    }

    public String getSignDate() {
        return signDate;
    }

    public void setSignDate(String signDate) {
        this.signDate = signDate;
    }

    public String getContractTemplateId() {
        return contractTemplateId;
    }

    public void setContractTemplateId(String contractTemplateId) {
        this.contractTemplateId = contractTemplateId;
    }

    public String getLotId() {
        return lotId;
    }

    public void setLotId(String lotId) {
        this.lotId = lotId;
    }

    public String getContranctNo() {
        return contranctNo;
    }

    public void setContranctNo(String contranctNo) {
        this.contranctNo = contranctNo;
    }

    public String getUserIdNumber() {
        return userIdNumber;
    }

    public void setUserIdNumber(String userIdNumber) {
        this.userIdNumber = userIdNumber;
    }
}
