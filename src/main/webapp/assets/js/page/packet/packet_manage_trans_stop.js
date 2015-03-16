/*!
 * 	
 *  融资包管理 - 交易部-终止申购
 *  创建人：陈恩明
 * 	创建日期：2014-05-13
 *
 */

require(['jquery', 
        'packet_manage',
        'module/datatables',
        'global',
        'module/util',
        'bootstrap',
        'requirejs/domready!'],
    function($,packet,datatables,global,util){
        'use strict';
        var mainTable = null;
        //返回操作按钮
        var returnOperBtn = function(data,type,full){
            var btnHtml = '<div class="btn-group">';
        	if(full.canStopFinancingPackage){
                btnHtml+= packet.renderTableBtn("终止申购","btn-stop","fa fa-stop",full.id);
        	}
            btnHtml+='</div>';
        	return btnHtml;
        };

        //初始化
        var init = function(){
             packet.initHoverMsg("platform");
        	 var options = {};
             options.tableId = '#packet-manage-table';
             options.sAjaxSource = global.context+"/web/financingpackage/stop/getpackagelist";
             options.aaSorting = [[0,"desc"]];
             options.functionList={"hover_msg":packet.returnHoverMsg,"btn_oper":returnOperBtn,"btn_detail":packet.renderDetails,"btn_log":packet.returnLogBtn};
             mainTable= packet.renderTable(options);
             packet.init();
             packet.initLog();
        };  

        //绑定事件
        var bindEvent = function(){

        	//table按钮事件绑定
           	$("#table-wrapper").on("click",".btn-stop",function(){
	           	 var packageId = $(this).attr("data-id");
	             packet.getDetails(packageId,"stop");
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


