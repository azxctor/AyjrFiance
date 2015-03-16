/*!
 *
 *  报表 - 当日逾期明细
 *  创建人：陈恩明
 * 	创建日期：2014-07-10
 *
 */
require(['packet_manage',
        'global',
        'module/util'],
    function(packet,global,util){
        'use strict';
        var ReportGenerator = function(){};
        ReportGenerator.prototype = {
            init:function(){
                var options = {};
                options.tableId = '#packet-manage-table';
                options.sAjaxSource = global.context+"/web/dailyrisk/getoverduedetail";
                options.aaSorting = [[0,"desc"]];
                packet.renderTable(options);
                packet.init();
            },
            bindEvent:function(){
                var me = this;
               /* $("#btn-export").click(function(){
                    var data = util.getSearchData("#search-wrapper");
                    me.request(data);
                });*/
            },
            request:function(data){
                $.ajax({
                    url:global.context+"/web/dailyrisk/exportoverdueExcel?startDate="+data[0].value+"&&endDate="+data[1].value,
                    type:"POST",
                 /*   dataType:"json",*/
                    contentType:'application/json;charset=utf-8',
                    success:function(data){
                        console.log(data);
                    }
                });
            }
        };
        var report = new ReportGenerator();
        report.init();
       // report.bindEvent();
    });


