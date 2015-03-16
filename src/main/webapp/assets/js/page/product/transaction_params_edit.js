require(['jquery',
         'module/util',
         'form',
         'global',
         'module/view_pusher',
         'bootstrap',
         'bootstrap-datepicker',
         'bootstrap-datetimepicker',
         'select2',
         'plugins',
         'requirejs/domready!'], 
function($, util, form, global,pusher){
	var finance_amount = $("#finance-amount").attr("value");
	var edit_finance_amount = finance_amount;
	var feeRt = 0.0;
	var finance_term = $('#funding-duration-type').html();
	var option_html = '';
	
	var temp_finance_amount = finance_amount;
	var finance_amount = $("#remain_sum").html();
	var html = "";
	
	var init = function(){
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
		
		$(".vote-group").select2();
		
		reset_index();
	};
	
	var bindEvent = function(){
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
		
		$('#project-params input').attr("readonly", "readonly");
		
		
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
		 * 即时发布事件
		 * */
		$('body').on('click', '.immediate', function(){
			var me = this;
			var parent_div = $(me).parents('.rzb');
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
				parent_div.find('.order-time').val(parent_div.find('.order-time').attr('data-value'));
				parent_div.find('.start-time').val(parent_div.find('.start-time').attr('data-value'));
				parent_div.find('.start-time-aipStartTime').val(parent_div.find('.start-time-aipStartTime').attr('data-value'));
				parent_div.find('.order-time').removeAttr('disabled');
				parent_div.find('.start-time').removeAttr('disabled');
				parent_div.find('.start-time-aipStartTime').removeAttr('disabled');
				parent_div.find('.order-time').attr('data-validate','{required:true}');
				parent_div.find('.start-time').attr('data-validate','{required:true}');
				parent_div.find('.start-time-aipStartTime').attr('data-validate','{required:true}');
			}
		});
		/**
		 * 根据融资包状态显示信息
		 */
		$('.rzb').each(function(index,element){
			
			var package_status = $(element).find('.package-status').val();
			if (package_status == 1){//待申购
				/*$(element).find('.start-time, .end-time, .end-time-aipEndTime').datetimepicker({
					format: 'yyyy-mm-dd hh:00',
					weekStart: 1,
				    startView: 2,
					minView: 1,
					autoclose: true
				});*/
			}
			if (package_status == 2){//申购中
				/*$(element).find('.end-time').datetimepicker({
					format: 'yyyy-mm-dd hh:00',
					weekStart: 1,
				    startView: 2,
					minView: 1,
					autoclose: true
				});
				$(element).find('.end-time-aipEndTime').datetimepicker({
					format: 'yyyy-mm-dd hh:00',
					weekStart: 1,
				    startView: 2,
					minView: 1,
					autoclose: true
				});*/
				$(element).find('.order-time, .start-time, .start-time-aipStartTime').attr("disabled","disabled");
			}
			
			if (package_status == 3){//申购完成
				$(element).find('.order-time, .start-time,.end-time, .start-time-aipStartTime, .end-time-aipEndTime').attr("disabled","disabled");
			}
			
			datetimepicker(element);
			/*var date = new Date();
			date.setHours(date.getHours()+1);*/
			/*$(element).find(".order-time").datetimepicker('setStartDate', date);

			$(element).find(".start-time").datetimepicker('setStartDate', date);
			$(element).find(".end-time").datetimepicker('setStartDate', date);
			//$(".start-time-aipStartTime").datetimepicker('setStartDate', date);
			$(element).find(".end-time-aipEndTime").datetimepicker('setStartDate', date);*/
			
			if ($('.chk-vote', element).is(':checked')){
				var data = $(element).find('.group-value').html();
				$(element).find('.vote-content').show(function(){
					$(element).find('select').html(option_html);
					$(element).find('.vote-group').select2('data', $.parseJSON(data));
					$(element).find('.vote-group').change();
				});
			}
		});
		
		//平均分项挂单
		$("#chk-avgunpaking").on("click", function(){
			finance_amount = temp_finance_amount;
			if ($("#chk-avgunpaking").is(":checked")) {
				var unpakingNum = $("input[name='itemCount']").val();
				finance_amount /= unpakingNum;
				finance_amount = convertDecimal(finance_amount);
				$("#unpaking-content .contract-templ").val(finance_amount);
				$("#unpaking-content .contract-templ").attr("readonly", "true");
				finance_amount = temp_finance_amount - convertDecimal((unpakingNum-1) * finance_amount);
				$("#unpaking-content .contract-templ").last().val(convertDecimal(finance_amount));
			}else{
				$('#remain_sum').html();
				finance_amount = 0;
				$("#unpaking-content .contract-templ").removeAttr("readonly");
			}
		});
		
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
		
		/*改变融资包金额事件*/
		$("body").on("focus", ".contract-templ", function(){
			var me = this;
			value = $(me).val();
		}).on("change", ".contract-templ", function(){
			var me = this;
			if (isNaN($(me).val())){
				$(me).val(value).focus();
			}else if("" == $(me).val()){
				$(me).val("0.0");
				finance_amount = finance_amount * 1 + value * 1;
			}else {
				var amount = $(me).val();
				$(me).val(convertDecimal($(me).val()));
				finance_amount = finance_amount * 1 + value * 1 - amount;
			}
			finance_amount = convertDecimal(finance_amount);
			$('#remain_sum').html(finance_amount);
			$('.fixed').show('1000');
		});
		
		/*定向发行事件*/
		$("body").on("click", ".chk-vote", function(){
			var me = this;
			var data_role = $(me).attr("data-role");
			var parent_div = $(me).parents('.rzb');
			if ($(me).is(":checked")) {
				parent_div.find('.start-time-aipStartTime').val(parent_div.find(".start-time").val());
				parent_div.find('.vote-content').show('1000',function(){
					$(this).find("select").html(option_html);
				});
			}else{
				parent_div.find('select.vote-group').select2('data', []);
				parent_div.find('.start-time-aipStartTime').val('');
				parent_div.find('.end-time-aipEndTime').val('');
				parent_div.find('.vote-content').hide("1000");
			}
			$(window).trigger("resize");
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
			var apply_name = $('.rdo-auto-apply:checked').attr('name');
			if ('undefined' == typeof(apply_name)){
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
		
		var options = {url:null, action:null, context:null, mode:null,context1:null,list:null};
		/* 表单验证 */
		$('#transactionparams-form').validate({
			submitHandler : function(form) {
				$(form).find('.select2-input').each(function(){
	                $(this).attr("name","select2Name");
	                $(this).attr("data-ignore","true");
				});
				
				options.context = ".package";
				options.mode = "arrayMode";
				options.context1 = ".flat";
				options.list=".list";
				
				$('.contract-templ').attr('name','packageQuota');
				$('.order-time').attr('name','prePublicTime');
				$('.start-time').attr('name','supplyStartTime');
				$('.end-time').attr('name','supplyEndTime');
				//$('.order-time').attr('name','prePublicTime');
				$('.start-time-aipStartTime').attr('name','aipStartTime');
				$('.end-time-aipEndTime').attr('name','aipEndTime');
				
				util.ajax_submit('#transactionparams-form',options).done(function(response){
					var _response = eval(response);
					if(_response.code == "SUCCESS"){
								modal_content.modal_header = '提示';
								modal_content.modal_body = '交易参数设置更新成功！';
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
	
	/*重置编号事件*/
	function reset_index(){
		$('.rzb').each(function(index, element){
			index++;
			$('.amount-count', $(this)).html(Utils.numberToChinese(index));
			
		});
	}
	
	function term(value){
		if (finance_term == '天'){
			feeRt = convertDecimal(edit_finance_amount * value / 100 / 30 * ($('#funding-duration').html()));
		}else {
			feeRt = convertDecimal(edit_finance_amount * value / 100 * ($('#funding-duration').html()));
		}
	}
	
	function datetimepicker(ele){
		var date = new Date();
		date.setHours(date.getHours()+1);
		
		$(ele).find(".end-time, .end-time-aipEndTime, .order-time").focus(function(){
			var $this = $(this);
			if(!$this.data('datetimepicker')){
				$this.datetimepicker({
					format: 'yyyy-mm-dd hh:00',
					weekStart: 1,
				    startView: 2,
					minView: 1,
					autoclose: 1,
					startDate:date
			    });
				$this.datetimepicker('show');
			}
			
		});
		
		$(ele).find(".start-time").focus(function(){
			var $this = $(this);
			if(!$this.data('datetimepicker')){
				$this.datetimepicker({
			    	format: 'yyyy-mm-dd hh:00',
			        weekStart: 1,
			        autoclose: true,
			        startView: 2,
			        minView: 1,
			        startDate:date
			    }).on("changeDate", function(e){
			    	var $this = $(this);
			    	var parent_package = $this.parents(".rzb");
			    	if(parent_package.find(".chk-vote").is(":checked")){
			    		parent_package.find(".start-time-aipStartTime").val($this.val());
			    	}
			    	
			    });
				$this.datetimepicker('show');
			}
		});
	}
	
	/**
	 * 显示列表当前数据
	 */
	function loadLocalPage(){
		 $('#table-search-result').dataTable().fnPageChange(0,true); 
		  pusher.pop();
		/*$('#finance-details').hide("1000",function(){
		      $('#finance-product').show();
		      var page = $('#table-search-result').dataTable().fnPagingInfo().iPage;
		      $('#table-search-result').dataTable().fnDraw();
		      $('#table-search-result').dataTable().fnPageChange(page,true);
		});*/
	};
	
//	$('body').on('changeDate', '.js-time-primary', function(){
//	var me = this;
//	var data_name = $(me).attr('data-name');
//	
//	var label_name = $(me).attr('name');
//	$('label[for='+ label_name +']').remove();
//	$(me).removeClass('invalid');
//	$(me).addClass('valid');
//	
//	var orderTime = $(me).parents('.package-all-index').find('.order-time').val();
//	var currentStartTime = $(me).parents('.package-all-index').find('.start-time').val();
//	var currentEndTime = $(me).parents('.package-all-index').find('.end-time').val();
//	var aipStartTime = $(me).parents('.package-all-index').find('.start-time-aipStartTime').val();
//	var aipEndTime = $(me).parents('.package-all-index').find('.end-time-aipEndTime').val();
//	
//	if ('order-time' == data_name){
//		$(me).parents('.package-all-index').find('.start-time').datetimepicker('setStartDate', orderTime);
//		$(me).parents('.package-all-index').find('.end-time').datetimepicker('setStartDate', orderTime);
//		$(me).parents('.package-all-index').find('.start-time-aipStartTime').datetimepicker('setStartDate', orderTime);
//		$(me).parents('.package-all-index').find('.end-time-aipEndTime').datetimepicker('setStartDate', orderTime);
//	}else if ('start-time' == data_name){
//		aipTime();
//		$(me).parents('.package-all-index').find('.order-time').datetimepicker('setEndDate', currentStartTime);
//		$(me).parents('.package-all-index').find('.end-time').datetimepicker('setStartDate', currentStartTime);
//	}else if ('end-time' == data_name){
//		aipTime();
//		$(me).parents('.package-all-index').find('.order-time').datetimepicker('setEndDate', currentEndTime);
//		$(me).parents('.package-all-index').find('.start-time').datetimepicker('setEndDate', currentEndTime);
//	}else if ('start-time-aipStartTime' == data_name){
//		$(me).parents('.package-all-index').find('.order-time').datetimepicker('setStartDate', aipStartTime);
//		$(me).parents('.package-all-index').find('.end-time-aipEndTime').datetimepicker('setStartDate', aipStartTime);
//	}else if ('end-time-aipEndTime' == data_name){
//		$(me).parents('.package-all-index').find('.order-time').datetimepicker('setEndDate', aipEndTime);
//		$(me).parents('.package-all-index').find('.start-time-aipStartTime').datetimepicker('setEndDate', aipEndTime);
//	}
//	
//	function aipTime(){
//		$(me).parents('.package-all-index').find('.start-time-aipStartTime').val("");
//		$(me).parents('.package-all-index').find('.end-time-aipEndTime').val("");
//			
//		$(me).parents('.package-all-index').find('.start-time-aipStartTime').datetimepicker('setStartDate', currentStartTime);
//		$(me).parents('.package-all-index').find('.start-time-aipStartTime').datetimepicker('setEndDate', currentEndTime);
//			
//		$(me).parents('.package-all-index').find('.end-time-aipEndTime').datetimepicker('setStartDate', currentStartTime);
//		$(me).parents('.package-all-index').find('.end-time-aipEndTime').datetimepicker('setEndDate', currentEndTime);
//		}
//});

	function convertDecimal(x){
		var f = parseFloat(x);
		if (isNaN(f)){
			return false;
		}
		
		var f = Math.round(x * 100) / 100;
		var s = f.toString();
		var rs = s.indexOf('.');
		
		if (rs < 0){
			rs = s.length;
			s += '.';
		}
		
		while (s.length <= rs+2){
			s += '0';
		}
		
		return s;
	}

//$('.rzb').each(function(index, elements){
//	var amount = $('.contract-templ', elements).val();
//	$('.contract-templ', elements).val(util.get_thousand(amount));
//});
	
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
	
	init();
	bindEvent();
});
