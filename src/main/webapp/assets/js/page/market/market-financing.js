require(['jquery',
        'global',
        'module/util',
        'module/datatables',
        'module/sock_helper',
        'masonry',
        'requirejs/text!template/square_view.templ',
        'underscore',
        'jquery-bridget/jquery.bridget',
        'module/ajax',
        'bootstrap',
        'bootstrap-datepicker',
        'bootstrap-datepicker.zh-CN',
        'ickeck',
        'sockjs',
        'stomp',
        'bootstrapx-popoverx',
        'plugins',
        'requirejs/domready!'],
    function($,global,util,datatables,Sock_helper,Masonry,template,_){

		// 定义面值金额
		var _unitFaceValue = 1000;
	
		var market_flag = $("#is-market-close").val();
        $('input').iCheck({
            checkboxClass: 'icheckbox_minimal-orange',
            increaseArea: '20%' // optional
        });

        $("#search-start-time").datepicker({
            format: "yyyy-mm-dd",
            language: "zh-CN",
            autoclose: true
        });
        $("#search-end-time").datepicker({
            format: "yyyy-mm-dd",
            language: "zh-CN",
            autoclose: true
        });
        $("#prerelease").iCheck("check");
        $("#hasexpired").iCheck("uncheck");
        $("#all").iCheck("uncheck");

        function _fndetailButton(data,type,full){
            var hide = $('.search-form').attr('data-hide-sub');
            var flag = $('.search-form').attr('data-id');
            var html = '';
            if(hide != "true"){
                if(flag=="true" || full.status.code != "S" || full.progress == 1){
                    html +='<a class="btn btn-default purchase disabled" href="javascript:void(0);" data-id="'+full.id+'" data-content="loading...">立即申购</a>';
                }else{
                    html +='<a class="btn btn-default purchase" href="javascript:void(0);" data-id="'+full.id+'" data-content="loading...">立即申购</a>';
                }
            }
            return html;
        };
        function _fndetailButton3(data,type,full){
            return '<span class="tdpay">'+full.packageQuotaStr+'</span>';
        };
        function _fnflag(data,type,full){
            var url = global.context+"/web/market/financing/detail/" + full.id;
            if(full.aipFlag==true){
                //<span><i class="fa fa-info-circle flag"></i>
                return '<a href='+url+' target="_blank"><span class="label label-info flag">定</span>'+full.packageName+'</a>';
            }else{
                return '<a href='+url+' target="_blank">'+full.packageName+'</a>';
            }
        };

        function _fnrenderButton2(data,type,full){
            var progress = full.progress*1000;
            
            if(progress%10 == 0){
                progress = (progress/10).toFixed(0);
            }else{
                progress = (progress/10).toFixed(1);
            }
            return '<div>'+full.status.text+'</div>'+
                '<div class="progress pr" id='+full.id+'>'+
                '<div class="progress-bar" style="width: '+progress+'%;"></div>'+
                '</div>'+
                '<span class="prspan">'+progress+'%</span>';
        };

        function _fnbuyButton(nTd, sData, oData, iRow, iCol,align){
            var id = $(nTd).find(".purchase").attr("data-id");
            var minSubscribeAmount = '';
            var maxSubscribeAmount = '';
            var totalSubscribeAmountStr = '';
            var levelSubscribeAmountStr = '';
            var minSubscribeAmountStr = '';
            var maxSubscribeAmountStr = '';
            var remainingStartTime = '';
            var rateForRisk = '';
            var rate = 0;
            var term = 0;
            var termLength = 0;
            var payMethod = '';
            var subscribeStartTimeStr = oData.subscribeStartTimeStr;
            var item = '<button type="button" class="btn btn-mf-primary br0 purchased" data-id='+id+'>申购</button></div>';
            var item1 = '';
            var activeRisk = false;
            var displayLevel4 = false;
            $(nTd).find('.purchase').popoverx({
                ensure_visiable : true,
                placement : function(){
                    return align || 'left';
                },
                width: 250,
                global_close: 1,
                onShown:function(){
                    $.ajax({
                        url : global.context+'/web/market/financing/purchase/'+id,
                        type : 'GET',
                        contentType:'application/json;charset=utf-8',
                        async : false,
                        error:function(){
                        },
                        success : function(response) {
                            var respJson = $.parseJSON(response);
                            totalSubscribeAmountStr = respJson.totalSubscribeAmountStr;
                            accumulativeAmount = respJson.accumulativeAmountStr;
                            rateForRisk = respJson.rateStr;
                            levelSubscribeAmountStr = respJson.levelSubscribeAmountStr;
                            minSubscribeAmountStr = respJson.minSubscribeAmountStr;
                            maxSubscribeAmountStr = respJson.maxSubscribeAmountStr;
                            minSubscribeAmount = respJson.minSubscribeAmount;
                            maxSubscribeAmount = respJson.maxSubscribeAmount;
                            remainingStartTime = respJson.remainingStartTime;
                            subscribed = respJson.subscribed;
                            reason = respJson.reason;
                            rate = respJson.product.rate;
                            term = respJson.product.termToDays;
                            payMethod = respJson.product.payMethod.code;
                            termLength = respJson.product.termLength;
                            item = '<button type="button" class="btn btn-mf-primary br0 purchased" data-id='+id+'>申购</button></div>';
                            activeRisk = respJson.activeRisk;
                            displayLevel4 = respJson.displayLevel4;
                        }
                    });

                    if(remainingStartTime>0){
                        item1 = '<input type="text" class="br0" id="total" placeholder="申购未开始" disabled/>';
                        item = '<button type="button" class="btn btn-mf-primary br0 purchased" disabled  data-id='+id+'>'+subscribeStartTimeStr+'后可申购</button></div>';
                    }else if(subscribed == false){
                        item1 = '<h5 class="danger">'+reason+'</h5>';
                        var investorFlag = $('.search-form').attr('data-investor-flag');
                        if(investorFlag == "false"){
                            item1 = item1 + '<span>点击<a href="'+ global.context+ '/web/members/investorinfo">这里</a>申请成为投资会员</span>';
                        }
                        item = '';
                    }else if(maxSubscribeAmount<=0){
                    	//TODO 不确定此判断是否有意义,如果需要显示 已满额 是否可以改成 subscribed = false形式.
                        item1 = '<input type="text" class="br0" id="total" placeholder="已满额" disabled/>';
                        item = '<button type="button" class="btn btn-mf-primary br0 purchased" disabled  data-id='+id+'>已满额</button></div>';
                    }else{
                        item1 = '<div>'+
                            '<label>申购金额</label><span class="notice"></span>'+
                            '<input type="text" class="br0" id="total" placeholder="请输入'+_unitFaceValue+'的整数倍"/>'+
                            '</div>';
                        var item2 = '';
                        if (activeRisk) {
                        	if (displayLevel4) {
                        		item2 =  '<div>'+
                                //  '<label class="mg-b0">同一融资项目级别项目累计申购额 <i class="fa fa-question-circle accumulativeAmount-tip" title="截至上一交易日休市，您持有该风险类别项目的累计金额为'+accumulativeAmount+'元，为了更好地控制风险，建议您分散投资，持有该类风险项目的投资额不超过总资产的'+ rateForRisk + '"></i></label>'+
                                '<label class="mg-b0">您持有IV级项目的累计金额 </label>'+
                                '<div id="accumulativeAmount" class="mg-b5">'+accumulativeAmount+'元</div>'+
                                '</div>';
                        	}
						}
                        item1 = item1 +
                            '<div>'+
                            '<label  class="mg-b0">申购上下限区间</label>'+
                            '<div id="maxSubscribeAmount" class="mg-b5">[' + minSubscribeAmountStr + '，'+ maxSubscribeAmountStr + ']元</div>' +
                            '</div>'+
                            '<div>'+
                            '<label>您申购该项目的累计金额</label>'+
                            '<span id="totalSubscribeAmountStr" class="ml10">'+totalSubscribeAmountStr+'元</span>'+
                            '</div>'+ item2 +
                            '<div>' +
                            '<label>预计收益</label>'+
                            '<span id="profit" class="ml10">0.00元</span>'+
                            '</div>';
                    }

                    var html = '<div>'+
                        '<div>'+item1+
                        '</div>'+
                        '<div>'+item;


                    this.$tip.find('.popover-content').html(html);
                    
                    this.resetPosition();
                    
                    $(".accumulativeAmount-tip").data("tooltip",null);
                    $(".accumulativeAmount-tip").tooltip();
                    
                    $(".purchased").on("click",function(){
                        var id = $(this).attr('data-id');
                        if(isNaN($('#total').val())){
                            $('.notice').text("输入金额有误");
                        }else if(($('#total').val() >= minSubscribeAmount) && ($('#total').val()<= maxSubscribeAmount)){
                            if($('#total').val() % _unitFaceValue != 0){
                                $('.notice').text("申购额必须是"+_unitFaceValue+"的倍数");
                            }else{
                                buyitem(id,$('#total').val());
                            };
                        }else if($('#total').val() > maxSubscribeAmount){
                            $('.notice').text("申购额超过上限");
                        }else{
                            $('.notice').text("申购额未达到下限");
                        };
                    });

                    this.$tip.find('#total').keydown(function(){
                        var total = 0;
                        if(payMethod && payMethod == '04'){
                            var b = rate / 12;
                            var sum = Math.pow((1 + b), termLength);
                            var monthly = $('#total').val() * b * sum / (sum - 1);
                            total = monthly * termLength - $('#total').val();
                        }else{
                            total = $('#total').val()*rate*term/360;
                        }
                        $('#profit').text((Math.floor(total*100)/100).toFixed(2) + "元");
                    });
                    this.$tip.find('#total').keyup(function(e){

                        var total = 0;
                        if(payMethod && payMethod == '04'){
                            var b = rate / 12;
                            var sum = Math.pow((1 + b), termLength);
                            var monthly = $('#total').val() * b * sum / (sum - 1);
                            total = monthly * termLength - $('#total').val();
                        }else{
                            total = $('#total').val()*rate*term/360;
                        }
                        $('#profit').text(util.get_thousand((Math.floor(total*100)/100)) + "元");
                        if(e.keyCode=="13") $(e.target).parents(".popover-content").find(".purchased").click();
                    });
                }
            });

        };
        
        var render_my_thumb = function (code) {
       	    var th_map = {"A":4,"B":3,"C":2,"D":1};
       	    var num = th_map[code];
       	  	var thumb_html = "";
       	  	for(var i=0;i<num;i++){
       	  		thumb_html += "<i class='fa fa-thumbs-o-up'></i>";
       	  	} 
       	  	return 	thumb_html;	
       	  }
        
        function _fnrenderButton4(data,type,full){
        	var code = full.product.riskLevel.code;
        	var text = full.product.riskLevel.text;
        //	var risk_level = text.substr(0,1)=="高"?"险":text.substr(0,1);
        	var msg_map = {"A":"A级项目：提供货币类资产质押，本金覆盖率大于100%，违约可能性极低。",
        				   "B":"B级项目：提供有形或无形资产抵质押（变现渠道畅通），本金覆盖率不低于100%，违约可能性较低。",
        				   "C":"C级项目：提供有形或无形资产抵质押（有变现渠道）；或提供合格保证人担保，本金覆盖率不到100%，可能会违约。",
        				   "D":"D级项目：无有形或无形资产抵质押，无保证机构担保，能提供非保证机构担保，违约的可能性较高。"
        				  };
        	var html = '<a data-toggle="tooltip" class="creditlevel ' + code + '" title="' + msg_map[code] + '">' + code + '</a>';
       //     return '<a class="creditlevel ' + code + '" title="' + text + '项目">' + risk_level + '</a>';
        	return html;
        }
        
  //--------------------
        function _fnrenderButton5(data,type,full){
        	var code = full.product.riskLevel.code;
        	
        	var th_map = {"A":4,"B":3,"C":2,"D":1};
       	    var num = th_map[code];
       	  	var thumb_htm = "";
       	  	var thumb_html2 = "";
       	  	for(var i=0;i<num;i++){
       	  		thumb_htm += "<i class='fa fa-heart' style='color:#EA6B24'></i>&nbsp;";
       	  	}
       	  	for(var i=0;i<5-num;i++){
       	  		thumb_html2 += "<i class='fa fa-heart-o' style='border-color:#EAEAEA'></i>&nbsp;";
       	  	}
       	  	
       	  	var html = thumb_htm + thumb_html2;
       	  	return html;
        }
  //--------------------

        var market_table = null;
        var markettable = function(){
            var options = {};
            //alert(global.context);
            options.tableId = '#market-table';
            options.sAjaxSource = global.context+"/web/market/financing/search";
            options.functionList = {"_fndetailButton":_fndetailButton,"_fnflag":_fnflag,"_fnrenderButton2":_fnrenderButton2,"_fnbuyButton":_fnbuyButton,"_fndetailButton3":_fndetailButton3,"_fnrenderButton4":_fnrenderButton5};
            options.rawOptions = {"fnDrawCallback" : _fnsubscribe};
            options.aaSorting = [[7,"desc"]];
            market_table = datatables.init(options);
            market_table.setParams(util.getSearchData('#search_area'));
            return market_table.create();
        }();

        $('#search_area').find('input').each(function(){
            if($(this).val()=="true"){
                $(this).iCheck('check');
            }

        });

        $('#search_area input').on("ifClicked",function(){
            if($(this).val()=="true"){
                $(this).val("false");
                $('#all').val("false");
                $('#all').iCheck("uncheck");
            }else{
                $(this).val("true");
            }
        });
        $('#all').on("ifClicked",function(){
            $('#search_area input').val("true");
            $('#search_area input').iCheck('check');
        });

        $('#search').on("click",function(){
            $.extend(dto,util.get_search_data("#search_area"));
            if(view_flag == 1){
                dto.iDisplayStart = 0;
                current_page = 1;
                draw_square_view();
                return;
            }
            market_table.setParams(util.getSearchData("#search_area"));
            market_table.invoke("fnDraw");
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
                    $('.popover').hide();
                    //markettable.fnDraw();
                }
            });
        };

        var connection = function() {
            stompClient.connected = true;
            _fnsubscribe();
        };

        var errorCallback = function(error) {
            console.log(error);
           var t = window.setTimeout(function(){
            		stompClient = sockHelper.init();
            	    sockHelper.connect();
            	    window.clearTimeout(t);
            }, 5000);
        };

        var socketSetting = {
            url:global.context + "/web/ws",
            debug:false,
            connection:connection,
            errorCallback:errorCallback
        };

        var sockHelper = new Sock_helper(socketSetting);
        var stompClient = sockHelper.init();
        sockHelper.connect();


        function _fnrenderCallback(frame) {
            var resp = JSON.parse(frame.body);
            var items = resp.data;
            var status_now = resp.status+"";
        	if(status_now != market_flag){
        		if(status_now == "false"){
        			$(".search-form").attr("data-id","false");
        			$("#market-status-modal").find(".modal-body").html('<div class="market-status-info status-open">现已开市,欢迎申购 !</div>');
        		}else{
        			$(".search-form").attr("data-id","true");
        			$("#market-status-modal").find(".modal-body").html('<div class="market-status-info status-close">现已闭市 !</div>');
        		}
        		$("#market-status-modal").modal();
        	}
        	market_flag = status_now;
            for(var i=0;i<items.length;i++){
                var progress = items[i].progress * 1000;
                if(progress%10 == 0){
                    progress = (progress/10).toFixed(0);
                }else{
                    progress = (progress/10).toFixed(1);
                }

                var id = items[i].id;
                if(view_flag == 0){
                    $('#'+id+'').parent().find('span').text(progress+"%");
                    $('#'+id+'').find('div').css("width",progress+"%");
                    $('#'+id+'').parent().find('div:first').text(items[i].status);
                    if(items[i].status == '申购中' && progress < 100){
                    	$('#'+id+'').parent().parent().find('.purchase').removeClass("disabled");
                    }else{
                    	$('#'+id+'').parent().parent().find('.purchase').addClass("disabled");
                    }
                }else{
                    $("."+id).find(".progress-num").text(progress+"%");
                    $("."+id).find(".progress-bar").css("width",progress+"%");
                    $('.'+id).find('.progress-state').html(items[i].status+" &nbsp");
                    if(items[i].status == '申购中' && progress < 100){
                    	$('.'+id).find('.purchase').removeClass("disabled");
                    }else{
                    	$('.'+id).find('.purchase').addClass("disabled");
                    }
                }
            };
        }
        function _fnsubscribe(oSettings){
            if(oSettings){
                dto.asSortDir = [market_table.aaSorting[0][1]];
                dto.aiSortCol = [market_table.aaSorting[0][0]];
            }

            if (!stompClient.connected) return;
            if (stompClient.subscription) {
                stompClient.subscription.unsubscribe();
                stompClient.subscription = null;
            }
            
           // stompClient.subscription = stompClient.subscribe('/topic/market/financing/status', _fnMarketStatusCallback);
            
            var ids = [];
            if(view_flag == 0){
                $("#market-table tbody tr td:first-child:not('.dataTables_empty')").each(function(){
                    ids.push(this.innerHTML);
                });
            }else{
                $("#container").find(".item").each(function(){
                    ids.push($(this).attr("data-id"));
                })
            }

            if (ids.length == 0) {
                return;
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
                    if (topic != null) {
                        stompClient.subscription = stompClient.subscribe('/topic/market/financing/' + topic, _fnrenderCallback);
                    }
                }
            });
        };
        
        $('#market-status-modal').on('hidden.bs.modal', function (e) {
        	window.location.reload();
        });
        
        //九宫格页面

        // make Masonry a jQuery plugin
        $.bridget( 'masonry', Masonry );
        var $container = $('#container');
        var dto = {};
        var view_flag = 0;
        var iDisplayLength = 9;
        var current_page = 1;
        var total_page = null;
        var init = function(){
            dto.hasexpired = false;
            dto.prerelease = true;
            dto.topurchase = true;
            dto.iDisplayLength = iDisplayLength;
            dto.iDisplayStart = 0;
            dto.sEcho = 0;
            dto.iColumns = 10;
            dto.sColumns = "";
            dto.iSortingCols = 1;
            dto.asSortDir = [ "desc"];
            dto.aiSortCol = [7],
                dto.abSortable = [
                    true,
                    false,
                    true,
                    false,
                    true,
                    true,
                    true,
                    true,
                    false,
                    false
                ];
            dto.amDataProp = [
                "id",
                1,
                "product.riskLevel.text",
                "product.warrantyType.text",
                "product.ratePercentage",
                "packageQuotaStr",
                "product.term",
                "subscribeStartTimeStr",
                8,
                9
            ];


            $("#change-view-area").find("span").on("click",function(){
                $container.html("");
                view_flag = 0;
                var $this = $(this);
                $this.siblings().removeClass("view-selected").end().addClass("view-selected");
                $(".info-area").hide();
                var tar = $this.attr("data-target-area");
                if(tar == "#list-table-area"){
                    market_table.setParams(util.getSearchData("#search_area"));
                    market_table.invoke("fnDraw");
                }
                $(tar).show(function(){
                    if(tar == "#square-view"){
                        view_flag = 1;
                        dto.iDisplayStart = 0;
                        current_page = 1;
                        draw_square_view();
                    }
                });
            });

            $("#square-view").on("click",".paginate_button",function(){
                var $this = $(this);
                if(total_page == 0){
                    return;
                }
                if($this.hasClass("first")){
                    current_page = 1;
                }else if($this.hasClass("previous")){
                    current_page = (current_page-1)>1?(current_page-1):1;
                }else if($this.hasClass("next")){
                    current_page = (current_page+1)>total_page?total_page: (current_page+1);
                }else if($this.hasClass("last")){
                    current_page = total_page;
                }else{
                    current_page = parseInt($this.text());
                }
                dto.iDisplayStart = (current_page-1)*iDisplayLength;
                draw_square_view();
            });

            //	draw_square_view();
        };

        var draw_square_view = function(){
            $.ajax({
                url:global.context+"/web/market/financing/search",
                type: "post",
                contentType: "application/json",
                dataType: "json",
                data:JSON.stringify(dto)
            }).done(function(resp){
                var url = global.context+"/web/market/financing/detail/";
                var dtos = resp.aaData;
                var hide = $('.search-form').attr('data-hide-sub');
                var flag = $('.search-form').attr('data-id');
                dtos.hide = hide;
                dtos.flag = flag;
                dtos.iTotalRecords = resp.iTotalRecords;
                dtos.url = url;
                var items = _.template(template,{dtos:dtos});
                $container.html(items);
                $container.find(".item").each(function(){
                    _fnbuyButton(this, null, dtos, null, null,"top");
                });
                $container.data("masonry","");
                // initialize
                $container.masonry({
                    // columnWidth: 40,
                   /* isFitWidth: true,*/
                    gutter: 40,
                    itemSelector: '.item'
                });
                $('[data-toggle="tooltip"]').tooltip();
                var start_page,end_page;
                var page_index = [];
                total_page = Math.ceil(resp.iTotalRecords/iDisplayLength);
                start_page = current_page-2;
                end_page = current_page+2;
                if(start_page<1){
                    start_page = 1;
                };
                if(end_page>total_page){
                    end_page = total_page;
                }
                while ((end_page - start_page) < 4 &&
                    (start_page != 1 || end_page != total_page)) {
                    if (start_page > 1){
                        start_page--;
                    } else if (end_page < total_page) {
                        end_page++;
                    }
                }
                for(var i=start_page;i<=end_page;i++){
                    page_index.push(i);
                }
                var pages = _.template($("#tpl").html(),{page_index:page_index,current_page:current_page});
                $("#page-area").html(pages);
                _fnsubscribe();

            });
        };


        init();
        $("#btn-square").click();
    });
