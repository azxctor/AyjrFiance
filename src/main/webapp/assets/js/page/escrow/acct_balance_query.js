require(['jquery',
         'global',
         'module/util',
         'form',
         'module/datatables',
         'module/ajax',
         'bootstrap',
         'jquery.validate.methods',
         'requirejs/domready!'],function($,global,util,Form,datatables){
		/**
		 * 变量
		 */
		var options = {};
		$("#btn-search").on("click",function(){
			if(!$("#search-form").validate().form()){
				return false;
			}
			options.context = "#info-form";
			$.ajax({
				url : global.context+ "/web/esw/acct/get/balance?acctNo=" + $("#acct-no").val(),
				type : "POST",
				dataType : 'json',
				contentType : 'application/json; charset=UTF-8'
			}).done(function(resp){
				if(resp.retCode == "00"){
					resp.balance = util.get_thousand(resp.balance);
					resp.acctBal = util.get_thousand(resp.acctBal);
					resp.avlAmt = util.get_thousand(resp.avlAmt);
					resp.cashAbleAmt = util.get_thousand(resp.cashAbleAmt);
					util._render_data("#info-form",resp,options);
				}
			});
		});
});