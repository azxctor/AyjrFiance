/*!
 *
 *  activity_statistics (2014-06-13)
 *  Author：jiangang lu
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
	
	var activity_report_table = new report("#activity-frame-table");
	var activity_report = new report("#activity-frame");
	
	var ht = {};
	
	ht.tools = {};
	
	ht.tools.tableurl = global.context + '/web/report/getinvestoractivity';
	ht.tools.pieurl = global.context + '/web/report/getinvestoractivitypie';
	
	ht.tools.getDto = function(url){
		var id = $('.id-wrapper').text();
		var dto = {};
		var param = util.get_search_data('#activity-form');
		var features = {"commonId" : id};
		dto.data = $.extend(param, features);
		dto.url = url
		return dto;
	}
	
	ht.ui = {};
	
	ht.ui.clickSearchBtn = function(obj){
		obj.on('click', function(){
			activity_report_table.request(ht.tools.getDto(ht.tools.tableurl));
			activity_report.request(ht.tools.getDto(ht.tools.pieurl));
		});
	}
	
	ht.ui.clickChartBtn = function(obj){
		obj.on('click', function(){
			var dto = {};
			var param = util.get_search_data('#activity-form');
			var features = {};
			dto.data = $.extend(param, features);
			dto.url = global.context + '';
			return dto;
		});
	}
	
	ht.app = {};
	
	ht.app.datetimepicker = function(){
		packet.bindDatePicker();
	}
	
	ht.app.toGetDto = function(){
		activity_report_table.request(ht.tools.getDto(ht.tools.tableurl));
	}
	
	ht.app.toGetChartDto = function(){
		activity_report.request(ht.tools.getDto(ht.tools.pieurl));
	}
	
	ht.app.toSearch = function(){
		var searchBtn = $('#activity-search-btn');
		ht.ui.clickSearchBtn(searchBtn);
	}
	
	ht.app.datetimepicker();
	
	ht.app.toGetDto();
	
	ht.app.toGetChartDto();
	
	ht.app.toSearch();
});
