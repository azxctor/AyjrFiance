require(['global'], function(global){
	require.config({
		baseUrl: global.context + '/assets/js',
		waitSeconds: 200,
		urlArgs : '',
		paths : {
			// the left side is the module ID, the right side is the path to
			// the jQuery file, relative to baseUrl. Also, the path should NOT include
			// the '.js' file extension. This example is using jQuery located at
			// vendor/jquery/jquery-1.11.0.js, relative to the HTML page.
			'jquery' : '../vendor/jquery/jquery-1.11.0.min',
			'bootstrap' : '../vendor/bootstrap/js/bootstrap.min',
			'bootstrap-datepicker' : '../vendor/bootstrap-datepicker/js/bootstrap-datepicker',
			'bootstrap-datepicker.zh-CN' : '../vendor/bootstrap-datepicker/js/bootstrap-datepicker.zh-CN',
			'bootstrap-datetimepicker' : '../vendor/bootstrap-datatimepicker/js/bootstrap-datetimepicker.min',
			'bootstrap-switch' : '../vendor/bootstrap-switch/js/bootstrap-switch.min',
			'bootstrapx-popoverx' : '../vendor/bootstrapx-popoverx/bootstrapx-popoverx',
            'bootstrap-tooltip' : '../vendor/bootstrap-tooltip/js/bootstrap-tooltip',
			'jquery.ui' : '../vendor/jquery-ui/js/jquery-ui.min',
			'jquery-ui-multiselect' : '../vendor/jquery-ui-multiselect/js/jquery.multiselect.min',
			'jquery.validate' : '../vendor/jquery-validation/jquery.validate',
			'jquery.validate.fix' : '../vendor/jquery-validation/jquery.validate.fix',
			'jquery.validate.language' : '../vendor/jquery-validation/localization/messages_zh',
			'jquery.validate.methods' : '../vendor/jquery-validation/additional-methods',
			'jquery.metadata' : '../vendor/jquery-validation/jquery.metadata',
			'jquery.spinner' : '../vendor/jquery.spinner/jquery.spinner.min',
			'jquery.pnotify' : '../vendor/pnotify/jquery.pnotify.min',
			'jquery-bridget/jquery.bridget': '../vendor/masonry/masonry.pkgd',
			'jquery.lazyload' : "../vendor/jquery-lazyload/jquery.lazyload",
			'jquery.popupwindow' : "../vendor/jquery-popupwindow/jquery.popupwindow",
			'jquery.printarea' : "../vendor/jquery-printarea/jquery.printarea",
			'jquery.cookie' : "../vendor/jquery.cookie/jquery.cookie",
			'ickeck' : '../vendor/ickeck/icheck.min',
			'holder' : '../vendor/holder/holder-2.3.1',
			'imagesloaded' : '../vendor/imagesloaded/imagesloaded.pkgd',
			'jquery.infinitescroll' : '../vendor/infinite-scroll/jquery.infinitescroll',
			'jquery.nicescroll' : '../vendor/jquery.nicescroll/jquery.nicescroll',
			'masonry' : '../vendor/masonry/masonry.pkgd',
			'modernizr' : '../vendor/modernizr/modernizr-2.7.1',
			'requirejs/text' : '../vendor/requirejs/plugins/text',
			'requirejs/i18n' : '../vendor/requirejs/plugins/i18n',
			'requirejs/domready' : '../vendor/requirejs/plugins/domReady',
			'requirejs/cs' : '../vendor/requirejs/plugins/cs',
			'datatables' : '../vendor/datatables/js/jquery.dataTables',
			'columnfilter': '../vendor/datatables/js/ColumnFilter',
            'fixedcolumns':'../vendor/dataTables.fixedColumns/js/dataTables.fixedColumns',
			'jquery.ui.widget':'../vendor/jquery-ui/js/jquery.ui.widget',
			'jquery.iframe-transport':'../vendor/jquery.iframe-transport/jquery.iframe-transport',
			'jquery.fileupload':'../vendor/jquery.fileupload/jquery.fileupload',
			'select2' : '../vendor/select2/js/select2.min',
			'jquery.placeholder' : '../vendor/jquery-placeholder/jquery.placeholder',
			'templ':'../js/page/prototype/templ',
			'form':'module/form',
            'jquery.migrate':'../vendor/jquery.migrate/jquery.migrate',
			'sockjs' : '../vendor/sockjs/sockjs-0.3.4.min',
			'stomp' : '../vendor/stomp/stomp',
			'underscore' : '../vendor/underscore/underscore',
			'template':'../template',
			'packet_manage':'page/packet/packet_manage',
			'report_helper':'page/packet/report_helper',
			'chartjs' : '../vendor/chartjs/Chart',
			'countup' : '../vendor/count-up/countUp',
			'raphael' : '../vendor/morris/js/raphael',
			'morris' : '../vendor/morris/js/morris',
            'numeral' : '../vendor/numeral/js/numeral.min',
            'raty' : '../vendor/jquery.raty/jquery.raty'
		},
		// Remember: only use shim config for non-AMD scripts,
		// scripts that do not already call define(). The shim
		// config will not work correctly if used on AMD scripts,
		// in particular, the exports and init config will not
		// be triggered, and the deps config will be confusing
		// for those cases.
		shim : {
			'modernizr': {
				// Once loaded, use the global 'Holder' as the
				// module value.
				exports: 'Modernizr'
			},
			'holder': {
				// Once loaded, use the global 'Holder' as the
				// module value.
				exports: 'Holder'
			},
			'bootstrap': {
				// These script dependencies should be loaded before loading
				// bootstrap modual
				deps : [ 'jquery' ]
			},
			'bootstrap-datepicker': {
				// These script dependencies should be loaded before loading
				// bootstrap modual
				deps : [ 'jquery' ]
			},
            'bootstrap-tooltip': {
                // These script dependencies should be loaded before loading
                // bootstrap modual
                deps : [ 'jquery' ]
            },
			'bootstrap-datepicker.zh-CN': {
				// These script dependencies should be loaded before loading
				// bootstrap modual
				deps : [ 'jquery' , 'bootstrap-datepicker']
			},
			'bootstrap-datetimepicker': {
				deps : [ 'jquery' ]
			},
			'bootstrap-switch': {
				// These script dependencies should be loaded before loading
				// bootstrap modual
				deps : [ 'jquery' ]
			},			
			'jquery.ui': {
				deps : [ 'jquery' ]
			},
			'jquery-ui-multiselect': {
				deps : [ 'jquery','jquery.ui' ]
			},
			'jquery.validate': {
				deps : [ 'jquery' ]
			},
			'jquery.spinner' : {
				deps : [ 'jquery' ]
			},
			'jquery.pnotify' : {
				deps : [ 'jquery' ]
			},
			'jquery.infinitescroll' : {
				deps : [ 'jquery' ]
			},
			'jquery.nicescroll' : {
				deps : [ 'jquery' ]
			},
			'jquery.iframe-transport' : {
				deps : [ 'jquery' ]
			},
			'jquery.fileupload' : {
				deps : [ 'jquery','jquery.ui','jquery.iframe-transport' ]
			},
			'jquery.placeholder' : {
				depts : ['jquery']
			},
			'plugins': {
				deps : [ 'jquery.pnotify','jquery.validate','jquery.metadata','bootstrapx-popoverx','jquery.placeholder']
			},
			'datatables': {
				// These script dependencies should be loaded before loading
				// bootstrap modual
				deps : [ 'jquery' ]
			},
			'chartjs': {
				// These script dependencies should be loaded before loading
				// bootstrap modual
				deps : [ 'jquery' ]
			},
			'ickeck': {
				// These script dependencies should be loaded before loading
				// bootstrap modual
				deps : [ 'jquery' ]
			},
			'select2' : {
				deps : [ 'jquery' ]
			},
			'jquery.validate.fix' : {
				deps : ['jquery.validate']
			},
			'jquery.validate.language' : {
				deps : ['jquery.validate']
			},
			'jquery.validate.methods' : {
				deps : ['jquery.validate']
			},
			'jquery.metadata':{
				deps : ['jquery']
			},
			'jquery.popupwindow':{
				deps : ['jquery']
			},
			'jquery.printarea':{
				deps : ['jquery']
			},
			'jquery.cookie':{
				deps : ['jquery']
			},
			'module/cascading_listener':{
				deps:['jquery']
			},
	        'jquery.migrate':{
	            deps : ['jquery']
	        },
            'fixedcolumns':{
                deps : ['jquery','datatables', 'jquery.migrate']
            },
	        'columnfilter':{
                deps : ['jquery','datatables']
            },
            'morris' : {
            	deps : ['jquery','raphael']
            },
            'bootstrapx-popoverx': {
				// These script dependencies should be loaded before loading
				// bootstrap modual
				deps : [ 'jquery','bootstrap' ]
			},
            'raty' : {
            	deps : [ 'jquery']
            }
		}
	});
});