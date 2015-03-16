
/*
* Project Name: kmfex-platform
* File Name: NumberTool.java
* Class Name: NumberTool
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
	
package com.hengxin.platform.common.util.velocity;

import java.math.BigDecimal;

import com.hengxin.platform.common.util.NumberUtil;

/**
 * Class Name: NumberTool
 * Description: TODO
 * @author rczhan
 *
 */

public class NumberTool extends org.apache.velocity.tools.generic.NumberTool {

    public String formatMoney(BigDecimal value) {
        return NumberUtil.formatMoney(value, true);
    }

    public String formatMoneyWithoutDecimal(BigDecimal value) {
        return NumberUtil.formatMoney(value, false);
    }

    public String formatLargeMoney(BigDecimal value) {
        return NumberUtil.formatLargeMoney(value);
    }

    public String formatToThousands(BigDecimal value) {
        return NumberUtil.formatToThousands(value);
    }

}
