(function ($) {
    'use strict';

    $.pagination = function (el, options) {
        if (!(this instanceof $.pagination)) {
            return new $.pagination(el, options);
        }

        var self = this;

        self.$container = $(el);

        self.$container.data('pagination', self);


        self.init = function () {

            self.options = $.extend({}, $.pagination.defaultOptions, options);

            self.extendJquery();

            if (self.options.currentPage
                && self.options.totalPages) {
                self.verify();
                self.render();
            }

            self.fireEvent(this.options.currentPage, 'init');


        };

        self.verify = function () {
            var opts = self.options;

            if (typeof opts.totalPages === 'undefined' || opts.totalPages === '' || opts.totalPages == 0) {
                opts.totalPages = 1;
            }
            if (typeof opts.currentPage === 'undefined' || opts.currentPage === '') {
                opts.currentPage = 1;
            }

            if (typeof opts.totalPages === 'string') {
                opts.totalPages = parseInt(opts.totalPages, 10);
            }

            if (typeof opts.currentPage === 'string') {
                opts.currentPage = parseInt(opts.currentPage, 10);
            }

            if (opts.totalPages < opts.currentPage) {
                opts.currentPage = opts.totalPages;
            }
        };

        self.extendJquery = function () {
            $.fn.paginatorHTML = function (s) {
                return s ? this.before(s).remove() : $('<p>').append(this.eq(0).clone()).html();
            };
            $.fn.writeContentHTML = function (s, opts) {
                return s ? $('#' + opts.content).html($('<div>').attr('pagination-role', 'div').html(s)) : '';
            };
            $.fn.loadContentHTML = function (opts) {
                var $content = $('#' + opts.content);
                $content.html($("<span>").addClass(opts.shadeClass).html($("<img>").attr('src',basePath+'/images/loading.jpg')));
                $content.load(opts.url + (opts.url.indexOf('?') < 0 ? '?': '&') + 'currentPage=' + opts.currentPage + (opts.limit ? '&limit=' + opts.limit : ''),
                    opts.data = opts.form ? $('#' + opts.form).formSerialize() : '', function (data) {
                        $.fn.writeContentHTML(data, opts);
                    });
            };
        };


        self.render = function () {
            self.renderHtml();
            self.setStatus();
            self.bindEvents();
        };

        self.renderHtml = function () {
            var html = [];

            var pages = self.getPages();
            for (var i = 0, j = pages.length; i < j; i++) {
                html.push(self.buildItem('page', pages[i]));
            }


            self.isEnable('prev') && html.unshift(self.buildItem('prev', self.options.currentPage - 1));
            self.isEnable('first') && html.unshift(self.buildItem('first', 1));
            self.isEnable('statistics') && html.unshift(self.buildItem('statistics', self.options.totalPages));
            self.isEnable('next') && html.push(self.buildItem('next', self.options.currentPage + 1));
            self.isEnable('command') && html.push(self.buildItem('command', self.options.currentPage));
            self.isEnable('last') && html.push(self.buildItem('last', self.options.totalPages));

            if (self.options.wrapper) {
                self.$container.html($(self.options.wrapper).addClass(self.options.cls).html(html.join('')));
            } else {
                self.$container.html(html.join(''));
            }
        };


        self.buildItem = function (type, pageData) {
            var html = self.options[type]
                .replace(/{{page}}/g, pageData)
                .replace(/{{rows}}/g, rows)
                .replace(/{{total}}/g, pageData);

            return $(html).attr({
                'pagination-role': type,
                'pagination-data': pageData
            }).paginatorHTML();
        };


        self.setStatus = function () {
            var options = self.options;
            if (!self.isEnable('first') || options.currentPage === 1) {
                $('[pagination-role=first]', self.$container).find('a').addClass(options.disableClass);
            }
            if (!self.isEnable('prev') || options.currentPage === 1) {
                $('[pagination-role=prev]', self.$container).find('a').addClass(options.disableClass);
            }
            if (!self.isEnable('next') || options.currentPage >= options.totalPages) {
                $('[pagination-role=next]', self.$container).find('a').addClass(options.disableClass);
            }
            if (!self.isEnable('last') || options.currentPage >= options.totalPages) {
                $('[pagination-role=last]', self.$container).find('a').addClass(options.disableClass);
            }

            $('[pagination-role=page]', self.$container).find('a').removeClass(options.activeClass);
            $('[pagination-role=page][pagination-data=' + options.currentPage + ']', self.$container).find('a').addClass(options.activeClass);
        };

        self.isEnable = function (type) {
            return self.options[type] && typeof self.options[type] === 'string';
        };

        self.getPages = function () {
            var pages = [],
                visiblePages = self.options.visiblePages, //显示的页码数
                currentPage = self.options.currentPage,   //当前页
                totalPages = self.options.totalPages;     //总页数

            if (visiblePages > totalPages) {
                visiblePages = totalPages;
            }

            var half = Math.floor(visiblePages / 2);
            var start = currentPage - half + 1 - visiblePages % 2;
            var end = currentPage + half;

            if (start < 1) {
                start = 1;
                end = visiblePages;
            }
            if (end > totalPages) {
                end = totalPages;
                start = 1 + totalPages - visiblePages;
            }

            var itPage = start;
            while (itPage <= end) {
                pages.push(itPage);
                itPage++;
            }

            return pages;
        };

        self.callMethod = function (method, options) {

            switch (method) {
                case 'option':
                    self.options = $.extend({}, self.options, options);
                    if (self.options.totalPages <= 0 || self.options.currentPage <=
                        self.options.totalPages) {
                        self.verify();
                        self.render();
                        break;
                    }
                case 'reload':
                    self.options = $.extend({}, self.options, options);
                    if (self.options.content && self.options.url) {
                        self.verify();
                        self.render();
                        $.fn.loadContentHTML(self.options);
                    }
                    break;
                case 'destroy':
                    self.$container.empty();
                    self.$container.removeData('pagination');
                    break;
                default :
                    throw new Error('[pagination] method "' + method + '" does not exist');
            }

            return self.$container;
        };


        self.switchPage = function (pageIndex) {
            self.options.currentPage = pageIndex;
            self.render();
        };

        self.fireEvent = function (pageIndex, type) {
            return (typeof self.options.onPageChange !== 'function') || (self.options.onPageChange(pageIndex, type) !== false);
        };

        self.bindEvents = function () {
            var opts = self.options;

            self.$container.off();
            self.$container.on('click', '[pagination-role]', function () {

                var $el = $(this);
                if ($el.find('a').hasClass(opts.disableClass) || $el.find('a').hasClass(opts.activeClass)) {
                    return;
                }

                var role = $el.attr('pagination-role');

                if (role == 'statistics')
                    return;


                if (role == 'command') {
                    if (!$el.find('input').val() || $el.find('input').val() == '' || isNaN($el.find('input').val())) {
                        return;
                    }
                    $el.attr('pagination-data', $el.find('input').val());
                }

                var pageIndex = +$el.attr('pagination-data');
                if (pageIndex < 1 || pageIndex > self.options.totalPages) {
                    return;
                }

                if (self.fireEvent(pageIndex, 'change')) {
                    self.switchPage(pageIndex);
                }
            });
        };

        self.init();

        return self.$container;
    };


    $.pagination.defaultOptions = {
        wrapper: '<ul>',
        first: '<li><a class="first" href="javascript:;">首页</a></li>',
        prev: '<li><a class="previous" href="javascript:;">上一页</a></li>',
        next: '<li><a class="next" href="javascript:;">下一页</a></li>',
        last: '<li class="last"><a class="" href="javascript:;">尾页</a></li>',
        page: '<li><a href="javascript:;">{{page}}</a></li>',
        command: "<li class='tz'><span>跳转</span><input onkeyup='this.value=this.value.replace(/\\D/g,\"\")'  onafterpaste='this.value=this.value.replace(/\\D/g,\"\")' type='text'/><a href='javascript:;' class='go'>GO</a></li>",
        statistics:'<li class="tz"><span>搜索出</span><span style="color:red;font-weight:bold">{{rows}}</span><span>条结果&nbsp;&nbsp;&nbsp;总共{{total}}页</span></li>',
        totalPages: 0,
        currentPage: 1,
        visiblePages: 7,
        limit: 20,
        disableClass: 'disable',
        activeClass: 'current',
        data: '',
        //cls: 'pagination',
        cls: 'pagination paginationB paginationB02 fr mt10 mb10 cb',
        shadeClass : 'shade',
        onPageChange: function (pageIndex, type) {
            if (this.content && this.url) {
                this.currentPage = pageIndex;
                $.fn.loadContentHTML(this);
            }
        }
    };

    $.fn.pagination = function () {
        var self = this,
            args = Array.prototype.slice.call(arguments);

        if (typeof args[0] === 'string') {

            var $instance = $(self).data('pagination');

            if (!$instance) {
                throw new Error('[pagination] the element is not instantiated');
            } else {
                return $instance.callMethod(args[0], args[1]);
            }
        } else {
            return new $.pagination(this, args[0]);
        }
    };
})($);
$(function () {
	$(".yuan").change(function () {
		$('#pagination').pagination('reload',{currentPage : 1});
	});
	$(".yuan").keyup(function () {
		$('#pagination').pagination('reload',{currentPage : 1});
	});
});
