require(['jquery',
         'global',
         'module/util',
         'module/view_pusher',
         'module/ajax',
         'bootstrap',
         'jquery.validate.methods',
         'select2',
         'requirejs/domready!'], 
function($,global,util,pusher){
	/**
	 * --------变量---------
	 */
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
	//ESC键 返回列表页
	$(document).keyup(function(event){
        if(event.keyCode == 27){
        	$('#prompt-btn').click();
        }
    });
	/**
	 * modal渲染
	 */
	var renderModal=function(modal_content){
		$("#project-modal-header").empty().html(modal_content.modal_header);
		$("#project-modal-body").empty().html(modal_content.modal_body);
		$("#project-modal-footer").empty().html(modal_content.modal_footer);
		$("#project-modal").modal(modal_content.modal_data);
		$("#project-modal").on('hidden.bs.modal', function (e) {
			$("#project-modal-footer").unbind("click");
		});
	} ;
	
	$(".modal").on("hidden.bs.modal",function(){
		$(this).find("form").each(function(){
			this.reset();
		});
	});
	
	/**
	 * 显示列表当前数据
	 */
	function loadLocalPage(){
		  $('#table-search-result').dataTable().fnPageChange(0,true); 
		  pusher.pop();
		/*$('#finance-details').hide("1000",function(){
		      $('#finance-product').show("1000");
		      var page = $('#table-search-result').dataTable().fnPagingInfo().iPage;
		      $('#table-search-result').dataTable().fnPageChange(page,true);
		});*/
	};
	
	
	/**
	 * 保证金验证
	 */
	var freeze_validator = $( "#freeze-form" ).validate();

	/**
	 * “取消”按钮返回列表页
	 */
	$('.btn-back-product').on('click',function(){
		$("#finance-details").hide("1000",function(){$('#finance-product').show();});
	});
	
	
	/**
	 * modal显示，form提交
	 */
	$(".operate-background").on("click","#btn-accept",function(){//审核通过
		$("#approve-accept-modal").modal("show");
		
		//合同模板获取
		$.ajax({
			url: global.context+"/web/product/getTemplateList",
			type: 'GET',
			contentType: 'application/json;charset=utf-8',
			error: function(){
			},
			success: function(response,data){
				var options = eval(response);
				$("#contract-template").empty();
				for(var option in options){
					$("#contract-template").append("<option value=" + options[option].templateId+">"+ options[option].templateName + "</option>");
				}
			}
		});
		
		$("#form-modal-approve-accept").validate({
			submitHandler : function(form) {
				var passed = true;
				var productId = $("#product-id").text();
				option.dataExtension = {"passed":passed,"productId":productId};
				form.action = global.context+"/web/product/audit";
				util.ajax_submit(form,option).done(function(response){
					var _response = eval(response);
					if(_response.code == "SUCCESS"){
						rein_flag = true;
						$("#approve-accept-modal").modal('hide')
						.on('hidden.bs.modal', function (e) {
							if(rein_flag){
								modal_content.modal_header = '提示';
								modal_content.modal_body = '审核成功，融资项目状态更新为“待评级”！';
								modal_content.modal_footer = '<button type="button" class="btn btn-mf-primary" id="prompt-btn" data-dismiss="modal">确定</button>';
								renderModal(modal_content);
								$('#prompt-btn').click(function(){
									rein_flag = false;
									loadLocalPage();
								});
							}
						});
					}
				});
			}
		});
	})
	.on("click","#btn-reject",function(){//审核驳回
		modal_content.modal_header = '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>'
									+'<h4 class="modal-title" id="myModalLabel">审批驳回确认</h4>';
		modal_content.modal_body =  '<label for="note-reject" class="control-label required">备注</label>'
									+'<textarea class="form-control" id="note-reject" name="comments" data-validate="{required:true}"></textarea>';
		modal_content.modal_footer = '<button type="submit" class="btn btn-mf-primary" id="btn-modal-reject">确定</button>'
									+'<button type="button" class="btn btn-mf-default" data-dismiss="modal">取消</button>';
		renderModal(modal_content);
		
		$("#project-modal-footer").on("click","#btn-modal-reject",function(){
			$("#form-modal").validate({
				submitHandler : function(form) {
					var passed = false;
					var productId = $("#product-id").text();
					option.dataExtension = {"passed":passed,"productId":productId};
					form.action = global.context+"/web/product/audit";
					util.ajax_submit(form,option).done(function(response){
						var _response = eval(response);
						if(_response.code == "SUCCESS"){
							rein_flag = true;
							$("#project-modal").modal('hide')
							.on('hidden.bs.modal', function (e) {
								if(rein_flag){
									modal_content.modal_header = '提示';
									modal_content.modal_body = '审核拒绝，融资项目状态更新为“待提交”！';
									modal_content.modal_footer = '<button type="button" class="btn btn-mf-primary" id="prompt-btn" data-dismiss="modal">确定</button>';
									renderModal(modal_content);
									$('#prompt-btn').click(function(){
										rein_flag = false;
										loadLocalPage();
									});
								}
							});
						}
					});
				}
			});
		});
	})
	.on("click",".btn-status-back",function(){//退回
		var next_status = null;
		var status = $("#product-status").text();
		switch(status){
			case "WAITTOEVALUATE":
				next_status = "项目审核";
				break;
			case "WAITTOFREEZE":
				next_status = "风险评级";
				break;
			case "WAITTOPUBLISH":
				next_status = "保证金冻结";
				break;
			default:
				break;
		}
		modal_content.modal_header = '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>'
									+'<h4 class="modal-title" id="myModalLabel">退回确认</h4>';
		modal_content.modal_body = '<label class="control-label required">退回理由 (点击【确定】按钮将退回到“'+ next_status +'”步骤)</label>'
									+'<textarea id="note-retreat" name="comment" data-validate="{required:true}"></textarea>';
		modal_content.modal_footer = '<button type="submit" class="btn btn-mf-primary" id="btn-modal-back">确定</button>'
									+'<button type="button" class="btn btn-mf-default" data-dismiss="modal">取消</button>';
		renderModal(modal_content);
		
		$("#project-modal-footer").on("click","#btn-modal-back",function(){
			$("#form-modal").validate({
				submitHandler : function(form) {
					var productId = $("#product-id").text();
					option.dataExtension = {"status":status,"productId":productId};
					option.action = global.context+"/web/product/retreat";
					util.ajax_submit(form,option).done(function(response){
						var _response = eval(response);
						if(_response.code == "SUCCESS"){
							rein_flag = true;
							$("#project-modal").modal('hide')
							.on('hidden.bs.modal', function (e) {
								if(rein_flag){
									modal_content.modal_header = '提示';
									modal_content.modal_body = '退回成功，融资项目状态更新为“'+ _response.data.text +'”！';
									modal_content.modal_footer = '<button type="button" class="btn btn-mf-primary" id="prompt-btn" data-dismiss="modal">确定</button>';
									renderModal(modal_content);
									$('#prompt-btn').click(function(){
										rein_flag = false;
										loadLocalPage();
									});
								}
							});
						}
					});
				}
			});
		});
	})
	.on("click","#order-cancellation",function(){//撤单
		modal_content.modal_header = '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>'
									+'<h4 class="modal-title" id="myModalLabel">撤单确认</h4>';
		modal_content.modal_body = '<label class="control-label required">撤单理由</label>'
									+'<textarea id="note-retreat" name="comment" data-validate="{required:true}"></textarea>';
		modal_content.modal_footer = '<button type="submit" class="btn btn-mf-primary">确定</button>'
									+'<button type="button" class="btn btn-mf-default" data-dismiss="modal">取消</button>';
		renderModal(modal_content);
		
		$("#form-modal").validate({
			submitHandler : function(form) {
				var productId = $("#product-id").text();
				option.dataExtension = {"productId":productId};
				form.action = global.context+"/web/product/revoke";
				util.ajax_submit(form,option).done(function(response){
					var _response = eval(response);
					if(_response.code == "SUCCESS"){
						rein_flag = true;
						$("#project-modal").modal('hide')
						.on('hidden.bs.modal', function (e) {
							if(rein_flag){
								modal_content.modal_header = '提示';
								modal_content.modal_body = '撤单成功，该融资项目废弃！';
								modal_content.modal_footer = '<button type="button" class="btn btn-mf-primary" id="prompt-btn" data-dismiss="modal">确定</button>';
								renderModal(modal_content);
								$('#prompt-btn').click(function(){
									rein_flag = false;
									loadLocalPage();
								});
							}
						});
					}
				});
			}
		});
	})
	.on("click","#btn-freeze",function(){//保证金冻结
		var frozenFee_margin_result = true;
		var warrant_margin_result = true;
		//校验
		if($("#real-frozenFee-margin").length>0 && $("#real-warrant-margin").length>0){
			frozenFee_margin_result = freeze_validator.element( "#real-frozenFee-margin" );
			warrant_margin_result = freeze_validator.element( "#real-warrant-margin" );
		}
		if(frozenFee_margin_result && warrant_margin_result){
			var frozenFee_margin = null;
			var warrant_margin = null;
			if($("#real-frozenFee-margin").length > 0){
				frozenFee_margin = $("#real-frozenFee-margin").val();
				if(frozenFee_margin == ''){
					frozenFee_margin = $("#frozenFee-margin").text();
				}
				else{
					frozenFee_margin = util. get_thousand($("#real-frozenFee-margin").val());
				}
			}
			else{
				frozenFee_margin = $("#frozenFee-margin").text();
			}
			
			if($("#real-warrant-margin").length > 0){
				warrant_margin = $("#real-warrant-margin").val();
				if(warrant_margin == ''){
					warrant_margin = $("#warrant-margin").text();
				}
			}
			else{
				warrant_margin = $("#warrant-margin").text();
			}
			
			modal_content.modal_header = '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>'
										+'<h4 class="modal-title" id="myModalLabel">保证金冻结确认</h4>';
			modal_content.modal_body = '<div>融资服务合同保证金: <span id="real-serv-mrgn">'+ frozenFee_margin +'</span>元</div>';
										//+'<div>担保机构还款保证金: '+ $("#warrant-account").text() +'元(<span id="real-wrtr-mrgn">'+ warrant_margin +'</span>%)</div>';
			modal_content.modal_footer = '<button type="button" class="btn btn-mf-primary" id="btn-modal-freeze">确定</button>'
										+'<button type="button" class="btn btn-mf-default" data-dismiss="modal">取消</button>';
			renderModal(modal_content);
			$("#project-modal-footer").on("click","#btn-modal-freeze",function(){
				var productId = $("#product-id").text();
				var servMrgnAmt = numeral().unformat($("#real-serv-mrgn").text());
				var wrtrMrgnRt = numeral().unformat($("#real-wrtr-mrgn").text());
				option.dataExtension = {
						"productId":productId,
						"servMrgnAmt":servMrgnAmt,
						"wrtrMrgnRt":wrtrMrgnRt,
						};
				option.action = global.context+"/web/product/freeze";
				util.ajax_submit("#form-modal",option).done(function(response){
					var _response = eval(response);
					if(_response.code  == "SUCCESS"){
						rein_flag = true;
						$("#project-modal").modal('hide')
						.on('hidden.bs.modal', function () {
							if(rein_flag){
								modal_content.modal_header = '提示';
								modal_content.modal_body = '冻结保证金成功，融资项目状态更新为“待发布”！';
								modal_content.modal_footer = '<button type="button" class="btn btn-mf-primary" id="prompt-btn" data-dismiss="modal">确定</button>';
								renderModal(modal_content);
								$('#prompt-btn').click(function(){
									rein_flag = false;
									loadLocalPage();
								});
							}
						});
					}
				});
			});
		}
	});
	
	//保证金实际值变化
	var flag = true;
	
	$("#real-warrant-margin").on("keyup",function(){
		var real_warrant_per = parseFloat($("#real-warrant-margin").val());
		var hidden_warrant_account = parseFloat($("#hidden-warrant-account").text());
		if(flag){
			real_warrant = numeral().unformat($("#warrant-account").text());
			flag = false;
		}
		var warrant_account = real_warrant_per*hidden_warrant_account/100;
		if(isNaN(warrant_account)){
			$("#warrant-account").text(util.get_thousand(real_warrant));
		}
		else{
			$("#warrant-account").text(util.get_thousand(warrant_account));
		}
	});
	
	$("#real-frozenFee-margin").on("blur",function(){
		var real_frozen = $(this).val();
		if(!isNaN(real_frozen.trim()) && real_frozen.trim().length != 0){
			$(this).val(real_frozen);
		}
		else{
			$(this).val(null);
		}
	});
	
	/**
	 * 风险评级
	 */
	/*表单验证提交*/
	$("#form-risk").validate({
		submitHandler : function(form) {
			var productId = $("#product-id").text();
			option.dataExtension = {"productId":productId};
			util.ajax_submit(form,option).done(function(response){
				var _response = eval(response);
				if(_response.code == "SUCCESS"){
					modal_content.modal_header = '提示';
					modal_content.modal_body = '评级成功，融资项目状态更新为“待冻结”！';
					modal_content.modal_footer = '<button type="button" class="btn btn-mf-primary" id="prompt-btn" data-dismiss="modal">确定</button>';
					renderModal(modal_content);
					$('#prompt-btn').click(function(){loadLocalPage();});
				}
			});
		}
	});
	
	/*下拉框变化时，项目总评分变化*/
	$("#product-rating,#guarantee-agency-rating,#financiers-rating").on('change',function(){
		var project_level = null;
		var product_scroe = parseFloat($("#product-rating").val());
		var financiers_scroe =parseFloat( $("#financiers-rating").val());
		var guarantee_scroe = parseFloat($("#guarantee-agency-rating").val());
		var score = product_scroe + financiers_scroe + guarantee_scroe ;
		if(parseFloat(8.75) <= parseFloat(score) && parseFloat(score) <= parseFloat(10)){
			project_level = "A级项目";
		}
		else if(parseFloat(6.25) <= parseFloat(score) && parseFloat(score) < parseFloat(8.75)){
			project_level = "B级项目";
		}
		else if(parseFloat(3.75) <= parseFloat(score) && parseFloat(score) < parseFloat(6.25)){
			project_level = "C级项目";
		}	
		else{
			project_level = "D级项目";
		}
		$('#total-score').html(project_level+ '(' + score + '分)');
		
		produce_rate_tip();
		
	});
	
	
	var produce_rate_tip = function(){
		var msg_map = {"A":"A级项目：提供货币类资产质押，本金覆盖率大于100%，违约可能性极低。",
				   "B":"B级项目：提供有形或无形资产抵质押（变现渠道畅通），本金覆盖率不低于100%，违约可能性较低。",
				   "C":"C级项目：提供有形或无形资产抵质押（有变现渠道）；或提供合格保证人担保，本金覆盖率不到100%，可能会违约。",
				   "D":"D级项目：无有形或无形资产抵质押，无保证机构担保，能提供非保证机构担保，违约的可能性较高。"
				  };
		
		$(".level-short-msg").text(msg_map[$("#product-rating :selected").text()]);
	};
	
	produce_rate_tip();
	
	$(".file-upload").fileUploader({
		url:global.context+"/web/uploadfile"
	});
});