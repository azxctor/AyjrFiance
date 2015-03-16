require(['jquery',
         'global',
         'module/util',
         'form',
         'module/datatables',
         'module/ajax',
         'bootstrap',
         'requirejs/domready!'],function($,global,util,Form,datatables){
	
		/**
		 * 变量
		 */
		var option = {};
		//收付款数据
		summaryData="";
		/**
		 * 初始化
		 */
		//表单控件初始化
		Form.init();
		
		//dataTable 待处理初始函数
		var initTable_todo = function(){
			var options = {};
			options.tableId = '#table-todo';
			options.sAjaxSource = global.context+"/web/esw/transfer/getTransferOrderList/deal";
			options.aaSorting=[[0,"desc"]];
			var tb=datatables.init(options);
			tb.setParams(util.getSearchData("#form-todo-search"));
			tb.create();
			return tb;
		};
		//datatable操作句柄
		var table_todo=initTable_todo();
		
		//dataTable 已处理初始函数
		var initTable_done = function(){
			var options = {};
			options.tableId = '#table-done';
			options.sAjaxSource = global.context+"/web/esw/transfer/getTransferOrderList/done";
			options.aaSorting=[[0,"desc"]];
			var tb=datatables.init(options);
			tb.setParams(util.getSearchData("#form-done-search"));
			tb.create();
			return tb;
		};
		//datatable操作句柄
		var table_done=initTable_done();
		
		//获取汇总数据
	/*	var getSum = function(content){
			$.ajax({
				url : global.context+ "/web/esw/transfer/getTransferSumAmt",
				type : "POST",
				dataType : 'json',
				data: JSON.stringify({"trxDateStr":$(content).find(".trx-data").val()}),
				contentType : 'application/json; charset=UTF-8'
			}).done(function(resp){
				
			});
		};*/
		
		var t_par={
				"payerAcctNo":$("#todo_search_one").find("input").val(),
				"payeeAcctNo":$("#todo_search_two").find("input").val(),
				"trxDate":$("#todo_search_three").find("input").val(),
				"status":$("#todo_search_four").val(),
			};
		var d_par={
				"payerAcctNo":$("#done_search_one").find("input").val(),
				"payeeAcctNo":$("#done_search_two").find("input").val(),
				"trxDate":$("#done_search_three").find("input").val(),
				"status":$("#done_search_four").val(),
			};
		//获取收付款合计
		function getTotal_ajax(c){
			return $.ajax({
				url : global.context+ "/web/esw/transfer/getTransferSumAmt",
				type : "POST",
				dataType : 'json',
				data: JSON.stringify(c),
				contentType : 'application/json; charset=UTF-8'
			});
		};
		//获取收付款初始函数
		var formatFoot = function(tableId,resp){
			$.each(resp,function(key){
				$(tableId).find("tfoot td").each(function(){
					mData = $(this).attr("data-mData");
					sFormatType = $(this).attr("data-sFormatType");
					if(key == mData){
					//	if(key=='count'){
							footData = resp[key];
					//	}else{
					 //		footData = numeral(resp[key]).format('0,0.00');
						//}
						$(this).text(footData);
						return false;
					}
				});
			});
		};
		getTotal_ajax(t_par).done(function(resp){
			formatFoot("#table-todo",resp);
		});
		getTotal_ajax(d_par).done(function(resp){
			formatFoot("#table-done",resp);
		});
		/**
		 * 事件
		 */
		//搜索
		$("#todo").on("click",".btn-search",function(){
			var todo_par={
					"payerAcctNo":$("#todo_search_one").find("input").val(),
					"payeeAcctNo":$("#todo_search_two").find("input").val(),
					"trxDate":$("#todo_search_three").find("input").val(),
					"status":$("#todo_search_four").val(),
				};
			table_todo.setParams(util.getSearchData("#form-todo-search"));
			table_todo.rawTable.fnDraw();
			getTotal_ajax(todo_par).done(function(resp){
				formatFoot("#table-todo",resp);
			});
		});
		$("#done").on("click",".btn-search",function(){
			var done_par={
					"payerAcctNo":$("#done_search_one").find("input").val(),
					"payeeAcctNo":$("#done_search_two").find("input").val(),
					"trxDate":$("#done_search_three").find("input").val(),
					"status":$("#done_search_four").val(),
				};
			table_done.setParams(util.getSearchData("#form-done-search"));
			table_done.rawTable.fnDraw();
			getTotal_ajax(done_par).done(function(resp){
				formatFoot("#table-done",resp);
			});
		});
		
		$("#todo").find(".trx-data").on("changeDate",function(){
			if($(this).val() == ""){
				$("body").find(".btn-modal-tranfer").attr("disabled","disabled");
			//	$("body").find(".btn-search").attr("disabled","disabled");
			}
			else{
				$("body").find(".btn-modal-tranfer").removeAttr("disabled");
			//	$("body").find(".btn-search").removeAttr("disabled");
			}
		});
		
		$("#confirmModal").on("shown.bs.modal", function() {
			var trx_dt = $("#todo").find(".trx-data").val();
			$("#modal-confirm-info").text("是否确定对"+ trx_dt +"的交易进行转账？");
		});
		
		//操作按钮点击
		$("body").on("click",".btn-transfer",function(e){
			var $target=$(e.currentTarget);
			var trx_dt = $("#todo").find(".trx-data");
			if($target.hasClass("btn-transfer")){
				$.ajax({
					url : global.context+ "/web/esw/transfer/doTransfer/" + trx_dt.val(),
					type : "POST",
					dataType : 'json',
					contentType : 'application/json; charset=UTF-8'
				}).done(function(resp){
					if(resp.code == "ACK"){
						table_todo.setParams(util.getSearchData("#form-todo-search"));
						table_todo.rawTable.fnDraw();
						table_done.setParams(util.getSearchData("#form-done-search"));
						table_done.rawTable.fnDraw();
					}
				});
			}
		});
		
});