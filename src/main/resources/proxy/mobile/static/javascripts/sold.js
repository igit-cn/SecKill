var _batchMgr, listRender = function (t) {
    if (t.length <= 0)return void($(".not-data").length <= 0 && $("#list").hide().before('<div class="not-data">没有找到任何数据。</div>'));
    for (var e = ["正常", "冻结", "删除"], a = ["sui-green", "sui-red", "sui-gray"], i = $.getLoginInfo(), r = 0; r < t.length; ++r) {
        var o = t[r];
        o.create_time_label = "购买时间", 1 == $(".tab .active").data("fromtype") && (o.create_time_label = "转让时间"), o.statusText = e[o.status], o.statusColor = a[o.status], i.proxy_name == o.from_proxy_name && (o.from_proxy_name = "自己"), "" == o.recharge_account && (o.recharge_account = "----"), o.create_time = $.dateTimeFormat(o.create_time), o.sell_time = $.dateTimeFormat(o.sell_time), o.recharge_time = $.dateTimeFormat(o.recharge_time), "" == o.create_time && (o.create_time = "----"), "" == o.sell_time && (o.sell_time = "----"), "" == o.recharge_time && (o.recharge_time = "----"), o.fromtype = $(".tab .active").data("fromtype");
        $(doT.template($("#itemTpl").text())(o)).appendTo($("#list"))
    }
    $(".detail-btn").off("click").on("click", function () {
        var t = (JSON.parse($(this).children("div").text()), $(doT.template($("#detailTpl").text())(o)));
        $.alert('<div class="detail">' + t.html() + "</div>", "详情")
    }), $("#list").show(), $("#list-hd").show(), _batchMgr = $.batchManagement()
}, initDataList = function (t) {
    void 0 === t && (t = {});
    var e = {page: 1, flag: !1, size: 15}, a = function () {
        e.flag || (e.flag = !0, $("#loadWrap").loading(), setTimeout(function () {
            var a = ($.getLoginInfo(), $(".tab .active").data("fromtype"));
            t = $.extend({
                token: $.getToken(),
                fromType: a,
                page: e.page,
                size: e.size
            }, t), $.apiGet("getRechargeCodeTransferOrUsed", t, function (t) {
                listRender(t.data.list);
                var a = Math.ceil(t.data.total / e.size);
                if (e.page >= a)return $("body").rollPage("destroy"), void $("#loadWrap").hideLoading();
                e.page++, e.flag = !1
            })
        }, 600))
    };
    $("body").rollPage("destroy"), $("#list").html(""), $(".not-data").remove(), a(), $("body").rollPage("load", function () {
        a()
    })
};
$(function () {
    initDataList(), $.apiGet("getRechargeCodeTypeList", {token: $.getToken()}, function (t) {
        for (var e = '<option value="">全部</option>', a = 0; a < t.data.length; ++a) {
            var i = t.data[a];
            e += '<option value="' + i.type + '">' + i.tips + "</option>"
        }
        $("#rechargeType").html(e)
    }), $("#showFilterButton").on("click", function () {
        var t = $("#beginTime").data("date"), e = $("#endTime").data("date");
        ($.empty(t) || $.empty(e)) && ($("#timeType option:eq(0)").prop("selected", !0), $("#beginTime").val(""), $("#endTime").val("")), $("#filterPopup").popup("push")
    }), $("#hidePopup").on("click", function () {
        $("#filterPopup").closePopup()
    }), $(".input-date input").on("click", function () {
        var t = $(this);
        $.datePicker({
            title: t.attr("placeholder"), currentDate: t.data("date"), onSelect: function (e) {
                if (e) {
                    var a = e.year + "年" + e.month + "月" + e.date + "日 " + e.week;
                    t.data("date", e.str).val(a)
                } else t.data("date", "").val("")
            }
        })
    });
    var t = {};
    $("#searchButton").on("click", function () {
        var e = {}, a = $("#beginTime").data("date"), i = $("#endTime").data("date");
        $.empty(a) || $.empty(i) || (e[$("#timeType").val() + "BeginTime"] = a, e[$("#timeType").val() + "EndTime"] = i);
        var r = $.trim($("#field").val()), o = $.trim($("#keyword").val());
        $.empty(o) || (e[r] = o);
        var n = $.trim($("#rechargeType").val());
        $.empty(n) || (e.rechargeType = n);
        var l = $.trim($("#status").val());
        $.empty(l) || (e.status = l);
        var s = $.trim($("#sellStatus").val());
        $.empty(s) || (e.sellStatus = s);
        var c = $.trim($("#rechargeStatus").val());
        $.empty(c) || (e.rechargeStatus = c), t = e, initDataList(t), $("#filterPopup").closePopup()
    }), $(".tab li").on("click", function () {
        $(this).hasClass("active") || ($(".tab .active").removeClass("active"), $(this).addClass("active"), $("#list-hd").hide(), initDataList(t))
    }), $(".batch-btn").on("click", function () {
        if (!_batchMgr)return void $.alert("没有任何数据可供选择。");
        if (_batchMgr.values.length <= 0)return void $.alert("请选择最少一条可操作的数据。");
        var t = _batchMgr.values, e = $(this).data("status"), a = ["恢复", "冻结", "删除"];
        $.confirm("是否确定要执行" + a[e] + "操作？", function () {
            $.showLoading(), $.apiPost("resetRechargeStatusByProxy", {
                rechargeIds: t.join(","),
                status: e,
                token: $.getToken()
            }, function () {
                var a = ["正常", "冻结", "删除"], i = ["sui-green", "sui-red", "sui-gray"];
                $.each(t, function (t, r) {
                    $("#status" + r).attr("class", "status").addClass(i[e]).text(a[e]), $(".checkbox").prop("checked", !1), $.toastSuccess()
                }), _batchMgr.values = []
            })
        })
    })
});