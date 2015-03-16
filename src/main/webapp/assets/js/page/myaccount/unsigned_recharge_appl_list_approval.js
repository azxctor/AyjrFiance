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
		 */
		//当前操作的申请
		var currentApplNo="";
		
		//datatable审批按钮
		var _fnOperate=function(data, type, extra){
			var disabled=extra.apprBtnEnable?"":" disabled ";
			var applNo=extra.applNo;
			var canPrint=extra.printBtnEnable? "<button type='button' title='打印' class='btn btn-xs btn-mf-primary btn-print-set' data-applNo="+ extra.relBnkJnlNo +"><i class='fa fa-print fa-lg'></i></button>":"";
			return $.format("<div class='btn-group'><button type='button' title='通过' class='btn btn-xs btn-mf-primary btn-approve'{0}data-applNo={1}><i class='fa fa-check fa-lg'></i></button> <button type='button' title='否决' class='btn btn-xs btn-mf-default btn-refuse'{0}data-applNo={1}><i class='fa fa-times fa-lg'></i></button>"+ canPrint +"</div>",disabled,applNo);
		};
		//dataTable初始函数
		var initTable = function(){
			var options = {};
			options.tableId = '#withdraw-table';
			options.sAjaxSource = global.context+"/web/myaccount/get/unsigned/recharge/appl/list";
			options.aaSorting=[[0,"desc"]];
			options.functionList={
				"_fnOperate":_fnOperate
			};
			var tb=datatables.init(options);
			tb.setParams(util.getSearchData(".search-withdraw"));
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
			table.setParams(util.getSearchData(".search-withdraw"));
			table.rawTable.fnDraw();
		});
		
		//时间限制
		$("#start-date").on("changeDate",function(){
			$("#end-date").datepicker("setStartDate",$(this).val());
		});
		$("#end-date").on("changeDate",function(){
			$("#start-date").datepicker("setEndDate",$(this).val());
		});
		
		//操作按钮点击
		$("body").on("click",".btn-approve,.btn-refuse",function(e){
			var $target=$(e.currentTarget);
			currentApplNo=$target.attr("data-applno");
			if(!currentApplNo){
				return false;
			}
			if($target.hasClass("btn-approve")){
				$("#modal-approve").modal().show();
			}
			else if($target.hasClass("btn-refuse")){
				$("#modal-refuse").modal().show();
			}
		});
		
		$("body").on("click",".btn-modal-agree",function(e){
			var $modal=$(e.currentTarget).parents(".modal");
			if($modal.attr("id")=="modal-approve"){
				$.ajax({
					url:global.context+"/web/myaccount/recharge/appl/approve",
					type: "POST",
					dataType: 'json',
					contentType: 'application/json; charset=UTF-8',
					data:JSON.stringify({passed:true,appId:currentApplNo})
				}).done(function(){
					$modal.data("bs.modal").hide();
					table.setParams(util.getSearchData(".search-withdraw"));
					table.rawTable.fnDraw();
				});
			}
			else if($modal.attr("id")=="modal-refuse"){
				if(!$("#form-reason").validate().form()){
					return false;
				}
				$.ajax({
					url:global.context+"/web/myaccount/recharge/appl/approve",
					type: "POST",
					dataType: 'json',
					contentType: 'application/json; charset=UTF-8',
					data:JSON.stringify({passed:false,comments:$modal.find("#reason").val(),appId:currentApplNo})
				}).done(function(){
					$modal.data("bs.modal").hide();
					table.setParams(util.getSearchData(".search-withdraw"));
					table.rawTable.fnDraw();
				});
			}
			else if($modal.attr("id")=="modal-print"){
				$modal.data("bs.modal").hide();
				$modal.find("#print-content").printArea({popClose:true});  
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