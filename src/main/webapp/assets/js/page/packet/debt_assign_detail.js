require(['jquery',
        'global',
        'module/util',
        'packet_manage',
        'module/report',
        'jquery.popupwindow',
        'module/cascading_listener',
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
        var param = util.get_search_data("#debt-assign-form");
        var features = {"exportFileExt":"xls","parentClientEvent":"reportController.detail","eventName":"common.packet.detail,debtaggrement.detail,debtassign.detail"};
        dto.data = $.extend(param,features);
        dto.url = global.context+"/web/report/getcredittransfer";
        return dto;
    };
    
    
    
    init();
    bindEvent();
	
});