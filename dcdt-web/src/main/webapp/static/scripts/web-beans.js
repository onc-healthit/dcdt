(function ($) {
    $.extend($.dcdt, {
        "beans": $.extend(function () {
            return this;
        }, {
            "setBean": function (settings) {
                return $.dcdt.beans.queryBean($.extend({
                    "contentType": "application/json",
                    "type": "POST"
                }, settings));
            },
            "removeBean": function (settings) {
                return $.dcdt.beans.queryBean($.extend({
                    "type": "POST"
                }, settings));
            },
            "getBean": function (settings) {
                return $.dcdt.beans.queryBean(settings);
            },
            "queryBean": function (settings) {
                return $.ajax($.extend({
                    "beforeSend": function (jqXhr, settings) {
                        var preQueryBeanCallback = settings["preQueryBean"];
                        
                        if ($.isFunction(preQueryBeanCallback)) {
                            preQueryBeanCallback(jqXhr, settings);
                        }
                    },
                    "complete": function (jqXhr, status) {
                        var postQueryBeanCallback = settings["postQueryBean"];
                        
                        if ($.isFunction(postQueryBeanCallback)) {
                            postQueryBeanCallback(jqXhr, status);
                        }
                    },
                    "dataType": "json",
                    "error": function (jqXhr, status, error) {
                        console.error("Unable to query (url=" + settings["url"] + ", status=" + status + ") bean" + ((error) ? (":\n" + error) : "."));
                        
                        var queryBeanErrorsCallback = settings["queryBeanErrors"];
                        
                        if ($.isFunction(queryBeanErrorsCallback)) {
                            queryBeanErrorsCallback({
                                "errors": {
                                    "global": "Unable to query (url=" + settings["url"] + ", status=" + status + ") bean."
                                }
                            }, status, jqXhr);
                        }
                    },
                    "success": function (data, status, jqXhr) {
                        var dataJson = $.encodeJson(data);
                        
                        if ($.dcdt.beans.isQuerySuccess(data)) {
                            console.info("Successfully queried (url=" + settings["url"] + ", status=" + status + ") bean:\n" + dataJson);
                            
                            var queryBeanSuccessCallback = settings["queryBeanSuccess"];
                            
                            if ($.isFunction(queryBeanSuccessCallback)) {
                                queryBeanSuccessCallback(data, status, jqXhr);
                            }
                        } else {
                            console.error("Unable to query (url=" + settings["url"] + ", status=" + status + ") bean:\n" + dataJson);
                            
                            var queryBeanErrorsCallback = settings["queryBeanErrors"];
                            
                            if ($.isFunction(queryBeanErrorsCallback)) {
                                queryBeanErrorsCallback(data, status, jqXhr);
                            }
                        }
                    }
                }, settings));
            },
            "addQueryErrors": function (form, data) {
                var dataErrors = data["errors"];
                
                if (dataErrors) {
                    var dataErrorsGlobal = dataErrors["global"];
                    
                    if (dataErrorsGlobal) {
                        $.each(dataErrorsGlobal, function (dataErrorGlobalIndex, dataErrorGlobal) {
                            var dataErrorGlobalMsgs = dataErrorGlobal["messages"];
                            
                            if (dataErrorGlobalMsgs) {
                                $.each(dataErrorGlobalMsgs, function (dataErrorGlobalMsgIndex, dataErrorGlobalMsg) {
                                    $.dcdt.beans.addBeanMessageGlobal(form, "error", dataErrorGlobalMsg);
                                });
                            }
                        });
                    }
                    
                    var dataErrorsFieldsMap = dataErrors["fields"];
                    
                    if (dataErrorsFieldsMap) {
                        for ( var dataErrorFieldName in dataErrorsFieldsMap) {
                            if (dataErrorsFieldsMap.hasOwnProperty(dataErrorFieldName)) {
                                var dataErrorsField = dataErrorsFieldsMap[dataErrorFieldName];
                                
                                if (dataErrorsField) {
                                    $.each(dataErrorsField, function (dataErrorFieldIndex, dataErrorField) {
                                        var dataErrorFieldMsgs = dataErrorField["messages"];
                                        
                                        if (dataErrorFieldMsgs) {
                                            $.each(dataErrorFieldMsgs, function (dataErrorFieldMsgIndex, dataErrorFieldMsg) {
                                                $.dcdt.beans.addBeanMessageField(form, dataErrorFieldName, "error", dataErrorFieldMsg);
                                            });
                                        }
                                    });
                                }
                            }
                        }
                    }
                }
            },
            "addBeanMessageField": function (form, fieldName, level, msg) {
                return $(form).dcdt.form.addMessageField(fieldName.replace(/^items\[\d+\]\./, ""), level, msg);
            },
            "addBeanMessageGlobal": function (form, level, msg) {
                return $(form).dcdt.form.addMessageGlobal(level, msg);
            },
            "clearBeanMessages": function (form) {
                return $(form).dcdt.form.clearMessages();
            },
            "isQuerySuccess": function (data) {
                return ($.dcdt.beans.getQueryStatus(data) == "success");
            },
            "getQueryStatus": function (data) {
                return ((data && data.hasOwnProperty("status")) ? data["status"].toLowerCase() : null);
            }
        })
    });
})(jQuery);
