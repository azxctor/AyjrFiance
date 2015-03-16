/*!
 *
 *
 *  创建人：陈恩明
 * 	创建日期：2014-07-28
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
            var options = {};
            options.tableId = '#packet-manage-table';
            options.sAjaxSource = global.context+"/web/signinstatus/cmbsearch";
            options.aaSorting = [[0,"desc"]];
            mainTable = packet.renderTable(options);
            packet.init();
            dp_helper.init("#sign-date");
        };
        var dp_helper = function(){
            return{
                init:function(selector){
                    var $dp = $(selector);
                    var me =this;
                    var datePickerSetting = {
                        format: 'yyyy-mm-dd',
                        startDate:me.UTCToday(),
                        weekStart: 1,
                        autoclose: true,
                        todayBtn: false,
                        language: 'zh-CN'
                    };
                    $dp.attr({"readonly":"readonly"});
                    $dp.datepicker(datePickerSetting);
                    $dp.datepicker("setUTCDate",me.UTCToday());
                    return $dp;
                },
                UTCDate:function UTCDate(){
                    return new Date(Date.UTC.apply(Date, arguments));
                },
                UTCToday:function UTCToday(){
                    var today = new Date();
                    return this.UTCDate(today.getFullYear(), today.getMonth(), today.getDate());
                }
            }
        }();

        var bindEvent = function(){
            $("body").on("click", "#sign", function () {
                var date = $("#sign-date").val();
                if(date.length==0){
                	date = "1000";//invalid date
                };
                
                $.ajax({
                    url:global.context+"/web/signinstatus/cmbsign/"+date,
                    type:'POST',
                    dataType:'json',
                    success:function(){
                        mainTable.invoke("fnDraw");
                    }

                });
            });
        };
        init();
        bindEvent();

    });
