/*!
 *
 *  volume_statistics (2014-06-12)
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
	
	 var volume_report_table = new report('#volume-frame-table');
	 var volume_report = new report('#volume-frame');
	 
	 var ht = {};
	 
	 ht.tools = {};
	 
	 ht.tools.tableurl = global.context + '/web/report/gettradestatistics';
	 ht.tools.barurl = global.context + '/web/report/gettradestatisticsbar';
	 
	 ht.tools.getDto = function(url){
		 var id = $('.id-wrapper').text();
		 var dto = {};
		 var param = util.get_search_data("#volume-statistics");
		 var features = {'commonId':id};
		 dto.data = $.extend(param, features);
		 dto.url = url;
	     return dto;
	 }
	 
	 ht.ui = {};
	 
	 ht.ui.changeSelect = function(obj){
		 obj.on('change', function(){
			 volume_report_table.request(ht.tools.getDto(ht.tools.tableurl));
			 volume_report.request(ht.tools.getDto(ht.tools.barurl));
		 });
	 }
	 
	 ht.app = {};
	 
	 ht.app.toGetDto = function(){
		 volume_report_table.request(ht.tools.getDto(ht.tools.tableurl));
	 }
	 
	 ht.app.toGetBar = function(){
		 volume_report.request(ht.tools.getDto(ht.tools.barurl));
	 }
	 
	 ht.app.toChangeYear = function(){
		 var yearSelect = $('#year');
		 ht.ui.changeSelect(yearSelect);
	 }
	 
	 ht.app.toGetDto();
	 
	 ht.app.toGetBar();
	 
	 ht.app.toChangeYear();
});
