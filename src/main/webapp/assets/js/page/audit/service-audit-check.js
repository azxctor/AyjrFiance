require([ 'jquery',
          'global', 
          'module/util',
          'module/ajax',
          'requirejs/domready!' ], function($, global,util) {
	$("#serviceCheckModal input[name='passed']").change(function() {
		var $comm = $("#comments_id");
		if ($(this).val() == 'false') {
			$comm.addClass('required');
			$comm.find("span.nope").show();
		} else if ($(this).val() == 'true') {
			$comm.removeClass('required');
			$comm.find("span.nope").hide();
			$("#serviceCheckModal").find("label.invalid").remove();
		}
	});

	$('#serviceCheckModal').on('hidden.bs.modal', function(e) {
		$("#check-form")[0].reset();
		$("#comments_id").addClass("required").find("span").show();
		$(this).find("label.invalid").remove();
	});
	
	
	
	$('#check-form').validate({
		submitHandler : function(form) {
				util.ajax_submit('#check-form');
		}
	});
});
