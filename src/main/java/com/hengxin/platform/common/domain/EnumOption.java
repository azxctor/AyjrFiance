
/*
 * Project Name: kmfex-platform
 * File Name: EnumOption.java
 * Class Name: EnumOption
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
	
package com.hengxin.platform.common.domain;

import java.io.Serializable;

/**
 * Class Name: EnumOption Description: TODO
 * 
 * @author rczhan
 * 
 */

@SuppressWarnings("serial")
public class EnumOption implements Serializable {

    private String code;
    
    private String text;

    public EnumOption(String code, String text) {
        this.code = code;
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    
    @Override
    public String toString() {
        return code;
    }

}
