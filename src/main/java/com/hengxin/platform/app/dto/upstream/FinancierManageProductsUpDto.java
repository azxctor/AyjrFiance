/*
 * Project Name: kmfex-platform
 * File Name: FinancierManageProductPackagesUpDto.java
 * Class Name: FinancierManageProductPackagesUpDto
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
 * 融资项目管理上行DTO.
 * 
 * 
 * @author yicai
 *
 */
public class FinancierManageProductsUpDto extends PagingDto {

	private static final long serialVersionUID = 5L;

	private String searchKeyString;  // 关键字
	private String productStatus; // 融资项目状态
//	private String resultType; // 待处理，已处理
//	private String productActionType; // 动作类型
//	private String comString; // 包或者产品状态
	
    private int displayLength; // 每页显示条目数
//	private long createTime; // 当前系统时间戳, System.currentTimeMillis() 
	
	
	public String getSearchKeyString() {
		return searchKeyString;
	}
	public void setSearchKeyString(String searchKeyString) {
		this.searchKeyString = searchKeyString;
	}
	public String getProductStatus() {
		return productStatus;
	}
	public void setProductStatus(String productStatus) {
		this.productStatus = productStatus;
	}
//	public String getResultType() {
//		return resultType;
//	}
//	public void setResultType(String resultType) {
//		this.resultType = resultType;
//	}
//	public String getProductActionType() {
//		return productActionType;
//	}
//	public void setProductActionType(String productActionType) {
//		this.productActionType = productActionType;
//	}
//	public String getComString() {
//		return comString;
//	}
//	public void setComString(String comString) {
//		this.comString = comString;
//	}
//	public long getCreateTime() {
//		return createTime;
//	}
//	public void setCreateTime(long createTime) {
//		this.createTime = createTime;
//	}
	public int getDisplayLength() {
		return displayLength;
	}
	public void setDisplayLength(int displayLength) {
		this.displayLength = displayLength;
	}
}
