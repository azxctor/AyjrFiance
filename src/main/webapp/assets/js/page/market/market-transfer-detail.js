require(['jquery',
         'global',
         'module/util',
         'module/datatables',
         'module/bread_crumb',
         'module/ajax',
         'bootstrap',
         'bootstrap-datepicker',
         'bootstrap-datepicker.zh-CN',
         'bootstrapx-popoverx',
         'ickeck',
         'sockjs',
         'stomp',
         'chartjs',
         'jquery.popupwindow',
         'module/report_controller',
         'requirejs/domready!'],
         function($,global,util,datatables, mfBread){
	
	var init = function(){
		var is_closed = $("#hidden-package-closed").val();
		var is_transfered = $("#hidden-package-transfered").val();
		if(is_transfered == "false" && is_closed == "false"){
			$("#purchase").attr("disabled",true);
		}
	};
	
	init();
	
	/*
	$(".right-blk-body").on("scroll", function(event){
		$(".radar").each(function(){
			var fold = $(window).height() + $(window).scrollTop();
			if( fold > $(this).offset().top){
				loadradar();
				$(".right-blk-body").off("scroll");
			}
		});
	});

	var fold = $(".right-blk-body").height();
	if( fold > $(".radar").offset().top){
		loadradar();
		$(".right-blk-body").off("scroll");
	};
	*/

	function loadradar(){
		var num = $('.radar').attr('data-id').split(',');
		var level = $('.radar').attr('data-level');
        if (num[0] == 'A') {
        	num[0] = 100;
        } else if (num[0] == 'B') {
        	num[0] = 75;
        } else if (num[0] == 'C') {
        	num[0] = 50;
        } else if (num[0] == 'D') {
        	num[0] = 25;
        }
        num[1] = num[1] == '' ? 0 : num[1];
        num[2] = num[2] == '' ? 0 : num[2];
        var radarChartData = {
            labels : ["融资项目级别："+level+" 级","融资会员信用积分："+num[1]+"分","担保机构信用积分："+num[2]+"分"],
            datasets : [
                {
                    fillColor : "rgba(220,220,220,0.5)",
                    strokeColor : "rgba(220,220,220,1)",
                    pointColor : "rgba(220,220,220,1)",
                    pointStrokeColor : "#fff",
                    data : [10,10,10]
                },
                {
                    fillColor : "rgba(221, 81, 27, 0.39)",
                    strokeColor : "rgba(221, 81, 27, 1)",
                    pointColor : "rgba(221, 81, 27, 1)",
                    pointStrokeColor : "#fff",
                    data : [10*num[0]/100,10*num[1]/100,10*num[2]/100]
                }
            ]

        }
		var steps = 5;
		var max = 10;
		if(document.getElementById("canvas")){
		var myRadar = new Chart(document.getElementById("canvas").getContext("2d")).Radar(
				radarChartData,
				{	
					scaleOverride: true,
					scaleSteps: steps,
					scaleStepWidth: Math.ceil(max / steps),
					scaleStartValue: 0,
					scaleShowLabels : false, 
				}
		);
		}	
	};

	$('.check-link').popoverx({
		ensure_visiable : true,
		placement : 'left',
		width: 220,
		global_close: 1,
		onShown:function(){
			var html =  '<div>'+$('#loan-purpose').text()+'</div>';
			this.$tip.find('.popover-content').html(html);
			this.resetPosition();
		}
	});
	
	
	var rate = $('.rate').attr('data-id');
	var term = $('.term').attr('data-id');
	$('#purchased').keydown(function(){
		var  total = $('#purchased').val()*rate/365*term;
		$('.total').text(total.toFixed(1));
	});
	$('#purchased').keyup(function(){
		var  total = $('#purchased').val()*rate/365*term;
		$('.total').text(total.toFixed(1));
	});
	
	
	$('#purchase').on('click',function(){
		$("#buy-confirm-id").text($("#hidden-package-id").val());
		$("#buy-confirm-name").text($("#hidden-package-name").val());
		$("#buy-confirm-price").text($("#trans-mrjg").text());
		$("#buy-confirm-fee").text($("#trans-sxf").text());
		$("#buy-confirm-modal").modal();
		//buyitem(id);
	});
	
	
	$("#buy-packet-confirm").on("click",function(){
		var id = $('.form-mk').attr('data-id');
		buyitem(id);
		$("#buy-confirm-modal").modal("hide");
	});
	
	$("#contract-accept").on('click',function(){
		$("#purchase").attr("disabled",!$(this).is(":checked"));
	});
	
	//查看合同模板
	$("#contract-link").on('click',function(){
		var contractId = $(this).attr("data-contract");
    	window.reportController.detail("contract.template",contractId);
	});
	
	var buyitem = function(id){
		var item = {};
		item.id = id;
		$.ajax({
			url : global.context+'/web/market/transfer/purchase',
			type : 'post',
			contentType:'application/json;charset=utf-8',
			dataType:'json',
			data: JSON.stringify(item),
			error:function(){
				$('#purchased').val('');
			},
			success : function(response) {
				$('#purchased').val('');
				if (response.code == 'ACK') {
					$('#purchase').attr("disabled",true);
					$('#contract-accept').attr("disabled",true);
				}
			}
		});
	};
	var loan = $('#loan-purpose').text();
	var length = loan.length;
	if(length>=80){
		loan = loan.substr(0,60);
		$('#loan-purpose').text(loan+"...");
		$('#loan').show();
	}
	
	   
    var risk_tip = {"A":"A级融资项目:<br>由信用达标的融资人发起，以具备充分流动性的货币类资产进行质押，包括现金保证金等，能覆盖借款本金的比例在100%以上的融资项目，产生违约的可能性极低。",
    				"B":"B级融资项目:<br>由信用达标的融资人发起，以有形或无形资产抵、质押，其价值可评估、易变现、并具有专业变现渠道及市场的风险保障措施，其覆盖借款本金的比例不低于100%的融资项目，预计发生违约的可能性较低。",
    				"C":"C级融资项目:<br>1. 由信用达标的融资人发起，以有形或无形资产抵、质押，其价值可评估、可变现、但变现渠道及市场接受程度一般，其覆盖借款本金的比例不足100%的融资项目；<br>2. 由信用达标的融资人发起，提供合格保证人的保证担保的融资项目；<br>3. 可能会产生违约的风险。",
    		        "D":"D级融资项目:<br>1. 由信用达标的融资人发起，不能提供有形或无形资产抵、质押，也不能提供专业保证机构担保的融资项目；<br>2. 能提供非专业保证机构（非安益金融产品服务商）担保的融资项目；<br>3. 产生违约的可能性较高。‍"
    				};
    var tip_con = risk_tip[$("#level-tip").attr("data-code")];
    $("#level-tip").popover({
    	html:true,
    	content:function(){
    	  return "<p class='level-tip-msg'>"+risk_tip[$(this).attr("data-code")]+"</p>";
    	},
    	placement:"top",
    	trigger:"hover"
    });
    
    $("#xqsy-tip").popover({
		html:true,
		placement:"top",
		trigger:"hover",
		content:" <p style=line-height:20px>1. 预期收益 = 剩余本金+剩余利息-报价-交易手续费-债权转让手续费<br>2. 交易手续费按收益的5%计算，2014年5月1日前签约的融资包不收取交易手续费<br>3. 预期收益不包含因融资人提前还款等因素产生的收益变动</p>"
	});
    
    $("#wrtr-credit-desc").popover({
    	html:true,
    	content:function(){
    	  return "<p class='wrdesc-tip-msg'>"+$(this).attr("data-wrdesc")+"</p>";
    	},
    	placement:"bottom",
    	trigger:"hover"
    })
    
    
	/* update the bread crumb*/
	var time_task = function(){
		setTimeout(function(){
			mfBread.push({
				label: $('.basic-info').attr('data-name')
			}, function(e){
				util.redirect(global.context + '/web/market/transfer');
			});
			
			if($("#hidden-package-closed").val() == "true"){
		    	$("#transfer-info-modal").modal();
		    }
		}, 500);
	};
	time_task();
});
