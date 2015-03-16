/*!
 *
 *  融资包管理 -报表 -我的还款计划表
 *  创建人：吴淑颖
 * 	创建日期：2014-06-16
 *
 */

require(['jquery',
        'global',
        'module/util',
        'packet_manage',
        'module/report',
        'module/report_controller'
       ],
    function($,global,util,packet,report){
    var repayment_report = new report("#repayment-schedule-frame");
    var init = function () {
    	repayment_report.request(getDto());
    };
    var getDto = function(){
        var dto = {};
        var features = {"parentClientEvent":"reportController.detail","eventName":"common.packet.detail"};
        dto.data = features;
        dto.url = global.context+"/web/report/getmyrepaymentschedule";
        return dto;
    };

    init();
});