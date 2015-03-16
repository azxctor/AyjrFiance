require(['jquery',
         'global',
         'module/util',
         'module/datatables',
         'module/form',
         'module/bread_crumb',
         'jquery.ui',
         'module/ajax',
         'bootstrap',
         'jquery.validate.methods',
         'bootstrap-datepicker',
         'bootstrap-datepicker.zh-CN',
         'module/cascading_listener',
         'requirejs/domready!'], 

function($, global, util,datatables,upload,mfBread){
	
	var authzdCtrTable = null;
	var contractsTable = null; 
	
	var ajaxObject = null;
	
	var _fnAgencyBtnRender = function(data,type,full){
		return '<div class="btn-group btn-group-xs"><a data-ctr-id="'+full.userId+'" data-ctr-name="'+full.fullName+'" data-ctr-no="'+full.acctNo+'" class="btn btn-mf-primary go-details check-link table-link" type="button"><i title="详情" class="fa fa-eye fa-lg"></i></a></div>';
	};
	var _fnAgencyBtnRender2 = function(data,type,full){
		return '<div class="btn-group btn-group-xs"><a data-id="'+full.id+'" class="btn btn-mf-primary go-details check-link table-link" type="button"><i title="详情" class="fa fa-eye fa-lg"></i></a></div>';
	};
	
	var createTable = function(){
		var options = {};
		options.tableId = '#authzdCtrTable';
		options.sAjaxSource = global.context+"/web/member/authzdctr/agencyFee/constract/manage/authzdctr/list";
		options.functionList={"_fnAgencyBtnRender":_fnAgencyBtnRender};
		options.aaSorting=[[4,"desc"]];
		authzdCtrTable = datatables.init(options);
		authzdCtrTable.setParams(util.getSearchData('#search-area'));
		return authzdCtrTable.create();
	};
	
	var createTable2 = function(){
		var options = {};
		options.tableId = '#contractsTable';
		options.sAjaxSource = global.context+"/web/member/authzdctr/agencyFee/constract/manage/constract/list";
		options.functionList={"_fnAgencyBtnRender2":_fnAgencyBtnRender2};
		options.aaSorting=[[3,"asc"]];
		contractsTable = datatables.init(options);
		//contractsTable.setParams({'authzd_id':});
		//return contractsTable.create();
	};
	
	createTable();
	
	/**
	 * Define search event
	 */
	$('#authzdctr-search-btn').click(function(){
		authzdCtrTable.setParams(util.getSearchData("#search-area"));
		authzdCtrTable.invoke("fnDraw");
	});
	
	$(".right-blk-head").on("click","li:eq(1) a",function(){
		$("#main-wrapper").show("slide");
		$("#contracts-wrapper").hide();
		formReset();
		if($("#contract-detail-wrapper").is(':visible')){
			mfBread.pop();
			$("#contract-detail-wrapper").hide();
		}
		authzdCtrTable.invoke("fnStandingRedraw");//
		mfBread.pop();
	});
	
	$(".right-blk-head").on("click","li:eq(2) a",function(){
		$("#contracts-wrapper").show("slide");
		$("#contract-detail-wrapper").hide();
		formReset();
		contractsTable.invoke("fnStandingRedraw");//
		mfBread.pop();
	});
	
	$("body").on("click", "#authzdCtrTable a.go-details", function() {
		$("#main-wrapper").hide();
		$("#contracts-wrapper").show("slide");
		var userId = $(this).attr("data-ctr-id");
		if(contractsTable == null){
			createTable2();
			contractsTable.setParams([{'name':'authzd_id','value':userId}]);
			contractsTable.create();
		}else{
			contractsTable.setParams([{'name':'authzd_id','value':userId}]);
			contractsTable.invoke("fnDraw");
		}
		$('#contracts-wrapper #authzdctr-insert-btn').attr('data-ctr-id',$(this).attr("data-ctr-id"));
		$('#contracts-wrapper #authzdctr-insert-btn').attr('data-ctr-name',$(this).attr("data-ctr-name"));
		$('#contracts-wrapper #authzdctr-insert-btn').attr('data-ctr-no',$(this).attr("data-ctr-no"));
		mfBread.push({
			label : "合同列表"
		});
	});
	
	var setVal = function (){
		$('form#form-contract input[name="id"]').val(ajaxObject['id']);
		$('form#form-contract input[name="version"]').val(ajaxObject['version']);
		$('form#form-contract input[name="orgId"]').val(ajaxObject['orgId']);
		$('form#form-contract span.authzd-ctr-name').text(ajaxObject['fullName']);
		$('form#form-contract span.authzd-ctr-acct-no').text(ajaxObject['acctNo']);
		$('form#form-contract input[name="contractName"]').val(ajaxObject['contractName']);
		$('form#form-contract select[name="contractType"]').val(ajaxObject['contractType'].code);
		$('form#form-contract input[name="startDt"]').attr('value',ajaxObject['startDt']);
		$('form#form-contract input[name="startDt"]').val(ajaxObject['startDt']);
		$('form#form-contract input[name="endDt"]').attr('value',ajaxObject['endDt']);
		$('form#form-contract input[name="endDt"]').val(ajaxObject['endDt']);
		//$('form#form-contract input[name="bisunessType"]').val(ajaxObject['bisunessType']);
		//$('form#form-contract input[name="director"]').val(ajaxObject['director']);
		//$('form#form-contract input[name="chanDirector"]').val(ajaxObject['chanDirector']);
		$('form#form-contract input[name="seatFeeAmt"]').val(ajaxObject['seatFeeAmt']);
		//$('form#form-contract input[name="state"]').val(ajaxObject['state']);
		//$('form#form-contract input[name="note"]').val(ajaxObject['note']);
		$('form#form-contract input[name="month3RT"]').val(ajaxObject['month3RT']);
		$('form#form-contract input[name="month6RT"]').val(ajaxObject['month6RT']);
		$('form#form-contract input[name="month9RT"]').val(ajaxObject['month9RT']);
		$('form#form-contract input[name="month12RT"]').val(ajaxObject['month12RT']);
		$('form#form-contract input[name="allocRT"]').val(ajaxObject['allocRT']);
		$('form#form-contract input[name="actgStdRT"]').val(ajaxObject['actgStdRT']);
	};
	var setNullVal = function (){
		//$('form#form-contract input[name="id"]').val("");
		//$('form#form-contract input[name="version"]').val("");
		//$('form#form-contract input[name="orgId"]').val("");
		//$('form#form-contract span.authzd-ctr-name').text("");
		//$('form#form-contract span.authzd-ctr-acct-no').text("");
		$('form#form-contract input[name="contractName"]').val("");
		$('form#form-contract select[name="contractType"]').val("A");
		//$('form#form-contract input[name="startDt"]').attr('value',ajaxObject['startDt']);
		$('form#form-contract input[name="startDt"]').val("");
		//$('form#form-contract input[name="endDt"]').attr('value',ajaxObject['endDt']);
		$('form#form-contract input[name="endDt"]').val("");
		$('form#form-contract input[name="seatFeeAmt"]').val("");
		$('form#form-contract input[name="month3RT"]').val("");
		$('form#form-contract input[name="month6RT"]').val("");
		$('form#form-contract input[name="month9RT"]').val("");
		$('form#form-contract input[name="month12RT"]').val("");
		$('form#form-contract input[name="allocRT"]').val("");
		$('form#form-contract input[name="actgStdRT"]').val("");
	};
	var formReset = function(){
		$('form#form-contract label.invalid').remove();
		$('form#form-contract input[name="id"]').val('');
		$('form#form-contract input[name="version"]').val('');
		$('form#form-contract input[name="orgId"]').val('');
		$('form#form-contract span.authzd-ctr-name').text('');
		$('form#form-contract span.authzd-ctr-acct-no').text('');
		$('form#form-contract')[0].reset();
		$('form#form-contract input[name="startDt"]').removeAttr('value');
		$('form#form-contract input[name="endDt"]').removeAttr('value');
	};
	
	$("body").on("click", "#contractsTable a.go-details", function() {
		var contractId = $(this).attr("data-id");
		$("#contracts-wrapper").hide();
		$("#contract-detail-wrapper").show("slide");
		$("#form-btn-reset").attr("data-is-new","false");
		mfBread.push({
			label : "合同详细信息"
		});
		
		$('form#form-contract input[name="startDt"],form#form-contract input[name="endDt"]').datepicker({
			format: 'yyyy-mm-dd',
            autoclose: true,
            language: 'zh-CN'});
		
		$.ajax({
			url : global.context + "/web/member/authzdctr/agencyFee/constract/manage/constract/info/"+contractId,
			type : "post",
			dataType : 'json',
			contentType : 'application/json; charset=UTF-8',
		}).done(function(resp) {
			ajaxObject = resp;
			setVal();
		});
		//$("#contractType").trigger("change");
	});
	
	$("body").on("click", "#form-contract .form-btn-reset", function() {
		var btnVal = $("#form-btn-reset").attr("data-is-new");
		if(btnVal=="true"){
			setNullVal();
		}else{
			setVal();
		}
	});
	$("body").on("click", "#form-contract #person-info-sub", function() {
		util.ajax_submit('#form-contract').done(function(resp){
			if(resp['code'] == 'ACK'){
				$(".right-blk-head li:eq(2) a").trigger("click");
			}
		});
		return false;
	});
	$("body").on("click", "#contracts-wrapper #authzdctr-insert-btn", function() {
		ajaxObject = null;
		$("#contracts-wrapper").hide();
		$("#contract-detail-wrapper").show("slide");
		$('form#form-contract input[name="orgId"]').val($(this).attr("data-ctr-id"));
		$('form#form-contract span.authzd-ctr-name').text($(this).attr("data-ctr-name"));
		$('form#form-contract span.authzd-ctr-acct-no').text($(this).attr("data-ctr-no"));
		$("#form-btn-reset").attr("data-is-new","true");
		//$("#contractType").trigger("change");
		mfBread.push({
			label : "新建合同"
		});
		
		$('form#form-contract input[name="startDt"],form#form-contract input[name="endDt"]').datepicker({
			format: 'yyyy-mm-dd',
            autoclose: true,
            language: 'zh-CN'
        });
		return false;
	});
/*	
	$("#contractType").on("change",function(){
		var type = $(this).val();
		if(type=="A"){
			$(".type_a").show();
			$(".type_b").hide();
		}else{
			$(".type_b").show();
			$(".type_a").hide();
		}
	});
	
	$("#contractType").trigger("change");
*/
	$("#search-start-date").datepicker({
		 autoclose: true,
		 format: "yyyy-mm-dd",
		 language: 'zh-CN'
	});
	
	$("#search-end-date").datepicker({
		 autoclose: true,
		 format: "yyyy-mm-dd",
		 language: 'zh-CN'
	});
	$("#search-area").on("keydown",function(e){
		if(e.which == 13){
			$("#authzdctr-search-btn").trigger("click");
			return false;
		}
	});
});
