require(['jquery',
         'bootstrap',
         'bootstrap-datepicker',
         'select2',
         'datatables',
         'requirejs/domready!'], 
function($){
	$(".finance-type-group").select2({tags:["分组A", "分组B", "分组C"]});
	$(".finance-type-group-package").select2({tags:["融资包A", "融资包B", "融资包C"]});
	$('.selectpicker').select2();
	
	$(".form-horizontal").on("click", ".rdo-group", function(){
		if ($(".rdo-group[data-over='1']").is(":checked")){
			$(".rdo-content").show("1000");
		}else{
			$(".rdo-content").hide("1000");
		}
	});
	
});

