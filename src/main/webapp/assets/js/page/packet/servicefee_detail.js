/*!
 *
 *  报表 - 信息服务费查询 詳情
 *  创建人：陈恩明
 * 	创建日期：2014-06-16
 *
 */

require(['jquery',
        'global',
        'module/util',
        'module/report',
        'report_helper',
        'module/report_controller'
    ],
    function($,global,util,report){
        var main_report = new report("#detail-frame");
        var init = function () {
            dp_helper.init("#search-start-date");
            main_report.request(getDto());
        };
        var bindEvent = function(){
            $("#btn-search-detail").on("click",function(){
                var $dp = $("#search-start-date");
                if($dp.val()==""){
                    $.pnotify({
                        text: '统计时间为必选项',
                        type: 'error'
                    });
                    return;
                }
                main_report.request(getDto());
            });
        };
        var dp_helper = function(){
            return{
                init:function(selector){
                    var $dp = $(selector);
                    var me = this;
                    var datePickerSetting = {
                        format: 'yyyy-mm',
                        weekStart: 1,
                        autoclose: true,
                        todayBtn: false,
                        language: 'zh-CN',
                        minViewMode:1
                    };
                    $dp.attr({"readonly":"readonly"});
                    $dp.datepicker(datePickerSetting);
                    $dp.datepicker("setStartDate",me.UTCDate("2014","07"));
                    $dp.datepicker("setUTCDate",me.UTCCurrentMonth());
                    return $dp;
                },
                UTCDate:function(){
                    return new Date(Date.UTC.apply(Date, arguments));
                },
                UTCCurrentMonth:function(){
                    var today = new Date();
                    return this.UTCDate(today.getFullYear(), today.getMonth());
                }
            }
        }();
        var getDto = function(){
            var dto = {};
            var param = util.get_search_data("#detail-form");
            var id = $(".id-wrapper").text();
            var features = {"exportFileExt":"xls,pdf","parentClientEvent":"reportController.detail","commonId":id};
            dto.data = $.extend(param,features);
            dto.url = global.context+"/web/report/getservicefeedetail";
            return dto;
        };

        init();
        bindEvent();

    });