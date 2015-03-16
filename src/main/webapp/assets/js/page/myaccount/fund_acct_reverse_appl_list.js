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
		 *//*
		var modal_content = {
					modal_header:null,
					modal_body:null,
					modal_footer:null,
					modal_data:null,
		};
			
		// modal渲染
		var renderModal=function(modal_content){
			$("#project-modal-header").empty().html(modal_content.modal_header);
			$("#project-modal-body").empty().html(modal_content.modal_body);
			$("#project-modal-footer").empty().html(modal_content.modal_footer);
			$("#project-modal").modal(modal_content.modal_data);
			$("#project-modal").on('hidden.bs.modal', function (e) {
				$("#project-modal-footer").unbind("click");
			});
		} ;*/
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
		
		//datatable冲正按钮
		var _fnOperate=function(data, type, extra){
			var disabled= extra.trxStatus.name == "NORMAL"? "":"disabled";
			var applNo= extra.trxStatus.name == "NORMAL"? extra.jnlNo:(extra.rvsApplNo||"");
			var rvsFlg = extra.rvsFlg;
			if(rvsFlg!=null&&extra.rvsFlg.name=='YES'){
				disabled = "disabled";
			}
			return $.format("<button type='button' title='出入金冲正申请' class='btn btn-xs btn-mf-primary btn-revocation' {0} data-applNo={1}><i class='fa fa-reply-all fa-lg'></i></button>",disabled,applNo);
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
				"sScrollX": "200%",
				"bAutoWidth":false
			};
			var tb=datatables.init(options);
			tb.setParams(util.getSearchData(".search-withdraw"));
			tb.create();
			return tb;
		};
		//datatable操作句柄
		var applylTable = initTable("#normal-table","get/bank/trx/jnl/reverse/list");
		
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
			applylTable.setParams(util.getSearchData(".search-withdraw"));
			applylTable.rawTable.fnDraw();
		});
		
		//操作按钮点击
		$("body").on("click",".btn-revocation,.btn-log",function(e){
			var $target=$(e.currentTarget);
			currentApplNo = $target.attr("data-applNo");
			if(!currentApplNo){
				return false;
			}
			if($target.hasClass("btn-revocation")){
				$("#modal-revocation").modal().show();
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
			/*var $targetBtn=$(e.currentTarget);*/
			if($modal.attr("id")=="modal-revocation"){
				if(!$("#form-reason").validate().form()){
					return false;
				}
				$.ajax({
					url:global.context+"/web/bank/trx/jnl/reverse/appl/create",
					type: "POST",
					dataType: 'json',
					contentType: 'application/json; charset=UTF-8',
					data:JSON.stringify({trxMemo:$modal.find("#reason").val(),rvsJnlNo:currentApplNo})
				}).done(function(resp){
					$modal.data("bs.modal").hide();
					/*if(resp.code == "ACK" || resp.code == "NACK"){
						modal_content.modal_header = '<h4 class="modal-title" id="myModalLabel">提示</h4>';
						modal_content.modal_body = resp.message;
						modal_content.modal_footer = '<button type="button" class="btn btn-mf-primary btn-modal-agree" id="modal-sure" data-dismiss="modal">确定</button>';
						renderModal(modal_content);
					}*/
					if(resp.code == "ACK"){
						applylTable.setParams(util.getSearchData(".search-withdraw"));
						applylTable.rawTable.fnDraw();
					}
				});
			}
			/*else if($targetBtn.attr("id")=="modal-sure"){
				applylTable.setParams(util.getSearchData(".search-withdraw"));
				applylTable.rawTable.fnDraw();
			}*/
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