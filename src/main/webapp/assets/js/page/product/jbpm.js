require([ 'jquery', 'global', 'module/util', 'module/ajax', 'bootstrap',
		'requirejs/domready!' ], function($, global, util) {
	$('#jbpm-form21').validate({
		submitHandler : function(form) {
			util.ajax_submit(form);
		}
	});
});
