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
	var streamTable = null;
	
	var _bindDatePicker = function(){
		 packet.bindDatePicker();
	};
	
	var _rendarTable = function(){
		var options = {};
		options.tableId = '#streamTable';
		options.aaSorting = [[5,"desc"]];
		options.sAjaxSource = global.context+"/web/report/account/transaction/journal/search";
		streamTable = datatables.init(options);
		streamTable.setParams(util.getSearchData("#search-area"));
		return streamTable.create();
	};
	
	var bindEvent = function(){
		$("#stream-search-btn").on("click",function(){
			streamTable.setParams(util.getSearchData('#search-area'));
			streamTable.invoke("fnDraw");
			renderTableFoot();
		});
		
		$("#search-area").on("keydown",function(e){
			if(e.which == 13){
				$("#stream-search-btn").trigger("click");
				return false;
			};
		});
	};
	
	var init = function(){
		_bindDatePicker();
		_rendarTable();
		bindEvent();
		renderTableFoot();
	};
	
	var renderTableFoot = function(){
		var params = util.getSearchData('#search-area');
		var params_str = '{';
		$.each(params,function(i,val){
			if(i != 0 ){
				params_str += ',';
			}
			params_str += '"'+val.name+'"';
			params_str += ':"'+val.value+'"';
		});
		params_str += '}';
		
		$.ajax({
            type: "POST",
            url: streamTable.sAjaxSource+"_sum",
            data: params_str,
            dataType: "json",
            contentType: "application/json",
            success: function(data){
            	$(".receiveAmtSum").html(numeral(data['receiveAmtSum']).format('0,0.00'));
            	$(".payAmtSum").html(numeral(data['payAmtSum']).format('0,0.00'));
            }
        });
        
	}
	init();
	
	//导出excel
	$('#export-my-excel').on('click',function(){   
		$("#toex-keyword").val($('#keyword-q').val());
		$("#toex-useType").val($('#useType-q').val());
		$("#toex-agent").val($('#agent-q').val());
		$("#toex-agentName").val($('#agentName-q').val());  
		$("#toex-beginDate").val($('#search-start-date').val());
		$("#toex-endDate").val($('#search-end-date').val());   
		$("#form-export-excel").submit();
		return false;
	});
});