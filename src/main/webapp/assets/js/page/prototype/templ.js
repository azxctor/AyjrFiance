require(['jquery'],
function($){
	var weekdays = ["SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"];
    //同步时间函数
    var _syncClock = function()
    {
        var date = new Date();
        var hour = _fillNumber(date.getHours());
        var minute = _fillNumber(date.getMinutes());
        var month = _fillNumber(date.getMonth() + 1);
        var day = _fillNumber(date.getDate());
        var week = weekdays[date.getDay()];
        $(".top-time .hours").text(hour + ":" + minute);
        $(".top-time .date").text(month + "-" + day);
        $(".top-time .week").text(week);
    };
    //0补全函数
    var _fillNumber=function(number) {
        if (number < 10) {
            return "0" + number;
        }
        else {
            return number;
        }
    };
    $(".menu-blk-el").on("click",function(){
        var me = this;
        var w = $(".page-wrapper");
        var s = $(".submenu-wrapper");
        var sub = $(me).find(".sub-content").clone();
        if(w.is(":animated")) return;
        if($(".menu-blk-el.on").length==0){
            //弹出
            $(".menu-blk-el.on").removeClass("on");
            $(me).addClass("on");
            s.find(".submenu-content").empty().append(sub);
            var sub_width = parseInt(s.css("width"));
            sub_width += 100;
            s.show();
            w.animate({ "padding-left":sub_width});
            s.animate({ "left":"100px"});
        }else{
            if($(me).hasClass("on")&& !$(me).hasClass("active")){
                $(me).removeClass("on");
                closeMenu();
            }else{
                $(".menu-blk-el.on").removeClass("on");
                $(me).addClass("on");
                closeMenu(function(){
                    s.find(".submenu-content").empty().append(sub);
                    var sub_width = parseInt(s.css("width"));
                    sub_width += 100;
                    s.show();
                    w.animate({ "padding-left":sub_width});
                    s.animate({ "left":"100px"});
                });
            }
        }

    });
    $(".submenu-back").on("click",function(){
        $(".menu-blk-el.on").removeClass("on");
        closeMenu();
    });

    var closeMenu = function(callback){
        var w = $(".page-wrapper");
        var s = $(".submenu-wrapper");
        w.animate({ "padding-left":"100px"});
        s.animate({ "left":"-100px"},function(){
            s.hide();
            if(callback) callback();
        });
    };
    _syncClock();
    setInterval(_syncClock, 3000);
});