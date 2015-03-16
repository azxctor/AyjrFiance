require(['global',
         'jquery',
         'module/ajax',
         'bootstrap',
         'raty',
         'module/raty',
        // 'page/common',
         'requirejs/domready!'], 
function(global,$){
	/*---------------------
	    定义变量
	---------------------*/
	var Events = {
	    VIEW_USER_INFO: "event-userinfo",
	    EXIT: "event-userexit",
	    VIEW_INVEST: "event-viewinvest",
	    VIEW_AD1: "event-viewad1",
	    VIEW_FINANCING: "event-viewfinancing",
	    VIEW_AD2: "event-viewad2",
	    VIEW_ANALYSIS: "event-viewanalysis",
	    VIEW_QUOTATION: "event-viewquotation",
	    MORE: "event-openmore"
	};
	
	var showModal = function(modal_arr){ 
		if(modal_arr.length == 0){
			return;
		}
		modal_arr.eq(0).modal();
		for(var i=0;i<modal_arr.length;i++){
			(function(){
				var j = i;
				modal_arr.eq(j).on("hidden.bs.modal",function(){
					if(j+1<modal_arr.length){
						modal_arr.eq(j+1).modal();
					}
					
				});
			})();
		}
	};
	
	
	/*----------------------
	    定义成员函数
	----------------------*/
	//初始化
	var init=function()
	{
		$('.container').find('.lazyloading').each(function(index, el) {
			var data_original = $(el).attr('data-original');
			$(el).attr('src', data_original);
		});
		//showModal($(".modal"));移动到_getListOnlyList()方法后执行
	    //设置控制面板大小
	    _resizeDashboard(); 
	    //弹出窗口列表
	    _getListOnlyList(); 
		
	    
	};
	//绑定事件
	var bindEvents=function()
	{
		//债权转让提交
		$("#transfer-submit").on("click",function(){
			var ischeck = $("#transfer-check").is(":checked");
			if(ischeck){
				$.ajax({
					url: global.context+ "/web/user/setting",
					type: "post",
					dataType: 'json',
	//				contentType: 'application/json; charset=UTF-8',
					data: {"showBulletin":!ischeck}
				});
			}
			$("#transfer-open-modal").modal("hide");
			
			
		});
		
		//申购更新确定
		
		$("#autoinvest-submit").on("click",function(){
			$("#autoinvest-open-modal").modal("hide");
		});
		
		$(".ques-wrapper").on("click",".choice-content",function(e){
			if($(e.target).hasClass("choice-content")){
				var $radio = $(this).siblings().filter("input[type='radio']");
				$radio.prop("checked","checked");
			}
		});
		
		$("#ques-submit").on("click",function(){
			var $age = $(".ques-age");
			var $content = $("#error-message-content"); 
			var $area = $("#error-message"); 
			var reg = new RegExp("^[0-9]*$");
			var $checked = $(".ques-wrapper").find("input[type='radio']:checked");
			if($.trim($age.val()).length == 0 || $checked.length != 7){
				$("#ques-modal-body").animate({scrollTop:0},500);
				$content.text("您还有未回答的问题,请将所有问题答完提交,谢谢!");
				$area.show();
				return;
			}
			
		    if(!reg.test($age.val())){
		    	$("#ques-modal-body").animate({scrollTop:0},500);
		    	$content.text("请输入一个合法的年龄");
		    	$area.show();
		    	return;
		    }
		    
		    var iage = Math.abs(parseInt($age.val())-35);
		    var age_point = (50-iage)<0?0:50-iage;
		    if(parseInt($age.val())>=70){
		    	age_point = 0;
		    }
		    var choice_point = 0;
		    $checked.each(function(){
		    	choice_point += parseInt($(this).attr("data-score"));
		    });
		    var total = age_point + choice_point;
		    
		    var score = {score:total};
		    $.ajax({
				url: global.context+ "/web/risk/questionnaire/submit",
				type: "post",
				dataType: 'json',
				contentType: 'application/json; charset=UTF-8',
				data: JSON.stringify(score)
			}).done(function(resp){
				$("#ques-result-msg").html("谢谢您的合作！您当前的风险偏好类型为 <span class='modal-resp-level'>"+resp.level+"</span>。");
			});
		    $("#ques-modal").modal("hide");
		});
		
		$("#ques-do-next").on("click",function(){
			var bear_level = $.trim($(".risk-bear-level").text());
			if(bear_level.length == 0){
				bear_level = "新手型";
			}
			$("#ques-result-msg").html("由于您尚未提交问卷调查表，您的风险偏好类型将被评定为"+bear_level+"，您的申购上限可能会受到影响，请尽快填写并提交问卷调查表。");
		});
		
	    //监听窗口大小变化事件，重置控制面板的大小
	    $(window).on("resize", function ()
	    {
	        _resizeDashboard();
	    });
	    $("*[data-event^=event]").on("click", function ()
	    {
	        var event = $(this).attr("data-event");
	        switch (event)
	        {
	            //查看用户信息
	            case Events.VIEW_USER_INFO:
	                break;
	            //退出
	            case Events.EXIT:
	                break;
	            //投资
	            case Events.VIEW_INVEST:
	                break;
	            //广告1
	            case Events.VIEW_AD1:
	                break;
	            //融资
	            case Events.VIEW_FINANCING:
	                break;
	            //广告2
	            case Events.VIEW_AD2:
	                break;
	            //统计分析
	            case Events.VIEW_ANALYSIS:
	                break;
	            //行情
	            case Events.VIEW_QUOTATION:
	                break;
	            //登录
	            case Events.MORE:
	                break;
	            default:
	                break;
	        }
	    });
	};
	
	function _ajax(url, type, param){
		this.type = type || 'GET';
		return $.ajax({
			url: url,
			type: this.type,
			contentType: 'application/json',
			dataType: 'json',
			data: JSON.stringify(param)
		});
	};
	
	function _getListOnlyList(){  
		var noticeContentlist=''; 
		var url = global.context + '/web/notice/listOnlyList'; 
		_ajax(url).done(function(response){  
			if (response != null){  
				noticeContentlist='';
				noticeContent='';
				$('#listOnlyList').html('');  
				for (var i = 0; i<response.aaData.length; i++){  
					var message = $('#temp_notice_comtent').html(response.aaData[i].content).text(); 
					noticeContentlist+="<div class='modal fade modal-micro modal-micro-listOnly' tabindex='-1' role='dialog' aria-labelledby='modal-title' aria-hidden='true'>";
					noticeContentlist+="<div class='modal-dialog'>"; 
					noticeContentlist+="<div class='modal-content' id='modal-content'>";
					noticeContentlist+="<div class='modal-header'>";
					noticeContentlist+="<button type='button' class='close' data-dismiss='modal' aria-hidden='true'>&times;</button>";
					noticeContentlist+="<h4 class='modal-title' id='modal-title'>通知</h4>";
					noticeContentlist+="</div>";
					noticeContentlist+="<div class='modal-body' id='modal-body'>";
					noticeContentlist+="<h3 class='text-center'>"+response.aaData[i].title+"</h3>";
					noticeContentlist+="<div>"+message+"</div>"; 
					noticeContentlist+="</div>";
					noticeContentlist+="</div>";
					noticeContentlist+="</div>"; 
					noticeContentlist+=" </div>";
					noticeContentlist+="</div>";    
					$('#temp_notice_comtent').html(''); 
			 	} 
			 	
				if(response.aaData.length<1){
				   $('#listOnlyList').html(''); 
				}else {
				   $('#listOnlyList').html(noticeContentlist);    
				} 
			}  
		//异步初始化化model后
		showModal($(".modal-micro")); 
	  }); 
	} 
	//重置控制面板的大小
	var _resizeDashboard = function _resizeDashboard()
	{
		var containerWidth=$(window).width();
		var containerHeight=$(window).height()-$("header.navbar").height()-10;
		var dashboardWidth=containerWidth-40;
		var dashboardHeight=dashboardWidth*0.52;
		if(containerHeight-dashboardHeight<20){
			dashboardHeight=containerHeight;
			dashboardWidth=dashboardHeight/0.52;
		}
	    
	    if(containerWidth<=460){
	    	$(".container-dashboard").css({
	    		height:"auto",
		    	width:dashboardWidth,
		    	marginTop:"20px",
		    	marginLeft:(-dashboardWidth/2)+"px",
		    	top:"0px",
		    	left:"50%",
		    	transition:'all ease-in-out .5s'
	    	});
	    	$(".container-dashboard").find(".dashboard-item").height(dashboardWidth*0.55);
	    }
	    else{
	    	$(".container-dashboard").css({
		    	height:dashboardHeight,
		    	width:dashboardWidth,
		    	marginTop:(-dashboardHeight/2+$("header.navbar").height()/2)+"px",
		    	marginLeft:(-dashboardWidth/2)+"px",
		    	top:"50%",
		    	left:"50%",
		    	transition:'all ease-in-out .5s'
		    });
	    	$(".container-dashboard").find(".dashboard-item").height("100%");
	    }
	};
	
	
	/*----------------------
	    初始化函数
	----------------------*/
	
	bindEvents();
	init();
});
