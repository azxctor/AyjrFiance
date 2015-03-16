require(['jquery',
         'global',
         'module/util',
         'module/datatables',
         'form',
         'jquery.validate.methods',
         'requirejs/domready!'], 
function($, global, util, datatables, form){
	
	var option={
			action:null,
			context:null,
			dataExtension:null,
	};
	var _fnOperate=function(data, type, extra){
		var data_contractId = extra.contractId;
		var data_productLevelId = extra.productLevelId;
		var data_PrepayDeductIntrflg = extra.deductFlg;
		var data_financierPrepaymentPenaltyRate = extra.financierRate;
		var data_platformPrepaymentPenaltyRate = extra.platformRate;
		var data_paymentPenaltyFineRate = extra.paymentRate;
		//return $.format("<div class='btn-group'><button type='button' class='btn btn-xs btn-mf-primary btn-edit' data-contractId={0} data-productLevelId={1} data-PrepayDeductIntrflg={2} data-financierPrepaymentPenaltyRate={3} data-platformPrepaymentPenaltyRate={4} data-paymentPenaltyFineRate={5}>修改</button><button type='button' class='btn btn-xs btn-mf-primary btn-delete' data-contractId={0} data-productLevelId={1}>删除</button></div>",data_contractId,data_productLevelId,data_PrepayDeductIntrflg,data_financierPrepaymentPenaltyRate,data_platformPrepaymentPenaltyRate,data_paymentPenaltyFineRate);
		return $.format("<div class='btn-group'><button type='button' class='btn btn-xs btn-mf-primary btn-edit' data-contractId={0} data-productLevelId={1} data-PrepayDeductIntrflg={2} data-financierPrepaymentPenaltyRate={3} data-platformPrepaymentPenaltyRate={4} data-paymentPenaltyFineRate={5}>修改</button></div>",data_contractId,data_productLevelId,data_PrepayDeductIntrflg,data_financierPrepaymentPenaltyRate,data_platformPrepaymentPenaltyRate,data_paymentPenaltyFineRate);
	};
	
	var _fnPrepayDeductIntrflg=function(data, type, extra){
		if(extra.deductFlg){
			return "<span>是</span>";
		}
		else{
			return "<span>否</span>";
		}
	};
	
	var _fnContract=function(data, type, extra){
		return "<span>" + extra.contractName + "</span>";
	};
	var _fnProductLevel=function(data, type, extra){
		return "<span>" + extra.productLevelTextShort + "</span>";
	};
	
	var _fnFinancierRate=function(data, type, extra){
		return "<span>" + extra.financierRate + "</span>";
	}
	
	var _fnPlatformRate=function(data, type, extra){
		return "<span>" + extra.platformRate + "</span>";
	}
	
	var _fnPaymentRate=function(data, type, extra){
		return "<span>" + extra.paymentRate + "</span>";
	}
	
	var rate_table = null;
	var rateTable = function(){
		var options = {};
		options.tableId = '#table-search-result';
		options.sAjaxSource = global.context+"/web/contractRate/getlist";
		options.functionList = {
				"_fnOperate" : _fnOperate,
				"_fnPrepayDeductIntrflg": _fnPrepayDeductIntrflg,
				"_fnContract":_fnContract,
				"_fnProductLevel":_fnProductLevel,
				"_fnFinancierRate":_fnFinancierRate,
				"_fnPlatformRate":_fnPlatformRate,
				"_fnPaymentRate":_fnPaymentRate
				
		};
		rate_table = datatables.init(options);
		rate_table.setParams(util.getSearchData('#search-wrapper'));
		return rate_table.create();
	};
	rateTable();
	
	$("#table-search-result").on("click",".btn-edit,.btn-delete",function(e){
		var $target=$(e.currentTarget);
		var contractId=$target.attr("data-contractId");
		var productLevelId = $target.attr("data-productLevelId");
		if($target.hasClass("btn-edit")){
			var PrepayDeductIntrflg = $target.attr("data-PrepayDeductIntrflg");
			var financierPrepaymentPenaltyRate = $target.attr("data-financierPrepaymentPenaltyRate");
			var platformPrepaymentPenaltyRate = $target.attr("data-platformPrepaymentPenaltyRate");
			var paymentPenaltyFineRate = $target.attr("data-paymentPenaltyFineRate");
			$("#modal-contract").find("#contract-form").attr("data-action","contractRate/update");
			$("#contractId").val(contractId).attr("disabled","disabled");
			$("#productLevelId").val(productLevelId).attr("disabled","disabled");
			$("#financierPrepaymentPenaltyRate").val(financierPrepaymentPenaltyRate).attr("disabled","disabled");
			$("#platformPrepaymentPenaltyRate").val(platformPrepaymentPenaltyRate);
			$("#paymentPenaltyFineRate").val(paymentPenaltyFineRate);
			if(PrepayDeductIntrflg == "false"){
				$("#deductFlg").removeAttr("checked").attr("disabled","disabled");
			}
			else{
				$("#deductFlg").attr("checked","checked").attr("disabled","disabled");
			}
			$("#modal-contract").modal("show");
			//表单验证
			$("#contract-form").validate();
		}
		if($target.hasClass("btn-delete")){
			$("#modal-contract-delete").find(".btn-modal-delete").data("data-contractId",contractId);
			$("#modal-contract-delete").find(".btn-modal-delete").data("data-productLevelId",productLevelId);
			$("#modal-contract-delete").modal("show");
		}
	});
	$("#search-wrapper").on("click",".btn-add,.btn-search",function(e){
		var $target=$(e.currentTarget);
		if($target.hasClass("btn-add")){
			$("#modal-contract").find("#contract-form").attr("data-action","contractRate/save");
			$("#modal-contract").modal("show");
			//表单验证
			$("#contract-form").validate();
		}
		if($target.hasClass("btn-search")){
			rate_table.setParams(util.getSearchData("#search-wrapper"));
			rate_table.invoke("fnDraw");
		}
	});
	
	$("#modal-contract-delete").on("click",".btn-modal-delete",function(e){
		var $target=$(e.currentTarget);
		var contractId=$target.data("data-contractId");
		var productLevelId = $target.data("data-productLevelId");
		$.ajax({
			url:global.context+"/web/contractRate/delete",
			type: "POST",
			dataType: 'json',
			contentType: 'application/json; charset=UTF-8',
			data:JSON.stringify({"contractId":contractId,"productLevelId":productLevelId})
		}).done(function(resp){
			$("#modal-contract-delete").modal("hide");
			if(resp.code == "ACK"){
				rate_table.setParams(util.getSearchData("#search-wrapper"));
				rate_table.invoke("fnDraw");
			}
		});
	});
	$("#modal-contract").on("click",".btn-modal-tosure",function(e){
		var $target=$(e.currentTarget);
		if(!$("#contract-form").validate().form()){
            return false;
		}
		//$("#contract-form").validate({
			//submitHandler : function(form) {
		        option.action = global.context+"/web/" + $("#contract-form").attr("data-action");
				util.ajax_submit($("#contract-form"),option).done(function(resp){
					$("#modal-contract").modal("hide");
					if(resp.code == "ACK"){
						rate_table.setParams(util.getSearchData("#search-wrapper"));
						rate_table.invoke("fnDraw");
					}
				});
			//}
		//});
	});
	
	//表单清空
	$(".modal").on("hidden.bs.modal",function(){
		$(this).find("form").each(function(){
			this.reset();
			$(this).find("label.invalid").remove();
			$("#contractId").removeAttr("disabled");
			$("#productLevelId").removeAttr("disabled");
			$("#financierPrepaymentPenaltyRate").removeAttr("disabled","disabled");
			$("#deductFlg").removeAttr("disabled","disabled");
		});
	});
	/**
	 * 初始化
	 */
	//表单控件初始化
	form.init();
});