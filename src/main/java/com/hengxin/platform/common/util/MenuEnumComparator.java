/*
 * Project Name: kmfex-platform
 * File Name: ComparatorEnum.java
 * Class Name: ComparatorEnum
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

import com.hengxin.platform.common.domain.SysMenu;
import com.hengxin.platform.common.enums.EMenuConstant;

/**
 * Class Name: ComparatorEnum Description: 菜单排序
 * 
 * @author yingchangwang
 * 
 */

public class MenuEnumComparator implements Comparator<Object> {

    @Override
    public int compare(Object objOne, Object objTwo) {
     EMenuConstant menuOne =   ((SysMenu) objOne).getMenu();
     EMenuConstant menuTwo =   ((SysMenu)objTwo) .getMenu();
        Integer firstCode = Integer.valueOf(menuOne.getCode());
        Integer secondCode = Integer.valueOf(menuTwo.getCode());
        return firstCode.compareTo(secondCode);
    }

}
