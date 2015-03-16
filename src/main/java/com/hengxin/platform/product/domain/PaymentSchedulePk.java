
/*
* Project Name: kmfex-platform
* File Name: PaymentSchedulePk.java
* Class Name: PaymentSchedulePk
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
	
package com.hengxin.platform.product.domain;

import java.io.Serializable;


/**
 * Class Name: PaymentSchedulePk
 * Description: TODO
 * @author tingwang
 *
 */

@SuppressWarnings("serial")
public class PaymentSchedulePk implements Serializable{

    private String packageId;
    
    private int sequenceId;

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public int getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(int sequenceId) {
        this.sequenceId = sequenceId;
    }
    
    
}
