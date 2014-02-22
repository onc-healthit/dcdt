(function ($) {
    $.extend($, {
        "capitalize": function (str) {
            return str ? (str.charAt(0).toUpperCase() + str.slice(1)) : str;
        },
        "encodeJson": function (value, replacer, space) {
            return JSON.stringify(value, replacer, (space || "    "));
        },
        "isBoolean": function (value) {
            return !$.isNull(value) && !$.isUndefined(value) && (value.__proto__ == Boolean.prototype);
        },
        "isNull": function (value) {
            return value === null;
        },
        "isUndefined": function (value) {
            return value === undefined;
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
