
/*
* Project Name: kmfex-platform-trunk
* File Name: DisplayableError.java
* Class Name: DisplayableError
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
	
package com.hengxin.platform.common.exception;


/**
 * Class Name: DisplayableError
 * Description: TODO
 * @author zhengqingye
 *
 */
public interface DisplayableError {
    String getErrorCode();
    String getDisplayMsg();
    Object[] getArgs();
    boolean isBizError();
}
