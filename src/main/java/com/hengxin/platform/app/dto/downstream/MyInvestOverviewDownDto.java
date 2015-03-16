/*
 * Project Name: kmfex-platform
 * File Name: MyInvestOverviewDownDto.java
 * Class Name: MyInvestOverviewDownDto
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

/**
 * MyInvestOverviewDownDto: 我的债权总览下行DTO.
 * 
 * @author yicai
 *
 */
public class MyInvestOverviewDownDto implements Serializable {

	private static final long serialVersionUID = 5L;
	
	private String totalPrincipal; // 下期预期总收益
	private String totalNextPayAmt; // 申购本金总额
	private String totalRestAmt; // 剩余本息总额
	
	
	public String getTotalPrincipal() {
		return totalPrincipal;
	}
	public void setTotalPrincipal(String totalPrincipal) {
		this.totalPrincipal = totalPrincipal;
	}
	public String getTotalNextPayAmt() {
		return totalNextPayAmt;
	}
	public void setTotalNextPayAmt(String totalNextPayAmt) {
		this.totalNextPayAmt = totalNextPayAmt;
	}
	public String getTotalRestAmt() {
		return totalRestAmt;
	}
	public void setTotalRestAmt(String totalRestAmt) {
		this.totalRestAmt = totalRestAmt;
	}
}
