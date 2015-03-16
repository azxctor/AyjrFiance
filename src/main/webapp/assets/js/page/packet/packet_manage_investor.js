/*!
 * 	
 *  融资包管理 - 投资人
 *  创建人：陈恩明
 * 	创建日期：2014-05-12
 *
 */
require(['jquery',
        'module/datatables',
        'global',
        'module/util',
        'packet_manage',
        'module/view_pusher',
        'jquery.popupwindow',
        'module/report_controller'
       ],
    function($,datatables,global,util,packet,pusher){
        'use strict';
        var mainTable = null;
        var packet_global = {};
        var search_data = util.get_search_data("#search-wrapper");
        
        //返回操作按钮
        var returnOperBtn = function(data,type,full){
            var btnHtml = '<div class="btn-group-vertical">';
            btnHtml+= '<button class="btn btn-xs btn-getlist" data-id="'+full.pkgId+'" title="查看收益表"><a href="javascript:void(0);"  style="font-size:12px" title="查看收益表">收益表</a></button>';
            if(full.canTransferCancel)  btnHtml+='<button class="btn btn-xs btn-cancel" data-id="'+full.lotId+'" title="转让撤单\n您的报价'+util.get_thousand(full.transferPrice)+'元"><a href="javascript:void(0);" style="font-size: 12px;" title="转让撤单\n您的报价'+util.get_thousand(full.transferPrice)+'元">撤销</a></button>';

            if(full.canCreditorRightsTransfer)  btnHtml+='<button class="btn btn-xs btn-trans"><a href="javascript:void(0);" style="font-size:12px" data-packetNo='+full.pkgId+' data-packageName="'+full.packageName+'" data-lotId="'+full.lotId+'"  data-rate="'+full.rate+'" data-nextPayDay="'+full.nextPayDay+'"  data-lastPayDay="'+full.lastPayDay+'" data-packageName="'+full.packageName+'" data-lastAmount="'+ full.lastAmount+'" data-transferMinPrice="' + full.transferMinPrice + '" data-transferMaxPrice="' + full.transferMaxPrice + '" data-residualPrincipalAmt="'+full.residualPrincipalAmt+'" data-residualInterestAmt="'+full.residualInterestAmt+'"  data-sysRate="'+full.sysRate+'" data-costPrice="'+full.costPrice+'" title="债权转让">转让</a></button>';
            if(full.fromTransfer) btnHtml+= '<button class="btn btn-xs btn-getcontract" data-id="'+full.lotId+'" title="查看转让协议"><a href="javascript:void(0);"  style="font-size:12px" title="查看转让协议">转让协议</a></button>';
        	btnHtml+='</div>';
            return btnHtml;
        };
        
        //返回详情按钮
        var returnDetail = function(data,type,full){
        	if(full.aipFlag=="Y"){
    			return '<span class="label label-info flag">定</span><a class="detail-link" href="javascript:void(0);" data-id="'+full.pkgId+'" data-lot-id="'+full.lotId+'">'+full.packageName+'</a>';
    		}else{
    			return '<a class="detail-link" href="javascript:void(0);" data-id="'+full.pkgId+'" data-lot-id="'+full.lotId+'">'+full.packageName+'</a>';
    		}
        };
        
        function _fnflag(data,type,full){
    		var url = global.context+"/web/market/financing/detail/" + full.id;
    		if(full.aipFlag==true){
    			//<span><i class="fa fa-info-circle flag"></i>
    			return '<a href='+url+'><span class="label label-info flag">定</span>'+full.packageName+'</a>';
    		}else{
    			return '<a href='+url+'>'+full.packageName+'</a>';
    		}
    	};

        //返回下一收益额
        var returnNextPayAmount = function(_,__,data){
            var hoverMsgHtml='';
            if(data.deductFlag){
            	hoverMsgHtml = '<span class="hover-msg pull-right" data-toggle="tooltip" data-original-title="已扣'+util.get_percent(data.paymentRate)+'费用">'+util.get_thousand(data.nextPayAmount)+'</span>';
            }else{
            	hoverMsgHtml = '<span class="pull-right">'+util.get_thousand(data.nextPayAmount)+'</span>';
            }
            return hoverMsgHtml;
        };

        var init = function(){
            var options = {};
            options.tableId = '#packet-manage-table';
            options.sAjaxSource = global.context+"/web/financingpackage/investor/search/getpackagelist";
            options.aaSorting = [[8,"desc"]];
            options.functionList={"btn_oper":returnOperBtn,"btn_detail":returnDetail,"returnNextPayAmount":returnNextPayAmount};
            options.rawOptions = {fnDrawCallback:function(){
    			draw_total();
    		}};
            mainTable = packet.renderTable(options);
            packet.init();
            packet.initHoverMsg();
            $("#form-trans").validate();
        };
        
        var draw_total = function(){
        	search_data = util.get_search_data("#search-wrapper");
        	$.ajax({
    			url:global.context+"/web/financingpackage/mycreditors/sum",
    			type:"post",
    			contentType:"application/json",
    			dataType:"json",
    			data:JSON.stringify(search_data)
    		}).done(function(resp){
    			$("#span-totalPrincipal").text(util.get_thousand(resp.totalPrincipal));
    			$("#span-totalRestAmt").text(util.get_thousand(resp.totalRestAmt));
    			$("#span-totalNextPayAmt").text(util.get_thousand(resp.totalNextPayAmt));
    		});
        }
        
        //渲染弹窗
        var renderModal = function(title,content,footBtn){
            var modal = $("#modal");
            var modalTitle = $("#modal-title");
            var modalBody = $("#modal #modal-body");
            var modalFooterWrapper = $("#modal #modal-footer-wrapper");
            modalTitle.empty().append(title);
            modalBody.empty().append(content);
            modalFooterWrapper.empty().append(footBtn);
            modal.modal();
        };

        //绑定事件
        var bindEvent = function() {

            //table按钮事件绑定
            //转让撤单
            $("#table-wrapper").on("click", ".btn-cancel", function () {
                var lotId = $(this).attr("data-id");
                var btnHtml = '<div class="modal-footer">\
                    <button class="btn btn-mf-primary" id="confirm-cancel"  data-id="' + lotId + '" type="button">确定</button>\
                    <button class="btn btn-mf-default"class="btn btn-default" type="button" data-dismiss="modal">取消</button>\
               			</div>';
                renderModal("确认撤单", "是否放弃债权转让？", btnHtml);
            })
                //查看收益表
                .on("click", ".btn-getlist", function () {
                	var pkgId = $(this).attr("data-id");
                	window.reportController.detail("income.detail",pkgId);
                })
                //债权转让协议
                .on("click", ".btn-getcontract", function () {
                	var lotId = $(this).attr("data-id");
                	$.popupWindow(global.context + "/web/report/transfercontract/buyer/" + lotId,{"width":"1224","height":"700"});
                })
                .on("click", ".btn-trans", function () {
                    var me = $(this).find('a');
                    var packageName = $(me).attr("data-packageName");
                    var costPrice = $(me).attr("data-costPrice");
                    var nextPayDay = $(me).attr("data-nextPayDay");
                    var lastPayDay = $(me).attr("data-lastPayDay");
                    var lastAmount = $(me).attr("data-lastAmount");
                    var residualPrincipalAmt = $(me).attr("data-residualPrincipalAmt");
                    var residualInterestAmt = $(me).attr("data-residualInterestAmt");
                    var transferMinPrice = $(me).attr("data-transferMinPrice");
                    var transferMaxPrice = $(me).attr("data-transferMaxPrice");
                    var lotId = $(me).attr("data-lotId");
                    var rate = $(me).attr("data-rate");
                    var sysRate = $(me).attr("data-sysRate");
                    var packetNo = $(me).attr("data-packetNo");
                    
                    packet_global.packetNo = packetNo;
                    packet_global.packageName = packageName;
                    packet_global.lotId = lotId;
                    var transContent = '<div class="row">\
                        <div class="transfer-wrapper">\
                            <div class="row">\
                                <div class="col-xs-4">项目简称：</div>\
                                <div class="col-xs-8">' + packageName + '</div>\
                            </div>\
                            <div class="row">\
                                <div class="col-xs-4">年利率：</div>\
                                <div class="col-xs-8">' + util.get_trans_percent(rate) + '</div>\
                            </div>\
                            <div class="row">\
                                <div class="col-xs-4">下期还款日：</div>\
                                <div class="col-xs-8">' + nextPayDay + '</div>\
                            </div>\
                            <div class="row">\
	                            <div class="col-xs-4 text-imt">剩余本金：</div>\
	                            <div class="col-xs-8 text-imt">' + util.get_thousand(residualPrincipalAmt) + ' 元</div>\
	                        </div>\
	                        <div class="row">\
	                            <div class="col-xs-4 text-imt">剩余利息：</div>\
	                            <div class="col-xs-8 text-imt">' + util.get_thousand(residualInterestAmt) + ' 元</div>\
	                        </div>\
                            <div class="row">\
                                <div class="col-xs-4">剩余本息合计：</div>\
                                <div class="col-xs-8">' + util.get_thousand(lastAmount) + ' 元</div>\
                            </div>\
                            <div class="row">\
	                            <div class="col-xs-4 text-imt">债权到期日：</div>\
	                            <div class="col-xs-8 text-imt">' + lastPayDay + '</div>\
	                        </div>\
                            <div class="row">\
                                <div class="col-xs-4 text-imt" style="line-height: 34px;">报价：</div>\
                                <div class="col-xs-5 text-imt">\
                                	<input type="hidden" name="lotId" value="' + lotId + '"/>\
                                	    <div class="input-group">\
                        <input class="form-control" id="transfer-input"  name="transPrice" data-validate="{required:true,fixednumber:2,max:'+transferMaxPrice+',min:'+transferMinPrice+'}" data-hidden-validate="true" maxlength="10"  type="text"/> <span class="input-group-addon">元 </span>\
                    </div>\
                          </div>\
                                </div>\
                                <div class="row">\
                                    <div class="col-xs-8 col-xs-offset-4">\
                                      	<p  class="text-danger text-imt" id="KSMONEY" style="display:none;">转让盈亏：<span id="modal-feeMoney"></span><i class="fa fa-question-circle" title="转让盈亏 = 报价 - 成本价"></i></p>\
                                      	<p class="">成本价&nbsp;<i class="fa fa-question-circle" title="债权转让成本价 = 买入价 - 已还本息 +  转让手续费"></i>：<span id="modal-costPrice" data-costPrice="'+ costPrice +'">' + util.get_thousand(costPrice) + '</span> 元</p>\
                                		<p class="">报价上限：' + util.get_thousand(transferMaxPrice) + ' 元</p>\
                                		<p class="">报价下限：' + util.get_thousand(transferMinPrice) + ' 元</p>\
                                		<p class="text-danger text-imt">转让手续费：<span id="modal-fee">0.00</span> 元</p>\
                                	</div>\
                                </div>\
                                </div>\
                            </div>';
                    var btnHtml = '<div class="modal-footer">\
                    <button class="btn btn-mf-primary" id="confirm-trans" data-id="' + lotId + '" type="button">确定</button>\
                    <button class="btn btn-mf-default"class="btn btn-default" type="button" data-dismiss="modal">取消</button>\
               			</div>';
                    
                    jQuery.extend(jQuery.validator.messages, {
                        max: jQuery.validator.format("债权转让价格不能大于报价上限{0}."),
                        min: jQuery.validator.format("债权转让价格不能小于报价下限{0}.")
                    });
                    
                    $("#form-trans").validate({
                    	
                    });
                    renderModal("债权转让", transContent, btnHtml);
                    $(".fa-question-circle").tooltip();
                    
                    $("#transfer-input").on("keyup",function(){
                    	var f_val= parseFloat($(this).val());
                    	if (isNaN(f_val)) {
                    		f_val=0;
                    	} 
                    	var fee = util.get_thousand(sysRate * f_val);
                    	$("#modal-fee").text(fee);
                    	var feeMoney=parseFloat($("#modal-costPrice").attr("data-costPrice")) + sysRate * f_val;
                    	var costPrice = util.get_thousand(feeMoney);
                    	$("#modal-costPrice").text(costPrice);
                    	
                    	/**加入转让亏盈**/
                    	$("#KSMONEY").css('display','');
	                	var finayMoney=f_val-feeMoney;
	                	if(finayMoney>0){
	                		 $("#modal-feeMoney").text("盈"+util.get_thousand(f_val-feeMoney)+"元");
	                	}else{ 
	                		 $("#modal-feeMoney").text("亏"+util.get_thousand(feeMoney-f_val)+"元"); 
	                	}
                    	 
                    	
                    });
                });

            //获取融资包详情
            var getFinancingPackageDetailsForInvestor = function (subsId, lotId) {
                pusher.push({
                    url:global.context + "/web/financingpackage/investor/details/" + subsId,
                    type : 'GET',
                    element:"#main-wrapper",
                    title:"融资详情",
                    onHide:function(){
                        $("#supply-user-modal").remove();
                    },
                    data:{	
                    	lotId : lotId
                    }
                });
            };

            //modal按钮事件绑定
            $("body").on("click", "#confirm-cancel", function () {
                var lotId = $(this).attr("data-id");
                $.ajax({
                    url: global.context + "/web/financingpackage/investor/transfer/cancel/" + lotId,
                    type: 'POST',
                    contentType: 'application/json;charset=utf-8',
                    async: false,
                    error: function () {
                    },
                    success: function (response) {
                    }
                }).done(function (response) {
                    var resp = $.parseJSON(response);
                    if (resp.code == "ACK") {
                        mainTable.invoke("fnDraw");
                        packet.closeModal();
                    }
                });
            }).on("click", "#confirm-trans", function () {
                if( !$("#form-trans").valid()) return false;
                
                $("#confirm-packet-id").text(packet_global.packetNo);
                $("#confirm-packet-name").text(packet_global.packageName);
                $("#confirm-packet-price").text(util.get_thousand($("#transfer-input").val()));
                $("#confirm-packet-fee").text($("#modal-fee").text());
                $("#confirm-packet-money").text($("#modal-feeMoney").text()); 
                $("#modal").modal("hide");
                $("#confirm-modal").modal();
                
              /*  var me = this;
                var lotId = $(me).attr("data-id");
                var url = global.context + "/web/financingpackage/investor/transfer/save/" + lotId;
                util.ajax_submit("#form-trans", {
                    action: url
                }).done(function (response) {
                    if (response.code == "ACK") {
                        mainTable.invoke("fnDraw");
                        packet.closeModal();
                    }
                });*/

            }).on("click", ".detail-link", function () {
                var subsId = $(this).attr("data-id");
                var lotId = $(this).attr("data-lot-id");
                getFinancingPackageDetailsForInvestor(subsId,lotId);
                packet.showDetails();
            });
            
            $("#sell-packet-confirm").on("click",function(){
                var lotId = $("#confirm-trans").attr("data-id");
                var url = global.context + "/web/financingpackage/investor/transfer/save/" + lotId;
                util.ajax_submit("#form-trans", {
                    action: url
                }).done(function (response) {
                    if (response.code == "ACK") {
                        mainTable.invoke("fnDraw");
                        packet.closeModal();
                    }
                    if (response.code == "VALIDATION_ERROR") {
                    	packet.closeModal();
                    	$("#modal").modal("show");
                    }
                });
            });
            
    /*        $("#transfer-role-tip").popover({
            	content:'<div class="pull-left" style="margin-right: 80px;">'+
                    '<p>债权转让规则：</p>'+
    				'<ul>'+
    					'<li>债权转让仅限当日有效，当天未成交的债权，在休市后将自动撤消 </li>'+
    					'<li>最高转让价格为融资项目的剩余本息；最低转让价格为融资项目剩余本金的70% </li>'+
    					'<li>债权转让成交后将按照成交价格收取2‰手续费</li>'+
    					'<li>融资项目累计持有人数不超200人</li>'+
                    '</ul>'+
               '</div>'+
    		    '<div class="pull-left">'+
                    '<p>不可进行债权转让的融资项目：</p>'+
    				'<ul>'+
    					'<li>当天到期融资项目 </li>'+
    					'<li>应还款当天的融资项目</li>'+
    					'<li>债权持有期限不满5个交易日（含5日）的融资项目</li>'+
    					'<li>融资期限小于（包含）15天的融资项目 </li>'+
    					'<li>逾期中的融资项目 </li>'+
    					'<li>安益金融认为有必要停牌的情况</li>'+
                    '</ul>'+
               '</div>',
               html: true,
               trigger:"click",
               placement:"bottom",
               container:'.right-blk-content'
            })*/
            
            $("#transfer-role-tip").hover(function(){
            	$("#transfer-role-area").fadeIn();
            },function(){
            	$("#transfer-role-area").fadeOut();
            });
        };
        init();
        bindEvent();
    });


