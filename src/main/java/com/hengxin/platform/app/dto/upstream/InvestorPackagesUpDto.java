/*
 * Project Name: kmfex-platform
 * File Name: InvestorPackagesUpDto.java
 * Class Name: InvestorPackagesUpDto
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
package com.hengxin.platform.app.dto.upstream;

/**
 * 我的债权上行DTO.
 * 
 * @author yicai
 *
 */
public class InvestorPackagesUpDto extends PagingDto {

	private static final long serialVersionUID = 5L;

	private String startDate; // 开始时间
	private String endDate; // 结束时间
	private String keyword; // 关键字
	private String payStatus; // 还款状态
//	private String displayStart;
    private int displayLength; // 每页显示条目数
	private long createTime; // 当前系统时间戳, System.currentTimeMillis() 
	
	
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public int getDisplayLength() {
		return displayLength;
	}
	public void setDisplayLength(int displayLength) {
		this.displayLength = displayLength;
	}
}
