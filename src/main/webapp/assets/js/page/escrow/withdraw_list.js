
require(['jquery',
         'global',
         'module/util',
         'form',
         'module/datatables',
         'module/ajax',
         'bootstrap',
         'jquery.printarea',
         'requirejs/domready!'],function($,global,util,Form,datatables){
	
		/**
		 * 变量
		 */
		var option = {};
		
		/**
		 * 初始化
		 */
		//表单控件初始化
		Form.init();
		
		//datatable操作
		var _fnOperate=function(data, type, extra){
			var cashId=extra.cashId;
			var orderId = extra.orderId;
			return "<div class='btn-group'><button type='button' title='实时状态查询' class='btn btn-xs btn-mf-primary btn-now-search' data-cashId="+ cashId +" data-orderId="+orderId+"><i class='fa fa-search fa-lg'></i></button></div>";
		};
		
		//dataTable初始函数
		var initTable = function(){
			var options = {};
			options.tableId = '#withdraw-table';
			options.sAjaxSource = global.context+"/web/esw/withdrawalinfolist";
			options.aaSorting=[[0,"desc"]];
			options.functionList={
				"_fnOperate":_fnOperate,
			};
			var tb=datatables.init(options);
			tb.setParams(util.getSearchData(".search-withdraw"));
			tb.create();
			return tb;
		};
		//datatable操作句柄
		var table=initTable();
		
		/**
		 * 事件
		 */
		//搜索
		$("#btn-search").on("click",function(){
			table.setParams(util.getSearchData(".search-withdraw"));
			table.rawTable.fnDraw();
		});
		
		//时间限制
		$("#start-date").on("changeDate",function(){
			$("#end-date").datepicker("setStartDate",$(this).val());
		});
		$("#end-date").on("changeDate",function(){
			$("#start-date").datepicker("setEndDate",$(this).val());
		});
		
		//操作按钮点击
		$("body").on("click.widthdraw",".btn-now-search",function(e){
			var $target=$(e.currentTarget);
			var orderId = $target.attr("data-orderId");
			var cashId = $target.attr("data-cashId");
			if($target.hasClass("btn-now-search")){
				$.ajax({
					url : global.context+ "/web/esw/getwithdrawalstate/"+orderId+"/"+cashId,
					type : "POST",
					dataType : 'json',
					data: cashId,
					contentType : 'application/json; charset=UTF-8'
				}).done(function(resp){
					$("#modal-status").find("#status").text(resp.retMsg);
					$("#modal-status").modal("show");
				});
			}
		});
});