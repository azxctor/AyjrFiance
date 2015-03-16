/*
 * Project Name: kmfex-platform
 * File Name: ActionResult.java
 * Class Name: ActionResult
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
import java.util.Date;

import com.hengxin.platform.common.dto.DataTablesRequestDto;
import com.hengxin.platform.product.enums.EPackageStatus;
import com.hengxin.platform.product.enums.EPayStatus;

/**
 * @author yingchangwang
 * 
 */
public class FinancingPackageSearchDto extends DataTablesRequestDto implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String keyword;
    private EPackageStatus packageStatus;
    private String startDate; // 开始时间
    private String endDate; // 结束时间
    private String signDate;
    private String endStartDate; // 结清开始时间
    private String endEndDate; // 结清结束时间
    private EPayStatus payStatus; // 还款状态
    private Date createTime; // 当前系统时间戳
    
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public EPackageStatus getPackageStatus() {
        return packageStatus;
    }

    public void setPackageStatus(EPackageStatus packageStatus) {
        this.packageStatus = packageStatus;
    }

    /**
     * @return return the value of the var startDate
     */

    public String getStartDate() {
        return startDate;
    }

    /**
     * @param startDate
     *            Set startDate value
     */

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * @return return the value of the var endDate
     */

    public String getEndDate() {
        return endDate;
    }

    /**
     * @param endDate
     *            Set endDate value
     */

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getSignDate() {
        return signDate;
    }

    /**
     * 
     * @param signDate
     */
    public void setSignDate(String signDate) {
        this.signDate = signDate;
    }

    public String getEndStartDate() {
        return endStartDate;
    }

    public void setEndStartDate(String endStartDate) {
        this.endStartDate = endStartDate;
    }

    public String getEndEndDate() {
        return endEndDate;
    }

    public void setEndEndDate(String endEndDate) {
        this.endEndDate = endEndDate;
    }

    public EPayStatus getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(EPayStatus payStatus) {
        this.payStatus = payStatus;
    }

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
