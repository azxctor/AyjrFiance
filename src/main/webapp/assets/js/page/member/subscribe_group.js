require(['jquery',
         'global',
         'module/util',
         'module/datatables',
         'underscore',
         'module/ajax',
         'jquery.placeholder',
         'bootstrap',
         'bootstrap-switch',
         'bootstrap-datepicker',
         'requirejs/domready!'], function($,global,util,datatables,_){
	//针对不同功能的确认框
	var ADD_GROUP_CONFIRM = 1,DELETE_GROUP_CONFORM = 2;
	var group_id;
	
	var _fnRenderBtn = function(data,type,full){
		return '<div class="btn-group btn-group-xs">'+
		'<a data-id="'+full.groupId+'" class="btn btn-mf-primary go-details edit-link" data-toggle="modal" data-target="#configModal"  data-backdrop="static">'+
		'<i title="详情" class="fa fa-pencil fa-lg"></i></a>'+
	/*	'<a data-id="'+full.groupId+'" class="btn btn-mf-primary delete-link" data-group-name="'+full.groupName+'">'+
		'<i title="删除" class="fa fa-times fa-lg"></i></a>'+*/
		'</div>';
	};
	var _fnRenderBtn1 = function(data,type,full){
		if(!_.contains(selected_user_arr,full.userId))
		return '<input type="checkbox"><input type="hidden" value="'+full.userId+'">';
	};
	
	var _fnRenderSwitch = function(data,type,full){
		var checked = "";
		if(full.actvie){
			checked = "checked";
		}
		return '<input type="checkbox" '+checked+' data-id="'+full.groupId+'" class="group-switch"/>';

	};
	
	var _fnInitBootSwitch = function(nRow, aData, iDataIndex){
		var groupSwitch = $(nRow).find(".group-switch");
		groupSwitch.bootstrapSwitch({
			onColor:"success",
			size:"small",
			onText:"启用",
			offText:"停用"
		});
		groupSwitch.on('switchChange.bootstrapSwitch', function(event, state) {
			var active = "active";
			if(!state){
				active = "deactive";
			}
			var group_id = $(this).attr("data-id");
			console.log($(this).attr("data-id")+"---"+state);
			$.ajax({
				url: global.context+"/web/subscriber/groupinfo/"+active+"/"+group_id+"",
				type: "post"
			}).done(function(resp){
			/*	if(resp.code == "ACK"){
					sub_table.invoke("fnStandingRedraw");
				}*/
			});
		});
	}
	var leftTable,rightTable;
	var sub_table = null;
	var sub_table1 = null;
	var option = {};
	var option1 = {};
	var selected_user_arr = [];
	var html = '';
	var $lefttable = $('#leftTable');
	var $righttable = $('#rightTable');
	
	var initialTable = function(){
		option.tableId = "#configSearchTable";
		option.sAjaxSource = global.context+"/web/subscriber/getgroupinfo";
		option.functionList = {"_fnRenderBtn":_fnRenderBtn,"_fnRenderSwitch":_fnRenderSwitch};
		option.rawOptions = {"fnCreatedRow":_fnInitBootSwitch};
		sub_table = datatables.init(option);
		//sub_table.setParams(util.getSearchData('#search_area'));
	   return  sub_table.create();
	};
	var initialLeftTable = function(){
		option1.tableId = "#leftTable";
		option1.sAjaxSource = global.context+"/web/subscriber/groupinfo/investor";
		option1.functionList = {"_fnRenderBtn1":_fnRenderBtn1};
		option1.rawOptions = {"bAutoWidth":false};
		/*option1.bFilter = true;*/
		sub_table1 = datatables.init(option1);
		sub_table1.setParams(util.getSearchData('#serch'));
	    return  sub_table1.create();
	};
	
	
	var inintial_left_table = function(){
		leftTable = initialLeftTable().rawTable;
		/* $("#leftTable_filter").find("label").text('');
		 var input = '<input type="text" aria-controls="leftTable" name="investorName" placeholder="输入关键字搜索">';
		 $("#leftTable_filter").find("label").append(input);*/
		// $('input').placeholder();
	};
	
	var inintial_right_table = function(){
		 rightTable = $righttable.dataTable({
				"bFilter": true,
				"bLengthChange": false,
				"bInfo": false,
				"iDisplayLength":10,
				"sPaginationType": "full_numbers",
				"bAutoWidth":false,
				aaSorting:[[2,"asc"]],
				"oLanguage": {
				      "sSearch": "",
				      "oPaginate" : {
							"sFirst" : "首页",
							"sPrevious" : "前一页",
							"sNext" : "后一页",
							"sLast" : "尾页",
						},
					  "sInfoEmpty" : "没有数据",
					  "sZeroRecords" : "没有检索到数据"
				},
				"aoColumnDefs": [
				              {"aTargets" : [ '_all' ], "sDefaultContent" : "&nbsp;"},
				              {"aTargets" : [ 0 ], "bSearchable": false, "bSortable":false },
				              {"aTargets" : [ 1 ], "bSortable": false}, 
				              {"aTargets" : [ 2 ], "bSearchable": false, "bSortable": false},
				              {"aTargets" : [ 3 ], "bSearchable": false, "bSortable": false},
				              {"aTargets" : [ 4 ], "bSearchable": false}
				]
				
			});
		 //$("#rightTable_filter").find("input").attr("placeholder","输入授权服务中心搜索");
		 $('input').placeholder();
	};
	var conTable = initialTable();
	
	
	/*var get_all_invest = function(){
		$.ajax({
			url:global.context+"/web/subscriber/groupinfo/investor",
			type : 'GET',
			contentType:'application/json;charset=utf-8'
		}).done(function(resp){
			var resp = $.parseJSON(resp);
			for(var i=0;i<resp.length;i++){
				html+='<tr data-id='+resp[i].userId+'>'+
                       '<td><input type="checkbox"><input type="hidden" value="'+resp[i].userId+'"></td>'+
                       '<td>'+resp[i].userName+'</td>'+
                       '<td>'+resp[i].authorizedCenterName+'</td>'+
                       '<td>'+resp[i].level+'</td>'+
                       '</tr>';
			}
			$lefttable.append(html);
		});
	};
	get_all_invest();*/
	
	
	
//	$(".dataTables_filter").find("input").attr("placeholder","搜索");
	
	
	    $("#go-right").on("click",function(){
	    	var $trs = $("#leftTable").find("input:checked").parents("tr");
	    	$(".select-all").prop("checked",false);
			addData($trs,rightTable);
			
			$trs.each(function(){
				var user_id = $(this).find("input[type='hidden']").val();
			    selected_user_arr.push(user_id);
			});
			sub_table1.invoke("fnStandingRedraw");
		});
		
		var addData = function(tr_obj,table){
			var arr = [];
			tr_obj.each(function(){
				$(this).find("td").each(function(){
					arr.push($(this).html());
//					console.log(arr);
				});
				table.fnAddData(arr);
			    arr = [];
			});
		};
	
		var removeData = function(tr_obj,table){
			tr_obj.each(function(){
				 var target_row = $(this).get(0);
				 var aPos = table.fnGetPosition(target_row); 
				 table.fnDeleteRow(aPos);
			});
		};
		$("#go-left").on("click",function(){
			$(".select-all").prop("checked",false);
			var $trs = $("#rightTable").find("input:checked").parents("tr");
			removeData($trs,rightTable);
			
			// 除去一个选中的user
			$trs.each(function(){
				var user_id = $(this).find("input[type='hidden']").val();
				var index = selected_user_arr.indexOf(user_id);
				if (index > -1) {
					selected_user_arr.splice(index, 1);
				}
			});
			sub_table1.invoke("fnStandingRedraw");
		});
		
		$("#agency_search").on("click",function(){
			sub_table.setParams(util.getSearchData('#search_area'));
			sub_table.invoke("fnDraw");
		});
		$("#serchbt").on("click",function(){
			//alert(util.getSearchData('#serch'));
			var search_name = $(".search-type").val();
			var search_val = $(".inner-left-input").val();
			sub_table1.setParams([{"name":search_name,"value":search_val}]);
			sub_table1.invoke("fnDraw");
		});
	
		$('#configModal').on('hidden.bs.modal', function (e) {
			leftTable.fnDestroy();
			rightTable.fnDestroy();
			$lefttable.find("tbody").empty();
			$righttable.find("tbody").empty();
			$lefttable.append(html);
		    $("#group-name").val("");
			$("#group-desc").val("");
			$("#group-id").val("");
			$(".inner-left-input").val("");
			$(".select-all").prop("checked",false);
			$("label.invalid").remove();
			selected_user_arr = [];
			
			$("#sc-area").html("");
			selected_centers = [];
			
			$(".auth_center").select2("val","");
			
			$('#add-tab a:first').tab('show');
			
		});
		
		//click table edit link
		$("#configSearchTable").on("click",".edit-link",function(){
			var id = $(this).attr("data-id");
			var user_ids = [];
			$.ajax({
				url:global.context+"/web/subscriber/groupinfo/"+id,
				contentType:"application/json"
			}).done(function(resp){
				var resp = $.parseJSON(resp);
				$("#group-name").val(resp.groupName);
				$("#group-desc").val(resp.groupDescribe);
				$("#group-id").val(resp.groupId);
				
				var users_html = "";
				var user_list = resp.users;
				for(var j=0;j<user_list.length;j++){
					selected_user_arr.push(user_list[j].userId);
				}
				for(var i=0;i<user_list.length;i++){
					users_html+='<tr>'+
	                       '<td><input type="checkbox"><input type="hidden" value='+user_list[i].userId+'></td>'+
	                       '<td>'+user_list[i].userName+'</td>'+
	                       '<td>'+user_list[i].accountNo+'</td>'+
	                       '<td>'+user_list[i].authorizedCenterName+'</td>'+
	                       '<td>'+user_list[i].level+'</td>'+
	                       '</tr>';
					user_ids.push(user_list[i].userId);
				}
				$righttable.append(users_html);
				
			//    removeExit($lefttable,user_ids);
			    inintial_left_table();
			    inintial_right_table();
			    $("#myModalLabel").text("编辑");
			});
		});
		
		//click table delete link
		$("#configSearchTable").on("click",".delete-link",function(){
			$("#confirm-modal").attr("data-type",DELETE_GROUP_CONFORM);
			var $this = $(this);
			group_id = $this.attr("data-id");
			_show_confirm_modal("确定删除定投组 "+$this.attr("data-group-name")+" ?");
		});
		
		/*初始化新增表单*/
		$("#add-group").on("click",function(){
			 inintial_left_table();	 
			 inintial_right_table();	 
			 $("#configModal").modal({
				 backdrop:'static'
			 });
			 $("#myModalLabel").text("新增");
		});
		
	/*	var removeExit = function(table,ids){
		table.find("tr").each(function(){
				if(_.contains(ids,$(this).attr("data-id"))){
					$(this).remove();
				}
				
			});
		};*/
		
		$("#add-group-confirm").on("click",function(){
			var type = parseInt($("#confirm-modal").attr("data-type"));
			switch (type) {
			case ADD_GROUP_CONFIRM:
				_commit_group();
				break;

			case DELETE_GROUP_CONFORM:
				_delete_group();
				break;
			}
			$("#confirm-modal").modal("hide");
		});
		
		$(".btn-confirm").click(function(){
			if(selected_centers.length != 0){
				$("#confirm-modal").attr("data-type",ADD_GROUP_CONFIRM);
				var center_names = ",";
				$(".center-label").each(function(){
					center_names += $(this).text()+",";
				})
				/*var b = window.confirm("确定将授权服务中心"+center_names+"下的所有投资人加入定投组?");
				if(!b){
					return;
				}*/
				_show_confirm_modal("确定将授权服务中心"+center_names+"下的所有投资人加入定投组 ?");
				return;
			}
			
			_commit_group();
		});
		
		var _delete_group = function(){
			$.ajax({
				url: global.context+"/web/subscriber/groupinfo/delete/"+group_id+"",
				type: "post",
				contentType: "application/json",
				headers: {
					'x-form-id' : "subscriber-group-form"
				},
				dataType: "json"
			}).done(function(resp){
				if(resp.code == "ACK"){
					sub_table.invoke("fnStandingRedraw");
				}
			});
		}
		
		var _commit_group = function(){
			var user_arrays = [];
			var $hiddens = $("input[type='hidden']",rightTable.fnGetNodes()).each(function(){
				var user_id = {};
				user_id.userId = $(this).val();
				user_arrays.push(user_id);
			});
			
			var groupName = $("#group-name").val();
			var groupDescribe = $("#group-desc").val();
			var groupId = $("#group-id").val();
			
			var data = {};
			data.groupName = groupName;
			data.groupId = groupId;
			data.groupDescribe = groupDescribe;
			data.users = user_arrays;
			data.serviceCenterIds = selected_centers;
			
			$.ajax({
				url: global.context+"/web/subscriber/groupinfo/save",
				type: "post",
				contentType: "application/json",
				headers: {
					'x-form-id' : "subscriber-group-form"
				},
				dataType: "json",
				data: JSON.stringify(data)
			}).done(function(resp){
				if(resp.code == "ACK"){
					$("#configModal").modal("hide");
					sub_table.invoke("fnStandingRedraw");
				}
			});
		};
		
		$(".select-all").on("click",function(){
			var $this = $(this);
			var checked = $this.is(":checked");
			var table_id = $(this).attr("data-tar");
			$(table_id).find("input[type='checkbox']").each(function(){
				$(this).prop("checked",checked);
			});
		});
		
		$("#search_area").on("keydown",function(e){
			if(e.which == 13){
				$("#agency_search").trigger("click");
				return false;
			}
		});
		
		/*add array indexof() method in ie<=8*/
		var add_indexOf_ie = function(){
			if (!Array.prototype.indexOf)
			{
			  Array.prototype.indexOf = function(elt /*, from*/)
			  {
			    var len = this.length >>> 0;

			    var from = Number(arguments[1]) || 0;
			    from = (from < 0)
			         ? Math.ceil(from)
			         : Math.floor(from);
			    if (from < 0)
			      from += len;

			    for (; from < len; from++)
			    {
			      if (from in this &&
			          this[from] === elt)
			        return from;
			    }
			    return -1;
			  };
			}
		};
		
		add_indexOf_ie();
		
		$(".auth_center").select2({
			 placeholder: "请输入授权服务中心的名称 ",
			 allowClear: true,
			formatNoMatches: function () { return "未找到匹配项"; }
		});
		
		var selected_centers = [];
		$("#addsc2group").on("click",function(){
			
			if(!$(".auth_center").select2("data")){
				//alert("请选择授权服务中心");
				_show_message_modal("请选择授权服务中心 !");
				return false;
			}
			var sc_text = $(".auth_center").select2("data").text;
			var sc_value = $(".auth_center").select2("val");
			
			if(_.contains(selected_centers,sc_value)){
				//alert("授权服务中心已存在");
				_show_message_modal("授权服务中心已存在 !");
				return false;
			}
			selected_centers.push(sc_value);
			$("#sc-area").append('<span class="label label-primary pull-left center-label" data-id="'+sc_value+'" title="双击删除">'+sc_text+'</span>');
			$(".center-label").tooltip({
			});
			return false;
		});
		
		$("body").on("dblclick",".center-label",function(){
			var $this = $(this);
			var center_id = $this.attr("data-id");
			$this.remove();
			$("#sc-area").find(".tooltip").remove();
			selected_centers = _.without(selected_centers,center_id);
		});
		
		var _show_message_modal = function(content){
			$("#message-content").html(content);
			$("#message-modal").modal("show");
		};
		
		var _show_confirm_modal = function(content){
			$("#confirm-content").html(content);
			$("#confirm-modal").modal("show");
		};
});