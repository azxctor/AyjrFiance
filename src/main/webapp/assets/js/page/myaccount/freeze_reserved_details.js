require(['jquery',
         'global',
         'module/util',
         'form',
         'module/datatables',
         'module/ajax',
         'bootstrap',
         'jquery.printarea',
         'requirejs/domready!'],function($,global,util,Form,datatables){
		
		//冻结保留明细总数据
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
		
		var isPlat=$("#is-plat").text();
		
		//获取列表冻结保留明细总数据
		var getTableInfo = function(){
			return $.ajax({
				url:global.context+"/web/fund/getFreezeSumAmt",
				type: "POST",
				dataType: 'json',
				contentType: 'application/json; charset=UTF-8',
				data:JSON.stringify(
						isPlat == "true"? {
								 "fromDate":$("#start-date").val(),
								 "toDate":$("#end-date").val(),
								 "fundUseType":$("#fund-use-type").val(),
								 "acctNo":$("#acctNo").val(),
								 "name":$("#name").val(),
								 "status":$("#fnr-status").val()
								}:{
								  "fromDate":$("#start-date").val(),
								  "toDate":$("#end-date").val(),
								  "fundUseType":$("#fund-use-type").val()
								}
						)
			});
		};
		
		//冻结保留明细总数据初始函数
		var formatFoot = function(tableId,resp){
			$.each(resp,function(key){
				$(tableId).find("tfoot td").each(function(){
					mData = $(this).attr("data-mData");
					sFormatType = $(this).attr("data-sFormatType");
					if(key == mData){
						if(key=='count'){
							footData = resp[key];
						}else{
							footData = numeral(resp[key]).format('0,0.00');
						}
						$(this).text(footData);
						return false;
					}
				});
			});
		};
		
		//冻结保留明细总数据操作句柄
		if(isPlat == "true"){
			getTableInfo().done(function(resp){
				summaryData = resp;
				formatFoot("#freeze-reserved-table",summaryData);
			});
		}
		
		//生效、失效日期显示
		var _fnExpireDt=function(data,type,extra){
			var expireDt=extra.expireDt=="9999-12-31"?' -- ':extra.expireDt;
			return expireDt;
		};
		//dataTable初始函数
		var initTable = function(tableId,url){
			var options = {};
			options.tableId = tableId;
			options.sAjaxSource = global.context+"/web/"+url;
			options.aaSorting=[[0,"desc"]];
			options.functionList={
					"_fnExpireDt":_fnExpireDt
			};
			if(isPlat == "true"){
				options.rawOptions={
						"fnDrawCallback ":formatFoot(tableId,summaryData)
				};
			}
			var tb=datatables.init(options);
			tb.setParams(util.getSearchData(".search-freeze-reserved"));
			tb.create();
			return tb;
		};
		
		relUrl= isPlat == "true"?"fund/getPlatformFundFreezeReverseDetails":"fund/getUserFundFreezeReverseDetails";
		//datatable操作句柄
		var freezeReservedTable = initTable("#freeze-reserved-table",relUrl);
		
		/**
		 * 事件
		 */
		//搜索
		$("#btn-search").on("click",function(){
			freezeReservedTable.setParams(util.getSearchData(".search-freeze-reserved"));
			freezeReservedTable.rawTable.fnDraw();
			if(isPlat == "true"){
				getTableInfo().done(function(resp){
					summaryData = resp;
					formatFoot("#freeze-reserved-table",summaryData);
				});
			}
		});
		
		//表单控件初始化
		Form.init();
});