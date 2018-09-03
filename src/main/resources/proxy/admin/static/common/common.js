var _LOGIN_URL = 'login.html';  // 登录地址
var _isDebug = false;	// 调试模式

$(function() {
    var flag = true;
    if(typeof _IS_AUTH_ != 'undefined' && _IS_AUTH_ === false) {
        flag = false;
        $('#webView').css('visibility', 'visible');
        return;
    }
    if(!$.getToken()) {
        top.location.href = _LOGIN_URL;
    } else {
        $('#webView').css('visibility', 'visible');
    }
});


// token处理
;(function($) {
	
	var tokenKey = '!#@!$^^$%&';
    var loginInfo = '*&&^%$^&*';
	
	$.getToken = function() {
		var token = $.cookie(tokenKey);
		if(!token) {
			return false;
		} else {
			return Base64.decode(token);
		}
	}
    
    $.getLoginInfo = function() {
        return store.get(loginInfo);
    }
    
    $.removeToken = function() {
        $.cookie(tokenKey, null, {path: '/'});
        store.remove(loginInfo);
    }
	
	$.setToken = function(data) {
		$.cookie(tokenKey, Base64.encode(data.token), {path:'/'});
        delete data.token;
        store.set(loginInfo, data);
	}
})(jQuery);

// ajax请求
;(function($) {
    
    var _apiUrl = '/api/';  // 接口地址
    
    // 常规ajax请求包装
    var ajax = function(options, type, error) {
        $.ajax({
            url: _apiUrl + options.url + '.action',
            type: type,
            data: options.data,
            dataType: 'json',
            beforeSend: function() {
                $.sui.toast.loading.show('正在请求服务器...');
            },
            success: function(res) {
                $.sui.toast.loading.hide();
				if(_isDebug) {
					console.log(res);
				}
                try {
                    if(res.code != 0) {
                        if(res.code == -9) {
                            top.location.href = _LOGIN_URL;
                        } else {
                            if($.isFunction(error)) {
                                error(res);
                            } else {
                                $.sui.error(res.msg);
                            }
                        }
                    } else {
                        if (typeof res.data == 'undefined') {
                            res.data = {length: 0};
                        }
                        options.success(res);
                    }
                } catch(e) {
                    $.sui.error(e.message);
					if(_isDebug) console.error(e.stack);
                }
            }
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
    
    // 包装query.form插件
    $.fn.apiForm = function(before, success, error) {
        var url = $(this).attr('action');
        $(this).ajaxForm({
            url: _apiUrl + url + '.action',
            dataType: 'json',
            beforeSubmit: function(formData, jqForm, options) {
                var form = jqForm[0];
                if($.isFunction(before)) {
                    if(false === before(form)) {
                        return false;
                    }
                }
                $.sui.loading.show('请求服务器...');
            },
            success: function(res) {
                $.sui.loading.hide();
				if(_isDebug) console.log(res);
                try {
                    if(0 !== res.code) {
                        if(res.code == -9) {
                            top.location.href = _LOGIN_URL;
                        } else {
                            if($.isFunction(error)) {
                                error(res);
                            } else {
                                $.sui.toast.error(res.msg);
                            }
                        }
                    } else {
                        success(res);
                    }
                } catch(e) {
                    $.sui.error(e.message);
					if(_isDebug) console.error(e.stack);
                }
            }
        });
    }
})(jQuery);

// 工具箱
;(function($) {
    $.beforeZero = function(num) {
        return num > 9 ? num : '0' + num;
    }
    $.empty = function(val) {
        if(typeof val != 'undefined' && val != '') {
            return false;
        } else {
            return true;
        }
    }
    $.dateTimeFormat = function(timestamp) {
		if(timestamp == 0) return "";
        var date = new Date(parseInt(timestamp));
        return $.dateFormat(parseInt(timestamp)) + ' ' +
               $.beforeZero(date.getHours()) + ':' + 
               $.beforeZero(date.getMinutes()) + ':' + 
               $.beforeZero(date.getSeconds());
    }
    $.dateFormat = function(timestamp) {
		if(timestamp == 0) return "";
        var date = new Date(parseInt(timestamp));
        return date.getFullYear() + '-' + 
               $.beforeZero(date.getMonth() + 1) + '-' +
               $.beforeZero(date.getDate());
    }
})(jQuery);

// 批量选择器
;(function($) {
    
    var getIds = function(checkboxIds) {
        var ids = [];
        checkboxIds.each(function(index, element) {
            if($(element).is(':checked')) {
                ids.push($(element).val());
            }
        });
        return ids;
    }
    
    $.batchManagement = function(parameter) {
        var options = $.extend({
            checkboxAll: '#checkboxAll',
            checkboxIds: '.checkboxIds',
        }, parameter);
        var checkboxAll = $(options.checkboxAll);
        var checkboxIds = $(options.checkboxIds);
        
        if(checkboxAll.length <= 0 || checkboxAll.length > 1) {
            console.error('$.batchManagement plugin loading error');
            return;
        }
        
        var management = {
            values: []
        };
        checkboxAll.off('change').on('change', function() {
            var checked = $(this).is(':checked');
            checkboxIds.prop('checked', checked);
            management.values = getIds(checkboxIds);
        });
        checkboxIds.off('change').on('change', function() {
            management.values = getIds(checkboxIds);
        });
        return management;
    }
})(jQuery);
