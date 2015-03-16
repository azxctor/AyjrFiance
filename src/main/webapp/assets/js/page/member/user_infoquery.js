require(['jquery',
         'global',
         'module/util',
         'module/ajax',
         'bootstrap',
         'bootstrap-datepicker',
         'requirejs/domready!'], 
function($, global, util){
	$('#datetimepicker').datepicker();
});
