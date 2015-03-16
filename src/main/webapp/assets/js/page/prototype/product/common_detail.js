require(['jquery',
         'bootstrap',
         'select2',
         'requirejs/domready!'], 
function($){
	/*var titles = new Array('风险管理','会员管理','我的债务','系统管理','交易管理','我的账户');
	var subTitles = new Array();
	subTitles[0] = new Array('项目审核','风险评级','保证金管理','放款审批','融资项目查询');
	subTitles[1] = new Array('融资申请意向选择','项目审核');
	subTitles[2] = new Array('融资项目管理','融资包管理');
	subTitles[3] = new Array('手工还款','融资包异常处理');
	subTitles[4] = new Array('融资项目查询');
	subTitles[5] = new Array('我的债权','交易明细');
	
	 //获取url中的参数
    function getUrlParam(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
        var r = window.location.search.substr(1).match(reg);  //匹配目标参数
        if (r != null) return unescape(r[2]); return null; //返回参数值
    }
    
	function getParam(param){
		var patt = window.location.search;
		var urlId = patt.indexOf(param);
		var paramId = patt.substring(urlId + param.length + 1, urlId + param.length + 2);
		return paramId;
	}
	function checkRole(){
		var role = getUrlParam("role");
		switch (role){
			case "5":
				$("#account").attr("disabled","disabled");
				break;
			case "6":
				break;
			default:
				break;
		}
	}
	
	function checkStatus(){
		var status = getUrlParam("status");
		var pTitle = getUrlParam("title");
		var pSubTitle = getUrlParam("sub_title");
		var title = $(".right-blk-head-title");
		var subTitle = $(".right-blk-head-subtitle");
		title.html(titles[pTitle-1]);
		subTitle.html(subTitles[pTitle-1][pSubTitle-1]);
		switch (status){
			case "1":
				$(".status-1").removeClass("status-1");
				break;
			case "2":
				$(".status-2").removeClass("status-2");
				break;
			case "3":
				$(".status-3").removeClass("status-3");
				$(".status-3-4").removeClass("status-3-4");
				break;
			case "4":
				$(".status-4").removeClass("status-4");
				$(".status-3-4").removeClass("status-3-4");
				$("#account").attr("disabled","disabled");
				break;
			case "5":
				$("#name").html("刘**");
				$("#phone").html("***********");
				$(".status-5").removeClass("status-5");
				break;
			case "6":
				$(".status-4").removeClass("status-4");
				$(".status-3-4").removeClass("status-3-4");
				break;
			case "7":
				$(".status-6").removeClass("status-6");
				$(".status-3-4").removeClass("status-3-4");
				$(".modal-title").html("确认放款");
				$(".modal-body").html("是否确认对该融资包进行放款？");
				break;
			default:
				break;
		}
	}
	
	//checkRole();
	checkStatus();
/////////////////___JS___////////////////////
	function getTitle(){
		var pTitle = getUrlParam("title");
		var pSubTitle = getUrlParam("sub_title");
		var title = $(".right-blk-head-title");
		var subTitle = $(".right-blk-head-subtitle");
		title.html(titles[pTitle]);
		subTitle.html(subTitles[pTitle][pSubTitle]);
	}
	function checkStatus(status){
		switch (status) {
		case "6":
			$(".status-guarantee").removeClass("status-guarantee");
			break;
		case "7":
		case "8":
			$(".status-risk").removeClass("status-risk");
			$(".status-guarantee").removeClass("status-guarantee");
			break;
		case "9":
		case "10":
		case "11":
		case "12":
		case "13":
		case "14":
		case "15":
		case "16":
		case "17":
		case "18":
		case "19":
		case "20":
			$(".status-risk").removeClass("status-risk");
			$(".status-guarantee").removeClass("status-guarantee");
			$(".status-trading").removeClass("status-trading");
			break;
		default:
			break;
		}
	}
	function checkRole(){
		var role = getUrlParam("role");
		var status = getUrlParam("status");
		checkStatus(status);
		switch (role) {
		case "2"://担保机构
			switch(status){
			case "1":
				$("#name").html("**");
				$("#phone").html("***********");
				$(".status-intent").removeClass("status-intent");
				break;
			case "4":
				$(".status-audit-agency").removeClass("status-audit-agency");
				break;
			default:
				break;
			}
			break;
		case "3"://风控人员
			switch(status){
			case "5":
				$(".status-audit-platform").removeClass("status-audit-platform");
				break;
			case "6":
				if(getUrlParam("sub_title") == '1'){
					$(".status-ratings").removeClass("status-ratings");
				}
				break;
			case "7":
				if(getUrlParam("sub_title") == '2'){
					$(".status-freeze").removeClass("status-freeze");
					$("#account").attr("disabled","disabled");
				}
				break;
			case "12":
				var operate = getUrlParam("operate");
				switch(operate){
				case '2':
					$(".status-loan").removeClass("status-loan");
					$(".modal-title").html("放款确认");
					$(".modal-body").html("是否确认对该融资包进行放款？");
					break;
				default:
					break;
				}
				break;
			default:
				break;
			}
			break;
		case "4"://风控经理
			switch(status){
			case "5":
				$(".status-audit-platform").removeClass("status-audit-platform");
				break;
			case "6":
				$(".status-ratings").removeClass("status-ratings");
				break;
			case "7":
				$(".status-freeze").removeClass("status-freeze");
				break;
			case "12":
				var operate = getUrlParam("operate");
				switch(operate){
				case '2':
					$(".status-loan").removeClass("status-loan");
					$(".modal-title").html("放款确认");
					$(".modal-body").html("是否确认对该融资包进行放款？");
					break;
				default:
					break;
				}
				break;
			default:
				break;
			}
			break;
		case "7"://系统管理员
			var operate = getUrlParam("operate");
			switch(status){
			case "10":
			case "11":
				switch(operate){
				case "0":
					$(".status-cancellation").removeClass("status-cancellation");
					$(".modal-title").html("撤单确认");
					$(".modal-body").html("是否确认对该融资包进行撤单？");
					break;
				case "1":
					$(".status-terminate").removeClass("status-terminate");
					$(".modal-title").html("终止确认");
					$(".modal-body").html("是否确认对该融资包进行终止？");
					break;
				default:
					break;
				}
				break;
			case "14":
				switch(operate){
				case "3":
					$(".status-repayment").removeClass("status-repayment");
					$(".modal-title").html("还款确认");
					$(".modal-body").html("是否确认对该融资包进行还款？");
					break;
				default:
					break;
				}
				break;
			default:
				break;
			}
		default:
			break;
		}
	}
	
	getTitle();
	checkRole();
////////////////////////////////////////
*/	
	$('#myTab a').click(function (e) {
		  e.preventDefault();
		  $(this).tab('show');
		});
		
	$('#cancleSure').click(function(){$('#myModal-sure').modal('hide');});
	$('#cancleReject').click(function(){$('#myModal-reject').modal('hide');});
	
	$("#panel-heading").click(function(){
		$("#collapseOne").collapse('toggle');
		if($("#fa-icon").hasClass("fa-caret-down")){
			$("#fa-icon").removeClass("fa-caret-down").addClass("fa-caret-up");
		}
		else{
			$("#fa-icon").removeClass("fa-caret-up").addClass("fa-caret-down");
		}
	});
	
});
