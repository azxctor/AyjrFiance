/*!
 * 
 *  mytodo (2014-06-25)
 *  Author：jiangang lu
 *  
 */
;require(['jquery',
          'global',
          'module/datatables',
          'sockjs',
          'stomp',
          ],
function($, global, datatables){
	
	var Mytodo = function(){
		var _this = this;
		_this.mytodo_table = null;
		_this.flag = true;
		var _messageBtn = $('#message'),
		    _mytodo = $('#mytodo'),
		    _mytodoTable = $('#mytodo-table thead');
		
		_messageBtn.on('click', function(){
			var _option_html = '',
				show_all_message = $('.show-all-message').find('a');
			_mytodoTable.hide();
			if ('none' == $('#mytodo').css('display')){
				var url = global.context + '/web/message/unread';
				_this._ajax(url, 'POST').done(function(response){
					if (response != null){
						for (var i = 0; i<response.aaData.length; i++){
							var icon = response.aaData[i].type == 'M'?'<img src="'+global.context+'/assets/img/message_unread.png" title="消息" alt="消息">':'<img src="'+global.context+'/assets/img/todo_unread.png" title="待办" alt="待办" width="16px" height="16px">';
							_option_html += 
								'<tr>' +
									'<td width="550px">'+ icon +'&nbsp;<a class="show-message" data-id='+response.aaData[i].messageId+' data-type='+response.aaData[i].type+' href="'+global.context+'/web/getMessageView/'+response.aaData[i].messageId+'/'+response.aaData[i].type+'">'+ response.aaData[i].title +'</a></td>' +
									'<td>'+ response.aaData[i].createTs +'</td>' +
								'</tr>'
						}
						$('#mytable').append(_option_html);
						show_all_message.show();
					}
					_mytodo.slideDown('normal');
				});
			}else{
				show_all_message.hide();
				_mytodo.slideUp('normal');
				$('#mytable').find('tr').remove();
			}
		});
	};
	
	Mytodo.prototype = {
		
		_ajax: function(url, type, param){
			this.type = type || 'GET';
			return $.ajax({
				url: url,
				type: this.type,
				contentType: 'application/json',
				dataType: 'json',
				data: JSON.stringify(param)
			});
		},
		
		renderTable: function(){
			var that = this;
			var option = {};
			option.tableId = '#mytodo-table';
			option.sAjaxSource = global.context + '/web';
			that.mytodo_table = datatables.init(option);
			return that.mytodo_table.create();
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
	};
	
//	var socket = new SockJS(global.context + "/web/ws");
//	var stompClient = Stomp.over(socket);
//	stompClient.debug = false;
//
//	var errorCallback = function(error) {
//		console.log(error);
//	};
//
//	var connectCallback = function() {
//		stompClient.connected = true;
//		_fnsubscribe();
//	};
//
//	stompClient.connect({}, connectCallback, errorCallback);
//	
//	function _fnrenderCallback(frame) {
//		$('.badge').html(frame);
//	}
//	
//	function _fnsubscribe(){
//		if (!stompClient.connected) return;
//		if (stompClient.subscription) {
//			stompClient.subscription.unsubscribe();
//			stompClient.subscription = null;
//		}
//		stompClient.subscription = stompClient.subscribe(global.context + '/web/message/getmessagenum', _fnrenderCallback);
//		$.ajax({
//			url : '',
//			type : 'post',
//			contentType:'application/json;charset=utf-8',
//			data: JSON.stringify(),
//			error:function(){
//			},
//			success : function(response) {
//				var topic = $.parseJSON(response).data;
//				if (topic != null) {
//					stompClient.subscription = stompClient.subscribe(global.context + '/web/message/getmessagenum', _fnrenderCallback);
//				}
//			}
//		});
//	};
	
	var mytodo = new Mytodo();
	
	mytodo.getMessageCounts();
})
