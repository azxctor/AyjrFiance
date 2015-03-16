/*!
 *
 *  资金财务-招行对账明细
 *  创建人：陈恩明
 * 	创建日期：2014-05-27
 *
 */
require(['jquery',
        'global',
        'packet_manage',
        'module/util'
    ],
    function($,global,packet,util){
        'use strict';
        var mainTable;
        var init = function(){
        	var searchDate =  $(".form-control");
            var datePickerSetting = {
                format: 'yyyy-mm-dd',
                weekStart: 1,
                autoclose: true,
                todayBtn: false,
                language: 'zh-CN'
            };
            
            searchDate.datepicker(datePickerSetting);
            var date = new Date();
            date = UTCDate(date.getFullYear(), date.getMonth(), date.getDate(), 0, 0, 0);
            searchDate.datepicker("setUTCDate",date);
            $("#btn-search").on("click",function(){
                var $start = $(".statement-start").val();
                var $end = $(".statement-end").val();
                if($start==""||$end==""){
                    $.pnotify({
                        text: '系统交易日期和银行交易日期为必选项',
                        type: 'error'
                    });
                    return;
                }
                
                set_total();
                
            	mainTable.setParams(util.getSearchData("#search-wrapper"));
                mainTable.invoke("fnDraw");
    		});

            //初始化table
            var options = {};
            options.tableId = '#packet-manage-table';
            options.rawOptions = {"bPaginate":false,"fnDrawCallback": function ( oSettings ) {
    			/* Need to redo the counters if filtered or sorted */
    			if ( oSettings.bSorted || oSettings.bFiltered )
    			{
    				for ( var i=0, iLen=oSettings.aiDisplay.length ; i<iLen ; i++ )
    				{
    					$('td:eq(0)', oSettings.aoData[ oSettings.aiDisplay[i] ].nTr ).html( i+1 );
    				}
    			}
    		}};
            options.sAjaxSource = global.context+"/web/fund/transferTrxDetails";
            options.aaSorting = [[0,"desc"]];
            options.functionList = { 
            	"returnResult":returnResult,
				"_fnTrxAmt":_fnTrxAmt,
				"_fnTxAmt":_fnBnkTxAmt
            };
            mainTable = packet.renderTable(options);
        };
        var returnResult = function(_,__,data){
              if(data.result==true) return  '<span class="table-align-center" style="color:#49B21F;font-size:20px;padding-left:5px;">√</span>';
              else if(data.result==false) return '<span class="table-align-center" style="color:RED;font-size:20px;padding-left:5px;">×</span>';
        };
		
		
		var _fnTrxAmt=function(data, type, extra){
			var payRecvFlg = extra.payRecvFlag.name;
			var data1 = numeral(extra.trxAmt).format('0,0.00');
			if(payRecvFlg=="RECHARGE"){
				return '<span class="table-align-right" style="color:#49B21F;">'+("+"+data1)+'</span>';
			}else if(payRecvFlg=="WITHDRAW"){
				return '<span class="table-align-right" style="color:RED;">'+("-"+data1)+'</span>';
			}else{
				return '<span class="table-align-right" style="color:#000000;">'+(data1=='0.00'?'':data1)+'</span>';
			}
		};
		var _fnBnkTxAmt=function(data, type, extra){
			var payRecvFlg2 = extra.txDir.name;
			var data2 = numeral(extra.txAmt).format('0,0.00');
			if(payRecvFlg2=="BANK2EXCHANGE"){
				return '<span class="table-align-right" style="color:#49B21F;">'+("+"+data2)+'</span>';
			}else if(payRecvFlg2=="EXCHANGE2BANK"){
				return '<span class="table-align-right" style="color:RED;">'+("-"+data2)+'</span>';
			}else{
				return '<span class="table-align-right" style="color:#000000;">'+(data2=='0.00'?'':data2)+'</span>';
			}
		};

        var appendThead = function(){
            var theadHtml = '<tr class="state-thead"><th colspan="7" data-ignore="true"  data-bSortable="false" class="state-thead-th">系统明细</th>\
            <th colspan="8" data-ignore="true"  data-bSortable="false" class="state-thead-th">招行明细</th>\
            <th colspan="1" rowspan="2" data-ignore="true" data-bSortable="false" class="state-res1">结果</th>\
             </tr>';
            $("#packet-manage-table").find("thead").prepend(theadHtml);
        };

        var UTCDate = function(){
            return new Date(Date.UTC.apply(Date, arguments));
        };

        init();

        appendThead();
        
        var set_total = function(){
        	var $start = $(".statement-start").val();
            var $end = $(".statement-end").val();
            var data = {"trxDt":$start,"txDt":$end};
        	$.ajax({
        		url:global.context+"/web/fund/transferTrxDetailsSum",
        		dataType: 'json',
        		type: 'post',
				contentType: 'application/json; charset=UTF-8',
			    data: JSON.stringify(data)
        	}).done(function(resp){
        		$("#jys").find(".rjbs").text(resp.platformRechargeCount||"0");
        		$("#jys").find(".rjze").text('+'+util.get_thousand(resp.platformRechargeAmt)||"0");
        		$("#jys").find(".cjbs").text(resp.platformWithdrawalCount||"0");
        		$("#jys").find(".cjze").text('-'+util.get_thousand(resp.platformWithdrawalAmt)||"0");
        		
        		$("#bank").find(".rjbs").text(resp.bnkRechargeCount||"0");
        		$("#bank").find(".rjze").text('+'+util.get_thousand(resp.bnkRechargeAmt)||"0");
        		$("#bank").find(".cjbs").text(resp.bnkWithdrawalCount||"0");
        		$("#bank").find(".cjze").text('-'+util.get_thousand(resp.bnkWithdrawalAmt)||"0");
        	});
        };
        
        set_total();
    });
