/*!
 *
 *  融资包管理 -报表 -授权服务中心-交易手续费查询
 *  创建人：吴淑颖
 * 	创建日期：2014-06-14
 *
 */

require(['jquery',
        'global',
        'module/util',
        'packet_manage',
        'module/report',
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
        var dto = {};
        var param = util.get_search_data("#packet-transaction-form");
        var features = {"exportFileExt":"xls,pdf"};
        dto.data = $.extend(param,features);
        dto.url = global.context+"/web/report/getauthorizationself";
        return dto;
    };

    init();
    bindEvent();
	
});