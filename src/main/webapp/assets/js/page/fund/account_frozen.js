require(['jquery',
         'global',
         'module/util',
         'module/view_pusher',
         'module/datatables',
         'form',
         'select2',
         'requirejs/domready!'], 
function($, global, util, pusher, datatables, form){

	var frozen_table = null;
	var unfrozen_table = null;
	var member_frozen_table = null;
	var member_unfrozen_table = null;
	
	var ht = {};
	
    ht.tools = {};
    
    ht.tools.showModal = function(object){
    	if(object.type == "datatables"){
			$('#modal-title').html(object.title);
			$('#modal .modal-table').show();
			$('#modal .modal-notice').hide();
			$('#modal .modal-footer').hide();
		}else if(object.type == "notice"){
			$('#modal-title').html(object.title);
			$('#modal .modal-table').hide();
			$('#modal .modal-notice').html(object.message).show();
			$('#modal .modal-footer').show();
			$('#modal #btn-modal-sure').attr("data-type",object.operate);
			$.each(object.datas,function(key,value){
				$('#modal #btn-modal-sure').attr("data-"+key,value);
			});
		}
		$('#modal').modal('show');
    };
	
    ht.ui = {};
    
    ht.ui.clickSearch = function(obj, tabActive){
    	obj.on('click', function(){
    		var tab_type = tabActive.find('.active a').attr('href');
    		if ('#unfrozen' == tab_type) {
	    		frozen_table.setParams(util.getSearchData('#form-frozen-search'));
	    		frozen_table.invoke("fnDraw");
	    	}else if ('#account-list' == tab_type) {
	    		unfrozen_table.setParams(util.getSearchData('#form-frozen-search'));
	    		unfrozen_table.invoke("fnDraw");
	    	}
		});
    };
    
    ht.ui.clickMemberSearch = function(obj, memberTabActive){
    	obj.on('click', function(){
    		var member_type = memberTabActive.find('.active a').attr('href');
    		if ('#user-frozen-pending-list' == member_type) {
	    		member_frozen_table.setParams(util.getSearchData('#form-memberfrozen-search'));
	    		member_frozen_table.invoke("fnDraw");
	    	}else if ('#user-unfrozen-pending-list' == member_type) {
	    		member_unfrozen_table.setParams(util.getSearchData('#form-memberfrozen-search'));
				member_unfrozen_table.invoke("fnDraw");
	    	}
		});
    };
    
    ht.ui.enterKeyup = function(objTxt, obj){
    	objTxt.keyup(function(e){
    		if (e.keyCode == 13){
    			obj.click();
    		}
    	});
    }
    
    ht.ui.clickFrozenBtn = function(obj){
    	obj.on('click', '.btn-frozen', function(){
    		var userId = $(this).attr("data-id");
			$('#modal').modal('show');
			ht.tools.showModal({
				type: "notice",
				title: "账户冻结",
				message: '<div class="row"><label class="col-sm-2 text-right required">备注</label><div class="col-sm-8"><textarea class="col-sm-12" name="trxMemo" row="2" cols="" maxlength="100" data-validate="{required:true}"></textarea><input type="hidden" name="userId" value="'+userId+'"></div></div>',
				datas: {userId:userId},
				operate: "frozen"
			});
    	});
    };
    
    ht.ui.clickUnfrozenBtn = function(obj){
    	obj.on('click', '.btn-unfrozen',function(){
			var userId = $(this).attr('data-id');
			var jnlNo = $(this).attr('data-jnlno');
			$('#modal').modal('show');
			ht.tools.showModal({
				type: 'notice',
				title: '账户解冻',
				message: '<div class="row"><label class="col-sm-2 text-right required">备注</label><div class="col-sm-8"><textarea class="col-sm-12" name="trxMemo" row="2" cols="" maxlength="100" data-validate="{required:true}"></textarea><input type="hidden" name="userId" value="'+userId+'"><input type="hidden" name="freezeJnlNo" value="'+jnlNo+'"></div></div>',
				datas: {userId:userId},
				operate: 'unfrozen'
			});
		});
    }
    
    ht.ui.clickMemberFrozenBtn = function(obj){
    	obj.on('click', '.btn-memberfrozen', function(){
    		var userId = $(this).attr("data-id");
			$('#modal').modal('show');
			ht.tools.showModal({
				type: "notice",
				title: "会员冻结",
				message: '<div class="row"><label class="col-sm-2 text-right required">备注</label><div class="col-sm-8"><textarea class="col-sm-12" name="trxMemo" row="2" cols="" maxlength="100" data-validate="{required:true}"></textarea><input type="hidden" name="userId" value="'+userId+'"></div></div>',
				datas: {userId:userId},
				operate: "frozen"
			});
    	});
    };
    
    ht.ui.clickMemberUnfrozenBtn = function(obj){
    	obj.on('click', '.btn-memberunfrozen',function(){
			var userId = $(this).attr('data-id');
			var jnlNo = $(this).attr('data-jnlno');
			$('#modal').modal('show');
			ht.tools.showModal({
				type: 'notice',
				title: '会员解冻',
				message: '<div class="row"><label class="col-sm-2 text-right required">备注</label><div class="col-sm-8"><textarea class="col-sm-12" name="trxMemo" row="2" cols="" maxlength="100" data-validate="{required:true}"></textarea><input type="hidden" name="userId" value="'+userId+'"><input type="hidden" name="freezeJnlNo" value="'+jnlNo+'"></div></div>',
				datas: {userId:userId},
				operate: 'unfrozen'
			});
		});
    }
    
    ht.ui.clickLogBtn = function(obj){
		obj.on('click','.view-log',function(){
			var acctNo = $(this).attr("data-acctno");
			frozenlog_table.setParams([{name:"acctNo",value:acctNo}]);
			frozenlog_table.invoke('fnDraw');
			ht.tools.showModal({
				type: "datatables",
				title: "日志",
				acctNo: acctNo,
			});
		})
    };
    
    ht.ui.clickTabs = function(obj){
    	obj.on('click', function(e){
			e.preventDefault();
			var tab_type = $(this).attr('href');
	    	if ('#unfrozen' == tab_type) {
	    		frozen_table.setParams(util.getSearchData('#form-frozen-search'));
	    		frozen_table.invoke("fnDraw");
	    	}else if ('#account-list' == tab_type) {
	    		unfrozen_table.setParams(util.getSearchData('#form-frozen-search'));
	    		unfrozen_table.invoke("fnDraw");
	    	}else if ('#user-frozen-pending-list' == tab_type) {
	    		member_frozen_table.setParams(util.getSearchData('#form-memberfrozen-search'));
	    		member_frozen_table.invoke("fnDraw");
	    	}else if ('#user-unfrozen-pending-list' == tab_type) {
	    		member_unfrozen_table.setParams(util.getSearchData('#form-memberfrozen-search'));
				member_unfrozen_table.invoke("fnDraw");
	    	}
			$(this).tab('show');
		});
    }
    
    ht.app = {};
    
    ht.app.renderFrozenTable = function(){
    	var _fnFrozenOperateBtn = function(a1,a2,data){
	        return '<button class="btn btn-mf-primary btn-frozen" data-id='+data.userId+' type="button"><i class="fa fa-lock" title="只收不付冻结"></i></button>';
		}
		var _fnLogLink = function(a1,a2,data){
			return '<a class="view-log" data-id="'+data.userId+'" data-acctno="'+data.acctNo+'">日志</a>';
		}
    	
    	var options = {};
		options.tableId = '#table-frozen';
		options.sAjaxSource =  global.context+"/web/fund/user/acct/frozenlist";
		options.functionList = {"_fnOperateBtn" : _fnFrozenOperateBtn, "_fnLogLink" : _fnLogLink};
		options.aaSorting=[[0,"asc"]];
		frozen_table = datatables.init(options);
		frozen_table.setParams(util.getSearchData('#form-frozen-search'));
		return frozen_table.create();
    };
    
    ht.app.renderUnfrozenTable = function(){
    	var _fnOperateBtn = function(a1,a2,data){
			return '<button class="btn btn-mf-primary btn-unfrozen" data-id='+data.userId+' data-jnlno='+data.jnlNo+' type="button"><i class="fa fa-unlock-alt" title="只收不付解冻"></i></button>';
		}
    	
    	var options = {};
		options.tableId = '#table-unfrozen';
		options.sAjaxSource =  global.context+"/web/fund/user/acct/unfrozenlist";
		options.functionList = {"_fnOperateBtn" : _fnOperateBtn};
		options.aaSorting=[[0,"asc"]];
		unfrozen_table = datatables.init(options);
		unfrozen_table.setParams(util.getSearchData('#form-frozen-search'));
		return unfrozen_table.create();
    };
    
    ht.app.renderMemberFrozenTable = function() {
    	var _fnMemberFrozenOperateBtn = function(a1,a2,data){
	        return '<button class="btn btn-mf-primary btn-memberfrozen" data-id='+data.userId+' type="button"><i class="fa fa-lock" title="会员冻结"></i></button>';
		}
    	
    	var _fnStatus = function(data){
    		if (data.status == 'A') {
				return '正常'
			} else if (data.status == 'F') {
				return '注销';
			}
		}
		
		var options = {};
		options.tableId = '#table-frozen-pending';
		options.sAjaxSource = global.context + '/web/normalUsers';
		options.functionList = {'_fnStatus': _fnStatus, '_fnOperateBtn': _fnMemberFrozenOperateBtn};
		options.aaSorting=[[0,"asc"]];
		member_frozen_table = datatables.init(options);
		member_frozen_table.setParams(util.getSearchData('#form-frozen-search'));
		return member_frozen_table.create();
    }
    
    ht.app.renderMemberUnfrozenTable = function() {
    	var _fnMemberFrozenOperateBtn = function(a1,a2,data){
	        return '<button class="btn btn-mf-primary btn-memberunfrozen" data-id='+data.userId+' type="button"><i class="fa fa-lock" title="会员冻结"></i></button>';
		}
		
		var options = {};
		options.tableId = '#table-unfrozen-pending';
		options.sAjaxSource = global.context + '/web/frozenUsers';
		options.functionList = {'_fnOperateBtn': _fnMemberFrozenOperateBtn};
		options.aaSorting=[[0,"asc"]];
		member_unfrozen_table = datatables.init(options);
		member_unfrozen_table.setParams(util.getSearchData('#form-frozen-search'));
		return member_unfrozen_table.create();
    }
    
    ht.app.renderLogTable = function(){
    	var options = {};
		options.tableId = '#table-log';
		options.sAjaxSource = global.context+"/web/fund/user/acct/frozenlog/";
		options.aaSorting=[[0,"asc"]];
		frozenlog_table = datatables.init(options);
		frozenlog_table.setParams([{name:"acctNo",value:""}]);
		return frozenlog_table.create();
    }
    
    ht.app.toSearch = function(){
    	var searchBtn = $('#btn-search');
    	var tabActive = $('#frozen-tabs');
    	ht.ui.clickSearch(searchBtn, tabActive);
    }
    
    ht.app.toMemberSearch = function() {
    	var searchBtn = $('#btn-membersearch');
    	var memberTabActive = $('#memberfrozen-tabs');
    	ht.ui.clickMemberSearch(searchBtn, memberTabActive);
    }
    
    ht.app.toEnterSearch = function(){
    	var acctnoTxt = $('#acctno');
    	var username = $('#username');
    	var searchBtn = $('#btn-search');
    	ht.ui.enterKeyup(acctnoTxt, searchBtn);
    	ht.ui.enterKeyup(username, searchBtn);
    }
    
    ht.app.toFrozen = function(){
    	var frozenTable = $('#table-frozen');
    	ht.ui.clickFrozenBtn(frozenTable);
    }
    
    ht.app.toUnfrozen = function(){
    	var unfrozenTable = $('#table-unfrozen');
    	ht.ui.clickUnfrozenBtn(unfrozenTable);
    }
    
    ht.app.toMemberFrozen = function(){
    	var memberFrozenTable = $('#user-frozen-pending-list');
    	ht.ui.clickMemberFrozenBtn(memberFrozenTable);
    }
    
    ht.app.toMemberUnfrozen = function(){
    	var memberUnfrozenTable = $('#user-unfrozen-pending-list');
    	ht.ui.clickMemberUnfrozenBtn(memberUnfrozenTable);
    }
    
    ht.app.toValidate = function(){
    	var option={
    		url:null,
    		action:null,
    		context:null
    	};
    	
    	$("#frozen-modal").validate({
    		submitHandler:function(form){
    			var message = '';
    			var modal_title = $('#modal-title').html();
    			if ('账户冻结' == modal_title){
    				option.action = global.context+"/web/fund/user/acct/freeze";
    				util.ajax_submit('#frozen-modal', option).done(function(resp){
    					if(resp.code == "ACK"){
    						$("#modal").modal("hide");
    						$("#modal-confirm-info").html('账号冻结成功');
    						$("#confirmModal").modal().data("refresh",true);
    						frozen_table.setParams(util.getSearchData('#form-frozen-search'));
    						frozen_table.invoke("fnStandingRedraw");
    					}
    				});
    			}
    			if ('账户解冻' == modal_title){
    				option.action = global.context+"/web/fund/user/acct/unfreeze";
    				util.ajax_submit('#frozen-modal', option).done(function(resp){
    					if(resp.code == "ACK"){
    						$("#modal").modal("hide");
    						$("#modal-confirm-info").html('账号解冻成功');
    						$("#confirmModal").modal().data("refresh",true);
    						unfrozen_table.setParams(util.getSearchData('#form-frozen-search'));
    						unfrozen_table.invoke("fnStandingRedraw");
    					}
    				});
    			}
    			if ('会员冻结' == modal_title){
    				option.action = global.context+"/web/freeze";
    				util.ajax_submit('#frozen-modal', option).done(function(resp){
    					if(resp.code == "ACK"){
    						$("#modal").modal("hide");
    						$("#modal-confirm-info").html('账号冻结成功');
    						$("#confirmModal").modal().data("refresh",true);
    						member_frozen_table.setParams(util.getSearchData('#form-frozen-search'));
    						member_frozen_table.invoke("fnStandingRedraw");
    					}
    				});
    			}
    			if ('会员解冻' == modal_title){
    				option.action = global.context+"/web/unfreeze";
    				util.ajax_submit('#frozen-modal', option).done(function(resp){
    					if(resp.code == "ACK"){
    						$("#modal").modal("hide");
    						$("#modal-confirm-info").html('账号解冻成功');
    						$("#confirmModal").modal().data("refresh",true);
    						member_unfrozen_table.setParams(util.getSearchData('#form-frozen-search'));
    						member_unfrozen_table.invoke("fnStandingRedraw");
    					}
    				});
    			}
    		}
    	});
    }
    
    ht.app.toLog = function(){
    	frozenTable = $('#table-frozen');
    	ht.ui.clickLogBtn(frozenTable);
    }
    
    ht.app.tabs = function(){
    	var frozenTabs = $('#frozen-tabs').find('a');
    	ht.ui.clickTabs(frozenTabs);
    }
    
    ht.app.memberTabs = function() {
    	var frozenMemberTabs = $('#memberfrozen-tabs').find('a');
    	ht.ui.clickTabs(frozenMemberTabs);
    }
    
    $('#form-memberfrozen-search').keydown(function(e) {
    	var keycode = e.which;
    	if (13 == keycode) {
    		$('#btn-membersearch').trigger('click');
    		return false;
    	}
    });
    
    ht.app.renderFrozenTable();
    
    ht.app.renderUnfrozenTable();
    
    ht.app.renderMemberFrozenTable();
    
    ht.app.renderMemberUnfrozenTable();
    
    ht.app.renderLogTable();
    
    ht.app.toSearch();
    
    ht.app.toMemberSearch();
    
    ht.app.toEnterSearch();
    
    ht.app.toFrozen();
    
    ht.app.toUnfrozen();
    
    ht.app.toMemberFrozen();
    
    ht.app.toMemberUnfrozen();
	
    ht.app.toValidate();
    
    ht.app.toLog();
    
    ht.app.tabs();
    
    ht.app.memberTabs();
});
