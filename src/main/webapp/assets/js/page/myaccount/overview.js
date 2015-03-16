require(
		[ 'jquery', 'global', 'module/util','module/view_pusher', 'form', 'countup', 'morris','jquery.fileupload','module/upload_tool',
				'requirejs/domready!' ],
		function($, global, util,pusher,form) {
			var donut_data = null;
			var line_data = [];
			var donut = null;
			var $zzz = $("#header-zzz");
			var $czb = $("#header-czb");
			var $zzy = $("#header-zzy");
			var $xwb = $("#main-xwb-zzc");
			var z_num = $zzz.attr("data-amount");
			var b_num = $czb.attr("data-amount");
			var h_num = $zzy.attr("data-amount");
			var xwb_num = $xwb.attr("data-amount");
			var isInvestor = $('#check-investor').val();
			
			if ($("#header-zzz").length > 0) {
				var hzzz_anim = new countUp("header-zzz", 0, z_num, 2, 1);
				hzzz_anim.start();
			}
			if ($("#header-czb").length > 0) {
				var hczb_anim = new countUp("header-czb", 0, b_num, 2, 1);
				hczb_anim.start();
			}
			if ($("#header-zzy").length > 0) {
				var hzzc_anim = new countUp("header-zzy", 0, h_num, 2, 1);
				hzzc_anim.start();
			}
			if ($("#main-xwb-zzc").length > 0) {
				var mxzzc_anim = new countUp("main-xwb-zzc", 0, xwb_num, 2, 1);
				mxzzc_anim.start();
			}
			$('.right-blk-line').hide();

			var sayHello = function() {
				var now = new Date();
				var hour = now.getHours();
				var hello = "";
				if (hour < 6) {
					hello = "凌晨好,";
				} else if (hour < 9) {
					hello = "早上好,";
				} else if (hour < 12) {
					hello = "上午好,";
				} else if (hour < 14) {
					hello = "中午好,";
				} else if (hour < 17) {
					hello = "下午好,";
				} else if (hour < 19) {
					hello = "傍晚好,";
				} else if (hour < 24) {
					hello = "晚上好,";
				}
				;
				$("#day-greeting").text(hello);
			};
			sayHello();

            var getBenefitVal = function(){
            	if (isInvestor == "true") {
	                var url = global.context + "/web/myaccount/investment/myprofit";
	                $.getJSON(url,function(data){
	                    $("#val-expectTotalProfit").empty().append(data.expectTotalProfit);
	                    $("#val-realizedTotalRecvProfit").empty().append(data.realizedTotalRecvProfit);
	                    $("#val-expectTotalRecvProfit").empty().append(data.expectTotalRecvProfit);
	                    $("#val-expectTotalProfitRate").empty().append(data.expectTotalProfitRate+"%");
	                   // $("#val-realizedTotalRecvProfitRate").empty().append(data.realizedTotalRecvProfitRate);
	                });
            	}
            };
            getBenefitVal();

			$(".i-banner-stat li a").tooltip();
			$("#no-sign-cz").tooltip();
			$("#avatar-lock").tooltip();

			var renderBenefit = function() {
				if (isInvestor == "true") {
					$.ajax(
									{
										url : global.context
												+ "/web/myaccount/accountbenifits",
										type : "GET",
										dataType : 'json',
										contentType : 'application/json; charset=UTF-8',
									}).done(function(resp) {
								line_data = resp.investBenifit;

								if ($("#line-main").length > 0) {
									new Morris.Bar({
										// ID of the element in which to draw
										// the chart.
										element : 'line-main',
										// Chart data records -- each entry in
										// this array
										// corresponds to a point on
										// the chart.
										data : line_data,
										xLabels : "month",
										// The name of the data record attribute
										// that contains
										// x-values.
										/*
										 * xLabelFormat:function(x){ return
										 * "2014-01-01"; },
										 */
										xkey : 'week',
										// A list of names of data record
										// attributes that
										// contain y-values.
										ykeys : [ 'investBenifit' ],
										// Labels for the ykeys -- will be
										// displayed when you
										// hover over the
										// chart.
										labels : [ '收益' ],
										barColors : [ '#006080' ]
									});
								}
								;
							});
				}
			};
//			renderBenefit();

//		
			form.init();
			$('#img_sure_btn').on('click',function(){
				if(!$("#upload-img-form").validate().form()){
					return false;
				}
				var fileId = $("#upload-img-form").find(".file-upload").attr("data-img");
				$.ajax({
					url : global.context + "/web/user/setting/icon/update",
					type : "POST",
					dataType : 'json',
					contentType : 'application/json; charset=UTF-8',
					data:JSON.stringify({"iconFileId":fileId})
				}).done(function(resp){
					if(resp.code === 'ACK'){
						self.location.reload();
						/*var img_path = $("#upload-img-form").find(".upload-img").attr("src");
						$(".avatar-div").find(".img_upload").attr("src",img_path);
						$("#img_modal").modal("hide");*/
					}
				});
				
			});
			
//
			var isLoad = false;
			$('.overv_li').on('click', function(){
				if(!isLoad){
					renderBenefit();
					isLoad = true;
				}
			});
			
			$.ajax({
				url : global.context + "/web/myaccount/accountInfo",
				type : "GET",
				dataType : 'json',
				contentType : 'application/json; charset=UTF-8',
			}).done(
				function(resp) {
					$("#sign-user-recharge").removeAttr("disabled");
					var accountOverview = resp.accountOverview;

					if (accountOverview.rechargeNum == 0
							&& accountOverview.withdrawNum == 0
							&& accountOverview.interestNum == 0
							&& accountOverview.investNum == 0
							&& accountOverview.financeNum == 0) {
						return;
					}
					donut_data = [ {
						label : "累计充值",
						value : accountOverview.rechargeNum
					}, {
						label : "累计提现",
						value : accountOverview.withdrawNum
					}, {
						label : "累计活期结算",
						value : accountOverview.interestNum
					}, {
						label : "累计投资申购",
						value : accountOverview.investNum
					}, {
						label : "累计融资还款",
						value : accountOverview.financeNum
					} ];
					donut = new Morris.Donut({
						element : 'donut-main',
						data : donut_data,
						colors : [ '#006080', '#56a5c0', '#309cc0',
								'#003445', '#1b667e' ]
					});
				});
			
			$("#donut-info").on("mouseenter", "li", function() {
				$(this).css("marginLeft", "-15px");
				if ($("#donut-main").find("svg").length > 0) {
					donut.select($(this).index());
				}
			});

			$("#donut-info").on("mouseleave", "li", function() {
				$(this).css("marginLeft", "0px");
			});
			
			$("#search-req").on("click",function(){
				pusher.push({
					element:"#overview",
					url:global.context+"/web/esw/withdrawal/my/list",
					title:"提现结果查询",
					onHide:function(){
						$("body").off("click.widthdraw");
					}
				});
				return false;
			});

			$("#recharge-req").on("click",function(){
				pusher.push({
					element:"#overview",
					url:global.context+"/web/esw/recharge/my/list",
					title:"充值结果查询",
					onHide:function(){
						$("body").off("click.recharge");
					}
				});
				return false;
			});
			
			$("#recharge-form")
					.validate(
							{
								submitHandler : function(form) {
									util
											.ajax_submit(form)
											.done(
													function(resp) {
														if (resp.code == "ACK") {
															$("#rechargeModal")
																	.modal(
																			"hide");
															$(
																	"#modal-confirm-info")
																	.html(
																			"您已成功充值&nbsp;"
																					+ $(
																							form)
																							.find(
																									"#recharge-amount")
																							.val()
																					+ "&nbsp;元&nbsp;!");
															$("#confirmModal")
																	.modal()
																	.data(
																			"refresh",
																			true);
														}
													});
								}
							});

			$("#withdraw-form")
					.validate(
							{
								submitHandler : function(form) {
									util
											.ajax_submit(form)
											.done(
													function(resp) {
														if (resp.code == "ACK") {
															$("#withdrawModal")
																	.modal(
																			"hide");
															$(
																	"#modal-confirm-info")
																	.html(
																			"您已成功提现&nbsp;"
																					+ $(
																							form)
																							.find(
																									"#withdraw-amount")
																							.val()
																					+ "&nbsp;元&nbsp;!");
															$("#confirmModal")
																	.modal()
																	.data(
																			"refresh",
																			true);
														}
													});
								}
							});

			$("#with-apply-form").validate({
				submitHandler : function(form) {
					util.ajax_submit(form).done(function(resp) {
						if (resp.code == "ACK") {
							$("#withApplyModal").modal("hide");
							$("#modal-confirm-info").html("您的提现申请已提交，请在提现记录查询页面查看信息&nbsp;!");
							$("#confirmModal").modal().data("refresh",true);
						}
					});
				}
			});

			$("#confirmModal").on("hidden.bs.modal", function() {
				if ($(this).data("refresh") == true) {
					window.location.reload();
				}
			});

			$("#rechargeModal")
					.on(
							"shown.bs.modal",
							function() {
								$
										.ajax(
												{
													url : global.context
															+ "/web/myaccount/currentacctrechargeview",
													type : "GET",
													dataType : 'json',
													contentType : 'application/json; charset=UTF-8',
												})
										.done(
												function(resp) {
													$("#rechargeModal").find(
															"form").each(
															function() {
																this.reset();
															});
													$("#rechargeModal")
															.find(
																	".bankcard-num")
															.text(
																	resp.bankAcctNo);
													$("#bankacctno").val(
															resp.bankAcctNo);
												});
							});

			$("#withdrawModal")
					.on(
							"shown.bs.modal",
							function() {
								$
										.ajax(
												{
													url : global.context
															+ "/web/myaccount/currentacctwithdrawalview",
													type : "GET",
													dataType : 'json',
													contentType : 'application/json; charset=UTF-8',
												})
										.done(
												function(resp) {
													$("#withdrawModal").find(
															"form").each(
															function() {
																this.reset();
															});
													$("#withdrawModal")
															.find(
																	".bankcard-num")
															.text(
																	resp.bankAcctNo);
													$("#with-bankacctno").val(
															resp.bankAcctNo);
													$("#avail-amount")
															.text(
																	resp.cashableAmt
																			.toFixed(2));
													$("#residue-money").text(
															resp.cashableAmt
																	.toFixed(2)
																	+ " 元");
												});
							});

			$("#withApplyModal").on("shown.bs.modal", function() {
				$.ajax({
					url : global.context + "/web/esw/getbankinfo",
					type : "GET",
					dataType : 'json',
					contentType : 'application/json; charset=UTF-8',
				}).done(function(resp) {
					$("#rechargeModal").find("form").each(function() {
						this.reset();
					});
					// $("#bnk-name").attr("readonly","readonly");
					$("#bnk-acctname").val(resp.bankCardName);
					/*
					 * if(!resp.bnkName || $.trim(resp.bnkName).length==0){
					 * $("#bnk-name").removeAttr("readonly"); }
					 */
					$("#bnk-full-name").val(resp.bnkFullName);
					$("#bnk-name").val(resp.bnkName);

					$("#bnk-acctno").val(resp.bankCardNo);
				});
			});

			$(".modal").on("hidden.bs.modal", function() {
				var $this = $(this);
				$this.find("form")[0].reset();
				$this.find("label.invalid").remove();
			});

			$("#withdraw-amount")
					.blur(
							function() {
								var value = $(this).val();
								if (!isNaN(value) && parseFloat(value) >= 0) {
									$("#residue-money")
											.text(
													(parseFloat($(
															"#avail-amount")
															.text()) - parseFloat(value))
															.toFixed(2)
															+ " 元");
								}
								;
							});

			$(".btn-disabled").on("click", function() {
				return false;
			});

			$('.right-blk-body').on('scroll', function() {
				var p = $(this).scrollTop();
				if (p > 100) {
					$('.right-blk-head').css({
						'border-bottom' : '1px solid #ccc'
					});
				} else {
					$('.right-blk-head').css({
						'border-bottom' : 'none'
					});
				}
			});
			
			$(".fa-question-circle").tooltip();
			$("#recharge-type").tooltip();
			$("#btn-recharge").tooltip();
			//初始获取充值列表
			$("#account-recharge-Modal").on("shown.bs.modal", function() {
				var typeval= $("#recharge-radio").find("input[type='radio']:first").val();
				recharge_list_ajax(typeval);
			});
			
			//打开充值modal
			/*$("#reg-sub-btn").on("click",function(){
				$("#account-recharge-Modal").modal("show");
			});*/
			//根据银行卡类型（个人和企业）获取充值列表
			$("#recharge-radio input[type='radio']").click(function () {
              //  alert($(this).val());
				var typeval = $(this).val();
				recharge_list_ajax(typeval);
            });
			
			function recharge_list_ajax(val){
				$.ajax({
					url : global.context+ "/web/esw/getbanklist?userType="+val,
					type : "GET",
					dataType : 'json',
					contentType : 'application/json; charset=UTF-8'
				}).done(function(resp){
					var html="";
					$.each(resp,function(index,item){
						html += '<option value='+ item.id +'>'+ item.name +'</option>';
					});
					$("#bank-id").empty().append(html);
				});
			};
			//充值
			$("#btn-submit-recharge").on("click",function(){
				$("#account-recharge-form").validate({
					submitHandler : function(form) {
						$.ajax({
							url : global.context+ "/web/esw/isAllowConnect",
							type : "GET",
							dataType : 'json',
							contentType : 'application/json; charset=UTF-8',
						}).done(function(resp){
							if(resp.code == "ACK"){
								form.submit();
								$("#account-recharge-Modal").modal("hide");
							}
						});
					}
				});
			});
			
			//提现-获取短信验证码
			$("#withApplyModal").on("click","#btn-get-message",function(){
				$.ajax({
					url : global.context+ "/web/esw/account/sendmessage",
					type : "GET",
					dataType : 'json',
					contentType : 'application/json; charset=UTF-8',
				});
			});
			
		});