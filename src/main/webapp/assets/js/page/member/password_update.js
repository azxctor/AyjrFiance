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
			util.ajax_submit("#password-form").complete(function(xhr) {
				var result = $.parseJSON(xhr.responseText);
				if(result.code=="ACK"){
					$("#reg-btn").attr("disabled",true);
					$("#password-form")[0].reset();
				}
			});
	    }
	});

	$('#user-form').validate({
		submitHandler: function(form) {
			util.ajax_submit("#user-form").complete(function(xhr) {
				var result = $.parseJSON(xhr.responseText);
				if(result.code=="ACK"){
					$("#user-form")[0].reset();
				}
			});
	    }
	});
});
