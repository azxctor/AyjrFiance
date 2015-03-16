require(['jquery',
        'bootstrap',
        'global',
        'select2',
        'bootstrap-datepicker',
        'datatables',
        'requirejs/domready!'],
    function($){
        var tableWrapper = $(".table-wrapper");
        var roleNum = 0;
        var oper;
        var renderTitle = function(title,subtitle){
            $("span.right-blk-head-title").html(title);
            $("span.right-blk-head-subtitle").html(subtitle);
        };
        var renderSelect = function(selects){
            var i;
            var html = "";
            for(i = 0;i<selects.length;i++){
                html += "<option>"+selects[i]+"</option>";
            }
            $("#operation-select").empty().append(html);
        };
        var renderModal = function(title,content,buttons){
            $("#myModalLabel").html(title);
            $("#myModal .modal-body").html(content);
            $("#myModal .modal-footer").html(buttons);
        };

        var renderTable = function(head,row,id){
            var i;
            var rows = "";
            for(i = 0;i < 10;i++){
                rows+=row;
            }
            var tableHtml = '<table class="dataTable-micro" id="'+id+'" cellspacing="0" cellpadding="0" border="0">'+head+'\
            <tbody>\
            '+rows+'</tbody>\
            </table>';
            tableWrapper.empty();
            tableWrapper.append(tableHtml);
            $("#"+id).dataTable({
                "bPaginate":true,
                "bFilter":false,
                "bLengthChange": false,
                "iDisplayLength":15,
                "sPaginationType": "full_numbers"
            });
        };
        var loadDefault = function(url,spState){
            var state =  $("#operation-select").val();
            if(spState) state = spState;
            var head = "<thead> <tr> <th>编号</th><th>融资包简称</th> <th>融资额(万)</th>   <th>风险保障</th>   <th>融资期限</th>   <th>年利率</th>   <th>还款方式</th>   <th>担保机构</th>   <th>融资项目级别</th>   <th>状态</th>      </tr></thead>";
            var row = "<tr><td>001</td><td><a href='"+url+"'>服装店融资</a></td>            <td>5,000</td>   <td>有担保</td>   <td>1年</td>   <td>1.1%</td>   <td>一期还清</td>   <td>诚信担保</td>   <td>3级</td><td>"+state+"</td></tr>";
            renderTable(head,row,"table-applying");
        };
        var mainRender = function(){
            $(".popover").hide();
            oper = $("#operation-select").val();
            switch(roleNum){
                case "0"://融资人
                    var url = "common_detail.html?title=2&sub_title=1&role=0&status=10";
                    switch(oper){
                        case "待申购":
                            var head = "<thead> <tr> <th>编号</th><th>融资包简称</th> <th>融资额(万)</th>   <th>风险保障</th>   <th>融资期限</th>   <th>年利率</th>   <th>还款方式</th>   <th>担保机构</th>   <th>融资项目级别</th>   <th>状态</th>      <th>起始时间	</th><th>截止时间</th>  </tr></thead>";
                            var row = "<tr><td>001</td><td><a href='"+url+"'>服装店融资</a></td>            <td>5,000</td>   <td>有担保</td>   <td>1年</td>   <td>1.1%</td>   <td>一期还清</td>   <td>诚信担保</td>   <td>3级</td><td>待申购</td><td>2014-2-14</td><td>2014-12-18</td>   </tr>";
                            renderTable(head,row,"table-applying");

                            break;
                        case "申购中":
                            var head = "<thead> <tr> <th>编号</th><th>融资包简称</th> <th>融资额(万)</th>   <th>风险保障</th>   <th>融资期限</th>   <th>年利率</th>   <th>还款方式</th>   <th>担保机构</th>   <th>融资项目级别</th>   <th>状态</th>      <th>申购进度</th><th>	申购人数	</th><th>剩余申购额</th><th>截止时间</th> <th>操作</th> </tr></thead>";
                            var row = "<tr><td>001</td><td><a href='"+url+"'>服装店融资</a></td>            <td>5,000</td>   <td>有担保</td>   <td>1年</td>   <td>1.1%</td>   <td>一期还清</td>   <td>诚信担保</td>   <td>3级</td><td>申购中</td><td>50%</td><td>400</td><td>400</td><td>2014-5-20</td><td><div class='operation-box'>" +
                                "<a class='stop-apply' href='javascript:void(0);'><i class='fa  fa-check-square fa-lg' title='终止'></i></a>" +
                                "<a class='drop-apply' href='javascript:void(0);' data-toggle='popover' data-container='.right-blk-body' data-placement='right' data-content=' ' ><i class='fa fa-minus-square fa-lg' title='撤单'></i></a></div></td></tr>";
                            renderTable(head,row,"table-applying");
                            break;
                        case "待签约":
                            var head = "<thead> <tr> <th>编号</th><th>融资包简称</th> <th>融资额(万)</th>   <th>风险保障</th>   <th>融资期限</th>   <th>年利率</th>   <th>还款方式</th>   <th>担保机构</th>   <th>融资项目级别</th>   <th>状态</th>      <th>操作</th>  </tr></thead>";
                            var row = "<tr><td>001</td><td><a href='"+url+"'>服装店融资</a></td>            <td>5,000</td>   <td>有担保</td>   <td>1年</td>   <td>1.1%</td>   <td>一期还清</td>   <td>诚信担保</td>   <td>3级</td><td>待签约</td><td>" +
                                "<div class='operation-box'><a class='sign' href='javascript:void(0);' data-toggle='popover' data-container='.right-blk-body' data-placement='right' data-content=' '><i class='fa fa-pencil-square-o  fa-lg' title='签约'></i></a>" +
                                "<a class='drop-apply2' href='javascript:void(0);' data-toggle='popover' data-container='.right-blk-body' data-placement='right' data-content=' '><i class='fa fa-minus-square fa-lg'  title='撤单'></i></a></div></td></tr>";
                            renderTable(head,row,"table-signing");
                            bindDrop2("签约");
                            break;
                        case "还款中":
                            var head = "<thead> <tr> <th>编号</th><th>融资包简称</th> <th>融资额(万)</th>   <th>风险保障</th>   <th>融资期限</th>   <th>年利率</th>   <th>还款方式</th>   <th>担保机构</th>   <th>融资项目级别</th>   <th>状态</th>      <th>查看还款表</th> <th>提前还款</th></tr></thead>";
                            var row = "<tr><td>001</td><td><a href='"+url+"'>服装店融资</a></td>            <td>5,000</td>   <td>有担保</td>   <td>1年</td>   <td>1.1%</td>   <td>一期还清</td>   <td>诚信担保</td>   <td>3级</td><td>还款中</td><td>" +
                                "<div class='operation-box'><a class='check-owe' href='javascript:void(0);'> 查看还款表</a></div></td><td><a class='pay-earlier' href='javascript:void(0);'>提前还款</a></td></tr>";
                            renderTable(head,row,"table-paying");
                            break;
                        default: loadDefault(url);
                    }
                    break;
                case "3"://风控部门 放款审批
                    var url = "common_detail.html?role=3&title=0&sub_title=3&status=12";
                    switch(oper){
                        case'待处理':
                            var head = "<thead> <tr> <th>编号</th><th>融资包简称</th> <th>融资额(万)</th>   <th>风险保障</th>   <th>融资期限</th>   <th>年利率</th>   <th>还款方式</th>   <th>担保机构</th>   <th>融资项目级别</th>   <th>状态</th>      <th>操作</th>  </tr></thead>";
                            var row = "<tr><td>001</td><td><a href='"+url+"'>服装店融资</a></td>            <td>5,000</td>   <td>有担保</td>   <td>1年</td>   <td>1.1%</td>   <td>一期还清</td>   <td>诚信担保</td>   <td>3级</td><td>待放款审批</td><td><div class='operation-box'><a href='common_detail.html?role=3&title=0&sub_title=3&status=12&operate=2'><i class='fa fa-pencil-square-o  fa-lg' title='放款审批'></i></a></div></td></tr>";
                            renderTable(head,row,"table-applying");
                            break;
                        default: loadDefault(url,"还款中");
                    }
                    break;
            }
        }

        var bindDrop2 = function(func){
            $('.sign').popover().on('shown.bs.popover', function () {
                var self = this;
                $('.popover-content').html("<button class='btn btn-mf-primary btn-operate-revoke btn-sign  btn-sm'>"+func+"</button><button class='btn btn-mf-default btn-operate-cancel btn-sm'>取消</button>");
                $('.popover-content').click(function(){
                    $(self).popover('hide');
                });
            });
        }
        var bindEvent = function(){

            $("#operation-select").change(function(){
               mainRender();
            });

            $(".table-wrapper").on("click",".stop-apply",function(){
                var buttons = '<button type="button" class="btn btn-mf-primary" data-dismiss="modal">确定</button>\
                             <button type="button" class="btn btn-mf-default" data-dismiss="modal">取消</button>';
                renderModal("确认终止","终止后将进入待签约状态，确定终止申购融资包？",buttons);
                $("#myModal").modal();
            }).on("click",".drop-apply",function(){
                var buttons = '<button type="button" class="btn btn-mf-primary" data-dismiss="modal">确定</button>\
                             <button type="button" class="btn btn-mf-default" data-dismiss="modal">取消</button>';
                renderModal("确认撤单","系统将从您的账户扣除委托发布履约保证金，是否撤单？",buttons);
                $("#myModal").modal();
            }).on("click",".drop-apply2",function(){
                var buttons = '<button type="button" class="btn btn-mf-primary" data-dismiss="modal">确定</button>\
                             <button type="button" class="btn btn-mf-default" data-dismiss="modal">取消</button>';
                renderModal("确认撤单","系统将从您的账户扣除委托发布履约保证金，是否撤单？",buttons);
                $("#myModal").modal();
            }).on("click",".check-owe",function(){
                $("#modal-owe").modal();
            }).on("click",".check-get",function(){
                $("#modal-get").modal();
            }).on("click",".transfer",function(){
                $("#modal-transfer").modal();
            }).on("click",".pay-earlier",function(){
                var buttons = '<button type="button" class="btn btn-mf-primary" data-dismiss="modal">确定</button>\
                             <button type="button" class="btn btn-mf-default" data-dismiss="modal">取消</button>';
                renderModal("提前还款确认","您的提前还款总额为26351.03元其中剩余利息600.00元，违约金351.03元 请确认您的账户足额，点击确认系统将自动扣除还款额",buttons);
                $("#myModal").modal();
            });
            $("body").on("click",".btn-sign",function(){

                $("#modal-agreement").modal();
            });
        }
        var getRole = function(){
            var url = window.location.href;
            roleNum = url.substr(url.indexOf('role=')+5);
            switch(roleNum){
                case "0"://融资人
                    renderTitle("我的债务","融资包管理");
                    renderSelect(["待申购","申购中","待签约","待放款审批","订单废弃","还款中","逾期还款","代偿中","已完成"]);
                    break;
                case "1"://投资人-债权
                    renderTitle("我的债权","债权管理");
                    renderSelect(["待签约","待放款审批","订单废弃","还款中","逾期还款","代偿中","已完成"]);
                    break;
                case "2"://投资人-债权转让
                    renderTitle("我的债权","债权转让管理");
                    renderSelect(["还款中","转让中","已转让"]);
                    break;
                case "3"://投资人-债权转让
                    renderTitle("风险管理","放款审批");
                    renderSelect(["待处理","已处理"]);
                    break;
            }

        }
        getRole();
        mainRender();
        bindEvent();

        $("#search-start-time,#search-end-time").datepicker({
            format: 'yyyy-mm-dd',
            weekStart: 1,
            autoclose: true,
            todayBtn: false,
            language: 'zh-CN'
        }).on("changeDate",function(ev){
        });

    });
