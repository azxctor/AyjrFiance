/*
 * Project Name: kmfex-platform
 * File Name: ApplicationConverterService.java
 * Class Name: ApplicationConverterService
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

import com.hengxin.platform.common.converter.ObjectConverter;
import com.hengxin.platform.member.domain.BaseApplication;
import com.hengxin.platform.member.dto.BaseApplicationDto;

/**
 * Class Name: ApplicationConverterService Description:
 * 
 * @author runchen
 * @param <F>
 * 
 */
public class ApplicationConverter implements ObjectConverter<BaseApplicationDto, BaseApplication> {

    @Override
    public void convertFromDomain(BaseApplication domain, BaseApplicationDto dto) {

    }

    @Override
    public void convertToDomain(BaseApplicationDto dto, BaseApplication domain) {
        Date now = new Date();
        domain.setLastMntTs(now);
        if (dto.getCreateTs() == null) {
            domain.setCreateTs(now);
        }
    }
}
