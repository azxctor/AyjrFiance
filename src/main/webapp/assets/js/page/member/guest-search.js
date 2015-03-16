require(['jquery',
         'global',
         'module/util',
         'module/datatables',
         'module/form',
         'module/bread_crumb',
         'jquery.ui',
         'module/ajax',
         'bootstrap',
         'bootstrap-datepicker',
         'requirejs/domready!'], 
function($, global, util,datatables,form,mfBread){
	var tabFlag = 'P';
	var person_table = null;
	var org_table = null;
	//var except = ["gender", "legal_gender"];
	//var original_obj = {};
	
	var personalTable = function(){
		var options = {};
		options.tableId = '#personGuestTable';
		options.sAjaxSource = global.context+"/web/search/memberinfo";
		//options.functionList={"_fnAgentBtnRender":_fnAgentBtnRender,"_fnfinanceAgent":_fnfinanceAgent,"_fnAgentRegister":_fnAgentRegister,"_fnRenderlog":_fnRenderlog};
		//options.aaSorting=[[2,"desc"]];
		person_table = datatables.init(options);
		person_table.setParams(util.getSearchData('#guest_search_area'));
		return person_table.create();
	};

	var organizationTable = function(){
		//Organization 
		var options = {};
		options.tableId = '#orgGuestTable';
		options.sAjaxSource = global.context+"/web/search/memberinfo";
		//options.functionList={"_fnOrgAgentBtnRender":_fnOrgAgentBtnRender,"_fnfinanceAgent":_fnfinanceAgent,"_fnAgentRegister":_fnAgentRegister,"_fnRenderlog":_fnRenderlog};
		//options.aaSorting=[[2,"desc"]];
		org_table = datatables.init(options);
		org_table.setParams(util.getSearchData('#guest_search_area'));
		return org_table.create();
	};
	
	var personal=personalTable();
	var organization;
	
	/**
	 * Define search event
	 */
	$('#guest_search_btn').click(function(){
	
		if(tabFlag=='P'){
			person_table.setParams(util.getSearchData("#guest_search_area"));
			person_table.invoke("fnDraw");
		}
		if(tabFlag=='O'){
			if(organization){
				org_table.setParams(util.getSearchData("#guest_search_area"));
				org_table.invoke("fnDraw");
			}
		}
	});
	
	/*extend String prototype*/
	String.prototype.endsWith = function(suffix) {
	    return this.indexOf(suffix, this.length - suffix.length) !== -1;
	};
	
	/*$(".right-blk-head").on("click","li:eq(1) a",function(){
		$(".form-wrapper").hide();
		$("#table-wrapper").show("slide");
		if(person_table){
			person_table.invoke("fnStandingRedraw");
		}
		if(org_table){
			 org_table.invoke("fnStandingRedraw");
		}
		mfBread.pop();
	});*/
	
	$(".datepicker").datepicker({
		 autoclose: true,
		 format: "yyyy-mm-dd",
		 language: 'zh-CN'
	});

	
	$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
		 console.log(e.target);// activated tab
		//  e.relatedTarget  previous tab
		$('#guest-search-form-id')[0].reset();
		 if($(e.target).text() == "个人"){
			$('#hidden_search_user_type').val('PERSON');
			person_table.setParams(util.getSearchData("#guest_search_area"));
			tabFlag='P';
			person_table.invoke("fnDraw");
		 }else if($(e.target).text() == "机构"){
			$('#hidden_search_user_type').val('ORGANIZATION');
			 tabFlag='O';
			 if(organization){
				 org_table.setParams(util.getSearchData("#guest_search_area"));
				 org_table.invoke("fnDraw");
			 }else{
				 organization = organizationTable();
			 }
		 }
	});
	
	$("#guest_search_area").on("keydown",function(e){
		if(e.which == 13){
			$("#guest_search_btn").trigger("click");
			return false;
		}
	});
	
	//excel导出
	//导出excel
	$('#export-excel').on('click',function(){  
		$("#use-types").val($('#hidden_search_user_type').val());
		$("#from-date").val($('#member_start_time').val());
		$("#to-date").val($('#member_end_time').val());   
		$("#form-export").submit();
		return false;
	});
});
