/*!
 *
 *  融资包管理 -报表 -还款表
 *  创建人：吴淑颖
 * 	创建日期：2014-06-12
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
	var data = $(".id-wrapper").text();
    var repayment_report = new report("#repayment-frame");
    var init = function () {
    	repayment_report.request(getDto());
    };

    var getDto = function(){
        var dto = {};
        var features = {"commonId":data,"exportFileExt":"xls","parentClientEvent":"reportController.detail","eventName":"ins.payback.detail"};
        dto.data = features;
        dto.url = global.context+"/web/report/getrepaymentbypackageId";
        return dto;
    };

    init();
});