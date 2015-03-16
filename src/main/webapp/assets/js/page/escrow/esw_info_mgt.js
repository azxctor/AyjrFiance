require(['jquery',
         'global',
         'module/util',
         'module/datatables',
         'module/ajax',
         'module/cascading_listener',
         'bootstrap',
         'jquery.placeholder',
         'requirejs/domready!'], 
function($, global, util, datatables){
	
	$('input').placeholder();
	

	//账户激活Form
	$('#activate-form').validate({
		submitHandler: function(form) {
			util.ajax_submit("#activate-form").complete(function(xhr) {
				var result = $.parseJSON(xhr.responseText);
				if(result.code=="ACK"){
					setTimeout(function(){
						location.reload(true);
					},1000);
				}
			});
	    }
	});
	
	//密码修改Form
	$('#password-form').validate({
		submitHandler: function(form) {
			$('#password-form').find('.captcha-link').removeAttr('disabled');
			util.ajax_submit("#password-form").complete(function(xhr) {
				var result = $.parseJSON(xhr.responseText);
				if(result.code=="ACK"){
					//$("#password-form")[0].reset();
					location.reload(true);
				}
			});
	    }
	});
	
	//电话号码修改Form
	$('#phone-number-form').validate({
		submitHandler: function(form) {
			$('#phone-number-form').find('.captcha-link').removeAttr('disabled');
			util.ajax_submit("#phone-number-form").complete(function(xhr) {
				var result = $.parseJSON(xhr.responseText);
				if(result.code=="ACK"){
					location.reload(true);
					//$("#phone-number-form")[0].reset();
				}
			});
	    }
	});
	
	function timer(form,intDiff){
		var $cl = $(form).find('.captcha-link');
	    var interval = setInterval(function(){
	    var day=0,
	        hour=0,
	        minute=0,
	        second=0;//时间默认值        
	    if(intDiff > 0){
	        day = Math.floor(intDiff / (60 * 60 * 24));
	        hour = Math.floor(intDiff / (60 * 60)) - (day * 24);
	        minute = Math.floor(intDiff / 60) - (day * 24 * 60) - (hour * 60);
	        second = Math.floor(intDiff) - (day * 24 * 60 * 60) - (hour * 60 * 60) - (minute * 60);
	    }
	    if (minute <= 9) minute = '0' + minute;
	    if (second <= 9) second = '0' + second;
	    $cl.html(second+'秒');
	    $cl.attr('disabled', 'disabled');
	    if (second == '00'){
	    	clearInterval(interval);
	    	$cl.html('获取手机验证码');
	    	if ($(form).find('input[name="mobile"]').hasClass('valid')||$(form).find('input[name="mobile"]').is(":hidden")){
	    		$cl.removeAttr('disabled');
	    	}
	    }
	    intDiff--;
	    }, 1000);
	}
	
	$(".captcha-link").click(function() {
		var intDiff = parseInt(59);//倒计时总秒数量
		var parentForm=$(this).parents("form")[0];
		timer(parentForm,intDiff);
		$("input[name=authCode]").show();
		var phone = $(parentForm).find("input[name=mobile]").val();
		$.ajax({
			url : global.context + "/web/esw/account/sendmessage?mobile="+phone,
			type : "get",
			dataType : 'json',
			contentType : 'application/json; charset=UTF-8',
		}).done(function(resp) {
		});
	});
	
	$('input[name="mobile"]').on('blur', function() {
		var parentForm=$(this).parents("form")[0];
		var $cl = $(parentForm).find('.captcha-link');
		if ('获取手机验证码' != $cl.html()){
			return;
		}
		if ($(this).hasClass('valid')){
			$cl.removeAttr('disabled');
		}
	});
	
	//绑定银行账户
	$('#bank-info-form').validate({
		submitHandler: function(form) {
			util.ajax_submit("#bank-info-form").complete(function(xhr) {
				var result = $.parseJSON(xhr.responseText);
				if(result.code=="ACK"){
					location.reload(true);
					/*$("#reg-btn").attr("disabled",true);
					$("#binding-form")[0].reset();*/
				}
			});
	    }
	});
	
	//解除绑卡
	$("#remove-card-btn").on("click",function(){
		$("#remove-modal").modal("show");
		$('#remove-binding').click(function(){
			$.ajax({
				url : global.context + "/web/esw/unbinding",
				type : "GET",
				dataType : "json",
				contentType : "application/json; charset=UTF-8"
			}).done(function(resp){
				if(resp.code == "ACK"){
					$("#remove-modal").modal("hide");
					self.location.reload();
				}
			});
		});
	});
	
	//操作列表
	function _banketailButton(data,type,extra){
		var bankCardNo = extra.bankCardNo;//银行账号
		var bankCardName = extra.bankCardName;//账户名称
		var bankTypeCode = extra.bankTypeCode;//开户银行（code）
		var bankName = extra.bankName;//发卡银行（详细）
		var bankCode = extra.bankCode;
		var provinceCode = extra.provinceCode;//省份（code）
		var cityCode = extra.cityCode;//城市（code）
		return "<div class='btn-group'><button type='button' title='选择' class='btn btn-xs btn-mf-primary btn-now-choose cancel-btn' data-bankCardNo="+bankCardNo+" data-bankName="+bankName+" data-bankCode="+bankCode+" data-bankCardName="+bankCardName+" data-bankTypeCode="+bankTypeCode+" data-provinceCode="+provinceCode+" data-cityCode="+cityCode+">选择</button></div>";
		//return "";
	};
	//
	var browse_table = null;
    var browsetable = function(){
        var options = {};
        //alert(global.context);
        options.tableId = '#browse-table';
        options.sAjaxSource = global.context+"/web/esw/getBindedBankList";
        options.functionList = {"_banketailButton":_banketailButton,};
      //  options.rawOptions = {"fnDrawCallback" : _fnsubscribe};
     //   options.aaSorting = [[7,"desc"]];
        browse_table = datatables.init(options);
       // browse_table.setParams(util.getSearchData('#bank-info-form'));
        return browse_table.create();
    }();
	 
	//银行卡浏览按钮
	$("#browse-btn").on("click",function(){
		$("#browse-modal").modal("show");
		browse_table.invoke("fnDraw");
	});
	//重置按钮
	$("#cancle-reset-btn").on("click",function(){
		$("#bankcardnum").val("");
		$("#bankname").val("");
		$("#bankTypeCode").val("");
		$("#bank-province").val("");
		$("#bank-city").val("");
		$("#subBank").val("");
		
		$("#bankcardnum").removeAttr("disabled");
		$("#bankname").removeAttr("disabled");
		$("#bankTypeCode").removeAttr("disabled");
		$("#bank-province").removeAttr("disabled");
		$("#bank-city").removeAttr("disabled");
		$("#subBank").removeAttr("disabled");
	});
	
	//选择按钮
	$("body").on("click",".btn-now-choose",function(e){
		/*$("#bankcardnum").val(null);
		$("#bankname").val(null);
		$("#bankTypeCode").val(null);
		$("#bank-province").val(null);
		$("#bank-city").val(null);
		$("#subBank").val(null);*/
		var $target=$(e.currentTarget);
		var bankCardNo = $target.attr("data-bankCardNo");
		var bankName = $target.attr("data-bankName");
		var bankCode = $target.attr("data-bankCode");
		var bankCardName = $target.attr("data-bankCardName");
		var bankTypeCode = $target.attr("data-bankTypeCode");
		var provinceCode = $target.attr("data-provinceCode");
		var cityCode = $target.attr("data-cityCode");
		$("#bankcardnum").val(bankCardNo);
		$("#bankname").val(bankCardName);
		$("#bankTypeCode").val(bankTypeCode);
		/*$("#bank-city").val(cityCode);
		$("#subBank").val(bankCode);*/
		$("#bank-city").attr("data-cityval",cityCode);
		$("#subBank").attr("data-subbankval",bankCode);
		$("#bank-province").removeAttr("data-inited");//
		$("#bank-province").val(provinceCode).trigger("change");//选择时的province值
		//$("#bank-city").val(cityCode);
		//$("#subBank").val(bankName);
		//var soptionl = $("#bankTypeCode").find("option");
		//alert("1--"+soptionl.length);
		//alert("2--"+soptionl[1].value);
		/*for(var i=0;i<soptionl.length;i++){
			if(soptionl[i].value == bankTypeCode){
				soptionl[i].attr("selected",true);
			}
		};*/
		$("#bankcardnum").attr("disabled","disabled");
		$("#bankname").attr("disabled","disabled");
		$("#bankTypeCode").attr("disabled","disabled");
		$("#subBank").attr("disabled","disabled");
		$("#bank-province").attr("disabled","disabled");
		$("#bank-city").attr("disabled","disabled");
		
		$("#browse-modal").modal("hide");
	});
	
	//
	$('.btn-collapse').click(function(){
		var btn_id = $(this).attr("id");
		var car_img_id = $("#card-img-id").attr("data-id");
		if(btn_id == "modify-phone"){
			//var car_img_id = false;
			if(car_img_id == "true"){
				$('#card-img').modal('show');
				return false;
			}
		}
			$($(this).data("target")).show();
			$($(this).data("parent")).hide();
			if($(this).data("target")=="#view-row"){
				$("#subBank").attr("disabled","disabled");
				$("#bank-province").attr("disabled","disabled");
				$("#bank-city").attr("disabled","disabled");
			}
			if($(this).data("target")=="#edit-row"){
				$("#subBank").removeAttr("disabled");
				$("#bank-province").removeAttr("disabled");
				$("#bank-city").removeAttr("disabled");
			}
			return false;
		
	});
	
	$("#bank-province").listenChange({
		url:global.context+'/web/esw/getCityInfo?provinceCode=',
		nextElement:$("#bank-city"),
		bindData:function(context,data){
			$.each(data,function(index,item){
				context.append($("<option>").val(item.citycode).text(item.cityname));
			});
		},
		//bindingElement:$("#org-region"),
		initialCallback:function(current,next){
			next.val(next.data("val")).trigger("change");
			
			if($("#bank-city").attr("data-cityval")){
				$("#bank-city").val($("#bank-city").attr("data-cityval")).trigger("change");//选择时的city值
				//$("#bank-city").trigger("change");
				//$("#bank-city").removeAttr("data-cityval");
			}
			
		}
	});
	////省份change事件
	/*$("#bank-province").on("change",function(){
		if($("#bank-city").attr("data-cityval")){
			$("#bank-city").val($("#bank-city").attr("data-cityval")).trigger("change");//选择时的city值
			//$("#bank-city").trigger("change");
			//$("#bank-city").removeAttr("data-cityval");
		}
	});*/
	
	$("#bank-city,#bankTypeCode").on("change",function(){
		var city=$("#bank-city").val();
		var bank=$("#bankTypeCode").val();
		$("#subBank").empty();
		$("#bankId").val("");
		if(city&&bank){	
			$.ajax({
				url:global.context+"/web/esw/getBankInfo?payeeBankId="+bank+"&cityCode="+city,
				type : "get",
				cache:false,
				dataType : 'json',
				contentType : 'application/json; charset=UTF-8',
			}).done(function(data){
				var bank=eval(data);
				if(bank.bankList.length>0){
					$.each(bank.bankList,function(index,item){
						$("#subBank").append($("<option>").val(item.bankcode).text(item.bankname));
					});
					if(!$("#subBank").data("inited")){
						$("#subBank").val($("#subBank").data("val"));
						$("#subBank").data("inited",true);
					}
					$("#bankId").val(bank.bankId);
				}
				
				if($("#subBank").attr("data-subbankval")){
					$("#subBank").val($("#subBank").attr("data-subbankval"));//选择时的subBnak值
					//$("#subBank").removeAttr("data-subbankval");
				}
			});
		}
	});
	
	$("#subBank").on("change",function(){
		 var $subBnk = $("#subBank");
		 if($subBnk) {
			 $('#bankName').val($(this).find(":selected").text());
		 }
	});
		
	$("#bank-province").change();
	
	
	//忘记密码
	$("#forget-pwd").on("click",function(){
		$('#pswd-modal').modal('show');
		$("#reset-pwd-form")[0].reset();
	});
	
	$('#reset-pwd-form').validate({
		submitHandler: function(form) {
			$('#reset-pwd-form').find('.captcha-link').removeAttr('disabled');
			util.ajax_submit("#reset-pwd-form").complete(function(xhr) {
				var result = $.parseJSON(xhr.responseText);
				if(result.code=="ACK"){
					//$("#reset-pwd-form")[0].reset();
					location.reload(true);
					$('#pswd-modal').modal('hide');
				}
			});
	    }
	});
	
	//修改手机号
	//$("#modify-phone").on("click",function(){
	//	alert(1111);
	//	var car_img_id = $("#card-img-id").attr("data-id");
	//	var car_img_id = false;
	//	alert(car_img_id);
	//	if(!car_img_id){
	//		$('#card-img').modal('show');
	//		$('#update-phone-number').attr('display','none');
	//	}
	//});
});
