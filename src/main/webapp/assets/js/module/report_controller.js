/*
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * The left menu module for the main frame.
 *
 * Author: Tao Shi
 */
;require(['jquery',
         'global',
        'module/util',
        'module/report',
        'module/view_pusher',
        'packet_manage',
        'underscore'
        ],
function($, global, util, MFReport,pusher,packet){
	
	var MFReportController = function(){
		
	};
	
	MFReportController.prototype = {
		
		_ajax:function(dto){
			return $.ajax({
				url: dto.url,
				dataType:"json",
				data: JSON.stringify(dto.param),
                type:'POST',
                contentType:'application/json;charset=utf-8'
			});
		},
		detail: function(event,data){

			switch(event){

				//公共事件：融资包详情
				case'common.packet.detail':
					packet.getDetails(data,"view");
				break;			
				case'common.packet.detail.right':
					packet.getDetails(data,"view","#detail-wrapper");
					break;

				case 'ins.payback.detail':
                    var dto = {};
                    dto.url = global.context+"/web/report/getrepaymentdetail";
                    dto.param = {"commonId":data,"exportFileExt":"pdf"};
                    this._ajax(dto).done(function(resp){
                        //临时使用该地址，上线前需更换
                     //   var temp_iframe_url = "http://vdevwincnhz0050.hengtiansoft.com:55000/Report/RepaymentDetail.aspx?key=";
                        $.popupWindow(resp.parameterString,{"width":"1224","height":"700"});
                    });
					break;
                //债权明细
				case 'obligations.detail':
                    var dto = {};
                    dto.url = global.context+"/web/report/getcreditordetailbylotid";
                    dto.param = {"commonId":data,"exportFileExt":"xls"};
                    this._ajax(dto).done(function(resp){
                        //临时使用该地址，上线前需更换
                     //   var temp_iframe_url = "http://vdevwincnhz0050.hengtiansoft.com:55000/Report/RepaymentDetail.aspx?key=";
                        $.popupWindow(resp.parameterString,{"width":"1224","height":"700"});
                    });
					break;
				//回款情况
				case 'payback.detail':
                	$(".detail-id-wrapper").empty().append(data);
                	 pusher.push({
                         url:global.context + "/web/report/paymentdetailpage",
                         type : 'GET',
                         element:"#detail-wrapper",
                         title:"回款情况 查看明细",
                         onHide:function(){
                         	
                         }
                     });
					break;
				//申购情况
				case 'subscribe.detail':
					var id = $(".id-wrapper").text();
                    var dto = {};
                    dto.url = global.context+"/web/report/getsubscribedetail";
                    dto.param = {"commonId":data,"keyWord":id,"exportFileExt":"pdf"};
                    this._ajax(dto).done(function(resp){
                        $.popupWindow(resp.parameterString,{"width":"1224","height":"700"});
                    });
					break;	

					
				case 'income.detail'://融资包 收益表
                    var dto = {};
                    dto.url = global.context+"/web/report/getreceiptsbypackageId";
                    dto.param = {"commonId":data};
                    this._ajax(dto).done(function(resp){
                        $.popupWindow(resp.parameterString,{"width":"1224","height":"700"});
                    });
					break;

                case 'trace'://追踪
                	var id = data;
                	$(".id-wrapper").empty().append(id);
                    pusher.push({
                        url:global.context + "/web/report/creditorpage",
                        type : 'GET',
                        element:"#main-wrapper",
                        title:"跟踪信息详情",
                        onHide:function(){
                        	
                        }
                    });
                    break;
                    
                case 'repayment.schedule': //融资包  按期还款表
                	var id = data;
                	$(".id-wrapper").empty().append(id);
                	pusher.push({
                        url:global.context + "/web/report/repaymentpage",
                        type : 'GET',
                        element:"#main-wrapper",
                        title:"还款表",
                        onHide:function(){
                        	
                        }
                    });
                	break;
                case 'volume.detail'://交易量统计
                	var id = data;
                	$(".id-wrapper").empty().append(id);
                    pusher.push({
                        url:global.context + "/web/report/tradestatisticspage",
                        type : 'GET',
                        element:"#main-wrapper",
                        title:"交易量统计表",
                        onHide:function(){
                        	
                        }
                    });
                	break;
                case 'payment.detail'://还款统计
                	var id = data;
                	$(".id-wrapper").empty().append(id);
                    pusher.push({
                        url:global.context + "/web/report/paymentstatisticspage",
                        type : 'GET',
                        element:"#main-wrapper",
                        title:"还款统计表",
                        onHide:function(){
                        	
                        }
                    });
                	break;
                case 'activity.detail'://投资会员活跃度统计
                	var id = data;
                	$(".id-wrapper").empty().append(id);
                    pusher.push({
                        url:global.context + "/web/report/investoractivitypage",
                        type : 'GET',
                        element:"#main-wrapper",
                        title:"投资会员活跃度统计",
                        onHide:function(){
                        	
                        }
                    });
                	break;
                case 'contract.template'://查看合同模板
                    var dto = {};
                    dto.url = global.context+"/web/report/getcontracttemplate";
                    dto.param = {"commonId":data,"exportFileExt":"pdf"};
                    this._ajax(dto).done(function(resp){
                        $.popupWindow(resp.parameterString,{"width":"1224","height":"700"});
                    });
					break;
                case 'member.contract'://会员投资统计查看合同
                    var dto = {};
                    dto.url = global.context+"/web/report/getcontract";
                    dto.param = {"commonId":data.contractId,"productId":data.productId,"exportFileExt":"pdf"};
                    this._ajax(dto).done(function(resp){
                        $.popupWindow(_.unescape(resp.parameterString),{"width":"1224","height":"700"});
                    });
                    break;
                case 'transaction.detail'://交易手续费 清单列表（系统管理员）
                	var dto = {};
                    dto.url = global.context+"/web/report/getauthorizationdetail";
                    dto.param = {"commonId":data,"startDate":$("#start-time").text(),"endDate":$("#end-time").text(),"exportFileExt":"xls,pdf"};
                    this._ajax(dto).done(function(resp){
                        $.popupWindow(resp.parameterString,{"width":"1224","height":"700"});
                    });
                	break;
                case 'investment.detail'://会员投资统计 投资人查看详情
                	$(".id-wrapper").empty().append(data);
                    pusher.push({
                        url:global.context + "/web/report/memberstatisticsinvestorpage",
                        type : 'GET',
                        element:"#main-wrapper",
                        title:"按投资人查看详情",
                        onHide:function(){
                        	
                        }
                    });
                	break;
                case 'packet.detail'://会员投资统计 融资包查看详情
                	$(".id-wrapper").empty().append(data);
                    pusher.push({
                        url:global.context + "/web/report/memberstatisticspackagepage",
                        type : 'GET',
                        element:"#main-wrapper",
                        title:"按融资包查看详情",
                        onHide:function(){
                        	
                        }
                    });
                	break;
                //信息服务费 详细账单
                case 'servicefee.detail':
                    $(".id-wrapper").empty().append(data);
                	pusher.push({
            	  		url: global.context+"/web/report/servicefeedetailpage",
            	  		type : 'GET',
            	  		element:"#main-wrapper",
            	  		title:"信息服务费 详细账单",
            	  		 onHide:function(){
                         	
                         }
            	  	});
                	break;	
                	   //月度理财报告
                case 'monthreport.detail':
                    $(".id-wrapper").empty().append(data);
                    $(".modal").modal();
                	break;		
                	
                case 'contract'://电子合同汇总
                	var dto = {};
                    dto.url = global.context+"/web/report/geteletroniccontract";
                    dto.param = {"commonId":data};
                    this._ajax(dto).done(function(resp){
                        $.popupWindow(resp.parameterString,{"width":"1224","height":"700"});
                    });
                    break;
                //会员基本信息查询
                case 'memberbasicinfo.detail':
                	var dto = {};
                    dto.url = global.context+"/web/report/getmemberInfodetail";
                    dto.param = {"commonId":data,"exportFileExt":"pdf"};
                    this._ajax(dto).done(function(resp){
                        $.popupWindow(resp.parameterString,{"width":"1224","height":"700"});
                    });
                	break;
                //债权转让费用-协议
                case 'protocol.detail':
                	var dto = {};
                	dto.url = global.context+"/web/report/getcredittransferfeecontract";
                	dto.param = {"commonId":data};
                	this._ajax(dto).done(function(resp){
                		$.popupWindow(resp.parameterString,{"width":"1224","height":"700"});
                	});
                	break;
                //债权转让费用-合同
                case 'contract.detail':
                	var dto = {};
                	dto.url = global.context+"/web/report/getcontract";
                	dto.param = {"commonId":data};
                	this._ajax(dto).done(function(resp){
                		$.popupWindow(_.unescape(resp.parameterString),{"width":"1224","height":"700"});
                	});
                	break;
                //债权转让协议
                case 'debtaggrement.detail':
                	var dto = {};
                	dto.url = global.context+"/web/report/getcredittransfercontract";
                	dto.param = {"commonId":data};
                	this._ajax(dto).done(function(resp){
                		$.popupWindow(resp.parameterString,{"width":"1224","height":"700"});
                	});
                	break;
                //债权转让详情
                case 'debtassign.detail':
                	var dto = {};
                	dto.url = global.context+"/web/report/getcredittransferdetail";
                	dto.param = {"commonId":data};
                	this._ajax(dto).done(function(resp){
                		$.popupWindow(resp.parameterString,{"width":"1224","height":"700"});
                	});
                	break;                	
				default:
					break;
			}
			
		},
		
		loadingMask:function(){
			var threadCount=0;
			$.ajaxSetup({
		    	cache : false,
		    	beforeSend:function(xhr, settings){
	    			++threadCount;
	    			$(".km-loading-mask").show();
	    			$("*:focus").blur();
		    	},
		    	complete:function(xhr){
	    			setTimeout(function(){
	    				if(--threadCount){
	    	    			$(".km-loading-mask").show();
	    	    		}
	    	    		else{
	    	    			$(".km-loading-mask").hide();
	    	    		}
	        		},500);
		    	}
		    });
		},
		
		resetIframeHeight:function(){
			var mainheight = $("iframe").contents().find("body").height()+150;
			$("iframe").height(mainheight);
		},
		
		pdf: function(){
			
		}
			
	};
	
	window.reportController = new MFReportController();
	
});