require(['jquery',
        'global',
        'module/util',
        'module/datatables',
        'module/sock_helper',
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
    function($,global,util,datatables,Sock_helper, mfBread){

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

		// 定义面值金额
		var _unitFaceValue = 1000;

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
                    scaleShowLabels : false
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
        var termLength = $('.rate').attr('data-term');
        var payMethod = $('.rate').attr('data-pay-method');
        var term = $('.term').attr('data-id');
        var maxSub = $('.sub_range').attr('data-max');
        var minSub = $('.sub_range').attr('data-min');
        $('#purchased').keydown(function(){
            var total = 0;
            if(payMethod && payMethod == '04'){
                var b = rate / 12;
                var sum = Math.pow((1 + b), termLength);
                var monthly = $('#purchased').val() * b * sum / (sum - 1);
                total = monthly * termLength - $('#purchased').val();
            }else{
                total = $('#purchased').val()*rate*term/360;
            }
            $('.total').text((Math.floor(total*100)/100).toFixed(2) + "元");
        });
        $('#purchased').keyup(function(e){
            var total = 0;
            if(payMethod && payMethod == '04'){
                var b = rate / 12;
                var sum = Math.pow((1 + b), termLength);
                var monthly = $('#purchased').val() * b * sum / (sum - 1);
                total = monthly * termLength - $('#purchased').val();
            }else{
                total = $('#purchased').val()*rate*term/360;
            }
            $('.total').text(util.get_thousand((Math.floor(total*100)/100)) + "元");
            if(e.keyCode=="13") {
            	if ($('#package-status').text() == '申购中') {
            		$("#purchase").click();
            	}
            }
        });


        $('#purchase').on('click',function(){
            var id = $('.form-mk').attr('data-id');
            var amount = $('#purchased').val();
            if(isNaN(amount)){
                $('.notice').text("输入金额有误");
            }else if(amount - maxSub > 0){
                $('.notice').text("申购额超过上限");
            }else if(amount - minSub < 0){
                $('.notice').text("申购额未达到下限");
            }else if(amount% _unitFaceValue != 0){
                $('.notice').text("申购额必须是"+_unitFaceValue+"的倍数");
            }else{
                buyitem(id,amount);
            };
        });

        $("#contract-accept").on('click',function(){
            $("#purchase").attr("disabled",!$(this).is(":checked"));
        });

        //查看合同模板
        $("#contract-link").on('click',function(){
            var contractId = $(this).attr("data-contract");
            window.reportController.detail("contract.template",contractId);
        });

        var buyitem = function(id,amount){
            var item = {};
            item.id = id;
            item.amount = amount;
            $.ajax({
                url : global.context+'/web/market/financing/purchase',
                type : 'post',
                contentType:'application/json;charset=utf-8',
                async : false,
                data: JSON.stringify(item),
                error:function(){
                },
                success : function(response) {
                    $.ajax({
                        url : global.context+'/web/market/financing/purchase/'+id,
                        type : 'GET',
                        contentType:'application/json;charset=utf-8',
                        async : false,
                        error:function(){
                        },
                        success : function(response) {
                        	if($.parseJSON(response).subscribed){
                        		totalSubscribeAmountStr = $.parseJSON(response).totalSubscribeAmountStr;
                                minSubscribeAmountStr = $.parseJSON(response).minSubscribeAmountStr;
                                maxSubscribeAmountStr = $.parseJSON(response).maxSubscribeAmountStr;
                                maxSub = $.parseJSON(response).maxSubscribeAmount;
                                minSub = $.parseJSON(response).minSubscribeAmount;
                                $('.sub_range').attr('data-max',maxSub);
                                $('.sub_range').attr('data-min',minSub);
                                $('#totalmoney').text(totalSubscribeAmountStr);
                                $('#maxmin').text("["+minSubscribeAmountStr+" , "+maxSubscribeAmountStr+"]");
                        	}else{
                        		$("#inner-content-wrapper").html('<div class="col-sm-12">'+
                                        '<h5 class="danger shadow-meg">'+$.parseJSON(response).reason+'</h5>'+
                                        '</div>');
                        	}
                            
                        }
                    });
                    $('#purchased').val('');
                    $('.total').text("0.00元");
                    $('.notice').text("");
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
        var remainingStartTime = $('.remainingStartTime').attr('data-id');
        var remainingTime = $('.remainingTime').attr('data-id');
        var supplyStartTime = $('.supplyStartTime').attr('data-id');
        var supplyEndTime = $('.supplyEndTime').attr('data-id');
        var progress = $('.progress-bar').attr('data-id');


        if(remainingStartTime>0){
            $('.remainingTime').text('于'+supplyStartTime+'开始');
            $('#purchase').addClass('disabled');
            $('#purchase').text('待申购');
        }
        if((remainingStartTime<0)&&(remainingTime>=0)){
            $('.remainingTime').text('于'+supplyEndTime+'结束');
        }
        if((remainingTime<=0)||(progress==100)){
            $('.remainingTime').text('已满额或已截止');
            $('#purchase').addClass('disabled');
            $('#purchase').text('已满额或已截止');
        }


        var errorCallback = function(error) {
            console.log(error);
            var t = window.setTimeout(function(){
        		stompClient = sockHelper.init();
        	    sockHelper.connect();
        	    window.clearTimeout(t);
            }, 5000);
        };

        var connection = function() {
            stompClient.connected = true;
            _fnsubscribe();
        };
        var socketSetting = {
            url:global.context + "/web/ws",
            debug:false,
            connection:connection,
            errorCallback:errorCallback
        };

        var sockHelper = new Sock_helper(socketSetting);
        if ($('#expired').val() == 'false') {
        	var stompClient = sockHelper.init();
        	sockHelper.connect();
        }

        function _fnrenderCallback(frame) {
            var resp = JSON.parse(frame.body);
            var items = resp.data;
            for(var i=0;i<items.length;i++){
                var progress = items[i].progress * 100;
//                console.log("progress = " + progress + " status = " + items[i].status);
                if(progress*10%10 == 0){
                    progress = progress.toFixed(0);
                }else{
                    progress = progress.toFixed(1);
                }
                $('.progresspan').text(progress+"%");
                $('.progress-bar').css("width",progress+"%");
                $('#package-status').text(items[i].status);
                if (items[i].status == '待申购') {
                	$('#purchase').addClass('disabled');
                    $('#purchase').text('待申购');
                } else if(items[i].status != '申购中' || progress >= 100) {
                    $('.remainingTime').text('已满额或已截止');
                    $('#purchase').addClass('disabled');
                    $('#purchase').text('已满额或已截止');
                } else {
                	if(items[i].status == '申购中' && progress < 100){
                    	$('#purchase').removeClass("disabled");
                    	$('#purchase').text("立即申购");
                    } else {
                    	$('#purchase').addClass("disabled");
                    }
                }
            };
        }
        function _fnsubscribe(){
            if (!stompClient.connected) return;
            var ids = [];
            ids.push($('#pkgId').val());
            if (stompClient.subscription) {
                stompClient.subscription.unsubscribe();
                stompClient.subscription = null;
            }
            $.ajax({
                url : global.context+'/web/market/financing/subscribe',
                type : 'post',
                contentType:'application/json;charset=utf-8',
                async : false,
                data: JSON.stringify(ids),
                error:function(){
                },
                success : function(response) {
                    var topic = $.parseJSON(response).data;
                    stompClient.subscription = stompClient.subscribe('/topic/market/financing/' + topic, _fnrenderCallback);
                }
            });
        };
        
        var risk_tip = {"A":"A级融资项目: <br>由信用达标的融资人发起，以具备充分流动性的货币类资产进行质押，包括现金保证金等，能覆盖借款本金的比例在100%以上的融资项目，产生违约的可能性极低。",
        				"B":"B级融资项目: <br>由信用达标的融资人发起，以有形或无形资产抵、质押，其价值可评估、易变现、并具有专业变现渠道及市场的风险保障措施，其覆盖借款本金的比例不低于100%的融资项目，预计发生违约的可能性较低。",
        				"C":"C级融资项目: <br>1. 由信用达标的融资人发起，以有形或无形资产抵、质押，其价值可评估、可变现、但变现渠道及市场接受程度一般，其覆盖借款本金的比例不足100%的融资项目；<br>2. 由信用达标的融资人发起，提供合格保证人的保证担保的融资项目；<br>3. 可能会产生违约的风险。",
        		        "D":"D级融资项目: <br>1. 由信用达标的融资人发起，不能提供有形或无形资产抵、质押，也不能提供专业保证机构担保的融资项目；<br>2. 能提供非专业保证机构（非安益金融产品服务商）担保的融资项目；<br>3. 产生违约的可能性较高。‍"
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
                    util.redirect(global.context + '/web/market/financing');
                });
            }, 500);
        };
        time_task();
    });
