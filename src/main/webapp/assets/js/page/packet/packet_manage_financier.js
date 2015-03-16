/*!
 * 	
 * 融资包管理 - 融资人/担保机构
 *  创建人：陈恩明
 * 	创建日期：2014-05-08
 *
 */
require(['packet_manage','global','module/util','jquery.popupwindow','module/report_controller'],
    function(packet,global,util){
        'use strict';
        //返回操作按钮
        var returnOperBtn = function(data,type,full){
            var btnHtml = '<div class="btn-group">';
            if(full.canStopPackage){
                btnHtml+=packet.renderTableBtn("终止申购","btn-stop","fa fa-pause",full.id,null,full.marketOpen);
            }
            if(full.canSignContract){
                btnHtml+=packet.renderTableBtnByFont("请在"+full.lastSignDate+"日24点前签约,逾期将视作自动撤单并产生违约金！","btn-sign","签",full.id,full.supplyAmount);
            }
            if(full.canCancelPackage){
            	if(full.status.code=="WN"){
            		btnHtml += '<button class="btn btn-xs btn-cancel" data-id="'+full.id+'" title="撤单"><a href="javascript:void(0);" title="撤单"><i class="fa fa-trash-o" ></i></a></button>';
            	}else{
            		btnHtml+=packet.renderTableBtn("撤单","btn-cancel","fa fa-trash-o",full.id,null,full.marketOpen);
            	}
            }
            if(full.canPrepayment){
                btnHtml+=packet.renderTableBtn("提前还款","btn-paynow","fa fa-cny",full.id);
            }
            if(full.canViewRepayTable){
            	btnHtml+=packet.renderTableBtn("查看还款表","btn-paylist","fa fa-calendar-o",full.id);
            }
            btnHtml+='</div>';
            return btnHtml;
        };
        //返回详情
        var returnDetail = function(data,type,full){
            var detailLink = '<a class="detail-link" href="javascript:void(0);" data-id="'+full.id+'">'+full.packageName+'</a>';
            return detailLink;
        };

        
        var mainTable = null;
        //初始化
        var init = function(){
            packet.initHoverMsg("financier");
            var options = {};
            options.tableId = '#packet-manage-table';
            options.sAjaxSource = global.context+"/web/financingpackage/financier/getpackagelist";
            options.aaSorting = [[0,"desc"]];
            options.functionList={"hover_msg":packet.returnHoverMsg,"btn_oper":returnOperBtn,"btn_log":packet.returnLogBtn,"btn_detail":returnDetail};
            mainTable = packet.renderTable(options);
            packet.init();
            packet.initLog();
        };



        //操作按钮
        var doOperate = function(url){
        	return $.ajax({
				url: url,
				type: 'POST',
				contentType: 'application/json;charset=utf-8',
				error: function(){
				},
				success: function(response){
					mainTable.invoke("fnDraw");
                    packet.closeModal();
				}
			});
        };

        //绑定事件
        var bindEvent = function(){
            //列表上按钮事件绑定
            //终止
            $("#table-wrapper").on("click",".btn-stop",function(){
            	var packageId = $(this).attr('data-id');
                var btnHtml = '<div class="modal-footer">\
                <button class="btn btn-mf-primary" id="confirm-stop" data-id="'+packageId+'" type="button">确定</button>\
                <button class="btn btn-mf-default"class="btn btn-default" type="button" data-dismiss="modal">取消</button>\
           			</div>';
                packet.renderModal("确认终止","是否确认终止当前申购？",btnHtml);
                //撤单
            }).on("click",".btn-cancel",function(){
            	var packageId = $(this).attr('data-id');
                var btnHtml = '<div class="modal-footer">\
                    <button class="btn btn-mf-primary" id="confirm-cancel" data-id="'+packageId+'" type="button">确定</button>\
                    <button class="btn btn-mf-default"class="btn btn-default" type="button" data-dismiss="modal">取消</button>\
               			</div>';
                packet.renderModal("确认撤单","是否确认撤销申购？点击确认系统将对融资包进行作废处理，并扣除相应融资服务合同保证金，清分给投资人和平台！",btnHtml);
            })
            //签约
            .on("click",".btn-sign",function(){
            	var packageId = $(this).attr('data-id');
            	var packageQuota = $(this).attr('data-packageQuota');
                var signContent = "是否确认按照实际申购额（"+util.get_thousand(packageQuota)+"元）签约";
                var btnHtml = '<div class="modal-footer">\
                <button class="btn btn-mf-primary" id="confirm-sign" data-id="'+packageId+'" type="button">签约</button>\
                <button class="btn btn-mf-default"class="btn btn-default" type="button" data-dismiss="modal">取消</button>\
                    </div>';
                packet.renderModal("签约",signContent,btnHtml);
            })
            //查看还款表
            .on("click",".btn-paylist",function(){
            	var pkgId = $(this).attr("data-id");
            	window.reportController.detail("repayment.schedule",pkgId);
            })
            //提前还款
            .on("click",".btn-paynow",function(){
            	var totalAmt = 0.0;
            	var totalPrincipaBalance=0.0;
            	var totalInterestAmt = 0.0;
            	var totalPenalty = 0.0;
            	var feeAmt = 0.00;
            	var packageId = $(this).attr('data-id');
            	$.ajax({
        			url: global.context + "/web/financingpackage/payment/"+packageId+"/getprepayments",
        			type: 'GET',
        			async:false,
        			contentType: 'application/json;charset=utf-8',
        			error: function(){
        			},
        			success: function(response){
        				var _response = $.parseJSON(response);
	        				totalAmt = _response.totalAmt;
	        				totalPrincipaBalance=_response.totalPrincipaBalance;
	        				totalInterestAmt=_response.totalInterestAmt;
	        				totalPenalty = _response.totalPenalty;
	        				feeAmt = _response.feeAmt;
        				}
            	});
                var signContent = '融资包 '+packageId+' 提前还款，'
                				 +"还款总额为"+ util.get_thousand(totalAmt)
                                 +"元，其中应还本金为"+util.get_thousand(totalPrincipaBalance)
                                 +"元，应另付当月利息为"+util.get_thousand(totalInterestAmt)
		                         +'元，应付当月费用为'+ util.get_thousand(feeAmt) 
                                 +"元，提前还款违约金为"+util.get_thousand(totalPenalty)
                                 +"元.<br/>点击【确定】系统将自动扣除还款额！";
                var btnHtml = '<div class="modal-footer">\
                <button class="btn btn-mf-primary" id="confirm-paynow" data-id="'+packageId+'" type="button">确定</button>\
                <button class="btn btn-mf-default"class="btn btn-default" type="button" data-dismiss="modal">取消</button>\
                    </div>';
                packet.renderModal("提前还款",signContent,btnHtml);
            })
            //融资包详情
            .on("click",".detail-link",function(){
                var packageId = $(this).attr("data-id");
                packet.getDetails(packageId,"view");
                packet.showDetails();
            });




            //弹窗按钮事件绑定
            $("body").on("click","#confirm-stop",function(){//确认终止
            	var packageId = $(this).attr("data-id");
            	doOperate(global.context+"/web/financingpackage/"+packageId+"/stop").done(function(response){
            		
            	});
            })
            //确认撤单
            .on("click","#confirm-cancel",function(){
            	var packageId = $(this).attr("data-id");
            	doOperate(global.context+"/web/financingpackage/"+packageId+"/withdrawals");
            })
            //确认签约
            .on("click","#confirm-sign",function(){
            	var packageId = $(this).attr("data-id");
            	doOperate(global.context+"/web/financingpackage/"+packageId+"/signed");
            })
            //确认提前还款
            .on("click","#confirm-paynow",function(){
            	var packageId = $(this).attr("data-id");
            	doOperate(global.context+"/web/financingpackage/"+packageId+"/prepayment");
            });
        };

        init();
        bindEvent();
    });


