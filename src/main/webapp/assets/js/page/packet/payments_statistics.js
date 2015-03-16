/*!
 *
 *  payments_statistics (2014-06-12)
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
	
	var payment_report = new report('#payment-frame');
	
	var ht = {};
	
	ht.tools = {};
	
	ht.tools.paymenturl = global.context + '/web/report/getpaymentstatistics';
	ht.tools.realpaymenturl = global.context + '/web/report/getrealpaymentstatistics';
	
	ht.tools.getDto = function(url){
		var id = $('.id-wrapper').text();
		var dto = {};
		var param = util.get_search_data('#payments-statistics-search');
		var features = {'commonId' : id,"exportFileExt":"xls,pdf"};
		dto.data = $.extend(param, features);
		dto.url = url;
		return dto;
	}
	
	ht.tools.getDateTypeUrl = function(obj){
		var pay_date_type = obj.val();
		var url = "";
		if ("NEED" == pay_date_type){
			return ht.tools.paymenturl;
		}
		if ("REAL" == pay_date_type){
			return ht.tools.realpaymenturl;
		}
	}
	
	ht.ui = {};
	
	ht.ui.clickSearchBtn = function(objBtn, objSelect){
		objBtn.on('click', function(){
			var url = ht.tools.getDateTypeUrl(objSelect)
			payment_report.request(ht.tools.getDto(url));
		});
	}
	
	ht.app = {};
	
	ht.app.datetimepicker = function(){
		packet.bindDatePicker();
	}
	
	ht.app.toGetDto = function(){
		payment_report.request(ht.tools.getDto(ht.tools.paymenturl));
	}

	ht.app.toSearch = function(){
		var payments_search_btn = $('#pyaments-search-btn');
		var pay_date_type = $('#payDateType');
		ht.ui.clickSearchBtn(payments_search_btn, pay_date_type);
	}
	
	ht.app.datetimepicker();
	
	ht.app.toGetDto();
	
	ht.app.toSearch();
});
