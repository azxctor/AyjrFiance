/*!
 *
 *  融资包管理 -报表 -授权服务中心表(系统管理员，交易手续费查询结果)
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
    var transaction_report = new report("#transaction-frame");
    var init = function () {
    	transaction_report.request(getDto());
    };
    var bindEvent = function(){
        packet.bindDatePicker();
        $("#transaction-search-btn").on("click",function(){
        	transaction_report.request(getDto());
        });
    };
    var getDto = function(){
    	$("#start-time").text($("#search-start-date").val());
    	$("#end-time").text($("#search-end-date").val());
        var dto = {};
        var param = util.get_search_data("#packet-transaction-form");
        var features = {"parentClientEvent":"reportController.detail","eventName":"transaction.detail"};
        dto.data = $.extend(param,features);
        dto.url = global.context+"/web/report/getauthorization";
        return dto;
    };

    init();
    bindEvent();
	
});