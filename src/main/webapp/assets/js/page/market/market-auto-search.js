require(['jquery',
         'global',
         'module/util',
         'module/datatables',
         'underscore',
         'module/ajax',
         'bootstrap',
         'bootstrap-datepicker',
         'bootstrap-datepicker.zh-CN',
         'bootstrapx-popoverx',
         'ickeck',
         'requirejs/domready!'], 
         function($,global,util,datatables,_){
	
	var principalTable = null;
	var _rendarTable = function(){
		var options = {};
		options.tableId = '#principal-table';
		options.aaSorting = [[1,"desc"]];
		options.sAjaxSource = global.context+"/web/market/autosubscribe/principals/query";
		principalTable = datatables.init(options);
		principalTable.setParams(util.getSearchData("#search_area"));
		return principalTable.create();
	};
	
	
	$('.riskParam input').iCheck({
		 checkboxClass: 'icheckbox_square-orange',
		 increaseArea: '20%' // optional
	});
	$('.payMethodParam input').iCheck({
		checkboxClass: 'icheckbox_square-orange',
		 increaseArea: '20%' // optional
	});
	$('.warrantyTypeParam input').iCheck({
		checkboxClass: 'icheckbox_square-orange',
		 increaseArea: '20%' // optional
	});
	
	$("#datatype").on("change",function(){
		$(".time-range").hide().find("input,select").attr("data-ignore","true");
		$($(this).find(" :selected").attr("data-type")).show().find("input,select").removeAttr("data-ignore");
	})
	
	var risk_arr = [];
	var paymethod_arr = [];
	var warranty_arr = [];
	
	$("#search_area").find("input[type='checkbox']").on('ifChecked', function(event){
		 if(this.name == "riskParam"){
			 if(!_.contains(risk_arr,this.value)){
				 risk_arr.push(this.value);
			 }
		 }else if(this.name == "payMethodParam"){
			 if(!_.contains(paymethod_arr,this.value)){
				 paymethod_arr.push(this.value);
			 }
		 }else if(this.name == "warrantyTypeParam"){
			 if(!_.contains(warranty_arr,this.value)){
				 warranty_arr.push(this.value);
			 }
		 }
	});
	
	$("#search_area").find("input[type='checkbox']").on('ifUnchecked', function(event){
		 if(this.name == "riskParam"){
			risk_arr = _.without(risk_arr,this.value);
		 }else if(this.name == "payMethodParam"){
			 paymethod_arr = _.without(risk_arr,this.value);
		 }else if(this.name == "warrantyTypeParam"){
			 warranty_arr = _.without(risk_arr,this.value);
		 }
	});
	
	$("#search").on("click",function(){
		 if( !$("#search-form-id").valid()) return false;
		 var $range = $(".time-range:visible");
		var low = parseInt($range.find(".low-range").val());
		var high = parseInt($range.find(".high-range").val());
		$(".lowandhigh").remove();
		if(low>high){
			$range.find(".input-group").after('<label  class="invalid lowandhigh">下限应该大于等于上限</label>')
			return;
		}
		$("#riskparam-hidden").val(risk_arr.join(","));
		$("#paymethodparam-hidden").val(paymethod_arr.join(","));
		$("#warrantytypeparam-hidden").val(warranty_arr.join(","));
		principalTable.setParams(util.getSearchData("#search_area"));
		principalTable.invoke("fnDraw");
	})
	
	$("#search-form-id").validate();
	_rendarTable();
});
