/*!
 * 	
 *  融资包管理 - 风控部-放款审批
 *  创建人：陈恩明
 * 	创建日期：2014-05-13
 *
 */
require(['packet_manage','global'],
    function(packet,global){
        'use strict';
        var mainTable = null;
        //返回操作按钮
        var returnOperBtn = function(data,type,full){
        	var btnHtml = '<div class="btn-group">';
        	if(full.canLoanApprove){
                btnHtml += packet.renderTableBtn("放款审批","btn-loan","fa fa-check",full.id);
        	//	btnHtml += '<button class="btn btn-xs btn-loan" data-id="'+full.id+'"><a href="javascript:void(0);" title="放款审批"><i class="fa fa-stop" >放款审批</i></a></button></div>';
        	}
            btnHtml+='</div>';
            return btnHtml;
        };
        
        var _fnCheckBox = function(data,type,full){ 
			return '<input type="checkbox" name="ids-checkList" value="'+data.id+'" data11="'+data.financierName+'" data22="'+data.supplyAmount+'">';
		}; 

		var selectAll=function(element,oTable){
			if ($(element).is(':checked')){
				oTable.find('input[type="checkbox"]').each(function(){
					if (!$(this).is(':checked')){
						$(this).prop('checked', 'checked');
					}
				});
			}else {
				oTable.find('input[type="checkbox"]').each(function(){
					if ($(this).is(':checked')){
						$(this).removeAttr('checked');
					}
				});
			}
		};
		var _ajax=function(url, type, data){ 
			return $.ajax({
				url: url,
				type: type || 'GET',
				data: JSON.stringify(data),
				dataType: 'json',
				contentType: 'application/json;charset=utf-8'
			});
		};

		var wlac_apple_All=function(){ 
			var ids = [];
			$('table').find('input[name="ids-checkList"]:checked').each(function(){
				var id = $(this).val();
				ids.push(id);
			});
			var param = {'idList':ids};
			if (ids && ids.length > 0){
				//确认框
				
				var urlStr=global.context+"/web/financingpackage/batchLoanapprove";
				_ajax(urlStr, 'POST', param).done(function(response){
					if (response != null){
						mainTable.invoke('fnStandingRedraw'); 
					} 
					var text = '批量放款审核成功!';
					if (!response){
						text = '批量放款审核失败!';
					}
					$.pnotify({
					    text: text,
					    type: 'success'
					});  
					
				});
			}
		};
        //初始化
        var init = function(){
            var options = {};
            options.tableId = '#packet-manage-table';
            options.sAjaxSource = global.context+"/web/financingpackage/approve/getpackagelist";
            options.aaSorting = [[1,"desc"]];
            options.functionList={"_fnCheckBox":_fnCheckBox,"btn_oper":returnOperBtn,"btn_detail":packet.renderDetails,"btn_log":packet.returnLogBtn};
            mainTable = packet.renderTable(options);
            packet.init();
            packet.initLog();
        };
        //绑定事件
        var bindEvent = function(){
            //table按钮事件绑定
            $("#table-wrapper").on("click",".btn-loan",function(){
            	 var packageId = $(this).attr("data-id");
                 packet.getDetails(packageId,"loanapprove");
                 packet.showDetails();
            })//融资包详情
            .on("click",".detail-link",function(){
                var packageId = $(this).attr("data-id");
                packet.getDetails(packageId,"view");
                packet.showDetails();
            }).on("click","#hidden_span",function(){//刷新表
                    mainTable.invoke("fnStandingRedraw");
            });
            
    		$('#wlac-selectAll').on('click', function(){
    			var oTable = $('#packet-manage-table');
    			selectAll(this, oTable);
    		});
    		
    		$('#wlac-apple-All').on('click', function(){
    			var ids = [];
    			var contn='';
    			$('table').find('input[name="ids-checkList"]:checked').each(function(){
    				var id = $(this).val();
    				var data11 = $(this).attr("data11");
    				var data22= $(this).attr("data22"); 
    				contn+='&nbsp;&nbsp;&nbsp;['+data11+'--'+id+'--'+numeral(data22).format('0,0.00')+']<br/>';
    				ids.push(id);
    			}); 
    			if (ids.length<1){
    				$("#title_manage_wind_loan").html('操作提示');
    				$("#content_manage_wind_loan").html('至少选择一条放款审批条目 !');
    				$("#btn_manage_wind_loan").hide();
    			}else{ 
    				$("#title_manage_wind_loan").html('确认提示');  
    				$("#content_manage_wind_loan").html('是否对以下'+ids.length+'个融资包:<br/>'+contn+'进行批量放款审核操作？');
    				$("#btn_manage_wind_loan").show();
    			}
    			
    			$("#modal-approve_manage_wind_loan").modal().show();
    		});
    		$('#btn_manage_wind_loan').on('click', function(){ 
    			wlac_apple_All();
    			$("#clsoe_manage_wind_loan").trigger('click');
    		});
    		
        };
        init();
        bindEvent();
    });