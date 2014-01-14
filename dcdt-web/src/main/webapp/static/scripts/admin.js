(function ($) {
    $.extend($.dcdt, {
        "admin": $.extend(function () {
            return this;
        }, {
            "setInstanceConfig": function () {
                return $.dcdt.beans.setBean({
                    "data": $.encodeJson({
                        "@type": "request",
                        "items": [
                            instanceConfig
                        ]
                    }),
                    "queryBeanSuccess": function (data, status, jqXhr) {
                        $.dcdt.admin.getInstanceConfig();
                    },
                    "queryBeanErrors": function (data, status, jqXhr) {
                        $.dcdt.beans.addQueryErrors(instanceConfigForm, data);
                    },
                    "preQueryBean": function (jqXhr, settings) {
                        $.dcdt.beans.clearBeanErrors(instanceConfigForm);
                    },
                    "url": URL_ADMIN_INSTANCE_CONFIG_SET
                });
            },
            "removeInstanceConfig": function () {
                return $.dcdt.beans.removeBean({
                    "queryBeanSuccess": function (data, status, jqXhr) {
                        $.dcdt.admin.getInstanceConfig();
                    },
                    "queryBeanErrors": function (data, status, jqXhr) {
                        $.dcdt.beans.addQueryErrors(instanceConfigForm, data);
                    },
                    "preQueryBean": function (jqXhr, settings) {
                        $.dcdt.beans.clearBeanErrors(instanceConfigForm);
                    },
                    "url": URL_ADMIN_INSTANCE_CONFIG_RM
                });
            },
            "getInstanceConfig": function () {
                return $.dcdt.beans.getBean({
                    "queryBeanSuccess": function (data, status, jqXhr) {
                        instanceConfig = data["items"][0];
                    },
                    "queryBeanErrors": function (data, status, jqXhr) {
                        $.dcdt.beans.addQueryErrors(instanceConfigForm, data);
                    },
                    "postQueryBean": function (jqXhr, status) {
                        if (instanceConfig) {
                            instanceConfigDomainInput.val(instanceConfig["domain"]);
                            
                            instanceConfigRmButton.removeAttr("disabled");
                        } else {
                            instanceConfigDomainInput.val("");
                            
                            instanceConfigRmButton.attr("disabled", "disabled");
                        }
                    },
                    "preQueryBean": function (jqXhr, settings) {
                        instanceConfig = null;
                        
                        $.dcdt.beans.clearBeanErrors(instanceConfigForm);
                    },
                    "url": URL_ADMIN_INSTANCE_CONFIG_GET
                });
            }
        })
    });
    
    var instanceConfigForm, instanceConfigDomainInput, instanceConfigRmButton, instanceConfigSetButton, instanceConfig;
    
    $(document).ready(function () {
        instanceConfigForm = $("form[name=\"admin-instance-config\"]");
        instanceConfigDomainInput = instanceConfigForm.find("input#admin-instance-config-domain");
        instanceConfigRmButton = instanceConfigForm.find("button#admin-instance-config-rm");
        instanceConfigSetButton = instanceConfigForm.find("button#admin-instance-config-set");
        
        instanceConfigRmButton.click(function (event) {
            if (!instanceConfigRmButton.attr("disabled")) {
                $.dcdt.admin.removeInstanceConfig();
            }
        });
        
        instanceConfigSetButton.click(function (event) {
            if (!instanceConfigSetButton.attr("disabled")) {
                instanceConfig = {
                    "@type": "instanceConfig",
                    "domain": instanceConfigDomainInput.val()
                };
                
                $.dcdt.admin.setInstanceConfig();
            }
        });
        
        $.dcdt.admin.getInstanceConfig();
    });
})(jQuery);
