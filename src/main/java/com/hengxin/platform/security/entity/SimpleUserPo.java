/*
 * Project Name: kmfex-platform
 * File Name: UserPo.java
 * Class Name: UserPo
 *
 * Copyright 2014 KMFEX Inc
 *
 * 
 *
 * http://www.kmfex.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hengxin.platform.security.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.hengxin.platform.fund.entity.AcctPo;

/**
 * To facilitate joins between entities that needs to go through USER entity.
 * <p>
 * DO NOT use this for any other purpose.
 * 
 * 
 * @author yeliangjin
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "UM_USER")
public class SimpleUserPo implements Serializable {

	@Id
	@Column(name = "USER_ID")
	private String username;

	@Column(name = "NAME")
	private String name;

	@OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
	private AcctPo account;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public AcctPo getAccount() {
		return account;
	}

	public void setAccount(AcctPo account) {
		this.account = account;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
