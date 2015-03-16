/*!
 *
 *  历史行情
 *  创建人：陈恩明
 * 	创建日期：2014-08-19
 *
 */
require(['packet_manage',
        'global',
        'module/datatables'],
    function(packet,global){
        'use strict';
        var mainTable;
        var curPage = 1;
        var period = "ONEMONTH";
        var pageNum = 20;


        
        $.fn.dataTableExt.oApi.fnPagingInfo = function(oSettings){
            return {
                "iTotalPages": oSettings._iDisplayLength === -1 ?
                    0 : Math.ceil( oSettings.fnRecordsDisplay() / oSettings._iDisplayLength )
            };
        };    

        
        var screenTable = $(".big-screen-table").dataTable({
			"bFilter": false,
			"bLengthChange": false,
			"bInfo": false,
			//一页显示多少条数据
			"iDisplayLength":10,
			"sPaginationType": "full_numbers",
			"bAutoWidth":false,
			//初始排序条件
			"aaSorting":[[7,"desc"]],
			"oLanguage": {
			      "sSearch": "",
			      "oPaginate" : {
						"sFirst" : "首页",
						"sPrevious" : "前一页",
						"sNext" : "后一页",
						"sLast" : "尾页",
					},
				  "sInfoEmpty" : "谢谢您的关注，暂无您要查询的数据！",
				  "sZeroRecords" : "谢谢您的关注，暂无您要查询的数据！"
			}
        });
        
        $(".big-screen-table").removeClass("hide");
        //总页数
        var total_page = screenTable.fnPagingInfo().iTotalPages;
        //当前页数
        var counter = 0;
        
        //每隔5秒刷新翻页
        window.setInterval(function(){
        	if(counter>(total_page-1)){
        		counter = 0;
        	}
        	screenTable.fnPageChange(counter);
        	counter++;
        }, 5*1000);
        
        //每隔20分钟刷新页面
        window.setInterval(function(){
        	window.location.reload();
        },20*60*1000);
        
        //融资包名称
        function _fnflag(data,type,full){
            var url = global.context+"/web/market/financing/detail/" + full.id;
            if(full.aipFlag==true){
                return '<span class="label label-info flag">定</span>'+full.packageName+'';
            }else{
                return full.packageName;
            }
        }
        
        //融资项目级别
        function _fnrenderButton4(data,type,full){
        	var code = full.product.riskLevel.code;
        	var text = full.product.riskLevel.text;
        	var msg_map = {"A":"A级项目：提供货币类资产质押，本金覆盖率大于100%，违约可能性极低。",
 				   "B":"B级项目：提供有形或无形资产抵质押（变现渠道畅通），本金覆盖率不低于100%，违约可能性较低。",
 				   "C":"C级项目：提供有形或无形资产抵质押（有变现渠道）；或提供合格保证人担保，本金覆盖率不到100%，可能会违约。",
 				   "D":"D级项目：无有形或无形资产抵质押，无保证机构担保，能提供非保证机构担保，违约的可能性较高。"
 				  };
        	var html = '<a class="creditlevel ' + code + '" title="' + msg_map[code] + '">' + text + '</a>';
        	return html;
        }

        //融资额
        function _fndetailButton3(data,type,full){
            return '<span class="tdpay">'+full.packageQuotaStr+'</span>';
        }

        //年利率
        //product.ratePercentage
        function _fndetailButton5(data,type,full){
            return '<span class="tdpay">'+full.product.ratePercentage+'</span>';
        }

        
        //刷新方法 翻页做成了自动的，手动翻页的div做了隐藏处理
        function refresh(){
        	mainTable.setParams([{name:"period",value:period},{name:"iDisplayLength",value:pageNum},{name:"iDisplayStart",value:curPage*pageNum}]);
            if(curPage<5){
            	curPage++;
            }else{
            	curPage=0;
            }
        	mainTable.invoke("fnDraw");
        }
        
        /*window.setInterval(function(){
        	refresh();
    	}, 60*1000);*/
    });
