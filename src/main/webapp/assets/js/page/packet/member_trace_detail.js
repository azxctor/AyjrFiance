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
    var repay_report = new report("#main-frame");
    //初始化第一个tab(债权明细)
    var init = function () {
    	var commonId = $(".id-wrapper").text();
    	var fea = {"commonId":commonId,"exportFileExt":"xls","parentClientEvent":"reportController.detail","eventName":"common.packet.detail.right,obligations.detail"};
        repay_report.request(getDto("#obligations-detail-form",fea));
    };
	

    var bindEvent = function(){
    	var commonId = $(".id-wrapper").text();
        packet.bindDatePickerByClass(".obligations-start-date",".obligations-end-date");
        packet.bindDatePickerByClass(".payback-start-date",".payback-end-date");
        packet.bindDatePickerByClass(".subscribe-start-date",".subscribe-end-date");
        packet.bindDatePickerByClass(".balance-start-date",".balance-end-date");
        $(".btn-search").on("click",function(){
        	var me = this;
        	var name = $(me).attr("data-tab");
        	var eventName = "";
        	if(name=="obligations") eventName = "common.packet.detail.right,"+name+".detail";
        	else eventName = name+".detail";
        	var fea = {"commonId":commonId,"exportFileExt":"xls","parentClientEvent":"reportController.detail","eventName":eventName};
        	repay_report.request(getDto("#"+name+"-detail-form",fea));
        });
        
	    $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
	    	var me = this;
        	var name = $(me).attr("data-tab");
        	var eventName = "";
         	if(name=="obligations") eventName = "common.packet.detail.right,"+name+".detail";
        	else eventName = name+".detail";
        	var fea = {"commonId":commonId,"exportFileExt":"xls","parentClientEvent":"reportController.detail","eventName":eventName};
        	repay_report.request(getDto("#"+name+"-detail-form",fea));
	    });
        	
    };      

        
    var getDto = function(form,_features){
        var dto = {};
        var param = null;
        if(form){
        	param = util.get_search_data(form);
        }
        var features = _features;
        dto.data = $.extend(param,features);
        dto.url = $(form).attr("action");
        return dto;
    };

    init();
    bindEvent();   
    
});