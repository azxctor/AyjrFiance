require(['jquery',
         'global',
         'module/util',
         'module/datatables',
         'module/ajax',
         'bootstrap',
         'requirejs/domready!'], 
function($, global, util,datatables){
	var options = {};
	var cols = [];
	var data=[{"name":"username","value":"fff","operator":"eq"}];//QUERY VARIABLE
	cols.push({"mDataProp":"username"},
            {"mDataProp":"email"},
            {"mDataProp":"mobile"});
	options.tableId = '#pagination-example';
	options.sAjaxSource = "getAllUsers";
	options.aoColumns = cols; 
	datatables.init(options);
	datatables.setParams(data);
	datatables.create();
});
