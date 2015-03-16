require(['jquery',
         'jquery.pnotify',
         'global',
         'module/util',
         'module/view_pusher',
         'module/datatables',
         'form',
         'select2',
         'requirejs/domready!'], 
function($, popup, global, util, pusher, datatables, form){
	
	var validator = '';
	
	var _fnNameLink = function(data){
        var detailLink = '<a class="go-details" data-id="'+data.packageId+'">'+data.packageName+'</a>';
        return detailLink;
    };
	var _fnOperateBtn = function(data){
		var clearbtn = !(data.cleared) ? '<button type="button" class="btn btn-mf-primary btn-clear" data-id="'+ data.packageId 
				                																+'"  data-status="'+ data.payStatus.text
				                																+'"  data-amount="'+ data.financerAmount
				                                                                                +'"  data-user="'+ data.financerId 
				                                                                                +'"  data-period="'+ data.sequenceId 
				                                                                                +'"  data-debt="'+ data.debt 
				                                                                                +'"  data-total-amt="'+ (data.totalAmount||0.00)
				                                                                                +'"  title="清分">'
				                                                                                +'<i class="fa fa-fire-extinguisher fa-lg" title="清分"></i></button>' : '';
		var operateHTML = '<div class="btn-group btn-group-xs">'+ clearbtn +'</div>';
		return operateHTML;
	};
	var _fnLogLink = function(data){
		return "<a class='view-log' data-id='"+data.packageId+"'>日志</a>";
	};
	
	var _fnWarrantAmount = function(data){
		if(data.warrantAmount!=null){
			return '<span class="table-align-right">'+numeral(data.warrantAmount).format('0,0.00')+'</span>'; 
		}else{
			return '<span class="table-align-right">-</span>';
		}
	};
	
	var bindDatePickerByClass = function(start,end){
        var startTime =  $(start);
        var endTime = $(end);
        startTime.attr({"readonly":"readonly"});
        endTime.attr({"readonly":"readonly"});
        var datePickerSetting = {
            format: 'yyyy-mm-dd',
            weekStart: 1,
            autoclose: true,
            todayBtn: false,
            language: 'zh-CN'
        };
        startTime.datepicker(datePickerSetting);
        endTime.datepicker(datePickerSetting);
        //后面时间必须大于前面时间
        startTime.on("changeDate",function(){
            var start = startTime.val();
            endTime.datepicker('setStartDate',start);
        });
        endTime.on("changeDate",function(){
            var end = endTime.val();
            startTime.datepicker('setEndDate',end);
        });
    };
    
    bindDatePickerByClass('#search-start-date','#search-end-date');
	
	var repayment_table = null;
	repaymentTable();
	function repaymentTable(){
		var dataAction = $("#search-wrapper").attr("data-action");
		var options = {};
		options.tableId = '#table-search-result';
		options.sAjaxSource = global.context+"/web/product/search/"+dataAction;
		options.functionList = {"_fnNameLink" : _fnNameLink, "_fnWarrantAmount":_fnWarrantAmount,"_fnOperateBtn" : _fnOperateBtn, "_fnLogLink" : _fnLogLink};
	    //options.rawOptions = {"bSort": false};
		repayment_table = datatables.init(options);
		repayment_table.setParams(util.getSearchData('#search-wrapper'));
		return repayment_table.create();
	}
	var repaymentlog_table = null;
	repaymentlogTable();
	function repaymentlogTable( id ){
		var options = {};
		options.tableId = '#table-log';
		options.sAjaxSource = global.context+"/web/view/packagelog";
		options.aaSorting=[[0,"desc"]];
		repaymentlog_table = datatables.init(options);
		return repaymentlog_table.create();
	};
	
	$('#table-search-result')
	.on('click','.view-log',function(){
		var packageId = $(this).attr('data-id');
		repaymentlog_table.setParams([{name:"packageId",value:packageId}]);
		repaymentlog_table.invoke("fnDraw");
		showModal({
			type: "datatables",
			title: "日志",
			packageId: packageId,
		});
	})
	.on('click','.go-details',function(){
		var packageId = $(this).attr("data-id");
		pusher.push({
    		url:global.context+"/web/financingpackage/details/"+packageId+"/view",
    		type : 'GET',
    		element:"#packet-list",
    		title:"融资详情"
    	});
	})
	.on('click', '.btn-clear', function() {
		var $this = $(this);
		var status = $this.attr('data-status');
		var amount = $this.attr('data-amount');
		if ((status == '违约中' || status == '已代偿') && amount == 0) {
			$.pnotify({
			    text: '融资人的状态是'+ status +'，清分金额为：'+ amount +'，无法清分。',
			    type: 'error'
			});
			return;
		}
		$('#modal-clear').find('.amttip').hide();
		$('#modal-clear').modal();
		validator = $('#form-clear').validate({
			
		});
		var data = {};
		data.id = $this.attr("data-id");
		data.user = $this.attr("data-user");
		data.period = $this.attr("data-period");
		data.totalamt = $this.attr("data-total-amt");
		data.debt = $this.attr("data-debt");
		
		$("#clear-pkg-info").text("融资包"+data.id +"的第"+data.period+"期清分，欠款额为"+util.get_thousand(data.debt)+"元。 请选择清分规则：");
		$.each(data, function(key, value) {
			$('#btn-modal-tosure').attr('data-'+key, value);
		});
	})
	$('#btn-search').on('click', function(){
		repayment_table.setParams(util.getSearchData('#search-wrapper'));
		repayment_table.invoke("fnDraw");
	});
	$('#form-repayment-search').keydown(function(event){
		var keycode = event.which;  
	    if (keycode == 13) {  
	    	$('#btn-search').trigger('click');   
	    	return false;
	    }  	    
	});
	$('#modal-clear').on('click', '#btn-modal-tosure', function() {
		if (!$('#form-clear').valid()) return false;
		var $this = $(this);
		var odata = {};
		odata.packageId = $this.attr("data-id");
		odata.period = $this.attr("data-period");
		odata.nclear_amt = $('#clear-amt').val();
		var userId = $this.attr("data-user");
		var ntotal_amt = $this.attr('data-totalamt');
		var sselect_choose = $('#select-choose').val();
		var sselect_choose_text = $('.' + sselect_choose).text();
		
		var debt_amount = $(".p_prin_amt:visible").text() || $(".p_intr_amt:visible").text() || "";
		if(debt_amount){
			debt_amount = "<br>"+debt_amount+"元";
		}
		
		if ('AUTO_SHARE' == sselect_choose) {
			odata.nclear_amt = ntotal_amt;
		}
		var hints = '是否确定对融资包 '+ odata.packageId +' 的第'+ odata.period +'期'+sselect_choose_text+'<br>'+debt_amount+'<br>清分金额： '+ util.get_thousand(odata.nclear_amt)+'元';
		$('#modal-clear').modal('hide');
		
		showModal({
			type: 'notice',
			title: sselect_choose_text,
			message: hints,
			datas: {packageid: odata.packageId, period: odata.period, clearamt: odata.nclear_amt,cleartype:  sselect_choose},
			operate: 'clearchoose'
		});
	});
	$('#modal').on('click','#btn-modal-sure',function(){
		var $this = $(this);
		var packageId = $this.attr("data-packageid");
		var period = $this.attr("data-period");
		var clearAmt = $this.attr('data-clearamt');
		var clearType = $this.attr('data-cleartype');
		var odata = {};
		odata.packageId = packageId;
		odata.period = period;
		odata.clearAmt = clearAmt;
        odata.clearType = clearType;

		$.ajax({
			url: global.context + '/web/payment/clear/'+ packageId +'/'+ period +'/rules',
			type: 'POST',
			dataType: 'json',
			data: JSON.stringify(odata),
			contentType: 'application/json;charset=utf-8',
			error: function(){
			},
			success: function(response,data){
				if (response.code == 'ACK') {
					repayment_table.invoke("fnDraw");
					$('#modal').modal('hide');
				}
			}
		});
	});
	
	$('#select-choose').on('change', function() {
		var odata = {};
		var $clear_amt = $('#clear-amt');
		var $btn_modal_tosure = $('#btn-modal-tosure');
		odata.packageId = $btn_modal_tosure.attr("data-id");
		odata.period = $btn_modal_tosure.attr("data-period");
		odata.schooseValue = $(this).val();
		var $amttip = $('#modal-clear').find('.amttip');
		$clear_amt.attr('disabled', 'disabled').val('');
		$('.modal').find('label.invalid').remove();
		if ('AUTO_SHARE' == odata.schooseValue) {
			$amttip.hide();
		}else {
			$amttip.hide().show();
			if ('PRIN_CLEAR' == odata.schooseValue) {
				$('.p_intr_amt').hide();
				$('.p_prin_amt').show();
			}
			if ('INTR_CLEAR' == odata.schooseValue) {
				$('.p_prin_amt').hide();
				$('.p_intr_amt').show();
			}
			$clear_amt.removeAttr('disabled');
			_ajax(odata);
		}
	});
	
	$('.modal').on('hidden.bs.modal', function() {
		var $this = $(this);
		$this.find('form').each(function() {
			this.reset();
		})
		$this.find('label.invalid').remove();
	});
	
	function _ajax(odata) {
		$.ajax({
			url: global.context+'/web/payment/clear/amt/info/'+ odata.packageId +'/'+ odata.period +'',
			type: 'GET',
			contentType: 'application/json;charset=utf-8',
			error: function() {
			},
			success: function(resp) {
				var ncanpayamt = '';
				var resp = JSON.parse(resp);
				var amt_html = '';
				if ('PRIN_CLEAR' == odata.schooseValue){
					if (resp.totalCanPaymentAmt < resp.prinAmt) {
						ncanpayamt = resp.totalCanPaymentAmt;
					}else {
						ncanpayamt = resp.prinAmt;
					}
					amt_html = '<p class="p_prin_amt">本金欠款：<span id="prin_amt"></span></p>';
				}else if('INTR_CLEAR' == odata.schooseValue) {
					if (resp.totalCanPaymentAmt < resp.intrAmt) {
						ncanpayamt = resp.totalCanPaymentAmt;
					}else {
						ncanpayamt = resp.intrAmt;
					}
					amt_html = '<p class="p_intr_amt">利息欠款：<span id="intr_amt"></span></p>';
				}
				var clear_hidden = '<div class="form-group amttip">\
						            	<label class="col-sm-3 control-label required">清分金额</label>\
						            	<div class="col-sm-6">\
						            		<input class="form-control" id="clear-amt" name="clearAmt" data-validate="{required: true, min: 0.01, fixednumber: 2, max: '+ ncanpayamt +'}" data-hidden-validate="true">\
						            	</div>\
						            </div>\
						            <div class="form-group amttip">\
						            	<div class="col-sm-offset-3" style="padding-left:15px;">\
						            		<p class="p_canpay_amt">可还款金额：<span id="can_payment_amt"></span></p>\
						            		'+ amt_html +'\
						            	</div>\
						            </div>';
				$('#clear-hidden').empty().append(clear_hidden);
				$('#form-clear').validate({
					
				});
				//validator.resetForm();
				$('#can_payment_amt').text(util.get_thousand(resp.totalCanPaymentAmt));
				$('#prin_amt').text(util.get_thousand(resp.prinAmt));
				$('#intr_amt').text(util.get_thousand(resp.intrAmt));
			}
		})
	}
	
	function showModal(object){
		if(object.type == "datatables"){
			$('#modal-title').html(object.title);
			$('#modal .modal-table').show();
			$('#modal .modal-notice').hide();
			$('#modal .modal-footer').hide();
		}else if(object.type == "notice"){
			$('#modal-title').html(object.title);
			$('#modal .modal-table').hide();
			$('#modal .modal-notice').html(object.message).show();
			$('#modal .modal-footer').show();
			$('#modal #btn-modal-sure').attr("data-type",object.operate);
			$.each(object.datas,function(key,value){
				$('#modal #btn-modal-sure').attr("data-"+key,value);
			});
		}
		$('#modal').modal('show');
	}
	
});
