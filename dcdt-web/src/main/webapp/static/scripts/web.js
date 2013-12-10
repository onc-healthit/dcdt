(function ($) {
    $.extend($, {
        "encodeJson": function (value, replacer, space) {
            return JSON.stringify(value, replacer, (space || "    "));
        }
    });
    
    $.extend($.fn, {
        "disableClass": function (classesArg) {
            return classesArg ? this.each(function () {
                $(this).removeClass(classesArg);
            }) : this;
        },
        "enableClass": function (classesArg) {
            if (classesArg) {
                this.filter(function () {
                    return !$(this).hasClass(classesArg);
                }).each(function () {
                    $(this).addClass(classesArg);
                });
            }
            
            return this;
        },
        "getClass": function () {
            return this.map(function () {
                var classes = $(this).attr("class");
                
                return classes ? $.trim(classes).split(/\s+/g) : [];
            });
        },
        "tag": function () {
            return this.map(function () {
                return this.tagName;
            });
        }
    });
    
    $.extend($, {
        "dcdt": function () {
            return this;
        }
    });
    
    $.extend($.fn, {
        "dcdt": function (opts) {
            return this.each(function () {
                var elem = $(this);
                
                elem.data("dcdt", $.extend((elem.data("dcdt") || {}), {
                    "opts": (opts || {})
                }));
            });
        }
    });
})(jQuery);
