require(
		[ 'jquery', 'global', 'module/util', 'form', 'module/datatables',
				'module/ajax', 'bootstrap', 'requirejs/domready!' ],
		function($, global, util, Form, datatables) {
			// 获取打印列表函数
			var getPrintTable = function(url, param) {
				return $.ajax({
					url : global.context + "/web/" + url,
					type : "POST",
					dataType : 'json',
					contentType : 'application/json; charset=UTF-8',
					data : JSON.stringify(param)
				});
			};

			// print内容初始函数
			var initPrintContent = function(contentId, resp) {
				$(contentId)
						.find("span[data-mData]")
						.each(
								function(index) {
									var that = $(this);
									mData = that.attr("data-mData");
									sFormatType = that.attr("data-sFormatType");
									$
											.each(
													resp,
													function(key) {
														if (key == mData) {
															var val = "";
															if (key == "payMethod"
																	|| key == "termType") {
																val = resp[key].text;
															} else if (key == "intrRate") {
																val = util
																		.get_aprate(resp[key] * 100)
																		+ "%";
															} else {
																val = resp[key] != null
																		&& resp[key] != "" ? (sFormatType == "thousand" ? numeral(
																		resp[key])
																		.format(
																				'0,0.00')
																		: resp[key])
																		: "";
															}
															that.text(val);
														}
													});
								});
			};

			// 时间格式化
			var FormatDateTime = function(obj) {
				var myDate = new Date(obj);
				var year = myDate.getFullYear();
				var month = ("0" + (myDate.getMonth() + 1)).slice(-2);
				var day = ("0" + myDate.getDate()).slice(-2);
				var h = ("0" + myDate.getHours()).slice(-2);
				var m = ("0" + myDate.getMinutes()).slice(-2);
				var s = ("0" + myDate.getSeconds()).slice(-2);
				return year + "-" + month + "-" + day + " " + h + ":" + m + ":"
						+ s;
			};

			// view 提现、充值打印联
			var viewPrint = function(title, url, param) {
				$("#modal-print").find(".print-title").text(title);
				$("#modal-print").find(".print-date").text(
						FormatDateTime(new Date));
				getPrintTable(url, param).done(
						function(resp) {
							$("#modal-print").find(".print-type").text(
									resp.roleType.text);
							if ($(".print-type").text().length == 1) {
								$(".type").css({
									"font-size" : "50px",
									"margin-top" : "-40px"
								});
							} else if ($(".print-type").text().length == 3) {
								$(".type").css({
									"font-size" : "38px",
									"margin-top" : "-40px"
								});
							}
							initPrintContent("#print-content", resp);
						});
				$("#modal-print").modal("show");
			};
			// view 手工转账打印联
			var viewTransferPrint = function(title, url, param) {
				$("#modal-transfer-print").find(".print-title").text(title);
				getPrintTable(url, param).done(function(resp) {
					initPrintContent("#print-transfer-content", resp);
				});
				$("#modal-transfer-print").modal("show");
			};

			// view 签约凭证打印联
			var viewSignedPrint = function(url, param) {
				$("#print-finance-iou-content,#print-signed-evidence-content")
						.find(".print-date").text(FormatDateTime(new Date));
				getPrintTable(url, param).done(function(resp) {
					initPrintContent("#print-finance-iou-content", resp);
					initPrintContent("#print-signed-evidence-content", resp);
				});
				$("#print-iou-content").find("td").css("font-size", "13px");
				$(".title-16").css("font-size", "16px");
				$("#modal-signed-iou-print").find("td").css("padding", "2px");
				$("#modal-signed-iou-print").modal("show");
			};

			$('#btn-modal-agree-withddepappls').on('click', function() {
				var $modal = $(this).parents(".modal");
				$modal.data("bs.modal").hide();
				$modal.find("#print-withddepappls-content").printArea({
					popClose : true
				});
			});

			// view
			var viewWithddepapplsPrint = function(type) {
				if ($('#start-date').val() == '' || $('#end-date').val() == '') { 
					$("#modal-alert-formtip-content").html("请选择起止日期");
					$("#modal-alert-formtip").modal("show");
					return;
				}
				var url = '';
				var titleName = '';
				var lendi=15;
				if (type == 1) {  
					titleName = '安益金融交易服务有限公司-提现审批';
					url = 'myaccount/withDspPrintView'; 
				} else {
					titleName = '安益金融交易服务有限公司-提现确认';
					url = 'myaccount/withDspPrint2View';
				}

				var fromDate = $('#start-date').val();
				var toDate = $('#end-date').val();
				var cashPool = $('#cashPool').val();
				var status = $('#status').val();
				$('#startDt').html(fromDate);
				$('#endDt').html(toDate);
				var applFlag = $('#applStatusFlag').val();
				var dealFlag = $('#dealStatusFlag').val();
				$("#print-withddepappls-content").find(".print-date").text(
						FormatDateTime(new Date));
				var contentlist = '';
				var param = {
					"fromDate" : fromDate,
					"toDate" : toDate,
					"cashPool" : cashPool,
					"status" : status,
					"applFlag" : applFlag,
					"dealFlag" : dealFlag
				};
				getPrintTable(url, param)
						.done(
								function(response) {
									if (response != null) { 
										var sum = 0;
										contentlist = '';
										for (var i = 0, x = 0; i < response.length; i++, x++) {
											if (x == lendi
													&& i != response.length - 1) {
												contentlist += '<tr ><td colspan="9" >';
												contentlist += '<div class="pa-10">';
												contentlist += '<div class="ft-r">';
												contentlist += '    <span class="ma-r50">';
												contentlist += '		<span>经办：</span>';
												contentlist += '		<span data-mData="handler"></span>';
												contentlist += '	</span>';
												contentlist += '	<span class="ma-r50">';
												contentlist += '		<span>复核：</span>';
												contentlist += '		<span data-mData="checker"></span>';
												contentlist += '	</span>';
												contentlist += '	<span class="ma-r50">';
												contentlist += '		<span>打印时间：</span>';
												contentlist += '	<span class="print-date">'
														+ FormatDateTime(new Date)
														+ '</span>';
												contentlist += '	</span>';
												contentlist += '</div>';
												contentlist += '</div>';
												contentlist += '</td></tr> ';
												x = 0;
											}

											if (i % lendi == 0) {
												// console.log(i);
										        contentlist += '<tr hight="27px" ><td colspan="9" >';  
												contentlist += '<div class="logo-left">';
												contentlist += '	<img class="wt-p100"  src="/assets/img/printlogo.png"/>';
												contentlist += '</div>';
												contentlist += '<div class="title-16">';
												contentlist += '	<span class="withddep-print-title">'
														+ titleName + '</span>';
												contentlist += '</div>';
												contentlist += '<div class="text-center ma-10"style="width: 675px;">';
												contentlist += '   <span>会计期间:</span>';
												contentlist += '	<span id="startDt">'
														+ fromDate
														+ '</span>-<span id="endDt">'
														+ toDate + '</span>';
												contentlist += '</div>';
												contentlist += '</td></tr>';
												contentlist += '<tr style="font-size:12px;font-weight:bold;"><th>序号</th><th>申请日期</th><th>交易账号</th><th>提现金额（元）</th><th>银行卡号</th><th>银行账号名</th><th>开户银行</th><th>资金池</th><th>申请理由</th></tr>';
											}

											var f_val = response[i].trxAmt;
											if (isNaN(f_val)) {
												f_val = 0;
											} else {
												sum += f_val;
											}
											if (null != response[i].applStatus) {
												stautsName = response[i].applStatus.text;
											} else {
												stautsName = response[i].dealStatus.text;
											}

											contentlist += '<tr><td>'
													+ (i + 1)
													+ '</td><td>'
													+ response[i].applDt
													+ '</td><td>'
													+ response[i].acctNo
													+ '</td><td>'
													+ util .get_thousand(response[i].trxAmt)
													+ '</td><td>'
													+ response[i].bnkAcctNo
													+ '</td><td>'
													+ response[i].bnkAcctName
													+ '</td><td>'
													+ response[i].bnkName
													+ '</td><td>'
													+ response[i].cashPool.text
													+ '</td><td>'
													+ response[i].trxMemo
													+ '</td></tr>';

											if (i == response.length - 1) {
												contentlist += '<tr><td ></td><td></td><td></td><td>'
														+ util
																.get_thousand(sum)
														+ '</td><td></td><td></td><td></td><td></td><td></td></tr>';
												contentlist += '<tr ><td colspan="9" >';
												contentlist += '<div class="pa-10">';
												contentlist += '<div class="ft-r">';
												contentlist += '    <span class="ma-r50">';
												contentlist += '		<span>经办：</span>';
												contentlist += '		<span data-mData="handler"></span>';
												contentlist += '	</span>';
												contentlist += '	<span class="ma-r50">';
												contentlist += '		<span>复核：</span>';
												contentlist += '		<span data-mData="checker"></span>';
												contentlist += '	</span>';
												contentlist += '	<span class="ma-r50">';
												contentlist += '		<span>打印时间：</span>';
												contentlist += '	<span class="print-date">'
														+ FormatDateTime(new Date)
														+ '</span>';
												contentlist += '	</span>';
												contentlist += '</div>';
												contentlist += '</div>';
												contentlist += '</td></tr>';
											}
										}

										$('#print-withddepappls-table').html(contentlist);
									}
								});
				$("#modal-withddepappls-print").modal("show");
			};
			$("body").on("click", ".btn-modal-withddepappls-print",
					function(e) {
						viewWithddepapplsPrint(1);
					});
			$("body").on("click", ".btn-modal-withddepappls-print2",
					function(e) {
						viewWithddepapplsPrint(2);
					});

			$("body")
					.on(
							"click",
							".btn-print-get,.btn-print-set,.btn-print-transfer,.btn-print-signed,.btn-print-app-set",
							function(e) {
								var $target = $(e.currentTarget);
								var currentApplNo = $target.attr("data-applno")
										|| $target.attr("data-id");
								if (!currentApplNo) {
									return false;
								}
								if ($target.hasClass("btn-print-get")) {
									var param = "";
									if ($(".jnlNo").length > 0) {
										param = {
											jnlNo : currentApplNo
										};
									}
									if ($(".applNo").length > 0) {
										param = {
											applNo : currentApplNo
										};
									}
									viewPrint("安益金融交易服务有限公司提现凭证",
											"myaccount/queryPrintInfo", param);
								}
								if ($target.hasClass("btn-print-set")) {
									viewPrint("安益金融交易服务有限公司充值凭证",
											"myaccount/queryPrintInfo", {
												jnlNo : currentApplNo
											});
								}
								;
								if ($target.hasClass("btn-print-app-set")) {
									viewPrint("安益金融交易服务有限公司充值申请凭证",
											"myaccount/queryApplPrintInfo", {
												applNo : currentApplNo
											});
								}
								;
								if ($target.hasClass("btn-print-transfer")) {
									viewTransferPrint(
											"安益金融交易服务有限公司收、付凭证",
											"myaccount/queryTransferApplPrintInfo",
											{
												applNo : currentApplNo
											});
								}
								;
								if ($target.hasClass("btn-print-signed")) {
									viewSignedPrint(
											"financingpackage/platform/getdebtbillpriinfodetail",
											{
												pkgId : currentApplNo
											});
								}
							});
		});
