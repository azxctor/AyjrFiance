require(['jquery',
         'global',
         'module/util',
         'form',
         'module/datatables',
         'module/ajax',
         'bootstrap',
         'jquery.printarea',
         'requirejs/domready!'],function($,global,util,Form,datatables){

	//datepicker
	$("#creat-date").datepicker({
			 autoclose: false,
			 format: "yyyy-mm-dd",
			 language: 'zh-CN'
		});
		
	
	var _fnTrx_memo=function(data, type, extra){
		var msgBody = extra.msgBody;
		return "<div class='btn-group'><a type='' title='查看' class='msg-search' data-msgBody="+msgBody+">查看</a></div>";
	};
	//datatable设置
	var initTable = function(){
		var options = {};
		options.tableId = '#msg_log_table';
		options.sAjaxSource = global.context+"/web/esw/eswmsgloglist";
		options.aaSorting=[[6,"desc"]];
		options.functionList={
				"_fnTrx_memo":_fnTrx_memo,
			};
		/*options.rawOptions = {"fnCreatedRow":function(row, data, dataIndex){
			$(row).find(".table-tooltip").tooltip({
				container: 'body',
				placement: 'bottom',
				trigger:'hover'
			});
		}};*/
		var tb=datatables.init(options);
		tb.setParams(util.getSearchData(".search-recharge"));
		tb.create();
		return tb;
	};
	var table=initTable();
	//查询按钮
	$("#log_search_btn").on("click",function(){
		table.setParams(util.getSearchData(".search-recharge"));
		table.rawTable.fnDraw();
	});
	//查看操作
	$("body").on("click",".msg-search",function(e){
		var $target=$(e.currentTarget);
		var msgBody = $target.attr("data-msgBody");
		$(".msg_info").html(msgBody);
		$("#msgBody-modal").modal("show");
	});
	
});