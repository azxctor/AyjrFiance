require(
		['jquery',
         'global',
         'module/util',
         'form',
         'module/ajax',
         'jquery.pnotify', 
         'bootstrap',
         'page/member/basicinfo_common',
         'requirejs/domready!'],
		function($, global, util,form) {
			
			/**
			 * -------变量--------
			 */
			var option={action:null,context:null};
			
			/**
			 * --------事件--------
			 */
			/*提交重置按钮事件*/
			$(".form-btn-save,.form-btn-submit").on("click",function(e){
				var target=$(e.currentTarget);
				if(target.attr("disabled")){
					return;
				}
				if(target.hasClass("form-btn-save")){
					var $currentForm=$('#investorinfo-form');
					option.action=$currentForm.attr("data-save-action");
					util.ajax_submit($currentForm,option);
					return false;
				}
				if(target.hasClass("form-btn-submit")){
					option.action=null;
				}
			});
			/*表单验证事件*/
			$('#investorinfo-form').validate({
				submitHandler : function(form) {
					util.ajax_submit(form,option);
				}
			});
			/*表单重置事件*/
			form.init({
				onReset:function(currentForm){
					util.render_form(currentForm, {
						formDataSelector:"#investor-json-string"
					});
				}
			});
			/*基本信息打开闭合事件*/
			$("#toggle-basicinfo").on("click",function(){
				var $span = $(this).find("span");
				if($span.hasClass("fa-angle-up")){
					$span.removeClass("fa-angle-up").addClass("fa-angle-down");
				}else{
					$span.removeClass("fa-angle-down").addClass("fa-angle-up");
				}
				$(".invest_toggle_slide").slideToggle();
			});
			/*查看原表单事件*/
			$("#show-pre-info").on("click",function(){
				$("#invest-info-full").hide();
				$("#pre-info-form").show("slide");
			});
			
			$(".go-back").on("click",function(){
				$("#pre-info-form").hide();
				$("#invest-info-full").show("slide");
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
			
			/**
			 * -------初始化--------
			 */
			util.render_form("#investorinfo-form", {
				formDataSelector:"#investor-json-string"
			});
			
			util.render_form("#basicinfo-form", {
				formDataSelector: "#basic-json-string"
			});
			
			if($('#pre-info-form').length>0){
				util.render_form("#pre-info-form", {
					formDataSelector: "#basic-json-string"
				});
				util.render_form("#pre-info-form", {
					formDataSelector: "#investor-json-string"
				});
			}
			
			/*触发授权服务中心改变事件*/
			$(".auth_center").trigger("change");
			
			util.bind_popoverx();
			
			
		$("#open-account-agg").on("click",function(){
			if($(this).is(":checked")){
				$("#invest-info-submit").removeAttr("disabled");
			}else{
				$("#invest-info-submit").attr("disabled","disabled");
			}
		});
		
		
		
		$("#aggre-pro-btn").on("click",function(){
			if($("#open-account-agg").attr("disabled") != "disabled"){
				$("#open-account-agg").prop("checked",true);
				$("#invest-info-submit").removeAttr("disabled");
			}
			$("#onlineProModal").modal("hide");
		});
		
		$(".auth_center").select2({
			 placeholder: "请输入授权服务中心的区域或名称 ",
			 allowClear: true,
			formatNoMatches: function () { return "未找到匹配项"; }
		});
		//fix conflict between validate and select2
		$(".select2-focusser").attr("name","xx");
});
