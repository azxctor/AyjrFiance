define(['jquery',
        'module/popup',
        'module/util',
        'module/upload_tool',
        'global',
        'module/ajax',
        'jquery.pnotify',
        'jquery.ui',
        'bootstrap',
        'bootstrap-datepicker',
        'bootstrap-datepicker.zh-CN',
        'jquery.validate',
        'jquery.metadata',
        'jquery.validate.methods',
        'bootstrapx-popoverx',
        'plugins',
    ],
function($, popup, util,upload,global){
	var Form=function(){};
	var formOptions={
		onReset:function(){return false;}
	};
	Form.prototype={
			init:function(option){
				option=option||formOptions;
				$(".datepicker").datepicker({
					 autoclose: true,
					 format: "yyyy-mm-dd",
					 weekStart: 1,
					 todayBtn: false,
					 language: 'zh-CN'
				});
				$(".file-upload").fileUploader({
					url:global.context+"/web/uploadfile"
				});
				
			/*	popoverx
				$(".image-example-popoverx").popoverx({
          			ensure_visiable : true,
          			placement : 'top',
          			width: 204,
          			height: 151,
          			elementToAppend:  $(".right-blk-body").length==0? $(".blk-body"):  $(".right-blk-body"),
          			onShown: function(){
          				var src = $(this.element).attr("data-src");
          				var html = '<img src="'+src+'" width="100%";></img>';
          				this.$tip.find('.popover-content').html(html);
          				this.resetPosition();
          			}
          		});*/
				
				
				/*重置事件*/
				$(".form-btn-reset").on("click",function(){
					var $currentForm=$(this).parents("form").eq(0);
					$currentForm.each(function(){
						this.reset();
					});
					$currentForm.find(".file-upload").removeAttr("data-img").find("img.upload-img").attr("src","")
		 		    .end().find("span.upload-filename").text("");
					$currentForm.find(".data-binder").removeAttr("data-val");
					$currentForm.find(".upload-thumbnail").hide();
					if(option&&option.onReset&&typeof option.onReset==="function"){
						option.onReset($currentForm);
					}
					if(!$currentForm.hasClass("view-mode")){
						$currentForm.find("select,input").each(function(){
							if(this.type=="file"){
								return;
							}
							$(this).removeAttr("data-inited").trigger("change");
						});
					}
					return false;
				});
			}
	};
	
	return new Form();
});