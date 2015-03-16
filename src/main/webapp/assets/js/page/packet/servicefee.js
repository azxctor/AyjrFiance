/*!
 *
 *  报表 - 信息服务费查询询
 *  创建人：陈恩明
 * 	创建日期：2014-06-07
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
    var main_report = new report("#main-frame");
    var init = function () {
        main_report.request(getDto());
    };
    var bindEvent = function(){
    	report_helper.bindDtpsById();
	    $("#btn-search").on("click",function(){
	        main_report.request(getDto());
	    });
    };
    var getDto = function(){
        var dto = {};
        var param = util.get_search_data("#main-form");
        var features = {"parentClientEvent":"reportController.detail","eventName":"servicefee.detail"};
        dto.data = $.extend(param,features);
        dto.url = global.context+"/web/report/getservicefee";
        return dto;
    };

    init();
    bindEvent();
	
});