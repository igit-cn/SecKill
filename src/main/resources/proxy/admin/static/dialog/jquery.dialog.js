/**
 * 基于JQuery UI 弹出框组件
 * date: 2016-04-27
 * author: summer <summer@68coder.com>
 * version: 1.2.0
 */
;(function($) {
	
	//全局Key
	var _handle = '@.SUI.DIALOG.HANDLE',
		_data = '@.SUI.DIALOG.DATA',
		_opener = '@.SUI.DIALOG.OPENER';
		
	//生成唯一ID
	var createId = function(name) {
		var d = new Date();
		return name + '_' + (Math.floor(Math.random() * 999) + 100) + d.getDate() + d.getHours() + d.getMinutes() + d.getSeconds();
	}
	
	//Toast
	var toastTimer;
	var toastBridge = function(dtype, message, callback, msec) {
		
		if(typeof(message) == 'object') {
			callback = message.callback;
			msec = message.msec;
		}
		
		clearTimeout(toastTimer);
		$('#dialogToast').remove();
		
		var isAuto = typeof(msec) == 'undefined' ? true : false;
		var isLoading = dtype == 'loading' ? true : false;
		
		if(isLoading && typeof(message) == 'undefined') message = '数据加载中...';
		
		$('body').append('<div id="dialogToast" class="dialog-toast dialog-toast-' + dtype + '">' + message + '</div>');
		$('#dialogToast').css({marginLeft: -($('#dialogToast').outerWidth() / 2) + 'px', 'top': '0px'});

		if((isLoading && !isAuto) || !isLoading) {
			if(isAuto) msec = 3;
			toastTimer = setTimeout(function() {
				$('#dialogToast').fadeOut('fast', function() {
					$(this).remove();
				});
			}, msec * 1000);
		}
		
	}
	
	//创建提示框
	var dialogBridge = function(dtype, message, callback, options) {
		
		if(typeof(options) == 'undefined') options = {};

		var config = $.extend({
			message: message,
			ok: callback,
			title: '温馨提示',
			cancel: null,
			okVal: '确定',
			cancelVal: '取消'
		}, options);
		
		
		if(typeof(message) == 'object') {
			config = $.extend(config, {
				message: '',
				ok: null
			}, message);
		}
		
		var id = createId(dtype);
		
		$('body').append('<div id="' + id + '" style="display:none"><div class="dialog-ui-icon dialog-ui-' + dtype + '">' + config.message + '</div></div>');
		var dialogConfig = {
			
			modal: true,
			resizable: false,
			dialogClass: 'jquery-ui-dialog',
			width: 360,
			minHeight:150,
			closeText: '',
			title: config.title,
			focus: function(event, ui) {
				$(this).parent().find('button').blur();
			},
			close: function() {$(this).remove()}
		};
		
		//显示按钮
		var buttons = [{
			text: config.okVal,
			click: function() {
				$(this).dialog('close').remove();
				if($.isFunction(config.ok)) {
					config.ok();
				}
			}
		}];
		
		if(dtype == 'confirm') {
			buttons.push({
				text: config.cancelVal,
				click: function() {
					$(this).dialog('close').remove();
					if($.isFunction(config.cancel)) {
						config.cancel();
					}
				}
			});
		}		
		
		dialogConfig.buttons = buttons;
		$('#' + id).dialog(dialogConfig);
	}
	
	//对外公开接口
	if(typeof($.sui) == 'undefined') $.sui = {};
	
	var dialogList = {};
	$.each(['alert', 'error', 'success', 'confirm'], function(key, value) {
		dialogList[value] = function(message, callback, options) {
			if(window.top != window.self) {
				top.$.sui[value](message, callback, options);
			} else {
				dialogBridge(value, message, callback, options);
			}
		}
	});
	
	//loading
	dialogList.loading = {
		show: function(message, msec) {
			if(window.top != window.self) {
				top.$.sui.loading.show(message, msec);
				return;
			}
			if(typeof(message) == 'undefined') message = '正在提交数据...';
			var strHtml = '<div class="jquery-dialog-loading"><div class="loading-icon"></div><p class="loading_content">' + message + '</p></div><div class="loading-overlay"></div>';
			
			$('.jquery-dialog-loading').remove();
			$('.loading-overlay').remove();
			$('body').append(strHtml);

			if(typeof(msec) != 'undefined') {
				setTimeout(function() {
					$('.jquery-dialog-loading').fadeOut('fast', function() {$(this).remove()});
					$('.loading-overlay').fadeOut('fast', function() {$(this).remove()});
				}, msec * 1000);
			}
		},
		hide: function() {
			if(window.top != window.self) {
				top.$.sui.loading.hide();
				return;
			}
			$('.jquery-dialog-loading').fadeOut('fast', function() {$(this).remove()});
			$('.loading-overlay').fadeOut('fast', function() {$(this).remove()});
		}
	};
	
	dialogList.dialog = {
		open: function(url, data, callback, options) {
			if(window.top != window.self) {
				return top.$.sui.dialog.open(url, data, callback, options);
			}
			if(typeof(url) == 'object') {
				options = url;
				url = options.url;
				data = options.data;
				callback = options.callback;
			}
			
			var options = $.extend({
				scroll: 'no',
				modal: true,
				resizable: false
			}, options);
			
			var id = createId('dialog');
			$('body').append('<div id="' + id + '" style="display:none"><iframe name="' + id + '_iframe" id="' + id + '_iframe" scrolling="' + options.scroll + '" frameborder="0" style="border:0px none;"></iframe></div><iframe id="' + id + '_iframe_detector" style="display:none"></iframe>');
            
            $.sui.loading.show('页面加载中');
			$('#' + id + '_iframe_detector').load(function() {
                $.sui.loading.hide();
                $(this).show();
				var isAutoSize = /^(http:|https:|ftp:)\/\/.*/.test(url);
				if(isAutoSize) {
					var title = '\u6d88\u606f\u7a97\u53e3',
						width = 800,
						height = 400,
						minWidth = 200,
						minHeight = 200
				} else {
					var title = $(this).contents().find('title').html(),
						width = $(this).contents().find('body').outerWidth(true),
						height = $(this).contents().find('body').outerHeight(true);
					var minWidth = width, minHeight = height + 40;
				}
				
				title = typeof(options.title) != 'undefined' ? options.title : title;
				
				if(typeof(options.width) != 'undefined') {
					minWidth = width = options.width;
				}
				
				if(typeof(options.height) != 'undefined') {
					height = options.height;
					minHeight = height + 40;
				}
				
				var config = {
					modal: options.modal,
					resizable: options.resizable,
					dialogClass: 'jquery-ui-dialog-iframe',
					width: width,
					height:height + 50,
					minWidth: minWidth,
					minHeight: minHeight,
					closeText: '',
					title: title,
					close: function() {
						$('#' + id + '_iframe').remove();
						$(this).remove();
					},
					beforeClose: function(event) {
                        var $child = $('#' + id + '_iframe')[0].contentWindow;
						if($.isFunction(callback) && typeof($child[_data]) != 'undefined') {
							callback($child[_data]);
						}
					},
					resize: function(event, ui) {
						$('#' + id + '_iframe').css({width: ui.size.width + 'px',height: (ui.size.height - 40) + 'px'});
					},
					focus: function(event, ui) {
						$(this).parent().find('button').blur();
					},
				}
				
				$('#' + id).dialog(config);
				$('#' + id + '_iframe').css({width: width + 'px', height: height + 'px'});
				$('#' + id + '_iframe').css({width: width + 'px', height: height + 'px'});
                $('#' + id + '_iframe').attr('src', url);
                
                $('#' + id + '_iframe').load(function() {
                    $(this)[0].contentWindow[_handle] = id;
					if(data) {
						$(this)[0].contentWindow[_opener] = data;
					}
                    if( $.isFunction($(this)[0].contentWindow.onloadSuccess) ) {
                        $(this)[0].contentWindow.onloadSuccess();
                    }
                });  
                $('#' + id + '_iframe_detector').remove();
			});
			
			$('#' + id + '_iframe_detector').attr('src', url);
			return id;
		},
		close: function(handle) {
			var isSelfWindow = typeof(self.window[_handle]) != 'undefined' && typeof(handle) == 'undefined';
			if(isSelfWindow) {
				top.$.sui.dialog.close(self.window[_handle]);
				return;
			}
			if(window.top != window.self) {
				top.$.sui.dialog.close(handle);
				return;
			}
			if(!isSelfWindow) {
				$('#' + handle).dialog('close');
			}
		},
		data: function(dataValue) {
			if(typeof(dataValue) == 'undefined') {
				return self.window[_opener] ? self.window[_opener] : '';
			} else {
				if(window.top != window.self && typeof(self.window[_handle]) != 'undefined') {
					self.window[_data] = dataValue;
				}
			}
		}
	};
	
	//站内消息
	dialogList.message = function(strHtml, title, msec) {
		if(window.top != window.self) {
			top.$.sui.message(strHtml, title, msec);
			return;
		}
		$('#dialog_message').remove();
		$('body').append('<div id="dialog_message" style="display:none">' + strHtml + '</div>');
		$('#dialog_message').dialog({
			title:title,
			modal: false,
			resizable: false,
			dialogClass: 'jquery-ui-dialog-message',
			width: 270,
			minHeight: 180,
			position:{at: "right bottom", of: window},
			closeText:'',
			close: function() {$(this).remove()}
		});
		
		if(typeof(msec) == 'undefined') return;
		
		setTimeout(function() {
			$('#dialog_message').dialog('close').remove();
		}, msec * 1000);
	}
	
	//toast
	dialogList.toast = {};
	$.each(['warning', 'error', 'success'], function(key, value) {
		dialogList.toast[value] = function(message, callback, msec) {
			if(window.top != window.self) {
				top.$.sui.toast[value](message, callback, msec);
			} else {
				toastBridge(value, message, callback, msec);
			}
		}
	});
	
	//toast loading
	dialogList.toast.loading = {
		show: function(message, callback, msec) {
			if(window.top != window.self) {
				top.$.sui.toast.loading.show(message, callback, msec);
			} else {
				toastBridge('loading', message, callback, msec);
			}
		},
		hide: function()　{
			if(window.top != window.self) {
				top.$.sui.toast.loading.hide();
			} else {
				$('.dialog-toast-loading').fadeOut('fast', function() {
					$(this).remove();
				});
			}
		}
	}
	
	$.sui = $.extend($.sui, dialogList);

})(jQuery);