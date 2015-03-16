require(['jquery',
         'global',
         'module/util',
         'module/view_pusher',
         'module/datatables',
         'form',
         'select2',
         'requirejs/domready!'], 
function($, global, util, pusher, datatables, form){
	
	var _fnNameLink = function(data){
        var detailLink = '<a class="go-details" data-id="'+data.packageId+'">'+data.packageName+'</a>';
        return detailLink;
    };
	var _fnOperateBtn = function(data){
		var clearbtn = !(data.cleared) ? '<button type="button" class="btn btn-mf-primary btn-clear" data-id="'+ data.packageId 
				                                                                                +'"  data-user="'+ data.financerId 
				                                                                                +'"  data-period="'+ data.sequenceId 
				                                                                                +'"  data-total-amt="'+ (data.totalAmount||0.00)
				                                                                                +'"  title="清分">'
				                                                                                +'<i class="fa fa-fire-extinguisher fa-lg" title="清分"></i></button>' : '';
		var unfreezebtn = (data.cleared) ? '<button type="button" class="btn btn-mf-primary btn-unfreeze" data-id="'+ data.packageId +'"  data-user="'+ data.financerId +'"  data-period="'+ data.sequenceId +'" title="解冻">'+
				'<i class="fa fa-unlock-alt fa-lg" title="解冻"></i></button>' : '';
		var transferbtn = (data.cleared) ? '<button type="button" class="btn btn-mf-primary btn-transfer" data-id="'+ data.packageId +'"  data-user="'+ data.financerId +'"  data-period="'+ data.sequenceId +'" title="解冻划转">'+
				'<i class="fa fa-arrows-h fa-lg" title="解冻划转"></i></button' : '';
		var operateHTML = '<div class="btn-group btn-group-xs">'+ clearbtn + unfreezebtn + transferbtn +'</div>';
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
	.on('click','.btn-clear',function(){
		var packageId = $(this).attr("data-id");
		var userId = $(this).attr("data-user");
		var period = $(this).attr("data-period");
		var totalAmt = $(this).attr("data-total-amt");
		var hints = "是否确定对 "+packageId+" 融资包的第"+period+"期,可还款金额为 "+totalAmt+" 元进行清分？";
		showModal({
			type: "notice",
			title: "清分",
			message: hints,
			datas: {packageId:packageId,userId:userId,period:period},
			operate: "clear"
		});
	})
	.on('click','.btn-unfreeze',function(){
		var packageId = $(this).attr("data-id");
		var userId = $(this).attr("data-user");
		var period = $(this).attr("data-period");
		var hints = "点击确定将会对"+packageId+"融资包的第"+period+"期的融资人账户进行解冻，但不对代偿金额进行划转，同时此还款计划结束，是否确定操作？";
		showModal({
			type: "notice",
			title: "解冻",
			message: hints,
			datas: {packageId:packageId,userId:userId,period:period},
			operate: "unfreeze"
		});
	})
	.on('click','.btn-transfer',function(){
		var packageId = $(this).attr("data-id");
		var userId = $(this).attr("data-user");
		var period = $(this).attr("data-period");
		var hints = "点击确定将会对"+packageId+"融资包的第"+period+"期的融资人账户进行解冻，且将发生的代偿金额划转至担保公司账户，同时此还款计划结束，是否确定操作？";
		showModal({
			type: "notice",
			title: "解冻划转",
			message: hints,
			datas: {packageId:packageId,userId:userId,period:period},
			operate: "transfer"
		});
	});
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
	$('#modal').on('click','#btn-modal-sure',function(){
		var url = "";
		var type = $(this).attr("data-type");
		var packageId = $(this).attr("data-packageid");
		var userId = $(this).attr("data-userid");
		var period = $(this).attr("data-period");
		switch(type){
			case "clear": url = global.context+"/web/payment/clear/"+packageId+"/"+period;break;
			case "unfreeze": url = global.context+"/web/payment/unfreeze/"+packageId+"/"+period+"/"+userId;break;
			case "transfer": url = global.context+"/web/payment/unfreezeTransfer/"+packageId+"/"+period+"/"+userId;break;
		}
		$.ajax({
			url: url,
			type: 'GET',
			contentType: 'application/json;charset=utf-8',
			error: function(){
			},
			success: function(response,data){
				repayment_table.invoke("fnDraw");
			}
		});
	});
	
	
	
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
