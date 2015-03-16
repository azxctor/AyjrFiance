require(['jquery',
         'global',
         'module/util',
         'module/datatables',
         'module/bread_crumb',
         'jquery.ui',
         'module/ajax',
         'bootstrap',
         'bootstrap-datepicker',
         'requirejs/domready!'], 
function($, global, util,datatables,mfBread){
	var PENDDING_STATUS = 'P';
	var service_table = null;
	/**
	 * Define datatables
	 */
	var _fnServiceRenderbtn = function(data,type,full){
		return '<div class="btn-group btn-group-xs"><a data-id="'+full.userId+'" class="btn btn-mf-primary go-details check-link table-link" type="button"><i title="详情" class="fa fa-eye fa-lg"></i></a></div>';
	};
	
	var _fnRenderlog = function(data,type,full){
		return '<a  data-id="'+full.userId+'" class="log-link table-link" data-type="ind" data-toggle="modal" data-target="#logModal">日志</a>';
	};
	var serviceTable = function(){
		var options = {};
		options.tableId = '#serviceTable';
		options.aaSorting = [[5,"desc"]];
		options.sAjaxSource = global.context+"/web/audit/getservicecenterapp";
		options.functionList={"_fnServiceRenderbtn":_fnServiceRenderbtn,"_fnRenderlog":_fnRenderlog};
		service_table = datatables.init(options);
		
		service_table.setParams(util.getSearchData("#search-area"));
		return service_table.create();
	};
	
	/**
	 * Initial datatables
	 */
	var service=serviceTable();
	
	/**
	 * Define search event
	 */
	$('#audit_service_search').click(function(){
		service_table.setParams(util.getSearchData('#search-area'));
		service_table.invoke("fnDraw");
	});
	
	/**
	 * Get details info for specific user
	 */
	var getApplicationDetails = function(userId) {
		$.ajax({
			url : global.context+'/web/audit/servicecenterapp/'+userId,
			type : 'GET',
			contentType:'application/json;charset=utf-8',
			async : false,
			success : function(response,data) {
				var parseJSON=$.parseJSON(response);
				var tabId="#service-wrapper";
				$(tabId+' span').each(function(){
					var value="";
					var attrName=$(this).attr('data-prop');
					try{
						value=eval("parseJSON."+attrName);
					}catch(e){
						console.log('');
					}
					if(attrName){
						$(this).text(value||"");
						$(this).attr("title",value||"");
					}
				});
				
				$(tabId+' img').each(function(){
					var value="";
					var attrName=$(this).attr('data-prop');
					try{
						value=eval("parseJSON."+attrName);
					}catch(e){
						console.log('');
					}
                    if(attrName&&value){
                        if(util.is_pdf(value)) {
                            $(this).attr('src',global.context+"/assets/img/pdf.jpg");
                            $(this).attr('data-pdfpath',value||"");
                        }
                        else {
                            $(this).removeAttr("data-pdfpath");
                            $(this).attr('src',value||"");
                        }
                    }
				});
				
				var memberLevelList = parseJSON["memberLevelList"];
				initMemberLevelSelect(memberLevelList);
				
				var serviceLevel = parseJSON["level"];
				if(serviceLevel){
					$('#service-level').find("option[value=" + serviceLevel.code +"]").attr("selected",true).show();
					$('#service-level-span').text(serviceLevel.text).hide();
				}
				$("#hidden_app_id").val(parseJSON['appId']);
				$("#hidden_user_id").val(parseJSON['userId']);
				var status = parseJSON['serviceCenterStatus'];
				if(status && status.code!=PENDDING_STATUS){
					$('#service-center-audit').hide();
					$('#service-level').hide();
					$('#service-level-span').show();
				}
				
			}
		});
	};
	/**
	 * Initial member level select
	 */
	var initMemberLevelSelect = function(memberLevelList){
		var option_html="";
		for(var i=0;i<memberLevelList.length;i++){
			option_html += "<option value="+memberLevelList[i].code+">"+memberLevelList[i].text+"</option>";
		}
		$('#service-level').html(option_html);
	};
	$("body").on("click","#serviceTable a.check-link",function(){
			$("#service-wrapper").show("slide");
			$("#table-wrapper").hide();
			$("#service-center-audit").show();
			var userId = $(this).attr("data-id");
			getApplicationDetails(userId);
			mfBread.push({
				label:"详情"
			});
	});
	
	$(".right-blk-head").on("click","li:eq(1) a",function(){
		$(".form-wrapper").hide();
		$("#table-wrapper").show("slide");
		$("#service-center-audit").hide();
		service_table.invoke("fnStandingRedraw");
		mfBread.pop();
	});
	
	/*$(".go-back").on("click",function(e){
		$(".form-wrapper").hide();
		$("#table-wrapper").show("slide");
		$("#service-center-audit").hide();
		service_table.invoke("fnStandingRedraw");
	});*/
	$('#service-center-audit').click(function(){
		$('#hidden_user_level').val($('#service-level').val());
	});
	
	$("#start-time").datepicker({
		 autoclose: true,
		 format: "yyyy-mm-dd"
	});
	
	$("#end-time").datepicker({
		 autoclose: true,
		 format: "yyyy-mm-dd"
	});
	
	$("#search-area").on("keydown",function(e){
		if(e.which == 13){
			$("#audit_service_search").trigger("click");
			return false;
		}
	});
	
	util.bind_popoverx();
});
