/*
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * The left menu module for the main frame.
 *
 * Author: Tao Shi
 */

;define(['jquery',
        'global',
        'requirejs/domready!'], 
function($, global){
	
	var MFMenu = function(){
		this._items = [];
		this._selectedMainItem = null;
		this._selectedSubItem = null;
		this._deffer = null;
		this._mw = 100;
		this._sw = 150;
		this._status = MFMenu.FOLDED;
		this.available=false;
	};
	
	MFMenu.setting = {
		menu:'#mf-menu',
		content:'.page-wrapper',
		mainMenu:'.menu-wrapper',
		subMenu:'.submenu-wrapper',
		mainContainer : '.menu-wrapper',
		subContainerWrapper: '.submenu-content',
		subContainer : '.submenu-content > .submenu-inner',
		url:'/web/menu'
	};
	
	MFMenu.FOLDED = 'folded';
	MFMenu.OPENED = 'opened';
	
	MFMenu.prototype = {
		
		init: function(){
			var $this = this;
			if($(MFMenu.setting.menu).length==0){
				this.available=false;
				return this;
			}
			this.available=true;
			// create the menu items according server side json data
			$this._deffer = 
				$.getJSON(global.context + MFMenu.setting.url, 
					function(json){
				if(json && $.isArray(json)){
					for(var i=0; i<json.length; i++){
						if(json[i]){
							var mainMenu = new MainMenuItem(json[i]);
							mainMenu.attach();
							$this._items.push(mainMenu);
						}
					}
				}
			});
			// add listeners for the main menu
			$(MFMenu.setting.mainMenu).on('click', '.menu-blk-el', function(e){
				// clear any actived main menu
				var $a = $(MFMenu.setting.mainMenu)
					.find('.menu-blk-el.actived').each(function(){
						var obj = $(this).data('menu-obj');
						obj.inactive();
					});
				
				// active this clicked item
				var mi = $(this).data('menu-obj');
				mi.active();
				
				if($(this).hasClass('selected')){
					// if sub menu is others, we don't wanna toggle the menu
					if($a.length == 0){
						$this.toggle();	
					}
				}else{
					$this.open();
				}
				// not click on the already active or selected item
				if(($a[0] != this &&
						!$(this).hasClass('selected')) ||
						($(this).hasClass('selected') && $a.length != 0)){
					mi.onclick();
				}
				e.stopPropagation();
				return false;
			});
			// add listeners for the sub menu back
			$(MFMenu.setting.subMenu).on('click', '.submenu-back', function(e){
				if($this._status != MFMenu.FOLDED){
					// turn back the original select
					$this.orgin();
				}
			});
			// add listeners for the outside
			$('body').on('click', function(e){
				// outside the menu
				if(!$(e.target).closest(MFMenu.setting.menu).length){
					if($this._status != MFMenu.FOLDED){
						// turn back the original select
						$this.orgin();
					}
				}
			});
			// compute the width of menu wrapper
			this._mw = $(MFMenu.setting.mainMenu).innerWidth();
			this._sw = $(MFMenu.setting.subMenu).innerWidth();
			return this;
		},
		
		deffer: function(){
			return this._deffer;
		},
		
		orgin: function(){
			// turn back the original select
			$(MFMenu.setting.mainMenu)
				.find('.menu-blk-el.selected')
				.trigger('click');
		},
		
		select: function(mCode, sCode){
			
			var mi, tmi, tsi, d, changed,
				mc = mCode || (this._selectedMainItem && this._selectedMainItem.data().code),
				sc = sCode || (this._selectedMainItem && this._selectedSubItem.data().code);
			
			// clear any actived main menu
			var $a = $(MFMenu.setting.mainMenu)
				.find('.menu-blk-el.actived').each(function(){
					var obj = $(this).data('menu-obj');
					obj.inactive();
				});
			
			// select menu and submenu
			for(var i=0; i<this._items.length; i++){
				mi = this._items[i];
				d = mi.data();
				if(d.code === mc){
					tmi = mi;
					break;
				}
			}
			if(this._selectedMainItem != tmi){
				changed = true;
			}
			if(tmi && tmi.has_children()){
				this._selectedMainItem = tmi;
				tmi.select();
				tmi.attach_children();
				tsi = tmi.get_child(sc);
			}
			if(this._selectedSubItem != tsi){
				changed = true;
			}
			if(tsi){
				this._selectedSubItem = tsi;
				this._selectedSubItem.select();
			}
			if(changed){
				$(MFMenu.setting.menu).trigger('mf.menu.select', [{
					md: this._selectedMainItem ? this._selectedMainItem.data() : {},
					sd: this._selectedSubItem ? this._selectedSubItem.data() : {}
				}]);
			}
			return this;
		},
		
		get_menu_item: function($menuDom){
			var code = $menuDom.attr('data-menu-code'),
				mi, tmi, d;
			for(var i=0; i<this._items.length; i++){
				mi = this._items[i];
				d = mi.data();
				if(d.code === code){
					tmi = mi;
					break;
				}
			}
			return tmi;
		},
		
		toggle: function(){
			if(this._status == MFMenu.OPENED){
				this.fold();
			}else{
				this.open();
			}
		},
		
		open: function(callback){
			var $c = $(MFMenu.setting.content),
				$this = this;
			var isOpening = $c.hasClass('opening');
			if(isOpening){
				$c.stop( true, true );
				$c.removeClass('opening');
				callback && $.isFunction(callback)
					&& callback();
			}else{
				$c.addClass('opening');
				$c.animate({
					'padding-left': this._mw + this._sw
				}, {
					complete: function(){
						$c.removeClass('opening');
						callback && $.isFunction(callback)
							&& callback();
					}
				});
			}
			$this.remove_indicator();
			this._status = MFMenu.OPENED;
		},
		
		fold: function(callback){
			var $c = $(MFMenu.setting.content),
				$this = this;
			var isFolding = $c.hasClass('folding');
			if(isFolding){
				$c.stop( true, true );
				$c.removeClass('folding');
			}
			$c.addClass('folding');
			$c.animate({
				'padding-left': this._mw
			}, {
				complete: function(){
					$c.removeClass('folding');
					$this.stack_indicator();
					callback && $.isFunction(callback)
						&& callback();
				}
			});
			this._status = MFMenu.FOLDED;
		},
		
		stack_indicator: function(){
			var $selected = $('.menu-blk-el.selected');
			var icon = this._selectedSubItem.data().icon;
			$('<div class="on"><i class="fa"></i></div>')
				.appendTo($selected)
				.find('i.fa').addClass(icon);
			
			// render arrow
			this.render_arrow();
		},
		
		remove_indicator: function(){
			$('.menu-blk-el.selected .on').remove();
			$('.menu-ph').remove();
		},
		
		render_arrow: function(){
			var $selected = $('.menu-blk-el.selected');
			$('.menu-ph').remove();
        	var $arrow = $('<div class="menu-ph"><div class="arrow"></div></div>')
        		.hide()
        		.appendTo('body');
        	var coord = $selected.offset();
        	var w = $selected.innerWidth();
        	var h = $selected.innerHeight();
        	$arrow.offset({
        		top: coord.top + h/2, 
        		left: w - 16
        	}).fadeIn();
	    }
	};
	
	var MenuItem = function(data,parent){
		this._status = MenuItem.EMPTY;
		this.parent(parent);
		this.data(data);
	};
	
	MenuItem.EMPTY = '';
	MenuItem.ACTIVED = 'actived';
	MenuItem.SELECTED = 'selected';
	
	
	MenuItem.prototype = {
		
		constructor: MenuItem,
		
		parent: function(p){
			if(p && this._parent != p){
				this._parent = p;
			}else if(p == undefined){
				return this._parent;
			}
		},
		
		data: function(d){
			if(d && this._data != d){
				this._data = d;
				this.render();
			}else if(d == undefined){
				return this._data;
			}
		},
		
		dom: function(){
			return this._dom || $('<div></div>');
		},
		
		render: function(){
			// create the html on the fly
			var d = this._data;
			if(!this._html){
				this._html = $('<li class="submenu-blk-el"><a><i class="fa"></i><span></span></a></li>');		
			}
			if(d){
				this._html.attr('data-menu-code', d.code || '')
					.find('a').attr('href', global.context + '/web/' + d.link || '')
						.find('i.fa').addClass(d.icon || '')
					.parent()
						.find('span').text(d.label || '');
			}
		},
		
		attach: function($dom){
			var $target = $dom || $(MFMenu.setting.subContainer).last();
			if($target.length == 0){
				console.warn('_parent dom is not prepared before rendering the menu item');
				return;
			}
			if(!this._html){
				this.render();
			}
			// attach the html into dom
			if(!this._dom){
				this._dom = this._html
					.clone().appendTo($target)
					.data('menu-obj', this);
			}
		},
		
		detach: function(){
			// detach the html from the dom
			if(this._dom){
				this._dom.remove();
				delete this._dom;
			}
		},
		
		active: function(){
			if(this._status != MenuItem.ACTIVED &&
					this._status != MenuItem.SELECTED)
			{
				this._status = MenuItem.ACTIVED;
				this._html.addClass('actived');
				this._dom && this._dom.addClass('actived');
			}
		},
		
		inactive: function(){
			if(this._status == MenuItem.ACTIVED )
			{
				this._status = MenuItem.EMPTY;
				this._html.removeClass('actived');
				this._dom && this._dom.removeClass('actived');
			}
		},
		
		select: function(){
			if(this._status != MenuItem.SELECTED)
			{
				this._status = MenuItem.SELECTED;
				this._html.addClass('selected');
				this._dom && this._dom.addClass('selected');
			}
		},
		
		unselect: function(){
			if(this._status == MenuItem.SELECTED)
			{
				this._status = MenuItem.EMPTY;
				this._html.removeClass('selected');
				this._dom && this._dom.removeClass('selected');
			}
		},
		
		onclick: function(){
			
		}
	};
	
	var MainMenuItem = function(data){
		this._subItems = [];
		this.data(data);
	};
	
	MainMenuItem.prototype = $.extend(new MenuItem(), {
		
		constructor: MainMenuItem,
		
		has_children: function(){
			return this._data && this._data.subItems
					&& this._data.subItems.length != 0;
		},
		
		get_child: function(sCode){
			var si;
			for(var i=0; i<this._subItems.length; i++){
				si = this._subItems[i];
				if(si.data().code == sCode){
					return si;
				}
			}
		},
		
		render: function(){
			var d = this._data;
			if(!this._html){
				this._html = $('<div class="menu-blk-el"><div class="menu-blk-el-ico"><i class="fa fa-border"></i></div><h4></h4></div>');		
			}
			if(d){
				this._html.attr('data-menu-code', d.code || '')
						.find('i.fa').addClass(d.icon || '')
					.parents('.menu-blk-el')
						.find('h4').text(d.label || '');
			}
			// create the sub items
			if(d.subItems){
				for(var i=0; i<d.subItems.length; i++){
					this._subItems.push(new MenuItem(d.subItems[i], this));
				}
			}
		},
	
		attach: function(){
			if($(MFMenu.setting.mainContainer).length == 0){
				console.warn('_parent dom is not prepared before rendering the menu item');
				return;
			}
			if(!this._html){
				this.render();
			}
			// attach the html into dom
			if(!this._dom){
				this._dom = this._html
					.clone().appendTo(MFMenu.setting.mainContainer)
					.data('menu-obj', this);;
			}
			
		},
		
		attach_children: function(){
			var $w = $(MFMenu.setting.subContainerWrapper);
			var current = parseInt($w.css('left')) || 0;
			// build the second sub menu inner div
			var $new = $('<ul class="submenu-inner"></ul>')
				.appendTo($w)
				.css('left', (0-current + 150)+'px');
			// attach each sub menu items
			for(var i=0; i<this._subItems.length; i++){
				this._subItems[i] && this._subItems[i].attach($new);
			}
			
			if($w.hasClass('moving')){
				$w.stop(false, true);
			}
			$w.addClass('moving');
			$(MFMenu.setting.subContainerWrapper).animate({
				'left' : (current - 150) + 'px'
			}, function(){
				// remove flag class
				$w.removeClass('moving');
				// remove the existing items
				$(MFMenu.setting.subContainer).first().find('.submenu-blk-el')
					.each(function(i){
						var o = $(this).data('menu-obj');
						o && o.detach();
					});
				$(MFMenu.setting.subContainer).first().remove();
			});
		},
		
		detach_children: function(){
			// detach each sub menu items
			for(var i=0; i<this._subItems.length; i++){
				this._subItems[i] && this._subItems[i].detach();
			}
		},
		
		onclick: function(){
			this.attach_children();
		}
	
	});
	
	
	return new MFMenu().init();
	
});