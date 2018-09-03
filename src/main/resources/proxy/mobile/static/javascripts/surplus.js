var _batchMgr, _batchIds, listRender = function (t) {
    if (t.length <= 0)return void($(".not-data").length <= 0 && $("#list").hide().before('<div class="not-data">没有找到任何数据。</div>'));
    for (var e = 0; e < t.length; ++e) {
        var a = t[e];
        a.create_time = $.dateTimeFormat(a.create_time);
        $(doT.template($("#itemTpl").text())(a)).appendTo($("#list tbody")), $("#list").show()
    }
    _batchMgr = $.batchManagement()
}, initDataList = function (t) {
    var e = {page: 1, flag: !1, size: 20}, a = function () {
        e.flag || (e.flag = !0, $("#list").loading(), setTimeout(function () {
            var a = {token: $.getToken(), type: t, page: e.page, size: e.size};
            $.apiGet("getSurplusRechargeList", a, function (t) {
                listRender(t.data.list);
                var a = Math.ceil(t.data.total / e.size);
                if (e.page >= a)return $("body").rollPage("destroy"), void $("#list").hideLoading();
                e.page++, e.flag = !1
            })
        }, 600))
    };
    $("body").rollPage("destroy"), $("#list").hide().children("tbody").html(""), $(".not-data").remove(), a(), $("body").rollPage("load", function () {
        a()
    })
}, initTypeList = function () {
    $.apiGet("getSurplusRechargeCountList", {token: $.getToken()}, function (t) {
        if (t.data.length <= 0)return void $("#list").hide().before('<div class="not-data">没有找到任何数据。</div>');
        for (var e = "", a = "", i = 0; i < t.data.length; ++i) {
            var o = t.data[i];
            e += '<li class="sui-border-b', 0 == i && (a = o.type, e += " active"), e += '"  data-type="' + o.type + '">' + o.tips + "/" + o.count + "</li>"
        }
        $("#typeList").html(e), initDataList(a), $("#typeList li").on("click", function () {
            if (!$(this).hasClass("active")) {
                var t = $(this).data("type");
                $("#typeList .active").removeClass("active"), $(this).addClass("active"), initDataList(t)
            }
        })
    })
}, initTransferForm = function () {
    $.apiGet("queryProxyListByPreviousProxyName", {token: $.getToken(), page: 1, size: 1e4}, function (t) {
        for (var e = "", a = 0; a < t.data.list.length; ++a) {
            var i = t.data.list[a];
            e += '<option value="' + i.proxy_name + '">' + i.proxy_name + "</option>"
        }
        $("#proxyName").html(e)
    })
};
$(function () {
    initTypeList(), initTransferForm(), $("#transferButton").on("click", function () {
        return _batchMgr ? _batchMgr.values.length <= 0 ? void $.alert("请选择最少一条可操作的数据。") : (_batchIds = _batchMgr.values, void $("#transferPopup").popup("push")) : void $.alert("没有任何数据可供选择。")
    }), $("#soldButton").on("click", function () {
        return _batchMgr ? _batchMgr.values.length <= 0 ? void $.alert("请选择最少一条可操作的数据。") : (_batchIds = _batchMgr.values, void $("#sellPopup").popup("push", function () {
            for (var t = "", e = 0; e < _batchIds.length; ++e)t += _batchIds[e] + "<br/>";
            $(".sell-card-list").html(t)
        })) : void $.alert("没有任何数据可供选择。")
    }), $(".hidePopup").on("click", function () {
        _batchIds = null, $("#transferPopup").closePopup(), $("#sellPopup").closePopup()
    }), $("#submitButon").click(function () {
        var t = $.trim($("#proxyName").val()), e = $.trim($("#remark").val());
        return _batchIds ? t.length <= 0 ? ($.alert("请选择要转让的代理账号。"), !1) : ($.showLoading(), void $.apiPost("makeOverRechargeCodeByProxy", {
            token: $.getToken(),
            toProxyName: t,
            rechargeCodes: _batchIds.join(","),
            remark: e
        }, function () {
            $.alert("转让成功。", function () {
                window.location.reload();
                // $.each(_batchIds, function (t, e) {
                //     $("#rechargeCode_" + e).remove()
                // }), _batchMgr.values = [], _batchIds = null, $("#transferPopup").closePopup()
            })
        })) : ($.alert("参数无效。"), !1)
    }), $("#cellSubmitButon").click(function () {
        var t = $.trim($("#cellRemark").val());
        if (!_batchIds)return $.alert("参数无效。"), !1;
        $.showLoading(), $.apiPost("sellRechargeCodeSign", {
            token: $.getToken(),
            rechargeCode: _batchIds.join(","),
            sellRemark: t
        }, function () {
            $.alert("出售成功。", function () {
                // $.each(_batchIds, function (t, e) {
                //     $("#rechargeCode_" + e).remove()
                // }), _batchMgr.values = [], _batchIds = null, $("#sellPopup").closePopup()
                window.location.reload();
            })
        })
    })
});