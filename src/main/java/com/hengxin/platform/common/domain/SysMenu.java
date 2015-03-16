/*
 * Project Name: kmfex-platform
 * File Name: File.java
 * Class Name: File
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

package com.hengxin.platform.common.domain;

import java.io.Serializable;
import java.util.List;

import com.hengxin.platform.common.enums.EMenuConstant;

/**
 * Class Name: File
 * 
 * @author yingchangwang
 * 
 */
@SuppressWarnings("serial")
public class SysMenu implements Serializable {

    private EMenuConstant menu;
    List<SysMenu> childMenu;

    public SysMenu() {
    }

    public SysMenu(EMenuConstant menu) {
        this.menu = menu;
    }

    public EMenuConstant getMenu() {
        return menu;
    }

    /**
     * 
     * @param menu
     */
    public void setMenu(EMenuConstant menu) {
        this.menu = menu;
    }

    public List<SysMenu> getChildMenu() {
        return childMenu;
    }

    /**
     * @param childMenu
     */
    public void setChildMenu(List<SysMenu> childMenu) {
        this.childMenu = childMenu;
    }

}
