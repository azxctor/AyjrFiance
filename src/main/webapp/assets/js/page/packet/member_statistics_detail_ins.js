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
    function($,global,util,report,report_helper){
    var main_report = new report("#detail-frame");
    var init = function () {
    	renderTime();
        main_report.request(getDto());
    };
    var bindEvent = function(){
	    $("#btn-search-detail").on("click",function(){
	        main_report.request(getDto());
	    });
    };
    var renderTime = function(){
        var type = $(".type-time-wrapper").attr("data-type");
        var startDate = $(".start-time-wrapper").text();
        var endDate = $(".end-time-wrapper").text();
    	$(".detail-time").empty().append(type);
        if(startDate||endDate) {
        
            $("#search-start-date").val(startDate);
            $("#search-end-date").val(endDate);	
        }
   
    };
    var getDto = function(){
        var dto = {};
        var param = util.get_search_data("#detail-form");
        var id = $(".id-wrapper").text();
        var startDate = $(".start-time-wrapper").text();
        var endDate = $(".end-time-wrapper").text();
        var typeValue =  $(".type-time-wrapper").attr("data-value");
        var features = {"exportFileExt":"xls","startDate":startDate,"eventName":"member.contract","endDate":endDate,"parentClientEvent":"reportController.detail","commonId":id,"timeType":typeValue};
        dto.data = $.extend(param,features);
        dto.url = global.context+"/web/report/getmemberstatisticsinvestor";
        return dto;
    };

    init();
    bindEvent();
	
});