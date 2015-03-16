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
 * The left menu module for the main frame.
 *
 * Author: Tao Shi
 */
;define(['jquery',
         'global',
        'module/util',
        'plugins'], 
function($, global, util){
	
	var MFReport = function(iframe){
		this._key = "";
		this._targetFrame = iframe;
		if(!iframe){
			console.error('MFReport should be binded with sepcific iframe');
		};
		var me = this;
		$(this._targetFrame).load(function(){
			me._resetIframeHeight();
		});
		/*$(this._targetForm).find(".btn").on("click",function(){
			me.request();
		})*/
	};
	
	MFReport.prototype = {
	
		key: function(dto){
			var me = this;
			return $.ajax({
				url:dto.url,
				type:"post",
				contentType:"application/json",
				dataType:"json",
				data:JSON.stringify(dto.data)
			}).done(function(resp){
				me._key = resp.parameterString;
			});
		},
		
		_resetIframeHeight : function(){
			var mainheight = $(this._targetFrame).contents().find("body").height()+150;
			$(this._targetFrame).height(mainheight);
		},
		
		request: function(dto){
			var me = this;
			this.key(dto).done(function(){
				$(me._targetFrame).attr('src', me._key);
			});
		}
		
		
	};
	
	return MFReport;
});

