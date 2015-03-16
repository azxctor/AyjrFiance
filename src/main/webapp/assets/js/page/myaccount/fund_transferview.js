require(['jquery',
         'global',
         'module/util',
         'module/view_pusher',
         'module/datatables',
         'form',
         'requirejs/domready!'], 
function($, global, util, pusher, datatables,form){
	var logTable = null;
//	var _fnUseType = function(data){
//		return data.useType.text;
//	};
	var option = {
		context:null,//提交表单域 
		action:null,//提交url
		//dataExtension:{payerUserId:"",payeeUserId:""},//额外数据
		mode:"",
	};
	
	var form = $('#form');
	$('#form').validate({
		onsubmit: false
	});
	
	function resetForm(){
		form.data("validator")&&form.data("validator").resetForm();
		form.find('input,select,textarea').each(function(){
			if($(this).is('select')){
				$(this).val("IN");
			}else{
				if($(this).attr("name")=="useType"){
					return;
				}
				$(this).val("");
			}
		});
		$('.hide-info').hide();
		if($('#type').html()=="PM"){
			var acctno = $('[name=acctType]').attr("data-acctno");
			$('[name=payeeAcctNo]').val(acctno).attr("disabled",true);
			$('[name=payerAcctNo]').removeAttr("disabled");
			getAccountInfo(acctno,"ASSETS","payee");
		}
	}
	
	var _fnEllipsisTre=function(data, type, extra){
		var dealMemo=extra.trxMemo;
		if(dealMemo != null){
			var dealMemoWhow = dealMemo.length > 10?'<span class="link" data-toggle="tooltip" data-dealMemo="'+dealMemo+'">'+ dealMemo.substring(0,11)+'...'+'</span>':dealMemo;
			return dealMemoWhow;
		}
	};
	var _fnEllipsisDeal=function(data, type, extra){
		var dealMemo=extra.dealMemo;
		if(dealMemo != null){
			var dealMemoWhow = dealMemo.length > 10?'<span class="link" data-toggle="tooltip" data-dealMemo="'+dealMemo+'">'+ dealMemo.substring(0,11)+'...'+'</span>':dealMemo;
			return dealMemoWhow;
		}
	};
	
	var table=logListTable();
	
	function logListTable(){
		var options = {};
		options.tableId = '#table-result';
		options.sAjaxSource = global.context+"/web/myaccount/getTransferAppls";
		options.aaSorting=[[0,"desc"]];
		options.functionList={
			"_fnEllipsisTre":_fnEllipsisTre,
			"_fnEllipsisDeal":_fnEllipsisDeal
		};
//		options.functionList={"_fnUseType":_fnUseType};
		options.rawOptions={"fnDrawCallback": function(){colorTable();}};
		logTable = datatables.init(options);
		logTable.setParams(util.getSearchData(".row-search"));
		return logTable.create();
	};
	
	function colorTable(){
		var trs = $('#table-result').find('tbody').find('tr');
		$.each(trs,function(i,item){
			if(i%4 == 0 || i%4 == 1){
				$(item).css("background-color","#FFFFFF");
			}else{
				$(item).css("background-color","#F7F6FB");
			}
		});
	}
	
	if($('#type').html()=="PM"){
		var acctno = $('[name=acctType]').attr("data-acctno");
		$('[name=payeeAcctNo]').val(acctno).attr("disabled",true);
		getAccountInfo(acctno,"ASSETS","payee");
	}
	/*
	 * 事件
	 */
	$('[name=acctType]').on('change',function(){
		var acctno = $(this).attr("data-acctno");
		if(this.value == "IN"){
			$('[name=payeeAcctNo]').val(acctno).attr("disabled",true);
			$('[name=payerAcctNo]').val("").removeAttr("disabled");
			$('.payer .hide-info').hide();
			getAccountInfo(acctno,"ASSETS","payee");
		}else if(this.value == "OUT"){
			$('[name=payerAcctNo]').val(acctno).attr("disabled",true);
			$('[name=payeeAcctNo]').val("").removeAttr("disabled");
			$('.payee .hide-info').hide();
			getAccountInfo(acctno,"ASSETS","payer");
		}
	});
	
	$("#btn-search").on("click",function(){
		table.setParams(util.getSearchData(".row-search"));
		table.rawTable.fnDraw();
	});
	
	//备注显示
	$("body").on("mouseover",".link",function(e){
		var $target=$(e.currentTarget);
		$target.popover({
			trigger:'hover',
			html:true,
			placement:'bottom',
			selector:true,
			container: 'body',
			content:$target.attr("data-dealMemo")
		});
	});
	
	function getAccountInfo(acctno,accttype,role){
		$('[name='+role+'AcctNo]').rules("remove","businessError");
		$('.'+role+' [data-name=bnkAcctName]').val("");
		$('.'+role+' [name=bnkName]').val("");
		$('.'+role+' [name=bnkAcctNo]').val("");
		$('.'+role+' [name=availableBal]').val("");
		if(!isNaN(acctno) && acctno != ""){
			if($('[name=payerAcctNo]').val() == $('[name=payeeAcctNo]').val()){
//				$.pnotify({
//				    text: "转入转出交易账号不能相同，请重新输入！",
//				    type: 'error'
//				});
				$('[name='+role+'AcctNo]').rules("add",{
					businessError:true,
					messages:{
						businessError:"转入转出交易账号不能相同，请重新输入！"
					}
				});
			}else{
				$.ajax({
					url: global.context+"/web/myaccount/currentAccountInfo",
					type: 'POST',
					dataType: 'json',
					data: JSON.stringify({acctNo:acctno,acctType:accttype}),
					contentType: 'application/json;charset=utf-8',
					error: function(){
					},
					success: function(response){
//						$('input[name='+role+'AcctNo]').rules("remove","businessError");
						var data = response.data;
						if(data == null){
//							$('[name='+role+'AcctNo]').rules("add",{
//								businessError:true,
//								messages:{
//									businessError:"交易账号错误，请重新输入！"
//								}
//							});
						}else{
							$('.'+role+' .hide-info').show();
							//option.dataExtension[role+"UserId"] = data.userId;
							$('.'+role+' [data-name=bnkAcctName]').val(data.bnkAcctName);
							$('.'+role+' [name=bnkName]').val(data.bnkName);
							$('.'+role+' [name=bnkAcctNo]').val(data.bnkAcctNo);
							$('.'+role+' [name=availableBal]').val(util.get_thousand(data.availableBal));
						}
					}
				});
			}
		}
		else if(isNaN(acctno)){
//			$.pnotify({
//			    text: "交易账号错误，请重新输入！",
//			    type: 'error'
//			});
			$('[name='+role+'AcctNo]').rules("add",{
				businessError:true,
				messages:{
					businessError:"交易账号错误，请重新输入！"
				}
			});
		}
	};
	
	$('[name=payerAcctNo]').on('change',function(){
		getAccountInfo(this.value,"DEBT","payer");
	});
	
	$('[name=payeeAcctNo]').on('change',function(){
		getAccountInfo(this.value,"DEBT","payee");
	});
	
	$('#btn-reset').click(function(){
		resetForm();
	});
	
	$('#btn-transfer').click(function(){
		if(form.valid()){
			$("#modal-confirm").modal().show();
		}
	});
	
	$("#btn-muti-transfer").click(function(){
		pusher.push({
			url:global.context+"/web/myaccount/batchTransferview",
			title:'批量转账',
			element:"#container-transfer",
			onHide:function(){
				$("#modal-batch-confirm").data("bs.modal")&&$("#modal-batch-confirm").data("bs.modal").hide();
				$("#modal-batch-confirm,.modal-backdrop").remove();
				table.setParams(util.getSearchData(".row-search"));
				table.rawTable.fnDraw();
				resetForm();
			}
		});
	});
	
	$(".btn-modal-agree").click(function(){
		option.action = "../myaccount/fundtransferappl";
		option.dataExtension = {"payerName":$("[name='payerName']").val(),"payeeName":$("[name='payeeName']").val()};
		var call = util.ajax_submit(form,option);
		call.success(function(response){
			if(response.code == "ACK"){
				table.setParams(util.getSearchData(".row-search"));
				table.rawTable.fnDraw();
				resetForm();
			}
		});	
	});
	
	$("body").on("hide.fund-batch-transfer",function(){
		pusher.pop();
	});
	
	/*
	 * 初始化
	 */
	$(".datepicker").datepicker({
		 autoclose: true,
		 format: "yyyy-mm-dd",
		 weekStart: 1,
		 todayBtn: false,
		 language: 'zh-CN'
	});
});