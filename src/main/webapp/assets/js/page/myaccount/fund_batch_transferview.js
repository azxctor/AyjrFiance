require(['jquery',
         'global',
         'module/upload_tool',], 
function($, global){
	/*
	 * 定义
	 */
	var listDto={};
	
	function colorTable(){
		var trs = $('.dataTable').find('tbody').find('tr');
		$.each(trs,function(i,item){
			if(i%4 == 0 || i%4 == 1){
				$(item).css("background-color","#FFFFFF");
			}else{
				$(item).css("background-color","#F7F6FB");
			}
		});
	}
	
	function format( source, params ) {
		
		if ( arguments.length > 2 && params.constructor !== Array  ) {
			params = $.makeArray(arguments).slice(1);
		}
		if ( params.constructor !== Array ) {
			params = [ params ];
		}
		$.each(params, function( i, n ) {
			source = source.replace( new RegExp("\\{" + i + "\\}", "g"), function() {
				return n;
			});
		});
		return source;
	};
	
	/*
	 * 事件
	 */
	$(".file-upload").fileUploader({
		url:global.context+"/web/myaccount/uploadAndGetBatchTansferExcelMsg",
		title:"<i class='fa fa-file-excel-o'></i>&nbsp;上传Excel"
	});
	
	$(".file-upload").on("fileupload.clear",function(){
		$("#result-list").empty();
	});
	
	$(".file-upload").on("fileupload.change",function(e,path){
		$("#result-list").empty();
		/*$.ajax({
			type: "GET",
			dataType: 'json',
			contentType: 'application/json; charset=UTF-8',
			url:global.context+"/web/myaccount/getBatchTransferExcelMsg",
			data:{
				filePath:path
			}
		}).done(function(data){*/
		var data=$(".file-upload").data("uploaded.file");
		listDto=data||{};
		$.each(data.transferMsgs,function(index,item){
			$("#result-list").append(format("<tr>\
				<td>{0}</td>\
                <td>{1}</td>\
                <td>{2}</td>\
                <td>{3}</td>\
				<td>{4}</td>\
				<td>{5}</td>\
            </tr>",
            item.payerAcctNo,
            item.payerName,
            item.receiverAcctNo,
            item.receiverName,
        	'<span class="table-align-right">'+numeral(item.trxAmt).format('0,0.00')+'</span>',
        	item.memo));
		});
		colorTable();
			
		/*}).fail(function(){
			listDto={};
			$("#result-list").empty();
		});*/
	});
	
	$("#btn-batch-transfer").on("click",function(){
		if(listDto.transferMsgs&&listDto.transferMsgs.length>0)
		{
			$("#modal-batch-confirm").modal().show();
		}
		else{
			$.pnotify({
			    text: "请上传文件",
			    type: 'error'
			});
		}
	});
	
	$(".btn-modal-batch-agree").click(function(){
		listDto.useType=$("input[name=useType]").val();
		$.ajax({
			data:JSON.stringify(listDto),
			dataType: 'json',
			type:"POST",
			contentType: 'application/json; charset=UTF-8',
			url:global.context+'/web/myaccount/createfundBatchTransferAppl'
		}).done(function(data){
			if(data.code=="ACK"){
				$("body").trigger("hide.fund-batch-transfer");
			}
		});
		$("#modal-batch-confirm").modal("hide");
	});
	/*
	 * 初始化
	 */
	
});