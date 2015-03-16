/*!
 *
 *  融资包管理 -报表 - 近期还款计划
 *  创建人：吴淑颖
 * 	创建日期：2014-06-016
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
    var repay_report = new report("#recent-repayment-frame");
    var init = function () {
        repay_report.request(getDto());
    };
    var bindEvent = function(){
    	$("#input-key-word").tooltip({
    		placement:"bottom"
    	});
    	
        packet.bindDatePicker();
        packet.bindDatePickerByClass(".signin-start-date",".signin-end-date");
        $("#recent-repayment-search-btn").on("click",function(){
            repay_report.request(getDto());
        });
    }; 
    var getDto = function(){
        var dto = {};
        var param = util.get_search_data("#recent-repayment-form");
        var features = {"exportFileExt":"xls","eventName":"common.packet.detail","parentClientEvent":"reportController.detail"};
        dto.data = $.extend(param,features);
        dto.url = global.context+"/web/report/getrecentrepaymentschedule";
        return dto;
    };

    init();
    bindEvent();
	
});