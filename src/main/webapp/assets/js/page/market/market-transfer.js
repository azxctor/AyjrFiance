require(['jquery',
         'global',
         'module/util',
         'module/datatables',
         'module/ajax',
         'bootstrap',
         'bootstrap-datepicker',
         'bootstrap-datepicker.zh-CN',
         'bootstrapx-popoverx',
         'ickeck',
         'sockjs',
         'stomp',
         'plugins',
         'requirejs/domready!'], 
         function($,global,util,datatables){
	
	$("#OTHER").iCheck("check");
	
	var packet_info = {};
	function _fndetailButton(data,type,full){
		var flag = $('.search-form').attr('data-id');
		var hide = $('.search-form').attr('data-hide-transfer');
		var html = '';
		if(hide != "true" && !full.mine){
			if(flag=="true"){
				html +='<a class="btn btn-default purchase disabled" href="javascript:void(0);" data-id="'+full.transferId+'" data-content="loading...">立即买入</a>';
			}else{
				html +='<a class="btn btn-default purchase" href="javascript:void(0);" data-id="'+full.transferId+'" data-content="loading...">立即买入</a>';
			}
		}
		return html;
	};
	
	function _fnflag(data,type,full){
		var overdue = "";
		if(full.overdue){
			overdue = "overdue";			
		}
		var url = global.context+"/web/market/transfer/detail/" + full.transferId;
		if(full.aipFlag==true){
			//<span><i class="fa fa-info-circle flag"></i>
			return '<a href='+url+' target="_blank" class="'+overdue+'"><span class="label label-info flag">定</span>'+full.packageName+'</a>';
		}else{
			return '<a href='+url+' target="_blank" class="'+overdue+'">'+full.packageName+'</a>';
		}
	};
	
	$("#transfer-table").on("click",".detail",function(e){
		window.open(global.context+"/web/market/transfer/detail/" + $(this).attr('data-id'));
	});

	function _fnbuyButton(nTd, sData, oData, iRow, iCol){
		var id = $(nTd).find(".purchase").attr("data-id");
		var price = '';
		var fee = '';
		var transfered = '';
		var reason = '';
		var packatNo = '';
		var expectedReturn = '';
		var expectedReturnRate = '';

		$(nTd).find('.purchase').popoverx({
			ensure_visiable : true,
			placement : 'left',
			width: 298,
			global_close: 1,
			onShown:function(){
				var isLoss = "";
				$.ajax({
					url : global.context+'/web/market/transfer/purchase/'+id,
					type : 'GET',
					contentType:'application/json;charset=utf-8',
					async : false,
					error:function(){
					},
					success : function(response) {
						price = $.parseJSON(response).priceStr;
						fee = $.parseJSON(response).transferFeeStr;
						transfered = $.parseJSON(response).transfered;
						reason = $.parseJSON(response).reason;
						packageNo = $.parseJSON(response).pkg.id;
						
						packet_info.packageNo = packageNo;
						packet_info.packageName = oData.packageName;
						packet_info.price = price;
						packet_info.fee = fee;
						packet_info.id = id;
						

						expectedReturn = $.parseJSON(response).expectedReturnStr;
						
						if(parseFloat(expectedReturn)<0){
							$("#buy-confirm-moneyfee").text(expectedReturn);
							expectedReturn = "亏损"+expectedReturn.substr(1); 
							isLoss = "loss";
						}else{ 
							expectedReturn = "盈"+expectedReturn;
							$("#buy-confirm-moneyfee").text(expectedReturn);
						}
						
						expectedReturnRate = $.parseJSON(response).expectedReturnRate;

					}
				});


				var html = '<div class="popover-item">'+
				'<label class="wh90 pop-transfer-title">融资编号</label>'+
				'<span id="balance" class="ml10 pop-transfer-con">'+oData.id+'</span>'+
				'</div>'+
				'<div class="popover-item">'+
				'<label class="wh90 pop-transfer-title">项目简称</label>'+
				'<span id="balance" class="ml10 pop-transfer-con">'+oData.packageName+'</span>'+
				'</div>'+
				'<div class="popover-item">'+
				'<label class="balance wh90 pop-transfer-title">买入价格</label>'+
				'<span id="minSubscribeAmount" class="ml10 pop-transfer-con">'+price+'元</span>'+
				'</div>'+
				'<div class="popover-item">'+
				'<label class="balance wh90 pop-transfer-title">转让手续费</label>'+
				'<span id="minSubscribeAmount" class="ml10 pop-transfer-con">'+fee+'元</span>'+
				'</div>'+
				'<div class="popover-item">'+
				//'<label class="balance wh90 pop-transfer-title">预期收益  <i class="fa fa-question-circle" data-content="<p style=line-height:20px>1. 预期收益 = 剩余本金+剩余利息-报价-交易手续费-债权转让手续费<br>2. 交易手续费按收益的5%计算，2014年5月1日前签约的融资包不收取交易手续费<br>3. 预期收益不包含因融资人提前还款等因素产生的收益变动</p>"></i></label>'+
				'<label class="balance wh90 pop-transfer-title">预期收益 </label>'+
				'<span id="minSubscribeAmount" class="ml10 pop-transfer-con '+isLoss+'" style="color:red">'+expectedReturn+'元</span>'+
				'</div>'+
				'<div class="popover-item">'+
				'<label class="balance wh90 pop-transfer-title">预期年化收益率</label>'+
				'<span id="minSubscribeAmount" class="ml10 pop-transfer-con">'+expectedReturnRate+'</span>'+
				'</div>';
				if(transfered == false){
					html = html + '<h5 class="danger text-center">'+reason+'</h5>';
					var investorFlag = $('.search-form').attr('data-investor-flag');
					if(investorFlag == "false"){
						html = html + '<h5 class="text-center"><span>点击<a href="'+ global.context+ '/web/members/investorinfo">这里</a>申请成为投资会员</span></span>';
					}
				}else{
					html = html + '<div><button type="button" class="btn btn-mf-primary br0 purchased" data-id='+id+'>买入</button></div>';
				}


				this.$tip.find('.popover-content').html(html);
				$(".fa-question-circle").popover({
					html:true,
					placement:"top",
					trigger:"hover"
				});
				this.resetPosition();


				$(".purchased").on("click",function(){
					var id = $(this).attr('data-id');
					$("#buy-confirm-id").text(packet_info.packageNo);
					$("#buy-confirm-name").text(packet_info.packageName);
					$("#buy-confirm-price").text(packet_info.price);
					$("#buy-confirm-fee").text(packet_info.fee); 
					$("#buy-confirm-modal").modal();
					//buyitem(id);
				});
			}

		});

	}
	
	 var render_my_thumb = function (code) {
    	    var th_map = {"A":4,"B":3,"C":2,"D":1};
    	    var num = th_map[code];
    	  	var thumb_html = "";
    	  	for(var i=0;i<num;i++){
    	  		thumb_html += "<i class='fa fa-thumbs-o-up'></i>";
    	  	} 
    	  	return 	thumb_html;	
    }
	
	/*风险等级图标显示*/
/*	function _fnrenderRiskLevel(data,type,full){
		var code = full.product.riskLevel.code;
    	var text = full.product.riskLevel.text;
    	var msg_map = {"A":"A级项目：提供货币类资产质押，本金覆盖率大于100%，违约可能性极低。",
				   "B":"B级项目：提供有形或无形资产抵质押（变现渠道畅通），本金覆盖率不低于100%，违约可能性较低。",
				   "C":"C级项目：提供有形或无形资产抵质押（有变现渠道）；或提供合格保证人担保，本金覆盖率不到100%，可能会违约。",
				   "D":"D级项目：无有形或无形资产抵质押，无保证机构担保，能提供非保证机构担保，违约的可能性较高。"
				  };
    //	var risk_level = text.substr(0,1)=="高"?"险":text.substr(0,1);
    	var html = '<a class="creditlevel ' + code + '" title="' + msg_map[code] + '">' + text + '</a>';
   //     return '<a class="creditlevel ' + code + '" title="' + text + '项目">' + risk_level + '</a>';
    	return html;
	}
	*/
	 function fnrenderRiskLevels(data,type,full){
	 	var code = full.product.riskLevel.code;
 	
 		var th_map = {"A":4,"B":3,"C":2,"D":1};
	    var num = th_map[code];
	  	var thumb_htm = "";
	  	var thumb_html2 = "";
	  	for(var i=0;i<num;i++){
	  		thumb_htm += "<i class='fa fa-heart' style='color:#EA6B24'></i>&nbsp;";
	  	}
	  	for(var i=0;i<5-num;i++){
	  		thumb_html2 += "<i class='fa fa-heart-o' style='border-color:#EAEAEA'></i>&nbsp;";
	  	}
	  	
	  	var html = thumb_htm + thumb_html2;
	  	return html;
	 };
	var transfer_table =function(){
		var options = {};
		options.tableId = '#transfer-table';
		options.sAjaxSource = global.context+"/web/market/transfer/search";
		options.functionList = {"_fndetailButton":_fndetailButton,"_fnflag":_fnflag,"_fnbuyButton":_fnbuyButton,"_fnrenderRiskLevel":fnrenderRiskLevels};
		options.aaSorting = [];
		transfer_table = datatables.init(options);
		transfer_table.setParams(util.getSearchData('#search_area'));
		transfer_table.rawOptions = {
				"oLanguage" : {
					"sLengthMenu" : "每页显示 _MENU_ 条记录",
					"sInfo" : "从 _START_ 到 _END_ /共 _TOTAL_ 条记录",
					"sInfoEmpty" : "没有数据",
					"oPaginate" : {
						"sFirst" : "首页",
						"sPrevious" : "前一页",
						"sNext" : "后一页",
						"sLast" : "尾页",
					},
					"sZeroRecords" : "谢谢您的关注，暂无债权转让信息，请稍后查看！",
					"sProcessing": "loading..."
				},
				"fnCreatedRow":function(nRow, aData, iDataIndex){
					if(aData.overdue){
						$(nRow).css("color","red");			
					}
					
				}
		};
		return transfer_table.create();
	}();
	
	
	/*var formatCurrency = function(num) {
		num = num.toString().replace(/\$|\,/g,'');
		if(isNaN(num))
			num = "0";
		sign = (num == (num = Math.abs(num)));
		num = Math.floor(num*10+0.50000000001);
		cents = num%10;
		num = Math.floor(num/10).toString();
		for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)
			num = num.substring(0,num.length-(4*i+3))+','+
			num.substring(num.length-(4*i+3));
		return (((sign)?'':'-') + num + '.' + cents);
	};*/



	$("#search").on("click",function(){
		transfer_table.setParams(util.getSearchData("#search_area"));
		transfer_table.invoke("fnDraw");

	});
	$('#search_area').keydown(function(event){
		var keycode = event.which;  
	    if (keycode == 13) {  
	    	$('#search').trigger('click');   
	    	return false;
	    }  	    
	});

	/*$("#transfer-table").on("click",".info",function(e){
//		window.open("../market/market_detail.html");

	});*/


	var buyitem = function(id){
		var item = {};
		item.id = id;
		$.ajax({
			url : global.context+'/web/market/transfer/purchase',
			type : 'post',
			contentType:'application/json;charset=utf-8',
			async : false,
			data: JSON.stringify(item),
			error:function(){
			},
			success : function(response) {
				$('.popover').hide();
				transfer_table.invoke("fnDraw");
			}
		});
	};
	
    $("#transfer-role-tip").hover(function(){
    	$("#transfer-role-area").fadeIn();
    },function(){
    	$("#transfer-role-area").fadeOut();
    });
	
	$("#buy-packet-confirm").on("click",function(){
		buyitem(packet_info.id);
		$("#buy-confirm-modal").modal("hide");
	});
	
	window.setInterval(function(){
		transfer_table.invoke("fnStandingRedraw");
	}, 5*60*1000);
	
});
