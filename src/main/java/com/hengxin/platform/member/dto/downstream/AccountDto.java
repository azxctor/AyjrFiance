/*
 * Project Name: kmfex-platform
 * File Name: AccountDto.java
 * Class Name: AccountDto
 *
 * Copyright 2014 KMFEX Inc
 *
 * 
 *
 * http://www.kmfex.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hengxin.platform.member.dto.downstream;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Class Name: AccountDto
 * 
 * @author runchen
 * 
 */
public class AccountDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("number")
    private String acctNo;

    @JsonProperty("create_time")
    private String createTs;

    public String getAcctNo() {
        return acctNo;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }


    public String getCreateTs() {
        return createTs;
    }

    public void setCreateTs(String createTs) {
        this.createTs = createTs;
    }

}
