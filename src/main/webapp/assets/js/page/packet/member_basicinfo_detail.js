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
        
        /*地区下拉框事件*/
		$("#person-province").listenChange({
			url:global.context+'/web/option/region/',
			nextElement:$("#person-city"),
			bindingElement:$("#person-region")
		});
		$("#person-city").listenChange({
			url:global.context+'/web/option/region/',
			nextElement:$("#person-county"),
			bindingElement:$("#person-region")
		});
    };
    var getDto = function(){
        var dto = {};
        var param = util.get_search_data("#member-basicinfo-form");
        var features = {"exportFileExt":"","parentClientEvent":"reportController.detail","eventName":"memberbasicinfo.detail"};
        dto.data = $.extend(param,features);
        dto.url = global.context+"/web/report/getmemberInfo";
        return dto;
    };
    
    
    
    init();
    bindEvent();
	
});