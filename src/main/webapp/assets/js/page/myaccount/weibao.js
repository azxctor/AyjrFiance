/*!
 *
 *  我的账户-小微宝
 *  创建人：陈恩明
 * 	创建日期：2014-05-14
 *
 */
require(['jquery',
        'global',
        'packet_manage',
        'module/util',
        'requirejs/domready!'
    ],
    function($,global,packet,util){
        'use strict';
        var mainTable = null;
        var init = function(){
            var options = {};
            options.tableId = '#weibao-table';
            options.sAjaxSource = global.context+"/web/myaccount/xwbjnlssearch";
            options.aaSorting = [[0,"desc"]];
            mainTable = packet.renderTable(options);
            packet.init();
            getOverview();
        };
        //绑定事件
        var bindEvent = function(){
            $("#form-xwb").validate();
            var detail = $("body");
            //转入按钮
           detail.on("click","#btn-in",function(){
                var modalFooterHtml = '<div class="modal-footer">\
                    <button class="btn btn-mf-primary" id="confirm-in" type="button">转入</button>\
                    <button class="btn btn-mf-default" class="btn btn-default" type="button" data-dismiss="modal">取消</button>\
               			</div>';
                renderModal("小微宝转入","转入金额",modalFooterHtml);
            });
            //转出按钮
           detail.on("click","#btn-out",function(){
                var modalFooterHtml = '<div class="modal-footer">\
                    <button class="btn btn-mf-primary" id="confirm-out" type="button">转出</button>\
                    <button class="btn btn-mf-default" class="btn btn-default" type="button" data-dismiss="modal">取消</button>\
               			</div>';
                renderModal("小微宝转出","转出金额",modalFooterHtml);
            });
            //确定转入
           detail.on("click","#confirm-in",function(){
                if(!$("#form-xwb").validate().valid()) return false;
                util.ajax_submit("#weibao-modal",{
                    action:global.context+"/web/myaccount/xwbrecharge"
                }).done(function(){
                    getOverview();
                });
            });
            //确定转出
           detail.on("click","#confirm-out",function(){
               if(!$("#form-xwb").validate().valid()) return false;
                util.ajax_submit("#weibao-modal",{
                    action:global.context+"/web/myaccount/xwbwithdrawal"
                }).done(function(){
                    getOverview();
                });

            });
        };
        //渲染Modal
        var renderModal = function(title,label,footer){
            var sModalTitle = $("#modal-title");
            var sModalForm = $("#form-xwb");
            var sFooter = $("#modal-footer-wrapper");
            var sModal =   $("#modal");
            var sModalInput = $("#modal-input-amount");
            var content = '    <div class="row"><div class="col-xs-5" id="modal-label">'+label+'</div>\
                <div class="col-xs-4">\
                    <div class="input-group">\
                        <input class="form-control" id="modal-input-amount"  name="amount" data-validate="{required:true,fixednumber:2}" maxlength="10"  type="text"/> <span class="input-group-addon">元 </span>\
                    </div>\
                </div>\
            </div>';
            sModalTitle.empty().append(title);
            sModalForm.empty().append(content);
            sFooter.empty().append(footer);
            sModalInput.val("");
            sModal.modal();
        };

        //刷新页面数据
        var getOverview = function(){
            var sModal =   $("#modal");
            sModal.modal("hide");
            $.ajax({
                url:global.context+"/web/myaccount/xwboverview",
                type:"GET",
                dataType:"json",
                contentType:"application/json",
                success:function(data){
                    var stotalAmount = $("#total-amount");
                    var sTotalProfit = $("#total-profit");
                    var mTotalAmount = $(".modal-total-amount");
                    stotalAmount.empty().append(data.totalAmount);
                    sTotalProfit.empty().append(data.totalProfit);
                    var amountCount = new countUp("total-amount", 0, data.totalAmount, 2, 1);
                    var profitCount = new countUp("total-profit", 0, data.totalProfit, 2, 1);
                    amountCount.start();
                    profitCount.start();
                    mTotalAmount.empty().append(util.get_thousand(data.totalAmount));
                }
            });
            mainTable.invoke("fnDraw");
        }

        init();
        bindEvent();
    });
