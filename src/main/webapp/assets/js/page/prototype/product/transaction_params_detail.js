require(['jquery',
         'bootstrap',
         'bootstrap-datepicker',
         'select2',
         'requirejs/domready!'], 
function($){
//	var url = window.location.href;
//	var arg = url.substr(url.indexOf('?')+1);
	$('#form-user-infoquery #datetimepicker').datepicker({});
	$('.selectpicker').select2({width:'100%'});
	function addProtepy( name ){
		var btnname = "add-"+name+"-btn";
		var boxname = "add-"+name+"-box";
		var contentname = "add-"+name+"-content";
		$('.'+contentname).hide();
		$('.'+btnname).click(function(){
			var node = $('.'+boxname+' .'+contentname+':first-child').clone().show()
			.on('click','.add-close-btn', function(){node.hide();});
			$('.'+boxname).append( node );
		});
	};
	addProtepy( "car" );
	addProtepy( "house" );
	addProtepy( "movable" );
	addProtepy( "natural" );
	addProtepy( "legal" );
	
	$('.house-yes-content').hide();
	$('.car-yes-content').hide();
	$('.asset-yes-content').hide();
	$('.credit1-yes-content').hide();
	$('.credit2-yes-content').hide();
	$('.check-click').click(function(){
		var name = this.name.substring(2);
		$('.'+name+'-yes-content').show();
	});
	$('.uncheck-click').click(function(){
		var name = this.name.substring(2);
		$('.'+name+'-yes-content').hide();
	});
	
	var financeAmount = $("div[name='appliedQuota']").attr("value");
	$("#unpaking-content .contract-templ").val(financeAmount);
	
	$("#chk-unpaking").on("click", function(){
		if ($("#chk-unpaking").is(":checked")) {
			$("#unpaking-num").show("1000");
			$("#txt-unpaking-num").focus();
		}else{
		//	$("body #chk-avgunpaking").attr("checked", "false");
			$(".avg-unpaking").hide("1000");
			$("#unpaking-num").hide("1000");
			$("#unpaking").empty();
			financeAmount = $("div[name='appliedQuota']").attr("value");
			$("#unpaking-content .contract-templ").val(financeAmount);
			$("#txt-unpaking-num").val("");
			$("#unpaking-content .pake").html("融资包1金额(万元)");
		}
	});
	
	function addPackage(){
		var unpakingNum = $("#txt-unpaking-num").val();
		$("#unpaking").empty();
		for (var i = 1; i < unpakingNum; i++){
			html = "<hr><div class='package'><div class='row'>" +
			"<div class='col-sm-6'>" +
			"<div class='form-group'>" +
			"<label class='col-sm-4 control-label pake'>融资包"+ i +"金额(万元)</label>" +
			"<div class='col-sm-6'>" +
			"<input class='form-control contract-templ' name='name.packageQuota' type='text'>" +
			"</div>" +
			"</div>" +
			"</div>" +
			"</div>" +
			"<div class='row'>" +
			"<div class='col-sm-6'>" +
			"<div class='form-group' data-role='0'>" +
			"<label class='col-sm-4 control-label'>融资包起始日期</label>" +
			"<div class='col-sm-6'>" +
			"<input class='form-control start-time js-time-primary' name='name.packageStartTime' type='text' placeholder='' data-role='"+ i +"' data-name='start-time'>" +
			"</div>" +
			"</div>" +
			"<div class='row content-margin'>" +
			"<div class='col-sm-4 control-label'>" +
			"<label class='checkbox inline control-label'>" +
			"<input class='chk-vote' type='checkbox' value='option1' data-role='"+ i +"'>&nbsp定投" +
			"</label>" +
			"</div>" +
			"<div  class='col-sm-6 vote-content' data-role='"+ i +"'>" +
			"<input class='vote-group' type='hidden' value='分组A, 分组B'/>" +
			"</div>" +
			"</div>" +
			"</div>" +
			"<div class='col-sm-6'>" +
			"<div class='form-group' data-role='"+ i +"'>" +
			"<label class='col-sm-4 control-label'>融资包截止日期</label>" +
			"<div class='col-sm-6'>" +
			"<input class='form-control end-time js-time-primary' name='name.packageEndTime' type='text' placeholder='' data-role='"+ i +"' data-name='end-time'>" +
			"</div>" +
			"</div>" +
			"</div>" +
			"</div>" +
			"<div class='row content-margin'>" +
			"<div class='col-sm-6'>" +
			"<div class='form-group vote-content' data-role='"+ i +"'>" +
			"<label class='col-sm-4 control-label'>定投起始日期</label>" +
			"<div class='col-sm-6'>" +
			"<input class='form-control start-time-aipStartTime js-time-primary' name='name.aipStartTime' type='text' placeholder='' data-role='"+ i +"' data-name='start-time-aipStartTime'>" +
			"</div>" +
			"</div>" +
			"</div>" +
			"<div class='col-sm-6'>" +
			"<div class='form-group vote-content' data-role='"+ i +"'>" +
			"<label class='col-sm-4 control-label'>定投截止日期</label>" +
			"<div class='col-sm-6'>" +
			"<input class='form-control end-time-aipEndTime js-time-primary' name='name.aipEndTime' type='text' placeholder='' data-role='"+ i +"' data-name='end-time-aipEndTime'>" +
			"</div>" +
			"</div>" +
			"</div>" +
			"</div>" +
			"</div>"
			$("#unpaking").append(html);
		}
		$("#unpaking-content .pake").html("融资包"+ unpakingNum +"金额(万元)");
//		$("#unpaking-content .contract-templ").attr(name, "package["+ unpakingNum +"].packageQuota");
//		$("#unpaking-content .start-time").attr(name, "package["+ unpakingNum +"].packageStartTime");
//		$("#unpaking-content .end-time").attr(name, "package["+ unpakingNum +"].packageEndTime");
		
		$(".vote-group").select2({tags:["分组A", "分组B", "分组C"]});
		datepicker();
	}
	
	//平均分项挂单
	$("#chk-avgunpaking").on("click", function(){
		if ($("#chk-avgunpaking").is(":checked")) {
			var unpakingNum = $("#txt-unpaking-num").val();
			financeAmount /= unpakingNum;
			$("#unpaking .contract-templ").val(financeAmount);
			$("#unpaking .contract-templ").attr("readonly", "true");
			$("#unpaking-content .contract-templ").val((unpakingNum-1)*($("div[name='appliedQuota']").attr("value"))/unpakingNum);
		}else{
			financeAmount = $("div[name='appliedQuota']").attr("value");
			$("#unpaking-content .contract-templ").val(financeAmount);
			addPackage();
		}
	});
	
	$("#txt-unpaking-num").on("change",function(){
		html = "";
		var unpakingNum = $("#txt-unpaking-num").val();
		var value = 0.0;
		if (isNaN($(this).val())){
			$("#msg").html("请输入数字！");
			$("#modal-log").modal();
		}
		if (unpakingNum > 1){
			$(".avg-unpaking").show("1000");
			financeAmount = $("div[name='appliedQuota']").attr("value");
			$("#unpaking-content .contract-templ").val(financeAmount);
			addPackage();
		}else {
			$("#msg").html("拆包份数不能小于1");
			$("#modal-log").modal();
			$("#txt-unpaking-num").val("");
			$("#txt-unpaking-num").focus();
			
		}

	});
	
	$("#unpaking").on("focus", ".contract-templ", function(){
		value = $(this).val();
	}).on("change", ".contract-templ", function(){
		if (isNaN($(this).val())){
			$(this).val(value).focus();
			$("#msg").html("请输入数字！");
			$("#modal-log").modal();
		}else {
			financeAmount = financeAmount * 1 + value * 1 - $(this).val();
		}
		if (financeAmount < 1){
			$("#msg").html("输入总额不能大于申请额！");
			$("#modal-log").modal();
			financeAmount += $(this).val() * 1;
			$(this).val("");
			$(this).focus();
		}
		$("#unpaking-content .contract-templ").val(financeAmount);
	});
	
	datepicker();
	
	function datepicker(){
		$("#unpaking .start-time,#unpaking .end-time,.start-time,.end-time, .start-time-aipStartTime, .end-time-aipEndTime").datepicker({
	        format: 'yyyy-mm-dd',
	        weekStart: 1,
	        autoclose: true,
	        todayBtn: false,
	        language: 'zh-CN'
	    }).on("changeDate",function(ev){
	    });
	}
	
	$("#transaction-submit").on("click", function(){
		location.href="transaction_params.html";
	});
	
	function getRole(){
		var patt = window.location.search;
		var urlId = patt.indexOf("role");
		var roleId = patt.substring(urlId + 5, urlId + 6);
		return roleId;
	}
	
	if (getRole()){
		$("#is-unpaking").show();
	}
	
	$(".vote-group").select2({tags:["分组A", "分组B", "分组C"]});
	
	$("body").on("click", ".chk-vote", function(){
		var me = this;
		var data_role = $(me).attr("data-role");
		if ($(me).is(":checked")) {
			$(".vote-content[data-role="+data_role+"]").show("1000",function(){
				var current = $(".start-time").val();
				var aipEndTime = $(".end-time").val();
				/*$('.start-time-aipStartTime').datepicker('setStartDate', current);
				$('.start-time-aipStartTime').datepicker('setEndDate', aipEndTime);
				$('.end-time-aipEndTime').datepicker('setEndDate', aipEndTime);*/
				/*var aipStartTime = $(".start-time-aipStartTime").val();
				$('.end-time-aipEndTime').datepicker('setEndDate', aipStartTime);*/
			});
		}else{
			$(".vote-content[data-role="+data_role+"]").hide("1000");
		}
		
	});
	
	$("#panel-heading").click(function(){
		$("#collapseOne").collapse('toggle');
		if($("#fa_icon").hasClass("fa-caret-down")){
			$("#fa_icon").removeClass("fa-caret-down").addClass("fa-caret-up");
		}
		else{
			$("#fa_icon").removeClass("fa-caret-up").addClass("fa-caret-down");
		}
	});
	
	
	var options = {url:null, action:null, context:null, mode:null};
	
	//表单提交
	$(".form-btn-submit").on("click", function(){
		options.context = ".package";
		options.mode = "arrayMode";
		util.ajax_submit('#transactionparams-form',option);
	});
	
	$('body').on('changeDate', '.js-time-primary', function(){
		var me = this;
		console.log(me);
		var data_role = $(me).attr('data-role');
		var data_name = $(me).attr('data-name');
		
		var currentStartTime = $('.start-time[data-role='+ data_role +']').val();
		var currentEndTime = $('.end-time[data-role='+ data_role +']').val();
		var aipStartTime = $('.start-time-aipStartTime[data-role='+ data_role +']').val();
		var aipEndTime = $('.end-time-aipEndTime[data-role='+ data_role +']').val();
		
		
		/*$('.start-time[data-role='+ data_role +']').datepicker('setEndDate', currentEndTime);
		$('.end-time[data-role='+ data_role +']').datepicker('setStartDate', currentStartTime);
		
		$('.start-time-aipStartTime[data-role='+ data_role +']').datepicker('setStartDate', currentStartTime);
		$('.start-time-aipStartTime[data-role='+ data_role +']').datepicker('setEndDate', aipEndTime);
		
		$('.end-time-aipEndTime[data-role='+ data_role +']').datepicker('setStartDate', currentStartTime);
		$('.end-time-aipEndTime[data-role='+ data_role +']').datepicker('setEndDate', aipEndTime);*/
		
		
		if ('start-time' == data_name){
			$('.start-time-aipStartTime[data-role='+ data_role +']').val("");
			$('.end-time-aipEndTime[data-role='+ data_role +']').val("");
			
			$('.end-time[data-role='+ data_role +']').datepicker('setStartDate', currentStartTime);
			
			$('.start-time-aipStartTime[data-role='+ data_role +']').datepicker('setStartDate', currentStartTime);
			$('.start-time-aipStartTime[data-role='+ data_role +']').datepicker('setEndDate', currentEndTime);
			
			$('.end-time-aipEndTime[data-role='+ data_role +']').datepicker('setStartDate', currentStartTime);
			$('.end-time-aipEndTime[data-role='+ data_role +']').datepicker('setEndDate', currentEndTime);
			
		}else if ('end-time' == data_name){
			$('.start-time-aipStartTime[data-role='+ data_role +']').val("");
			$('.end-time-aipEndTime[data-role='+ data_role +']').val("");
			
			$('.start-time[data-role='+ data_role +']').datepicker('setEndDate', currentEndTime);
			
			$('.start-time-aipStartTime[data-role='+ data_role +']').datepicker('setStartDate', currentStartTime);
			$('.start-time-aipStartTime[data-role='+ data_role +']').datepicker('setEndDate', currentEndTime);
			
			$('.end-time-aipEndTime[data-role='+ data_role +']').datepicker('setStartDate', currentStartTime);
			$('.end-time-aipEndTime[data-role='+ data_role +']').datepicker('setEndDate', currentEndTime);
			
		}else if ('start-time-aipStartTime' == data_name){
			
			//$('.start-time[data-role='+ data_role +']').datepicker('setEndDate', aipStartTime);
			
			$('.end-time-aipEndTime[data-role='+ data_role +']').datepicker('setStartDate', aipStartTime);
			
		}else if ('end-time-aipEndTime' == data_name){
			$('.start-time-aipStartTime[data-role='+ data_role +']').datepicker('setEndDate', aipEndTime);
			
		}
		console.log(currentStartTime);
		console.log(currentEndTime);
		console.log(aipStartTime);
		console.log(aipEndTime);
		
	});
	
	/*$('.start-time').on("changeDate", function(){
		var current = $(".start-time").val();
		$('.end-time').datepicker('setStartDate', current);
		$('.start-time-aipStartTime').val("");
		$('.end-time-aipEndTime').val("");
		var currentEndTime = $(".end-time").val();
		$('.start-time-aipStartTime').datepicker('setStartDate', current);
		$('.start-time-aipStartTime').datepicker('setEndDate', currentEndTime);
		$('.end-time-aipEndTime').datepicker('setEndDate', currentEndTime);
	});
	
	$('.end-time').on("changeDate", function(){
		var current = $(".end-time").val();
		var currentStartTime = $(".start-time").val();
		$('.start-time').datepicker('setEndDate', currentEndTime);
		$('.start-time-aipStartTime').val("");
		$('.end-time-aipEndTime').val("");
		var currentEndTime = $(".end-time").val();
		$('.start-time-aipStartTime').datepicker('setStartDate', currentStartTime);
		$('.start-time-aipStartTime').datepicker('setEndDate', currentEndTime);
		$('.end-time-aipEndTime').datepicker('setEndDate', currentEndTime);
		$('.end-time-aipEndTime').datepicker('setStartDate', currentStartTime);
	//	$('.start-time-aipStartTime').datepicker('setEndDate', current);
	});
	
	$('.start-time-aipStartTime').on("changeDate", function(){
		var aipStartTime = $(".start-time-aipStartTime").val();
		$('.end-time-aipEndTime').datepicker('setStartDate', aipStartTime);
	});
	
	$('.end-time-aipEndTime').on("changeDate", function(){
		var aipEndTime = $(".end-time-aipEndTime").val();
		$('.start-time-aipStartTime').datepicker('setEndDate', aipEndTime);
	});
	
	function datePickerUtil(){
		var current = $(".start-time").val();
		var aipEndTime = $(".end-time").val();
		$('.start-time-aipStartTime').datepicker('setStartDate', current);
		$('.start-time-aipStartTime').datepicker('setEndDate', aipEndTime);
		$('.end-time-aipEndTime').datepicker('setEndDate', aipEndTime);
	}*/
	
});
