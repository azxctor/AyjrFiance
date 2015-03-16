require(['jquery',
         'global',
         'module/util',
         'module/datatables',
         'module/form',
         'module/bread_crumb',
         'jquery.ui',
         'module/ajax',
         'bootstrap',
         'bootstrap-datepicker',
         'requirejs/domready!'], 
function($, global, util,datatables,form,mfBread){
	var tabFlag = 'P';
	var person_table = null;
	var org_table = null;
	var except = ["gender", "legal_gender"];
	var original_obj = {};
	var _fnAgentBtnRender = function(data,type,full){
		return '<div class="btn-group btn-group-xs"><a data-id="'+full.userId+'" class="btn btn-mf-primary go-details check-link table-link" type="button"><i title="详情" class="fa fa-eye fa-lg"></i></a></div>';
	};
	
	var _fnRenderlog = function(data,type,full){
		return '<a  data-id="'+full.userId+'" class="log-link table-link" data-type="ind" data-toggle="modal" data-target="#logModal">日志</a>';
	};
	
	var personalTable = function(){
		var options = {};
		options.tableId = '#personSearchTable';
		options.sAjaxSource = global.context+"/web/search/getmemberinfo";
		options.functionList={"_fnAgentBtnRender":_fnAgentBtnRender,"_fnfinanceAgent":_fnfinanceAgent,"_fnAgentRegister":_fnAgentRegister,"_fnRenderlog":_fnRenderlog};
		options.aaSorting=[[2,"desc"]];
		person_table = datatables.init(options);
		person_table.setParams(util.getSearchData('#search_area'));
		return person_table.create();
	};
	
	var _fnfinanceAgent = function(source,type,val){
		var finance = source.financeAgent;
		var invest = source.investorAgent;
		if(invest && $.trim(invest).length>0){
			return invest;
		}
			
		if(finance && $.trim(finance).length>0){
			return finance;
		}
		
		return "";
	};
	
	var _fnAgentRegister = function(source,type,val){
		var agentRegister = source.agentRegister;
		if(agentRegister && agentRegister=='Y'){
			return "是";
		}
		return "否";
	};
	
	var _fnOrgAgentBtnRender = function(data,type,full){
		return '<div class="btn-group btn-group-xs"><a data-id="'+full.userId+'" class="btn btn-mf-primary go-details check-link table-link" type="button"><i title="详情" class="fa fa-eye fa-lg"></i></a></div>';
	};
	var organizationTable = function(){
		//Organization 
		var options = {};
		options.tableId = '#orgSearchTable';
		options.sAjaxSource = global.context+"/web/search/getmemberinfo";
		options.functionList={"_fnOrgAgentBtnRender":_fnOrgAgentBtnRender,"_fnfinanceAgent":_fnfinanceAgent,"_fnAgentRegister":_fnAgentRegister,"_fnRenderlog":_fnRenderlog};
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
	
	/*extend String prototype*/
	String.prototype.endsWith = function(suffix) {
	    return this.indexOf(suffix, this.length - suffix.length) !== -1;
	};
	
	$("body").on("click","#personSearchTable a.check-link",function(){
			var userId = $(this).attr("data-id");
			//
			//alert(userId);
			$("#search-detail-content").html("");
			$("#search-detail-wrapper").show("slide");
			$("#table-wrapper").hide();
			$.ajax({
				url:  global.context+"/web/members/details/"+userId+"",
				type : 'GET'
			}).done(function(resp){
				mfBread.push({
					label:"详情"
				});
				$("#search-detail-content").html(resp);
				util.bind_popoverx();
				util.select2_init();
				var url2 = $("#form-investor").attr("data-url");
				util.render_form("#form-investor",{
					url:url2,
					cache:false,
					except:except
				});
				
				var url1 = $("#form-basic").attr("data-url");
				util.render_form("#form-basic",{
					url:url1,
					cache:false,
					except:except
				}).done(function(resp){
					/*
					 * Add the original value of the mask value into the array
					 * and put the array in the page hidden field
					 */
					for (field in resp){
						if(field.endsWith("_original")){
							original_obj[field] = resp[field];
						}
					}
					$("#hidden_original_data").text(JSON.stringify(original_obj));
				});
				
				
				
				var url3 = $("#form-financier").attr("data-url");
				util.render_form("#form-financier",{
					url:url3,
					cache:false,
					except:except
				});
				form.init();
				
			});
	});
	
	$("body").on("click","#orgSearchTable a.check-link",function(){
		var userId = $(this).attr("data-id");
		$("#search-detail-content").html("");
		$("#search-detail-wrapper").show("slide");
		$("#table-wrapper").hide();
		$.ajax({
			url:  global.context+"/web/members/details/"+userId+"",
			type : 'GET'
		}).done(function(resp){
			mfBread.push({
				label:"详情"
			});
			$("#search-detail-content").html(resp);
			util.bind_popoverx();
			util.select2_init();
			var url2 = $("#form-investor").attr("data-url");
			util.render_form("#form-investor",{
				url:url2,
				cache:false,
				except:except
			});
			
			var url1 = $("#form-basic").attr("data-url");
			util.render_form("#form-basic",{
				url:url1,
				cache:false,
				except:except
			}).done(function(resp){
				for (field in resp){
					if(field.endsWith("_original")){
						original_obj[field] = resp[field];
					}
				}
				$("#hidden_original_data").text(JSON.stringify(original_obj));
			});;
			
			
			
			var url3 = $("#form-financier").attr("data-url");
			util.render_form("#form-financier",{
				url:url3,
				cache:false,
				except:except
			});
			form.init();
		});
	
    });
	
	$(".right-blk-head").on("click","li:eq(1) a",function(){
		$(".form-wrapper").hide();
		$("#table-wrapper").show("slide");
		if(person_table){
			person_table.invoke("fnStandingRedraw");
		}
		if(org_table){
			 org_table.invoke("fnStandingRedraw");
		}
		mfBread.pop();
	});
	
	/*$(".go-back").on("click",function(e){
		$(".form-wrapper").hide();
		$("#table-wrapper").show("slide");
		if(person_table){
			person_table.invoke("fnStandingRedraw");
		}
		if(org_table){
			 org_table.invoke("fnStandingRedraw");
		}
	});*/
	
	$(".datepicker").datepicker({
		 autoclose: true,
		 format: "yyyy-mm-dd",
		 language: 'zh-CN'
	});

	
	$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
		 console.log(e.target);// activated tab
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
