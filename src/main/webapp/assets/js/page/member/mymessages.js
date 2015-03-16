/*!
 * 
 * mymessage (2014-06-26)
 * Author：jiangang lu
 * 
 */
;require([
          'jquery',
          'global',
          'module/util',
          'module/datatables',
          'module/view_pusher',
          'form',
          'jquery.pnotify',
          'select2',
          'requirejs/domready!'
          ],
function($, global, util, datatables, pusher, form){
	
	var Mymessages = function(){
		var _this = this;
		_this.todo_table = null;
		_this.message_table = null;
		_this.notice_table = null;
		var tabs = $('#message-tabs').find('a'),
		    message_title = $('.todo-message-title'),
		    todo_content = $('#todo-content'),
		    notice_content = $('#notice-content'),
		    tempMessageId = '',
		    message_content = $('#message-content');
		 
		$("#noticeContent").css("diplay","none");    
		
	    tabs.on('click', function(e){
			var that = $(this);
			e.preventDefault();
			that.tab('show');
			var tab_type = that.attr('data-type')
			if ('message' == tab_type){
				_this.message_table.setParams([{name:'category',value:'1'}]);
				_this.message_table.invoke('fnDraw');
				$('#message-hide-right').trigger('click');
			}
			if ('todo' == tab_type){
				_this.todo_table.setParams([{name:'category',value:'2'}]);
				_this.todo_table.invoke('fnDraw');
				$('#todo-hide-right').trigger('click');
			}
			if ('notice' == tab_type){
				_this.notice_table.setParams([{name:'category',value:'5'}]);
				_this.notice_table.invoke('fnDraw');
				$('#notice-hide-right').trigger('click');
			}
		});
		
		$('#message-table').on('click', '.message-title', function(){
			var messageId = $(this).attr('data-id'),
				url = null,
				typeTitle = $(this).attr('class');
			$('#message-list').css('border-right', '2px solid #A9651A');
			$('#message-list').find('i').attr('class', 'fa fa-caret-right fa-lg');
			$('#message-list').attr('class', 'col-sm-6');
			$('.message-list-span').show();
			$('#message-content').find('hr').show();
			$('#message-content').show('1000', function(){
				if (tempMessageId != messageId){
					url = global.context + '/web/message/'+messageId;
					_this._ajax(url).done(function(response){
						tempMessageId = messageId;
						if (response != null){
							message_content.find('h4').html('<small></small>');
							message_content.find('h4').prepend(response.title);
							message_content.find('small').html(response.createTs);
							var message = message_content.find('p').html(response.message).text();
							message_content.find('p').html(message);
							var href = global.context + message_content.find('a').attr('href');
							message_content.find('a').attr('href', href);
							
							_this.message_table.setParams([{name:'category',value:'1'}]);
							_this.message_table.invoke('fnStandingRedraw');
							
							_this.getMessageCounts();
						}
					});
				}
			});
		});
		
		$('.message-list-span').on('click', function(){
			var message_content = $('#message-content');
			var message_list = $('#message-list');
			_this.hideRight(message_content, message_list, $(this));
		});
		
		$('#todo-table').on('click', '.todo-message-title', function(){
			var messageId = $(this).attr('data-id'),
				url = null;
			$('#todo-list').css('border-right', '2px solid #e67e22');
			$('#todo-list').find('i').attr('class', 'fa fa-caret-right fa-lg');
			$('#todo-list').attr('class', 'col-sm-6');
			$('.todo-list-span').show();
			$('#todo-content').find('hr').show();
			$('#todo-content').show('1000', function(){
				if (tempMessageId != messageId){
					var url = global.context + '/web/message/'+messageId;
					_this._ajax(url).done(function(response){
						tempMessageId = messageId;
						if (response != null){
							todo_content.find('h4').html('<small></small>');
							todo_content.find('h4').prepend(response.title);
							todo_content.find('small').html(response.createTs);
							var message = todo_content.find('p').html(response.message).text();
							todo_content.find('p').html(message);
							var href = global.context + todo_content.find('a').attr('href');
							todo_content.find('a').attr('href', href);
							
							_this.todo_table.setParams([{name:'category',value:'2'}]);
							_this.todo_table.invoke('fnStandingRedraw');
							
							_this.getMessageCounts();
						}
					});
				}
			});
		});
		
		$('.todo-list-span').on('click', function(){
			var todo_content = $('#todo-content');
			var todo_list = $('#todo-list');
			_this.hideRight(todo_content, todo_list, $(this));
		});
		
		$('#notice-table').on('click', '.notice-title', function(){
			var noticeId = $(this).attr('data-id'),
			url = null; 
			$('#notice-list').css('border-right', '2px solid #e67e22');
			$('#notice-list').find('i').attr('class', 'fa fa-caret-right fa-lg');
			$('#notice-list').attr('class', 'col-sm-6');
			$('.notice-list-span').show();
			$('#notice-content').find('hr').show();
			$('#notice-content').show('1000', function(){
				if (tempMessageId != noticeId){
					var url = global.context + '/web/notice/'+noticeId;
					_this._ajax(url).done(function(response){
						tempMessageId = noticeId;
						if (response != null){
							notice_content.find('h4').html('<small></small>');
							notice_content.find('h4').prepend(response.title);
							notice_content.find('small').html(response.createTs);
							var message = notice_content.find('p').html(response.content).text();
							notice_content.find('p').html(message);
							var href = global.context + todo_content.find('a').attr('href');
							notice_content.find('a').attr('href', href);
							
							_this.notice_table.setParams([{name:'category',value:'5'}]);
							_this.notice_table.invoke('fnStandingRedraw'); 
							_this.getMessageCounts();
						}
					});
				}
			});
		});
		
		$('.notice-list-span').on('click', function(){
			var notice_content = $('#notice-content');
			var notice_list = $('#notice-list');
			_this.hideRight(notice_content, notice_list, $(this));
		});
		
		
		$('#message-read-true').on('click', function(){
			var url = global.context + '/web/message/update/read';
			_this.isReadOrRemove(url, 'message');
		});
		
		$('#message-read-false').on('click', function(){
			var url = global.context + '/web/message/update/unread';
			_this.isReadOrRemove(url, 'message');
		});
		
		$('#message-selectAll').on('click', function(){
			var oTable = $('#message-table');
			_this.selectAll(this, oTable);
		});
		
		$('#message-remove').on('click', function(){
			$('.remove').attr('id', 'message-remove-true');
			_this.modal();
		});
		
		$('body').on('click', '#message-remove-true', function(){
			var url = global.context + '/web/message/delete';
			_this.isReadOrRemove(url, 'message', 'remove');
		});
		
		$('#todo-read-true').on('click', function(){
			var url = global.context + '/web/message/update/read';
			_this.isReadOrRemove(url, 'todo');
		});
		
		$('#todo-read-false').on('click', function(){
			var url = global.context + '/web/message/update/unread';
			_this.isReadOrRemove(url, 'todo');
		});
		
		$('#todo-selectAll').on('click', function(){
			var oTable = $('#todo-table');
			_this.selectAll(this, oTable);
		});
		
		$('#todo-remove').on('click', function(){
			$('.remove').attr('id', 'todo-remove-true');
			_this.modal();
		});
		
		$('body').on('click', '#todo-remove-true', function(){
			var url = global.context + '/web/message/delete';
			_this.isReadOrRemove(url, 'todo', 'remove');
		});
	};
	
	Mymessages.prototype = {
		
		_ajax: function(url, type, data){
			this.type = type || 'GET';
			return $.ajax({
				url: url,
				type: this.type,
				data: JSON.stringify(data),
				dataType: 'json',
				contentType: 'application/json;charset=utf-8'
			});
		},
		
		toRenderMessageTable: function(){
			$("#noticeContent").css("diplay","none");
			
			var _fnCheckBox = function(data){
				return '<input type="checkbox" name="message-checkList" value="'+data.messageId+'">';
			}
			var _fnMessageTitle = function(data){
				var icon = data.read?'<img src="'+global.context+'/assets/img/message_read.png" title="消息" alt="消息">':'<img src="'+global.context+'/assets/img/message_unread.png" title="消息" alt="消息">';
				var unreadClass = data.read?'':' fwb';
//				if ((data.title).length > 16){
//					data.title = (data.title).substring(0,16) + '...';
//				}
				return '<a class="message-title'+ unreadClass +'" data-id="'+data.messageId+'">'+icon+'&nbsp;'+data.title+'</a>';
			}
			
			var _fnMessageRemoveSelect = function(){
				$('#message-selectAll').removeAttr('checked');
			}
			
			var hide_wrap = $('#hide-wrap');
			var type = hide_wrap.html();
			var page_num = parseInt(hide_wrap.attr('data-page'))-1;
			if ('undefined' == typeof(hide_wrap.attr('data-page'))){
				page_num = 0;
			}
			var option = {};
			option.tableId = '#message-table';
			var that = this;
			option.sAjaxSource = global.context + '/web/message';
			option.rawOptions = {"fnDrawCallback" : _fnMessageRemoveSelect,"fnServerData":function(sSource, aoData, fnCallback){
				var new_aoData = aoData.concat([{name:'category',value:'1'}]);
				if(page_num){
					new_aoData[3].value= page_num*10;
				}
				
	     		$.ajax( {
	     			"type": "POST", 
	     			"url": sSource, 
	     			"contentType": 'application/json; charset=UTF-8',
	     			"dataType": "json",
	     			"data": util.stringify_aoData(new_aoData), 
	     			"success": function(resp) {
	     				fnCallback(resp); //服务器端返回的对象的returnObject部分是要求的格式
	     			}
	     		});
				
			},"fnInitComplete":function(){
				that.message_table.rawTable.fnPageChange(page_num,true);
				page_num = null;
			}};
			option.functionList = {'_fnCheckBox' : _fnCheckBox, '_fnMessageTitle' : _fnMessageTitle};
			this.message_table = datatables.init(option);
			this.message_table.setParams([{name:'category',value:'1'}]);
			return this.message_table.create();
		},
		
		toRenderNoticeTable: function(){ 
			$("#noticeContent").css("diplay","none");
			
			var _fnNoticeTitle = function(data){
				var icon = data.read?'<img src="'+global.context+'/assets/img/notice.png" title="通知公告" alt="通知公告">':'<img src="'+global.context+'/assets/img/notice.png" title="通知公告" alt="通知公告">';
  				return '<a class="notice-title" data-id="'+data.noticeId+'">'+icon+'&nbsp;'+data.title+'</a>';
			};    
			
			var hide_wrap = $('#hide-wrap'); 
			var type = hide_wrap.html();
			var page_num = parseInt(hide_wrap.attr('data-page'))-1;
			if ('undefined' == typeof(hide_wrap.attr('data-page'))){
				page_num = 0;
			}
			var option = {};
			option.tableId = '#notice-table';
			var that = this;
			option.sAjaxSource = global.context + '/web/notice/list';
			option.rawOptions = {"fnServerData":function(sSource, aoData, fnCallback){
			    if($.trim($("#keyword-q").val()).length>0){  
					aoData=aoData.concat([{name:'keyword',value:$("#keyword-q").val()}]);
				}
				var new_aoData = aoData.concat([{name:'category',value:'5'}]);
				if(page_num){
					new_aoData[3].value= page_num*10;
				} 
				$.ajax( {
					"type": "POST", 
					"url": sSource, 
					"contentType": 'application/json; charset=UTF-8',
					"dataType": "json",
					"data": util.stringify_aoData(new_aoData), 
					"success": function(resp) { 
						fnCallback(resp); //服务器端返回的对象的returnObject部分是要求的格式
					}
				});
				
			},"fnInitComplete":function(){
				
				that.notice_table.rawTable.fnPageChange(page_num,true);
				page_num = null;
			}};
			option.functionList = {'_fnNoticeTitle' : _fnNoticeTitle};
			this.notice_table = datatables.init(option);
			this.notice_table.setParams([{name:'category',value:'5'}]);
			return this.notice_table.create();
		},
		
		toRenderToDoTable: function(){
			var _fnCheckBox = function(data){
				return '<input type="checkbox" name="todo-checkList" value="'+data.messageId+'">';
			};
			var _fnToDoTitle = function(data){
				var icon = data.read?'<img src="'+global.context+'/assets/img/todo_read.png" title="待办" alt="待办" width="16px" height="16px">':'<img src="'+global.context+'/assets/img/todo_unread.png" title="待办" alt="待办" width="16px" height="16px">';
				var unreadClass = data.read?'':' fwb';
//				if ((data.title).length > 16){
//					data.title = (data.title).substring(0,16) + '...';
//				}
				return '<a class="todo-message-title'+ unreadClass +'" data-id="'+data.messageId+'">'+icon+'&nbsp;'+data.title+'</a>';
			};
			var _fnTodoRemoveSelect = function(){
				$('#todo-selectAll').removeAttr('checked');
			};
			
			var hide_wrap = $('#hide-wrap');
			var type = hide_wrap.html();
			var page_num = parseInt(hide_wrap.attr('data-page'))-1;
			if ('undefined' == typeof(hide_wrap.attr('data-page'))){
				page_num = 0;
			}
			var option = {};
			var that = this;
			option.tableId = '#todo-table';
			option.sAjaxSource = global.context + '/web/message';
			option.rawOptions = {"fnDrawCallback" : _fnTodoRemoveSelect,"fnServerData":function(sSource, aoData, fnCallback){
				var new_aoData = aoData.concat([{name:'category',value:'2'}]);
				if(page_num){
					new_aoData[3].value= page_num*10;
				}
				
	     		$.ajax( {
	     			"type": "POST", 
	     			"url": sSource, 
	     			"contentType": 'application/json; charset=UTF-8',
	     			"dataType": "json",
	     			"data": util.stringify_aoData(new_aoData), 
	     			"success": function(resp) {
	     				fnCallback(resp); //服务器端返回的对象的returnObject部分是要求的格式
	     			}
	     		});
				
			},"fnInitComplete":function(){
				that.todo_table.rawTable.fnPageChange(page_num,true);
				page_num = null;
			}};
			
			option.functionList = {'_fnCheckBox' : _fnCheckBox, '_fnToDoTitle' : _fnToDoTitle};
			this.todo_table = datatables.init(option);
			this.todo_table.setParams([{name:'category',value:'2'}]);
			return this.todo_table.create();
		},
		
		toGetInfoById: function(){
			var hide_wrap = $('#hide-wrap');
			var messageId = hide_wrap.attr('data-id');
			var type = hide_wrap.html();
			var content = $('#message-content');
			var url = global.context + '/web/message/'+messageId;
			if ('M' == type){
				content = $('#message-content');
				this._ajax(url).done(function(response){
					if (response != null){
						content.find('h4').html('<small></small>');
						content.find('h4').prepend(response.title);
						content.find('small').html(response.createTs);
						var message = content.find('p').html(response.message).text();
						content.find('p').html(message);
						var href = global.context + content.find('a').attr('href');
						content.find('a').attr('href', href);
						$('.message-list-span').show();
						$('#message-content').find('hr').show();
					}
				});
			}else if ('T' == type){
				content = $('#todo-content');
				this._ajax(url).done(function(response){
					if (response){
						content.find('h4').html('<small></small>');
						content.find('h4').prepend(response.title);
						content.find('small').html(response.createTs);
						var message = content.find('p').html(response.message).text();
						content.find('p').html(message);
						var href = global.context + content.find('a').attr('href');
						content.find('a').attr('href', href);
						$('.todo-list-span').show();
						$('#todo-content').find('hr').show();
					}
				});
			}else if ('NOTICE' == type){
				var url = global.context + '/web/notice/'+messageId;
				content = $('#notice-content');
				this._ajax(url).done(function(response){
					if (response){
						content.find('h4').html('<small></small>');
						content.find('h4').prepend(response.title);
						content.find('small').html(response.createTs);
						var message = content.find('p').html(response.content).text();
						content.find('p').html(message);
						var href = global.context + content.find('a').attr('href');
						content.find('a').attr('href', href);
						$('.notice-list-span').show();
						$('#notice-content').find('hr').show();
					}
				});
			}else {
				$('#message-hide-right').trigger('click');
			}
		},
		
		toHideThead: function(){
			var todo_table = $('#todo-table thead');
			var message_table = $('#message-table thead');
			var notice_table = $('#notice-table thead');
			todo_table.hide();
			message_table.hide();
			notice_table.hide();
		},
		
		hideRight: function(content, list, oBtn){
			$('#message-list').css('border-right', 'none');
			$('#todo-list').css('border-right', 'none');
			$('#notice-list').css('border-right', 'none');
			content.hide();
			//oBtn.find('i').attr('class', 'fa fa-caret-left fa-lg');
			oBtn.hide();
			list.attr('class', 'col-sm-12');
		},
		
		isReadOrRemove: function(url, category, operation){
			var _this = this;
			this.category = category;
			var messageIds = [];
			$('table').find('input[type="checkbox"]:checked').each(function(){
				var messageId = $(this).val();
				messageIds.push(messageId);
			});
			var param = {'messageIdList':messageIds};
			if (messageIds && messageIds.length > 0){
				this._ajax(url, 'POST', param).done(function(response){
					if (response != null){
						if ('message' == _this.category){
							_this.message_table.setParams([{name:'category',value:'1'}]);
							_this.message_table.invoke('fnStandingRedraw');
						}
						if ('todo' == _this.category){
							_this.todo_table.setParams([{name:'category', value:'2'}]);
							_this.todo_table.invoke('fnStandingRedraw');
						}
						
						_this.getMessageCounts();
					}
					if ('remove' == operation){
						var text = '删除成功';
						if (!response){
							text = '删除失败';
						}
						$.pnotify({
						    text: text,
						    type: 'success'
						});
						if ('message' == _this.category){
							$('#message-hide-right').trigger('click');
						}
						if ('todo' == _this.category){
							$('#todo-hide-right').trigger('click');
						}
					}
				});
			}
		},
		
		modal: function (){
			var messageIds = [];
			$('table').find('input[type="checkbox"]:checked').each(function(){
				var messageId = $(this).val();
				messageIds.push(messageId);
			});
			if (messageIds && messageIds.length > 0){
				$('#message-alert').modal();
			}
		},
		
		getMessageCounts: function(){
			var badge = $('.badge');
			var url = global.context + '/web/message/getmessagenum';
			this._ajax(url).done(function(response){
				if (response < 1){
					badge.hide();
				}else {
					badge.html(response);
					badge.show();
				}
			});
		},
		
		selectAll: function(element,oTable){
			if ($(element).is(':checked')){
				oTable.find('input[type="checkbox"]').each(function(){
					if (!$(this).is(':checked')){
						$(this).prop('checked', 'checked');
					}
				});
			}else {
				oTable.find('input[type="checkbox"]').each(function(){
					if ($(this).is(':checked')){
						$(this).removeAttr('checked');
					}
				});
			}
		}
	};
	
	var mymessage = new Mymessages();
	
	mymessage.getMessageCounts();
	
	mymessage.toGetInfoById();
	
	mymessage.toRenderToDoTable();
	
	mymessage.toRenderMessageTable();
	
	var table=mymessage.toRenderNoticeTable();
	
	mymessage.toHideThead();
	
	$('#btn-search').on('click', function(){  
		table.setParams(util.getSearchData(".search-withNotice"));
		table.rawTable.fnDraw();
	});
});
