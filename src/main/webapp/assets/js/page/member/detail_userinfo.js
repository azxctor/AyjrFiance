require(
		['jquery',
         'global',
         'module/util',
         'module/form',
         'module/ajax',
         'module/cascading_listener',
         'bootstrap',
         'requirejs/domready!'],
		function($, global, util,form) {
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
					except :["user_type", "gender", "legal_gender"]
			};
			/**
			 * ----------事件---------
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
			
			/*地区下拉框事件*/
			$("#person-province").listenChange({
				url:global.context+'/web/option/region/',
				nextElement:$("#person-city"),
				bindingElement:$("#person-region"),
				initialCallback:function(current,next){
					var $current_form = next.parents("form");
					next.val( $current_form.data("cache")["person_city"]).trigger("change");
				}
			});
			$("#person-city").listenChange({
				url:global.context+'/web/option/region/',
				nextElement:$("#person-county"),
				bindingElement:$("#person-region"),
				initialCallback:function(current,next){
					var $current_form = next.parents("form");
					next.val( $current_form.data("cache")["person_county"]).trigger("change");
				}
			});
			$("#org-province").listenChange({
				url:global.context+'/web/option/region/',
				nextElement:$("#org-city"),
				bindingElement:$("#org-region"),
				initialCallback:function(current,next){
					var $current_form = next.parents("form");
					next.val( $current_form.data("cache")["org_city"]).trigger("change");
				}
			});
			$("#org-city").listenChange({
				url:global.context+'/web/option/region/',
				nextElement:$("#org-county"),
				bindingElement:$("#org-region"),
				initialCallback:function(current,next){
					var $current_form = next.parents("form");
					next.val( $current_form.data("cache")["org_county"]).trigger("change");
				}
			});
			
			$("#org-bank-province").listenChange({
				url:global.context+'/web/option/region/',
				nextElement:$("#org-bank-city"),
				hideNext:false,
				initialCallback:function(current,next) {
					var $current_form = next.parents("form");
					next.val( $current_form.data("cache")["bank_city"].code).trigger("change");
				}
			});
			$("#person-bank-province").listenChange({
				url:global.context+'/web/option/region/',
				nextElement:$("#person-bank-city"),
				hideNext:false,
				initialCallback:function(current,next){
					var $current_form = next.parents("form");
					next.val( $current_form.data("cache")["bank_city"].code).trigger("change");
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
			
			/*查看原表单*/
			$("#show-pre-info").on("click",function(){
				$("#form-person,#form-org").each(function(){
					if($(this).is(":visible")){
						$(this).addClass("flag-prev").hide();
					}
				});
				$("#pre-info-form").show("slide"); 
			});
			
			$(".go-back").on("click",function(){
				$("#pre-info-form").hide();
				$("#form-person,#form-org").each(function(){
					if($(this).hasClass("flag-prev")){
						$(this).show("slide");
					}
				});
			});
			
			/* 保存提交按钮事件 */
			$(".form-btn-submit").on("click",function(e){
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
					$(".select2-focusser").attr("name","nature");
					$("#s2id_autogen1_search").addClass("masked-ignore");
					option.action=null;
				}
			});
			
			/* 表单验证 */
			$('#form-basic').validate({
				ignore: ".masked-ignore",
				submitHandler : function(form) {
					/*
					 * submit the original value of the masked fileds
					 * */
					var original_data = $("#hidden_original_data").text() || "{}";
					var basic_option = $.extend({},option,{dataExtension:{masked_data:$.parseJSON(original_data)}});
					util.ajax_submit(form,basic_option).done(function(resp){
						if(resp.code == "ACK"){
							$(".form-btn-edit").removeAttr("disabled");
							var html='<div class="alert alert-warning">'+
									'<ul class="fa-ul">'+
										'<li>'+
										'<i class="fa fa-info-circle fa-lg"> </i>'+
										'信息审核中！'+
										'</li>'+
									'</ul>'+
								'</div>';
							$(form).find(".info-area").html(html);
							$(form).addClass("view-mode");
							var url1 = $(form).attr("data-url");
							util.render_form("#form-basic",{
								url:url1,
								cache:false
							});
						}
					});
					
					
				}
			});
			
			
			$('#form-financier').validate({
				submitHandler : function(form) {
					util.ajax_submit(form,option).done(function(resp){
						if(resp.code == "ACK"){
							var html = '<div class="alert alert-warning">'+
								'<ul class="fa-ul">'+
									'<li>'+
									'<i class="fa fa-info-circle fa-lg"> </i>'+
									'信息审核中！'+
									'</li>'+
								'</ul>'+
								'</div>';
							$(form).find(".info-area").html(html);
							$(form).addClass("view-mode");
							var url1 = $(form).attr("data-url");
							util.render_form("#form-financier",{
								url:url1,
								cache:false
							});
						}
				});
					
				}
			});
			
			$('#form-investor').validate({
				submitHandler : function(form) {
					util.ajax_submit(form,option).done(function(resp){
						if(resp.code == "ACK"){
							var html = '<div class="alert alert-warning">'+
								'<ul class="fa-ul">'+
									'<li>'+
									'<i class="fa fa-info-circle fa-lg"> </i>'+
									'信息审核中！'+
									'</li>'+
								'</ul>'+
								'</div>';
							$(form).find(".info-area").html(html);
							$(form).addClass("view-mode");
							var url1 = $(form).attr("data-url");
							util.render_form("#form-investor",{
								url:url1,
								cache:false
							});
						}
					});
					
				}
			});
			
			/*表单重置*/
			/*form.init({
				onReset:function(currentForm){
					util.render_form(currentForm,{
						url:currentForm.attr("data-url"),
						cache:true
					});
				}
			});*/
			
			$("body").on("click",".form-btn-edit",function(){
				var currentForm=$(this).attr("data-target");
				$(currentForm).removeClass("view-mode");
				$(this).parents(".row-button").find(".form-btn-edit").hide()
										.end().find(".form-btn-submit").show();
				$(currentForm).find("select").each(function(){
					$(this).removeAttr("data-inited").trigger("change");
				});
				
			});
			
			/**
			 * ----------初始化-----------
			 */
			
			/*初始化表单*/
			/*$("form").each(function(){
				util.render_form(this,{
					url:$(this).attr("data-url"),
					cache:true
				});
			});*/
			
			/*触发编辑状态级联操作*/
			if(!$("form:visible").hasClass("view-mode")){
				$("select").each(function(){
					$(this).removeAttr("data-inited").trigger("change");
				});
			}
			
});


