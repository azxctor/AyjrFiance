require(['jquery',
         'global',
         'module/util',
         'form',
         'module/datatables',
         'module/ajax',
         'bootstrap',
         'jquery.printarea',
         'requirejs/domready!'],function($,global,util,Form,datatables){
	
	var applNos = new Array();
	
	//datatable审批按钮
	var _fnOperate=function(data, type, extra){
		var disabled=extra.applStatus.name=="WAIT_APPROVAL"?"":" disabled ";
		var applNo=extra.applNo;
		var canPrint="<button type='button' title='打印' class='btn btn-xs btn-mf-primary btn-print-transfer' data-applNo="+ applNo +"><i class='fa fa-print fa-lg'></i></button>";
		return $.format("<div class='btn-group'><button type='button' title='通过' class='btn btn-xs btn-mf-primary btn-approve'{0}data-applNo={1}><i class='fa fa-check fa-lg'></i></button> <button type='button' title='否决' class='btn btn-xs btn-mf-default btn-refuse'{0}data-applNo={1}><i class='fa fa-times fa-lg'></i></button>"+ canPrint +"</div>",disabled,applNo);
	};
	//选择按钮
	var _fnSelect=function(data,type,extra){
		var disabled=extra.applStatus.name=="WAIT_APPROVAL"?' class="approve-check" ':' disabled ';
		var applNo=extra.applNo;
		return $.format('<div class="checkbox"><label><input type="checkbox"{0}value={1}></label></div>',disabled,applNo);
	};
	
	var _fnEllipsisTre=function(data, type, extra){
		var dealMemo=extra.trxMemo;
		if(dealMemo != null){
			var dealMemoWhow = dealMemo.length > 10?'<span class="link" data-toggle="tooltip" data-dealMemo='+dealMemo+'>'+ dealMemo.substring(0,11)+'...'+'</span>':dealMemo;
			return dealMemoWhow;
		}
	};
	var _fnEllipsisDeal=function(data, type, extra){
		var dealMemo=extra.dealMemo;
		if(dealMemo != null){
			var dealMemoWhow = dealMemo.length > 10?'<span class="link" data-toggle="tooltip" data-dealMemo='+dealMemo+'>'+ dealMemo.substring(0,11)+'...'+'</span>':dealMemo;
			return dealMemoWhow;
		}
	};
	//dataTable初始函数
	var initTable = function(){
		var options = {};
		options.tableId = '#table-result';
		options.sAjaxSource = global.context+"/web/myaccount/getTransferAppls";
		options.aaSorting=[[0,"desc"]];
		options.functionList={
			"_fnOperate":_fnOperate,
			"_fnSelect":_fnSelect,
			"_fnEllipsisTre":_fnEllipsisTre,
			"_fnEllipsisDeal":_fnEllipsisDeal
		};
		var tb=datatables.init(options);
		tb.setParams(util.getSearchData(".row-search"));
		tb.create();
		return tb;
	};
	//datatable操作句柄
	var table=initTable();
	
	/*
	 * 事件
	 */
	$("#btn-search").on("click",function(){
		table.setParams(util.getSearchData(".row-search"));
		table.rawTable.fnDraw();
	});
	
	//时间限制
	$("#start-date").on("changeDate",function(){
		$("#end-date").datepicker("setStartDate",$(this).val());
	});
	$("#end-date").on("changeDate",function(){
		$("#start-date").datepicker("setEndDate",$(this).val());
	});
	
	//全选、全不选
	$("body").on("click",".approve-check-all",function(){
		$("#table-result").find(".approve-check").prop("checked",$(".approve-check-all").is(":checked"));
	});
	
	//全选按钮是否选中
	$("body").on("click",".approve-check",function(){
		var self=$(this);
		if(!self.is(":checked")){
			$("#table-result").find(".approve-check-all").prop("checked",false);
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
			container: 'body',
			content:$target.attr("data-dealMemo")
		});
	});
	
	//判断批量审批是否可用
	$("body").on("click",".approve-check-all,.approve-check",function(){
		$("#table-result").find(".approve-check").each(function(index){
			if($(this).is(":checked")){
				$(".btn-do-all").attr("disabled",false);
				return false;
			}
			$(".btn-do-all").attr("disabled",true);
		});
	});
	
	//操作按钮点击
	$("body").on("click",".btn-approve,.btn-refuse,.btn-approve-all,.btn-refuse-all",function(e){
		var $target=$(e.currentTarget);
		applNos = [];
		if($target.hasClass("btn-approve")){
			var currentApplNo=$target.attr("data-applno");
			applNos.push(currentApplNo);
			$("#modal-approve").modal().show();
		}
		else if($target.hasClass("btn-refuse")){
			var currentApplNo=$target.attr("data-applno");
			applNos.push(currentApplNo);
			$("#modal-refuse").modal().show();
		}
		else if($target.hasClass("btn-approve-all")){
			$("table").find(".approve-check").each(function(index){
				if($(this).is(":checked")){
					applNos.push($(this).val());
				}
			});
			$("#modal-approve").modal().show();
		}
		else if($target.hasClass("btn-refuse-all")){
			$("table").find(".approve-check").each(function(index){
				if($(this).is(":checked")){
					applNos.push($(this).val());
				}
			});
			$("#modal-refuse").modal().show();
		}
	});
	
	$("body").on("click",".btn-modal-agree",function(e){
		var $modal=$(e.currentTarget).parents(".modal");
		if($modal.attr("id")=="modal-approve"){
			$.ajax({
				url:global.context+"/web/myaccount/fundtransferapplApprove",
				type: "POST",
				dataType: 'json',
				contentType: 'application/json; charset=UTF-8',
				data:JSON.stringify({applStatus:"APPROVED",applNos:applNos})
			}).done(function(){
				$modal.data("bs.modal").hide();
				table.setParams(util.getSearchData(".row-search"));
				table.rawTable.fnDraw();
			});
		}
		else if($modal.attr("id")=="modal-refuse"){
			if(!$("#form-reason").validate().form()){
				return false;
			}
			$.ajax({
				url:global.context+"/web/myaccount/fundtransferapplApprove",
				type: "POST",
				dataType: 'json',
				contentType: 'application/json; charset=UTF-8',
				data:JSON.stringify({applStatus:"REJECT",dealMemo:$modal.find("#reason").val(),applNos:applNos})
			}).done(function(){
				$modal.data("bs.modal").hide();
				table.setParams(util.getSearchData(".row-search"));
				table.rawTable.fnDraw();
			});
		}
		else if($modal.attr("id")=="modal-transfer-print"){
			$modal.data("bs.modal").hide();
			$modal.find("#print-transfer-content").printArea({popClose:true});  
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
	
});