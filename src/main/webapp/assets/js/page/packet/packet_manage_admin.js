/*!
 * 	
 *  融资包管理 - 管理员-异常撤单
 *  创建人：陈恩明
 * 	创建日期：2014-05-13
 *
 */

require(['packet_manage','global'],
    function(packet,global){
        'use strict';
        var mainTable = null;
        //返回操作按钮
        var returnOperBtn = function(data,type,full){
        	var btnHtml = '<div class="btn-group">';
        	if(full.canWithDraw){
                btnHtml +=  packet.renderTableBtn("异常撤单","btn-cancel","fa fa-trash-o",full.id);
        	}
            btnHtml+='</div>';
        	return btnHtml;
        };
        //初始化
        var init = function(){
            packet.initHoverMsg("platform");
            var options = {};
            options.tableId = '#packet-manage-table';
            options.sAjaxSource = global.context+"/web/financingpackage/withdraw/getpackagelist";
            options.aaSorting = [[0,"desc"]];
            options.functionList={"btn_oper":returnOperBtn,"btn_log":packet.returnLogBtn,"btn_detail":packet.renderDetails,"hover_msg":packet.returnHoverMsg};
            mainTable = packet.renderTable(options);
            packet.init();
            packet.initLog();
        };
        //绑定事件
        var bindEvent = function(){
        	//table按钮事件绑定
            //异常撤单
           	$("#table-wrapper").on("click",".btn-cancel",function(){
           		var packageId = $(this).attr("data-id");
                packet.getDetails(packageId,"withdraw");
                packet.showDetails();
            })//融资包详情
            .on("click",".detail-link",function(){
                var packageId = $(this).attr("data-id");
                packet.getDetails(packageId,"view");
                packet.showDetails();
            }).on("click","#hidden_span",function(){//刷新表
                    mainTable.invoke("fnStandingRedraw");
            });
        };
        
       
        init();
        bindEvent();
    });


