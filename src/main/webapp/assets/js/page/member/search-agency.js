require(['jquery',
         'global',
         'module/util',
         'module/datatables',
         'module/form',
         'module/bread_crumb',
         'jquery.ui',
         'module/ajax',
         'bootstrap',
         'jquery.validate.methods',
         'bootstrap-datepicker',
         'module/cascading_listener',
         'requirejs/domready!'], 

function($, global, util,datatables,upload,mfBread){
	
	var agency_table = null;
	var _fnAgencyBtnRender = function(data,type,full){
		return '<div class="btn-group btn-group-xs"><a data-id="'+full.userId+'" class="btn btn-mf-primary go-details check-link table-link" type="button"><i title="详情" class="fa fa-eye fa-lg"></i></a></div>';
	};
	
	var _fnRenderlog = function(data,type,full){
		return '<a  data-id="'+full.userId+'" class="log-link table-link" data-type="ind" data-toggle="modal" data-target="#logModal">日志</a>';
	};
	var agencyTable = function(){
		var options = {};
		options.tableId = '#agencySearchTable';
		options.sAjaxSource = global.context+"/web/search/getagencyinfo";
		options.functionList={"_fnAgencyBtnRender":_fnAgencyBtnRender,"_fnRenderlog":_fnRenderlog};
		options.aaSorting=[[2,"desc"]];
		agency_table = datatables.init(options);
		agency_table.setParams(util.getSearchData('#search_area'));
		return agency_table.create();
	};
	
	var agency=agencyTable();
	
	/**
	 * Define search event
	 */
	$('#agency_search').click(function(){
		agency_table.setParams(util.getSearchData("#search_area"));
		agency_table.invoke("fnDraw");
	});
	
	$(".right-blk-head").on("click","li:eq(1) a",function(){
		$("#table-wrapper").show("slide");
		$(".form-wrapper").hide();
		$("search-agency-wrapper").hide();
		agency_table.invoke("fnStandingRedraw");
		mfBread.pop();
	});
	
	var getAgencyDetails = function(userId) {
		$.ajax({
			url : 'getagencyinfo/'+userId,
			type : 'GET',
			contentType:'application/json;charset=utf-8',
		//	async : false,
			error:function(){
			},
			success : function(response) {
				$("#search-agency-wrapper").html(response);
				util.bind_popoverx();
				upload.init();
				$(".file-upload").show();
				$(".file-upload").each(function(){
					var $this = $(this);
					var popImg = $this.attr('pop');
					$this.find(".upload-filename").text(popImg);
					$this.attr("data-img",popImg);
				});
				
			/*	$(".go-back").on("click",function(e){
					$("#table-wrapper").show("slide");
					$(".form-wrapper").hide();
					$("search-agency-wrapper").hide();
					agency_table.invoke("fnStandingRedraw");
				});*/
				
				$("#select_bank_province").listenChange({
					url:global.context+'/web/option/region/',
					nextElement:$("#select_bank_city"),
					hideNext:false,
					initialCallback:function(current,next){
						next.val(next.attr("data")).trigger("change");
					}
				});
				
				var attrName=$("#select_bank_province").attr('data');
				$("#select_bank_province").val(attrName);
				
				var attrName=$("#select_bank_city").attr('data');
				$("#select_bank_city").val(attrName);
				
				var attrName=$("#select_bankname").attr('data');
				$("#select_bankname").val(attrName);
				
				var attrName=$("#sleect_proSeverLevel").attr('data');
				$("#sleect_proSeverLevel").val(attrName);
				
				var attrName=$("#sleect_serviceCenterLevel").attr('data');
				$("#sleect_serviceCenterLevel").val(attrName);
				
				$(".search-edit-row-age button").on("click",function(e){
					$("#agency_bank_view").hide();
					$("#agency_bank_edit").show();
					var newImg = $("#agency_bank_view img").attr("src");
					$(".file-upload .upload-img").attr("src",newImg);
					$(".upload-thumbnail").show();
					
					$(".search-save-row-age").show();
					$(".search-edit-row-age").hide();
					$("#select_bank_province").trigger("change");
				});
				$(".search-edit-row-pro button").on("click",function(e){
					
					$("#agency_prolevel_view").hide();
					$("#agency_prolevel_edit").show();
					$("#agency_proagent_view").hide();
					$("#agency_proagent_edit").show();
					
					
					$("#agency_procrd_edit").show();
					$("#agency_procrd_view").hide();
					$("#agency_procrd_edit .file-upload").attr("style","display:block !important");
					
					$(".search-save-row-pro").show();
					$(".search-edit-row-pro").hide();
				});
				$(".search-edit-row-ser button").on("click",function(e){
					$("#agency_serlevel_view").hide();
					$("#agency_serlevel_edit").show();
					$("#agency_seragent_view").hide();
					$("#agency_seragent_edit").show();
					$("#agency_seragentdesc_view").hide();
					$("#agency_seragentdesc_edit").show();
					
					
					$(".search-save-row-ser").show();
					$(".search-edit-row-ser").hide();
				});
				
				$('#agencybasic-update-form').validate({
					submitHandler : function(form) {
							$("#select_bank_province").attr("data",$("#select_bank_province").val());
							$("#select_bank_city").attr("data",$("#select_bank_city").val());
							//从input type hidden  获取
						//	var original_data = {}; 
						//	var option = {dataExtension:{masked_data:$.parseJSON(original_data)}};
							util.ajax_submit('#agencybasic-update-form').done(function(resp)
									{
										if(resp.code == "ACK")
										{
											getAgencyDto("#agencybasic-update-form",userId);
											$("#agency_bank_view").show();
											$("#agency_bank_edit").hide();
											
											$(".search-save-row-age").hide();
											$(".search-edit-row-age").show();
										}
									});
					}
				});
				$('#agencypro-update-form').validate({
					submitHandler : function(form) {
							util.ajax_submit('#agencypro-update-form').done(function(resp)
									{
										if(resp.code == "ACK")
										{
											getAgencyDto("#agencypro-update-form",userId);
											$("#agency_prolevel_view").show();
											$("#agency_prolevel_edit").hide();
											$("#agency_proagent_view").show();
											$("#agency_proagent_edit").hide();
											
											//隐藏|显示担保机构信用披露查看链接
											var wrtrCreditFile = resp.data.wrtrCreditFile;
											if(wrtrCreditFile){
												$(".agency-procrd-link").css("display","block");
												$(".agency-procrd-info").css("display","none");
											}else{
												$(".agency-procrd-link").css("display","none");
												$(".agency-procrd-info").css("display","block");
											}
											
											$("#agency_procrd_edit").hide();
											$("#agency_procrd_view").show();
											
											$("#agency_procrd_edit .file-upload").attr("style","display:none !important");
											
											$(".search-save-row-pro").hide();
											$(".search-edit-row-pro").show();
										}
									});
					}
				});
				$('#agencyser-update-form').validate({
					submitHandler : function(form) {
							util.ajax_submit('#agencyser-update-form').done(function(resp)
									{
										if(resp.code == "ACK")
										{
											getAgencyDto("#agencyser-update-form",userId);
											$("#agency_serlevel_view").show();
											$("#agency_serlevel_edit").hide();
											$("#agency_seragent_view").show();
											$("#agency_seragent_edit").hide();
											$("#agency_seragentdesc_view").show();
											$("#agency_seragentdesc_edit").hide();
											
											$(".search-save-row-ser").hide();
											$(".search-edit-row-ser").show();
										}
									});;
					}
				});
				var getAgencyDto = function(tabId,userId) {
					$.ajax({
						url : "getagencydtoinfo/"+userId,
						type : 'GET',
						contentType:'application/json;charset=utf-8',
					//	async : false,
						error:function(){
						},
						success : function(response,data) {
							var parseJSON=$.parseJSON(response);
							if (parseJSON.productProvider != null) {
								//特殊处理word查看链接
								$(tabId+' .agency-procrd-link').attr("href",global.context+"/web/product/details/"+parseJSON.productProvider.wrtrCreditFile);
							}
							//original code
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
							$("#agencybasic-update-form [data-inited=true]").removeAttr("data-inited");
						}
						});
					};
			}
			});
		};
		
	$("body").on("click","#agencySearchTable a.check-link",function(){
			$("#search-agency-wrapper").html("");
			$("#search-agency-wrapper").show("slide");
			$("#table-wrapper").hide();
			var userId = $(this).attr("data-id");
			getAgencyDetails(userId);
			$(".search-save-row").show();
			mfBread.push({
				label:"详情"
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
	$("#search_area").on("keydown",function(e){
		if(e.which == 13){
			$("#agency_search").trigger("click");
			return false;
		}
	});
});
