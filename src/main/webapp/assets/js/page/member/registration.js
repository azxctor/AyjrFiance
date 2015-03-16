require(['jquery',
         'global',
         'module/util',
         'module/ajax',
         'bootstrap',
         'jquery.metadata',
         'jquery.validate.methods',
         'requirejs/domready!'], 
function($, global, util){
	$("#captcha-img").attr("src", global.context+ "/web/captcha?r="  + Math.random());
    $("#reg-btn").removeAttr("disabled");
	// Add event handler for captcha image click
	$("#captcha-img").click(function() {
	    $(this).attr("src", global.context+ "/web/captcha?r=" + Math.random());
	});
	
	$("#agg-check").on("click",function(){
		if($(this).is(":checked")){
			$("#reg-btn").removeAttr("disabled");
		}else{
			$("#reg-btn").attr("disabled","disabled");
		}
	});
	// Add jQuery validate to sign-in form
	$('#register-form').validate({
		/*rules: {
			captcha: {
		      required: true,
		      remote: global.context+ "/web/members/signin/captcha",
		   
		    }
		  },*/
	//	  onkeyup: false,
		submitHandler: function(form) {
			$('#captcha-link').removeAttr('disabled');
			util.ajax_submit(form).complete(function(xhr){
				var result = $.parseJSON(xhr.responseText);
				//Write own logic if necessary
				$('#captcha-img').attr("src", 
						global.context+ "/web/captcha?r=" + Math.random());
			});
	    },
/*	    messages: {
			captcha: {
				remote: "验证码错误"
			}
		},*/
	    highlight: function(element, errorClass, validClass) {
	    	$(element).addClass(errorClass).removeClass(validClass);
	    	$(element).parent().removeClass('has-success')
	    		.addClass('has-error').find('i.form-control-feedback').remove();
	    	$(element).parent().append('<i class="fa fa-exclamation-circle form-control-feedback"></i>');
	    },
	    unhighlight: function(element, errorClass, validClass) {
	    	if(!$(element).hasClass("yzm-input"))
	    	{
	    		$(element).removeClass(errorClass).addClass(validClass);
		    	$(element).parent().removeClass('has-error')
	    			.addClass('has-success').find('i.form-control-feedback').remove();
		    	$(element).parent().append('<i class="fa fa-check form-control-feedback"></i>');
	    	}else{
		    	$(element).parent().removeClass('has-error').find('i.form-control-feedback').remove();
	    	}	
	    	
	    }
	});
	
	function timer(intDiff){
		var $cl = $('#captcha-link');
	    var interval = setInterval(function(){
	    var day=0,
	        hour=0,
	        minute=0,
	        second=0;//时间默认值        
	    if(intDiff > 0){
	        day = Math.floor(intDiff / (60 * 60 * 24));
	        hour = Math.floor(intDiff / (60 * 60)) - (day * 24);
	        minute = Math.floor(intDiff / 60) - (day * 24 * 60) - (hour * 60);
	        second = Math.floor(intDiff) - (day * 24 * 60 * 60) - (hour * 60 * 60) - (minute * 60);
	    }
	    if (minute <= 9) minute = '0' + minute;
	    if (second <= 9) second = '0' + second;
	    $cl.html(second+'秒');
	    $cl.attr('disabled', 'disabled');
	    if (second == '00'){
	    	clearInterval(interval)
	    	$cl.html('获取手机验证码');
	    	if ($('input[name="mobile"]').next().hasClass('fa-check')){
	    		$cl.removeAttr('disabled');
	    	}
	    }
	    intDiff--;
	    }, 1000);
	} 
	
	var isSmsCheckEnabled = $('#is_sms_check_enabled').val();
    if (isSmsCheckEnabled == "true") {
    	$(".mobile-check-item").show();
    }
	//get phone captcha
	$("#captcha-link").click(function() {
		var intDiff = parseInt(59);//倒计时总秒数量
		timer(intDiff);
		$("input[name=code]").show();
		var phone = $("input[name=mobile]").val();
		$.ajax({
			url : global.context + "/web/members/smsVerify/"+phone,
			type : "post",
			dataType : 'json',
			contentType : 'application/json; charset=UTF-8',
		}).done(function(resp) {
		});
	});
	
	$('input[name="mobile"]').on('blur', function() {
		var $cl = $('#captcha-link');
		if ('获取手机验证码' != $cl.html()){
			return;
		}
		if ($(this).next().hasClass('fa-check')){
			$cl.removeAttr('disabled');
		}
	});
});
