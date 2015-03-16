require(['jquery',
         'module/util',
         'form',
         'global',
         'bootstrap',
         'plugins',
         'bootstrapx-popoverx',
         'requirejs/domready!'], 
function($, util, form, global){
	/**
	 * 变量
	 */
	var options = {url:null, action:null, context:null, mode:null};
	
	/**
	 * 事件
	 */
	$('#form-auto-apply1').validate({
		submitHandler : function(form) {
			$(form).find(".choose-one-msg").remove();
			if($(form).find(".chk-range:checked").length == 0){
				$(form).find("#aprMessageD1").after("<label class='invalid choose-one-msg'>请至少选择一项<label>")
				return;
			}
			util.ajax_submit(form,options).done(function(resp){
				var result=resp;
				if(result.code=="ACK"){
					$("#modal-success").find("#message").text(result.message);
					$("#modal-success").modal().show();
				}
			});
		}
	});
	
	$('#form-auto-apply2').validate({
		submitHandler : function(form) {
			$(form).find(".choose-one-msg").remove();
			if($(form).find(".chk-range:checked").length == 0){
				$(form).find("#aprMessageD1").after("<label class='invalid choose-one-msg'>请至少选择一项<label>")
				return;
			}
			util.ajax_submit(form,options).done(function(resp){
				var result=resp;
				if(result.code=="ACK"){
					$("#modal-success").find("#message").text(result.message);
					$("#modal-success").modal().show();
				}
			});
		}
	});
	
	$("form.edit-mode input[name=minDateRange]," +
			"form.edit-mode input[name=maxDateRange]," +
			"form.edit-mode input[name=minAPRForDate]").on("change",function(){
				var finished=true;
				$(this).parents("fieldset").find("input").each(function(){
					if($(this).val().trim()=="")
					{
						finished=false;
						return false;
					}
				});
				$(this).parents("form").find("input[name=repaymentD]").each(function(){
					this.checked=finished;
				});
			});
	
	/** .btn-launch 测试用. **/
	$('.btn-submit-params,.btn-relieve,.btn-launch,.btn-agree,.btn-refuse,.btn-confirm-relieve').on('click', function(e){
		var target = $(e.currentTarget);
		
		if (target.hasClass('btn-submit-params')){
			if($("#modal-log").length>0&&!$("#modal-log").data("agree")){
				$("#modal-log").modal().show();
				return false;
			}
			options.action = null;
		}
		if(target.hasClass("btn-refuse")){
			$.pnotify({
			    text: "自动申购参数设置失败!",
			    type: 'error'
			});
			return;
		}
		if(target.hasClass("btn-agree")){
			$("#modal-log").data("agree",true);
			$('.btn-submit-params').click();
			options.action = null;
		}
		if (target.hasClass('btn-relieve')){
			if(target.hasClass("confirmed")){
				options.action = $('form:visible').attr('data-relieve-action');
			}
			else{
				$("#modal-confirm").modal().show();
				return false;
			}
		}
		if (target.hasClass('btn-launch')){
			options.action = $('form:visible').attr('data-launch-action');
		}
		if(target.hasClass("btn-confirm-relieve")){
			$(".btn-relieve").addClass("confirmed").click();
		}
		
	});
	
	$(".chk-range").on("change",function(){
		var $parent=$(this).parents(".thumbnail");
		
		var checked=$(this).is(":checked");
		if(checked){
			$parent.find("input[type=text],select").each(function(){
				$(this).removeAttr("disabled").attr("data-validate",$(this).attr("data-validate-back")).removeAttr("data-validate-back");
			});
			$parent.removeAttr("data-ignore");
		}
		else{
			$parent.find("input[type=text],select").each(function(){
				$(this).attr("disabled",true).attr("data-validate-back",$(this).attr("data-validate")).removeAttr("data-validate").removeClass("invalid");
			});
			$parent.find("label.invalid").remove();
			$parent.attr("data-ignore",true);
		}
	});
	
	/**
	 * 初始化
	 */
	$("form").each(function(){
		var $this=$(this);
		if($this.hasClass("edit-mode")){
			$this.find("input, select").removeAttr("disabled");
		}
	});
	/*$("form.edit-mode input[name=minDateRange]," +
			"form.edit-mode input[name=maxDateRange]," +
			"form.edit-mode input[name=minAPRForDate]").each(function(){
				var finished=true;
				$(this).parents("fieldset").find("input").each(function(){
					if($(this).val().trim()=="" )
					{
						finished=false;
						return false;
					}
				});
				$(this).parents(".thumbnail.group").find(".chk-range").each(function(){
					this.checked=finished;
				});

			});*/
			
	$(".chk-range").each(function(){
		var $parent=$(this).parents(".thumbnail");
		
		var checked=$(this).is(":checked");
		if(checked){
			$parent.find("input[type=text],select").each(function(){
				$(this).removeAttr("disabled").attr("data-validate",$(this).attr("data-validate-back")).removeAttr("data-validate-back");
			});
			$parent.removeAttr("data-ignore");
		}
		else{
			$parent.find("input[type=text],select").each(function(){
				$(this).attr("disabled",true).attr("data-validate-back",$(this).attr("data-validate")).removeAttr("data-validate").removeClass("invalid");
			});
			$parent.find("label.invalid").remove();
			$parent.attr("data-ignore",true);
		}
	});
	
	//初始化Tab
	if(location.hash&&$("#auto_sub_tab").length>0){
		$("#auto_sub_tab a[href="+location.hash+"]").tab("show");
	}
	
	var info = "<p>1.&nbsp;\"资金账户保留额XXX元\" 是指：在您的账户中至少保留XXX元不参与自动申购。</p>"+
				"<p><li>若您设置的资金账户保留额\"0元\"，则表示您愿意将您交易账号中的所有可用余额用于自动申购。</li></p>"+
				"<p><li>您交易账号中的每一笔还款都会被记入可用余额，您须谨慎设置。</li></p>"+
				"<p>2.&nbsp;若您设置的单笔申购最大金额高于安益金融交易规则规定的单个项目可申购上限，则按照交易规则规定</p>"+
				"<p>的上限金额成交。若您设置的单笔申购最大金额低于安益金融交易规则规定的单个项目可申购下限，则</p>"+
				"<p>您不能参与该项目的自动申购</p>";
//	$("#info-tip").attr("title",info);
	$("#info-tip").popoverx({
		ensure_visiable : true,
		fire_on: 'hover',
		placement : 'right',
	//	width: iwidth,
	//	height: iheight,
		elementToAppend:  $(".right-blk-body").length==0? $(".blk-body"):  $(".right-blk-body"),
		onShown: function(){
			var html = info;
			this.$tip.find('.popover-content').html(html);
			this.resetPosition();
		}
	});
	
	if($("#minDateRange").val() != "0"){
		$("input[name='range-date']").attr("checked","checked");
		if($("form").hasClass("edit-mode") == false){
			$(".day-rz-pro").find("input").each(function(){
				$(this).attr("disabled","disabled");
			}); 
		}else{
			$(".day-rz-pro").find("input").each(function(){
				$(this).removeAttr("disabled");
			}); 
		}
	}
	
	if($.trim($("#minMonthRange").val()) != "0"){
		$("input[name='range-month']").attr("checked","checked");
		if($("form").hasClass("edit-mode") == false){
			$(".month-rz-pro").find("input,select").each(function(){
				$(this).attr("disabled","disabled");
			}); 
		}else{
			$(".month-rz-pro").find("input,select").each(function(){
				$(this).removeAttr("disabled");
			}); 
		}
	}
	
	var info = "<p>1.&nbsp;\"资金账户保留额XXX元\" 是指：在您的账户中至少保留XXX元不参与自动申购。</p>"+
	"<p><li>若您设置的资金账户保留额\"0元\"，则表示您愿意将您交易账号中的所有可用余额用于自动申购。</li></p>"+
	"<p><li>您交易账号中的每一笔还款都会被记入可用余额，您须谨慎设置。</li></p>"+
	"<p>2.&nbsp;若您设置的单笔申购最大金额高于安益金融交易规则规定的单个项目可申购上限，则按照交易规则规定</p>"+
	"<p>的上限金额成交。若您设置的单笔申购最大金额低于安益金融交易规则规定的单个项目可申购下限，则</p>"+
	"<p>您不能参与该项目的自动申购</p>";
	//$("#info-tip").attr("title",info);
	$("#info-tip").popoverx({
	ensure_visiable : true,
	fire_on: 'hover',
	placement : 'right',
	//	width: iwidth,
	//	height: iheight,
	elementToAppend:  $(".right-blk-body").length==0? $(".blk-body"):  $(".right-blk-body"),
	onShown: function(){
	var html = info;
	this.$tip.find('.popover-content').html(html);
	this.resetPosition();
	}
	});
	
	//$('#minAPRForDate').val(util.get_aprate($('#minAPRForDate').val()));
	//$('#minAPRForMonth').val(util.get_aprate($('#minAPRForMonth').val()));
	
});

