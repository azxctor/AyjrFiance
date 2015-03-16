
/*
* Project Name: kmfex-platform-trunk
* File Name: CryptoUtil.java
* Class Name: CryptoUtil
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

import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.CodecSupport;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.util.ByteSource;

/**
 * Class Name: CryptoUtil
 * Description:  use AES as the symmetric crypto algorithm
 * @author zhengqingye
 *
 */
public class CryptoUtil {
    
    public static String encrypt(String plainText, String key){
        AesCipherService aes = new AesCipherService();
        byte[] keyBytes = CodecSupport.toBytes(key);
        ByteSource encryptedBytes = aes.encrypt(CodecSupport.toBytes(plainText), keyBytes);
        return encryptedBytes.toString();
    }
    
    public static String decrypt(String cipherText, String key){
        AesCipherService aesCs = new AesCipherService();
        byte[] keyBytes = CodecSupport.toBytes(key);
        
        ByteSource decryptedBytes  = aesCs.decrypt(Base64.decode(CodecSupport.toBytes(cipherText)), keyBytes);
        return CodecSupport.toString(decryptedBytes.getBytes());
    }

}
