/*!
 * 	
 *  融资包管理 - 交易部（经理）-提前还款
 *  创建人：陈恩明
 * 	创建日期：2014-05-19
 *
 */

require(['jquery', 
        'packet_manage',
        'global',
        'jquery.popupwindow',
        'module/report_controller'],
    function($,packet,global){
        'use strict';
        var mainTable = null;
        //返回操作按钮
        var returnOperBtn = function(data,type,full){
            var btnHtml = '<div class="btn-group">';
        	if(full.canprepayment){
        		btnHtml += packet.renderTableBtn("提前还款","btn-advance","fa fa-cny",full.id);
        	}
        	if(full.canViewPayTable){
            	btnHtml += packet.renderTableBtn("查看还款表","btn-paylist","fa fa-calendar-o",full.id);
            }
            btnHtml+='</div>';
            return btnHtml;
        };

       
        var init = function(){
             packet.initHoverMsg("platform");
        	 var options = {};
             options.tableId = '#packet-manage-table';
             options.sAjaxSource = global.context+"/web/financingpackage/prepayment/getpackagelist";
             options.aaSorting = [[0,"desc"]];
             options.functionList={"hover_msg":packet.returnHoverMsg,"btn_oper":returnOperBtn,"btn_detail":packet.renderDetails,"btn_log":packet.returnLogBtn};
             mainTable = packet.renderTable(options);
             packet.init();
             packet.initLog();
        };  

        //绑定事件
        var bindEvent = function(){
            //转让撤单
           	$("#table-wrapper").on("click",".btn-advance",function(){
           		var packageId = $(this).attr("data-id");
                packet.getDetails(packageId,"prepayment");
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


