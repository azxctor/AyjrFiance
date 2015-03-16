/*!
 *
 * 融资包管理 - 平台查看
 *  创建人：陈恩明
 * 	创建日期：2014-05-15
 *
 */
require(['packet_manage',
         'global',
         'jquery.popupwindow',
         'jquery.printarea',
         'module/report_controller'],
    function(packet,global){
        'use strict';
        var mainTable="";
        //融资包汇总数据
		var summaryData="";
        //返回操作按钮
        var returnOperBtn = function(data,type,full){
            var btnHtml = '<div class="btn-group">';
            var status = full.status.code;
            var canPrint = status == "WL"||status == "LC"||status == "PN"||status == "E" ? true:false;
            if(full.canViewRepayTable){
            	btnHtml+=packet.renderTableBtn("还款表","btn-paylist","fa fa-calendar-o",full.id);
            }
            if(full.canPrintDebtInfo && canPrint){
            	btnHtml+=packet.renderTableBtn("打印","btn-print-signed","fa fa-print",full.id);
            }
            btnHtml+='</div>';
            return btnHtml;
        };

        //返回详情
        var returnDetail = function(data,type,full){
            var detailLink = '<a class="detail-link" href="javascript:void(0);" data-id="'+full.id+'">'+full.packageName+'</a>';
            return detailLink;
        };
        
        //新增
        //融资包汇总数据
		var getPkgSumInfo = function(){
			return $.ajax({
				url:global.context+"/web/financingpackag/pkg_sum",
				type: "POST",
				dataType: 'json',
				contentType: 'application/json; charset=UTF-8',
				data:JSON.stringify({"startDate":$("#search-start-date").val(),"endDate":$("#search-end-date").val(),"endStartDate":$("#search-end-start-date").val(),"endEndDate":$("#search-end-end-date").val(),"packageStatus":$("#operation-select").val(),"keyword":$("#keyword").val()})
			});
		};
		
		//融资包汇总数据初始函数
		var formatFoot = function(tableId,resp){
			$.each(resp,function(key){
				$(tableId).find("tfoot td").each(function(){
					var mData = $(this).attr("data-mData");
					if(key == mData){
						var footData = "";
						if(key !='count'){
							footData = numeral(resp[key]).format('0,0.00');
						}else{
							footData = resp[key]+"笔";
						}
						if(key=='sum_quota'||key=='sum_invstr'||key=='count'){
							$(this).text("");
							$(this).text(footData);
						}
						return false;
					}
				});
			});
		};
        //新增
        
        //初始化
        var init = function(){
            packet.initHoverMsg("platform");
            var options = {};
            options.tableId = '#packet-manage-table';
            options.sAjaxSource = global.context+"/web/financingpackage/platform/getpackagelist";
            options.aaSorting = [[0,"desc"]];
            options.rawOptions = {
                "scrollX": true
            };
            options.functionList={"btn_oper":returnOperBtn,"hover_msg":packet.returnHoverMsg,"btn_log":packet.returnLogBtn,"btn_detail":returnDetail};
            mainTable = packet.renderTable(options);
            packet.init();
            packet.bindDatePickerByClass(".search-end-start-date",".search-end-end-date");
            packet.initLog();
        };
        
        //绑定事件
        var bindEvent = function(){
            //table按钮事件绑定
            //终止
            $("#table-wrapper")           //查看还款表
	            .on("click",".btn-paylist",function(){
	            	var pkgId = $(this).attr("data-id");
	            	window.reportController.detail("repayment.schedule",pkgId);
	            }) 
	            .on("click",".detail-link",function(){
	                    var packageId = $(this).attr("data-id");
	                    packet.getDetails(packageId,"view");
	                    $("#main-wrapper").hide();
	                    $("#details-wrapper").show("slide");
	            })
	            .on("click","#hidden_span",function(){//刷新表
                    mainTable.invoke("fnStandingRedraw");
                });
        };
        
        //modal 绑定
        $("body").on("click",".btn-modal-agree",function(e){
			var $modal=$(e.currentTarget).parents(".modal");
			 if($modal.attr("id")=="modal-signed-iou-print"){
				$modal.data("bs.modal").hide();
				$modal.find("#print-iou-content").printArea({popClose:true});  
			}
        });
        
        init();
        bindEvent();
        
        //融资包汇总数据操作执行
        getPkgSumInfo().done(function(resp){
			summaryData = resp;
			formatFoot("#packet-manage-table",summaryData);
		});
        
        //点击查询按钮，融资包汇总数据操作执行
		$("#btn-search").on("click",function(){
			getPkgSumInfo().done(function(resp){
				summaryData = resp;
				formatFoot("#packet-manage-table",summaryData);
			});
		});
    });


