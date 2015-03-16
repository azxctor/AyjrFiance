
/*
* Project Name: kmfex-platform
* File Name: JsonDirective.java
* Class Name: JsonDirective
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

import java.io.IOException;
import java.io.Writer;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;
import org.apache.velocity.runtime.parser.node.SimpleNode;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hengxin.platform.common.util.WebUtil;


/**
 * Class Name: JsonDirective
 * Description: TODO
 * @author rczhan
 *
 */

public class JsonDirective extends Directive {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonDirective.class);

    @Override
    public String getName() {
        return "toJson";
    }

    @Override
    public int getType() {
        return LINE;
    }

    @Override
    public boolean render(InternalContextAdapter context, Writer writer, Node node) throws IOException,
            ResourceNotFoundException, ParseErrorException, MethodInvocationException {
        SimpleNode sn_region = (SimpleNode) node.jjtGetChild(0);
        Object object = sn_region.value(context);
        if (object != null) {
            ObjectMapper mapper = WebUtil.getObjectMapper();
            try {
                writer.write(mapper.writeValueAsString(object));
            } catch (JsonGenerationException e) {
                LOGGER.warn("JsonGenerationException from JsonUtil!", e);
            } catch (JsonMappingException e) {
                LOGGER.warn("JsonMappingException from JsonUtil!", e);
            } catch (IOException e) {
                LOGGER.warn("IOException from JsonUtil!", e);
            }
        }
        return true;
    }

}
