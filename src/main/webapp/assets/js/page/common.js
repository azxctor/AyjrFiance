require(['jquery',
         'module/menu',
         'module/bread_crumb',
         'global',
         'module/view_pusher',
         'raty',
         'module/raty',
     /*    'jquery.nicescroll',*/
         'bootstrap',
         'numeral',
         'requirejs/domready!'],
function($, mfMenu, mfBread, global,pusher){

    //如果是开发环境，则set domain
    var isDev = $(".is-dev").text();
    if(isDev=="true"){
        try{
            // get current domain
            var domainArray = window.location.hostname.split(".");
            var domain1 = domainArray.pop();
            var domain2 = domainArray.pop();
            // set up the domain to make it cross reports server
            document.domain = domain2+"."+domain1;       //"hengtiansoft.com"
        }catch(error){
            console.log(error);
        }
    }

    //控制全局模态框，点击区域外不关闭
    $('.modal').attr("data-backdrop","static");
    //全局ajax设置
    var threadCount=0;
    $.ajaxSetup({
    	cache : false,
    	beforeSend:function(xhr, settings){
    		//是否触发全局Loading,hideDefaultLoading将不会转菊花
    		//如文件上传控件
    		//$.ajax({
    		//		...
    		// 		hideDefaultLoading:true
    		//});
    		if(!settings.hideDefaultLoading){
    			++threadCount;
    			$(".km-loading-mask").show();
    			$("*:focus").blur();
    		}
    		xhr.hideDefaultLoading=settings.hideDefaultLoading;
    	},
    	complete:function(xhr){
    		if(!xhr.hideDefaultLoading){
    			setTimeout(function(){
    				if(--threadCount){
    	    			$(".km-loading-mask").show();
    	    		}
    	    		else{
    	    			$(".km-loading-mask").hide();
    	    		}
        		},500);
    		}
    	}
    });
    $(window).on("resize",function(){
/*    	$(".right-blk-body").height(($(window).height()-outHeight)+"px");
    	$(".right-blk-body").getNiceScroll().resize();
    	if($(".blk-body").length>0){
	    	$(".blk-body").height(($(window).height()-100)+"px");
	    	$(".blk-body").getNiceScroll().resize();
    	}*/
    });

    //图片预览事件
    $("body").on("click","img.hidden-form-control,img.show-form-control,.upload-img,.financeapply-upload-img",function(e){
    	var url=$(e.currentTarget).attr("src");
        var pdfPath = $(e.currentTarget).attr("data-pdfpath");
    	if(url){
            if(typeof pdfPath ==="undefined") window.open(url);
            else window.open(pdfPath);
    	}
    });
    
    //切换modal显示的位置，时期 DOM显示在body下,这样可以解决z-index的问题
    var checkeventcount = 1, prevTarget;
    $('body').on('show.bs.modal', '.modal', function (e) {
        if(typeof prevTarget == 'undefined' || (checkeventcount==1 && e.target!=prevTarget))
        {  
          prevTarget = e.target;
          checkeventcount++;
          e.preventDefault();
          $(e.target).appendTo('body').modal('show');
        }
        else if(e.target==prevTarget && checkeventcount==2)
        {
          checkeventcount--;
        }
     });
  
    $('body').on('mf.menu.select', function(e, o){
		mfBread.clear()
			.push({
				label : o.md.label,
				click : function(e){
					mfMenu.orgin();
					e.stopPropagation();
				}
			}).push({
				label: o.sd.label
			});
	});
    
    $("body").on('mf.bread.click',function(e,o){
    	pusher.popTo(o.index+1);
    });
    
    if(mfMenu.available){
    	mfMenu.deffer().done(function(){
        	mfMenu.select(global.mc, global.sc).fold();
        });
    }
    
    $(".right-blk-body").scroll(function() {
    	var focused = $('input:focus');
    	if(focused.data("datepicker")){
    		focused.datepicker('hide');
    		$(".right-blk-body").focus();
    	}
    	if(focused.data("datetimepicker")){
    		focused.datetimepicker('hide');
    		$(".right-blk-body").focus();
    	}	
    	if ($('.ui-multiselect-menu').css('display') == 'block'){
    		$('.ui-multiselect-close').trigger('click');
    	}
    });
    
    /*设置主内容显示区域最小高度*/
    var main_container_height =  $('#page-wrapper').find('.right-blk-body').height();
    var copyright_height =  $('#page-wrapper').find('.copy-right').outerHeight(true);
    
    $('#page-wrapper').find('.right-blk-content').css({minHeight: (main_container_height - copyright_height) + 'px'});
    $('#page-wrapper').find('.copy-right').css({visibility: 'visible'});
    
//    var raty_div = $("#raty-div");
//    var star_num = parseInt(raty_div.attr("data-code"));
//    var discount = parseFloat($("#invs_rate").text())*10;
//    if(star_num!=0){
//    	 raty_div.raty({
//	    	number:star_num,
//	    	score:star_num,
//	    	readOnly: true,
//	    	starOff :global.context+'/assets/img/star-off.png',
//	    	starOn  : global.context+'/assets/img/star-on.png'
//    	 });
//    	 
//    	// var discount = parseFloat($("#invs_rate").text())*10;
//    	 if(discount != 10){
//    		 $("#invest-level-li").tooltip({
//    		    	title:"会员等级为"+star_num+"星，9月15日至12月31日可享受交易手续费"+discount+"折优惠",
//    		    	placement:"bottom"
//    		 });
//    	 }
//	    
//    }else{
//    	raty_div.text("普通");
//    	if(discount != 10){
//    		$("#invest-level-li").tooltip({
//    	    	title:"会员等级为普通，9月15日至12月31日可享受交易手续费"+discount+"折优惠",
//    	    	placement:"bottom"
//    	     });
//    	}
//    }
   
});