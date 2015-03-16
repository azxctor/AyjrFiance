require(['jquery',
         'global',
         'module/util',
         'module/datatables',
         'packet_manage',
         'module/ajax',
         'bootstrap',
         'bootstrap-datepicker',
         'bootstrap-datepicker.zh-CN',
         'requirejs/domready!'], 
function($, global, util,datatables,packet){
	var transFeeTable = null;
	var search_data = util.get_search_data("#transfer-fee-form");
	
	var _bindDatePicker = function(){
		 packet.bindDatePicker("yyyy-mm",1);
		 $(".search-date").attr("readonly",false);
	};
	
	var draw_total = function(){
		$.ajax({
			url:global.context+"/web/report/authzd/ctr/transfer/fee/search/total",
			type:"post",
			contentType:"application/json",
			data:JSON.stringify(search_data)
		}).done(function(resp){
			$("#total-trxAmt").text(resp);
		});
	};
	
	var _rendarTable = function(){
		var options = {};
		options.tableId = '#transferFeeTable';
		options.aaSorting = [[4,"desc"]];
		options.sAjaxSource = global.context+"/web/report/authzd/ctr/transfer/fee/search";
		transFeeTable = datatables.init(options);
		transFeeTable.rawOptions = {fnDrawCallback:function(){
			draw_total();
		}};
		transFeeTable.setParams(util.getSearchData("#search-area"));
		return transFeeTable.create();
	};
	
	var bindEvent = function(){
		$("#transFee-search-btn").on("click",function(){
			search_data = util.get_search_data("#transfer-fee-form");
			transFeeTable.setParams(util.getSearchData('#search-area'));
			transFeeTable.invoke("fnDraw");
		});
		
		$("#transfer-fee-form").on("keydown",function(e){
			if(e.which == 13){
				$("#transFee-search-btn").trigger("click");
				return false;
			};
		});
		
		$("#keyword-input").tooltip({
			placement:"bottom"
		});
	};
	
	var init = function(){
		_bindDatePicker();
		_rendarTable();
		bindEvent();
	};
	
	init();
});