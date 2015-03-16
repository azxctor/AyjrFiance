require(['jquery',
         'global',
         'module/util',
         'module/view_pusher',
         'module/datatables',
         'form',
         'module/popup',
         'requirejs/domready!'], 
function($, global, util, pusher, datatables, form){
	//页面隐藏域productId的作用在于区分新建和修改等
console.log("start");
	var option = {
		context:null,//提交表单域 
		action:null,//提交url
		dataExtension:null,//额外数据
		mode:"",
	};
	var dataExtensionCopy = {"houseList":[],"carList":[],"movableList":[],"naturalList":[],"legalList":[],"assetList":[],"liabilityList":[],};
	option.dataExtension = {
			"otherPledge":"",
			"otherMortgage":"",
			"notes":"",
			"financierIndustryType":{},
			"productAttachmentDetailsDtoList":[],
			"productMortgageResidentialDetailsDtoList":dataExtensionCopy.houseList,
			"productMortgageVehicleDetailsDtoList":dataExtensionCopy.carList,
			"productPledgeDetailsDtoList":dataExtensionCopy.movableList,
			"productWarrantPersonDtoList":dataExtensionCopy.naturalList,
			"productWarrantEnterpriseDtoList":dataExtensionCopy.legalList,
			"productAssetDtoList":dataExtensionCopy.assetList,
			"productDebtDtoList":dataExtensionCopy.liabilityList,
			"versionCt":" ",//需要加一个无意义的字段
			"productId":null,
	};
	var addOption = {
		context:null,//提交表单域 
		action:null,//提交url
		dataExtension:null,//额外数据
		mode:"",
	};
	var action = {//请求对应字段
		house: "productMortgageResidentialDetailsDto",
		car: "productMortgageVehicleDetailsDto",
		movable: "productPledgeDetailsDto",
		natural: "productWarrantPersonDto",
		legal: "productWarrantEnterpriseDto",
		asset: "productAssetDto",
		liability: "productDebtDto",
	};
	var feeRate = {};
	$(".datepicker").datepicker({
		 autoclose: true,
		 format: "yyyy-mm-dd",
		 weekStart: 1,
		 todayBtn: false,
		 language: 'zh-CN'
	});
    $('#fileupload').fileupload({
    	url: global.context+"/web/uploadmultifile",
    	acceptFileTypes: /(\.|\/)(gif|jpe?g|png)$/i,
    	maxFileSize: 200000,
		dataType: 'json',
    	formData:null,
    	add: function(e, data) {
        	var maxFileSize = 2110000;
    		var extension = $("#btn-file-upload").attr("data-extension");
    		var reg_accept = new RegExp("("+extension.replace(/,/g,"|").replace(/doc/g,"word")+")","i");
            var acceptFileTypes = reg_accept;
            if(!acceptFileTypes.test(data.originalFiles[0]['type'])) {
            	 $.pnotify({
                     text: "不支持的文件格式,请上传正确的文件格式("+extension+")",
                     type: 'error'
                 });
            }
            else if(data.originalFiles[0]['size'] > maxFileSize) {
                $.pnotify({
                    text: "文件大小必须小于2M，请重新上传",
                    type: 'error'
                });
            }else{
                data.submit();
            }
        },
		success: function( response ){
			if(response.code == "ACK"){
				if($('#image-table tbody').find('tr').length == 0){
					$('#image-table').show();
				}
				var tr = $('<tr></tr>');
				if(util.is_pdf(response.data[0].path)){
					tr.html("<td>"+response.data[0].orgFileName+"</td><td><img class='financeapply-upload-img' height='100' src='/assets/img/pdf.jpg' data-pdfpath='"+response.data[0].path+"'/></td>" +
							"<td><div class='btn-group btn-group-xs' data-id='"+ response.data[0].id +"'>" +
								"<button type='button' class='btn btn-mf-primary btn-delete-img'>" +
								"<i class='fa fa-times fa-lg' title='删除'></i></button></div></td>");
				}else{
					tr.html("<td>"+response.data[0].orgFileName+"</td><td><img class='financeapply-upload-img' height='100' src='"+response.data[0].path+"'/></td>" +
							"<td><div class='btn-group btn-group-xs' data-id='"+ response.data[0].id +"'>" +
								"<button type='button' class='btn btn-mf-primary btn-delete-img'>" +
								"<i class='fa fa-times fa-lg' title='删除'></i></button></div></td>");
				}
				
				$('#image-table tbody').append(tr);
				option.dataExtension.productAttachmentDetailsDtoList.push({"file":response.data[0].id});
				$('#form-btn-cancel').attr("data-notice","true");
			}
		},
    });
	$.ajax({
		url: global.context+"/web/product/financeapply/getFeeRate",
		type: 'POST',
		dataType: 'json',
		contentType: 'application/json;charset=utf-8',
		error: function(){
		},
		success: function(response){
			var feeMap = response.feeMap;
			feeRate.warrantFee = response.warrantFee;
			feeRate.contractFee = response.contractFee;
			feeRate.riskFee =  feeMap.风险管理费;
			feeRate.serviceFee =  feeMap.融资服务费;
			$("#warrantFee-rule").on('shown.bs.popover', function () {
				$("#warrant-fee-rule").html(feeRate.warrantFee*100+"%");
			});
			$("#contractFee-rule").on('shown.bs.popover', function () {
				$("#contract-fee-rule").html(feeRate.contractFee*100+"%");
			});
			$("#riskFee-rule").on('shown.bs.popover', function () {
				$("#risk-fee-rule").html((feeRate.riskFee*100).toFixed(6)+"%");
			});
			$("#serviceFee-rule").on('shown.bs.popover', function () {
				$("#service-fee-rule").html((feeRate.serviceFee*100).toFixed(6)+"%");
			});
		}
	});	
	$('#image-table').on('click','.btn-delete-img',function(){
		var $this = $(this);
		var fileId = $(this).parent().attr("data-id");
		var data = {};
		data.id = fileId;
		$.ajax({
			url: global.context+"/web/removefile",
			type: 'DELETE',
			data: JSON.stringify(data),
			dataType: 'json',
			contentType: 'application/json;charset=utf-8',
			error: function(){
			},
			success: function(response){
				var $tr = $this.parent().parent().parent();
				option.dataExtension.productAttachmentDetailsDtoList.splice($tr.prevAll().length,1);
				$tr.remove();
				if($('#image-table tbody').find('tr').length == 0){
					$('#image-table').hide();
				}
			}
		});
	});
	function everChange(){
		$("#form-apply").find('input,select,textarea').on('change',function(){
			$('#form-btn-cancel').attr("data-notice","true");
		});
		$("#main-content .notes,.other-pledge,.other-mortgage").on('change',function(){
			$('#form-btn-cancel').attr("data-notice","true");
		});
	}	
    function getTableData( data ){//把后台获取的数据转换格式以便插入table
    	var result = {};
    	$.each(data,function(key,val){
    		if(val && typeof(val) == "object"){
    			result[key] = val.name;
    		}else{
    			result[key] = val;
    		}
    	});
    	return result;
    }
    function setTableData( container, data ){//当前生成与后台读取都可插入table（select分别是字符串与对象）
    	var num = $(container).find('thead').find('th').length;
    		var tr = $("<tr></tr>");
        	for(var i=0;i<num;i++){
        		var name = $($(container).find('thead').find('th').get(i)).attr("data-name");
    			var td = $("<td></td>");
    			if(name == "delete"){
    				td.html("<div class='btn-group btn-group-xs'>" +
						"<button type='button' class='btn btn-mf-primary btn-delete-data'>" +
						"<i class='fa fa-times fa-lg' title='删除'></i></button></div>");
    			}
    			else if(data[name] && typeof(data[name])== "object"){
	        		td.html(data[name].text);
        		}else if(name){
        			if(name == "mortgageType" && data.dmortgageType){
        				td.html(data.dmortgageType.text);
        			}
        			else if(name == "enterpriseIndustry" && data.denterpriseIndustry){
        				td.html(data.denterpriseIndustry.text);
        			}
        			else if(name == "job" && data.djob){
        				td.html(data.djob.text);
        			}
        			else if(name == "type" && data.dtype){
        				td.html(data.dtype.text);
        			}
        			else{
        				td.html(data[name]);
        			}
        		}
        		tr.append(td);
        	}
        	$(container).find('tbody').append(tr);
    }
	function setFormData( containers, json ){//编辑状态下把后台获取的数据插入表格
		for(var i=0;i<containers.length;i++){
			$(containers[i]).find('input,select,textarea').each(function(){
				var name = $(this).attr("name");
				if(json[name] && typeof(json[name])== "object"){
					$(this).val(json[name].name);
				}else{
					$(this).val(json[name]);
				}
				if(name=="termLength"){
					if(json.termLength && json.termLength == "13" ){
						$('.term-days').show();
						$(".term-days input").removeAttr("data-ignore");
						$(".term-days input").val("");
					}
				}
				if(name=="financierName"){
					counterGuarantee(json.user.type.code);
				}
			});	
		}
	}    
	function getFormData( container ){//把填写的form data格式化
		var result = {"listData": {},"tableData": {},};
		$(container).find('input,select,textarea').each(function(){
			if($(this).is('select')){
				result.tableData[$(this).attr('name')] = $(this).find('option:selected').text();
				result.listData[$(this).attr('name')] = $(this).val();
			}else{
				result.tableData[$(this).attr('name')] = $(this).val();
				result.listData[$(this).attr('name')] = $(this).val();
			}
		});
		return result;
	};
	function clearFormData( container ){
		$(container).find('input,select,textarea').each(function(){
			if($(this).is('select')){
				$(this).find('option:eq(0)').attr("selected",true);
				if($('[name=ownerType]').attr("name") == "ownerType"){
					$('.coower-hide').attr("disabled",true);
				}
			}else{
				$(this).val("");
			}
		});
	}
	
	/*表单交互*/
	payMethod([1,1,1,1]);	
	function payMethod( list,arg ){
		var flag = true;
		for(var i=0;i<list.length;i++){
			var $option=$(".pay-method option:eq("+i+")");
			if(list[i]){
				if($option.parent().is("div")){
					$option.unwrap();
				}
				if(flag){
					$option[0].selected=true;
					flag = false;
				}
			}
			else{
				if(!$option.parent().is("div")){
					$option.wrap("<div></div>");
				}
			}
		}
		if(arg && arg == "13"){
			$(".term-days").show();
			$(".term-days input").removeAttr("data-ignore");
		}else{
			$(".term-days").hide();
			$(".term-days input").attr("data-ignore","true");
		}
	}
	function counterGuarantee( code ){
		if(code == "O"){
			$('#main-tab li:eq(0)').find('a').tab('show');
			$('#main-tab li:eq(1)').show();
			option.dataExtension.productMortgageResidentialDetailsDtoList = dataExtensionCopy.houseList;
			option.dataExtension.productMortgageVehicleDetailsDtoList = dataExtensionCopy.carList;
			option.dataExtension.productPledgeDetailsDtoList = dataExtensionCopy.movableList;
			option.dataExtension.productWarrantPersonDtoList = dataExtensionCopy.naturalList;
			option.dataExtension.productWarrantEnterpriseDtoList = dataExtensionCopy.legalList;
		}else{
			$('#main-tab li:eq(1)').show();
			option.dataExtension.productMortgageResidentialDetailsDtoList = dataExtensionCopy.houseList;
			option.dataExtension.productMortgageVehicleDetailsDtoList = dataExtensionCopy.carList;
			option.dataExtension.productPledgeDetailsDtoList = dataExtensionCopy.movableList;
			option.dataExtension.productWarrantPersonDtoList = dataExtensionCopy.naturalList;
			option.dataExtension.productWarrantEnterpriseDtoList = dataExtensionCopy.legalList;
		}
	}
	
	$(".btn-modal-select").on("click",function(){
		var financierAcct=$("#modal-select").find("input[type='radio']:checked").attr("data-accno");
		$("#form-apply").find('.acctno').val(financierAcct);
		getAfterData();
	});
	$('#form-apply')
	.on('change','.maturity-select',function(){
		switch($(this).val()){
			case "1":payMethod([1,0,0,0]);break;
//			case "2":payMethod([1,1,0,0]);break;
//			case "3":payMethod([1,1,1,1]);break;
//			case "4":payMethod([0,1,0,0]);break;
//			case "6":payMethod([1,1,1,1]);break;
//			case "8":payMethod([0,0,1,1]);break;
//			case "9":payMethod([0,1,1,1]);break;
//			case "12":payMethod([0,1,1,1]);break;
			case "13":payMethod([1,0,0,0],"13");break;
			default:payMethod([1,1,1,1]);break;
		}
	})
	.on('change','.financier-name',function(){
		var username = $(this).val();
		if($.trim(username).length == 0){
			$(".acctno").val("");
			return;
		}
		$.ajax({
			url:global.context+"/web/product/financeapply/getAccNo/"+username,
			success:function(respond){
				$("#form-apply").find('.acctno').val("");
				var result=$.parseJSON(respond).data;
				if(result.length>0){
					if(result.length==1){
						var financierAcct=result[0].acctNo;
						$("#form-apply").find('.acctno').val(financierAcct);
						getAfterData();
					}
					else{
						var html = "";
						for(var i=0;i<result.length;i++){
							if(result[i]){
								html += '<tr align="center"><td><input type="radio" data-accno='+result[i].acctNo+' name="modal-accno"></td>><td>'+result[i].acctNo+'</td></tr>'
							}
						}
						$("#list-total").html(result.length);
						$("#list-result").html(html);
						$("#list-result").find("input[type='radio']:first").attr("checked","checked");
						$("#modal-select").modal();
					}
				}
			}
		});
		
	})
	.on('change','[name=appliedQuota]',function(){
		feeCount();
		getAfterData();
	})
	.on('change','[name=rate]',function(){
		feeCount();
	})
	.on('change','[name=termLength]',function(){
		feeCount();
	})
	.on('change','[name=termToDays]',function(){
		feeCount();
	})
	.on('change','[name=warrantyType]',function(){
		feeCount();
	});
	function getAfterData(){//融资人，融资会员席位费及到期日
		var appliedQuota = $("#form-apply").find('[name=appliedQuota]').val();
		var acctNo = $("#form-apply").find('[name=acctNo]').val();
		if(acctNo){
			$.ajax({
				url: global.context+"/web/product/financeapply/getName/"+acctNo,
				type: 'GET',
				contentType: 'application/json;charset=utf-8',
				error: function(){
				},
				success: function(response){
					var financierName = JSON.parse(response).name;
					var dueDate = JSON.parse(response).seatFee ? JSON.parse(response).seatFee.endDt:null;
					if(financierName){
						$('.financier-name').val(financierName);
						counterGuarantee(JSON.parse(response).type.code);
						if(dueDate){
							$('#financierSeatFee').html("到期日："+dueDate);
						}else{
							if(appliedQuota && !isNaN(appliedQuota)){
								$.ajax({
									url: global.context+"/web/product/financeapply/getFee",
									type: 'POST',
									dataType: 'json',
									data: JSON.stringify({"appliedQuota":appliedQuota}),
									contentType: 'application/json;charset=utf-8',
									error: function(){
									},
									success: function(response){
										$('#financierSeatFee').html("￥ " + formatNum(Number(response.financierSeatFee).toFixed(2)) + " 元");
										option.dataExtension.financierSeatFee = Number(response.financierSeatFee).toFixed(2);
									}
								});
							}else{
								$('#financierSeatFee').html("");
							}
						}
						$("#modal-select").modal("hide");
					}else{
						$('#financierSeatFee').html("");
						$("#form-apply").find('[name=acctNo]').val("");
						$('.financier-name').val("");
						$.pnotify({
						    text: "该交易账号错误或者未开通融资权限，请重新输入！",
						    type: 'error'
						});
						$('#main-tab li:eq(1)').show();
					}
				}
			});	
		}else{
			$('.financier-name').val("");
			$('#financierSeatFee').html("");
		}
	}
	function feeCount(){//保证金及费用计算
		var appliedQuota = $("#form-apply").find('[name=appliedQuota]').val();
		var rate = $("#form-apply").find('[name=rate]').val();
		var termLength = $("#form-apply").find('[name=termLength]').val();
		var termToDays = $("#form-apply").find('[name=termToDays]').val();
		var warrantyType = $("#form-apply").find('[name=warrantyType]').val();
		var ready = false;
		if(termLength == "13" && appliedQuota && !isNaN(appliedQuota) && rate && !isNaN(rate) && termToDays && !isNaN(termToDays) && warrantyType)
			ready = true;
		else if( appliedQuota && !isNaN(appliedQuota) && rate && !isNaN(rate) && termLength && !isNaN(termLength) && warrantyType)
			ready = true;
		appliedQuotaOld = appliedQuota;
		if(ready){
			var result = {};
			result.financeFee = Number(appliedQuota*rate/100/12).toFixed(2);
			if(warrantyType == "PRINCIPALINTEREST" || warrantyType == "PRINCIPAL"){
				result.warrantFee = Number(appliedQuota*feeRate.warrantFee).toFixed(2);
			}else{
				result.warrantFee = 0;
			}
			result.contractFee = Number(appliedQuota*feeRate.contractFee).toFixed(2);
			if(termLength == "13"){
				result.riskFee = Number(appliedQuota*feeRate.riskFee*termToDays/30).toFixed(2);
				result.serviceFee = Number(appliedQuota*feeRate.serviceFee*termToDays/30).toFixed(2);
			}else{
				result.riskFee = Number(appliedQuota*feeRate.riskFee*termLength).toFixed(2);
				result.serviceFee = Number(appliedQuota*feeRate.serviceFee*termLength).toFixed(2);
			}
			$('#financeFee').html("￥ " + formatNum(result.financeFee) + " 元");
			$('#warrantFee').html("￥ " + formatNum(result.warrantFee) + " 元");
			$('#contractFee').html("￥ " + formatNum(result.contractFee) + " 元");
			$('#riskFee').html("￥ " + formatNum(result.riskFee) + " 元");
			$('#serviceFee').html("￥ " + formatNum(result.serviceFee) + " 元");
			option.dataExtension.financeFee = result.financeFee;
			option.dataExtension.warrantFee = result.warrantFee;
			option.dataExtension.contractFee = result.contractFee;
			option.dataExtension.riskFee = result.riskFee;
			option.dataExtension.serviceFee = result.serviceFee;	
		}		
	}
	function formatNum(strNum) {
		if (strNum.length <= 3){return strNum;}
		if (!/^(\+|-)?(\d+)(\.\d+)?$/.test(strNum)){return strNum;}
		var a = RegExp.$1, b = RegExp.$2, c = RegExp.$3;
		var re = new RegExp();
		re.compile("(\\d)(\\d{3})(,|$)");
		while (re.test(b)) {
			b = b.replace(re, "$1,$2$3");
		}
		return a + "" + b + "" + c;
	}
	//add from 相关
	var bindAddEvent = function( name ){
		var addForm = $('#add-'+name+'-form');
		$('#add-'+name+'-form').validate({
			onsubmit: false,
		});
		$('#link-add-'+name).on('click',function(){
			$('#add-'+name+'-form').show();
			$(window).trigger("resize");
			$('#link-add-'+name).hide();
		});
		$('#btn-add-'+name).on('click',function(){
			if($('#add-'+name+'-form').valid()){
				addOption.action = "../financeapply/validate/" + action[name];
				var call = util.ajax_submit(addForm,addOption);
				call.success(function(response){
					if(response.code == "ACK"){
						var tabledata = getFormData("#add-"+name+"-form").tableData;
						var listdata = getFormData("#add-"+name+"-form").listData;
							$('#add-'+name+'-form').hide();
							$(window).trigger("resize");
							$('#link-add-'+name).show();
							if($('#add-'+name+'-table tbody').find('tr').length == 0){
								$('#add-'+name+'-table').show();
							}
							setTableData("#add-"+name+"-table",tabledata);
							dataExtensionCopy[name+"List"].push(listdata);
							clearFormData('#add-'+name+'-form');
							$('#form-btn-cancel').attr("data-notice","true");
					}
				});	
			}
		});
		$('#btn-cancel-'+name).on('click',function(){
			$('#add-'+name+'-form').hide();
			$('#link-add-'+name).show();
			$("#add-"+name+"-form").find("label.invalid").remove().end()[0].reset();
		});
		$('#add-'+name+'-table tbody').on('click','.btn-delete-data',function(){
			var $tr = $(this).parent().parent().parent();
			dataExtensionCopy[name+"List"].splice($tr.prevAll().length,1);
			$tr.remove();
			if($('#add-'+name+'-table tbody').find('tr').length == 0){
				$('#add-'+name+'-table').hide();
			}
		});
	};
	bindAddEvent("house");
	bindAddEvent("car");
	bindAddEvent("movable");
	bindAddEvent("natural");
	bindAddEvent("legal");
	bindAddEvent("asset");
	bindAddEvent("liability");
	$('#add-house-form').on('change','.coowner-type',function(){
		if(this.value == "INDEPENDENT"){
			$('.coower-hide').attr("disabled",true).val("");
		}else if(this.value == "SHARE"){
			$('.coower-hide').attr("disabled",false);
		}
	});
	$(".add-form input[name=premisesPermitNo]").on('change',function(){
		var unique = true;
		for(var i=0;i<dataExtensionCopy["houseList"].length;i++){
			var item = dataExtensionCopy["houseList"][i];
			if(item.premisesPermitNo == this.value){
				unique = false;break;
			}
		}
		if(!unique){
			$(this).rules("add",{
				businessError:true,
				messages:{
					businessError:"该产权证号已被使用，请重新输入！"
				}
			});
		}else{
			$(this).rules("remove","businessError");
		}
	});
	$(".add-form input[name=registNo]").on('change',function(){
		var unique = true;
		for(var i=0;i<dataExtensionCopy["carList"].length;i++){
			var item = dataExtensionCopy["carList"][i];
			if(item.registNo == this.value){
				unique = false;break;
			}
		}
		if(!unique){
			$(this).rules("add",{
				businessError:true,
				messages:{
					businessError:"该登记编号已被使用，请重新输入！"
				}
			});
		}else{
			$(this).rules("remove","businessError");
		}
	});

	/* 表单提交*/
	function addOtherData(){
		option.dataExtension.financierIndustryType = {"code":$('#form-apply [name=financierIndustryType]').val()};
		option.dataExtension.otherPledge = $('.other-pledge').val();
		option.dataExtension.otherMortgage = $('.other-mortgage').val();
		option.dataExtension.notes = $('.notes').val();
		//productID,附件,addform,保证金及费用等都有各自更新
	}
	var applyForm = $('#form-apply');	
	applyForm.validate({
		onsubmit: false,
	});
	$('#form-btn-save').click(function(){
		$(this).attr("disabled");
		if(applyForm.valid()){
			addOtherData();
			option.dataExtension.productId = ($('#productId').html().length == 0) ? null : $('#productId').html();
			if($('#productId').attr("data-type") == "edit"){
				
				option.action="../financeapply/save/edit";
			}else{
				option.action="../financeapply/save/apply";
			}
			var call = util.ajax_submit(applyForm,option);
			call.success(function(response){
				if(response&&response.code == "ACK"){
					$('#productId').html(response.data);
					$(this).removeAttr("disabled");
					$('#table-search-result').dataTable().fnPageChange(0,true);
					$('#form-btn-cancel').attr("data-notice","false");
//					if($('#productId').attr("data-type") == "edit"){
//						var page = $('#table-search-result').dataTable().fnPagingInfo().iPage;
//						$('#table-search-result').dataTable().fnPageChange(page,true);
//					}
//						else{
//						var page = $('#table-search-result').dataTable().fnPagingInfo().iTotalPages;
//						$('#table-search-result').dataTable().fnPageChange(page-1,true);
//					}
				}
			});
		}else{
			$(this).removeAttr("disabled");
		}
	});	
	$('#form-btn-submit').click(function(){
		if(applyForm.valid()){
			addOtherData();
			option.dataExtension.productId = ($('#productId').html().length == 0) ? null : $('#productId').html();
			if($('#productId').attr("data-type") == "edit"){
				option.action="../financeapply/submit/edit";
			}else{
				option.action="../financeapply/submit/apply";
			}
			var call = util.ajax_submit(applyForm,option);
			call.success(function(response){
				if(response&&response.code == "ACK"){
					$('#table-search-result').dataTable().fnPageChange(0,true);	
					pusher.pop();
				}
			});
		}
	});
	$('#btn-file-upload').click(function(){
		$("#fileupload").click();
	});
	$('.panel-heading').click(function(){
		$(window).trigger("resize");
	});
	//是否编辑过
	if($('#productId').attr("data-type") == "apply"){
		everChange();
	}
	/*编辑状态的初始化*/
	if($('#productId').attr("data-type") == "edit"){
		var productId = $('#productId').html();//在点击按钮时已设置。
		$.ajax({
			url: global.context+"/web/product/edit/"+productId,
			type: 'GET',
			contentType: 'application/json;charset=utf-8',
			error: function(){
			},
			success: function(response){
				var json = JSON.parse(response);
				setFormData(["#form-apply","#remark"],json);
				$(".other-mortgage").val(json["otherMortgage"]);
				$(".other-pledge").val(json["otherPledge"]);
				$('#form-apply [name=financierName]').val(json.user.name);
				if(json.financierIndustryType){
					$('#form-apply [name=financierIndustryType]').val(json.financierIndustryType.code);
				}
				getAfterData();
				feeCount();
				for(var i=0;i<json.productMortgageResidentialDetailsDtoList.length;i++){
					$('#add-house-table').show();
					setTableData("#add-house-table",json.productMortgageResidentialDetailsDtoList[i]);
					dataExtensionCopy["houseList"].push(getTableData(json.productMortgageResidentialDetailsDtoList[i]));
				}
				for(var i=0;i<json.productMortgageVehicleDetailsDtoList.length;i++){
					$('#add-car-table').show();
					setTableData("#add-car-table",json.productMortgageVehicleDetailsDtoList[i]);
					dataExtensionCopy["carList"].push(getTableData(json.productMortgageVehicleDetailsDtoList[i]));
				}
				for(var i=0;i<json.productPledgeDetailsDtoList.length;i++){
					$('#add-movable-table').show();
					setTableData("#add-movable-table",json.productPledgeDetailsDtoList[i]);
					dataExtensionCopy["movableList"].push(getTableData(json.productPledgeDetailsDtoList[i]));
				}
				for(var i=0;i<json.productWarrantPersonDtoList.length;i++){
					$('#add-natural-table').show();
					setTableData("#add-natural-table",json.productWarrantPersonDtoList[i]);
					dataExtensionCopy["naturalList"].push(getTableData(json.productWarrantPersonDtoList[i]));
				}
				for(var i=0;i<json.productWarrantEnterpriseDtoList.length;i++){
					$('#add-legal-table').show();
					setTableData("#add-legal-table",json.productWarrantEnterpriseDtoList[i]);
					dataExtensionCopy["legalList"].push(getTableData(json.productWarrantEnterpriseDtoList[i]));
				}
				for(var i=0;i<json.productAssetDtoList.length;i++){
					$('#add-asset-table').show();
					setTableData("#add-asset-table",json.productAssetDtoList[i]);
					dataExtensionCopy["assetList"].push(getTableData(json.productAssetDtoList[i]));
				}
				for(var i=0;i<json.productDebtDtoList.length;i++){
					$('#add-liability-table').show();
					setTableData("#add-liability-table",json.productDebtDtoList[i]);
					dataExtensionCopy["liabilityList"].push(getTableData(json.productDebtDtoList[i]));
				}
				for(var i=0;i<json.productAttachmentDetailsDtoList.length;i++){
					var file = json.productAttachmentDetailsDtoList[i];
					var tr = $('<tr></tr>');
					if(util.is_pdf(file.path)){
						tr.html("<td>"+file.orgFileName+"</td><td><img class='financeapply-upload-img' height='100'  src='/assets/img/pdf.jpg' data-pdfpath='"+file.path+"'/></td>" +
								"<td><div class='btn-group btn-group-xs' data-id='"+ file.file +"'>" +
									"<button type='button' class='btn btn-mf-primary btn-delete-img'>" +
									"<i class='fa fa-times fa-lg' title='删除'></i></button></div></td>");
						}else{
						tr.html("<td>"+file.orgFileName+"</td><td><img class='financeapply-upload-img' height='100' src='"+file.path+"'/></td>" +
								"<td><div class='btn-group btn-group-xs' data-id='"+ file.file +"'>" +
									"<button type='button' class='btn btn-mf-primary btn-delete-img'>" +
									"<i class='fa fa-times fa-lg' title='删除'></i></button></div></td>");
					}						
				
					$('#image-table tbody').append(tr);
					option.dataExtension.productAttachmentDetailsDtoList.push({"file":file.file,"createTs":file.createTs});
				}
				if($('#image-table tbody').find('tr').length > 0){
					$('#image-table').show();
				}
				option.dataExtension.createTs = json.createTs;
				everChange();
			}
		});
	}
	var popText = {"financeFee":"本金额为融资（人）企业在融资时需要缴纳的合同保证金，该金额为参考金额，以实际冻结金额为准。该保证金会在融资项目审核通过后按照实际金额冻结！融资成功签约，放款后解冻！如在申购过程中出现撤单情况则不会返还融资人（企业）！<br/>计算规则：一个月利息，即融资申请额*年利率/12）",
			"warrantFee":"该金额为参考金额，以实际冻结金额为准。该保证金会在平台放款审批通过后按照实际金额冻结！融资包撤单后解冻！如在最后一期还款时有违约情况需要担保机构代偿，则会按实际违约金额扣取该保证金！</br>（计算规则：融资申请额*<span id='warrant-fee-rule'>10%</span>）",
			"contractFee":"该金额为参考金额，以实际冻结金额为准。该保证金会在平台放款审批通过后按照实际金额冻结！如在最后一期还款时会解冻并扣取该保证金！</br>（计算规则：融资申请额*<span id='contract-fee-rule'>0%</span>）",
			"serviceFee":"本金额为融资（人）企业需要缴纳给我公司的服务费用，本金额为参考金额，实际扣取金额与该金额可能会有小幅度偏差，该费用在放款时由系统扣取！<br/>（计算规则：融资申请额*<span id='service-fee-rule'>0.4%</span>*期限值，对以日计息融资项目，期限值为天数则除以30）",
			"riskFee":"本金额为融资（人）企业需要缴纳给我公司的风险管理费用，本金额为参考金额，实际扣取金额与该金额可能会有小幅度偏差，该费用在放款时由系统扣取！！<br/>（计算规则：融资申请额*<span id='risk-fee-rule'>0.1%</span>*期限值，对以日计息融资项目，期限值为天数则除以30）",
			"financierSeatFee":"本金额为融资（人）企业注册成为我公司融资会员时需要缴纳的席位费用，本金额为参考金额，实际扣取金额与该金额可能会有小幅度偏差，该费用在本年度首次放款时由系统扣取，一年一缴。"};
	function costPopover(container,text){
		$(container).popover({
			trigger:'hover',
			html:true,
			placement:'right',
			selector:true,
			container: 'body',
			content:"<div class='mypopover'>"+ popText[text] +"</div>",
		});		
	}
	costPopover("#financeFee-rule","financeFee");
	costPopover("#warrantFee-rule","warrantFee");
	costPopover("#contractFee-rule","contractFee");
	costPopover("#serviceFee-rule","serviceFee");
	costPopover("#riskFee-rule","riskFee");
	costPopover("#financierSeatFee-rule","financierSeatFee");
}
);