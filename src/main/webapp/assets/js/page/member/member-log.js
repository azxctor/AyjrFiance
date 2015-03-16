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
	/**
	 * Get audit log for specific User
	 */
	var getLog = function(userId) {
			var options = {};
			var cols = [];
			cols.push({aTargets : [ '_all' ], "sDefaultContent" : "&nbsp;"},
					{"aTargets":[0],"mData":"operateTime"},
					{"aTargets":[1],"mData":"operateUser"},
					{"aTargets":[2],"mData":"action.text","bSortable":false},
					{"aTargets":[3],"mData":"result","bSortable":false});
			options.tableId = '#logTable';
			options.sAjaxSource = global.context+"/web/search/getlogs";
			options.aoColumns = cols; 
			options.aaSorting=[[0,"desc"]];
			options.functionList={"_fnresultRender":_fnresultRender};
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
		log_table.invoke("fnDraw");
	};
	
	$("body").on("click","#personSearchTable a.log-link",function(){
		var userId = $(this).attr("data-id");
		renderLogTalbe(userId);
	});
	$("body").on("click","#orgSearchTable a.log-link",function(){
		var userId = $(this).attr("data-id");
		renderLogTalbe(userId);
	});
	$("body").on("click","#agencySearchTable a.log-link",function(){
		var userId = $(this).attr("data-id");
		renderLogTalbe(userId);
	});
	$("body").on("click","#providerSearchTable a.log-link",function(){
		var userId = $(this).attr("data-id");
		renderLogTalbe(userId);
	});
	$("body").on("click",".PWResetTable a.log-link",function(){
		var userId = $(this).attr("data-id");
		renderLogTalbe(userId);
	});
	
});
