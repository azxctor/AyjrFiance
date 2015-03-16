/*
 * Project Name: kmfex-platform
 * File Name: AccountDetailListDownstreamDto.java
 * Class Name: AccountDetailListDownstreamDto
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

import java.io.Serializable;
import java.util.List;

/**
 * 账户明显下行DTO.
 * 
 * @author yicai
 *
 */
public class AccountDetailListDownstreamDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<DetailListDto> detailList;
	
	private long currentTime = System.currentTimeMillis();
	
	public long getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(long currentTime) {
		this.currentTime = currentTime;
	}

	public List<DetailListDto> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<DetailListDto> detailList) {
		this.detailList = detailList;
	}

}
