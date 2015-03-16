/*!
 *
 *  member_trace_property (2014-06-11)
 *  Author：jiangang lu
 *  
 */
require(['jquery',
        'global',
        'module/util',
        'form',
        'packet_manage',
        'module/report',
        'jquery.popupwindow',
        'module/report_controller'
       ],
  function($,global,util,form,packet,report){
    var member_report = new report('#member-frame');
    
    var ht = {};
    
    ht.tools = {};
    
    ht.tools.getDto = function(){
    	var dto = {};
    	var param = util.get_search_data("#member-subscribe-search");
    	var features = {'exportFileExt':'xls'};
    	dto.data = $.extend(param, features);
    	dto.url = global.context + "/web/report/getmemberasset";
    	return dto;
    }
    
    ht.ui = {};
    
    ht.ui.clickSearchBtn = function(obj){
    	obj.on('click', function(){
    		obj.trigger('submit');
    	});
    }
    
    ht.app = {};
    
    ht.app.init = function(){
    	member_report.request(ht.tools.getDto());
    }
    
    ht.app.toSearch = function(){
    	var searchBtn = $('#subscribe-search-btn');
    	ht.ui.clickSearchBtn(searchBtn);
    }
    
    ht.app.validate = function(){
    	$("#member-subscribe-search").validate({
    		submitHandler : function(){
    			member_report.request(ht.tools.getDto());
    		}
    	});
    }
    
    ht.app.init();
    
    ht.app.toSearch();
    
    ht.app.validate();
});