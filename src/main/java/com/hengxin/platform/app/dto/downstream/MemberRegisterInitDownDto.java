/*
 * Project Name: kmfex-platform
 * File Name: MemberRegisterInitDownDto.java
 * Class Name: MemberRegisterInitDownDto
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
 * Class Name: InvestorDownDto
 * 
 * 这是手机端代注册，授权服务中心及相关信息下行DTO
 * 
 * refer to InvestorDto
 * 
 * @author yicai
 *
 */
public class MemberRegisterInitDownDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 是否通过代理注册
    private Boolean registerByAgentScenario;
	
    // 是否可编辑授权服务中心和经办人
    private Boolean fixedAgency;

    // 授权服务中心描述(上边两个值，可不用，直接用这个值)
    private String desc;

	/**
	 * @return the registerByAgentScenario
	 */
	public Boolean getRegisterByAgentScenario() {
		return registerByAgentScenario;
	}

	/**
	 * @param registerByAgentScenario the registerByAgentScenario to set
	 */
	public void setRegisterByAgentScenario(Boolean registerByAgentScenario) {
		this.registerByAgentScenario = registerByAgentScenario;
	}

	/**
	 * @return the fixedAgency
	 */
	public Boolean getFixedAgency() {
		return fixedAgency;
	}

	/**
	 * @param fixedAgency the fixedAgency to set
	 */
	public void setFixedAgency(Boolean fixedAgency) {
		this.fixedAgency = fixedAgency;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
