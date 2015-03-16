require(['jquery',
         'global',
         'bootstrap',
         'bootstrap-datepicker',
         'select2',
         'datatables',
         'requirejs/domready!'], 
function($, global){
	var url = window.location.href;
	if(url.indexOf('?')>0){
		var arg = url.substr(url.indexOf('?')+1);
		var roleNum = arg.substr(arg.indexOf('=')+1);
	}else{
		var roleNum = "0";
	}
	var statusHTML = "";//modify initial status
	var operationHTML = "";//modify initial operate
	var role = "";
	var status = "";
	var title = "";
	var subtitle = "";
	var titleArray = new Array("风险管理","会员管理","我的债务","系统管理","交易管理");
	var subttAttay = new Array("项目审核","风险评级","保证金管理","放款审批","融资项目查询","融资申请意向选择","项目审核","融资项目管理","手工还款","融资包异常撤单","融资项目查询");
	var statusArray = new Array("待提交","待接受","已接受","项目废弃","待机构审核","待平台审核","待评级","待冻结","待发布","待申购","申购中","待签约","待放款审批","订单废弃","还款中","逾期还款","代偿中","已完成","还款中","转让中","已转让");
	function getStatusNum( statusText ){
		var statusNum;
		for(var i=0;i<statusArray.length;i++){
			if(statusArray[i]==statusText){
				statusNum=i;
				return statusNum;
				break;
			}
		}
		if(i==statusArray.length){
			statusNum=-1;
		}
	}
	switch(roleNum){
	case "0"://融资会员
		role = 0;
		title = 2;
		subtitle = 0;
		status = 0;
		$('#btn-finance-apply').show();
		$('.right-blk-head-title').html("我的债务");
		$('.right-blk-head-subtitle').html("融资项目管理");
		$('#operation-select').val("待提交");//status select initial
		statusHTML = "待提交";//status and operation table initial
		operationHTML = "<div class='btn-group btn-group-xs'>" +
				"<button type='button' class='btn btn-mf-primary operation-edit'>" +
				"<i class='fa fa-pencil fa-lg operation-edit' title='修改'></i></button> " +
				"<button type='button' class='btn btn-mf-primary operation-revoke' data-toggle='popover' data-html=true data-container='.right-blk-body' data-placement='right' data-content='<button>撤单</button> <button>取消</button>'>" +
				"<i class='fa fa-times fa-lg' title='撤单'></i></button></div> ";
		$('#operation-select').on('change',function(){
			$('.status-name').html(this.value);
			status = getStatusNum(this.value);
			switch(this.value){
			case "待提交":
				$('.status-operate').html(operationHTML);
				popoverInitial();
				break;
			case "已接受":
				$('.status-operate').html(" <div class='btn-group btn-group-xs'>" +
						"<button type='button' class='btn btn-mf-primary go-details'>" +
						"<i class='fa fa-eye fa-lg' title='详情'></i></button> "+
						"<button type='button' class='btn btn-mf-primary' data-toggle='modal' data-target='#modal-affirm' title='意向确认'>" +
						"<i class='fa fa-check-square-o fa-lg'></i></button></div> ");
				break;
			default:
				$('.status-operate').html(" <div class='btn-group btn-group-xs'>" +
						"<button type='button' class='btn btn-mf-primary go-details'>" +
						"<i class='fa fa-eye fa-lg' title='详情'></i></button></div> ");
			}
		});	
		break;
	case "1"://担保机构 意向选择
		role = 2;
		title = 1;
		subtitle = 1;
		status = 1;
		$('#btn-finance-apply,#finance-apply').remove();
		$('.right-blk-head-title').html("会员管理");
		$('.right-blk-head-subtitle').html("融资申请意向选择");
		$('#operation-select-title').html("意向处理状态");
		$('#operation-select').html("<option selected>待处理意向</option><option>已处理意向</option><option>过期意向</option>");
		$('#operation-select').val("待处理意向");
		statusHTML = "待处理意向";
		operationHTML = " <div class='btn-group btn-group-xs'>" +
				"<button type='button' class='btn btn-mf-primary go-details'>" +
				"<i class='fa fa-thumbs-o-up fa-lg' title='意向选择'></i></button></div> ";
		$('#operation-select').on('change',function(){
			$('.status-name').html(this.value);
			switch(this.value){
			case "待处理意向":
				status = 1;
				$('.status-operate').html(operationHTML);
				break;
			default:
				status = 2;
				$('.status-operate').html(" <div class='btn-group btn-group-xs'>" +
						"<button type='button' class='btn btn-mf-primary go-details'>" +
						"<i class='fa fa-eye fa-lg' title='详情'></i></button></div> ");
			}
		});	
		break;
	case "2"://担保机构 审核
		role = 2;
		title = 1;
		subtitle = 1;
		status = 4;
		$('#btn-finance-apply,#finance-apply').remove();
		$('.right-blk-head-title').html("会员管理");
		$('.right-blk-head-subtitle').html("项目审核");
		$('#operation-select').html("<option selected>待审核</option><option>待平台审核</option><option>审核驳回</option>");
		$('#operation-select').val("待审核");
		statusHTML = "待审核";
		operationHTML = " <div class='btn-group btn-group-xs'>" +
				"<button type='button' class='btn btn-mf-primary go-details'>" +
				"<i class='fa fa-gavel fa-lg' title='审核'></i></button></div> ";
		$('#operation-select').on('change',function(){
			$('.status-name').html(this.value);
			switch(this.value){
			case "待审核":
				status = 4;
				$('.status-operate').html(operationHTML);
			break;
			default:
				status = 5;
				$('.status-operate').html(" <div class='btn-group btn-group-xs'>" +
						"<button type='button' class='btn btn-mf-primary go-details'>" +
						"<i class='fa fa-eye fa-lg' title='详情'></i></button></div> ");
			}
		});	
		break;
	case "3"://风控 审核
		role = 3;
		title = 0;
		subtitle = 0;
		status = 5;
		$('#btn-finance-apply,#finance-apply').remove();
		$('.right-blk-head-title').html("风险管理");
		$('.right-blk-head-subtitle').html("项目审核");
		$('#operation-select').html("<option selected>待平台审核</option><option>已处理</option>");
		$('#operation-select').val("待平台审核");
		statusHTML = "待平台审核";
		operationHTML = " <div class='btn-group btn-group-xs'>" +
				"<button type='button' class='btn btn-mf-primary go-details'>" +
				"<i class='fa fa-gavel fa-lg' title='审核'></i></button></div> ";
		$('#operation-select').on('change',function(){
			$('.status-name').html(this.value);
			switch(this.value){
			case "待平台审核":
				status = 5;
				$('.status-operate').html(operationHTML);
				break;
			default:
				status = 6;
				$('.status-operate').html(" <div class='btn-group btn-group-xs'>" +
						"<button type='button' class='btn btn-mf-primary go-details'>" +
						"<i class='fa fa-eye fa-lg' title='详情'></i></button></div> ");
			}
		});	
		break;
	case "4"://风控 评级
		role = 3;
		title = 0;
		subtitle = 1;
		status = 6;
		$('#btn-finance-apply,#finance-apply').remove();
		$('.right-blk-head-title').html("风险管理");
		$('.right-blk-head-subtitle').html("风险评级");
		$('#operation-select').html("<option selected>待评级</option><option>已处理</option>");
		$('#operation-select').val("待评级");
		statusHTML = "待评级";
		operationHTML = " <div class='btn-group btn-group-xs'>" +
				"<button type='button' class='btn btn-mf-primary go-details'>" +
				"<i class='fa fa-star fa-lg' title='评级'></i></button></div> ";
		$('#operation-select').on('change',function(){
			$('.status-name').html(this.value);
			switch(this.value){
			case "待评级":
				status = 6;
				$('.status-operate').html(operationHTML);
				break;
			default:
				status = 7;
				$('.status-operate').html(" <div class='btn-group btn-group-xs'>" +
						"<button type='button' class='btn btn-mf-primary go-details'>" +
						"<i class='fa fa-eye fa-lg' title='详情'></i></button></div> ");
			}
		});
		break;
	case "5"://风控人员 冻结
		role = 3;
		title = 0;
		subtitle = 2;
		status = 7;
		$('#btn-finance-apply,#finance-apply').remove();
		$('.right-blk-head-title').html("风险管理");
		$('.right-blk-head-subtitle').html("保证金管理");
		$('#operation-select').html("<option selected>待冻结</option><option>已处理</option>");
		$('#operation-select').val("待冻结");
		statusHTML = "待冻结";
		operationHTML = " <div class='btn-group btn-group-xs'>" +
				"<button type='button' class='btn btn-mf-primary go-details'>" +
				"<i class='fa fa-asterisk fa-lg' title='冻结'></i></button></div> ";
		$('#operation-select').on('change',function(){
			$('.status-name').html(this.value);
			switch(this.value){
			case "待冻结":
				status = 7;
				$('.status-operate').html(operationHTML);
				break;
			default:
				status = 8;
				$('.status-operate').html(" <div class='btn-group btn-group-xs'>" +
						"<button type='button' class='btn btn-mf-primary go-details'>" +
						"<i class='fa fa-eye fa-lg' title='详情'></i></button></div> ");
			}
		});
		break;
	case "6"://风控经理 冻结
		role = 4;
		title = 0;
		subtitle = 2;
		status = 7;
		$('#btn-finance-apply,#finance-apply').remove();
		$('.right-blk-head-title').html("风险管理");
		$('.right-blk-head-subtitle').html("保证金管理");
		$('#operation-select').html("<option selected>待冻结</option><option>已处理</option>");
		$('#operation-select').val("待冻结");
		statusHTML = "待冻结";
		operationHTML = " <div class='btn-group btn-group-xs'>" +
				"<button type='button' class='btn btn-mf-primary go-details'>" +
				"<i class='fa fa-asterisk fa-lg' title='冻结'></i></button></div> ";
		//response status
		$('#operation-select').on('change',function(){
			$('.status-name').html(this.value);
			switch(this.value){
			case "待冻结":
				status = 7;
				$('.status-operate').html(operationHTML);
				break;
			default:
				status = 8;
				$('.status-operate').html(" <div class='btn-group btn-group-xs'>" +
						"<button type='button' class='btn btn-mf-primary go-details'>" +
						"<i class='fa fa-eye fa-lg' title='详情'></i></button></div> ");
			}
		});
		break;
	case "7"://风控 融资项目查询
		role = 3;
		title = 0;
		subtitle = 4;
		status = 0;
		$('#btn-finance-apply,#finance-apply').remove();
		$('.right-blk-head-title').html("风险管理");
		$('.right-blk-head-subtitle').html("融资项目查询");
		$('#operation-select').val("待提交");
		statusHTML = "待提交";
		operationHTML = " <div class='btn-group btn-group-xs'>" +
				"<button type='button' class='btn btn-mf-primary go-details'>" +
				"<i class='fa fa-eye fa-lg' title='详情'></i></button></div> ";
		$('#operation-select').on('change',function(){
			status = getStatusNum(this.value);
			$('.status-name').html(this.value);
		});
		break;
	case "8"://交易部 融资项目查询
		role = 5;
		title = 4;
		subtitle = 0;
		status = 0;
		$('#btn-finance-apply,#finance-apply').remove();
		$('.right-blk-head-title').html("交易管理");
		$('.right-blk-head-subtitle').html("融资项目查询");
		$('#operation-select').val("待提交");
		statusHTML = "待提交";
		operationHTML = " <div class='btn-group btn-group-xs'>" +
				"<button type='button' class='btn btn-mf-primary go-details'>" +
				"<i class='fa fa-eye fa-lg' title='详情'></i></button></div> ";
		$('#operation-select').on('change',function(){
			status = getStatusNum(this.value);
			$('.status-name').html(this.value);
		});
		break;
	}	
	
	//table initial
	var testdata1 = new Array("A01","2014-05-01","昆明小微","小微","1000,000,000","100天","分期付款","","","");
	testdata1[9] = "<a>审核日志</a>";
	for(var i=20;i>0;i--){
		var tr=document.createElement("tr");
		for(var j=0;j<=9;j++){
			var th=document.createElement("th");
			switch(j){
			case 0:
				th.innerHTML = i;break;
			case 7:
				th.innerHTML = statusHTML;
				th.setAttribute("class","status-name");break;
			case 8:
				th.innerHTML = operationHTML;
				th.setAttribute("class","status-operate");break;
			case 9:
				th.innerHTML = testdata1[j];
				th.setAttribute("value", i);
				th.setAttribute("data-toggle", "modal");
				th.setAttribute("data-target", "#modal-log");break;
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
        }
	});
	
	//operation initial
	popoverInitial();
	function popoverInitial(){
		//revoke button
		$('.operation-revoke').popover()
		.on('show.bs.popover',function(){
			$('.popovered').popover('hide');
			$('.popovered').removeClass("popovered");
		})
		.on('shown.bs.popover', function () {
			var self = $(this);
			self.addClass("popovered");
			$('.popover-content button:even').addClass("btn btn-sm btn-mf-primary");
			$('.popover-content button:odd').addClass("btn btn-sm btn-mf-default");
//			$('.popover-content button').css({"border":"1px solid #ccc","background-color":"#fff","border-radius":"3px"});
//			$('.popover-content button:even').css({"background-color":"#732808","color":"#fff","border":"#732808"});
		});
		$('body').not('.operation-revoke').click(function(){
			$('.popovered').popover('hide');
			$('.popovered').removeClass("popovered");
		});
	}
	$('.status-operate ').on('click','.operation-edit',function(){
		$('#finance-product').hide();
		$('#main-tab').addClass("tab-details");
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
		window.location.href='common_detail.html?role='+role+'&status='+status+'&title='+title+'&sub_title='+subtitle;
	});
/**************************************financing apply*********************************************/	
	//datepicker initail
	$("#latest-money").datepicker({
        format: 'yyyy-mm-dd',
        weekStart: 1,
        autoclose: true,
        todayBtn: false,
        language: 'zh-CN'
    }).on("changeDate",function(ev){
    });
	//add btn and operation in add content
	function addProtepy( name ){
		var btnname = "add-"+name+"-btn";
		var boxname = "add-"+name+"-box";
		var contentname = "add-"+name+"-content";
		$('.'+contentname).hide();
		$('.'+btnname).click(function(){
			var node = $('.'+boxname+' .'+contentname+':first-child').clone().show()
			.on('click','.add-close-btn', function(){node.hide();})
			.on('change','.house-common',function(){
				if(this.value == 'common'){
					node.find('.house-co-owner').show();
				}else if(this.value == 'alone'){
					node.find('.house-co-owner').hide();
				}
			});
			$('.'+boxname).append( node );
			node.find('.house-co-owner').hide();
			node.find('.selectpicker ').select2();
		});
	};
	addProtepy( "car" );
	addProtepy( "house" );
	addProtepy( "movable" );
	addProtepy( "natural" );
	addProtepy( "legal" );
	
	//yes or no in assets liabilities
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
	
	//guarantee org select
	$('#org-choose').select2({
		minimumInputLength: 0,
		placeholder: "选择一个担保机构",
		width:'100%',
		tags:["担保机构A", "担保机构B", "担保机构C", "担保机构A", "担保机构B", "担保机构C", "担保机构A", "担保机构B", "担保机构C"]
	});
	
	//finance apply click
	$('#btn-finance-apply').on('click',function(){
		$('#finance-product').hide();
		$('#main-tab').removeClass("tab-details");
		$('#main-tab li a').attr("data-toggle","");
		var tags = 0;
		$('.savestep').hide().on('click',function(){
			window.location.reload();
		});
		$('.submitstep').hide().on('click',function(){
			window.location.reload();
		});
		$('.nextstep').show().on('click',function(){
			if(tags<5){tags++;}
			switch(tags){
			case 1: $('#main-tab a[href="#counter-guarantee"]').tab('show');break;
			case 2: $('#main-tab a[href="#assets-liabilities"]').tab('show');break;
			case 3: $('#main-tab a[href="#data-bank"]').tab('show');break;
			case 4: $('#main-tab a[href="#remark"]').tab('show');
				$('.savestep').show();
				$('.submitstep').show();
				$('.nextstep').hide();
				break;
			}
		});
		$('.laststep').show().on('click',function(){
			if(tags>0){tags--;}
			switch(tags){
			case 0: $('#main-tab a[href="#margin-cost"]').tab('show');break;
			case 1: $('#main-tab a[href="#counter-guarantee"]').tab('show');break;
			case 2: $('#main-tab a[href="#assets-liabilities"]').tab('show');break;
			case 3: $('#main-tab a[href="#data-bank"]').tab('show');
			$('.savestep').hide();
			$('.submitstep').hide();
			$('.nextstep').show();
				break;
			}
		});
		$('#finance-apply').show("1000");
	});
	$('#btn-backtoproduct').on('click',function(){
		$('#finance-apply').hide("1000",function(){$('#finance-product').show()});
	});
});
