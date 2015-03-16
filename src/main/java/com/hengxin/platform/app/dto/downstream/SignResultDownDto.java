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
package com.hengxin.platform.app.dto.downstream;

import java.io.Serializable;


/**
 * SignResultDownDto: 登录结果下行DTO.
 * 
 * @author yicai
 *
 */
public class SignResultDownDto implements Serializable{
	private static final long serialVersionUID = 5L;
	
	private Boolean isInvestor;
	private Boolean isFinacier;
	
	public SignResultDownDto(Boolean isInvestor, Boolean isFinacier) {
		this.isFinacier = isFinacier;
		this.isInvestor = isInvestor;
	}
	
	public Boolean getIsInvestor() {
		return isInvestor;
	}
	public void setIsInvestor(Boolean isInvestor) {
		this.isInvestor = isInvestor;
	}
	public Boolean getIsFinacier() {
		return isFinacier;
	}
	public void setIsFinacier(Boolean isFinacier) {
		this.isFinacier = isFinacier;
	}
}
