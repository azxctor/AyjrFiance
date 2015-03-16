require(['jquery',
         'global',
         'module/util',
         'module/datatables',
         'module/ajax',
         'bootstrap',
         'requirejs/domready!'], 
         function($,global,util,datatables){
		/**
		 * 变量
		 */
		var table=function(){
			var options = {};
			options.tableId = '#table-draw';
			options.sAjaxSource = global.context+"/web/market/autosubscribe/drawList";
			options.rawOptions = {"fnDrawCallback":function(){
				  $('#table-draw_paginate').hide();
			}};
			var tb=datatables.init(options);
			tb.setParams(util.getSearchData("#draw_search_area"));
			tb.create();
			return tb;
		}();
		
		/**
		 * 事件
		 */
		$("#deal-submit").on("click",function(){
			var pkgID=$("#packageId").val();
			$.ajax({
				type:"POST",
				contentType:'application/json;charset=utf-8',
				url: global.context+ "/web/market/autosubscribe/drawdone/"+pkgID
			}).done(function(){			
				$('.btn-done').attr("disabled", true);
			}).always(function(){
				$('.modal').modal('hide');
			});
		});
		$("#draw-search").on("click",function(){
			table.setParams(util.getSearchData("#draw_search_area"));
			table.rawTable.fnDraw();
		});
		$('#draw_search_area').keydown(function(event){
			var keycode = event.which;  
		    if (keycode == 13) {  
		    	$('#draw-search').trigger('click');   
		    	return false;
		    }  	    
		});
});
