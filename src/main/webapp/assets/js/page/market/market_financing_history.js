/*!
 *
 *  历史行情
 *  创建人：陈恩明
 * 	创建日期：2014-08-19
 *
 */
require(['packet_manage',
        'global'],
    function(packet,global){
        'use strict';
        var mainTable;

        //初始化
        var init = function(){
            var options = {};
            options.tableId = '#packet-manage-table';
            options.sAjaxSource = global.context+"/web/market/financing/history/search";
            options.aaSorting = [[7,"desc"]];
            options.functionList={"_fnflag":_fnflag,"_fnrenderButton4":fnrenderButton4,"_fndetailButton3":_fndetailButton3};
            mainTable = packet.renderTableByPeriod(options);
            packet.init();
        };

        //绑定事件
        var bindEvent = function(){
            var $btn = $("#btn-search-history");
            $btn.click(function(){
                var period = $("#search-wrapper input[name=period]:checked").val();
                mainTable.setParams([{
                    name:"period",
                    value:period
                }]);
                mainTable.invoke("fnDraw");
            });
        };

        //融资包名称
        function _fnflag(data,type,full){
            var url = global.context+"/web/market/financing/detail/" + full.id;
            if(full.aipFlag==true){
                return '<a href='+url+' target="_blank"><span class="label label-info flag">定</span>'+full.packageName+'</a>';
            }else{
                return '<a href='+url+' target="_blank">'+full.packageName+'</a>';
            }
        }
        
        var render_my_thumb = function (code) {
    	    var th_map = {"A":4,"B":3,"C":2,"D":1};
    	    var num = th_map[code];
    	  	var thumb_html = "";
    	  	for(var i=0;i<num;i++){
    	  		thumb_html += "<i class='fa fa-thumbs-o-up'></i>";
    	  	} 
    	  	return 	thumb_html;	
        }
        
	
        
        //融资项目级别
/*        function _fnrenderButton4(data,type,full){
        	var code = full.product.riskLevel.code;
        	var text = full.product.riskLevel.text;
        //	var risk_level = text.substr(0,1)=="高"?"险":text.substr(0,1);
        	
        	var msg_map = {"A":"A级项目：提供货币类资产质押，本金覆盖率大于100%，违约可能性极低。",
 				   "B":"B级项目：提供有形或无形资产抵质押（变现渠道畅通），本金覆盖率不低于100%，违约可能性较低。",
 				   "C":"C级项目：提供有形或无形资产抵质押（有变现渠道）；或提供合格保证人担保，本金覆盖率不到100%，可能会违约。",
 				   "D":"D级项目：无有形或无形资产抵质押，无保证机构担保，能提供非保证机构担保，违约的可能性较高。"
 				  };
        	var html = '<a class="creditlevel ' + code + '" title="' + msg_map[code] + '">' + text + '</a>';
       //     return '<a class="creditlevel ' + code + '" title="' + text + '项目">' + risk_level + '</a>';
        	return html;
        }
*/
        function fnrenderButton4(data,type,full){
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
        };
        //融资额
        function _fndetailButton3(data,type,full){
            return '<span class="tdpay">'+full.packageQuotaStr+'</span>';
        }


        init();
        bindEvent();
    });
