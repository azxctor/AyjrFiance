define(['jquery','global','select2','numeral'], function($,global){
	
	var Util = function(){
		this.prefixRendered=false;
	};
	var submitOptions={
		context:null,//提交表单域 
		action:null,//提交url
		dataExtension:null,//额外数据
		mode:""
	};
	
	var renderOptions={
		context:null,//渲染表单域
		url:null,//提交url
	//	dataExtension:null,//额外数据
		formDataSelector: null
	};
	
	Util.prototype = {
		
		stringify_aoData : function (aoData) {
		    var o = {};
		    var modifiers = ['mDataProp_', 
                 'sSearch_', 
                 'iSortCol_', 
                 'bSortable_', 
                 'bRegex_', 
                 'bSearchable_', 
                 'sSortDir_'];
		    $.each(aoData, function(idx,obj) {
		        if (obj.name) {
		            for (var i=0; i < modifiers.length; i++) {
		                if (obj.name.substring(0, modifiers[i].length) == modifiers[i]) {
		                    var index = parseInt(obj.name.substring(modifiers[i].length));
		                    var key = 'a' + modifiers[i].substring(0, modifiers[i].length-1);
		                    if (!o[key]) {
		                        o[key] = [];
		                    }
		                    //console.log('index=' + index);
		                    o[key][index] = obj.value;
		                    //console.log(key + ".push(" + obj.value + ")");
		                    return;
		                }
		            }
		            //console.log(obj.name+"=" + obj.value);
		            o[obj.name] = obj.value;
		        }
		        else {
		            o[idx] = obj;
		        }
		    });
		    return JSON.stringify(o);
		},
		
		set_menu: function(item){
			$('.navbar-fixed-top .navbar-nav li').removeClass('active');
			$('.navbar-fixed-top .navbar-nav li').each(function(i){
				var menuItem = $(this).attr('data-menu-item');
				if(menuItem == item){
					$(this).addClass('active');
				}
			});
		},
		
		ajax_submit: function(form,option){
			option=option||submitOptions;
			var rootElement=option.context?("#"+$(form).attr("id")+" "+option.context):form;
			var formId=option.context?($(form).attr("id")+" "+option.context):$(form).attr("id");
			var result ={};
			
			if(option.context1&&option.context1==".flat"){
				var rootElement1=option.context1?("#"+$(form).attr("id")+" "+option.context1):form;
				result = this._clone( this._submit_data(rootElement1,option));
			}
			if(option.mode && option.mode=="arrayMode"){
				var firstRootElement=option.list?("#"+$(form).attr("id")+" "+option.list):form;
				formId = $(form).attr('id');
				for(var i=1;i<=$(firstRootElement).length;i++){
					var listAttr="";
					var arr = [];
					var secondRootElement = option.context?("#"+$(form).attr("id")+" "+option.list+"-"+i+" "+option.context):form;
					for(var j=0;j<$(secondRootElement).length;j++){
						var temp = secondRootElement;
						temp = temp + ":eq("+j+")";
						var data =this._submit_data(temp,option);
						arr.push(this._clone(data));
						listAttr=temp;
					}
					result[$(listAttr).attr("data-name")]=arr;
				}
				
				return $.ajax({
					url: option.action||$(form).attr('action'),
					type: $(form).attr('method'),
					dataType: 'json',
					headers: {
						'x-form-id' : formId
					},
					contentType: 'application/json; charset=UTF-8',
					data: JSON.stringify(result)
					
				});
			}
			var data = this._submit_data(rootElement,option);
			//ajax提交
			return $.ajax({
				url: option.action||$(form).attr('action'),
				type: $(form).attr('method'),
				dataType: 'json',
				headers: {
					'x-form-id' : formId
				},
				contentType: 'application/json; charset=UTF-8',
				data: JSON.stringify(data)
			});
		},
		
		
		_submit_data: function(rootElement,option){
			 var rootElement = rootElement;
				var o = {};
				var key;
				var tokens;
				var t;
				var c;
			  var option = option;
			$(rootElement).find('input,textarea,select,.file-upload,.data-binder').each(function(){
				if($(this).attr("data-ignore")=="true"||$(this).parents("[data-ignore=true]").length>0){
					return true;
				}
				if($(this).attr("type")=="file"){
					return true;
				}
				if($(this).hasClass("select2-focusser select2-offscreen") || $(this).hasClass("select2-input")){
					return true;
				}
				key = $(this).attr('name');
				if(key){
					tokens = key.split('\.');//[org,person,name]l
					c = o;
					for(var i=0; i<tokens.length; i++){
						t = tokens[i]; //org,person,name
						if(!c.hasOwnProperty(t)){ 
							c[t] = {}; //c.org={} c.person={} c.name={}
						}				//o.org={}
						
						if(i == tokens.length -1){
							if($(this).hasClass("file-upload")){
								c[t] = $(this).attr("data-img");
							}
							else if($(this).hasClass("data-binder")){
								c[t] = $(this).attr("data-val");
							}
							else if($(this).attr("type")=="checkbox"){
								c[t] = $(this).is(":checked");
							}
							else if($(this).attr("type") == "radio")
							{
								if($(this).is(":checked"))
								{	
									c[t] = $(this).val();
								}
							}else if(this.tagName == "SELECT" && $(this).attr("multiple")){
								c[t] = $(this).select2("val");
							}else{
								c[t] = $(this).hasClass("select2-hidden")
								?($(this).select2("data")==null
										?""
										:$(this).select2("data").id)
								:$(this).val();//c.name="xxx";o={org:{person:{name:"xxx"}}}
							}
						}else{
							c = c[t]; //o={org:{}},o={org:{person:{}}}
						}
					}
				}
			});
			//加入额外数据
			if(option.dataExtension&&typeof option.dataExtension==="object"){
				for(var prop in option.dataExtension){
					if(prop == "masked_data"){
						var masked_data = option.dataExtension[prop];
						for(var mask in masked_data){
							var ligten_data = o[mask.slice(0,mask.indexOf("_original"))];
							if(ligten_data && ligten_data.indexOf("*") != -1){
								o[mask.slice(0,mask.indexOf("_original"))] = masked_data[mask];
							}
						}
					}else{
						o[prop]=option.dataExtension[prop];
					}
					
				}
			}
			return o;
		},
		
		_render_data: function(root,data,option){
            var me = this;
			$(root).find("input,select,.hidden-form-control,.file-upload,.show-form-control,textarea").each(function(){
				var $this = $(this);
				var name = $this.attr("name");
				if(name)
				{
					var s = name.split(".");
					var temp = data;
					for(var i=0;i<s.length;i++)
					{
						if(temp) temp = temp[s[i]];
					}
					
					if(temp){
						if($(this).attr("type") == "radio" || $(this).attr("type") == "checkbox"){
								$(this).prop("checked",temp);
						}else if($(this).hasClass("select2-hidden")){
							$(this).select2("val", temp);
						}
						else if($(this).hasClass("file-upload")){
							$(this).attr("data-img",temp);
							$(this).find(".file-binder").val(temp);
							$(this).find(".upload-filename").text(temp);
						}
						else if(this.tagName == "SPAN")
						{
							$this.text(temp).attr("title",temp);
						}else if(this.tagName == "IMG"){
                             if(me.is_pdf(temp)) {
                                $this.attr("src",global.context+"/assets/img/pdf.jpg");
                                $this.attr("data-pdfpath",temp);
                                $(this).parent().find(".file-upload .upload-img").attr("src",global.context+"/assets/img/pdf.jpg");
                                $(this).parent().find(".file-upload .upload-img").attr("data-pdfpath",temp);
                            }else if(me.is_doc(temp)) {
                                $this.attr("src",global.context+"/assets/img/doc.jpg");
                                $this.attr("data-pdfpath",temp);
                                $(this).parent().find(".file-upload .upload-img").attr("src",global.context+"/assets/img/doc.jpg");
                                $(this).parent().find(".file-upload .upload-img").attr("data-pdfpath",temp);
                            }
                             else{
                                $this.attr("src",temp);
                                $this.removeAttr("data-pdfpath");
                                $(this).parent().find(".file-upload .upload-img").attr("src",temp);
                                $(this).parent().find(".file-upload .upload-img").removeAttr("data-pdfpath");
                            }
                            $(this).parent().find(".file-upload .upload-img").parent(".upload-thumbnail").show();





             /*               $(this).parent().find(".file-upload .upload-img").parent(".upload-thumbnail").show();*/
						}else{
							var except = option.except || [];
							var flat = false;
							for(var i=0;i<except.length;i++)
							{
								if(option.except[i] == $this.attr("name"))
								{
									flat = true;
								}
							}
							if(flat){
								var name = temp.name || "";
								$this.val(name);
							}else{
								$this.val(temp);
							}
						}
					}
				}
				
			});
		},
		
		render_form : function(form,option){
			var that = this; 
			this.render_prefix(form,option);
			option=option||renderOptions;
			var rootElement=option.context?("#"+$(form).attr("id")+" "+option.context):form;
			var formDataSelector = option.formDataSelector;
			var formData=$(rootElement).data("cache");
			if(option.cache && formData){
				this._render_data(rootElement,formData,option);
				return;
			}
			if(formDataSelector)
			{
				var jsontext = $.trim($(formDataSelector).text()).length==0?"{}":$(formDataSelector).text();
				formData = $.parseJSON(jsontext);
				this._render_data(rootElement,formData,option);
				return;
			}
			return $.ajax({
				url: option.url,
				type: "GET",
				dataType: 'json',
				contentType: 'application/json; charset=UTF-8',
			}).done(function(resp){
				$(rootElement).data("cache",resp);
				that._render_data(rootElement,resp,option);
			});
		},
		
		render_prefix:function(form,option){
			if(this.prefixRendered){
				return;
			}
			option=option||renderOptions;
			var rootElement=option.context?("#"+$(form).attr("id")+" "+option.context):"form";
			$(rootElement).find("input,select,.hidden-form-control,.file-upload,.data-binder,.show-form-control,textarea").each(function(){
				if($(this).hasClass("upload-input")){
					return;
				}
				var currentName=$(this).attr("name")?$(this).attr("name"):"";
				var parentName=$(this).parents(".hasPrefix").length>0?$(this).parents(".hasPrefix").eq(0).attr("data-prefix"):"";
				$(this).attr("name",parentName+currentName);
			});
			this.prefixRendered=true;
		},
		
		redirect: function(url) {
            // Similar behavior as an HTTP redirect
            window.location.replace(url);
        },
        
        getSearchData:function(containerId){
        	var result = [];
	        $(containerId).find('input,textarea,select').each(function(){
				var o = {};
				var key;
				if($(this).attr("data-ignore")=="true"){
					return true;
				}
				if($(this).hasClass("select2-focusser select2-offscreen") || $(this).hasClass("select2-input")){
					return true;
				}
				key = $(this).attr('name');
				if(key){		
					if($(this).attr("Type")=="checkbox"){
						o["name"] = key;
						if($(this).val()=="true"){
							o["value"]=true;
						}else{
							o["value"]=false;
						}
					}
					else if($(this).attr("Type")=="radio"){
						if($(this).is(":checked")){
							o["name"] = key;
							o["value"]= $(this).val();
						 } else {
							 return;
						 }
					}else{
						o["name"] = key;
						o["value"]=$(this).val();
					}
					result.push(o);
				}
			});
		return result;
        },
        
        _clone: function(myObj){
        	 if(typeof(myObj) != 'object') return myObj;
   		  if(myObj == null) return myObj;
   		  
   		  var myNewObj = new Object();
   		  
   		  for(var i in myObj)
   			myNewObj[i] = this._clone(myObj[i]);
   		  
   		  return myNewObj;
        },
        
        select2_init: function(){
        	var enlist = [
		{text:"内资企业",children:[
		       	            {id:190,text:"其他企业"},
		       	     		{text:"私营企业",children:[
		       	     			{id:174,text:"私营股份有限公司"},
		       	     			{id:173,text:"私营有限责任公司"},
		       	     			{id:172,text:"私营合伙企业"},
		       	     			{id:171,text:"私营独资企业"}
		       	     			
		       	     		]},
		       	     		{id:160,text:"股份有限公司"},
		       	     		{text:"有限责任公司",children:[
		       	     		     {id:159,text:"其他有限责任公司"},                     	
		       	     		     {id:151,text:"国有独资公司"}                	
		       	     		]},
		       	     		{text:"联营企业",children:[
		       	     		     {id:149,text:"其他联营企业"},
		       	     		     {id:143,text:"国有与集体联营企业"},
		       	     		     {id:142,text:"集体联营企业"},
		       	     		     {id:141,text:"国有联营企业"},
		       	     		]},
		       	     		{id:130,text:"股份合作企业"},
		       	     		{id:120,text:"集体企业"},
		       	     		{id:110,text:"国有企业"}
		       	     	]},
		       	     	{text:"港、澳、台商投资企业",children:[
		       	     	    {id:290,text:"其他港、澳、台商投资企业"},   
		       	     	    {id:240,text:"港、澳、台商投资股份有限公司"},   
		       	     	    {id:230,text:"港、澳、台商独资经营企业"},
		       	     	    {id:220,text:"合作经营企业（港或澳、台资）"},
		       	     	    {id:210,text:"合资经营企业（港或澳、台资）"}, 
		       	     	]},
		       	     	{text:"外商投资企业",children:[
		       	     	    {id:330,text:"外资企业"},
		       	     	    {id:320,text:"中外合作经营企业"},
		       	     	    {id:310,text:"中外合资经营企业"}
		       	     	]},
		       	     	{id:400,text:"个人"},
		       	     	{id:500,text:"国有"},
		       	     	{id:600,text:"集体"},
		       	     	{id:700,text:"有限责任公司"},
		       	     	{id:800,text:"股份有限公司"},
		       	     	{id:900,text:"外商独资"},
		       	     	{id:1000,text:"中外合资"},
		       	     	{id:9999,text:"其他"},
        		     	];
        			$("#org-type").select2({
        				width:"100%",
        				data:enlist,
        				formatNoMatches: function () { return "未找到匹配项"; }
        			});
        },
        
        MillisecondToDate: function(msd) {
    		var time = parseFloat(msd) / 1000;
    		if (null != time && "" != time) {
    			if (time > 60 && time < 60 * 60) {
    				time = parseInt(time / 60.0) + "分" + parseInt((parseFloat(time / 60.0) -
    						parseInt(time / 60.0)) * 60) + "秒";
    			}
    			else if (time >= 60 * 60 && time < 60 * 60 * 24) {
    				time = parseInt(time / 3600.0) + "时" + parseInt((parseFloat(time / 3600.0) -
    						parseInt(time / 3600.0)) * 60) + "分" +
    						parseInt((parseFloat((parseFloat(time / 3600.0) - parseInt(time / 3600.0)) * 60) -
    								parseInt((parseFloat(time / 3600.0) - parseInt(time / 3600.0)) * 60)) * 60) + "秒";
    			}
    			else {
    				time = parseInt(time) + "秒";
    			}
    		}
    		return time;
    	},
    	_decimal_floor: function(digit, length) {  
    	    length = length ? parseInt(length) : 0;  
    	    if (length <= 0) return Math.floor(digit);  
    	    digit = Math.floor(digit * Math.pow(10, length)) / Math.pow(10, length);  
    	    return digit;  
    	},
    	
    	get_thousand_floor: function(num,length){
    		 if(!length){
    			 length = 2;
    		 };
    		 num = this._decimal_floor(num,length);
    		 return numeral(num).format('0,0.00');
    	},
    	
        get_thousand:function(num){
           return numeral(num).format('0,0.00');
        },
        get_aprate:function(num){
        	var data = parseFloat(num);
        	return !/\./.test(data) == true? data += ".0":data;
        },
        get_percent:function(num){
            return numeral(num).format('0.0')+"%";
        },
        get_trans_percent:function(num){
        	if(num.indexOf(".")!=-1){
        		return num+"%";
        	}else{
        		return num+".0%";
        	}
        },
        get_twoScale:function(num){
        	return numeral(num).format('0.00');
        },
        
        is_pdf:function(path){
            var type = path.substr(path.length-3).toLowerCase();
            if(type=="pdf") return true;
            else return false;
        },  
        is_doc:function(path){
        	var array=path.split(".");
            return array.length>0&&(array[array.length-1]=="doc"||array[array.length-1]=="docx");
        },
        is_excel:function(path){
        	var array=path.split(".");
            return array.length>0&&(array[array.length-1]=="xls"||array[array.length-1]=="xlsx");
        },
        
        get_search_data:function(form_selector){
        	var data = {};
        	$(form_selector).find("input,select").each(function(){
        		var $this = $(this);
        		if($this.attr("data-beignored") == "true"){
        			return;
        		}
        		var name = $this.attr("name");
        		var val = $this.val();
        		if( !data.hasOwnProperty(name) || $.trim(val).length>0){
        			data[name] = val;
        		}
 
        	});
        	return data;
    	},

    	bind_popoverx:function(selector,width,height){
    		var iselector = selector || ".image-example-popoverx";
    		var iwidth = width || 204;
    		var iheight = height || 151;
    		$(iselector).popoverx({
    			ensure_visiable : true,
    			trigger: 'hover',
    			placement : 'top',
    			width: iwidth,
    			height: iheight,
    			elementToAppend:  $(".right-blk-body").length==0? $(".blk-body"):  $(".right-blk-body"),
    			onShown: function(){
    				var src = $(this.element).attr("data-src");
    				var html = '<img src="'+src+'" width="100%";></img>';
    				this.$tip.find('.popover-content').html(html);
    				this.resetPosition();
    			}
    		});
    	}
	};
	
	return new Util();
});