/*
 * Project Name: kmfex-platform
 * File Name: UserConverterService.java
 * Class Name: UserConverterService
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
package com.hengxin.platform.member.converter;

import java.util.Date;

import com.hengxin.platform.common.constant.ApplicationConstant;
import com.hengxin.platform.common.converter.ObjectConverter;
import com.hengxin.platform.security.domain.User;
import com.hengxin.platform.security.entity.UserPo;

/**
 * Class Name: UserConverterService 
 * Description: convert between member domain and member pojo.
 * 
 * @author shengzhou
 * @param <F>
 * 
 */
public class UserConverter implements ObjectConverter<UserPo, User> {

    @Override
    public void convertFromDomain(User domain, UserPo po) {
        Date currentDate = new Date();
        po.setLastMntTs(currentDate);
        if (po.getCreateTs() == null) {
            po.setCreateTs(currentDate);
            po.setStatus(ApplicationConstant.ACTIVE_CODE);
            po.setFailureCount(0L);
        }

    }

    @Override
    public void convertToDomain(UserPo po, User domain) {
    }

}
