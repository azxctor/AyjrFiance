require(['jquery',
         'bootstrap',
         'bootstrap-datepicker',
         'select2',
         'datatables',
         'requirejs/domready!'], 
function($){
	var tabledata = new Array("2014-3-21", "债权申购", "申购", "10", "A123021834", "厂房贷款", "6个月", "11%", "2级");
	var tabledata1 = new Array("2014-3-21", "债权转让", "卖出", "10", "A123021834", "厂房贷款", "6个月", "11%", "2级")
	var tabledata2 = new Array("2014-3-21", "债权转让", "买入", "10", "A123021834", "厂房贷款", "6个月", "11%", "2级")
	for(var row=0; row<20; row++){
		var tr = document.createElement("tr");
		for(var j=0; j<9; j++){
			var td = document.createElement("td");
			if (4 == j){
				td.innerHTML = "<a href='common_detail.html?title=5&sub_title=1&role=1&status=14'>"+ tabledata[j] +"</a>";
			}else{
				if (row % 2 == 0){
					td.innerHTML = tabledata1[j];
				}else if (row % 3 == 0){
					td.innerHTML = tabledata2[j];
				}else{
					td.innerHTML = tabledata[j];
				}
				
			}
			
			tr.appendChild(td);
		}
		$("#transaction-table").find("tbody").append(tr);
	}
	
	var transactionTable = $("#transaction-table").dataTable({
		"bFilter": false,
		"bLengthChange": false,
		"iDisplayLength": 8,
		"sPaginationType": "full_numbers"
	});
	
	$("#transaction-table").on("click",".check-link",function(){
        $("#modal-log").modal();
    });
	
	datepicker();
	
	function datepicker(){
		$(".start-time,.end-time").datepicker({
	        format: 'yyyy-mm-dd',
	        weekStart: 1,
	        autoclose: true,
	        todayBtn: false,
	        language: 'zh-CN'
	    }).on("changeDate",function(ev){
	    });
	}
	
});

