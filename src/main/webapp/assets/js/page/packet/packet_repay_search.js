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
    var repay_report = new report("#repay-frame");
    var init = function () {
        repay_report.request(getDto());
    };
        var bindEvent = function(){
        packet.bindDatePicker();
        $("#repay-search-btn").on("click",function(){
            repay_report.request(getDto());
        });
    };
    var getDto = function(){
        var dto = {};
        var param = util.get_search_data("#packet-repay-form");
        var features = {"exportFileExt":"xls","parentClientEvent":"reportController.detail","eventName":"common.packet.detail,ins.payback.detail"};
        dto.data = $.extend(param,features);
        dto.url = global.context+"/web/report/getpackagerepayment";
        return dto;
    }

    init();
    bindEvent();
	
});