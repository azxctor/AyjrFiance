require(
		[ 'jquery', 'global', 'module/util', 'module/datatables', 'module/bread_crumb', 'jquery.ui',
				'module/ajax', 'bootstrap', 'bootstrap-datepicker',
				'requirejs/domready!' ],
		function($, global, util, datatables,mfBread) {
			var provider_table = null;
			var _fnProviderRenderbtn = function(data, type, full) {
				return '<div class="btn-group btn-group-xs"><a data-id="'+full.userId+'" class="btn btn-mf-primary go-details check-link table-link" type="button"><i title="详情" class="fa fa-eye fa-lg"></i></a></div>';
			};
			
			var _fnRenderlog = function(data,type,full){
				return '<a  data-id="'+full.userId+'" class="log-link table-link" data-type="ind" data-toggle="modal" data-target="#logModal">日志</a>';
			};
			
			var productProviderTable = function() {
				var options = {};
				options.tableId = '#providerTable';
				options.sAjaxSource = global.context
						+ "/web/audit/getproviderinfo";
				options.aaSorting = [ [ 5, "desc" ] ];
				options.functionList = {
					"_fnProviderRenderbtn" : _fnProviderRenderbtn,
					"_fnRenderlog":_fnRenderlog
				};
				provider_table = datatables.init(options);
				provider_table.setParams(util.getSearchData("#search-area"));
				return provider_table.create();
			};

			var productProvider = productProviderTable();

			/**
			 * Define search event
			 */
			$('#audit_provider_search').click(function() {
				provider_table.setParams(util.getSearchData("#search-area"));
				if (productProvider) {
					provider_table.invoke("fnDraw");
				}
			});

			/**
			 * Get user's details, basic info, investor info, financier info.
			 */
			var getMemberDetails = function(userId) {
				$.ajax({
					url : global.context + "/web/audit/providerinfo/" + userId,
					type : 'GET',
					contentType : 'application/json;charset=utf-8',
					async : false,
					error : function() {
					},
					success : function(response, data) {
						var parseJSON = $.parseJSON(response);

						var tabId = '#provider-wrapper';
						$('#provider-level').hide();
						$('#provider-level-span').hide();
						$('#product-provider-audit').show();

						$(tabId + ' span').each(function() {
							var attrName = $(this).attr('data-prop');
							var value ="";
							try {
								value = eval("parseJSON." + attrName);
							} catch (e) {
								console.log('');
							}
							if (attrName) {
								$(this).text(value||"");
								$(this).attr("title",value||"");
							}
						});
						$(tabId + ' img').each(function() {
							var value ="";
							var attrName = $(this).attr('data-prop');
							try {
								value = eval("parseJSON." + attrName);
							} catch (e) {
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
						
						var providerLevel = parseJSON["proSeverLevel"];
						if(providerLevel){
							$('#provider-level').find("option[value=" + providerLevel.code +"]").attr("selected",true);
						}
						
						var productServiceStatus = parseJSON["productServiceStatus"];

						if (productServiceStatus && productServiceStatus.code != 'P') {
							$('#product-provider-audit').hide();
							$('#provider-level-span').show();
							if(parseJSON['proSeverLevel']){
								$('#provider-level-span').html(parseJSON['proSeverLevel'].text);
							}
						}else{
							$('#provider-level').show();
						}

						$('#hidden_user_id').val(userId);
						$("#hidden_app_id").val(parseJSON['appId']);
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
				$('#provider-level').html(option_html);
			};
//			$('product-provider-audit').on('click', function() {
//				$('#hidden_user_level').val($('#provider-level').val());
//			});
			
			$('body').on('click',function() {
				$('#hidden_user_level').val($('#provider-level').val());
			});

			$("body").on("click", "#providerTable a.check-link", function() {
				$("#provider-wrapper").show("slide");
				$("#table-wrapper").hide();
				var userId = $(this).attr("data-id");
				getMemberDetails(userId);
				mfBread.push({
					label:"详情"
				});
			});
			
			$(".right-blk-head").on("click","li:eq(1) a",function(){
				$(".form-wrapper").hide();
				$("#table-wrapper").show("slide");
				provider_table.invoke("fnStandingRedraw");
				mfBread.pop();
			});
			
		

			$("#start-time").datepicker({
				autoclose : true,
				format : "yyyy-mm-dd"
			});

			$("#end-time").datepicker({
				autoclose : true,
				format : "yyyy-mm-dd"
			});
			
			$("#search-area").on("keydown",function(e){
				if(e.which == 13){
					$("#audit_provider_search").trigger("click");
					return false;
				}
			});
			
			util.bind_popoverx();
		});
