require(['jquery',
         'global',
         'module/util',
         'module/datatables',
         'module/view_pusher',
         'module/ajax',
         'bootstrap',
         'requirejs/domready!'], 
         function($,global,util,datatables,pusher){
	/**
	 * 变量
	 */
	//datatable操作列button
	function _fnProductName(data,type,full){
		return '<a href="javascript:void(0)" class="link-table productDetail" data-id='+full.id+'>'+full.productName+'</a>'; 
	}
	/*function _fnFinancingName(data,type,full){
		return '<a href="javascript:void(0)" class="link-table financingDetail" data-id='+full.id+'>'+full.financingName+'</a>'; 
	}*/
	function _fnSelectButton(data,type,full){
		return '<a href="javascript:void(0)" class="link-table select" data-id='+full.id+'>筛选人</a>'; 
	};
	function _fnDrawButton(data,type,full){
		return '<a href="javascript:void(0)" class="link-table draw" data-id="'+full.id+'" data-packageQuota="'+full.packageQuota+'" data-availableQuota="'+full.availableQuota+'" >抽签</a>';
	};
	function _fnPublicButton(data,type,full){
		return  '<button type="button" title="对外公布" class="btn btn-xs btn-mf-primary publish" data-id='+full.id+' data-pkg-name='+full.productName+' ><i class="fa fa-share-square-o fa-lg"></i></button>';
	};
	//datatable操作句柄
	var autotable = function(){
		var options = {};
		options.tableId = '#auto-table';
		options.sAjaxSource = global.context+"/web/market/autosubscribe/view";
		options.functionList = {"_fnProductName":_fnProductName,
								//"_fnFinancingName":_fnFinancingName,
								"_fnSelectButton":_fnSelectButton,
		                        "_fnDrawButton":_fnDrawButton,
		                        "_fnPublicButton":_fnPublicButton};
		var tb=datatables.init(options);
		tb.setParams(util.getSearchData('#search_area'));
		tb.create();
		return tb;
	}();
	//当前操作的融资包编号
	var currentPkgId="";
	
	/**
	 * 事件
	 */
	//融资包链接点击
	$("#auto-table").on("click",".productDetail",function(e){
		pusher.push({
			element:$(".row.header"),
			title:"融资详情",
			url:global.context+"/web/financingpackage/details/"+ $(this).attr('data-id')+"/view",
			onHide:function(){
				autotable.rawTable.fnDraw();
			}
		});
	});
	
	//融资人链接点击
	/**
	 * TODO: push融资人信息页面，需要融资人id
	 */
	/*$("#auto-table").on("click",".financingDetail",function(e){
		
		pusher.push({
			element:$(".row.header"),
			title:"融资人详情",
			url:"融资人详情对应的URL",
			onHide:function(){
				autotable.rawTable.fnDraw();
			}
		});
	});*/
	
	//筛选人按钮点击
	$("#auto-table").on("click",".select",function(e){
		pusher.push({
			element:$(".row.header"),
			title:"符合条件的人",
			url:global.context+"/web/market/autosubscribe/candidates/"+ $(this).attr('data-id'),
			onHide:function(){
				autotable.rawTable.fnDraw();
			}
		});
		//location.href=global.context+"/web/market/autosubscribe/candidates/" + $(this).attr('data-id');
	});
	//抽签按钮点击
	$("#auto-table").on("click",".draw",function(e){
		var id = $(this).attr("data-id");
		var packageQuota = $(this).attr("data-packageQuota");
		var availableQuota = $(this).attr("data-availableQuota");
		pusher.push({
			element:$(".row.header"),
			title:"中奖名单",
			url:global.context+"/web/market/autosubscribe/draw",
			method:"post",
			data:JSON.stringify({
				packageId:id,
				totalAmount:packageQuota,
				availableAmount:availableQuota,
			}),
			onHide:function(){
				autotable.rawTable.fnDraw();
			}
		});
	});
	//对外公布按钮点击
	$("#auto-table").on("click",".publish",function(e){
		$("#modal-approve").find("#well-pkg-name").text($(this).attr("data-pkg-name"));
		$("#modal-approve").modal().show();
		currentPkgId=$(this).attr('data-id');
	});
	//确认公布按钮点击
	$(".btn-modal-agree").on("click",function(){
		$("#modal-approve").modal("hide");
		$.ajax({
			url : global.context+'/web/market/autosubscribe/publish/'+currentPkgId,
			type : 'post',
			contentType:'application/json;charset=utf-8',
			error:function(){
			},
			success : function(response) {
				autotable.rawTable.fnDraw();
			}
		});
	});
	
	//搜索按钮点击
	$("#search").on("click",function(){
		autotable.setParams(util.getSearchData("#search_area"));
		autotable.rawTable.fnDraw();
	});
	$('#search_area').keydown(function(event){
		var keycode = event.which;  
	    if (keycode == 13) {  
	    	$('#search').trigger('click');   
	    	return false;
	    }  	    
	});
});
