/*
 * Project Name: kmfex-platform
 * File Name: SignResultDownDtoV3.java
 * Class Name: SignResultDownDtoV3
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
 * SignResultDownDto: 三期，登录结果下行DTO.
 * 
 * @author yicai
 *
 */
public class SignResultDownDtoV3 implements Serializable{
	private static final long serialVersionUID = 5L;
	
	private Boolean isProdServ; // 担保机构
	private Boolean isAuthServiceCenter; // 服务中心
	private Boolean isAuthServiceCenterAgent; // 代理服务中心
	private Boolean isAuthServiceCenterGeneral; // 普通服务中心
	
	public SignResultDownDtoV3(Boolean isProdServ, Boolean isAuthServiceCenter,
			Boolean isAuthServiceCenterAgent, Boolean isAuthServiceCenterGeneral) {
		super();
		this.isProdServ = isProdServ;
		this.isAuthServiceCenter = isAuthServiceCenter;
		this.isAuthServiceCenterAgent = isAuthServiceCenterAgent;
		this.isAuthServiceCenterGeneral = isAuthServiceCenterGeneral;
	}
	
	public Boolean getIsProdServ() {
		return isProdServ;
	}
	public void setIsProdServ(Boolean isProdServ) {
		this.isProdServ = isProdServ;
	}
	public Boolean getIsAuthServiceCenter() {
		return isAuthServiceCenter;
	}
	public void setIsAuthServiceCenter(Boolean isAuthServiceCenter) {
		this.isAuthServiceCenter = isAuthServiceCenter;
	}
	public Boolean getIsAuthServiceCenterAgent() {
		return isAuthServiceCenterAgent;
	}
	public void setIsAuthServiceCenterAgent(Boolean isAuthServiceCenterAgent) {
		this.isAuthServiceCenterAgent = isAuthServiceCenterAgent;
	}
	public Boolean getIsAuthServiceCenterGeneral() {
		return isAuthServiceCenterGeneral;
	}
	public void setIsAuthServiceCenterGeneral(Boolean isAuthServiceCenterGeneral) {
		this.isAuthServiceCenterGeneral = isAuthServiceCenterGeneral;
	}
	
}
