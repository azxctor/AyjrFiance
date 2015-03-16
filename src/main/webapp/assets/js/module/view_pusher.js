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
 * push a view and add a bread crumb to bread crum nav.
 *
 * Author: Haoxiang Lan
 */
define(['jquery','module/bread_crumb','jquery.ui'],function($,mfBread){
	var pusher=function(){
		this.defaults=pusher.defaults;
		this.elementStack=[];
	};
	$.extend(pusher,{
		defaults:{
			url:"",//返回页面的controller
			data:{},//获取页面ajax方法传的参数
			method:"get",//获取页面的方法
			title:"请设置标题",//对应在面包屑上的标题
			element:null,//需要隐藏的元素
			onShow:function(){},//显示视图后的callback
			beforeHide:function(){return true;},//隐藏视图前的callback
			onHide:function(){},//隐藏push视图的callback
			animate:"drop"
		},
		prototype:{
			_init:function(element){
				var that=this,$element=element;
				if($(".pusher-wrapper").length==0){
					$("<div></div>")
					.addClass("pusher-wrapper")
					.hide()
					.insertBefore($element)
					//.append("<div class='col-sm-12'><button type='button' class='btn btn-mf-primary pusher-btn-back'><i class='fa fa-mail-reply fa-lg'></i></button></div>")
					.append("<div class='pusher-content'></div>");
					/*$(".pusher-wrapper").on("click",".pusher-btn-back,pusher-link",function(e){
						var $el=that._get_$el(that.elementStack[0]);
						var option=$el.data("options");
						var beforeHideCallback=option.beforeHide;
						if(beforeHideCallback.call(that)==false){
							return false;
						}
						that.pop();
					});*/
					$(".pusher-wrapper").data("pusher",that);
				}
			},
			push:function(ops){
				var options=$.extend({},this.defaults,ops);
				var $element=$(options.element);
				$element.data("options",options);
				var that=this;
				that._init($element);
				if(this._ajaxTimer){
					that._ajaxTimer.abort();
					that._ajaxTimer=null;
				}
				this._ajaxTimer=$.ajax({
					type : options.method,
					contentType:'application/json;charset=utf-8',
					url:options.url,
					data:options.data
				}).done(function(resp){
					$element.hide();
					var guid="pusher-view-stack"+new Date().getTime();
					$(".pusher-wrapper .pusher-content").append("<div id="+guid+">"+resp+"</div>");
					$element.attr("pusher-view-id","#"+guid);
					that.elementStack.unshift(guid);
					$(".pusher-wrapper").hide().show(options.animate);
					options.onShow.call(that);
					mfBread.push({
						label:options.title
					});
				});
			},
			_get_$el:function(guid){
				return $("*[pusher-view-id=#"+guid+"]");
			},
			popTo:function(index){
				var $el,option;
				var that=this;
				if(that.elementStack.length==0||index==0){
					return true;
				}
				while(index!=mfBread.size()){
					if(that.elementStack.length==0){
						return true;
					}
					$el=that._get_$el(that.elementStack[0]);
					option=$el.data("options");
					var beforeHideCallback=option.beforeHide;
					if(beforeHideCallback.call(that)==false){
						return false;
					}
					that.pop();
				}
			},
			pop:function(){
				var that=this;
				if(that.elementStack.length>0){
					var $element=that._get_$el(that.elementStack[0]);
					$element.show($element.data("options").animate);
					that.elementStack=that.elementStack.slice(1);
					if(that.elementStack.length==0){
						$(".pusher-wrapper").hide();
					}
					$($element.attr("pusher-view-id")).unbind().remove();
					$(".pusher-link[data-target="+$element.attr("pusher-view-id")+"]")
					.removeClass("pusher-link")
					.removeAttr("data-target")
					.nextAll().each(function(){
						$(this).remove();
					});
					
					$element.data("options").onHide.call(that);
					mfBread.pop();
				}
			}
		}
	});
	if($(".pusher-wrapper").length>0){
		return $(".pusher-wrapper").data("pusher");
	}
	else{
		return new pusher();
	}
});