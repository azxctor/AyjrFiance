require(['jquery',
         'global',
         'module/util',
         'module/ajax',
         'bootstrap',
         'requirejs/domready!'], 
function($, global, util){
	$('#member-detail-form').validate({
		submitHandler: function(form) {
			util.ajax_submit(form);
	    },
	    highlight: function(element, errorClass, validClass) {
	    	$(element).addClass(errorClass).removeClass(validClass);
	    	$(element).parent().removeClass('has-success')
	    		.addClass('has-error').find('.fa').remove();
	    	$(element).parent().append('<i class="fa fa-exclamation-circle form-control-feedback"></i>');
	    },
	    unhighlight: function(element, errorClass, validClass) {
	    	$(element).removeClass(errorClass).addClass(validClass);
	    	$(element).parent().removeClass('has-error')
    			.addClass('has-success').find('.fa').remove();
	    	$(element).parent().append('<i class="fa fa-check form-control-feedback"></i>');
	    }
	});
});

function loadCityOptions(){
	var provinceCode = $('#provinceSelection').val();
	var citySelect = $('#citySelection');
	citySelect.empty();
	citySelect.append(new Option('', ''));
	
	if(provinceCode){
		var data = "province="+provinceCode;
		var url = "provice/ajax/" + provinceCode;
		var successFn = function(data){
			var json = eval(data);
            $.each(json, function(idx, city) {
	        	var option = new Option(city.name, city.code);
	        	citySelect.append(option);
	        });
		};
        ajaxFunction(data,url,successFn);
	}
};



function uploadFile(fileElementId, errorSpanId){
	//$("#loading").ajaxStart(function(){$(this).show();}).ajaxComplete(function(){$(this).hide();});
	$.ajaxFileUpload({
		url:'uploadfile',
		secureuri:false,
		fileElementId:fileElementId,
		dataType:'json',
		success:function(data, status){        
			data = data.replace("<PRE>", '');
			data = data.replace("</PRE>", '');
			data = data.replace("<pre>", '');
			data = data.replace("</pre>", '');
			if(data.substring(0, 1) == 0){
				$("img[id='uploadImage']").attr("src", data.substring(2));
				$('#' + errorSpanId).html("successful!");
			}else{
				$('#' + errorSpanId).html('error1!');
			}
		},
		error:function(data, status, e){ 
			$('#' + errorSpanId).html('error2!');
		}
	});
}

