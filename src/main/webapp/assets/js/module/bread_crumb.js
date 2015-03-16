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
 * The breadcrumb nav module for the main frame.
 *
 * Author: Tao Shi
 */
;define(['jquery',
        'requirejs/domready!'], 
function($){
	
	var MFBread = function(){
		this._curmbs = [];
		this._dom = null;
		this.available=false;
	};
	
	MFBread.setting = {
		container: '.right-blk-head',
	};
	
	MFBread.prototype = {
		
		init: function(){
			var $this = this;
			if($(MFBread.setting.container).length == 0){
				console.warn('_parent dom is not prepared before rendering the crumb item');
				return;
			}
			this.available=true;
			this._dom = $('<ol class="breadcrumb"></ol>')
				.appendTo(MFBread.setting.container)
				.on('click', 'li', function(e){
					var obj = $(this).data('crumb-obj');
					if(obj && !obj.last()){
						$this.dom().trigger('mf.bread.click', [{
							index: $this.index(this),
							data : obj.data()
						}]);
						return obj.onclick(e);
					}
				});
			return this;
		},
		
		dom: function(){
			return this._dom;
		},
			
		push: function(data, pre){
			var last;
			if(data){
				if(this._curmbs.length > 0){
					last = this._curmbs[this._curmbs.length -1];
					last.last(false);
					last.uptach();
					if(pre && $.isFunction(pre)){
						last.data().click = pre;
					}
				}
				var c = new CrumbItem(data, this, true);
				this._curmbs.push(c.attach());
			}
			return this;
		},
		
		pop: function(i){
			var last;
			if(!i || i < 1){
				i = 1;
			}
			while(i > 0){
				if(this._curmbs.length > 0){
					last = this._curmbs[this._curmbs.length -1];
					last.detach();
					this._curmbs.pop();
				}
				i--;
			}
			if(this._curmbs.length > 0){
				last = this._curmbs[this._curmbs.length -1];
				last.last(true);
				last.uptach();
			}
			return this;
		},
		
		clear: function(){
			if(this._dom){
				this._dom.empty();
				this._curmbs = [];
			}
			return this;
		},
		
		index: function(elem){
			var obj = $(elem).data('crumb-obj');
			var _index=-1;
			for(var i=0,length=this._curmbs.length;i<length;++i){
				if(obj==this._curmbs[i]){
					_index=i;
					break;
				}
			}
			return _index;
		},
		
		size: function(){
			return this._curmbs.length;
		}
	};
	
	var CrumbItem = function(data, parent, last){
		this._dom = null;
		this._html = null;
		this._last = last;
		this.data(data);
		this.parent(parent);
	};
	
	CrumbItem.prototype = {
		
		constructor: CrumbItem,
		
		parent: function(p){
			if(p && this._parent != p){
				this._parent = p;
			}else if(p == undefined){
				return this._parent;
			}
		},
		
		data: function(d){
			if(d && this._data != d){
				this._data = d;
				this.render();
			}else if(d == undefined){
				return this._data;
			}
		},
	
		dom: function(){
			return this._dom || $('<li></li>');
		},
		
		last: function(v){
			if(v != undefined){
				this._last = v;
				this.render();
			}else{
				return this._last;
			}
		},
		
		render: function(){
			// create the html on the fly
			var d = this._data;
			this._html = $('<li></li>');		
			if(d){
				if(!this._last){
					$('<a href="#"></a>').appendTo(this._html)
						.attr('href', d.link || 'javascript:void(0);')
						.text(d.label || '');
				}else{
					this._html.text(d.label || '');
				}
			}
			return this;
		},
		
		attach: function(){
			if(!this.parent() || !this.parent().dom()){
				console.warn('_parent dom is not prepared before rendering the crumb item');
				return;
			}
			if(!this._html){
				this.render();
			}
			// attach the html into dom
			if(!this._dom){
				this._dom = this._html
					.clone().appendTo(this.parent().dom())
					.data('crumb-obj', this);
			}
			return this;
		},
		
		detach: function(){
			// detach the html from the dom
			if(this._dom){
				this._dom.remove();
				delete this._dom;
			}
			return this;
		},
		
		uptach: function(){
			this.detach();
			this.render();
			return this.attach();
		},
		
		onclick: function(e){
			var f = this._data && this._data.click;
			f && f(e);
		}
	
	};
	
	return new MFBread().init();
	
});