/*!
 *
 *  会员审核查询 -报表
 *  创建人：王晓阳
 * 	创建日期：2014-06-16
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
    var repay_report = new report("#member-frame");
    var init = function () {
        repay_report.request(getDto());
    };
        var bindEvent = function(){
        packet.bindDatePicker();
        $("#member-audit-btn").on("click",function(){
            repay_report.request(getDto());
        });
    };
    var getDto = function(){
        var dto = {};
        var param = util.get_search_data("#member-audit-form");
        var features = {"exportFileExt":"xls"};
        dto.data = $.extend(param,features);
        console.log(dto.data);
        dto.url = global.context+"/web/report/getmemberapproveInfo";
        return dto;
    }

    init();
    bindEvent();
	
});