require(
		['jquery',
         'global',
         'module/util',
         'form',
         'module/ajax',
         'bootstrap',
         'page/member/basicinfo_common',
         'requirejs/domready!'],
		function($, global, util,form) {
			/**
			 * --------变量--------
			 */
			var option={action:null,context:null};
			
			/**
			 * --------事件--------
			 */
			/*表单提交事件*/
			$(".form-btn-save,.form-btn-submit").on("click",function(e){
				var target=$(e.currentTarget);
				if(target.attr("disabled")){
					return;
				}
				if(target.hasClass("form-btn-save")){
					var $currentForm=$('#financierinfo-form');
					option.action=$currentForm.attr("data-save-action");
					util.ajax_submit($currentForm,option);
					return false;
				}
				if(target.hasClass("form-btn-submit")){
					option.action=null;
				}
			});
			/*表单验证事件*/
			$('#financierinfo-form').validate({
				submitHandler : function(currentForm) {
					util.ajax_submit(currentForm,option);
				}
			});
			/*表单重置事件*/
			form.init({
				onReset:function(currentForm){
					util.render_form(currentForm, {
						formDataSelector :"#financier-json-string"
					});
				}
			});
			/*查看原始表单事件*/
			$("#show-pre-info").on("click",function(){
				$("#financier-info-full").hide();
				$("#pre-info-form").show("slide");
			});
			
			$(".go-back").on("click",function(){
				$("#pre-info-form").hide();
				$("#financier-info-full").show("slide");
			});
			/*基本信息展开闭合事件*/
			$("#toggle-basicinfo").on("click",function(){
				var $span = $(this).find("span");
				if($span.hasClass("fa-angle-up")){
					$span.removeClass("fa-angle-up").addClass("fa-angle-down");
				}else{
					$span.removeClass("fa-angle-down").addClass("fa-angle-up");
				}
				$(".invest_toggle_slide").slideToggle();
				
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
			 * --------初始化--------
			 */
			util.render_form("#financierinfo-form", {
				formDataSelector :"#financier-json-string"
			});
			
			util.render_form("#basicinfo-form", {
				formDataSelector :"#basic-json-string"
			});
			
			if($('#pre-info-form').length>0){
				util.render_form("#pre-info-form", {
					formDataSelector :"#basic-json-string"
				});
				util.render_form("#pre-info-form", {
					formDataSelector :"#financier-json-string"
				});
			}
			
			$("#open-account-agg").on("click",function(){
				if($(this).is(":checked")){
					$("#financier-info-submit").removeAttr("disabled");
				}else{
					$("#financier-info-submit").attr("disabled","disabled");
				}
			});
			
			$("#aggre-pro-btn").on("click",function(){
				if($("#open-account-agg").attr("disabled") != "disabled"){
					$("#open-account-agg").prop("checked",true);
					$("#financier-info-submit").removeAttr("disabled");
				}
				$("#financierProModal").modal("hide");
			});
			
			/*触发授权服务中心改变事件*/
			$(".auth_center").trigger("change");
			
			util.bind_popoverx();
});

