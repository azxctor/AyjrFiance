;(function($) {

	$.extend($.validator.prototype,{
		
		findByName: function( name ) {
			if(name){
				name = name.replace(/\[/g, '\\[');
				name = name.replace(/\]/g, '\\]');
			}
			//console.log('enter the customized findByName with name='+name);
			var elem =  $(this.currentForm).find('[data-error-field="'+name+ '"]:visible');
			if(elem.length == 0){
				//console.log('elem not found with data-error-field, try to search with name');
				elem =  $(this.currentForm).find("[name='" + name + "']:visible");
				var kmfex_elem = $(this.currentForm).find("[name='" + name + "']");
				if(elem.length == 0 && kmfex_elem.attr("type")=="password"){
					elem = kmfex_elem;
				}
				if(kmfex_elem.attr("id") == "org-type"){
					elem = kmfex_elem;
				}
				if(elem.length == 0 && kmfex_elem.attr("data-hidden-validate") == "true"){
					elem = kmfex_elem;
				}
			}
			return elem;
		},
		
		idOrName: function( element ) {
			var name = $(element).attr('data-error-field') || element.name;
			return this.groups[name] || (this.checkable(element) ? name : element.id || name);
		}
	});
	
	$.extend($.validator,{
		metadataRules: function(element) {
			if (!$.metadata) return {};
			var meta = $.data(element.form, 'validator').settings.meta;
			return meta ?
			$(element).metadata()[meta] :
			$(element).metadata();
		}
	});
	
	$.extend($.fn,{
		rules: function( command, argument ) {
			var element = this[0];

			if ( command ) {
				var settings = $.data(element.form, "validator").settings;
				var staticRules = settings.rules;
				var existingRules = $.validator.staticRules(element);
				switch(command) {
				case "add":
					$.extend(existingRules, $.validator.normalizeRule(argument));
					// remove messages from rules, but allow them to be set separetely
					delete existingRules.messages;
					staticRules[element.name] = existingRules;
					if ( argument.messages ) {
						settings.messages[element.name] = $.extend( settings.messages[element.name], argument.messages );
					}
					break;
				case "remove":
					if ( !argument ) {
						delete staticRules[element.name];
						return existingRules;
					}
					var filtered = {};
					$.each(argument.split(/\s/), function( index, method ) {
						filtered[method] = existingRules[method];
						delete existingRules[method];
					});
					return filtered;
				}
			}

			var data = $.validator.normalizeRules(
			$.extend(
				{},
				$.validator.metadataRules(element),
				$.validator.classRules(element),
				$.validator.attributeRules(element),
				$.validator.dataRules(element),
				$.validator.staticRules(element)
			), element);

			// make sure required is at front
			if ( data.required ) {
				var param = data.required;
				delete data.required;
				data = $.extend({required: param}, data);
			}

			return data;
		}
	});
	
})(jQuery);