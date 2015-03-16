require(['jquery',
        'global',
        'module/util',
        'module/popup',
        'module/ajax',
        'bootstrap',
        'jquery.cookie',
        'requirejs/domready!'],
    function($, global, util, popup){
		//删除原来版本的cookie
		$.removeCookie('username', { expires: 31,path: '/platform/web/members'});
		$.removeCookie('username', { expires: 31,path: '/platform'});
	
        $("#captcha-img").attr("src", global.context+ "/web/captcha?r=" + Math.random());
        $("#btn-login").removeAttr("disabled");
        var enableSignin = true;
        var remCheckbox = document.getElementById("rem-chkbox");
        // Add event handler for captcha image click
        $("#captcha-img").click(function() {
            $(this).attr("src", global.context+ "/web/captcha?r=" + Math.random());
        });
        if($.cookie('username')){
           // remCheckbox.checked = true;
            $("#user-name").val($.cookie('username'));
        }
        
        $("#signin-form").on("submit",function(){
            var isRem;
            if(!enableSignin){
                return false;
            }
            enableSignin = false;

           /* isRem = remCheckbox.checked;
            $.removeCookie('username',{ expires: 31,path: '/'});
            if(isRem){
            	$.cookie('username', $("#user-name").val(), { expires: 31,path: '/'});
            }*/
            util.ajax_submit("#signin-form").complete(function(xhr){
                var result = $.parseJSON(xhr.responseText);
                //Write own logic if necessary
                if(result.code!="REDIRECT"){
                    $('#captcha-img').attr("src",
                            global.context+ "/web/captcha?r=" + Math.random());
                }else{
                	 isRem = remCheckbox.checked;
                     $.removeCookie('username',{ expires: 31,path: '/'});
                     if(isRem){
                     	$.cookie('username', $("#user-name").val(), { expires: 31,path: '/'});
                     }
                }
                enableSignin = true;
            });
            return false;
        });

        $('#forgetpwd').on('click', function(){
            popup({
                title:'忘记密码',
                msg: '为了您的账号安全，请联系安益金融官方客服热线:4009997327，给您带来的不便，请您谅解!',
                detail: '我们的客服会尽快帮您解决问题, 请耐心等待。'
            });
        });



        // Add jQuery validate to sign-in form
        /*$('#signin-form').validate({
         submitHandler: function(form) {
         util.ajax_submit(form).complete(function(xhr){
         var result = $.parseJSON(xhr.responseText);
         //Write own logic if necessary
         $('#captcha-img').attr("src",
         global.context+ "/web/captcha?r=" + Math.random());
         });
         }
         });*/
    });
