/*!
 * jQuery cascading Plugin 1.0.1
 *
 * Copyright 2014 haoxianglan
 * Released under the MIT license:
 *   http://www.opensource.org/licenses/mit-license.php
 */
(function($){
	//consturctor
	var CascadingListener=function(element,options){
		this.options=$.extend(this.defaultOptions,options);
		this.currentElement=element;
		
		this.init();
	};
	$.extend(CascadingListener,{
		defaultOtions:{
			url:null,//required
			nextElement:null,//required
			bindingElement:null,//optional
			initialCallback:null,//optional
			successFn:null,//optional
			hideNext:true,//optional
			bindData:null//optional
		},
		prototype:{
			_bindData:function(context,data){
				$.each(data,function(index,item){
					context.append($("<option>").val(item.code).text(item.text));
				});
			},
			_changeHandler:function(){
				
				var that=this,
					options=this.options,
					$currentElement=$(this.currentElement),
					$nextDropDown=options.nextElement?$(options.nextElement):null,
					$bindingElement=options.bindingElement?$(options.bindingElement):null,
					func_bindData=options.bindData||this._bindData,
					url=options.url+$currentElement.val();
				
				if(options.hideNext){
					$nextDropDown.hide();
				}
				$nextDropDown.empty().trigger("change");
				if(!$currentElement.val()){
					if($bindingElement){
						var parentVal=this.$parent?this.$parent.val():"";
			    		 if($bindingElement.is("input")){
			    			 $bindingElement.val(parentVal);
			    		 }
			    		 else{
			    			 $bindingElement.attr("data-val",parentVal);
			    		 }
			    	}
					return;
				}
				var cache=$currentElement.data("cache")||{};
				if(cache&&cache.hasOwnProperty(url)){
					var json=cache[url];
					if(json.length>1){
						$nextDropDown.show();
					}
			    	func_bindData($nextDropDown,json);
			    	if(!$currentElement.attr("data-inited")&&options.initialCallback){
			    		 options.initialCallback($currentElement,$nextDropDown);
			    		 $currentElement.attr("data-inited",true);
			    	 }
			    	if($bindingElement){
			    		 if($bindingElement.is("input")){
			    			 $bindingElement.val($currentElement.val());
			    		 }
			    		 else{
			    			 $bindingElement.attr("data-val",$currentElement.val());
			    		 }
			    	}
			    	return;
				}
				$.ajax({
				     type: "get",
				     dataType:'json', 
				     cache:false,
				     url: url,
				     success: options.successFn||function(data){
				    	 var json=eval(data);
				    	 cache[url]=json;
				    	 $currentElement.data("cache",cache);
				    	 if(json.length>1){
				    		 $nextDropDown.show();
				    	 }
				    	 func_bindData($nextDropDown,json);
				    	 if(!$currentElement.attr("data-inited")&&options.initialCallback){
				    		 options.initialCallback($currentElement,$nextDropDown);
				    		 $currentElement.attr("data-inited",true);
				    	 }
				    
				    	 if($bindingElement){
				    		 if($bindingElement.is("input")){
				    			 $bindingElement.val($currentElement.val());
				    		 }
				    		 else{
				    			 $bindingElement.attr("data-val",$currentElement.val());
				    		 }
				    	 }
				     }
				});
			},
			init:function(){
				var context=this;
				$(this.currentElement).on("change",function(){
					context._changeHandler();
				});
				
				$(this.options.nextElement).data("parent",$(this.currentElement));
				this.$parent=$(this.currentElement).data("parent");
			},
			destory:function(){
				$(this.currentElement).off("change").removeData().removeAttr("data-inited");
				return this;
			}
		}
	});
	
	$.extend($.fn,{
		
		listenChange:function(options){
			if ( !this.length ) {
				if ( options && options.debug && window.console ) {
					console.warn( "Nothing selected, returning nothing." );
				}
				return;
			}
			else if(options&&options.url&&options.nextElement){
				var listner=$.data(this[0],"change_listener");
				if(!listner){
					listner=new CascadingListener(this[0],options);
					$.data(this[0],"change_listener",listner);
				}
				return listner;
			}
			else{
				if ( options && options.debug && window.console ) {
					console.warn( "options url and nextElement are required." );
				}
			}
		}
	});
})(jQuery);