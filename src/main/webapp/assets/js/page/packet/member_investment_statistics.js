/*!
 *
 *  member_investment_statistics (2014-06-14)
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
	
	var member_report = new report('#member-investment-frame');
	
	var ht = {};
	
	ht.tools = {};
	
	ht.tools.memberType = 'PACKAGE';
	
	ht.tools.getDto = function(eventName){
		var dto = {};
		var param = util.get_search_data('#member-investment-search');
		var features = {'exportFileExt':'xls', 'parentClientEvent':'reportController.detail', 'eventName':eventName};
		dto.data = $.extend(param, features);
		dto.url = global.context + "/web/report/getmemberstatistics";
		return dto;
	}
	
	ht.ui = {};
	
	ht.ui.changeSelect = function(obj, keyword){
		var placeholder = '';
		
		obj.change(function(){
			ht.tools.memberType = $(this).val();
			
			if ('FINANCIER' == ht.tools.memberType){
				placeholder = '输入投资会员交易账号模糊查询...';
			}
			
			if ('PACKAGE' == ht.tools.memberType){
				placeholder = '输入融资包编号、融资包简称模糊查询...';
			}
			
			if ('' != placeholder){
				keyword.attr('placeholder', placeholder);
			}
		});
		
	}
	
	ht.ui.clickSearchBtn = function(obj){
		var event_name = '';
		
		obj.on('click', function(){
			if ('PACKAGE' == ht.tools.memberType){
				event_name = 'common.packet.detail,packet.detail';
			}
			
			if ('FINANCIER' == ht.tools.memberType){
				event_name = 'investment.detail';
			}
			member_report.request(ht.tools.getDto(event_name));
			
			var timetype = $('#timetype').val();
			var type_time_wrapper = $(".type-time-wrapper");
			var timetypetext = "签约日期";
			if ("SIGN" == timetype){
				timetypetext = "签约日期";
			}
			if ("SUBSCRIBE" == timetype){
				timetypetext = "申购日期";
			}
			type_time_wrapper.attr("data-value", timetype);
			type_time_wrapper.attr("data-type", timetypetext);
			$(".start-time-wrapper").empty().append($('#search-start-date').val());
			$(".end-time-wrapper").empty().append($('#search-end-date').val());
		});
        obj.click();
	}
	
	ht.app = {};
	
	ht.app.init = function(){
		var type_time_wrapper = $(".type-time-wrapper");
    	type_time_wrapper.attr("data-value", "SIGN");
    	type_time_wrapper.attr("data-type", "签约日期");
	}
	
	ht.app.datetimepicker = function(){
		packet.bindDatePicker();
	}
	
	ht.app.toGetDto = function(){
		member_report.request(ht.tools.getDto('common.packet.detail,packet.detail'));
	}
	
	ht.app.toGetMemberType = function(){
		var select_member_type = $('#member-type');
		var key_word = $('#key-word');
		
		ht.ui.changeSelect(select_member_type, key_word);
	}
	
	ht.app.toSearch = function(){
		var searchBtn = $('#investment-search-btn');
		ht.ui.clickSearchBtn(searchBtn);
	}
	
	ht.app.init();
	
	ht.app.datetimepicker();
	
/*	ht.app.toGetDto();*/
	
	ht.app.toGetMemberType();
	
	ht.app.toSearch();
});
