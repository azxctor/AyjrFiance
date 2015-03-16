require(['jquery',
         'global',
         'module/util',
         'module/view_pusher',
         'module/datatables',
         'form',
         'select2',
         'requirejs/domready!'], 
function($, global, util, pusher, datatables, form){
	//datepicker init
	$('.from-time, .to-time').datepicker({
        format: 'yyyy-mm-dd',
        weekStart: 1,
        autoclose: true,
        todayBtn: false,
        language: 'zh-CN',
    }).on('changeDate', function(e){
    	$('.to-time').datepicker({ autoclose: true}).datepicker('setStartDate', e.date).focus();
    });	
	//datatables init
//	jQuery.fn.dataTableExt.oApi.fnPagingInfo = function ( oSettings )
//	{
//	    return {
//	        "iStart":         oSettings._iDisplayStart,
//	        "iEnd":           oSettings.fnDisplayEnd(),
//	        "iLength":        oSettings._iDisplayLength,
//	        "iTotal":         oSettings.fnRecordsTotal(),
//	        "iFilteredTotal": oSettings.fnRecordsDisplay(),
//	        "iPage":          oSettings._iDisplayLength === -1 ?
//	            0 : Math.ceil( oSettings._iDisplayStart / oSettings._iDisplayLength ),
//	        "iTotalPages":    oSettings._iDisplayLength === -1 ?
//	            0 : Math.ceil( oSettings.fnRecordsDisplay() / oSettings._iDisplayLength )
//	    };
//	};
	var _fnOperateBtn = function(data){
		var checkbtn = (data.canCheck)? "<button type='button' class='btn btn-mf-primary go-details' data-id='"+data.productId+"' data-viewonly='true'>" +
				"<i class='fa fa-eye fa-lg' title='详情'></i></button>" : "";
		var modifybtn = (data.canModify)? "<button type='button' class='btn btn-mf-primary operation-edit' data-id='"+data.productId+"'>" +
				"<i class='fa fa-pencil fa-lg' title='修改'></i></button>" : "";
		var withdrawbtn = (data.canWithdraw)? "<button type='button' class='btn btn-mf-primary operation-revoke' data-id='"+data.productId+"'>" +
				"<i class='fa fa-times fa-lg' title='撤单'></i></button>" : "";
//		var withdrawbtn = (data.canWithdraw)? "<button type='button' class='btn btn-mf-primary operation-revoke' data-id='"+data.productId+"' data-toggle='popover' data-html=true data-container='.right-blk-body' data-placement='bottom' data-content='<button>撤单</button> <button>取消</button>'>" +
//				"<i class='fa fa-times fa-lg' title='撤单'></i></button>" : "";
		var approvebtn = (data.canApprove)? "<button type='button' class='btn btn-mf-primary go-details' data-id='"+data.productId+"'>" +
				"<i class='fa fa-gavel fa-lg' title='审核'></i></button>" : "";
		var evaluatebtn = (data.canEvaluate)? "<button type='button' class='btn btn-mf-primary go-details' data-id='"+data.productId+"'>" +
				"<i class='fa fa-star fa-lg' title='评级'></i></button>" : "";
		var freezebtn = (data.canFreeze)? "<button type='button' class='btn btn-mf-primary go-details' data-id='"+data.productId+"'>" +
				"<i class='fa fa-asterisk fa-lg' title='冻结'></i></button>" : "";
		var publishbtn = (data.canPublish)? "<button type='button' class='btn btn-mf-primary go-details' data-id='"+data.productId+"'>" +
				"<i class='fa fa-wrench fa-lg' title='交易参数设置'></i></button>" : "";
		var updatebtn = (data.canUpdate)? "<button type='button' class='btn btn-mf-primary go-details' data-id='"+data.productId+"'>" +
				"<i class='fa fa-pencil fa-lg' title='修改'></i></button>" : "";
		var operateHTML = "<div class='btn-group btn-group-xs'>"+ checkbtn + modifybtn + withdrawbtn + evaluatebtn + approvebtn + freezebtn + publishbtn + updatebtn + "</div>";
		return operateHTML;
	};	
	var _fnLogLink = function(data){
		return "<a class='view-log' data-id='"+data.productId+"'>日志</a>";
	};
	var _fnNameLink = function(data){
		return "<a class='go-details' data-id='null' data-viewonly='true'>"+data+"</a>";
	};
	var _fnComment = function(data){
		var result = data.result ? data.result : "";
		var comment = data.comment ? data.comment : "";
		return result+"<br/>"+comment;
	};
	
	var project_status = $("#project-status").val();
	var _fnProjectStatus = function(data){
		if ("COMMIT" == project_status || "CHECK" == project_status){
			if (data.status.text == "待审核")
				return "审核中";
		}
		return data.status.text;				
	}
	var log_table = null;
	productlogTable();	
	function productlogTable(){
		var options = {};
		options.tableId = '#table-log';
		options.sAjaxSource = global.context+"/web/product/productLog";
		options.aaSorting=[[0,"desc"]];
		options.functionList={"_fnComment":_fnComment};
		log_table = datatables.init(options);
		log_table.setParams([{name:"productId",value:""}]);
		return log_table.create();
	};
	var list_table = null;
	productlistTable();
	function productlistTable(){
		var sortNum = $("#table-search-result thead").find("[data-aSsorting]:eq(0)").index();
		var options = {};
		options.tableId = '#table-search-result';
		options.sAjaxSource = global.context+"/web/product/getproductlist";
		options.aaSorting=[[sortNum,"desc"]];
		options.functionList={"_fnOperateBtn":_fnOperateBtn,"_fnLogLink":_fnLogLink,"_fnNameLink":_fnNameLink,"_fnProjectStatus":_fnProjectStatus};
//		options.rawOptions={"bStateSave":true};
//		options.rawOptions={"fnDrawCallback": function(){localStorage.page = this.fnPagingInfo().iPage;}};
		list_table = datatables.init(options);
		list_table.setParams(util.getSearchData("#form-finance-search"));
		return list_table.create();
	};
	
	$('#table-search-result')
	.on('click','.go-details',function(){
		var productId = $(this).attr('data-id')=="null" ? $(this).parent().parent().find('.view-log').attr("data-id") : $(this).attr("data-id");
		var viewonly = $(this).attr('data-viewonly')? true:false ;
		var title = $(this).attr('data-viewonly')? "详情":"操作" ;
		pusher.push({
			element: $("#finance-product"),
			title: title,
			url: global.context+"/web/product/details/"+productId+"/"+viewonly,
			method: "GET",
			beforeHide: function(){
			},
			onHide: function(){
				//勿删，交易参数设置要用到
				$('body').off('click.package');
			}
		});
	})
	.on('click','.operation-edit',function(){
		var productId = $(this).attr('data-id');
		$('#productId').html(productId);
		$('#productId').attr("data-type","edit");
		pusher.push({
			element: $("#finance-product"),
			title: "申请",
			url: global.context+"/web/product/edit/getView/",
			method: "POST",
			beforeHide:function(){
				return noticeSave();
			}
		});
	})
	.on('click','.operation-revoke',function(){
		var productId = $(this).attr('data-id');
		showModal({
			type: "notice",
			title: "撤单",
			message: "是否确认撤销该融资项目申请？",
			productId: productId,
			operate: "revoke"
		});
	})
	.on('click','.view-log',function(){
		log_table.getParams();
		var productId = $(this).attr('data-id');
		log_table.setParams([{name:"productId",value:productId}]);
		log_table.invoke("fnDraw");
		showModal({
			type: "datatables",
			title: "日志",
			productId: productId
		});
	});
	
	$("#btn-finance-apply").on("click",function(e){
		$('#productId').attr("data-type","apply");
		$('#productId').html("");
		var financierFlag = $('#financier_apply_flag').val();
		if(financierFlag=="true"){
			pusher.push({
				element: $("#finance-product"),
				title: "申请",
				url: global.context+"/web/product/financier/financeproductapply",
				method: "GET",
				beforeHide:function(){
					return noticeSave();
				}
			});
		}else{
			pusher.push({
				element: $("#finance-product"),
				title: "申请",
				url: global.context+"/web/product/financeapply/financeproductapply",
				method: "GET",
				beforeHide:function(){
					return noticeSave();
				}
			});
		}
		
	});
	$('#btn-modal-sure').click(function(){
		if($(this).attr("data-type")=="revoke"){
			var productId = $(this).attr('data-id');
			 $.ajax({
				url: global.context+"/web/product/revoke",
				type: 'POST',
				dataType: 'json',
				contentType: 'application/json; charset=UTF-8',
				data: JSON.stringify({"productId":productId,"comment":""}),
				success: function(response,data){
					list_table.invoke("fnDraw");
				}
			});
		}else if($(this).attr("data-type")=="cancel"){
			pusher.pop();
		}
	});
	$('#search-btn').click(function(){
		list_table.setParams(util.getSearchData("#form-finance-search"));
		list_table.invoke("fnDraw");
		if($("#result-select").val() == "UNRESOLOVING"){
			list_table.invoke("fnSetColumnVis", 9, true, false );	
		}else if($("#result-select").val() == "RESOLOVED"){
			list_table.invoke("fnSetColumnVis", 9, false, false );	
		}
	});
	$('#form-finance-search').keydown(function(event){
		var keycode = event.which;  
	    if (keycode == 13) {  
	    	$('#search-btn').trigger('click');   
	    	return false;
	    }  	    
	});
	$('.right-blk-body').on('click','#form-btn-cancel',function(){
		if($('#form-btn-cancel').attr("data-notice") == "true"){
			showModal({
				type: "notice",
				title: "提示",
				message: "您录入的信息尚未保存，是否确定退出？",
				operate: "cancel"
			});
		}else{
			pusher.pop();
		}
	});
	function noticeSave(){
		if($('#form-btn-cancel').attr("data-notice") == "true"){
			showModal({
				type: "notice",
				title: "提示",
				message: "您录入的信息尚未保存，是否确定退出？",
				operate: "cancel"
			});
			//modal处理
			return false;
		}else{
			return true;
		}
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
			$('#modal #btn-modal-sure').attr({"data-id":object.productId,"data-type":object.operate});
		}
		$('#modal').modal('show');
	}
	
});

//var popoverInitial = function(){
//	var productId = "";
//	$('.operation-revoke').popover()
//	.on('click',function(){
//		productId = $(this).attr('data-id');
//	})
//	.on('show.bs.popover',function(){
//		$('.popovered').popover('hide');
//		$('.popovered').removeClass("popovered");
//	})
//	.on('shown.bs.popover', function () {
//		var self = $(this);
//		self.addClass("popovered");
//		$('.popover-content button:even').addClass("btn btn-sm btn-mf-primary btn-revoke-yes");
//		$('.popover-content button:odd').addClass("btn btn-sm btn-mf-default btn-revoke-no");
//		$('.btn-revoke-yes').click(function(){
//			var data = {};
//			data.productId = productId;
//			data.comment = "";
//			 $.ajax({
//				url: global.context+"/web/product/revoke",
//				type: 'POST',
//				dataType: 'json',
//				contentType: 'application/json; charset=UTF-8',
//				data: JSON.stringify(data),
//				success: function(response,data){
//					list_table.invoke("fnDraw");
//				}
//			});
//		});
//	});
//	$('body').not('.operation-revoke').click(function(){
//		$('.popovered').popover('hide');
//		$('.popovered').removeClass("popovered");
//	});
//};