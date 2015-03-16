require(['jquery',
         'global',
         'module/util',
         'module/datatables',
         'module/view_pusher',
         'module/ajax',
         'bootstrap',
         'bootstrap-datepicker',
         'bootstrap-datepicker.zh-CN',
         'requirejs/domready!'], 
         function($,global,util,datatables,pusher){
	/**
	 * 变量
	 */
	//datatable操作列button
	function _fnDrawButton(data,type,full){
		var canApprove = full.canApprove;
		if(canApprove==true){
			return '<button class="btn btn-mf-primary refuse"  data-id="'+full.applNo+'"><i class="fa fa-reply"></i>申请否决</button>';
		}
	};
	function _fnbuyButton(nTd, sData, oData, iRow, iCol){
		var applNo = $(nTd).find(".refuse").attr("data-id");
		$(nTd).find('.refuse').on('click',function(){
			$(".btn-modal-ok").attr("data-id",applNo);
			$('#modal-refuse').modal({
				  show: true
				})
		});
	};
	
	$(".btn-modal-ok").on('click',function(){
		$("#modal-refuse").modal("hide");
		var data = {};
		data.appId = $(".btn-modal-ok").attr("data-id");
		data.comments = $(".reason").val();
		$.ajax({
			url : global.context+'/web/settlement/withddepapplapprove',
			type : 'post',
			contentType:'application/json;charset=utf-8',
			data:JSON.stringify(data),
			error:function(){
			},
			success : function(response) {
				batchtable.rawTable.fnDraw();
			}
		});
		
	})
	
	$('#search-start-date').datepicker({
        format: 'yyyy-mm-dd',
        weekStart: 1,
        autoclose: true,
        todayBtn: false,
        language: 'zh-CN',
    })
	//datatable操作句柄
	var batchtable = function(){
		var options = {};
		options.tableId = '#batch-table';
		options.sAjaxSource = global.context+"/web/settlement/withddepappllist";
		options.functionList = {"_fnDrawButton":_fnDrawButton,"_fnbuyButton":_fnbuyButton};
		var tb=datatables.init(options);
		tb.setParams(util.getSearchData('#search_area'));
		tb.create();
		return tb;
	}();
	
	
	//批量审核按钮
	$('#audit').on('click',function(){
		if($('#search-start-date').val()==''||$('#bank option:selected').val()==''){
			alert("请选择时间和开户银行");
		}else{
			$('.date').text($('#search-start-date').val());
			$('.bnkname').text($('#bank option:selected').text());
			$('#modal-audit').modal({
				  show: true
				})
		}
	});
	//导出excel
	$('#excel').on('click',function(){
		$("#form-searchDate").val($('#search-start-date').val());
		$("#form-bnkCd").val($('#bank option:selected').val());
		$("#export-form").submit();
		return false;
	});
	//确认按钮点击
	$(".btn-modal-agree").on("click",function(){
		$("#modal-audit").modal("hide");
		var data = {}
		data.searchDate = $('#search-start-date').val();
		data.bnkCd = $('#bank option:selected').val();
		$.ajax({
			url : global.context+'/web/settlement/withdrawalConfirm',
			type : 'post',
			contentType:'application/json;charset=utf-8',
			data:JSON.stringify(data),
			error:function(){
			},
			success : function(response) {
				batchtable.rawTable.fnDraw();
			}
		});
	});
	
	//搜索按钮点击
	$("#search").on("click",function(){
		if($('#search-start-date').val()==''||$('#bank option:selected').val()==''){
			alert("请选择日期和开户银行");
		}else{
			batchtable.setParams(util.getSearchData("#search_area"));
			batchtable.rawTable.fnDraw();
		}
	});
});
