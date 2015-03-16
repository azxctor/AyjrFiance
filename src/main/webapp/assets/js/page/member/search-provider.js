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
	$("body").on("click","#providerSearchTable a.check-link",function(){
			$("#search-provider-wrapper").show("slide");
			$("#table-wrapper").hide();
	});
	
	
	$(".go-back").on("click",function(e){
		e.preventDefault();
		$(".form-wrapper").hide();
		$("#table-wrapper").show("slide");
	});
	
	$("#start-time").datepicker({
		 autoclose: true,
		 format: "yyyy-mm-dd"
	});
	
	$("#end-time").datepicker({
		 autoclose: true,
		 format: "yyyy-mm-dd"
	});
});
