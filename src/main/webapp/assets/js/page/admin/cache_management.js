require(['jquery',
         'global',
         'module/util',
         'module/ajax',
         'bootstrap',
         'jquery.placeholder',
         'requirejs/domready!'], 
function($, global, util){

	$("#refresh-common-btn").click(function() {
		$.ajax({
			url: global.context + '/web/admin/cachemanagement/refreshcommon',
			type: 'POST',
			dataType: 'json',
			contentType: 'application/json; charset=UTF-8',
			data: null
		});
	});
	
	$("#refresh-market-all-btn").click(function() {
		$.ajax({
			url: global.context + '/web/market/financing/refreshall',
			type: 'POST',
			dataType: 'json',
			contentType: 'application/json; charset=UTF-8',
			data: null
		});
	});
	

	$("#refresh-market-one-btn").click(function() {
		var pkgId = $('#pkgId').val().trim();
		$.ajax({
			url: global.context + '/web/market/financing/refreshone',
			type: 'POST',
			dataType: 'json',
			contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
			data: 'pkgId=' + pkgId
		});
	});
	
	$("#refresh-auth-btn").click(function() {
		var userName = $('#userName').val().trim();
		if (userName.length > 0) {
			$.ajax({
				url: global.context + '/web/admin/cachemanagement/refreshauth',
				type: 'POST',
				dataType: 'json',
				contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
				data: 'userName=' + userName
			});
		}
	});
	
	$("#refresh-all-authz-btn").click(function() {
		$.ajax({
			url: global.context + '/web/admin/cachemanagement/refreshallauthz',
			type: 'POST',
			dataType: 'json',
			contentType: 'application/json; charset=UTF-8',
			data: null
		});
	});
	
	
});
