require(['jquery',
         'global',
         'module/util',
         'module/form',
         'module/ajax',
         'bootstrap',
         'bootstrap-datepicker',
         'module/cascading_listener',
         'jquery.validate.methods',
         'page/member/basicinfo_common',
         'requirejs/domready!'], 
function($, global, util, form){
	// Add jQuery validate to sign-in form
	
	/**
	 * --------变量--------
	 */
	var option={
			url:null,
			action:null,
			context:null
	};
	var renderFormOption={
			formDataSelector:"#json-string",
			except :["user_type", "gender", "legal_gender"]
	};
	/**
	 * --------事件--------
	 */
	$(".form-btn-save,.form-btn-submit").on("click",function(e){
		var target=$(e.currentTarget);
		if(target.attr("disabled")){
			return;
		}
		$(".select2-focusser").attr("name","common.nature.code");
		if(target.hasClass("form-btn-save")){
			var $currentForm=$('#agency-form');
			option.action=$('#agency-form').attr("data-save-action");
			util.ajax_submit($currentForm,option);
			return false;
		}
		if(target.hasClass("form-btn-submit")){
			option.action=null;
		}
	});
	
	$('#agency-form').validate({
		submitHandler: function(form) {
			util.ajax_submit('#agency-form',option);
	    }
	});
	
	/*身份证号变更事件*/
	$("#legal-id-card").on("change",function(){
		var value = $.trim($(this).val());
		value = value.substr(0,value.length-1)+value.substr(-1).toUpperCase();
		$(this).val(value);
	});
	
	/*地区下拉框事件*/
	$("#org-province").listenChange({
		url:global.context+'/web/option/region/',
		nextElement:$("#org-city"),
		bindingElement:$("#org-region"),
		initialCallback:function(current,next){
			var nextElementName=next.attr("name");
			var json=$.parseJSON($("#json-string").text()==""?"{}":$("#json-string").text());
			var temp=nextElementName.split(".");
			for(var i=0;i<temp.length;++i){
				if(json.hasOwnProperty(temp[i])&&json[temp[i]]){
					json=json[temp[i]];
				}
				else{
					break;
				}
			}
			next.val(json).trigger("change");
		}
	});
	$("#org-city").listenChange({
		url:global.context+'/web/option/region/',
		nextElement:$("#org-county"),
		bindingElement:$("#org-region"),
		initialCallback:function(current,next){
			var nextElementName=next.attr("name");
			var json=$.parseJSON($("#json-string").text()==""?"{}":$("#json-string").text());
			var temp=nextElementName.split(".");
			for(var i=0;i<temp.length;++i){
				if(json.hasOwnProperty(temp[i])&&json[temp[i]]){
					json=json[temp[i]];
				}
				else{
					break;
				}
			}
			next.val(json).trigger("change");
		}
	});
	$("#basic-province").listenChange({
		url:global.context+'/web/option/region/',
		nextElement:$("#basic-city"),
		bindingElement:$("#basic-region"),
		initialCallback:function(current,next){
			var nextElementName=next.attr("name");
			var json=$.parseJSON($("#json-string").text()==""?"{}":$("#json-string").text());
			var temp=nextElementName.split(".");
			for(var i=0;i<temp.length;++i){
				if(json.hasOwnProperty(temp[i])&&json[temp[i]]){
					json=json[temp[i]];
				}
				else{
					break;
				}
			}
			next.val(json).trigger("change");
		}
	});
	$("#basic-city").listenChange({
		url:global.context+'/web/option/region/',
		nextElement:$("#basic-county"),
		bindingElement:$("#basic-region"),
		initialCallback:function(current,next){
			var nextElementName=next.attr("name");
			var json=$.parseJSON($("#json-string").text()==""?"{}":$("#json-string").text());
			var temp=nextElementName.split(".");
			for(var i=0;i<temp.length;++i){
				if(json.hasOwnProperty(temp[i])&&json[temp[i]]){
					json=json[temp[i]];
				}
				else{
					break;
				}
			}
			next.val(json).trigger("change");
		}
	});

	$("#bank-province").listenChange({
		url:global.context+'/web/option/region/',
		nextElement:$("#bank-city"),
		hideNext:false,
		initialCallback:function(current,next){
			var nextElementName=next.attr("name");
			var json=$.parseJSON($("#json-string").text()==""?"{}":$("#json-string").text());
			var temp=nextElementName.split(".");
			for(var i=0;i<temp.length;++i){
				if(json.hasOwnProperty(temp[i])&&json[temp[i]]){
					json=json[temp[i]];
				}
				else{
					break;
				}
			}
			next.val(json).trigger("change");
		}
	});

	$("#org_type").on("change",function(){
		if($(this).val() == "AUTHZDCENTER"){
			$("#agency_service").show();
			$("#agency_provide").hide();
		}else if(($(this).val() == "PRODUCTSERVICE")){
			$("#agency_service").hide();
			$("#agency_provide").show();
		}else{
			$("#agency_service").hide();
			$("#agency_provide").hide();
		}
		$(window).trigger("resize");
	});
	/*表单重置*/
	form.init({
		onReset:function(currentForm){
			util.render_form(currentForm, renderFormOption);
		}
	});
	
	/**
	 * --------初始化--------
	 */
	util.render_form("#agency-form", renderFormOption);
	/*触发编辑状态在级联操作*/
	if(!$("#agency-form").hasClass("view-mode")){
		$("select,input").each(function(){
			if(this.type=="file"){
				return;
			}
			$(this).removeAttr("data-inited").trigger("change");
		});
	}
	
	util.bind_popoverx();
});
