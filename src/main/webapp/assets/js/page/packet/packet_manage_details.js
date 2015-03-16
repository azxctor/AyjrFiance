require(['jquery',
         'global',
         'module/datatables',
         'module/util',
         'module/view_pusher',
         'module/ajax',
         'bootstrap',
         'jquery.popupwindow',
         'requirejs/domready!',
         'underscore'], 
function($,global,datatables,util,pusher){
	console.log(111);
	var supply_table = null;
	var contract_table = null;
	var option={
			action:null,
			context:null,
			dataExtension:null,
	};
	var modal_content = {
			modal_header:null,
			modal_body:null,
			modal_footer:null,
			modal_data:null,
	};
	
	/**
	 * modal渲染
	 */
	var renderModal=function(modal_content){
		$("#package-modal-header").empty().html(modal_content.modal_header);
		$("#package-modal-body").empty().html(modal_content.modal_body);
		$("#package-modal-footer").empty().html(modal_content.modal_footer);
		$("#package-modal").modal(modal_content.modal_data);
	} ;
	
	/**
	 * 详情展开，闭合
	 */
	$("#panel-heading").click(function(){
		$("#collapse-more-detail").collapse('toggle');
		if($("#fa-icon").hasClass("fa-caret-down")){
			$("#fa-icon").removeClass("fa-caret-down").addClass("fa-caret-up");
		}
		else{
			$("#fa-icon").removeClass("fa-caret-up").addClass("fa-caret-down");
		}
	});
	
	
	/**
	 * 显示列表当前数据
	 */
	function loadLocalPage(){
		  pusher.pop();
		  $('#hidden_span').click();
	};
	
	/**
	 * ajax请求成功回调
	 */
	function ajaxCallBack(response,successTip){
		var _response = eval(response);
		if(_response.code == "SUCCESS"){
			var rein_flag = true;
			$("#package-modal").modal('hide')
			.on('hidden.bs.modal', function (e) {
				if(rein_flag){
					modal_content.modal_header = '提示';
					modal_content.modal_body = successTip;
					modal_content.modal_footer = '<button type="button" class="btn btn-mf-primary" id="prompt-btn" data-dismiss="modal">确定</button>';
					renderModal(modal_content);
					$('#prompt-btn').click(function(){
						rein_flag = false;
						loadLocalPage();
					});
				}
			});
		}
	}
	
	
	/**
	 * 返回列表页
	 */
	/*$('.btn-back-list').on('click',function(){
		$("#details-wrapper").hide("1000",function(){$('#main-wrapper').show();});
	});*/
	
	
	/**
	 * 申购人数及金额列表
	 */
	
	function getSupplyTable(){
		var options = {};
		options.tableId = '#supply-table';
		options.sAjaxSource = global.context+"/web/search/getsubscribes";
		options.aaSorting=[[3,"desc"]];
		supply_table = datatables.init(options);
		supply_table.setParams([{name:"packageId",value:""}]);
		return supply_table.create();
	};
	getSupplyTable();
	
	/**
	 *  合同模板列表
	 */
	
	var _fnShowContract = function(data){
        return '<a class="view-contract" data-productId="'+data.productId+'" data-lotid="'+data.lotId+'">'+data.contranctNo+'</a>';
	};
	
	function getContractTable(){
		var options = {};
		options.tableId = '#contract-table';
		options.sAjaxSource = global.context+"/web/package/getcontractlist";
		options.functionList = {"_fnShowContract" : _fnShowContract};
		options.aaSorting=[[4,"desc"]];
		contract_table = datatables.init(options);
		contract_table.setParams([{name:"packageId",value:""}]);
		return contract_table.create();
	}
	getContractTable();
	
	$("#product-name").on("click",function(){
		var productId = $("#product-id").text();
		  pusher.push({
			url: global.context+"/web/product/details/"+productId+"/true",
			type : 'GET',
			element:"#package-details",
			title:"融资项目详情",
			onShow:function(){
				$(".package-name-link").removeClass("link package-name-link");
			}
		});
	});
	
	/**
	 * modal显示，form提交
	 */
	//申购人数连接
	$("#supply-user-count").on("click",function(){
			//申购人数及金额列表获取
			var packageId = $(this).attr('data-id');
//			 getSupplyTable(packageId);
			supply_table.setParams([{name:"packageId",value:packageId}]);
			supply_table.invoke("fnDraw");
	});
	
	$('#contract-list-link').on('click',function(){
		var packageId = $(this).attr('data-id');
		$('#export-packageid').val(packageId);
		var userId = $(this).attr('data-userid');
		contract_table.setParams([{name:'packageId',value:packageId},{name:'userId',value:userId}]);
		contract_table.invoke("fnDraw");
	});
	
	$('#contract-table').on('click', '.view-contract', function(){
		var lot_id = $(this).attr('data-lotid');
		var productId = $(this).attr('data-productId');
		$.ajax({
			url:global.context + "/web/report/getcontract",
			dataType:"json",
            type:'POST',
            contentType:'application/json;charset=utf-8',
            data:JSON.stringify({"commonId":lot_id, "exportFileExt":"pdf","productId":productId})
		}).done(function(resp){
            $.popupWindow(_.unescape(resp.parameterString),{"width":"1024","height":"700"});
        });
	});
	
	//查看合同模板
	$("#contract-link").on('click',function(){
		var contractId = $(this).attr("data-contract");
		var productId= $(this).attr("data-productId");
    	//window.reportController.detail("contract.template",contractId);
		$.ajax({
			url:global.context + "/web/report/getcontracttemplate",
			dataType:"json",
            type:'POST',
            contentType:'application/json;charset=utf-8',
            data:JSON.stringify({"commonId":contractId, "exportFileExt":"pdf","productId":productId})
		}).done(function(resp){
            $.popupWindow(resp.parameterString,{"width":"1024","height":"700"});
        });
	});
	
	$('#contract-list').on('click', function(){
    	var lot_id = $(this).attr('data-contract');
    	var productId = $(this).attr('data-productId');
		$.ajax({
			url:global.context + "/web/report/getcontract",
			dataType:"json",
            type:'POST',
            contentType:'application/json;charset=utf-8',
            data:JSON.stringify({"commonId":lot_id, "productId":productId})
		}).done(function(resp){
            $.popupWindow(_.unescape(resp.parameterString),{"width":"1024","height":"700"});
        });
    });
	
	$(".operate-background").on("click","#order-cancellation",function(){//撤单
		modal_content.modal_header = '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>'
									+'<h4 class="modal-title" id="myModalLabel">撤单确认</h4>';
		modal_content.modal_body = '<label class="control-label required">撤单理由</label>'
									+'<textarea class="note-modal" name="comments" data-validate="{required:true}"></textarea>';
		modal_content.modal_footer = '<button type="submit" class="btn btn-mf-primary">确定</button>'
									+'<button type="button" class="btn btn-mf-default" data-dismiss="modal">取消</button>';
		renderModal(modal_content);
		
		$("#form-modal").validate({
			submitHandler : function(form) {
				var packageId = $("#package-id").text();
				form.action = global.context+"/web/financingpackage/"+packageId+"/abnormalwithdraw";
				util.ajax_submit(form,option).done(function(response){
					ajaxCallBack(response,'撤单成功，系统已对该融资包进行作废处理并扣除实际融资服务合同保证金！');
				});
			}
		});
	})
	.on("click","#terminate-purchase",function(){//终止申购
		modal_content.modal_header = '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>'
									+'<h4 class="modal-title" id="myModalLabel">确认终止</h4>';
		modal_content.modal_body = '是否确认终止当前申购？';
		modal_content.modal_footer = '<button type="submit" class="btn btn-mf-primary">确定</button>'
									+'<button type="button" class="btn btn-mf-default" data-dismiss="modal">取消</button>';
		renderModal(modal_content);
		
		$("#form-modal").validate({
			submitHandler : function(form) {
				var packageId = $("#package-id").text();
				form.action = global.context+"/web/financingpackage/"+packageId+"/stop ";
				util.ajax_submit(form,option).done(function(response){
					ajaxCallBack(response,'终止申购成功，融资包状态更新为“待签约”！');
				});
			}
		});
	})
	.on("click","#hand-repayment",function(){//手工还款
		var packageId = $("#package-id").text();
		$.ajax({
			url: global.context + "/web/financingpackage/payment/"+packageId+"/getmanualdetails",
			type: 'GET',
			contentType: 'application/json;charset=utf-8',
			error: function(){
			},
			success: function(response,data){
				var _response = $.parseJSON(response);
				modal_content.modal_header = '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>'
											+'<h4 class="modal-title" id="myModalLabel">手工还款确认</h4>';
				modal_content.modal_body = '是否确认对'+ packageId + '融资包的第' + _response.period +'期进行手工还款？<br/>还款总额为'+ util.get_thousand(_response.totalPayment) +'元，其中本金'+ util.get_thousand(_response.principalBalance) +'元，利息'+ util.get_thousand(_response.interest) +'元，费用'+ util.get_thousand(_response.feeAmt) +'元！';
				modal_content.modal_footer = '<button type="submit" class="btn btn-mf-primary">确定</button>'
											+'<button type="button" class="btn btn-mf-default" data-dismiss="modal">取消</button>';
				renderModal(modal_content);
				var period = _response.period;
				$("#package-id").data("period",period);
				$("#form-modal").validate({
					submitHandler : function(form) {
						packageId = $("#package-id").text();
						period = $("#package-id").data("period");
						form.action = global.context+"/web/financingpackage/"+packageId+"/manualpay/"+period ;
						util.ajax_submit(form,option).done(function(response){
							ajaxCallBack(response,'手工还款成功！');
						});
					}
				});
			}
		});
		
	})
	.on("click","#prepayment",function(){//提前还款
		var packageId = $("#package-id").text();
		$.ajax({
			url: global.context + "/web/financingpackage/payment/"+packageId+"/getprepaymentdetails",
			type: 'GET',
			contentType: 'application/json;charset=utf-8',
			error: function(){
			},
			success: function(response){
				var _response = $.parseJSON(response);
				modal_content.modal_header = '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>'
											+'<h4 class="modal-title" id="myModalLabel">提前还款确认</h4>';
				modal_content.modal_body =  '融资包 '+_response.packageId+' 提前还款，'
					                        +'还款总额为'+ util.get_thousand(_response.totalAmt)
				                            + '元，其中应还本金为'+ util.get_thousand(_response.totalPrincipaBalance) 
				                            +'元，应另付当月利息为'+ util.get_thousand(_response.totalInterestAmt) 
				                            +'元，应付当月费用为'+ util.get_thousand(_response.feeAmt) 
				                            + '元，提前还款违约金为'+util.get_thousand(_response.totalPenalty)
				                            +'元。<br/>点击【确定】系统将自动扣除还款额。';
				modal_content.modal_footer = '<button type="submit" class="btn btn-mf-primary">确定</button>'
											+'<button type="button" class="btn btn-mf-default" data-dismiss="modal">取消</button>';
				renderModal(modal_content);
					
				$("#form-modal").validate({
					submitHandler : function(form) {
						packageId = $("#package-id").text();
						form.action = global.context+"/web/financingpackage/"+packageId+"/prepayment";
						util.ajax_submit(form,option).done(function(response){
							ajaxCallBack(response,'提前还款成功,账户金额已扣除！');
						});
					}
				});
			}
		});
	})
	.on("click","#loan-approval",function(){//放款审批通过
		modal_content.modal_header = '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>'
									+'<h4 class="modal-title" id="myModalLabel">放款审批确认</h4>';
		modal_content.modal_body = '是否确认通过对该融资包放款审批？';
		modal_content.modal_footer = '<button type="submit" class="btn btn-mf-primary">通过</button>'
									+'<button type="button" class="btn btn-mf-default" data-dismiss="modal">取消</button>';
		renderModal(modal_content);
		
		$("#form-modal").validate({
			submitHandler : function(form) {
				var packageId = $("#package-id").text();
				form.action = global.context+"/web/financingpackage/"+packageId+"/loanapprove" ;
				util.ajax_submit(form,option).done(function(response){
					ajaxCallBack(response,'放款审批成功，该融资包状态更新为待放款！');
				});
			}
		});
	})
	.on("click","#loan-approval-confirm",function(){//放款
		modal_content.modal_header = '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>'
									+'<h4 class="modal-title" id="myModalLabel">放款确认</h4>';
		modal_content.modal_body = '是否确认对该融资包放款？';
		modal_content.modal_footer = '<button type="submit" class="btn btn-mf-primary">确认</button>'
									+'<button type="button" class="btn btn-mf-default" data-dismiss="modal">取消</button>';
		renderModal(modal_content);
		
		$("#form-modal").validate({
			submitHandler : function(form) {
				var packageId = $("#package-id").text();
				form.action = global.context+"/web/financingpackage/"+packageId+"/loanapproveconfirm" ;
				util.ajax_submit(form,option).done(function(response){
					ajaxCallBack(response,'放款成功，该融资包状态更新为已还款中！');
				});
			}
		});
	});
	
	
	
});