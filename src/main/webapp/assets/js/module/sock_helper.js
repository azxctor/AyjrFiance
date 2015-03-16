/*
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * SockJs辅助工具
 *
 * @Enming Chen
 */

;define(["jquery",'sockjs'],
    function($){

        var SockHelper = function(setting){
            this.url = setting.url;
            this.connection = setting.connection;
            this.errorCallback = setting.errorCallback;
            this.rawSetting = setting.rawSetting||{protocols_whitelist:['xdr-streaming', 'xhr-streaming', 'iframe-eventsource', 'iframe-htmlfile', 'xdr-polling', 'xhr-polling', 'iframe-xhr-polling', 'jsonp-polling']};
        };
        SockHelper.prototype = function(){
            var _socket;
            var _stompClient;
            return{
                init:function(){
                    _socket = new SockJS(this.url,null,this.rawSetting);
                    _stompClient = Stomp.over(_socket);
                    if(!this.debug){
                        _stompClient.debug = false;
                    }
                    return _stompClient;
                },
                connect:function(){
                    _stompClient.connect({}, this.connection, this.errorCallback);
                }
            }
        }();

        return SockHelper;

    });