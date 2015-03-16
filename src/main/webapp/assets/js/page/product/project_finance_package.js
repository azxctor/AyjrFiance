require(['jquery',
         'global',
         'module/util',
         'module/datatables',
         'module/view_pusher',
         'requirejs/domready!'], 
function($,global,util,datatables,pusher){
	
	  $('#package').dataTable( {
		  "bInfo": false,//页脚信息
		  "bPaginate": false, //开关，是否显示分页器  
		  "bFilter": false, //开关，是否启用客户端过滤器  
		  "bAutoWith": true,  
		  "bSort": false, //开关，是否启用各列具有按列排序的功能    
		  "bRetrieve": true
	  } );
	  $("tbody").on("click",".package-name-link",function(){
		  var packageId = $(this).attr("data-package-id");
		  pusher.push({
	  		url: global.context+"/web/financingpackage/details/"+packageId+"/view",
	  		type : 'GET',
	  		element:"#pro-product-details",
	  		title:"融资详情",
	  		onShow:function(){
	  			$("#product-name").removeClass("link").attr("id","");
	  		}
	  	});
	 });

});