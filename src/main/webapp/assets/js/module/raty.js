;define(['jquery',
         'global',
         'raty',
         'bootstrap',
         'requirejs/domready!'],
function($, global) {
	var Raty = function() {
		
	};
	
	Raty.prototype = {
		init: function() {
		    var raty_div = $("#raty-div");
		    var star_num = parseInt(raty_div.attr("data-code"));
		    var discount = parseFloat($("#invs_rate").text())*10;
		    if(star_num!=0){
		    	 raty_div.raty({
			    	number:star_num,
			    	score:star_num,
			    	readOnly: true,
			    	starOff :global.context+'/assets/img/suns-off.png',
			    	starOn  : global.context+'/assets/img/suns-on.png'
		    	 });
		    	 
		    	// var discount = parseFloat($("#invs_rate").text())*10;
		    	 if(discount != 10){
		    		 $("#invest-level-li").tooltip({
		    		    	title:"会员等级为"+star_num,//，9月15日至12月31日可享受交易手续费"+discount+"折优惠",
		    		    	placement:"bottom"
		    		 });
		    	 }
			    
		    }else{
		    	raty_div.text("普通");
		    	if(discount != 10){
		    		$("#invest-level-li").tooltip({
		    	    	title:"会员等级为普通",//，9月15日至12月31日可享受交易手续费"+discount+"折优惠",
		    	    	placement:"bottom"
		    	     });
		    	}
		    }
		}
	};
	
	return new Raty().init();
})