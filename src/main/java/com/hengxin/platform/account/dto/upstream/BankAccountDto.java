
/*
* Project Name: kmfex-platform
* File Name: BankAccountDto.java
* Class Name: BankAccountDto
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
	
package com.hengxin.platform.account.dto.upstream;


/**
 * Class Name: BankAccountDto
 * Description: TODO
 * @author congzhou
 *
 */

public class BankAccountDto {

    /**
     * 银行账号
     */
    private String bankAcctNo;

    
    /**
    * BankAccountDto Constructor
    *
    * @param bankAcctNo
    */
    	
    public BankAccountDto(String bankAcctNo) {
        super();
        this.bankAcctNo = bankAcctNo;
    }

    public String getBankAcctNo() {
        return bankAcctNo;
    }

    public void setBankAcctNo(String bankAcctNo) {
        this.bankAcctNo = bankAcctNo;
    }
    
}
