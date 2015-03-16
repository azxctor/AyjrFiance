/*!
 * 
 *  myNotice 
 *  
 */
;require(['jquery','global',
          'module/ajax'],
function($, global){ 
	

	
	var MyNotice = function(){ 
		  
	}; 
	
	MyNotice.prototype = {
		
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
		initMove: function(){ 
			  var speed=40; //数字越大速度越慢 
			  var tabNotice=document.getElementById("demoNotice"),
			  tab1Notice=document.getElementById("demo1Notice"),
			  tab2Notice=document.getElementById("demo2Notice"); 
			  tab2Notice.innerHTML=tab1Notice.innerHTML; 
			  function Marquee(){  
			     if(tab1Notice.offsetWidth-tabNotice.scrollLeft<=0){
			    	 tabNotice.scrollLeft-=tab1Notice.offsetWidth ;
			      } 
			     else{tabNotice.scrollLeft++;} 
			  } 
			  var myMar=setInterval(Marquee,speed),oButstop=document.getElementById('butstop'),oButstart=document.getElementById('butstart');
			  oButstart.style.display="none";
			  oButstop.onclick=function() { clearInterval(myMar); oButstop.style.display="none"; oButstart.style.display="block"; }; 
			  oButstart.onclick=function() {myMar=setInterval(Marquee,speed);oButstop.style.display="block";oButstart.style.display="none";}; 
 		},
		getNoticelistTop: function(){
			var badge = $('#noticeContent');
			var noticeContentlist='';
			var url = global.context + '/web/notice/listTop'; 
			this._ajax(url).done(function(response){  
				if (response != null){ 
						noticeContentlist='';
						$('#noticeContentList1').html('');    
						$('#noticeContentList2').html('');    
						for (var i = 0; i<response.aaData.length; i++){                                               
						 noticeContentlist+="<li>";
						 noticeContentlist+="<a href='"+global.context +"/web/getMessageView/"+response.aaData[i].noticeId+"/NOTICE'>"+response.aaData[i].title+"</a>";
						 noticeContentlist+="<a href='"+global.context +"/web/getMessageView/"+response.aaData[i].noticeId+"/NOTICE'>"+response.aaData[i].title+"</a>";
 						 noticeContentlist+="</li>";
					 	} 
						$('#noticeContentList1').html(noticeContentlist);   
						$('#noticeContentList2').html(noticeContentlist);  
					    if(response.aaData.length>0){
					    	badge.show();
					    }else{
					    	badge.hide();
					    }
						
					}else{
						badge.hide();
					} 
			});
		} 
	};  
	 
	var myNotice = new MyNotice(); 
	myNotice.getNoticelistTop(); 
	myNotice.initMove();
	
}); 	  
 
