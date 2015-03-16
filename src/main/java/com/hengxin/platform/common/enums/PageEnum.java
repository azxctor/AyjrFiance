
/*
 * Project Name: kmfex-platform
 * File Name: BaseEnum.java
 * Class Name: BaseEnum
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
	
package com.hengxin.platform.common.enums;


/**
 * Class Name: BaseEnum
 * Description: TODO
 * @author rczhan
 *
 */

public interface PageEnum extends DBEnum {

    String getText();

    void setText(String text);

    String name();

}