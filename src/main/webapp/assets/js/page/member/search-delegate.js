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
	$("body").on("click","#delPerSearchTable a.check-link",function(){
			$("#search-delPer-wrapper").show("slide");
			$("#table-wrapper").hide();
			var userId = $(this).attr("data-id");
			getMemberDetails('#indiv-basic-info','PERSON',userId);
	});
	
	$("body").on("click","#delOrgSearchTable a.check-link",function(){
		$("#search-delOrg-wrapper").show("slide");
		$("#table-wrapper").hide();
		var userId = $(this).attr("data-id");
		getMemberDetails('#org-basic-info','ORGANIZATION',userId);
	
    });
	$("#delPerSearchTable").dataTable({
		"bPaginate":true,
		"bFilter":false,
		"bSort" : true,
		"bLengthChange": false,
		"iDisplayLength":10,
		"sPaginationType": "full_numbers",
	});
	
	$("#delOrgSearchTable").dataTable({
		"bPaginate":true,
		"bFilter":false,
		"bSort" : true,
		"bLengthChange": false,
		"iDisplayLength":10,
		"sPaginationType": "full_numbers",
	});
	$(".indiv-go-back").on("click",function(e){
		e.preventDefault();
		$(".form-wrapper").hide();
		$("#table-wrapper").show("slide");
	});
	
	$(".org-go-back").on("click",function(e){
		e.preventDefault();
		$("#table-wrapper").show("slide");
		$(".form-wrapper").hide();
	});
	$(".datapicker").datepicker({
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
