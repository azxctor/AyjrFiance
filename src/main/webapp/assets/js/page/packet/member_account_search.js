/*!
 *
 *  member_account_search (2014-06-18)
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
	
	var member_report = new report('#member-account-frame');
	
	var member_account = {};
	
	member_account.tools = {};
	
	member_account.tools.getDto = function(){
		var dto = {};
		var param = util.get_search_data('#member_account_search');
		var features = {'exportFileExt':'xls,pdf'};
		dto.data = $.extend(param, features);
		dto.url = global.context + '/web/report/getmemberaccount';
		return dto;
	}
	
	member_account.ui = {};
	
	member_account.ui.clickSearchBtn = function(obj){
		obj.on('click', function(){
			member_report.request(member_account.tools.getDto());
		});
	}
	
	member_account.app = {};
	
	member_account.app.datetimepicker = function(){
		packet.bindDatePicker();
	}
	
	member_account.app.toGetDto = function(){
		member_report.request(member_account.tools.getDto());
	}
	
	member_account.app.toSearch = function(){
		var search_btn = $('#member-account-btn');
		member_account.ui.clickSearchBtn(search_btn);
	}
	
	member_account.app.datetimepicker();
	
	member_account.app.toGetDto();
	
	member_account.app.toSearch();
});
