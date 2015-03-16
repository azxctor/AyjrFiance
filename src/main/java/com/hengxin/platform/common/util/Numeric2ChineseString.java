/*
 * Project Name: kmfex-platform
 * File Name: Numeric2ChineseStr.java
 * Class Name: Numeric2ChineseStr
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

/**
 * Class Name: Numeric2ChineseStr.
 * 
 * @author yingchangwang
 * 
 */

public class Numeric2ChineseString {
    private static final String[] UNITS = new String[] { "千", "百", "十", ""};

    private static final char[] NUM_CHARS = new char[] { '一', '二', '三', '四', '五', '六', '七', '八', '九' };

    private static final char NUM_ZERO = '零';

    /**
     * 将一位数字转换为一位中文数字; eg: 1 返回 一;
     * 
     * @param arabNumber
     * @return
     */
    public static char numberCharArab2CN(char arabNumber) {

        if (arabNumber == '0') {
            return NUM_ZERO;
        }

        if (arabNumber > '0' && arabNumber <= '9') {
            return NUM_CHARS[arabNumber - '0' - 1];
        }

        return arabNumber;
    }

    public static String numberArab2ChineseString(int num) {
        char[] numChars = (num + "").toCharArray();

        String tempStr = "";

        int inc = UNITS.length - numChars.length;

        for (int i = 0; i < numChars.length; i++) {
            if (numChars[i] != '0') {
                tempStr += numberCharArab2CN(numChars[i]) + UNITS[i + inc];
            } else {
                tempStr += numberCharArab2CN(numChars[i]);
            }
        }

        tempStr = tempStr.replaceAll(NUM_ZERO + "+", NUM_ZERO + "");

        tempStr = tempStr.replaceAll(NUM_ZERO + "$", "");

        return tempStr;
    }
    
    private Numeric2ChineseString() {
		super();
	}
    
}
