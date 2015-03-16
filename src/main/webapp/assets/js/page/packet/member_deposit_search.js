/*!
 *
 *  member_deposit_search (2014-06-17)
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
	
	var memeber_report_fund = new report('#member-frame-fundChannel'),
	    member_report = new report('#member-frame');
	
	
	var member_deposit = {};
	
	member_deposit.tools = {};
	
	member_deposit.tools.printUrl = global.context + '/web/report/getcashpoolchargewithdraw';
	member_deposit.tools.url = global.context + '/web/report/getmemberchargewithdraw';
	
	member_deposit.tools.getDto = function(url, exportFileExt){
		var dto = {};
		var param = util.get_search_data('#member-deposit-search');
		var features = {'exportFileExt':exportFileExt};
		dto.data = $.extend(param, features);
		dto.url = url;
		return dto;
	}
	
	member_deposit.ui = {};
	
	member_deposit.ui.clickSearchBtn = function(obj){
		obj.on('click', function(){
			memeber_report_fund.request(member_deposit.tools.getDto(member_deposit.tools.printUrl, 'pdf'));
			member_report.request(member_deposit.tools.getDto(member_deposit.tools.url, 'xls'));
		});
	}
	
	member_deposit.app = {};
	
	member_deposit.app.datetimepicker = function(){
		packet.bindDatePicker();
	}
	
	member_deposit.app.toGetFundDto = function() {
		memeber_report_fund.request(member_deposit.tools.getDto(member_deposit.tools.printUrl, 'pdf'));
	}
	
	member_deposit.app.toGetDto = function(){
		member_report.request(member_deposit.tools.getDto(member_deposit.tools.url, 'xls'));
	}
	
	member_deposit.app.toSearch = function(){
		var search_btn = $('#member_deposit_btn');
		member_deposit.ui.clickSearchBtn(search_btn);
		
	}
	
	member_deposit.app.datetimepicker();
	
	member_deposit.app.toGetFundDto();
	
	member_deposit.app.toGetDto();
	
	member_deposit.app.toSearch();
});
