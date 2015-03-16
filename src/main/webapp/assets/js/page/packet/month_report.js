/*!
 *
 *  报表 - 月度理财报告
 *  创建人：陈恩明
 * 	创建日期：2014-07-08
 *
 */

require(['jquery',
         'global',
         'module/util',
         'module/report',
         'report_helper',
         'module/view_pusher',
         'module/report_controller'
         ],
         function($,global,util,report,report_helper,pusher){
	var main_report = new report("#main-frame");
	var init = function () {
		main_report.request(getDto());
        MonthSelector.init();
	};
	var bindEvent = function(){
		var startTime =  $("#search-date");
		startTime.attr({"readonly":"readonly"});
		var datePickerSetting = {
				format: 'yyyy-mm-dd',
				weekStart: 1,
				autoclose: true,
				todayBtn: false,
				language: 'zh-CN'
		};
		startTime.datepicker(datePickerSetting);
        $("#query-year").change(function(){
            MonthSelector.renderMonth();
        });
		$("#btn-search").on("click",function(){
			main_report.request(getDto());
		});

		$("body").on("click","#btn-search-month",function(){
			var year = $("#query-year").val();
			var month = $("#query-month").val();
			$(".month-wrapper").empty().text(year+"-"+month);
			$(".modal").modal("hide");
			pusher.push({
				url: global.context+"/web/report/investormonthreportdetailpage",
				type : 'GET',
				element:"#main-wrapper",
				title:"会员月度理财报告",
				onHide:function(){
					
				}
			});
		});
	};
	var getDto = function(){
		var dto = {};
		var param = util.get_search_data("#main-form");
		var features = {"parentClientEvent":"reportController.detail","eventName":"monthreport.detail"};
		dto.data = $.extend(param,features);
		dto.url = global.context+"/web/report/getinvestormonthreport";
		return dto;
	};
    var MonthSelector = function(){
       var $year = $("#query-year");
       var $month = $("#query-month");
       var cDate = new Date();
       var cYear = cDate.getFullYear();
       var cMonth = cDate.getMonth()+1;
       var i;


       return{
           init:function(){
               var me = this;
               me.renderYear();
               me.renderMonth();
           },
           renderYear: function(){
               var html = "";
               for(i = cYear;i >= 2000;i--){
                   html+="<option value='"+i+"'>"+i+"</option>";
               }
               $year.empty().append(html);
           },
           renderMonth: function() {
               var max = $year.val()==cYear? cMonth:12;
               var html = "";
               for(i = 1;i <= max;i++){
                   if(i < 10) html+="<option value='0"+i+"'>0"+i+"</option>";
                   else html+="<option value='"+i+"'>"+i+"</option>";
               }
               $month.empty().append(html);
           }
       };
    }();
	init();
	bindEvent();

});