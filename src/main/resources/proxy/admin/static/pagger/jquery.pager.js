/**
 * jQuery 分页插件
 * date: 2017-09-23
 * author: 黄海 <master@nommo.cn>
 * version: 1.0.0
 * dependency base.apiGet, jquery.purl
 
 response dataformat:
 {
    code: 0,
    msg: '请求成功。',
    data: {
        total: 100,     // 数据总数
        list: [object,object,object,object]   // 数据列表
    }
 }
 */
;(function($) {
    
    var showControl = function(element, options, data) {
        var start = options.page - 2 > 0 ? (options.page - 2) : 1;
        var end = start + 5 > data.totalPage ? data.totalPage  + 1 : start + 5;
        if(end - start < 5) start = data.totalPage - 4;
        if(start < 1) start = 1;
        
        element.addClass('jquery-pager');
        strHtml = ''+
            '<a class="page-first" href="javascript:void(0)">首页</a>'+
            '<a class="page-prev" href="javascript:void(0)">上一页</a>';
        
        for (var i = start; i < end; ++i) {
            var current = '';
            if(i == options.page) {
                current = ' current';
            }
            strHtml += '<a class="page ' + current + '" data-page="' + i + '" href="javascript:void(0)">' + i + '</a>';
        }
       strHtml += '<a class="page-next" href="javascript:void(0)">下一页</a>'+
            '<a class="page-last" href="javascript:void(0)">尾页</a>';
        element.html(strHtml);
        
        var first = element.children('.page-first');
        var prev = element.children('.page-prev');
        var page = element.children('.page');
        var next = element.children('.page-next');
        var last = element.children('.page-last');
        
        if(options.page <= 1) {
            first.addClass('disabled');
            prev.addClass('disabled');
        }
        
        if(options.page >= data.totalPage) {
            next.addClass('disabled');
            last.addClass('disabled');
        }
        element.children('.page[data-page="' + options.page + '"]').addClass('current');
        
        if(options.page > 1) {
            first.off('click').on('click', function() {
                options.page = 1;
                location.hash = '#' + options.varPage + '=' + options.page;
                Pager(element, options);
            });
            prev.off('click').on('click', function() {
                options.page = parseInt(options.page) - 1;
                location.hash = '#' + options.varPage + '=' + options.page;
                Pager(element, options);
            });
        }
        page.off('click').on('click', function() {
            var p = $(this).data('page');
            if(p == options.page) return;
            options.page = p;
            location.hash = '#' + options.varPage + '=' + options.page;
            Pager(element, options);
        });
        if(options.page < data.totalPage) {
            next.off('click').on('click', function() {
                options.page = parseInt(options.page) + 1;
                location.hash = '#' + options.varPage + '=' + options.page;
                Pager(element, options);
            });
            last.off('click').on('click', function() {
                options.page = data.totalPage;
                location.hash = '#' + options.varPage + '=' + options.page;
                Pager(element, options);
            });
        }
    }
    
    var Pager = function(element, options) {
        var data = options.data;
        data[options.varPage] = options.page;
        data[options.varSize] = options.size;
        $.apiGet(options.url, data, function(res) {
            res.data.totalPage = Math.ceil(res.data.total / options.size);
            if(res.data.totalPage <= 0) res.data.totalPage = 1;
            options.success({
                list: res.data.list,
                page: options.page,
                totalPage: res.data.totalPage,
                totalData: res.data.total
            });
            showControl(element, options, res.data);
        });
    }
    
    $.fn.pager = function(param) {
        var options = $.extend({
            url: '',                    // url  (必填)
            data: {},                   // 附件参数
            varPage: 'page',            // 当前页变量名
            varSize: 'size',            // 每页显示数量变量名
            page: 1,                    // 默认页编号
            size: 20,                   // 每页显示数量
            success: function(data){}   // 回调数据 data = array:数据列表
        }, param);
        
        // 从hash上取值
        var page = $.url(location.href).fparam(options.varPage);
        if(typeof page != 'undefined' && !isNaN(page)) {
            options.page = page;
        }
        
        var self = $(this);
        if(self.length <= 0 || self.length > 1) {
            console.log('pagger plugin loading error');
            return;
        }
        
        Pager(self, options);
    }
})(jQuery);