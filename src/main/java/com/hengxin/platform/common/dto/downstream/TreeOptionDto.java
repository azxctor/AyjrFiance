/*
 * Project Name: kmfex-platform
 * File Name: TreeOptionDto.java
 * Class Name: TreeOptionDto
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

package com.hengxin.platform.common.dto.downstream;

import java.io.Serializable;
import java.util.List;

/**
 * Class Name: TreeOptionDto
 * <p>
 * Description: 用于树型下拉控件显示选项
 * 
 * @author runchen
 * 
 */

public class TreeOptionDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

    private String text;

    private List<TreeOptionDto> children;

    public TreeOptionDto() {

    }

    public TreeOptionDto(String id, String text) {
        this.id = id;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<TreeOptionDto> getChildren() {
        return children;
    }

    public void setChildren(List<TreeOptionDto> children) {
        this.children = children;
    }

}
