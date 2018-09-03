
var _isDebug = true; // 开启-关闭 调试模式

// ajax请求
(function($) {
    var _apiUrl = '';  // 接口地址
    // 常规ajax请求包装
    var ajax = function(options, type, error) {
        $.showLoading('网络请求中...');
        $.ajax({
            url: _apiUrl + options.url,
            type: type,
            data: JSON.stringify(options.data),
            contentType: 'application/json;charset=utf-8',
            dataType: 'json',
            success: function(res) {
                $.hideLoading();
                if(_isDebug) {
                    console.log('URL\t\t\t%s\nRequest\t\t%o\nResponse\t%o',_apiUrl+options.url,options.data,res);
                }
                try {
                    if(res.returnCode == 'M01001') {
                        options.success(res);
                    } else {
                        if($.isFunction(error)) {
                            error(res);
                        } else {
                            $.alert(res.returnDesc);
                        }
                    }
                } catch(e) {
                    if(_isDebug) {
                    } else {
                        $.alert('程序运行出错，请联系管理员');
                    }
                }
            },
            error:function(xhr){
                $.hideLoading();//发生错误,也要取消loading..
            },
        });
    }

    $.apiGet = function(url, data, success, error) {
        if($.isFunction(data)) {
            success = data;
            data = {};
        }
        var paramer = {
            url: url,
            data: data,
            success: success
        }
        ajax(paramer, 'GET', error);
    }

    $.apiPost = function(url, data, success, error) {
        if($.isFunction(data)) {
            success = data;
            data = {};
        }
        var paramer = {
            url: url,
            data: data,
            success: success
        }
        ajax(paramer, 'POST', error);
    }

    $.apiRequest = $.apiPost;
})(Zepto);
