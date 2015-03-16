require(
		['jquery',
         'global',
         'module/util',
         'module/view_pusher',
         'form',
         'countup',
         'morris',
         'requirejs/domready!'],function($,global,util,pusher){
		var donut_data = null;
		var line_data = [];
		var donut = null;
		var $zzc = $("#header-zzc");
		var $xwb = $("#main-xwb-zzc");
		var h_num = $zzc.attr("data-amount");
		var xwb_num = $xwb.attr("data-amount");
		if($("#header-zzc").length>0)
		{
			var hzzc_anim = new countUp("header-zzc", 0, h_num, 2, 1);
			hzzc_anim.start();
		}
		
		if($("#main-xwb-zzc").length>0)
		{
			var mxzzc_anim = new countUp("main-xwb-zzc", 0, xwb_num, 2, 1);
			mxzzc_anim.start();
		}	
		$('.right-blk-line').hide();
		
		var sayHello = function(){
			var now = new Date();
			var hour = now.getHours();
			var hello = "";
			if(hour < 6){
				hello = "凌晨好,";
			}else if (hour < 9){
				hello = "早上好,";
			}else if (hour < 12){
				hello = "上午好,";
			}else if (hour < 14){
				hello = "中午好,";
			}else if (hour < 17){
				hello = "下午好,";
			} else if (hour < 19){
				hello = "傍晚好,";
			} else if (hour < 24){
				hello = "晚上好,";
			};
			$("#day-greeting").text(hello);
		};
		sayHello();
		
		$(".i-banner-stat li a").tooltip();
		$("#no-sign-cz").tooltip();

		$.ajax({
			url : global.context + "/web/myaccount/accountInfo",
			type : "GET",
			dataType : 'json',
			contentType : 'application/json; charset=UTF-8',
		}).done(
				function(resp) {

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
						colors : [ '#EF8100', '#FEC99B', '#FFAA62',
								'#A85623', '#C14E00' ]
					});
				});
		
			$("#donut-info").on("mouseenter","li",function(){
				$(this).css("marginLeft","-15px");
				if($("#donut-main").find("svg").length>0){
					donut.select($(this).index());
				}
			});
			
			$("#donut-info").on("mouseleave","li",function(){
				$(this).css("marginLeft","0px");
			});
		
			$("#recharge-form").validate({
				submitHandler:function(form){
					util.ajax_submit(form).done(function(resp){
						if(resp.code == "ACK"){
							$("#rechargeModal").modal("hide");
							$("#modal-confirm-info").html("您已成功充值&nbsp;"+$(form).find("#recharge-amount").val()+"&nbsp;元&nbsp;!");
							$("#confirmModal").modal().data("refresh",true);
						}
					});
				}
			});
			
			$("#withdraw-form").validate({
				submitHandler:function(form){
					util.ajax_submit(form).done(function(resp){
						if(resp.code == "ACK"){
							$("#withdrawModal").modal("hide");
							$("#modal-confirm-info").html("您已成功提现&nbsp;"+$(form).find("#withdraw-amount").val()+"&nbsp;元&nbsp;!");
							$("#confirmModal").modal().data("refresh",true);
						}
					});
				}
			});
			
			$("#with-apply-form").validate({
				submitHandler:function(form){
					util.ajax_submit(form).done(function(resp){
						if(resp.code == "ACK"){
						  $("#withApplyModal").modal("hide");
						  $("#modal-confirm-info").html("您的提现申请已提交，请在<a href="+global.context+"/web/myaccount/platformwithddepapplsview>提现申请查询</a>页面查看审核信息&nbsp;!");
						  $("#confirmModal").modal().data("refresh",false);
						} 
					});
				}
			});
			
			$("#confirmModal").on("hidden.bs.modal",function(){
				if($(this).data("refresh") == true){
					window.location.reload();
				}
			});
			
			$("#rechargeModal").on("shown.bs.modal",function(){
				$.ajax({
					url:global.context+"/web/myaccount/platformrechargeview",
					type: "GET",
					dataType: 'json',
					contentType: 'application/json; charset=UTF-8',
				}).done(function(resp){
					$("#rechargeModal").find("form").each(function(){
						this.reset();
					});
					$("#rechargeModal").find(".bankcard-num").text(resp.bankAcctNo);
					$("#bankacctno").val(resp.bankAcctNo);
				});
			});
			
			$("#withdrawModal").on("shown.bs.modal",function(){
				$.ajax({
					url:global.context+"/web/myaccount/platformwithdrawalview",
					type: "GET",
					dataType: 'json',
					contentType: 'application/json; charset=UTF-8',
				}).done(function(resp){
					$("#withdrawModal").find("form").each(function(){
						this.reset();
					});
					$("#withdrawModal").find(".bankcard-num").text(resp.bankAcctNo);
					$("#with-bankacctno").val(resp.bankAcctNo);
					$("#avail-amount").text(resp.cashableAmt.toFixed(2));
					$("#residue-money").text(resp.cashableAmt.toFixed(2)+" 元");
				});
			});
			
			$("#withApplyModal").on("shown.bs.modal",function(){
				$.ajax({
					url:global.context+"/web/myaccount/platformwithdrawApplyView",
					type: "GET",
					dataType: 'json',
					contentType: 'application/json; charset=UTF-8',
				}).done(function(resp){
					$("#rechargeModal").find("form").each(function(){
						this.reset();
					});
					//$("#bnk-name").attr("readonly","readonly");
					$("#bnk-acctname").val(resp.bnkAcctName);
					/*if(!resp.bnkName || $.trim(resp.bnkName).length==0){
						$("#bnk-name").removeAttr("readonly");
					}*/
					$("#bnk-name").val(resp.bnkName);
					$("#bnk-full-name").val(resp.bnkFullName);
					
					$("#bnk-acctno").val(resp.bnkAcctNo);
				});
			});
			
			$(".modal").on("hidden.bs.modal",function(){
				var $this = $(this);
				$this.find("form")[0].reset();
				$this.find("label.invalid").remove();
			});
			
			
			$("#withdraw-amount").blur(function(){
				var value = $(this).val();
				if(!isNaN(value) && parseFloat(value)>=0){
					$("#residue-money").text((parseFloat($("#avail-amount").text())-parseFloat(value)).toFixed(2)+" 元");
				};
			});
			
			$(".btn-disabled").on("click",function(){
				return false;
			});
			
			$('.right-blk-body').on('scroll', function(){
				var p = $(this).scrollTop();
				if(p > 100) {
					$('.right-blk-head').css({
						'border-bottom': '1px solid #ccc'
					});
				}else{
					$('.right-blk-head').css({
						'border-bottom': 'none'
					});
				}
			});
			
			$(".search-req").on("click",function(){
				pusher.push({
					element:"#overview",
					url:global.context+"/web/myaccount/platformwithddepapplsview",
					title:"非签约会员提现记录",
					onHide:function(){
						$("body").off("click.widthdraw");
						$("#modal-print").remove();
						$("#project-modal").remove();
					}
				});
				return false;
			});
});