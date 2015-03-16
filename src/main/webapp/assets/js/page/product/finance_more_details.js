require(['jquery',
         'module/datatables',
         'bootstrap',
         'requirejs/domready!'], 
function($,datatables){
	
	$("#panel-heading").click(function(){
		$("#collapse-more-detail").collapse('toggle');
		if($("#fa-icon").hasClass("fa-caret-down")){
			$("#fa-icon").removeClass("fa-caret-down").addClass("fa-caret-up");
		}
		else{
			$("#fa-icon").removeClass("fa-caret-up").addClass("fa-caret-down");
		}
	});
		
	//popup
	function popupTip(select,content){
		$('#'+select).popover({
			trigger:'hover',
			html:true,
			placement:'right',
			selector:true,
			container: 'body',
			content:content
		});
	}
	
	//融资服务合同保证金
	popupTip("service-margin-tip","<div class='popovers'>本金额为融资（人）企业在融资时需要缴纳的合同保证金，该金额为参考金额，以实际冻结金额为准。该保证金会在融资项目审核通过后按照实际金额冻结！融资成功签约，放款后解冻！如在申购过程中出现撤单情况则不会返还融资人（企业）！<br/>计算规则：一个月利息，即融资申请额*年利率/12</div>");

	//融资会员席位费
	popupTip("seat-fees-tip","<div class='popovers'>本金额为融资（人）企业注册成为我公司融资会员时需要缴纳的席位费用，本金额为参考金额，实际扣取金额与该金额可能会有小幅度偏差，该费用在本年度首次放款时由系统扣取，一年一缴。</div>");

	//风险管理费
	popupTip("risk-fees-tip","<div class='popovers'>本金额为融资（人）企业需要缴纳给我公司的风险管理费用，本金额为参考金额，实际扣取金额与该金额可能会有小幅度偏差，该费用在放款时由系统扣取！<br/>计算规则：融资申请额*"+ $("#risk-fee-rate").text() +"% *期限值，对以日计息融资项目，比例需要除以30</div>");

	//融资服务费
	popupTip("financing-charges-tip","<div class='popovers'>本金额为融资（人）企业需要缴纳给我公司的服务费用，本金额为参考金额，实际扣取金额与该金额可能会有小幅度偏差，该费用在放款时由系统扣取！<br/>计算规则： 融资申请额*"+ $("#service-fee-rate").text() +"%* 期限值，对以日计息融资项目，比例需要除以30</div>");
	
	//借款合同履约保证金
	popupTip("contract-tip","<div class='popovers'>该金额为参考金额，以实际冻结金额为准。该保证金会在平台放款审批通过后按照实际金额冻结！如在最后一期还款时会解冻并扣取该保证金！<br/>计算规则：融资申请额*"+ $("#contract-fee-per").text() +"% </div>");
	
	//担保公司还款保证金
	popupTip("repayment-fees-tip","<div class='popovers'>该金额为参考金额，以实际冻结金额为准。该保证金会在平台放款审批通过后按照实际金额冻结！融资包撤单后解冻！如在最后一期还款时有违约情况需要担保机构代偿，则会按实际违约金额扣取该保证金！<br/>计算规则：融资申请额*"+ $("#warrant-fee-per").text() +"% </div>");
	
	 $('#table-files').dataTable( {
		  "bInfo": false,//页脚信息
		  "bPaginate": false, //开关，是否显示分页器  
		  "bFilter": false, //开关，是否启用客户端过滤器  
		  "bAutoWith": true,  
		  "bSort": false, //开关，是否启用各列具有按列排序的功能 
	  } );
});