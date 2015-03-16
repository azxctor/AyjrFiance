/*!
 *
 *  融资包管理 -报表 - 还款查询
 *  创建人：唐超，陈恩明
 * 	创建日期：2014-06-07
 *
 */

require(['jquery',
        'global',
        'module/util',
        'packet_manage',
        'module/report',
        'jquery.popupwindow',
        'module/report_controller'
       ],
    function($,global,util,packet,report){
    var report = new report("#main-frame");
    var init = function () {
        report.request(getDto());
    };
        var bindEvent = function(){
        packet.bindDatePicker();
        $("#search-btn").on("click",function(){
            report.request(getDto());
        });
    };
    var getDto = function(){
        var dto = {};
        var param = util.get_search_data("#packet-repay-form");
        var features = {"exportFileExt":"xls","parentClientEvent":"reportController.detail","eventName":"trace"};
        dto.data = $.extend(param,features);
        dto.url = global.context+"/web/report/gememberbusinesstrace";
        return dto;
    }

    init();
    bindEvent();
	
});