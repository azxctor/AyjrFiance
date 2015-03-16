require(['jquery',
         'global',
         'module/util',
         'module/ajax',
         'bootstrap',
         'jquery.placeholder',
         'requirejs/domready!'], 
function($, global, util){
	$('input').placeholder();
	
	// Add jQuery validate to sign-in form
	$('#password-form').validate({
		submitHandler: function(form) {
			$('#captcha-link').removeAttr('disabled');
			util.ajax_submit("#password-form").complete(function(xhr) {
				var result = $.parseJSON(xhr.responseText);
				/*if(result.code=="ACK"){
					$("#reg-btn").attr("disabled",true);
					$("#password-form")[0].reset();
				}*/
			});
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
	$("#captcha-link").click(function() {
		var intDiff = parseInt(59);//倒计时总秒数量
		timer(intDiff);
		$("input[name=authCode]").show();
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
