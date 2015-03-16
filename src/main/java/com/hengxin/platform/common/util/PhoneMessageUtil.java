/*
 * Project Name: kmfex-platform
 * File Name: PhoneMessageUtil.java
 * Class Name: PhoneMessageUtil
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

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

/**
 * Class Name: PhoneMessageUtil Description: TODO
 * 
 * @author ruifengwang
 * 
 */

public class PhoneMessageUtil {

    public static String formatDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static String arrayToString(String[] ls) {
        String str = StringUtils.EMPTY;
        if (null != ls && ls.length > 0) {
            int size = ls.length;
            for (int i = 0; i < size; i++) {
                if (i == size - 1) {
                    str += ls[i];
                } else {
                    str += ls[i] + ",";
                }
            }
        }
        return str;
    }
    
    public static String getCaptcha(){
        String retStr = "";
        String strTable = "1234567890";
        int len = strTable.length();
        boolean bDone = true;
        do {
            retStr = "";
            int count = 0;
            for (int i = 0; i < 6; i++) {
                double dblR = Math.random() * len;
                int intR = (int) Math.floor(dblR);
                char c = strTable.charAt(intR);
                if (('0' <= c) && (c <= '9')) {
                    count++;
                }
                retStr += strTable.charAt(intR);
            }
            if (count >= 2) {
                bDone = false;
            }
        } while (bDone);

        return retStr;
    }
}
