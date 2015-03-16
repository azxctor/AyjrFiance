/*
 * Project Name: kmfex-platform
 * File Name: AccountBrowsingV3DownDto.java
 * Class Name: AccountBrowsingV3DownDto
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

package com.hengxin.platform.app.dto.downstream;

/**
 * 账户总览三期DTO.
 * 
 * @author yicai
 *
 */
public class AccountBrowsingV3DownDto extends AccountBrowsingV2DownDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String availableAsset = ""; // 可用金额
	private String availableCash = ""; // 可提现金
	
	
	/**
	 * @return the availableAsset
	 */
	public String getAvailableAsset() {
		return availableAsset;
	}
	/**
	 * @param availableAsset the availableAsset to set
	 */
	public void setAvailableAsset(String availableAsset) {
		this.availableAsset = availableAsset;
	}
	/**
	 * @return the availableCash
	 */
	public String getAvailableCash() {
		return availableCash;
	}
	/**
	 * @param availableCash the availableCash to set
	 */
	public void setAvailableCash(String availableCash) {
		this.availableCash = availableCash;
	}
}
