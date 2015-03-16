require(['jquery',
         'bootstrap',
         'bootstrap-datepicker',
         'select2',
         'requirejs/domready!'], 
function($){
	$("#panel-heading").click(function(){
		$("#collapseOne").collapse('toggle');
		if($("#fa_icon").hasClass("fa-caret-down")){
			$("#fa_icon").removeClass("fa-caret-down").addClass("fa-caret-up");
		}
		else{
			$("#fa_icon").removeClass("fa-caret-up").addClass("fa-caret-down");
		}
	});
	
	$(".check-link").on("click", function(){
		$("#modal-log").modal();
	});
	
});