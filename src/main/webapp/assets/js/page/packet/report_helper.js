/*!
 * 	
 *  融资包管理 - 通用事件绑定
 *  创建人：陈恩明
 * 	创建日期：2014-05-14
 *
 */

define(['jquery',
        'jquery.popupwindow',
        'bootstrap-datepicker',
        'bootstrap-datepicker.zh-CN',
        'requirejs/domready!'],
    function($){
        'use strict';
        
        //绑定datepicker
        var _bindDtpsById = function(){
            var startTime =  $("#search-start-date");
            var endTime = $("#search-end-date");
            startTime.attr({"readonly":"readonly"});
            endTime.attr({"readonly":"readonly"});
            var datePickerSetting = {
                format: 'yyyy-mm-dd',
                weekStart: 1,
                autoclose: true,
                todayBtn: false,
                language: 'zh-CN'
            };
            startTime.datepicker(datePickerSetting);
            endTime.datepicker(datePickerSetting);
            //后面时间必须大于前面时间
            startTime.on("changeDate",function(){
                var start = startTime.val();
                endTime.datepicker('setStartDate',start);
            });
            endTime.on("changeDate",function(){
                var end = endTime.val();
                startTime.datepicker('setEndDate',end);
            });
        };
        var _features = {
        		
        };
        var reportHelper = {
            init:function(){
                 
            },
            bindDtpsById:_bindDtpsById
 
        };
        return reportHelper;
    });
