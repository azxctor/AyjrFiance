/*!
 *
 *  authorization_service_center_transaction (2014-06-12)
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
	
	var transaction_report = new report("#transaction-frame");
	
	var ht = {};
	
	ht.tools = {};
	
	ht.tools.getDto = function(){
		var dto = {};
		var param = util.get_search_data('#authorization-transaction-search');
		var features = {'parentClientEvent':'reportController.detail', 'eventName':'volume.detail,payment.detail,activity.detail'};
		dto.data = $.extend(param, features);
		dto.url = global.context + "/web/report/getauthorizationtradetrace";
		return dto;
	}
	
	ht.ui = {};
	
	ht.ui.clickSearchBtn = function(obj){
		obj.on('click',function(){
			transaction_report.request(ht.tools.getDto());
		});
	}
	
	ht.app = {};
	
	ht.app.toGetDto = function(){
		transaction_report.request(ht.tools.getDto());
	}
	
	ht.app.datetimepicker = function(){
		packet.bindDatePicker();
	}
	
	ht.app.toSearch = function(){
		var searchBtn = $('#authorization-search-btn');
		ht.ui.clickSearchBtn(searchBtn);
	}
	
	ht.app.toGetDto();
	
	ht.app.datetimepicker();
	
	ht.app.toSearch();
	
});
