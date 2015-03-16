require([ 'jquery',
          'global', 
          'module/util',
          'module/bread_crumb',
          'module/ajax',
          'requirejs/domready!' ], function($, global,util,mfBread) {
	$("#checkModal input[name='passed']").change(function() {
		var $comm = $("#comments_id");
		if ($(this).val() == 'false') {
			$comm.addClass('required');
			$comm.find("span.nope").show();
		} else if ($(this).val() == 'true') {
			$comm.removeClass('required');
			$comm.find("span.nope").hide();
			$("#checkModal").find("label.invalid").remove();
		}
	});

	$('#checkModal').on('hidden.bs.modal', function(e) {
		$("#check-form")[0].reset();
		$("#comments_id").addClass("required").find("span").show();
		$(this).find("label.invalid").remove();
	});
	
	
	
	$('#check-form').validate({
		submitHandler : function(form) {
				util.ajax_submit('#check-form').complete(function(response){
					var count=$('#unaudited_count').val();
					if(response&&response.responseJSON&&response.responseJSON.code!="VALIDATION_ERROR"){
						$('#checkModal').modal("hide");
					}
					var responseJSON = response.responseJSON;
					if(responseJSON){
						var data = responseJSON.data;
						if(!data.passed && data.memberType && data.memberType.code=='B'){
							$(".go-back").click();
						}else if(data.passed && data.memberType && data.memberType.code=='B'){
							$('#indiv_basic_audit').hide();
							$('#indiv_investor_tip').hide();
							$('#indiv_investor_audit').removeClass('disabled');
							$('#indiv_financier_tip').hide();
							$('#indiv_financier_audit').removeClass('disabled');
							$('#org_basic_audit').hide();
							$('#org_investor_tip').hide();
							$('#org_investor_audit').removeClass('disabled');
							$('#org_financier_tip').hide();
							$('#org_financier_audit').removeClass('disabled');
							count = count-1;
						}else if(data.memberType && data.memberType.code=='T'){
							$('#indiv_investor_audit').hide();
							$('#org_investor_audit').hide();
							count = count-1;
						}else if(data.memberType && data.memberType.code=='R'){
							$('#indiv_financier_audit').hide();
							$('#org_financier_audit').hide();
							count = count-1;
						}
					}
					$('#unaudited_count').val(count);
					if($('#unaudited_count').val()==0){
						mfBread.pop();
						$('.go-back').click();
					}
					
				});
		}
	});
});
