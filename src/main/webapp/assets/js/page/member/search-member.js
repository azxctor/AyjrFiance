require(['jquery',
         'global',
         'module/util',
         'module/datatables',
         'module/form',
         'module/bread_crumb',
         'jquery.ui',
         'module/ajax',
         'jquery.pnotify',  
         'bootstrap',
         'bootstrap-datepicker',
         'module/cascading_listener',
         'requirejs/domready!'], 
function($, global, util,datatables,upload,mfBread){
	var person_table = null;
	var org_table = null;
/*	function _fnSrhPerBtnRender(data,type,full){
		return '<div class="btn-group">'+
				'<a class="btn btn-silk check-link table-link" data-id="'+full.userId+'" data-type="org" title="详情"><i class="fa fa-eye"></i></a>'+
				'<a class="btn btn-silk log-link table-link"" data-type="ind" data-toggle="modal" data-target="#logModal"  title="日志"><i class="fa fa-book"></i></a>'+
				'</div>'; 
	}*/
	var _fnSrhPerBtnRender2 = function (data,type,full){
		return '<div class="btn-group btn-group-xs"><a data-id="'+full.userId+'" class="btn btn-mf-primary go-details check-link table-link" type="button"><i title="详情" class="fa fa-eye fa-lg"></i></a></div>';
	};
	
	var _fnRenderlog = function(data,type,full){
		return '<a  data-id="'+full.userId+'" class="log-link table-link" data-type="ind" data-toggle="modal" data-target="#logModal">日志</a>';
	};
	var tabFlag = 'P';
	var personalTable = function(){
		var options = {};
		/*var cols = [];
		cols.push({aTargets : [ '_all' ], "sDefaultContent" : "&nbsp;"},
				{"aTargets":[0],"mData":"userId"},
				{"aTargets":[1],"mData":"userRole.text","bSortable": false},
				{"aTargets":[2],"mData":"name","bSortable": false},
				{"aTargets":[3],"mData":"region.text"},
				{"aTargets":[4],"mData":"personJob","bSortable": false},
				{"aTargets":[5],"mData":"personEducation.text"},
				{"aTargets":[6],"mData":"investorStatus.text"},
				{"aTargets":[7],"mData":"financeStatus.text"},
				{"aTargets":[8],"mRender":_fnSrhBtnRender,"bSortable": false});*/
		options.tableId = '#personSearchTable';
		options.sAjaxSource = global.context+"/web/search/getmemberinfo";
	//	options.aoColumns = cols; 
		options.functionList={"_fnSrhPerBtnRender2":_fnSrhPerBtnRender2,"_fnRenderlog":_fnRenderlog};
		options.aaSorting=[[2,"desc"]];
		person_table = datatables.init(options);
		person_table.setParams(util.getSearchData('#search_area'));
		return person_table.create();
	};
	
	
	var _fnSrhOrgBtnRender = function(data,type,full){
		return '<div class="btn-group btn-group-xs"><a data-id="'+full.userId+'" class="btn btn-mf-primary go-details check-link table-link" type="button"><i title="详情" class="fa fa-eye fa-lg"></i></a></div>';

	};
	var organizationTable = function(){
		//Organization 
		var options = {};
		/*var cols = [];
		cols.push({aTargets : [ '_all' ], "sDefaultContent" : "&nbsp;"},
				{"aTargets":[0],"mData":"userId"},
				{"aTargets":[1],"mData":"userRole.text","bSortable": false},
				{"aTargets":[2],"mData":"name","bSortable": false},
				{"aTargets":[3],"mData":"region.text"},
				{"aTargets":[4],"mData":"orgIndustry.text","bSortable": false},
				{"aTargets":[5],"mData":"orgNature.text","bSortable": false},
				{"aTargets":[6],"mData":"orgType.text"},
				{"aTargets":[7],"mData":"investorStatus.text"},
				{"aTargets":[8],"mData":"financeStatus.text"},
				{"aTargets":[9],"mRender":function(data,type,full){
					return '<a href="javascript:void(0)" data-id="'+full.userId+'" class="check-link table-link"  data-type="org"><span class="label label-default">详情</span></a>&nbsp;<a data-id="'+full.userId+'" class="log-link table-link" data-type="ind" data-toggle="modal" data-target="#logModal" ><span class="label label-default">日志</span></a>';
					},"bSortable": false});*/
		options.tableId = '#orgSearchTable';
		options.sAjaxSource = global.context+"/web/search/getmemberinfo";
	//	options.aoColumns = cols; 
		options.functionList={"_fnSrhOrgBtnRender":_fnSrhOrgBtnRender,"_fnRenderlog":_fnRenderlog};
		options.aaSorting=[[2,"desc"]];
		org_table = datatables.init(options);
		org_table.setParams(util.getSearchData('#search_area'));
		return org_table.create();
	};
	
	var personal=personalTable();
	var organization;
	
	/**
	 * Define search event
	 */
	$('#member_search').click(function(){
		if(tabFlag=='P'){
			person_table.setParams(util.getSearchData("#search_area"));
			person_table.invoke("fnDraw");
		}
		if(tabFlag=='O'){
			if(organization){
				org_table.setParams(util.getSearchData("#search_area"));
				org_table.invoke("fnDraw");
			}
		}
	});
	
	
	$(".right-blk-head").on("click","li:eq(1) a",function(){
		$(".form-wrapper").hide();
		$("#table-wrapper").show("slide");
		$(".search-save-row").hide();
		$("#search-member-wrapper").hide();
		if(person_table){
			person_table.invoke("fnStandingRedraw");
		}
		if(org_table){
			org_table.invoke("fnStandingRedraw");
		}
		
		mfBread.pop();
	});
	
	
	/**
	 * 
	 */
	var getMemberDetails = function(userType,userId) {
		$.ajax({
			url : global.context+'/web/search/getmemberinfo/'+userId,
			type : 'GET',
			contentType:'application/json;charset=utf-8',
		//	async : false,
			data : {
				userType : userType
			},
			error:function(){
			},
			success : function(response) {
				if($("#person-bank-province").data("change_listener")){
					$("#person-bank-province").data("change_listener").destory();
				}
				if($("#org-bank-province").data("change_listener")){
					$("#org-bank-province").data("change_listener").destory();
				}
				$("#search-member-wrapper").html(response);
				
				
				
				util.bind_popoverx();
				
				$(".auth-center").select2({
					 placeholder: "请输入授权服务中心的区域或名称 ",
					 allowClear: true,
					formatNoMatches: function () { return "未找到匹配项"; }
				});
				//fix conflict between validate and select2
				$(".select2-focusser").attr("name","xx");
				
				$("#person-bank-province").listenChange({
					url:global.context+'/web/option/region/',
					nextElement:$("#person-bank-city"),
					hideNext:false,
					initialCallback:function(current,next){
						next.val(next.attr("data")).trigger("change");
					}
				});
				$("#org-bank-province").listenChange({
					url:global.context+'/web/option/region/',
					nextElement:$("#org-bank-city"),
					hideNext:false,
					initialCallback:function(current,next){
						next.val(next.attr("data")).trigger("change");
					}
				});
				upload.init();
				$(".file-upload").show();
				var popImg = $(".file-upload").attr('pop');
				$(".upload-filename").text(popImg);
				$(".file-upload").attr("data-img",popImg);
				/*$(".go-back").on("click",function(e){
					$(".form-wrapper").hide();
					$("#table-wrapper").show("slide");
					$(".search-save-row").hide();
					$("#search-member-wrapper").hide();
					if(person_table){
						person_table.invoke("fnStandingRedraw");
					}
					if(org_table){
						org_table.invoke("fnStandingRedraw");
					}
					
				});*/
				
				var attrName=$("#select_bankname").attr('data');
				$("#select_bankname").val(attrName);
				/*var attrName=$("#select_bank_province").attr('data');
				$("#select_bank_province").val(attrName);
				var attrName=$("#select_bank_city").attr('data');
				$("#select_bank_city").val(attrName);*/
				$("#person-bank-province").val($("#person-bank-province").attr("data")).trigger("change");
				$("#org-bank-province").val($("#org-bank-province").attr("data")).trigger("change");
				var attrName=$("#sleect_investorLevel").attr('data');
				$("#sleect_investorLevel").val(attrName);
				
			/*	var attrName=$("#sleect_financierLevel").attr('data');
				$("#sleect_financierLevel").val(attrName);*/
				
				var per_attrName=$("#select_authCenter").attr('data');
				 $("#select_authCenter").select2("val", per_attrName);
				
			/*	var org_attrName=$("#org_select_authCenter").attr('data');
				$("#org_select_authCenter").val(org_attrName);*/
				
				$(".search-edit-row_basic button").on("click",function(e){
					$("#bank_view").hide();
					$("#bank_edit").show();
					var newImg = $("#bank_view img").attr("src");
					$(".file-upload .upload-img").attr("src",newImg);
					$(".upload-thumbnail").show();
					
					$(".search-save-row_basic").show();
					$(".search-edit-row_basic").hide();
				});
				
				$(".search-edit-row_inve button").on("click",function(e){
					$("#agent_authcenter_view").hide();
					$("#agent_authcenter_edit").show();
					$("#investorLevel_view").hide();
					$("#investorLevel_edit").show();
					
					$(".search-save-row_inve").show();
					$(".search-edit-row_inve").hide();
				});
				
				$(".search-edit-row_fin button").on("click",function(e){
					if(userType == "PERSON")
					{
						$("#agent_financierlevel_view").hide();
						$("#agent_financierlevel_edit").show();
					}
					if(userType == "ORGANIZATION")
					{
						$("#org_financierlevel_view").hide();
						$("#org_financierlevel_edit").show();
					}
				
					$(".search-save-row_fin").show();
					$(".search-edit-row_fin").hide();
				});
				
				$(".search-save-row button").click(function(){
				    $("div").scrollTop(0);
				  });
				$('#perbasic-update-form').validate({
					submitHandler : function(form) {
							util.ajax_submit('#perbasic-update-form').done(function(resp)
									{
										if(resp.code == "ACK")
										{
											getMemberDto("#perbasic-update-form",userId,userType);
											$("#bank_view").show();
											$("#bank_edit").hide();
//											$(".file-upload .upload-img").attr("src",attrImg);
//											$(".upload-thumbnail").show();
											$(".search-save-row_basic").hide();
											$(".search-edit-row_basic").show();
										}
									});
					}
				});
				$('#perinvest-update-form').validate({
					submitHandler : function(form) {
							util.ajax_submit('#perinvest-update-form').done(function(resp)
									{
										if(resp.code == "ACK")
										{
											getMemberDto("#perinvest-update-form",userId,userType);
											$("#agent_authcenter_view").show();
											$("#agent_authcenter_edit").hide();
											$("#investorLevel_view").show();
											$("#investorLevel_edit").hide();
											
											$(".search-save-row_inve").hide();
											$(".search-edit-row_inve").show();
											
											//修改了投资信息里面的授权服务中心或是经办人后，投资信息同样做同步刷新
											getMemberDto("#perfina-update-form",userId,userType);
											if(userType == "PERSON")
											{
												$("#agent_financierlevel_view").show();
												$("#agent_financierlevel_edit").hide();
											}
											if(userType == "ORGANIZATION")
											{
												$("#org_financierlevel_view").show();
												$("#org_financierlevel_edit").hide();
											}
										
											$(".search-save-row_fin").hide();
											$(".search-edit-row_fin").show();
											
										}
									});
					}
				});
				$('#perfina-update-form').validate({
					submitHandler : function(form) {
							util.ajax_submit('#perfina-update-form').done(function(resp)
									{
										if(resp.code == "ACK")
										{
											getMemberDto("#perfina-update-form",userId,userType);
											if(userType == "PERSON")
											{
												$("#agent_financierlevel_view").show();
												$("#agent_financierlevel_edit").hide();
											}
											if(userType == "ORGANIZATION")
											{
												$("#org_financierlevel_view").show();
												$("#org_financierlevel_edit").hide();
											}
										
											$(".search-save-row_fin").hide();
											$(".search-edit-row_fin").show();
										}
									});
					}
				});
				
				var getMemberDto = function(tabId,userId,userType) {
					$.ajax({
						url : "getmemberdtoinfo/"+userId,
						type : 'GET',
						contentType:'application/json;charset=utf-8',
						data : {
							userType : userType
						},
				//		async : false,
						error:function(){
						},
						success : function(response,data) {
							var parseJSON=$.parseJSON(response);
							
							$(tabId+' span').each(function(){
								var attrName=$(this).attr('name');
								var value="";
								try{
									value=eval("parseJSON."+attrName);
								}catch(e){
									//console.log('');
								}
								if(attrName){
									$(this).text(value||"");
									$(this).attr('title',value);
								}
							});
							$(tabId+' img').each(function(){
								var attrName=$(this).attr('name');
								var value="";
								try{
									value=eval("parseJSON."+attrName);
								}catch(e){
									//console.log('');
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
						}
						});
					};
			}
		});
	};
		
	$("body").on("click","#personSearchTable a.check-link",function(){
	//		$("#search-indiv-wrapper").show("slide");
			$("#search-member-wrapper").html("");
			$("#table-wrapper").hide();
			var userId = $(this).attr("data-id");
			getMemberDetails('PERSON',userId);
//			$(".search-save-row").show();
			$("#search-member-wrapper").show("slide");
			mfBread.push({
				label:"个人信息详情"
			});
	});
	
	$("body").on("click","#orgSearchTable a.check-link",function(){
	//	$("#search-org-wrapper").show("slide");
		$("#search-member-wrapper").html("");
		$("#table-wrapper").hide();
		var userId = $(this).attr("data-id");
		getMemberDetails('ORGANIZATION',userId);
//		$(".search-save-row").show();
		$("#search-member-wrapper").show("slide");
		mfBread.push({
			label:"机构信息详情"
		});
    });
	
	
	$("#start-time").datepicker({
		 autoclose: true,
		 format: "yyyy-mm-dd"
	});
	
	$("#end-time").datepicker({
		 autoclose: true,
		 format: "yyyy-mm-dd"
	});
	
	$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
		 //console.log(e.target);// activated tab
		//  e.relatedTarget  previous tab
		 $('#search-form-id')[0].reset();
		 if($(e.target).text() == "个人"){
			$("#search-name").text("姓名:");
			$('#hidden_search_user_type').val('PERSON');
			person_table.setParams(util.getSearchData("#search_area"));
			tabFlag='P';
			person_table.invoke("fnDraw");
		 }else if($(e.target).text() == "机构"){
			 $("#search-name").text("企业名称:");
			 $('#hidden_search_user_type').val('ORGANIZATION');
			 tabFlag='O';
			 if(organization){
				 org_table.setParams(util.getSearchData("#search_area"));
				 org_table.invoke("fnDraw");
			 }else{
				 organization = organizationTable();
			 }
		 }
	});
	
	$("#search_area").on("keydown",function(e){
		if(e.which == 13){
			$("#member_search").trigger("click");
			return false;
		}
	});
	
});
