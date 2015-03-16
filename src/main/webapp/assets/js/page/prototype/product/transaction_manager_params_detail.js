require(['jquery',
         'bootstrap',
         'bootstrap-datepicker',
         'select2',
         'requirejs/domready!'], 
function($){
//	var url = window.location.href;
//	var arg = url.substr(url.indexOf('?')+1);
	$('#form-user-infoquery #datetimepicker').datepicker({});
	$('.selectpicker').select2();
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
	$('#org-choose').select2({
		minimumInputLength: 0,
		placeholder: "选择一个担保机构",
		width:'100%',
		tags:["担保机构A", "担保机构B", "担保机构C", "担保机构A", "担保机构B", "担保机构C", "担保机构A", "担保机构B", "担保机构C"]});
	
	var financeAmount = $("#finance-amount").attr("value");
	$("#unpaking-content .contract-templ").val(financeAmount);
	
	$("#chk-unpaking").on("click", function(){
		if ($("#chk-unpaking").is(":checked")) {
			$("#unpaking-num").show("1000");
			$("#txt-unpaking-num").focus();
		}else{
			$("#unpaking-num").hide("1000");
			$("#unpaking").empty();
			financeAmount = $("#finance-amount").attr("value");
			$("#unpaking-content .contract-templ").val(financeAmount);
			$("#txt-unpaking-num").val("");
			$("#unpaking-content .pake").html("融资包1金额");
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
			$("#unpaking").empty();
			financeAmount = $("#finance-amount").attr("value");
			$("#unpaking-content .contract-templ").val(financeAmount);
			for (var i = 1; i < unpakingNum; i++){
				html = "<hr><div class='row aaaaa'>" +
				"<div class='col-sm-6'>" +
				"<div class='form-group'>" +
				"<label class='col-sm-4 control-label' id=pake" + i + ">融资包" + i + "金额</label>" +
				"<div class='col-sm-6'>" +
				"<input class='form-control contract-templ' type='text'>" +
				"</div>" +
				"</div>" +
				"<div class='form-group'>" +
				"<label class='col-sm-4 control-label'>起始日期</label>" +
				"<div class='col-sm-6'>" +
				"<input type='text' class='form-control start-time' placeholder=''>" +
				"</div>" +
				"</div>" +
				"<div class='form-group'>" +
				"<label class='col-sm-4 control-label'>风险管理费</label>" +
				"<div class='col-sm-6'>" +
				"<div class='input-group'>" +
				"<input class='form-control' type='text'>" +
				"<span class='input-group-addon'>%</span>" +
				"</div>" +
				"</div>" +
				"</div>" +
				"<div class='form-group'>" +
				"<label class='col-sm-4 control-label'>合同模板*</label>" +
				"<div class='col-sm-6'>" +
				"<input type='file'>" +
				"</div>" +
				"</div>" +
				"<div class='form-group'>" +
				"<div class='col-sm-4 control-label'>" +
				"<label class='checkbox inline control-label'>" +
				"<input class='chk-vote' type='checkbox' value='' data-role='"+i+"'> 定投" +
				"</label>" +
				"</div>" +
				"<div data-role='"+i+"' class='col-sm-6 vote-content'>" +
				"<input class='vote-group' type='hidden' value='分组A, 分组B'/>" +
				"</div>" +
				"</div>" +
				"</div>" +
				"<div class='col-sm-6'>" +
				"<div class='form-group'>" +
				"<label class='col-sm-4 control-label'>借款合同履约保证金</label>" +
				"<div class='col-sm-6'>" +
				"<div class='input-group'>" +
				"<input class='form-control' type='text'>" +
				"<span class='input-group-addon'>%</span>" +
				"</div>" +
				"</div>" +
				"</div>" +
				"<div class='form-group'>" +
				"<label class='col-sm-4 control-label'>截止日期</label>" +
				"<div class='col-sm-6'>" +
				"<input type='text' class='form-control end-time' placeholder=''>" +
				"</div>" +
				"</div>" +
				"<div class='form-group'>" +
				"<label class='col-sm-4 control-label'>融资服务费</label>" +
				"<div class='col-sm-6'>" +
				"<div class='input-group'>" +
				"<input class='form-control' type='text'>" +
				"<span class='input-group-addon'>%</span>" +
				"</div>" +
				"</div>" +
				"</div>" +
				"</div>" +
				"</div>" +
				"<div class='row'>" +
				"<div class='col-sm-6'>" +
				"<div  data-role='"+i+"' class='form-group vote-content'>" +
				"<label class='col-sm-4 control-label'>起始日期</label>" +
				"<div class='col-sm-6'>" +
				"<input class='form-control start-time' type='text' placeholder=''>" +
				"</div>" +
				"</div>" +
				"</div>" +
				"<div class='col-sm-6'>" +
				"<div data-role='"+i+"' class='form-group vote-content'>" +
				"<label class='col-sm-4 control-label'>截止日期</label>" +
				"<div class='col-sm-6'>" +
				"<input type='text' class='form-control end-time' placeholder=''>" +
				"</div>" +
				"</div>" +
				"</div>" +
				"</div>" +
				"</div>"
				$("#unpaking").append(html);
			}
			$("#unpaking-content .pake").html("融资包"+ unpakingNum +"金额");
			
			$(".vote-group").select2({tags:["分组A", "分组B", "分组C"]});
			$('.selectpicker').select2({width:'100%'});
			datepicker();
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
		$("#unpaking .start-time,#unpaking .end-time,.start-time,.end-time").datepicker({
	        format: 'yyyy-mm-dd',
	        weekStart: 1,
	        autoclose: true,
	        todayBtn: false,
	        language: 'zh-CN'
	    }).on("changeDate",function(ev){
	    });
	}
	
	$("#transaction-submit").on("click", function(){
		location.href="transaction_params.html?role=1";
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
			$(".vote-content[data-role="+data_role+"]").show("1000");
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
	
});
