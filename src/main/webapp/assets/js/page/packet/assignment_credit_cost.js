/*!
 *
 *  assignment_credit_cost (2014-07-08)
 *  Author：jiangang lu
 *  
 */
;require(['jquery',
        'global',
        'module/util',
        'packet_manage',
        'module/report',
        'jquery.popupwindow',
        'module/report_controller'
       ],
function($,global,util,packet,report){
	
	var AssignmentCreditCost = function (){
		var _this = this;
		this.assignmentReport = new report('#assignment-cost-frame');
		var assignmentCostBtn = $('#assignment-cost-btn');
		
		assignmentCostBtn.on('click', function (){
			_this.assignmentReport.request(_this.getDto());
		});
	}
	
	AssignmentCreditCost.prototype = {
		
		getDto: function (){
			var dto={};
			var param = util.get_search_data('#assignment-cost-form')
			var features = {'exportFileExt':'xls', 'parentClientEvent':'reportController.detail', 'eventName':'protocol.detail,contract.detail'}
			dto.data = $.extend(param, features);
			dto.url = global.context + '/web/report/getcredittransferfee';
			return dto;
		},
	
		toGetDto: function (){
			this.assignmentReport.request(this.getDto());
		},
		
		dataPicker: function (){
			packet.bindDatePicker();
		}
	};
	
	var assignment = new AssignmentCreditCost();
	
	assignment.toGetDto();
	
	assignment.dataPicker();
});
