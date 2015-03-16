require(['jquery',
         'global',
         'module/util',
         'module/datatables',
         'module/form',
         'bootstrap',
         'bootstrap-datepicker',
         'select2',
         'jquery.validate.methods',
         'plugins',
         'jquery.fileupload',
         'requirejs/domready!'], 
function($, global, util, datatables, form){
	//datepicker init
	$('.from-time, .to-time').datepicker({
        format: 'yyyy-mm-dd',
        weekStart: 1,
        autoclose: true,
        todayBtn: false,
        language: 'zh-CN',
    }).on('changeDate', function(e){
    	$('.to-time').datepicker({ autoclose: true}).datepicker('setStartDate', e.date).focus();
    });
	//datatables init
	var product_table = null;
	function productlistTable(){
		var options = {};
		options.tableId = '#table-search-result';
		options.sAjaxSource = global.context+"/web/signinstatus/search";
		options.aaSorting=[[0,"desc"],[1,"desc"]];
		options.functionList={};
		product_table = datatables.init(options);
		return product_table.create();
	};
	var productList = productlistTable();

	$("#btn-trading").click(function(){
		var status=$('#current_status').val();
		var url="";
		if(status=="C"){
			url=global.context+"/web/signinstatus/save/O";
		}else{
			url=global.context+"/web/signinstatus/save/C";
		}
		$.ajax({
			url: url,
			type: 'POST',
			dataType: 'json',
			contentType: 'application/json;charset=utf-8',
			error: function(){
			},
			success: function(response){
				if(response.code == "ACK")
				{
					if(status=="C"){
						$("#btn-trading").html("闭市");
						$("#current_market_status").html("开市");
						$('#current_status').val("O");
					}else if(status=="O"){
						$('#current_status').val("C");
						$("#btn-trading").html("开市");
						$("#current_market_status").html("闭市");
					}
					product_table.invoke("fnDraw");
				}
			}
		});			
	});
});
