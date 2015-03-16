require(['jquery',
         'global',
         'module/util',
         'form',
         'module/datatables',
         'module/ajax',
         'bootstrap',
         'jquery.printarea',
         'requirejs/domready!'],function($,global,util,Form,datatables){
	
		/**
		 * 变量
		 */
		//datatable打印按钮
		var _fnOperate=function(data, type, extra){
			var applNo=extra.applNo;
			var canPrint=extra.applStatus.name == "WAIT_APPROVAL"? "<button type='button' title='打印' class='btn btn-xs btn-mf-primary btn-print-app-set' data-applNo="+ applNo +"><i class='fa fa-print fa-lg'></i></button>":"";
			return "<div class='btn-group'>" + canPrint + "</div>";
		};
		var rechargeTable = function(){
			var options = {};
			options.tableId = '#recharge-table';
			options.sAjaxSource = global.context+"/web/myaccount/get/unsigned/recharge/appl/list";
			options.aaSorting=[[0,"desc"]];
			options.functionList={
				"_fnOperate":_fnOperate
			};
			var tb=datatables.init(options);
			tb.create();
			return tb;
		};
		
		var table=rechargeTable();
		
		var queryUnsignedUserInfoTimer=null;
		
		$("#apply-submit-btn").attr("disabled",true);
		
		var rowTemplate="<tr><td><input type='radio' name='radio-recharge'/></td><td data-prop='acctNo'>{0}</td><td data-prop='bnkAcctNo'>{1}</td><td data-prop='bnkAcctName'>{2}</td><td data-prop='channel'>{3}</td><td data-prop='userType'>{4}</td></tr>";
		/**
		 * 事件
		 */
		$("#form-recharge").validate({
			onsubmit: false
		});
		
		//交易账户变更联动事件
		$("#unsigned-acc-no").blur(function(){
			$("#recharge-ditch").val("");
			$("#recharge-account-name").val("");
			$("#recharge-account-no").val("");
			$("#recharge-userType").val("");
			if(!$(this).val().trim()){
				$("#apply-submit-btn").attr("disabled",true);
				return false;
			}
			var obj = {};
			obj.acctNo = $(this).val().trim();
			if(queryUnsignedUserInfoTimer){
				queryUnsignedUserInfoTimer.abort();
			}
			queryUnsignedUserInfoTimer=$.ajax({
               url:global.context + "/web/myaccount/queryUnsignedUserInfo",
               type: "POST",
               dataType: "json",
               contentType: "application/json",
               data: JSON.stringify(obj)
			}).done(function(resp){
				var result=resp.data;
				if(result&&result.list&&result.list.length==1){
					var dto=result.list[0];
					$("#recharge-ditch").val(dto.channel.text);
					$("#recharge-account-name").val(dto.bnkAcctName);
					$("#recharge-account-no").val(dto.bnkAcctNo);
					$("#recharge-userType").val(dto.userType);
					$("#apply-submit-btn").attr("disabled",false);
				}else{
					$("#apply-submit-btn").attr("disabled",true);
				}
			}).always(function(){
				queryUnsignedUserInfoTimer=null;
			});
			return false;
		});
		
		$("#recharge-account-name").blur(function(){
			$("#recharge-ditch").val("");
			$("#unsigned-acc-no").val("");
			$("#recharge-account-no").val("");
			$("#recharge-userType").val("");
			if(!$(this).val().trim()){
				$("#apply-submit-btn").attr("disabled",true);
				return false;
			}
			var obj = {};
			obj.bnkAcctName = $(this).val().trim();
			if(queryUnsignedUserInfoTimer){
				queryUnsignedUserInfoTimer.abort();
			}
			queryUnsignedUserInfoTimer=$.ajax({
               url:global.context + "/web/myaccount/queryUnsignedUserInfo",
               type: "POST",
               dataType: "json",
               contentType: "application/json",
               data: JSON.stringify(obj)
			}).done(function(resp){
				var result=resp.data;
				if(result&&result.list&&result.list.length==1){
					var dto=result.list[0];
					 $("#recharge-ditch").val(dto.channel.text);
					 $("#unsigned-acc-no").val(dto.acctNo);
					 $("#recharge-account-no").val(dto.bnkAcctNo);
					 $("#recharge-userType").val(dto.userType);
					 $("#apply-submit-btn").attr("disabled",false);
				}
				else if(result&&result.list&&result.list.length>1){
					$("#list-result").empty();
					$("#list-total").text(result.list.length);
					$.each(result.list,function(index,item){
						$("#list-result").append($.format(rowTemplate,item.acctNo,item.bnkAcctNo,item.bnkAcctName,item.channel.text,item.userType));
					});
					$("#modal-select").modal().show();
				}else{
					$("#apply-submit-btn").attr("disabled",true);
				}
			}).always(function(){
				queryUnsignedUserInfoTimer=null;
			});
			return false;
		});
		
		$("#recharge-account-no").blur(function(){
			 $("#recharge-ditch").val("");
			 $("#unsigned-acc-no").val("");
			 $("#recharge-account-name").val("");
			 $("#recharge-userType").val("");
			if(!$(this).val().trim()){
				return false;
			}
			var obj = {};
			obj.bnkAcctNo = $(this).val().trim();
			if(queryUnsignedUserInfoTimer){
				queryUnsignedUserInfoTimer.abort();
			}
			queryUnsignedUserInfoTimer=$.ajax({
               url:global.context + "/web/myaccount/queryUnsignedUserInfo",
               type: "POST",
               dataType: "json",
               contentType: "application/json",
               data: JSON.stringify(obj)
			}).done(function(resp){
				var result=resp.data;
				if(result&&result.list&&result.list.length==1){
					var dto=result.list[0];
					 $("#recharge-ditch").val(dto.channel.text);
					 $("#unsigned-acc-no").val(dto.acctNo);
					 $("#recharge-account-name").val(dto.bnkAcctName);
					 $("#recharge-userType").val(dto.userType);
					 $("#apply-submit-btn").attr("disabled",false);
				}
				else if(result&&result.list&&result.list.length>1){
					$("#list-result").empty();
					$("#list-total").text(result.list.length);
					$.each(result.list,function(index,item){
						$("#list-result").append($.format(rowTemplate,item.acctNo,item.bnkAcctNo,item.bnkAcctName,item.channel.text,item.userType));
					});
					$("#modal-select").modal().show();
				}else{
					$("#apply-submit-btn").attr("disabled",true);
				}
			}).always(function(){
				queryUnsignedUserInfoTimer=null;
			});
			return false;
		});
		
		$(".btn-modal-select").on("click",function(){
			var row=$("input[name='radio-recharge']:checked").closest("tr");
			$("#recharge-ditch").val(row.find("[data-prop='channel']").text()).trigger("focusout");
			$("#unsigned-acc-no").val(row.find("[data-prop='acctNo']").text()).trigger("focusout");
			$("#recharge-account-no").val(row.find("[data-prop='bnkAcctNo']").text()).trigger("focusout");
			$("#recharge-account-name").val(row.find("[data-prop='bnkAcctName']").text()).trigger("focusout");
			$("#recharge-userType").val(row.find("[data-prop='userType']").text()).trigger("focusout");
			$("#modal-select").modal("hide");
			$("#apply-submit-btn").attr("disabled",false);
		});
		
		$(".btn-recharge").on("click",function(){
			if(!$("#form-recharge").valid()){
				return false;
			}
			$("#modal-approve").find("#acctNo").text($("input[name=acctNo]").val());
			$("#modal-approve").find("#amount").text(util.get_thousand($("input[name=amount]").val()));
			$("#modal-approve").modal().show();
			return false;
		});
		$("body").on("click",".btn-modal-agree",function(e){
			var $modal=$(e.currentTarget).parents(".modal");
			if($modal.attr("id")=="modal-print"){
				$modal.data("bs.modal").hide();
				$modal.find("#print-content").printArea({popClose:true});  
			}
			else{ 
				var amount_money=$("#amount_money").val();
				amount_money = amount_money.replace(/\,|\s/g,'');
				$("#amount_money").val(amount_money); 
				util.ajax_submit("#form-recharge").done(function(resp){
					var result=resp;
					if(result.code=="ACK"){
						table.rawTable.fnSort([[0,"desc"]]);
						$("#form-recharge")[0].reset();
						$("#apply-submit-btn").attr("disabled",true);
					}
				});
				$("#modal-approve").modal("hide");
			}
		});
		$("#btn-search").on("click",function(){
			table.setParams(util.getSearchData(".search-recharge"));
			table.rawTable.fnDraw();
		});
		
		$("#start-date").on("changeDate",function(){
			$("#end-date").datepicker("setStartDate",$(this).val());
		});
		
		$("#end-date").on("changeDate",function(){
			$("#start-date").datepicker("setEndDate",$(this).val());
		});
		/**
		 * 初始化
		 */
		
		Form.init();
		/**输入时，千分位处理**/ 
		$("#amount_money").keyup(function(event){
			var old = $(this).val();
			var value=old.replace(/\,|\s/g,'');
			value = value.replace(/[^(\d|\.|\-)]/g,'');
			value = value.replace(/(\d{1,3})(?=(\d{3})+(?:$|\.))/g, '$1,');
			$(this).val(value); 
		});

});