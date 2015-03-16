/*!
 * 	
 *  融资包管理 - 交易部-手工还款
 *  创建人：陈恩明
 * 	创建日期：2014-05-13
 *
 */

require(['jquery', 
        'packet_manage',
        'module/datatables',
        'global',
        'bootstrap',
        'jquery.popupwindow',
        'module/report_controller',
        'requirejs/domready!'],
    function($,packet,datatables,global){
        'use strict';
        var mainTable = null;
        //返回操作按钮
        var returnOperBtn = function(data,type,full){
        	var btnHtml = '<div class="btn-group">';
        	if(full.canManualPay){
        		btnHtml += packet.renderTableBtn("手工还款","btn-pay","fa fa-slack",full.id);
        	}
        	if(full.canViewPayTable){
            	btnHtml +=packet.renderTableBtn("查看还款表","btn-paylist","fa fa-calendar-o",full.id);
            }
            btnHtml+='</div>';
        	return btnHtml;
        };


        var init = function(){
        	 var options = {};
             options.tableId = '#packet-manage-table';
             options.sAjaxSource = global.context+"/web/financingpackage/manualpay/getpackagelist";
             options.aaSorting = [[0,"desc"]];
             options.functionList={"btn_oper":returnOperBtn,"btn_detail":packet.renderDetails,"btn_log":packet.returnLogBtn};
             mainTable = packet.renderTable(options);
             packet.init();
             packet.initLog();
        };  

        //绑定事件
        var bindEvent = function(){
            //转让撤单
           	$("#table-wrapper").on("click",".btn-pay",function(){
           		var packageId = $(this).attr("data-id");
                packet.getDetails(packageId,"manualpay");
                packet.showDetails();
            })//融资包详情
            .on("click",".detail-link",function(){
                var packageId = $(this).attr("data-id");
                packet.getDetails(packageId,"view");
                packet.showDetails();
            }).on("click","#hidden_span",function(){//刷新表
                    mainTable.invoke("fnStandingRedraw");
            })
            //查看还款表
            .on("click",".btn-paylist",function(){
            	var pkgId = $(this).attr("data-id");
            	window.reportController.detail("repayment.schedule",pkgId);
            });
        };
        init();
        bindEvent();
    });


