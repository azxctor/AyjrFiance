require(['jquery',
         'bootstrap',
         'bootstrap-datepicker',
         'select2',
         'datatables',
         'requirejs/domready!'], 
function($){
	$('.form-user-infoquery #datetimepicker').datepicker({});
	$('.selectpicker').select2();
	//	$('#myTab a:last').tab('show');
	function addProtepy( name ){
		var btnname = "add-"+name+"-btn";
		var boxname = "add-"+name+"-box";
		var contentname = "add-"+name+"-content";
		$('.'+contentname).hide();
		$('.'+btnname).click(function(){
			$('.'+boxname).append($('.'+boxname+' .'+contentname+':first-child').clone().show());
		});
	};
	$('.add-close-btn').on('click', function(){
		$(this).parent(".add-car-content").remove();
	});
	addProtepy( "car" );
	addProtepy( "house" );
	addProtepy( "movable" );
	addProtepy( "natural" );
	addProtepy( "legal" );
	
	$('.house-yes-content').hide();
	$('.car-yes-content').hide();
	$('.asset-yes-content').hide();
	$('.credit1-yes-content').hide();
	$('.credit2-yes-content').hide();
	$('.check-click').click(function(){
		var name = this.name.substring(2);
		$('.'+name+'-yes-content').show();
	});
	$('.uncheck-click').click(function(){
		var name = this.name.substring(2);
		$('.'+name+'-yes-content').hide();
	});
	
	var financeapprovetable = $("#transaction-table").dataTable({
		"bFilter": false,
		"bLengthChange": false,
		"iDisplayLength": 8,
		"sPaginationType": "full_numbers"
	});
	
	$("#transaction-table").on("click",".check-link",function(){
        $("#modal-log").modal();
    });
	
	function getRole(){
		var patt = window.location.search;
		var urlId = patt.indexOf("role");
		var roleId = patt.substring(urlId + 5, urlId + 6);
		return roleId;
	}
	
	if (getRole()){
		$("#transaction-table .view").attr("href", "transaction_manager_params_detail.html")
		$("#transaction-table .update").attr("href", "transaction_manager_params_detail.html?role=1")
	}
	
	$("#transaction-table").on("click","tr",function(){
		$("tbody tr.row-selected").removeClass("row-selected");
		$(this).addClass('row-selected');
	});
	
});

