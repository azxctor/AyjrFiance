/*!
 *
 *  报表 - 月度理财报告 詳情
 *  创建人：陈恩明
 * 	创建日期：2014-07-07
 *
 */

require(['jquery',
        'global',
        'module/util',
        'module/report',
        'report_helper',
        'module/report_controller'
       ],
    function($,global,util,report,report_helper){
	var ReportPage = function(){
		this.init = function(interfaces,num){
			var me = this;
			for(var i = 0;i<num;i++ ){
				var main_report = new report("#detail-frame"+i);
			    main_report.request(me.getDto(interfaces[i]));
			}
		},
		this.getDto = function(url){
			var dto = {};
	        var param = util.get_search_data("#detail-form");
	        var id = $(".id-wrapper").text();
	        var month = $(".month-wrapper").text();
	        var features = {"keyWord":month,"parentClientEvent":"reportController.detail","commonId":id};
	        dto.data = $.extend(param,features);
	        dto.url = url;
	        return dto;
		}
	};
    var reportPage = new ReportPage();
    var interfaces = [global.context+"/web/report/getinvestormonthreportdetail",
	                  global.context+"/web/report/getinvestormonthtotalreport",
	                  global.context+"/web/report/getaccountmonthreport",
	                  global.context+"/web/report/getinvestormonthrisk"];

    reportPage.init(interfaces,4);	
    
  /*  var ResizeController = function(){};
    ResizeController.prototype = {
    		resetIframeHeight:function(){
    			var mainheight = $("iframe").contents().find("body").height();
    			$("iframe").height(mainheight);
    		}		
    };
	
	window.reportController = new ResizeController(); */
});