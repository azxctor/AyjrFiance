require(['jquery',
         'global',
         'masonry', 
         'imagesloaded',
         'module/util',
         'jquery-bridget/jquery.bridget',
         'jquery.infinitescroll',
         'bootstrap',
         'module/ajax',
         'plugins',
         'jquery.ui',
         'jquery.spinner',
         'requirejs/domready!'], 
function($, global, Masonry, ImagesLoaded, util, bridget){
	
	// make Masonry a jQuery plugin
	bridget( 'masonry', Masonry );
	
	$('#items-container').masonry({
		itemSelector: '.item',
		gutter: '.gutter',
	});
	
	$('#items-container').infinitescroll({
			navSelector  : '#page-nav', // selector for the paged navigation
			nextSelector : '#page-nav a', // selector for the NEXT link (to page 2)
			itemSelector : '.item', // selector for all items you'll retrieve
			debug        : false,
			animate	 	 : true,
			animationOptions: {
			    duration: 750,
			    easing: 'linear',
			    queue: false
			},
			loading: {
				selector: '#masonry-loading',
				finishedMsg: '<img src="'+global.context+'/assets/img/end.png">',
				msgText: 'Loading...',
				speed: 0,
				finishHide: false 
			},
			state : {
				currPage: -1
			},
			pathParse: function() {
		        return [ global.context + '/web/items/',''];
		    }
		},
		// trigger Masonry as a callback
		function( newElements ) {
			// hide new items while they are loading
			var elems = $( newElements ).hide();
			
			// ensure that images load before adding to masonry layout
			new ImagesLoaded(elems, function(){
				// show elems now they're ready
				$(elems).spinner();
				$('#items-container').append( elems )
				.masonry( 'appended', elems).masonry();
			}); 
	});
	
	$('#items-container').on('click','.item .btn-primary', function(){
        var cart = $('#cart-icon');
        var item = $(this).parents('.item');
        var imgtodrag = item.find(".item-avatar").eq(0);
        var imgmask = item.find(".hover-mask").eq(0);
        var input = item.find('.spinner .form-control');
		$.ajax({
			url: global.context+ '/web/cart',
			dataType: 'json',
			contentType: 'application/json; charset=UTF-8',
			type: 'PUT',
			data: JSON.stringify({
				itemId: item.attr('data-item-id'),
				delta: parseInt(input.val()) || 0
			})
		}).done(function(){
			var num = input.val();
			num = parseInt(num) + parseInt($('#cart-num').text()) || 0;
			if (imgtodrag) {
	            imgtodrag.clone()
	                .offset({
	                top: imgtodrag.offset().top,
	                left: imgtodrag.offset().left
	            }).css({
	                'opacity': '0.8',
                    'position': 'absolute',
                    'z-index': '10000',
                    'width': imgmask.width(),
                    'height': imgmask.height()
	            }).appendTo($('body'))
	              .animate({
	                'top': cart.offset().top + 10,
                    'left': cart.offset().left,
                    'width': imgmask.width()*0.2,
                    'height': imgmask.height()*0.2
	            }, 1000, 'easeInOutExpo', function () {
	            	$('#cart-num').text(num);
					input.val(1);
	             	cart.parent().effect("highlight", 600);
	                $(this).detach();
	            });
	        }else{
	        	$('#cart-num').text(num);
				input.val(1);
	        }
		});
    });
	
	util.set_menu( global.menuItem );
	
	$('#items-container').infinitescroll('retrieve');
});
	

