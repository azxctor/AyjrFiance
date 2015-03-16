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
		//汇总数据
		summaryData="";
		var roleType = $("#role-type").text();
		var option = {};
		
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
		} ;
		//当前行
		var $currentRow=null;
		//当前操作的申请
		var currentApplNo="";
		
		//datatable扣款确认按钮
		var _fnOperateConfirm=function(data, type, extra){
			var disabled=extra.canConfirm?"":" disabled ";
			var applNo=extra.canConfirm?extra.applNo:"";
			var printNo=extra.dealStatus.name == "SUCCED"? extra.relBnkJnlNo:extra.applNo;
			var printType=extra.dealStatus.name == "SUCCED"? ' jnlNo':' applNo';
			var canPrint="<button type='button' title='打印' class='btn btn-xs btn-mf-primary btn-print-get"+ printType +"' data-applNo="+ printNo +"><i class='fa fa-print fa-lg'></i></button>";
			return $.format("<div class='btn-group'><button type='button' title='确认' class='btn btn-xs btn-mf-primary btn-pay'{0}data-applNo={1}><i class='fa fa-check fa-lg'></i></button> <button type='button' title='驳回' class='btn btn-xs btn-mf-default btn-dismiss'{0}data-applNo={1}><i class='fa fa-times fa-lg'></i></button>" + canPrint + "</div>",disabled,applNo);
		};
		//datatable审批按钮
		var _fnOperateApprove=function(data, type, extra){
			var disabled=extra.canApprove?"":" disabled ";
			var applNo=extra.canApprove?extra.applNo:"";
			return $.format("<div class='btn-group'><button type='button' title='通过' class='btn btn-xs btn-mf-primary btn-approve'{0}data-applNo={1}>&nbsp;<i class='fa fa-check fa-lg'></i>&nbsp;</button> <button type='button' title='拒绝' class='btn btn-xs btn-mf-default btn-refuse'{0}data-applNo={1}>&nbsp;<i class='fa fa-times fa-lg'></i>&nbsp;</button></div>",disabled,applNo,disabled,applNo);
		};
		
		//获取列表出入金汇总数据
		var getTableInfo = function(){
			var status = roleType == "01"?"dealStatus":"applStatus";
			var data = {"fromDate":$("#start-date").val(),"toDate":$("#end-date").val(),"cashPool":$("#cashPool").val()};
			data[status] = $("#status").val();
			return $.ajax({
				url:global.context+"/web/myaccount/withddepapplsumamt",
				type: "POST",
				dataType: 'json',
				contentType: 'application/json; charset=UTF-8',
				data:JSON.stringify(data)
			});
		};
		
		//出入金汇总数据初始函数
		var formatFoot = function(tableId,resp){
			$.each(resp,function(key){
				$(tableId).find("tfoot td").each(function(){
					mData = $(this).attr("data-mData");
					if(key == mData){
						footData = numeral(resp[key]).format('0,0.00');
						$(this).text(footData);
						return false;
					}
				});
			});
		};
		
		//出入金汇总数据操作句柄
		if(roleType == "01" || roleType == "02"){
			getTableInfo().done(function(resp){
				summaryData = resp;
				formatFoot("#withdraw-table",summaryData);
			});
		}
		
		//dataTable初始函数
		var initTable = function(){
			var options = {};
			options.tableId = '#withdraw-table';
			var isExchangeWithdrawalAppl = $('#is-exchange-withdrawalAppl').val();
			if(isExchangeWithdrawalAppl == "true"){
				options.sAjaxSource = global.context+"/web/myaccount/platformwithddepappllist";
			} else {
				options.sAjaxSource = global.context+"/web/myaccount/withddepappllist";
			}
			options.aaSorting=[[0,"desc"]];
			options.functionList={
				"_fnOperateConfirm":_fnOperateConfirm,
				"_fnOperateApprove":_fnOperateApprove
			};
			if(roleType == "01" || roleType == "02"){
				options.rawOptions={
					"fnDrawCallback ":formatFoot("#withdraw-table",summaryData)
				};
			}
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
			if(roleType == "01" || roleType == "02"){
				getTableInfo().done(function(resp){
					summaryData = resp;
					formatFoot("#withdraw-table",summaryData);
				});
			}
		});
		
		//批量审核按钮
		$('#audit').on('click',function(){
			modal_content.modal_header =  '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>'
										+'<h4 class="modal-title" id="myModalLabel">工行批量审核</h4>';
			modal_content.modal_body = '确定对工行进行批量审核？';
			modal_content.modal_footer = '<button type="button" class="btn btn-mf-primary btn-modal-agree" id="modal-audit" data-dismiss="modal">确定</button>'
										+'<button type="button" class="btn btn-mf-default" data-dismiss="modal">取消</button>';
			renderModal(modal_content);
		});
		
		//批量确认按钮
		$('#batch-confirm').on('click',function(){
			modal_content.modal_header = '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>'
										+'<h4 class="modal-title" id="myModalLabel">工行批量确认</h4>';
			modal_content.modal_body = '确定对工行进行批量确认？';
			modal_content.modal_footer = '<button type="button" class="btn btn-mf-primary btn-modal-agree" id="modal-batch-confirm" data-dismiss="modal">确定</button>'
										+'<button type="button" class="btn btn-mf-default" data-dismiss="modal">取消</button>';
			
			renderModal(modal_content);
		});
		
		//导出excel
		$('#excel').on('click',function(){
				$("#form-searchDate").val($('#start-date').val());
				$("#to-searchDate").val($('#end-date').val());
				$("#form-bnkCd").val('ICBC');
				$("#export-form").submit();
				return false;
		});
		
		//导出excel2
		$('#excel2').on('click',function(){ 
			$("#form-searchDate2").val($('#start-date').val());
			$("#to-searchDate2").val($('#end-date').val());
			$("#form-cashPool2").val($('#cashPool').val());  
			$("#form-status2").val($('#status').val()); 
			$("#export-form2").submit();
			return false;
		});
		
		//时间限制
		$("#start-date").on("changeDate",function(){
			$("#end-date").datepicker("setStartDate",$(this).val());
		});
		$("#end-date").on("changeDate",function(){
			$("#start-date").datepicker("setEndDate",$(this).val());
		});
		
		//操作按钮点击
		$("body").on("click.widthdraw",".btn-pay,.btn-approve,.btn-refuse,.btn-dismiss",function(e){
			var $target=$(e.currentTarget);
			//如果不允许点击则返回
			if($target.is("disabled")||!$target.attr("data-applno")){
				return false;
			}
			
			$currentRow=$target.parents("tr");
			currentApplNo=$target.attr("data-applno");
			if(!currentApplNo){
				return false;
			}
			if($target.hasClass("btn-pay")){
				$("#modal-pay").find("#acctNo").text($currentRow.find("td:eq(1)").text());
				$("#modal-pay").find("#trxAmt").text($currentRow.find("td:eq(2)").text());
				$("#modal-pay").modal().show();
			}
			else if($target.hasClass("btn-approve")){
				$("#modal-approve").modal().show();
			}
			else if($target.hasClass("btn-refuse")){
				$("#modal-refuse").modal().show();
			}
			else if($target.hasClass("btn-dismiss")){
				$("#modal-dismiss").modal().show();
			}
		});
		
		$("body").on("click.widthdraw",".btn-modal-agree",function(e){
			var $modal=$(e.currentTarget).parents(".modal");
			if($modal.attr("id")=="modal-pay"){
				$.ajax({
					url:global.context+"/web/myaccount/withdrawalafterconfirm",
					type: "POST",
					dataType: 'json',
					contentType: 'application/json; charset=UTF-8',
					data:JSON.stringify({applId:currentApplNo})
				}).done(function(){
					$modal.data("bs.modal").hide();
					table.setParams(util.getSearchData(".search-withdraw"));
					table.rawTable.fnDraw();
					getTableInfo().done(function(resp){
						summaryData = resp;
						formatFoot("#withdraw-table",summaryData);
					});
				});
			}
			else if($modal.attr("id")=="modal-approve"){
				$.ajax({
					url:global.context+"/web/myaccount/withddepapplapprove",
					type: "POST",
					dataType: 'json',
					contentType: 'application/json; charset=UTF-8',
					data:JSON.stringify({passed:true,appId:currentApplNo})
				}).done(function(){
					$modal.data("bs.modal").hide();
					table.setParams(util.getSearchData(".search-withdraw"));
					table.rawTable.fnDraw();
					getTableInfo().done(function(resp){
						summaryData = resp;
						formatFoot("#withdraw-table",summaryData);
					});
				});
			}
			else if($modal.attr("id")=="modal-refuse"){
				if(!$("#form-reason").validate().form()){
					return false;
				}
				$.ajax({
					url:global.context+"/web/myaccount/withddepapplapprove",
					type: "POST",
					dataType: 'json',
					contentType: 'application/json; charset=UTF-8',
					data:JSON.stringify({passed:false,comments:$modal.find("#reason").val(),appId:currentApplNo})
				}).done(function(){
					$modal.data("bs.modal").hide();
					table.setParams(util.getSearchData(".search-withdraw"));
					table.rawTable.fnDraw();
					getTableInfo().done(function(resp){
						summaryData = resp;
						formatFoot("#withdraw-table",summaryData);
					});
				}).always(function(){
					$modal.find("#reason").val("");
				});
			}
			else if($modal.attr("id")=="modal-print"){
				$modal.data("bs.modal").hide();
				$modal.find("#print-content").printArea({popClose:true});  
			}
			else if($(e.currentTarget).attr("id")=="modal-audit"){
				var data = {};
				data.fromDate = $('#start-date').val();
				data.endDate = $('#end-date').val();
				data.bnkCd = 'ICBC';
				$.ajax({
					url : global.context+'/web/settlement/withdrawalApprove',
					type : 'post',
					contentType:'application/json;charset=utf-8',
					data:JSON.stringify(data),
				}).done(function(){
					table.setParams(util.getSearchData(".search-withdraw"));
					table.rawTable.fnDraw();
					getTableInfo().done(function(resp){
						summaryData = resp;
						formatFoot("#withdraw-table",summaryData);
					});
				});
			}
			else if($(e.currentTarget).attr("id")=="modal-batch-confirm"){
				var data = {};
				data.fromDate = $('#start-date').val();
				data.endDate = $('#end-date').val();
				data.bnkCd = 'ICBC';
				$.ajax({
					url : global.context+'/web/settlement/withdrawalConfirm',
					type : 'post',
					contentType:'application/json;charset=utf-8',
					data:JSON.stringify(data),
				}).done(function(){
					table.setParams(util.getSearchData(".search-withdraw"));
					table.rawTable.fnDraw();
					getTableInfo().done(function(resp){
						summaryData = resp;
						formatFoot("#withdraw-table",summaryData);
					});
				});
			}
			else if($modal.attr("id")=="modal-dismiss"){
				if(!$("#form-reason").validate().form()){
					return false;
				}
				$.ajax({
					url:global.context+"/web/myaccount/withdrawalreject",
					type: "POST",
					dataType: 'json',
					contentType: 'application/json; charset=UTF-8',
					data:JSON.stringify({memo:$modal.find("#reason").val(),applId:currentApplNo})
				}).done(function(){
					$modal.data("bs.modal").hide();
					table.setParams(util.getSearchData(".search-withdraw"));
					table.rawTable.fnDraw();
					getTableInfo().done(function(resp){
						summaryData = resp;
						formatFoot("#withdraw-table",summaryData);
					});
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