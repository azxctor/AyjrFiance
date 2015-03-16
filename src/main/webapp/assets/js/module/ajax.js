define(['jquery',
        'module/popup',
        'module/util',
        'jquery.pnotify', 
        'jquery.validate.fix',
        'jquery.validate.language',
        'jquery.validate.methods',
        'plugins'], 
function($, popup, util){
	
	$(document).ajaxError(function(event, xhr, settings, thrownError ) {
	    try{
		    var error = $.parseJSON(xhr.responseText);
		    if(error.code === 'SESSION_TIME_OUT'){
		    	popup({
			    	msg: error.message,
			    	title: '登录会话超时',
			    	buttons: {
						'确定': {
							primary: true,
							callback: function(event){
								this.$element.modal('hide');
								window.location.reload();
							}
						}
					}
			    });
		    }else{
		    	popup({
		    		title: '操作异常',
			    	msg: error.message,
			    	detail: error.data
			    });
		    }
		}catch(Error){
			popup({
				title: '操作异常',
				msg: '网络异常，请重试一下！给您带来不便，敬请谅解！',
		    	detail:  Error || 'Unknown error occurred'
		    });
		}
	});
	
	$(document).ajaxSuccess(function(event, xhr, settings) {
		try{
			// Status 206 (Partial Content) indicates that the partial HTML string
			// are returned as the ajax result, we need avoid to parseJSON against
			// the HTML string.
			if(xhr.status != 206){
				var result = $.parseJSON(xhr.responseText);
				if(result && result.hasOwnProperty('code') &&
						result.hasOwnProperty('message') &&
						result.hasOwnProperty('data')){
					if( result.code === 'ACK'){
						$.pnotify({
						    text: result.message,
						    type: 'success'
						});
					}else if(result.code === 'NACK'){
						$.pnotify({
						    text: result.message,
						    type: 'error'
						});
					}else if(result.code === 'BUSINESS_ERROR'){
						popup({
							title:'操作失败',
					    	msg: result.message,
					    	detail: result.data
					    });
					}else if(result.code === 'VALIDATION_ERROR'){
						$.pnotify({
						    text: result.message,
						    type: 'error'
						});
						$.each(result.data, function(i, obj){
							if(obj && obj.formId){
								var errors = {};
								if((obj.generalError || []).length !=0){
									$('#' + obj.formId).find('.general-error').show().text(
											(obj.generalError || []).join(','));
								}
								if(obj.fieldErrors){
									$('#' + obj.formId).find('input,textarea,select,div[data-error-prop="true"],.file-upload')
									.each(function(){
										var prop = $(this).attr('data-error-field') || $(this).attr('name');
										if(prop && obj.fieldErrors.hasOwnProperty(prop)){
											errors[prop] = obj.fieldErrors[prop];
										}
									});
									$('#' + obj.formId).validate().showErrors(errors);
									//fix bug of nicescroll
									$(window).trigger("resize");
								}
							}
						});
					}else if(result.code === 'REDIRECT'){
						util.redirect(result.data);
					}
				}
			}
		}catch(Error){
			popup({
				title: '操作失败',
				msg: '系统错误造成请求失败。',
		    	detail:  Error || 'Unknown error occurred'
		    });
		}
	});
	
	return {};
});