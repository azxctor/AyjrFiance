
require(['packet_manage','global'],
    function(packet,global){
        var mainTable = null;
        var searchDate =  $(".form-control");
        var datePickerSetting = {
            format: 'yyyy-mm-dd',
            weekStart: 1,
            autoclose: true,
            todayBtn: false,
            language: 'zh-CN'
        };
        
        searchDate.datepicker(datePickerSetting);
        
        //var UTCDate = function(){
        //    return new Date(Date.UTC.apply(Date, arguments));
        //};
        //var date = new Date();
        //date = UTCDate(date.getFullYear(), date.getMonth(), date.getDate(), 0, 0, 0);
        //searchDate.datepicker("setUTCDate",date);
        
        var _fmtPool=function(data, type, extra){
        	if(extra.total==false){
				return '<span style="font-size:14px;">'+data+'</span>';
			}else{
				return '<span style="font-size:16px;font-weight:bold;">'+data+'</span>';
			}
		};
        
        var _fmtAmt_red=function(data, type, extra){
			var data2 = numeral(data).format('0,0.00');
			if(extra.total==false){
				return '<span class="table-align-right" style="font-size:14px;color:red;">'+data2+'</span>';
			}else{
				return '<span class="table-align-right" style="font-size:16px;font-weight:bold;color:red;">'+data2+'</span>';
			}
		};
		
		var _fmtAmt_green=function(data, type, extra){
			var data2 = numeral(data).format('0,0.00');
			if(extra.total==false){
				return '<span class="table-align-right" style="font-size:14px;color:green;">'+data2+'</span>';
			}else{
				return '<span class="table-align-right" style="font-size:16px;font-weight:bold;color:green;">'+data2+'</span>';
			}
		};
		
		var _fmtResult=function(data, type, extra){
			if(data=="√"){
				return '<span style="font-size:18px;color:green;">'+data+'</span>';
			}else{
				return '<span style="font-size:18px;color:red;">'+data+'</span>';
			}
		};
        
        //初始化
        var init = function(){
            packet.initHoverMsg("platform");
            var options = {};
            options.tableId = '#pool-check-list';
            options.sAjaxSource = global.context+"/web/fund/cash/pool/Datas";
            options.rawOptions = {"bPaginate":false,"bSort":false};
            options.functionList = {
				"_fmtAmt_red":_fmtAmt_red,
				"_fmtAmt_green":_fmtAmt_green,
				"_fmtResult":_fmtResult,
				"_fmtPool":_fmtPool
            };
            mainTable = packet.renderTable(options);
            packet.init();
            packet.initLog();
        };
        init();
        $("#pool-check-list .fa-question-circle").tooltip();
    });