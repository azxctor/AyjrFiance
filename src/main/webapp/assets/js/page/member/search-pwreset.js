require(['jquery',
         'global',
         'module/util',
         'module/datatables',
         'module/bread_crumb',
         'bootstrap',
         'module/ajax',
         'module/form',
         'requirejs/domready!'], 
function($, global,util,datatables,mfBread){
	var person_table;
	var _fnSrhPerBtnRender2 = function (data,type,full){
		return '<div class="btn-group btn-group-xs"><a title="&nbsp;" data-toggle="modal" data-target="#serviceCheckModal" class="btn btn-mf-primary go-details check-link table-link" type="button"><i class="fa fa-eye fa-lg"></i></a></div>';
	};
	var _fnSrhPerBtnRender3 = function (nTd,type,full){
		$(nTd).find('.go-details').click(function(){
			var num=""; 
			for(var i=0;i<6;i++) num+=Math.floor(Math.random()*10); 
			$("#serviceCheckModal input[id='user_id']").val(full.userId);
			$("#serviceCheckModal lable[id='selected_user']").text(full.name);
			$("#serviceCheckModal input[id='new_password']").val(num);
			$("#serviceCheckModal lable[id='selected_password']").text(num);
		});
	};
	
	var _fnRenderlog = function(data,type,full){
		return '<a  data-id="'+full.userId+'" class="log-link table-link" data-type="ind" data-toggle="modal" data-target="#logModal">日志</a>';
	};
	
	
	var init = function(){
		var options = {};
		options.tableId = '#table-wrapper';
		options.sAjaxSource = global.context+"/web/search/getmemberinfoforpwreset";
		options.functionList={"_fnSrhPerBtnRender2":_fnSrhPerBtnRender2,"_fnRenderlog":_fnRenderlog,"_fnSrhPerBtnRender3":_fnSrhPerBtnRender3};
		options.aaSorting=[[2,"desc"]];
		
		person_table = datatables.init(options);
		person_table.setParams(util.getSearchData('#search_area'));
		person_table.create();
		
	};
	
	 //绑定事件
    var bindEvent = function(){
        //table按钮事件绑定
        $("#member_search").click(function(){
        	person_table.setParams(util.getSearchData("#search_area"));
        	person_table.invoke("fnDraw");
        	
        });
        $("body").on("keydown",function(e){
    		if(e.which == 13){
    			$("#member_search").trigger("click");
    			return false;
    		}
    	});
        
        $("#submit-form").submit(function(){
        	util.ajax_submit('#submit-form');
        	$("#serviceCheckModal .modal-footer .btn-cancel").trigger("click");
        	return false;
        });
        
        
   
    };
    
	init();
	bindEvent();
	
	
	
});


