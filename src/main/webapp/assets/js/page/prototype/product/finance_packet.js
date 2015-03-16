require(['jquery',
         'global',
         'bootstrap',
         'bootstrap-datepicker',
         'select2',
         'datatables',
         'requirejs/domready!'], 
function($, global){
	var url = window.location.href;
	var arg = url.substr(url.indexOf('?')+1);
	var roleNum = arg.substr(arg.indexOf('=')+1);
	var statusHTML = "";//modify initial status
	var operationHTML = "";//modify initial operate
	var role = "";
	var status = "";
	var title = "";
	var subtitle = "";
	var operate = "";
	
	$('#from-time,#to-time').datepicker({
        format: 'yyyy-mm-dd',
        weekStart: 1,
        autoclose: true,
        todayBtn: false,
        language: 'zh-CN',
//        setStartDate: ''
    }).on('changeDate', function(e){
    	$('#to-time').datepicker({ autoclose: true}).datepicker('setStartDate', e.date).focus();
    });
	
	switch(roleNum){
	case "0"://系统管理 手工还款
		role = 7;
		status = 14;
		title = 3;
		subtitle = 0;
		$('.right-blk-head-title').html("系统管理");
		$('.right-blk-head-subtitle').html("手工还款");
		$('#operation-select').html("<option selected>待处理</option><option>已处理</option>");
		$('#operation-select').val("待处理");//status select initial
		statusHTML = "还款中";//status and operation table initial
		operationHTML = " <div class='btn-group btn-group-xs'>" +
				"<button type='button' class='btn btn-mf-primary go-details' data-operate=3>" +
				"<i class='fa fa-dollar fa-lg' title='还款'></i></button></div> ";
		$('#operation-select').on('change',function(){
			$('.status-name').html(this.value);
			switch(this.value){
			case "待处理":
				$('.status-operate').html(operationHTML);
				$('.time-search').hide();
				break;
			default:
				status = 17;
				$('.time-search').show();
				$('.status-operate').html(" <div class='btn-group btn-group-xs'>" +
						"<button type='button' class='btn btn-mf-primary go-details'>" +
						"<i class='fa fa-eye fa-lg' title='详情'></i></button></div> ");
			}
		});	
		break;
	case "1"://系统管理 异常处理
		role = 7;
		status = 10;
		title = 3;
		subtitle = 1;
		$('.right-blk-head-title').html("系统管理");
		$('.right-blk-head-subtitle').html("融资包异常处理");
		$('#operation-select').html("<option selected>待处理</option><option>已处理</option>");
		$('#operation-select').val("待处理");
		statusHTML = "申购中";
		operationHTML = " <div class='btn-group btn-group-xs'>" +
				"<button type='button' class='btn btn-mf-primary go-details' data-operate=1>" +
				"<i class='fa fa-power-off fa-lg' title='终止'></i></button>" +
				"<button type='button' class='btn btn-mf-primary go-details' data-operate=0>" +
				"<i class='fa fa-times fa-lg' title='撤单'></i></button></div> ";
		$('#operation-select').on('change',function(){
			$('.status-name').html(this.value);
			switch(this.value){
			case "待处理":
				$('.status-operate').html(operationHTML);
				$('.time-search').hide();
				break;
			default:
				$('.time-search').show();
				$('.status-operate').html(" <div class='btn-group btn-group-xs'>" +
						"<button type='button' class='btn btn-mf-primary go-details'>" +
						"<i class='fa fa-eye fa-lg' title='详情'></i></button></div> ");
			}
		});	
		break;
	}
	
	//table initial
	var testdata1 = new Array("A01","昆明小微","1000,000,000","有担保","6各月","10%","一次性还本付息","昆明市政府","3级","2014-4-11 10:00","待审批","");
	for(var i=20;i>0;i--){
		var tr=document.createElement("tr");
		for(var j=0;j<=10;j++){
			var th=document.createElement("th");
			switch(j){
			case 0:
				th.innerHTML = i;break;
			case 1:
				th.innerHTML = "<a href='common_detail.html?role="+role+"&status="+status+"&title="+title+"&sub_title="+subtitle+"&operate=none'>" + testdata1[j] + "</a>";
				break;
			case 9:
				th.innerHTML = statusHTML;
				th.setAttribute("class","status-name");break;
			case 10:
				th.innerHTML = operationHTML;
				th.setAttribute("class","status-operate");break;
			default:
				th.innerHTML = testdata1[j];
			}
			tr.appendChild(th);
		}
		$("#table-search-result").find("tbody").append(tr);
	}
	$("#table-search-result").on('click','tr',function(){
		$("#table-search-result tbody tr.mytr-selected").removeClass("mytr-selected");
		$(this).addClass("mytr-selected");
	});
	//datatables initial
	$("#table-search-result").dataTable({
		"bPaginate":true,
        "bFilter":false,
        "bLengthChange": false,
        "iDisplayLength":8,
        "sPaginationType": "full_numbers",
        "oLanguage": {
            "sLengthMenu": "每页显示 _MENU_ 条记录",
            "sZeroRecords": "抱歉，没有找到",
            "sInfo": "从 _START_ 到 _END_ 共 _TOTAL_ 条数据",
            "sInfoEmpty": "没有数据",
            "sInfoFiltered": "(从 _MAX_ 条数据中检索)",
            "oPaginate": {
            	 "sFirst": "首页",
                 "sPrevious": "前一页",
                 "sNext": "后一页",
                 "sLast": "尾页",
            }
//			"sZeroRecords": "没有检索到数据",
        }
	});
	
	//operation initial
	$('.status-operate ').on('click','.operation-edit',function(){
		$('#finance-product').hide();
		$('#finance-apply').show("1000",function(){
			$('#main-tab li a').attr("data-toggle","tab");
			$('#main-tab a[href="#margin-cost"]').tab('show');
			$('.savestep').show();
			$('.submitstep').show();
			$('.nextstep').hide();
			$('.laststep').hide();
		});
	})
	.on('click','.go-details',function(){
		operate = $(this).attr("data-operate")?$(this).attr("data-operate"):"none";
		window.location.href='common_detail.html?role='+role+'&status='+status+'&title='+title+'&sub_title='+subtitle+'&operate='+operate;
	});
});
