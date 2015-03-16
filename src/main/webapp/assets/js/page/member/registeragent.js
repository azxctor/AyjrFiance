require(['jquery',
         'global',
         'module/util',
         'form',
         'module/cascading_listener',
         'module/ajax',
         'bootstrap',
         'jquery.pnotify', 
         'page/member/basicinfo_common',
         'requirejs/domready!'], 
function($, global, util,form){

	// Add jQuery validate to sign-in form
	/**
	 * --------变量---------
	 */
	var option={
			url:null,
			action:null,
			context:null
	};
	var renderFormOption={
			formDataSelector:"#json-string",
			except :["type", "member.gender", "org.legal_gender"]
	};
	
	/**
	 * --------事件---------
	 */

	/*监听用户类型切换事件*/
	$("#user-type").on("change",function(){
		if($(this).val().toUpperCase()=="PERSON"){
			$("#form-person").show();
			$("#form-org").hide();
		}
		else{
			$("#form-person").hide();
			$("#form-org").show();
		}
		$(window).trigger("resize");
	});
	
	/* 保存提交按钮事件 */
	$(".form-btn-save,.form-btn-submit").on("click",function(e){
		var target=$(e.currentTarget);
		if(target.attr("disabled")){
			return;
		}
		if(target.hasClass("form-btn-save")){
			var $currentForm=target.parents("form");
			option.action=$currentForm.attr("data-save-action");
			util.ajax_submit($currentForm,option);
		}
		if(target.hasClass("form-btn-submit")){
			$(".select2-focusser").attr("name","org.nature");
			option.action=null;
		}
	});
	
	/*投融资信息勾选操作*/
	$(".toggle-slide").click(function(){
		var target = $(this).attr("data-target");
		$(this).parents("form").find(target).slideToggle().attr("data-ignore",!($(this).is(":checked")));
	});
	
	/*地区下拉框事件*/
	$("#person-province").listenChange({
		url:global.context+'/web/option/region/',
		nextElement:$("#person-city"),
		bindingElement:$("#person-region"),
		initialCallback:function(current,next){
			var nextElementName=next.attr("name");
			var json=$.parseJSON($("#json-string").text()==""?"{}":$("#json-string").text());
			var temp=nextElementName.split(".");
			for(var i=0;i<temp.length;++i){
				if(json.hasOwnProperty(temp[i])){
					json=json[temp[i]];
				}
				else{
					break;
				}
			}
			next.val(json).trigger("change");
		}
	});
	$("#person-city").listenChange({
		url:global.context+'/web/option/region/',
		nextElement:$("#person-county"),
		bindingElement:$("#person-region"),
		initialCallback:function(current,next){
			var nextElementName=next.attr("name");
			var json=$.parseJSON($("#json-string").text()==""?"{}":$("#json-string").text());
			var temp=nextElementName.split(".");
			for(var i=0;i<temp.length;++i){
				if(json.hasOwnProperty(temp[i])){
					json=json[temp[i]];
				}
				else{
					break;
				}
			}
			next.val(json).trigger("change");
		}
	});
	$("#org-province").listenChange({
		url:global.context+'/web/option/region/',
		nextElement:$("#org-city"),
		bindingElement:$("#org-region"),
		initialCallback:function(current,next){
			var nextElementName=next.attr("name");
			var json=$.parseJSON($("#json-string").text()==""?"{}":$("#json-string").text());
			var temp=nextElementName.split(".");
			for(var i=0;i<temp.length;++i){
				if(json.hasOwnProperty(temp[i])){
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
				if(json.hasOwnProperty(temp[i])){
					json=json[temp[i]];
				}
				else{
					break;
				}
			}
			next.val(json).trigger("change");
		}
	});
	/*地区下拉框事件*/
	$("#person-bank-province").listenChange({
		url:global.context+'/web/option/region/',
		nextElement:$("#person-bank-city"),

		initialCallback:function(current,next){
			var nextElementName=next.attr("name");
			var json=$.parseJSON($("#json-string").text()==""?"{}":$("#json-string").text());
			var temp=nextElementName.split(".");
			for(var i=0;i<temp.length;++i){
				if(json.hasOwnProperty(temp[i])){
					json=json[temp[i]];
				}
				else{
					break;
				}
			}
			next.val(json).trigger("change");
		}
	});
	$("#org-bank-province").listenChange({
		url:global.context+'/web/option/region/',
		nextElement:$("#org-bank-city"),
		
		initialCallback:function(current,next){
			var nextElementName=next.attr("name");
			var json=$.parseJSON($("#json-string").text()==""?"{}":$("#json-string").text());
			var temp=nextElementName.split(".");
			for(var i=0;i<temp.length;++i){
				if(json.hasOwnProperty(temp[i])){
					json=json[temp[i]];
				}
				else{
					break;
				}
			}
			next.val(json).trigger("change");
		}
	});
	
	/*身份证变更事件*/
	$("#id-card-number").on("change",function(){
		var value = $.trim($(this).val());
		value = value.substr(0,value.length-1)+value.substr(-1).toUpperCase();
		$(this).val(value);   

		if($(this).val().length==15){
			var yearOfBirth=parseInt("19"+$(this).val().substr(6,2));
			var birthday=yearOfBirth+"-"+$(this).val().substr(8,2);
			$("#birthday").val(birthday);
			$("#age").val(new Date().getFullYear()-yearOfBirth);
		}
		else if($(this).val().length==18){
			var yearOfBirth=parseInt($(this).val().substr(6,4));
			var birthday=yearOfBirth+"-"+$(this).val().substr(10,2);
			$("#birthday").val(birthday);
			$("#age").val(new Date().getFullYear()-yearOfBirth);
		}
		else{
			$("#birthday").val("");
			$("#age").val("");
		}
	});
	$("#legal-id-card").on("change",function(){
		var value = $.trim($(this).val());
		value = value.substr(0,value.length-1)+value.substr(-1).toUpperCase();
		$(this).val(value);

		if($(this).val().length==15){
			var yearOfBirth=parseInt("19"+$(this).val().substr(6,2));
			var birthday=yearOfBirth+"-"+$(this).val().substr(8,2);
			$("#legal-birthday").val(birthday);
			$("#legal-age").val(new Date().getFullYear()-yearOfBirth);
		}
		else if($(this).val().length==18){
			var yearOfBirth=parseInt($(this).val().substr(6,4));
			var birthday=yearOfBirth+"-"+$(this).val().substr(10,2);
			$("#legal-birthday").val(birthday);
			$("#legal-age").val(new Date().getFullYear()-yearOfBirth);
		}
		else{
			$("#legal-birthday").val("");
			$("#legal-age").val("");
		}
	});
	
	/*授权服务中心改变事件*/
	$(".auth_center").on("change",function(){
		if(!$(this).is(":disabled")){
			if($(this).val()){
				$(this).parents(".row").find(".agent").removeAttr("disabled");
			}
			else{
				$(this).parents(".row").find(".agent").attr("disabled",true).val("");
			}
		}
		
	});
	util.bind_popoverx(".image-example-popoverx");
	
	/*表单验证*/
	$('#form-person').validate({
		submitHandler: function(form) {
			if($(form).find(".toggle-slide:checked").length == 0){
				$.pnotify({
				    text: "请选择投资或融资信息 !",
				    type: 'error'
				});
				return;
			}
			util.ajax_submit(form,option);
	    }
	});
	$('#form-org').validate({
		submitHandler: function(form) {
			if($(form).find(".toggle-slide:checked").length == 0){
				$.pnotify({
				    text: "请选择投资或融资信息 !",
				    type: 'error'
				});
				return;
			}
			util.ajax_submit(form,option);
	    }
	});
	/*表单重置事件*/
	form.init({
		onReset:function(currentForm){
			util.render_form(currentForm,renderFormOption);
		}
	});
	/**
	 * --------初始化--------
	 */
	/*只显示个人信息或机构信息*/
	if($("#user-type").val().toUpperCase()=="PERSON"){
		$("#form-person").show();
		$("#form-org").hide();
		util.render_form("#form-person",renderFormOption);
	}
	else{
		$("#form-person").hide();
		$("#form-org").show();
		util.render_form("#form-org",renderFormOption);
	}
	/*触发编辑状态级联操作*/
	if(!$("form:visible").hasClass("view-mode")){
		$("select").each(function(){
			$(this).removeAttr("data-inited").trigger("change");
		});
	}
	/**
	 * 
	 **/
	$("#id_card_back_img .upload-filename").text("上传“手持身份证照”");
	$('.image-example-popoverx').attr('data-src','/ayjr/assets/img/IDCard_s.jpg');
	
	
});
