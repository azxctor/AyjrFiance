/*
 * Project Name: kmfex-platform
 * File Name: User.java
 * Class Name: User
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

package com.hengxin.platform.member.dto.upstream;

import java.io.Serializable;

import com.hengxin.platform.common.dto.annotation.Domain;
import com.hengxin.platform.security.domain.User;

/**
 * Class Name: User
 * 
 * @author shengzhou
 * 
 */
@Domain(types = User.class)
public class UserDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String username;

    private String email;

    private String mobile;

    private String name;


    /**
     * @return return the value of the var username
     */

    public String getUsername() {
        return username;
    }

    /**
     * @param username
     *            Set username value
     */

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return return the value of the var email
     */

    public String getEmail() {
        return email;
    }

    /**
     * @param email
     *            Set email value
     */

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return return the value of the var mobile
     */

    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile
     *            Set mobile value
     */

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    /**
     * @return return the value of the var name
     */

    public String getName() {
        return name;
    }

    /**
     * @param name
     *            Set name value
     */

    public void setName(String name) {
        this.name = name;
    }

}
