require(['jquery',
         'global',
         'module/util',
         'module/popup',
         'module/ajax',
         'bootstrap',
         'jquery.placeholder',
         'requirejs/domready!'], 
function($, global, util, popup){
	
	$(document).unbind('ajaxError');
	$(document).unbind('ajaxSuccess');
	
	$("#submit").click(function() {
		$.ajax({
			url: global.context + '/app' + $('#url').val(),
			type: $('#method').val(),
			dataType: 'json',
			headers: {
				'x-form-id' : 'FAKEFORM'
			},
			contentType: 'application/json; charset=UTF-8',
			data: $('#req_data').val()
		}).complete(function(xhr){
//			console.log(xhr);
			$('#resp_status').val(xhr.status);
			$('#resp_text').val(xhr.responseText);
		});
	});
	
	$("#url").change(function() {
		$('#resp_status').val('');
		$('#resp_text').val('');
		$.ajax({
			url: global.context + '/app/test/sample?url=' + $('#url').val(),
			type: 'POST',
			dataType: 'json',
			headers: {
				'x-form-id' : 'FAKEFORM'
			},
			contentType: 'application/json; charset=UTF-8',
			data: null
		}).complete(function(xhr){
//			console.log(xhr);
			if (xhr.status=='200' && xhr.responseJSON != null) {
				$('#req_data').val(JSON.stringify(xhr.responseJSON.request));
				$('#method').val(xhr.responseJSON.method);
			} else {
				$('#req_data').val('');
				$('#method').val('GET');
			}
		});
	});
	$('#url').change();
	
});
