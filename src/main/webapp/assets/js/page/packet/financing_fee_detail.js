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
        var param = util.get_search_data("#financing-fee-form");
        var features = {"exportFileExt":"xlsx,pdf","parentClientEvent":"reportController.detail","eventName":"financingfee.detail"};
        dto.data = $.extend(param,features);
        dto.url = global.context+"/web/report/getfinancefeedetail";
        return dto;
    }
    
    
    
    init();
    bindEvent();
	
});