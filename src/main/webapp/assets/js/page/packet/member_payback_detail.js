/*!
 *
 *  报表 - 投资会员业务跟踪 - 回款詳情
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
    var main_report = new report("#payback-frame");
    var init = function () {
        main_report.request(getDto());
    };
    var bindEvent = function(){
	    $("#btn-search").on("click",function(){
	        main_report.request(getDto());
	    });
    };
    var getDto = function(){
        var dto = {};
        var param = util.get_search_data("#payback-form");
        var detailId = $(".detail-id-wrapper").text();
        var features = {"commonId":detailId,"exportFileExt":"xls,pdf","parentClientEvent":"reportController.detail"};
        dto.data = $.extend(param,features);
        dto.url = global.context+"/web/report/getpaymentdetail";
        return dto;
    };

    init();
    bindEvent();
	
});