require(['jquery',
         'global',
         'module/util',
         'module/ajax',
         'bootstrap',
         'requirejs/domready!'], 
function($, global, util){
	
	$('#account-form').validate({
		submitHandler: function(form) {
			util.ajax_submit(form);
	    },
	    highlight: function(element, errorClass, validClass) {
	    	$(element).addClass(errorClass).removeClass(validClass);
	    	$(element).parent().removeClass('has-success')
	    		.addClass('has-error').find('.fa').remove();
	    	$(element).parent().append('<i class="fa fa-exclamation-circle form-control-feedback"></i>');
	    },
	    unhighlight: function(element, errorClass, validClass) {
	    	$(element).removeClass(errorClass).addClass(validClass);
	    	$(element).parent().removeClass('has-error')
    			.addClass('has-success').find('.fa').remove();
	    	$(element).parent().append('<i class="fa fa-check form-control-feedback"></i>');
	    }
	});
	$('#delete-account-form').validate({
		submitHandler: function(form) {
			util.ajax_submit(form);
	    }
	});
	$('#delete').click(function(){
		$.ajax({
			url: $('#delete').attr('action'),
			type: 'DELETE',
			dataType: 'json',
			contentType: 'application/json; charset=UTF-8'
		});
	});
});
