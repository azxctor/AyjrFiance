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
 * iframe event list
 *
 * Author: Chao Tang
 */
define(['jquery',"jquery.popupwindow"],function($){
	var detailEvent = function(){
		
	};
	
	detailEvent.prototype = {
			eventListener:function(url){
				$.popupWindow(url);
			},
			
	};
	
	return detailEvent;
	
});