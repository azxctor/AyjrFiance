/*
 * Project Name: kmfex-platform
 * File Name: ProductPackageInvestorDetailsDto.java
 * Class Name: ProductPackageInvestorDetailsDto
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
 * 
 * 债权转让上行DTO.
 * 
 * @author yicai
 *
 */
public class TransferMarketUpDto extends PagingDto {

	private static final long serialVersionUID = 5L;

	private String packageId;
	private String transferType;
//	private String displayStart;
    private int displayLength; // 每页显示条目数
	private long createTime; // 当前系统时间戳, System.currentTimeMillis() 
	
	public String getPackageId() {
		return packageId;
	}
	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}
	public String getTransferType() {
		return transferType;
	}
	public void setTransferType(String transferType) {
		this.transferType = transferType;
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
