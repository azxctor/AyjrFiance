require(['jquery',
         'global',
         'module/util',
         'module/datatables',
         'module/ajax',
         'bootstrap',
         'bootstrap-datepicker',
         'bootstrap-datepicker.zh-CN',
         'bootstrapx-popoverx',
         'ickeck',
         'sockjs',
         'stomp',
         'requirejs/domready!'], 
         function($,global,util,datatables){
	/**
	 * 变量
	 */
	var table=function(){
		var options = {};
		options.tableId = '#table-candidate';
		options.sAjaxSource = global.context+"/web/market/autosubscribe/candidatesList";
		options.rawOptions = {"fnDrawCallback":function(){
			  $('#table-candidate_paginate').hide();
		}};
		var tb=datatables.init(options);
		tb.setParams(util.getSearchData('#candidate_search_area'));
		tb.create();
		return tb;
	}();
	
	$("#tip-riskParam").tooltip({
		html:true,
		duration:20000,
		title:function(){
			return	"<ul class=\"candidate-tooltip\">" +
					"<li>1. A级项目</li>" +
					"<li>2. B级项目</li>" +
					"<li>3. C级项目</li>" +
					"<li>4. D级项目</li>" +
					"</ul>";
		}
	});
	
	$("#tip-payMethodParam").tooltip({
		html:true,
		duration:20000,
		title:function(){
			return 	"<ul class=\"candidate-tooltip\">" +
					"<li>1. 到期一次还本付息</li>" +
					"<li>2. 按月等额还息，到期一次还本</li>" +
					"<li>3. 按月等本等息</li>" +
					"<li>4. 按月等额本息</li>" +
					"</ul>";
		}
	});
	
	$("#tip-warrantyTypeParam").tooltip({
		html:true,
		duration:20000,
		title:function(){
			return  "<ul class=\"candidate-tooltip\">" +
					"<li>1. 本金担保</li>" +
					"<li>2. 本息担保</li>" +
					"<li>3. 无担保</li>" +
					"</ul>";
		}
	});
	
	
	/**
	 * 事件
	 */
	$("#draw-search").on("click",function(){
        table.setParams(util.getSearchData("#candidate_search_area"));
  		table.invoke("fnDraw");
	});
	$('#candidate_search_area').keydown(function(event){
		var keycode = event.which;  
	    if (keycode == 13) {  
	    	$('#draw-search').trigger('click');   
	    	return false;
	    }  	    
	});
});
