/*!
 *
 *  我的账户-交易明细
 *  创建人：陈恩明
 * 	创建日期：2014-05-16
 *
 */
require(['jquery',
        'global',
        'packet_manage',
        'module/util',
        'module/view_pusher',
        'module/report_controller',
        'jquery.popupwindow'
    ],
    function($,global,packet,util,pusher){
        'use strict';
        var init = function(){
            var options = {};
            options.tableId = '#packet-manage-table';
            options.sAjaxSource = global.context+"/web/myaccount/gettradeDetails";
            options.aaSorting = [[0,"desc"]];
            options.functionList = {"btn_oper":returnOperBtn,"btn-detail":returnDetail,"btn-protocol":returnProtocol,"returnRiskLvl":returnRiskLvl};
            packet.renderTable(options);
            packet.init();

        };
        var bindEvent = function(){
            $("body").on("click", ".detail-link", function () {
                var subsId = $(this).attr("data-id");
                var lotId = $(this).attr("data-lot-id");
                getFinancingPackageDetailsForInvestor(subsId,lotId);
                packet.showDetails();
            })//债权转让协议
            
            //查看收益表
            .on("click", ".btn-getlist", function () {
            	var pkgId = $(this).attr("data-id");
            	window.reportController.detail("income.detail",pkgId);
            })
            .on("click", ".btn-getcontract", function () {
            	var lotId = $(this).attr("data-id");
            	var type = $(this).attr("data-type");
            	$.popupWindow(global.context + "/web/report/transfercontract/" + type + "/" + lotId,{"width":"1224","height":"700"});
            });
        };
        var returnOperBtn = function(data,type,full){
            var btnHtml = '<div class="btn-group">';
            btnHtml+= '<button class="btn btn-xs btn-getlist" data-id="'+full.pkgId+'" title="查看收益表"><a href="javascript:void(0);"  style="font-size:12px" title="查看收益表">收益表</a></button>';
            if(full.trdType.code == '02' && full.direction == '卖出') {
            	btnHtml+= '<button class="btn btn-xs btn-getcontract" data-id="'+full.lotId+'" data-type="buyer" title="查看转让协议"><a href="javascript:void(0);"  style="font-size:12px" title="查看转让协议">转让协议</a></button>';
            }
        	btnHtml+='</div>';
            return btnHtml;
        };
        var returnDetail = function(_,__,full){
            var detailLink = '<a class="detail-link" href="javascript:void(0);" data-id="'+full.pkgId+'" data-lot-id="'+full.lotId+'">'+full.pkgName+'</a>';
            if(full.status.text=="已转让"){
                detailLink = full.pkgName;
            }
            return detailLink;
        };
        var returnProtocol = function(){
            var protocolLink = '<span class="view-protocol"><a>查看协议</a></span>';
            return protocolLink;
        };
        var returnRiskLvl = function (_,__,data) {
          /*  var level = data.riskLvl.code;
            var lvlMap = {
                "A":"优质",
                "B":"中等",
                "C":"合格",
                "D":"高风险"
            };*/
            return data.riskLvl.text;
        };

        //获取融资包详情
        var getFinancingPackageDetailsForInvestor = function (subsId,lotId) {
            pusher.push({
                url:global.context + "/web/financingpackage/investor/details/" + subsId,
                type : 'GET',
                element:"#main-wrapper",
                title:"融资详情",
                onHide:function(){
                    $("#supply-user-modal").remove();
                },
                data:{	
                	lotId : lotId
                }
            });
        };
        
        init();
        bindEvent();

    });
