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
		var _fnRetCode=function(data, type, extra){
			var strRetCode = "";
			var retCode=extra.retCode;
			if(retCode && retCode.length >= 10){
				strRetCode = retCode.substr(0,10) + "...";
			}else if(retCode){
				strRetCode = retCode;
				retCode = "";
			}
			return "<span class='table-tooltip' data-toggle='tooltip' data-placement='bottom' title="+ retCode +">"+ strRetCode +"</span>";
		};
		
		var _fnTrx_memo=function(data, type, extra){
			var strTrxMemo = "";
			var trxMemo=extra.trxMemo;
			if(trxMemo && trxMemo.length >= 10){
				strTrxMemo = trxMemo.substr(0,10) + "...";
			}else if(trxMemo){
				strTrxMemo = trxMemo;
				trxMemo = "";
			}
			return "<span class='table-tooltip' data-toggle='tooltip' data-placement='bottom' title="+ trxMemo +">"+ strTrxMemo +"</span>";
		};
		
		var _fnCancellation=function(data,type,extra){
			var orderId = extra.orderId;
			var statusCode = extra.status.code;
			if(statusCode == "P"){
				return "<div class='btn-group'><button type='button' title='作废' class='btn btn-xs btn-mf-primary btn-now-search cancel-btn' data-orderId="+orderId+">作废</button></div>";
			}
		};
		//dataTable初始函数
		var initTable = function(){
			var options = {};
			options.tableId = '#recharge-table';
			options.sAjaxSource = global.context+"/web/esw/recharge/getRechargeTransList";
			options.aaSorting=[[0,"desc"]];
			options.functionList={
				"_fnRetCode":_fnRetCode,
				"_fnTrx_memo":_fnTrx_memo,
				"_fnCancellation":_fnCancellation
			};
			options.rawOptions = {"fnCreatedRow":function(row, data, dataIndex){
				$(row).find(".table-tooltip").tooltip({
					container: '#recharge-table',
					placement: 'bottom',
					trigger:'hover'
				});
			}};
			var tb=datatables.init(options);
			tb.setParams(util.getSearchData(".search-recharge"));
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
			table.setParams(util.getSearchData(".search-recharge"));
			table.rawTable.fnDraw();
		});
		
		//
		//作废按钮
		$("body").on("click",".btn-now-search",function(e){
			var $target=$(e.currentTarget);
			var orderId = $target.attr("data-orderId");
			$("#cancle-sure").attr("data-orderId",orderId);
			$("#cancle-modal").modal("show");
		});
		

		$("#cancle-sure").on("click",function(){
			var orderId = $(this).attr("data-orderId");
			$.ajax({
				url : global.context+ "/web/esw/recharge/abolish/",
				type : "POST",
				dataType : 'json',
				data: orderId,
				contentType : 'application/json; charset=UTF-8'
			}).done(function(resp){
				if(resp.code === "ACK"){
					table.setParams(util.getSearchData(".search-recharge"));
					table.rawTable.fnDraw();
				}
			});
		});
		
		//时间限制
		$("#start-date").on("changeDate",function(){
			$("#end-date").datepicker("setStartDate",$(this).val());
		});
		$("#end-date").on("changeDate",function(){
			$("#start-date").datepicker("setEndDate",$(this).val());
		});
		
});