/*
 * Project Name: kmfex-platform
 * File Name: CommonMenuDto.java
 * Class Name: CommonMenuDto
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

package com.hengxin.platform.common.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Class Name: CommonMenuDto Description: TODO
 * 
 * @author yingchangwang
 * 
 */

public class CommonMenuDto implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
    private String code;
    private String label;
    private String icon;
    private String link;
    private String parentLink;
    List<CommonMenuDto> subItems;
    
    
    public CommonMenuDto(){
        
    }
    
    public CommonMenuDto(String id,String code,String label,String icon,String link,String parentLink){
        this.id=id;
        this.code=code;
        this.label=label;
        this.icon=icon;
        this.link=link;
        this.parentLink=parentLink;
    }
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
    
    public String getParentLink() {
        return parentLink;
    }

    public void setParentLink(String parentLink) {
        this.parentLink = parentLink;
    }

    public List<CommonMenuDto> getSubItems() {
        return subItems;
    }

    public void setSubItems(List<CommonMenuDto> subItems) {
        this.subItems = subItems;
    }

}
