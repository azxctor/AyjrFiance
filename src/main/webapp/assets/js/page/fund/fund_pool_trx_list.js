require(['jquery',
         'global',
         'module/util',
         'form',
         'module/datatables',
         'module/ajax',
         'bootstrap',
         'requirejs/domready!'],function($,global,util,Form,datatables){
		
		//出入金汇总数据
		summaryData="";
		
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
		$(".datepicker").datepicker({
			 autoclose: true,
			 format: "yyyy-mm-dd",
			 weekStart: 1,
			 todayBtn: false,
			 language: 'zh-CN'
		});
		var nowDate = Date.parse($("#now-date").text().replace(/-/g,"/"));
		$('.datepicker').datepicker('setDate',new Date(nowDate));
		
		//获取列表出入金汇总数据
		var getTableInfo = function(){
			return $.ajax({
				url:global.context+"/web/fund/pool/amt/summarizing",
				type: "POST",
				dataType: 'json',
				contentType: 'application/json; charset=UTF-8',
				data:JSON.stringify({"fromDate":$("#start-date").val(),"toDate":$("#end-date").val(),"acctNo":$("#acctNo").val(),"cashPool":$("#cash-pool").val(),"useType":$("#use-type").val()})
			});
		};
		
		//出入金汇总数据初始函数
		var formatFoot = function(tableId,resp){
			$.each(resp,function(key){
				$(tableId).find("tfoot td").each(function(){
					mData = $(this).attr("data-mData");
					sFormatType = $(this).attr("data-sFormatType");
					if(key == mData){
						if(key=="cashPool"){
							footData = resp[key].text;
						}
						else{

							footData = numeral(resp[key]).format('0,0.00');
							if(key=='recvAmt'){
								footData = '+'+footData;
							}else if(key=='payAmt'){
								footData = '-'+footData;
							}
						}
						$(this).text(footData);
						return false;
					}
				});
			});
		};
		
		//出入金汇总数据操作句柄
		getTableInfo().done(function(resp){
			summaryData = resp;
			formatFoot("#table-cash-pool",summaryData);
		});
		
		//收付金额
		var _fnRecv=function(data, type, extra){
			var payRecvFlg=extra.payRecvFlg.name;
			var data = payRecvFlg=="RECV"?numeral(extra.trxAmt).format('0,0.00'):"--";
			return '<span class="table-align-right" style="color:#49B21F">'+(data=="--"?data:("+"+data))+'</span>';
		};
		var _fnPay=function(data, type, extra){
			var payRecvFlg=extra.payRecvFlg.name;
			var data = payRecvFlg=="PAY"?numeral(extra.trxAmt).format('0,0.00'):"--";
			return '<span class="table-align-right" style="color:RED">'+(data=="--"?data:("-"+data))+'</span>';
		};
		//dataTable初始函数
		var initTable = function(){
			var options = {};
			options.tableId = '#table-cash-pool';
			options.sAjaxSource = global.context+"/web/fund/pool/trx/list";
			options.aaSorting=[[0,"desc"]];
			options.functionList={
				"_fnRecv":_fnRecv,
				"_fnPay":_fnPay
			};
			options.rawOptions={
				"fnDrawCallback ":formatFoot("#table-cash-pool",summaryData)
			};
			var tb=datatables.init(options);
			tb.setParams(util.getSearchData(".row-search"));
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
			table.setParams(util.getSearchData(".row-search"));
			table.rawTable.fnDraw();
			setSearch();
			getTableInfo().done(function(resp){
				summaryData = resp;
				formatFoot("#table-cash-pool",summaryData);
			});
		});
		
		//导出excel
		$('#export-excel').on('click',function(){
			if($(".sorting_desc").length > 0){
				$("#sort-column").val($(".sorting_desc").attr("data-mdata"));
				$("#sort-dir").val("desc");
			}
			else{
				$("#sort-column").val($(".sorting_asc").attr("data-mdata"));
				$("#sort-dir").val("asc");
			}
			$("#form-export").submit();
			return false;
		});
		
		var setSearch = function(){
			$("#acct-no").val($('#acctNo').val());
			$("#cash-pools").val($('#cash-pool').val());
			$("#use-types").val($('#use-type').val());
			$("#from-date").val($('#start-date').val());
			$("#to-date").val($('#end-date').val());
		};
		
		setSearch();
		
});