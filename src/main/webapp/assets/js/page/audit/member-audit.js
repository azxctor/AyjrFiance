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
	var PENDING_STATUS = 'P';//Constant, indicates waiting audit status
	var tabFlag = 'P';//'P':Personal Tab, 'O': Organization Tab
	var person_table = null;
	var org_table = null;
	var _fnIndviRenderbtn = function(data,type,full){
		return '<div class="btn-group btn-group-xs"><a data-id="'+full.userId+'" class="btn btn-mf-primary go-details check-link table-link" type="button"><i title="详情" class="fa fa-eye fa-lg"></i></a></div>';
		//return '<a href="javascript:void(0)" data-id="'+full.userId+'"class="check-link table-link"  data-type="org"><span class="label label-default">详情</span></a>&nbsp;';
	};
	
	var _fnRenderlog = function(data,type,full){
		return '<a  data-id="'+full.userId+'" class="log-link table-link" data-type="ind" data-toggle="modal" data-target="#logModal">日志</a>';
	};
	
	/**
	 * Create person table
	 */
	var personalTable = function(){
		var options = {};
		options.tableId = '#auditTable';
		options.sAjaxSource = global.context+"/web/audit/getmemberinfo";
		options.aaSorting=[[6,"desc"]];
		options.functionList={"_fnIndviRenderbtn":_fnIndviRenderbtn,"_fnIndivInanceStatus":_fnInanceStatus,"_fnRenderlog":_fnRenderlog};
		person_table = datatables.init(options);
		
		person_table.setParams(util.getSearchData("#search_area"));
		return person_table.create();
	};
	
	
	var _fnInanceStatus = function(source,type,val){
		var searchStatus=$('#audit_status').val();
		var val='';
		if(source.basicStatus&&source.basicStatus.code==PENDING_STATUS||source.basicStatus&&source.basicStatus.code!=PENDING_STATUS&&searchStatus=="AUDITED"){
			val+='/基本信息';
		}
		if(source.investorStatus&&source.investorStatus.code==PENDING_STATUS||source.investorStatus&&source.investorStatus.code!=PENDING_STATUS&&searchStatus=="AUDITED"){
			val+='/投资权限';
		}
		if(source.financeStatus&&source.financeStatus.code==PENDING_STATUS||source.financeStatus&&source.financeStatus.code!=PENDING_STATUS&&searchStatus=="AUDITED"){
			val+='/融资权限';
		}
		return val.substring(1);
	};
	
	var _fnOrgRenderbtn = function(data,type,full){
		return '<div class="btn-group btn-group-xs"><a data-id="'+full.userId+'" class="btn btn-mf-primary go-details check-link table-link" type="button"><i title="详情" class="fa fa-eye fa-lg"></i></a></div>';
		//return '<a href="javascript:void(0)" data-id="'+full.userId+'" class="check-link table-link"  data-type="org"><span class="label label-default">详情</span></a>&nbsp;';
	};
	
	/**
	 * Create organization table
	 */
	var organizationTable = function(){
		var options = {};
		options.tableId = '#orgAuditTable';
		options.sAjaxSource = global.context+"/web/audit/getmemberinfo";
		options.aaSorting=[[7,"desc"]];
		options.functionList={"_fnOrgRenderbtn":_fnOrgRenderbtn,"_fnOrgInanceStatus":_fnInanceStatus,"_fnRenderlog":_fnRenderlog};
		org_table = datatables.init(options);
		org_table.setParams(util.getSearchData("#search_area"));
		return org_table.create();
	};
	
	
	/**
	 * Render person table
	 */
	var personal=personalTable();
	var organization = null;
	
	/**
	 * Define search event
	 */
	$('#audit_member_search').click(function(){
		var searchStatus=$('#audit_status').val();
	
		if(tabFlag=='P'){
			if(searchStatus=="AUDITED"){
				$('#waiting_content_person_th').html('已审核内容');
			}else{
				$('#waiting_content_person_th').html('待审核内容');
			}
			person_table.setParams(util.getSearchData("#search_area"));
			person_table.invoke("fnDraw");
		}
		if(tabFlag=='O'){
			if(searchStatus=="AUDITED"){
				$('#waiting_content_org_th').html('已审核内容');
			}else{
				$('#waiting_content_org_th').html('待审核内容');
			}

			if(organization){
				org_table.setParams(util.getSearchData("#search_area"));
				org_table.invoke("fnDraw");
			}
		}
	});
	
	/**
	 * Get user's details, basic info, investor info, financier info.
	 */
	var getMemberDetails = function(tabId,userType,userId) {
		$.ajax({
			url : global.context+"/web/audit/userinfo/"+userId,
			type : 'GET',
			contentType:'application/json;charset=utf-8',
			async : false,
			data : {
				userType : userType,
				queryStatus:$('#audit_status').val()
			},
			error:function(){
			},
			success : function(response,data) {
				resetPage();
				$('#hidden_user_id').val(userId);
				var parseJSON=$.parseJSON(response);
				
				$(tabId+' span').each(function(){
					var attrName=$(this).attr('data-prop');
					var value="";
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
					var attrName=$(this).attr('data-prop');
					var value="";
					try{
						value=eval("parseJSON."+attrName);
					}catch(e){
						console.log('');
					}
					if(attrName&&value){
                        if(util.is_pdf(value)) {
                            $(this).attr('src',global.context+"/assets/img/pdf.jpg");
                            $(this).attr('data-pdfpath',value||"");
                        } else if(util.is_doc(value)) {
                            $(this).attr('src',global.context+"/assets/img/doc.jpg");
                            $(this).attr('data-pdfpath',value||"");
                        }else {
                            $(this).removeAttr("data-pdfpath");
                            $(this).attr('src',value||"");
                        }
					}
				});
				var basicApproved = parseJSON["basicApproved"];
				var person = parseJSON["person"];
				var org = parseJSON["organization"];
				var investor = parseJSON["investorInfo"];
				var financier = parseJSON["financierInfo"];
				var investorLevelList = parseJSON["investorLevelList"];
				var financierLevelList = parseJSON["financierLevelList"];
				initMemberLevelSelect(investorLevelList,'investor');
				initMemberLevelSelect(financierLevelList,'financier');
				
				var changeInvestorLevel = parseJSON["changeInvestorLevel"];
				var changeFinancierLevel = parseJSON["changeFinancierLevel"];
				if(changeInvestorLevel){
					$('#indiv-investor-level-select').show();
					$('#org-investor-level-select').show();
				}else{
					$('#indiv-investor-level-span').show();
					$('#org-investor-level-span').show();
				}
				if(changeFinancierLevel){
					$('#indiv-financier-level-select').show();
					$('#org-financier-level-select').show();
				}else{
					$('#indiv-financier-level-span').show();
					$('#org-financier-level-span').show();
				}
				$('#unaudited_count').val(countButtons(person,org,investor,financier));
				//显示当前level
				if(investor){
					var indivInvestorLevel = investor["investorLevel"];
					$('#indiv-investor-level-select').find("option").each(function(){
						if(indivInvestorLevel&&$(this).val() == indivInvestorLevel.code){
							$(this).attr("selected","selected");
						}
					});
					$('#org-investor-level-select').find("option").each(function(){
						if(indivInvestorLevel&&$(this).val() == indivInvestorLevel.code){
							$(this).attr("selected","selected");
						}
					});
				}
				if(financier){
					var indivFinancierLevel = financier["financierLevel"];
					$('#indiv-financier-level-select').find("option").each(function(){
						if(indivFinancierLevel&& $(this).val() == indivFinancierLevel.code){
							$(this).attr("selected","selected");
						}
					});
					$('#org-financier-level-select').find("option").each(function(){
						if(indivFinancierLevel&&$(this).val() == indivFinancierLevel.code){
							$(this).attr("selected","selected");
						}
					});
				}
				
				
				if(userType=='PERSON'){
					if(person){
						$("#pid-cardf").attr("src",person.id_card_front_img_url);
						$("#pid-cardb").attr("src",person.id_card_back_img_url);
						$("#indiv-basic-info").show();
					}
					if(person && person.status.code != PENDING_STATUS){
						$('#indiv_basic_audit').hide();
					}
					if(investor){
						$("#appl-first").attr("src",investor.application_img_first_url);
						$("#appl-second").attr("src",investor.application_img_second_url);
						
						if(person &&person.status&& person.status.code==PENDING_STATUS 
								&&investor.status&& investor.status.code==PENDING_STATUS){ // && !basicApproved
							$('#indiv_investor_audit').addClass('disabled');
							$('#indiv_investor_tip').show();
						}
						if(investor.status&&investor.status.code!=PENDING_STATUS){
							$('#indiv_investor_audit').hide();
						}
						$("#indiv-invest-info").show();
					}
					if(financier){
						$("#financeimg-first").attr("src",financier.application_img_first_url);
						$("#financeimg-second").attr("src",financier.application_img_second_url);
						
						if(person &&person.status&& person.status.code==PENDING_STATUS
								&&financier.status&& financier.status.code==PENDING_STATUS){ //&& !basicApproved
							$('#indiv_financier_audit').addClass('disabled');
							$('#indiv_financier_tip').show();
						}
						if(financier.status&&financier.status.code!=PENDING_STATUS){
							$('#indiv_financier_audit').hide();
						}
						$("#indiv-financing-info").show();
					}
				}
				if(userType=='ORGANIZATION'){
					if(org){
						$("#org-cardf").attr("src",org.legal_id_card_front_img_url);
						$("#org-cardb").attr("src",org.legal_id_card_back_img_url);
						$("#org-basic-info").show();
					}
					if(org &&org.status&& org.status.code != PENDING_STATUS){
						$('#org_basic_audit').hide();
					}
					if(investor){
						$("#orginv-first").attr("src",investor.application_img_first_url);
						$("#orginv-second").attr("src",investor.application_img_second_url);
						
						if(org &&org.status&& org.status.code==PENDING_STATUS 
								&&investor.status&& investor.status.code==PENDING_STATUS ){//&& !basicApproved
							$('#org_investor_audit').addClass('disabled');
							$('#org_investor_tip').show();
						}
						if(investor.status.code!=PENDING_STATUS){
							$('#org_investor_audit').hide();
						}
						$("#org-invest-info").show();
					}
					if(financier){
						$("#orgfin-first").attr("src",financier.application_img_first_url);
						$("#orgfin-second").attr("src",financier.application_img_second_url);
						$("#orgfin-onfu").attr("src",financier.org_number_file_url);
						$("#orgfin-lfu").attr("src",financier.licence_file_url);
						$("#orgfin-frfu").attr("src",financier.fax_register_file_url);
						
						if(org &&org.status&& org.status.code==PENDING_STATUS
								&&financier.status&& financier.status.code==PENDING_STATUS){// && !basicApproved
							$('#org_financier_audit').addClass('disabled');
							$('#org_financier_tip').show();
						}
						if(financier.status&&financier.status.code!=PENDING_STATUS){
							$('#org_financier_audit').hide();
						}
						$("#org-financing-info").show();
					}
				}
			}
			});
		};
	
		/**
		 * Initial member level select
		 */
		var initMemberLevelSelect = function(memberLevelList,type){
			var option_html="";
			for(var i=0;i<memberLevelList.length;i++){
				option_html += "<option value="+memberLevelList[i].code+">"+memberLevelList[i].text+"</option>";
			}
			$('#indiv-'+type+'-level-select').html(option_html);
			$('#org-'+type+'-level-select').html(option_html);
		};
		
		/**
		 * reset page
		 */
		var resetPage = function(){
			//hide
			$("#indiv-basic-info").hide();
			$("#org-basic-info").hide();
			$("#indiv-invest-info").hide();
			$("#indiv-financing-info").hide();
			$("#org-invest-info").hide();
			$("#org-financing-info").hide();
			
			//disable level select
			$('#indiv-investor-level-select').hide();
			$('#indiv-investor-level-span').hide();
			$('#indiv-financier-level-select').hide();
			$('#indiv-financier-level-span').hide();
			
			$('#org-investor-level-select').hide();
			$('#org-investor-level-span').hide();
			$('#org-financier-level-select').hide();
			$('#org-financier-level-span').hide();
			
			//show audit buttons
			$('#indiv_basic_audit').show();
			$('#indiv_investor_audit').show();
			$('#indiv_financier_audit').show();
			$('#org_basic_audit').show();
			$('#org_investor_audit').show();
			$('#org_financier_audit').show();
			
			//show audit buttons
			$('#indiv_basic_audit').removeClass("disabled");
			$('#indiv_investor_audit').removeClass("disabled");
			$('#indiv_financier_audit').removeClass("disabled");
			$('#org_basic_audit').removeClass("disabled");
			$('#org_investor_audit').removeClass("disabled");
			$('#org_financier_audit').removeClass("disabled");
			
			//hide tips
			$('#indiv_investor_tip').hide();
			$('#org_investor_tip').hide();
			$('#indiv_financier_tip').hide();
			$('#org_financier_tip').hide();
		};
		
	/**
	 * 
	 */	
	var countButtons = function(person,org,investor,financier){
		var count = 0;
		if(person && person.status && person.status.code==PENDING_STATUS){
			count+=1;
		}
		
		if(org && org.status && org.status.code==PENDING_STATUS){
			count+=1;
		}
		
		if(investor && investor.status && investor.status.code==PENDING_STATUS){
			count+=1;
		}
		if(financier && financier.status && financier.status.code==PENDING_STATUS){
			count+=1;
		}
		
		return count;
	};
	$("body").on("click","#auditTable a.check-link",function(){
			$("#indiv-wrapper").show("slide");
			$("#table-wrapper").hide();
			var userId = $(this).attr("data-id");
			getMemberDetails('#indiv-wrapper','PERSON',userId);
			mfBread.push({
				label:"详情"
			});
	});
	
	$("body").on("click","#orgAuditTable a.check-link",function(){
		$("#org-wrapper").show("slide");
		$("#table-wrapper").hide();
		var userId = $(this).attr("data-id");
		getMemberDetails('#org-wrapper','ORGANIZATION',userId);
		mfBread.push({
			label:"详情"
		});
	});
	
	
	$(".right-blk-head").on("click","li:eq(1) a",function(){
		$("#table-wrapper").show("slide");
		$(".form-wrapper").hide();
		if(tabFlag=='P'){
			if(person_table){
				person_table.invoke("fnStandingRedraw");
			}
			
		}
		if(tabFlag=="O"){
			if(org_table){
				org_table.invoke("fnStandingRedraw");
			}
		}
		mfBread.pop();
	});
	
	$(".go-back").on("click",function(e){
		$("#table-wrapper").show("slide");
		$(".form-wrapper").hide();
		if(tabFlag=='P'){
			if(person_table){
				person_table.invoke("fnStandingRedraw");
			}
		}
		if(tabFlag=="O"){
			if(org_table){
				org_table.invoke("fnStandingRedraw");
			}
		}
	});
	$("#start-time").datepicker({
		 autoclose: true,
		 format: "yyyy-mm-dd"
	});
	
	$("#end-time").datepicker({
		 autoclose: true,
		 format: "yyyy-mm-dd"
	});
	
	/**
	 * 
	 */
	$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
		 console.log(e.target);// activated tab
		//  e.relatedTarget  previous tab
		 $('#search-form-id')[0].reset();
		 $('#audit_content_container').show();
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
	
	/**
	 * Change event for switch audit status
	 */
	$('#audit_status').change(function(){
		var val=$(this).val();
		if(val=='AUDITED'){
			$('#audit_content_container').hide();
		}else{
			$('#audit_content_container').show();
		}
	});
	
	/* */
	$('#indiv_basic_audit').click(function(){
		$('#hidden_audit_type').val('BASIC');
	});
	$('#indiv_investor_audit').click(function(){
		$('#hidden_audit_type').val('INVESTOR');
		$('#hidden_user_level').val($('#indiv-investor-level-select').val());
	});
	$('#indiv_financier_audit').click(function(){
		$('#hidden_audit_type').val('FINANCER');
		$('#hidden_user_level').val($('#indiv-financier-level-select').val());
	});
	
	$('#org_basic_audit').click(function(){
		$('#hidden_audit_type').val('BASIC');
	});
	
	$('#org_investor_audit').click(function(){
		$('#hidden_audit_type').val('INVESTOR');
		$('#hidden_user_level').val($('#org-investor-level-select').val());
	});
	$('#org_financier_audit').click(function(){
		$('#hidden_audit_type').val('FINANCER');
		$('#hidden_user_level').val($('#org-financier-level-select').val());
	});
	
	$("#search_area").on("keydown",function(e){
		if(e.which == 13){
			$("#audit_member_search").trigger("click");
			return false;
		}
	});
	
	util.bind_popoverx();
	
	var urlStr = window.location.href;
	if (urlStr.indexOf('#')>-1){
		urlStr = urlStr.substring(urlStr.indexOf('#')+1, urlStr.length);
		if ('org' == urlStr){
			$('.micro-nav-tabs').find('a[href="#org"]').trigger('click');
		}
	}
});
