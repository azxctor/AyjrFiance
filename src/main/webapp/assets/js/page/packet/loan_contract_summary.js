/*!
 *
 *  loan_contract_summary (2014-06-18)
 *  Author：jiangang lu
 *  
 */
require(['jquery',
        'global',
        'module/util',
        'packet_manage',
        'module/report',
        'jquery.popupwindow',
        'module/report_controller',
        'jquery-ui-multiselect'
       ],
  function($,global,util,packet,report){
	
	var loan_report = new report('#main-frame');
	
	var Loan = function(){
		
	}
	
	Loan.prototype = {
		
		_multiSelect: function(obj){
			obj.multiselect({
				show: ["bounce", 200], 
				hide: ["explode", 1000],
				noneSelectedText: "==请选择==",
		        checkAllText: "全选",
		        uncheckAllText: '全不选'
			});
		},
		
		_getDto: function(){
			var dto = {};
			var param = util.get_search_data('#loan_search');
			var features = {'exportFileExt':'xls', 'parentClientEvent':'reportController.detail', 'eventName':'common.packet.detail,contract'};
			dto.data = $.extend(param, features);
			dto.url = global.context + '/web/report/getloancontract';
			return dto;
		},
		
		_clickSearch: function(obj){
			obj.on('click', function(){
				loan_report.request(Loan.prototype._getDto());
			});
		},
		
		toGetSelect: function(){
			var select = $('#autorization');
			this._multiSelect(select);
		},
		
		datatimepicker: function(){
			packet.bindDatePicker();
		},
		
		toGetDto: function(){
			loan_report.request(this._getDto());
		},
		
		toSearch: function(){
			var search_btn = $('#loan-search-btn');
			this._clickSearch(search_btn);
		}
	}
	
	var loan = new Loan();
	
	loan.toGetSelect();
	
	loan.datatimepicker();
	
	loan.toGetDto();
	
	loan.toSearch();
});
