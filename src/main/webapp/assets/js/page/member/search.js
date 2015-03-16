require(['jquery',
         'global',
         'module/util',
         'module/datatables',
         'jquery.ui',
         'module/ajax',
         'bootstrap',
         'bootstrap-datepicker',
         'requirejs/domready!'], 
function($, global, util,datatables){
	$("#indivSearchTable").dataTable({
		"bPaginate":true,
		"bFilter":false,
		"bLengthChange": false,
		"iDisplayLength":10,
		"sPaginationType": "full_numbers"
	});
	
	$("#orgSearchTable").dataTable({
		"bPaginate":true,
		"bFilter":false,
		"bLengthChange": false,
		"iDisplayLength":10,
		"sPaginationType": "full_numbers"
	});
	
	$("body").on("click","#indivSearchTable a.detail-link",function(){
			$("#search-indiv-wrapper").show("slide");
			$("#table-wrapper").hide();
		
	});
	
	$("body").on("click","#orgSearchTable a.detail-link",function(){
		$("#search-org-wrapper").show("slide");
		$("#table-wrapper").hide();
	
});
	
	$("#start-time").datepicker({
		 autoclose: true,
		 format: "yyyy-mm-dd"
	});
	
	$("#end-time").datepicker({
		 autoclose: true,
		 format: "yyyy-mm-dd"
	});
	
	$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
		 console.log(e.target);// activated tab
		//  e.relatedTarget  previous tab
		 if($(e.target).text() == "个人"){
			 $("#search-name").text("姓名:");
		 }else if($(e.target).text() == "机构"){
			 $("#search-name").text("企业名称:");
		 }
	});
});
