(function ($) {
    $.extend($, {
        "encodeJson": function (value, replacer, space) {
            return JSON.stringify(value, replacer, (space ? space : "    "));
        }
    });
    
    $.extend($.fn, {
        "disableClass": function (classesArg) {
            return this.each(function () {
                var elem = $(this);
                
                if (classesArg) {
                    if (elem.hasClass(classesArg)) {
                        elem.removeClass(classesArg);
                    }
                } else {
                    elem.removeClass();
                }
            });
        },
        "enableClass": function (classesArg) {
            return this.each(function () {
                var elem = $(this);
                
                if (classesArg) {
                    if (!elem.hasClass(classesArg)) {
                        elem.addClass(classesArg);
                    }
                }
            });
        },
        "getClass": function () {
            return this.each(function () {
                var classes = $(this).attr("class");
                
                return (classes ? classes.split(/\s+/g) : [])
            });
        },
        "tag": function () {
            return this.each(function () {
                return this.tagName;
            });
        }
    });
    
    var ToolJQueryPlugin = function (elemIn, optsIn) {
        var plugin = this;
        var elem = elemIn;
        var opts = $.extend({
        }, (optsIn || {}));
        
        if (elem.is("form")) {
            $.extend(plugin, {
                "addErrorField": function (fieldName, errorMsg) {
                    var errorsField = this.errorsField(fieldName);
                    
                    this.addError(errorsField, errorMsg);
                    
                    formGroupsData(fieldName).enableClass("has-errors");
                },
                "addErrorGlobal": function (errorMsg) {
                    this.addError(this.errorsGlobal(), errorMsg);
                },
                "addError": function (errors, errorMsg) {
                    errors.enableClass("input-group-addon-active");
                    
                    $("ul", errors).append($("<li/>").text(errorMsg));
                },
                "clearErrors": function () {
                    this.statusField(null, null);
                    
                    var errors = this.errors();
                    errors.disableClass("input-group-addon-active");
                    
                    $("ul", errors).empty();
                    
                    return elem;
                },
                "statusField": function (fieldName, status) {
                    var formGroupsDataField = formGroupsData(fieldName);
                    
                    return ((status !== undefined) ? formGroupsDataField.enableClass(status) : formGroupsDataField.each(function () {
                        return $(this).getClass();
                    }));
                },
                "errorsField": function (fieldName) {
                    return this.errors(":not(:first-child)");
                },
                "errorsGlobal": function () {
                    return this.errors(":first-child");
                },
                "errors": function (formGroupAddonsSelector) {
                    return $("div.has-errors div.input-group-addon", formGroupsAddons(formGroupAddonsSelector));
                }
            });
            
            var formGroupsData = function (fieldName) {
                var formGroupsData = $("div:first-child", formGroups(":not(.form-group-addons)"));
                
                return (fieldName ? formGroupsData.filter(function () {
                    return ($("span.form-cell.form-cell-control input[name=\"" + fieldName +"\"]", $(this)).length > 0);
                }) : formGroupsData);
            };
            
            var formGroupsAddons = function (formGroupAddonsSelector) {
                return formGroups(".form-group-addons" + (formGroupAddonsSelector || ""));
            };
            
            var formGroups = function (formGroupSelector) {
                return $("div.form-group" + (formGroupSelector || ""), inputGroup());
            };
            
            var inputGroup = function () {
                return $("div.input-group-sm", elem);
            };
        }
    };
    
    $.extend($.fn, {
        "dcdt": function (opts) {
            return this.each(function () {
                var elem = $(this);
            
                if (!elem.data("dcdt")) {
                    elem.data("dcdt", new ToolJQueryPlugin(elem, opts));
                }
            });
        }
    });
})(jQuery);
