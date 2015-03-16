/*
 * Project Name: kmfex-platform
 * File Name: CommonMenuComparator.java
 * Class Name: CommonMenuComparator
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

package com.hengxin.platform.common.util;

import java.util.Comparator;

import com.hengxin.platform.common.dto.CommonMenuDto;

/**
 * Class Name: CommonMenuComparator Description: TODO
 * 
 * @author yingchangwang
 * 
 */

public class CommonMenuComparator implements Comparator<Object> {
    @Override
    public int compare(Object objOne, Object objTwo) {
        CommonMenuDto commonMenuDtoOne = (CommonMenuDto) objOne;
        CommonMenuDto commonMenuDtoTwo = (CommonMenuDto) objTwo;

        Integer firstCode = Integer.valueOf(commonMenuDtoOne.getId());
        Integer secondCode = Integer.valueOf(commonMenuDtoTwo.getId());
        return firstCode.compareTo(secondCode);
    }
}
