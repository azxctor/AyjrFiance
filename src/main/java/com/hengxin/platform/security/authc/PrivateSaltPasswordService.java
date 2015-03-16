
/*
* Project Name: kmfex-platform-trunk
* File Name: PrivateSaltPasswordService.java
* Class Name: PrivateSaltPasswordService
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
	
package com.hengxin.platform.security.authc;

import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.HashService;
import org.apache.shiro.crypto.hash.format.HashFormat;
import org.apache.shiro.crypto.hash.format.HexFormat;
import org.apache.shiro.util.SimpleByteSource;


/**
 * Class Name: PrivateSaltPasswordService
 * Description: TODO
 * @author zhengqingye
 *
 */
public class PrivateSaltPasswordService extends DefaultPasswordService {
    
    private static final String PRIVATE_SALT = "Kmfex_Pri_Salt_//@";
    
    private static final int LEGACY_MD5_LEN = 32;
    
    private HashService md5HashService;
    
    private HashFormat hexHashFormat;
    
    private HashService defaultHashService;
    
    private HashFormat defaultHashFormat;
    
    public PrivateSaltPasswordService(){
        super();
        HashService hashService = getHashService();
        if(hashService instanceof DefaultHashService){
            ((DefaultHashService)hashService).setPrivateSalt(new SimpleByteSource(PRIVATE_SALT));
        }
        DefaultHashService md5HashService=new DefaultHashService();
        md5HashService.setHashAlgorithmName("MD5");
        this.md5HashService = md5HashService;
        hexHashFormat = new HexFormat();
        this.defaultHashService =  super.getHashService();
        defaultHashFormat = super.getHashFormat();
    }
    
    @Override
    public boolean passwordsMatch(Object submittedPlaintext, String saved) {
        if(saved.length()==LEGACY_MD5_LEN){//MD5
            super.setHashService(md5HashService);
            super.setHashFormat(hexHashFormat);
            boolean match= super.passwordsMatch(submittedPlaintext, saved);
            restoreHashStrategy();
            if(match){
                throw new LegacyPasswordMatchException("Legacy md5 format password matched.");
            }
            return match;
        }else if (saved.length()>LEGACY_MD5_LEN){//shiro format
            return  super.passwordsMatch(submittedPlaintext, saved);
        }else{//plain text
            String plainText = "";
            if(submittedPlaintext instanceof char[]  ){
                plainText = new String((char[])submittedPlaintext);
            }else if(submittedPlaintext instanceof String ){
                plainText = (String)submittedPlaintext;
            }else{
                throw new IllegalArgumentException("Not supported parameter type for submittedPlaintext: "+submittedPlaintext.getClass().getName() );
            }
            boolean match =  plainText.equals(saved);
            if(match){
                throw new LegacyPasswordMatchException("Legacy plaintext format password matched.");
            }
        }
        return false;
    }
    
    private void restoreHashStrategy(){
        super.setHashService(defaultHashService);
        super.setHashFormat(defaultHashFormat);
    }

    /**
     * @param hashInterations Set hashInterations value
     */
    public void setHashIterations(int hashIterations) {
        DefaultHashService defaultHashService = (DefaultHashService)super.getHashService();
        defaultHashService.setHashIterations(hashIterations);
    }
    
}
