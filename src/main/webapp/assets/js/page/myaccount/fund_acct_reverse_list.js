require(['jquery',
         'global',
         'module/util',
         'form',
         'module/datatables',
         'module/ajax',
         'bootstrap',
         'jquery.printarea',
         'requirejs/domready!'],function($,global,util,Form,datatables){
	
		//当前操作的申请
		var currentApplNo="";
		
		//时间限制
		$("#start-date").on("changeDate",function(){
			$("#end-date").datepicker("setStartDate",$(this).val());
		});
		$("#end-date").on("changeDate",function(){
			$("#start-date").datepicker("setEndDate",$(this).val());
		});
		
		/*
		 * 初始化
		 */
		/*
		$(".datepicker").datepicker({
			 autoclose: true,
			 format: "yyyy-mm-dd",
			 weekStart: 1,
			 todayBtn: false,
			 language: 'zh-CN'
		});
		var nowDate = Date.parse($("#now-date").text().replace(/-/g,"/"));
		$('.datepicker').datepicker('setDate',new Date(nowDate));
		*/
		
		//datatable审批按钮
		var _fnOperate=function(data, type, extra){
			var disabled=extra.trxStatus.name == "DEALING"?"":"disabled";
			var applNo= extra.trxStatus.name == "DEALING"? (extra.rvsApplNo||""):"";
			return $.format("<div class='btn-group'><button type='button' title='通过' class='btn btn-xs btn-mf-primary btn-approve' {1} data-applNo={0}>&nbsp;<i class='fa fa-check fa-lg'></i>&nbsp;</button> <button type='button' title='拒绝' class='btn btn-xs btn-mf-default btn-refuse' {1} data-applNo={0}>&nbsp;<i class='fa fa-times fa-lg'></i>&nbsp;</button></div>",applNo,disabled);
		};
		var _fnLog=function(data, type, extra){
			var rvsJnlNo=extra.jnlNo||'';
			var rvsFlg = extra.rvsFlg;
			if(rvsFlg!=null&&extra.rvsFlg.name=='YES'){
				rvsJnlNo=extra.rvsJnlNo||'';
			}
			return $.format("<button type='button' title='日志' class='btn btn-xs btn-mf-primary btn-log' data-applNo={0}><i class='fa fa-list-ul fa-lg'></i></button>",rvsJnlNo);
		};
		var _fnEllipsis=function(data, type, extra){
			var dealMemo=extra.dealMemo;
			if(dealMemo != null){
				var dealMemoWhow = dealMemo.length > 10?'<span class="link" data-toggle="popover" data-content="'+dealMemo+'">'+ dealMemo.substring(0,11)+'...'+'</span>':dealMemo;
				return dealMemoWhow;
			}
		};
		var _fnTrxAmt = function(data, type, extra){
			var rvsFlg=extra.rvsFlg;
			var rvs = false;
			if(rvsFlg!=null&&rvsFlg.name=='YES'){
				rvs = true;
			}
			var data = numeral(extra.trxAmt).format('0,0.00');
			if(rvs){
				return '<span class="table-align-right" style="color:RED">'+data+'</span>';
		    }else{
				return '<span class="table-align-right" style="color:#49B21F">'+data+'</span>';
		    }
		};
		
		//dataTable初始函数
		var initTable = function(tableId,url){
			var options = {};
			options.tableId = tableId;
			options.sAjaxSource = global.context+"/web/"+url;
			options.aaSorting=[[0,"desc"]];
			options.functionList={
				"_fnOperate":_fnOperate,
				"_fnLog":_fnLog,
				"_fnTrxAmt":_fnTrxAmt,
				"_fnEllipsis":_fnEllipsis
			};
			options.rawOptions={
				"bAutoWidth":false,
				"sScrollX": "200%"
			};
			var tb=datatables.init(options);
			tb.setParams(util.getSearchData(".search-withdraw"));
			tb.create();
			return tb;
		};
		//datatable操作句柄
		var normalTable = initTable("#normal-table","get/bank/trx/jnl/reverse/list");
		
		//日志table初始
		function initLogTable(){
			var options = {};
			options.tableId = '#table-log';
			options.sAjaxSource = global.context+"/web/get/reverse/appl/list";
			options.rawOptions={
				"bAutoWidth":false
			};
			log_table = datatables.init(options);
			log_table.setParams([{name:"rvsJnlNo",value:""}]);
			return log_table.create();
		};
		var logTable = initLogTable();
		
		/**
		 * 事件
		 */
		//搜索
		$("#btn-search").on("click",function(){
			normalTable.setParams(util.getSearchData(".search-withdraw"));
			normalTable.rawTable.fnDraw();
		});
		
		//操作按钮点击
		$("body").on("click",".btn-approve,.btn-refuse,.btn-log",function(e){
			var $target=$(e.currentTarget);
			currentApplNo=$target.attr("data-applNo");
			if(!currentApplNo){
				return false;
			}
			if($target.hasClass("btn-approve")){
				$("#modal-approve").modal().show();
			}
			else if($target.hasClass("btn-refuse")){
				$("#modal-refuse").modal().show();
			}
			else if($target.hasClass("btn-log")){
				//alert(currentApplNo);
				logTable.setParams([{name:"rvsJnlNo",value:currentApplNo}]);
				logTable.rawTable.fnDraw();
				$("#modal-log").modal().show();
			}
		});
		//备注显示
		$("body").on("mouseover",".link",function(e){
			var $target=$(e.currentTarget);
			$target.popover({
				trigger:'hover',
				html:true,
				placement:'bottom',
				selector:true,
				container: 'body'//,
				/*content:$target.attr("data-dealMemo")*/
			});
		});
		$("body").on("click",".btn-modal-agree",function(e){
			var $modal=$(e.currentTarget).parents(".modal");
			if($modal.attr("id")=="modal-approve"){
				$.ajax({
					url:global.context+"/web/bank/trx/jnl/reverse/appl/approve",
					type: "POST",
					dataType: 'json',
					contentType: 'application/json; charset=UTF-8',
					data:JSON.stringify({passed:true,appId:currentApplNo})
				}).done(function(resp){
					$modal.data("bs.modal").hide();
					if(resp.code == "ACK"){
						normalTable.setParams(util.getSearchData(".search-withdraw"));
						normalTable.rawTable.fnDraw();
					}
				});
			}
			else if($modal.attr("id")=="modal-refuse"){
				if(!$("#form-reason").validate().form()){
					return false;
				}
				$.ajax({
					url:global.context+"/web/bank/trx/jnl/reverse/appl/approve",
					type: "POST",
					dataType: 'json',
					contentType: 'application/json; charset=UTF-8',
					data:JSON.stringify({passed:false,comments:$modal.find("#reason").val(),appId:currentApplNo})
				}).done(function(resp){
					$modal.data("bs.modal").hide();
					if(resp.code == "ACK"){
						normalTable.setParams(util.getSearchData(".search-withdraw"));
						normalTable.rawTable.fnDraw();
					}
				}).always(function(){
					$modal.find("#reason").val("");
				});
			}
		});
		//表单验证
		$("#form-reason").validate();
		//表单清空
		$(".modal").on("hidden.bs.modal",function(){
			$(this).find("form").each(function(){
				this.reset();
			});
		});
		/**
		 * 初始化
		 */
		//表单控件初始化
		Form.init();
});