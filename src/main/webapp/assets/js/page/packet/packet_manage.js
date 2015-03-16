/*!
 * 	
 *  融资包管理 - 通用事件绑定
 *  创建人：陈恩明
 * 	创建日期：2014-05-14
 *
 */

define(['jquery',
        'module/datatables',
        'global',
        'module/util',
        'module/view_pusher',
        'bootstrap',
        'module/ajax',
        'bootstrap-datepicker',
        'bootstrap-datepicker.zh-CN',
        'countup',
        'form',
        'requirejs/domready!'],
    function($,datatables,global,util,pusher){
        'use strict';
        var newLogTable;
        var newTable;
        var hoverMsgType;
        //渲染弹窗
        var _renderModal = function(title,content,footBtn){
            var modal = $("#modal");
            var modalTitle = $("#modal-title");
            var modalBody = $("#modal #modal-body");
            var modalFooterWrapper = $("#modal #modal-footer-wrapper");
            modalTitle.empty().append(title);
            modalBody.empty().append(content);
            modalFooterWrapper.empty().append(footBtn);
            modal.modal();
        };

        //渲染table
        var _renderTable = function(options){
            newTable = datatables.init(options);
            newTable.setParams(util.getSearchData("#search-wrapper"));
            return newTable.create();
        };

        var _renderTableByPeriod = function(options){
            newTable = datatables.init(options);
            var period = $("#search-wrapper input[name=period]:checked").val();
            newTable.setParams([{
                name:"period",
                value:period
            }]);
            return newTable.create();
        };
        //渲染btn
        var _renderTableBtn = function(title,selector,icon,id,packageQuota,marketOpen){
        	if(packageQuota){
        		return '<button class="btn btn-xs '+selector+'" data-id="'+id+'" data-packageQuota="'+packageQuota+'" title="'+title+'"><a href="javascript:void(0);" title="'+title+'"><i class="'+icon+'" ></i></a></button>';
        	}else if(marketOpen){
        		return '<button class="gray_im btn btn-xs visualble="false" enable="false"'+selector+'"  title="当前开市中，无法操作"><a href="javascript:void(0);" title="当前开市中，无法操作"><i class="'+icon+'" ></i></a></button>';
        	}
        	else{
        		return '<button class="btn btn-xs '+selector+'" data-id="'+id+'" title="'+title+'"><a href="javascript:void(0);" title="'+title+'"><i class="'+icon+'" ></i></a></button>';
        	}   
        };

        var _renderTableBtnByFont = function(title,selector,icon,id,packageQuota){
            if(packageQuota)      return '<button class="btn btn-xs '+selector+'" data-id="'+id+'" data-packageQuota="'+packageQuota+'" title="'+title+'"><a href="javascript:void(0);" title="'+title+'">'+icon+'</a></button>';
            else   return '<button class="btn btn-xs '+selector+'" data-id="'+id+'" title="'+title+'"><a href="javascript:void(0);"  title="'+title+'">'+icon+'</a></button>';
        };
        //返回详情按钮
        var _returnDetail = function(data,type,full){
            var detailLink = '<a class="detail-link" href="javascript:void(0);" data-id="'+full.id+'">'+full.packageName+'</a>';
            return detailLink;
        };
        //绑定事件
        var _bindEvent = function(){
            var sBody = $("body");
            //绑定返回按钮
            sBody.on("click","#btn-backto-list",function(){
                var  mainWrapper = $("#main-wrapper");
                var detailWrapper = $("#details-wrapper");
                detailWrapper.hide();
                mainWrapper.show("slide");
            });
        };
        //显示详情
        var _showDetails = function(){
            var  mainWrapper = $("#main-wrapper");
            var detailWrapper = $("#details-wrapper");
            detailWrapper.show("slide");
            mainWrapper.hide();
        };
        //绑定datepicker
        var _bindDatePicker = function(format,view){
            var startTime =  $("#search-start-date");
            var endTime = $("#search-end-date");
            startTime.attr({"readonly":"readonly"});
            endTime.attr({"readonly":"readonly"});
            var def_format = 'yyyy-mm-dd';
            var min_view = 0;
            if(format){
            	def_format = format;
            }
            if(view){
            	min_view = view;
            }
            var datePickerSetting = {
                format: def_format,
                weekStart: 1,
                autoclose: true,
                todayBtn: false,
                language: 'zh-CN',
                minViewMode: min_view
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
        
        var _bindDatePickerByClass = function(start,end){
            var startTime =  $(start);
            var endTime = $(end);
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
        

        var _closeModal = function(){
            var $modal = $(".modal");
            $modal.modal("hide");
        };
        //获取融资包详情
        var _getDetails = function(packageId,mode,selector){
        	if(typeof(selector)=="undefined") selector = "#main-wrapper";
        	pusher.push({
        		url:global.context+"/web/financingpackage/details/"+packageId+"/"+mode,
        		type : 'GET',
        		element:selector,
        		title:"融资详情",
        		onHide:function(){
        			$("#supply-user-modal").remove();
        			$("#supply-contract-modal").remove();
        		}
        	});
        };

        function _initLogTable(){
            var options = {};
            options.tableId = '#table-log';
            options.sAjaxSource = global.context+"/web/view/packagelog";
            options.aaSorting=[[0,"desc"]];
            newLogTable = datatables.init(options);
            newLogTable.create();
        };
        _initLogTable();


        var _initLog = function(){
             $('#packet-manage-table').on('click','.view-log',function(){
                var packetId = $(this).find("a").attr('data-id');
                newLogTable.setParams([{name:"packageId",value:packetId}]);
                newLogTable.invoke("fnDraw");
                $('#modal-log').modal();
            });
        };
        var _returnLogBtn = function(data,type,full){
            var btnHtml = '<span class="view-log"><a  data-id="'+full.id+'">日志</a></span>';
            return btnHtml;
        };
        var _returnHoverMsg = function(_,__,data){
            var hoverMsgHtml;
            var hoverMsg;
            if(hoverMsgType=="financier"){
                var last = parseFloat(data.packageQuota)-parseFloat(data.supplyAmount);
                hoverMsg = "申购时间："+data.supplyStartTime+"~"+data.supplyEndTime+"&nbsp;剩余申购额：\n"+last+"元";
            }else if(hoverMsgType=="platform"){
                var last = data.supplyUserCount;
                hoverMsg = "申购时间："+data.supplyStartTime+"~"+data.supplyEndTime+"&nbsp;申购人数：\n"+last;
            }
            hoverMsgHtml = '<span class="hover-msg" data-toggle="tooltip" data-original-title="'+hoverMsg+'">'+data.subsPercent+'</span>';
            return hoverMsgHtml;
        };
        var _initHoverMsg = function(type){
            hoverMsgType = type;
            $("#table-wrapper").on("mouseover",".hover-msg",function(){
                var me = this;
                $(me).tooltip("show");
            });
        };

        var packet = {
            init:function(){
                var $search = $("#btn-search");
                var $keyword =  $("#keyword");
                //页面按钮事件绑定
                $search.click(function(){
                    newTable.setParams(util.getSearchData("#search-wrapper"));
                    newTable.invoke("fnDraw");
                });
                $keyword.keyup(function(e){
                    if(e.keyCode==13){
                        $search.click();
                    }
                });
                _bindEvent();
                _bindDatePicker();
            },
            initLog:_initLog,
            initHoverMsg:_initHoverMsg,
            renderTable:_renderTable,
            renderTableByPeriod:_renderTableByPeriod,
            renderDetails:_returnDetail,
            renderModal:_renderModal,
            renderTableBtn:_renderTableBtn,
            getDetails:_getDetails,
            showDetails:_showDetails,
            closeModal:_closeModal,
            returnLogBtn:_returnLogBtn,
            returnHoverMsg:_returnHoverMsg,
            bindDatePicker:_bindDatePicker,
            bindDatePickerByClass:_bindDatePickerByClass,
            renderTableBtnByFont:_renderTableBtnByFont
        };
        return packet;
    });
