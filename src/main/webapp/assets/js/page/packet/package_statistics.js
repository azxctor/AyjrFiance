/*!
 *
 *  融资包管理 -报表 -融资包统计表
 *  创建人：吴淑颖
 * 	创建日期：2014-06-16
 *
 */

require(['jquery',
        'global',
        'module/util',
        'packet_manage',
        'module/report',
       ],
    function($,global,util,packet,report){
	//绑定datepicker
    var bindDatePicker = function(sTime,etime){
        var startTime =  $(sTime);
        var endTime = $(etime);
        startTime.attr({"readonly":"readonly"});
        endTime.attr({"readonly":"readonly"});
        var datePickerSetting = {
            format: 'yyyy-mm-dd',
            weekStart: 1,
            autoclose: true,
            todayBtn: false,
            language: 'zh-CN'
        };
        startTime.datepicker(datePickerSetting);
        endTime.datepicker(datePickerSetting);
        //后面时间必须大于前面时间
        startTime.on("changeDate",function(){
            var start = startTime.val();
            endTime.datepicker('setStartDate',start);
        });
        endTime.on("changeDate",function(){
            var end = endTime.val();
            startTime.datepicker('setEndDate',end);
        });
    };
    var statistics_report = new report("#statistics-frame");
    var init = function () {
    	bindDatePicker("#sign-start-date","#sign-end-date");
    	bindDatePicker("#subscripte-start-date","#subscripte-end-date");
    	statistics_report.request(getDto());
    };
    
    var bindEvent = function(){
        packet.bindDatePicker();
        $("#statistics-search-btn").on("click",function(){
        	statistics_report.request(getDto());
        });
    };
    var getDto = function(){
        var dto = {};
        var param = util.get_search_data("#packet-statistics-form");
        var features = {"exportFileExt":"xls"};
        dto.data = $.extend(param,features);
        dto.url = global.context+"/web/report/getpackagestatistics";
        return dto;
    };

    init();
    bindEvent();
});