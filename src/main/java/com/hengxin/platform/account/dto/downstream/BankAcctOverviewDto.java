/*
 * Project Name: kmfex-platform
 * File Name: BankAcctOverviewDto.java
 * Class Name: BankAcctOverviewDto
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

package com.hengxin.platform.account.dto.downstream;

/**
 * Class Name: BankAcctOverviewDto Description: TODO
 * 
 * @author tingwang
 * 
 */

public class BankAcctOverviewDto {

    private String bnkAcctNo;

    private String bnkName;

    private String bnkCd;

    private String bnkCardImg;

    private String signedFlg;

    private String bnkAcctName;

    private String bnkOpenProv;

    private String bnkOpenCity;

    private String bnkBrch;

    public String getBnkAcctNo() {
        return bnkAcctNo;
    }

    public void setBnkAcctNo(String bnkAcctNo) {
        this.bnkAcctNo = bnkAcctNo;
    }

    public String getBnkName() {
        return bnkName;
    }

    public void setBnkName(String bnkName) {
        this.bnkName = bnkName;
    }

    public String getBnkCd() {
        return bnkCd;
    }

    public void setBnkCd(String bnkCd) {
        this.bnkCd = bnkCd;
    }

    public String getBnkCardImg() {
        return bnkCardImg;
    }

    public void setBnkCardImg(String bnkCardImg) {
        this.bnkCardImg = bnkCardImg;
    }

    public String getSignedFlg() {
        return signedFlg;
    }

    public void setSignedFlg(String signedFlg) {
        this.signedFlg = signedFlg;
    }

    public String getBnkAcctName() {
        return bnkAcctName;
    }

    public void setBnkAcctName(String bnkAcctName) {
        this.bnkAcctName = bnkAcctName;
    }

    public String getBnkOpenProv() {
        return bnkOpenProv;
    }

    public void setBnkOpenProv(String bnkOpenProv) {
        this.bnkOpenProv = bnkOpenProv;
    }

    public String getBnkOpenCity() {
        return bnkOpenCity;
    }

    public void setBnkOpenCity(String bnkOpenCity) {
        this.bnkOpenCity = bnkOpenCity;
    }

    public String getBnkBrch() {
        return bnkBrch;
    }

    public void setBnkBrch(String bnkBrch) {
        this.bnkBrch = bnkBrch;
    }

}
