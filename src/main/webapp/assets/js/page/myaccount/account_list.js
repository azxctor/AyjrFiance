/*!
 *
 *  我的账户-账户明细
 *  创建人：陈恩明
 * 	创建日期：2014-05-16
 *
 */
require(['jquery',
        'global',
        'packet_manage',
        'module/util'
    ],
    function($,global,packet,util){
        'use strict';
        var renderGold = function(_,__,data){
            var flag;
            var color;
            var value = "";
            //P支出   R收取
            if(data.payRecvFlg=="P"){
                flag = "-"; 
                color = "amount-pay-out";
            }else{
                flag = "+";
                color = "amount-pay-in";
            }
            if(!isNaN(data.trxAmt))  value = flag + util.get_thousand(data.trxAmt);
            return  '<span class="'+color+' pull-right">'+value+'</span>';
        };
        var init = function(){
        	var options = {};
            options.tableId = '#packet-manage-table';
            var isPlatformUser = $('#is-platform-user').val();
            if (isPlatformUser == "true") {
            	options.sAjaxSource = global.context+"/web/myaccount/getplatformaccountdetails";
            	$("#export-excel-div").css('display','');
            } else {
            	options.sAjaxSource = global.context+"/web/myaccount/getaccountdetails";
            }
            options.aaSorting = [[0,"desc"]];
            options.functionList = { "renderGold":renderGold };
            packet.renderTable(options);
            packet.init();
        };
        init();
       
		//导出excel
		$('#export-excel').on('click',function(){  
			$("#use-types").val($('#use-type').val());
			$("#from-date").val($('#search-start-date').val());
			$("#to-date").val($('#search-end-date').val());   
			$("#form-export").submit();
			return false;
		});
		
		//导出excel
		$('#export-my-excel').on('click',function(){  
			$("#use-types-myacc").val($('#use-type').val());
			$("#from-date-myacc").val($('#search-start-date').val());
			$("#to-date-myacc").val($('#search-end-date').val());   
			$("#form-export-myacc").submit();
			return false;
		});
    });
