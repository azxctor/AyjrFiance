require(['jquery',
         'global',
         'module/util',
         'module/ajax',
         'bootstrap',
         'jquery.placeholder',
         'requirejs/domready!'], 
function($, global, util){
	$('input').placeholder();
	
	// Add jQuery validate to binding form
	$('#binding-form').validate({
		submitHandler: function(form) {
			util.ajax_submit("#binding-form").complete(function(xhr) {
				var result = $.parseJSON(xhr.responseText);
				if(result.code=="ACK"){
					/*$("#reg-btn").attr("disabled",true);
					$("#binding-form")[0].reset();*/
				}
			});
	    }
	});
	
	//
});
