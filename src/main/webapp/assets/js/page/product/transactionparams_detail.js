require(['jquery',
         'module/util',
         'form',
         'global',
         'module/view_pusher',
         'numeral',
         'bootstrap',
         'bootstrap-datepicker',
         'bootstrap-datetimepicker',
         'select2',
         'plugins',
         'requirejs/domready!'], 
function($, util, form, global,pusher){

	// 定义面值金额
	var _unitFaceValue = 1000;
	
	var finance_amount = $("#finance-amount").attr("value");
	var edit_finance_amount = finance_amount;
	var temp_finance_amount = finance_amount;
	var html = "";
	var feeRt = 0.0;
	var productname = $('.product-name').html();
	
	var finance_term = $('#funding-duration-type').html();
	
	$("#unpaking-content .contract-templ").val(convertDecimal(finance_amount));
	
	var option_html = '';
	
	var reCalculateGold = function(){
        var finance_amount = temp_finance_amount/_unitFaceValue;
        if ($("#chk-avgunpaking").is(":checked")) {
            var unpakingNum = $("#txt-unpaking-num").val();
            var each = Math.floor(finance_amount/unpakingNum);
            each = convertDecimal(each);
            $("#unpaking .contract-templ").val(each*_unitFaceValue+'.00');
            $("#unpaking .contract-templ").attr("readonly", "true");
            var last = finance_amount - each*(unpakingNum-1);
            $("#unpaking-content .contract-templ").val(convertDecimal(last)*_unitFaceValue+".00");
        }else{
            finance_amount = convertDecimal(finance_amount);
            $('.package .contract-templ').val('0.00');
            $("#unpaking-content .contract-templ").val(finance_amount*_unitFaceValue+'.00');
            $(".package .contract-templ").removeAttr("readonly");
            $("#unpaking-content .contract-templ").attr("readonly", "true");
        }
    };
	var init = function(){
		
		//updateDataErrorField();
		
		/**
		 * 计算风险管理费、融资服务费、借款合同履约保证金
		 */
		$('div[data-name="productFeeList"]').each(function(index, element){
			var temp_value = $('input[name="feeRt"]', element).val();
			term(temp_value);
			$('.feeName', element).html(util.get_thousand(feeRt));
		});
		
		var temp_value = $('input[name="performanceBondRate"]').val();
		$('.performanceBondRate').html(util.get_thousand(edit_finance_amount * temp_value / 100));
		
		/**
		 *  空时格式化风险管理费率，融资服务费率，借款合同履约保证金
		 */
		$('input[name="feeRt"]').on('change', function(){
			var me = $(this);
			var sValue = me.val();
			if ("" == sValue){
				me.val(me.attr('data-value'));
				return;
			}
			//保留四位小数
			var replace=sValue.replace(/(\.\d{6})\d+/ig,"$1");
			me.val(replace);
		});
		
		$('input[name="performanceBondRate"]').on('change', function(){
			var me = $(this);
			var sValue = me.val();
			if ("" ==sValue){
				me.val(me.attr('data-value'));
				return;
			}
			//保留四位小数
			var replace=sValue.replace(/(\.\d)\d+/ig,"$1");
			me.val(replace);
		});
		
		$(".vote-group").select2();
		
		/**
		 * 获取所有定投组
		 */
		$.ajax({
			url: global.context+"/web/financingpackage/getallgroups",
			contentType:"application/json"
		}).done(function(response){
			var resp = $.parseJSON(response);
			for(var i=0;i<resp.length;i++){
				option_html += "<option value="+resp[i].groupId+">"+resp[i].groupName+"</option>";
			}
		});
		
		datetimepicker();
		
		prepareDateEvent();
		
	};
	
	
	
	var bindEvent = function(){
		//updateDataErrorField();
		
		$('input[name="performanceBondRate"]').on('blur', function(){
			var temp_value = $(this).val();
			temp_value = numeral(temp_value).format('0.0');
			$('.performanceBondRate').html(util.get_thousand(edit_finance_amount * temp_value / 100));
			$(this).val(numeral(temp_value).format('0.0'));
		});
		
		$('input[name="feeRt"]').on('blur', function(){
			var temp_value = $(this).val();
			temp_value = numeral(temp_value).format('0.000000');
			term(temp_value);
			$(this).parents('div[data-name="productFeeList"]').find('.feeName').html(util.get_thousand(feeRt));
			$(this).val(numeral(temp_value).format('0.000000'));
		});
		
		/**
		 * 分项挂单事件
		 */
		$("#chk-unpaking").on("click", function(){
			if ($("#chk-unpaking").is(":checked")) {
				$("#unpaking-num").show("1000");
				$(".avg-unpaking").show("1000");
				$('.add-package').show('1000');
				$("#txt-unpaking-num").val("2");
				addPackage();
				$("#txt-unpaking-num").focus();
			}else{
				$("#chk-avgunpaking").removeAttr('checked');
				$('.add-package').hide('1000');
				$(".avg-unpaking").hide("1000");
				$("#unpaking-num").hide("1000");
				$("#unpaking").empty();
				finance_amount = temp_finance_amount;
				$("#unpaking-content .contract-templ").val(convertDecimal(finance_amount));
				$("#txt-unpaking-num").val("");
				$(".product-name").html(productname);
				$("#unpaking-content .amount-count").html("1");
				$("#unpaking-content .package-index").val('1');
			}
			$(window).trigger("resize");
			reset_index();
			prepareDateEvent();
		});
		
		/*添加融资包事件*/
		$('.add-package').on('click',function(){
			var temp_num = $('#txt-unpaking-num').val();
			$('#txt-unpaking-num').attr('readonly', 'true');
			var html = to_html(temp_num, new Date().valueOf(), productname);
			$(".product-name").html(productname+"-"+(temp_num * 1 + 1));
			$('#txt-unpaking-num').val((temp_num * 1 + 1));
			$("#unpaking-content .amount-count").html((temp_num * 1 + 1));
			$('.package-index').attr('value', (temp_num * 1 + 1));
			$("#unpaking").append(html);
			$(window).trigger("resize");
//			$('select .vote-group').select2();
			datetimepicker();
			reset_index();
			prepareDateEvent();
			if ($("#chk-avgunpaking").is(":checked")) {
				reCalculateGold();
			}
		});
		
		/*删除融资包事件*/
		$('body').on('click.package', '.remove-package', function(){
			//updateDataErrorField();
			var me = this;
			var data_role = $(me).attr("data-role");
			var temp_finance_amount = $('.contract-templ[data-role="'+ data_role +'"]').val();
			finance_amount = temp_finance_amount * 1 + $("#unpaking-content .contract-templ").val() * 1;
			
			$(this).parents('.package-all-index').remove();
			reset_index();
			var temp_num = $('#txt-unpaking-num').val();
			$('#txt-unpaking-num').val((temp_num * 1 - 1));
			$(".product-name").html(productname+"-"+(temp_num * 1 - 1));
			$("#unpaking-content .contract-templ").val(convertDecimal(finance_amount));
			$('#txt-unpaking-num').attr('readonly', 'true');
			if ($("#chk-avgunpaking").is(":checked")) {
				reCalculateGold();
			}
		});
		
		//平均分项挂单
		$("#chk-avgunpaking").on("click", function(){
	         reCalculateGold();
		});
		
		/*改变融资包金额事件*/
		$("#unpaking").on("focus", ".contract-templ", function(){
			var me = this;
			value = $(me).val();
		}).on("change", ".contract-templ", function(){
			var me = this;
			if (isNaN($(me).val())){
				$(me).val(value).focus();
			}else if("" == $(me).val()){
				$(me).val("0.00");
				finance_amount = finance_amount * 1 + value * 1;
			}else {
				var amount = $(me).val();
				$(me).val(convertDecimal($(me).val()));
				finance_amount = finance_amount * 1 + value * 1 - amount;
			}
			$("#unpaking-content .contract-templ").val(convertDecimal(finance_amount));
			if (finance_amount < 1){
				$("#unpaking-content .contract-templ").val('0.00');
			}
		});
		
		$('body').on('click','input[type="checkbox"]',function(){
			if($(this).is(":checked")){
				$(this).val(true);
			}else{
				$(this).val(false);
			}
		});
		
		$('body').on('change','.auto-purchase input[type="radio"]',function(){
			var autoPurchaseObj=$(this).parent().parent().find("input[type='hidden']");
			if ($(this).val() == 'false') {
				autoPurchaseObj.val(false);
			} else if ($(this).val() == 'true') {
				autoPurchaseObj.val(true);
			}
		});
		
		$('body').on('change','.aip-group-ids',function(){
			var aipGroupsOjb=$(this).find("input[type='hidden']");
			var select2=$(this).find('select').select2("val");
			aipGroupsOjb.val(select2);
		});
		
		/**
		 * 即时发布事件
		 * */
		$('body').on('click', '.immediate', function(){
			var me = this;
			var parent_div = $(me).parents('.package-all-index');
			if ($(me).is(":checked")) {
				parent_div.find('.order-time').val('');
				parent_div.find('.start-time').val('');
				parent_div.find('.start-time-aipStartTime').val('');
				parent_div.find('.order-time').attr('disabled','disabled');
				parent_div.find('.start-time').attr('disabled','disabled');
				parent_div.find('.start-time-aipStartTime').attr('disabled','disabled');
				parent_div.find('.order-time').removeAttr('data-validate');
				parent_div.find('.start-time').removeAttr('data-validate');
				parent_div.find('.start-time-aipStartTime').removeAttr('data-validate');
			}else{
				parent_div.find('.order-time').removeAttr('disabled');
				parent_div.find('.start-time').removeAttr('disabled');
				parent_div.find('.start-time-aipStartTime').removeAttr('disabled');
				parent_div.find('.order-time').attr('data-validate','{required:true}');
				parent_div.find('.start-time').attr('data-validate','{required:true}');
				parent_div.find('.start-time-aipStartTime').attr('data-validate','{required:true}');
			}
		});
		
		
		/*定投发行事件*/
		$("body").on("click", ".chk-vote", function(){
			var me = this;
			var parent_div = $(me).parents('.package-all-index');
			if ($(me).is(":checked")) {
				parent_div.find('.vote-content').show('1000',function(){
					$(this).find("select").html(option_html);
					$(me).find('.start-time-aipStartTime').addClass('required');
					parent_div.find('.start-time-aipStartTime').val(parent_div.find(".start-time").val());
					$(me).find('.end-time-aipEndTime').attr('data-validate', '{required:true}');
				});
			}else{
				parent_div.find('.start-time-aipStartTime').val('');
				parent_div.find('.end-time-aipEndTime').val('');
				parent_div.find('.vote-group').val('');
				parent_div.find('.vote-content').hide("1000");
			}
			$(window).trigger('resize');
			$('select.vote-group').select2();
			datetimepicker();
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
		
		/*委托自动申购事件*/
		$('body').on('click', '.rdo-auto-apply', function(){
			$('.auto-apply').show("1000");
		});
		
		/*对外手工申购事件*/
		$('body').on('click', '.rdo-hand-apply', function(){
			if ('undefined' == typeof($('.rdo-auto-apply:checked').attr('name'))){
				$('.auto-apply').hide("1000");
			}
		});
		
		/**
		 * 确定事件
		 */
		$('#btn-trans-settings').on('click', function(){
			$('.package-all-index').each(function(index, element){
				if(!$(element).attr('name')){
					$('.select2-input', element).attr('name', 'select2Name');
					$('.select2-input', element).attr('data-ignore', 'true');
				}
			});
		});
		
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
			$("#project-modal-header").empty().html(modal_content.modal_header);
			$("#project-modal-body").empty().html(modal_content.modal_body);
			$("#project-modal-footer").empty().html(modal_content.modal_footer);
			$("#project-modal").modal(modal_content.modal_data);
		} ;
		/**
		 * 显示列表当前数据
		 */
		function loadLocalPage(){
			 $('#table-search-result').dataTable().fnPageChange(0,true); 
			  pusher.pop();
		};
		
		var options = {url:null, action:null, context:null, mode:null,context1:null,list:null};
		
		/* 表单验证 */
		$('#transactionparams-form').validate({
			submitHandler : function(form) {
				options.context = ".package";
				options.mode = "arrayMode";
				options.context1 = ".flat";
				options.list=".list";
				
				$('.contract-templ').attr('name','packageQuota');
				$('.order-time').attr('name','prePublicTime');
				$('.start-time').attr('name','supplyStartTime');
				$('.end-time').attr('name','supplyEndTime');
				$('.start-time-aipStartTime').attr('name','aipStartTime');
				$('.end-time-aipEndTime').attr('name','aipEndTime');
				updateDataErrorField();
				
				$('label.invalid').each(function(){
					$(this).remove();
				});
				
				util.ajax_submit('#transactionparams-form',options).done(function(response){
					var _response = eval(response);
					if(_response.code == "SUCCESS"){
								modal_content.modal_header = '提示';
								modal_content.modal_body = '交易参数设置成功，融资项目状态更新为“已发布”！';
								modal_content.modal_footer = '<button type="button" class="btn btn-mf-primary" id="prompt-btn" data-dismiss="modal">确定</button>';
								renderModal(modal_content);
								$('#prompt-btn').click(function(){
									loadLocalPage();
								});
					}
				});
			}
		});
		
	};
	
	var Utils = {
			/**
			 * 数字转中文
			 * 
			 * @number {Integer} 形如123的数字
			 * @return {String} 返回转换成的形如 一百二十三 的字符串
			 */
			numberToChinese : function(number) {
				/*
				 * 单位
				 */
				var units = '个十百千万@#%亿^&~';
				/*
				 * 字符
				 */
				var chars = '零一二三四五六七八九';
				var a = (number + '').split(''), s = [];
				if (a.length > 12) {
					throw new Error('too big');
				} else {
					for ( var i = 0, j = a.length - 1; i <= j; i++) {
						if (j == 1 || j == 5 || j == 9) {// 两位数 处理特殊的 1*
							if (i == 0) {
								if (a[i] != '1')
									s.push(chars.charAt(a[i]));
							} else {
								s.push(chars.charAt(a[i]));
							}
						} else {
							s.push(chars.charAt(a[i]));
						}
						if (i != j) {
							s.push(units.charAt(j - i));
						}
					}
				}
				// return s;
				return s.join('').replace(/零([十百千万亿@#%^&~])/g, function(m, d, b) {// 优先处理 零百 零千 等
					b = units.indexOf(d);
					if (b != -1) {
						if (d == '亿')
							return d;
						if (d == '万')
							return d;
						if (a[j - b] == '0')
							return '零';
					}
					return '';
				}).replace(/零+/g, '零').replace(/零([万亿])/g, function(m, b) {// 零百 零千处理后 可能出现 零零相连的 再处理结尾为零的
					return b;
				}).replace(/亿[万千百]/g, '亿').replace(/[零]$/, '').replace(/[@#%^&~]/g, function(m) {
					return {
						'@' : '十',
						'#' : '百',
						'%' : '千',
						'^' : '十',
						'&' : '百',
						'~' : '千'
					}[m];
				}).replace(/([亿万])([一-九])/g, function(m, d, b, c) {
					c = units.indexOf(d);
					if (c != -1) {
						if (a[j - c] == '0')
							return d + '零' + b;
					}
					return m;
				});
			}
		};
	
	function term(value){
		if (finance_term == '天'){
			feeRt = convertDecimal(edit_finance_amount * value / 100 / 30 * ($('#funding-duration').html()));
		}else {
			feeRt = convertDecimal(edit_finance_amount * value / 100 * ($('#funding-duration').html()));
		}
	};
	
	var prepareDateEvent = function(){
		var date = new Date();
		date.setHours(date.getHours()+1);
		$(".order-time").datetimepicker('setStartDate', date);
		$(".start-time").datetimepicker('setStartDate', date);
		$(".end-time").datetimepicker('setStartDate', date);
		//$(".start-time-aipStartTime").datetimepicker('setStartDate', date);
		$(".end-time-aipEndTime").datetimepicker('setStartDate', date);
	};
	
	function addPackage(){
	//	updateDataErrorField();
		var unpakingNum = $("#txt-unpaking-num").val();
		$("#chk-avgunpaking").removeAttr('checked');
		$("#unpaking").empty();
		for (var i = 1; i <unpakingNum; i++){
			html = to_html(i, new Date().valueOf(), productname);
			$("#unpaking").append(html);
		}
		$(".product-name").html(productname+"-"+unpakingNum);
		$("#unpaking-content .amount-count").html(Utils.numberToChinese(unpakingNum));
		$("#unpaking-content .package-index").val(unpakingNum);
		datetimepicker();
	}
	
	/*重置编号事件*/
	function reset_index(){
		var index = 0;
		$('.package-all-index').each(function(k){
			index++;
			$('.amount-count', $(this)).html(Utils.numberToChinese(index));
			$('.data-role', $(this)).attr('data-role', index);
			$('.package-index', $(this)).attr('value', index);
			$('.numindex',$(this)).html(index);
		});
	}
	
	function to_html(i, index, productname){
		var packageHtml = "<div class='package package-all-index' data-name='packageList' data-role='"+ i +"'>" +
		"<div class='row'>" +
		"<div class='col-sm-6'>" +
  		"<h4>"+ productname + "-" + "<label class='numindex' style='font-weight:normal'>" + i + "</label>" +"&nbsp<span class=''></span></h4>" +
  		"</div>" +
		"<div class='col-sm-6 text-right'>" +
		"<div class='col-sm-10'>" +
		"<div class='btn-group btn-group-xs'>" +
		"<button type='button' class='btn btn-mf-primary btn-delete-data remove-package' data-role='"+ i +"'><i class='fa fa-times fa-lg' title='删除'></i></button>" +
		"</div>" +
		"</div>" +
		"</div>" +
		"</div>" +
		"<div class='row'>" +
		"<input name='index' type='hidden' value='"+ i +"'>" +
		"<div class='col-sm-6'>" +
		"<div class='form-group'>" +
		"<label class='col-sm-5 control-label pake required'>融资包<span class='amount-count'>"+ i +"</span>金额(元)</label>" +
		"<div class='col-sm-6'>" +
		"<input class='form-control contract-templ' name='packageQuota-"+ index +"' type='text' value='0.00' data-role='"+ i +"' data-validate='{required:true,number:true,min:0,maxlength:13}'>" +
		"</div>" +
		"</div>" +
		"</div>" +
		"<div class='col-sm-6'>" +
		"<div class='row'>" +
		"<div class='form-group' data-role='0'>" +
		"<label class='col-sm-4 control-label required'>" +
		"预挂单时间" +
		"</label>" +
		"<div class='col-sm-6'>" +
		"<div class='input-group date tocover'>" +
		"<input class='form-control order-time js-time-primary' name='prePublicTime-"+ index +"' type='text'" +
		"onkeypress='return false' data-name='order-time' data-validate='{required:true}'/>" +
		"<span class='input-group-addon'>" +
		"<i class='fa fa-calendar'></i>" +
		"</span>" +
		"</div>" +
		"</div>" +
		"</div>" +
		"</div>" +
		"</div>" +
		"</div>" +
		"<div class='row content-margin'>" +
		"<div class='col-sm-6'>" +
		"<div class='form-group' data-role='"+ i +"'>" +
		"<label class='col-sm-5 control-label required'>发布起始时间</label>" +
		"<div class='col-sm-6'>" +
		"<div class='input-group tocover'>" +
		"<input class='form-control start-time js-time-primary' name='supplyStartTime-"+ index +"' type='text'" +
		"data-name='start-time' onkeypress='return false' data-date-format='yyyy-mm-dd' data-validate='{required:true}'/>" +
		"<span class='input-group-addon'>" +
		"<i class='fa fa-calendar'></i>" +
		"</span>" +
		"</div>" +
		"</div>" +
		"</div>" +
//		"<div class='form-group' data-role='"+ i +"'>" +
//		"<label class='col-sm-5 control-label content-margin'>" +
//		"委托自动申购" +
//		"</label>" +
//		"<div class='col-sm-7 content-margin auto-purchase'>" +
//		"<label class='inline control-label'>"+
//		"<input class='auto-sub rdo-hand-apply' type='radio'  data-ignore='true' name='uselessName-"+index+"' value='false' checked='checked'>&nbsp对外手工申购"+
//		"</label><label class='inline control-label'>"+
//		"&nbsp<input class='auto-sub rdo-auto-apply' type='radio'  data-ignore='true' name='uselessName-"+index+"' value='true'>委托自动申购</label>" +
//		"<input type='hidden'  name='autoSubscribeFlag' value='false'/>"+
//		"</div>" +
//		"</div>" +
		"<div class='form-group' data-role='"+ i +"'>" +
		"<div class='col-sm-5 control-label'>" +
		"<label class='checkbox inline control-label'>" +
		"<input class='chk-vote data-role' type='checkbox' value='false' name='aipFlag' data-role='"+ i +"'>&nbsp定向发行" +
		"</label>" +
		"</div>" +
		"<div class='col-sm-6 vote-content required aip-group-ids' data-role='"+ i +"'>" +
		"<input type='hidden' name='aipGroupIds' value=''/>"+
		"<select class='vote-group' name='vote-group-"+ index +"' data-ignore='true' multiple='multiple' data-validate='{required:true}'>" +
		"</select>" +
		"</div>" +
		"</div>" +
		"</div>" +
		"<div class='col-sm-6'>" +
		"<div class='row'>" +
		"<div class='form-group data-role' data-role='"+ i +"'>" +
		"<label class='col-sm-4 control-label required'>发布截止时间</label>" +
		"<div class='col-sm-6'>" +
		"<div class='input-group tocover'>" +
		"<input class='form-control end-time js-time-primary' name='supplyEndTime-"+ index +"' type='text'" +
		"data-name='end-time' onkeypress='return false' data-date-format='yyyy-mm-dd' data-validate='{required:true}'/>" +
		"<span class='input-group-addon'>" +
		"<i class='fa fa-calendar'></i>" +
		"</span>" +
		"</div>" +
		"</div>" +
		"</div>" +
		"</div>" +
		"<div class='form-group data-role' data-role='0'>"+
		"<div class='col-sm-5 control-label'>" +
		"<label class='checkbox inline control-label'>" +
		"<input class='auto-sub data-role immediate' name='instantPublish' type='checkbox' data-role='0' value='false'/>" +
		"&nbsp即时发布" +
		"</label>" +
		"</div>" +
		"</div>" +
		"</div>" +
		"</div>" +
		"<div class='row content-margin'>" +
		"<div class='col-sm-6'>" +
		"<div class='form-group vote-content data-role' data-role='"+ i +"'>" +
		"<label class='col-sm-5 control-label required'>定向起始时间</label>" +
		"<div class='col-sm-6'>" +
		"<div class='input-group tocover'>" +
		"<input class='form-control start-time-aipStartTime js-time-primary' name='aipStartTime-"+ index +"' type='text'" +
		"data-name='start-time-aipStartTime' onkeypress='return false' data-date-format='yyyy-mm-dd' data-validate='{required:true}' readonly/>" +
		"<span class='input-group-addon'>" +
		"<i class='fa fa-calendar'></i>" +
		"</span>" +
		"</div>" +
		"</div>" +
		"</div>" +
		"</div>" +
		"<div class='col-sm-6'>" +
		"<div class='row'>" +
		"<div class='form-group vote-content data-role' data-role='"+ i +"'>" +
		"<label class='col-sm-4 control-label required'>定向截止时间</label>" +
		"<div class='col-sm-6'>" +
		"<div class='input-group tocover'>" +
		"<input class='form-control end-time-aipEndTime js-time-primary' name='aipEndTime-"+ index +"' type='text'" +
		"data-role='0' data-name='end-time-aipEndTime' onkeypress='return false' data-date-format='yyyy-mm-dd' data-validate='{required:true}'/>" +
		"<span class='input-group-addon'>" +
		"<i class='fa fa-calendar'></i>" +
		"</span>" +
		"</div>" +
		"</div>" +
		"</div>" +
		"</div>" +
		"</div>" +
		"</div>" +
		"</div>";
		return packageHtml;
	}
	
	function term(value){
		if (finance_term == '天'){
			feeRt = convertDecimal(edit_finance_amount * value / 100 / 30 * ($('#funding-duration').html()));
		}else {
			feeRt = convertDecimal(edit_finance_amount * value / 100 * ($('#funding-duration').html()));
		}
	};
	
	function datetimepicker(){
		$("#unpaking .start-time,#unpaking .end-time,.end-time, .end-time-aipEndTime, .order-time").datetimepicker({
	        format: 'yyyy-mm-dd hh:00',
	        weekStart: 1,
	        autoclose: true,
	        startView: 2,
	        minView: 1
	    });
	    
	    $(".start-time").datetimepicker({
	    	format: 'yyyy-mm-dd hh:00',
	        weekStart: 1,
	        autoclose: true,
	        startView: 2,
	        minView: 1
	    }).on("changeDate", function(e){
	    	var $this = $(this);
	    	var parent_package = $this.parents(".package-all-index");
	    	if(parent_package.find(".chk-vote").is(":checked")){
	    		parent_package.find(".start-time-aipStartTime").val($this.val());
	    	}
	    	
	    });
	    
	}
	
	function updateDataErrorField(){
		$('#is-unpaking .package').each(function(i){
			$(this).attr('data-role', i+1);
			var dataParentName = $(this).attr('data-name');
			$(this).find('input').each(function(){
				$(this).removeAttr('data-error-field');
				var attrName = $(this).attr('name');
				if(attrName && attrName.indexOf('-')>0){
					attrName = attrName.substring(0,attrName.length-14);
				}
				$(this).attr('data-error-field', 
						dataParentName+'['+i+'].'+attrName);
			});
		});
	}
	
	function convertDecimal(x){
		return x;
	}
	
	init();
	bindEvent();
	
});
