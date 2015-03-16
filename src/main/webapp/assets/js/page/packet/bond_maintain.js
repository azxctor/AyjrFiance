require(['jquery',
         'global',
         'module/util',
         'module/datatables',
         'form',
         'bootstrap-switch',
         'requirejs/domready!'], 
function($, global, util, datatables, form){
	$("[name='overdueFlag']").on('change',function(){
		$(".overdue").toggle();
		$("[name='maxOverdueDate']").val("2");
		$("[name='maxOverdueCount']").val("2");
	});
	function formatForm( list ){
		var arr = [];
		$.each(list,function(i,item){
			arr.push(item.id);
		});
		return arr;
	}
	$.ajax({
		url: global.context+"/web/rulemaintain/show",
		type: 'GET',
		dataType: 'json',
		contentType: 'application/json;charset=utf-8',
		error: function(){
		},
		success: function(response){
			var overdueFlag = response.creditorTransferRuleDto.overdueFlag;
			if(overdueFlag == "Y"){
				$("[name='overdueFlag']").attr("checked",true);
				$(".overdue").show();
				$("[name='maxOverdueDate']").val(response.creditorTransferRuleDto.maxOverdueDate);
				$("[name='maxOverdueCount']").val(response.creditorTransferRuleDto.maxOverdueCount);
			}else{
				$("[name='overdueFlag']").attr("checked",false);
				$(".overdue").hide();
			}
			$("[name='minTermDays']").val(response.creditorTransferRuleDto.minTermDays);
			var packageListSelected = formatForm(response.packageListSelected);
			$("#notransfer").val(packageListSelected).select2();
			var ruleListSelected = formatForm(response.ruleListSelected);
			$("#transfer").val(ruleListSelected).select2();
		}
	});
	var option = {
		context:null,//提交表单域 
		action:global.context+"/web/rulemaintain/save",//提交url
		dataExtension:null,//额外数据
		mode:"",
	};
	option.dataExtension = {"creditorTransferRuleDto":"","ruleListSelected":[],"packageListSelected":[]};
	function formatTo( arr ){
		var list = [];
		$.each(arr,function(i,item){
			list.push({"id":item});
		});
		return list;
	}
	var form = $('#form-maintain');	
	form.validate({
		onsubmit: false,
	});
	$('#form-btn-submit').click(function(){
		if(form.valid()){
			var overdueFlag = $("[name='overdueFlag']").is(':checked')?"Y":"N";
			var maxOverdueDate = $("[name='maxOverdueDate']").val();
			var maxOverdueCount = $("[name='maxOverdueCount']").val();
			var minTermDays = $("[name='minTermDays']").val();
			option.dataExtension.creditorTransferRuleDto = {"overdueFlag": overdueFlag,"maxOverdueDate":Number(maxOverdueDate),"maxOverdueCount":Number(maxOverdueCount),"minTermDays":Number(minTermDays)};
			if($("#notransfer").val()){
				option.dataExtension.packageListSelected = formatTo($("#notransfer").val());
			}
			if($("#transfer").val()){
				option.dataExtension.ruleListSelected = formatTo($("#transfer").val());
			}
			var call = util.ajax_submit(form,option);
			call.success(function(response){
				if(response&&response.code == "ACK"){
				}
			});
		}else{
		}
	});	
}
);