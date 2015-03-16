
/*
* Project Name: kmfex-platform-trunk
* File Name: XssSantizeJsonSerializer.java
* Class Name: XssSantizeJsonSerializer
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
	
package com.hengxin.platform.security.xss;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.std.SerializerBase;
import org.owasp.encoder.Encode;

/**
 * Class Name: XssSantizeJsonSerializer
 * Description: Sanitize String type fields in object for json serialization.   
 * @author zhengqingye
 *
 */
public class XssSantizeJsonSerializer extends SerializerBase<String> {

    public XssSantizeJsonSerializer(){
        this(String.class);
    }
    
    protected XssSantizeJsonSerializer(Class<String> t) {
        super(t);
    }

    @Override
    public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
            JsonProcessingException {
        if(value!=null){
            String encoded = Encode.forHtml(value);
            jgen.writeString(encoded);
        }
        
    }

}
