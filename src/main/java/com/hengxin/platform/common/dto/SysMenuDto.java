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

package com.hengxin.platform.common.dto;

import java.io.Serializable;
import java.util.List;

import com.hengxin.platform.common.domain.SysMenu;

/**
 * Class Name: SysMenuDto
 * 
 * @author yingchangwang
 * 
 */

public class SysMenuDto implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<SysMenu> menu;
    private String firstLevelHighlight;
    private String secondLevelHighlight;

    public List<SysMenu> getMenu() {
        return menu;
    }

    /**
     * @param menu
     */
    public void setMenu(List<SysMenu> menu) {
        this.menu = menu;
    }

    public String getFirstLevelHighlight() {
        return firstLevelHighlight;
    }

    /**
     * @param firstLevelHighlight
     */
    public void setFirstLevelHighlight(String firstLevelHighlight) {
        this.firstLevelHighlight = firstLevelHighlight;
    }

    public String getSecondLevelHighlight() {
        return secondLevelHighlight;
    }

    /**
     * @param secondLevelHighlight
     */
    public void setSecondLevelHighlight(String secondLevelHighlight) {
        this.secondLevelHighlight = secondLevelHighlight;
    }

}
