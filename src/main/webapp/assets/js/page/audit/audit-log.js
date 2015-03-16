require(['jquery',
         'global',
         'module/datatables',
         'module/util',
         'requirejs/domready!'], 
function($, global,datatables,util){
	var log_table = null;
	var _fnresultRender = function(data,type,full){
		return full.result.replace(/;/g,"<br>");
	};
	/**
	 * Get audit log for specific User
	 */
	var getLog = function(userId) {
			var options = {};
			options.tableId = '#logTable';
			options.sAjaxSource = global.context+"/web/audit/getlogs";
			options.functionList={"_fnresultRender":_fnresultRender};
			options.aaSorting=[[0,"desc"]];
			log_table = datatables.init(options);
			log_table.setParams([{"name":"userId","value":userId}]);
			return log_table.create();
	};
	
	var table = null;
	
	var renderLogTalbe = function(userId){
		if(!table){
			table = getLog(userId);
			return;
		}
		log_table.setParams([{"name":"userId","value":userId}]);
		table.invoke("fnDraw");
	};
	
	$("body").on("click","#auditTable a.log-link",function(){
		var userId = $(this).attr("data-id");
		renderLogTalbe(userId);
	});
	$("body").on("click","#orgAuditTable a.log-link",function(){
		var userId = $(this).attr("data-id");
		renderLogTalbe(userId);
	});
	$("body").on("click","#serviceTable a.log-link",function(){
		var userId = $(this).attr("data-id");
		renderLogTalbe(userId);
	});
	$("body").on("click","#providerTable a.log-link",function(){
		var userId = $(this).attr("data-id");
		renderLogTalbe(userId);
	});
	
});
