var _transfer_proxy = null, detailRender = function (t, e) {
    var o = $(t).parents(".btn-group").next(".detail");
    if (o.is(":visible"))return void o.hide();
    $.showLoading(), $.apiGet("getMakeOverChildDetail", {token: $.getToken(), childProxyName: e}, function (t) {
        var e = "";
        if (t.data.length <= 0)return void o.addClass("not").html("<div>无数据~!</div>").show();
        console.log(t.data);
        for (var a = 0; a < t.data.length; ++a) {
            var r = t.data[a];
            e += "<tr><td>" + $.dateTimeFormat(r.time) + "</td><td>" + r.type + "</td><td>" + r.count + "</td></tr>"
        }
        o.find("tbody").html(e), o.show()
    })
}, editRemark = function (t, e, o) {
    $.prompt({
        title: "修改备注", placeholder: "请输入备注信息。", input: o, empty: !0, onOk: function (o) {
            $.showLoading(), $.apiPost("addRemark", {token: $.getToken(), proxyName: e, remark: o}, function () {
                $("#remark_" + e).text(o), $(t).data("remark", o), $.alert("备注修改成功！")
            })
        }
    })
}, listRender = function (t) {
    if (t.length <= 0)return void $("#list").hide().before('<div class="not-data">没有找到任何数据。</div>');
    for (var e = 0; e < t.length; ++e) {
        var o = t[e];
        o.register_time = $.dateTimeFormat(o.register_time);
        $(doT.template($("#itemTpl").text())(o)).appendTo($("#list"))
    }
    $(".btn-edit").off("click").on("click", function () {
        var t = $(this).data("proxy"), e = $(this).data("remark");
        editRemark(this, t, e)
    }), $(".btn-detail").off("click").on("click", function () {
        var t = $(this).data("proxy");
        detailRender(this, t)
    }), $(".btn-transfer").off("click").on("click", function () {
        _transfer_proxy = $(this).data("proxy"), $("#codeType option").eq(0).prop("selected", !0), $("#remark").val(""), $("#count").val(""), $("#transferPopup").popup("push")
    })
};
$(function () {
    var t = {page: 1, flag: !1, size: 15}, e = function () {
        t.flag || (t.flag = !0, $("#list").loading(), setTimeout(function () {
            $.getLoginInfo();
            $.apiGet("queryProxyListByPreviousProxyName", {
                token: $.getToken(),
                page: t.page,
                size: t.size
            }, function (e) {
                listRender(e.data.list);
                var o = Math.ceil(e.data.total / t.size);
                if (t.page >= o)return $("body").rollPage("destroy"), void $("#list").hideLoading();
                t.page++, t.flag = !1
            })
        }, 600))
    };
    e(), $("body").rollPage("load", function () {
        e()
    }), initForm(), $("#hidePopup").on("click", function () {
        _transfer_proxy = null, $("#transferPopup").closePopup()
    })
});
var initForm = function () {
    $.apiGet("getRechargeCodeTypeList", {token: $.getToken()}, function (t) {
        for (var e = "", o = 0; o < t.data.length; ++o) {
            var a = t.data[o];
            e += '<option value="' + a.type + '">' + a.tips + "</option>"
        }
        $("#codeType").html(e)
    }), $("#submitButon").on("click", function () {
        var t = $.trim($("#codeType").val()), e = _transfer_proxy, o = $.trim($("#count").val()), a = $.trim($("#remark").val());
        return e ? t.length <= 0 ? ($.alert("请选择点卡类型。"), !1) : /^\d+$/.test(o) ? ($.showLoading(), console.log({
            token: $.getToken(),
            rechargeCodeType: t,
            toProxyName: e,
            count: o,
            remark: a
        }), void $.apiPost("makeOverRechargeCodeByType", {
            token: $.getToken(),
            rechargeCodeType: t,
            toProxyName: e,
            count: o,
            remark: a
        }, function (t) {
            $.alert("密卡转让成功。", function () {
                _transfer_proxy = null, $("#transferPopup").closePopup()
            })
        })) : ($.alert("个数不是一个有效的数字。"), !1) : ($.alert("参数有误。"), !1)
    })
};