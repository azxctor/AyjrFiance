require(['jquery',
        'global',
        'module/datatables',
        'module/util',
        'bootstrap',
        'select2',
        'bootstrap-datepicker',
        'requirejs/domready!'],
    function($,global,datatables,util){
        'use strict';
        var renderModal = function(title,content,buttons){
            $("#myModalLabel").html(title);
            $("#myModal .modal-body").html(content);
            $("#myModal .modal-footer").html(buttons);
        }
        var bindEvent = function(){
            var renderTable = function(){
                var options = {};
                options.tableId = '#invest-table';
                options.sAjaxSource = global.context+"";
                options.aaSorting= [[0,"desc"]];
            /*    options.functionList={"_fnOperateBtn":_fnOperateBtn,"_fnLogLink":_fnLogLink};*/
                datatables.init(options);
                datatables.setParams(util.getSearchData("#form-finance-search"));
                return datatables.create();
            };
            var tbList = renderTable();

/*            var invest_table = $("#invest-table").dataTable({
                "bPaginate":true,
                "bFilter":false,
                "sScrollX": "110%",
                "bAutoWidth": false,
                "bLengthChange": true,
                "iDisplayLength":15,
                "sPaginationType": "full_numbers"
            });*/

            $('.drop-apply').popover().on('shown.bs.popover', function () {
                var self = this;
                $('.popover-content').html("<button class='btn btn-mf-primary btn-operate-revoke  btn-sm'>撤单</button><button class='btn btn-mf-default btn-operate-cancel btn-sm'>取消</button>");
                $('.popover-content').click(function(){
                    $(self).popover('hide');
                });
            });

            $(".table-wrapper").on("click",".drop-transfer",function(){
                var buttons = '<button type="button" class="btn btn-mf-primary" data-dismiss="modal">确认</button>\
                             <button type="button" class="btn  btn-mf-default" data-dismiss="modal">取消</button>';
                renderModal("确认终止","是否确认撤销转让？",buttons);
                $("#myModal").modal();
            }).on("click",".check-get",function(){
                $("#modal-get").modal();
            }).on("click",".transfer",function(){
                $("#modal-transfer").modal();
            });
            $("body").on("click",".btn-sign",function(){
                $("#modal-agreement").modal();
            });

            $("#search-start-date,#search-end-date").datepicker({
                format: 'yyyy-mm-dd',
                weekStart: 1,
                autoclose: true,
                todayBtn: false,
                language: 'zh-CN'
            });

            $("#search-start-date").on("changeDate",function(){
                var start = $("#search-start-date").val();
                $("#search-end-date").datepicker('setStartDate',start);
            });

            $("#search-end-date").on("changeDate",function(){
                var end = $("#search-end-date").val();
                $("#search-start-date").datepicker('setEndDate',end);
            });

        };

        bindEvent();



    });
