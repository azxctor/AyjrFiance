require(['jquery',
 'global',
 'module/util',
 'form',
 'module/datatables',
 'module/ajax',
 'bootstrap',
 'jquery.printarea',
 'requirejs/domready!'],
function($,global,util,Form,datatables){
	
		/**
		 * 变量
		 */
		var option = {};
		
		/**
		 * 初始化
		 */
		//表单控件初始化
		Form.init();	
		
		var _fntrxAmt = function(data, type, extra){
			var account = numeral(extra.trxAmt).format('0,0.00');
			if(extra.payRecvFlg.code == "R"){
				return '<span class="table-align-right" style="color:#49B21F">'+ '+' + account +'</span>';
			}
			else{
				return '<span class="table-align-right" style="color:RED">'+ '-' + account +'</span>';
			}
		};
		//dataTable初始函数
		var dealTable = function(){
			var options = {};
			options.tableId = '#table-netting-mgt-deal';
			options.sAjaxSource = global.context+"/web/netting/getRecvPayOrderList/deal";
			options.aaSorting=[[0,"desc"]];
			options.functionList={
				"_fntrxAmt":_fntrxAmt
			};
			var tb=datatables.init(options);
			tb.setParams(util.getSearchData(".search"));
			tb.create();
			return tb;
		};

		var doneTable = function(){
			var options = {};
			options.tableId = '#table-netting-mgt-done';
			options.sAjaxSource = global.context+"/web/netting/getRecvPayOrderList/done";
			options.aaSorting=[[0,"desc"]];
			options.functionList={
				"_fntrxAmt":_fntrxAmt
			};
			var tb=datatables.init(options);
			tb.setParams(util.getSearchData(".search"));
			tb.create();
			return tb;
		};

		//datatable操作句柄
		var dlTable = dealTable();
		var deTable = doneTable();

		/**
		 * 事件
		 */
		//搜索
		$("#btn-search-done").on("click",function(){
			deTable.setParams(util.getSearchData(".search-done"));
			deTable.rawTable.fnDraw();
		});

		$("#btn-search-deal").on("click",function(){
			dlTable.setParams(util.getSearchData(".search-deal"));
			dlTable.rawTable.fnDraw();
		});
		
		$("#deal").find(".trx-data").on("changeDate",function(){
			if($(this).val() == ""){
				$("body").find(".btn-modal-netting").attr("disabled","disabled");
				//$("#btn-search-deal").attr("disabled","disabled");
			}
			else{
				$("body").find(".btn-modal-netting").removeAttr("disabled");
				//$("#btn-search-deal").removeAttr("disabled");
			}
		});
		
		$("#confirmModal").on("shown.bs.modal", function() {
			var trx_dt = $("#deal").find(".trx-data").val();
			$("#modal-confirm-info").text("是否确定对"+ trx_dt +"的交易进行轧差？");
		});
		
		//操作按钮点击
		$("body").on("click",".btn-netting",function(e){
			var $target=$(e.currentTarget);
			var trx_dt = $("#deal").find(".trx-data");
			if($target.hasClass("btn-netting")){
				$.ajax({
					url : global.context+ "/web/netting/doNetting/" + trx_dt.val(),
					type : "POST",
					dataType : 'json',
					contentType : 'application/json; charset=UTF-8'
				}).done(function(resp){
					if(resp.code == "ACK"){
						deTable.setParams(util.getSearchData(".search-done"));
						deTable.rawTable.fnDraw();
						dlTable.setParams(util.getSearchData(".search-deal"));
						dlTable.rawTable.fnDraw();
					}
				});
			}
		});
});