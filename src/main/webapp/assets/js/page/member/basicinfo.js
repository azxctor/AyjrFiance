require(
		['jquery',
         'global',
         'module/util',
         'form',
         'module/ajax',
         'module/cascading_listener',
         'page/member/basicinfo_common',
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
			
			/*监听基本信息域展开闭合事件*/
			$(".toggle-baseinfo").on("click",function(){
				if($(".container-baseinfo").is(":visible")){
					$(this).find(".fa").switchClass("fa-angle-down","fa-angle-up");
					$(".container-baseinfo").slideUp("fast");
				}
				else{
					$(this).find(".fa").switchClass("fa-angle-up","fa-angle-down");
					$(".container-baseinfo").slideDown("fast");
				}
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
			$("#person-city").listenChange({
				url:global.context+'/web/option/region/',
				nextElement:$("#person-county"),
				bindingElement:$("#person-region"),
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
			$("#org-bank-province").listenChange({
				url:global.context+'/web/option/region/',
				nextElement:$("#org-bank-city"),
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
			$("#person-bank-province").listenChange({
				url:global.context+'/web/option/region/',
				nextElement:$("#person-bank-city"),
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
				$("#basic-info-full").hide();
				$("#pre-info-form").show("slide");
			});
			
			$(".go-back").on("click",function(){
				$("#pre-info-form").hide();
				$("#basic-info-full").show("slide");
			});
			
			/* 保存提交按钮事件 */
			$(".form-btn-save,.form-btn-submit").on("click",function(e){
				var target=$(e.currentTarget);
				if(target.attr("disabled")){
					return;
				}
				$(".select2-focusser").attr("name","nature");
				if(target.hasClass("form-btn-save")){
					var $currentForm=target.parents("form");
					option.action=$currentForm.attr("data-save-action");
					util.ajax_submit($currentForm,option);
					return false;
				}
				if(target.hasClass("form-btn-submit")){
					option.action=null;
				}
			});
			
			/* 表单验证 */
			$('#form-person').validate({
				submitHandler : function(form) {
					util.ajax_submit(form,option).always(function(){
						
					});
				}
			});
			
			$('#form-org').validate({
				submitHandler : function(form) {
					util.ajax_submit(form,option);
				}
			});
			
			/*表单重置*/
			form.init({
				onReset:function(currentForm){
					util.render_form(currentForm, renderFormOption);
				}
			});
			
			/**
			 * ----------初始化-----------
			 */
			/*只显示个人信息或机构信息*/
			if($("#user-type").val().toUpperCase()=="PERSON"){
				$("#form-person").show();
				$("#form-org").hide();
			}
			else{
				$("#form-person").hide();
				$("#form-org").show();
			}
			
			/*初始化表单*/
			util.render_form("#form-person,#form-org,#form-user-type", renderFormOption);
			
			if($('#pre-info-form').length>0){
				util.render_form("#pre-info-form", renderFormOption);
			}
			/*触发编辑状态级联操作*/
			if(!$("form:visible").hasClass("view-mode")){
				$("select").each(function(){
					$(this).removeAttr("data-inited").trigger("change");
				});
			}
			
			$(".basic-info-tip").popoverx({
				ensure_visiable : true,
				fire_on: 'hover',
				placement : 'top',
				//	width: iwidth,
				//	height: iheight,
				elementToAppend:  $(".right-blk-body").length==0? $(".blk-body"):  $(".right-blk-body"),
				onShown: function(){
					var html = "由于新、老系统切换，银行卡开户地区可能不准确，若您的银行卡开户地区不对，请拨打客服热线400-999-7327或是官网QQ进行修改。";
					this.$tip.find('.popover-content').html(html);
					this.resetPosition();
				}
			});
			
			
			util.bind_popoverx();
			
			/**
			 * 
			 **/
			$("#id_card_back_img .upload-filename").text("上传“手持身份证照”");
});


