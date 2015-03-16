// Avoid `console` errors in browsers that lack a console.
(function() {
    var method;
    var noop = function () {};
    var methods = [
        'assert', 'clear', 'count', 'debug', 'dir', 'dirxml', 'error',
        'exception', 'group', 'groupCollapsed', 'groupEnd', 'info', 'log',
        'markTimeline', 'profile', 'profileEnd', 'table', 'time', 'timeEnd',
        'timeStamp', 'trace', 'warn'
    ];
    var length = methods.length;
    var console = (window.console = window.console || {});

    while (length--) {
        method = methods[length];

        // Only stub undefined methods.
        if (!console[method]) {
            console[method] = noop;
        }
    }
}());

//Set jQuery pnotify plugin globally.
if($.pnotify){
	$.pnotify.defaults.history = false;
	$.pnotify.defaults.width = '350px';
	$.pnotify.defaults.styling = 'fontawesome';
	$.pnotify.defaults.addclass = 'custom stack-topright';
	$.pnotify.defaults.sticker = false;
	$.pnotify.defaults.hide = true;
	$.pnotify.defaults.stack = {"dir1": "down", "dir2": "left", "firstpos1": 60, "firstpos2": 25, "spacing1": 5};
}
// Set jQuery Validation plugin globally.
if($.validator){
	$.validator.setDefaults({
		debug: true,
		errorClass: 'invalid',
		validClass: 'valid',
		highlight : function(element, errorClass,validClass) {
			$(element).addClass(errorClass).removeClass(validClass);
		},
		unhighlight : function(element, errorClass,validClass) {
			$(element).removeClass(errorClass).addClass(validClass);
		},
		messages:{
			idcard:'请输入正确的身份证号码',
			extension:'文件格式不正确'
		},
		errorPlacement: function(error, element){
			if($(element).parent().hasClass("input-group")){
				element.parent().after(error);
				return;
			}
			 error.insertAfter(element); 
		}
	});
}

// Set jQuery Metadata plugin globally
if($.metadata){
	$.metadata.setType("attr","data-validate");
}

//enabel placeholder in ie<=8
if($.fn && $.fn.placeholder)
{
	$('input').placeholder();
}	
if($.fn.popoverx){
	$.fn.popoverx.defaults.elementToAppend=$(".right-blk-body");
}

//append triangle_div to tab>li

//$('.micro-nav-tabs li').append('<div class="triangle"></div>');


